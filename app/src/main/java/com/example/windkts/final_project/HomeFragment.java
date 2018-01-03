package com.example.windkts.final_project;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.example.windkts.final_project.DataBase.DB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by windtksLin on 2018/1/1 0001.
 */

public class HomeFragment extends Fragment {
    private EditText input;
    private View view;
    private RecyclerView recyclerView;
    private  RvAdapter mAdapter ;
    private  myAdapter newAdapter;
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
                final Animation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(300);
                mswitch.startAnimation(rotateAnimation);
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
                    if(event.getAction()== KeyEvent.ACTION_UP && !input.getText().toString().equals("")){
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
        Collections.reverse(newData);
        history.clear();
        history.addAll(newData);
        Log.e("Frag","history: "+ String.valueOf(history.size()));
        if(newAdapter!=null){

            newAdapter.notifyDataSetChanged();
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
        newAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());
                builder.setTitle("删除记录？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface DialogInterface, int i) {
                                historyOp.delete(history.get(position).getSource());
                                history.remove(position);
                                newAdapter.notifyDataSetChanged();
                            }
                        });
                builder.create().show();
                return false;
            }
        });
        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }
            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

            }
            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                historyOp.delete(history.get(pos).getSource());
                history.remove(pos);
                Log.e("heros","when delete: "+String.valueOf(history.size()));
                newAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(ContextCompat.getColor(getContext(), R.color.g));
            }
        };

        ItemDragAndSwipeCallback mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(newAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        //mItemDragAndSwipeCallback.setDragMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN);
        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        newAdapter.enableSwipeItem();
        newAdapter.setOnItemSwipeListener(onItemSwipeListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(newAdapter);
    }


}
