package com.example.windkts.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dell on 2018/1/3.
 */

public class SelectActivity extends AppCompatActivity {

    private RvAdapter mAdapter;
    public Language lan = new Language();
    private List<String> languages = lan.getAllLanguage();;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_select);

        Intent intent = getIntent();
        this.setTitle(intent.getStringExtra("title"));
        recyclerView = findViewById(R.id.language);

        mAdapter = new RvAdapter<String>(this,R.layout.language_item,languages){
            @Override
            public void convert(ViewHolder holder, String language){
                TextView lan = holder.getView(R.id.lan);
                lan.setText(language);
            }
        };

        mAdapter.setOnItemClickListener(new RvAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                String choice = languages.get(position);
                intent.putExtra("choice",choice);
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onLongClick(int position) {

            }

            @Override
            public void onItemViewClick(View v, int p) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }
}
