package nl.tudelft.cse1110.andy.writer.weblab;

import com.google.gson.Gson;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.result.*;
import nl.tudelft.cse1110.andy.writer.standard.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.standard.StandardResultWriter;
import nl.tudelft.cse1110.andy.writer.standard.VersionInformation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.writeToFile;

public class WebLabResultWriter extends StandardResultWriter {

    public WebLabResultWriter(VersionInformation versionInformation, RandomAsciiArtGenerator asciiArtGenerator) {
        super(versionInformation, asciiArtGenerator);
    }

    public WebLabResultWriter() {
        super();
    }

    @Override
    public void write(Context ctx, Result result) {

        super.write(ctx, result);

        writeResultsXmlFile(ctx, result);
        writeHighlightsJson(ctx, result);
        writeAnalyticsFile(ctx, result);
    }

    private void writeResultsXmlFile(Context ctx, Result result) {
        String xml = buildResultsXml(result);

        File resultsXml = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "results.xml"));
        writeToFile(resultsXml, xml);
    }

    private String buildResultsXml(Result result) {
        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n\t<testsuite>\n");
        int score = result.getFinalGrade();
        String failed = "\t\t<testcase><failure></failure></testcase>\n";
        String passed = "\t\t<testcase/>\n";

        xml.append(failed.repeat(100 - score));
        xml.append(passed.repeat(score));

        xml.append("\t</testsuite>\n</testsuites>\n");
        return xml.toString();
    }


    private void writeHighlightsJson(Context ctx, Result result){

        List<Highlight> highlights = buildHighlights(result);
        String json = new Gson().toJson(highlights);

        File highlightsJson = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "highlights.json"));
        writeToFile(highlightsJson, json);
    }

    private List<Highlight> buildHighlights(Result result) {
        List<Highlight> highlights = new ArrayList<>();

        // coverage
        for(int line : result.getCoverage().getFullyCoveredLines()) {
            highlights.add(new Highlight(line, "100% coverage",
                    Highlight.HighlightLocation.LIBRARY, Highlight.HighlightPurpose.FULL_COVERAGE));
        }

        for(int line : result.getCoverage().getPartiallyCoveredLines()) {
            highlights.add(new Highlight(line, "Partial coverage",
                    Highlight.HighlightLocation.LIBRARY, Highlight.HighlightPurpose.PARTIAL_COVERAGE));
        }

        for(int line : result.getCoverage().getNotCoveredLines()) {
            highlights.add(new Highlight(line, "No coverage",
                    Highlight.HighlightLocation.LIBRARY, Highlight.HighlightPurpose.NO_COVERAGE));
        }

        // compilation error
        for (CompilationErrorInfo error : result.getCompilation().getErrors()) {
            highlights.add(new Highlight(error.getLineNumber(), error.getMessage(), Highlight.HighlightLocation.SOLUTION, Highlight.HighlightPurpose.COMPILATION_ERROR));
        }

        return highlights;
    }

    private void writeAnalyticsFile(Context ctx, Result result) {
        if(ctx.getModeActionSelector()==null || !ctx.getModeActionSelector().shouldGenerateAnalytics())
            return;

        Submission submission = new Submission(
                ctx.getAction(),
                ctx.getSubmissionMetaData(),
                result
        );

        String json = new Gson().toJson(submission);

        File file = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "post.json"));
        writeToFile(file, json);
    }



}
