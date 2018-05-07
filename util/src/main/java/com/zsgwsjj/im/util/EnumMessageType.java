package com.zsgwsjj.im.util;

/**
 * @author : jiang
 * @time : 2018/4/25 16:33
 */
public enum EnumMessageType {

    MESSAGE(1),
    USER(2);

    private int type;

    EnumMessageType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
