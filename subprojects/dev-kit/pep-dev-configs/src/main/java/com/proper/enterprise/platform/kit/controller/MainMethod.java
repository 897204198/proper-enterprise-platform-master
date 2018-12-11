package com.proper.enterprise.platform.kit.controller;

import java.io.File;
import java.io.FileFilter;

public class MainMethod {

    public static void goThrough(File folder) {
        FileFilter javaFilter = new FileFilter() {
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                } else {
                    String name = file.getName();
                    if (name.endsWith("Controller.java")) {

                        return true;
                    }
                }
                return false;
            }
        };

        File[] children = folder.listFiles(javaFilter);
        for (File child : children) {
            if (child.isDirectory()) {
                goThrough(child);
            } else {
                child.getPath();
                //System.out.println(child.getPath());
            }
        }
    }

    public static void main(String args[]) {
        File file = new File("D:\\workspace\\proper-enterprise-platform\\subprojects");
        goThrough(file);

    }
}
