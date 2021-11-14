package model;

import javafx.beans.property.SimpleStringProperty;
public class ReplyModel {
    private SimpleStringProperty reply;
    private SimpleStringProperty responder;

    public ReplyModel(String responderId, String value){
        this.responder = new SimpleStringProperty(responderId);
        this.reply = new SimpleStringProperty(value);
    }
    public String getResponder() {
        return responder.get();
    }
    public String getReply() {
        return reply.get();
    }
    public void setResponder(String responder) {
        this.responder.set(responder);
    }
    public void setReply(String reply) {
        this.reply.set(reply);
    }




}
