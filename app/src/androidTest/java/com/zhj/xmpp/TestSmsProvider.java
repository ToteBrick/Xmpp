package com.zhj.xmpp;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.zhj.xmpp.DB.SmsOpenHelper;
import com.zhj.xmpp.provider.SmsProvider;


/*
 * @创建者     Administrator
 * @创建时间   2015/9/1 11:00
 * @描述	      ${TODO}
 *
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class TestSmsProvider extends AndroidTestCase {
	public void testInsert() {
		/**
		 public static final String FROM_ACCOUNT = "from_account";//消息从哪里来
		 public static final String TO_ACCOUNT = "to_account";//消息到哪里去
		 public static final String BODY = "body";//消息内容
		 public static final String STATUS = "status";//消息状态
		 public static final String TYPE = "type";//消息的类型
		 public static final String TIME = "time";//消息创建的时间
		 public static final String SESSION_ACCOUNT = "session_account";//消息的会话者(非当前登录用户)

		 */
		ContentValues values = new ContentValues();
		values.put(SmsOpenHelper.SMSTABLE.FROM_ACCOUNT, "billy@itheima.com");
		values.put(SmsOpenHelper.SMSTABLE.TO_ACCOUNT, "xiaoli@itheima.com");
		values.put(SmsOpenHelper.SMSTABLE.BODY, "小丽,今晚约吗?");
		values.put(SmsOpenHelper.SMSTABLE.STATUS, "offline");
		values.put(SmsOpenHelper.SMSTABLE.TYPE, "chat");
		values.put(SmsOpenHelper.SMSTABLE.TIME, "2015年9月1日11:02:45");
		values.put(SmsOpenHelper.SMSTABLE.SESSION_ACCOUNT, "xiaoli@itheima.com");
		getContext().getContentResolver().insert(SmsProvider.SMS_URI, values);
	}

	public void testDelete() {
		getContext().getContentResolver().delete(SmsProvider.SMS_URI, SmsOpenHelper.SMSTABLE.FROM_ACCOUNT + "=?",
				new String[] { "billy@itheima.com" });
	}

	public void testUpdate() {
		ContentValues values = new ContentValues();
		values.put(SmsOpenHelper.SMSTABLE.FROM_ACCOUNT, "billy@itheima.com");
		values.put(SmsOpenHelper.SMSTABLE.TO_ACCOUNT, "xiaoli@itheima.com");
		values.put(SmsOpenHelper.SMSTABLE.BODY, "小丽,今晚约吗?我想你很久了");
		values.put(SmsOpenHelper.SMSTABLE.STATUS, "offline");
		values.put(SmsOpenHelper.SMSTABLE.TYPE, "chat");
		values.put(SmsOpenHelper.SMSTABLE.TIME, "2015年9月1日11:07:01");
		values.put(SmsOpenHelper.SMSTABLE.SESSION_ACCOUNT, "xiaoli@itheima.com");
		getContext().getContentResolver().update(SmsProvider.SMS_URI, values,
				SmsOpenHelper.SMSTABLE.FROM_ACCOUNT + "=?", new String[] { "billy@itheima.com" });
	}

	public void testQuery() {
		Cursor c =
				getContext().getContentResolver().query(SmsProvider.SMS_URI, null,
						SmsOpenHelper.SMSTABLE.FROM_ACCOUNT + "=?", new String[] { "admin@itheima.com" }, null);

		int columnCount = c.getColumnCount();
		// 遍历输出所有的列
		while (c.moveToNext()) {
			for (int i = 0; i < columnCount; i++) {
				System.out.print(c.getString(i) + "  ");
			}
			System.out.println("");
		}
		// 1 billy@itheima.com xiaoli@itheima.com 小丽,今晚约吗? offline chat 2015年9月1日11:02:45 xiaoli@itheima.com
	}
}
