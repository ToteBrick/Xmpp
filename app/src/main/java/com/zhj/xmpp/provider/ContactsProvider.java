package com.zhj.xmpp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.zhj.xmpp.DB.ContactOpenHelper;


/*
 * @创建者     Administrator
<<<<<<< Updated upstream
 * @创建时间
=======
 * @创建时间
>>>>>>> Stashed changes
 * @描述	      ${TODO}
 *
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class ContactsProvider extends ContentProvider {

	// 定义主机名为一个常量
	private static String		AUTHORITIES		= ContactsProvider.class.getCanonicalName();

	static UriMatcher			mUriMatcher;

	private static final int	CONTACT			= 1;

	// 定义了一个uri,方便我们直接访问
	public static Uri			CONTACT_URI	= Uri.parse("content://" + AUTHORITIES + "/contact");

	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// 添加匹配规则
		mUriMatcher.addURI(AUTHORITIES, "/contact", CONTACT);
	}

	private ContactOpenHelper mHelper;

	@Override
	public boolean onCreate() {
		mHelper = new ContactOpenHelper(getContext());
		if (mHelper != null) {
			return true;
		}
		return false;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	/*--------------- crud begin---------------*/
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// 插入之前需要匹配uri
		switch (mUriMatcher.match(uri)) {
		case CONTACT:
			SQLiteDatabase db = mHelper.getWritableDatabase();
			// 新插入条目的id
			long id = db.insert(ContactOpenHelper.T_CONTACT, "", values);
			if (id != -1) {
				System.out.println("--------------ContactsProvider insert success--------------");

				// 组装最新的uri
				uri = ContentUris.withAppendedId(uri, id);

				// 通知观察者记录改变
				// getContext().getContentResolver().notifyChange(对应通知哪一个url对应的contentObserver,是否指定具体的contentObserver);
				getContext().getContentResolver().notifyChange(CONTACT_URI, null);
			}
			break;

		default:
			break;
		}
		return uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// 插入之前需要匹配uri
		Cursor c = null;
		switch (mUriMatcher.match(uri)) {
		case CONTACT:
			SQLiteDatabase db = mHelper.getReadableDatabase();
			c = db.query(ContactOpenHelper.T_CONTACT, projection, selection, selectionArgs, null, null, sortOrder);

			if (c.getCount() > 0) {
				System.out.println("--------------ContactsProvider query success--------------");
			}

			break;

		default:
			break;
		}
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// 插入之前需要匹配uri
		int updateCount = 0;
		switch (mUriMatcher.match(uri)) {
		case CONTACT:
			SQLiteDatabase db = mHelper.getWritableDatabase();
			// 更新条目的总数
			updateCount = db.update(ContactOpenHelper.T_CONTACT, values, selection, selectionArgs);
			if (updateCount > 0) {
				System.out.println("--------------ContactsProvider update success--------------");
				// 通知观察者记录改变
				// getContext().getContentResolver().notifyChange(对应通知哪一个url对应的contentObserver,是否指定具体的contentObserver);
				getContext().getContentResolver().notifyChange(CONTACT_URI, null);
			}
			break;

		default:
			break;
		}
		return updateCount;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// 插入之前需要匹配uri
		int deleteCount = 0;
		switch (mUriMatcher.match(uri)) {
		case CONTACT:
			SQLiteDatabase db = mHelper.getWritableDatabase();
			// 删除记录的条目总数
			deleteCount = db.delete(ContactOpenHelper.T_CONTACT, selection, selectionArgs);
			if (deleteCount > 0) {
				System.out.println("--------------ContactsProvider delete success--------------");
				// 通知观察者记录改变
				// getContext().getContentResolver().notifyChange(对应通知哪一个url对应的contentObserver,是否指定具体的contentObserver);
				getContext().getContentResolver().notifyChange(CONTACT_URI, null);
			}
			break;

		default:
			break;
		}
		return deleteCount;
	}

	/*--------------- crud  end---------------*/

}
