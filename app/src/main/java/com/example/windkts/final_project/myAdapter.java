package com.example.windkts.final_project;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by windtksLin on 2018/1/3 0003.
 */

public class myAdapter extends BaseItemDraggableAdapter<History, BaseViewHolder> {
    private Context mContext;
    myAdapter(Context context, int layoutResId, List data) {
        super(layoutResId, data);
        mContext = context;
    }
    @Override
    protected void convert(BaseViewHolder helper, History item) {
        TextView source = helper.getView(R.id.source);
        TextView result = helper.getView(R.id.result);
        ImageView star = helper.getView(R.id.star);
        source.setText(item.getSource());
        result.setText(item.getResult());
        if(item.getIs_liked()==0){
            star.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
        }
        else{
            star.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_yellow_24dp));
        }
        helper.addOnClickListener(R.id.star);
    }


}
