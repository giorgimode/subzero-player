package com.giorgimode.subzero.downloader;

import com.giorgimode.subzero.util.UnzipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
class FtpDownloader {
    /**
     *  * Download a single file from the FTP server
     *  * @param ftpClient an instance of org.apache.commons.net.ftp.FTPClient class.
     *  * @param remoteFilePath path of the file on the server
     *  * @param savePath path of directory where the file will be stored
     *  * @return true if the file was downloaded successfully, false otherwise
     *  
     */
    boolean downloadFile(FTPClient ftpClient, String remoteFilePath, String savePath) {
        File downloadFile = new File(savePath);

        File parentDir = downloadFile.getParentFile();
        boolean parentDirExists = true;
        if (!parentDir.exists()) {
            parentDirExists = parentDir.mkdirs();
        }

        if (parentDirExists) {
            boolean isDownloaded = false;
            try (OutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(downloadFile))) {
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                isDownloaded = ftpClient.retrieveFile(remoteFilePath, outputStream);
            } catch (IOException ex) {
                log.error("IOException: {}", ex);
            }
            if (!isDownloaded) {
                log.error("Failed to download {}", downloadFile.getName());
                cleanDownloadedArchive(downloadFile);
                return false;
            }
            log.info("Language pack {} downloaded successfully", downloadFile.getName());
            return extractData(downloadFile);
        } else {
            log.error("Language directory {} could not be created", parentDir.getName());
            return false;
        }
    }

    private boolean extractData(File downloadFile) {
        log.debug("Unzipping downloaded file {}", downloadFile.getName());
        boolean isSuccessfulyUnzipped = UnzipUtil.unzip(downloadFile);
        cleanDownloadedArchive(downloadFile);
        cleanUnzippedDir(downloadFile, isSuccessfulyUnzipped);
        return isSuccessfulyUnzipped;
    }

    private void cleanUnzippedDir(File downloadFile, boolean isSuccessfulyUnzipped) {
        if (!isSuccessfulyUnzipped) {
            String unzipFileDir = downloadFile.getAbsolutePath().replace(".zip", "");
            try {
                File unzipFile = new File(unzipFileDir);
                if (unzipFile.exists()) {
                    FileUtils.deleteDirectory(unzipFile);
                }
            } catch (IOException e) {
                log.error("Unzipped directory failed to be removed: {}", unzipFileDir);
            }
            log.error("Failed to unzip archive: {}", downloadFile.getName());
        }
    }

    private void cleanDownloadedArchive(File downloadFile) {
        if (!downloadFile.delete()) {
            log.error("Downloaded archive failed to be removed: {}", downloadFile.getName());
        }
    }
}
