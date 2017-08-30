package me.ruslanys.highloadcup.component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.annotation.Ordered;
import me.ruslanys.highloadcup.component.warmer.*;
import me.ruslanys.highloadcup.service.LocationService;
import me.ruslanys.highloadcup.service.UserService;
import me.ruslanys.highloadcup.service.VisitService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
@Slf4j
@Ordered(Integer.MAX_VALUE)
public class HttpWarmer implements StartupListener {

    private static final CountDownLatch LATCH = new CountDownLatch(8);

    private final UserService userService;
    private final LocationService locationService;
    private final VisitService visitService;

    private final ExecutorService executor;

    private final int timeout = DI.getConfig().getMode() == Config.Mode.TEST ? 60 : 250;

    public HttpWarmer() {
        this.userService = DI.getBean(UserService.class);
        this.locationService = DI.getBean(LocationService.class);
        this.visitService = DI.getBean(VisitService.class);

        this.executor = Executors.newFixedThreadPool(10);
    }

    @SneakyThrows
    @Override
    public void onStartup() {
        log.info("Warming is starting...");
        log.info("Warm time is {} sec", timeout);

        executor.submit(new GetWarmRunner(LATCH, timeout, "users", userService));
        executor.submit(new GetWarmRunner(LATCH, timeout, "locations", locationService));
        executor.submit(new GetWarmRunner(LATCH, timeout, "visits", visitService));

        executor.submit(new UpdateWarmRunner(LATCH, timeout, "users", userService));
        executor.submit(new UpdateWarmRunner(LATCH, timeout, "locations", locationService));
        executor.submit(new UpdateWarmRunner(LATCH, timeout, "visits", visitService));

        executor.submit(new AvgWarmRunner(LATCH, timeout, locationService));
        executor.submit(new UserVisitsWarmRunner(LATCH, timeout, userService));

        executor.submit(new Notifier());
        executor.shutdown();
    }

    private static class Notifier implements Runnable {
        @Override
        public void run() {
            try {
                LATCH.await();
                log.info("{} request were done due the warming process", WarmRunner.COUNTER.get());
                System.gc();
            } catch (InterruptedException ignored) {}
        }
    }

}
