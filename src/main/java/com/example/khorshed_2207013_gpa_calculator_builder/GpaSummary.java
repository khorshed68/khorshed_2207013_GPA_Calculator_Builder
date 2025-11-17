package com.example.khorshed_2207013_gpa_calculator_builder;

import java.util.List;

public class GpaSummary {
    private final List<Course> courses;
    private final double totalCredits;
    private final double gpa;
    private final int targetCredits;

    public GpaSummary(List<Course> courses,
                      double totalCredits,
                      double gpa,
                      int targetCredits) {
        this.courses = List.copyOf(courses);
        this.totalCredits = totalCredits;
        this.gpa = gpa;
        this.targetCredits = targetCredits;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public double getTotalCredits() {
        return totalCredits;
    }

    public double getGpa() {
        return gpa;
    }

    public int getTargetCredits() {
        return targetCredits;
    }
}
