package com.giorgimode.subzero.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnzipUtil.class);
    private static final int BUFFER_SIZE = 1024;

    public static boolean unzip(File zipFile) {
        String parentDir;
        try {
            parentDir = zipFile.getParentFile().getCanonicalPath();
        } catch (IOException e) {
            LOGGER.error("Failed to access target directory to unzip {}", zipFile.getName());
            return false;
        }
        return unzip(zipFile, parentDir);
    }

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     */
    private static boolean unzip(File zipFile, String destDirectory) {
        createDir(destDirectory);
        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    // if the entry is a directory, make the directory
                    createDir(filePath);
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch (IOException e) {
            LOGGER.error("Failed to unzip file {}", zipFile.getName());
            LOGGER.error("Thrown exception: {}", e);
            return false;
        }
        LOGGER.info("Unzipped file {}", zipFile.getName());
        return true;
    }

    private static void createDir(String destDirectory) {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            boolean mkdirs = destDir.mkdirs();
            if (!mkdirs) LOGGER.error("Failed to create directory {}", destDirectory);
        }
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        LOGGER.info("Extracting file {}", filePath);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read;
        while ((read = zipIn.read(bytesIn)) > 0) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}