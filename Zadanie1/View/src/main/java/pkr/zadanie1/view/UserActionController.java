package pkr.zadanie1.view;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import pkr.exceptions.IOReadException;
import pkr.exceptions.IOWriteException;
import pkr.zadanie1.AdvancedEncryptionStandard;
import pkr.zadanie1.IOManager;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class UserActionController {

    @FXML
    private TextArea plainText;

    @FXML
    private TextArea cipherText;

    @FXML
    private TextArea userInputKey;

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
            byte[] fileInputPlainText = ioManager.readBytesFromFile();
            plainText.setText(new String(fileInputPlainText, StandardCharsets.UTF_8));
        }
    }

    @FXML
    public void readCipherTextFromFile() throws IOReadException {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            IOManager ioManager = new IOManager(pathToFile);
            byte[] fileInputCipherText = ioManager.readBytesFromFile();
            cipherText.setText(new String(fileInputCipherText, StandardCharsets.UTF_8));
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
    public void saveCipherTextToFile() throws IOWriteException {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] cipherTextToSave = cipherText.getText().getBytes(StandardCharsets.UTF_8);
            IOManager ioManager = new IOManager(file.getAbsolutePath());
            ioManager.saveBytesToFile(cipherTextToSave);
        }
    }

    @FXML
    public void cipherTheInputPlainText() {
        byte[] encryptionKey = userInputKey.getText().getBytes(StandardCharsets.UTF_8);
        byte[] inputPlainText = plainText.getText().getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessage = AES.encryptMessage(inputPlainText, encryptionKey);
        cipherText.setText(new String(encryptedMessage, StandardCharsets.UTF_8));
    }

    @FXML
    public void decipherTheCipherText() {
        byte[] decryptionKey = userInputKey.getText().getBytes(StandardCharsets.UTF_8);
        byte[] inputCipherText = cipherText.getText().getBytes(StandardCharsets.UTF_8);
        byte[] decryptedMessage = AES.decryptMessage(inputCipherText, decryptionKey);
        plainText.setText(new String(decryptedMessage, StandardCharsets.UTF_8));
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

}