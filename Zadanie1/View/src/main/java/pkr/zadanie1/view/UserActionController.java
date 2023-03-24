package pkr.zadanie1.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import pkr.exceptions.IOReadException;
import pkr.exceptions.IOWriteException;
import pkr.exceptions.KeyValidityException;
import pkr.exceptions.NoMessageException;
import pkr.zadanie1.AdvancedEncryptionStandard;
import pkr.zadanie1.IOManager;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class UserActionController {

    @FXML
    private TextArea plainText;

    @FXML
    private TextArea userInputKey;

    AdvancedEncryptionStandard AES = new AdvancedEncryptionStandard();

    @FXML
    public void initialize() {
        plainText.setPromptText("Wprowadź tekst jawny...");
        userInputKey.setPromptText("Wprowadź 128, 192 lub 256 bitowy klucz do szyfrowania / deszyfrowania.");
    }

    @FXML
    public void readPlainTextFromFile() throws IOReadException {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            IOManager ioManager = new IOManager(pathToFile);
            byte[] fileInputPlainText = ioManager.readBytesFromFile();
            plainText.setText(new String(fileInputPlainText, StandardCharsets.UTF_8));
        }
    }

    @FXML
    public void decryptCipherText() throws IOReadException {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            IOManager ioManager = new IOManager(pathToFile);
            byte[] decryptionKey = userInputKey.getText().getBytes(StandardCharsets.UTF_8);
            byte[] fileInputCipherText = ioManager.readBytesFromFile();
            try {
                byte[] decryptedMessage = AES.decryptMessage(fileInputCipherText, decryptionKey);
                plainText.setText(new String(decryptedMessage, StandardCharsets.UTF_8));
            } catch (KeyValidityException keyValExc) {
                String title = "Wystąpił błąd";
                String header = "Nieprawidłowy klucz";
                String content = keyValExc.getMessage();
                showAlert(Alert.AlertType.ERROR, title, header, content);
            } catch (NoMessageException noMessExc) {
                String title = "Wystąpił błąd";
                String header = "Nieprawidłowa wiadomość";
                String content = noMessExc.getMessage();
                showAlert(Alert.AlertType.ERROR, title, header, content);
            }
        }
    }

    @FXML
    public void savePlainTextToFile() throws IOWriteException {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] plainTextToSave = plainText.getText().getBytes(StandardCharsets.UTF_8);
            IOManager ioManager = new IOManager(file.getAbsolutePath());
            ioManager.saveBytesToFile(plainTextToSave);
        }
    }

    @FXML
    public void encryptPlainText() throws IOWriteException {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] plainTextToSave = plainText.getText().getBytes(StandardCharsets.UTF_8);
            byte[] encryptionKey = userInputKey.getText().getBytes(StandardCharsets.UTF_8);
            try {
                byte[] encryptedMessage = AES.encryptMessage(plainTextToSave, encryptionKey);
                IOManager ioManager = new IOManager(file.getAbsolutePath());
                ioManager.saveBytesToFile(encryptedMessage);
            } catch (KeyValidityException keyValExc) {
                String title = "Wystąpił błąd";
                String header = "Nieprawidłowy klucz";
                String content = keyValExc.getMessage();
                showAlert(Alert.AlertType.ERROR, title, header, content);
            } catch (NoMessageException noMessExc) {
                String title = "Wystąpił błąd";
                String header = "Nieprawidłowa wiadomość";
                String content = noMessExc.getMessage();
                showAlert(Alert.AlertType.ERROR, title, header, content);
            }
        }
    }

    @FXML
    public void showAuthors() {
        showPopUpWindow("Autorzy", "Aleksander Janicki 242405\nPiotr Kwiatkowski 242447");
    }

    private void showPopUpWindow(String title, String content) {
        Dialog<String> popUpWindow = new Dialog<>();
        popUpWindow.setTitle(title);
        popUpWindow.setContentText(content);
        ButtonType closeWindow = new ButtonType("Zamknij", ButtonBar.ButtonData.CANCEL_CLOSE);
        popUpWindow.getDialogPane().getButtonTypes().add(closeWindow);
        popUpWindow.show();
    }

    private void showAlert(Alert.AlertType typeOfAlert, String title, String header, String content) {
        Alert someAlert = new Alert(typeOfAlert);
        someAlert.setTitle(title);
        someAlert.setHeaderText(header);
        someAlert.setContentText(content);
        someAlert.show();
    }

}