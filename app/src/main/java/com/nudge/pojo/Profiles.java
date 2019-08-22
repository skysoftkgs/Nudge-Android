package com.nudge.pojo;

/**
 * Created by ADVANTAL on 6/29/2017.
 */

public class Profiles
{
    private String relationship;

    private String fb_id;

    private String contact_no;

    private String dob;

    private String name;

    private String image;

    private String gender;

    private String type;

    public String getRelationship ()
    {
        return relationship;
    }

    public void setRelationship (String relationship)
    {
        this.relationship = relationship;
    }

    public String getFb_id ()
    {
        return fb_id;
    }

    public void setFb_id (String fb_id)
    {
        this.fb_id = fb_id;
    }

    public String getContact_no ()
    {
        return contact_no;
    }

    public void setContact_no (String contact_no)
    {
        this.contact_no = contact_no;
    }

    public String getDob ()
    {
        return dob;
    }

    public void setDob (String dob)
    {
        this.dob = dob;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [relationship = "+relationship+", fb_id = "+fb_id+", contact_no = "+contact_no+", dob = "+dob+", name = "+name+", image = "+image+", gender = "+gender+", type = "+type+"]";
    }
}
