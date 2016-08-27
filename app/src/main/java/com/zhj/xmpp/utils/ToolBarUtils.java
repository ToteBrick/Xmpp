package com.zhj.xmpp.utils;

import android.view.View;
import android.widget.LinearLayout;

import com.zhj.xmpp.R;

/**
 * Created by hasee on 2016/8/28.
 */
public class ToolBarUtils {

    public void createToolBar(LinearLayout mainLlBottom) {
        //动态添加view
        View view = View.inflate(mainLlBottom.getContext(), R.layout.inflate_toolbar_btn, null);
        mainLlBottom.addView(view);
    }
}
