package com.example.windkts.final_project;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.windkts.final_project.DataBase.DB;

/**
 * Created by windtksLin on 2018/1/1 0001.
 */

public class SettingFragment extends Fragment {
    private Button btn_de;
    private View view;
    private DB DBOP = new DB(getContext());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        btn_de = view.findViewById(R.id.clear);
        btn_de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(btn_de.getContext());
                builder.setTitle("清除翻译历史记录？")
                        .setMessage("系统将此应用中清除所有翻译历史记录。")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface DialogInterface, int i) {
                                DB DBOP = new DB(getContext());
                                DBOP.rebuild();
                            }
                        });
                builder.create().show();


            }
        });
        return view;
    }
}
