package integration;

import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@EnabledOnOs({OS.LINUX, OS.MAC})
public class ExternalProcessTest extends IntegrationTestBase {

    @Test
    void externalProcessGracefulExit() {
        Result result = run("EmptyLibrary", "EmptySolution",
                "ExternalProcessGracefulExitConfiguration");

        assertThat(result.getExternalProcessOutput()).isEqualTo("hello\n");
    }

    @Test
    void externalProcessEndSignal() {
        assertTimeout(ofSeconds(20), () -> {

            Result result = run("EmptyLibrary", "EmptySolution",
                    "ExternalProcessEndSignalConfiguration");

            assertThat(result.getExternalProcessOutput())
                    .startsWith("hello hello endsignal");

        });
    }
}