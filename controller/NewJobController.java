package controller;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.JobModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class NewJobController implements Initializable {

    public TextField jobProposedPrice;
    public TextArea jobDescc;
    public Button createNewJob;
    public TextField jobTitle;
    String imageName;
    String imagePath;
    String loggedInUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numberValidation(jobProposedPrice);
    }

    public void initData(String currentUser) {
        this.loggedInUser = currentUser;
    }

    public void createJobOnClick(ActionEvent actionEvent) throws Exception{
        boolean validInput = false;

        if(jobTitle.getText().equals("")){
            displayMessage("ERROR","Title cannot be empty");
            validInput = false;
        }
        else if(jobDescc.getText().equals("")){
            displayMessage("ERROR","Description cannot be empty");
            validInput = false;
        }
        else if(jobProposedPrice.getText().equals("")){
            displayMessage("ERROR","Proposed price cannot be empty");
            validInput = false;
        }
        else{
            validInput = true;
        }

        if(validInput){
            String jobTitle = this.jobTitle.getText();
            String jobDescription = jobDescc.getText();
            double proposedPrice = Double.parseDouble(jobProposedPrice.getText());
            JobModel job = new JobModel();
            if(imagePath==null){
                job.makeNewJob(loggedInUser, jobTitle, jobDescription, proposedPrice,"no_image_available");
            }
            else{
                job.makeNewJob(loggedInUser, jobTitle, jobDescription, proposedPrice, imageName);
            }
            Stage stage =  (Stage) createNewJob.getScene().getWindow();
            stage.close();
            displayMessage("SUCCESSFUL", "JobModel Created Successfully");
        }
    }


    private void displayMessage(String title, String message) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/message.fxml"));
        Parent root = (Parent) loader.load();
        ShowErrorController controller = loader.getController();
        controller.initData(title,message);
        Scene newScene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        if(title.equals("SUCCESSFUL")){
            newStage.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(4));
            delay.setOnFinished( event -> newStage.close() );
            delay.play();
        }
        else{
            newStage.showAndWait();
        }
    }

    public void selectImage(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            imagePath = file.toURI().toString();
            imageName = file.getName();
            // Store file in the images folder
            Path to = Paths.get("C:/Users/LENOVO/Downloads/UniLinkUI/images", imageName);
            Files.copy(file.toPath(), to);
        }
    }

    public void numberValidation(TextField field){
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

}
