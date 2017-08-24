package com.sevenpick.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.sevenpick.R;
import com.sevenpick.ui.base.BaseFragment;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;


public class UserFragment extends BaseFragment {

    @BindView(R.id.checkUpdate_btn)
    Button button;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_user;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Beta.checkUpgrade();
            }
        });
    }
}
