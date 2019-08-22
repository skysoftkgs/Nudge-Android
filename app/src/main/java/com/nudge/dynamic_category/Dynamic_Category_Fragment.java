package com.nudge.dynamic_category;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.nudge.R;
import com.nudge.adapter.SliderAdapter;
import com.nudge.category.ConnectionDetector;
import com.nudge.category.Constant;
import com.nudge.product.Data;
import com.nudge.product.Product_List_BySubcategory_Adapter;
import com.nudge.product.Section;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
public class Dynamic_Category_Fragment extends Fragment {
    private static final int PAGE_START = 1;
    ViewPager mPager;
    CircleIndicator indicator;
    ConnectionDetector cd;
    public static ListView subcategory_list;
    public static  String main_cate_id,main_cate_name;
    ProgressBar progressbar;
    List<Data> allSampleData;
    String image_url;
    String mainid;
    private int currentPage = PAGE_START;
    SliderAdapter pager_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gift_fragment_one, container, false);
        subcategory_list = (ListView) rootView.findViewById(R.id.subcategory_list);
        progressbar =(ProgressBar) rootView.findViewById(R.id.progressbar);
         cd = new ConnectionDetector(getActivity());
        allSampleData = new ArrayList<Data>();


        Bundle data1 = getArguments();
        mainid =data1.getString("cate_id");
               if (Constant.jsonStr != null) {
            try {
                allSampleData.clear();
                JSONObject json = new JSONObject(Constant.jsonStr);
                JSONArray Android = json.getJSONArray("result");
                for (int Android_i = 0; Android_i < Android.length(); Android_i++) {
                    JSONObject Android_obj = Android.getJSONObject(Android_i);
                    main_cate_id = Android_obj.getString("id");
                    main_cate_name = Android_obj.getString("name");
                    if (main_cate_id.equals(mainid)) {
                        JSONArray dataArary= Android_obj.getJSONArray("subcategory");
                        if(dataArary!=null)
                        {
                            for(int i=0;i<dataArary.length();i++)
                            {
                                JSONObject sectionObj= (JSONObject) dataArary.get(i);
                                String title= sectionObj.getString("name");
                                String sub_id= sectionObj.getString("id");
                                List<Section> sections= new ArrayList<Section>();
                                JSONArray sectionsArray=sectionObj.getJSONArray("products");
                                if(sectionsArray!=null)
                                {
                                    for(int j=0;j<sectionsArray.length();j++)
                                    {
                                        JSONObject obj= (JSONObject) sectionsArray.get(j);
                                        Section section = new Section();
                                        section.setId(obj.getString("id"));
                                        section.setName(obj.getString("name"));
                                        section.setSlug(obj.getString("slug"));
                                        section.setDescription(obj.getString("description"));
                                        section.setShort_description(obj.getString("short_description"));
                                        section.setPrice(obj.getString("price"));
                                        section.setRegular_price(obj.getString("regular_price"));
                                        section.setSale_price(obj.getString("sale_price"));
                                        section.setExternal_url(obj.getString("external_url"));
                                        //section.setImage(obj.getString("image"));
                                        JSONArray image_array = obj.getJSONArray("image");
                                        if(image_array!=null) {
                                            for (int m = 0; m < image_array.length(); m++) {
                                                image_url = (String) image_array.get(0);
                                            }
                                        }
                                        System.out.println("image_url=>>"+image_url);
                                        section.setImage(image_url);
                                        sections.add(section);
                                           }
                                }
                                Data data= new Data();
                                data.setTitle(title);
                                data.setSubcategory_id(sub_id);
                                data.setSection(sections);
                                allSampleData.add(data);
                            }
                            if(allSampleData!=null) {
                                Product_List_BySubcategory_Adapter adapter = new Product_List_BySubcategory_Adapter(getActivity(), allSampleData);
                                subcategory_list.setAdapter(adapter);
                            }
                            try
                            {
                                LayoutInflater inflater1 = getLayoutInflater();
                                ViewGroup header = (ViewGroup) inflater.inflate(R.layout.banner_layout, subcategory_list,false);
                                mPager = (ViewPager) header.findViewById(R.id.pager);
                                indicator = (CircleIndicator) header.findViewById(R.id.indicator);

                            pager_adapter =   new SliderAdapter(getActivity(),Dynamic_Shop_Category_Activity.imageUrls);
                                mPager.setAdapter(pager_adapter);


                                pager_adapter.notifyDataSetChanged();
                                indicator.setViewPager(mPager);
                                final Handler handler1 = new Handler();
                                final Runnable Update1 = new Runnable() {
                                    public void run() {
                                       try
                                       {
                                           if (currentPage == Dynamic_Shop_Category_Activity.imageUrls.size()) {
                                               currentPage = 0;
                                           }
                                       }
                                       catch (NullPointerException scs)
                                       {

                                       }
                            try
                            {
                                pager_adapter.notifyDataSetChanged();

                                mPager.setCurrentItem(currentPage++, true);

                            }
                            catch (IllegalStateException ff)
                            {

                            }
                            catch (NullPointerException d)
                            {

                            }
                                    }
                                };
                                Timer swipeTimer1 = new Timer();
                                swipeTimer1.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        handler1.post(Update1);
                                    }
                                }, 5000, 5000);
                                subcategory_list.addHeaderView(header);
                            }

                            catch (ArrayIndexOutOfBoundsException v)
                            {

                            }
                            catch (IllegalStateException bh)
                            {

                            }

                            catch (NullPointerException nn)
                            {

                            }


                        }
                    }
                }
            } catch (final JSONException e) {

                Log.e("Exception==", e.toString());


            } catch (NullPointerException nm) {

            }
        }

        return rootView;
    }
    public class Subcategory_Asynctask extends AsyncTask<Void, Void, Void> {
        // static final String TAG = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            //   progressbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            allSampleData = new ArrayList<Data>();
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(300, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();

            // OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
            //RequestBody body = RequestBody.create(JSON,"category_id=830&offset=0&per_page=10&debug=0");
            Request request = new Request.Builder()
                    .url("http://app.thegiftingapp.com/woo_api/ get_shop_categories_filter.php")
                    .build();

            okhttp3.Response response = null;
            try {
                response = client.newCall(request).execute();
                Constant.jsonStr = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            progressbar.setVisibility(View.GONE);
            try
            {
                if(allSampleData!=null) {
                    SharedPreferences db1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor collection1 = db1.edit();
                    Gson gson1 = new Gson();
                    String arrayList1 = gson1.toJson(allSampleData);
                    collection1.putString("card_product", arrayList1);
                    collection1.commit();
                    Product_List_BySubcategory_Adapter adapter = new Product_List_BySubcategory_Adapter(getActivity(), allSampleData);
                    subcategory_list.setAdapter(adapter);
                }


            }
            catch (NullPointerException vv)
            {

            }
        }
    }

}



