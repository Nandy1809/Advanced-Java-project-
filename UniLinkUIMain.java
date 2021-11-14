package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UniLinkUIMain extends Application {


    /**
     * This method is the main entry point of the application
     * */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/uniLinkLogin.fxml"));

        primaryStage.setTitle("UniLink System");
        primaryStage.setScene(new Scene(root, 381, 207));
        primaryStage.show();


    }

    // For changing the scene
    public static void changeScene(String scene) throws Exception{

    }

    public static void main(String[] args) {
        launch(args);
    }
}
