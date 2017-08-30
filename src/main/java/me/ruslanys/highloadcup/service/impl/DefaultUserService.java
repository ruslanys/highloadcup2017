package me.ruslanys.highloadcup.service.impl;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.dao.UserDao;
import me.ruslanys.highloadcup.exception.NotFoundException;
import me.ruslanys.highloadcup.model.User;
import me.ruslanys.highloadcup.service.UserService;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public class DefaultUserService extends EntityService<User, UserDao> implements UserService {

    public DefaultUserService() {
        super(DI.getBean(UserDao.class));
    }

    @Override
    public User update(User model) {
        User user = dao.findById(model.getId());
        if (user == null) {
            throw new NotFoundException();
        }

        User updatedUser = new User(
                user.getId(),
                model.getEmail() != null ? model.getEmail() : user.getEmail(),
                model.getFirstName() != null ? model.getFirstName() : user.getFirstName(),
                model.getLastName() != null ? model.getLastName() : user.getLastName(),
                model.getGender() != null ? model.getGender() : user.getGender(),
                model.getBirthDate() != Long.MAX_VALUE ? model.getBirthDate() : user.getBirthDate()
        );

        return dao.update(updatedUser);
    }
}
