package controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.SaleModel;
import java.net.URL;
import java.util.ResourceBundle;

public class ReplyToSaleController implements Initializable {
    public AnchorPane saleReplyPane;
    String loggedInUser;
    SaleModel saleObject;
    public TextField replyText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numberValidation(replyText);
    }

    public void addSaleReply(ActionEvent actionEvent) throws Exception {
        String saleReplyStr = replyText.getText();
        boolean saveSuccessful =  saleObject.handleReply(Double.parseDouble(saleReplyStr), loggedInUser);
        if(saveSuccessful){
            Stage stage = (Stage) replyText.getScene().getWindow();
            stage.close();
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

    public void initialize(String currentUser, SaleModel saleObject) {
        this.loggedInUser = currentUser;
        this.saleObject = saleObject;
    }
}
