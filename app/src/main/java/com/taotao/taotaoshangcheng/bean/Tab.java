package com.taotao.taotaoshangcheng.bean;

/**
 * Created by Administrator on 2018/2/22 0022.
 */

public class Tab {
    //Title should be string, but we need add it to values string
    private int title;
    private int icon;
    private Class fragment;

    public Tab(int title, int icon, Class fragment) {
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
