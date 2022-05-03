package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;


public class ExternalProcessMetaTestsTest extends BaseMetaTestsTest {

    private static final String EXTERNAL_PROCESS_LOCAL_CONNECTION =
            "/andy_test_external_process_local_connection";
    private static final String EXTERNAL_PROCESS_LOCAL_CONNECTION_META_TEST_PASS_1 =
            "/andy_test_external_process_local_connection_meta_test_pass_1";
    private static final String EXTERNAL_PROCESS_LOCAL_CONNECTION_META_TEST_PASS_2 =
            "/andy_test_external_process_local_connection_meta_test_pass_2";

    private static final String INDEX_FILE_NAME = "/index.html";

    @BeforeAll
    static void copyFiles() throws IOException {
        final String tmp = getTempDirectory();

        createDirectoryAndHtmlFile(tmp, EXTERNAL_PROCESS_LOCAL_CONNECTION, "hello");
        createDirectoryAndHtmlFile(tmp, EXTERNAL_PROCESS_LOCAL_CONNECTION_META_TEST_PASS_1, "bye");
        createDirectoryAndHtmlFile(tmp, EXTERNAL_PROCESS_LOCAL_CONNECTION_META_TEST_PASS_2, "auf wiedersehen");
    }

    private static void createDirectoryAndHtmlFile(String tmp, String directory, String content) throws IOException {
        Files.createDirectories(Path.of(tmp + directory));
        Files.writeString(Path.of(tmp + directory + INDEX_FILE_NAME), content);
    }

    private static String getTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    @AfterAll
    static void cleanupFiles() throws IOException {
        final String tmp = getTempDirectory();
        deleteDirectoryAndHtmlFile(tmp, EXTERNAL_PROCESS_LOCAL_CONNECTION);
        deleteDirectoryAndHtmlFile(tmp, EXTERNAL_PROCESS_LOCAL_CONNECTION_META_TEST_PASS_1);
        deleteDirectoryAndHtmlFile(tmp, EXTERNAL_PROCESS_LOCAL_CONNECTION_META_TEST_PASS_2);
    }

    private static void deleteDirectoryAndHtmlFile(String tmp, String directory) throws IOException {
        Files.deleteIfExists(Path.of(tmp + directory + INDEX_FILE_NAME));
        Files.deleteIfExists(Path.of(tmp + directory));
    }

    @Test
    void localConnectionWithMetaTestsTest() {
        assertTimeoutPreemptively(ofSeconds(30), () -> {
            Result result = run(Action.FULL_WITH_HINTS, "EmptyLibrary", "ExternalProcessLocalConnectionSolution",
                    "ExternalProcessLocalConnectionWithMetaTestsConfiguration");

            assertThat(result.hasFailed()).isFalse();
            assertThat(result.hasGenericFailure()).isFalse();

            assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);
            assertThat(result.getMetaTests().getTotalTests()).isEqualTo(5);

            assertThat(result.getMetaTests())
                    .has(passedMetaTest("example of a passing meta test"))
                    .has(passedMetaTest("example of another passing meta test"))
                    .has(failedMetaTest("example of a failing meta test"));
        });
    }
}
