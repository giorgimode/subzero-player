package com.giorgimode.subzero.downloader;

import com.giorgimode.dictionary.impl.LanguageEnum;
import com.giorgimode.subzero.util.Utils;
import com.google.common.primitives.Ints;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.giorgimode.subzero.util.Utils.normalizePath;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

@Getter
@Setter
public class FtpConnector {
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
        //todo proper logger
        System.out.println("FTP properties were not properly configured. One of the property is missing");
        return false;
    }

    public void downloadLanguagePack(LanguageEnum languageEnum) {
        if (!isLoaded) {
            System.out.println("Files are not downloaded. FTP properties were not properly configured");
            return;
        }
        try {
            // connect and login to the server
            ftpClient.connect(host, port);
            ftpClient.login(username, password);

            // use local passive mode to pass firewall
            ftpClient.enterLocalPassiveMode();

            System.out.println("Connected");

            String remoteDirPath = normalizePath(remoteDir) + languageEnum.getValue();
            saveDirPath += languageEnum.getValue();

            FtpDownloader.downloadDirectory(ftpClient, remoteDirPath, saveDirPath);

            // log out and disconnect from the server
            ftpClient.logout();
            ftpClient.disconnect();

            System.out.println("Disconnected");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}