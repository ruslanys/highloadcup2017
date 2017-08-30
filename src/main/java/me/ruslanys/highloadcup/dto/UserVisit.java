package me.ruslanys.highloadcup.dto;

import lombok.Data;

@Data
public class UserVisit {

    private final long visitedAt;
    private final byte mark;
    private final String place;

    @Override
    public String toString() {
        return "{\"visited_at\":" + visitedAt + "," +
                "\"mark\":" + mark + "," +
                "\"place\":\"" + place + "\"}";
    }

}
