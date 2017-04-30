package com.giorgimode.subzero.downloader;

import com.giorgimode.dictionary.LanguageEnum;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by modeg on 4/30/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DownloadServiceTest {
    @Mock
    private FTPClient       ftpClient;
    @Mock
    private FtpDownloader   ftpDownloader;
    @InjectMocks
    private DownloadService downloadService;

    @Test
    public void testFtpConnectionFailed() throws IOException {
        boolean outcome = downloadService.downloadLanguagePack(LanguageEnum.BG_DE);
        assertFalse("ftp client should not authenticate user", outcome);

        verify(ftpClient).connect(anyString(), anyInt());
        verify(ftpClient).login(anyString(), any());
        verify(ftpClient).disconnect();
        verifyNoMoreInteractions(ftpClient, ftpDownloader);
    }

    @Test
    public void testFtpConnectionSuccess() throws IOException {
        when(ftpClient.login(anyString(), anyString())).thenReturn(true);
        when(ftpDownloader.downloadFile(eq(ftpClient), anyString(), anyString())).thenReturn(true);

        boolean outcome = downloadService.downloadLanguagePack(LanguageEnum.BG_DE);
        assertTrue("download was not successful", outcome);

        verify(ftpClient).connect(anyString(), anyInt());
        verify(ftpClient).login(anyString(), any());
        verify(ftpClient).enterLocalPassiveMode();
        verify(ftpClient).logout();
        verify(ftpClient).disconnect();
        verify(ftpDownloader).downloadFile(eq(ftpClient), anyString(), anyString());
        verifyNoMoreInteractions(ftpClient, ftpDownloader);
    }

    @Test
    public void testFtpConnectionException() throws IOException {
        when(ftpClient.login(anyString(), anyString())).thenReturn(true);
        when(ftpClient.logout()).thenThrow(new IOException());

        boolean outcome = downloadService.downloadLanguagePack(LanguageEnum.BG_DE);
        assertFalse("ftp log was successful incorrectly", outcome);

        verify(ftpClient).connect(anyString(), anyInt());
        verify(ftpClient).login(anyString(), any());
        verify(ftpClient).enterLocalPassiveMode();
        verify(ftpClient).logout();
        verify(ftpDownloader).downloadFile(eq(ftpClient), anyString(), anyString());
        verifyNoMoreInteractions(ftpClient, ftpDownloader);
    }

}