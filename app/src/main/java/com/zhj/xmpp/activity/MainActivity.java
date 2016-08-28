package com.zhj.xmpp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhj.xmpp.R;
import com.zhj.xmpp.fragment.SessionFragment;
import com.zhj.xmpp.fragment.ContactFragment;
import com.zhj.xmpp.utils.ToolBarUtils;

import java.util.ArrayList;
import java.util.List;

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

    private List<Fragment> mFragments;
    FragmentManager fm;
    private ToolBarUtils mToolBarUtils;
    private int[] mToolBarIconArr;
    private String[] mToolBarTitleArr;

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

        //初始化view,初始化toolbar
        mToolBarUtils = new ToolBarUtils();
        //底部文字的数组
        mToolBarTitleArr = new String[]{"会话，联系人"};
        //按钮
        mToolBarIconArr = new int[]{R.drawable.icon_meassage_selector,R.drawable.icon_home_selector};
        mToolBarUtils.createToolBar(mMainLlBottom , mToolBarTitleArr, mToolBarIconArr);
        mToolBarUtils.changeColor(0);

    }

    private void initData() {
//        mMainViewpager-->view--> PagerAdapter
//        mMainViewpager-->view-->FragmentPagerAdapter ->会缓存fragment,适合fragment比较少的情况
//        mMainViewpager-->view--> FragmentStatePagerAdapter->只会缓存fragment的state
        mFragments = new ArrayList<Fragment>();
        mFragments.add(new ContactFragment());
        mFragments.add(new SessionFragment());
        mMainViewpager.setAdapter(new MainPagerAdapter(fm));
    }

    private void initListner() {
        mMainViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mToolBarUtils.changeColor(position);
                mMainTitle.setText(mToolBarTitleArr[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        mToolBarUtils.setOnToolBarClickListner(new ToolBarUtils.OnToolBarClickListner() {
            @Override
            public void OnToolBarClick(int position) {
                //切换viewpager
                mMainViewpager.setCurrentItem(position);
            }
        });
    }

    class MainPagerAdapter extends FragmentPagerAdapter{


        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
