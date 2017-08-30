package me.ruslanys.highloadcup.handler;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.exception.BadRequestException;
import me.ruslanys.highloadcup.model.User;
import me.ruslanys.highloadcup.service.UserService;

import java.util.Map;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public class UserUpdateHandler extends UpdateHandler<User> {

    public static final UserService USER_SERVICE = DI.getBean(UserService.class);

    public UserUpdateHandler() {
        super(USER_SERVICE);
    }

    @Override
    protected User parseJson(Map<String, String> object) {
        for (Map.Entry<String, String> entry : object.entrySet()) {
            if (entry.getValue() == null) {
                throw new BadRequestException();
            }
        }

        User user = new User();
        for (Map.Entry<String, String> entry : object.entrySet()) {
            int hash = entry.getKey().hashCode();
            String value = entry.getValue();

            if ("email".hashCode() == hash) {
                user.setEmail(value);
            } else if ("first_name".hashCode() == hash) {
                user.setFirstName(value);
            } else if ("last_name".hashCode() == hash) {
                user.setLastName(value);
            } else if ("gender".hashCode() == hash) {
                user.setGender(value);
            } else if ("birth_date".hashCode() == hash) {
                user.setBirthDate(Long.parseLong(value));
            }
        }
        return user;
    }
}
