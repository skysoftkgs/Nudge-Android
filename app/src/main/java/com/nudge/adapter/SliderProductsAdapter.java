package com.nudge.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.nudge.R;

import java.util.ArrayList;

/**
 * Created by ADVANTAL on 11/13/2017.
 */

public class SliderProductsAdapter extends PagerAdapter {
    private ArrayList<String> images = new ArrayList<String>();
    private LayoutInflater inflater;
    private Context context;

    public SliderProductsAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        final ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);

       String url = images.get(position);
        Glide.with(context)
                .load(url)
                .placeholder(R.raw.product_loading_gif)
                .into(new GlideDrawableImageViewTarget(
                        myImage));

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}

