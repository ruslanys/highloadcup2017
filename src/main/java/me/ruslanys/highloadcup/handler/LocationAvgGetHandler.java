package me.ruslanys.highloadcup.handler;

import io.netty.handler.codec.http.FullHttpRequest;
import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.dto.response.AvgResponseDto;
import me.ruslanys.highloadcup.exception.BadRequestException;
import me.ruslanys.highloadcup.service.VisitService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public class LocationAvgGetHandler extends BaseHandler {

    private static final VisitService SERVICE = DI.getBean(VisitService.class);

    @Override
    public Object handleRequest(FullHttpRequest request) throws Exception {
        Map<String, List<String>> params = parameters(request);

        int id = fetchId(request.uri());
        Long fromDate = paramAsLong(params, "fromDate", false);
        Long toDate = paramAsLong(params, "toDate", false);
        Integer fromAge = paramAsInteger(params, "fromAge", false);
        Integer toAge = paramAsInteger(params, "toAge", false);
        String gender = param(params, "gender", false);

        if (gender != null && (!"m".equals(gender) && !"f".equals(gender))) {
            throw new BadRequestException();
        }


        BigDecimal avg = SERVICE.getAvg(id, fromDate, toDate, fromAge, toAge, gender);
        return new AvgResponseDto(avg);
    }

}
