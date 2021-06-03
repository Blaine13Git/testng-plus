package com.qa.testng_plus.data_provider;

import com.qa.testng_plus.common_utils.StringUtils;

public class CaseContext {
    private String caseId = StringUtils.EMPTY_STRING;
    private String dataProviderPath;

    public String getCaseId() {
        return this.caseId;
    }

    public void setCaseId(String caseId2) {
        this.caseId = caseId2;
    }

    public String getDataProviderPath() {
        return this.dataProviderPath;
    }

    public void setDataProviderPath(String dataProviderPath2) {
        this.dataProviderPath = dataProviderPath2;
    }

    public String toString() {
        return "CaseContext [caseId=" + this.caseId + ",dataProviderPath=" + this.dataProviderPath + "]";
    }

}