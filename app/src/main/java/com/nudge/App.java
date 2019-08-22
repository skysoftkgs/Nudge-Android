package com.nudge;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.activeandroid.ActiveAndroid;
import com.nudge.model.CategoryDetail;
import com.nudge.model.RelationDetail;
import com.nudge.pojo.EventDetail;
import com.nudge.pojo.NudgesDetail;
import com.nudge.pojo.ProductDetail;
import com.nudge.pojo.UserGetProfileDetails;

import java.util.ArrayList;
import java.util.List;
public class App extends Application {
    private SharedPreferences defaultAppSharedPreferences;
    private static App instance;
    List<RelationDetail> relationArrayList = new ArrayList<>();
    List<CategoryDetail> categoryArrayList = new ArrayList<>();
    List<CategoryDetail> newCategoryArrayList = new ArrayList<>();
    List<EventDetail> eventArrayList = new ArrayList<>();
    List<ProductDetail> productDetailList = new ArrayList<>();
    List<UserGetProfileDetails> incompleteGetProfileDetails = new ArrayList<>();
    List<NudgesDetail> nudgeDetailList = new ArrayList<>();
    List<UserGetProfileDetails> completeGetProfileDetails = new ArrayList<>();
    List<CategoryDetail> oldCategoryArrayList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initDefaultPreferences();
        initApp();
        ActiveAndroid.initialize(this);

    }

    public List<CategoryDetail> getOldCategoryArrayList() {
        return oldCategoryArrayList;
    }

    public void setOldCategoryArrayList(List<CategoryDetail> oldCategoryArrayList) {
        this.oldCategoryArrayList = oldCategoryArrayList;
    }


    public List<UserGetProfileDetails> getCompleteGetProfileDetails() {
        return completeGetProfileDetails;
    }

    public void setCompleteGetProfileDetails(List<UserGetProfileDetails> completeGetProfileDetails) {
        this.completeGetProfileDetails = completeGetProfileDetails;
    }

    public List<NudgesDetail> getNudgeDetailList() {
        return nudgeDetailList;
    }

    public void setNudgeDetailList(List<NudgesDetail> nudgeDetailList) {
        this.nudgeDetailList = nudgeDetailList;
    }

    public List<ProductDetail> getProductDetailList() {
        return productDetailList;
    }

    public void setProductDetailList(List<ProductDetail> productDetailList) {
        this.productDetailList = productDetailList;
    }

    public List<UserGetProfileDetails> getIncompleteGetProfileDetails() {
        return incompleteGetProfileDetails;
    }

    public void setIncompleteGetProfileDetails(List<UserGetProfileDetails> incompleteGetProfileDetails) {
        this.incompleteGetProfileDetails = incompleteGetProfileDetails;
    }

    public List<EventDetail> getEventArrayList() {
        return eventArrayList;
    }

    public void setEventArrayList(List<EventDetail> eventArrayList) {
        this.eventArrayList = eventArrayList;
    }

    public List<CategoryDetail> getNewCategoryArrayList() {
        return newCategoryArrayList;
    }

    public void setNewCategoryArrayList(List<CategoryDetail> newCategoryArrayList) {
        this.newCategoryArrayList = newCategoryArrayList;
    }

    public List<RelationDetail> getRelationArrayList() {
        return relationArrayList;
    }

    public void setRelationArrayList(List<RelationDetail> relationArrayList) {
        this.relationArrayList = relationArrayList;
    }

    public List<CategoryDetail> getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(List<CategoryDetail> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }

    private void initDefaultPreferences() {
        defaultAppSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }
    public SharedPreferences getDefaultAppSharedPreferences() {
        return defaultAppSharedPreferences;
    }

    protected void initApp() {

    }


    public static App getInstance() {
        return instance;
    }
}
