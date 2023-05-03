package org.example.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.model.ElGamal;

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
    public void generateSignature(ActionEvent event) throws NoSuchAlgorithmException {
        BigInteger[] result = object.signMessage(text_file.getText().getBytes());
        text_signature.setText(Arrays.toString(result));
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
            while (scanner.hasNextLine()) {
                text_signature.appendText(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void verifySignature(ActionEvent event) {
        BigInteger[] signature = convertTextFieldToArray(text_signature.getText());
        boolean result = object.verifySignature(signature, text_file.getText().getBytes());
        signature_result.setText(String.valueOf(result));
    }

    private BigInteger[] convertTextFieldToArray(String text) {                                                         //Nie działa
        String[] stringValues = text.split(",");
        BigInteger[] bigIntegersValues = new BigInteger[stringValues.length];
        for (int i = 0; i < stringValues.length; i++) {
            bigIntegersValues[i] = new BigInteger(stringValues[i].trim());
        }
        return bigIntegersValues;
    }
}