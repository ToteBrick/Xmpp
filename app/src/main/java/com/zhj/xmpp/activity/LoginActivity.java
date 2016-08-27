package com.zhj.xmpp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhj.xmpp.R;

/**
 * Created by hasee on 2016/8/27.
 */
public class LoginActivity extends Activity {

    private EditText mEtUserName;
    private EditText mEtPassword;
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("LoginActivity", "login");

        initView();
        initData();
        initEvent();
    }



    private void initData() {

    }

    private void initView() {
        mEtUserName = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn);
    }

    private void initEvent() {

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保证帐号、密码不能为空
                String userName = mEtUserName.getText().toString();
                String password = mEtPassword.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    mEtUserName.setError("帐号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mEtPassword.setError("密码不能为空");
                    return;
                }
            }
        });

    }
}
