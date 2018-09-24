package Kefel;

public class AnswerDetails {

    private int secondsDiff = 0;
    private boolean falseAnswer = false;
    private int grade = 0;
    private int gradeAll = 0;


//    /**
//     * Constructor-1
//     * @param secondsDiff int
//     * @param falseAnswer boolean
//     * @param grade int
//     * @param gradeAll int
//     **/
    public AnswerDetails(int secondsDiff, boolean falseAnswer, int grade,
                         int gradeAll) {//throws CouponException {

        this.secondsDiff = secondsDiff;
        this.falseAnswer = falseAnswer;
        this.grade = grade;
        this.gradeAll = gradeAll;
    }

    public AnswerDetails() {

    }

    public int getSecondsDiff() {
        return secondsDiff;
    }

    public void setSecondsDiff(int secondsDiff) {
        this.secondsDiff = secondsDiff;
    }

    public boolean getFalseAnswer() {
        return falseAnswer;
    }

    public void setFalseAnswer(boolean falseAnswer) {
        this.falseAnswer = falseAnswer;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getGradeAll() {
        return gradeAll;
    }

    public void setGradeAll(int gradeAll) {
        this.gradeAll = gradeAll;
    }



    @Override
    public String toString() {
        return "AnswerDetails [secondsDiff=" + secondsDiff + ", falseAnswer=" + falseAnswer +
                ", grade=" + grade + ", gradeAll=" + gradeAll  + "]\n";
    }
}
