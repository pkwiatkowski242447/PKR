package pkr.zadanie2.view.view;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class PKRApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        StageSetup.buildStage(stage, "main-form.fxml", "Zadanie 2 - Podstawy kryptografii.");
    }

    public static void main(String[] args) {
        launch();
    }
}