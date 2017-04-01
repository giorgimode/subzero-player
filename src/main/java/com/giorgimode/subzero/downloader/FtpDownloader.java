package com.giorgimode.subzero.downloader;

import com.giorgimode.subzero.util.UnzipUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by modeg on 3/23/2017.
 */
public class FtpDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpDownloader.class);

    /**
     *  * Download a single file from the FTP server
     *  * @param ftpClient an instance of org.apache.commons.net.ftp.FTPClient class.
     *  * @param remoteFilePath path of the file on the server
     *  * @param savePath path of directory where the file will be stored
     *  * @return true if the file was downloaded successfully, false otherwise
     *  * @throws IOException if any network or IO error occurred.
     *  
     */
    static boolean downloadFile(FTPClient ftpClient,
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
                boolean isDownloaded = ftpClient.retrieveFile(remoteFilePath, outputStream);
                if (!isDownloaded) {
                    LOGGER.error("Failed to download {}", downloadFile.getName());
                    return false;
                }
            } catch (IOException ex) {
                LOGGER.error("Error: {}", ex);
                throw ex;
            }
            LOGGER.info("Language pack {} downloaded successfully", downloadFile.getName());
            return extractData(downloadFile);
        } else {
            LOGGER.error("Language directory {} could not be created", parentDir.getName());
            return false;
        }
    }

    private static boolean extractData(File downloadFile) {
        boolean isSuccessfulyUnzipped = UnzipUtil.unzip(downloadFile);
        if (!downloadFile.delete()) {
            LOGGER.error("Downloaded archive failed to be removed");
        }
        return isSuccessfulyUnzipped;
    }
}
