package me.ruslanys.highloadcup.handler;

import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.model.Visit;
import me.ruslanys.highloadcup.service.VisitService;

public class VisitGetHandler extends GetHandler<Visit> {
    public VisitGetHandler() {
        super(DI.getBean(VisitService.class));
    }
}
