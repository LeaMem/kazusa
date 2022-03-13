package com.lea.demo.util;

import com.lea.demo.protocol.LoginRequestPacket;
import com.lea.demo.protocol.Packet;
import com.lea.demo.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class PacketCodeC {

    private static final int MAGIC_NUMBER = 0x12345678;

    public static ByteBuf encode(Packet packet){
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    public static Packet decode(ByteBuf byteBuf){
        //跳过魔数
        byteBuf.skipBytes(4);
        //跳过版本号
        byteBuf.skipBytes(1);
        //序列化算法标志
        byte serializeAlgorithm = byteBuf.readByte();
        //指令
        byte command = byteBuf.readByte();
        //数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Class<? extends Packet> requestTypeClass = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);
        return serializer.deserialize(requestTypeClass, bytes);
    }

    private static Class<? extends Packet> getRequestType(int command){
        switch (command){
            case 1:
                return LoginRequestPacket.class;
        }
        throw new UnsupportedOperationException("command unSupport");
    }

    private static Serializer getSerializer(byte serializeAlgorithm){
        switch (serializeAlgorithm){
            case 1:
                return Serializer.DEFAULT;
        }

        throw new UnsupportedOperationException("serializeAlgorithm unSupport");
    }

}
