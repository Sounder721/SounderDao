package com.sounder.sounder.resolver;

import com.sounder.sounder.model.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * Createed by Sounder on 2017/2/3
 */
public class Table {
    private String mEntityClassName;
    private String mTableName;
    private ArrayList<Param> mParams;
    private String mCreateTableStatement;
    public Table(Class<?> eClass){
        mParams = new ArrayList<>();
        this.mEntityClassName = eClass.getSimpleName();
        Resolver resolver = new Resolver(this,eClass);
    }


    public void setTableName(String tableName) {
        mTableName = tableName;
    }

    public String getTableName() {
        return mTableName;
    }

    public ArrayList<Param> getParams() {
        return mParams;
    }
    public Param getParamByIndex(int index){
        return mParams.get(index);
    }

    public String getCreateTableStatement() {
        return mCreateTableStatement;
    }

    public void setCreateTableStatement(String createTableStatement) {
        mCreateTableStatement = createTableStatement;
    }

    public String getEntityClassName() {
        return mEntityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        mEntityClassName = entityClassName;
    }
}
