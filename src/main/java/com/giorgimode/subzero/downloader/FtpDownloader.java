package com.giorgimode.subzero.downloader;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.giorgimode.subzero.util.Utils.normalizePath;

/**
 * Created by modeg on 3/23/2017.
 */
public class FtpDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpDownloader.class);

    static boolean downloadDirectory(FTPClient ftpClient, String remoteDir, String saveDir) throws IOException {
        return downloadDirectory(ftpClient, remoteDir, "", saveDir);
    }

    /**
     * Download a whole directory from a FTP server.
     *
     * @param ftpClient  an instance of org.apache.commons.net.ftp.FTPClient class.
     * @param remoteDir  Path of the parent directory of the current directory being
     *                   downloaded.
     * @param currentDir Path of the current directory being downloaded.
     * @param saveDir    path of directory where the whole remote directory will be
     *                   downloaded and saved.
     * @throws IOException if any network or IO error occurred.
     */
    public static boolean downloadDirectory(FTPClient ftpClient, String remoteDir,
                                            String currentDir, String saveDir) throws IOException {
        String dirToList = remoteDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }

        FTPFile[] subFiles = ftpClient.listFiles(dirToList);

        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (!currentFileName.endsWith(".properties")) {
                    // download only property files
                    continue;
                }
                String filePath = remoteDir + normalizePath("/" + currentDir + "/") + currentFileName;
                String newDirPath = saveDir + normalizePath("/" + currentDir + "/") + currentFileName;


                if (aFile.isDirectory()) {
                    // create the directory in saveDir
                    File newDir = new File(newDirPath);
                    boolean created = newDir.mkdirs();
                    if (created) {
                        LOGGER.info("CREATED the directory: {}", newDirPath);
                    } else {
                        LOGGER.info("COULD NOT create the directory: {}", newDirPath);
                    }

                    // download the sub directory
                    return downloadDirectory(ftpClient, dirToList, currentFileName,
                            saveDir);
                } else {
                    // download the file
                    boolean isSuccessful = downloadSingleFile(ftpClient, filePath,
                            newDirPath);
                    if (isSuccessful) {
                        LOGGER.info("DOWNLOADED the file: {}", filePath);
                        return true;
                    } else {
                        LOGGER.info("COULD NOT download the file: {}", filePath);
                        File file = new File(newDirPath);
                        if (file.exists() && file.isFile()) {
                            boolean isDeleted = file.delete();
                            LOGGER.info("Corrupt File Deletion {}", isDeleted ? "was successful" : "failed");
                        }
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     *  * Download a single file from the FTP server
     *  * @param ftpClient an instance of org.apache.commons.net.ftp.FTPClient class.
     *  * @param remoteFilePath path of the file on the server
     *  * @param savePath path of directory where the file will be stored
     *  * @return true if the file was downloaded successfully, false otherwise
     *  * @throws IOException if any network or IO error occurred.
     *  
     */
    private static boolean downloadSingleFile(FTPClient ftpClient,
                                              String remoteFilePath, String savePath) throws IOException {
        File downloadFile = new File(savePath);

        File parentDir = downloadFile.getParentFile();
        boolean parentDirExists = true;
        if (!parentDir.exists()) {
            parentDirExists = parentDir.mkdirs();
        }

        if (parentDirExists) {
            try (OutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(downloadFile))) {
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                return ftpClient.retrieveFile(remoteFilePath, outputStream);
            } catch (IOException ex) {
                LOGGER.info("Error: {}", ex);
                throw ex;
            }
        } else {
            LOGGER.info("Language directory could not be created");
            return false;
        }
    }
}
