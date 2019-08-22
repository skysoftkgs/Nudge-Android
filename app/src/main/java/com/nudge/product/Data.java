package com.nudge.product;

import java.util.List;

/**
 * Created by pratap.kesaboyina on 30-11-2015.
 */
public class Data {



    private String title;
    private String subcategory_id;

    private List<Section> section;


    public Data() {

    }
    public Data(String title, String subcategory_id,List<Section> section) {
        this.title = title;
        this.subcategory_id = subcategory_id;
        this.section = section;

    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(String subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public List<Section> getSection() {
        return section;
    }

    public void setSection(List<Section> section) {
        this.section = section;
    }


}
