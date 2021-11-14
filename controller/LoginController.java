package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.LoginModel;


public class LoginController{
    public Text loginMessage;
    public Button loginButton;
    public TextField loginDetails;

    public void loginCliked(ActionEvent event) throws Exception {
        boolean usernameValidFlag = false;
        String userId = loginDetails.getText();
        for (int i = 1; i < userId.length(); i++) {
            if ((int) userId.charAt(i) > 57 || (int) userId.charAt(i) < 49) {
                loginMessage.setText("Username should contain numbers after 's'");
                break;
            }
            if (i == userId.length() - 1) {
                usernameValidFlag = true;
            }
        }
        if (Character.toLowerCase(userId.charAt(0)) != 's') {
            usernameValidFlag = false;
            loginMessage.setText("LoginModel should start with S/s");
        }
        else if (userId.equals("")) {
            loginMessage.setText("User name cannot be left blank");
        }
        else if (userId.length() > 8) {
            loginMessage.setText("Username cannot be longer than 7 characters");
        }
        if(usernameValidFlag) {
            LoginModel lg = new LoginModel();
            lg.processLogin(userId);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/UniLinkUI.fxml"));
            Parent mainViewParent = loader.load();
            Scene mainViewScene = new Scene(mainViewParent);
            UniLinkController controller = loader.getController();
            controller.initData(userId,event);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(mainViewScene);
        }
    }
}
