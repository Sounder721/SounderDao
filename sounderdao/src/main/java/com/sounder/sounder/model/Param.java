package com.sounder.sounder.model;

import com.sounder.sounder.annotation.FieldType;

/**
 * Table中每个字段属性
 * Created by Sounder on 2017/1/21
 */
public class Param {
    /**
     * 表属性字段
     */
    private String column;
    /**
     * class中定义字段
     */
    private String name;
    /**
     * 表属性字段的类型
     */
    private String type;
    private boolean autoincrement;
    private boolean primarykey;
    public Param(){}
    public Param(String name,String type , boolean autoincrement,boolean primarykey){
        this.name = name;
        this.type = type;
        this.autoincrement = autoincrement;
        this.primarykey = primarykey;
        if(autoincrement){
            this.type = "INTEGER";
            this.primarykey = true;
        }
    }
    public Param(String name,String type){
        this(name, type,false,false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAutoincrement() {
        return autoincrement;
    }

    public void setAutoincrement(boolean autoincrement) {
        this.autoincrement = autoincrement;
        if(autoincrement) {
            this.primarykey = true;
            this.type = FieldType.INTEGER.getType();
        }
    }

    public boolean isPrimarykey() {
        return primarykey;
    }

    public void setPrimarykey(boolean primarykey) {
        this.primarykey = primarykey;
    }


    public String toString(){
        return "name="+getName()+"\tcolumn="+column+"\ttype="+getType()+"\tautoincrement="+isAutoincrement()+"\tprimaryKey="+isPrimarykey();
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
