package testers;

import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.WindowsFakeFileSystem;

/**
 * Created by modeg on 3/23/2017.
 */
public class MockFtpServer {
    public static void main(String[] args) {
        FakeFtpServer fakeFtpServer = new FakeFtpServer();
        String homeDir = "D:/remotedir";
        fakeFtpServer.addUserAccount(new UserAccount("user", "password", homeDir));
        fakeFtpServer.setServerControlPort(21);
        FileSystem fileSystem = new WindowsFakeFileSystem();
        fileSystem.add(new DirectoryEntry(homeDir));
        fileSystem.add(new FileEntry(homeDir + "/file1.txt", "abcdef 1234567890"));
        fileSystem.add(new FileEntry(homeDir + "/run.exe"));
        fakeFtpServer.setFileSystem(fileSystem);

        fakeFtpServer.start();
    }
}
