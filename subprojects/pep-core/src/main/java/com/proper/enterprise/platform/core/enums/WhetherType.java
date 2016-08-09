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

    private WhetherType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return 0;
    }

}
