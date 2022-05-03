package delft;


class MScAdmission {

    public boolean admit(int act, double gpa) {

        // Dependency between act / gpa: they have a joint effect


        if (act < 0 || act > 36) {
            throw new RuntimeException("ACT has to be between [0,36]");
        }

        if (gpa < 0 || gpa > 4.0) {
            throw new RuntimeException("GPA has to be between [0, 4.0]");
        }


        if(act==36 && gpa >= 3.5)
            return true;
        if(act>=35 && gpa >= 3.6)
            return true;
        if(act >= 34 && gpa >= 3.7)
            return true;
        if(act >= 33 && gpa >= 3.8)
            return true;
        if(act >= 32 && gpa >= 3.9)
            return true;
        if(act >= 31 && gpa == 4.0)
            return true;

        return false;
    }
}
