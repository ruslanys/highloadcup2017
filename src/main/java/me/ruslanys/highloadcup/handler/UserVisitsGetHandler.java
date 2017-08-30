package me.ruslanys.highloadcup.handler;

import io.netty.handler.codec.http.FullHttpRequest;
import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.dto.UserVisit;
import me.ruslanys.highloadcup.dto.response.VisitsResponseDto;
import me.ruslanys.highloadcup.service.VisitService;

import java.util.List;
import java.util.Map;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public class UserVisitsGetHandler extends BaseHandler {

    private static final VisitService SERVICE = DI.getBean(VisitService.class);

    @Override
    public Object handleRequest(FullHttpRequest request) throws Exception {
        Map<String, List<String>> params = parameters(request);
        int id = fetchId(request.uri());
        Long fromDate = paramAsLong(params, "fromDate", false);
        Long toDate = paramAsLong(params, "toDate", false);
        String country = param(params, "country", false);
        Integer toDistance = paramAsInteger(params, "toDistance", false);


        List<UserVisit> visits = SERVICE.findByUser(id, fromDate, toDate, country, toDistance);
        return new VisitsResponseDto(visits);
    }
}
