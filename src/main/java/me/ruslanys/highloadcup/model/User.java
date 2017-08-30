package me.ruslanys.highloadcup.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends BaseModel {

    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private long birthDate = Long.MAX_VALUE;

    public User(int id, String email, String firstName, String lastName, String gender, long birthDate) {
        super(id);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        setBirthDate(birthDate);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "{\"id\":" + id + "," +
                "\"first_name\":\"" + firstName + "\"," +
                "\"last_name\":\"" + lastName + "\"," +
                "\"email\":\"" + email + "\"," +
                "\"gender\":\"" + gender + "\"," +
                "\"birth_date\":" + birthDate + "}";
    }

}