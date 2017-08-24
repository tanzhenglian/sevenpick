package com.sevenpick.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sevenpick.R;
import com.sevenpick.ui.adapter.AdvertImagePagerAdapter;
import com.sevenpick.ui.base.BaseFragment;
import com.sevenpick.ui.widget.AutoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class HomeFragment extends BaseFragment {

    @BindView(R.id.home_banner)
    AutoScrollViewPager autoScrollViewPager;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    private List<Integer> imageIdList;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setBanner();
    }

    private void setBanner(){
        imageIdList = new ArrayList<Integer>();
        imageIdList.add(R.drawable.icon_eing_color);
        imageIdList.add(R.drawable.icon_home_color);
        imageIdList.add(R.drawable.icon_section_color);
        imageIdList.add(R.drawable.icon_shopcar_color);


        autoScrollViewPager.setAdapter(new AdvertImagePagerAdapter(getContext(), imageIdList));

        autoScrollViewPager.setInterval(2000);
        autoScrollViewPager.startAutoScroll();
        autoScrollViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageIdList.size());
    }
}
