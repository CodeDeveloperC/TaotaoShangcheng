package com.taotao.taotaoshangcheng.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.taotao.taotaoshangcheng.BuildConfig;
import com.taotao.taotaoshangcheng.R;
import com.taotao.taotaoshangcheng.adapter.DividerItemDecortion;
import com.taotao.taotaoshangcheng.adapter.HomeCatgoryAdapter;
import com.taotao.taotaoshangcheng.bean.Banner;
import com.taotao.taotaoshangcheng.bean.HomeCategory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/2/22 0022.
 * 出现jar包没有引入错误
 */

public class HomeFragment extends Fragment {

    private final static String TAG = "HomeFragment";
    @BindView(R.id.slider)
    SliderLayout mSlider;
    Unbinder unbinder;
    @BindView(R.id.custom_indicator)
    PagerIndicator mCustomIndicator;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private HomeCatgoryAdapter mAdatper;

    private Gson mGson=new Gson();
    private List<Banner> mBanners;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        //initSlider();
        requestImages();

        initRecyclerView();
        return view;
    }

    private  void requestImages(){

        String url ="http://112.124.22.238:8081/course_api/banner/query?type=1";


        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormEncodingBuilder()
                .add("type","1")

                .build();


        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();

                    Type type = new TypeToken<List<Banner>>() {
                    }.getType();

                    mBanners = mGson.fromJson(json, type);
                    initSlider();

//                    Log.d(TAG, "json= " + json);
                }
            }
        });



    }


    private void initRecyclerView() {

        List<HomeCategory> datas = new ArrayList<>(15);

        HomeCategory category = new HomeCategory("热门活动",R.drawable.img_big_1,R.drawable.img_1_small1,R.drawable.img_1_small2);
        datas.add(category);

        category = new HomeCategory("有利可图",R.drawable.img_big_4,R.drawable.img_4_small1,R.drawable.img_4_small2);
        datas.add(category);
        category = new HomeCategory("品牌街",R.drawable.img_big_2,R.drawable.img_2_small1,R.drawable.img_2_small2);
        datas.add(category);

        category = new HomeCategory("金融街 包赚翻",R.drawable.img_big_1,R.drawable.img_3_small1,R.drawable.imag_3_small2);
        datas.add(category);

        category = new HomeCategory("超值购",R.drawable.img_big_0,R.drawable.img_0_small1,R.drawable.img_0_small2);
        datas.add(category);


        mAdatper = new HomeCatgoryAdapter(datas);

        mRecyclerView.setAdapter(mAdatper);

        //addItemDecoration方法添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecortion());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    }

    private void initSlider() {

        if (mBanners != null) {
            for (final Banner banner : mBanners) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView baseSliderView) {

                        Toast.makeText(HomeFragment.this.getActivity(), banner.getName(), Toast.LENGTH_LONG).show();

                    }
                });
                mSlider.addSlider(textSliderView);
            }
        }


        mSlider.setCustomIndicator(mCustomIndicator);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSlider.setDuration(3000);

        mSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {


//                Log.d(TAG, "onPageScrolled");

            }

            @Override
            public void onPageSelected(int i) {

//                Log.d(TAG, "onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int i) {

//                Log.d(TAG, "onPageScrollStateChanged");
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
