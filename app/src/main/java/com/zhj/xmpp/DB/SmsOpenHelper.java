package com.zhj.xmpp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/*
 * @创建者     Administrator
 * @创建时间   2015/8/31 15:49
 * @描述	      ${TODO}
 *
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class SmsOpenHelper extends SQLiteOpenHelper {
    public SmsOpenHelper(Context context) {
        super(context, "sms.db", null, 1);
    }

    public static final String T_SMS = "t_sms";

    /**
     from_account
     to_account
     body
     status
     type
     time
     session_account
     */
    /**
     * 把所有的表字段定义成常量
     */
    public class SMSTABLE implements BaseColumns {
        public static final String FROM_ACCOUNT = "from_account";//消息从哪里来
        public static final String TO_ACCOUNT = "to_account";//消息到哪里去
        public static final String BODY = "body";//消息内容
        public static final String STATUS = "status";//消息状态
        public static final String TYPE = "type";//消息的类型
        public static final String TIME = "time";//消息创建的时间
        public static final String SESSION_ACCOUNT = "session_account";//消息的会话者(非当前登录用户)

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " +
                T_SMS +
                "(" +
                SMSTABLE._ID + " integer primary key autoincrement," +
                SMSTABLE.FROM_ACCOUNT + " text," +
                SMSTABLE.TO_ACCOUNT + " text," +
                SMSTABLE.BODY + " text," +
                SMSTABLE.STATUS + " text," +
                SMSTABLE.TYPE + " text," +
                SMSTABLE.TIME + " text," +
                SMSTABLE.SESSION_ACCOUNT + " text" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
