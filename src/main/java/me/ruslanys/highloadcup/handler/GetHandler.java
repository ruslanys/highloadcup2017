package me.ruslanys.highloadcup.handler;

import io.netty.handler.codec.http.FullHttpRequest;
import lombok.NonNull;
import me.ruslanys.highloadcup.exception.BadRequestException;
import me.ruslanys.highloadcup.exception.NotFoundException;
import me.ruslanys.highloadcup.model.BaseModel;
import me.ruslanys.highloadcup.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public abstract class GetHandler<T extends BaseModel> extends BaseHandler {

    private final BaseService<T> baseService;

    public GetHandler(@NonNull BaseService<T> baseService) {
        this.baseService = baseService;
    }

    @Override
    public Object handleRequest(FullHttpRequest request) throws Exception {
        Map<String, List<String>> parameters = parameters(request);
        if (!parameters.isEmpty()) {
            throw new BadRequestException();
        }

        try {
            int id = fetchId(request.uri());
            return baseService.findById(id);
        } catch (BadRequestException e) {
            throw new NotFoundException();
        }
    }

}
