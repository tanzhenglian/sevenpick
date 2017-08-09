package com.sevenpick.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sevenpick.R;
import com.sevenpick.framework.BaseFragment;


public class UserFragment extends BaseFragment {

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_user;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
