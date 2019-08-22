package com.nudge.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nudge.App;
import com.nudge.R;
import com.nudge.alphabaticIndexSearch.ListSecond;
import com.nudge.model.CategoryDetail;
import com.nudge.model.IntentConstants;
import com.nudge.model.RelationDetail;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.BudgetDetail;
import com.nudge.pojo.CreateChoosePojo;
import com.nudge.pojo.EventDetail;
import com.nudge.pojo.ProductDetail;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by ADVANTAL on 2/19/2018.
 */
public class AddContactActivity extends AppCompatActivity {
    LinearLayout importFromPhoneLinearLayout,importFromFacebookLinearLayout,manuallyAddContactLinearLayout;
    String TAG = AddContactActivity.this.getClass().getSimpleName();
     List<CategoryDetail> newCategoryArrayList = new ArrayList<>();
    List<CategoryDetail> oldCategoryArrayList = new ArrayList<>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    RelativeLayout rl_back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        importFromPhoneLinearLayout = (LinearLayout)findViewById(R.id.importFromPhoneLinearLay);
        importFromFacebookLinearLayout = (LinearLayout)findViewById(R.id.importFromFacebookLinearLayout);
        manuallyAddContactLinearLayout = (LinearLayout)findViewById(R.id.manuallyAddContactLinearLayout);
        rl_back_arrow = (RelativeLayout)findViewById(R.id.rl_back_arrow);
        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        importFromFacebookLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddContactActivity.this,"Development in Progress",Toast.LENGTH_SHORT).show();
               // startActivity(new Intent(AddContactActivity.this,FacebookFriendListActivity.class));
            }
        });
        importFromPhoneLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddContactActivity.this,ListSecond.class));
            }
        });

        manuallyAddContactLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App app = (App) getApplication();
                oldCategoryArrayList = app.getOldCategoryArrayList();
                newCategoryArrayList = app.getNewCategoryArrayList();
                oldCategoryArrayList.clear();
                newCategoryArrayList.clear();
                sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                editor = sharedpreferences.edit();
                editor.putString(SharedPreferencesConstants.fromFriendListOptionsActivity,"fromFriendListOptionsActivity");
                editor.remove(SharedPreferencesConstants.createChoosePojoArrayList);
                editor.remove(SharedPreferencesConstants.friendPersonaList);
                editor.remove(SharedPreferencesConstants.friendEventList);
                editor.remove(SharedPreferencesConstants.friendLastName);
                editor.remove(SharedPreferencesConstants.friendBudget);
                editor.remove(SharedPreferencesConstants.friendId);
                editor.remove(SharedPreferencesConstants.friendName);
                editor.remove(SharedPreferencesConstants.friendGender);
                editor.remove(SharedPreferencesConstants.friendRelation);
                editor.remove(SharedPreferencesConstants.friendImage);
                editor.remove(SharedPreferencesConstants.fromCompleteFriendProfileActivity);
                editor.remove(SharedPreferencesConstants.friendEventListNew);
                editor.remove(SharedPreferencesConstants.fromCustomOccasionActivity);
                editor.commit();
                app.setOldCategoryArrayList(oldCategoryArrayList);
                app.setNewCategoryArrayList(newCategoryArrayList);
                Intent intent = new Intent(AddContactActivity.this,FriendDetailActivity.class);
                intent.putExtra(IntentConstants.addFriendDetail, true);
                startActivity(intent);
            }
        });
    }
}
