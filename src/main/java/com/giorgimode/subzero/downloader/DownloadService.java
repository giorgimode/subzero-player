package com.giorgimode.subzero.downloader;

import com.giorgimode.dictionary.impl.LanguageEnum;
import com.giorgimode.subzero.util.Utils;
import com.google.common.primitives.Ints;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.giorgimode.subzero.util.Utils.normalizePath;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

@Getter
@Setter
public class DownloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);
    private FTPClient ftpClient;
    private String host;
    private int port;
    private String username;
    private String password;
    private String remoteDir;
    private String saveDirPath;
    private boolean isLoaded = false;

    public DownloadService() {
        ftpClient = new FTPClient();
        isLoaded = loadProperties();
    }

    public boolean downloadLanguagePack(LanguageEnum languageEnum) {
        if (!isLoaded) {
            LOGGER.error("Files are not downloaded. FTP properties were not properly configured");
            return false;
        }
        try {
            if (connectToFTP()) return false;

            String remoteDirPath = normalizePath(remoteDir) + languageEnum.getValue() + ".zip";
            saveDirPath += languageEnum.getValue() + ".zip";

            boolean isDownloaded = FtpDownloader.downloadFile(ftpClient, remoteDirPath, saveDirPath);
            // log out and disconnect from the server
            ftpClient.logout();
            ftpClient.disconnect();

            LOGGER.info("Disconnected");
            return isDownloaded;
        } catch (IOException ex) {
            LOGGER.error("Could not open FTP connection");
            LOGGER.error("Exception thrown: {}", ex);
            return false;
        }
    }

    private boolean loadProperties() {
        String parentDir = Utils.parentDir("config");
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
        String pRemoteDir = prop.getProperty("remoteDir", "D:/lang/");
        if (isNoneBlank(pHost, pPort, pUsername, pPassword, pRemoteDir)) {
            setHost(pHost);
            setPort(Ints.tryParse(pPort));
            setUsername(pUsername);
            setPassword(pPassword);
            setRemoteDir(pRemoteDir);
            setSaveDirPath(Utils.parentDir());
            return true;
        }
        LOGGER.error("FTP properties were not properly configured. One of the property is missing");
        return false;
    }

    private boolean connectToFTP() throws IOException {
        // connect and login to the server
        ftpClient.connect(host, port);
        if (!ftpClient.login(username, password)) {
            LOGGER.error("User not authenticated");
            return true;
        }

        // use local passive mode to pass firewall
        ftpClient.enterLocalPassiveMode();

        LOGGER.info("Connected");
        return false;
    }
}