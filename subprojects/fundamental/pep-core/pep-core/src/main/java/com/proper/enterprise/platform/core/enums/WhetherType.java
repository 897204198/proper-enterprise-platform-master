package com.proper.enterprise.platform.core.enums;

public enum WhetherType implements IntEnum {

    /**
     * 是
     */
    YES(1),

    /**
     * 否
     */
    NO(0);

    private int code;

    WhetherType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static WhetherType codeOf(int code) {
        for (WhetherType whetherType : values()) {
            if (whetherType.getCode() == code) {
                return whetherType;
            }
        }
        return null;
    }

}
