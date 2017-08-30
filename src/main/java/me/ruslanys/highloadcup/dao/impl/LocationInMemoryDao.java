package me.ruslanys.highloadcup.dao.impl;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.component.Config;
import me.ruslanys.highloadcup.dao.LocationDao;
import me.ruslanys.highloadcup.model.Location;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public class LocationInMemoryDao extends InMemoryDao<Location> implements LocationDao {
    public LocationInMemoryDao() {
        super(DI.getConfig().getMode() == Config.Mode.TEST ? SIZE_TEST : SIZE_PRODUCTION);
    }
}
