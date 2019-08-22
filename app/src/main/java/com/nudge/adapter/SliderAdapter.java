package com.nudge.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nudge.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ADVANTAL on 11/13/2017.
 */

public class SliderAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context context;
    ArrayList<HashMap<String,String>> imageUrls;
    public SliderAdapter(Context context, ArrayList<HashMap<String,String>> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        final ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);
        Picasso.with(context).load(imageUrls.get(position).get("image")).into(myImage);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
