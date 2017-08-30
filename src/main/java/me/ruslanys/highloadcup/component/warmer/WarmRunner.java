package me.ruslanys.highloadcup.component.warmer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class WarmRunner implements Runnable {

    public static final AtomicInteger COUNTER = new AtomicInteger();
    static final Random RANDOM = new Random();

    private final CountDownLatch latch;
    private final int timeout;

    WarmRunner(CountDownLatch latch, int timeout) {
        this.latch = latch;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        while (((System.currentTimeMillis() - startTime) / 1000) < timeout) {
            try {
                action();
                COUNTER.incrementAndGet();
            } catch (Exception ignored) {}
        }
        latch.countDown();
    }

    public abstract void action() throws Exception;

    protected void requestPost(String url, String json) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream out = connection.getOutputStream()) {
            out.write(json.getBytes());
            out.flush();
        }

        try (InputStream in = connection.getInputStream()) {
            while (in.read() != -1) {
                // --
            }
        }
    }

    protected void requestGet(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try (InputStream in = connection.getInputStream()) {
            while (in.read() != -1) {
                // --
            }
        }
    }
    
}
