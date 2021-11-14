package model;

import controller.ShowErrorController;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.database.ConnectDB;

import java.io.Serializable;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.Statement;

public class JobModel extends PostModel implements Serializable {

    Connection connection;
    private double jobLowestOffer;
    private double jobProposedPrice;

    public JobModel(String jobId,
                    String jobTitle,
                    String jobDesc,
                    String jobCreator,
                    String jobStatus,
                    double jobProposedPrice,
                    double jobLowestOffer,
                    String image){
        super(jobId, "JOB", jobTitle, jobDesc, jobCreator, jobStatus,image);
        this.jobLowestOffer = jobLowestOffer;
        this.jobProposedPrice = jobProposedPrice;
    }

    public JobModel(){}


    @Override
    public boolean handleReply(ReplyModel reply) {
        return false;
    }

    public void setJobLowestOffer(int jobLowestOffer) {
        this.jobLowestOffer = jobLowestOffer;
    }

    public double getJobLowestOffer() {
        return jobLowestOffer;
    }

    public void setJobProposedPrice(int jobProposedPrice) {
        this.jobProposedPrice = jobProposedPrice;
    }

    public double getJobProposedPrice() {
        return jobProposedPrice;
    }

    public void makeNewJob(String userId,
                           String title,
                           String description,
                           double proposedPrice,
                           String fileName) throws Exception{
        String postId="";
        connection = ConnectDB.connect();
        String getCount = "select total from POST_COUNT where TYPE = 'JOB'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(getCount);
        while(resultSet.next()) {
            int currentSaleCount = resultSet.getInt("total");
            postId = "JOB"+( String.format("%03d", ++currentSaleCount ));
        }

        String createJobPost = "INSERT INTO POST VALUES (" +
                "'" + postId + "'," +
                "'JOB'," +
                "'" + title + "'," +
                "'" + description + "'," +
                "'" + userId + "'," +
                "'OPEN'," +
                "'" + fileName + "')";
        statement.executeUpdate(createJobPost);
        connection.commit();

        String createJob = "INSERT INTO JOB VALUES (" +
                "'" + postId + "'," +
                proposedPrice + "," +
                proposedPrice + ")";
        statement.executeUpdate(createJob);
        connection.commit();

        String updateJobCount = "UPDATE POST_COUNT SET TOTAL = TOTAL + 1 WHERE TYPE = 'JOB';";
        statement.executeUpdate(updateJobCount);
    }

    public void displayMessage(String title, String message) throws Exception{
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

    public boolean handleReply(Double jobBid, String loggedInUser) throws Exception{
        if(jobBid < 0){
            displayMessage("Invalid Offer","Offer cannot be less than 0");
            return false;
        }
        else if( jobBid > jobLowestOffer){
            displayMessage("Invalid Offer","Offer must be lower than the current lowest offer");
            return false;
        }
        else{
            connection = ConnectDB.connect();
            Statement statement = connection.createStatement();
            String getRegisteredPrevious = "SELECT * FROM REPLY WHERE CREATORID = '" + loggedInUser + "' AND POSTID = '"+ getPostId() +"';";
            ResultSet getRegisteredPreviousRs = statement.executeQuery(getRegisteredPrevious);
            if(getRegisteredPreviousRs.next()){
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/message.fxml"));
                Parent root = (Parent) loader.load();
                ShowErrorController controller = loader.getController();
                controller.initData("UNSUCCESSFUL","CANNOT REPLY TO SAME JOB AGAIN");
                Scene newScene = new Scene(root);
                Stage newStage = new Stage();
                newStage.setScene(newScene);
                newStage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(4));
                delay.setOnFinished( event -> newStage.close() );
                delay.play();
                return false;
            }

            else{
                jobLowestOffer = jobBid;
                String setHighestOffer = "UPDATE JOB SET LOWEST_PRICE = " + jobBid + " WHERE ID = '"+getPostId()+"';";
                statement.executeUpdate(setHighestOffer);
                String insertReplyQuery = "INSERT INTO REPLY (REPLY, POSTID, CREATORID) VALUES" +
                        "(" + jobBid + ", " +
                        "'" + getPostId() + "', " +
                        "'" + loggedInUser + "');";
                statement.executeUpdate(insertReplyQuery);
                addReply(Double.toString(jobBid));

                if(jobBid<= jobProposedPrice){
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/message.fxml"));
                    Parent root = (Parent) loader.load();
                    ShowErrorController controller = loader.getController();
                    controller.initData("Congratulations!!","The JobModel has been assigned to you");
                    Scene newScene = new Scene(root);
                    Stage newStage = new Stage();
                    newStage.setScene(newScene);
                    newStage.show();
                    PauseTransition delay = new PauseTransition(Duration.seconds(2));
                    delay.setOnFinished( event -> newStage.close() );
                    delay.play();
                    this.setStatus("CLOSE");
                    String closeSaleQuery = "UPDATE POST SET STATUS = 'CLOSE' WHERE ID = " + getPostId() + ";";
                    int closeJobResult = statement.executeUpdate(closeSaleQuery);
                    return true;
                }
                else{
                    displayMessage("SUCCESSFUL", "ReplyModel successfully added for the sale");
                    return true;
                }


            }
        }
    }
}
