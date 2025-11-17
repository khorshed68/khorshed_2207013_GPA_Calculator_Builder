package com.example.khorshed_2207013_gpa_calculator_builder;

import com.example.khorshed_2207013_gpa_calculator_builder.ResultController;
import com.example.khorshed_2207013_gpa_calculator_builder.GpaSummary;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public final class SceneNavigator {
    private static Stage primaryStage;
    private static final double DEFAULT_WIDTH = 1100;
    private static final double DEFAULT_HEIGHT = 720;

    private SceneNavigator() {
    }

    public static void init(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Student GPA Calculator");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(620);
    }

    public static void showHome() {
        applyScene(loadRoot("home-view.fxml"));
    }

    public static void showCalculator() {
        applyScene(loadRoot("calculator-view.fxml"));
    }

    public static void showResult(GpaSummary summary) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("result-view.fxml"));
            Parent root = loader.load();
            ResultController controller = loader.getController();
            controller.setSummary(summary);
            applyScene(root);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load result view", e);
        }
    }

    private static Parent loadRoot(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlFile));
            return loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load view: " + fxmlFile, e);
        }
    }

    private static void applyScene(Parent root) {
        ensureStage();
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(root);
        }
        String stylesheet = Objects.requireNonNull(SceneNavigator.class.getResource("styles.css")).toExternalForm();
        if (!scene.getStylesheets().contains(stylesheet)) {
            scene.getStylesheets().add(stylesheet);
        }
    }

    private static void ensureStage() {
        if (primaryStage == null) {
            throw new IllegalStateException("SceneNavigator has not been initialized");
        }
    }
}
