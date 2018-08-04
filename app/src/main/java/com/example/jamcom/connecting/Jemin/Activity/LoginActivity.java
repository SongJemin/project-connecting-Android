package com.example.jamcom.connecting.Jemin.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.jamcom.connecting.R;
import com.kakao.auth.ISessionCallback;

import com.kakao.auth.Session;
import com.kakao.auth.helper.Base64;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by JAMCOM on 2018-03-26.
 */

public class LoginActivity extends Activity {

    private SessionCallback sessionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
            }
        }else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.BLACK);
        }
        try{
            PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures){
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("aaaa", Base64.encodeBase64URLSafeString(messageDigest.digest()));
            }
        } catch (Exception e){
            Log.d("error", "PackageInfo error is " + e.toString());
        }

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
        String token = Session.getCurrentSession().getTokenInfo().getAccessToken();
        Log.v("TAG", "토큰값 = "+token);
        //com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
        //한번이라도 로그인한 기록이있다면 재로그인

        // 로컬에 저장되어 있는 토큰 만료 시간 값
        // Session.getCurrentSession().getTokenInfo().hasValidAccessToken()

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            return ;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void request(){
        UserManagement.getInstance().requestMe(new MeResponseCallback() {

            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.d("Error", "오류로 카카오로그인 실패 ");
            }
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("Error", "오류로 카카오로그인 실패 ");
            }
            @Override
            public void onNotSignedUp() {
            }
            @Override
            public void onSuccess(final UserProfile userProfile) {
                //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴

                Log.e("UserProfile", userProfile.toString());
               // Toast.makeText(LoginActivity.this, "사용자 이름은 " + userProfile.getNickname(), Toast.LENGTH_SHORT).show();

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable()
                {
                    public void run() {


                        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                        //myIntent.putExtra("nickName",userProfile.getNickname());
                        startActivity(myIntent);		//main.class 시작
                    }
                }, 3000);//딜레이 3000

            }
        });


        /* 나중에 사용할 카카오 계정 정보들
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("error", "Session Closed Error is " + errorResult.toString());
            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onSuccess(UserProfile result) {
                Toast.makeText(LoginActivity.this, "사용자 이름은 " + result.getNickname(), Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    private class SessionCallback implements ISessionCallback{

        @Override
        public void onSessionOpened() {
            request();

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d("error", "Session Fail Error is " + exception.getMessage().toString());
        }
    }






}