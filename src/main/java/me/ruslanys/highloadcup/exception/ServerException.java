package me.ruslanys.highloadcup.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public class ServerException extends BaseException {

    public static final HttpResponseStatus INTERNAL_SERVER_ERROR = HttpResponseStatus.INTERNAL_SERVER_ERROR;

    public ServerException(Throwable cause) {
        super(INTERNAL_SERVER_ERROR, cause);
    }

}
