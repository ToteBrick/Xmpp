package com.zhj.xmpp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhj.xmpp.R;

/**
 * Created by hasee on 2016/8/28.
 */
public class ContactFragment extends android.support.v4.app.Fragment {
    public ContactFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container,
                false);
    }


}
