package com.proper.enterprise.platform.core;

public final class PEPVersion {

    private PEPVersion() {}

    public static final long VERSION = getVersion().hashCode();

    public static String getVersion() {
        return "0.5.0.4.RELEASE";
    }

}
