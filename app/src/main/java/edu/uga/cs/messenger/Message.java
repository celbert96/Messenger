package edu.uga.cs.messenger;

public class Message {
    private String recipientID;
    private String senderID;
    private String timestamp;
    private String content;

    public Message(String recipientID, String senderID, String timestamp,String content) {
        this.recipientID = recipientID;
        this.senderID = senderID;
        this.timestamp = timestamp;
        this.content = content;
    }

    //Firebase requires a no-arg constructor
    public Message()
    {
        this.recipientID = "";
        this.senderID = "";
        this.timestamp = "";
        this.content = "";
    }


    public String getRecipientID() {
        return recipientID;
    }
    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public String getSenderID() {
        return senderID;
    }
    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }








}
