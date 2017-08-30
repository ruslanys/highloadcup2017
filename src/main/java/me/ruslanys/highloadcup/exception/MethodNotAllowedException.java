package me.ruslanys.highloadcup.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class MethodNotAllowedException extends BaseException {

    public static final HttpResponseStatus STATUS = HttpResponseStatus.METHOD_NOT_ALLOWED;

    public MethodNotAllowedException() {
        super(STATUS, STATUS.reasonPhrase());
    }
}
