package controller;

import javafx.event.ActionEvent;
import model.EditPostModel;
import model.PostModel;

public class DeletePostController {
    PostModel post;
    EditPostModel postEdit;
    boolean replies;

    public void initialize(PostModel post, EditPostModel postEdit, boolean replies) {
        this.post=post;
        this.postEdit=postEdit;
        this.replies = replies;
    }

    public void deleteConfirmation(ActionEvent actionEvent) throws Exception {
        postEdit.deletePost(post,replies);
    }
}
