package com.sounder.sounder.resolver;
import android.app.ActionBar;

import com.sounder.sounder.model.Param;

import java.util.HashMap;

/**
 * Createed by Sounder on 2017/2/3
 */
public class TableManager {
    private static TableManager sInstance = null;

    private HashMap<String,Table> mTableHashMap;

    public static TableManager getInstance(){
        if(sInstance == null){
            sInstance = new TableManager();
            sInstance.mTableHashMap = new HashMap<>();
        }
        return sInstance;
    }
    public void put(Table table){
        mTableHashMap.put(table.getEntityClassName(),table);
    }
    public Table get(Class<?> clz){
        String key = clz.getSimpleName();
        if(mTableHashMap.containsKey(key)) {
            return mTableHashMap.get(key);
        }else{
            return null;
        }
    }
    public void showTables(){
        for (String key:mTableHashMap.keySet()){
            Table t = mTableHashMap.get(key);
            System.out.println("-----------------------------------------------------");
            System.out.println("TableName--->"+t.getTableName());
            for(Param p : t.getParams()){
                System.out.println(p.toString());
            }
        }
    }

    public HashMap<String, Table> getTableHashMap() {
        return mTableHashMap;
    }
}
