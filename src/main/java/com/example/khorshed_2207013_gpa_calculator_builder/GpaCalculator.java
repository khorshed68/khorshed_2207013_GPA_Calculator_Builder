package com.example.khorshed_2207013_gpa_calculator_builder;

import com.example.khorshed_2207013_gpa_calculator_builder.Course;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class GpaCalculator {
    private static final Map<String, Double> GRADE_POINTS = new LinkedHashMap<>();

    static {
        GRADE_POINTS.put("A+", 4.00);
        GRADE_POINTS.put("A", 4.75);
        GRADE_POINTS.put("A-", 3.50);
        GRADE_POINTS.put("B+", 3.25);
        GRADE_POINTS.put("B", 3.00);
        GRADE_POINTS.put("B-", 2.75);
        GRADE_POINTS.put("C+", 2.50);
        GRADE_POINTS.put("C", 2.25);
        GRADE_POINTS.put("D", 2.00);
        GRADE_POINTS.put("F", 0.00);
    }

    private GpaCalculator() {
    }

    public static List<String> gradeOptions() {
        return List.copyOf(GRADE_POINTS.keySet());
    }

    public static double gradePointFor(String grade) {
        return GRADE_POINTS.getOrDefault(grade, 0.0);
    }

    public static double totalCredits(List<Course> courses) {
        return courses.stream().mapToDouble(Course::getCourseCredit).sum();
    }

    public static double totalQualityPoints(List<Course> courses) {
        return courses.stream().mapToDouble(Course::getQualityPoints).sum();
    }

    public static double gpa(List<Course> courses) {
        double credits = totalCredits(courses);
        if (credits == 0) {
            return 0.0;
        }
        return totalQualityPoints(courses) / credits;
    }
}
