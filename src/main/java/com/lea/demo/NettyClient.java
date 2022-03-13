package com.lea.demo;

import com.lea.demo.client.FirstClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    public static String it = "hello world";

    private static final int Max_Retry = 5;

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel ch) throws Exception {
//                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
                })
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE);

        connect(bootstrap, "127.0.0.1", 8000, Max_Retry);

//        Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();
//
//        while (true) {
//            channel.writeAndFlush(new Date() + createStr(new Random().nextInt(10)));
//            Thread.sleep(2000);
//        }
    }

    public static String createStr(int len) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(it);
        }
        return builder.toString();
    }

    private static void connect(final Bootstrap bootstrap, final String host, final int port, final int retry) {

        bootstrap.connect(host, port)
                .addListener(new GenericFutureListener<ChannelFuture>() {
                    public void operationComplete(ChannelFuture future1) throws Exception {
                        if (future1.isSuccess()) {
                            System.out.println("连接成功");

//                            Channel channel = future1.channel();
//                            while (true) {
//                                channel.writeAndFlush(new Date() + createStr(new Random().nextInt(10)));
//                                Thread.sleep(2000);
//                            }

                        } else if (retry <= 0) {
                            System.err.println("重试次数用完，放弃连接");
                        } else {
                            int order = Max_Retry - retry + 1;
                            int delay = 1 << order;
                            System.err.println(new Date() + " :连接失败,第" + order + "次重连...");
                            bootstrap.config().group().schedule(new Runnable() {
                                public void run() {
                                    connect(bootstrap, host, port, retry - 1);
                                }
                            }, delay, TimeUnit.SECONDS);
                        }
                    }
                });
    }

}
