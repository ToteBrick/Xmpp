package com.zhj.xmpp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhj.xmpp.R;
import com.zhj.xmpp.fragment.ContactsFragment;
import com.zhj.xmpp.fragment.SessionFragment;
import com.zhj.xmpp.utils.ToolBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends ActionBarActivity {
	@InjectView(R.id.main_title)
	TextView				mMainTitle;

	@InjectView(R.id.main_ll_bottom)
	LinearLayout			mMainBottom;

	@InjectView(R.id.main_viewpager)
	ViewPager				mMainViewpager;

	private List<Fragment>	mFragments;
	private ToolBarUtils mToolBarUtils;
	private String[]		mToolBarTitleArr;

	// xutils ViewUtils HttpUtils dbUtils imageUtils
	// 只想使用ViewUtils相关的功能

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);

		init();
		initView();
		initListener();
		initData();
	}

	private void init() {

	}

	private void initView() {
		// 找出所有的控件
		// 初始化toolBar
		mToolBarUtils = new ToolBarUtils();
		// 底部文字的数组
		mToolBarTitleArr = new String[] { "会话", "联系人" };
		// 底部按钮的图标
		int[] toolBarIconArr = { R.drawable.icon_meassage_selector, R.drawable.
		icon_home_selector};

		mToolBarUtils.createToolBar(mMainBottom, mToolBarTitleArr, toolBarIconArr);
		// 默认选中第一个
		mToolBarUtils.changeColor(0);
	}

	private void initListener() {
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
				//切换ViewPager
				mMainViewpager.setCurrentItem(position);
			}

		});
	}

	private void initData() {

		mFragments = new ArrayList<Fragment>();

		mFragments.add(new SessionFragment());
		mFragments.add(new ContactsFragment());

		// mMainViewpager-->view-->PagerAdapter
		// mMainViewpager-->fragment-->FragmentPagerAdapter-->会缓存fragment,适用fragment比较少的情况
		// mMainViewpager-->fragment-->FragmentStatePagerAdapter-->只会缓存Fragment的state
		mMainViewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
	}

	class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
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
