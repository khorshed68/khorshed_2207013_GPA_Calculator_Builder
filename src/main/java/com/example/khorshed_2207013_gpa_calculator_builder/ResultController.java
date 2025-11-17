package com.example.khorshed_2207013_gpa_calculator_builder;

import com.example.khorshed_2207013_gpa_calculator_builder.SceneNavigator;
import com.example.khorshed_2207013_gpa_calculator_builder.Course;
import com.example.khorshed_2207013_gpa_calculator_builder.GpaSummary;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ResultController {

    @FXML
    private Label gpaValueLabel;
    @FXML
    private Label totalCreditsLabel;
    @FXML
    private Label targetCreditsLabel;
    @FXML
    private VBox courseListContainer;

    private GpaSummary summary;

    public void setSummary(GpaSummary summary) {
        this.summary = summary;
        if (gpaValueLabel != null) {
            populateSummary();
        }
    }

    @FXML
    private void initialize() {
        if (summary != null) {
            populateSummary();
        }
    }

    private void populateSummary() {
        gpaValueLabel.setText(String.format("%.2f", summary.getGpa()));
        totalCreditsLabel.setText(String.format("%.1f credits", summary.getTotalCredits()));
        targetCreditsLabel.setText(String.format("Target: %d credits", summary.getTargetCredits()));
        renderCourseRows();
    }

    private void renderCourseRows() {
        courseListContainer.getChildren().clear();
        int index = 1;
        for (Course course : summary.getCourses()) {
            courseListContainer.getChildren().add(buildRow(index++, course));
        }
    }

    private GridPane buildRow(int index, Course course) {
        GridPane row = new GridPane();
        row.getStyleClass().add("course-row");
        row.setHgap(12);
        row.setVgap(4);

        Label header = new Label(index + ". " + course.getCourseName() + " (" + course.getCourseCode() + ")");
        header.getStyleClass().add("course-row-title");
        row.add(header, 0, 0, 2, 1);

        row.add(createCaptionValue("Credits", String.format("%.1f", course.getCourseCredit())), 0, 1);
        row.add(createCaptionValue("Grade", course.getGrade()), 1, 1);
        row.add(createCaptionValue("Teacher 1", course.getTeacherOne()), 0, 2);
        row.add(createCaptionValue("Teacher 2", course.getTeacherTwo()), 1, 2);
        return row;
    }

    private VBox createCaptionValue(String caption, String value) {
        Label captionLabel = new Label(caption);
        captionLabel.getStyleClass().add("course-caption");
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("course-value");
        VBox box = new VBox(2, captionLabel, valueLabel);
        return box;
    }

    @FXML
    private void handleBackToEntry() {
        SceneNavigator.showCalculator();
    }

    @FXML
    private void handleBackHome() {
        SceneNavigator.showHome();
    }
}
