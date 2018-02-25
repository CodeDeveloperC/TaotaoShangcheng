package com.taotao.taotaoshangcheng.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.taotao.taotaoshangcheng.R;
import com.taotao.taotaoshangcheng.bean.Campaign;
import com.taotao.taotaoshangcheng.bean.HomeCampaign;
import com.taotao.taotaoshangcheng.bean.HomeCategory;

import java.util.List;


/**
 * Created by Root on 15/9/30.
 */
public class HomeCatgoryAdapter extends RecyclerView.Adapter<HomeCatgoryAdapter.ViewHolder> {


    private static int VIEW_TYPE_L = 0;
    private static int VIEW_TYPE_R = 1;

    private LayoutInflater mInflater;


    private List<HomeCampaign> mDatas;
    private Context mContext;

    private onCampaignClickListener mOnCampaignClickListener;

    public void setOnCampaignClickListener(onCampaignClickListener onCampaignClickListener) {
        mOnCampaignClickListener = onCampaignClickListener;
    }

    public HomeCatgoryAdapter(List<HomeCampaign> datas, Context context) {
        mDatas = datas;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {


        mInflater = LayoutInflater.from(viewGroup.getContext());
        if (type == VIEW_TYPE_R) {

            //设置cardView 显示布局
            return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2, viewGroup, false));
        }

        return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {


        HomeCampaign category = mDatas.get(i);
        viewHolder.textTitle.setText(category.getTitle());
//        viewHolder.imageViewBig.setImageResource(category.getImgBig());
//        viewHolder.imageViewSmallTop.setImageResource(category.getImgSmallTop());
//        viewHolder.imageViewSmallBottom.setImageResource(category.getImgSmallBottom());
        //使用Picasso第三方jar包
        Picasso.with(mContext).load(category.getCpOne().getImgUrl()).into(viewHolder.imageViewBig);
        Picasso.with(mContext).load(category.getCpTwo().getImgUrl()).into(viewHolder.imageViewSmallTop);
        Picasso.with(mContext).load(category.getCpThree().getImgUrl()).into(viewHolder.imageViewSmallBottom);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    @Override
    public int getItemViewType(int position) {

        if (position % 2 == 0) {
            return VIEW_TYPE_R;
        } else return VIEW_TYPE_L;


    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);


            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);
            //添加监听事件
            imageViewSmallTop.setOnClickListener(this);
            imageViewBig.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (mOnCampaignClickListener != null) {
                HomeCampaign homeCampaign = mDatas.get(getLayoutPosition());
                if (mOnCampaignClickListener != null) {

                    switch (view.getId()) {

                        case R.id.imgview_big:
                            mOnCampaignClickListener.onClick(view, homeCampaign.getCpOne());
                            break;

                        case R.id.imgview_small_top:
                            mOnCampaignClickListener.onClick(view, homeCampaign.getCpTwo());
                            break;

                        case R.id.imgview_small_bottom:
                            mOnCampaignClickListener.onClick(view, homeCampaign.getCpThree());
                            break;
                    }
                }
            }
        }


    }

    //必须写public
   public interface onCampaignClickListener{
        void onClick(View view, Campaign campaign);
    }
}