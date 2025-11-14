module com.example.khorshed_2207013_gpa_calculator_builder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example.khorshed_2207013_gpa_calculator_builder to javafx.fxml;
    exports com.example.khorshed_2207013_gpa_calculator_builder;
}