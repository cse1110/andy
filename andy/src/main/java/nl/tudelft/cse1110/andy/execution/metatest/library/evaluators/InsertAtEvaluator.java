package nl.tudelft.cse1110.andy.execution.metatest.library.evaluators;

import java.util.List;
import java.util.stream.Collectors;

public class InsertAtEvaluator implements MetaEvaluator {

    private final int lineToInsert;
    private final String contentToAdd;

    public InsertAtEvaluator(int lineToInsertStartingIn1, String contentToAdd) {
        this.lineToInsert = lineToInsertStartingIn1;
        this.contentToAdd = contentToAdd;
    }

    @Override
    public String evaluate(String oldLibraryCode) {
        List<String> lines = oldLibraryCode.lines().collect(Collectors.toList());

        // handles possible out of the boundary values
        int sanitizedLineToInsert = lineToInsert - 1;
        if (sanitizedLineToInsert < 0)
            sanitizedLineToInsert = 0;
        if (sanitizedLineToInsert > lines.size())
            sanitizedLineToInsert = lines.size();

        lines.add(sanitizedLineToInsert, contentToAdd);

        return lines.stream().collect(Collectors.joining("\n"));
    }
}
