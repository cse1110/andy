package nl.tudelft.cse1110.andy.execution.metatest.library.evaluators;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineReplacementEvaluator implements MetaEvaluator {

    private int start;
    private int end;
    private String replacement;

    public LineReplacementEvaluator(int start, int end, String replacement) {
        this.start = start - 1;
        this.end = end - 1;
        this.replacement = replacement;
    }

    @Override
    public String evaluate(String oldLibraryCode) {
        List<String> lines = oldLibraryCode.lines().collect(Collectors.toList());
        List<String> result = new ArrayList<>(lines.subList(0, this.start));
        result.add(this.replacement);
        result.addAll(lines.subList(this.end + 1, lines.size()));

        String resultStr = String.join("\n", result);
        return resultStr;
    }
}
