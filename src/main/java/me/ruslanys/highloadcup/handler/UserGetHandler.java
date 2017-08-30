
package me.ruslanys.highloadcup.handler;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.model.User;
import me.ruslanys.highloadcup.service.UserService;

public class UserGetHandler extends GetHandler<User> {

    private static final UserService USER_SERVICE = DI.getBean(UserService.class);

    public UserGetHandler() {
        super(USER_SERVICE);
    }

}
