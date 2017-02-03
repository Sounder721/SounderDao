package com.sounder.sounder.util;
/**
 *  Created by Sounder on 2017/1/21.
 */

import com.sounder.sounder.model.Param;

import java.util.Comparator;

/**
 * Createed by Sounder on 2017/1/21
 */
public class ComparatorUtil implements Comparator<Param> {
    @Override
    public int compare(Param o1, Param o2) {
        if(o1.isAutoincrement() || o1.isPrimarykey()){
            return -1;
        }else if (o2.isAutoincrement() || o2.isPrimarykey()){
            return 1;
        }
        return 0;
    }
}
