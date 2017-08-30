package me.ruslanys.highloadcup.dao;

import me.ruslanys.highloadcup.model.Location;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public interface LocationDao extends BaseDao<Location> {
    int SIZE_TEST = 7812;
    int SIZE_PRODUCTION = 759627;
}
