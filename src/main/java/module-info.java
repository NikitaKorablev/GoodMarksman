module com.example.goodmarksman {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.google.gson;

    opens com.example.goodmarksman to javafx.fxml, com.google.gson;
    exports com.example.goodmarksman;
    exports com.example.goodmarksman.objects;
    exports com.example.goodmarksman.models;
    opens com.example.goodmarksman.objects to com.google.gson, javafx.fxml;
    opens com.example.goodmarksman.models to com.google.gson, javafx.fxml;
}