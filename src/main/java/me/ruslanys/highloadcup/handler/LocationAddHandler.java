package me.ruslanys.highloadcup.handler;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.exception.BadRequestException;
import me.ruslanys.highloadcup.model.Location;
import me.ruslanys.highloadcup.service.LocationService;

import java.util.Map;

public class LocationAddHandler extends AddHandler<Location> {
    public LocationAddHandler() {
        super(DI.getBean(LocationService.class));
    }

    @Override
    protected Location parseJson(Map<String, String> object) {
        for (Map.Entry<String, String> entry : object.entrySet()) {
            if (entry.getValue() == null) {
                throw new BadRequestException();
            }
        }

        Location location = new Location();

        for (Map.Entry<String, String> entry : object.entrySet()) {
            int hash = entry.getKey().hashCode();
            String value = entry.getValue();

            if ("id".hashCode() == hash) {
                location.setId(Integer.parseInt(value));
            } else if ("place".hashCode() == hash) {
                location.setPlace(value);
            } else if ("country".hashCode() == hash) {
                location.setCountry(value);
            } else if ("city".hashCode() == hash) {
                location.setCity(value);
            } else if ("distance".hashCode() == hash) {
                location.setDistance(Integer.parseInt(value));
            }
        }

        return location;
    }

}
