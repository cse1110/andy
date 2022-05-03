package delft;

class PassingGrade {

    public boolean passed(float grade) {
        // this line is not needed since we are allowed to assume
        // that no invalid grade can be passed as a param
        if (grade < 1 || grade > 10) throw new IllegalArgumentException();

        return grade >= 5;
    }

}
