package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

@EnabledOnOs({OS.LINUX, OS.MAC})
public class ExternalProcessTest extends IntegrationTestBase {

    private static final String INIT_SIGNAL_SHELL_SCRIPT_PATH = "/andy_test_external_process_init_signal.sh";
    private static final String GRACEFUL_EXIT_SHELL_SCRIPT_PATH = "/andy_test_external_process_graceful_exit.sh";
    private static final String EXTERNAL_PROCESS_ERROR = "/andy_test_external_process_error.sh";
    private static final String EXTERNAL_PROCESS_CRASHES = "/andy_test_external_process_crashes.sh";
    private static final String EXTERNAL_PROCESS_LOCAL_CONNECTION = "/andy_test_external_process_local_connection.sh";

    private static final String INIT_SIGNAL_GENERATED_FILE_PATH = "/andy_test_external_process_init_signal_generated";
    private static final String INIT_SIGNAL_GENERATED_FILE_PATH_2 = "/andy_test_external_process_init_signal_generated_2";
    private static final String GRACEFUL_EXIT_GENERATED_FILE_PATH = "/andy_test_external_process_graceful_exit_generated";

    @BeforeAll
    static void copyShellScripts() throws IOException {
        String tmp = getTempDirectory();

        Files.writeString(Path.of(tmp + INIT_SIGNAL_SHELL_SCRIPT_PATH), String.format("""
                        echo "hello" > %s
                        echo "initSignal"
                        sleep 30
                        echo "should be killed before this line is executed" > %s
                        """,
                tmp + INIT_SIGNAL_GENERATED_FILE_PATH,
                tmp + INIT_SIGNAL_GENERATED_FILE_PATH_2));

        Files.writeString(Path.of(tmp + GRACEFUL_EXIT_SHELL_SCRIPT_PATH), String.format("""
                echo "hello" > %s
                """, tmp + GRACEFUL_EXIT_GENERATED_FILE_PATH));

        Files.writeString(Path.of(tmp + EXTERNAL_PROCESS_ERROR), """
                echo "some error" 1>&2
                echo "initSignal"
                sleep 30
                """);

        Files.writeString(Path.of(tmp + EXTERNAL_PROCESS_CRASHES), """
                echo "some error" 1>&2
                exit 1
                """);

        Files.writeString(Path.of(tmp + EXTERNAL_PROCESS_LOCAL_CONNECTION), """
                while true; do echo "HTTP/1.1 200 OK\\nContent-Length: 5\\n\\nhello" | nc -l 8086; done
                """);
    }

    @AfterAll
    static void shellCleanup() throws IOException {
        String tmp = getTempDirectory();
        Files.deleteIfExists(Path.of(tmp + INIT_SIGNAL_SHELL_SCRIPT_PATH));
        Files.deleteIfExists(Path.of(tmp + GRACEFUL_EXIT_SHELL_SCRIPT_PATH));
        Files.deleteIfExists(Path.of(tmp + EXTERNAL_PROCESS_ERROR));
        Files.deleteIfExists(Path.of(tmp + EXTERNAL_PROCESS_CRASHES));
        Files.deleteIfExists(Path.of(tmp + EXTERNAL_PROCESS_LOCAL_CONNECTION));

        Files.deleteIfExists(Path.of(tmp + INIT_SIGNAL_GENERATED_FILE_PATH));
        Files.deleteIfExists(Path.of(tmp + INIT_SIGNAL_GENERATED_FILE_PATH_2));
        Files.deleteIfExists(Path.of(tmp + GRACEFUL_EXIT_GENERATED_FILE_PATH));
    }

    private static String getTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    @Test
    void externalProcessGracefulExit() {
        Result result = run("EmptyLibrary", "EmptySolution",
                "ExternalProcessGracefulExitConfiguration");

        assertThat(result.hasGenericFailure()).isFalse();

        String tmp = getTempDirectory();
        assertThat(new File(tmp + GRACEFUL_EXIT_GENERATED_FILE_PATH)).exists();
    }

    @Test
    void externalProcessInitSignal() {
        assertTimeoutPreemptively(ofSeconds(10), () -> {

            Result result = run("EmptyLibrary", "EmptySolution",
                    "ExternalProcessInitSignalConfiguration");

            assertThat(result.hasGenericFailure()).isFalse();

            String tmp = getTempDirectory();
            assertThat(new File(tmp + INIT_SIGNAL_GENERATED_FILE_PATH)).exists();
            assertThat(new File(tmp + INIT_SIGNAL_GENERATED_FILE_PATH_2)).doesNotExist();

        });
    }

    @Test
    void externalProcessError() {
        assertTimeoutPreemptively(ofSeconds(10), () -> {

            Result result = run("EmptyLibrary", "EmptySolution",
                    "ExternalProcessErrorConfiguration");

            assertThat(result.hasGenericFailure()).isFalse();

        });
    }

    @Test
    void externalProcessCrashes() {
        Result result = run("EmptyLibrary", "EmptySolution",
                "ExternalProcessCrashesConfiguration");

        assertThat(result.getGenericFailure().getExternalProcessExitCode())
                .isPresent()
                .hasValue(1);
        assertThat(result.getGenericFailure().getExternalProcessErrorMessages())
                .isPresent()
                .hasValue("some error");
    }

    @Test
    void localConnectionWithoutInitSignalTest() {
        assertTimeoutPreemptively(ofSeconds(10), () -> {
            Result result = run(Action.TESTS, "EmptyLibrary", "ExternalProcessLocalConnectionSolution",
                    "ExternalProcessLocalConnectionConfiguration");

            assertThat(result.hasFailed()).isFalse();
            assertThat(result.hasGenericFailure()).isFalse();
        });
    }
}
