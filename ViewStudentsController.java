package com.mycompany.project;

import java.io.FileInputStream; // ذا الامبورت حق استعمال الفايل و فتح الملف
import java.io.IOException; // لمعالجة الاغلاط
import java.io.ObjectInputStream; // لقراءة الاوب المخزن داخل الملف
import java.net.URL;
import java.util.ArrayList;  // لتخزين الغريد
import java.util.ResourceBundle; //ذا عشان اللغات
import javafx.beans.property.SimpleObjectProperty; // تشتعمل ل انشاء بروبريتي داخل ال تابل فيو
import javafx.collections.FXCollections; //
import javafx.collections.ObservableList;// قائمة مرتبة بالواجهه
import javafx.fxml.FXML; // لربط الكود مع ملفات ال اف اك ام ال
import javafx.fxml.Initializable; // اللي تشتغل على طول بعد فتح الشاشه
import javafx.scene.control.Label; // يتعامل مع الليبل
import javafx.scene.control.TableColumn; // يمثل العامود داخل ال تابل فيو
import javafx.scene.control.TableView; // ذا الجدول
import javafx.scene.control.cell.PropertyValueFactory; //يزبط المتغير ذاخل الكلاس

/**
 * View Students Controller
 * Student 4
 * Handles: reading data.bin and grades.bin, displaying in TableView
 *
 * FIX 1: Method name corrected from 'handlBackButton' (typo) to 'handleBackButton'
 *         FXML onAction="#handleBackButton" must match this method name exactly
 * FIX 2: grades.bin is now written by AddGradesController so Sub1/Sub2/Sub3 show correctly
 * FIX 3: Back button uses lowercase "dashboard" to match fxml filename
 */
public class ViewStudentsController implements Initializable {

    @FXML
    private TableView<Student> StudentsTable;

    @FXML
    private TableColumn<Student, Integer> idColumn; 

    @FXML
    private TableColumn<Student, String> nameColumn;

    @FXML
    private TableColumn<Student, Double> Sub1; // يعرض درجات المقرر الاول


    @FXML
    private TableColumn<Student, Double> Sub2;

    @FXML
    private TableColumn<Student, Double> Sub3;

    @FXML
    private TableColumn<Student, Double> averageColumn;

    @FXML
    private Label totalStudents;
    
    private ObservableList<Student> studentList = FXCollections.observableArrayList(); // ذي قايمة فاضية بعد م يتخزنون الطلاب يبدون ينظافون بوسطة
    // Local list of Grade objects loaded from grades.bin
    private ArrayList<Grade> allGradesList = new ArrayList<>(); // تم انشاء قائمة عشان تحدث البييانات تلقائا
    // ذي القايمة به الدرجات المقروءة
    // هنا تخزيم جميع البيانات المقروؤة
    @Override
    public void initialize(URL url, ResourceBundle rb) { // ذي اول شي ينتذ اول م تنفتح الشاشه
        // Bind basic columns to Student properties // ترا هنا ربط كل شي و بالسين بالدر كمل الربط
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id")); // هنا يستدعي ال آدي بعدين يجيب ايدي جديد يخزن به الايدي ( وهو يكون مستدعى من عند الطلاب )
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name")); // هنا يستدعي ال نيم بعدين يجيب ايدي جديد يخزن به الايدي ( وهو يكون مستدعى من عند الطلاب )
        averageColumn.setCellValueFactory(new PropertyValueFactory<>("score")); // // هنا يستدعي ال سكور بعدين يجيب ايدي جديد يخزن به الايدي ( وهو يكون مستدعى من عند الطلاب )

        // Bind subject grade columns dynamically using Grade objects from grades.bin
       /**
        * هنا يقول تطب كعلى الصفوف ذا الكود
        * 
        */
        Sub1.setCellValueFactory(cellData -> { // كل صف من هنا
            Grade g = findGradeByStudentId(cellData.getValue().getId());
            return new SimpleObjectProperty<>(g != null ? g.getGrade1() : 0.0);
        });
        Sub2.setCellValueFactory(cellData -> { //
            Grade g = findGradeByStudentId(cellData.getValue().getId());
            return new SimpleObjectProperty<>(g != null ? g.getGrade2() : 0.0);
        });
        Sub3.setCellValueFactory(cellData -> {
            Grade g = findGradeByStudentId(cellData.getValue().getId());
            return new SimpleObjectProperty<>(g != null ? g.getGrade3() : 0.0);
        });

        // Load data then populate table
        readDataFromFile();
        StudentsTable.setItems(studentList);
    }

    /**
     * Finds a Grade object by student ID from the loaded grades list
     */
    private Grade findGradeByStudentId(int studentId) {
        for (Grade g : allGradesList) {
            if (g.getStudentId() == studentId) return g;
        }
        return null;
    }

    /**
     * Reads both data.bin (students) and grades.bin (subject grades).
     * grades.bin is now properly written by AddGradesController.
     */
    private void readDataFromFile() {
        // Load students from data.bin
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.bin"))) {
            ArrayList<Student> importedStudents = (ArrayList<Student>) ois.readObject();
            App.allStudents.clear();
            App.allStudents.addAll(importedStudents);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not load data.bin: " + e.getMessage());
        }

        studentList.clear();
        studentList.addAll(App.allStudents);

        if (totalStudents != null) {
            totalStudents.setText("Total Students : " + studentList.size());
        }

        // Load grades from grades.bin
        // This file is written by AddGradesController.saveGradesToFile()
        try (ObjectInputStream oisGrades = new ObjectInputStream(new FileInputStream("grades.bin"))) {
            allGradesList = (ArrayList<Grade>) oisGrades.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("grades.bin not found or empty — subject columns will show 0.0");
            allGradesList.clear();
        }
    }

    /**
     * FIX: method name corrected from 'handlBackButton' (missing 'e') to 'handleBackButton'
     *      FXML onAction="#handleBackButton" must match this method name exactly
     */
    @FXML
    private void handleBackButton() throws IOException {
        App.setRoot("dashboard");
    }
}
