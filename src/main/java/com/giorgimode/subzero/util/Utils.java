package com.giorgimode.subzero.util;

import java.io.File;
import java.io.IOException;

public final class Utils {
    public static String parentDir() {
        return parentDir("lang" + "\\");
    }

    public static String parentDir(String dir) {
        try {
            return new File(".").getCanonicalFile().getParent() + "\\" + dir;
        } catch (IOException e) {
            return "";
        }
    }

    public static String normalizePath(String path) {
        path = path != null ? path.replaceAll("//", "/") : "";
        return  (path.endsWith("/") || path.endsWith("\\")) ? path : path + "/";
    }

    private Utils() {
    }
}

