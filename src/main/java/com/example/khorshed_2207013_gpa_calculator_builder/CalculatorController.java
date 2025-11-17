package com.example.khorshed_2207013_gpa_calculator_builder;

import com.example.khorshed_2207013_gpa_calculator_builder.SceneNavigator;
import com.example.khorshed_2207013_gpa_calculator_builder.Course;
import com.example.khorshed_2207013_gpa_calculator_builder.GpaSummary;
import com.example.khorshed_2207013_gpa_calculator_builder.GpaCalculator;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class CalculatorController {

    @FXML
    private TextField courseNameField;
    @FXML
    private TextField courseCodeField;
    @FXML
    private TextField courseCreditField;
    @FXML
    private TextField teacherOneField;
    @FXML
    private TextField teacherTwoField;
    @FXML
    private ComboBox<String> gradeComboBox;
    @FXML
    private Spinner<Integer> targetCreditsSpinner;
    @FXML
    private TableView<Course> courseTable;
    @FXML
    private TableColumn<Course, String> nameColumn;
    @FXML
    private TableColumn<Course, String> codeColumn;
    @FXML
    private TableColumn<Course, Number> creditColumn;
    @FXML
    private TableColumn<Course, String> teacherOneColumn;
    @FXML
    private TableColumn<Course, String> teacherTwoColumn;
    @FXML
    private TableColumn<Course, String> gradeColumn;
    @FXML
    private Button deleteCourseButton;
    @FXML
    private Button calculateButton;
    @FXML
    private Text totalCreditsText;
    @FXML
    private Text gpaPreviewText;
    @FXML
    private Text creditStatusText;

    private final ObservableList<Course> courses = FXCollections.observableArrayList();
    private final DoubleProperty totalCredits = new SimpleDoubleProperty(0);

    @FXML
    private void initialize() {
        setupGradeCombo();
        setupTargetCreditsSpinner();
        setupTable();
        bindSummaryLabels();
        deleteCourseButton.disableProperty().bind(courseTable.getSelectionModel().selectedItemProperty().isNull());
        calculateButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> {
                    Integer spinnerValue = targetCreditsSpinner.getValue();
                    double target = spinnerValue == null ? 0.0 : spinnerValue;
                    return Math.abs(totalCredits.get() - target) > 0.001;
                },
                totalCredits, targetCreditsSpinner.valueProperty()));
    }

    private void setupGradeCombo() {
        gradeComboBox.setItems(FXCollections.observableArrayList(GpaCalculator.gradeOptions()));
        gradeComboBox.getSelectionModel().selectFirst();
    }

    private void setupTargetCreditsSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(9, 30, 15, 1);
        targetCreditsSpinner.setValueFactory(valueFactory);
        targetCreditsSpinner.valueProperty().addListener((obs, oldValue, newValue) -> updateCreditStatus());
    }

    private void setupTable() {
        courseTable.setItems(courses);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        teacherOneColumn.setCellValueFactory(new PropertyValueFactory<>("teacherOne"));
        teacherTwoColumn.setCellValueFactory(new PropertyValueFactory<>("teacherone"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
    }

    private void bindSummaryLabels() {
        totalCreditsText.textProperty().bind(totalCredits.asString("%.1f"));
        gpaPreviewText.textProperty().bind(Bindings.createStringBinding(
                () -> String.format("%.2f", courses.isEmpty() ? 0.0 : GpaCalculator.gpa(courses)),
                courses));
        updateCreditStatus();
    }

    private void updateCreditStatus() {
        Integer spinnerValue = targetCreditsSpinner.getValue();
        int targetValue = spinnerValue == null ? 0 : spinnerValue;
        creditStatusText.setText(String.format("Credits Entered: %.1f / %d",
                totalCredits.get(), targetValue));
    }

    @FXML
    private void handleAddCourse() {
        if (!validateInputs()) {
            return;
        }
        double creditValue = Double.parseDouble(courseCreditField.getText().trim());
        double futureCredits = totalCredits.get() + creditValue;
        int targetCredits = targetCreditsSpinner.getValue();
        if (futureCredits - targetCredits > 0.001) {
            showAlert(Alert.AlertType.WARNING, "Credit limit",
                    "Adding this course exceeds the target credits.");
            return;
        }

        Course course = new Course(
                courseNameField.getText().trim(),
                courseCodeField.getText().trim(),
                creditValue,
                teacherOneField.getText().trim(),
                teacherTwoField.getText().trim(),
                gradeComboBox.getValue(),
                GpaCalculator.gradePointFor(gradeComboBox.getValue())
        );
        courses.add(course);
        refreshTotals();
        clearCourseForm();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Course added successfully!");
    }

    @FXML
    private void handleDeleteSelected() {
        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        courses.remove(selected);
        refreshTotals();
    }

    @FXML
    private void handleClearForm() {
        clearCourseForm();
    }

    @FXML
    private void handleResetAll() {
        courses.clear();
        refreshTotals();
        clearCourseForm();
    }

    @FXML
    private void handleCalculateGpa() {
        if (courses.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No courses", "Please add at least one course.");
            return;
        }
        double gpa = GpaCalculator.gpa(courses);
        GpaSummary summary = new GpaSummary(
                courses,
                totalCredits.get(),
                gpa,
                targetCreditsSpinner.getValue()
        );
        SceneNavigator.showResult(summary);
    }

    @FXML
    private void handleBackToHome() {
        SceneNavigator.showHome();
    }

    private void refreshTotals() {
        totalCredits.set(GpaCalculator.totalCredits(courses));
        updateCreditStatus();
    }

    private void clearCourseForm() {
        courseNameField.clear();
        courseCodeField.clear();
        courseCreditField.clear();
        teacherOneField.clear();
        teacherTwoField.clear();
        gradeComboBox.getSelectionModel().selectFirst();
    }

    private boolean validateInputs() {
        if (isBlank(courseNameField) || isBlank(courseCodeField) || isBlank(courseCreditField)
                || isBlank(teacherOneField) || isBlank(teacherTwoField)) {
            showAlert(Alert.AlertType.ERROR, "Missing Data",
                    "Fill in all course details before adding the course.");
            return false;
        }
        try {
            double creditValue = Double.parseDouble(courseCreditField.getText().trim());
            if (creditValue <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid credit",
                        "Course credit must be a positive number.");
                return false;
            }
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Invalid credit",
                    "Course credit must be numeric.");
            return false;
        }
        if (gradeComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Missing grade",
                    "Please select a grade.");
            return false;
        }
        return true;
    }

    private boolean isBlank(TextField field) {
        return field.getText() == null || field.getText().trim().isEmpty();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
