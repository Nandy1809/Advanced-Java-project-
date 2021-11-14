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
import model.SaleModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class NewSaleController implements Initializable {
    public TextField saleAskingPrice;
    public TextField saleTitle;
    public TextField saleMinimumRaise;
    public Button createNewSale;
    public TextArea saleDesc;
    String loggedInUser;
    String imagePath;
    String imageName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numberValidation(saleAskingPrice);
        numberValidation(saleMinimumRaise);
    }

    public void initData(String currentUser){
        this.loggedInUser = currentUser;
    }

    public void createSaleOnClick(ActionEvent event) throws Exception{
        boolean inputCheck = false;
        if(saleDesc.getText().equals("")){
            displayMessage("ERROR","Description cannot be empty");
            inputCheck = false;
        }
        else if(saleTitle.getText().equals("")){
            displayMessage("ERROR","Title cannot be empty");
            inputCheck = false;
        }
        else if(saleAskingPrice.getText().equals("")){
            displayMessage("ERROR","Asking price cannot be empty");
            inputCheck = false;
        }
        else if(saleMinimumRaise.getText().equals("")){
            displayMessage("ERROR","Minimum raise cannot be empty");
            inputCheck = false;
        }
        else{
            inputCheck = true;
        }
        if(inputCheck){
            double minimumRaise = Double.parseDouble(saleMinimumRaise.getText());
            String saleDesc = this.saleDesc.getText();
            double askingPrice = Double.parseDouble(saleAskingPrice.getText());
            String saleTitle = this.saleTitle.getText();
            SaleModel sale = new SaleModel();
            if(imagePath !=null){
                sale.newSale(loggedInUser,saleTitle, saleDesc, askingPrice, minimumRaise, imageName);
            }
            else{
                sale.newSale(loggedInUser,saleTitle, saleDesc, askingPrice, minimumRaise, "no_image_available");
            }
            Stage stage =  (Stage) createNewSale.getScene().getWindow();
            stage.close();
            displayMessage("SUCCESSFUL", "SUCCESSFULLY CREATED NEW SALE");
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
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished( event -> newStage.close() );
            delay.play();
        }
        else{
            newStage.showAndWait();
        }
    }

    public void selectImage(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = file.toURI().toString();
            imageName = file.getName();
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
