package com.example.jamcom.connecting.Old.retrofit;

import com.example.jamcom.connecting.Old.retrofit.list.ListData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by JAMCOM on 2018-06-04.
 */

public interface APIService {

    @GET("/boot/rest/posts/all")
    Call<List<ListData>> getPostData();

    @FormUrlEncoded
    @POST("/boot/rest/posts/postUp")
    Call<Post> sendName(@Field("roomTitle") String title, @Field("roomDate") String date, @Field("roomImgUrl") String imgUrl, @Field("fk_typeID") String type);

}
