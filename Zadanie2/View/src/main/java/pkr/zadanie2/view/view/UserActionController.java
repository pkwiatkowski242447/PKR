package pkr.zadanie2.view.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserActionController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}