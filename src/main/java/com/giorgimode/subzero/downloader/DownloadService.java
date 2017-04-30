package com.giorgimode.subzero.downloader;

import com.giorgimode.dictionary.LanguageEnum;
import com.giorgimode.subzero.util.Utils;
import com.google.common.primitives.Ints;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.giorgimode.subzero.util.Utils.normalizePath;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

@Slf4j
public class DownloadService {
    private FTPClient     ftpClient;
    private FtpDownloader ftpDownloader;
    private String        host;
    private int           port;
    private String        username;
    private String        password;
    private String        remoteDir;
    @Setter
    private String        saveFilePath;
    private boolean isLoaded = false;
    private String parentDir;

    public DownloadService() {
        parentDir = Utils.parentDir("config");
        init();
    }

    public DownloadService(String parentDir) {
        this.parentDir = parentDir;
        init();
    }

    private void init() {
        ftpClient = new FTPClient();
        ftpDownloader = new FtpDownloader();
        isLoaded = loadProperties();
    }

    public boolean downloadLanguagePack(LanguageEnum languageEnum) {
        if (!isLoaded) {
            log.error("Files are not downloaded. FTP properties were not properly configured");
            return false;
        }
        try {
            if (ftpConnectionFailed()) {
                ftpClient.disconnect();
                return false;
            }

            String remoteFilePath = normalizePath(remoteDir) + languageEnum.getValue() + ".zip";
            saveFilePath += languageEnum.getValue() + ".zip";

            boolean isDownloaded = ftpDownloader.downloadFile(ftpClient, remoteFilePath, saveFilePath);
            // log out and disconnect from the server
            ftpClient.logout();
            ftpClient.disconnect();

            log.info("Disconnected");
            return isDownloaded;
        } catch (IOException ex) {
            log.error("Could not open FTP connection: {}", ex);
            return false;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private boolean loadProperties() {
        File file = new File(normalizePath(parentDir) + "config.properties");
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String pHost = prop.getProperty("host");
        String pPort = prop.getProperty("port");
        String pUsername = prop.getProperty("username");
        String pPassword = prop.getProperty("password");
        String pRemoteDir = prop.getProperty("remoteDir");
        if (isNoneBlank(pHost, pPort, pUsername, pPassword, pRemoteDir)) {
            host = pHost;
            port = Ints.tryParse(pPort);
            username = pUsername;
            password = pPassword;
            remoteDir = pRemoteDir;
            saveFilePath = Utils.parentDir();
            return true;
        }
        log.error("FTP properties were not properly configured. One of the property is missing");
        return false;
    }

    private boolean ftpConnectionFailed() throws IOException {
        // connect and login to the server
        ftpClient.connect(host, port);
        if (!ftpClient.login(username, password)) {
            log.error("User not authenticated");
            return true;
        }

        // use local passive mode to pass firewall
        ftpClient.enterLocalPassiveMode();

        log.info("Connected");
        return false;
    }
}
