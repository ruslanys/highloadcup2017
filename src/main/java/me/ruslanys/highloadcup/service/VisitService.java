package me.ruslanys.highloadcup.service;

import lombok.NonNull;
import me.ruslanys.highloadcup.dto.UserVisit;
import me.ruslanys.highloadcup.model.Visit;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public interface VisitService extends BaseService<Visit> {

    List<UserVisit> findByUser(@NonNull int userId, Long fromDate, Long toDate, String country, Integer toDistance);

    BigDecimal getAvg(@NonNull int locationId, Long fromDate, Long toDate, Integer fromAge, Integer toAge, String gender);

}
