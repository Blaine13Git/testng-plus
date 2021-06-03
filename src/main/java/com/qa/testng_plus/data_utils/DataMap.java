package com.qa.testng_plus.data_utils;

import com.qa.testng_plus.common_utils.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;

public class DataMap extends HashMap<String, Object> {
    private static final long serialVersionUID = 9164967756576939731L;

    public String getStringValue(String key) {
        String key2 = convertKey(key);
        String value = null;
        if (containsKey(key2)) {
            Object obj = get(key2);
            if (obj == null) {
                return null;
            }
            if (obj instanceof BigDecimal) {
                value = ((BigDecimal) obj).toString();
            } else {
                value = String.valueOf(obj);
            }
        }
        return value;
    }

    public int getIntValue(String key) {
        return Integer.parseInt(getStringValue(convertKey(key)));
    }

    public long getLongValue(String key) {
        return Long.parseLong(getStringValue(convertKey(key)));
    }

    public static String convertKey(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return key.toUpperCase();
    }

    public Object get(String key) {
        return super.get(convertKey(key));
    }
}
