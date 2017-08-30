package me.ruslanys.highloadcup.dao;

import me.ruslanys.highloadcup.dto.UserVisit;
import me.ruslanys.highloadcup.model.Location;
import me.ruslanys.highloadcup.model.User;
import me.ruslanys.highloadcup.model.Visit;

import java.util.List;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public interface VisitDao extends BaseDao<Visit> {

    int SIZE_TEST = 100000;
    int SIZE_PRODUCTION = 10000000;

    List<UserVisit> findByUser(User user, Long fromDate, Long toDate, String country, Integer toDistance);

    double getAvg(Location location, Long fromDate, Long toDate, Integer fromAge, Integer toAge, String gender);

}
