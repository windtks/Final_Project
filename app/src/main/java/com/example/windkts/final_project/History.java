package com.example.windkts.final_project;

/**
 * Created by dell on 2018/1/3.
 */

public class History {
    private String source;
    private String result;
    private int is_liked;

    History(){
        this.source = "";
        this.result = "";
        this.is_liked = 0;
    }

    public void setSource(String source){
        this.source = source;
    }
    public void setResult(String result){
        this.result = result;
    }
    public void setIs_liked(int is_liked){
        this.is_liked = is_liked;
    }

    public String getSource(){
        return this.source;
    }
    public String getResult(){
        return this.result;
    }
    public int getIs_liked(){
        return  this.is_liked;
    }
}
