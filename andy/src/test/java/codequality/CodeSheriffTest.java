package codequality;

import com.github.mauricioaniche.codesheriff.dsl.CodeSheriff;
import com.github.mauricioaniche.codesheriff.junit.CodeSheriffJUnit5;

public class CodeSheriffTest extends CodeSheriffJUnit5 {

    CodeSheriff complexity() {
        CodeSheriff sheriff = new CodeSheriff();

        sheriff.thatEnsures()
                .methods()
                .inClassesOfPackage("nl.tudelft.cse1110.andy")
                .have()
                .complexity(m -> m <= 10);

        return sheriff;
    }

    CodeSheriff loc() {
        CodeSheriff sheriff = new CodeSheriff();

        sheriff.thatEnsures()
                .methods()
                .inClassesOfPackage("nl.tudelft.cse1110.andy")
                .withExceptionOfMethod("execute/2") // the Step methods are a bit long now, we gotta refactor them
                .withExceptionOfMethod("getCoverageLineByLine/1") // it's long but extracting makes it harder to understand
                .have()
                .linesOfCode(m -> m <= 25);

        return sheriff;
    }

}
