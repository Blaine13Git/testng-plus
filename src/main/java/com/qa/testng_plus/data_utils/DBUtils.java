package com.qa.testng_plus.data_utils;

import com.qa.testng_plus.common_utils.DateUtil;
import com.qa.testng_plus.common_utils.StringUtils;
import com.qa.testng_plus.csv_utils.CsvParser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DBUtils {
    private static ThreadLocal<Integer> argsIndex = new ThreadLocal<>();
    private static final CsvParser csvParser = new CsvParser();
    private static ThreadLocal<Connection> lockDBConnectionHolder = new ThreadLocal<>();
    private static Throwable th;

    static {
        argsIndex.set(0);
    }

    public static DataMap getQueryResultMap(String tableName, String sql) throws Throwable {
        DataMap map = null;
        DBConn conn = new DBConn();
        ResultSet resultSet = conn.executeQuery(tableName, sql);
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            if (resultSet.next()) {
                DataMap map2 = new DataMap();
                int i = 1;
                while (i <= rsmd.getColumnCount()) {
                    try {
                        map2.put(StringUtils.toUpperCase(rsmd.getColumnName(i)), resultSet.getObject(i));
                        i++;
                    } catch (SQLException e) {
                        map = map2;
                        conn.close();
                        return map;
                    } catch (Throwable th) {
                        th = th;
                        DataMap dataMap = map2;
                        conn.close();
                        throw th;
                    }
                }
                map = map2;
            }
            conn.close();
        } catch (SQLException e2) {
            conn.close();
            return map;
        } catch (Throwable th2) {
            th = th2;
            conn.close();
            throw th;
        }
        return map;
    }

    public static List<DataMap> getQueryMultiResultMap(String tableName, String sql) throws Throwable {
        DataMap map = null;
        List dataList = new ArrayList();
        DataMap map2 = null;
        DBConn conn = new DBConn();
        ResultSet resultSet = conn.executeQuery(tableName, sql);
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            while (true) {
                try {
                    map = map2;
                    if (!resultSet.next()) {
                        break;
                    }
                    map2 = new DataMap();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        map2.put(StringUtils.toUpperCase(rsmd.getColumnName(i)), resultSet.getObject(i));
                    }
                    dataList.add(map2);
                } catch (SQLException e) {
                    DataMap dataMap = map;
                    conn.close();
                    return dataList;
                } catch (Throwable th) {
                    th = th;
                    DataMap dataMap2 = map;
                    conn.close();
                    throw th;
                }
            }
            conn.close();
            DataMap dataMap3 = map;
        } catch (SQLException e2) {
        } catch (Throwable th2) {
            th = th2;
            conn.close();
            throw th;
        }
        return dataList;
    }

    @Deprecated
    public static ResultSet getQueryResult(String sql, String tableName) {
        DBConn conn = new DBConn();
        return new ResultSetProxy(conn.executeQuery(tableName, sql), conn).getProxy();
    }

    @Deprecated
    public static ResultSet getQueryResult(String sql, String tableName, String dbConfigKey) {
        DBConn conn = new DBConn();
        return new ResultSetProxy(conn.executeQuery(tableName, sql), conn).getProxy();
    }

    /* JADX INFO: finally extract failed */
    public static int getUpdateResultMap(String tableName, String sql) {
        DBConn conn = new DBConn();
        try {
            int resultSet = conn.executeUpdate(tableName, sql);
            conn.close();
            return resultSet;
        } catch (Exception e) {
            conn.close();
            return -1;
        } catch (Throwable th) {
            conn.close();
            throw th;
        }
    }

    public static String getStringValue(String tableName, String sql) {
        String value;
        DBConn conn = new DBConn();
        ResultSet resultSet = conn.executeQuery(tableName, sql);
        try {
            if (!resultSet.next()) {
                conn.close();
                return null;
            }
            value = resultSet.getString(1);
            return value;
        } catch (SQLException e) {
        } finally {
            conn.close();
        }
        return null;
    }

    public static List<String> getStringValueList(String tableName, String sql) {
        List<String> valueList = new ArrayList<>();
        DBConn conn = new DBConn();
        ResultSet resultSet = conn.executeQuery(tableName, sql);
        try {
            if (!resultSet.next()) {
                return null;
            }
            valueList.add(resultSet.getObject(1).toString());
            while (resultSet.next()) {
                valueList.add(resultSet.getObject(1).toString());
            }
            conn.close();
            return valueList;
        } catch (SQLException var9) {
            System.err.println(var9.getMessage());
            return valueList;
        } finally {
            conn.close();
        }
    }

    public static boolean DBCheckMultiColumns(String path, String columns, String[] args) {
        List<Integer> columnList;
        argsIndex.set(0);
        if (StringUtils.isBlank(path) || (columnList = StringUtils.convertColumnRegex2Columns(columns)) == null || columnList.size() == 0) {
            return false;
        }
        int err = 0;
        for (int i = 0; i < columnList.size(); i++) {
            if (!DBCheckWithoutConditionImpl(path, columnList.get(i).intValue(), args)) {
                err++;
            }
        }
        return err <= 0;
    }

    public static void genDBCheckTemplateCsv(String tableName, String path) {
        if (StringUtils.isBlank(tableName) || StringUtils.isBlank(path)) {
            throw new RuntimeException("tableName or path cannot be null!");
        }
        String tableName2 = tableName.toUpperCase();
        DBConn conn = new DBConn();
        List csvValues = new ArrayList();
        csvValues.add(new String[]{"tableName", "colsName", "comments", "flag", "exp1"});
        try {
            int i = 0;
            ResultSet resultSet = conn.executeQuery(tableName2, "select column_name, column_comment from information_schema.columns where table_name='" + tableName2 + "'");
            while (resultSet.next()) {
                String colsName = resultSet.getString(1);
                String comment = resultSet.getString(2);
                String firstColumn = StringUtils.EMPTY_STRING;
                if (i == 0) {
                    firstColumn = tableName2;
                }
                csvValues.add(new String[]{firstColumn, colsName, comment, "Y", StringUtils.EMPTY_STRING});
                i++;
            }
            conn.close();
            try {
                csvParser.writeToCsv(csvParser.getCsvFile(String.format(path, new Object[]{tableName2})), csvValues);
            } catch (Exception var16) {
                throw new RuntimeException(var16);
            }
        } catch (SQLException var17) {
            throw new RuntimeException(var17);
        } catch (Throwable th) {
            conn.close();
            throw th;
        }
    }

    public static boolean DBCheckEmptyColumns(String[] paths, String[] columns) {
        if (paths == null || columns == null || paths.length != columns.length) {
            return false;
        }
        int err = 0;
        for (int i = 0; i < paths.length; i++) {
            if (!DBCheckEmptyColumns(paths[i], columns[i], new String[0])) {
                err++;
            }
        }
        if (err <= 0) {
            return true;
        }
        return false;
    }

    public static boolean DBCheckEmptyColumns(String path, String columns, String[] args) {
        List<Integer> columnList;
        argsIndex.set(0);
        if (StringUtils.isBlank(path) || (columnList = StringUtils.convertColumnRegex2Columns(columns)) == null || columnList.size() == 0) {
            return false;
        }
        int err = 0;
        for (int i = 0; i < columnList.size(); i++) {
            if (!DBCheckEmptyColumnImpl(path, columnList.get(i).intValue(), args)) {
                err++;
            }
        }
        return err <= 0;
    }

    private static boolean DBCheckEmptyColumnImpl(String path, int index, String[] args) {
        String[] tableNameAndCondition = getTableNameAndConditionWithCsvCFlag(path, index, args);
        if (tableNameAndCondition == null) {
            return false;
        }
        String tableName = tableNameAndCondition[0];
        String condition = tableNameAndCondition[1];
        if (StringUtils.isBlank(tableName) || StringUtils.isBlank(condition)) {
            return false;
        }
        return checkDataExist("select * from " + tableName + " where (" + condition + ")", tableName, (String) null);
    }

    public static boolean checkDataExist(String executeSql, String tableName, String dbConfigKey) {
        try {
            DBConn conn = new DBConn();
            boolean retVal = !conn.executeQuery(tableName, executeSql).next();
            conn.close();
            boolean z = retVal;
            return retVal;
        } catch (SQLException var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public static boolean DBCheckWithoutCondition(String[] paths, String[] args) {
        argsIndex.set(0);
        return DBCheckWithoutCondition(paths, 1, args);
    }

    public static boolean DBCheckWithoutCondition(String path, String[] args) {
        argsIndex.set(0);
        return DBCheckWithoutConditionImpl(path, 1, args);
    }

    public static boolean DBCheckWithoutCondition(String[] paths, int index, String[] args) {
        argsIndex.set(0);
        int err = 0;
        for (String DBCheckWithoutConditionImpl : paths) {
            if (!DBCheckWithoutConditionImpl(DBCheckWithoutConditionImpl, index, args)) {
                err++;
            }
        }
        if (err <= 0) {
            return true;
        }
        return false;
    }

    public static boolean DBCheckWithoutCondition(String path, int index, String[] args) {
        argsIndex.set(0);
        return DBCheckWithoutConditionImpl(path, index, args);
    }

    private static boolean DBCheckWithoutConditionImpl(String path, int index, String[] args) {
        String[] tableNameAndCondition = getTableNameAndConditionWithCsvCFlag(path, index, args);
        String tableName = tableNameAndCondition[0];
        String condition = tableNameAndCondition[1];
        if (StringUtils.isBlank(tableName) || StringUtils.isBlank(condition)) {
            return false;
        }
        return DBCheck(new String[]{path}, new String[]{tableName}, new String[]{condition}, index);
    }

    private static java.lang.String[] getTableNameAndConditionWithCsvCFlag(java.lang.String r24, int r25, java.lang.String[] r26) {
        throw new UnsupportedOperationException("Method not decompiled: com.xinc818.qa.qats4testng.database.DBUtils.getTableNameAndConditionWithCsvCFlag(java.lang.String, int, java.lang.String[]):java.lang.String[]");
    }

    public static boolean DBCheck(String path, String tableName, String condition) {
        return DBCheck(new String[]{path}, new String[]{tableName}, new String[]{condition}, 1);
    }

    public static boolean DBCheck(String path, String tableName, String condition, String dbConfigKey) {
        return DBCheck(new String[]{path}, new String[]{tableName}, new String[]{condition}, 1, new String[]{dbConfigKey});
    }

    public static boolean DBCheck(String path, String tableName, String condition, int id) {
        return DBCheck(new String[]{path}, new String[]{tableName}, new String[]{condition}, id);
    }

    public static boolean DBCheck(String path, String tableName, String condition, int id, String dbConfigKey) {
        return DBCheck(new String[]{path}, new String[]{tableName}, new String[]{condition}, id, new String[]{dbConfigKey});
    }

    public static boolean DBCheck(String[] path, String[] tableName, String[] condition) {
        return DBCheck(path, tableName, condition, 1);
    }

    public static boolean DBCheck(String[] path, String[] tableName, String[] condition, int id) {
        return DBCheck(path, tableName, condition, id, (String[]) null);
    }

    public static boolean DBCheck(String[] path, String[] tableName, String[] condition, int id, String[] dbConfigKey) {
        boolean isSuccess = true;
        if (path.length != tableName.length || path.length != condition.length || (dbConfigKey != null && path.length != dbConfigKey.length)) {
            return false;
        }
        new HashMap();
        List retMsgList = new ArrayList();
        for (int i = 0; i < path.length; i++) {
            HashMap resultmap = DataCompare.compareTableData(path[i], DataCompare.composeQuerySql(path[i], tableName[i], condition[i]), tableName[i], id, dbConfigKey == null ? null : dbConfigKey[i]);
            if (!resultmap.isEmpty()) {
                retMsgList.add(resultmap);
                isSuccess = false;
            }
            for (Object key : resultmap.keySet()) {
                resultmap.get(key);
            }
        }
        boolean z = isSuccess;
        return isSuccess;
    }

    public static String composeSql(String sql, String condition) {
        return sql + " where " + condition;
    }

    public static boolean DBClean(String[] sqls, String[] tables) {
        if (sqls.length != tables.length) {
            return false;
        }
        int error = 0;
        for (int i = 0; i < sqls.length; i++) {
            if (getUpdateResultMap(tables[i], sqls[i]) < 0) {
                error++;
            }
        }
        if (error <= 0) {
            return true;
        }
        return false;
    }

    public static String getStringByDate() {
        return getStringByDate(new Date());
    }

    public static String getStringByDate(Date dt) {
        if (dt != null) {
            return new SimpleDateFormat(DateUtil.simple).format(dt);
        }
        return StringUtils.EMPTY_STRING;
    }

    public static void table2Csv(java.lang.String r16, java.lang.String r17) {
        throw new UnsupportedOperationException("Method not decompiled: DBUtils.table2Csv(java.lang.String, java.lang.String):void");
    }

    public void csvToMysql(java.lang.String r16, java.lang.String r17, java.lang.String[] r18) {
        throw new UnsupportedOperationException("Method not decompiled: DBUtils.csvToMysql(java.lang.String, java.lang.String, java.lang.String[]):void");
    }

    public void csvToMysql(String csvFile, String tableName) {
        csvToMysql(csvFile, tableName, new String[0]);
    }
}
