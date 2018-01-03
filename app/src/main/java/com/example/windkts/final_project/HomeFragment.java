package com.example.windkts.final_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.service.autofill.FillEventHistory;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by windtksLin on 2018/1/1 0001.
 */

public class HomeFragment extends Fragment {
    private EditText input;
    private View view=null;
    private RecyclerView recyclerView;
    private  RvAdapter mAdapter ;
    private HistoryOp historyOp;
    private List<History> history = new ArrayList<>();

    private Button msource;
    private Button mtarget;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        input = (EditText) view.findViewById(R.id.editText);

        msource = (Button)view.findViewById(R.id.source_lan);
        mtarget = (Button)view.findViewById(R.id.target_lan);

        msource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),SelectActivity.class);
//                intent.putExtra("source",msource.getText().toString());
                startActivity(intent);
            }
        });
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event!=null && event.getKeyCode()==KeyEvent.KEYCODE_ENTER){
                    if(event.getAction()== KeyEvent.ACTION_UP){
                        Intent intent = new Intent(getContext(),TranslateActivity.class);
                        intent.putExtra("query",input.getText().toString());
                        //测试用
                        intent.putExtra("source","zh-CHS");
                        intent.putExtra("target","EN");
                        getContext().startActivity(intent);
                        return true;
                    }
                    return true;
                }
                return false;
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        setupRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        HistoryOp = new HistoryOp(getActivity());
        List<History> newData = historyOp.getAllData();
        history.clear();
        history.addAll(newData);

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
                source.setText(h.getSource());
                result.setText(h.getResult());
                //to do ..
            }

        };

        mAdapter.setOnItemClickListener (new RvAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                //to do ..
                History h = history.get(position);
                intent.putExtra("history",h);
                getContext().startActivity(intent);

            }
            @Override
            public void onLongClick(final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());
                builder.setTitle("删除人物？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface DialogInterface, int i) {
                                historyOp.deleteDataByName(history.get(position).getSource());
                                history.remove(position);
                                Log.e("heros","when delete: "+String.valueOf(history.size()));
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                builder.create().show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(mAdapter);
    }


}
