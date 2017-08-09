package com.sevenpick.framework;

import android.content.Context;
import android.support.annotation.LayoutRes;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */

public class BaseAdapter<T> extends SuperAdapter<T> {

    private Context context;

    public BaseAdapter(Context context, List<T> items, @LayoutRes int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
    }

    @Override
    public void onBind(SuperViewHolder superViewHolder, int i, int i1, T t) {

    }
}
