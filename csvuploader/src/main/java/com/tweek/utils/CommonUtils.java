package com.tweek.utils;

public class CommonUtils {

    public static String getCollectionName(String filename) {

        filename = filename.trim().toLowerCase().replaceAll("\\s+", "_");

        if(filename.contains(".")) {
            filename = filename.substring(0, filename.lastIndexOf("."));
        }

        return filename;
    }
}
