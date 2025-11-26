package com.example.khorshed_2207013_gpa_calculator_builder;

import com.example.khorshed_2207013_gpa_calculator_builder.SceneNavigator;
import com.example.khorshed_2207013_gpa_calculator_builder.Course;
import com.example.khorshed_2207013_gpa_calculator_builder.GpaSummary;
import com.example.khorshed_2207013_gpa_calculator_builder.Student;
import com.example.khorshed_2207013_gpa_calculator_builder.StudentRepository;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;

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
    private String studentName;
    private String studentId;
    private String semester;
    private final StudentRepository studentRepository = new StudentRepository();

    public void setData(GpaSummary summary, String studentName, String studentId, String semester) {
        this.summary = summary;
        this.studentName = studentName;
        this.studentId = studentId;
        this.semester = semester;
        if (gpaValueLabel != null) {
            populateSummary();
        }
    }

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

    @FXML
    private void handleSaveRecord() {
        if (studentName == null || studentName.trim().isEmpty()) {
            showError("Validation Error", "Please enter student name before saving.");
            return;
        }
        if (studentId == null || studentId.trim().isEmpty()) {
            showError("Validation Error", "Please enter student ID before saving.");
            return;
        }
        if (semester == null || semester.trim().isEmpty()) {
            showError("Validation Error", "Please enter semester before saving.");
            return;
        }

        Student student = new Student();
        student.setStudentName(studentName);
        student.setStudentId(studentId);
        student.setSemester(semester);
        student.setTotalCredits(summary.getTotalCredits());
        student.setCgpa(summary.getGpa());
        student.setTargetCredits(summary.getTargetCredits());
        student.setCreatedAt(LocalDateTime.now());

        Task<Void> saveTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                studentRepository.insertStudent(student, summary.getCourses());
                return null;
            }
        };

        saveTask.setOnSucceeded(event -> {
            showSuccess("Record Saved", "Student record has been saved successfully!");
        });

        saveTask.setOnFailed(event -> {
            Throwable ex = saveTask.getException();
            showError("Save Failed", "Failed to save record: " + (ex != null ? ex.getMessage() : "Unknown error"));
        });

        new Thread(saveTask).start();
    }

    @FXML
    private void handleViewSaved() {
        SceneNavigator.showSavedRecords();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
