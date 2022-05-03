package unit.writer.weblab;

import nl.tudelft.cse1110.andy.result.CompilationErrorInfo;
import nl.tudelft.cse1110.andy.result.CoverageLineByLine;
import nl.tudelft.cse1110.andy.result.CoverageResult;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import testutils.ResultTestDataBuilder;
import unit.writer.standard.StandardResultWriterTest;

import java.io.File;
import java.util.List;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.readFile;
import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.assertThat;
import static unit.writer.weblab.WebLabHighlightsJsonTestAssertions.*;
import static unit.writer.standard.StandardResultTestAssertions.finalGradeInXml;

public class WebLabResultWriterTest extends StandardResultWriterTest {

    @Override
    protected ResultWriter buildWriter() {
        return new WebLabResultWriter(versionInformation, asciiArtGenerator);
    }

    private String highlightsJson() {
        return readFile(new File(concatenateDirectories(reportDir.toString(), "highlights.json")));
    }

    @Test
    void reportCompilationErrorWithJson() {
        Result result = new ResultTestDataBuilder().withCompilationFail(
                new CompilationErrorInfo("Library.java", 10, "some compilation error"),
                new CompilationErrorInfo("Library.java", 11, "some other compilation error")
        ).build();

        writer.write(ctx, result);

        String highlightsJson = highlightsJson();

        assertThat(highlightsJson)
                .has(highlightCompilationError(10, "some compilation error"))
                .has(highlightCompilationError(11, "some other compilation error"));
    }

    @Test
    void testLineCoverageInHighlightedFile() {
        Result result = new ResultTestDataBuilder()
                .withCoverageResult(CoverageResult.build(
                        4, 7, 5, 8, 1, 2,
                        new CoverageLineByLine(
                                List.of(1, 2, 3, 7),
                                List.of(4),
                                List.of(5, 6)
                        )
                ))
                .build();

        writer.write(ctx, result);

        String highlightsJson = highlightsJson();

        assertThat(highlightsJson)
                .has(highlightLineFullyCovered(1))
                .has(highlightLineFullyCovered(2))
                .has(highlightLineFullyCovered(3))
                .has(highlightLineFullyCovered(7))
                .has(highlightLinePartiallyCovered(4))
                .has(highlightLineNotCovered(5))
                .has(highlightLineNotCovered(6));
    }

    @Override
    protected Condition<? super String> finalGradeOnScreen(int grade) {

        // grade is correct
        Condition<? super String> correctFinalGrade = super.finalGradeOnScreen(grade);

        // result xml contains the correct score
        Condition<String> xmlIsCorrect = finalGradeInXml(reportDir.toString(), grade);

        return allOf(correctFinalGrade, xmlIsCorrect);
    }

    protected Condition<? super String> finalGradeNotOnScreen(int grade) {
        // result xml contains the correct score
        Condition<String> xmlIsCorrect = finalGradeInXml(reportDir.toString(), grade);

        return xmlIsCorrect;
    }


}
