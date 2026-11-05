package com.mycompany.project;

import java.io.Serializable;

/**
 * Grade Model Class
 * Stores individual subject grades for a student
 * Used by AddGradesController (write) and ViewStudentsController (read)
 */
public class Grade implements Serializable {

    private static final long serialVersionUID = 1L;

    private int studentId;
    private double grade1;
    private double grade2;
    private double grade3;
    private double average;

    public Grade(int studentId, double grade1, double grade2, double grade3) {
        this.studentId = studentId;
        this.grade1    = grade1;
        this.grade2    = grade2;
        this.grade3    = grade3;
        this.average   = (grade1 + grade2 + grade3) / 3.0;
    }

    public int    getStudentId() { return studentId; }
    public double getGrade1()    { return grade1; }
    public double getGrade2()    { return grade2; }
    public double getGrade3()    { return grade3; }
    public double getAverage()   { return average; }

    public void setGrades(double grade1, double grade2, double grade3) {
        this.grade1  = grade1;
        this.grade2  = grade2;
        this.grade3  = grade3;
        this.average = (grade1 + grade2 + grade3) / 3.0;
    }

    @Override
    public String toString() {
        return "Grade{studentId=" + studentId +
               ", g1=" + grade1 + ", g2=" + grade2 + ", g3=" + grade3 +
               ", avg=" + String.format("%.2f", average) + "}";
    }
}
