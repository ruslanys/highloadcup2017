package me.ruslanys.highloadcup.handler;

import me.ruslanys.highloadcup.model.BaseModel;
import me.ruslanys.highloadcup.service.BaseService;

import java.util.Map;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public abstract class EntityHandler<T extends BaseModel> extends BaseHandler {

    protected final BaseService<T> baseService;


    protected EntityHandler(BaseService<T> baseService) {
        this.baseService = baseService;
    }

    protected abstract T parseJson(Map<String, String> map);

}
