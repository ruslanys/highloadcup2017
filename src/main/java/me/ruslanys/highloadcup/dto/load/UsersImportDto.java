package me.ruslanys.highloadcup.dto.load;

import lombok.Data;
import me.ruslanys.highloadcup.model.User;

import java.util.List;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
@Data
public class UsersImportDto {

    private List<User> users;

}
