package com.nudge.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.nudge.App;
import com.nudge.R;
import com.nudge.firebase.Config;
import com.nudge.model.FileUtility;
import com.nudge.model.FileUtils;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.SignUpResponse;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import static com.nudge.activity.SignUpOneActivity.countrySaved;
import static com.nudge.activity.SignUpOneActivity.dobSaved;
import static com.nudge.activity.SignUpOneActivity.emailSaved;
import static com.nudge.activity.SignUpOneActivity.firstNameSaved;
import static com.nudge.activity.SignUpOneActivity.lastNameSaved;
import static com.nudge.activity.SignUpOneActivity.passwordSaved;
public class SignUpThreeActivity extends AppCompatActivity {
    TextView tv_birthday;
    EditText tv_country, et_user_gender;
    RelativeLayout rl_back_arrow, rl_country;
    ImageView signUpImageView;
    ApiInterface apiService;
    Uri fileUri = null;
    String ddate;
    String loginType = "";
    private ProgressDialog progress;
    long minDateInMilliSeconds;
    DatePickerDialog datePickerDialog;
    private Calendar myCalendar = Calendar.getInstance();
    TextView addPhotoTextView;
    ImageView userImageView;
    // ImageView iv_country;
    ImageView iv_birthday, iv_drop_down, iv_gender_drop;
    MultipartBody.Part body = null;
    private Bitmap selectedImageBitmap = null;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String device_token;
    File file;
    String pic;
    Intent CropIntent, GalIntent;
    final int MY_REQUEST_CAMERA = 20;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String isImage = "";
    Bitmap fb_thumbnail;
    long timpStamp;


    // String userImageBitmapString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_step_three);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_country = (EditText) findViewById(R.id.tv_country);
        tv_country.setText("United Kingdom");
        et_user_gender = (EditText) findViewById(R.id.et_user_gender);
        signUpImageView = (ImageView) findViewById(R.id.signUpImageView);
        rl_back_arrow = (RelativeLayout) findViewById(R.id.rl_back_arrow);
        rl_country = (RelativeLayout) findViewById(R.id.rl_country);
        addPhotoTextView = (TextView) findViewById(R.id.addPhotoTextView);
        userImageView = (ImageView) findViewById(R.id.userImageView);
        iv_gender_drop = (ImageView) findViewById(R.id.iv_gender_drop);
        // iv_country = (ImageView)findViewById(R.id.iv_country);
        iv_birthday = (ImageView) findViewById(R.id.iv_birthday);
        iv_drop_down = (ImageView) findViewById(R.id.iv_drop_down);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        try {
            Intent i = getIntent();
            loginType = i.getStringExtra("login_type");


        } catch (NullPointerException cv) {

        }
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        device_token = pref.getString("regId", null);
        //userImageBitmapString = pref.getString("pic", "");

        try {
            Intent in = getIntent();
            pic = in.getStringExtra("pic");
            if (loginType.equals("fb")) {

                GetXMLTask task = new GetXMLTask();
                task.execute(new String[]{pic});


            }
            System.out.println("pic==" + pic);
        } catch (NullPointerException xvx) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
        }
        System.out.println("loginType========" + loginType);
        System.out.println("device_token========" + device_token);

        if (loginType.equals("")) {
            loginType = "email";
            tv_country.setText("United Kingdom");

        } else if (loginType.equals("fb")) {
            loginType = "fb";
            tv_country.setText("United Kingdom");
        } else {
            loginType = "email";
            tv_country.setText("United Kingdom");

        }
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        tv_birthday.setText(sharedpreferences.getString(SharedPreferencesConstants.userFbBirthday, ""));
        dobSaved = sharedpreferences.getString(SharedPreferencesConstants.userFbBirthday, "");
        et_user_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(SignUpThreeActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.gender_layout, null);
                alertDialog.setView(convertView);
                ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpThreeActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender_arrays));
                lv.setAdapter(adapter);
                alertDialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String[] names = getResources().getStringArray(R.array.gender_arrays);
                        et_user_gender.setText(names[position]);
                        alertDialog.dismiss();
                    }
                });
            }
        });


        iv_gender_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(SignUpThreeActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.gender_layout, null);
                alertDialog.setView(convertView);
                ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpThreeActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender_arrays));
                lv.setAdapter(adapter);
                alertDialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String[] names = getResources().getStringArray(R.array.gender_arrays);
                        et_user_gender.setText(names[position]);
//                        genderStr = names[position];
                        alertDialog.dismiss();
                    }
                });
            }
        });


        tv_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(SignUpThreeActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.gender_layout, null);
                alertDialog.setView(convertView);
                ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpThreeActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.country_names_arrays));
                lv.setAdapter(adapter);
                alertDialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String[] names = getResources().getStringArray(R.array.country_names_arrays);
                        tv_country.setText(names[position]);
//                        genderStr = names[position];
                        alertDialog.dismiss();
                    }
                });
            }
        });
        iv_drop_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(SignUpThreeActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.gender_layout, null);
                alertDialog.setView(convertView);
                ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpThreeActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.country_names_arrays));
                lv.setAdapter(adapter);
                alertDialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String[] names = getResources().getStringArray(R.array.country_names_arrays);
                        tv_country.setText(names[position]);
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
                dobSaved = tv_birthday.getText().toString();
                countrySaved = tv_country.getText().toString();
                finish();

            }
        });


        tv_birthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        View view = SignUpThreeActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        minDateInMilliSeconds = System.currentTimeMillis();
                        datePickerDialog = new DatePickerDialog(SignUpThreeActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(minDateInMilliSeconds);
                        datePickerDialog.show();

                        break;
                }

                return false;
            }
        });
 signUpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_user_gender.getText().toString().equalsIgnoreCase("") || et_user_gender.getText().toString().equalsIgnoreCase(null)) {

                    Toast.makeText(SignUpThreeActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                } else if (tv_birthday.getText().toString().equalsIgnoreCase("") || tv_birthday.getText().toString().equalsIgnoreCase(null)) {

                    Toast.makeText(SignUpThreeActivity.this, "Please select birthday", Toast.LENGTH_SHORT).show();
                } else if (tv_country.getText().toString().equalsIgnoreCase("") || tv_country.getText().toString().equalsIgnoreCase(null)) {
                    Toast.makeText(SignUpThreeActivity.this, "please select country", Toast.LENGTH_SHORT).show();
                } else {
                    uploadProfile();
                }
            }
        });
    }

    private void uploadProfile() {
        apiService = ApiClient.createService(ApiInterface.class);

        if (loginType.equals("fb")) {
            file = FileUtils.getFile(this, fileUri);
        } else {
            file = FileUtils.getFile(this, fileUri);
        }
        MultipartBody.Part body = null;

        if (fileUri != null) {
            final RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            System.out.println("file name ==" + file.getName());
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }

        if (ddate != null && !ddate.equalsIgnoreCase("")) {
             String str_date = tv_birthday.getText().toString();
                DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                Date date = null;

                try {
                    date = (Date) formatter.parse(str_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long milliSeconds = date.getTime();
                    timpStamp = milliSeconds / 1000L;
                  }
        String timeStampStrVal = String.valueOf(timpStamp);

        System.out.println("timeStampStrVal=" + timeStampStrVal);
        System.out.println(createPartFromString(loginType));
        System.out.println(createPartFromString(firstNameSaved));
        System.out.println(createPartFromString(lastNameSaved));
        System.out.println(createPartFromString(emailSaved));
        System.out.println(createPartFromString(emailSaved));
        System.out.println(createPartFromString(passwordSaved));
        System.out.println(createPartFromString(et_user_gender.getText().toString()));
        System.out.println(createPartFromString(timeStampStrVal));
        System.out.println(createPartFromString(tv_country.getText().toString()));
        System.out.println(createPartFromString("android"));
        System.out.println(createPartFromString("device_token"));


        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("login_type", createPartFromString(loginType));
        map.put("name", createPartFromString(firstNameSaved));
        map.put("last_name", createPartFromString(lastNameSaved));
        map.put("email", createPartFromString(emailSaved));


        map.put("unique_id", createPartFromString(emailSaved));
        map.put("password", createPartFromString(passwordSaved));
        map.put("gender", createPartFromString(et_user_gender.getText().toString()));
        map.put("dob", createPartFromString(timeStampStrVal));
        map.put("location", createPartFromString(tv_country.getText().toString()));
        map.put("device_type", createPartFromString("android"));
        map.put("device_token", createPartFromString(device_token));
        /*map.put("device_token", createPartFromString(refreshedToken));*/
        progress = new ProgressDialog(this);
        progress.setMessage("Loading ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        // finally, execute the request
        Call<SignUpResponse> call = apiService.uploadProfile(map, body);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call,
                                   Response<SignUpResponse> response) {
                try {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {

                       // if (response.body().getUserDetails().getImage() != null) {
                            Log.e("Profile profile pic", response.body().getUserDetails().getImage());
                            Log.e("Profile profile pic", response.body().getUserDetails().getDob());
                            Log.e("Profile profile pic", response.body().getUserDetails().getName());
                            Log.e("Profile profile pic", response.body().getUserDetails().getEmail());
                            Log.e("Profile profile pic", response.body().getUserDetails().getId().toString());
                            Log.e("Profile profile pic", response.body().getMessage());
                            Log.e("Profile profile pic", response.body().getStatus());
                            Toast.makeText(SignUpThreeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            progress.dismiss();
                            Intent goUploadProfilePermissionIntent = new Intent(SignUpThreeActivity.this, LoginActivity.class);
                            goUploadProfilePermissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(goUploadProfilePermissionIntent);
                            finish();

                    } else {
                        progress.dismiss();
                        Toast.makeText(SignUpThreeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    progress.dismiss();
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(SignUpThreeActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dobSaved = tv_birthday.getText().toString();
        countrySaved = tv_country.getText().toString();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(countrySaved.equals(""))
        {
            tv_country.setText("United Kingdom");

        }
        else
        {
            tv_country.setText(countrySaved);

        }

        tv_birthday.setText(dobSaved);
    }

    //============upload image method================================

    protected void selectImage() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //  ActivityCompat.requestPermissions(act, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CAMERA);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), thumbnail, "Title", null);
            Uri imageUri = Uri.parse(path);
            startCropImageActivity(imageUri);
        }

        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(SignUpThreeActivity.this, data);
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

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(SignUpThreeActivity.this);
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
            Toast.makeText(SignUpThreeActivity.this, "Please grant required ramera and storage permissions", Toast.LENGTH_LONG).show();
        }
    }

    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            userImageView.setImageBitmap(result);

            String path = MediaStore.Images.Media.insertImage(SignUpThreeActivity.this.getContentResolver(), result, "Title", null);

           if(path.equals(""))
           {

           }
           else
           {
               Uri imageUri = Uri.parse(path);
               startCropImageActivity(imageUri);


           }

        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }


    }
    private InputStream getHttpConnection(String urlString)
            throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }
}











