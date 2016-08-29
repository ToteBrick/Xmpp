package com.zhj.xmpp.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhj.xmpp.DB.SmsOpenHelper;
import com.zhj.xmpp.R;
import com.zhj.xmpp.provider.SmsProvider;
import com.zhj.xmpp.service.IMService;
import com.zhj.xmpp.utils.ThreadUtils;

import org.jivesoftware.smack.packet.Message;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChatActivity extends ActionBarActivity {

	public static final String	CLICKACCOUNT		= "clickaccount";
	public static final String	CLICKNICKNAME		= "clicknickname";
	private static final int	VIEWTYPE_RECEIVE	= 0;
	private static final int	VIEWTYPE_SEND		= 1;
	@InjectView(R.id.title)
	TextView					mTitle;
	@InjectView(R.id.listView)
	ListView					mListView;
	@InjectView(R.id.et_body)
	EditText					mEtBody;
	@InjectView(R.id.btn_send)
	Button						mBtnSend;
	private String				mClickAccount;
	private String				mClickNickName;
	private CursorAdapter		mAdapter;
	private IMService mImService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		ButterKnife.inject(this);
		init();
		initView();
		initData();
	}

	private void init() {
		registerContentObserver();
		bindService();
		mClickAccount = getIntent().getStringExtra(CLICKACCOUNT);
		mClickNickName = getIntent().getStringExtra(CLICKNICKNAME);
	}

	private void initView() {
		// 设置title
		mTitle.setText("与" + mClickNickName + "聊天");
	}

	private void initData() {
		setAdapterOrNotify();
	}

	/**
	 * 设置adapter或者notify
	 */
	private void setAdapterOrNotify() {
		// 如果adapter不等于null,直接刷新就好
		if (mAdapter != null) {
			mAdapter.getCursor().requery();

			if (mAdapter.getCount() > 0) {
				// 选中mAdapter中最后一条数据
				mListView.setSelection(mAdapter.getCount() - 1);
			}
			return;
		}

		// 开启子线程查询游标数据
		ThreadUtils.runInThread(new Runnable() {
			@Override
			public void run() {
				final Cursor c =
						getContentResolver().query(SmsProvider.SMS_URI,
								null, //
								"(from_account = ? and to_account=?)or(from_account = ? and to_account= ? )",//
								new String[] { IMService.curLoginAccount, mClickAccount, mClickAccount,
										IMService.curLoginAccount },//
								SmsOpenHelper.SMSTABLE.TIME + " ASC");//
				// 在主线程中创建adapter
				ThreadUtils.runInUIThread(new Runnable() {
					@Override
					public void run() {
						mAdapter = new CursorAdapter(ChatActivity.this, c) {
							/*// 对应如果convertview==null==>决定根视图
							@Override
							public View newView(Context context, Cursor cursor, ViewGroup parent) {
								TextView tv = new TextView(context);
								return tv;
							}

							@Override
							public void bindView(View rootView, Context context, Cursor cursor) {
								// 得到数据
								String body = cursor.getString(c.getColumnIndex(SmsOpenHelper.SMSTABLE.BODY));
								// 设置数据
								TextView tv = (TextView) rootView;
								tv.setText(body);
							}*/

							@Override
							public int getViewTypeCount() {
								return super.getViewTypeCount() + 1;// 1+1=2
							}

							@Override
							public int getItemViewType(int position) {
								c.moveToPosition(position);
								// 消息从哪里来
								String fromAccount = c.getString(c.getColumnIndex(SmsOpenHelper.SMSTABLE.FROM_ACCOUNT));
								// 如果fromAccount 和 当前登录的账户相等===>消息是当前用户发送的
								// 如果fromAccount 和 当前登录的账户不相等===>消息是当前用户收到的
								if (IMService.curLoginAccount.equals(fromAccount)) {// 如果fromAccount 和 当前登录的账户相等
									return VIEWTYPE_SEND;
								} else {
									return VIEWTYPE_RECEIVE;
								}
							}

							/**覆写CursorAdapter中的getView方法*/
							@Override
							public View getView(int position, View convertView, ViewGroup parent) {
								ViewHolder holder = null;
								if (convertView == null) {

									/*--------------- 根本不同的viewType类型,返回不同的根视图 ---------------*/
									if (getItemViewType(position) == VIEWTYPE_SEND) {// 发送者
										convertView = View.inflate(ChatActivity.this, R.layout.item_chat_send, null);
										holder = new ViewHolder();
										holder.ivHead = (ImageView) convertView.findViewById(R.id.head);
										holder.tvContent = (TextView) convertView.findViewById(R.id.content);
										holder.tvTime = (TextView) convertView.findViewById(R.id.time);
										convertView.setTag(holder);
									} else {// 接收者
										convertView = View.inflate(ChatActivity.this, R.layout.item_chat_receive, null);
										holder = new ViewHolder();
										holder.ivHead = (ImageView) convertView.findViewById(R.id.head);
										holder.tvContent = (TextView) convertView.findViewById(R.id.content);
										holder.tvTime = (TextView) convertView.findViewById(R.id.time);
										convertView.setTag(holder);
									}

								} else {
									holder = (ViewHolder) convertView.getTag();
								}

								// 得到数据
								c.moveToPosition(position);
								String body = c.getString(c.getColumnIndex(SmsOpenHelper.SMSTABLE.BODY));
								String time = c.getString(c.getColumnIndex(SmsOpenHelper.SMSTABLE.TIME));
								// 设置数据
								holder.tvTime.setText(time);// 设置聊天时间
								holder.tvContent.setText(body);// 设置聊天具体内容

								return convertView;
							}

							@Override
							public View newView(Context context, Cursor cursor, ViewGroup parent) {
								return null;
							}

							@Override
							public void bindView(View view, Context context, Cursor cursor) {

							}

							class ViewHolder {
								TextView	tvTime;
								TextView	tvContent;
								ImageView	ivHead;
							}
						};

						// 设置adapter
						mListView.setAdapter(mAdapter);

						if (mAdapter.getCount() > 0) {
							// 选中mAdapter中最后一条数据
							mListView.setSelection(mAdapter.getCount() - 1);
						}
					}
				});
			}
		});
	}

	@OnClick(R.id.btn_send)
	public void send() {
		final String body = mEtBody.getText().toString();
		if (TextUtils.isEmpty(body)) {
			Toast.makeText(getApplicationContext(), "消息内容不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		ThreadUtils.runInThread(new Runnable() {
			@Override
			public void run() {
				// 4.创建消息
				Message msg = new Message();
				msg.setType(Message.Type.chat);// 消息类型
				msg.setBody(body);// 消息内容
				msg.setFrom(IMService.curLoginAccount);// 消息从哪里来-->当前登录的用户
				msg.setTo(mClickAccount);// 消息到哪里去
				System.out.println("mClickAccount:" + mClickAccount);
				// msg.setProperty("key","value");// 消息的附加属性
				// msg.setProperty("key","value");// 消息的附加属性

				if (mImService != null) {
					// 发送消息-->调用服务里面的sendmessage方法进行消息的发送
					mImService.sendMessage(msg);
				}

				// bindService启动服务
				ThreadUtils.runInUIThread(new Runnable() {
					@Override
					public void run() {
						mEtBody.setText("");
					}
				});
			}
		});
	}

	@Override
	protected void onDestroy() {
		unRegisterContentObserver();
		unBindService();
		super.onDestroy();
	}

	/*=============== 使用contentObserver监听数据库记录的改变,实时的更新ui ===============*/
	MySmsContentObserver	mMySmsContentObserver	= new MySmsContentObserver(new Handler());

	class MySmsContentObserver extends ContentObserver {
		public MySmsContentObserver(Handler handler) {
			super(handler);
		}

		/**
		 * 如果数据库记录改变,对应会回调该方法,我们在该方法,更新ui即可
		 */
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			// 更新ui
			setAdapterOrNotify();
		}
	}

	/**
	 * 注册监听
	 */
	public void registerContentObserver() {
		getContentResolver().registerContentObserver(SmsProvider.SMS_URI, true, mMySmsContentObserver);
	}

	/**
	 * 反注册监听
	 */
	public void unRegisterContentObserver() {
		getContentResolver().unregisterContentObserver(mMySmsContentObserver);
	}

	/**绑定服务*/
	public void bindService() {
		Intent service = new Intent(ChatActivity.this, IMService.class);
		bindService(service, mMyServiceConnection, BIND_AUTO_CREATE);
	}

	/**解绑服务*/
	public void unBindService() {
		unbindService(mMyServiceConnection);
	}

	MyServiceConnection	mMyServiceConnection	= new MyServiceConnection();

	class MyServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {// 服务连接的时候
			IMService.MyBinder myBinder = (IMService.MyBinder) service;
			// 调用myBinder类里面的方法
			mImService = myBinder.getImService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {// 服务断开连接的时候

		}
	}
}
