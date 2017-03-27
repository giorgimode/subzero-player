package testers;

import com.giorgimode.subzero.downloader.FtpDownloader;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public class FTPDownloadDirectoryTest {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 21;
        String user = "user";
        String pass = "password";

        FTPClient ftpClient = new FTPClient();

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