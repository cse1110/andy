package nl.tudelft.cse1110.andy.writer.weblab;

public class SubmissionMetaData {
    private final String course;
    private final String studentId;
    private final String exercise;

    public SubmissionMetaData(String course, String studentId, String exercise) {
        this.course = course;
        this.studentId = studentId;
        this.exercise = exercise;
    }

    public static SubmissionMetaData empty() {
        return new SubmissionMetaData(null, null, null);
    }
}