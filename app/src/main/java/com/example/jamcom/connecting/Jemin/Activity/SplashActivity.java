package com.example.jamcom.connecting.Jemin.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.jamcom.connecting.R;

/**
 * Created by JAMCOM on 2018-03-25.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
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


        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3000); // 3000ms=3초후에 핸들러 실행 //딜레이 3000
    }

    private class splashhandler implements Runnable{
        public void run(){
            //startActivity(new Intent(getApplication(), LoginActivity.class));
            startActivity(new Intent(getApplication(), UserSelectActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out); // fade in, fade out 애니메이션 효과

            SplashActivity.this.finish(); // 스플래쉬 페이지 액티비티 스택에서 제거
        }
    }

    @Override
    public void onBackPressed(){
        //스플래쉬 화면에서 뒤로가기 버튼 금지
    }


}
