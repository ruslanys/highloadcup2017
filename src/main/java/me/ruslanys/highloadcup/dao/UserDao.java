package me.ruslanys.highloadcup.dao;

import me.ruslanys.highloadcup.model.User;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public interface UserDao extends BaseDao<User> {

    int SIZE_TEST = 10000;
    int SIZE_PRODUCTION = 1000000;

}
