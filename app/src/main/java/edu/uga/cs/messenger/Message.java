package edu.uga.cs.messenger;

public class Message {
    private String recipientID;
    private String senderID;
    private long timestamp;
    private String messageID;
    private String content;

    public Message(String recipientID, String senderID, long timestamp, String messageID, String content) {
        this.recipientID = recipientID;
        this.senderID = senderID;
        this.timestamp = timestamp;
        this.messageID = messageID;
        this.content = content;
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

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageID() {
        return messageID;
    }
    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }








}
