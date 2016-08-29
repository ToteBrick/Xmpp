package com.zhj.xmpp.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhj.xmpp.DB.ContactOpenHelper;
import com.zhj.xmpp.R;
import com.zhj.xmpp.activity.ChatActivity;
import com.zhj.xmpp.provider.ContactsProvider;
import com.zhj.xmpp.utils.ThreadUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

	private ListView		mListView;
	private CursorAdapter	mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
		initView(rootView);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData();
		initListener();
		super.onActivityCreated(savedInstanceState);
	}

	private void init() {
		registerContentObserver();
	}

	private void initView(View rootView) {
		mListView = (ListView) rootView.findViewById(R.id.session_listview);
	}

	private void initListener() {
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), ChatActivity.class);
				// 得到对应的游标
				Cursor cursor = mAdapter.getCursor();
				// 游标移动到点击条目对应行
				cursor.moveToPosition(position);
				// 取值
				String account = cursor.getString(cursor.getColumnIndex(ContactOpenHelper.CONTACTTABLE.ACCOUNT));
				String nickname = cursor.getString(cursor.getColumnIndex(ContactOpenHelper.CONTACTTABLE.NICKNAME));

				intent.putExtra(ChatActivity.CLICKACCOUNT, account);
				intent.putExtra(ChatActivity.CLICKNICKNAME, nickname);
				startActivity(intent);
			}
		});
	}

	private void initData() {
		setAdapterOrNotify();
		// BaseAdapter
		// mListView.setAdapter(new SessionAdapter());
	}

	/**
	 * 设置adapter或者更新adapter
	 */
	private void setAdapterOrNotify() {
		// 判断adapter是否存在
		if (mAdapter != null) {
			// 刷新当前的adapter(cursoradapter)
			mAdapter.getCursor().requery();
			return;
		}

		ThreadUtils.runInThread(new Runnable() {
			@Override
			public void run() {
				// 在子线程中查询对应的cursor数据
				final Cursor cursor =
<<<<<<< Updated upstream
						getActivity().getContentResolver().query(ContactProvider.CONTACT_URI, null, null, null, null);
=======
						getActivity().getContentResolver().query(ContactsProvider.CONTACT_URI, null, null, null, null);
>>>>>>> Stashed changes

				// 在主线程中创建cursorAdapter,然后设置adapter
				ThreadUtils.runInUIThread(new Runnable() {
					@Override
					public void run() {
						// CursorAdapter 创建不能在子线程
						// getview-->newView()-->bindView()
						mAdapter = new CursorAdapter(getActivity(), cursor) {
							// convertView == null会调用该方法,决定根视图
							@Override
							public View newView(Context context, Cursor cursor, ViewGroup parent) {
								// 决定根视图
								View rootView = View.inflate(getActivity(), R.layout.item_contact, null);
								return rootView;
							}

							// 展示具体的数据
							@Override
							public void bindView(View rootView, Context context, Cursor cursor) {
								// 得到数据
								String account =
<<<<<<< Updated upstream
										cursor.getString(cursor.getColumnIndex(ContactOpenHelper.CONTACTTABLE.ACOUNT));
=======
										cursor.getString(cursor.getColumnIndex(ContactOpenHelper.CONTACTTABLE.ACCOUNT));
>>>>>>> Stashed changes
								String nickName =
										cursor.getString(cursor.getColumnIndex(ContactOpenHelper.CONTACTTABLE.NICKNAME));
								// 展示数据
								TextView tvNickName = (TextView) rootView.findViewById(R.id.nickname);
								TextView tvAccount = (TextView) rootView.findViewById(R.id.account);

								// 设置相应的文本
								tvNickName.setText(nickName);
								tvAccount.setText(account);

							}
						};
						// listView设置adapter
						mListView.setAdapter(mAdapter);
					}
				});
			}
		});
	}

	/*=============== 使用contentObserver时刻监听记录的改变 ===============*/

	/**
	 * 注册contentObserver
	 */
	public void registerContentObserver() {
		// getActivity().getContentResolver().registerContentObserver(对哪一个uri进行监听,是否通知后代,指定具体接收结果的contentObserver);
		getActivity().getContentResolver().registerContentObserver(ContactsProvider.CONTACT_URI, true,
				mMyContentObserver);
	}

	/**
	 * 反注册contentObserver
	 */
	public void unRegisterContentObserver() {
		getActivity().getContentResolver().unregisterContentObserver(mMyContentObserver);
	}

	MyContentObserver	mMyContentObserver	= new MyContentObserver(new Handler());

	class MyContentObserver extends ContentObserver {
		public MyContentObserver(Handler handler) {
			super(handler);
		}

		/**
		 * 监听记录的改变,会对应走到onChange方法里来
		 */
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			super.onChange(selfChange, uri);
			// 需要更新ui
			// 其实就是更新adapter
			// adapter
			// 如果adapter存在-->刷新adapter
			// 如果adapter不存在-->创建adapter然后在设置adapter
			setAdapterOrNotify();
		}
	}

	@Override
	public void onDestroy() {
		unRegisterContentObserver();
		super.onDestroy();
	}
}
