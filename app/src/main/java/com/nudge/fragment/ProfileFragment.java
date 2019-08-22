package com.nudge.fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nudge.App;
import com.nudge.R;
import com.nudge.activity.EditProfileOneActivity;
import com.nudge.activity.PrivacyPolicyActivity;
import com.nudge.activity.SettingActivity;
import com.nudge.activity.TermsAndConditionsActivity;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
public class ProfileFragment extends Fragment {
    TextView  tv_user_name, tv_user_country;
    LinearLayout accountSettingLayout, termsAndConditionsLay, editProfileLay, rl_rate_app, ll_about, privacy_policy_lay;
    ImageView iv_profile_pic;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static String userFirstName = "", userLastName = "", userEmail = "", userImageUrl = "",
            userGender = "", userDob = "", userCountry = "", userNewPassword = "", userOldPassword = "", userConfirmPassword = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_profile, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        accountSettingLayout = (LinearLayout) rootView.findViewById(R.id.accountSettingLayout);
        termsAndConditionsLay = (LinearLayout) rootView.findViewById(R.id.termsAndConditionsLay);
        privacy_policy_lay = (LinearLayout) rootView.findViewById(R.id.privacy_policy_lay);
        ll_about = (LinearLayout) rootView.findViewById(R.id.ll_about);
        editProfileLay = (LinearLayout) rootView.findViewById(R.id.editProfileLay);
        rl_rate_app = (LinearLayout) rootView.findViewById(R.id.rl_rate_app);
        tv_user_name = (TextView) rootView.findViewById(R.id.tv_user_name);
        tv_user_country = (TextView) rootView.findViewById(R.id.tv_user_country);
        iv_profile_pic = (ImageView) rootView.findViewById(R.id.iv_profile_pic);

        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading()
                .cacheOnDisc()
                .cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        privacy_policy_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), PrivacyPolicyActivity.class);
                startActivity(in);
            }
        });

        if(!sharedpreferences.getString(SharedPreferencesConstants.userImageUrl,"").equals(""))
        {
            String imageName = sharedpreferences.getString(SharedPreferencesConstants.userImageUrl, "");
            updateView(imageName, iv_profile_pic);
        }
        editProfileLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userFirstName = "";
                userLastName = "";
                userEmail = "";
                userImageUrl = "";
                userGender = "";
                userDob = "";
                userCountry = "";
                userNewPassword = "";
                userOldPassword = "";
                userConfirmPassword = "";

                userFirstName = sharedpreferences.getString(SharedPreferencesConstants.userFirstName, "");
                userLastName = sharedpreferences.getString(SharedPreferencesConstants.userLastName, "");
                userEmail = sharedpreferences.getString(SharedPreferencesConstants.userEmail, "");
                userImageUrl = sharedpreferences.getString(SharedPreferencesConstants.userImageUrl, "");
                userGender = sharedpreferences.getString(SharedPreferencesConstants.userGender, "");
                userDob = sharedpreferences.getString(SharedPreferencesConstants.userDob, "");
                userCountry = sharedpreferences.getString(SharedPreferencesConstants.userCountry, "");

                startActivity(new Intent(getActivity(), EditProfileOneActivity.class));
            }
        });
        accountSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        termsAndConditionsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TermsAndConditionsActivity.class));
            }
        });
        rl_rate_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!sharedpreferences.getString(SharedPreferencesConstants.userImageUrl,"").equals(""))
        {
            String imageName = sharedpreferences.getString(SharedPreferencesConstants.userImageUrl, "");
            updateView(imageName, iv_profile_pic);
        }
        String fullName = sharedpreferences.getString(SharedPreferencesConstants.userFirstName, "") + " " + sharedpreferences.getString(SharedPreferencesConstants.userLastName, "");
        tv_user_name.setText(Utils.convertStringToCamelCase(fullName));
        tv_user_country.setText(Utils.convertStringToCamelCase(sharedpreferences.getString(SharedPreferencesConstants.userCountry, "")));
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
}
