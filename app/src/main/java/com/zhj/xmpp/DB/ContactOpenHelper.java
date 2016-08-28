package com.zhj.xmpp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by hasee on 2016/8/28.
 */
public class ContactOpenHelper extends SQLiteOpenHelper {
    public ContactOpenHelper(Context context) {
        super(context, "Contact.db", null, 1);
    }

    /**
     * 1,_id:主键
     * 2,account:帐号
     * 3.nickname:昵称
     * 4,avator:头像
     * 5,pinyin:帐号拼音 ,用来根据拼音排序
     */
    public static final String T_CONTACT = "t_contact";

    /**
     * 把所有表字段定义为常量
     */
    public class CONTACTTABLE implements BaseColumns { //里面有_id,_count字段
        public static final String ACOUNT = "acount";
        public static final String NICKNAME = "nickname";
        public static final String AVATOR = "avator";
        public static final String PINYIN = "pinyin";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table" +
                T_CONTACT +
                "(" +
                CONTACTTABLE._ID + "integer primary key autoincrement , " +
                CONTACTTABLE.ACOUNT + " text ," +
                CONTACTTABLE.NICKNAME + " text," +
                CONTACTTABLE.AVATOR + " text," +
                CONTACTTABLE.PINYIN + " text" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
