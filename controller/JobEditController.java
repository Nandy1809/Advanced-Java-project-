package controller;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;

import java.io.File;
import java.net.URL;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Set;

public class JobEditController implements Initializable {
    Stage window;
    String currentUser;
    PostModel post;
    public Label status;
    public Text lowestPrice;
    public TableColumn responder;
    public Text creatorId;
    public TextField description;
    public Button closeSale;
    boolean hasReplies;
    public TextField proposedPrice;
    public ImageView image;
    public TableColumn reply;
    public Button deleteSale;
    public TextField title;
    public Label mainTitle;
    public Button save;
    public TableView replies;

    UniLinkModel unilink;
    EditPostModel editPost;
    private ObservableList<ReplyModel> replyList = FXCollections.observableArrayList();

    public void initialize(PostModel item, Event event, UniLinkModel unilink) throws Exception {
        post = item;
        this.unilink = unilink;
        currentUser = item.getCreatorId();
        Hashtable<String, String> saleReplies = EditPostModel.getRepliesJobSale(item);
        if(saleReplies==null){
            hasReplies =false;
        }
        else{
            reply.setCellValueFactory(
                    new PropertyValueFactory<ReplyModel,String>("value")
            );
            responder.setCellValueFactory(
                    new PropertyValueFactory<ReplyModel,String>("responderId")
            );
            Set<String> keys = saleReplies.keySet();
            for(String key: keys) {
                replyList.add(new ReplyModel(key, saleReplies.get(key)));
            }
            replies.setItems(replyList);
        }

        if(item.getPostImage().equals("no_image_available")){
            File file = new File("C:/Users/LENOVO/Downloads/UniLinkUI/images/no_image_available.png");
            Image imagePath = new Image(file.toURI().toString());
            image.setImage(imagePath);
            image.setCache(true);
        }
        else{
            File file = new File("C:/Users/LENOVO/Downloads/UniLinkUI/images/"+item.getPostImage());
            Image imagePath = new Image(file.toURI().toString());
            image.setImage(imagePath);
            image.setCache(true);
        }

        mainTitle.setText(item.getPostTitle());
        title.setText(item.getPostTitle());
        description.setText(item.getDescription());
        status.setText(item.getStatus());
        proposedPrice.setText(Double.toString(((JobModel)item).getJobProposedPrice()));
        lowestPrice.setText(Double.toString(((JobModel)item).getJobLowestOffer()));
        creatorId.setText(item.getCreatorId());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editPost = new EditPostModel();
    }


    public void goBackToMainPage(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/UniLinkUI.fxml"));
        Parent mainViewParent = loader.load();
        Scene mainViewScene = new Scene(mainViewParent);
        UniLinkController controller = loader.getController();
        controller.initData(currentUser,event,unilink);
        window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainViewScene);
    }

    public void updateJob(ActionEvent actionEvent) throws Exception {
        description.setText(post.getDescription());
        proposedPrice.setText(Double.toString(((JobModel)post).getJobProposedPrice()));
        title.setText(post.getPostTitle());
        editPost.editJob(title.getText(), description.getText(), proposedPrice.getText(), post.getPostId());
    }

    public void deleteJob(ActionEvent actionEvent) throws Exception {
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
        post=null;
    }

    public void closeJob(ActionEvent actionEvent) throws Exception{
        status.setText("CLOSE");
        post.setStatus("CLOSE");
        editPost.closePost(post);
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/message.fxml"));
        Parent root = (Parent) loader.load();
        ShowErrorController controller = loader.getController();
        controller.initData("SUCCESSFUL","JOB SUCCESSFULLY CLOSED ");
        Scene newScene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished( event -> newStage.close() );
        delay.play();
    }


}
