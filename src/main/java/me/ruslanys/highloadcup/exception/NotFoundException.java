package me.ruslanys.highloadcup.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public class NotFoundException extends BaseException {

    public static final HttpResponseStatus STATUS = HttpResponseStatus.NOT_FOUND;

    public NotFoundException() {
        super(STATUS, STATUS.reasonPhrase());
    }
}
