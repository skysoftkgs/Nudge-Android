package com.nudge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.nudge.R;
import com.nudge.alphabaticIndexSearch.IndexFastScrollRecyclerView;
import com.nudge.alphabaticIndexSearch.RecyclerAdpaterSecondScreen;
import com.nudge.pojo.Pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ADVANTAL on 3/20/2018.
 */

public class FacebookFriendListActivity extends AppCompatActivity {

    @Bind(R.id.fast_scroller_recycler)
    IndexFastScrollRecyclerView mRecyclerView;
    private ImageView backArrowImage;
    ArrayList<Pojo> arrayList=new ArrayList<>();
    private EditText searchEditText;
    private LinearLayout btnDone;
    private ImageView settingsImageView;
    RecyclerAdpaterSecondScreen recyclerAdpaterSecondScreen;
    TextView doneTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_friends_list);
        ButterKnife.bind(this);
        backArrowImage = (ImageView)findViewById(R.id.backArrowImage);
        searchEditText = (EditText)findViewById(R.id.search);
        doneTextView = (TextView)findViewById(R.id.doneText) ;
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        doneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FacebookFriendListActivity.this, SignUpFourActivity.class));
            }
        });
        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest graphRequest = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                try {
                    if(jsonObject !=null) {
                        JSONArray jsonArrayFriends = jsonObject.getJSONObject("friendlist").getJSONArray("data");
                        JSONObject friendlistObject = jsonArrayFriends.getJSONObject(0);
                        String friendListID = friendlistObject.getString("id");
                        myNewGraphReq(friendListID);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle param = new Bundle();
        param.putString("fields", "friendlist,members");
        graphRequest.setParameters(param);
        graphRequest.executeAsync();
        initialiseUI(arrayList);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = searchEditText.getText().toString().toLowerCase(Locale.getDefault());
                recyclerAdpaterSecondScreen.filter(text);
            }
        });
    }

    private List<String> getFriendsList() {
        final List<String> friendslist = new ArrayList<String>();
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/friends", null, HttpMethod.GET, new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
/* handle the result */
                Log.e("Friends List: 1", response.toString());
                try {
                    JSONObject responseObject = response.getJSONObject();
                    JSONArray dataArray = responseObject.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObject = dataArray.getJSONObject(i);
                        String fbId = dataObject.getString("id");
                        String fbName = dataObject.getString("name");
                        Log.e("FbId", fbId);
                        Log.e("FbName", fbName);
                        friendslist.add(fbId);
                    }
                    Log.e("fbfriendList", friendslist.toString());
                    List<String> list = friendslist;
                    String friends = "";
                    if (list != null && list.size() > 0) {
                        friends = list.toString();
                        if (friends.contains("[")) {
                            friends = (friends.substring(1, friends.length()-1));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        }).executeAsync();
        return friendslist;
    }


    protected void initialiseUI(List<Pojo> mDataArray) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Collections.sort(mDataArray, new Comparator<Pojo>() {
            public int compare(Pojo v1, Pojo v2) {
                return v1.getContact_name().compareTo(v2.getContact_name());
            }
        });
        recyclerAdpaterSecondScreen = new RecyclerAdpaterSecondScreen(mDataArray,FacebookFriendListActivity.this);
        mRecyclerView.setAdapter(recyclerAdpaterSecondScreen);

        mRecyclerView.setIndexTextSize(12);
        mRecyclerView.setIndexBarColor("#ffffff");
        mRecyclerView.setIndexBarCornerRadius(0);
        mRecyclerView.setIndexBarTransparentValue((float) 0.4);
        mRecyclerView.setIndexbarMargin(0);
        mRecyclerView.setIndexbarWidth(35);
        mRecyclerView.setPreviewPadding(0);
        mRecyclerView.setIndexBarTextColor("#a8a8a8");

        mRecyclerView.setIndexBarVisibility(true);
        mRecyclerView.setIndexbarHighLateTextColor("#1D1E1C");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);
    }

    private void myNewGraphReq(String friendlistId) {
        final String graphPath = "/"+friendlistId+"/members/";
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = new GraphRequest(token, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                JSONObject object = graphResponse.getJSONObject();
                try {
                    JSONArray arrayOfUsersInFriendList= object.getJSONArray("data");
                    JSONObject user = arrayOfUsersInFriendList.getJSONObject(0);
                    String usersName = user.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle param = new Bundle();
        param.putString("fields", "name");
        request.setParameters(param);
        request.executeAsync();
    }
}
