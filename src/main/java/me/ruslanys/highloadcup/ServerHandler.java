package me.ruslanys.highloadcup;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import lombok.extern.slf4j.Slf4j;
import me.ruslanys.highloadcup.exception.BaseException;
import me.ruslanys.highloadcup.handler.HttpHandler;

import java.nio.charset.Charset;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        HttpResponseStatus responseStatus = HttpResponseStatus.OK;
        String responseBody = "";
        AsciiString mimeType = APPLICATION_JSON;

        try {
            HttpHandler handler = PathHandlerProvider.getHandler(request);
            if (handler == null) {
                writeResponse(ctx, HttpResponseStatus.NOT_FOUND, TEXT_PLAIN, "Not found.");
                return;
            }

            Object object = handler.handleRequest(request);

            if (object instanceof String) {
                responseBody = (String) object;
            } else if (object != null) {
                responseBody = object.toString();
            }
        } catch (BaseException e) {
            responseStatus = e.getStatus();
            responseBody = e.getMessage();
            mimeType = TEXT_PLAIN;
        }

        writeResponse(ctx, responseStatus, mimeType, responseBody);
    }

    private void writeResponse(ChannelHandlerContext ctx, HttpResponseStatus status, AsciiString mimeType, String body) {
        ByteBuf buf = copiedBuffer(body, CHARSET);
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                buf
        );

        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeType + "; charset=UTF-8");
        HttpUtil.setKeepAlive(response, true);

        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Something went wrong", cause);
        writeResponse(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, TEXT_PLAIN, cause.getMessage());
    }
}
