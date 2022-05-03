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
            byte[] config = Files.readAllBytes(new File(workDir + "/delft/ExploitTest$1.class").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
