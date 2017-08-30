package me.ruslanys.highloadcup.component.warmer;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.service.UserService;

import java.util.concurrent.CountDownLatch;

public class UserVisitsWarmRunner extends WarmRunner {

    private final UserService service;
    private final String host = "http://127.0.0.1:" + DI.getConfig().getPort() + "/";

    public UserVisitsWarmRunner(CountDownLatch latch, int timeout, UserService service) {
        super(latch, timeout);
        this.service = service;
    }

    @Override
    public void action() throws Exception {
        int randomId = 1 + RANDOM.nextInt(service.count());
        requestGet(host + "users/" + randomId + "/visits");
    }
}
