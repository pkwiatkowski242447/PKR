package pkr.zadanie1.view;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class AESApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        StageSetup.buildStage(stage, "main-form.fxml", "Kryptografia - Zadanie 1.");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}