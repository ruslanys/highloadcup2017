package me.ruslanys.highloadcup.service.impl;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.dao.VisitDao;
import me.ruslanys.highloadcup.dto.UserVisit;
import me.ruslanys.highloadcup.exception.NotFoundException;
import me.ruslanys.highloadcup.model.Location;
import me.ruslanys.highloadcup.model.User;
import me.ruslanys.highloadcup.model.Visit;
import me.ruslanys.highloadcup.service.LocationService;
import me.ruslanys.highloadcup.service.UserService;
import me.ruslanys.highloadcup.service.VisitService;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public class DefaultVisitService extends EntityService<Visit, VisitDao> implements VisitService {

    private final LocationService locationService;
    private final UserService userService;

    public DefaultVisitService() {
        super(DI.getBean(VisitDao.class));
        this.userService = DI.getBean(UserService.class);
        this.locationService = DI.getBean(LocationService.class);
    }

    @Override
    public List<UserVisit> findByUser(int userId, Long fromDate, Long toDate, String country, Integer toDistance) {
        User user = userService.findById(userId);
        return dao.findByUser(user, fromDate, toDate, country, toDistance);
    }

    @Override
    public BigDecimal getAvg(int locationId, Long fromDate, Long toDate, Integer fromAge, Integer toAge, String gender) {
        Location location = locationService.findById(locationId);
        return BigDecimal.valueOf(dao.getAvg(location, fromDate, toDate, fromAge, toAge, gender))
                .setScale(5, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public Visit update(Visit model) {
        Visit visit = dao.findById(model.getId());
        if (visit == null) {
            throw new NotFoundException();
        }

        Visit updatedVisit = new Visit(
                visit.getId(),
                model.getLocationId() != 0 ? model.getLocationId() : visit.getLocationId(),
                model.getUserId() != 0 ? model.getUserId() : visit.getUserId(),
                model.getVisitedAt() != Long.MAX_VALUE ? model.getVisitedAt() : visit.getVisitedAt(),
                model.getMark() != Byte.MAX_VALUE ? model.getMark() : visit.getMark()
        );

        return dao.update(updatedVisit);
    }
}
