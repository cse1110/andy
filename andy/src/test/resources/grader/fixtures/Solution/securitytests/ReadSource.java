package delft;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class ExploitTest {

    @Test
    void exploitTest() throws IOException {
        try {
            String workDir = System.getProperty("andy.securitytest.workdir");
            String config = Files.readString(new File(workDir + "/delft/ExploitTest.java").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
