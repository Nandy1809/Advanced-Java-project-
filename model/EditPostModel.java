package model;

import controller.ShowErrorController;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.database.ConnectDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public class EditPostModel {
    Connection connection;

    public EditPostModel(){
        connection = ConnectDB.connect();
    }

    public void editPost(String description, String postId, String postType) throws SQLException {
        String editPost = "UPDATE POST SET DESCRIPTION = '" + description + "' WHERE ID = " +"'" + postId + "';";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(editPost);
        stmt.close();
    }

    public void editEvent(String title, String description, String venue, LocalDate date, String capacity, String postId) throws Exception{
        String updateEventPost = "UPDATE POST SET TITLE = '" + title + "', " +
                "DESCRIPTION = '"+description+"'" +
                "WHERE ID = '" + postId +  "';";
        String updateEvent = "UPDATE EVENT SET " +
                "VENUE = '"+venue+ "'," +
                "DATE = '"+date+ "'," +
                "CAPACITY = "+capacity+" " +
                " WHERE ID = '" + postId + "';";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(updateEventPost);
        stmt.executeUpdate(updateEvent);
        stmt.close();
    }

    public void editSale(String title, String description, String askingPrice, String minimumRaise, String postId) throws Exception{
        Statement stmt = connection.createStatement();
        String updateSalePost = "UPDATE POST SET TITLE = '" + title + "', " +
                "DESCRIPTION = '"+description+"' " +
                "WHERE ID =  '" + postId + "';";
        stmt.executeUpdate(updateSalePost);
        String updateSale = "UPDATE SALE SET " +
                "ASKING_PRICE = "+askingPrice+ "," +
                "MINIMUM_RAISE = "+minimumRaise+" WHERE ID = '" + postId + "';";
        stmt.executeUpdate(updateSale);
        stmt.close();
        connection.commit();
    }

    public void editJob(String title, String description, String proposedPrice, String postId) throws Exception{
        String updateJobPost = "UPDATE POST SET TITLE = '" + title + "', " +
                "DESCRIPTION = '"+description+"'" +
                " WHERE ID = '" + postId +  "';";
        String updateJob = "UPDATE JOB SET " +
                "PROPOSED_PRICE = "+proposedPrice+ " " +
                "WHERE ID = '" + postId + "';";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(updateJobPost);
        stmt.executeUpdate(updateJob);
        stmt.close();
    }


    public static ArrayList getRepliesEvent(PostModel item) throws Exception{
        ArrayList<String> eventAttendees = new ArrayList<String>();
        Connection connect = ConnectDB.connect();
        Statement statement = connect.createStatement();
        String eventReplies = "SELECT CREATORID FROM REPLY WHERE POSTID = '"+item.getPostId()+"';";
        ResultSet repliesRs = statement.executeQuery(eventReplies);
        if(repliesRs==null){
            connect.close();
            statement.close();
            return null;
        }
        else {
            while(repliesRs.next()){
                eventAttendees.add(repliesRs.getString("creatorid"));
            }
            connect.close();
            statement.close();
            return eventAttendees;
        }
    }

    public static ArrayList getRepliesJobSaleList(String postId) throws Exception{
        ArrayList<String> jobSaleReplies = new ArrayList<String>();
        Connection connect = ConnectDB.connect();
        Statement statement = connect.createStatement();
        String repliesJob = "SELECT * FROM REPLY WHERE POSTID = '"+postId+"';";
        ResultSet jobReplyRs = statement.executeQuery(repliesJob);
        if(jobReplyRs!=null) {
            while (jobReplyRs.next()) {
                jobSaleReplies.add(jobReplyRs.getString("reply"));
            }
            connect.close();
            statement.close();
            return jobSaleReplies;
        }
        else{
            return null;
        }
    }

    public void closePost(PostModel post)  throws SQLException{
        String updatePost = "UPDATE POST SET STATUS = 'CLOSE' WHERE ID = '" + post.getPostId() + "';";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(updatePost);
        stmt.close();
    }



    public static Hashtable getRepliesJobSale(PostModel item) throws Exception{
        Hashtable<String, String> saleReplies = new Hashtable<String, String>();
        Connection connect = ConnectDB.connect();
        Statement stmt = connect.createStatement();
        String getReplies = "SELECT * FROM REPLY WHERE POSTID = '"+item.getPostId()+"';";
        ResultSet relpliesRs = stmt.executeQuery(getReplies);
        if(relpliesRs==null){
            return null;
        }
        else{
            while(relpliesRs.next()){
                saleReplies.put(relpliesRs.getString("creatorid"),relpliesRs.getString("reply"));
            }
            connect.close();
            stmt.close();
            return saleReplies;
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
        newStage.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished( event -> newStage.close() );
        delay.play();
    }

    public void deletePost(PostModel postObj, boolean postReplies) throws Exception {
        Statement stmt = connection.createStatement();
        if(postReplies==true){
            String deleteReplies = "DELETE FROM REPLY WHERE ID = '" + postObj.getPostId() + "';";
            stmt.executeUpdate(deleteReplies);
        }
        if(postObj.getType().equals("EVENT")){
            String deleteEvent = "DELETE FROM EVENT WHERE ID = '" + postObj.getPostId() + "';";
            stmt.executeUpdate(deleteEvent);
        }
        if(postObj.getType().equals("JOB")){
            String deleteJob = "DELETE FROM JOB WHERE ID = '" + postObj.getPostId() + "';";
            stmt.executeUpdate(deleteJob);
        }
        if(postObj.getType().equals("SALE")){
            String deleteSale = "DELETE FROM SALE WHERE ID = '" + postObj.getPostId() + "';";
            stmt.executeUpdate(deleteSale);
        }
        String deletePost = "DELETE FROM POST WHERE ID = '" + postObj.getPostId() + "';";
        stmt.executeUpdate(deletePost);
        stmt.close();
        displayMessage("DELETED","PostModel deleted successfully");
    }
}
