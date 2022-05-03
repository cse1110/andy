package unit.utils;

import nl.tudelft.cse1110.andy.utils.ClassUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;

public class ClassUtilsTest {


    @Test
    void testExtractPackageName() {

        String sourceCode = "package tudelft.domain;\n" +
                "\n" +
                "class LeapYear {\n" +
                "\n" +
                "    public boolean isLeapYear(int year) {\n" +
                "        if ( year % 400 == 0)\n" +
                "            return true;\n" +
                "        if ( year % 100 == 0)\n" +
                "            return false;\n" +
                "        return year % 4 == 0;\n" +
                "    }\n" +
                "}\n";

        String result = ClassUtils.extractPackageName(sourceCode);

        assertThat(result).isEqualTo("tudelft.domain");
    }

    @Test
    void testNoPackageNameFound() {

        String sourceCode =  "\n" +
                "class LeapYear {\n" +
                "\n" +
                "    public boolean isLeapYear(int year) {\n" +
                "        if ( year % 400 == 0)\n" +
                "            return true;\n" +
                "        if ( year % 100 == 0)\n" +
                "            return false;\n" +
                "        return year % 4 == 0;\n" +
                "    }\n" +
                "}\n";

        assertThrows(RuntimeException.class,
                () -> ClassUtils.extractPackageName(sourceCode));
    }

    @ParameterizedTest
    @CsvSource({
            "A,A.class",
            "a.B,a/B.class",
            "a.b.C,a/b/C.class",
            "a.C.D,a/C$D.class",
            "a.b.C.D,a/b/C$D.class"
    })
    void clazzName(String input, String output) {
        assertThat(ClassUtils.clazzNameAsPath(input))
                .isEqualTo(output);
    }




}
