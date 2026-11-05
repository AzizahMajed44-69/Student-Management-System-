package com.mycompany.project;

import java.io.Serializable;

/**
 * Student Model Class
 * Stores: ID, Name, Major, Score (average of grades)
 */
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private int id;
    private String major;
    private double score;

    public Student(String name, int id, String major, double score) {
        this.name = name;
        this.id = id;
        this.major = major;
        this.score = score;
    }

    public String getName()            { return name; }
    public void   setName(String name) { this.name = name; }

    public int  getId()        { return id; }
    public void setId(int id)  { this.id = id; }

    public String getMajor()               { return major; }
    public void   setMajor(String major)   { this.major = major; }

    public double getScore()               { return score; }
    public void   setScore(double score)   { this.score = score; }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name=" + name + ", major=" + major + ", score=" + score + "}";
    }
}
