package com.zhj.xmpp.utils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhj.xmpp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2016/8/28.
 */
public class ToolBarUtils {

    //2,定义一个接口变量
    OnToolBarClickListner mOnToolBarClickListner;

    private List<TextView> mTextViews = new ArrayList<TextView>();

    public void createToolBar(LinearLayout mainLlBottom, String[] toolBarTitleArr, int[] toolBarIconArr) {

        for (int i = 0; i < toolBarTitleArr.length; i++) {
            //动态添加view
            TextView view = (TextView) View.inflate(mainLlBottom.getContext(), R.layout.inflate_toolbar_btn, null);
            //修改文字
            view.setText(toolBarTitleArr[i]);
            view.setCompoundDrawablesWithIntrinsicBounds(0, toolBarIconArr[i], 0, 0); //图标
            //修改位置
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            //设置layout_weight
            params.weight = 1;
            mainLlBottom.addView(view, params);
            //保存添加的textview
            mTextViews.add(view);
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //传递相关信息给mainactivity
                    //接口回调,不同模块传值
                    //3,拿着接口对象，调用具体方法
                    mOnToolBarClickListner.OnToolBarClick(finalI);
                }
            });

        }
    }

    public void changeColor(int position) {
        //还原
        for (TextView tv :
                mTextViews) {
            tv.setSelected(false);
        }
        //选中指定的item
        mTextViews.get(position).setSelected(true);
    }
    //1，定义接口和方法
    public interface OnToolBarClickListner{
        void OnToolBarClick(int position);

    }

    //4,暴露接口对象
    public void setOnToolBarClickListner (OnToolBarClickListner onToolBarClickListner){
        mOnToolBarClickListner = onToolBarClickListner;
    }



}
