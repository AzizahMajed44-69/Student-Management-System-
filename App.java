package com.mycompany.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Main JavaFX Application Entry Point
 * Student 1 - Leader
 */
public class App extends Application {

    private static Scene scene;

    // Login credentials (hardcoded)
    static final String username = "Azizah";
    static final String password = "123123";

    // Shared student list used across all controllers
    public static ArrayList<Student> allStudents = new ArrayList<>();

    @Override
    public void start(Stage stage) throws IOException {
        // Load existing data from file on startup
        loadDataOnStartup();

        scene = new Scene(loadFXML("login"), 640, 480);
        stage.setTitle("Student Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads student data from data.bin when the app starts
     * so all windows have the latest saved data immediately
     */
    private void loadDataOnStartup() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.bin"))) {
            ArrayList<Student> saved = (ArrayList<Student>) ois.readObject();
            allStudents.clear();
            allStudents.addAll(saved);
            System.out.println("Loaded " + allStudents.size() + " students from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No saved data found, starting fresh.");
        }
    }

    /**
     * Switches the current scene root to a new FXML file (always lowercase names)
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
