package com.sounder.sounder.dao;
import android.content.ContentValues;
import android.content.Entity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.sounder.sounder.annotation.FieldType;
import com.sounder.sounder.model.Param;
import com.sounder.sounder.resolver.Table;
import com.sounder.sounder.resolver.TableManager;
import com.sounder.sounder.util.TextUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Createed by Sounder on 2017/1/21
 */
public class Dao {
    private static Dao sInstance;

    /**
     * 数据库操作对象，需要用户自实现
     */
    private SQLiteOpenHelper mSQLiteOpenHelper;

    private SQLiteDatabase mSQLiteDatabase;

    public static Dao getInstance(){
        if(sInstance == null){
            sInstance = new Dao();
        }
        return sInstance;
    }
    public void insert(Object object){
        Table table = TableManager.getInstance().get(object.getClass());
        if(table == null){
            throw new IllegalArgumentException("当前不存在该数据表信息");
        }
        //通过反射获取属性值并设置键值对
        ContentValues cv = new ContentValues();
        for(Param p : table.getParams()){
            if(p.isPrimarykey()){
                continue;
            }
            try {
                Field field = object.getClass().getDeclaredField(p.getName());
                field.setAccessible(true);
                if(p.getType().equals(FieldType.TEXT.getType())){
                    cv.put(p.getColumn(),(String)field.get(object));
                }else if(p.getType().equals(FieldType.INTEGER.getType())){
                    cv.put(p.getColumn(),(int)field.get(object));
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        long id = mSQLiteDatabase.insert(table.getTableName(),null,cv);
        System.out.println("插入操作返回的id---->"+id);
    }

    public ArrayList<Object> query(Class<?> clz){
        return query(clz,null);
    }

    /**
     * 查询操作，可指定查询条件，形如 id=1 AND name='jack‘（不用假where）
     * @param clz
     * @param whereClause
     */
    public ArrayList<Object> query(Class<?> clz, String whereClause){
        Table table = TableManager.getInstance().get(clz);
        if(table == null){
            throw  new IllegalArgumentException("没有当前表信息"+clz.getSimpleName());
        }
        ArrayList<Object> results = new ArrayList<>();
        //查询全部
        String sql = "select * from "+table.getTableName();
        if(!TextUtil.isEmpty(whereClause)){
            sql += " where "+whereClause;
        }
        sql += ";";
        mSQLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(sql,null);
        while (cursor.moveToNext()){
            try {
                Object object = clz.newInstance();
                for(Param p : table.getParams()){
                    Field field = clz.getDeclaredField(p.getName());
                    field.setAccessible(true);
                    if(p.getType().equals(FieldType.INTEGER.getType())){
                        field.set(object,cursor.getInt(cursor.getColumnIndexOrThrow(p.getColumn())));
                    }else{
                        field.set(object,cursor.getString(cursor.getColumnIndexOrThrow(p.getColumn())));
                    }
                }
                results.add(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return results;
    }

    public int delete(Class<?> clz,String whereClause,String[] whereArgs){
        Table table = TableManager.getInstance().get(clz);
        if(table == null){
            throw  new IllegalArgumentException("没有当前表信息"+clz.getSimpleName());
        }
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        return mSQLiteDatabase.delete(table.getTableName(),whereClause,whereArgs);
    }
    public int update(Class<?> clz,ContentValues cv,String whereClause,String[] whereArgs){
        Table table = TableManager.getInstance().get(clz);
        if(table == null){
            throw  new IllegalArgumentException("没有当前表信息"+ clz.getSimpleName());
        }
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        return mSQLiteDatabase.update(table.getTableName(),cv,whereClause,whereArgs);
    }
    public void setSQLiteOpenHelper(SQLiteOpenHelper helper){
        this.mSQLiteOpenHelper = helper;
    }
    /**
     * 判断所需要的东西是否齐全
     */
    public void throwException(){
        if(mSQLiteOpenHelper == null){
            throw new NullPointerException("SQLiteOpenHelper can not be null");
        }
    }
    public SQLiteDatabase getSQLiteDatabase(){
        if(mSQLiteDatabase == null){
            System.out.println("--------------------->null");
        }
        return mSQLiteOpenHelper.getReadableDatabase();
    }
    /*
        #Summary
        部分静态方法
     */


    /**
     * 初始化表信息
     * 需要在使用数据库之前调用，否则无法成功创建表
     * @param clzs
     */
    public static void initTables(@NonNull Class<?>... clzs){
        for(Class<?> clz : clzs){
            if(TableManager.getInstance().get(clz) == null) {//当map中没有该数据表时才。。。，不然浪费时间
                Table table = new Table(clz);
            }
        }
    }
}
