package com.nudge.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nudge.App;
import com.nudge.R;
import com.nudge.model.FileUtility;
import com.nudge.model.FileUtils;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.UpdateRegistration;
import com.nudge.pojo.UserDetail_UpdateRegistration;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nudge.fragment.ProfileFragment.userCountry;
import static com.nudge.fragment.ProfileFragment.userFirstName;
import static com.nudge.fragment.ProfileFragment.userGender;
import static com.nudge.fragment.ProfileFragment.userLastName;
import static com.nudge.fragment.ProfileFragment.userNewPassword;

/**
 * Created by ADVANTAL on 2/16/2018.
 */

public class EditProfileThreeActivity extends AppCompatActivity {
    public String FirstName = "", LastName = "", Email = "", image = "", Gender = "", Dob = "", userId = "", password = "", newPassword = "", location;
    Bitmap selectedImageBitmap = null;
    String isImage = "no";
    MultipartBody.Part body = null;
    Intent CropIntent, GalIntent;
    final int MY_REQUEST_CAMERA = 20;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ApiInterface apiInterface;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    TextView tv_birthday, tv_save;
    EditText et_country, et_gender;
    RelativeLayout rl_country;
    RelativeLayout rl_back_arrow;
    ImageView signUpImageView;
    ApiInterface apiService;
    Uri fileUri = null;
    String ddate;
    String loginType = "email";
    private ProgressDialog progress;
    long minDateInMilliSeconds;
    DatePickerDialog datePickerDialog;
    private Calendar myCalendar = Calendar.getInstance();
    TextView addPhotoTextView;
    ImageView userImageView;
    //ImageView iv_country;
    ImageView iv_birthday, iv_drop_down;
//    Spinner sp_gender;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String userDob;
    long timpStamp;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_three);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        et_country = (EditText) findViewById(R.id.et_country);
        signUpImageView = (ImageView) findViewById(R.id.signUpImageView);
        rl_back_arrow = (RelativeLayout) findViewById(R.id.rl_back_arrow);
        addPhotoTextView = (TextView) findViewById(R.id.addPhotoTextView);
        userImageView = (ImageView) findViewById(R.id.userImageView);
        //  iv_country = (ImageView)findViewById(R.id.iv_country);
        iv_birthday = (ImageView) findViewById(R.id.iv_birthday);
        iv_drop_down = (ImageView) findViewById(R.id.iv_drop_down);
//        sp_gender = (Spinner) findViewById(R.id.sp_gender);
        tv_save = (TextView) findViewById(R.id.tv_save);
        et_gender = (EditText) findViewById(R.id.et_user_gender);
        rl_country = (RelativeLayout) findViewById(R.id.rl_country);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(EditProfileThreeActivity.this ));
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading()
                .cacheOnDisc()
                .cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        userDob =sharedpreferences.getString(SharedPreferencesConstants.userDob,"");
        editor = sharedpreferences.edit();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        if(!sharedpreferences.getString(SharedPreferencesConstants.userImageUrl,"").equals("")) {
            String imageName = sharedpreferences.getString(SharedPreferencesConstants.userImageUrl, "");
            updateView(imageName, userImageView);
        }
        et_gender.setText(userGender);
        et_country.setText(userCountry);
       // tv_birthday.setText(Utils.convertTimestampToDate(userDob));
        String S =userDob;
        //convert unix epoch timestamp (seconds) to milliseconds
        long timestamp = Long.parseLong(S) * 1000L;
        tv_birthday.setText(getDate(timestamp ));
        et_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(EditProfileThreeActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.gender_layout, null);
                alertDialog.setView(convertView);
                ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileThreeActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender_arrays));
                lv.setAdapter(adapter);
                alertDialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String[] names = getResources().getStringArray(R.array.gender_arrays);
                        et_gender.setText(names[position]);
//                        genderStr = names[position];
                        alertDialog.dismiss();
                    }
                });
            }
        });

        et_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(EditProfileThreeActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.gender_layout, null);
                alertDialog.setView(convertView);
                ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileThreeActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.country_names_arrays));
                lv.setAdapter(adapter);
                alertDialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String[] names = getResources().getStringArray(R.array.country_names_arrays);
                        et_country.setText(names[position]);
//                        genderStr = names[position];
                        alertDialog.dismiss();
                    }
                });
            }
        });


        addPhotoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                intentData();
                userDob = Utils.convertDateToTimestamp(tv_birthday.getText().toString());
                userCountry = et_country.getText().toString();
                Intent in = new Intent(EditProfileThreeActivity.this, EditProfileTwoActivity.class);
                startActivity(in);
                finish();
            }
        });


        tv_birthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        View view = EditProfileThreeActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        minDateInMilliSeconds = System.currentTimeMillis();
                        datePickerDialog = new DatePickerDialog(EditProfileThreeActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(minDateInMilliSeconds);
                        datePickerDialog.show();

                        break;
                }

                return false;
            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
        signUpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_gender.getText().toString().equalsIgnoreCase("") || et_gender.getText().toString().equalsIgnoreCase(null)) {
                    Toast.makeText(EditProfileThreeActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                } else if (tv_birthday.getText().toString().equalsIgnoreCase("") || tv_birthday.getText().toString().equalsIgnoreCase(null)) {

                    Toast.makeText(EditProfileThreeActivity.this, "Please Select Birthday", Toast.LENGTH_SHORT).show();
                } else if (et_country.getText().toString().equalsIgnoreCase("") || et_country.getText().toString().equalsIgnoreCase(null)) {
                    Toast.makeText(EditProfileThreeActivity.this, "Please Select Country", Toast.LENGTH_SHORT).show();
                } else {
                    uploadData();
                }
            }
        });
    }




    public RequestBody createPartFromString(String str) {
        RequestBody requestBody =
                RequestBody.create(
                        MultipartBody.FORM, str);
        return requestBody;

    }

    DatePickerDialog.OnDateSetListener date = new
            DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }
            };

    protected void updateLabel() {

        String myFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        ddate = sdf.format(myCalendar.getTime());
        tv_birthday.setText(ddate);
        long timeStampStr = 1504900389494l;
        if (ddate != null && !ddate.equalsIgnoreCase("")) {
            String str_date = ddate;
            DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            Date date = null;
            try {
                date = (Date) formatter.parse(str_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long timestamp = +date.getTime();
            timeStampStr = timestamp / 1000L;
        }
        String timeStampStrVal = String.valueOf(timeStampStr);
        Dob = timeStampStrVal;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        userGender = et_gender.getText().toString();
        userDob = Utils.convertDateToTimestamp(tv_birthday.getText().toString());
        userCountry = et_country.getText().toString();
        Intent in = new Intent(EditProfileThreeActivity.this, EditProfileTwoActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    protected void selectImage() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CAMERA);
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(EditProfileThreeActivity.this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), thumbnail, "Title", null);
            Uri imageUri = Uri.parse(path);
            startCropImageActivity(imageUri);
        }

        // handle result of CropImageActivity
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(EditProfileThreeActivity.this, data);
            startCropImageActivity(imageUri);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                fileUri = result.getUri();

                userImageView.setImageURI(result.getUri());
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                    userImageView.setImageBitmap(selectedImageBitmap);


                    File file = FileUtility.getFile(this, fileUri);

                    if (fileUri == null) {
                        Toast.makeText(this, "Please select Image", Toast.LENGTH_LONG).show();
                        isImage = "no";
                        return;
                    }

                    if (fileUri != null) {

                        isImage = "yes";
                        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                        if (fileUri != null)
                            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            CropImage.startPickImageActivity(this);
        } else {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
            Toast.makeText(EditProfileThreeActivity.this, "Please Grant Required Camera and Storage Permissions", Toast.LENGTH_LONG).show();
        }
    }

    public void setSpinnerAdapter(Spinner sp, ArrayList<String> s) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileThreeActivity.this, R.layout.spinner_textview, s);
        sp.setAdapter(adapter);
    }

    public void showDate(final TextView textView) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                // Display Selected date in textbox
                textView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

            }
        }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMaxDate(minDateInMilliSeconds);
        dpd.show();
    }

    private void uploadData() {

        if (tv_birthday.getText().toString().equalsIgnoreCase("") || tv_birthday.getText().toString().equalsIgnoreCase(null)) {

            Toast.makeText(EditProfileThreeActivity.this, "Please Select Birthday", Toast.LENGTH_SHORT).show();
        } else if (et_country.getText().toString().equalsIgnoreCase("") || et_country.getText().toString().equalsIgnoreCase(null)) {
            Toast.makeText(EditProfileThreeActivity.this, "Please Select Country", Toast.LENGTH_SHORT).show();
        } else {
            updateRegistration();
        }


    }

    public void updateRegistration() {

        userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
        Email = sharedpreferences.getString(SharedPreferencesConstants.userEmail, "");
        password = sharedpreferences.getString(SharedPreferencesConstants.userPassword, "");
        image = "";
        location = sharedpreferences.getString(SharedPreferencesConstants.userCountry, "");
        newPassword = "";

        try {
            String str_date = tv_birthday.getText().toString();
            DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            Date date = null;


                date = (Date) formatter.parse(str_date);

            long milliSeconds = date.getTime();
            timpStamp = milliSeconds / 1000L;

        } catch (ParseException e)
        {
            System.out.println("Exception :"+e);  }

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("name", createPartFromString(userFirstName));
        map.put("last_name", createPartFromString(userLastName));
        map.put("user_id", createPartFromString(userId));
        map.put("email", createPartFromString(Email));
        map.put("password", createPartFromString(password));
        map.put("dob", createPartFromString(String.valueOf(timpStamp)));
        map.put("location", createPartFromString(et_country.getText().toString()));
        map.put("isimage", createPartFromString(isImage));
        map.put("new_password", createPartFromString(userNewPassword));
        map.put("gender", createPartFromString(et_gender.getText().toString()));
        File file = FileUtils.getFile(this, fileUri);
         MultipartBody.Part body = null;
        if (fileUri != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            String file_name = file.getName();
            System.out.println("file_name=="+file_name);
        }
        if (Utils.haveNetworkConnection(EditProfileThreeActivity.this)) {
            progress = new ProgressDialog(this);
            progress.setMessage("Loading ...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();


            Call<UpdateRegistration> calll = apiInterface.getUpdateProfileResponse(map, body);
            calll.enqueue(new Callback<UpdateRegistration>() {
                @Override
                public void onResponse(Call<UpdateRegistration> call, Response<UpdateRegistration> response) {
                    if (response != null) {

                        try {

                            if (response.body().getStatus().equalsIgnoreCase("1")) {

                                Toast.makeText(EditProfileThreeActivity.this, getString(R.string.profile_update_success), Toast.LENGTH_SHORT).show();
                                UserDetail_UpdateRegistration userDetails = response.body().getUserDetails();


                                String country = userDetails.getLocation();
                                if(country.equals(""))
                                {
                                    country="United Kingdom";
                                }

                                String image = userDetails.getImage().toString();
                                System.out.println("image==="+image);
                                editor.putString(SharedPreferencesConstants.userFirstName, userDetails.getName());
                                editor.putString(SharedPreferencesConstants.userLastName, userDetails.getLastName());
                                editor.putString(SharedPreferencesConstants.userEmail, userDetails.getEmail());

                               if(isImage.equals("no"))
                               {
                                   editor.putString(SharedPreferencesConstants.userImageUrl, "");

                               }
                               else


                                if(isImage.equals("yes"))
                                {
                                    editor.putString(SharedPreferencesConstants.userImageUrl, userDetails.getImage());

                                }

                                editor.putString(SharedPreferencesConstants.userGender, userDetails.getGender());
                                editor.putString(SharedPreferencesConstants.userDob, userDetails.getDob());
                                editor.putString(SharedPreferencesConstants.userCountry, country);
                                String imageUrl = userDetails.getImage();
                                if (!imageUrl.equals("")) {
                                    Bitmap bitmap = Utils.getBitmapFromURL(SharedPreferencesConstants.imageBaseUrl + imageUrl);
                                    String bitmapString = Utils.ConvertBitmapToString(bitmap);
                                    editor.putString(SharedPreferencesConstants.userImageBitmapString, bitmapString);
                                } else {
                                    editor.putString(SharedPreferencesConstants.userImageBitmapString, "");
                                }
                                editor.commit();


                                progress.dismiss();
                                finish();

                            } else {
                                Toast.makeText(EditProfileThreeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.dismiss();
                        }
                    }
                }


                @Override
                public void onFailure(Call<UpdateRegistration> call, Throwable t) {
                    // Log error here since request failed
                    progress.dismiss();

                }
            });
        } else {
            Toast.makeText(EditProfileThreeActivity.this, getResources().getString(R.string.network_connectivity), Toast.LENGTH_LONG).show();
        }
    }

    private void updateView(String imgName, ImageView iv) {
        imageLoader.displayImage(SharedPreferencesConstants.imageBaseUrl+imgName, iv, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {


            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
    }
    private String getDate(long timeStamp){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

}
