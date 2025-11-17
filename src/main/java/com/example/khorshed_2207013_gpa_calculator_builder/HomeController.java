package com.example.khorshed_2207013_gpa_calculator_builder;

import com.example.khorshed_2207013_gpa_calculator_builder.SceneNavigator;
import javafx.fxml.FXML;

public class HomeController {

    @FXML
    private void handleStartCalculator() {
        SceneNavigator.showCalculator();
    }
}
