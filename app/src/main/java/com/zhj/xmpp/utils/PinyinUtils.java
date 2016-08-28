package com.zhj.xmpp.utils;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

/**
 * Created by hasee on 2016/8/28.
 */
public class PinyinUtils {
    public static String getPinyin(String content){
        PinyinHelper helper = new PinyinHelper();
        String str = helper.convertToPinyinString("吴立滔滔", "", PinyinFormat.WITHOUT_TONE);
        return content;
    }
}
