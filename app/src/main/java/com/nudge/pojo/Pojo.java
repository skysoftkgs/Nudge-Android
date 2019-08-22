package com.nudge.pojo;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by harshita on 6/12/2017.
 */
public class Pojo implements Serializable {
    //private String number;
   // private long contact_id;
    private String contact_name;
    private Uri image_uri;
    private String contact_number;


/* public Pojo() {
    }

    public Pojo(String number, long contact_id, String contact_name) {
        this.number = number;
        this.contact_id = contact_id;
        this.contact_name = contact_name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getContact_id() {
        return contact_id;
    }

    public void setContact_id(long contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }*/

    public Pojo() {
    }

    public Pojo(String contact_name, Uri image_uri,String  contact_number) {
        this.contact_name = contact_name;
        this.image_uri = image_uri;
        this.contact_number =contact_number;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public Uri getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(Uri image_uri) {
        this.image_uri = image_uri;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }
}
