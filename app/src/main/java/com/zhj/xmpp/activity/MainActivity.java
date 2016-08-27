package com.zhj.xmpp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhj.xmpp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by hasee on 2016/8/27.
 */
public class MainActivity extends Activity {


    @InjectView(R.id.main_title)
    TextView mMainTitle;
    @InjectView(R.id.main_viewpager)
    ViewPager mMainViewpager;
    @InjectView(R.id.main_ll_bottom)
    LinearLayout mMainLlBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        init();
        initView();
        initData();
        initListner();

    }

    private void init() {

    }

    private void initView() {

    }

    private void initData() {

    }

    private void initListner() {
    }
}
