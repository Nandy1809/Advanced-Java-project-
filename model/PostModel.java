package model;

import java.util.ArrayList;

public abstract class PostModel {

    private ArrayList<String> postReplies;
    private String status;
    private String postTitle;
    private String postImage;
    private String creatorId;
    private String description;
    private String type;
    private String postId;

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostId() {
        return postId;
    }

    public PostModel(){}

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void addReply(String reply) { this.postReplies.add(reply); }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getDescription() {
        return description;
    }

    public String getPostImage() {
        return postImage;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostId(String postId) { this.postId = postId; }

    public ArrayList<String> getPostReplies() {
        return postReplies;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPostReplies(ArrayList<String> postReplies) {
        this.postReplies = postReplies;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public PostModel(String postId, String type, String title, String description, String creatorId, String status, String image) {
        this.postId=postId;
        this.type=type;
        this.postTitle =title;
        this.description=description;
        this.creatorId=creatorId;
        this.status=status;
        this.postImage =image;
        this.postReplies = new ArrayList<String>();
    }


    public String fetchPostDetails(){
        return postId+":"+ postTitle +":"+description+":"+creatorId+":"+status;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public abstract boolean handleReply(ReplyModel reply);
}
