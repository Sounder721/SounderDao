package com.sounder.sounder.resolver;

import com.sounder.sounder.annotation.Constraint;
import com.sounder.sounder.dao.Dao;
import com.sounder.sounder.model.Param;
import com.sounder.sounder.util.ComparatorUtil;
import com.sounder.sounder.util.TextUtil;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 * Createed by Sounder on 2017/2/3
 */
public class Resolver {

    public Resolver(Table t,Class<?> clz){
        parse(t,clz);
    }
    private void parse(Table t,Class<?> clz){
        if(clz == null){
            return;
        }
        parseTableName(t,clz);
        parseTableParams(t,clz);
        //对属性字段做个排序，无关紧要
        sort(t);
        t.setCreateTableStatement(getCreateTableStatement(t));
        //将该表放入到表管理器中
        TableManager.getInstance().put(t);
    }
    /**
     * 解析表名称
     */
    private void parseTableName(Table t,Class<?> clz){
        //获取表名称
        com.sounder.sounder.annotation.Table anno = clz.getAnnotation(com.sounder.sounder.annotation.Table.class);
        if(anno == null){
            throw new RuntimeException("没有指定数据表名称");
        }else{
            t.setTableName(anno.value());
            if(TextUtil.isEmpty(t.getTableName())){
                throw new RuntimeException("指定的表名称为空");
            }
        }
    }
    /**
     * 解析所有字段
     */
    private void parseTableParams(Table t,Class<?> clz){
        //获取所有字段
        Field[] fields = clz.getDeclaredFields();
        if(fields == null){
            throw new IllegalStateException("how about to set a nickname to your table");
        }
        t.getParams().clear();
        for (Field f : fields){
            parseField(f,t);
        }
    }

    /**
     * 解析单个字段并生成字段对象，添加到集合
     * @param field
     */
    private void parseField(Field field,Table t){
        //获取注解
        Constraint anno = field.getAnnotation(Constraint.class);
        if(anno == null){
            //没有设置约束就不添加到数据表
            return;
        }
        Param param = new Param();
        String column = anno.name();
        String name = field.getName();
        param.setName(name);
        if(TextUtil.isEmpty(column)){
            param.setColumn(field.getName());
        }else{
            param.setColumn(column);
        }
        param.setType(anno.type().getType());
        param.setPrimarykey(anno.primaryKey());
        param.setAutoincrement(anno.autoInCrement());
        t.getParams().add(param);
    }

    /**
     * 将字段集合排序，将自增主键排最前面
     */
    private void sort(Table table){
        if(table.getParams().size() != 0){
            Collections.sort(table.getParams(),new ComparatorUtil());
        }
    }

    /**
     * 生成建表语句
     * @return
     */
    public String getCreateTableStatement(Table t){
        String s = "CREATE TABLE IF NOT EXISTS "+t.getTableName() + "(";
        for (int i = 0;i<t.getParams().size();i++){
            Param p = t.getParamByIndex(i);
            s += p.getColumn() + " " +p.getType();
            if(p.isAutoincrement()){
                s += " PRIMARY KEY AUTOINCREMENT";
            }else if(p.isPrimarykey()){
                s += " PRIMARYKEY";
            }
            if(i < t.getParams().size() -1){
                s += ",";
            }
        }
        s += ");";
        System.out.println("建表语句-->"+s);
        return s;
    }
}
