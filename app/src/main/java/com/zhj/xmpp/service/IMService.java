package com.zhj.xmpp.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.zhj.xmpp.DB.ContactOpenHelper;
import com.zhj.xmpp.DB.SmsOpenHelper;
import com.zhj.xmpp.activity.LoginActivity;
import com.zhj.xmpp.provider.ContactProvider;
import com.zhj.xmpp.provider.SmsProvider;
import com.zhj.xmpp.utils.PinyinUtils;
import com.zhj.xmpp.utils.ThreadUtils;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/*
 * @创建者     Administrator
 *
 * @描述	      1.在oncreate方法中同步联系人数据
 * 			  2.在oncreate方法中监听联系人信息的改变
 *
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class IMService extends Service {
	// 定义xmppConnection变量
	public static XMPPConnection	conn;
	public static String			curLoginAccount;
	private Roster					mRoster;
	private ChatManager				mChatManager;
	private Chat					mChat;
	private Map<String, Chat>		mChatMap	= new HashMap<String, Chat>();	// 保存chat对象.避免chat对象频繁创建

	@Override
	public IBinder onBind(Intent intent) {
		// 返回具体的IBinder接口的接口对象
		return new MyBinder();
	}

	@Override
	public void onCreate() {

		// 数据源
		// 得到联系人
		// 得到花名册,所有联系人
		mRoster = IMService.conn.getRoster();

		ThreadUtils.runInThread(new Runnable() {
			@Override
			public void run() {
				/*=============== 联系人同步到数据库 begin ===============*/
				System.out.println("--------------联系人同步到数据库 begin--------------");
				// 得到对应的联系人实体的集合
				Collection<RosterEntry> entries = mRoster.getEntries();
				/**
				 hm2 hm2@itheima.com [org.jivesoftware.smack.RosterGroup@52910f88] both
				 hm1 hm1@itheima.com [org.jivesoftware.smack.RosterGroup@52910f88] to
				 */
				/**
				 JID=[ node”@” ] domain [ “/” resource ]
				 eg: cyber@cyberobject.com/res
				 domain:服务器域名
				 node: 用户名
				 resource:属于用户的位置或设备
				 */
				for (RosterEntry entry : entries) {
					/*System.out.print(entry.getName() + " ");//nickname
					System.out.print(entry.getUser() + " ");//hm2@itheima.com-->jid-->用户唯一标识
					System.out.println("");*/
					updateOrInsertEntry(entry);
				}
				// 同步联系人信息到contentProvider里面去
				System.out.println("--------------联系人同步到数据库 end--------------");
				/*=============== 联系人同步到数据库 end ===============*/
			}
		});

		/*--------------- 监听联系人的改变 ---------------*/
		mRoster.addRosterListener(mMyRosterListener);

		/*--------------- 创建消息管理者 begin ---------------*/
		/**
		 XmppConnection:消息通道
		 ChatManager:消息管理者-->首先有消息通道
		 Chat:聊天对象-->首先需要得到消息的管理者
		 Message:消息对象-->首先需要得到聊天对象
		 MessageListener:消息的监听者-->首先需要得到聊天对象
		 */
		// 1.消息通道
		XMPPConnection conn = IMService.conn;
		// 2.消息管理者
		if (mChatManager == null) {
			mChatManager = conn.getChatManager();
		}
		mChatManager.addChatListener(mMyChatListener);

		/*--------------- 创建消息管理者 end ---------------*/

		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		/**移除对应的rosterListener*/
		if (mMyRosterListener != null && mRoster != null) {
			mRoster.removeRosterListener(mMyRosterListener);
		}
		/**service销毁的时候,移除mMyMessageListener*/
		if (mChat != null && mMyMessageListener != null) {
			mChat.removeMessageListener(mMyMessageListener);
		}
		super.onDestroy();
	}

	/**
	 * 更新或者插入Entry,优先是更新,其次是插入
	 *
	 * @param entry
	 */
	private void updateOrInsertEntry(RosterEntry entry) {
		ContentValues values = new ContentValues();
		String account = entry.getUser();
		values.put(ContactOpenHelper.CONTACTTABLE.ACOUNT, account);

		// account = account.substring(0,account.indexOf("@"))+"@"+ LoginActivity.SERVERNAME;

		String nickName = entry.getName();
		if (nickName == null || "".equals(nickName)) {
			nickName = account.substring(0, account.indexOf("@"));// hm2@itheima.com-->hm2
		}
		values.put(ContactOpenHelper.CONTACTTABLE.NICKNAME, nickName);
		values.put(ContactOpenHelper.CONTACTTABLE.AVATOR, "0");
		values.put(ContactOpenHelper.CONTACTTABLE.PINYIN, PinyinUtils.getPinyin(nickName));
		// content://android.content.ContentProvider/contact
		// 重点-->首先考虑更新,如果更新失败,在插入
		int updateCount =
				getContentResolver().update(ContactProvider.CONTACT_URI, values,
						ContactOpenHelper.CONTACTTABLE.ACOUNT + "=?", new String[] { account });
		if (updateCount <= 0) {// 没有更新到-->其实没有对应的记录
			getContentResolver().insert(ContactProvider.CONTACT_URI, values);
		}
	}

	/*=============== 监听联系人的改变 ===============*/
	MyRosterListener	mMyRosterListener	= new MyRosterListener();

	class MyRosterListener implements RosterListener {

		@Override
		public void entriesAdded(Collection<String> collection) {// 联系人添加
			System.out.println("--------------entriesAdded--------------");
			printCollection(collection);
			for (String account : collection) {
				// 根据回调的jid(account)找到对应的entry信息
				RosterEntry entry = mRoster.getEntry(account);

				// 进行entry信息的同步
				updateOrInsertEntry(entry);
			}
		}

		@Override
		public void entriesUpdated(Collection<String> collection) {// 联系人更新
			System.out.println("--------------entriesUpdated--------------");
			printCollection(collection);

			for (String account : collection) {
				// 根据回调的jid(account)找到对应的entry信息
				RosterEntry entry = mRoster.getEntry(account);

				// 进行entry信息的同步
				updateOrInsertEntry(entry);
			}
		}

		@Override
		public void entriesDeleted(Collection<String> collection) {// 联系人删除了
			System.out.println("--------------entriesDeleted--------------");
			printCollection(collection);
			for (String account : collection) {
				// 删除数据库里面对应的记录
				getContentResolver().delete(ContactProvider.CONTACT_URI,
						ContactOpenHelper.CONTACTTABLE.ACOUNT + "=?", new String[] { account });
			}
		}

		@Override
		public void presenceChanged(Presence presence) {// 联系人状态改变
			System.out.println("--------------presenceChanged--------------");
		}
	}

	/*=============== 监听别人发送回来的消息 ===============*/
	MyMessageListener	mMyMessageListener	= new MyMessageListener();

	class MyMessageListener implements MessageListener {

		@Override
		public void processMessage(Chat chat, Message message) {
			String body = message.getBody();
//			ToastUtils.showToastSafe(getApplicationContext(), body);

			// 得到会话的参与者
			String participant = chat.getParticipant();
			System.out.println("会话的参与者:" + participant);

			// 保存消息到数据库中
			saveMessage(message, participant);// 发送消息的时候保存消息
		}
	}

	/*--------------- 监听会话的创建 ---------------*/
	MyChatListener	mMyChatListener	= new MyChatListener();

	class MyChatListener implements ChatManagerListener {
		@Override
		public void chatCreated(Chat chat, boolean createdLocally) {
			String participant = chat.getParticipant();
			participant = filterAccount(participant);
			// mChatMap里面是否存在
			if (mChatMap.containsKey(participant)) {
				//设置监听
				chat.addMessageListener(mMyMessageListener);
			} else {
				//设置监听
				chat.addMessageListener(mMyMessageListener);
				mChatMap.put(participant, chat);
			}

			chat.addMessageListener(mMyMessageListener);

			if (createdLocally) {
				System.out.println("--------------我创建了一个会话--------------");
			} else {
				System.out.println("--------------别人创建了一个会话--------------");
			}
		}
	}

	/**
	 * 发送消息
	 *
	 * @param msg
	 */
	public void sendMessage(Message msg) {
		try {
			// 3.聊天对象
			// chatManager.createChat(消息接收对象的唯一表示(jid),消息监听者)

			// 判断mChatMap是否存在
			String toAcccount = msg.getTo();

			toAcccount = filterAccount(toAcccount);

			if (mChatMap.containsKey(toAcccount)) {
				mChat = mChatMap.get(toAcccount);
			} else {
				// 创建一个chat对象
				// mMyMessageListener只能监听有自己创建的chat
				mChat = mChatManager.createChat(msg.getTo(), mMyMessageListener);
				// 保存chat对象
				mChatMap.put(msg.getTo(), mChat);
			}
			// 5.发送消息
			mChat.sendMessage(msg);
			// 消息发送成功
			// ToastUtils.showToastSafe(ChatActivity.this, "消息发送成功");

			// 保存消息到数据库中
			saveMessage(msg, msg.getTo());// 发送消息的时候保存消息
		} catch (XMPPException e) {
			e.printStackTrace();
			// 消息发送失败
//			ToastUtils.showToastSafe(getApplicationContext(), "消息发送失败");
			Toast.makeText(this, "消息发送失败", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 发送消息-->通过cotentResolver保存消息
	 * 接收消息-->通过cotentResolver保存消息
	 */
	public void saveMessage(Message msg, String sessionAccout) {
		ContentValues values = new ContentValues();
		values.put(SmsOpenHelper.SMSTABLE.FROM_ACCOUNT, filterAccount(msg.getFrom()));
		values.put(SmsOpenHelper.SMSTABLE.TO_ACCOUNT, filterAccount(msg.getTo()));
		values.put(SmsOpenHelper.SMSTABLE.BODY, msg.getBody());
		values.put(SmsOpenHelper.SMSTABLE.STATUS, "offlie");
		values.put(SmsOpenHelper.SMSTABLE.TYPE, msg.getType().name());
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
		values.put(SmsOpenHelper.SMSTABLE.TIME, time);
		values.put(SmsOpenHelper.SMSTABLE.SESSION_ACCOUNT, filterAccount(sessionAccout));// 细节
		// billy@itheima.com--->xiaoli@itheima.com
		getContentResolver().insert(SmsProvider.SMS_URI, values);
	}

	/**
	 * 统一过滤一下account,避免hm1@itheima.com/Spark 2.6.3带有resource的jid
	 *
	 * @param account
	 * @return
	 */
	public String filterAccount(String account) {
		return account.substring(0, account.indexOf("@")) + "@" + LoginActivity.SERVER_NAME;
	}

	private void printCollection(Collection<String> collection) {
		for (String info : collection) {
			System.out.print(info + "  ");
		}
		System.out.println("");
	}

	public class MyBinder extends Binder {
		/**
		 * 获取service的实例方法
		 */
		public IMService getImService() {
			return IMService.this;
		}
	}
}
