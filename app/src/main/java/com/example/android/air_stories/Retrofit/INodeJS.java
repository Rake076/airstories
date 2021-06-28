package com.example.android.air_stories.Retrofit;

import com.example.android.air_stories.Model.Chapters;
import com.example.android.air_stories.Model.Comments;
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

    @GET("user")
    Observable<String> getUserData(@Query("user_id") int user_id);


    @Multipart
    @POST("user")
    Observable<ResponseBody> updateUserData(@Part("user_id") int user_id,
                                                @Part("username")String username,
                                                @Part("about") String about,
                                                @Part MultipartBody.Part image);


    @GET("shortStories")
        Call<List<ShortStories>> getShortStories();

    @GET("stories")
    Call<List<Stories>> getStories();


    @GET("shortStories/userStories")
    Call<List<ShortStories>> getUserShortStories(@Query("userID")int userID);

    @GET("shortStories/search")
    Call<List<ShortStories>> getShortStoriesTitle(@Query("shortTitle")String shortTitle);



    @GET("shortStories/readingList")
    Call<List<ShortStories>> getShortStoriesReadingList(@Query("userID")int userID);


    @GET("stories/userStories")
    Call<List<Stories>> getUserStories(@Query("userID")int userID);

    @GET("stories/search")
    Call<List<Stories>> getStoriesTitle(@Query("storyTitle")String storyTitle);

    @GET("stories/readingList")
    Call<List<Stories>> getStoriesReadingList(@Query("userID")int userID);


    @POST("shortStories/readingList")
    @FormUrlEncoded
    Observable<String> addShortReadingList(@Field("user_id") int userID,
                                      @Field("story_id") int story_id);

    @POST("stories/readingList")
    @FormUrlEncoded
    Observable<String> addStoryReadingList(@Field("user_id") int userID,
                                           @Field("story_id") int story_id);


    @POST("shortStories/like")
    @FormUrlEncoded
    Observable<String> likeShortStory(@Field("userID") int userID,
                                @Field("shortID") int shortID);

    @POST("shortStories/unlike")
    @FormUrlEncoded
    Observable<String> unlikeShortStory(@Field("userID") int userID,
                                @Field("shortID") int shortID);

    @GET("shortStories/like")
    Observable<String> isShortLiked(@Query("userID") int userID,
                                    @Query("shortID")int shortID);

    @POST("stories/like")
    @FormUrlEncoded
    Observable<String> likeStory(@Field("user_id") int userID,
                                      @Field("story_id") int storyID);

    @POST("stories/unlike")
    @FormUrlEncoded
    Observable<String> unlikeStory(@Field("user_id") int userID,
                                        @Field("story_id") int storyID);

    @GET("stories/like")
    Observable<String> isStoryLiked(@Query("user_id") int userID,
                                    @Query("story_id")int storyID);

    @GET("shortStories/comments")
    Call<List<Comments>> getShortComments(@Query("story_id")int story_id);

    @POST("shortStories/comments")
    @FormUrlEncoded
    Observable<String> submitShortComment(@Field("story_id") int story_id,
                                        @Field("user_id") int user_id,
                                          @Field("comment") String comment);

    @GET("stories/comments")
    Call<List<Comments>> getStoryComments(@Query("story_id")int story_id);

    @POST("stories/comments")
    @FormUrlEncoded
    Observable<String> submitStoryComment(@Field("story_id") int story_id,
                                          @Field("user_id") int user_id,
                                          @Field("comment") String comment);


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
    Observable<ResponseBody> submitShortStories(@Part("userID") int userID,
                                          @Part("shortTitle")String title,
                                          @Part("shortStory") String shortStory,
                                          @Part("shortGenre") String shortGenre,
                                          @Part("shortDescription") String shortDescription,
//                                           @Part("cover") MultipartBody.Part image);
                                           @Part MultipartBody.Part image);

    @Multipart
    @POST("stories")
//    @FormUrlEncoded
    Observable<ResponseBody> submitStories(@Part("userID") int userID,
                                                @Part("storyTitle")String title,
                                                @Part("storyGenre") String genre,
                                                @Part("storyDescription") String description,
                                                @Part("chapter_name") String chapter_title,
                                                @Part("chapter_text") String chapter_text,
//                                           @Part("cover") MultipartBody.Part image);
                                                @Part MultipartBody.Part image);


    @Multipart
    @POST("stories/edit")
//    @FormUrlEncoded
    Observable<ResponseBody> editStory(@Part("story_id") int story_id,
                                            @Part("story_title")String story_title,
                                            @Part("story_genre") String story_genre,
                                            @Part("story_description") String story_description,
                                            @Part MultipartBody.Part image);

    @Multipart
    @POST("shortStories/edit")
//    @FormUrlEncoded
    Observable<ResponseBody> editShortStory(@Part("shortID") int shortID,
                                                @Part("shortTitle")String title,
                                                @Part("shortStory") String shortStory,
                                                @Part("shortGenre") String shortGenre,
                                                @Part("shortDescription") String shortDescription,
//                                           @Part("cover") MultipartBody.Part image);
                                                @Part MultipartBody.Part image);

    @POST("stories/chapters")
    @FormUrlEncoded
    Observable<String> submitChapters(@Field("story_id") int story_id,
                                            @Field("chapter_name") String chapter_title,
                                            @Field("chapter_text") String chapter_text);

}
