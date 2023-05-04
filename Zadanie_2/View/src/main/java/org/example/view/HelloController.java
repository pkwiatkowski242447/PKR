package org.example.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.model.ElGamal;
import org.example.model.LengthException;

import java.io.*;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

public class HelloController {
    ElGamal object = new ElGamal();
    @FXML
    Button button_keys;

    @FXML
    Button button_file;
    @FXML
    TextField text_private_key;
    @FXML
    TextField text_public_key;
    @FXML
    TextField text_file;
    @FXML
    TextField text_file_info;
    @FXML
    Button button_save_keys;
    @FXML
    Button button_generate_signature;
    @FXML
    TextField signature_info;
    @FXML
    TextField text_signature;

    @FXML
    Button button_save_signature;
    @FXML
    Button button_load_signature;

    @FXML
    Button button_verify_signature;

    @FXML
    TextField signature_result_info;
    @FXML
    TextField signature_result;
    FileChooser fileChooser = new FileChooser();
    public BigInteger[] signature;


    @FXML
    public void handleButtonClick(ActionEvent event) {
        object.generatePrimeNumbers();
        text_private_key.setText(object.getPrivateKey());
        text_public_key.setText(object.getPublicKey());
    }

    @FXML
    public void loadFile(ActionEvent event) {
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                text_file.appendText(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void savePublicKey(ActionEvent event) {
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(text_public_key.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void generateSignature(ActionEvent event) throws NoSuchAlgorithmException, LengthException {
        signature = object.signMessage(text_file.getText().getBytes());
        String arrayString = String.join(", ", Arrays.stream(signature).map(Object::toString).toArray(String[]::new));
        text_signature.setText(arrayString);
    }

    @FXML
    public void saveSignature(ActionEvent event) {
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(text_signature.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void loadSignature(ActionEvent event) {
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();
            String[] strArray = line.split(", ");
            BigInteger[] array = new BigInteger[strArray.length];
            for (int i = 0; i < strArray.length; i++) {
                array[i] = new BigInteger(strArray[i]);
            }
            signature = array;
            text_signature.setText(Arrays.toString(array));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void verifySignature(ActionEvent event) {
        boolean result = object.verifySignature(signature, text_file.getText().getBytes());
        signature_result.setText(String.valueOf(result));
    }
}
