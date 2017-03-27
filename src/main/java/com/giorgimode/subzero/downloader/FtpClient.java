package com.giorgimode.subzero.downloader;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.giorgimode.subzero.Application.application;

public class FtpClient {
    private FTPClient ftpClient;
    //lombok
    private String host;
    private int port;
    private String user;
    private String pass;

    public FtpClient() {
        ftpClient = new FTPClient();
        loadProperties();
    }

    private void loadProperties() {
        String parentDir = application().parentDir("config");
        File file = new File(parentDir + "config.properties");
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prop.get("host");
    }

    public void main() {
        host = "localhost";
        port = 21;
        user = "user";
        pass = "password";


        try {
            // connect and login to the server
            ftpClient.connect(host, port);
            ftpClient.login(user, pass);

            // use local passive mode to pass firewall
            ftpClient.enterLocalPassiveMode();

            System.out.println("Connected");

            String remoteDirPath = "D:/remotedir";
            String saveDirPath = "D:/coding/workspace/projects/tempftp/output";

            FtpDownloader.downloadDirectory(ftpClient, remoteDirPath, "", saveDirPath);

            // log out and disconnect from the server
            ftpClient.logout();
            ftpClient.disconnect();

            System.out.println("Disconnected");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}