package pkr.zadanie2.view.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import pkr.zadanie2.exceptions.IOManagerReadException;
import pkr.zadanie2.exceptions.IOManagerWriteException;
import pkr.zadanie2.exceptions.IncorrectMessageDigestAlgorithm;
import pkr.zadanie2.model.ElGamalSystem;
import pkr.zadanie2.utils.Converter;
import pkr.zadanie2.utils.IOManager;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class UserActionController {

    @FXML
    private TextArea contentTextArea;
    @FXML
    private TextArea signatureTextArea;
    @FXML
    private TextField publicKeyTextField;
    @FXML
    private TextField privateKeyTextField;

    private ElGamalSystem elGamalSystem;

    @FXML
    public void initialize() {
        try {
            elGamalSystem = new ElGamalSystem();
        } catch(IncorrectMessageDigestAlgorithm exception) {
            showAlert(Alert.AlertType.ERROR, "Błąd", "Wstąpił błąd", exception.getMessage());
        }
        elGamalSystem.generateKeyMethod();
        displayKeys();
    }

    @FXML
    public void saveContentToAFile() {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] contentToBeSavedToAFile = contentTextArea.getText().getBytes(StandardCharsets.UTF_8);
            try {
                IOManager.writeBytesToAFile(file.getAbsolutePath(), contentToBeSavedToAFile);
            } catch (IOManagerWriteException writeException) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd zapisu", writeException.getMessage());
            }
        }
    }

    public void readContentFromAFile() {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Wybór pliku tekstowego do wczytania");
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            try {
                byte[] contentToBeShown = IOManager.readBytesFromAGivenFile(pathToFile);
                contentTextArea.setText(new String(contentToBeShown, StandardCharsets.UTF_8));
            } catch (IOManagerReadException readException) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd odczytu", readException.getMessage());
            }
        }
    }

    @FXML
    public void saveSignatureToAFile() {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] signatureToBeSavedToAFile = Converter.base64ToByteValueConverter(signatureTextArea.getText().getBytes(StandardCharsets.US_ASCII));
            try {
                IOManager.writeBytesToAFile(file.getAbsolutePath(), signatureToBeSavedToAFile);
            } catch (IOManagerWriteException writeException) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd zapisu", writeException.getMessage());
            }
        }
    }

    @FXML
    public void readSignatureFromAFile() {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            try {
                byte[] signatureToBeShown = Converter.byteValueToBase64Converter(IOManager.readBytesFromAGivenFile(pathToFile));
                signatureTextArea.setText(new String(signatureToBeShown, StandardCharsets.US_ASCII));
            } catch (IOManagerReadException readException) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił błąd", readException.getMessage());
            }
        }
    }

    @FXML
    public void generateSignature() {

    }

    @FXML
    public void singBytesInAFile() {

    }

    @FXML
    public void verifySignature() {

    }

    @FXML
    public void verifyBytesInAFile() {

    }

    @FXML
    public void savePublicKeyToAFile() {

    }

    @FXML
    public void readPublicKeyFromAFile() {

    }

    @FXML
    public void savePrivateKeyToAFile() {

    }

    @FXML
    public void readPrivateKeyFromAFile() {

    }

    @FXML
    public void showAuthorsData() {
        showPopUpWindow("Autorzy", "Aleksander Janicki 242405\nPiotr Kwiatkowski 242447");
    }

    @FXML
    public void generateAllTheKeys() {
        elGamalSystem.generateKeyMethod();
        displayKeys();
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

    private void displayKeys() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        stringBuilder.append(new BigInteger(elGamalSystem.getGNumber()).toString(16));
        stringBuilder.append(", ");
        stringBuilder.append(new BigInteger(elGamalSystem.getPNumber()).toString(16));
        stringBuilder.append(", ");
        stringBuilder.append(new BigInteger(elGamalSystem.getHNumber()).toString(16));
        stringBuilder.append(')');
        publicKeyTextField.setText(stringBuilder.toString());
        privateKeyTextField.setText('(' + new BigInteger(elGamalSystem.getANumber()).toString(16) + ')');
    }
}