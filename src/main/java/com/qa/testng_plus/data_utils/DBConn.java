package com.qa.testng_plus.data_utils;

import com.qa.testng_plus.common_utils.StringUtils;
import com.qa.testng_plus.config.DBConfig;
import com.qa.testng_plus.config.PropertyConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBConn {
    private Connection conn = null;
    private ResultSet myResultSet = null;
    private Statement statement = null;

    public ResultSet executeQuery(String tableName, String sql) {
        try {
            initConnection(tableName);
            this.myResultSet = this.statement.executeQuery(sql);
        } catch (Exception e) {
            System.err.println("数据库操作出错。sql=[" + sql + "], tableName=[" + tableName + "]");
        }
        return this.myResultSet;
    }

    public int executeUpdate(String tableName, String sql) {
        try {
            initConnection(tableName);
            return this.statement.executeUpdate(sql);
        } catch (Exception e) {
            System.err.println("数据库操作出错。sql=[" + sql + "], tableName=[" + tableName + "]");
            return -1;
        }
    }

    public void close() {
        try {
            if (this.myResultSet != null) {
                this.myResultSet.close();
            }
            if (this.conn != null) {
                this.conn.close();
            }
            if (this.statement != null) {
                this.statement.close();
            }
        } catch (SQLException e) {
            System.err.println("关闭数据库连接出错。");
        }
    }

    private void initConnection(String tableName) throws Exception {
        String dbType = PropertyConfig.getDBType();
        try {
            if (!StringUtils.isBlank(dbType) && dbType.equals("sqlite")) {
                String dbPath = PropertyConfig.getSqlitePath();
                String dbName = PropertyConfig.getSqliteDBName();
                Class.forName("org.sqlite.JDBC");
                if (dbPath == null) {
                    dbPath = Class.class.getClass().getResource("/").getPath();
                } else {
                    if (!dbPath.endsWith("/")) {
                        dbPath = dbPath + "/";
                    }
                    if (!dbPath.startsWith("/") && !dbPath.matches("^[a-hA-H]:\\S*")) {
                        dbPath = Class.class.getClass().getResource("/").getPath() + dbPath;
                    }
                }
                this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath + dbName);
                this.statement = this.conn.createStatement();
            } else if (dbType.isEmpty() || dbType.equals("mysql")) {
                DBConfig dbconf = PropertyConfig.getDBConfig();
                String user = dbconf.getUsername();
                String password = dbconf.getPassword();
                List<String> urlArr = new ArrayList<>(Arrays.asList(dbconf.getConnectionUrl().split(",")));
                ArrayList arrayList = new ArrayList(Arrays.asList(user.split(",")));
                List<String> passwordArr = new ArrayList<>(Arrays.asList(password.split(",")));
                urlArr.add("jdbc:mysql://xxx:3331/qacenter");
                arrayList.add("tester"); // user
                passwordArr.add("tester");
                Class.forName("com.mysql.jdbc.Driver");
                int index = 0;
                for (String url : urlArr) {
                    if (url.contains("qacenter")) {
                        user = "tester";
                        password = "tester";
                    }
                    if (arrayList.size() > 2 && passwordArr.size() > 2 && arrayList.size() == passwordArr.size() && arrayList.size() == urlArr.size()) {
                        user = (String) arrayList.get(index);
                        password = passwordArr.get(index);
                        index++;
                    }
                    this.conn = DriverManager.getConnection(url, user, password);
                    this.statement = this.conn.createStatement();
                    String[] tns = tableName.split("\\.");
                    if (tns.length != 2) {
                        if (this.conn.getMetaData().getTables((String) null, (String) null, tableName, (String[]) null).next()) {
                            return;
                        }
                    } else if (this.conn.getCatalog().equals(tns[0])) {
                        return;
                    }
                }
            }
        } catch (ClassNotFoundException var17) {
            System.out.println("数据库驱动不存在！" + var17.toString());
        } catch (SQLException var18) {
            System.out.println("数据库存在异常,请检查数据库配置！" + var18.toString());
        }
    }
}
