package com.nudge.alphabaticIndexSearch;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nudge.R;
import com.nudge.activity.SignUpFourActivity;
import com.nudge.pojo.Pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;

/**
 * Created by User on 2/9/2018.
 */

public class ListSecond extends AppCompatActivity {
    @Bind(R.id.fast_scroller_recycler)
    RecyclerView mRecyclerView;

    private List<Pojo> mDataArray;
    private List<AlphabetItem> mAlphabetItems;
    private ImageView backArrowImage;

    private static final int PERMISSION_REQUEST_CONTACT = 21;
    String name;
    //InputStream inputStream=null;
    String image,mobileNumber;
    Button click;
    Uri im_uri;
    ArrayList<Pojo> arrayList=new ArrayList<>();
    private EditText searchEditText;
    private LinearLayout btnDone;
    private ImageView settingsImageView;
    RecyclerAdpaterSecondScreen recyclerAdpaterSecondScreen;
    TextView doneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_second);
        //ButterKnife.bind(this);
       mRecyclerView = (RecyclerView) findViewById(R.id.fast_scroller_recycler);
        backArrowImage = (ImageView)findViewById(R.id.backArrowImage);
        searchEditText = (EditText)findViewById(R.id.search);
        doneTextView = (TextView)findViewById(R.id.doneText) ;
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        doneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListSecond.this, SignUpFourActivity.class));
            }
        });
        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /*initialiseData();*/
       // initialiseUI();
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(ListSecond.this, Manifest.permission.WRITE_CONTACTS)+ContextCompat.checkSelfPermission(ListSecond.this, Manifest.permission.READ_CONTACTS) !=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ListSecond.this,new String []{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS},PERMISSION_REQUEST_CONTACT);
            }

            else if (ContextCompat.checkSelfPermission(ListSecond.this, Manifest.permission.WRITE_CONTACTS)+ContextCompat.checkSelfPermission(ListSecond.this, Manifest.permission.READ_CONTACTS) ==
                    PackageManager.PERMISSION_GRANTED){
                final ContentResolver cr = getContentResolver();
                String filter = ""+ ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0 and " + ContactsContract.CommonDataKinds.Phone.TYPE +"=" + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
                String[] projection = new String[] {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.PHOTO_URI, ContactsContract.CommonDataKinds.Phone.NUMBER};
                final Cursor c = cr.query(ContactsContract.Data.CONTENT_URI, projection, filter, null, null);
                              try {

                    if (c.getCount() > 0) {
                        while (c.moveToNext()) {
                            Log.e("Cursor Count", "" + c.getCount());
                            name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            Log.e("CONTACT NAME = ", "" + name);

                            image = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                            Log.e("CONTACT IMAGE STRING = ", "" + image);

                            mobileNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Log.e("CONTACT Num STRING = ", "" + mobileNumber);

                            if (image == null) {
                                Log.e("NULL STRING", "" + image);
                                image = "IMAGE NOT AVAILABLE";

                                im_uri = Uri.parse(image);
                                Log.e("NULL URI", "" +  im_uri);


                            }
                            else if (image != null) {
                                im_uri = Uri.parse(image);
                                Log.e("NOT NULL URI", "" +  im_uri);
                            }


                            Pojo p = new Pojo();
                            p.setContact_name(name);
                            p.setImage_uri(im_uri);
                            p.setContact_number(mobileNumber);
                            arrayList.add(p);
                        }
                        c.close();
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

                } catch (Exception e) {
                }

            }
        }

        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.M) {
            final ContentResolver cr = getContentResolver();
            String filter = ""+ ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0 and " + ContactsContract.CommonDataKinds.Phone.TYPE +"=" + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
            String[] projection = new String[] {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.PHOTO_URI, ContactsContract.CommonDataKinds.Phone.NUMBER};
            final Cursor c = cr.query(ContactsContract.Data.CONTENT_URI, projection, filter, null, null);
            try {

                if (c.getCount() > 0) {
                    while (c.moveToNext()) {
                        Log.e("Cursor Count", "" + c.getCount());
                        name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Log.e("CONTACT NAME = ", "" + name);

                        image = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                        Log.e("CONTACT IMAGE STRING = ", "" + image);

                        mobileNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.e("CONTACT Num STRING = ", "" + mobileNumber);

                        if (image == null) {
                            Log.e("NULL STRING", "" + image);
                            image = "IMAGE NOT AVAILABLE";

                            im_uri = Uri.parse(image);
                            Log.e("NULL URI", "" + im_uri);


                        } else if (image != null) {
                            im_uri = Uri.parse(image);
                            Log.e("NOT NULL URI", "" + im_uri);
                        }


                        Pojo p = new Pojo();
                        p.setContact_name(name);
                        p.setImage_uri(im_uri);
                        p.setContact_number(mobileNumber);
                        arrayList.add(p);
                    }
                    c.close();
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

            } catch (Exception e) {
            }
        }
    }



    protected void initialiseUI(List<Pojo> mDataArray) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Collections.sort(mDataArray, new Comparator<Pojo>() {
            public int compare(Pojo v1, Pojo v2) {
                return v1.getContact_name().compareTo(v2.getContact_name());
            }
        });
         recyclerAdpaterSecondScreen = new RecyclerAdpaterSecondScreen(mDataArray,ListSecond.this);
        mRecyclerView.setAdapter(recyclerAdpaterSecondScreen);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_GRANTED){
            final ContentResolver cr = getContentResolver();
            String filter = ""+ ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0 and " + ContactsContract.CommonDataKinds.Phone.TYPE +"=" + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
            String[] projection = new String[] {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.PHOTO_URI, ContactsContract.CommonDataKinds.Phone.NUMBER};
            final Cursor c = cr.query(ContactsContract.Data.CONTENT_URI, projection, filter, null, null);


            try {

                if (c.getCount() > 0) {
                    while (c.moveToNext()) {
                        Log.e("Cursor Count", "" + c.getCount());
                        name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Log.e("CONTACT NAME = ", "" + name);

                        image = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                        Log.e("CONTACT IMAGE STRING = ", "" + image);

                        mobileNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.e("CONTACT Num STRING = ", "" + mobileNumber);

                        if (image == null) {
                            Log.e("NULL STRING", "" + image);
                            image = "IMAGE NOT AVAILABLE";

                            im_uri = Uri.parse(image);
                            Log.e("NULL URI", "" + im_uri);


                        } else if (image != null) {
                            im_uri = Uri.parse(image);
                            Log.e("NOT NULL URI", "" + im_uri);
                        }


                        Pojo p = new Pojo();
                        p.setContact_name(name);
                        p.setImage_uri(im_uri);
                        p.setContact_number(mobileNumber);
                        arrayList.add(p);
                    }
                    c.close();
                    //  mDataArray = arrayList;
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
                }else {
                    Toast.makeText(getBaseContext(),"Unable to get permission",Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
