package com.taotao.taotaoshangcheng.adapter;

import android.content.Context;

import com.taotao.taotaoshangcheng.R;
import com.taotao.taotaoshangcheng.bean.Category;

import java.util.List;

/**
 * Created by Administrator on 2018/2/25 0025.
 */

public class CategoryAdapter extends SimpleAdapter<Category> {
    public CategoryAdapter(Context context,  List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Category item) {
        viewHoder.getTextView(R.id.textView).setText(item.getName());
    }
}
