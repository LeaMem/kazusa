package com.lea.demo.server;

import io.netty.buffer.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务端读数据 " + byteBuf.toString(StandardCharsets.UTF_8));
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes("welcome kazuse".getBytes(StandardCharsets.UTF_8));
        ctx.channel().writeAndFlush(buffer);
    }

    public static void main(String[] args) {
        ByteBufAllocator allocator = new PooledByteBufAllocator();
        ByteBuf buffer = allocator.buffer();
        buffer.writeBytes("kitty".getBytes(StandardCharsets.UTF_8));
        ByteBuf slice = buffer.slice(0, 2);
        slice.release();

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer();
        byteBuf.writeBytes("kitty".getBytes(StandardCharsets.UTF_8));
        System.out.println(byteBuf.toString(StandardCharsets.UTF_8));
        byteBuf.release();

        ByteBuf byteBuf1 = PooledByteBufAllocator.DEFAULT.directBuffer();
        byteBuf1.release();

    }
}
