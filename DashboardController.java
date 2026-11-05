package com.mycompany.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Dashboard Controller
 * Student 1 - Leader
 * Handles: navigation to all windows + Save/Load file operations
 */
public class DashboardController {

    @FXML
    private Label resultlbl;

    // ---- Navigation Methods ----

    @FXML
    private void AddStudent() throws IOException {
        App.setRoot("addStudent");
    }

    @FXML
    private void showStudent() throws IOException {
        App.setRoot("viewStudents");
    }

    @FXML
    private void AddGrade() throws IOException {
        App.setRoot("addGrades");
    }

    @FXML
    private void GoToReport() throws IOException {
        App.setRoot("reports");
    }

    @FXML
    private void Logout() throws IOException {
        App.setRoot("login");
    }

    // ---- File Operations ----

    /**
     * Saves all students from App.allStudents into data.bin
     */
    @FXML
    private void SaveToFile() {
        try {
            File f = new File("data.bin");
            f.delete();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.bin"))) {
                oos.writeObject(App.allStudents);
            }
            resultlbl.setText("Saved " + App.allStudents.size() + " students to data.bin");
            resultlbl.setStyle("-fx-text-fill: #2ecc71;");
        } catch (IOException e) {
            resultlbl.setText("Error saving: " + e.getMessage());
            resultlbl.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

    /**
     * Loads students from data.bin into App.allStudents
     */
    @FXML
    private void LoadStudentdata() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.bin"))) {
            App.allStudents.clear();
            App.allStudents = (ArrayList<Student>) ois.readObject();
            resultlbl.setText("Loaded " + App.allStudents.size() + " students from data.bin");
            resultlbl.setStyle("-fx-text-fill: #2ecc71;");
        } catch (IOException | ClassNotFoundException e) {
            resultlbl.setText("Error loading: " + e.getMessage());
            resultlbl.setStyle("-fx-text-fill: #e74c3c;");
        }
    }
}
