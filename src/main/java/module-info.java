module com.example.goodmarksman {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.goodmarksman to javafx.fxml;
    exports com.example.goodmarksman;
    exports com.example.goodmarksman.objects;
    opens com.example.goodmarksman.objects to javafx.fxml;
}