package me.ruslanys.highloadcup.dao.impl;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.component.Config;
import me.ruslanys.highloadcup.dao.UserDao;
import me.ruslanys.highloadcup.model.User;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public class UserInMemoryDao extends InMemoryDao<User> implements UserDao {
    public UserInMemoryDao() {
        super(DI.getConfig().getMode() == Config.Mode.TEST ? SIZE_TEST : SIZE_PRODUCTION);
    }
}
