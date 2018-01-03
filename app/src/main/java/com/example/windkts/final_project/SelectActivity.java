package com.example.windkts.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2018/1/3.
 */

public class SelectActivity extends AppCompatActivity {

    private RvAdapter mAdapter;
    private List<Language> languages = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_select);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter = new RvAdapter<Language>(SelectActivity.this,R.layout.language_item,languages){
            @Override
            public void convert(ViewHolder holder, Language language){
                TextView lan = holder.getView(R.id.lan);
            }
        };

        mAdapter.setOnItemClickListener(new RvAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(SelectActivity.this,MainActivity.class);
                String chosed = languages.get(position).getLan();
                intent.putExtra("choice",chosed);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongClick(int position) {
                Intent intent = new Intent(SelectActivity.this,MainActivity.class);
                String chosed = languages.get(position).getLan();
                intent.putExtra("choice",chosed);
                startActivity(intent);
                finish();
            }
        });
    }
}
