package com.mycompany.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Add Student Controller
 * Student 2
 * Handles: validation, adding student, and saving to file
 *
 * FIX: now writes to data.bin immediately after adding
 *      (original code was only in-memory — data lost on app close)
 */
public class AddStudentController implements Initializable {

    @FXML
    private TextField idTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField majorTxt;

    @FXML
    private Label resultLbl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resultLbl.setText("");
    }

    /**
     * Called when "Add Student" button is clicked.
     * Validates all fields, adds student to list, then saves to data.bin.
     */
    @FXML
    private void addStudent() {
        String idText    = idTxt.getText().trim();
        String nameText  = nameTxt.getText().trim();
        String majorText = majorTxt.getText().trim();

        // Validation — check empty fields
        if (idText.isEmpty()) {
            showError("Student ID cannot be empty!");
            return;
        }
        if (nameText.isEmpty()) {
            showError("Student Name cannot be empty!");
            return;
        }
        if (majorText.isEmpty()) {
            showError("Major cannot be empty!");
            return;
        }

        // Validate ID is a number
        int studentId;
        try {
            studentId = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            showError("Student ID must be a number!");
            return;
        }

        // Check for duplicate ID
        for (Student s : App.allStudents) {
            if (s.getId() == studentId) {
                showError("Student with ID " + studentId + " already exists!");
                return;
            }
        }

        // Add to list
        Student newStudent = new Student(nameText, studentId, majorText, 0.0);
        App.allStudents.add(newStudent);

        // FIX: Save to data.bin immediately so data is not lost on close
        saveToFile();

        showSuccess("Student \"" + nameText + "\" added successfully! (ID: " + studentId + ")");
        clearFields();
    }

    /**
     * Saves the full student list to data.bin right after adding.
     * FIX: This was missing — data was lost when the app closed.
     */
    private void saveToFile() {
        try {
            File f = new File("data.bin");
            f.delete();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.bin"))) {
                oos.writeObject(App.allStudents);
            }
        } catch (IOException e) {
            showError("File save error: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        resultLbl.setStyle("-fx-text-fill: #e74c3c;");
        resultLbl.setText("Error: " + msg);
    }

    private void showSuccess(String msg) {
        resultLbl.setStyle("-fx-text-fill: #2ecc71;");
        resultLbl.setText(msg);
    }

    private void clearFields() {
        idTxt.clear();
        nameTxt.clear();
        majorTxt.clear();
    }

    /**
     * FIX: setRoot uses lowercase "dashboard" to match dashboard.fxml filename
     *      Original had "Dashboard" with capital D — crashes on Mac/Linux
     */
    @FXML
    private void goBack() throws IOException {
        App.setRoot("dashboard");
    }
}
