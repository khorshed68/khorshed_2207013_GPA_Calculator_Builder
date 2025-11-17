package com.example.khorshed_2207013_gpa_calculator_builder;

import javafx.application.Application;
import javafx.stage.Stage;

public class GpaApplication extends Application {
    @Override
    public void start(Stage stage) {
        SceneNavigator.init(stage);
        SceneNavigator.showHome();
        stage.show();
    }
}
