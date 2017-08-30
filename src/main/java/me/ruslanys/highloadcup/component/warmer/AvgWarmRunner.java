package me.ruslanys.highloadcup.component.warmer;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.service.LocationService;

import java.util.concurrent.CountDownLatch;

public class AvgWarmRunner extends WarmRunner {

    private final LocationService service;
    private final String host = "http://127.0.0.1:" + DI.getConfig().getPort() + "/";

    public AvgWarmRunner(CountDownLatch latch, int timeout, LocationService service) {
        super(latch, timeout);
        this.service = service;
    }

    @Override
    public void action() throws Exception {
        int randomId = 1 + RANDOM.nextInt(service.count());
        requestGet(host + "locations/" + randomId + "/avg");
    }
}
