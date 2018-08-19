package com.example.jamcom.connecting.Network

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApplicationController : Application(){

    lateinit var networkSerVice : NetworkService

    //private val baseUrl = "http://52.78.122.242:3000"
    //private val baseUrl = "http://18.188.54.59:8080" 이전서버
    private val baseUrl = "http://54.180.24.25:8080"
    companion object {
        lateinit var instance: ApplicationController
        //일종의 스태틱
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        buildNetwork()
    }

    fun buildNetwork(){
        val builder = Retrofit.Builder()
        val retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        networkSerVice = retrofit.create(NetworkService::class.java)
    }


}