package com.qa.testng_plus.report_utils;

import com.qa.testng_plus.common_utils.DateUtil;
import com.qa.testng_plus.common_utils.QaTsDBUtils;
import com.qa.testng_plus.common_utils.StringUtils;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConfigReport implements IReporter {

    public void generateReport(List<XmlSuite> list, List<ISuite> suites, String outputDirectory) {
        ReportLogDO reportLogDO = new ReportLogDO();
        int pass = 0;
        int fail = 0;
        int skip = 0;
        String moduleName = StringUtils.EMPTY_STRING;
        for (ISuite suite : suites) {
            reportLogDO.setProjectName(suite.getName());
            for (ISuiteResult suiteResult : suite.getResults().values()) {
                ITestContext testContext = suiteResult.getTestContext();
                moduleName = testContext.getName();
                pass += testContext.getPassedTests().size();
                fail += testContext.getFailedTests().size();
                skip += testContext.getSkippedTests().size();
                if (reportLogDO.getBuildStartTime() == null) {
                    reportLogDO.setBuildStartTime(formatDate(testContext.getStartDate()));
                }
                reportLogDO.setBuildEndTime(formatDate(testContext.getEndDate()));
            }
        }
        String versionStr = QaTsDBUtils.selectStrDB("qacenter.test_result_log", "version", "project_name='" + reportLogDO.getProjectName() + "' AND module_name = '" + moduleName + "' order by create_time desc");
        int version = 0;
        if (StringUtils.isNotBlank(versionStr)) {
            version = Integer.parseInt(versionStr);
        }
        reportLogDO.setPass(pass);
        reportLogDO.setFail(fail);
        reportLogDO.setSkip(skip);
        reportLogDO.setTotal(pass + fail + skip);
        reportLogDO.setCreateTime(formatDate(new Date()));
        reportLogDO.setUpdateTime(formatDate(new Date()));
        reportLogDO.setModuleName(moduleName);
        reportLogDO.setVersion(version + 1);
        QaTsDBUtils.insertDB("qacenter.test_result_log", QaTsDBUtils.objectToMap(reportLogDO));
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat(DateUtil.simple).format(date);
    }

}
