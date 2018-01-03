package com.example.windkts.final_project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LED on 2018/1/2.
 */

public class ViewHolder extends RecyclerView.ViewHolder{
    private SparseArray<View> mViews;
    private View mConertView;
    private Context mContext;


    public ViewHolder(Context context, View itemView, ViewGroup parent){
        super(itemView);
        mContext = context;
        mConertView = itemView;
        mViews = new SparseArray<View>();
    }

    public static ViewHolder get(Context context, ViewGroup parent, int layoutId){
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(context, itemView, parent);
        return holder;
    }

    public  <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(view == null){
            view = mConertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }
}

