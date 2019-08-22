package com.nudge.rest;

import com.nudge.category.ProductResponse;
import com.nudge.model.ForgotPassword;
import com.nudge.model.GetConfigResponse;
import com.nudge.pojo.AddFavoritesResponse;
import com.nudge.pojo.Checkpojo;
import com.nudge.pojo.EventDateResponse;
import com.nudge.pojo.GetCategoryList;
import com.nudge.pojo.GetFavouritesResponse;
import com.nudge.pojo.GetProductByCategoryId;
import com.nudge.pojo.GetProductByProductId;
import com.nudge.pojo.GetProfileResponse;
import com.nudge.pojo.GetShopCategories;
import com.nudge.pojo.GetUserDetailsResponse;
import com.nudge.pojo.LoginResponse;
import com.nudge.pojo.PersonaResponse;
import com.nudge.pojo.RatingBean;
import com.nudge.pojo.RemoveAccountBean;
import com.nudge.pojo.RemoveOccasionResponse;
import com.nudge.pojo.SignUpResponse;
import com.nudge.pojo.UpcomingNudgesResponse;
import com.nudge.pojo.UpdateProfileManualResponse;
import com.nudge.pojo.UpdateRegistration;
import com.nudge.pojo.UploadFbContactResponse;
import com.nudge.pojo.UploadManualContact;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by ADVANTAL on 6/11/2017.
 */

public interface ApiInterface {

    @GET("http://thegiftingapp.com/nudge/woo_api/get_shop_categories.php")
    Call<GetShopCategories> getShopCategories();

    @FormUrlEncoded
    @POST("RemoveEvents")
    Call<RemoveOccasionResponse> removeOccasion(@Field("event_date_id") String event_date_id);

    @GET("http://thegiftingapp.com/nudge/woo_api/get_productbyid.php")
    Call<GetProductByProductId> getProductByProductId(@Query("product_id") String productId, @Query("debug") String debug);

    @GET("http://thegiftingapp.com/nudge/woo_api/get_productby_categoryid.php")
    Call<List<ProductResponse>> getProductByCategoryId(@Query("category_id") String categoryId, @Query("offset") String offSet, @Query("per_page") String perPage, @Query("debug") String debug);

    @GET("http://thegiftingapp.com/nudge/woo_api/get_category_list.php")
    Call<GetCategoryList> getCategoryList(@Query("offset") String offset, @Query("per_page") String perPage, @Query("parent") String parent, @Query("debug") String debug);

    @FormUrlEncoded
    @POST("AddFavorites")
    Call<AddFavoritesResponse> setFavorites(@Field("user_id") String userId, @Field("friend_id") String friendId, @Field("prod_id") String prodId, @Field("prod_name") String prodName, @Field("price") String price, @Field("prod_image") String productImage);

    @FormUrlEncoded
    @POST("GetFavorites")
    Call<GetFavouritesResponse> getFavorites(@Field("user_id") String userId, @Field("friend_id") String friendId);

    @FormUrlEncoded
    @POST("UserDetails")
    Call<GetUserDetailsResponse> getUserDetails(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("UpcomingNudges")
    Call<UpcomingNudgesResponse> getUpcomingNudges(@Field("user_id") String userId);
  @Multipart
    @POST("UpdateProfileManual")
    Call<UpdateProfileManualResponse> updateProfileManual(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("EventDateNew2")
    Call<EventDateResponse> uploadEventDate(@Field("body") String uploadEventDate);

    @FormUrlEncoded
    @POST("Personna")
    Call<PersonaResponse> uploadPersona(@Field("body") String personaRequest);

    @FormUrlEncoded
    @POST("GetProfile")
    Call<GetProfileResponse> getProfileResponse(@Field("user_id") String userId);


    @POST("GetConfig")
    Call<GetConfigResponse> getConfig();

    @FormUrlEncoded
    @POST("UploadProfiles")
    Call<UploadFbContactResponse> uploadFbContact(@Field("body") String uploadFbContact);

    @FormUrlEncoded
    @POST("RateMyApp")
    Call<RatingBean> sendFeedback(@Field("user_id") String user_id, @Field("push_not") String push_not, @Field("email_not") String email_not, @Field("feedback") String feedback, @Field("rating") String rating);

    @FormUrlEncoded
    @POST("ForgotPassword")
    Call<ForgotPassword>checkForgot(@Field("email") String email);

    @FormUrlEncoded
    @POST("RemoveUserAccount")
    Call<RemoveAccountBean> removeAccount(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("LoginByFb")
    Call<Checkpojo> checkExistence(@Field("fb_id") String fbid, @Field("password") String password, @Field("device_token") String device_token, @Field("device_type") String device_type);


    @FormUrlEncoded
    @POST("LoginByEmail")
    Call<LoginResponse>getResponse(@Field("email") String email, @Field("password") String password, @Field("device_token") String device_token, @Field("device_type") String device_type);

    @Multipart
    @POST("RegistrationV1")
    Call<SignUpResponse> uploadProfile(
            @PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);

    @Multipart
    @POST("UploadProfileManual")
    Call<UploadManualContact> uploadManualContact(
            @PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);
     @Multipart
    @POST("UpdateRegistration")
    Call<UpdateRegistration> getUpdateProfileResponse(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);
}
