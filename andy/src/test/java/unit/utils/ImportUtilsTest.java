package unit.utils;

import nl.tudelft.cse1110.andy.utils.ImportUtils;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ImportUtilsTest {

    @Test
    void checkMissingImport_InDictionary() {
        String diagnostic = "cannot find symbol\n" +
                "  symbol:   class List\n" +
                "  location: class delft.ArrayUtilsTests";
        Optional<String> output = ImportUtils.checkMissingImport(diagnostic);
        assertThat(output).isPresent();
        assertThat(output.get()).isEqualTo("Maybe you missed the import for List?\n" +
                "Try adding this: import java.util.List;\n");
    }

    @Test
    void checkMissingImport_NotInDictionary() {
        String diagnostic = "cannot find symbol\n" +
                "  symbol:   class Tralala\n" +
                "  location: class delft.ArrayUtilsTests";

        Optional<String> output = ImportUtils.checkMissingImport(diagnostic);
        assertThat(output).isEmpty();
    }


    @Test
    void checkMissingImport_NoMatch() {
        String diagnostic = "cannot find symbol\n" +
                "   symbol:   method Indexof(java.lang.String)\n";

        Optional<String> output = ImportUtils.checkMissingImport(diagnostic);
        assertThat(output).isEmpty();
    }
}