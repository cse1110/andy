package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.result.UnitTestsResult;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JUnitTestsTest {

    @Nested
    class TraditionalJUnitTests extends IntegrationTestBase {

        @Test
        void allTestsPassing() {

            Result result = run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass");

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(4);
            assertThat(result.getTests().getTestsRan()).isEqualTo(4);
            assertThat(result.getTests().getNumberOfFailingTests()).isEqualTo(0);
        }

        // In test 1 and 2,  30*3 should be 30+50
        // In test 3, 30+50 should be 30*3
        // example: student misinterpreted the source code.
        // 1/4 normal @Tests passing
        @Test
        void someTestsFailing() {

            Result result = run(Action.TESTS, "PlayerPointsLibrary", "PlayerPointsSomeTestsFail");

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(1);
            assertThat(result.getTests().getTestsRan()).isEqualTo(4);
            assertThat(result.getTests().getNumberOfFailingTests()).isEqualTo(3);
            assertThat(result.getTests()).has(failingTest("lessPoints()"));
            assertThat(result.getTests()).has(failingTest("manyPointsAndManyLives()"));
            assertThat(result.getTests()).has(failingTest("manyPointsButLittleLives()"));
        }

        // example: student forgets @Test
        // 0/0 normal @Tests passing
        @Test
        void noTests() {
            Result result = run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsNoTests");

            assertThat(result.getTests().getTestsRan()).isEqualTo(0);
            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(0);
        }

        //Check to see if the System.out.printlns are caught from the console
        @Test
        void showConsoleOutput(){

            Result result = run(Action.TESTS, "ZagZigLibrary", "ZagZigRandomSysouts");

            assertThat(result.getTests().getConsole())
                    .isNotEmpty()
                    .contains("RandomString1")
                    .contains("HelloWorld2")
                    .contains("I love SQT3");

        }

    }

    @Nested
    class Mockito extends IntegrationTestBase {

        @Test
        void mocksWork() {
            Result result = run(Action.TESTS, "SoftWhereLibrary", "SoftWhereTests");

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(3);
            assertThat(result.getTests().getTestsRan()).isEqualTo(3);
            assertThat(result.getTests().hasTestsFailingOrFailures()).isFalse();
        }

    }


    @Nested
    class ParameterizedTests extends IntegrationTestBase {

        @Test
        void parameterizedTestsPass() {
            Result result = run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution");

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(31);
            assertThat(result.getTests().getTestsRan()).isEqualTo(31);
            assertThat(result.getTests().hasTestsFailingOrFailures()).isFalse();
        }

        // in test cases 1 and 2 of "validTest", 22 and 44 should be numbers in (20, 200) divisible by 20.
        // in test case 1 of "invalidTest", 40 should not be divisible by 20.
        @Test
        void someParameterizedTestsFail() {

            Result result = run(Action.TESTS, "ATMLibrary", "ATMMoreParameterizedTestsFail");  // 11/14 parameterized test cases

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(11);
            assertThat(result.getTests().getTestsRan()).isEqualTo(14);
            assertThat(result.getTests().hasTestsFailingOrFailures()).isTrue();
            assertThat(result.getTests())
                    .has(failingParameterizedTest("validTest", 1))
                    .has(failingParameterizedTest("validTest", 2))
                    .has(failingParameterizedTest("invalidTest", 1));
        }

        @Test
        void helperMethodsCanBeUsed() {

            Result result = run(Action.TESTS, "PiecewiseLibrary", "PiecewiseHelperInTest");  // 26/26 parameterized test cases

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(26);
            assertThat(result.getTests().getTestsRan()).isEqualTo(26);
        }

    }

    @Nested
    class JQWik extends IntegrationTestBase {

        @Test
        void testSimplePropertyTest() {
            Result result = run(Action.TESTS, "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfSimpleJqwikError");

            assertThat(result.getTests())
                    .has(failingTest("testNoElementInWholeArray"));
        }


        @Test
        void testMultiplePropertyTestsFailing() {
            Result result = run(Action.TESTS, "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfMultipleJqwikErrors");

            assertThat(result.getTests())
                    .has(failingTest("testNoElementInWholeArray"))
                    .has(failingTest("testValueInArrayUniqueElements"));
        }


        @Test
        void testMultiplePropertyWithParameterizedTests() {
            Result result = run(Action.TESTS, "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJqwikWithParameterized");

            assertThat(result.getTests())
                    .has(failingTest("testNoElementInWholeArray"))
                    .has(failingTest("testValueInArrayUniqueElements"))
                    .has(failingParameterizedTest("test", 6));
        }


        @Test
        void testMessageOtherThanAssertionError() {
            Result result = run(Action.TESTS, "NumberUtilsAddPositiveLibrary", "NumberUtilsAddPositiveJqwikException");

            assertThat(result.getTests())
                    .has(failingTest("testAddition"));
        }

    }


    @Nested
    class MixedTypesOfTests extends IntegrationTestBase {

        // test class contains normal @Tests, parameterized tests and pbt.
        @Test
        void threeDifferentTestTypesUsed() {

            Result result = run(Action.TESTS, "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfDifferentTestTypes");  // 5/5 @Tests passing

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(5);
            assertThat(result.getTests().getTestsRan()).isEqualTo(5);
            assertThat(result.getTests().hasTestsFailingOrFailures()).isFalse();
        }
    }


    @Nested
    class GeneralMistakes extends IntegrationTestBase {

        // @BeforeAll methods should be static -> no tests detected
        @Test
        void jUnitCantRunIfFindsProblemsSuchAsBeforeAllIsNonStatic(){

            Result result = run(Action.TESTS, "PiecewiseLibrary", "PiecewiseNonStaticBeforeAll");

            assertThat(result.getTests().getTestsRan()).isEqualTo(0);
            assertThat(result.getTests().hasTestsFailingOrFailures()).isTrue();
            assertThat(result.getTests())
                    .has(failingTestWithMessage("PiecewiseTest", "@BeforeAll method"));
        }

        // error in @Test1: instead of completeTodo(), addTodo() should be invoked.
        @Test
        void mockitoAssertionFailing() {

            Result result = run(Action.TESTS, "TodoApplicationLibrary", "TodoApplicationMockitoMethodNotInvoked");  // 2/3 normal @Tests passing

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);
            assertThat(result.getTests().getTestsRan()).isEqualTo(3);
            assertThat(result.getTests().hasTestsFailingOrFailures()).isTrue();
            assertThat(result.getTests())
                    .has(failingTestWithMessage("addTodoTest()", "todoService.completeTodo"));
        }

        // error in @Test 3: student is misusing Mockito stubs in line 48: TheQueue q is not a mock, thus its methods cannot be stubbed!
        @Test
        void stubbingNonMockClass() {

            Result result = run(Action.TESTS, "TheQueueLibrary", "TheQueueMisusingMockitoStub");  // 2/3 normal @Tests passing

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);
            assertThat(result.getTests().getTestsRan()).isEqualTo(3);
            assertThat(result.getTests().hasTestsFailingOrFailures()).isTrue();
            assertThat(result.getTests())
                    .has(failingTestWithMessage("getNextReturnsFirst()", "mockito.exceptions.misusing"));
        }

        // student reversed the 2 method names "invalidInputs" and "validInputs",
        //  raising exceptions since the number of arguments passed is incorrect, and thus making all tests fail.
        // in "invalidInputs", an exception is expected, whereas this is not being thrown by the method.
        // in "validInputs", exceptions are raised, whereas this is not expected.
        @Test
        void exceptionThrownByTest() {

            Result result = run(Action.TESTS, "MScAdmissionLibrary", "MScAdmissionParameterizedTestThrowsException");  // 0/5 parameterized test cases

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(0);
            assertThat(result.getTests().getTestsRan()).isEqualTo(5);
            assertThat(result.getTests().hasTestsFailingOrFailures()).isTrue();
            assertThat(result.getTests())
                    .has(failingParameterizedTest("validInputs", 1))
                    .has(failingParameterizedTest("validInputs", 2))
                    .has(failingParameterizedTest("invalidInputs", 1))
                    .has(failingParameterizedTest("invalidInputs", 2))
                    .has(failingParameterizedTest("invalidInputs", 3));
        }

        // @MethodSource method should be static -> test failure -> 2/2 pass (but in GitHub this is 0/2?)
        @Test
        void nonStaticMethodSourceSomeFail() {

            Result result = run(Action.TESTS, "MScAdmissionLibrary", "MScAdmissionNonStaticMethodSourceSomeFail");

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);
            assertThat(result.getTests().getTestsRan()).isEqualTo(2);
            assertThat(result.getTests().hasTestsFailingOrFailures()).isTrue();
            assertThat(result.getTests()).has(failingTest("validInputs(int, double, boolean)"));
        }


        // @MethodSource method should be static -> test failure
        @Test
        void nonStaticMethodSourceAllFail() {

            Result result = run(Action.TESTS, "MScAdmissionLibrary", "MScAdmissionNonStaticMethodSourceAllFail");

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(0);
            assertThat(result.getTests().getTestsRan()).isEqualTo(0);
            assertThat(result.getTests().hasTestsFailingOrFailures()).isTrue();
            assertThat(result.getTests())
                    .has(failingTestWithMessage("validInputs(int, double, boolean)", "is static"))
                    .has(failingTestWithMessage("invalidInputs(int, double)", "is static"));
        }

        // Student forgot @ParameterizedTest -> no tests detected
        @Test
        void forgotParameterizedTestAnnotation() {

            Result result = run(Action.TESTS, "PassingGradeLibrary", "PassingGradeForgotParameterizedTestAnnotation");

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(0);
            assertThat(result.getTests().getTestsRan()).isEqualTo(0);
        }

        // Student forgot @MethodSource -> no tests detected
        @Test
        void forgotMethodSourceAnnotationAllFail() {

            Result result = run(Action.TESTS, "PassingGradeLibrary", "PassingGradeForgotMethodSourceAnnotationAllFail");

            assertThat(result.getTests().hasTestsFailingOrFailures()).isTrue();
            assertThat(result.getTests()).has(failingTestWithMessage("passed(float, boolean)", "Make sure you have provided a @MethodSource for this @ParameterizedTest"));
        }

        // Student forgot @MethodSource -> 3/3 tests pass
        @Test
        void forgotMethodSourceAnnotationSomeFail() {

            Result result = run(Action.TESTS, "MScAdmissionLibrary", "MScAdmissionForgotMethodSourceSomeFail");

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(3);
            assertThat(result.getTests().getTestsRan()).isEqualTo(3);
            assertThat(result.getTests().hasTestsFailingOrFailures()).isTrue();
            assertThat(result.getTests())
                    .has(failingTestWithMessage("invalidInputs(int, double)", "Make sure you have provided a @MethodSource for this @ParameterizedTest!"));
        }

        // Student forgot @Property -> no tests detected... Nothing we can do here.
        @Test
        void forgotPropertyAnnotation() {

            Result result = run(Action.TESTS, "MathArraysLibrary", "MathArraysForgotProperty");

            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(0);
            assertThat(result.getTests().getTestsRan()).isEqualTo(0);
            assertThat(result.getTests().hasTestsFailingOrFailures()).isFalse();
        }

    }

    private static Condition<UnitTestsResult> failingTest(String nameOfTheTest) {
        return new Condition<>() {
            @Override
            public boolean matches(UnitTestsResult value) {
                return value.getFailures().stream()
                        .anyMatch(t -> t.getTestCase().equals(nameOfTheTest));
            }
        };
    }

    private static Condition<UnitTestsResult> failingTestWithMessage(String nameOfTheTest, String partOfTheMessage) {
        return new Condition<>() {
            @Override
            public boolean matches(UnitTestsResult value) {
                return value.getFailures().stream()
                        .anyMatch(t -> t.getTestCase().equals(nameOfTheTest) && t.getMessage().contains(partOfTheMessage));
            }
        };
    }

    private Condition<UnitTestsResult> failingParameterizedTest(String testName, int number) {
        return new Condition<>() {
            @Override
            public boolean matches(UnitTestsResult value) {
                return
                        value.getFailures().stream()
                                .anyMatch(t -> t.getTestCase().equals(String.format("%s (%d)", testName, number)));
            }
        };
    }

}
