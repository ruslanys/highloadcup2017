package me.ruslanys.highloadcup.handler;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.exception.BadRequestException;
import me.ruslanys.highloadcup.model.Visit;
import me.ruslanys.highloadcup.service.VisitService;

import java.util.Map;

public class VisitAddHandler extends AddHandler<Visit> {
    public VisitAddHandler() {
        super(DI.getBean(VisitService.class));
    }

    @Override
    protected Visit parseJson(Map<String, String> object) {
        for (Map.Entry<String, String> entry : object.entrySet()) {
            if (entry.getValue() == null) {
                throw new BadRequestException();
            }
        }

        Visit visit = new Visit();
        for (Map.Entry<String, String> entry : object.entrySet()) {
            int hash = entry.getKey().hashCode();
            String value = entry.getValue();

            if ("id".hashCode() == hash) {
                visit.setId(Integer.parseInt(value));
            } else if ("user".hashCode() == hash) {
                visit.setUserId(Integer.parseInt(value));
            } else if ("location".hashCode() == hash) {
                visit.setLocationId(Integer.parseInt(value));
            } else if ("mark".hashCode() == hash) {
                visit.setMark(Byte.parseByte(value));
            } else if ("visited_at".hashCode() == hash) {
                visit.setVisitedAt(Long.parseLong(value));
            }
        }
        return visit;
    }

}
