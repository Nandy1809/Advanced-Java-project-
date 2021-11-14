package model;

import java.sql.ResultSet;
import javafx.fxml.FXMLLoader;
import java.io.Serializable;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.animation.PauseTransition;
import javafx.stage.Stage;
import controller.ShowErrorController;
import model.database.ConnectDB;
import java.sql.Connection;
import javafx.scene.Scene;
import java.sql.Statement;

public class SaleModel extends PostModel implements Serializable {
    public SaleModel(){}

    Connection connection;
    private double saleAskingPrice;
    private double saleMinimumRaise;
    private double saleHighestOffer;
    public SaleModel(String saleId,
                     String saleTitle,
                     String saleDesc,
                     String saleCreator,
                     String saleStatus,
                     double saleAskingPrice,
                     double saleHighestOffer,
                     double saleMinimumRaise,
                     String image) {
        super(saleId, "SALE", saleTitle, saleDesc, saleCreator, saleStatus,image);
        this.saleMinimumRaise = saleMinimumRaise;
        this.saleHighestOffer = saleHighestOffer;
        this.saleAskingPrice = saleAskingPrice;
    }

    public void newSale(String userId,
                        String title,
                        String description,
                        double askingPrice,
                        double minimumRaise,
                        String image) throws Exception
    {
        String saleId="";
        connection = ConnectDB.connect();
        String totalSales = "select total from POST_COUNT where TYPE = 'SALE'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(totalSales);
        while(rs.next()) {
            int currentSaleCount = rs.getInt("total");
            saleId = "SAL"+( String.format("%03d", ++currentSaleCount ));
        }

        String insertSalePost = "INSERT INTO POST VALUES (" +
                "'" + saleId + "'," +
                "'SALE'," +
                "'" + title + "'," +
                "'" + description + "'," +
                "'" + userId + "'," +
                "'OPEN'," +
                "'" + image + "')";
        statement.executeUpdate(insertSalePost);
        connection.commit();

        String insertSale = "INSERT INTO SALE VALUES (" +
                    "'" + saleId + "'," +
                    askingPrice + "," +
                saleHighestOffer + "," +
                    minimumRaise + ")";

        statement.executeUpdate(insertSale);
        connection.commit();
        String updateSaleCount = "UPDATE POST_COUNT SET TOTAL = TOTAL + 1 WHERE TYPE = 'SALE';";
        statement.executeUpdate(updateSaleCount);
        connection.close();
        statement.close();
    }

    public double getSaleMinimumRaise() {
        return saleMinimumRaise;
    }

    public double getSaleHighestOffer() {
        return saleHighestOffer;
    }

    public void setSaleMinimumRaise(int saleMinimumRaise) {
        this.saleMinimumRaise = saleMinimumRaise;
    }

    public void setSaleAskingPrice(double saleAskingPrice) {
        this.saleAskingPrice = saleAskingPrice;
    }

    public void setSaleHighestOffer(int saleHighestOffer) {
        this.saleHighestOffer = saleHighestOffer;
    }

    public double getSaleAskingPrice() {
        return saleAskingPrice;
    }







    @Override
    public boolean handleReply(ReplyModel reply) {
        return false;
    }

    public boolean handleReply(Double replyValue, String currentUser) throws Exception{
        connection = ConnectDB.connect();
        Statement stmt = connection.createStatement();
        if(replyValue <= (saleHighestOffer + saleMinimumRaise)){
            showMessage("Invalid bid","Bid must be higher than the current highest bid");
            return false;
        }

        else if(replyValue < 0){
            showMessage("Invalid bid","Bid cannot be < 0");
            return true;
        }

        else{

            String checkAlreadyRegisteredQuery = "SELECT * FROM REPLY WHERE CREATORID = '" + currentUser + "' AND POSTID = '"+ getPostId() +"';";
            ResultSet alreadyRegisteredSet = stmt.executeQuery(checkAlreadyRegisteredQuery);
            if(alreadyRegisteredSet.next()){
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/message.fxml"));
                Parent root = (Parent) loader.load();
                ShowErrorController controller = loader.getController();
                controller.initData("FAILED","You cannot reply to the same job again.");
                Scene newScene = new Scene(root);
                Stage newStage = new Stage();
                newStage.setScene(newScene);
                newStage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished( event -> newStage.close() );
                delay.play();
                return false;
            }
            else{
                // Set to highest offer
                saleHighestOffer = replyValue;

                // Update the value in the database
                String updateSaleHighestOfferQuery = "UPDATE SALE SET HIGHEST_OFFER = " + replyValue + " WHERE ID = '"+getPostId()+"';";
                stmt = connection.createStatement();
                int updateSaleHighestOfferResult = stmt.executeUpdate(updateSaleHighestOfferQuery);

                // Insert the reply in the ReplyModel table
                String insertReplyQuery = "INSERT INTO REPLY (REPLY, POSTID, CREATORID) VALUES" +
                        "(" + replyValue + ", " +
                        "'" + getPostId() + "', " +
                        "'" + currentUser + "');";
                int insertReplyResult = stmt.executeUpdate(insertReplyQuery);
                addReply(Double.toString(replyValue));

                if(replyValue >= saleAskingPrice){
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/message.fxml"));
                    Parent root = (Parent) loader.load();
                    ShowErrorController controller = loader.getController();
                    controller.initData("Congratulations!!","The item has been sold to you.");
                    Scene newScene = new Scene(root);
                    Stage newStage = new Stage();
                    newStage.setScene(newScene);
                    newStage.show();
                    PauseTransition delay = new PauseTransition(Duration.seconds(2));
                    delay.setOnFinished( event -> newStage.close() );
                    delay.play();
                    // Update the status
                    this.setStatus("CLOSE");
                    // Update the status in the database
                    String closeSaleQuery = "UPDATE POST SET STATUS = 'CLOSE' WHERE ID = " + getPostId() + ";";
                    int closeSaleResult = stmt.executeUpdate(closeSaleQuery);
                    return true;
                }
                else{
                    showMessage("SUCCESSFUL", "ReplyModel successfully added for the sale");
                    return true;
                }
            }
        }

    }


    public void showMessage(String title, String message) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/message.fxml"));
        Parent root = (Parent) loader.load();
        ShowErrorController controller = loader.getController();
        controller.initData(title,message);
        Scene newScene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished( event -> newStage.close() );
        delay.play();
    }

}























