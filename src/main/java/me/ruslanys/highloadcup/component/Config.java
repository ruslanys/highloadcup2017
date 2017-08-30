package me.ruslanys.highloadcup.component;

import lombok.extern.slf4j.Slf4j;
import me.ruslanys.highloadcup.annotation.Ordered;
import org.joda.time.DateTime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
@Slf4j
@Ordered(Integer.MIN_VALUE)
public class Config {

    private static final String FILE_PATH = "/tmp/data/options.txt";
    private static final String VERSION = "3.2.18.1";

    private volatile DateTime timestamp = new DateTime().withTimeAtStartOfDay();
    private Mode mode = Mode.PRODUCTION;
    private int port;

    public Config() {
        log.info("VERSION: {}", VERSION);
        try {
            List<String> strings = Files.lines(Paths.get(FILE_PATH)).collect(Collectors.toList());
            setTimestamp(Long.parseLong(strings.get(0)));
            if (strings.get(1).equals("0")) {
                mode = Mode.TEST;
            } else {
                mode = Mode.PRODUCTION;
            }

            log.info("MODE: {}", mode);
        } catch (IOException e) {
            log.error("Failed to load config file.", e);
        }
    }

    public Mode getMode() {
        return mode;
    }

    public enum Mode {
        TEST, PRODUCTION
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    private void setTimestamp(long timestamp) {
        this.timestamp = new DateTime(timestamp * 1000);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
