package com.zhj.xmpp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.zhj.xmpp.R;

/**
 * Created by hasee on 2016/8/27.
 */
public class LoginActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_login);
    }
}
