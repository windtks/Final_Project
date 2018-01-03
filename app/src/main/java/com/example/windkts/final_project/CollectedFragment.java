package com.example.windkts.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.windkts.final_project.DataBase.DB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CollectedFragment extends Fragment {
    private View view=null;
    private View empty=null;

    private RecyclerView recyclerview;
    private  RvAdapter mAdapter ;
    private  myAdapter newAdapter;
    private DB historyOp = new DB(getContext());
    private List<History> history = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collected, container, false);
        empty = inflater.inflate(R.layout.emptyview, container, false);
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
        List<History> newData = historyOp.getAllLike();
        Collections.reverse(newData);
        history.clear();
        history.addAll(newData);
        Log.e("Frag","history: "+ String.valueOf(history.size()));
        if(newAdapter!=null){
            newAdapter.notifyDataSetChanged();
        }
    }
    private void setupRecyclerView(final RecyclerView recyclerView) {

        newAdapter = new myAdapter(getContext(),R.layout.collected_item,history);

        newAdapter.setOnItemChildClickListener(new BaseItemDraggableAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(history.get(position).getIs_liked()==0){
                    view.setBackground(getResources().getDrawable(R.drawable.ic_star_yellow_24dp));
                    history.get(position).setIs_liked(1);
                    historyOp.setisLiked(history.get(position).getSource(),1);
                }
                else {
                    view.setBackground(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                    history.get(position).setIs_liked(0);
                    historyOp.setisLiked(history.get(position).getSource(),0);
                }
            }
        });
        newAdapter.setOnItemClickListener(new BaseItemDraggableAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), TranslateActivity.class);
                History h = history.get(position);
                intent.putExtra("query",h.getSource());
                intent.putExtra("source",h.getLan_from());
                intent.putExtra("target",h.getLan_to());
                getContext().startActivity(intent);
            }
        });
        newAdapter.setEmptyView(empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(newAdapter);
    }

}
