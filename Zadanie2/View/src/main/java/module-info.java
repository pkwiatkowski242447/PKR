module pkr.zadanie2.view.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires ModelProject;

    opens pkr.zadanie2.view.view to javafx.fxml;
    exports pkr.zadanie2.view.view;
}