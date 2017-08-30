package me.ruslanys.highloadcup.dao.impl;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.component.Config;
import me.ruslanys.highloadcup.dao.LocationDao;
import me.ruslanys.highloadcup.dao.UserDao;
import me.ruslanys.highloadcup.dao.VisitDao;
import me.ruslanys.highloadcup.dto.UserVisit;
import me.ruslanys.highloadcup.model.Location;
import me.ruslanys.highloadcup.model.User;
import me.ruslanys.highloadcup.model.Visit;
import org.joda.time.DateTime;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public class VisitInMemoryDao extends InMemoryDao<Visit> implements VisitDao {

    private static final LocationDao LOCATION_DAO = DI.getBean(LocationDao.class);
    private static final UserDao USER_DAO = DI.getBean(UserDao.class);
    private static final long[] YEARS = new long[100];

    static {
        DateTime timestamp = DI.getConfig().getTimestamp();
        for (int i = 0; i < YEARS.length; i++) {
            YEARS[i] = timestamp.minusYears(i + 1).toDate().getTime() / 1000;
        }
    }

    private static final Comparator<Visit> COMPARATOR = (o1, o2) -> {
        int compareDates = (int) (o1.getVisitedAt() - o2.getVisitedAt());
        if (compareDates != 0) {
            return compareDates;
        } else {
            return o1.getId() - o2.getId();
        }
    };

    private final Index indexByUser;
    private final Index indexByLocation;

    public VisitInMemoryDao() {
        super(DI.getConfig().getMode() == Config.Mode.TEST ? SIZE_TEST : SIZE_PRODUCTION);

        Config.Mode mode = DI.getConfig().getMode();
        this.indexByUser = new Index(mode == Config.Mode.TEST ? UserDao.SIZE_TEST : UserDao.SIZE_PRODUCTION);
        this.indexByLocation = new Index(mode == Config.Mode.TEST ? LocationDao.SIZE_TEST : LocationDao.SIZE_PRODUCTION);
    }

    @Override
    public Visit add(Visit model) {
        Visit visit = super.add(model);
        index(visit);
        return visit;
    }


    @Override
    public Visit update(Visit model) {
        Visit original = findById(model.getId());
        removeIndex(original);

        Visit updated = super.update(model);
        index(updated);
        return updated;
    }

    @Override
    public List<UserVisit> findByUser(User user, Long fromDate, Long toDate, String country, Integer toDistance) {
        List<UserVisit> result = new ArrayList<>();

        for (Visit visit : indexByUser.get(user.getId(), fromDate, toDate)) {
            Location location = LOCATION_DAO.findById(visit.getLocationId());
            if (country != null || toDistance != null) {
                if (country != null && !location.getCountry().equals(country)) {
                    continue;
                }

                if (toDistance != null && location.getDistance() >= toDistance) {
                    continue;
                }
            }

            result.add(new UserVisit(visit.getVisitedAt(), visit.getMark(), location.getPlace()));
        }

        return result;
    }

    @Override
    public double getAvg(Location location, Long fromDate, Long toDate, Integer fromAge, Integer toAge, String gender) {
        long toBirthDate = Long.MIN_VALUE;
        if (fromAge != null && fromAge > 0 && fromAge < 100) {
            toBirthDate = YEARS[fromAge - 1];
        }

        long fromBirthDate = Long.MAX_VALUE;
        if (toAge != null && toAge > 0 && toAge < 100) {
            fromBirthDate = YEARS[toAge - 1];
        }


        List<Byte> result = new ArrayList<>();
        for (Visit visit : indexByLocation.get(location.getId(), fromDate, toDate)) {
            if (fromAge != null || toAge != null || gender != null) {
                User user = USER_DAO.findById(visit.getUserId());

                if (fromBirthDate != Long.MAX_VALUE && user.getBirthDate() < fromBirthDate) {
                    continue;
                }

                if (toBirthDate != Long.MIN_VALUE && user.getBirthDate() > toBirthDate) {
                    continue;
                }

                if (gender != null && !user.getGender().equals(gender)) {
                    continue;
                }
            }


            result.add(visit.getMark());
        }

        return result.stream().mapToInt(v -> v).average().orElse(0.0);
    }

    private void index(Visit visit) {
        indexByLocation.put(visit.getLocationId(), visit);
        indexByUser.put(visit.getUserId(), visit);
    }

    private void removeIndex(Visit visit) {
        indexByLocation.remove(visit.getLocationId(), visit);
        indexByUser.remove(visit.getUserId(), visit);
    }

    private static class Index {
        private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private final Map<Integer, List<Visit>> hashMap = new HashMap<>();
        private final List<Visit>[] storage;


        private Index(int size) {
            this.storage = new List[(int) (size * SIZE_K)];
        }

        public List<Visit> get(int id, Long fromDate, Long toDate) {
            readWriteLock.readLock().lock();

            try {
                List<Visit> list;

                if (storage.length < id) {
                    list = getFromMap(id);
                } else {
                    list = getFromArray(id);
                }

                return find(list, fromDate, toDate);
            } finally {
                readWriteLock.readLock().unlock();
            }
        }

        private List<Visit> find(List<Visit> origin, Long fromDate, Long toDate) {
            if (origin == null) {
                return Collections.emptyList();
            }
            if (fromDate == null && toDate == null) {
                return origin;
            }

            List<Visit> result = new ArrayList<>(origin.size());
            for (Visit visit : origin) {
                if (fromDate != null && visit.getVisitedAt() <= fromDate) {
                    continue;
                }

                if (toDate != null && visit.getVisitedAt() >= toDate) {
                    break;
                }

                result.add(visit);
            }

            return result;
        }

        private List<Visit> getFromMap(int id) {
            return hashMap.get(id);
        }

        private List<Visit> getFromArray(int id) {
            int i = id - 1;
            return storage[i];
        }

        void put(int id, Visit model) {
            readWriteLock.writeLock().lock();

            try {
                if (storage.length < id) {
                    putInMap(id, model);
                } else {
                    putInArray(id, model);
                }
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }

        private void putInMap(int id, Visit model) {
            hashMap.computeIfAbsent(id, v -> new ArrayList<>());
            List<Visit> list = hashMap.get(id);
            list.add(model);
            if (list.size() > 1) {
                list.sort(COMPARATOR);
            }
        }

        private void putInArray(int id, Visit model) {
            int i = id - 1;
            if (storage[i] == null) {
                storage[i] = new ArrayList<>();
            }
            List<Visit> list = storage[i];
            list.add(model);
            if (storage[i].size() > 1) {
                list.sort(COMPARATOR);
            }
        }

        void remove(int id, Visit model) {
            readWriteLock.writeLock().lock();

            try {
                if (storage.length < id) {
                    removeFromMap(id, model);
                } else {
                    removeFromArray(id, model);
                }
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }

        private void removeFromMap(int id, Visit model) {
            if (hashMap.get(id) == null) return;
            hashMap.get(id).remove(model);
        }

        private void removeFromArray(int id, Visit model) {
            int i = id - 1;
            if (storage[i] == null) return;
            storage[i].remove(model);
        }

    }
}
