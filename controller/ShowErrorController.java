package controller;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ShowErrorController{
    public Text titleError;
    public Text textError;
    public AnchorPane errorWindow;

    public void initData(String title, String message) throws Exception{
        titleError.setFill(Paint.valueOf("PURPLE"));
        textError.wrappingWidthProperty().bind(errorWindow.widthProperty());
        titleError.setText(title);
        textError.setText(message);
    }
}
