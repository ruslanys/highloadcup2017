package me.ruslanys.highloadcup.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public class BaseException extends RuntimeException {

    private final HttpResponseStatus status;

    public BaseException(HttpResponseStatus status, String message) {
        super(message);
        this.status = status;
    }

    public BaseException(HttpResponseStatus status, Throwable cause) {
        super(cause);
        this.status = status;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }
}
