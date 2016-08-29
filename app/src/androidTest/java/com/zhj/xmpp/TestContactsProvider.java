package com.zhj.xmpp;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.zhj.xmpp.DB.ContactOpenHelper;
import com.zhj.xmpp.provider.ContactsProvider;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

/*
 * @创建者     Administrator
 * @创建时间
 * @描述	      ${TODO}
 *
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class TestContactsProvider extends AndroidTestCase {
	public void testInsert() {
		ContentValues values = new ContentValues();
		/**
		 public static final String ACCOUNT = "account";
		 public static final String NICKNAME = "nickname";
		 public static final String AVATAR = "avatar";
		 public static final String PINYIN = "pinyin";
		 */
		values.put(ContactOpenHelper.CONTACTTABLE.ACCOUNT, "伍碧林@zhj.com");
		values.put(ContactOpenHelper.CONTACTTABLE.NICKNAME, "老伍@zhj.com");
		values.put(ContactOpenHelper.CONTACTTABLE.AVATOR, "0");
		values.put(ContactOpenHelper.CONTACTTABLE.PINYIN, "wubilin");
		// content://android.content.ContentProvider/contact
		getContext().getContentResolver().insert(ContactsProvider.CONTACT_URI, values);
	}

	public void testDelete() {
		getContext().getContentResolver().delete(ContactsProvider.CONTACT_URI,
				ContactOpenHelper.CONTACTTABLE.ACCOUNT + "=?", new String[] { "伍碧林@zhj.com" });
	}

	public void testUpdate() {
		ContentValues values = new ContentValues();
		values.put(ContactOpenHelper.CONTACTTABLE.ACCOUNT, "伍碧林@zhj.com");
		values.put(ContactOpenHelper.CONTACTTABLE.NICKNAME, "我是老伍@zhj.com");
		// content://android.content.ContentProvider/contact
		getContext().getContentResolver().update(ContactsProvider.CONTACT_URI, values,
				ContactOpenHelper.CONTACTTABLE.ACCOUNT + "=?", new String[] { "伍碧林@zhj.com" });

	}

	public void testQuery() {
		Cursor c = getContext().getContentResolver().query(ContactsProvider.CONTACT_URI, null, null, null, null);
		int columnCount = c.getColumnCount();
		while (c.moveToNext()) {
			for (int i = 0; i < columnCount; i++) {// 循环打印每一列
				System.out.print(c.getString(i) + "  ");
			}
			System.out.println("");
		}
		/**
		 --------------ContactsProvider query success--------------
		 1  伍碧林@itheima.com  老五@itheima.com  0  wubilin
		 2  伍碧林@itheima.com  老五@itheima.com  0  wubilin
		 */

		/**
		 --------------ContactsProvider query success----------
		 1  伍碧林@itheima.com  我是老伍@itheima.com  0  wubilin
		 2  伍碧林@itheima.com  我是老伍@itheima.com  0  wubilin
		 */
	}

	public void testPinyin() {
		PinyinHelper helper = new PinyinHelper();
		String str = helper.convertToPinyinString("吴立滔滔", "", PinyinFormat.WITHOUT_TONE);
		System.out.println(str);
	}
}
