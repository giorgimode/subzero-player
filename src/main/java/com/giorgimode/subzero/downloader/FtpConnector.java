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
public class FtpConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpConnector.class);
    private FTPClient ftpClient;
    private String host;
    private int port;
    private String username;
    private String password;
    private String remoteDir;
    private String saveDirPath;
    private boolean isLoaded = false;

    public FtpConnector() {
        ftpClient = new FTPClient();
        isLoaded = loadProperties();
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
        LOGGER.info("FTP properties were not properly configured. One of the property is missing");
        return false;
    }

    public boolean downloadLanguagePack(LanguageEnum languageEnum) {
        if (!isLoaded) {
            LOGGER.info("Files are not downloaded. FTP properties were not properly configured");
            return false;
        }
        try {
            // connect and login to the server
            ftpClient.connect(host, port);
            ftpClient.login(username, password);

            // use local passive mode to pass firewall
            ftpClient.enterLocalPassiveMode();

            LOGGER.info("Connected");

            String remoteDirPath = normalizePath(remoteDir) + languageEnum.getValue();
            saveDirPath += languageEnum.getValue();

            boolean outcome = FtpDownloader.downloadDirectory(ftpClient, remoteDirPath, saveDirPath);

            // log out and disconnect from the server
            ftpClient.logout();
            ftpClient.disconnect();

            LOGGER.info("Disconnected");
            return outcome;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}