package com.mycompany.project;

import java.io.File; // ذا الامبورت حق استعمال الفايل
import java.io.FileInputStream; // لقراءة البيانات من الملف
import java.io.FileOutputStream; // عشان نكتب بيانات جوا الملف
import java.io.IOException; // عشان يتعامل مع آخطاء الملفات
import java.io.ObjectInputStream;  //عشان يقرا الارب
import java.io.ObjectOutputStream; // لحفظ ال اوب داخل الملف
import java.net.URL; // 
import java.util.ArrayList;
import java.util.ResourceBundle; // انشلايز
import javafx.fxml.FXML; // عشان يدخل للعناصر جوا ال اف اك ام ال 
import javafx.fxml.Initializable;
import javafx.scene.control.Label; // يتعامل مع العنواين
import javafx.scene.control.TextField; // يتعامل مع النصوص

/**
 * Add Grades Controller
 * Student 3
 * Handles: finding student by ID, saving grades to data.bin and grades.bin
 *
 * FIX 1: Now creates Grade object and writes it to grades.bin
 *         (original only updated score in data.bin — grades.bin was never written)
 * FIX 2: calculateAverage() is connected to a button in the FXML
 * FIX 3: Back button uses lowercase "dashboard" to match fxml filename
 */
public class AddGradesController implements Initializable {

    @FXML
    private TextField studentIdTxt; // مربع إدخا رقم الطالبل
    @FXML
    private TextField grade1Txt; //درجة المواد
    @FXML
    private TextField grade2Txt;
    @FXML
    private TextField grade3Txt;

    @FXML
    private Label studentNameLbl; // عرض اسم الطالب
    @FXML
    private Label averageLbl; // المتوسط
    @FXML
    private Label resultLbl; // ذي النتيجة

    @Override
    public void initialize(URL url, ResourceBundle rb) { // ذي تنفذ تلقائيا عند البدء
        if (studentNameLbl != null) studentNameLbl.setText(" Student Name:"); // يتاكد العنصر موجود
        if (averageLbl     != null) averageLbl.setText(""); //
        if (resultLbl      != null) resultLbl.setText("");
    }

    /**
     * Calculates and displays the average from the three grade fields.
     * Connected to "Calculate Average" button in FXML.
     */
    @FXML
    private void calculateAverage() {
        try {
            if (grade1Txt.getText().isEmpty() ||
                grade2Txt.getText().isEmpty() ||
                grade3Txt.getText().isEmpty()) {
                if (averageLbl != null) averageLbl.setText("Fill all grades first.");
                return;
            }
            double g1  = Double.parseDouble(grade1Txt.getText().trim());
            double g2  = Double.parseDouble(grade2Txt.getText().trim());
            double g3  = Double.parseDouble(grade3Txt.getText().trim());
            double avg = (g1 + g2 + g3) / 3.0;
            if (averageLbl != null)
                averageLbl.setText(String.format("   Calculated Average: %.2f", avg));
        } catch (NumberFormatException e) {
            if (averageLbl != null) averageLbl.setText("Grades must be numbers!");
        }
    }

    /**
     * Main save action — called when "Save Grades" button is clicked.
     *
     * FIX: Now does THREE things:
     *   1. Updates student.score in App.allStudents (average)
     *   2. Saves updated students list to data.bin
     *   3. Creates Grade object and saves/updates grades.bin
     *      (ViewStudentsController reads grades.bin to show Sub1/Sub2/Sub3 columns)
     */
    @FXML
    private void handleSaveGrades() {
        if (studentIdTxt.getText().isEmpty() ||
            grade1Txt.getText().isEmpty()    ||
            grade2Txt.getText().isEmpty()    ||
            grade3Txt.getText().isEmpty()) {
            showError("All fields are required!");
            return;
        }

        int studentId;
        double g1, g2, g3;
        try {
            studentId = Integer.parseInt(studentIdTxt.getText().trim());
            g1 = Double.parseDouble(grade1Txt.getText().trim());
            g2 = Double.parseDouble(grade2Txt.getText().trim());
            g3 = Double.parseDouble(grade3Txt.getText().trim());
        } catch (NumberFormatException e) {
            showError("ID and grades must be numbers!");
            return;
        }

        // Find student in list
        Student found = null;
        for (Student s : App.allStudents) {
            if (s.getId() == studentId) {
                found = s;
                break;
            }
        }

        if (found == null) {
            showError("Student ID " + studentId + " not found!");
            if (studentNameLbl != null) studentNameLbl.setText("   Student Name: Not Found");
            if (averageLbl     != null) averageLbl.setText("");
            return;
        }

        double average = (g1 + g2 + g3) / 3.0;

        // 1. Update student score (average) in memory
        found.setScore(average);

        if (studentNameLbl != null)
            studentNameLbl.setText("   Student Name: " + found.getName());
        if (averageLbl != null)
            averageLbl.setText(String.format("   Calculated Average: %.2f", average));

        // 2. Save updated students to data.bin
        try {
            File f = new File("data.bin");
            f.delete();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.bin"))) {
                oos.writeObject(App.allStudents);
            }
        } catch (IOException e) {
            showError("Error saving student data: " + e.getMessage());
            return;
        }

        // 3. FIX: Create Grade object and save to grades.bin
        //    ViewStudentsController reads grades.bin to display Sub1/Sub2/Sub3 columns
        saveGradesToFile(studentId, g1, g2, g3);

        showSuccess("Grades saved successfully for " + found.getName() + "!");
    }

    /**
     * Reads existing grades.bin, replaces grade for this student if exists,
     * otherwise adds a new Grade object, then writes back to grades.bin.
     * FIX: This method is the key fix — grades.bin was never written before.
     */
    private void saveGradesToFile(int studentId, double g1, double g2, double g3) {
        ArrayList<Grade> allGrades = new ArrayList<>();

        // Load existing grades from file (if it exists)
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("grades.bin"))) {
            allGrades = (ArrayList<Grade>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("grades.bin not found, creating new.");
        }

        // Replace or add grade for this student
        boolean foundGrade = false;
        for (int i = 0; i < allGrades.size(); i++) {
            if (allGrades.get(i).getStudentId() == studentId) {
                allGrades.get(i).setGrades(g1, g2, g3);
                foundGrade = true;
                break;
            }
        }
        if (!foundGrade) {
            allGrades.add(new Grade(studentId, g1, g2, g3));
        }

        // Write updated grades list to grades.bin
        try {
            File f = new File("grades.bin");
            f.delete();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("grades.bin"))) {
                oos.writeObject(allGrades);
            }
        } catch (IOException e) {
            showError("Error saving grades file: " + e.getMessage());
        }
    }

    @FXML
    private void clearFields() {
        studentIdTxt.clear();
        grade1Txt.clear();
        grade2Txt.clear();
        grade3Txt.clear();
        if (studentNameLbl != null) studentNameLbl.setText("   Student Name:");
        if (averageLbl     != null) averageLbl.setText("");
        if (resultLbl      != null) resultLbl.setText("");
    }

    /**
     * FIX: uses lowercase "dashboard" to match dashboard.fxml
     */
    @FXML
    private void handleBack() throws IOException {
        App.setRoot("dashboard");
    }

    private void showError(String msg) {
        if (resultLbl != null) {
            resultLbl.setStyle("-fx-text-fill: red;");
            resultLbl.setText("Error: " + msg);
        }
    }

    private void showSuccess(String msg) {
        if (resultLbl != null) {
            resultLbl.setStyle("-fx-text-fill: green;");
            resultLbl.setText(msg);
        }
    }
}
