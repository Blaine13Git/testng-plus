package com.qa.testng_plus.csv_utils;

import com.qa.testng_plus.common_utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvParamHandler {
    private static Map<String, String> dbCheckVarMap = new HashMap();

    public static String getValue(String varName) {
        return dbCheckVarMap.get(varName) == null ? StringUtils.EMPTY_STRING : dbCheckVarMap.get(varName);
    }

    public static void putValue(String key, String value) {
        dbCheckVarMap.put(key, value);
    }

    public static String replaceVars(String originalValue) {
        if (!StringUtils.isNotBlank(originalValue) || !originalValue.contains("$")) {
            return originalValue;
        }
        Pattern varPattern = Pattern.compile("(^|[^\\$])(\\$[a-zA-Z_]\\w*)");
        Matcher varMatcher = varPattern.matcher(originalValue);
        while (varMatcher.find()) {
            String varName = varMatcher.group(2);
            originalValue = StringUtils.replaceSubString(originalValue, getValue(varName), varMatcher.start(2), varMatcher.end(2));
            varMatcher = varPattern.matcher(originalValue);
        }
        return originalValue.replaceAll("\\$\\$", "\\$");
    }
}