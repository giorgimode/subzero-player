package com.giorgimode.subzero.downloader;

import com.giorgimode.dictionary.LanguageEnum;
import com.giorgimode.subzero.util.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.WindowsFakeFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by modeg on 3/28/2017.
 */
public class DownloadServiceIT {
    private        DownloadService downloadService;
    private static FakeFtpServer   fakeFtpServer;
    private static final String RESOURCE_PATH = "src/test/resources/";
    public static final  String FTP_HOME_DIR  = "D:/lang";

    @BeforeClass
    public static void beforeClass() throws IOException {
        fakeFtpServer = new FakeFtpServer();
    }

    @Before
    public void init() {
        downloadService = new DownloadService(RESOURCE_PATH + "config/");
    }

    @After
    public void clean() throws IOException {
        FileUtils.deleteDirectory(new File(RESOURCE_PATH + "savedir"));
        fakeFtpServer.stop();
    }

    @Test
    public void downloadLanguagePack() throws Exception {
        startMock(fakeFtpServer, true);
        File savePathDirectory = new File(RESOURCE_PATH + "savedir/");
        String savePath = savePathDirectory.getAbsolutePath();

        downloadService.setSaveFilePath(Utils.normalizePath(savePath));
        boolean isDownloadSuccessful = downloadService.downloadLanguagePack(LanguageEnum.BG_DE);
        assertTrue(isDownloadSuccessful);

        File savedFileDir = new File(RESOURCE_PATH + "savedir/" + LanguageEnum.BG_DE.getValue());
        assertTrue(savedFileDir.exists());
        for (File f : savedFileDir.listFiles()) {
            String content = FileUtils.readFileToString(f, StandardCharsets.UTF_8);
            assertTrue(StringUtils.isNotBlank(content));
        }
    }

    @Test
    public void downloadLanguagePackFailed() throws Exception {
        startMock(fakeFtpServer, false);

        File savePathDirectory = new File(RESOURCE_PATH + "savedir/");
        String savePath = savePathDirectory.getAbsolutePath();

        downloadService.setSaveFilePath(Utils.normalizePath(savePath));
        boolean isDownloadSuccessful = downloadService.downloadLanguagePack(LanguageEnum.BG_DE);
        assertFalse(isDownloadSuccessful);

        File savedFileDir = new File(RESOURCE_PATH + "savedir/" + LanguageEnum.BG_DE.getValue());
        assertFalse(savedFileDir.exists());
    }

    private static void startMock(FakeFtpServer fakeFtpServer, boolean addArchive) throws IOException {
        fakeFtpServer.addUserAccount(new UserAccount("user", "password", FTP_HOME_DIR));
        fakeFtpServer.setServerControlPort(31);
        FileSystem fileSystem = new WindowsFakeFileSystem();
        fileSystem.add(new DirectoryEntry(FTP_HOME_DIR));

        if (addArchive) {
            addArchiveToFTP(fileSystem);
        }
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.start();
    }

    private static void addArchiveToFTP(FileSystem fileSystem) throws IOException {
        File configPathDirectory = new File(RESOURCE_PATH + "bg-de.zip");
        FileInputStream in = new FileInputStream(configPathDirectory);

        FileEntry fileSystemEntry = new FileEntry(FTP_HOME_DIR + "/bg-de.zip");
        fileSystemEntry.setContents(IOUtils.toByteArray(in));
        fileSystem.add(fileSystemEntry);
    }
}