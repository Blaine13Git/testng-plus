package com.qa.testng_plus.config;

import com.qa.testng_plus.common_utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyConfig {
    public static String DB_TABLENAME_SPIT_REGEX = ",";
    private static String QAMANAGER_DB_PASSWORD = "te#st_rDw_qaFcenGterM";
    private static String QAMANAGER_DB_URL = "jdbc:mysql://test-xc-daily.mysql.rds.aliyuncs.com:3331/qacenter";
    private static String QAMANAGER_DB_USERNAME = "test_rw_qacenter";
    public static int confNumber = -1;
    protected static Logger logger = LoggerFactory.getLogger(PropertyConfig.class);
    public static Configuration testConfigs = null;

    public static synchronized void initConfigs() {
        synchronized (PropertyConfig.class) {
            if (testConfigs == null) {
                testConfigs = ConfigurationFactory.getConfigration();
            }
            if (testConfigs == null || testConfigs.getConfig().isEmpty()) {
                testConfigs.setProperty("dbType", "mysql");
                testConfigs.setProperty("db_url", QAMANAGER_DB_URL);
                testConfigs.setProperty("db_username", QAMANAGER_DB_USERNAME);
                testConfigs.setProperty("db_password", QAMANAGER_DB_PASSWORD);
            } else {
                String dbtype = testConfigs.getPropertyValue("dbType");
                String dbUrl = testConfigs.getPropertyValue("db_url");
                String dbName = testConfigs.getPropertyValue("db_username");
                String dbPassWord = testConfigs.getPropertyValue("db_password");
                if (dbtype.isEmpty() || dbUrl.isEmpty() || dbName.isEmpty() || dbPassWord.isEmpty()) {
                    logger.error("DB配置项缺失");
                }
            }
        }
    }

    public static String getDBType() {
        initConfigs();
        String dbType = testConfigs.getPropertyValue("dbType");
        if (StringUtils.isBlank(dbType)) {
            return null;
        }
        return dbType;
    }

    public static String getSqlitePath() {
        if (testConfigs == null) {
            initConfigs();
        }
        String sqlitePath = testConfigs.getPropertyValue("sqlite.path");
        if (StringUtils.isBlank(sqlitePath)) {
            return null;
        }
        return sqlitePath;
    }

    public static String getSqliteDBName() {
        if (testConfigs == null) {
            initConfigs();
        }
        return testConfigs.getPropertyValue("sqlite.name");
    }

    public static DBConfig getDBConfig() {
        DBConfig dbconfig = new DBConfig();
        if (testConfigs == null) {
            initConfigs();
        }
        String url = testConfigs.getPropertyValue("db_url");
        String username = testConfigs.getPropertyValue("db_username");
        String password = testConfigs.getPropertyValue("db_password");
        dbconfig.setConnectionUrl(url);
        dbconfig.setUsername(username);
        dbconfig.setPassword(password);
        dbconfig.setSchema(StringUtils.EMPTY_STRING);
        return dbconfig;
    }

    public static DBConfig getDBConfig(String tableName, String dbConfigKey) {
        DBConfig dbconfig = new DBConfig();
        if (!StringUtils.isBlank(tableName) || !StringUtils.isBlank(dbConfigKey)) {
            if (testConfigs == null) {
                initConfigs();
            }
            String url = StringUtils.EMPTY_STRING;
            String username = StringUtils.EMPTY_STRING;
            String password = StringUtils.EMPTY_STRING;
            String schema = StringUtils.EMPTY_STRING;
            if (StringUtils.isNotBlank(tableName)) {
                tableName = tableName.toUpperCase().trim();
            }
            if (!StringUtils.isBlank(dbConfigKey) && !StringUtils.EMPTY_STRING.equals(dbConfigKey)) {
                String dbConfigKey2 = dbConfigKey.trim();
                if (dbConfigKey2.startsWith("ext")) {
                    url = testConfigs.getPropertyValue(dbConfigKey2 + "_db_url");
                    username = testConfigs.getPropertyValue(dbConfigKey2 + "_db_username");
                    password = testConfigs.getPropertyValue(dbConfigKey2 + "_db_password");
                    schema = testConfigs.getPropertyValue(dbConfigKey2 + "_db_schema");
                } else if (dbConfigKey2.startsWith("obstd")) {
                    url = testConfigs.getPropertyValue(dbConfigKey2 + "_db_url");
                    dbconfig.setConnectionType(DBConfig.CONN_TYPE.OB_STD);
                }
            } else if (matchTableWithMultiRegex(tableName, testConfigs.getPropertyValue("db_tablename"), testConfigs.getPropertyValue("db_regex"))) {
                url = testConfigs.getPropertyValue("db_url");
                username = testConfigs.getPropertyValue("db_username");
                password = testConfigs.getPropertyValue("db_password");
                schema = testConfigs.getPropertyValue("db_schema");
            } else {
                int i = 1;
                while (true) {
                    if (i >= confNumber) {
                        break;
                    } else if (matchTableWithMultiRegex(tableName, testConfigs.getPropertyValue("ext" + i + "_db_tablename"), testConfigs.getPropertyValue("ext" + i + "_db_regex"))) {
                        url = testConfigs.getPropertyValue("ext" + i + "_db_url");
                        username = testConfigs.getPropertyValue("ext" + i + "_db_username");
                        password = testConfigs.getPropertyValue("ext" + i + "_db_password");
                        schema = testConfigs.getPropertyValue("ext" + i + "_db_schema");
                        break;
                    } else {
                        i++;
                    }
                }
            }
            dbconfig.setConnectionUrl(url);
            dbconfig.setUsername(username);
            dbconfig.setPassword(password);
            dbconfig.setSchema(schema);
        }
        return dbconfig;
    }

    private static boolean matchTableWithMultiRegex(String tableName, String tableNameConfig, String regex) {
        if (StringUtils.isBlank(regex)) {
            return matchTable(tableName, tableNameConfig, regex);
        }
        String[] multiRegex = regex.split(DB_TABLENAME_SPIT_REGEX);
        if (multiRegex.length == 0) {
            return matchTable(tableName, tableNameConfig, regex);
        }
        for (String matchTable : multiRegex) {
            if (matchTable(tableName, tableNameConfig, matchTable)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchTable(String tableName, String tableNameConfig, String regex) {
        boolean regexMatch;
        if (StringUtils.isBlank(tableNameConfig) || StringUtils.isBlank(tableName)) {
            return false;
        }
        String[] tables = tableNameConfig.toUpperCase().split(DB_TABLENAME_SPIT_REGEX);
        if (!StringUtils.isNotBlank(regex) || !regex.contains("{tablename}")) {
            regexMatch = false;
        } else {
            regexMatch = true;
        }
        String[] var5 = tables;
        int var6 = tables.length;
        for (int var7 = 0; var7 < var6; var7++) {
            String table = var5[var7].trim();
            if (regexMatch) {
                if (tableName.matches(regex.replace("{schema}", calcSchema(table)).replace("{tablename}", calcTableName(table)))) {
                    return true;
                }
            } else if (table.equals(tableName)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0009, code lost:
        r0 = r2.indexOf(46);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String calcSchema(java.lang.String r2) {
        /*
            boolean r1 = com.xinc818.qa.qats4testng.utils.StringUtils.isBlank(r2)
            if (r1 == 0) goto L_0x0009
            java.lang.String r1 = ""
        L_0x0008:
            return r1
        L_0x0009:
            r1 = 46
            int r0 = r2.indexOf(r1)
            if (r0 > 0) goto L_0x0014
            java.lang.String r1 = ""
            goto L_0x0008
        L_0x0014:
            r1 = 0
            java.lang.String r1 = r2.substring(r1, r0)
            goto L_0x0008
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xinc818.qa.qats4testng.config.PropertyConfig.calcSchema(java.lang.String):java.lang.String");
    }

    private static String calcTableName(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return StringUtils.EMPTY_STRING;
        }
        int pos = tableName.indexOf(46);
        return pos >= 0 ? tableName.substring(pos + 1) : tableName;
    }

    public static String getGroupName() {
        initConfigs();
        return testConfigs.getPropertyValue("group_name");
    }
}
