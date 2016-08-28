package com.zhj.xmpp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhj.xmpp.R;
import com.zhj.xmpp.service.IService;
import com.zhj.xmpp.utils.ThreadUtils;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * Created by hasee on 2016/8/27.
 */
public class LoginActivity extends Activity {

    private EditText mEtUserName;
    private EditText mEtPassword;
    private Button mBtnLogin;
    public static  final String HOST ="192.168.56.1"; //服务器ip地址
    public static final int PORT = 5222; //xmpp默认的通信端口号
    public static final String SERVER_NAME = "zhj"; //服务器名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Log.d("LoginActivity", "login");

        initView();
        initData();
        initEvent();
    }





    private void initView() {
        mEtUserName = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn);
    }
    private void initData() {

    }

    private void initEvent() {

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保证帐号、密码不能为空
                final String userName = mEtUserName.getText().toString();
                final String password = mEtPassword.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    mEtUserName.setError("帐号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mEtPassword.setError("密码不能为空");
                    return;
                }
                ThreadUtils.runInThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //开始登陆
                            //连接配置参数的创建
                            ConnectionConfiguration config = new ConnectionConfiguration(HOST, PORT);
                            //额外配置
                            config.setDebuggerEnabled(true); //开启调试模式 ->打印具体的xml，方便查看
                            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);//禁用安全模式,明文调试，上线时调试回来
                            //建立连接
                            XMPPConnection conn = new XMPPConnection(config);
                            //1,直接连接到服务器
                            conn.connect();
                            //2,连接成功
                            //开始登录
                            conn.login(userName,password);
                            //登录成功
                            //保存相关变量
                            IService.conn = conn;
                            ThreadUtils.runInUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } catch (XMPPException e) {
                            ThreadUtils.runInUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }
}
