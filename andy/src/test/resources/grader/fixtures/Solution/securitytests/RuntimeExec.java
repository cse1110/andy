package delft;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class ExploitTest {

    @Test
    void exploitTest() throws IOException {
        Process process = Runtime.getRuntime().exec("echo test");
    }

}
