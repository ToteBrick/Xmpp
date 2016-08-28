package com.zhj.xmpp.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zhj.xmpp.DB.ContactOpenHelper;
import com.zhj.xmpp.R;
import com.zhj.xmpp.activity.LoginActivity;
import com.zhj.xmpp.provider.ContactProvider;
import com.zhj.xmpp.service.IService;
import com.zhj.xmpp.utils.PinyinUtils;
import com.zhj.xmpp.utils.ThreadUtils;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import java.util.Collection;

/**
 * Created by hasee on 2016/8/28.
 */
public class ContactFragment extends Fragment {

    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
    }


    public ContactFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_session, container,
                false);
        initView(rootView);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initData();
        initListner();
        super.onActivityCreated(savedInstanceState);
    }

    private void init() {
        Roster roster = IService.conn.getRoster();//得到花名册，就是所有联系人。
        Collection<RosterEntry> entries = roster.getEntries();//得到对应联系人实体集合

        for (RosterEntry entry :
                entries) {

            /**
             * JID = [NODE"@"]domain["/"resource]
             * eg:user@user.com/res
             * domain:服务器域名
             * node:用户名
             */
            updateOrInsertEntry(entry);
//            System.out.print(entry.toString() + " ");
//            System.out.print(entry.getName() + " ");//nickname别名
//            System.out.print(entry.getUser() + " ");//jid->用户唯一标识
//            System.out.print(entry.getGroups() + " ");
//            System.out.print(entry.getType() + " ");
//            System.out.println("------------------");
        }
    }

    /**
     * 更新或者插入entry,优先更新.其次插入
     *
     * @param entry
     */
    private void updateOrInsertEntry(RosterEntry entry) {
        ContentValues values = new ContentValues();
        String account = entry.getUser();
        values.put(ContactOpenHelper.CONTACTTABLE.ACOUNT, account);
        account = account.substring(0, account.indexOf("@"));
        String nickname = entry.getName();
        if (nickname == null || "".equals(nickname)) {
            nickname = account.substring(0, account.indexOf("@")) + "@" + LoginActivity.SERVER_NAME;
        }
        values.put(ContactOpenHelper.CONTACTTABLE.NICKNAME, nickname);
        values.put(ContactOpenHelper.CONTACTTABLE.AVATOR, 0);
        values.put(ContactOpenHelper.CONTACTTABLE.PINYIN, PinyinUtils.getPinyin(nickname));

        //重点，首先考虑更新，如更新失败，再插入
        int update = getActivity().getContentResolver().update(ContactProvider.CONTACT_URI, values,
                ContactOpenHelper.CONTACTTABLE.ACOUNT + "=?", new String[]{account});
        if (update <= 0) {
            //没有更新,没有对应记录
            Uri insert = getActivity().getContentResolver().insert(ContactProvider.CONTACT_URI, values);

        }
    }


    private void initView(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.session_listview);
    }

    private void initData() {
        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {

                final Cursor cursor = getActivity().getContentResolver().query(ContactProvider.CONTACT_URI,
                        null, null, null, null);
               // 创建CursorAdapter(主线程),设置adapter
                ThreadUtils.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        CursorAdapter adapter = new CursorAdapter(getActivity(),cursor) {
                            //convertView为空时调用
                            @Override
                            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                                //决定跟视图
                                TextView textView = new TextView(getActivity());
                                return textView;
                            }
                            //展示具体数据
                            @Override
                            public void bindView(View view, Context context, Cursor cursor) {

                                //得到数据，展示数据
                                String account = cursor.getString(cursor.getColumnIndex(ContactOpenHelper.CONTACTTABLE.ACOUNT));
                                TextView textView = (TextView) view;
                                textView.setText(account);

                            }
                        }; 

                        //listview 涉足adapter
                        mListView.setAdapter(adapter);
                    }
                });

            }
        });

    }

    private void initListner() {

    }

}
