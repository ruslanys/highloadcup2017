package me.ruslanys.highloadcup.component.warmer;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.model.BaseModel;
import me.ruslanys.highloadcup.service.BaseService;

import java.util.concurrent.CountDownLatch;

public class UpdateWarmRunner extends WarmRunner {

    private final String key;
    private final BaseService service;
    private final String host = "http://127.0.0.1:" + DI.getConfig().getPort() + "/";

    public UpdateWarmRunner(CountDownLatch latch, int timeout, String key, BaseService service) {
        super(latch, timeout);
        this.key = key;
        this.service = service;
    }

    @Override
    public void action() throws Exception {
        int randomId = 1 + RANDOM.nextInt(service.count());
        BaseModel model = service.findById(randomId);
        requestPost(host + key + "/" + randomId, model.toString());
    }
}
