package com.giorgimode.subzero.downloader;

import com.giorgimode.dictionary.impl.LanguageEnum;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by modeg on 3/28/2017.
 */
public class FtpConnectorTest {
    private FtpConnector ftpConnector;

    @Before
    public void init(){
        ftpConnector = new FtpConnector();
    }
    @Test
    public void downloadLanguagePack() throws Exception {
        ftpConnector.setSaveDirPath("D:/coding/workspace/projects/maste-project/tempftp/output/");
        ftpConnector.downloadLanguagePack(LanguageEnum.BG_DE);
    }

}