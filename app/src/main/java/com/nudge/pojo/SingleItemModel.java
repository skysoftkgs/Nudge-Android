package com.nudge.pojo;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.nudge.adapter.SectionListDataAdapter;
import com.nudge.model.Utils;

/**
 * Created by ADVANTAL on 3/1/2018.
 */
public class SingleItemModel {
    private String name;
    private String url;
    private String description;
    private String id;
    private Bitmap image;

    private SectionListDataAdapter sta;

    public SingleItemModel() {
    }

    public SingleItemModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public SectionListDataAdapter getSta() {
        return sta;
    }

    public void setSta(SectionListDataAdapter sta) {
        this.sta = sta;
    }

    public void loadImage(SectionListDataAdapter sta) {
        // HOLD A REFERENCE TO THE ADAPTER
        this.sta = sta;
        if (url != null && !url.equals("")) {
            new ImageLoadTask().execute(url);
        }
    }

    // ASYNC TASK TO AVOID CHOKING UP UI THREAD
    private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            Log.i("ImageLoadTask", "Loading image...");
        }

        // PARAM[0] IS IMG URL
        protected Bitmap doInBackground(String... param) {
            Log.i("ImageLoadTask", "Attempting to load image URL: " + param[0]);
            try {
                Bitmap b = Utils.getBitmapFromURL(param[0]);
                Log.d("bitmapdata","====="+b.toString());
                return b;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onProgressUpdate(String... progress) {
            // NO OP
        }

        protected void onPostExecute(Bitmap ret) {
            if (ret != null) {
                Log.i("ImageLoadTask", "Successfully loaded " + name + " image");
                image = ret;
                if (sta != null) {
                    // WHEN IMAGE IS LOADED NOTIFY THE ADAPTER
                    sta.notifyDataSetChanged();
                }
            } else {
                Log.e("ImageLoadTask", "Failed to load " + name + " image");
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
