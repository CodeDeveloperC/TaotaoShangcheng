package com.taotao.taotaoshangcheng.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.taotao.taotaoshangcheng.BuildConfig;
import com.taotao.taotaoshangcheng.Contants;
import com.taotao.taotaoshangcheng.R;
import com.taotao.taotaoshangcheng.adapter.BaseAdapter;
import com.taotao.taotaoshangcheng.adapter.CategoryAdapter;
import com.taotao.taotaoshangcheng.adapter.DividerItemDecortion;
import com.taotao.taotaoshangcheng.adapter.HotWaresAdapter;
import com.taotao.taotaoshangcheng.adapter.WaresAdapter;
import com.taotao.taotaoshangcheng.bean.Banner;
import com.taotao.taotaoshangcheng.bean.Category;
import com.taotao.taotaoshangcheng.bean.HomeCampaign;
import com.taotao.taotaoshangcheng.bean.Page;
import com.taotao.taotaoshangcheng.bean.Wares;
import com.taotao.taotaoshangcheng.decoration.DividerGridItemDecoration;
import com.taotao.taotaoshangcheng.http.BaseCallback;
import com.taotao.taotaoshangcheng.http.OkHttpHelper;
import com.taotao.taotaoshangcheng.http.SpotsCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Ivan on 15/9/22.
 */
public class CategoryFragment extends Fragment {

    @BindView(R.id.recyclerview_category)
    RecyclerView mRecyclerviewCategory;
    @BindView(R.id.slider)
    SliderLayout mSlider;
    @BindView(R.id.recyclerview_wares)
    RecyclerView mRecyclerviewWares;
    @BindView(R.id.refresh_layout)
    MaterialRefreshLayout mRefreshLayout;
    Unbinder unbinder;

    private OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstance();
    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdapter;

    private int mCurrpage = 1;
    private int mPageSize = 10;
    private int totalPage = 1;
    private long categoryId = 0;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);
        unbinder = ButterKnife.bind(this, view);


        requestCategoryData();
        requestBannerDatas();
        initRefreshLayout();

        return view;
    }

    private void initRefreshLayout() {
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (mCurrpage < totalPage) {
                    loadMoreData();
                } else {
                    Toast.makeText(getContext(), "没有下一页数据了", Toast.LENGTH_SHORT).show();
                    //没有数据也要停止刷新
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    //刷新
    private void refreshData() {
        mCurrpage = 1;
        state = STATE_REFREH;
        requestWares(categoryId);
    }

    private void loadMoreData() {
        mCurrpage += 1;
        state = STATE_MORE;
        requestWares(categoryId);
    }

    private void requestCategoryData() {
        mOkHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);

                if (categories != null && categories.size() > 0) {
                    categoryId = categories.get(0).getId();

                }

                requestWares(categoryId);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showCategoryData(List<Category> categories) {
        mCategoryAdapter = new CategoryAdapter(getContext(), categories);
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Category category = mCategoryAdapter.getItem(position);
                state = STATE_NORMAL;
                mCurrpage = 1;
                categoryId = category.getId();
                requestWares(categoryId);
            }
        });

        mRecyclerviewCategory.setAdapter(mCategoryAdapter);
        mRecyclerviewCategory.setItemAnimator(new DefaultItemAnimator());
        mRecyclerviewCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerviewCategory.addItemDecoration(new DividerItemDecortion(getContext(), DividerItemDecortion.VERTICAL_LIST));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showSliderViews(List<Banner> banners) {

        if (banners != null) {
            for (final Banner banner : banners) {
                TextSliderView textSliderView = new TextSliderView(this.getContext());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView baseSliderView) {

                        Toast.makeText(CategoryFragment.this.getActivity(), banner.getName(), Toast.LENGTH_SHORT).show();

                    }
                });
                mSlider.addSlider(textSliderView);
            }
        }


        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setPresetTransformer(SliderLayout.Transformer.RotateDown);
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


    private void requestBannerDatas() {

        String url = Contants.API.BANNER + "?type=1";

        mOkHttpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                showSliderViews(banners);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });


    }

    private void requestWares(long categoryId) {

        String url = Contants.API.WARES_LIST + "?categoryId=" + categoryId + "&curPage=" + mCurrpage + "&pageSize=" + mPageSize;
        mOkHttpHelper.get(url, new BaseCallback<Page<Wares>>() {
            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {

                mCurrpage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalCount() / waresPage.getPageSize() + 1;
                showData(waresPage.getList());
            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showData(List<Wares> wares) {
        switch (state) {
            case STATE_NORMAL:


                //防止间隙越来越大
                if (mWaresAdapter != null) {
                    mWaresAdapter = new WaresAdapter(getContext(), wares);

                    mRecyclerviewWares.setAdapter(mWaresAdapter);
                } else {
                    mWaresAdapter = new WaresAdapter(getContext(), wares);

                    mRecyclerviewWares.setAdapter(mWaresAdapter);
                    mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                }


                break;
            case STATE_REFREH:
                //先清空数据，再加载数据
                //直接赋值地址是一样的，不能清空
                mWaresAdapter.clear();
                //data变成了另外的引用
                //getData();
                mWaresAdapter.addData(wares);
                //设置recyclerview显示第一条数据
                mRecyclerviewWares.scrollToPosition(0);
                //结束刷新图标
                mRefreshLayout.finishRefresh();
                break;

            case STATE_MORE:
                mWaresAdapter.addData(mWaresAdapter.getDatas().size(), wares);
                mRecyclerviewWares.scrollToPosition(mWaresAdapter.getDatas().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;
            default:
                break;
        }


    }
}



