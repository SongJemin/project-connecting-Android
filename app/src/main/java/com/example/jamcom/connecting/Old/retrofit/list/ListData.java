package com.example.jamcom.connecting.Old.retrofit.list;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JAMCOM on 2018-04-16.
 */

public class ListData{

    @SerializedName("date")
    private String post_date;
    @SerializedName("title")
    private String post_title;
    @SerializedName("type")
    private String post_type;

    public int post_imgUrl;
    public int profile1, profile2, profile3;


    public ListData(String post_title, String post_date, String post_type, int post_imgUrl, int profile1, int profile2, int profile3){
        this.post_title = post_title;
        this.post_date = post_date;
        this.post_type = post_type;
        this.post_imgUrl = post_imgUrl;
        this.profile1 = profile1;
        this.profile2 = profile2;
        this.profile3 = profile3;
    }

    public String getTitle(){
        return post_title;
    }

    public String getDate(){
        return post_date;
    }

    public String getType(){
        return post_type;
    }

    public int getImgUrl(){
        return post_imgUrl;
    }

    public int getProfile1(){
        return profile1;
    }

    public int getProfile2(){
        return profile2;
    }

    public int getProfile3(){
        return profile3;
    }


}
