package me.ruslanys.highloadcup.dao;

import me.ruslanys.highloadcup.exception.NotFoundException;
import me.ruslanys.highloadcup.model.BaseModel;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public interface BaseDao<T extends BaseModel> {

    T add(T model);

    T update(T model);

    T findById(int id) throws NotFoundException;

    int count();

}
