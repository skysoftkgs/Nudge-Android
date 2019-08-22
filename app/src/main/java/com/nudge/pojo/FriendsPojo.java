package com.nudge.pojo;

import java.io.Serializable;

/**
 * Created by harshita on 6/12/2017.
 */
public class FriendsPojo implements Serializable {
    private String fb_Name;
    private String friend_pic;

    public FriendsPojo() {
    }

    public FriendsPojo(String fb_Name, String friend_pic) {
        this.fb_Name = fb_Name;
        this.friend_pic = friend_pic;
    }

    public String getFb_Name() {
        return fb_Name;
    }

    public void setFb_Name(String fb_Name) {
        this.fb_Name = fb_Name;
    }

    public String getFriend_pic() {
        return friend_pic;
    }

    public void setFriend_pic(String friend_pic) {
        this.friend_pic = friend_pic;
    }
}
