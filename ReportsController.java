package com.mycompany.project;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * Reports Controller
 * Student 5
 * Handles: reading students from file and displaying statistics (total, highest, average)
 */
public class ReportsController implements Initializable {

    @FXML
    private Label totalStudentsLbl;

    @FXML
    private Label highestGradeLbl;

    @FXML
    private Label generalAverageLbl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadReports();
    }

    private void loadReports() {
        ArrayList<Student> students = new ArrayList<>();

        // Try to load from file; fall back to in-memory list
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.bin"))) {
            students = (ArrayList<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            students.addAll(App.allStudents);
        }

        int    totalStudents = students.size();
        double highestGrade  = 0;
        double sum           = 0;

        for (Student s : students) {
            double score = s.getScore();
            sum += score;
            if (score > highestGrade) highestGrade = score;
        }

        double generalAverage = (totalStudents > 0) ? sum / totalStudents : 0;

        totalStudentsLbl.setText("Total Students : " + totalStudents);
        highestGradeLbl.setText("Highest Grade : " + String.format("%.2f", highestGrade));
        generalAverageLbl.setText("General Average : " + String.format("%.2f", generalAverage));
    }

    @FXML
    private void handleBackButton() throws IOException {
        App.setRoot("dashboard");
    }
}
