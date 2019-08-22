package com.nudge.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.adapter.RelationshipAdapter;
import com.nudge.model.FileUtils;
import com.nudge.model.IntentConstants;
import com.nudge.model.RecyclerViewClickListener;
import com.nudge.model.RecyclerViewTouchListener;
import com.nudge.model.RelationDetail;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.BudgetDetail;
import com.nudge.pojo.CreateChoosePojo;
import com.nudge.pojo.EventNew;
import com.nudge.pojo.UpdateProfileManualResponse;
import com.nudge.pojo.UploadManualContact;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADVANTAL on 2/19/2018.
 */

public class FriendDetailActivity extends AppCompatActivity {

    RelativeLayout nextRelativeLay;
    private EditText etRelationship,etBudget,etGender ,etLastName;
    SharedPreferences sharedpreferences;
    ApiInterface apiService;
    private List<EventNew> eventNewList = new ArrayList<>();
    Uri fileUri = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    final int MY_REQUEST_CAMERA=20;
    SharedPreferences.Editor editor;
    TextView editImage;
    private ImageView uploadPic,uploadPicTextDrawable;
    private EditText etName;
    private ProgressDialog progress;
    private String userId,friendName,friendLastName,friendIdStr,friendBirthDay,friendBudget,friendLocation,friendImage,friendGender,friendRelation;
    List<RelationDetail> relationArrayList = new ArrayList<>();
    List<RelationDetail> maleRelationArrayList = new ArrayList<>();
    List<RelationDetail> femaleRelationArrayList = new ArrayList<>();
    RelationshipAdapter relationshipAdapter;
    private String relationId = "1";
    private String isImage = "No";
    private RelativeLayout relationLinearLayout;
    Intent GalIntent;
    private String genderStr = "";
    TextView titleText;
    private String fromInComplete="false";
    RelativeLayout rl_back_arrow;
    ImageView etRelationshipImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile_details);
        nextRelativeLay = (RelativeLayout)findViewById(R.id.nextRelativeLay);
        etRelationship = (EditText)findViewById(R.id.etRelationship);
        editImage = (TextView)findViewById(R.id.editImage);
        uploadPic = (ImageView)findViewById(R.id.uploadPic);
        uploadPicTextDrawable = (ImageView)findViewById(R.id.uploadPicTextDrawable);
        etName = (EditText)findViewById(R.id.etName);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etGender = (EditText)findViewById(R.id.etGender);
        etRelationshipImage = (ImageView)findViewById(R.id.etRelationshipImage);
        relationLinearLayout = (RelativeLayout)findViewById(R.id.relationLinearLayout);
        titleText = (TextView)findViewById(R.id.titleText);
        rl_back_arrow = (RelativeLayout)findViewById(R.id.rl_back_arrow);
        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        etGender.setText("Gender");
        nextRelativeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendIdStr = sharedpreferences.getString(SharedPreferencesConstants.friendId,"");
                if(getIntent()!=null) {
                    if(getIntent().getStringExtra(IntentConstants.inComplete)!=null) {
                        fromInComplete = getIntent().getStringExtra(IntentConstants.inComplete);
                    }
                }
                if("true".equalsIgnoreCase(fromInComplete)){
                    updateData();
                }else if(friendIdStr != ""){
                    updateData();
                }
                else {
                    saveData();
                }
            }
        });

        etRelationship.setText(getResources().getString(R.string.relationship));
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
        friendName = sharedpreferences.getString(SharedPreferencesConstants.friendName,"");
        friendLastName = sharedpreferences.getString(SharedPreferencesConstants.friendLastName,"");
        friendBirthDay = sharedpreferences.getString(SharedPreferencesConstants.friendBirthDay,"");
        friendBudget = sharedpreferences.getString(SharedPreferencesConstants.friendBudget,"");
        friendGender = sharedpreferences.getString(SharedPreferencesConstants.friendGender,"");
        friendRelation = sharedpreferences.getString(SharedPreferencesConstants.friendRelation,"");
        friendLocation = sharedpreferences.getString(SharedPreferencesConstants.friendLocation,"");
        friendImage = sharedpreferences.getString(SharedPreferencesConstants.friendImage,"");
        String  relationDetailsExampleStr = sharedpreferences.getString(SharedPreferencesConstants.relationArrayList,"");
        Gson gson1 = new Gson();
        TypeToken<ArrayList<RelationDetail>> token1 = new TypeToken<ArrayList<RelationDetail>>() {
        };
        relationArrayList = gson1.fromJson(relationDetailsExampleStr, token1.getType());

        if(getIntent() !=null){
            if(getIntent().getStringExtra(IntentConstants.friendName)!= null){
                titleText.setText(getIntent().getStringExtra(IntentConstants.friendName));
            }

//            if(getIntent().getBooleanExtra(IntentConstants.addFriendDetail, false)){
//                titleText.setText(getString(R.string.proceedtxt));
//            }
        }
        if(!friendName.equalsIgnoreCase("")&&!friendName.equalsIgnoreCase(null)) {
            etName.setText(friendName);
        }
        if(!friendLastName.equalsIgnoreCase("")&&!friendLastName.equalsIgnoreCase(null)) {
            etLastName.setText(friendLastName);
        }

        if(!friendGender.equalsIgnoreCase("")&&!friendGender.equalsIgnoreCase(null)) {
            etGender.setText(friendGender);
            genderStr=friendGender;
        }
      try
      {
          if(!friendRelation.equalsIgnoreCase("")&&friendRelation!=null) {
              relationId = friendRelation;
              if(relationId != null) {
                  if(relationArrayList !=null) {
                      for (int i = 0; i < relationArrayList.size(); i++) {
                          if (relationId.equalsIgnoreCase(relationArrayList.get(i).getRid())) {
                              etRelationship.setText(relationArrayList.get(i).getRelation());
                          }
                      }
                  }
              }
          }
      }
catch (NullPointerException v)
{

}

        if(!friendImage.equalsIgnoreCase("")&&!friendImage.equalsIgnoreCase(null)) {
            uploadPic.setVisibility(View.VISIBLE);
            uploadPicTextDrawable.setVisibility(View.GONE);
            Picasso.with(FriendDetailActivity.this)
                    .load(SharedPreferencesConstants.imageBaseUrl+friendImage)
                    .into(uploadPic);
        }else if(friendName !=null && friendName.length()>1 ) {
            uploadPic.setVisibility(View.GONE);
            uploadPicTextDrawable.setVisibility(View.VISIBLE);
            String lastNameStr ="";

            if(friendName != null) {
                if(friendName.length() >1) {
                    String nameStr = friendName.substring(0, 1);
                    if(friendLastName !=null){
                        if(friendLastName.length() >1){
                            lastNameStr   = friendLastName.substring(0, 1);
                        }else {
                            nameStr = friendName.substring(0, 2);
                        }
                    }
                    TextDrawable drawable = TextDrawable.builder()
                            .beginConfig()
                            .textColor(Color.LTGRAY)
                            .useFont(Typeface.DEFAULT)
                            .fontSize(45) /* size in px */
                            .bold()
                            .toUpperCase()
                            .endConfig()
                            .buildRound(nameStr + lastNameStr, getResources().getColor(R.color.white));
                    uploadPicTextDrawable.setImageDrawable(drawable);
                }
            }
        }else {
            uploadPic.setVisibility(View.VISIBLE);
            uploadPicTextDrawable.setVisibility(View.GONE);
        }
        apiService = ApiClient.getClient().create(ApiInterface.class);

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                // onSelectImageClick(view);
            }
        });
try
{
    etRelationship.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final AlertDialog alertDialog = new AlertDialog.Builder(FriendDetailActivity.this).create();
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.custom, null);
            alertDialog.setView(convertView);
            RecyclerView lv = (RecyclerView) convertView.findViewById(R.id.listView1);
            maleRelationArrayList.clear();
            femaleRelationArrayList.clear();
            if(relationArrayList !=null) {
                for (int i = 0; i < relationArrayList.size(); i++) {
                    if (genderStr.equalsIgnoreCase("Male")) {
                        if (relationArrayList.get(i).getFw().equalsIgnoreCase("m")||relationArrayList.get(i).getFw().equalsIgnoreCase("b")) {
                            RelationDetail relationDetail = new RelationDetail();
                            relationDetail.setFw(relationArrayList.get(i).getFw());
                            relationDetail.setRelation(relationArrayList.get(i).getRelation());
                            relationDetail.setRid(relationArrayList.get(i).getRid());
                            maleRelationArrayList.add(relationDetail);
                        }
                        etGender.setText("Male");
                        genderStr = "Male";
                    } else if (genderStr.equalsIgnoreCase("Female")) {
                        if (relationArrayList.get(i).getFw().equalsIgnoreCase("f")||relationArrayList.get(i).getFw().equalsIgnoreCase("b")) {
                            RelationDetail relationDetail = new RelationDetail();
                            relationDetail.setFw(relationArrayList.get(i).getFw());
                            relationDetail.setRelation(relationArrayList.get(i).getRelation());
                            relationDetail.setRid(relationArrayList.get(i).getRid());
                            femaleRelationArrayList.add(relationDetail);
                        }
                        etGender.setText("Female");
                        genderStr = "Female";
                    }
                }
            }
            if(genderStr.equalsIgnoreCase("Male")){
                relationshipAdapter = new RelationshipAdapter(FriendDetailActivity.this,maleRelationArrayList,genderStr);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                lv.setLayoutManager(mLayoutManager);
                lv.setItemAnimator(new DefaultItemAnimator());
                lv.setAdapter(relationshipAdapter);
                alertDialog.show();
                lv.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), lv, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        etRelationship.setText(maleRelationArrayList.get(position).getRelation());
                        relationId = maleRelationArrayList.get(position).getRid();
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }else if(genderStr.equalsIgnoreCase("Female")){
                relationshipAdapter = new RelationshipAdapter(FriendDetailActivity.this,femaleRelationArrayList,genderStr);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                lv.setLayoutManager(mLayoutManager);
                lv.setItemAnimator(new DefaultItemAnimator());
                lv.setAdapter(relationshipAdapter);
                alertDialog.show();
                lv.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), lv, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        etRelationship.setText(femaleRelationArrayList.get(position).getRelation());
                        relationId = femaleRelationArrayList.get(position).getRid();
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }else {
                relationshipAdapter = new RelationshipAdapter(FriendDetailActivity.this,relationArrayList,genderStr);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                lv.setLayoutManager(mLayoutManager);
                lv.setItemAnimator(new DefaultItemAnimator());
                lv.setAdapter(relationshipAdapter);
                alertDialog.show();
                lv.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), lv, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        etRelationship.setText(relationArrayList.get(position).getRelation());
                        relationId = relationArrayList.get(position).getRid();
                        String fw =  relationArrayList.get(position).getFw();
                        if(fw !=null){
                            if(fw.equalsIgnoreCase("m")){
                                genderStr = "Male";
                                etGender.setText("Male");
                            }else {
                                genderStr = "Female";
                                etGender.setText("Female");
                            }
                        }
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }
        }
    });

}
catch (NullPointerException xfgf)
{

}
        relationLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(FriendDetailActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom, null);
                alertDialog.setView(convertView);
                RecyclerView lv = (RecyclerView) convertView.findViewById(R.id.listView1);

              try
              {
              }
              catch (NullPointerException xv)
              {

              }

                maleRelationArrayList.clear();
                femaleRelationArrayList.clear();
                for(int i=0; i<relationArrayList.size();i++){
                    if(genderStr.equalsIgnoreCase("Male")) {
                        if(relationArrayList.get(i).getFw().equalsIgnoreCase("m")){
                            RelationDetail relationDetail = new RelationDetail();
                            relationDetail.setFw(relationArrayList.get(i).getFw());
                            relationDetail.setRelation(relationArrayList.get(i).getRelation());
                            relationDetail.setRid(relationArrayList.get(i).getRid());
                            maleRelationArrayList.add(relationDetail);
                        }
                        etGender.setText("Male");
                        genderStr = "Male";
                    }else if(genderStr.equalsIgnoreCase("Female")) {
                        if(relationArrayList.get(i).getFw().equalsIgnoreCase("f")){
                            RelationDetail relationDetail = new RelationDetail();
                            relationDetail.setFw(relationArrayList.get(i).getFw());
                            relationDetail.setRelation(relationArrayList.get(i).getRelation());
                            relationDetail.setRid(relationArrayList.get(i).getRid());
                            femaleRelationArrayList.add(relationDetail);
                        }
                        etGender.setText("Female");
                        genderStr = "Female";
                    }
                }
                if(genderStr.equalsIgnoreCase("Male")){
                    relationshipAdapter = new RelationshipAdapter(FriendDetailActivity.this,maleRelationArrayList,genderStr);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    lv.setLayoutManager(mLayoutManager);
                    lv.setItemAnimator(new DefaultItemAnimator());
                    lv.setAdapter(relationshipAdapter);
                    alertDialog.show();
                    lv.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), lv, new RecyclerViewClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            etRelationship.setText(maleRelationArrayList.get(position).getRelation());
                            relationId = maleRelationArrayList.get(position).getRid();
                            alertDialog.dismiss();
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));
                }else if(genderStr.equalsIgnoreCase("Female")){
                    relationshipAdapter = new RelationshipAdapter(FriendDetailActivity.this,femaleRelationArrayList,genderStr);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    lv.setLayoutManager(mLayoutManager);
                    lv.setItemAnimator(new DefaultItemAnimator());
                    lv.setAdapter(relationshipAdapter);
                    alertDialog.show();
                    lv.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), lv, new RecyclerViewClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            etRelationship.setText(femaleRelationArrayList.get(position).getRelation());
                            relationId = femaleRelationArrayList.get(position).getRid();
                            alertDialog.dismiss();
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));
                }else {
                    relationshipAdapter = new RelationshipAdapter(FriendDetailActivity.this,relationArrayList,genderStr);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    lv.setLayoutManager(mLayoutManager);
                    lv.setItemAnimator(new DefaultItemAnimator());
                    lv.setAdapter(relationshipAdapter);
                    alertDialog.show();
                    lv.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), lv, new RecyclerViewClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            etRelationship.setText(relationArrayList.get(position).getRelation());
                            relationId = relationArrayList.get(position).getRid();
                            String fw =  relationArrayList.get(position).getFw();
                            if(fw !=null){
                                if(fw.equalsIgnoreCase("m")){
                                    genderStr = "Male";
                                    etGender.setText("Male");
                                }else {
                                    genderStr = "Female";
                                    etGender.setText("Female");
                                }
                            }
                            alertDialog.dismiss();
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));
                }
            }
        });

        etGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(FriendDetailActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.gender_layout, null);
                alertDialog.setView(convertView);
                ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendDetailActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.gender_arrays));
                lv.setAdapter(adapter);
                alertDialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id)
                    {
                        String[] names =   getResources().getStringArray(R.array.gender_arrays);
                        etGender.setText(names[position]);
                        genderStr = names[position];
                        etRelationship.setText(getResources().getString(R.string.relationship));
                        alertDialog.dismiss();
                    }});
            }
        });
    }

    protected void selectImage() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(FriendDetailActivity.this, Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(FriendDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //  ActivityCompat.requestPermissions(act, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CAMERA);
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CAMERA);
            } else if (ContextCompat.checkSelfPermission(FriendDetailActivity.this, Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(FriendDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(FriendDetailActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (options[which].equals("Take Photo")) {

                            cameraIntent();
                        } else if (options[which].equals("Choose from Gallery")) {

                            galleryIntent();
                        } else if (options[which].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        } else {

            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(FriendDetailActivity.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (options[which].equals("Take Photo")) {
                        cameraIntent();
                    } else if (options[which].equals("Choose from Gallery")) {
                        galleryIntent();
                    } else if (options[which].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    }

    private void galleryIntent() {
        GalIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // handle result of pick image chooser
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            Uri imageUri = CropImage.getPickImageResultUri(getApplicationContext(), data);
            startCropImageActivity(imageUri);
        }
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {

            Uri imageUri = CropImage.getPickImageResultUri(FriendDetailActivity.this, data);
            startCropImageActivity(imageUri);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                fileUri = result.getUri();
                uploadPic.setVisibility(View.VISIBLE);
                uploadPicTextDrawable.setVisibility(View.GONE);
                uploadPic.setImageURI(result.getUri());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                    uploadPic.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(FriendDetailActivity.this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(FriendDetailActivity.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (options[which].equals("Take Photo")) {
                        cameraIntent();
                    } else if (options[which].equals("Choose from Gallery")) {
                        galleryIntent();
                    } else if (options[which].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
            Toast.makeText(FriendDetailActivity.this, "Please Grant Required Camera and Storage Permissions", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    public void updateData() {
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
        if (etName.getText().toString().equalsIgnoreCase("")||etName.getText().toString().equalsIgnoreCase(null)) {
            Drawable dr = getResources().getDrawable(R.drawable.error_info_icon);
            dr.setBounds(0, 0, 50, 50);
            etName.setError(getResources().getString(R.string.invalid_name),dr);
        }else if (etLastName.getText().toString().equalsIgnoreCase("")||etLastName.getText().toString().equalsIgnoreCase(null)) {
            Drawable dr = getResources().getDrawable(R.drawable.error_info_icon);
            dr.setBounds(0, 0, 50, 50);
            etLastName.setError(getResources().getString(R.string.error_last_name),dr);
        }else if (etGender.getText().toString().equalsIgnoreCase("Gender")||etGender.getText().toString().equalsIgnoreCase(null)) {
            Toast.makeText(FriendDetailActivity.this,"Please select Gender",Toast.LENGTH_SHORT).show();
        }
        else if (etRelationship.getText().toString().equalsIgnoreCase("Relation")||etRelationship.getText().toString().equalsIgnoreCase(null)) {
            Toast.makeText(FriendDetailActivity.this,"Please select Relation",Toast.LENGTH_SHORT).show();
        }
      else {
            updateDataToServer();
        }
    }

    public void updateDataToServer() {

        if(Utils.haveNetworkConnection(FriendDetailActivity.this)) {
            sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
            editor = sharedpreferences.edit();
            userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
            // friendName = sharedpreferences.getString(SharedPreferencesConstants.friendName,"");
            friendIdStr = sharedpreferences.getString(SharedPreferencesConstants.friendId,"");
            friendName = etName.getText().toString();
            if(friendName.equalsIgnoreCase("")){
                Toast.makeText(FriendDetailActivity.this,"Please fill friend Name",Toast.LENGTH_LONG).show();
                return;
            }

            apiService = ApiClient.createService(ApiInterface.class);
            File file = FileUtils.getFile(FriendDetailActivity.this, fileUri);


            MultipartBody.Part body = null;

            if (fileUri != null) {
                final RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);


                if (fileUri != null)
                    isImage = "yes";
                body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            }


            String gender =   etGender.getText().toString();


            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("user_id", createPartFromString(userId));
            map.put("friend_id", createPartFromString(friendIdStr));
            map.put("name", createPartFromString(friendName));
            map.put("last_name", createPartFromString(etLastName.getText().toString()));
            map.put("isimage",createPartFromString(isImage));
            map.put("relationship", createPartFromString(relationId));
            map.put("gender", createPartFromString(gender));
            map.put("dob", createPartFromString("1293279790793"));
            map.put("budget", createPartFromString("1"));
            map.put("location", createPartFromString("Indore"));
            progress=new ProgressDialog(FriendDetailActivity.this);
            progress.setMessage("Loading ...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();

            Call<UpdateProfileManualResponse> call = apiService.updateProfileManual(map, body);
            call.enqueue(new Callback<UpdateProfileManualResponse>() {
                @Override
                public void onResponse(Call<UpdateProfileManualResponse> call,
                                       Response<UpdateProfileManualResponse> response) {
                    try {
                        if(response.body().getStatus().equalsIgnoreCase("1")){
                            progress.dismiss();
                            titleText.setText(friendName+" "+etLastName.getText().toString());
                            eventNewList = response.body().getUserDetails().getEvent();
                            String eventStr = new Gson().toJson(eventNewList);
                            if(eventStr!=null) {
                                editor.putString(SharedPreferencesConstants.friendEventList, eventStr);
                                editor.putString(SharedPreferencesConstants.fromFragment1,"fromFragment1");
                            }
                            editor.commit();
                            startActivity(new Intent(FriendDetailActivity.this,FriendInterestActivity.class));
                            Toast.makeText(FriendDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        }
                        else {
                            progress.dismiss();
                            Toast.makeText(FriendDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        progress.dismiss();
                        e.printStackTrace();
                    }
                }


                @Override
                public void onFailure(Call<UpdateProfileManualResponse> call, Throwable t) {
                    //  Log.e("Profile Upload error:", t.getMessage());
                    progress.dismiss();
                    Toast.makeText(FriendDetailActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(FriendDetailActivity.this,getResources().getString(R.string.network_connectivity),Toast.LENGTH_LONG).show();
        }

    }

    public RequestBody createPartFromString(String str) {
        if(null == str){
            str ="";
        }
        RequestBody requestBody =
                RequestBody.create(
                        MultipartBody.FORM, str);
        return requestBody;

    }

    public void saveData() {
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
        if (etName.getText().toString().equalsIgnoreCase("")||etName.getText().toString().equalsIgnoreCase(null)) {
            Drawable dr = getResources().getDrawable(R.drawable.error_info_icon);
            dr.setBounds(0, 0, 50, 50);
            etName.setError(getResources().getString(R.string.invalid_name),dr);
        }else if (etLastName.getText().toString().equalsIgnoreCase("")||etLastName.getText().toString().equalsIgnoreCase(null)) {
            Drawable dr = getResources().getDrawable(R.drawable.error_info_icon);
            dr.setBounds(0, 0, 50, 50);
            etLastName.setError(getResources().getString(R.string.error_last_name),dr);
        }else if (etGender.getText().toString().equalsIgnoreCase("Gender")||etGender.getText().toString().equalsIgnoreCase(null)) {
            Toast.makeText(FriendDetailActivity.this,"Please select Gender",Toast.LENGTH_SHORT).show();
        }
        else if (etRelationship.getText().toString().equalsIgnoreCase("Relation")||etRelationship.getText().toString().equalsIgnoreCase(null)) {
            Toast.makeText(FriendDetailActivity.this,"Please select Relation",Toast.LENGTH_SHORT).show();
        }
       else {
            sendDataToServer();
        }
    }

    public  void sendDataToServer() {
        try {
            if(Utils.haveNetworkConnection(FriendDetailActivity.this)) {
                sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                editor = sharedpreferences.edit();
                userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
                friendName = etName.getText().toString();
                if(friendName.equalsIgnoreCase("")){
                    Toast.makeText(FriendDetailActivity.this,"Please fill friend Name",Toast.LENGTH_LONG).show();
                    return;
                }

                apiService = ApiClient.createService(ApiInterface.class);
                File file = FileUtils.getFile(FriendDetailActivity.this, fileUri);


                MultipartBody.Part body = null;

                if (fileUri != null) {

                    final RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    if (fileUri != null)
                        body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                }

                String gender =   etGender.getText().toString();
                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("user_id", createPartFromString(userId));
                map.put("name", createPartFromString(friendName.trim()));
                map.put("last_name", createPartFromString(etLastName.getText().toString()));
                map.put("relationship", createPartFromString(relationId));
                map.put("gender", createPartFromString(gender));
                map.put("dob", createPartFromString("123456789"));
                map.put("budget", createPartFromString("1"));
                map.put("isimage", createPartFromString("yes"));
                map.put("location", createPartFromString("Indore"));
                progress=new ProgressDialog(FriendDetailActivity.this);
                progress.setMessage("Loading ...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.setProgress(0);
                progress.show();

                // finally, execute the request
                Call<UploadManualContact> call = apiService.uploadManualContact(map, body);
                call.enqueue(new Callback<UploadManualContact>() {
                    @Override
                    public void onResponse(Call<UploadManualContact> call,
                                           Response<UploadManualContact> response) {
                        try {
                            if(response.body().getStatus().equalsIgnoreCase("1")){
                                progress.dismiss();
                                eventNewList = response.body().getUserDetails().getEvent();
                                String idStr = response.body().getUserDetails().getId().toString();
                                editor.putString(SharedPreferencesConstants.friendId,idStr);
                                editor.commit();
                                startActivity(new Intent(FriendDetailActivity.this,FriendInterestActivity.class));
                                Toast.makeText(FriendDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                            }
                            else {
                                progress.dismiss();
                                Toast.makeText(FriendDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            progress.dismiss();
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onFailure(Call<UploadManualContact> call, Throwable t) {
                        //  Log.e("Profile Upload error:", t.getMessage());
                        progress.dismiss();
                        Toast.makeText(FriendDetailActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(FriendDetailActivity.this,getResources().getString(R.string.network_connectivity),Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FriendDetailActivity.this,TabsViewPagerFragmentActivity.class);
        intent.putExtra("profile","");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
