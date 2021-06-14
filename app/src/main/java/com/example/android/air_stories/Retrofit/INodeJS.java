package com.example.android.air_stories.Retrofit;

import com.example.android.air_stories.Model.Chapters;
import com.example.android.air_stories.Model.Journals;
import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;

import java.util.List;

import io.reactivex.Observable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("username") String username,
                                    @Field("password")String password);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                    @Field("password")String password);


    @GET("shortStories")
        Call<List<ShortStories>> getShortStories();

    @GET("stories")
    Call<List<Stories>> getStories();


    @GET("shortStories/userStories")
    Call<List<ShortStories>> getUserShortStories(@Query("userID")int userID);


    @GET("stories/chapters")
    Call<List<Chapters>> getStoryChapters(@Query("story_id")int story_id);

    @GET("journals")
    Call<List<Journals>> getUserJournals(@Query("userID")int userID);

    @POST("journals")
    @FormUrlEncoded
    Observable<String> submitJournals(@Field("userID") int userID,
                                      @Field("journal") String journal,
                                        @Field("journal_title") String journal_title);

    //    @Multipart
//    @POST("user/updateprofile")
//    Observable<ResponseBody> updateProfile(@Part("user_id") RequestBody id,
//                                           @Part("full_name") RequestBody fullName,
//                                           @Part MultipartBody.Part image,
//                                           @Part("other") RequestBody other);

    @Multipart
    @POST("shortStories")
//    @FormUrlEncoded
    Observable<ResponseBody> submitShortStories(@Part("userID") int userID,
                                          @Part("shortTitle")String title,
                                          @Part("shortStory") String shortStory,
                                          @Part("shortGenre") String shortGenre,
                                          @Part("shortDescription") String shortDescription,
//                                           @Part("cover") MultipartBody.Part image);
                                           @Part MultipartBody.Part image);


}
