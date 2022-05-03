package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class SecurityTest extends IntegrationTestBase {

    @DisabledOnOs(OS.WINDOWS) // Disable on Windows as Runtime#getExec is not well-received by Windows Defender.
    @ParameterizedTest
    @MethodSource("securityTestGeneratorAdvanced")
    void securityTestNonWindows(String exploitFile, String expectedMessage) {
        securityTest(exploitFile, expectedMessage);
    }

    @EnabledOnOs(OS.WINDOWS) // Enable on Windows only, tests everything except Runtime#getExec.
    @ParameterizedTest
    @MethodSource("securityTestGeneratorBasic")
    void securityTestWindows(String exploitFile, String expectedMessage) {
        securityTest(exploitFile, expectedMessage);
    }

    void securityTest(String exploitFile, String expectedMessage) {
        // Provide working directory path to user code for testing purposes
        System.setProperty("andy.securitytest.workdir", workDir.getAbsolutePath());

        Result result = run(Action.TESTS, "EmptyLibrary", "securitytests/" + exploitFile, "EmptyConfiguration");

        assertThat(result.getTests().getFailures().get(0).getMessage())
                .startsWith(SecurityException.class.getName())
                .contains("Operation not permitted")
                .contains(expectedMessage);
    }

    @Test
    void staticBlock() {
        Result result = run(Action.FULL_WITHOUT_HINTS, "EmptyLibrary", "securitytests/StaticBlock", "EmptyConfiguration");

        assertThat(result.getTests().getFailures().get(0).getMessage())
                .startsWith(ExceptionInInitializerError.class.getName());
    }

    @ParameterizedTest
    @CsvSource({
            "OtherPackageName,package name of your solution",
            "InstantiateConfiguration,Accessing the task configuration",
            "UseReflection,Using reflection"
    })
    void failingSecurityCheck(String exploitFile, String expectedMessage) {
        Result result = run(Action.FULL_WITHOUT_HINTS, "EmptyLibrary", "securitytests/"+exploitFile, "EmptyConfiguration");

        assertThat(result.getCompilation().successful()).isFalse();
        assertThat(result.getCompilation().getErrors())
                .hasSize(1)
                .allMatch(err -> err.getMessage().contains(expectedMessage));
    }

    static Stream<Arguments> securityTestGeneratorAdvanced() {
        return Stream.concat(securityTestGeneratorBasic(), Stream.of(Arguments.arguments("RuntimeExec", "actions=execute")));
    }

    static Stream<Arguments> securityTestGeneratorBasic() {
        return Stream.of(
            Arguments.arguments("WriteResultsXml", "name=results.xml actions=write"),
            Arguments.arguments("SystemExit", "name=exitVM."),
            Arguments.arguments("SetProperty", "test actions=write"),
            Arguments.arguments("ReadSource", "ExploitTest.java actions=read"),
            Arguments.arguments("ReadClass", "ExploitTest$1.class actions=read")
        );
    }
}
