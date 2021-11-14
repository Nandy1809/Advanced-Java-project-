package controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.EditPostModel;
import model.EventModel;
import model.PostModel;
import model.UniLinkModel;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EventEditController implements Initializable {
    String loggedInUser;
    Stage window;
    PostModel post;
    public ImageView image;
    public TextField venue;
    public TextField attendeeCount;
    public Label status;
    public TextField description;
    public ListView attendeesList;
    public DatePicker date;
    public Label mainTitle;
    public TextField capacity;
    public TextField title;
    boolean hasReplies;

    UniLinkModel unilinkModel;
    EditPostModel editPost;


    public void initialize(PostModel postItem, Event event, UniLinkModel unilink) throws Exception {
        post = postItem;
        this.unilinkModel = unilink;

        ArrayList<String> eventAttendees = EditPostModel.getRepliesEvent(postItem);
        if(eventAttendees==null){
            hasReplies =false;
        }
        else{
            for (String attendee: eventAttendees) {
                attendeesList.getItems().add(attendee);
            }
        }

        mainTitle.setText(postItem.getPostTitle());
        title.setText(postItem.getPostTitle());
        description.setText(postItem.getDescription());
        venue.setText( ((EventModel)postItem).getEventVenue());
        capacity.setText( Integer.toString( ((EventModel)postItem).getEventCapacity() ));
        attendeeCount.setText(Integer.toString( ((EventModel)postItem).getEventAttendees() ));
        date.setValue(LocalDate.parse(((EventModel)postItem).getEventDate()));
        status.setText(postItem.getStatus());
        loggedInUser = postItem.getCreatorId();

        if( postItem.getPostImage().equals("no_image_available")){
            File file = new File("C:/Users/LENOVO/Downloads/UniLinkUI/images/no_image_available.png");
            Image imagePath = new Image(file.toURI().toString());
            image.setImage(imagePath);
            image.setCache(true);
        }
        else{
            File file = new File("C:/Users/LENOVO/Downloads/UniLinkUI/images/"+postItem.getPostImage());
            Image imagePath = new Image(file.toURI().toString());
            image.setImage(imagePath);
            image.setCache(true);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editPost = new EditPostModel();
    }

    public void closeEvent(ActionEvent actionEvent) throws Exception{
        status.setText("CLOSE");
        post.setStatus("CLOSE");
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/message.fxml"));
        Parent root = (Parent) loader.load();
        ShowErrorController controller = loader.getController();
        controller.initData("CLOSE SUCCESSFUL","EVENT SUCCESSFULLY CLOSEd");
        Scene newScene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished( event -> newStage.close() );
        delay.play();

    }

    public void deleteEvent(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/deletePopup.fxml"));
        Parent root = (Parent) loader.load();
        Stage confirmDeleteWindow = new Stage();
        DeletePostController confirmDelete = loader.getController();
        if(hasReplies==true){
            confirmDelete.initialize(post,editPost,true);
        }
        else{
            confirmDelete.initialize(post,editPost,false);
        }
        Scene scene = new Scene(root);
        confirmDeleteWindow.setScene(scene);
        confirmDeleteWindow.showAndWait();
    }

    public void saveChanges(ActionEvent actionEvent) throws Exception {
        date.setValue(LocalDate.parse(((EventModel)post).getEventDate()));
        capacity.setText(Integer.toString(((EventModel)post).getEventCapacity()));
        description.setText(post.getDescription());
        venue.setText(((EventModel)post).getEventVenue());
        title.setText(post.getPostTitle());
        editPost.editEvent(title.getText(), description.getText(), venue.getText(), date.getValue() ,capacity.getText(),post.getPostId());
    }

    public void goToMainPage(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/UniLinkUI.fxml"));
        Parent mainViewParent = loader.load();
        Scene mainViewScene = new Scene(mainViewParent);
        UniLinkController controller = loader.getController();
        controller.initData(loggedInUser,event, unilinkModel);
        window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainViewScene);
    }

}
