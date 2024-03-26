package org.example.view;

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
import java.util.*;

public class Controller {
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
    public BigInteger[] signature = new BigInteger[2];

    public Controller() throws NoSuchAlgorithmException {
    }


    @FXML
    public void handleButtonClick() {
        text_private_key.setText(object.getPrivateKey());
        text_public_key.setText(object.getPublicKey());
    }

    @FXML
    public void loadFile() {
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
    public void savePublicKey() {
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
    public void generateSignature() throws LengthException {
        signature = object.signMessage(text_file.getText().getBytes());
        StringBuilder stringBuilder = new StringBuilder();
        for (BigInteger element : signature) {
            stringBuilder.append(element.toString());
        }
        String arrayString = stringBuilder.toString();
        text_signature.setText(arrayString);
    }

    @FXML
    public void saveSignature() {
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (BigInteger element : signature) {
                    writer.write(element.toString());
                    writer.newLine();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void loadSignature() {
        File file = fileChooser.showOpenDialog(new Stage());
        List<String> lines = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lines.add(line);
            }
            scanner.close();
            signature[0] = new BigInteger(lines.get(0));
            signature[1] = new BigInteger(lines.get(1));
            String lala = lines.get(0) + lines.get(1);
            text_signature.setText(lala);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void verifySignature() throws LengthException {
        boolean result = object.verifySignature(signature, text_file.getText().getBytes());
        signature_result.setText(String.valueOf(result));
    }


}
