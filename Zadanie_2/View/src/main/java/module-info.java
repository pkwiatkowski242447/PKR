module com.example.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.example.model;
    opens org.example.view to javafx.fxml;
    exports org.example.view;
}