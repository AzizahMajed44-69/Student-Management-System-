package com.mycompany.project;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Login Controller
 * Student 1 - Leader
 * Handles: user login validation and navigation to Dashboard
 */
public class LoginController implements Initializable {

    @FXML
    private TextField UserNametxt;

    @FXML
    private TextField PassTxt;

    @FXML
    private Label resultlbl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resultlbl.setText("");
    }

    /**
     * Called when Login button is clicked.
     * Validates username and password then navigates to dashboard.
     */
    @FXML
    private void switchToSecondary() throws IOException {
        String enteredUser = UserNametxt.getText().trim();
        String enteredPass = PassTxt.getText().trim();

        if (enteredUser.isEmpty() || enteredPass.isEmpty()) {
            resultlbl.setText("Please enter username and password.");
            return;
        }

        if (enteredUser.equals(App.username) && enteredPass.equals(App.password)) {
            // FIX: always use lowercase "dashboard" to match dashboard.fxml
            App.setRoot("dashboard");
        } else {
            resultlbl.setText("User name or password are not correct");
        }
    }
}
