package edu.uga.cs.messenger;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable
{
    private String gid;
    private String groupname;
    private String imageURL;
    private String uid;
    private ArrayList<String> uidList;

    public Group(String gid, String groupname, String imageURL, ArrayList<String> uidList)
    {
        this.gid = gid;
        this.groupname = groupname;
        this.imageURL = imageURL;
        this.uidList = uidList;
    }

    public Group()
    {
        this.gid = "";
        this.groupname = "";
        this.imageURL = "";
        this.uidList = null;
        this.uid = "";
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getGid() {
        return gid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public void addUidlist (String uid){
        uidList.add(this.uid);
    }
    public ArrayList<String> getUidList(ArrayList<String> uidList) {
        return uidList;
    }

}
