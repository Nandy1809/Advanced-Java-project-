package model;

import model.database.ConnectDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class UniLinkModel {

    public ArrayList<JobModel> jobList = new ArrayList<JobModel>();
    public ArrayList<PostModel> postList = new ArrayList<PostModel>();
    public ArrayList<SaleModel> saleList = new ArrayList<SaleModel>();
    Connection connection;
    String loggedInUser;

    public void getDataFromDb() throws Exception {
        connection = ConnectDB.connect();
        if(saleList.size()==0){
            getSales(connection);
        }
        if(jobList.size()==0){
            getEvents(connection);
        }
        if(jobList.size()==0){
            getJobs(connection);
        }
    }

    public UniLinkModel(String currentUser){
        this.loggedInUser = currentUser;
    }

    public void getSales(Connection con) throws Exception{
        double saleMinimumRaise;
        String saleTitle="";
        double saleHighestOffer;
        String saleStatus="";
        String saleDesc="";
        double saleAskingPrice;
        String saleCreator="";
        String saleImage="";
        String geSalePosts = "select * from sale JOIN post on sale.ID = post.ID;";
        Statement stmt = con.createStatement();
        ResultSet resultSet = stmt.executeQuery(geSalePosts);
        while(resultSet.next()){
            saleStatus = resultSet.getString("status");
            String id    = resultSet.getString("id");
            saleTitle        = resultSet.getString("title");
            saleDesc  = resultSet.getString("description");
            saleCreator    = resultSet.getString("creator_id");
            saleAskingPrice  = resultSet.getDouble("asking_price");
            saleHighestOffer = resultSet.getDouble("highest_offer");
            saleMinimumRaise = resultSet.getDouble("minimum_raise");
            saleImage        = resultSet.getString("image");
            SaleModel sale = new SaleModel(id,saleTitle, saleDesc, saleCreator, saleStatus, saleAskingPrice, saleHighestOffer, saleMinimumRaise, saleImage);
            postList.add(sale);
        }
    }

    private void getJobs(Connection con)  throws Exception{
        String title="";
        double lowestOffer;
        String creatorId="";
        String image="";
        String description="";
        String status="";
        double proposedPrice;
        ArrayList<String> replies = new ArrayList<String>();
        String query = "select * from job JOIN post on job.ID = post.ID;";
        Statement stmt = con.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while(resultSet.next()){
            status = resultSet.getString("status");
            String id     = resultSet.getString("id");
            title         = resultSet.getString("title");
            description   = resultSet.getString("description");
            creatorId     = resultSet.getString("creator_id");
            lowestOffer   = resultSet.getDouble("lowest_price");
            proposedPrice = resultSet.getDouble("proposed_price");
            image        = resultSet.getString("image");
            JobModel job = new JobModel(id,title, description, creatorId, status, proposedPrice, lowestOffer,image);
            postList.add(job);
        }
    }


    public void getEvents(Connection con) throws Exception{
        String eventVenue="";
        String eventDesc="";
        int eventCapacity=0;
        int eventAttendeeCount=0;
        String eventStatus="";
        String eventTitle="";
        String eventDate="";
        String eventCreator="";
        String image="";
        String getEventPosts = "select * from event JOIN post on event.ID = post.ID;";
        Statement stmt = con.createStatement();
        ResultSet resultSet = stmt.executeQuery(getEventPosts);
        while(resultSet.next()){
            eventCapacity = resultSet.getInt("capacity");
            eventDate = resultSet.getString("date");
            eventVenue = resultSet.getString("VENUE");
            eventAttendeeCount = resultSet.getInt("attendee_count");
            eventDesc = resultSet.getString("description");
            eventCreator = resultSet.getString("creator_id");
            eventStatus = resultSet.getString("status");
            String id = resultSet.getString("id");
            eventTitle = resultSet.getString("title");
            image        = resultSet.getString("image");
            EventModel event = new EventModel(id,"EVENT", eventTitle, eventDesc, eventCreator, eventStatus, eventVenue, eventDate, eventCapacity, eventAttendeeCount,image);
            postList.add(event);
        }
    }
}

