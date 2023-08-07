package com.bwongo.commons.models.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bkaaron
 * @created 02/07/2022
 * @project kabangali
 */
public class MapUtils {

    Map map;

    public MapUtils(){
        map = new HashMap<>();
    }

    public static MapUtils create(){
        return new MapUtils();
    }

    public static MapUtils create(String key, Object value) {
        return create().put(key, value);
    }

    public MapUtils put(String key, Object value){
        this.map.put(key,value);
        return this;
    }

    public Map build(){
        return this.map;
    }

}
