package me.ruslanys.highloadcup.service.impl;

import lombok.NonNull;
import me.ruslanys.highloadcup.dao.BaseDao;
import me.ruslanys.highloadcup.exception.BadRequestException;
import me.ruslanys.highloadcup.exception.NotFoundException;
import me.ruslanys.highloadcup.model.BaseModel;
import me.ruslanys.highloadcup.service.BaseService;

public abstract class EntityService<T extends BaseModel, R extends BaseDao<T>> implements BaseService<T> {

    protected final R dao;

    protected EntityService(R dao) {
        this.dao = dao;
    }

    @NonNull
    @Override
    public T findById(int id) {
        T model = dao.findById(id);
        if (model == null) {
            throw new NotFoundException();
        }
        return model;
    }

    @Override
    public T add(T model) {
        if (dao.findById(model.getId()) != null) {
            throw new BadRequestException();
        }

        return dao.add(model);
    }

    @Override
    public int count() {
        return dao.count();
    }

}
