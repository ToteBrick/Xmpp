package com.zhj.xmpp;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.zhj.xmpp.DB.ContactOpenHelper;
import com.zhj.xmpp.provider.ContactsProvider;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

/**
 * Created by hasee on 2016/8/28.
 */
public class TestContactProvider extends AndroidTestCase {
    public void testInsert() {
        /**
         *    public static final String ACOUNT = "acount";
         public static final String NICKNAME = "nickname";
         public static final String AVATOR = "avator";
         public static final String PINYIN = "pinyin";
         */
        ContentValues values = new ContentValues();
<<<<<<< Updated upstream
        values.put(ContactOpenHelper.CONTACTTABLE.ACOUNT, "user1@zhj.com");
        values.put(ContactOpenHelper.CONTACTTABLE.NICKNAME, "user1");
        values.put(ContactOpenHelper.CONTACTTABLE.AVATOR, 0);
        values.put(ContactOpenHelper.CONTACTTABLE.PINYIN, "user1");
        Uri insert = getContext().getContentResolver().insert(ContactProvider.CONTACT_URI, values);
=======
        values.put(ContactOpenHelper.CONTACTTABLE.ACCOUNT, "伍碧林@zhj.com");
        values.put(ContactOpenHelper.CONTACTTABLE.NICKNAME, "伍碧林");
        values.put(ContactOpenHelper.CONTACTTABLE.AVATOR, 0);
        values.put(ContactOpenHelper.CONTACTTABLE.PINYIN, "wubilin");
        Uri insert = getContext().getContentResolver().insert(ContactsProvider.CONTACT_URI, values);
>>>>>>> Stashed changes
    }

    public void testDelete() {
        getContext().getContentResolver().delete(ContactsProvider.CONTACT_URI,
                ContactOpenHelper.CONTACTTABLE.ACCOUNT + "=?", new String[]{"吴碧丽@zhj.com"});
    }

    public void testUpdate() {
        ContentValues values = new ContentValues();
        values.put(ContactOpenHelper.CONTACTTABLE.ACCOUNT, "伍碧林@zhj.com");
        values.put(ContactOpenHelper.CONTACTTABLE.NICKNAME, "我是老伍@zhj.com");
        // content://android.content.ContentProvider/contact
        getContext().getContentResolver().update(ContactsProvider.CONTACT_URI, values,
                ContactOpenHelper.CONTACTTABLE.ACCOUNT + "=?", new String[]{"伍碧林@zhj.com"});
    }

    public void testQuery() {
        Cursor cursor = getContext().getContentResolver().query(ContactsProvider.CONTACT_URI, null, null, null, null);
        int columnCount = cursor.getColumnCount(); //列的总数
        while (cursor.moveToNext()) {
            for (int i = 0; i < columnCount; i++) {
                System.out.print(cursor.getString(i) + " ");
            }
            System.out.println("");
        }
    }

    public void testPinyin() {
        PinyinHelper helper = new PinyinHelper();
        String str = helper.convertToPinyinString("吴立滔滔", "", PinyinFormat.WITHOUT_TONE);
        Log.d("TestContactProvider", "str :" + str);
        System.out.print(str);
    }
}
