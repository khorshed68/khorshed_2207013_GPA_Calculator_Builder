package com.example.khorshed_2207013_gpa_calculator_builder;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Student {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty studentName = new SimpleStringProperty();
    private final StringProperty studentId = new SimpleStringProperty();
    private final StringProperty semester = new SimpleStringProperty();
    private final DoubleProperty totalCredits = new SimpleDoubleProperty();
    private final DoubleProperty cgpa = new SimpleDoubleProperty();
    private final IntegerProperty targetCredits = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();

    public Student() {
    }

    public Student(int id, String studentName, String studentId, String semester,
                   double totalCredits, double cgpa, int targetCredits, LocalDateTime createdAt) {
        this.id.set(id);
        this.studentName.set(studentName);
        this.studentId.set(studentId);
        this.semester.set(semester);
        this.totalCredits.set(totalCredits);
        this.cgpa.set(cgpa);
        this.targetCredits.set(targetCredits);
        this.createdAt.set(createdAt);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getStudentName() {
        return studentName.get();
    }

    public StringProperty studentNameProperty() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName.set(studentName);
    }

    public String getStudentId() {
        return studentId.get();
    }

    public StringProperty studentIdProperty() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId.set(studentId);
    }

    public String getSemester() {
        return semester.get();
    }

    public StringProperty semesterProperty() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester.set(semester);
    }

    public double getTotalCredits() {
        return totalCredits.get();
    }

    public DoubleProperty totalCreditsProperty() {
        return totalCredits;
    }

    public void setTotalCredits(double totalCredits) {
        this.totalCredits.set(totalCredits);
    }

    public double getCgpa() {
        return cgpa.get();
    }

    public DoubleProperty cgpaProperty() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa.set(cgpa);
    }

    public int getTargetCredits() {
        return targetCredits.get();
    }

    public IntegerProperty targetCreditsProperty() {
        return targetCredits;
    }

    public void setTargetCredits(int targetCredits) {
        this.targetCredits.set(targetCredits);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt.get();
    }

    public ObjectProperty<LocalDateTime> createdAtProperty() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt.set(createdAt);
    }
}
