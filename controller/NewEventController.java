package controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.EventModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class NewEventController {
    public DatePicker eventDate;
    public Button eventImage;
    public TextField eventCapacity;
    public TextField eventTitle;
    public TextField eventVenue;
    public TextArea eventDesc;
    public Button createNewEvent;
    String loggedInUser;
    String imagePath;
    String imageName;

    public void createEventOnClick(ActionEvent event) throws Exception{
        boolean inputCheck = false;

        if(eventDesc.getText().equals("")){
            displayMessage("ERROR","Description cannot be empty");
            inputCheck = false;
        }
        else if(eventDate.equals("")){
            displayMessage("ERROR","Minimum raise cannot be empty");
            inputCheck = false;
        }
        if(eventTitle.getText().equals("")){
            displayMessage("ERROR","Title cannot be empty");
            inputCheck = false;
        }
        if(eventVenue.getText().equals("")){
            displayMessage("ERROR","Asking price cannot be empty");
            inputCheck = false;
        }

        else{
            inputCheck = true;
        }
        if(inputCheck){
            LocalDate eventDate = this.eventDate.getValue();
            String eventVenue = this.eventVenue.getText();
            String eventTitle = this.eventTitle.getText();
            String eventCapacity = this.eventCapacity.getText();
            String eventDesc = this.eventDesc.getText();
            EventModel eventPost = new EventModel();
            if(imagePath==null){
                eventPost.makeNewEvent(loggedInUser, eventTitle, eventDesc, eventVenue, eventCapacity, eventDate, "no_image_available");
            }
            else{
                eventPost.makeNewEvent(loggedInUser, eventTitle, eventDesc, eventVenue, eventCapacity, eventDate, imageName);
            }
            Stage stage =  (Stage) createNewEvent.getScene().getWindow();
            stage.close();
            displayMessage("SUCCESS", "EVENT SUCCESSFULLY CREATED");
        }
    }

    public void initialize(String currentUser){
        this.loggedInUser = currentUser;
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
            Path to = Paths.get("C:/Users/LENOVO/Downloads/UniLinkUI/images", imageName);
            Files.copy(file.toPath(), to);
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
        if(!title.equals("SUCCESSFUL")){
            newStage.showAndWait();
        }
        else{
            newStage.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished( event -> newStage.close() );
            delay.play();
        }
    }
}
