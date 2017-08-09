package com.sevenpick;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sevenpick.fragment.EingFragment;
import com.sevenpick.fragment.HomeFragment;
import com.sevenpick.fragment.SectionFragment;
import com.sevenpick.fragment.ShopCarFragment;
import com.sevenpick.fragment.UserFragment;
import com.sevenpick.framework.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    private HomeFragment homeFragment;
    private SectionFragment sectionFragment;
    private EingFragment eingFragment;
    private ShopCarFragment shopCarFragment;
    private UserFragment userFragment;

    private Fragment oldFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBottom();
        initFragmentContent();
    }

    private void initBottom(){
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.nav_home:
                        toFragment(homeFragment);
                        break;
                    case R.id.nav_section:
                        if(sectionFragment==null){
                            sectionFragment=new SectionFragment();
                        }
                        toFragment(sectionFragment);
                        break;
                    case R.id.nav_eing:
                        if(eingFragment==null){
                            eingFragment=new EingFragment();
                        }
                        toFragment(eingFragment);
                        break;
                    case R.id.nav_shopcar:
                        if(shopCarFragment==null){
                            shopCarFragment=new ShopCarFragment();
                        }
                        toFragment(shopCarFragment);
                        break;
                    case R.id.nav_user:
                        if(userFragment==null){
                            userFragment=new UserFragment();
                        }
                        toFragment(userFragment);
                        break;
                }
            }
        });
    }


    /**
     * 设置home页
     */
    private void initFragmentContent() {
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, homeFragment).commit();
        oldFragment = homeFragment;
    }

    /**
     * 切换Fragment
     * @param to 下一个Fragment
     */
    private void toFragment(Fragment to) {
        if(to==oldFragment)
            return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(android
                .R.anim.fade_in, android.R.anim.fade_out);
        if (!to.isAdded()) { // 先判断是否被add过
            transaction.hide(oldFragment).add(R.id.fragment_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            transaction.hide(oldFragment).show(to).commit(); // 隐藏当前的fragment，显示下一个
        }
        oldFragment = to;
    }
}
