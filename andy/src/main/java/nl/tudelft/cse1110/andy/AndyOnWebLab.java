package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.writer.weblab.SubmissionMetaData;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AndyOnWebLab {

    public static void main(String[] args) {
        if(args.length != 6) {
            System.out.println("Andy needs six parameters: <ACTION/TASK_MODE> <WORK DIRECTORY> <OUTPUT DIRECTORY> " +
                               "<WL_COURSE> <WL_STUDENT> <WL_ASSIGNMENT_TITLE>");
            System.exit(-1);
        }

        String action = args[0];
        String workDir = args[1];
        String outputDir = args[2];

        if (action == null) { System.out.println("No ACTION environment variable.");      System.exit(-1); }
        if (workDir == null) { System.out.println("No WORKING_DIR environment variable."); System.exit(-1); }
        if (outputDir == null) { System.out.println("No OUTPUT_DIR environment variable.");  System.exit(-1); }

        WebLabResultWriter writer = new WebLabResultWriter();
        SubmissionMetaData metaData = getSubmissionMetaData(args, 3);
        new Andy(getAction(action), workDir, outputDir, writer, metaData).run();
    }

    private static Action getAction(String action) {
        try {
            return Action.valueOf(action);
        } catch(IllegalArgumentException e) {
            System.out.println("Invalid action/task mode: " + action);
            System.out.println("Valid ones: " + Arrays.stream(Action.values()).map(a -> a.toString()).collect(Collectors.joining(",")));
            System.exit(-1);
            return null;
        }
    }

    private static SubmissionMetaData getSubmissionMetaData(String[] args, int start) {
        String course = args[start];
        String studentId = args[start + 1];
        String exercise = args[start + 2];

        if (course == null) {
            System.out.println("No WL_COURSE environment variable.");
            System.exit(-1);
        }
        if (studentId == null) {
            System.out.println("No WL_STUDENT environment variable.");
            System.exit(-1);
        }
        if (exercise == null) {
            System.out.println("No WL_ASSIGNMENT_TITLE environment variable.");
            System.exit(-1);
        }

        SubmissionMetaData metaData = new SubmissionMetaData(course, studentId, exercise);
        return metaData;
    }

}
