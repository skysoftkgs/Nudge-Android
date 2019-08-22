package com.nudge.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ADVANTAL on 6/27/2017.
 */

@Table(name = "FriendList")

public class FriendList extends Model {

    //The table consist only one field name
    @Column(name = "name")
    public String name;

    @Column(name = "image" )
    public String image;

    @Column(name = "mobile" )
    public String mobile;
}
