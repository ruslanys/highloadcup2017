package me.ruslanys.highloadcup;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import me.ruslanys.highloadcup.annotation.OrderedComparator;
import me.ruslanys.highloadcup.component.StartupListener;
import org.reflections.Reflections;

import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class Server implements Runnable {

    private final EventLoopGroup bossGroup = new EpollEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new EpollEventLoopGroup(12); // 12?

    private final int port;

    public Server(int port) {
        this.port = port;
        DI.getConfig().setPort(port);
        invokeStartupListeners();
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    private void invokeStartupListeners() {
        Reflections reflections = new Reflections("me.ruslanys.*");
        Set<Class<? extends StartupListener>> listeners = new TreeSet<>(new OrderedComparator());
        listeners.addAll(reflections.getSubTypesOf(StartupListener.class));

        for (Class<? extends StartupListener> clazz : listeners) {
            try {
                log.info("Starting {} startup hook.", clazz.getSimpleName());
                StartupListener listener = clazz.newInstance();
                listener.onStartup();

                DI.add(clazz, listener);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("Can not invoke listener {}", clazz.getName());
            }
        }
    }

    @Override
    public void run() {
        try {
            ServerBootstrap sb = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(EpollServerSocketChannel.class)
                    .childHandler(new ServerChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 512)
//                    .option(ChannelOption.SO_REUSEADDR, true)
//                    .childOption(ChannelOption.SO_SNDBUF, 128 * 1024)
//                    .childOption(ChannelOption.SO_RCVBUF, 128 * 1024)
//                    .childOption(ChannelOption.TCP_NODELAY, true)
//                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
            ;

            ChannelFuture future = sb.bind(port);
            log.info("Server is started on {} port.", port);

            future.sync(); // locking the thread until groups are going on
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("Something went wrong", e);
        } finally {
            shutdown();
        }
    }

    private void shutdown() {
        log.info("Server is shutting down.");
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Please configure the port number properly.");
            System.exit(1);
        }
        new Server(Integer.valueOf(args[0])).run();
    }
}
