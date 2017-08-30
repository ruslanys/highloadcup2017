package me.ruslanys.highloadcup.handler;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.model.Location;
import me.ruslanys.highloadcup.service.LocationService;

public class LocationGetHandler extends GetHandler<Location> {
    public LocationGetHandler() {
        super(DI.getBean(LocationService.class));
    }
}
