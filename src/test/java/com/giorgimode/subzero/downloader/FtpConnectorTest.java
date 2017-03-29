package com.giorgimode.subzero.downloader;

import com.giorgimode.dictionary.impl.LanguageEnum;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.WindowsFakeFileSystem;

import static org.junit.Assert.assertTrue;

/**
 * Created by modeg on 3/28/2017.
 */
public class FtpConnectorTest {
    private FtpConnector ftpConnector;
    private static FakeFtpServer fakeFtpServer;

    @Before
    public void init() {
        ftpConnector = new FtpConnector();
    }

    @BeforeClass
    public static void beforeClass() {
        fakeFtpServer = new FakeFtpServer();
        startMock(fakeFtpServer);
    }
    @AfterClass
    public static void afterClass() {
        fakeFtpServer.stop();
    }
    @Test
    public void downloadLanguagePack() throws Exception {
        ftpConnector.setSaveDirPath("D:/coding/workspace/projects/maste-project/tempftp/output/");
        boolean isDownloadSuccessful = ftpConnector.downloadLanguagePack(LanguageEnum.BG_DE);
        assertTrue(isDownloadSuccessful);
    }

    private static void startMock(FakeFtpServer fakeFtpServer) {
        String homeDir = "D:/langer/bg-de";
        fakeFtpServer.addUserAccount(new UserAccount("username", "password", homeDir));
        fakeFtpServer.setServerControlPort(21);
        FileSystem fileSystem = new WindowsFakeFileSystem();
        fileSystem.add(new DirectoryEntry(homeDir));
        fileSystem.add(new FileEntry(homeDir + "/file1.txt", "abcdef 1234567890"));
        fileSystem.add(new FileEntry(homeDir + "/run.exe"));
        fileSystem.add(new FileEntry(homeDir + "/test.properties"));
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.start();
    }

}