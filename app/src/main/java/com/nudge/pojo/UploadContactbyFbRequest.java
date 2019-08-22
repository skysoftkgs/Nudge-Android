package com.nudge.pojo;

/**
 * Created by ADVANTAL on 6/29/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadContactbyFbRequest {

    private String user_id;

    private Profiles[] profiles;

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public Profiles[] getProfiles ()
    {
        return profiles;
    }

    public void setProfiles (Profiles[] profiles)
    {
        this.profiles = profiles;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [user_id = "+user_id+", profiles = "+profiles+"]";
    }

}
