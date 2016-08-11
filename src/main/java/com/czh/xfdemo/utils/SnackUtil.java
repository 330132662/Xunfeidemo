package com.czh.xfdemo.utils;

import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/*************************************************
 * @desc Snackbar工具类
 * @auther LiJianfei
 * @time 2016/8/2 10:00
 ************************************/

public class SnackUtil {

    /**
     * 生成一个功能强大的带有两侧监听的snackbar
     * 建议你先看一下具体的封装内容
     *
     * @param viewRight     需要设置监听的右侧View
     * @param leftStr       左侧提示语
     * @param leftListener  左侧监听  view.getId()=2131492975  请内部实现事件处理吧！
     * @param rightStr      右侧响应语
     * @param rightListener 右侧监听 view.getId()=2131492976  这里是不正确的，id值会因为控件改变而改变，不能传入监听对象
     * @return
     */
    public static boolean getSnackbar(final View viewRight, String leftStr, View.OnClickListener leftListener,
                                      String rightStr, View.OnClickListener rightListener) {
        Snackbar snackbar = Snackbar.make(viewRight, leftStr, Snackbar.LENGTH_LONG).setAction(rightStr, rightListener);
        Snackbar.SnackbarLayout snacklayout = (Snackbar.SnackbarLayout) snackbar.getView();//snackd布局
//                snack背景
//        snacklayout.setBackgroundResource(R.drawable.sel_snack);
//                右侧文本(监听可在外部设置)
//        TextView lefttv = (TextView) snacklayout.findViewById(R.id.snackbar_text);
//        TextView actiontv = (TextView) snacklayout.findViewById(R.id.snackbar_action);
//        actiontv.setTextColor(Color.parseColor("#FF8040"));//此处控制透明度
//        lefttv.setTextColor(Color.parseColor("#FF8040"));
//                左侧文本
//        lefttv.setOnClickListener(leftListener);
//        设置snackbar在界面中的布局
        ViewGroup.LayoutParams params = snacklayout.getLayoutParams();
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(params.width, params.height);
        ll.setMargins(100, 0, 100, 0);
        ll.gravity = Gravity.BOTTOM;
//        snacklayout.setLayoutParams(ll);
        snackbar.show();
        return true;// 临时测试用  实际应用可随意改
    }

}
