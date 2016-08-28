package com.zhj.xmpp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhj.xmpp.R;
import com.zhj.xmpp.service.IService;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import java.util.Collection;

/**
 * Created by hasee on 2016/8/28.
 */
public class SessionFragment  extends Fragment {

    private ListView mListView;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
    }



    public SessionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
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

    }


    private void initView(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.session_listview);
    }

    private void initData() {
//        mListView.setAdapter(new SessionAdapter());
        Roster roster = IService.conn.getRoster();//得到花名册，就是所有联系人。
        Collection<RosterEntry> entries = roster.getEntries();//得到对应联系人实体集合

        for (RosterEntry entry:
             entries) {
            System.out.println(entry.toString());
        }
    }

    private void initListner() {

    }
}
