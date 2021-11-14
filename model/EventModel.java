package model;

import model.database.ConnectDB;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class EventModel extends PostModel implements Serializable {

    Connection connection;
    private String eventVenue;
    private String eventDate;
    private int eventCapacity;
    private int eventAttendees;
    private String status;

    public String getEventVenue() {
        return eventVenue;
    }

    public int getEventAttendees() {
        return eventAttendees;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public void setEventCapacity(int eventCapacity) {
        this.eventCapacity = eventCapacity;
    }

    public int getEventCapacity() { return eventCapacity; }

    public void setEventAttendees(int eventAttendees) {
        this.eventAttendees = eventAttendees;
    }

    public String getEventDate() {
        return eventDate;
    }

    public EventModel(){}

    public EventModel(String id,
                      String eventType,
                      String eventTitle,
                      String description,
                      String username,
                      String status,
                      String venue,
                      String date,
                      int capacity,
                      int attendeeCount,
                      String image){

        super(id, eventType, eventTitle, description, username, status, image);
        this.eventCapacity = capacity;
        this.status = status;
        this.eventAttendees = attendeeCount;
        this.eventVenue = venue;
        this.eventDate = date;
    }


    public void closeEvent(String postId) throws Exception {
        connection = ConnectDB.connect();
        Statement stmt = connection.createStatement();
        String closeEvent = "UPDATE POST SET STATUS = 'CLOSE' WHERE ID = '" + postId + "'";
        stmt.executeUpdate(closeEvent);
    }

    public void makeNewEvent(String userId,
                             String title,
                             String description,
                             String venue,
                             String capacity,
                             LocalDate date,
                             String image) throws Exception{
        String postId="";
        connection = ConnectDB.connect();
        String getTotalEvents = "select total from POST_COUNT where TYPE = 'EVENT'";
        Statement stmt = connection.createStatement();
        ResultSet totalEventsRs = stmt.executeQuery(getTotalEvents);
        while(totalEventsRs.next()) {
            int currentEventCount = totalEventsRs.getInt("total");
            postId = "EVE"+( String.format("%03d", ++currentEventCount ));
        }

        String insertPost = "INSERT INTO POST VALUES (" +
                "'" + postId + "'," +
                "'EVENT'," +
                "'" + title + "'," +
                "'" + description + "'," +
                "'" + userId + "'," +
                "'OPEN'," +
                "'" + image + "')";
        int resultPost = stmt.executeUpdate(insertPost);
        if(resultPost == 1){
        }
        connection.commit();

        String putInEvent = "INSERT INTO EVENT VALUES (" +
                "'" + postId + "'," +
                "'" + venue + "'," +
                "'" +date + "'," +
                capacity + "," +
                eventAttendees + ");";

        stmt.executeUpdate(putInEvent);
        connection.commit();
        String updateSaleCount = "UPDATE POST_COUNT SET TOTAL = TOTAL + 1 WHERE TYPE = 'EVENT';";
        stmt.executeUpdate(updateSaleCount);
        stmt.close();
        connection.close();
    }

    public String updateAttendees(String currentUser, PostModel post) throws Exception {
        connection = ConnectDB.connect();
        Statement stmt = connection.createStatement();
        String checkRegistered = "SELECT * FROM REPLY WHERE CREATORID = '" + currentUser + "' AND POSTID = '"+ post.getPostId() +"';";
        ResultSet alreadyRegisteredSet = stmt.executeQuery(checkRegistered);
        if(alreadyRegisteredSet.next()){
            return "ALREADY_REGISTERED";
        }
        else{
            String addReply = "INSERT INTO REPLY (POSTID, CREATORID) VALUES (" +
                    "'" + post.getPostId() + "', " +
                    "'" + currentUser + "');";
            int resultPost = stmt.executeUpdate(addReply);
            String updateEventTableQuery = "UPDATE EVENT SET ATTENDEE_COUNT = ATTENDEE_COUNT + 1 WHERE ID = '"+ post.getPostId() +"';";
            stmt.executeUpdate(updateEventTableQuery);
            post.addReply(currentUser);

            if(resultPost==1){
                connection.close();
                stmt.close();
                return "SUCCESSFUL";
            }
            else{
                connection.close();
                stmt.close();
                return "UNSUCCESSFUL";
            }
        }
    }

    @Override
    public boolean handleReply(ReplyModel reply) {
        return false;
    }
}
