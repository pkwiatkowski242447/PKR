package pkr.zadanie2.view.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import pkr.zadanie2.exceptions.IOManagerReadException;
import pkr.zadanie2.exceptions.IOManagerWriteException;
import pkr.zadanie2.exceptions.IncorrectMessageDigestAlgorithm;
import pkr.zadanie2.model.ElGamalSystem;
import pkr.zadanie2.utils.IOManager;
import pkr.zadanie2.utils.Parser;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserActionController {

    @FXML
    private TextArea contentTextArea;
    @FXML
    private TextArea signatureTextArea;
    @FXML
    private TextArea publicKeyTextField;
    @FXML
    private TextArea privateKeyTextField;

    private ElGamalSystem elGamalSystem;
    private BigInteger[] signature = new BigInteger[2];
    private int primeNumberSizeInBits = 1024;

    @FXML
    public void initialize() {
        try {
            elGamalSystem = new ElGamalSystem();
        } catch(IncorrectMessageDigestAlgorithm exception) {
            showAlert(Alert.AlertType.ERROR, "Błąd", "Wstąpił błąd", exception.getMessage());
        }
        elGamalSystem.generateKeyMethod();
        elGamalSystem.setSelectedPrimeNumberLength(primeNumberSizeInBits);
        displayKeys();
    }

    @FXML
    public void saveContentToAFile() {
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Utwórz plik, w którym zostanie zapisania zawartość");
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] contentToBeSavedToAFile = contentTextArea.getText().getBytes(StandardCharsets.UTF_8);
            try {
                IOManager.writeBytesToAFile(file.getAbsolutePath(), contentToBeSavedToAFile);
            } catch (IOManagerWriteException writeException) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd zapisu zawartości", writeException.getMessage());
            }
        }
    }

    public void readContentFromAFile() {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Wybierz plik, z którego zostanie wczytana zawartość");
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            try {
                byte[] contentToBeShown = IOManager.readBytesFromAGivenFile(pathToFile);
                contentTextArea.setText(new String(contentToBeShown, StandardCharsets.UTF_8));
            } catch (IOManagerReadException readException) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd odczytu zawartości", readException.getMessage());
            }
        }
    }

    @FXML
    public void saveSignatureToAFile() {
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Utwórz plik, do którego zostanie zapisany podpis cyfrowy");
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] signatureToBeSavedToAFile = Parser.parseSignatureToBeSavedToAFile(signature[0], signature[1]);
            try {
                IOManager.writeBytesToAFile(file.getAbsolutePath(), signatureToBeSavedToAFile);
            } catch (IOManagerWriteException writeException) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd zapisu podpisu cyfrowego", writeException.getMessage());
            }
        }
    }

    @FXML
    public void readSignatureFromAFile() {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Wybierz plik, z którego zostanie wczytany podpis cyfrowy");
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            try {
                List<byte[]> signatureToBeShown = Parser.parseSignatureToBeShownInsideTheProgram(IOManager.readBytesFromAGivenFile(pathToFile));
                signature[0] = new BigInteger(signatureToBeShown.get(0));
                signature[1] = new BigInteger(signatureToBeShown.get(1));
                displaySignature();
            } catch (IOManagerReadException readException) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd odczytu podpisu cyforwego", readException.getMessage());
            }
        }
    }

    @FXML
    public void generateSignature() {
        byte[] contentToSign = contentTextArea.getText().getBytes(StandardCharsets.UTF_8);
        if (contentToSign.length != 0) {
            signature = elGamalSystem.signGivenFile(contentToSign);
            displaySignature();
        } else {
            showAlert(Alert.AlertType.ERROR, "Błąd", "Brak zawartości do wygenerowania podpisu", "W polu zawartości nie znajduje się żadna zawartość do sporządznenia podpisu cyfrowego");
        }
    }

    @FXML
    public void signBytesInAFile() {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Wybierz plik, który zostanie podpisany");
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            try {
                byte[] contentToSign = IOManager.readBytesFromAGivenFile(pathToFile);
                if (contentToSign.length != 0) {
                    signature = elGamalSystem.signGivenFile(contentToSign);
                    displaySignature();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd zawartość w pliku", "Podany plik jest pusty i nie posiada tym samym zawartości do podpisania.");
                }
            } catch (IOManagerReadException readException) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd wczytywania zawartości pliku do podpisania", readException.getMessage());
            }
        }
    }

    @FXML
    public void verifySignature() {
        byte[] contentToCheckSignature = contentTextArea.getText().getBytes(StandardCharsets.UTF_8);
        if (contentToCheckSignature.length == 0) {
            showAlert(Alert.AlertType.ERROR, "Błąd", "Brak zawartości do weryfikacji podpisu", "W polu zawartości nie znajduje się żadna zawartość do weryfikacji podpisu cyfrowego");
        } else if (signature[0] == null || signature[1] == null) {
            showAlert(Alert.AlertType.ERROR, "Błąd", "Brak podpisu cyfrowego do weryfikacji", "W polu podpisu cyfrowego nie znajduje się żadna zawartość do weryfikacji");
        } else {
            boolean isCorrect = elGamalSystem.verifyIfSignatureIsCorrect(contentToCheckSignature, signature);
            if (isCorrect) {
                showAlert(Alert.AlertType.INFORMATION, "Wynik sprawdzenia podpisu cyfrowego", "Wynik porównania", "Podpis cyfrowy zgadza się z zawartością, pod którą został złożony");
            } else {
                showAlert(Alert.AlertType.ERROR, "Wynik sprawdzenia podpisu cyfrowego", "Wynik porównania", "Podpis cyfrowy nie zgadza się z zawartością, pod którą został złożony");
            }
        }
    }

    @FXML
    public void verifyBytesInAFile() {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Wybierz plik, dla którego wykonano wybrany podpis cyfrowy");
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            try {
                byte[] contentToVerifyFromAFile = IOManager.readBytesFromAGivenFile(pathToFile);
                if (contentToVerifyFromAFile.length == 0) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd zawartości w pliku", "W podanym pliku nie ma zawartości do weryfikacji podpisu cyfrowego");
                } else if (signature[0] == null || signature[1] == null) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd podpisu cyfrowego", "W polu podpisu cyfrowego nie ma zawartości w celu weryfikacji");
                } else {
                    boolean isCorrect = elGamalSystem.verifyIfSignatureIsCorrect(contentToVerifyFromAFile, signature);
                    if (isCorrect) {
                        showAlert(Alert.AlertType.INFORMATION, "Wynik sprawdzenia podpisu cyfrowego", "Wynik porównania", "Podpis cyfrowy zgadza się z zawartością, pod którą został złożony");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Wynik sprawdzenia podpisu cyfrowego", "Wynik porównania", "Podpis cyfrowy nie zgadza się z zawartością, pod którą został złożony");
                    }
                }
            } catch(IOManagerReadException readException) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd odczytu zawartości pliku do weryfikacji podpisu", readException.getMessage());
            }
        }
    }

    @FXML
    public void savePublicKeyToAFile() {
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Wybierz plik, do którego zostanie zapisany klucz publiczny");
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            try {
                byte[] contentToWriteToAFile = Parser.parseForSavingToAFile(new BigInteger(elGamalSystem.getGNumber()),
                        new BigInteger(elGamalSystem.getPNumber()), new BigInteger(elGamalSystem.getHNumber()));
                IOManager.writeBytesToAFile(file.getAbsolutePath(), contentToWriteToAFile);
            } catch (IOManagerWriteException writeException) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd zapisu klucza publicznego do pliku", writeException.getMessage());
            }
        }
    }

    @FXML
    public void readPublicKeyFromAFile() {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Wybierz plik, z którego zostanie wczytany klucz publiczny");
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            try {
                List<byte[]> publicKeyReadFromAFile = Parser.parseByteArrayReadFromAFile(IOManager.readBytesFromAGivenFile(pathToFile));
                elGamalSystem.setPublicKey(new BigInteger(publicKeyReadFromAFile.get(0)),
                        new BigInteger(publicKeyReadFromAFile.get(1)), new BigInteger(publicKeyReadFromAFile.get(2)));
                displayKeys();
            } catch(IOManagerReadException readException) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd odczytu klucza publicznego", readException.getMessage());
            }
        }
    }

    @FXML
    public void savePrivateKeyToAFile() {
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Wybierz plik, do którego zostanie zapisany klucz prywatny");
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            try {
                IOManager.writeBytesToAFile(file.getAbsolutePath(), elGamalSystem.getANumber());
            } catch(IOManagerWriteException writeException) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd zapisu klucza prywatnego", writeException.getMessage());
            }
        }
    }

    @FXML
    public void readPrivateKeyFromAFile() {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Wybierz plik, z którego zostanie odczytany klucz prywatny");
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (pathToFile != null) {
            try {
                byte[] privateKeyFromAFile = IOManager.readBytesFromAGivenFile(pathToFile);
                elGamalSystem.setPrivateKey(new BigInteger(privateKeyFromAFile));
                displayPrivateKey();
            } catch(IOManagerReadException readException) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd odczytu klucza prywatnego", readException.getMessage());
            }
        }
    }

    @FXML
    public void showAuthorsData() {
        showPopUpWindow("Autorzy", "Aleksander Janicki 242405\nPiotr Kwiatkowski 242447");
    }

    @FXML
    public void generateAllTheKeys() {
        elGamalSystem.setSelectedPrimeNumberLength(primeNumberSizeInBits);
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
        stringBuilder.append(new BigInteger(elGamalSystem.getGNumber()).toString(16));
        stringBuilder.append("\n\n");
        stringBuilder.append(new BigInteger(elGamalSystem.getPNumber()).toString(16));
        stringBuilder.append("\n\n");
        stringBuilder.append(new BigInteger(elGamalSystem.getHNumber()).toString(16));
        publicKeyTextField.setText(stringBuilder.toString());
        privateKeyTextField.setText(new BigInteger(elGamalSystem.getANumber()).toString(16));
    }

    private void displaySignature() {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(signature[0].toString(16)).append("\n\n").append(signature[1].toString(16));
        signatureTextArea.setText(signatureBuilder.toString());
    }

    private void displayPrivateKey() {
        StringBuilder privateKeyBuilder = new StringBuilder();
        privateKeyBuilder.append(new BigInteger(elGamalSystem.getANumber()).toString(16));
        privateKeyTextField.setText(privateKeyBuilder.toString());
    }

    @FXML
    public void setPrimeNumberSize256() {
        primeNumberSizeInBits = 256;
    }

    @FXML
    public void setPrimeNumberSize512() {
        primeNumberSizeInBits = 512;
    }

    @FXML
    public void setPrimeNumberSize1024() {
        primeNumberSizeInBits = 1024;
    }

    @FXML
    public void setPrimeNumberSize2048() {
        primeNumberSizeInBits = 2048;
    }
}