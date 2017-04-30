package com.giorgimode.subzero.downloader;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FtpDownloaderTest {
    private              FtpDownloader ftpDownloader = new FtpDownloader();
    private static final String        SAVEPATH      = "src/test/resources/save/";

    @Test
    public void testDownloadFailed() {
        File resourcesDirectory = new File("src/test/resources/remote/en-de.zip");
        File savePathDirectory = new File(SAVEPATH + "en-de.zip");

        FTPClient ftpClient = mock(FTPClient.class);
        boolean outcome = ftpDownloader.downloadFile(ftpClient, resourcesDirectory.getAbsolutePath(), savePathDirectory.getAbsolutePath());
        assertFalse("Download must fail", outcome);
    }

    @Test
    public void testDownloadSuccessful() throws IOException {
        File remoteDirectory = new File("src/test/resources/remote/en-de.zip");
        File savePathDirectory = new File(SAVEPATH + "en-de.zip");
        String remotePath = remoteDirectory.getAbsolutePath();
        String savePath = savePathDirectory.getAbsolutePath();

        FTPClient ftpClient = mock(FTPClient.class);
        when(ftpClient.retrieveFile(eq(remotePath), any())).thenReturn(true);
        boolean outcome = ftpDownloader.downloadFile(ftpClient, remotePath, savePath);
        assertTrue("Download should be successful", outcome);
        verify(ftpClient).retrieveFile(eq(remotePath), any());
    }

    @Test
    public void testDownloadException() {
        FTPClient ftpClient = mock(FTPClient.class);
        boolean outcome = ftpDownloader.downloadFile(ftpClient, "wrongpath", "c:/wrongpath2");
        assertFalse("Download should not be successful", outcome);
    }

    @After
    public void cleanup() {
        File directory = new File(SAVEPATH);
        boolean deleted;
        if (directory.exists()) {
            deleted = directory.delete();
            assertTrue(deleted);
        }
    }

}