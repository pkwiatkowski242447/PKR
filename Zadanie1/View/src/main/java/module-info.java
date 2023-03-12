module pkr.zadanie1.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires ModelProject;

    opens pkr.zadanie1.view to javafx.fxml;
    exports pkr.zadanie1.view;
}