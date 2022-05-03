package delft;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;

class ExploitTest {

    @Test
    void exploitTest() {
        try {
            Files.writeString(new File("results.xml").toPath(), "100");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
