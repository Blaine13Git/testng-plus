package com.qa.testng_plus.data_utils;

import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import com.qa.testng_plus.common_utils.StringUtils;

import java.io.InputStreamReader;
import java.util.List;

public class DataCompare {
    private static int COLS_COLUMN = 1;
    private static int COMMENT_COLUMN = 2;
    private static int EXPSTAR_COLUMN = 3;
    private static int FLAG_COLUMN = 3;
    private static List<?> dataExps = null;

    public static String composeQuerySql(String path, String tableName, String condition) {
        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
        strat.setType(DataExp.class);
        strat.setColumnMapping(new String[]{"tableName", "colsName", "comments", "flag", "exp"});
        CsvToBean csv = new CsvToBean();
        InputStreamReader isr = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
        dataExps = csv.parse(strat, isr);
        if (isr != null) {
            try {
                isr.close();
            } catch (Exception e) {
            }
        }
        String clos = StringUtils.EMPTY_STRING;
        for (int i = 1; i < dataExps.size(); i++) {
            if (!"N".equals(((DataExp) dataExps.get(i)).getFlag())) {
                clos = clos + "," + ((DataExp) dataExps.get(i)).getColsName();
            }
        }
        return "SELECT " + clos.replaceFirst(",", StringUtils.EMPTY_STRING) + " FROM " + tableName + " where " + condition;
    }

    public static java.util.HashMap<java.lang.String, java.lang.String> compareTableData(java.lang.String r42, java.lang.String r43, java.lang.String r44, int r45, java.lang.String r46) {
        throw new UnsupportedOperationException("Method not decompiled: DataCompare.compareTableData(java.lang.String, java.lang.String, java.lang.String, int, java.lang.String):java.util.HashMap");
    }
}
