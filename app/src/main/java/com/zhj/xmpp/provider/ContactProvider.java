package com.zhj.xmpp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zhj.xmpp.DB.ContactOpenHelper;

/**
 * Created by hasee on 2016/8/28.
 */
public class ContactProvider extends ContentProvider {

    //定义主机名为一个常量
    private static  String AUTHORITIES = ContactProvider.class.getCanonicalName();//主机名

    static UriMatcher mUriMatcher ;

    public static final int CONTACT = 1;

    public static  Uri CONTACT_URI = Uri.parse("content://"+AUTHORITIES+"/contact");//定义一个uri,方便直接访问

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //添加匹配规则

        mUriMatcher.addURI(AUTHORITIES,"/contact",CONTACT);
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

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //匹配uri
        Cursor c = null;
        switch (mUriMatcher.match(uri)){
            case CONTACT:
                SQLiteDatabase db = mHelper.getReadableDatabase();
                 c = db.query(ContactOpenHelper.T_CONTACT, projection, selection, selectionArgs, null, null, sortOrder);
               if (c.getCount()>0){
                   Log.d("ContactProvider", "Query sucess");
               }
                break;
            default:
                break;
        }
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //匹配uri
        switch (mUriMatcher.match(uri)){
            case CONTACT:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                long id = db.insert(ContactOpenHelper.T_CONTACT, "", values);
                if (id != -1) {
                    Log.d("ContactProvider", "insert suceess");
                    ContentUris.withAppendedId(uri,id); //组装最新的uri
                }
            break;
            default:
                break;
        }

        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //匹配uri
        int delete = 0;
        switch (mUriMatcher.match(uri)){
            case CONTACT:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                delete = db.delete(ContactOpenHelper.T_CONTACT, selection, selectionArgs);
                if (delete>0){
                    Log.d("ContactProvider", "delete success");
                }
                break;
            default:
                break;
        }
        return delete;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //匹配uri
        int update = 0;
        switch (mUriMatcher.match(uri)){
            case CONTACT:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                 update = db.update(ContactOpenHelper.T_CONTACT, values, selection, selectionArgs);
                if (update>0){
                    Log.d("ContactProvider", "update sucess");
                }
                break;
            default:
                break;
        }
        return update;
    }
}
