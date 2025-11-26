package com.example.khorshed_2207013_gpa_calculator_builder;

import com.example.khorshed_2207013_gpa_calculator_builder.SceneNavigator;
import com.example.khorshed_2207013_gpa_calculator_builder.Course;
import com.example.khorshed_2207013_gpa_calculator_builder.Student;
import com.example.khorshed_2207013_gpa_calculator_builder.StudentRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class SavedRecordsController {

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, Integer> idColumn;
    @FXML
    private TableColumn<Student, String> studentNameColumn;
    @FXML
    private TableColumn<Student, String> studentIdColumn;
    @FXML
    private TableColumn<Student, String> semesterColumn;
    @FXML
    private TableColumn<Student, Number> totalCreditsColumn;
    @FXML
    private TableColumn<Student, Number> cgpaColumn;
    @FXML
    private TableColumn<Student, String> createdAtColumn;
    @FXML
    private Button deleteButton;
    @FXML
    private Button viewDetailsButton;
    @FXML
    private VBox courseDetailsContainer;
    @FXML
    private Label detailsLabel;

    private final ObservableList<Student> students = FXCollections.observableArrayList();
    private final StudentRepository repository = new StudentRepository();

    @FXML
    private void initialize() {
        setupTable();
        loadStudents();
        deleteButton.disableProperty().bind(studentTable.getSelectionModel().selectedItemProperty().isNull());
        viewDetailsButton.disableProperty().bind(studentTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private void setupTable() {
        studentTable.setItems(students);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        totalCreditsColumn.setCellValueFactory(new PropertyValueFactory<>("totalCredits"));
        cgpaColumn.setCellValueFactory(new PropertyValueFactory<>("cgpa"));
        createdAtColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> cellData.getValue().getCreatedAt().format(formatter)
            );
        });
    }

    private void loadStudents() {
        Task<List<Student>> loadTask = new Task<>() {
            @Override
            protected List<Student> call() throws Exception {
                return repository.getAllStudents();
            }
        };

        loadTask.setOnSucceeded(e -> {
            students.setAll(loadTask.getValue());
        });

        loadTask.setOnFailed(e -> {
            showAlert(Alert.AlertType.ERROR, "Load Failed", "Could not load records: " + loadTask.getException().getMessage());
        });

        new Thread(loadTask).start();
    }

    @FXML
    private void handleDelete() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete student record?");
        confirm.setContentText("This will permanently delete " + selected.getStudentName() + "'s record and all associated courses.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Void> deleteTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    repository.deleteStudent(selected.getId());
                    return null;
                }
            };

            deleteTask.setOnSucceeded(e -> {
                students.remove(selected);
                courseDetailsContainer.getChildren().clear();
                detailsLabel.setText("Record deleted successfully.");
            });

            deleteTask.setOnFailed(e -> {
                showAlert(Alert.AlertType.ERROR, "Delete Failed", "Could not delete record: " + deleteTask.getException().getMessage());
            });

            new Thread(deleteTask).start();
        }
    }

    @FXML
    private void handleViewDetails() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        Task<List<Course>> loadCoursesTask = new Task<>() {
            @Override
            protected List<Course> call() throws Exception {
                return repository.getCoursesByStudentId(selected.getId());
            }
        };

        loadCoursesTask.setOnSucceeded(e -> {
            displayCourseDetails(selected, loadCoursesTask.getValue());
        });

        loadCoursesTask.setOnFailed(e -> {
            showAlert(Alert.AlertType.ERROR, "Load Failed", "Could not load courses: " + loadCoursesTask.getException().getMessage());
        });

        new Thread(loadCoursesTask).start();
    }

    private void displayCourseDetails(Student student, List<Course> courses) {
        courseDetailsContainer.getChildren().clear();

        VBox detailsBox = new VBox(8);

        // Student header
        Label headerLabel = new Label("Student Information");
        headerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        detailsBox.getChildren().add(headerLabel);

        detailsBox.getChildren().add(new Label("Name: " + student.getStudentName()));
        detailsBox.getChildren().add(new Label("ID: " + student.getStudentId()));
        detailsBox.getChildren().add(new Label("Semester: " + student.getSemester()));
        detailsBox.getChildren().add(new Label("CGPA: " + String.format("%.2f", student.getCgpa())));
        detailsBox.getChildren().add(new Label("Credits: " + String.format("%.1f", student.getTotalCredits())));

        // Separator
        Separator separator = new Separator();
        detailsBox.getChildren().add(separator);

        // Courses header
        Label coursesHeader = new Label("Courses (" + courses.size() + ")");
        coursesHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        detailsBox.getChildren().add(coursesHeader);

        // Course list
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            VBox courseBox = new VBox(4);
            courseBox.setStyle("-fx-padding: 8; -fx-background-color: #f5f5f5; -fx-background-radius: 4;");

            Label courseName = new Label((i + 1) + ". " + course.getCourseName() + " (" + course.getCourseCode() + ")");
            courseName.setStyle("-fx-font-weight: bold;");
            courseBox.getChildren().add(courseName);

            courseBox.getChildren().add(new Label("Grade: " + course.getGrade() + " | Credits: " + course.getCourseCredit()));
            courseBox.getChildren().add(new Label("Teachers: " + course.getTeacherOne() + ", " + course.getTeacherTwo()));

            detailsBox.getChildren().add(courseBox);
        }

        courseDetailsContainer.getChildren().add(detailsBox);
    }

    @FXML
    private void handleRefresh() {
        loadStudents();
        courseDetailsContainer.getChildren().clear();
        detailsLabel.setText("");
    }

    @FXML
    private void handleBack() {
        SceneNavigator.showCalculator();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
