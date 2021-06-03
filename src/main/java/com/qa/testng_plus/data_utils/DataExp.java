package com.qa.testng_plus.data_utils;

public class DataExp {
    private String colsName;
    private String comments;
    private String exp;
    private String flag;
    private String tableName;

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName2) {
        this.tableName = tableName2;
    }

    public String getColsName() {
        return this.colsName;
    }

    public void setColsName(String colsName2) {
        this.colsName = colsName2;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag2) {
        this.flag = flag2;
    }

    public String getExp() {
        return this.exp;
    }

    public void setExp(String exp2) {
        this.exp = exp2;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments2) {
        this.comments = comments2;
    }
}
