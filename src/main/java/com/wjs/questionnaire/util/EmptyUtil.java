package com.wjs.questionnaire.util;

import java.util.List;

/**
 * 判断对象是否为空
 */
public class EmptyUtil {

    /**
     * 判断对象存在
     * @param obj 对象名
     * @return 是否存在
     * 对象存在：false
     * 对象不存在：true
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if ((obj instanceof List)) {
            return ((List) obj).size() == 0;
        }
        if ((obj instanceof String)) {
            return ((String) obj).trim().equals("");
        }
        return false;
    }

    /**
     * 判断对象不存在
     * @param obj 对象名
     * @return 是否存在
     * 对象存在：true
     * 对象不存在：false
     */
    public static boolean isNotEmpty(Object obj)
    {
        return !isEmpty(obj);
    }
}
