package edu.uga.cs.messenger;

public class User
{
    private String uid;
    private String username;
    private String imageURL;

    public User(String uid, String username, String imageURL)
    {
        this.uid = uid;
        this.username = username;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
