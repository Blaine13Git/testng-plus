package com.qa.testng_plus.config;

public class DBConfig {
    private CONN_TYPE connectionType;
    private String connectionUrl;
    private String driverClass = "com.mysql.jdbc.Driver";
    private String password;
    private String schema;
    private String username;

    public enum CONN_TYPE {
        OB_STD
    }

    public String getConnectionUrl() {
        return this.connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl2) {
        this.connectionUrl = connectionUrl2;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username2) {
        this.username = username2;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }

    public String getDriverClass() {
        return this.driverClass;
    }

    public void setDriverClass(String driverClass2) {
        this.driverClass = driverClass2;
    }

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema2) {
        this.schema = schema2;
    }

    public CONN_TYPE getConnectionType() {
        return this.connectionType;
    }

    public void setConnectionType(CONN_TYPE connectionType2) {
        this.connectionType = connectionType2;
    }

    public String toString() {
        return "DBConfig [connectionUrl=" + this.connectionUrl + ", username=" + this.username + ", password=" + this.password + ", schema=" + this.schema + "]";
    }
}