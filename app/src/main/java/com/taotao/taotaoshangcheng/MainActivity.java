package com.taotao.taotaoshangcheng;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.taotao.taotaoshangcheng.bean.Tab;
import com.taotao.taotaoshangcheng.fragment.CartFragment;
import com.taotao.taotaoshangcheng.fragment.CategoryFragment;
import com.taotao.taotaoshangcheng.fragment.HomeFragment;
import com.taotao.taotaoshangcheng.fragment.HotFragment;
import com.taotao.taotaoshangcheng.fragment.MineFragment;
import com.taotao.taotaoshangcheng.weight.CnToolBar;
import com.taotao.taotaoshangcheng.weight.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar1)
    CnToolBar mToolbar;
    private LayoutInflater mLayoutInflater;
    private CartFragment mCartFragment;

    @BindView(R.id.realtabcontent)
    FrameLayout mRealtabcontent;
    @BindView(android.R.id.tabcontent)
    FrameLayout mTabcontent;
    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabhost;

    private List<Tab> mTabs = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


       // initToolBar();
        initTab();
    }

    private void initToolBar() {

        mToolbar = findViewById(R.id.toolbar);
    }

    private void initTab() {
        //引入xml，在里面配置选中和未选中时的状态
        Tab tab_home = new Tab(R.string.home, R.drawable.selector_icon_home, HomeFragment.class);
        Tab tab_hot = new Tab(R.string.hot, R.drawable.selector_icon_hot, HotFragment.class);
        Tab tab_category = new Tab(R.string.catagory, R.drawable.selector_icon_category, CategoryFragment.class);
        Tab tab_cart = new Tab(R.string.cart, R.drawable.selector_icon_cart, CartFragment.class);
        Tab tab_mine = new Tab(R.string.mine, R.drawable.selector_icon_mine, MineFragment.class);

        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        mLayoutInflater = LayoutInflater.from(this);
        mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                //必须使用 equals 不能用 == 否则运行会出错
                if (tabId.equals(getString(R.string.cart))) {

                    refData();


                } else {

                    mToolbar.showSearchView();
                    mToolbar.hideTitleView();
                    mToolbar.getRightButton().setVisibility(View.GONE);

                }
            }
        });

        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle())).setIndicator(buildIndicator(tab));
            //一定要设置fragment
            mTabhost.addTab(tabSpec, tab.getFragment(), null);

        }

        //设置取消分隔栏，并设置默认选择第一个
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);
    }

    private void refData() {

        if (mCartFragment == null) {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));

            if (fragment != null) {

                mCartFragment = (CartFragment) fragment;

                mCartFragment.refData();
                mCartFragment.changeToolbar();
            }
        } else {
            mCartFragment.refData();
            mCartFragment.changeToolbar();
        }
    }

    private View buildIndicator(Tab tab) {
        View view = mLayoutInflater.inflate(R.layout.tab_indicator, null);

        TextView mTxtIndicator = view.findViewById(R.id.txt_indicator);
        ImageView mIconTab = view.findViewById(R.id.icon_tab);

        mIconTab.setBackgroundResource(tab.getIcon());
        mTxtIndicator.setText(tab.getTitle());

        return view;
    }
}
