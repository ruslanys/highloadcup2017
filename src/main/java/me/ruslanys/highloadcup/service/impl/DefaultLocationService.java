package me.ruslanys.highloadcup.service.impl;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.dao.LocationDao;
import me.ruslanys.highloadcup.exception.NotFoundException;
import me.ruslanys.highloadcup.model.Location;
import me.ruslanys.highloadcup.service.LocationService;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public class DefaultLocationService extends EntityService<Location, LocationDao> implements LocationService {

    public DefaultLocationService() {
        super(DI.getBean(LocationDao.class));
    }

    @Override
    public Location update(Location model) {
        Location location = dao.findById(model.getId());
        if (location == null) {
            throw new NotFoundException();
        }

        Location updatedLocation = new Location(
                location.getId(),
                model.getPlace() != null ? model.getPlace() : location.getPlace(),
                model.getCountry() != null ? model.getCountry() : location.getCountry(),
                model.getCity() != null ? model.getCity() : location.getCity(),
                model.getDistance() != 0 ? model.getDistance() : location.getDistance()
        );

        return dao.update(updatedLocation);
    }
}
