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
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;

import java.io.File;
import java.net.URL;
import java.util.*;

public class SaleEditController implements Initializable {
    PostModel postObject;
    Stage window;
    String loggedUser;
    public Label status;
    public TextField saleMinimumRaise;
    public Button closeButton;
    public Button deleteButton;
    public Button saveSale;
    boolean hasReplies;
    public Label mainTitle;
    public TextField saleTitle;
    public TextField saleDesc;
    public TableView repliesTable;
    public ImageView saleImage;
    public TextField saleAskingPrice;
    public TableColumn saleReply;
    public TableColumn responder;
    EditPostModel editPostModel;
    UniLinkModel uniLink;


    private ObservableList<ReplyModel> repliesAll = FXCollections.observableArrayList();

    public void goBackToMainMenu(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/UniLinkUI.fxml"));
        Parent mainViewParent = loader.load();
        Scene mainViewScene = new Scene(mainViewParent);
        UniLinkController controller = loader.getController();
        controller.initData(loggedUser,event, uniLink);
        window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainViewScene);
    }

    public void initData(PostModel item, Event event, UniLinkModel unilink) throws Exception {
        postObject = item;
        this.uniLink = unilink;
        Hashtable<String, String> saleReplies = EditPostModel.getRepliesJobSale(item);
        if(saleReplies==null){
            hasReplies =false;
        }
        else{
            saleReply.setCellValueFactory(
                    new PropertyValueFactory<ReplyModel,String>("value")
            );
            responder.setCellValueFactory(
                    new PropertyValueFactory<ReplyModel,String>("responderId")
            );
            Set<String> keys = saleReplies.keySet();
            Iterator<String> itr = keys.iterator();
            String str;
            while (itr.hasNext()) {
                str = itr.next();
                repliesAll.add(new ReplyModel(str, saleReplies.get(str)));
            }
            repliesTable.setItems(repliesAll);
        }

        saleMinimumRaise.setText(Double.toString( ((SaleModel)item).getSaleMinimumRaise() ));
        mainTitle.setText(item.getPostTitle());
        loggedUser = item.getCreatorId();
        status.setText(item.getStatus());
        saleDesc.setText(item.getDescription());
        saleAskingPrice.setText( Double.toString(((SaleModel)item).getSaleAskingPrice() ));
        saleTitle.setText(item.getPostTitle());
        if(item.getPostImage().equals("no_image_available")){
            File file = new File("C:/Users/LENOVO/Downloads/UniLinkUI/images/no_image_available.png");
            Image imagePath = new Image(file.toURI().toString());
            saleImage.setImage(imagePath);
            saleImage.setCache(true);
        }
        else{
            File file = new File("C:/Users/LENOVO/Downloads/UniLinkUI/images/"+item.getPostImage());
            Image imagePath = new Image(file.toURI().toString());
            saleImage.setImage(imagePath);
            saleImage.setCache(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editPostModel = new EditPostModel();
    }

    public void uploadImage(ActionEvent actionEvent) {
    }

    public void saveChanges(ActionEvent actionEvent) throws Exception {
        editPostModel.editSale(saleTitle.getText(), saleDesc.getText(), saleAskingPrice.getText(), saleMinimumRaise.getText(), postObject.getPostId());
        postObject.setPostTitle(saleTitle.getText());
        postObject.setDescription(saleDesc.getText());
        ((SaleModel) postObject).setSaleAskingPrice(Double.parseDouble(saleAskingPrice.getText()));
        ((SaleModel) postObject).setSaleMinimumRaise(Integer.parseInt(saleMinimumRaise.getText()));
    }

    public void saleClose(ActionEvent actionEvent) throws Exception{
        status.setText("CLOSE");
        postObject.setStatus("CLOSE");
        editPostModel.closePost(postObject);
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/message.fxml"));
        Parent root = (Parent) loader.load();
        ShowErrorController controller = loader.getController();
        controller.initData("SUCCESSFUL","SALE CLOSED SUCCESSFULLY");
        Scene newScene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished( event -> newStage.close() );
        delay.play();
    }

    public void saleDelete(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/deletePopup.fxml"));
        Parent root = (Parent) loader.load();
        Stage confirmDeleteWindow = new Stage();
        DeletePostController confirmDelete = loader.getController();
        if(hasReplies ==false){
            confirmDelete.initialize(postObject, editPostModel,false);
        }
        else{
            confirmDelete.initialize(postObject, editPostModel,true);
        }
        Scene scene = new Scene(root);
        confirmDeleteWindow.setScene(scene);
        confirmDeleteWindow.showAndWait();
        postObject =null;
    }
}
