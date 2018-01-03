package com.example.windkts.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by windtksLin on 2018/1/1 0001.
 */

public class HomeFragment extends Fragment {
    private EditText input;
    private View view=null;

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
                if(event != null && event.getKeyCode()== KeyEvent.KEYCODE_ENTER){
                    if(event.getAction() == KeyEvent.ACTION_DOWN){
                        Intent intent = new Intent(getContext(),TranslateActivity.class);
                        intent.putExtra("query",input.getText().toString());
                        //测试用
                        intent.putExtra("source","zh-CHS");
                        intent.putExtra("target","EN");
                        getContext().startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });

        return view;
    }


}
