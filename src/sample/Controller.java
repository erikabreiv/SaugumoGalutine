package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;


    public class Controller implements Initializable{

        @FXML
        public TextField usernameTextField;
        @FXML
        private Button close;
        @FXML
        public TextField passwordTextField;
        @FXML
        public TextField errorField;
        @FXML
        private PasswordField hiddenPasswordTextField;
        @FXML
        private CheckBox showPassword;

        File file = new File("data.txt");

        HashMap<String, String> loginInfo = new HashMap<>();

        Encryptor encryptor = new Encryptor();

        byte[] encryptionKey = {65, 12, 12, 12, 12, 12, 12, 12, 12,
                12, 12, 12, 12, 12, 12, 12};


        @FXML
        void changeVisibility(ActionEvent event) {
            if (showPassword.isSelected()) {
                passwordTextField.setText(hiddenPasswordTextField.getText());
                passwordTextField.setVisible(true);
                hiddenPasswordTextField.setVisible(false);
                return;
            }
            hiddenPasswordTextField.setText(passwordTextField.getText());
            hiddenPasswordTextField.setVisible(true);
            passwordTextField.setVisible(false);
        }


        @Override
        public void initialize (URL url, ResourceBundle resourceBundle) {
            try {
                FileEncryptionDecription.file_decrypt();
                File myObj = new File("Encrypted.txt");
                myObj.delete();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.exit(1);
            }

        }

        @FXML
        void loginHandler(ActionEvent event) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
            String username = usernameTextField.getText();
            String password = getPassword();
            updateUsernamesAndPasswords();

            String encryptedPassword = loginInfo.get(username);
            if (password.equals(encryptor.decrypt(encryptedPassword, encryptionKey))) {
                System.out.println("successfully login!");
            } else {
                errorField.setVisible(true);
            }
        }

        @FXML
        void createAccount(ActionEvent event) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
            writeToFile();
        }

        private String getPassword() {
            if (passwordTextField.isVisible()) {
                return passwordTextField.getText();
            } else {
                return hiddenPasswordTextField.getText();
            }
        }

        private void updateUsernamesAndPasswords() throws IOException {
            Scanner scanner = new Scanner(file);
            loginInfo.clear();
            loginInfo = new HashMap<>();
            while (scanner.hasNext()) {
                String[] splitInfo = scanner.nextLine().split(",");
                loginInfo.put(splitInfo[0], splitInfo[1]);
            }
        }

        private void writeToFile() throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
            String username = usernameTextField.getText();
            String password = getPassword();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

            writer.write(username + "," + encryptor.encrypt(password, encryptionKey) + "\n");
            writer.close();
        }

        @FXML
        void LogOut(ActionEvent event) {

            try {
                FileEncryptionDecription.file_encrypt();
                File myObj = new File("data.txt");
                myObj.delete();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.exit(1);
            }

            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();

        }
    }

