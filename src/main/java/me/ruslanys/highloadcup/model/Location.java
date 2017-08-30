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
public class Location extends BaseModel {

    private String place;
    private String country;
    private String city;
    private int distance;

    public Location(int id, String place, String country, String city, int distance) {
        super(id);
        this.place = place;
        this.country = country;
        this.city = city;
        this.distance = distance;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "{\"id\": " + id + "," +
                "\"place\":\"" + place + "\"," +
                "\"country\":\"" + country + "\"," +
                "\"city\":\"" + city + "\"," +
                "\"distance\":" + distance + "}";
    }

}
