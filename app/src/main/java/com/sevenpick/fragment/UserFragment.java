package com.sevenpick.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.sevenpick.R;
import com.sevenpick.framework.BaseFragment;

import butterknife.BindView;
import io.reactivex.annotations.Beta;


public class UserFragment extends BaseFragment {

    @BindView(R.id.check_version)
    Button btn;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_user;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
