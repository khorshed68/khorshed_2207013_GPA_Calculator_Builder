package com.example.khorshed_2207013_gpa_calculator_builder;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {
    private final StringProperty courseName = new SimpleStringProperty();
    private final StringProperty courseCode = new SimpleStringProperty();
    private final DoubleProperty courseCredit = new SimpleDoubleProperty();
    private final StringProperty teacherOne = new SimpleStringProperty();
    private final StringProperty teacherTwo = new SimpleStringProperty();
    private final StringProperty grade = new SimpleStringProperty();
    private final DoubleProperty gradePoint = new SimpleDoubleProperty();
    private final ReadOnlyDoubleWrapper qualityPoints = new ReadOnlyDoubleWrapper();

    public Course(String courseName,
                  String courseCode,
                  double courseCredit,
                  String teacherOne,
                  String teacherTwo,
                  String grade,
                  double gradePoint) {
        this.courseName.set(courseName);
        this.courseCode.set(courseCode);
        this.courseCredit.set(courseCredit);
        this.teacherOne.set(teacherOne);
        this.teacherTwo.set(teacherTwo);
        this.grade.set(grade);
        this.gradePoint.set(gradePoint);
        this.qualityPoints.bind(this.gradePoint.multiply(this.courseCredit));
    }

    public String getCourseName() {
        return courseName.get();
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode.get();
    }

    public StringProperty courseCodeProperty() {
        return courseCode;
    }

    public double getCourseCredit() {
        return courseCredit.get();
    }

    public DoubleProperty courseCreditProperty() {
        return courseCredit;
    }

    public String getTeacherOne() {
        return teacherOne.get();
    }

    public StringProperty teacherOneProperty() {
        return teacherOne;
    }

    public String getTeacherTwo() {
        return teacherTwo.get();
    }

    public StringProperty teacherTwoProperty() {
        return teacherTwo;
    }

    public String getGrade() {
        return grade.get();
    }

    public StringProperty gradeProperty() {
        return grade;
    }

    public double getGradePoint() {
        return gradePoint.get();
    }

    public DoubleProperty gradePointProperty() {
        return gradePoint;
    }

    public double getQualityPoints() {
        return qualityPoints.get();
    }

    public ReadOnlyDoubleProperty qualityPointsProperty() {
        return qualityPoints.getReadOnlyProperty();
    }
}
