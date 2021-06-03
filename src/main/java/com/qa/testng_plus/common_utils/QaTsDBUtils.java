package com.qa.testng_plus.common_utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import com.qa.testng_plus.data_utils.DBUtils;
import com.qa.testng_plus.data_utils.DataMap;
import org.apache.commons.beanutils.BeanMap;

public class QaTsDBUtils {
    private static int argsIndex = 0;
    private static boolean lockFlag = false;
    private static Connection sdgDBConnection = null;
    private static PreparedStatement sdgDBPreparedStatement = null;
    public static DBUtils testData = new DBUtils();

    public static int selectCountDB(String tableName, String[] conditions) throws Throwable {
        if (conditions == null) {
            System.out.println("查询 " + tableName + " 数据记录，参数错误");
            return -1;
        }
        String selectSql = "SELECT count(*) n from " + tableName;
        if (conditions.length != 0) {
            selectSql = selectSql + " WHERE " + genConditionSql(conditions);
            System.out.println(selectSql);
        }
        return DBUtils.getQueryResultMap(tableName, selectSql).getIntValue("n");
    }

    public static int selectCountDB(String tableName, String condition) throws Throwable {
        return selectCountDB(tableName, new String[]{condition});
    }

    public static String selectStrDB(String tableName, String key, String condition) {
        if (!StringUtils.isBlank(condition)) {
            return DBUtils.getStringValue(tableName, "SELECT " + key + " from " + tableName + " WHERE " + condition);
        }
        System.out.println("查询 " + tableName + " 数据记录，条件为空");
        return null;
    }

    public static String selectStrDB(String tableName, String key, String[] conditions, String subCondition) {
        if (conditions == null || conditions.length == 0) {
            System.out.println("查询 " + tableName + " 数据记录，参数错误");
            return null;
        }
        if (StringUtils.isBlank(subCondition)) {
            subCondition = StringUtils.EMPTY_STRING;
        }
        return DBUtils.getStringValue(tableName, "SELECT " + key + " from " + tableName + " WHERE " + genConditionSql(conditions) + subCondition);
    }

    public static List<String> selectStrDBList(String tableName, String key, String[] conditions, String subCondition) {
        if (conditions == null || conditions.length == 0) {
            System.out.println("查询 " + tableName + " 数据记录，参数错误");
            return null;
        }
        if (StringUtils.isBlank(subCondition)) {
            subCondition = StringUtils.EMPTY_STRING;
        }
        return DBUtils.getStringValueList(tableName, "SELECT " + key + " from " + tableName + " WHERE " + genConditionSql(conditions) + subCondition);
    }

    public static DataMap selectMapDB(String tableName, String condition, String subCondition) throws Throwable {
        return selectMapDB(tableName, new String[]{condition}, subCondition);
    }

    public static DataMap selectMapDB(String tableName, String[] conditions, String subCondition) throws Throwable {
        if (conditions == null || conditions.length == 0) {
            System.out.println("查询 " + tableName + " 数据记录，参数错误");
            return null;
        }
        if (StringUtils.isBlank(subCondition)) {
            subCondition = StringUtils.EMPTY_STRING;
        }
        return DBUtils.getQueryResultMap(tableName, "SELECT * from " + tableName + " WHERE " + genConditionSql(conditions) + subCondition);
    }

    public static void upDateDB(String tableName, String col, String val, String condition) {
        upDateDB(tableName, new String[]{col}, new String[]{val}, new String[]{condition});
    }

    public static void upDateDB(String tableName, String[] cols, String[] values, String[] conditions) {
        if (cols.length != values.length || conditions == null || conditions.length == 0) {
            System.out.println("修改 " + tableName + " 数据记录，参数错误");
            return;
        }
        String tempSql = genColValSql(cols, values);
        DBUtils.getUpdateResultMap(tableName, ("UPDATE " + tableName + " SET " + tempSql + " WHERE " + genConditionSql(conditions)).replaceAll("'null'", "null"));
    }

    public static void deleteDB(String tableName, String condition) {
        deleteDB(tableName, new String[]{condition});
    }

    public static void deleteDB(String tableName, String[] conditions) {
        if (conditions == null || conditions.length == 0) {
            System.out.println("删除 " + tableName + " 数据记录，参数错误");
            return;
        }
        DBUtils.getUpdateResultMap(tableName, "DELETE FROM " + tableName + " WHERE " + genConditionSql(conditions));
    }

    public static void insertDB(String tableName, String keys, String vals) {
        DBUtils.getUpdateResultMap(tableName, "INSERT INTO " + tableName + ("(" + keys + ")") + " VALUES " + ("(" + vals + ")"));
    }

    public static void insertDB(String tableName, Map<Object, Object> keyMap) {
        String keys = StringUtils.EMPTY_STRING;
        String vals = StringUtils.EMPTY_STRING;
        for (Map.Entry entry : keyMap.entrySet()) {
            String key = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entry.getKey().toString());
            if (!key.equals("class")) {
                keys = keys + key + ",";
                vals = vals + "'" + entry.getValue() + "',";
            }
        }
        DBUtils.getUpdateResultMap(tableName, "INSERT INTO " + tableName + ("(" + keys.substring(0, keys.length() - 1) + ")") + " VALUES " + ("(" + vals.substring(0, vals.length() - 1) + ")"));
    }

    public static String genConditionSql(String[] conditions) {
        StringBuilder tempConditon = new StringBuilder();
        for (String append : conditions) {
            tempConditon.append(" AND ").append(append);
        }
        return tempConditon.toString().replaceFirst(" AND ", StringUtils.EMPTY_STRING);
    }

    public static String genColValSql(String[] cols, String[] values) {
        StringBuilder tempSql = new StringBuilder();
        for (int i = 0; i < cols.length; i++) {
            tempSql.append(",").append(cols[i]).append(" = '").append(values[i]).append("'");
        }
        return tempSql.toString().replaceFirst(",", StringUtils.EMPTY_STRING);
    }

    public static boolean DBCheckMultiColumns(String[] path, String[] condition, String[] columns) {
        int i = 0;
        String[] var4 = path;
        int var5 = path.length;
        for (int var6 = 0; var6 < var5; var6++) {
            String singlePath = var4[var6];
            String cols = columns[i];
            String tableName = singlePath.substring(singlePath.indexOf(".") + 1, singlePath.length() - 4);
            if (cols.contains(":")) {
                for (int j = Integer.parseInt(cols.split(":")[0]); j <= Integer.parseInt(cols.split(":")[1]); j++) {
                    if (!DBUtils.DBCheck(singlePath, tableName, condition[i], j)) {
                        return false;
                    }
                }
                continue;
            } else if (!DBUtils.DBCheck(singlePath, tableName, condition[i], Integer.parseInt(cols))) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean DBCheckWithoutConditionByMultiColums(String path, String columns, String... args) {
        List<Integer> columnList;
        argsIndex = 0;
        if (StringUtils.isBlank(path) || (columnList = StringUtils.convertColumnRegex2Columns(columns)) == null || columnList.size() == 0) {
            return false;
        }
        int err = 0;
        for (int i = 0; i < columnList.size(); i++) {
            int colNum = columnList.get(i).intValue();
            if (!DBCheckWithoutCondition(path, colNum, args)) {
                err++;
                System.out.println("第 " + colNum + "列对比错误");
            }
        }
        return err <= 0;
    }

    public static boolean DBCheckisNull(String sql) throws Throwable {
        return DBUtils.getQueryMultiResultMap(getTableNameFromSql(sql), sql).size() == 0;
    }

    public static void getUpdateResultMap(String[] sqls) {
        String[] var1 = sqls;
        int var2 = sqls.length;
        for (int var3 = 0; var3 < var2; var3++) {
            String sql = var1[var3];
            DBUtils.getUpdateResultMap(getTableNameFromSql(sql), sql);
        }
    }

    public static String getTableNameFromSql(String sql) {
        String[] single = sql.toUpperCase().split(" ");
        if (single[0].equals("SELECT")) {
            return single[3];
        }
        if (single[0].equals("DELETE") || single[0].equals("INSERT")) {
            return single[2];
        }
        if (single[0].equals("UPDATE")) {
            return single[1];
        }
        return null;
    }

    public static void csvToMysql(String tableName, String csvFile, String... args) {
        testData.csvToMysql(csvFile, tableName, args);
    }

    public static void csvToMysql(String tableName, String csvFile) {
        testData.csvToMysql(csvFile, tableName);
    }

    public static boolean DBCheckWithoutCondition(String path, int index, String... args) {
        return DBUtils.DBCheckWithoutCondition(path, index, args);
    }

    public static boolean updateAttributes(String key, String value, String table, String condition, String attributsName) {
        String attributes = selectStrDB(table, attributsName, condition);
        if (attributes.isEmpty()) {
            System.out.println("查询字段内容不存在！");
            return false;
        }
        try {
            JSONObject json = JSONObject.parseObject(attributes);
            json.put(key, value);
            upDateDB(table, attributsName, json.toString(), condition);
            return true;
        } catch (Exception e) {
            System.out.println("查询字段格式错误！");
            return false;
        }
    }

    public static void table2Csv(String tableName, String path) {
        DBUtils.table2Csv(tableName, path);
    }

    public static Map<Object, Object> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return new BeanMap(obj);
    }
}
