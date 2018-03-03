package com.taotao.taotaoshangcheng.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.taotao.taotaoshangcheng.MainActivity;
import com.taotao.taotaoshangcheng.R;
import com.taotao.taotaoshangcheng.adapter.CartAdapter;
import com.taotao.taotaoshangcheng.adapter.DividerItemDecortion;
import com.taotao.taotaoshangcheng.bean.ShoppingCart;
import com.taotao.taotaoshangcheng.utils.CartProvider;
import com.taotao.taotaoshangcheng.weight.CnToolBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Ivan on 15/9/22.
 */
public class CartFragment extends Fragment implements View.OnClickListener {

    public static final int ACTION_EDIT = 1;
    public static final int ACTION_CAMPLATE = 2;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.checkbox_all)
    CheckBox mCheckboxAll;
    @BindView(R.id.txt_total)
    TextView mTxtTotal;
    @BindView(R.id.btn_order)
    Button mBtnOrder;
    @BindView(R.id.btn_del)
    Button mBtnDel;
    Unbinder unbinder;
    private CartAdapter mCartAdapter;
    private CartProvider mCartProvider;
    private CnToolBar mCnToolBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        unbinder = ButterKnife.bind(this, view);

        mCartProvider = new CartProvider(getContext());

        showData();

        return view;
    }

    private void showData() {
        List<ShoppingCart> carts = mCartProvider.getAll();

        mCartAdapter = new CartAdapter(getContext(), carts, mCheckboxAll, mTxtTotal);

        mRecyclerView.setAdapter(mCartAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecortion(getContext(), DividerItemDecortion.VERTICAL_LIST));
    }


    public void refData() {
        mCartAdapter.clear();
        List<ShoppingCart> all = mCartProvider.getAll();
        mCartAdapter.addData(all);
        mCartAdapter.showTotalPrice();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof MainActivity) {

            MainActivity activity = (MainActivity) context;

            mCnToolBar = activity.findViewById(R.id.toolbar1);


            changeToolbar();


        }

    }

    public void changeToolbar() {

        mCnToolBar.hideSearchView();
        mCnToolBar.showTitleView();
        mCnToolBar.setTitle(R.string.cart);
        mCnToolBar.getRightButton().setVisibility(View.VISIBLE);
        mCnToolBar.setRightButtonText("编辑");
        mCnToolBar.setRightButtonIcon(R.drawable.bg_btn_style_red);

        mCnToolBar.getRightButton().setOnClickListener(this);

        mCnToolBar.getRightButton().setTag(ACTION_EDIT);


    }


    private void showDelControl() {
        mCnToolBar.getRightButton().setText("完成");
        mTxtTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mCnToolBar.getRightButton().setTag(ACTION_CAMPLATE);

        mCartAdapter.checkAll_None(false);
        mCheckboxAll.setChecked(false);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        int action = (int) view.getTag();
        if (ACTION_EDIT == action) {

            showDelControl();
        } else if (ACTION_CAMPLATE == action) {

            hideDelControl();
        }
    }

    private void hideDelControl() {

        mTxtTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);


        mBtnDel.setVisibility(View.GONE);
        mCnToolBar.setRightButtonText("编辑");
        mCnToolBar.getRightButton().setTag(ACTION_EDIT);

        mCartAdapter.checkAll_None(true);
        mCartAdapter.showTotalPrice();

        mCheckboxAll.setChecked(true);
    }

    @OnClick(R.id.btn_del)
    public void onViewClicked() {
        mCartAdapter.delCart();
    }
}
