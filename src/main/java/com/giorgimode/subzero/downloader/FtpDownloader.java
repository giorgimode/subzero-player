package com.giorgimode.subzero.downloader;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by modeg on 3/23/2017.
 */
public class FtpDownloader {

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
    public static void downloadDirectory(FTPClient ftpClient, String remoteDir,
                                         String currentDir, String saveDir) throws IOException {
        String dirToList = remoteDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }

        FTPFile[] subFiles = ftpClient.listFiles(dirToList);

        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (currentFileName.equals(".") || currentFileName.equals("..")) {
                    // skip parent directory and the directory itself
                    continue;
                }
                String filePath;
                if (currentDir.equals("")) {
                    filePath = remoteDir + "/" + currentFileName;
                } else {
                    filePath = remoteDir + "/" + currentDir + "/"
                            + currentFileName;
                }

                String newDirPath;
                if (currentDir.equals("")) {
                    newDirPath = saveDir + "/" + currentFileName;
                } else {
                    newDirPath = saveDir + "/" + currentDir + "/" + currentFileName;
                }

                if (aFile.isDirectory()) {
                    // create the directory in saveDir
                    File newDir = new File(newDirPath);
                    boolean created = newDir.mkdirs();
                    if (created) {
                        System.out.println("CREATED the directory: " + newDirPath);
                    } else {
                        System.out.println("COULD NOT create the directory: " + newDirPath);
                    }

                    // download the sub directory
                    downloadDirectory(ftpClient, dirToList, currentFileName,
                            saveDir);
                } else {
                    // download the file
                    boolean isSuccessful = downloadSingleFile(ftpClient, filePath,
                            newDirPath);
                    if (isSuccessful) {
                        System.out.println("DOWNLOADED the file: " + filePath);
                    } else {
                        System.out.println("COULD NOT download the file: " + filePath);
                        File file = new File(newDirPath);
                        if (file.exists() && file.isFile()) {
                            boolean isDeleted = file.delete();
                            System.out.println("Corrupt File Deletion " + (isDeleted ? "was successful" : "failed"));
                        }
                    }
                }
            }
        }
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
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }

        try (OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(downloadFile))) {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient.retrieveFile(remoteFilePath, outputStream);
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
            throw ex;
        }
    }
}
