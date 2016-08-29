package com.zhj.xmpp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.zhj.xmpp.DB.SmsOpenHelper;


/*
 * @创建者     Administrator
 * @修改时间   2016/8/29
 * @描述	      聊天记录的provider
 *
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class SmsProvider extends ContentProvider {
	// 定义了一个主机常量
	public static final String	AUTHORITIES		= SmsProvider.class.getCanonicalName();

	// 定义一个uri的常量,方便外界访问
	public static Uri			SMS_URI			= Uri.parse("content://" + AUTHORITIES + "/sms");
	public static Uri			SMS_SESSION_URI	= Uri.parse("content://" + AUTHORITIES + "/session");

	// 创建地址匹配器
	static UriMatcher			mUriMatcher;
	private static final int	SMS				= 1;
	private static final int	SESSION			= 2;

	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// 添加匹配规则
		mUriMatcher.addURI(AUTHORITIES, "/sms", SMS);
		mUriMatcher.addURI(AUTHORITIES, "/session", SESSION);
	}

	private SmsOpenHelper		mHelper;

	@Override
	public boolean onCreate() {
		mHelper = new SmsOpenHelper(getContext());
		if (mHelper != null) {
			return true;
		}
		return false;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	/*--------------- crud begin ---------------*/

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long newId = 0;
		switch (mUriMatcher.match(uri)) {
		case SMS:
			// 操作数据库表
			SQLiteDatabase db = mHelper.getWritableDatabase();
			newId = db.insert(SmsOpenHelper.T_SMS, "", values);
			if (newId != -1) {
				System.out.println("--------------SmsProvider insert Success--------------");
				// 拼接需要返回的uri
				uri = ContentUris.withAppendedId(uri, newId);
				// 如果数据改变,通知所有的观察者
				getContext().getContentResolver().notifyChange(SMS_URI, null);
			}
			break;

		default:
			break;
		}
		return uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor c = null;
		SQLiteDatabase db = null;
		switch (mUriMatcher.match(uri)) {
		case SMS:
			db = mHelper.getReadableDatabase();
			c = db.query(SmsOpenHelper.T_SMS, projection, selection, selectionArgs, null, null, sortOrder);
			if (c.getCount() > 0) {
				System.out.println("--------------SmsProvider query Success--------------");
			}
			break;

		case SESSION:
			/**
			 //假如,当前登录的是admin
			 需要查询和admin相关的会话信息


			 //admin发起的会话
			 select * from t_sms where from_account="admin@itheima.com";

			 //admin参与的会话 hm1 hm2
			 select * from t_sms where (from_account="admin@itheima.com" or to_account="admin@itheima.com") and session_account!="admin@itheima.com" ORDER BY time asc;

			 //只显示hm1 和 hm2最后一条数据
			 SELECT * FROM (select * from t_sms where (from_account="admin@itheima.com" or to_account="admin@itheima.com") and session_account!="admin@itheima.com" ORDER BY time asc )  GROUP BY session_account ;

			 */
			db = mHelper.getReadableDatabase();
			// 查询会话记录
			c =
					db.rawQuery(
							"SELECT * FROM "//
									+ "(select * from t_sms where (from_account=? or to_account=?) and session_account!=? ORDER BY time asc )"//
									+ "  GROUP BY " + "session_account ;", selectionArgs);//
			if (c.getCount() > 0) {
				System.out.println("--------------SmsProvider query Success--------------");
			}
			break;

		default:
			break;
		}
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int updateCount = 0;
		switch (mUriMatcher.match(uri)) {
		case SMS:
			SQLiteDatabase db = mHelper.getWritableDatabase();
			updateCount = db.update(SmsOpenHelper.T_SMS, values, selection, selectionArgs);
			if (updateCount > 0) {
				System.out.println("--------------SmsProvider update Success--------------");
				// 如果数据改变,通知所有的观察者
				getContext().getContentResolver().notifyChange(SMS_URI, null);
			}
			break;

		default:
			break;
		}
		return updateCount;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int deleteCount = 0;
		switch (mUriMatcher.match(uri)) {
		case SMS:
			SQLiteDatabase db = mHelper.getWritableDatabase();
			deleteCount = db.delete(SmsOpenHelper.T_SMS, selection, selectionArgs);
			if (deleteCount > 0) {
				System.out.println("--------------SmsProvider delete Success--------------");
				// 如果数据改变,通知所有的观察者
				getContext().getContentResolver().notifyChange(SMS_URI, null);
			}
			break;

		default:
			break;
		}
		return deleteCount;
	}

	/*--------------- crud end ---------------*/

}
