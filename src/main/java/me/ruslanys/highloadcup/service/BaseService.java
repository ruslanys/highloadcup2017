package me.ruslanys.highloadcup.service;


import lombok.NonNull;
import me.ruslanys.highloadcup.model.BaseModel;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public interface BaseService<T extends BaseModel> {

    @NonNull
    T findById(int id);

    T add(T model);

    int count();

    T update(T model);

}
