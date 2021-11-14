package controller;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import model.*;

import java.io.*;
import java.net.URL;
import java.util.*;


public class UniLinkController implements Initializable {

    Stage window;
    UniLinkModel unilink;
    String currentUser = null;


    public MenuBar menu_bar;
    public Text user_name;
    public ListView<PostModel> posts;
    public ScrollPane scrollArea;
    public StackPane stackPane;
    public ComboBox type;
    public ComboBox status;
    public ComboBox creator;


    // Load the current user posts
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        type.getItems().setAll("ALL","SALE", "EVENT", "JOB");
        status.getItems().setAll("ALL","OPEN", "CLOSED");
        creator.getItems().setAll("ALL","MY POSTS");
    }

    ObservableList<String> list = FXCollections.observableArrayList();


    // Probably delete
    public void initData(String userId, ActionEvent event, UniLinkModel unilink){
        currentUser = userId;
        // Set username to the userId
        user_name.setText("Welcome " + userId);
         
        this.unilink = unilink;
        // Print data on the main window
        printPosts(event);
    }

    public void initData(String userId, ActionEvent event) throws Exception{
        currentUser = userId;
        // Set username to the userId
        user_name.setText("Welcome " + userId);

        // Setting the current logged in user in the model
        unilink = new UniLinkModel(currentUser);

        // Load the data of the user from db in model
        unilink.getDataFromDb();

        // Print data on the main window
        printPosts(event);

        creator.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                if (oldValue != null) {
                    switch(oldValue) {
                        case "ALL":  printPosts(event); break;
                        case "MY POSTS": printMyPosts(event); break;
                    }
                }
                if (newValue != null) {
                    switch(newValue) {
                        case "ALL":  printPosts(event); break;
                        case "MY POSTS": printMyPosts(event); break;
                    }
                }
            }
        });

        status.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                if (oldValue != null) {
                    switch(oldValue) {
                        case "ALL":  printPosts(event); break;
                        case "OPEN": printOpenPosts(event); break;
                        case "CLOSED": printClosedPosts(event); break;
                    }
                }
                if (newValue != null) {
                    switch(newValue) {
                        case "ALL":  printPosts(event); break;
                        case "OPEN": printOpenPosts(event); break;
                        case "CLOSED": printClosedPosts(event); break;
                    }
                }
            }
        });



        type.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                if (oldValue != null) {
                    switch(oldValue) {
                        case "ALL":  printPosts(event); break;
                        case "SALE": printSales(event); break;
                        case "EVENT": printEvents(event); break;
                        case "JOB": printJobs(event); break;
                    }
                }
                if (newValue != null) {
                    switch(newValue) {
                        case "ALL":  printPosts(event); break;
                        case "SALE": printSales(event); break;
                        case "EVENT": printEvents(event); break;
                        case "JOB": printJobs(event); break;
                    }
                }
            }
        });

    }

    private void printMyPosts(ActionEvent event){
        ObservableList<PostModel> openData = FXCollections.observableArrayList();
        ArrayList<PostModel> openPosts = new ArrayList<PostModel>();
        for (PostModel post : unilink.postList) {
            if(post.getCreatorId().equals(currentUser)){
                openPosts.add(post);
            }
        }
        openData.addAll(openPosts);
        final ListView<PostModel> posts = new ListView<PostModel>(openData);
        posts.setCellFactory(new Callback<ListView<PostModel>, ListCell<PostModel>>() {
            public CustomListCell call(ListView<PostModel> posts) {
                return new CustomListCell();
            }
        });
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        StackPane root = new StackPane();
        stackPane.getChildren().add(posts);
    }

    private void printOpenPosts(ActionEvent event){
        ObservableList<PostModel> openData = FXCollections.observableArrayList();
        ArrayList<PostModel> openPosts = new ArrayList<PostModel>();
        for (PostModel post : unilink.postList) {
            if(post.getStatus().equals("OPEN")){
                openPosts.add(post);
            }
        }
        openData.addAll(openPosts);
        final ListView<PostModel> posts = new ListView<PostModel>(openData);
        posts.setCellFactory(new Callback<ListView<PostModel>, ListCell<PostModel>>() {
            public CustomListCell call(ListView<PostModel> posts) {
                return new CustomListCell();
            }
        });
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        StackPane root = new StackPane();
        stackPane.getChildren().add(posts);
    }

    private void printClosedPosts(ActionEvent event){
        ObservableList<PostModel> closeData = FXCollections.observableArrayList();
        ArrayList<PostModel> closePosts = new ArrayList<PostModel>();
        for (PostModel post : unilink.postList) {
            if(post.getStatus().equals("CLOSE")){
                closePosts.add(post);
            }
        }
        closeData.addAll(closePosts);
        final ListView<PostModel> posts = new ListView<PostModel>(closeData);
        posts.setCellFactory(new Callback<ListView<PostModel>, ListCell<PostModel>>() {
            public CustomListCell call(ListView<PostModel> posts) {
                return new CustomListCell();
            }
        });
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        StackPane root = new StackPane();
        stackPane.getChildren().add(posts);
    }


    private void printSales(ActionEvent event){
        ObservableList<PostModel> saleData = FXCollections.observableArrayList();
        ArrayList<PostModel> salePosts = new ArrayList<PostModel>();
        for (PostModel post : unilink.postList) {
            if(post.getType().equals("SALE")){
                salePosts.add(post);
            }
        }
        saleData.addAll(salePosts);
        final ListView<PostModel> posts = new ListView<PostModel>(saleData);
        posts.setCellFactory(new Callback<ListView<PostModel>, ListCell<PostModel>>() {
            public CustomListCell call(ListView<PostModel> posts) {
                return new CustomListCell();
            }
        });
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        StackPane root = new StackPane();
        stackPane.getChildren().add(posts);
    }


    private void printJobs(ActionEvent event){
        ObservableList<PostModel> jobData = FXCollections.observableArrayList();
        ArrayList<PostModel> jobPosts = new ArrayList<PostModel>();
        for (PostModel post : unilink.postList) {
            if(post.getType().equals("JOB")){
                jobPosts.add(post);
            }
        }
        jobData.addAll(jobPosts);
        final ListView<PostModel> posts = new ListView<PostModel>(jobData);
        posts.setCellFactory(new Callback<ListView<PostModel>, ListCell<PostModel>>() {
            public CustomListCell call(ListView<PostModel> posts) {
                return new CustomListCell();
            }
        });
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        StackPane root = new StackPane();
        stackPane.getChildren().add(posts);
    }



    private void printEvents(ActionEvent event){
        ObservableList<PostModel> eventData = FXCollections.observableArrayList();
        ArrayList<PostModel> eventPosts = new ArrayList<PostModel>();
        for (PostModel post : unilink.postList) {
            if(post.getType().equals("EVENT")){
                eventPosts.add(post);
            }
        }
        eventData.addAll(eventPosts);
        final ListView<PostModel> posts = new ListView<PostModel>(eventData);
        posts.setCellFactory(new Callback<ListView<PostModel>, ListCell<PostModel>>() {
            public CustomListCell call(ListView<PostModel> posts) {
                return new CustomListCell();
            }
        });
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        StackPane root = new StackPane();
        stackPane.getChildren().add(posts);
    }


    private void printPosts(ActionEvent event) {
        ObservableList<PostModel> data = FXCollections.observableArrayList();
        data.addAll(unilink.postList);
        final ListView<PostModel> posts = new ListView<PostModel>(data);

        posts.setCellFactory(new Callback<ListView<PostModel>, ListCell<PostModel>>() {
            public CustomListCell call(ListView<PostModel> posts) {
                return new CustomListCell();
            }
        });

        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        StackPane root = new StackPane();
        stackPane.getChildren().add(posts);
    }

    public void exportDataToFile(ActionEvent actionEvent) throws Exception {
        Stage stage =  (Stage) menu_bar.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);
        String FolderLocation = selectedDirectory.toURI().toString();
        File exportFile = new File(selectedDirectory.getAbsolutePath()+"/export_data.txt");
        FileWriter file = new FileWriter(exportFile);

        String sb =  "";

        for (PostModel post:unilink.postList) {
            if(post.getType().equals("SALE")){
                sb = "\n"+post.getType()+":"+post.getCreatorId() + ":" + post.getPostTitle() + ":" + post.getDescription() + ":" +
                        post.getStatus() + ":" + post.getPostImage() + ":" + ((SaleModel)post).getSaleAskingPrice() + ":" +
                        ((SaleModel)post).getSaleHighestOffer() + ":" + ((SaleModel)post).getSaleMinimumRaise()+ ":" + ((SaleModel)post).getPostId();
                ArrayList<String> replies = EditPostModel.getRepliesJobSaleList(post.getPostId());
                if(replies!=null){
                    for(String reply :replies){
                        sb += ":"+reply;
                    }
                }
            }
            if(post.getType().equals("EVENT")){
                sb = "\n" + post.getType() + ":" +post.getCreatorId() + ":" + post.getPostTitle() + ":" + post.getDescription() + ":" +
                        post.getStatus() + ":" + post.getPostImage() + ":" + ((EventModel)post).getEventVenue() + ":" +
                        ((EventModel)post).getEventDate() + ":" + ((EventModel)post).getEventAttendees() + ":" + ((EventModel)post).getEventCapacity()+ ":" + ((EventModel)post).getPostId();
            }
            if(post.getType().equals("JOB")){
                sb = "\n" + post.getType()+ ":" +post.getCreatorId() + ":" + post.getPostTitle() + ":" + post.getDescription() + ":" +
                        post.getStatus() + ":" + post.getPostImage() + ":" + ((JobModel)post).getJobProposedPrice() + ":" +
                        ((JobModel)post).getJobLowestOffer() + ":" + ((JobModel)post).getPostId();
                ArrayList<String> replies = EditPostModel.getRepliesJobSaleList(post.getPostId());
                if(replies!=null){
                    for(String reply :replies){
                        sb += ":"+reply;
                    }
                }
            }
            file.write(sb);
        }
            file.close();

        showMessage("SUCCESSFUL","All the posts have been written to a file");
    }



    public void importDataFromFile(ActionEvent actionEvent) throws Exception{
        Stage stage =  (Stage) menu_bar.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterTXT = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.TXT");
        fileChooser.getExtensionFilters().addAll(extFilterTXT);
        File selectedFile = fileChooser.showOpenDialog(stage);

        Hashtable h = null;
        try{
            FileInputStream fileIn = new FileInputStream(selectedFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            h = (Hashtable)in.readObject();
            in.close();
            fileIn.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        for(Enumeration e = h.keys(); e.hasMoreElements();){
            Object obj = e.nextElement();
        }

    }

    private class CustomListCell extends ListCell<PostModel>{
        private HBox content;
        private GridPane listGrid;
        private Text title;
        private Text description;
        private Text creatorId;
        private Text status;
        private Text venue;
        private Text date;
        private Text capacity;
        private Text attendeeCount;
        private Button reply;
        private Button moreDetails;
        private Text highestOffer;
        private Text minimumRaise;
        private Text proposedPrice;
        private Text lowestOffer;
        private ImageView imageView;


        public CustomListCell() {
            super();
            title = new Text();
            description = new Text();
            creatorId = new Text();
            status = new Text();
            reply = new Button();
            moreDetails = new Button();
            imageView = new ImageView();
            venue = new Text();
            date = new Text();
            capacity = new Text();
            attendeeCount = new Text();
            highestOffer = new Text() ;
            minimumRaise = new Text();
            proposedPrice = new Text();
            lowestOffer = new Text();
//            listGrid = new GridPane();



//            listGrid.addColumn(0,imageView);

            VBox vBoxTitleDesc = new VBox(title, description, highestOffer, venue);
            vBoxTitleDesc.setSpacing(10);
            vBoxTitleDesc.setAlignment(Pos.TOP_LEFT);
//            listGrid.addColumn(1,vBoxTitleDesc);

            VBox vBoxCreatorStatus = new VBox(creatorId, status,minimumRaise,date);
            vBoxCreatorStatus.setSpacing(10);
            vBoxCreatorStatus.setAlignment(Pos.TOP_LEFT);
//            listGrid.addColumn(2,vBoxCreatorStatus);

            content = new HBox(imageView, vBoxTitleDesc,vBoxCreatorStatus, reply, moreDetails);
            content.setAlignment(Pos.TOP_LEFT);
            content.setSpacing(20);
//            content = new HBox(reply, moreDetails);
//            listGrid.addColumn(3,content);
//            listGrid.getColumnConstraints().add(new ColumnConstraints(70));
//            listGrid.getColumnConstraints().add(new ColumnConstraints(190));
//            listGrid.getColumnConstraints().add(new ColumnConstraints(200));
        }

        @Override
        protected void updateItem(PostModel item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) { // <== test for null item and empty parameter
                    title.setText("Title : " + item.getPostTitle());
                    description.setText("Description : " + item.getDescription());
                    creatorId.setText("Creator ID : " + item.getCreatorId());
                    status.setText("Status : " + item.getStatus());
                    reply.setText("ReplyModel");


                    getStylesheets().add("view/style.css");


                    // If EventModel is found, set the event details to values from the Posts arraylist
                    if(item.getType().equals("EVENT")){
                        setStyle("-fx-background-color: #E0FFFF;");
                        venue.setText("Venue : " + ((EventModel)item).getEventVenue());
                        capacity.setText("Capacity : " + Integer.toString(((EventModel)item).getEventCapacity()));
                        highestOffer.setText("Attendee Count : " + Integer.toString(((EventModel)item).getEventAttendees()));
                        minimumRaise.setText("EventModel Date : " +((EventModel)item).getEventDate());
                        proposedPrice.setText("");
                        lowestOffer.setText("");

                        if(item.getCreatorId().equals(currentUser)){
                            reply.setVisible(false);
                            moreDetails.setVisible(true);
                            moreDetails.setText("More Details");
                            moreDetailsAndReplyFunctionality(moreDetails,reply,item,currentUser);
                        }
                        else{
                            reply.setVisible(true);
                            reply.setText("Join");
                            moreDetails.setVisible(false);
                            moreDetailsAndReplyFunctionality(moreDetails,reply,item,currentUser);

                        }
                        if(item.getPostImage().equals("no_image_available")){
                            File file = new File("C:/Users/LENOVO/Downloads/UniLinkUI/images/no_image_available.png");
                            Image imagePath = new Image(file.toURI().toString(),100,100,false,false);
                            imageView.setImage(imagePath);
                            imageView.setCache(true);
                        }
                        else{
                            File file = new File("C:/Users/LENOVO/Downloads/UniLinkUI/images/"+item.getPostImage());
                            Image imagePath = new Image(file.toURI().toString(),100,100,false,false);
                            imageView.setImage(imagePath);
                            imageView.setCache(true);
                        }

                    }
                    // If SaleModel is found, set the sale details to values from the Posts arraylist
                    if(item.getType().equals("SALE")){
                        setStyle("-fx-background-color: #FFB6C1;");
                        highestOffer.setText("Highest Offer : " + Double.toString(((SaleModel)item ).getSaleHighestOffer()));
                        minimumRaise.setText("Minimum Raise : " + Double.toString(((SaleModel)item ).getSaleMinimumRaise()));
                        venue.setText("");
                        date.setText("");
                        capacity.setText("");
                        attendeeCount.setText("");
                        proposedPrice.setText("");
                        lowestOffer.setText("");

                        if(item.getCreatorId().equals(currentUser)){
                            reply.setVisible(false);
                            moreDetails.setVisible(true);
                            moreDetails.setText("More Details");
                            moreDetailsAndReplyFunctionality(moreDetails,reply,item,currentUser);
                        }
                        else{
                            reply.setVisible(true);
                            moreDetails.setVisible(false);
                            moreDetailsAndReplyFunctionality(moreDetails,reply,item,currentUser);
                        }
                        // Set image
                        if(item.getPostImage().equals("no_image_available")){
                            File file = new File("C:/Users/LENOVO/Downloads/UniLinkUI/images/no_image_available.png");
                            Image imagePath = new Image(file.toURI().toString(),100,100,false,false);
                            imageView.setImage(imagePath);
                            imageView.setCache(true);
                        }
                        else{
                            File file = new File("C:/Users/LENOVO/Downloads/UniLinkUI/images/"+item.getPostImage());
                            Image imagePath = new Image(file.toURI().toString(),100,100,false,false);
                            imageView.setImage(imagePath);
                            imageView.setCache(true);
                        }

                    }
                    // If JobModel is found, set the job details to values from the Posts arraylist
                    if(item.getType().equals("JOB")){
                        setStyle("-fx-background-color: #FFFFE0;");
                        minimumRaise.setText("Lowest Offer : " + Double.toString(((JobModel)item ).getJobLowestOffer()));
                        venue.setText("");
                        date.setText("");
                        capacity.setText("");
                        attendeeCount.setText("");

                        if(item.getCreatorId().equals(currentUser)){
                            reply.setVisible(false);
                            moreDetails.setVisible(true);
                            moreDetails.setText("More Details");
                            moreDetailsAndReplyFunctionality(moreDetails,reply,item,currentUser);
                        }
                        else{
                            reply.setVisible(true);
                            moreDetails.setVisible(false);
                            moreDetailsAndReplyFunctionality(moreDetails,reply,item,currentUser);
                        }
                        // Set image
                        if(item.getPostImage().equals("no_image_available")){
                            File file = new File("C:/Users/LENOVO/Downloads/UniLinkUI/images/no_image_available.png");
                            Image imagePath = new Image(file.toURI().toString(),100,100,false,false);
                            imageView.setImage(imagePath);
                            imageView.setCache(true);
                        }
                        else{
                            File file = new File("C:/Users/LENOVO/Downloads/UniLinkUI/images/"+item.getPostImage());
                            Image imagePath = new Image(file.toURI().toString(),100,100,false,false);
                            imageView.setImage(imagePath);
                            imageView.setCache(true);
                        }
                    }

                    setGraphic(content);

                } else {
                    setGraphic(null);
                }
        }

        private void moreDetailsAndReplyFunctionality(Button moreDetails, Button reply, PostModel item, String currentUser){
            // Check if the post belongs to the currently logged in user
            if(item.getCreatorId().equals(currentUser)){
//                reply.setVisible(false);
//                // Set the button details and action listener
//                moreDetails.setText("More Details");
                moreDetails.setOnAction(new EventHandler() {
                    @Override
                    public void handle(javafx.event.Event event) {
                        FXMLLoader loader = new FXMLLoader();
                        if(item.getType().equals("EVENT")){
                            loader.setLocation(getClass().getClassLoader().getResource("view/editEvent.fxml"));
                        }
                        else if(item.getType().equals("SALE")){
                            loader.setLocation(getClass().getClassLoader().getResource("view/editSale.fxml"));
                        }
                        else if(item.getType().equals("JOB")){
                            loader.setLocation(getClass().getClassLoader().getResource("view/editJob.fxml"));
                        }
                        Parent mainViewParent = null;
                        try {
                            mainViewParent = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene mainViewScene = new Scene(mainViewParent);

                        // Initialize the controller
                        if(item.getType().equals("EVENT")){
                            EventEditController controller = loader.getController();
                            try {
                                controller.initialize(item,event,unilink);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(item.getType().equals("SALE")){
                            SaleEditController controller = loader.getController();
                            try {
                                controller.initData(item,event,unilink);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(item.getType().equals("JOB")){
                            JobEditController controller = loader.getController();
                            try {
                                controller.initialize(item,event,unilink);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        // Get current stage information
                        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

                        // Set the main scene to the stage
                        window.setScene(mainViewScene);
                    }
                });
            }
            else
            {
                moreDetails.setVisible(false);
                reply.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Check if post is of type EventModel
                        if(item.getType().equals("EVENT")){
                            // Check if there is place to register i.e if event is open
                            if(item.getStatus().equals("OPEN")){

                                // Add the entry to database
                                EventModel eventObj = new EventModel();
                                try {
                                    // If entry successful then display the message to user
                                    if(eventObj.updateAttendees(currentUser,item).equals("SUCCESSFUL")){
                                        showMessage("Joined EventModel", "You have successfully registered for " + item.getPostTitle());
                                        // Increment the attendee count
                                        ((EventModel)item).setEventAttendees(
                                                ((EventModel)item).getEventAttendees() + 1
                                        );
                                        // If attendee count is full then close the event
                                        if(((EventModel) item).getEventAttendees()==((EventModel) item).getEventCapacity()){
                                            item.setStatus("CLOSE");
                                            eventObj.closeEvent(item.getPostId());
                                            status.setText("CLOSE");
                                        }
                                    }
                                    else if(eventObj.updateAttendees(currentUser,item).equals("ALREADY_REGISTERED")){
                                        showMessage("FAILED", "You have already registered for " + item.getPostTitle() + " and cannot register again.");
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            else{
                                reply.setVisible(false);
                            }
                        }
                        if(item.getType().equals("SALE")){
                            // Check if sale is open
                            if(item.getStatus().equals("OPEN")){
                                try {
                                    openSaleReplyWindow((SaleModel)item);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if(item.getType().equals("JOB")){
                            if(item.getStatus().equals("OPEN")){
                                try {
                                    openJobReplyWindow((JobModel)item);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                moreDetails.setVisible(false);
            }
        }

    }

    private void setEventDetails(PostModel item) {
    }

    private void openJobReplyWindow(JobModel item) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/replyJOb.fxml"));
        // Get the fxml info
        Parent newSaleScreen = loader.load();
        // Create new stage
        Stage newSaleWindow = new Stage();
        // Create new scene
        Scene scene = new Scene(newSaleScreen);
        // Access UniLinkUIMain Controller and store login information
        ReplyToJobController controller = loader.getController();
        controller.initData(currentUser,item);
        // Set scene to the stage
        newSaleWindow.setScene(scene);
        // Wait until the window is closed by the user
        newSaleWindow.showAndWait();
    }

    private void openSaleReplyWindow(SaleModel item) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/replySale.fxml"));
        // Get the fxml info
        Parent newSaleScreen = loader.load();
        // Create new stage
        Stage newSaleWindow = new Stage();
        // Create new scene
        Scene scene = new Scene(newSaleScreen);
        // Access UniLinkUIMain Controller and store login information
        ReplyToSaleController controller = loader.getController();
        controller.initialize(currentUser,item);
        // Set scene to the stage
        newSaleWindow.setScene(scene);
        // Wait until the window is closed by the user
        newSaleWindow.showAndWait();
    }


    public void logoutClicked(ActionEvent event) throws Exception{
        if(currentUser!=null){
            currentUser=null;
            // Navigate to mainController window
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/uniLinkLogin.fxml"));
            Parent loginViewParent = loader.load();
            Scene loginViewScene = new Scene(loginViewParent);
            // Get current stage information
            window = (Stage)((Node)event.getSource()).getScene().getWindow();
            // Set the main scene to the stage
            window.setScene(loginViewScene);
        }
    }


    public void createNewSale(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/newSale.fxml"));
        // Get the fxml info
        Parent newSaleScreen = loader.load();
        // Create new stage
        Stage newSaleWindow = new Stage();
        // Create new scene
        Scene scene = new Scene(newSaleScreen);
        // Access UniLinkUIMain Controller and store login information
        NewSaleController controller = loader.getController();
        controller.initData(currentUser);
        // Set scene to the stage
        newSaleWindow.setScene(scene);
        // Wait until the window is closed by the user
        newSaleWindow.showAndWait();
    }

    public void createNewEvent(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/newEvent.fxml"));
        // Get the fxml info
        Parent newSaleScreen = loader.load();
        // Create new stage
        Stage newSaleWindow = new Stage();
        // Create new scene
        Scene scene = new Scene(newSaleScreen);
        // Access UniLinkUIMain Controller and store login information
        NewEventController controller = loader.getController();
        controller.initialize(currentUser);
        // Set scene to the stage
        newSaleWindow.setScene(scene);
        // Wait until the window is closed by the user
        newSaleWindow.showAndWait();
    }

    public void createNewJob(ActionEvent actionEvent) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/newJob.fxml"));
        // Get the fxml info
        Parent newJobScreen = loader.load();
        // Create new stage
        Stage newJobWindow = new Stage();
        // Create new scene
        Scene scene = new Scene(newJobScreen);
        // Access UniLinkUIMain Controller and store login information
        NewJobController controller = loader.getController();
        controller.initData(currentUser);
        // Set scene to the stage
        newJobWindow.setScene(scene);
        // Wait until the window is closed by the user
        newJobWindow.showAndWait();
    }


    // Show developer information in a separate window
    public void developerInformation(ActionEvent event) throws Exception{
        // Get the fxml info
        Parent developerInfoScreen = FXMLLoader.load(getClass().getClassLoader().getResource("view/devInfo.fxml"));
        // Create new stage
        Stage developerInfoWindow = new Stage();
        // Create new scene
        Scene scene = new Scene(developerInfoScreen);
        // Set scene to the stage
        developerInfoWindow.setScene(scene);
        // Wait until the window is closed by the user
        developerInfoWindow.showAndWait();
    }

    private void showMessage(String title, String message) throws Exception{
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

    public void quitApplication(ActionEvent event){
        // Exit application
        window = (Stage)menu_bar.getScene().getWindow();
        window.close();
    }


}
