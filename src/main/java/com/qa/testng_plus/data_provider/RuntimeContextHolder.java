package com.qa.testng_plus.data_provider;

import com.qa.testng_plus.common_utils.StringUtils;

public class RuntimeContextHolder {
    private static ThreadLocal<CaseContext> caseContextLocal = new ThreadLocal<>();

    public static void setCaseId(String caseId) {
        CaseContext caseContext = getCaseContext();
        if (caseContext == null) {
            caseContext = new CaseContext();
            setCaseContext(caseContext);
        }
        if (StringUtils.isNotBlank(caseId)) {
            caseContext.setCaseId(caseId);
        }
    }

    public static String getCaseId() {
        return getCaseContext() != null ? getCaseContext().getCaseId() : StringUtils.EMPTY_STRING;
    }

    public static void setDataProviderPath(String dataProviderPath) {
        CaseContext caseContext = getCaseContext();
        if (caseContext == null) {
            caseContext = new CaseContext();
            setCaseContext(caseContext);
        }
        caseContext.setDataProviderPath(dataProviderPath);
    }

    public static String getDataProviderPath() {
        if (getCaseContext() != null) {
            return getCaseContext().getDataProviderPath();
        }
        return null;
    }

    public static void setCaseContext(CaseContext context) {
        caseContextLocal.set(context);
    }

    public static CaseContext getCaseContext() {
        return caseContextLocal.get();
    }
}
