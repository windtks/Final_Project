package com.example.windkts.final_project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/1/3.
 */

public class Language {
    private Map<String,String> Lan = new HashMap<>();
    private Map<String,String> Code = new HashMap<>();
    private int total = 8;
    private String language[] = {"中文","日文","英文","韩文","法文","俄文","葡萄牙文","西班牙文"};
    private String lan_code[] = {"zh-CHS","ja","EN","ko","fr","ru","pt","es"};
    Language(){
        for(int i=0;i<total;i++){
            Lan.put(language[i],lan_code[i]);
            Code.put(lan_code[i],language[i]);
        }
    }
    public String getLan_code(String name){
        if(Lan.get(name)==null){
            return "ERROR";
        }
        else return Lan.get(name);
    }
    public String getLan_name(String code){
        if(Code.get(code)==null){
            return "ERROR";
        }
        else return Code.get(code);
    }
    public List<String> getAllLanguage(){
        List<String> temp = new ArrayList<>();
        for (Map.Entry<String,String> entry : Lan.entrySet()) {
            temp.add(entry.getKey());
        }
        return temp;
    }

}
