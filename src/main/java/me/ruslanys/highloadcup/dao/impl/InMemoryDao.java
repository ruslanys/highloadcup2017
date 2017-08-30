package me.ruslanys.highloadcup.dao.impl;

import me.ruslanys.highloadcup.dao.BaseDao;
import me.ruslanys.highloadcup.exception.NotFoundException;
import me.ruslanys.highloadcup.model.BaseModel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public abstract class InMemoryDao<T extends BaseModel> implements BaseDao<T> {

    static final double SIZE_K = 1.01;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Map<Integer, T> hashMap = new HashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    private volatile T[] storage;

    InMemoryDao(int size) {
        storage = (T[]) new BaseModel[(int) (size * SIZE_K)];
    }

    @Override
    public T add(T model) {
        counter.incrementAndGet();
        put(model);
        return model;
    }

    @Override
    public T update(T model) {
        put(model);
        return model;
    }

    @Override
    public T findById(int id) throws NotFoundException {
        return get(id);
    }

    @Override
    public int count() {
        return counter.get();
    }

    private void put(T model) {
        readWriteLock.writeLock().lock();

        try {
            if (storage.length < model.getId()) {
                putInMap(model);
            } else {
                putInArray(model);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void putInMap(T model) {
        hashMap.put(model.getId(), model);
    }

    private void putInArray(T model) {
        int i = model.getId() - 1;
        storage[i] = model;
    }

    private T get(int id) {
        readWriteLock.readLock().lock();
        try {
            T model;

            if (storage.length < id) {
                model = getFromMap(id);
            } else {
                model = getFromArray(id);
            }

            return model;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    private T getFromArray(int id) {
        int i = id - 1;
        if (i < 0) return null;
        return storage[i];
    }

    private T getFromMap(int id) {
        return hashMap.get(id);
    }

}
