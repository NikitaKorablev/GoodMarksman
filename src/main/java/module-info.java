open module com.example.goodmarksman {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.google.gson;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.desktop;
    requires org.jboss.jandex;

    exports com.example.goodmarksman;
    exports com.example.goodmarksman.objects;
    exports com.example.goodmarksman.models;
    exports com.example.goodmarksman.enams;
    exports com.example.goodmarksman.data;
}