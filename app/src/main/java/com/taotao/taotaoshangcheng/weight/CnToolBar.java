package com.taotao.taotaoshangcheng.weight;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.taotao.taotaoshangcheng.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/22 0022.
 */

public class CnToolBar extends Toolbar {

    @BindView(R.id.toolbar_searchview)
    EditText mToolbarSearchview;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar_rightButton)
    Button mToolbarRightButton;
    private LayoutInflater mLayoutInflater;
    private View mView;

    public CnToolBar(Context context) {
        this(context, null, 0);
    }

    public CnToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public CnToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //必须在参数设置之前初始化
        initView();

        //设置搜索框边距
        setContentInsetsRelative(100,100);

        if (attrs != null) {
            @SuppressLint("RestrictedApi") final TintTypedArray array =
                    TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                            R.styleable.CnToolbar, defStyleAttr, 0);

            @SuppressLint("RestrictedApi") Drawable rightIcon =
                    array.getDrawable(R.styleable.CnToolbar_rightButtonIcon);
            if (rightIcon != null) {
                setRightButton(rightIcon);
            }

            @SuppressLint("RestrictedApi") boolean isShowSearchView = array.getBoolean(R.styleable.CnToolbar_isShowSearchView,false);

            if (isShowSearchView) {

                showSearchView();
                hideTitleView();
            }




            array.recycle();
        }



    }

    public void setRightButtonText(CharSequence text){
        mToolbarRightButton.setText(text);
        mToolbarRightButton.setVisibility(VISIBLE);
    }

    public void setRightButtonText(int id){
        setRightButtonText(getResources().getString(id));
    }

    public  void setRightButtonOnClickListener(OnClickListener li){

        mToolbarRightButton.setOnClickListener(li);
    }

    private void setRightButton(Drawable rightIcon) {
        if(mToolbarRightButton !=null){

            mToolbarRightButton.setBackground(rightIcon);
//            mToolbarRightButton.setImageDrawable(rightIcon);
            mToolbarRightButton.setVisibility(VISIBLE);
        }
    }

    public Button getRightButton(){

        return this.mToolbarRightButton;
    }


    private void initView() {



        if (mView == null) {
            mLayoutInflater = LayoutInflater.from(getContext());
            mView = mLayoutInflater.inflate(R.layout.toolbar, null);

//            mToolbarRightButton = mView.findViewById(R.id.toolbar_rightButton);
//            mToolbarTitle = mView.findViewById(R.id.toolbar_title);
//            mToolbarSearchview = mView.findViewById(R.id.toolbar_searchview);

            ButterKnife.bind(this, mView);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            addView(mView, lp);
        }

    }

    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
            showTitleView();
        }
    }

    public  void showSearchView(){

        if(mToolbarSearchview !=null)
            mToolbarSearchview.setVisibility(VISIBLE);

    }


    public void hideSearchView(){
        if(mToolbarSearchview !=null)
            mToolbarSearchview.setVisibility(GONE);
    }

    public void showTitleView(){
        if(mToolbarTitle !=null)
            mToolbarTitle.setVisibility(VISIBLE);
    }


    public void hideTitleView() {
        if (mToolbarTitle != null)
            mToolbarTitle.setVisibility(GONE);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void  setRightButtonIcon(Drawable icon){

        if(mToolbarRightButton !=null){

            mToolbarRightButton.setBackground(icon);
            mToolbarRightButton.setVisibility(VISIBLE);
        }

    }

    public void  setRightButtonIcon(int icon){

        setRightButtonIcon(getResources().getDrawable(icon));
    }


}
