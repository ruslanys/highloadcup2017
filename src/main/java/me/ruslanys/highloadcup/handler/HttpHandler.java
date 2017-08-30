package me.ruslanys.highloadcup.handler;

import io.netty.handler.codec.http.FullHttpRequest;

public interface HttpHandler {

    Object handleRequest(FullHttpRequest request) throws Exception;

}
