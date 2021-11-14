package controller;

import javafx.beans.value.ChangeListener;
import javafx.stage.Stage;
import javafx.beans.value.ObservableValue;
import java.net.URL;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import model.JobModel;
import java.util.ResourceBundle;

public class ReplyToJobController implements Initializable {

    String loggedInUser;
    JobModel jobObject;
    public TextField jobReply;
    public AnchorPane jobReplyPane;

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

    public void initData(String currentUser, JobModel item) {
        this.loggedInUser = currentUser;
        jobObject = item;
    }

    public void replyToJob(ActionEvent actionEvent) throws Exception {
        String jobReplyStr = jobReply.getText();
        boolean saveSuccessful =  jobObject.handleReply(Double.parseDouble(jobReplyStr), loggedInUser);
        if(saveSuccessful){
            Stage stage = (Stage) jobReplyPane.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numberValidation(jobReply);
    }
}
