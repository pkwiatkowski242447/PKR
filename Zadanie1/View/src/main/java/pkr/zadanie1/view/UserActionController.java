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
import java.text.DecimalFormat;

public class UserActionController {

    @FXML
    private TextArea plainText;

    @FXML
    private TextArea cipherText;

    @FXML
    private TextArea userInputKey;

    @FXML
    private TextField operationTime;

    private long startOfMeasurementTime;
    private long endOfMeasurementTime;
    private final String ERROR_MESSAGE = "Błąd";

    AdvancedEncryptionStandard AES = new AdvancedEncryptionStandard();

    @FXML
    public void initialize() {
        plainText.setPromptText("Wprowadź tekst jawny...");
        cipherText.setPromptText("Wprowadź szyfrogram...");
        userInputKey.setPromptText("Wprowadź 128, 192 lub 256 bitowy klucz do szyfrowania / deszyfrowania.");
    }

    @FXML
    public void readPlainTextFromFile() {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            IOManager ioManager = new IOManager(pathToFile);
            try {
                byte[] fileInputPlainText = ioManager.readBytesFromFile();
                plainText.setText(new String(fileInputPlainText, StandardCharsets.UTF_8));
            } catch (IOReadException readException) {
                String header = "Błąd odczytu tekstu jawnego z pliku";
                showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, readException.getMessage());
            }
        }
    }

    @FXML
    public void readCipherTextFromFile() {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            IOManager ioManager = new IOManager(pathToFile);
            try {
                byte[] fileInputCipherText = IOManager.convertByteArrayToHex(ioManager.readBytesFromFile());
                cipherText.setText(new String(fileInputCipherText, StandardCharsets.US_ASCII));
            } catch (IOReadException readException) {
                String header = "Błąd odczytu szyfrogramu z pliku";
                showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, readException.getMessage());
            }
        }
    }

    @FXML
    public void savePlainTextToFile() {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] plainTextToSave = plainText.getText().getBytes(StandardCharsets.UTF_8);
            IOManager ioManager = new IOManager(file.getAbsolutePath());
            try {
                ioManager.saveBytesToFile(plainTextToSave);
            } catch (IOWriteException writeException) {
                String header = "Błąd zapisu tekstu jawnego do pliku";
                showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, writeException.getMessage());
            }
        }
    }

    @FXML
    public void saveCipherTextToFile() {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] cipherTextToSave = IOManager.convertHexToByteArray(cipherText.getText().getBytes(StandardCharsets.US_ASCII));
            IOManager ioManager = new IOManager(file.getAbsolutePath());
            try {
                ioManager.saveBytesToFile(cipherTextToSave);
            } catch (IOWriteException writeException) {
                String header = "Błąd zapisu szyfrogramu do pliku";
                showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, writeException.getMessage());
            }
        }
    }

    @FXML
    public void cipherTheInputPlainText() {
        byte[] inputPlainText = plainText.getText().getBytes(StandardCharsets.UTF_8);
        try {
            byte[] encryptionKey = IOManager.convertHexToByteArray(userInputKey.getText().getBytes(StandardCharsets.US_ASCII));
            startOfMeasurementTime = System.currentTimeMillis();
            byte[] outputCipherText = IOManager.convertByteArrayToHex(AES.encryptMessage(inputPlainText, encryptionKey));
            endOfMeasurementTime = System.currentTimeMillis();
            cipherText.setText(new String(outputCipherText, StandardCharsets.US_ASCII));
            double lengthInMegaBytes = (double) inputPlainText.length / (1024 * 1024);
            double periodOfTimeInSeconds = (double) (endOfMeasurementTime - startOfMeasurementTime) / 1000;
            double outputRate = lengthInMegaBytes / periodOfTimeInSeconds;
            DecimalFormat decimalFormat = new DecimalFormat("0.00000");
            operationTime.setText(decimalFormat.format(outputRate) + " MB / s");
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
        byte[] inputCipherText = IOManager.convertHexToByteArray(cipherText.getText().getBytes(StandardCharsets.US_ASCII));
        try {
            byte[] decryptionKey = IOManager.convertHexToByteArray(userInputKey.getText().getBytes(StandardCharsets.US_ASCII));
            startOfMeasurementTime = System.currentTimeMillis();
            byte[] outputPlainText = AES.decryptMessage(inputCipherText, decryptionKey);
            endOfMeasurementTime = System.currentTimeMillis();
            plainText.setText(new String(outputPlainText, StandardCharsets.UTF_8));
            double lengthInMegaBytes = (double) inputCipherText.length / (1024 * 1024);
            double periodOfTimeInSeconds = (double) (endOfMeasurementTime - startOfMeasurementTime) / 1000;
            double outputRate = lengthInMegaBytes / periodOfTimeInSeconds;
            DecimalFormat decimalFormat = new DecimalFormat("0.00000");
            operationTime.setText(String.format(decimalFormat.format(outputRate) + " MB / s"));
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

    @FXML
    public void generate128Key() {
        byte[] generatedKey = AdvancedEncryptionStandard.generateKey(128 / 8);
        userInputKey.setText(new String(IOManager.convertByteArrayToHex(generatedKey), StandardCharsets.US_ASCII));
    }

    @FXML
    public void generate192Key() {
        byte[] generatedKey = AdvancedEncryptionStandard.generateKey(192 / 8);
        userInputKey.setText(new String(IOManager.convertByteArrayToHex(generatedKey), StandardCharsets.US_ASCII));
    }

    @FXML
    public void generate256Key() {
        byte[] generatedKey = AdvancedEncryptionStandard.generateKey(256 / 8);
        userInputKey.setText(new String(IOManager.convertByteArrayToHex(generatedKey), StandardCharsets.US_ASCII));
    }

    @FXML
    public void readUserKey() {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            IOManager ioManager = new IOManager(pathToFile);
            try {
                byte[] fileInputCipherText = IOManager.convertByteArrayToHex(ioManager.readBytesFromFile());
                userInputKey.setText(new String(fileInputCipherText, StandardCharsets.US_ASCII));
            } catch (IOReadException readException) {
                String header = "Błąd odczytu klucza z pliku";
                showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, readException.getMessage());
            }
        }
    }

    @FXML
    public void saveUserKey() {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] userKeyToSave = IOManager.convertHexToByteArray(userInputKey.getText().getBytes(StandardCharsets.US_ASCII));
            IOManager ioManager = new IOManager(file.getAbsolutePath());
            try {
                ioManager.saveBytesToFile(userKeyToSave);
            } catch (IOWriteException writeException) {
                String header = "Błąd zapisu klucza do pliku";
                showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, writeException.getMessage());
            }
        }
    }

    @FXML
    public void readBinaryFile() {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            IOManager ioManager = new IOManager(pathToFile);
            try {
                byte[] binaryFileInput = IOManager.convertByteArrayToHex(ioManager.readBytesFromFile());
                plainText.setText(new String(binaryFileInput, StandardCharsets.US_ASCII));
            } catch (IOReadException readException) {
                String header = "Błąd odczytu z pliku";
                showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, readException.getMessage());
            }
        }
    }

    @FXML
    public void saveBinaryFile() {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] binaryFileOutput = IOManager.convertHexToByteArray(plainText.getText().getBytes(StandardCharsets.US_ASCII));
            IOManager ioManager = new IOManager(file.getAbsolutePath());
            try {
                ioManager.saveBytesToFile(binaryFileOutput);
            } catch (IOWriteException writeException) {
                String header = "Błąd zapisu do pliku";
                showAlert(Alert.AlertType.ERROR, ERROR_MESSAGE, header, writeException.getMessage());
            }
        }
    }

}