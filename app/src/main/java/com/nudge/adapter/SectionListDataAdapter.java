package com.nudge.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nudge.App;
import com.nudge.R;
import com.nudge.activity.ProductByIdActivity;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.SingleItemModel;

import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by ADVANTAL on 3/1/2018.
 */
public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<SingleItemModel> itemsList;
    private Context mContext;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    SingleItemModel singleItem;
    private ArrayList<SingleItemModel> itemsListRecentlyViewed = new ArrayList<>();
    private ArrayList<SingleItemModel> itemsListRecentlyViewedPrevious = new ArrayList<>();

    public SectionListDataAdapter(Context context, ArrayList<SingleItemModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_by_category_id_adapter, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {
        try {
            singleItem = itemsList.get(position);

            holder.tvTitle.setText(singleItem.getName());
            String euro = mContext.getString(R.string.euroStr);
            holder.tvProductPrice.setText(euro + itemsList.get(position).getDescription());
            if (singleItem.getUrl() != null) {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                URL url = new URL(singleItem.getUrl());

// Create a trust manager that does not validate certificate chains
                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }};
                // Install the all-trusting trust manager
                final SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                // Create all-trusting host name verifier
                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };

                // Install the all-trusting host verifier
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

                Glide.with(mContext)
                        .load(url)
                        .asBitmap().override(1080, 600).into(holder.itemImage);;
            } else {
           }
        } catch (Exception e) {

        }

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemsListRecentlyViewed.clear();
                sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                editor = sharedpreferences.edit();

                editor.putString(SharedPreferencesConstants.productId, itemsList.get(position).getId());
                editor.commit();

                Intent intent = new Intent(mContext.getApplicationContext(), ProductByIdActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.getApplicationContext().startActivity(intent);
                ((Activity)mContext).finish();
            }
        });

    }

    /**
     * @return
     * @throws Exception
     */
    private static SSLSocketFactory getFactorySimple() throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, null, null);
        return context.getSocketFactory();
    }

    @Override
    public int getItemCount() {
        if (itemsList != null) {
            return itemsList.size();
        }
        return 0;
        // return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle, tvProductPrice;

        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.productNameText);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.tvProductPrice = (TextView) view.findViewById(R.id.productPrice);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        }
            });


        }

    }
}

