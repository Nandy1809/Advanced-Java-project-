package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.EditPostModel;
import model.PostModel;
import model.UniLinkModel;

import java.net.URL;
import java.util.ResourceBundle;

public class PostEditController implements Initializable {

    Stage window;
    public TextArea details;
    UniLinkModel uniLinkModel;
    PostModel postObject;
    public Text title;
    EditPostModel editPostObject;
    String loggedInUser;

    public void goBackToMainPage(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/UniLinkUI.fxml"));
        Parent mainViewParent = loader.load();
        Scene mainViewScene = new Scene(mainViewParent);
        UniLinkController controller = loader.getController();
        controller.initData(loggedInUser,event, uniLinkModel);
        window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainViewScene);
    }

    public void initialize(PostModel postObject, Event event, UniLinkModel uniLink) {
        this.postObject = postObject;
        this.uniLinkModel = uniLink;
        title.setText(postObject.getPostTitle());
        details.setText(postObject.getDescription());
        loggedInUser = postObject.getCreatorId();
    }

    public void deletePost(ActionEvent actionEvent) {
    }

    public void closePost(ActionEvent actionEvent) {
        postObject.setStatus("CLOSE");
    }

    public void savePost(ActionEvent actionEvent) throws Exception{
        String description = details.getText();
        editPostObject.editPost(description, postObject.getPostId(), postObject.getType());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editPostObject = new EditPostModel();
    }
}
