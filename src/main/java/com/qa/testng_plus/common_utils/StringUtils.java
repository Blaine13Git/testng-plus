package com.qa.testng_plus.common_utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StringUtils {
    public static final String EMPTY_STRING = "";

    public static String replaceSubString(String originalValue, String replacement, int start, int end) {
        if (isBlank(originalValue) || start < 0 || start > originalValue.length() || end < 0 || end > originalValue.length() || end <= start) {
            return originalValue;
        }
        String preStr = originalValue.substring(0, start);
        return preStr + replacement + originalValue.substring(end, originalValue.length());
    }

    public static String amountFormat(String amount) {
        return (amount == null || amount.equals("0")) ? "0" : amount + "00";
    }

    public static String stringFormat(String str) {
        if (str.equals("null") || str.equals(EMPTY_STRING)) {
            return "0";
        }
        return str;
    }

    public static String stringNullFormat(String str) {
        if (str.equals("null") || str.equals(EMPTY_STRING)) {
            return null;
        }
        return str;
    }

    public static String amountSysFormat(String amount) {
        if (amount == null || amount.equals("0")) {
            return "0.00";
        }
        if (amount.length() < 3) {
            return "0." + amount;
        }
        return amount.substring(0, amount.length() - 2) + "." + amount.substring(amount.length() - 2, amount.length());
    }

    public static boolean isBlank(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlankOrNull(String str) {
        if (str == null || str.equals("null") || str.length() == 0) {
            return true;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String stringFirstCharToUpperCase(String str) {
        if (str == null || str.length() < 1) {
            return str;
        }
        String strt1 = str.substring(0, 1).toUpperCase();
        return strt1 + str.substring(1);
    }

    public static String stringFirstCharToLowerCase(String str) {
        if (str == null || str.length() < 1) {
            return str;
        }
        String strt1 = str.substring(0, 1).toLowerCase();
        return strt1 + str.substring(1);
    }

    public static List<Integer> convertColumnRegex2Columns(String columnRegex) {
        int to;
        List listColumns = new ArrayList();
        if (isBlank(columnRegex) || !columnRegex.matches("^((\\d+)|(\\d+):(\\d+))(,((\\d+)|(\\d+):(\\d+)))*$")) {
            return listColumns;
        }
        String[] columnStrings = columnRegex.split(",");
        for (String split : columnStrings) {
            String[] columns = split.split(":");
            if (1 == columns.length) {
                listColumns.add(Integer.valueOf(columns[0]));
            } else if (2 != columns.length) {
                return new ArrayList();
            } else {
                int from = Integer.valueOf(columns[0]).intValue();
                int temp = Integer.valueOf(columns[1]).intValue();
                if (from > temp) {
                    to = from;
                    from = temp;
                } else {
                    to = temp;
                }
                for (int j = from; j <= to; j++) {
                    listColumns.add(Integer.valueOf(j));
                }
            }
        }
        Collections.sort(listColumns);
        return listColumns;
    }

    public static String getTradeNo(String url) {
        if (url == null) {
            return EMPTY_STRING;
        }
        String[] tr = url.split("=");
        return tr[tr.length - 1];
    }

    public static boolean isNull(String str) {
        return str == null;
    }

    public static String getRandomId() {
        return DateUtil.getLongDateString() + "-" + new Random().nextInt(1000);
    }

    public static List<Map<String, String>> getParamList(String path) {
        List list = new ArrayList();
        BufferedReader br = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)));
        String[] keys = null;
        int count = 0;
        while (isNotBlank(new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(path))).readLine())) {
            try {
                count++;
            } catch (IOException var15) {
                var15.printStackTrace();
            }
        }
        HashMap[] hashMapArr = new HashMap[(count - 1)];
        String line1 = br.readLine();
        if (isNotBlank(line1)) {
            keys = line1.split(",");
        }
        int t = 0;
        while (true) {
            String line = br.readLine();
            if (line == null) {
                break;
            }
            Map map = new HashMap();
            String[] strs = line.split(",");
            if (strs.length == keys.length) {
                for (int i = 0; i < keys.length; i++) {
                    map.put(keys[i], strs[i]);
                }
            }
            hashMapArr[t] = map;
            list.add(hashMapArr[t]);
            t++;
        }
        return list;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean compareLarger(String arg1, String arg2) {
        return Integer.parseInt(arg1) > Integer.parseInt(arg2);
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equals(str2);
    }

    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000d, code lost:
        r0 = r2.indexOf(r3);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String substringAfter(java.lang.String r2, java.lang.String r3) {
        /*
            if (r2 == 0) goto L_0x0021
            int r1 = r2.length()
            if (r1 == 0) goto L_0x0021
            if (r3 != 0) goto L_0x000d
            java.lang.String r1 = ""
        L_0x000c:
            return r1
        L_0x000d:
            int r0 = r2.indexOf(r3)
            r1 = -1
            if (r0 != r1) goto L_0x0017
            java.lang.String r1 = ""
            goto L_0x000c
        L_0x0017:
            int r1 = r3.length()
            int r1 = r1 + r0
            java.lang.String r1 = r2.substring(r1)
            goto L_0x000c
        L_0x0021:
            r1 = r2
            goto L_0x000c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xinc818.qa.qats4testng.utils.StringUtils.substringAfter(java.lang.String, java.lang.String):java.lang.String");
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equalsIgnoreCase(str2);
    }

    public static String toUpperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }

    public static String toLowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }

    public static Integer objectToInt(Object ob) {
        if (ob != null) {
            return Integer.valueOf(String.valueOf(ob));
        }
        return null;
    }

    public static boolean isNotContainsBlank(String str) {
        if (!isBlank(str) && isNotBothSidesBlank(str)) {
            return str.replaceAll(" ", EMPTY_STRING).equals(str);
        }
        return false;
    }

    public static boolean isNotBothSidesBlank(String str) {
        if (isBlank(str)) {
            return false;
        }
        return str.trim().equals(str);
    }

    private static class IntegerCompare implements Comparator<Object> {
        private IntegerCompare() {
        }

        public int compare(Object o1, Object o2) {
            return ((Integer) o1).intValue() - ((Integer) o2).intValue();
        }
    }
}
