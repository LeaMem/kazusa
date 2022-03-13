package com.lea.demo.protocol;

import lombok.Data;

@Data
public abstract class Packet {

    //协议版本
    private Byte version;

    //获取指令
    public abstract Byte getCommand();

}
