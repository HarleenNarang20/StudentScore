package com.dani.makersinstitute.studentscore.model;

/**
 * Created by dani@taufani.com on 1/25/17.
 */
public class ScoreModel {
    Integer id;
    String subjectName;
    Double subjectScore;

    public ScoreModel() {
    }

    public ScoreModel(int id, String subjectName, Double subjectScore) {
        this.id = id;
        this.subjectName = subjectName;
        this.subjectScore = subjectScore;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Double getSubjectScore() {
        return subjectScore;
    }

    public void setSubjectScore(Double subjectScore) {
        this.subjectScore = subjectScore;
    }
}
