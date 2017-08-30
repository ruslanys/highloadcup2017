package me.ruslanys.highloadcup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Visit extends BaseModel {

    @JsonProperty("location")
    private int locationId;

    @JsonProperty("user")
    private int userId;

    private long visitedAt = Long.MAX_VALUE;

    private byte mark = Byte.MAX_VALUE;

    public Visit(int id, int locationId, int userId, long visitedAt, byte mark) {
        super(id);
        this.locationId = locationId;
        this.userId = userId;
        this.visitedAt = visitedAt;
        this.mark = mark;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setVisitedAt(long visitedAt) {
        this.visitedAt = visitedAt;
    }

    public void setMark(byte mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "{" +
                "\"visited_at\":" + visitedAt + "," +
                "\"mark\":" + mark + "," +
                "\"location\":" + locationId + "," +
                "\"user\":" + userId + "," +
                "\"id\":" + id +
                "}";
    }

}