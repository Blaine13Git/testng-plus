package com.qa.testng_plus.report_utils;

public class ReportLogDO {
    String buildEndTime;
    String buildStartTime;
    String createTime;
    int fail;
    String moduleName;
    int pass;
    String projectName;
    int skip;
    int total;
    String updateTime;
    int version;
    String viewName;

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName2) {
        this.projectName = projectName2;
    }

    public String getViewName() {
        return this.viewName;
    }

    public void setViewName(String viewName2) {
        this.viewName = viewName2;
    }

    public int getPass() {
        return this.pass;
    }

    public void setPass(int pass2) {
        this.pass = pass2;
    }

    public int getFail() {
        return this.fail;
    }

    public void setFail(int fail2) {
        this.fail = fail2;
    }

    public int getSkip() {
        return this.skip;
    }

    public void setSkip(int skip2) {
        this.skip = skip2;
    }

    public String getBuildStartTime() {
        return this.buildStartTime;
    }

    public void setBuildStartTime(String buildStartTime2) {
        this.buildStartTime = buildStartTime2;
    }

    public String getBuildEndTime() {
        return this.buildEndTime;
    }

    public void setBuildEndTime(String buildEndTime2) {
        this.buildEndTime = buildEndTime2;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime2) {
        this.createTime = createTime2;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime2) {
        this.updateTime = updateTime2;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total2) {
        this.total = total2;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public void setModuleName(String moduleName2) {
        this.moduleName = moduleName2;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version2) {
        this.version = version2;
    }
}
