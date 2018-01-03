package com.example.windkts.final_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windkts.final_project.DataBase.DB;

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
    private DB historyOp = new DB(getContext());
    private List<History> history = new ArrayList<>();

    private Button msource;
    private Button mtarget;
    private ImageButton mswitch;
    private Language language = new Language();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        input = (EditText) view.findViewById(R.id.editText);

        msource = (Button)view.findViewById(R.id.source_lan);
        mtarget = (Button)view.findViewById(R.id.target_lan);
        mswitch = (ImageButton) view.findViewById(R.id.imageButton);
        mswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = msource.getText().toString();
                msource.setText(mtarget.getText().toString());
                mtarget.setText(temp);
            }
        });
        msource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),SelectActivity.class);
                intent.putExtra("title","源语言");

//              intent.putExtra("source",msource.getText().toString());
                startActivityForResult(intent,1);
            }
        });
        mtarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SelectActivity.class);
                intent.putExtra("title","目标语言");

//              intent.putExtra("source",msource.getText().toString());
                startActivityForResult(intent,2);
            }
        });
        msource.setText("中文");
        mtarget.setText("英文");
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event!=null && event.getKeyCode()==KeyEvent.KEYCODE_ENTER){
                    if(event.getAction()== KeyEvent.ACTION_UP){
                        Intent intent = new Intent(getContext(),TranslateActivity.class);
                        intent.putExtra("query",input.getText().toString());
                        //测试用
                        intent.putExtra("source",language.getLan_code(msource.getText().toString()));
                        intent.putExtra("target",language.getLan_code(mtarget.getText().toString()));
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
        historyOp = new DB(getContext());
        List<History> newData = historyOp.getAllData();
        history.clear();
        history.addAll(newData);

        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == getActivity().RESULT_OK) {
                    if(data.getStringExtra("choice")!=null){
                        msource.setText(data.getStringExtra("choice"));

                    }
                }
                break;
            case 2:
                if (resultCode == getActivity().RESULT_OK) {
                    if(data.getStringExtra("choice")!=null){
                        mtarget.setText(data.getStringExtra("choice"));

                    }
                }
                break;
            default:
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
                if(historyOp.queryisliked(h.getSource())){
                    star.setBackground(getResources().getDrawable(R.drawable.ic_star_yellow_24dp));
                }
                else{
                    star.setBackground(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                }
            }

        };

        mAdapter.setOnItemClickListener (new RvAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getContext(), TranslateActivity.class);
                //to do ..
                History h = history.get(position);
                intent.putExtra("query",h.getSource());
                intent.putExtra("source",h.getLan_from());
                intent.putExtra("target",h.getLan_to());
                getContext().startActivity(intent);

            }
            @Override
            public void onLongClick(final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());
                builder.setTitle("删除记录？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface DialogInterface, int i) {
                                historyOp.delete(history.get(position).getSource());
                                history.remove(position);
                                if(!(historyOp.queryisliked(history.get(position).getSource()))){
                                    historyOp.delete(history.get(position).getSource());
                                }
                                Log.e("heros","when delete: "+String.valueOf(history.size()));
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
