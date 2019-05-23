package com.proper.enterprise.platform.core;

public final class PEPVersion {

    private PEPVersion() {}

    public static final long VERSION = getVersion().hashCode();

    public static String getVersion() {
        // Avoid find bug match version string as hard coded IP,
        // use .RELEASE or -SNAPSHOT suffix
        // https://pmd.github.io/pmd-5.5.7/pmd-java/rules/java/basic.html#AvoidUsingHardCodedIP
        return "0.5.1.2.RELEASE";
    }

}
