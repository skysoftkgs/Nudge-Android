package com.nudge.model;

/**
 * Created by ADVANTAL on 7/7/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RelationDetail {

    @SerializedName("fw")
    @Expose
    private String fw;
    @SerializedName("rid")
    @Expose
    private String rid;
    @SerializedName("relation")
    @Expose
    private String relation;

    public String getFw() {
        return fw;
    }

    public void setFw(String fw) {
        this.fw = fw;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

}
