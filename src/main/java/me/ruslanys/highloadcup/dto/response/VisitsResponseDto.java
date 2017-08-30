package me.ruslanys.highloadcup.dto.response;

import lombok.Data;
import lombok.NonNull;
import me.ruslanys.highloadcup.dto.UserVisit;

import java.util.Iterator;
import java.util.List;

@Data
public class VisitsResponseDto {

    @NonNull
    private List<UserVisit> visits;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{\"visits\":[");

        Iterator<UserVisit> it = visits.iterator();
        while (it.hasNext()) {
            UserVisit visit = it.next();
            sb.append(visit.toString());
            if (it.hasNext()) sb.append(",");
        }
        sb.append("]}");

        return sb.toString();
    }

}
