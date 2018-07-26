package com.example.jamcom.connecting.Jemin.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jamcom.connecting.R;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

public class RoomViewActivity extends AppCompatActivity {
    private Button inviteBtn;

    String url = "https://cdn-images-1.medium.com/max/2000/1*irPXj5W9eigW-VY7LvYX8Q.jpeg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_view);

        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                    /*
                    KakaoLink kakaoLink = KakaoLink.getKakaoLink(RoomViewActivity.this);
                    KakaoTalkLinkMessageBuilder messageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                    messageBuilder.addText("연결고리 어플리케이션입니다.");
                    messageBuilder.addImage(url,160,160);
                    messageBuilder.addAppButton("연결고리 앱 실행");
                    kakaoLink.sendMessage(messageBuilder,RoomViewActivity.this);

                    */
               // sendDefaultTextTemplate();
                sendLink();

            }
        });



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
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void sendDefaultTextTemplate(){
        TextTemplate params = TextTemplate.newBuilder("약속방으로 초대합니다!", LinkObject.newBuilder().setWebUrl("확인하러 가기").setMobileWebUrl("afsd").build()).setButtonTitle("asfd").build();

        KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });
    }

    private void sendLink() {
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("졸업작품 회의",
                        url,
                        LinkObject.newBuilder().setWebUrl("")
                                .setMobileWebUrl("").build())
                        .setDescrption("일하자")
                        .build())
                .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                        .setWebUrl("'https://developers.kakao.com")
                        .setMobileWebUrl("'https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .build();

        KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {

            }
        });

    }
}
