package com.taotao.taotaoshangcheng.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Response;
import com.taotao.taotaoshangcheng.BuildConfig;
import com.taotao.taotaoshangcheng.Contants;
import com.taotao.taotaoshangcheng.R;
import com.taotao.taotaoshangcheng.adapter.DividerItemDecortion;
import com.taotao.taotaoshangcheng.adapter.HWAdatper;
import com.taotao.taotaoshangcheng.adapter.HotWaresAdapter;
import com.taotao.taotaoshangcheng.bean.Page;
import com.taotao.taotaoshangcheng.bean.Wares;
import com.taotao.taotaoshangcheng.http.OkHttpHelper;
import com.taotao.taotaoshangcheng.http.SpotsCallBack;
import com.taotao.taotaoshangcheng.utils.Pager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Ivan on 15/9/22.
 */
public class HotFragment extends Fragment {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.refresh_view)
    MaterialRefreshLayout mRefreshView;
    Unbinder unbinder;
    private OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstance();

    private int mCurrpage = 1;
    private int mPageSize = 10;
    private int totalPage = 1;
    private static final String TAG = "HotFragment";

    private List<Wares> datas;
//    private HotWaresAdapter mHotWaresAdapter;
    private HWAdatper mHotWaresAdapter;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        unbinder = ButterKnife.bind(this, view);


//        getData();
//        initRefreshLayout();

        Pager pager = Pager.newBuilder().setUrl(Contants.API.WARES_HOT).setLoadMore(true)
                .setRefreshLayout(mRefreshView)
                .setOnPageListener(new Pager.OnPageListener() {
            @Override
            public void load(List datas, int totalPage, int totalCount) {
                mHotWaresAdapter = new HWAdatper(getContext(),datas);

                mRecyclerview.setAdapter(mHotWaresAdapter);

                mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerview.setItemAnimator(new DefaultItemAnimator());
                mRecyclerview.addItemDecoration(new DividerItemDecortion(getContext(), DividerItemDecortion.VERTICAL_LIST));
            }

            @Override
            public void refresh(List datas, int totalPage, int totalCount) {
                //先清空数据，再加载数据
                if (BuildConfig.DEBUG) Log.d(TAG, "STATE_REFREH" + datas.size());
                //直接赋值地址是一样的，不能清空
                mHotWaresAdapter.clear();
                //data变成了另外的引用
                if (BuildConfig.DEBUG) Log.d(TAG, "STATE_REFREH" + datas.size());
                //getData();
                mHotWaresAdapter.addData(datas);
                //设置recyclerview显示第一条数据
                mRecyclerview.scrollToPosition(0);
            }

            @Override
            public void loadMore(List datas, int totalPage, int totalCount) {
                mHotWaresAdapter.addData(mHotWaresAdapter.getDatas().size(), datas);
                mRecyclerview.scrollToPosition(mHotWaresAdapter.getDatas().size());
            }
        }).setPageSize(20).build(getContext(), new TypeToken<Page<Wares>>() {
        }.getType());
        pager.request();

        return view;

    }

    private void initRefreshLayout() {
        mRefreshView.setLoadMore(true);
        mRefreshView.setMaterialRefreshListener(new MaterialRefreshListener() {
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
                    mRefreshView.finishRefreshLoadMore();
                }
            }
        });
    }

    //刷新
    private void refreshData() {
        mCurrpage = 1;
        state = STATE_REFREH;
        getData();
    }

    private void loadMoreData() {
        mCurrpage += 1;
        state = STATE_MORE;
        getData();
    }

    private void getData() {
        String url = Contants.API.WARES_HOT + "?curPage=" + mCurrpage + "&pageSize=" + mPageSize;
        mOkHttpHelper.get(url, new SpotsCallBack<Page<Wares>>(getContext()) {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {

                datas = waresPage.getList();
                mCurrpage = waresPage.getCurrentPage();
                //由于服务器问题，totalPage需要自己设定
                totalPage = waresPage.getTotalCount() / waresPage.getPageSize() + 1;

                showData();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showData() {
        switch (state) {
            case STATE_NORMAL:
                mHotWaresAdapter = new HWAdatper(getContext(),datas);

                mRecyclerview.setAdapter(mHotWaresAdapter);

                mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerview.setItemAnimator(new DefaultItemAnimator());
                mRecyclerview.addItemDecoration(new DividerItemDecortion(getContext(), DividerItemDecortion.VERTICAL_LIST));

                break;
            case STATE_REFREH:
                //先清空数据，再加载数据
                if (BuildConfig.DEBUG) Log.d(TAG, "STATE_REFREH" + datas.size());
                //直接赋值地址是一样的，不能清空
                mHotWaresAdapter.clear();
                //data变成了另外的引用
                if (BuildConfig.DEBUG) Log.d(TAG, "STATE_REFREH" + datas.size());
                //getData();
                mHotWaresAdapter.addData(datas);
                //设置recyclerview显示第一条数据
                mRecyclerview.scrollToPosition(0);
                //结束刷新图标
                mRefreshView.finishRefresh();
                break;

            case STATE_MORE:
                mHotWaresAdapter.addData(mHotWaresAdapter.getDatas().size(), datas);
                mRecyclerview.scrollToPosition(mHotWaresAdapter.getDatas().size());
                mRefreshView.finishRefreshLoadMore();
                break;
            default:
                break;
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (datas.size() == 0) {
            Log.d(TAG, "Destory" + 0);
        }
        if (BuildConfig.DEBUG) Log.d(TAG, "Destory");
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (datas.size() == 0) {
            Log.d(TAG, "onStop" + 0);
        }
        Log.d(TAG, "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (datas.size() == 0) {
            Log.d(TAG, "onPause" + 0);
        }
        Log.d(TAG, "onPause");
    }
}
