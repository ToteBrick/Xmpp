package com.zhj.xmpp;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.zhj.xmpp.activity.LoginActivity;
import com.zhj.xmpp.utils.ThreadUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        XMPPConnection connection = new XMPPConnection();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SystemClock.sleep(3000);
//
//            }
//        }).start();

        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
