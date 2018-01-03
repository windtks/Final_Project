package com.example.windkts.final_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windkts.final_project.DataBase.DB;

import java.util.ArrayList;
import java.util.List;


public class CollectedFragment extends Fragment {
    private View view=null;
    private RecyclerView recyclerview;
    private  RvAdapter mAdapter ;
    private DB historyOp = new DB(getContext());
    private List<History> history = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collected, container, false);

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        setupRecyclerView(recyclerview);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.onHiddenChanged(isVisibleToUser);
        if (isVisibleToUser) {   // 在最前端显示 相当于调用了onResume();
            update();
        }
    }
    private void update(){
        historyOp = new DB(getContext());

        List<History> newData = historyOp.getAllData();
        history.clear();
        history.addAll(newData);
        Log.e("Frag","history: "+ String.valueOf(history.size()));
        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
    }
    private void setupRecyclerView(final RecyclerView recyclerView) {

        mAdapter = new RvAdapter <History>(getActivity(), R.layout.collected_item, history) {
            @Override
            public void convert(ViewHolder holder, History h) {
                TextView source = holder.getView(R.id.source);
                TextView result = holder.getView(R.id.result);
                ImageView star = holder.getView(R.id.star);
                source.setText(h.getSource());
                result.setText(h.getResult());
                star.setBackground(getResources().getDrawable(R.drawable.ic_star_yellow_24dp));
            }

        };
        mAdapter.setOnItemClickListener (new RvAdapter.OnItemClickListener() {
            @Override
            public void onClick(final int position) {
                Intent intent = new Intent(getContext(), TranslateActivity.class);
                History h = history.get(position);
                intent.putExtra("query",h.getSource());
                getContext().startActivity(intent);

            }
            @Override
            public void onLongClick(final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());
                builder.setTitle("取消收藏？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface DialogInterface, int i) {
                                History h = history.get(position);
                                h.setIs_liked(0);
                                historyOp.delete(h.getSource());
                                history.remove(position);
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                builder.create().show();
            }

            @Override
            public void onItemViewClick(View v, int p) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(mAdapter);
    }

}
