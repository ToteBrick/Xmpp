package com.zhj.xmpp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.zhj.xmpp.R;
import com.zhj.xmpp.utils.ThreadUtils;

public class SplashActivity extends Activity {

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
