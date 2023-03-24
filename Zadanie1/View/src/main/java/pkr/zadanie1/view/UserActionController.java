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
import java.security.Key;

public class UserActionController {

    @FXML
    private TextArea plainText;

    @FXML
    private TextArea cipherText;

    @FXML
    private TextArea userInputKey;

    private final String ERROR_MESSAGE = "Błąd";

    AdvancedEncryptionStandard AES = new AdvancedEncryptionStandard();

    @FXML
    public void initialize() {
        plainText.setPromptText("Wprowadź tekst jawny...");
        cipherText.setPromptText("Wprowadź szyfrogram...");
        userInputKey.setPromptText("Wprowadź 128, 192 lub 256 bitowy klucz do szyfrowania / deszyfrowania.");
    }

    @FXML
    public void readPlainTextFromFile() throws IOReadException {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            IOManager ioManager = new IOManager(pathToFile);
            try {
                byte[] fileInputPlainText = ioManager.readBytesFromFile();
                plainText.setText(new String(fileInputPlainText, StandardCharsets.UTF_8));
            } catch (IOReadException readException) {
                String header = "Błąd odczytu z pliku";
                showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, readException.getMessage());
            }
        }
    }

    @FXML
    public void readCipherTextFromFile() throws IOReadException {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            IOManager ioManager = new IOManager(pathToFile);
            try {
                byte[] fileInputCipherText = ioManager.readBytesFromFile();
                cipherText.setText(new String(fileInputCipherText, StandardCharsets.UTF_8));
            } catch (IOReadException readException) {
                String header = "Błąd odczytu z pliku";
                showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, readException.getMessage());
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
            try {
                ioManager.saveBytesToFile(plainTextToSave);
            } catch (IOWriteException writeException) {
                String header = "Błąd zapisu do pliku";
                showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, writeException.getMessage());
            }
        }
    }

    @FXML
    public void saveCipherTextToFile() throws IOWriteException {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] cipherTextToSave = cipherText.getText().getBytes(StandardCharsets.UTF_8);
            IOManager ioManager = new IOManager(file.getAbsolutePath());
            try {
                ioManager.saveBytesToFile(cipherTextToSave);
            } catch (IOWriteException writeException) {
                String header = "Błąd zapisu do pliku";
                showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, writeException.getMessage());
            }
        }
    }

    @FXML
    public void cipherTheInputPlainText() {
        byte[] encryptionKey = userInputKey.getText().getBytes(StandardCharsets.UTF_8);
        byte[] inputPlainText = plainText.getText().getBytes(StandardCharsets.UTF_8);
        try {
            byte[] encryptedMessage = AES.encryptMessage(inputPlainText, encryptionKey);
            String hexRepresentation = IOManager.convertByteArrayToHex(encryptedMessage);
            cipherText.setText(hexRepresentation);
        } catch (KeyValidityException invalidKeyException) {
            String header = "Nieprawidłowy klucz";
            showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, invalidKeyException.getMessage());
        } catch (NoMessageException messageException) {
            String header = "Nieprawidłowa wiadomość";
            showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, messageException.getMessage());
        }
    }

    @FXML
    public void decipherTheCipherText() {
        byte[] decryptionKey = userInputKey.getText().getBytes(StandardCharsets.UTF_8);
        String hexRepresentation = cipherText.getText();
        byte[] inputCipherText = IOManager.convertHexToByteArray(hexRepresentation);
        try {
            byte[] decryptedMessage = AES.decryptMessage(inputCipherText, decryptionKey);
            plainText.setText(new String(decryptedMessage, StandardCharsets.UTF_8));
        } catch (KeyValidityException invalidKeyException) {
            String header = "Nieprawidłowy klucz";
            showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, invalidKeyException.getMessage());
        } catch (NoMessageException messageException) {
            String header = "Nieprawidłowa wiadomość";
            showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, messageException.getMessage());
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