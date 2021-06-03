package com.qa.testng_plus.data_provider;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;

import com.qa.testng_plus.common_utils.StringUtils;
import com.qa.testng_plus.config.PropertyConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.DataProvider;

public class DataDriver extends AbstractTestNGSpringContextTests {
    private static final Log log = LogFactory.getLog(DriverDataProvider.class);

    @DataProvider(name = "CsvDataProvider")
    public Iterator<Object[]> getDataProvider(Method method) throws IOException {
        if (method.getAnnotation(TestData.class) == null) {
            return getDataProvider(method.getDeclaringClass(), method);
        }
        return getDataProvider(method.getDeclaringClass(), method, ((TestData) method.getAnnotation(TestData.class)).path() + ((TestData) method.getAnnotation(TestData.class)).fileName());
    }

    public Iterator<Object[]> getDataProvider(Class<?> cls, Method method) throws IOException {
        String className = cls.getSimpleName();
        DataModel dataModel = (DataModel) method.getAnnotation(DataModel.class);
        String fileName = StringUtils.EMPTY_STRING;
        String filePath = StringUtils.EMPTY_STRING;
        if (dataModel == null || dataModel.value() == 2) {
            fileName = className + "." + method.getName() + ".csv";
        } else if (dataModel.value() == 1) {
            fileName = className + ".csv";
        }
        String groups = PropertyConfig.getGroupName();
        if (StringUtils.isNotBlank(groups)) {
            String[] groupArr = groups.split(",");
            String[] var11 = groupArr;
            int var12 = groupArr.length;
            int var13 = 0;
            while (var13 < var12) {
                filePath = ("testdata" + "/" + var11[var13] + "/" + className) + "/" + fileName;
                try {
                    if (new File(cls.getClassLoader().getResource(filePath).getPath()).isFile()) {
                        break;
                    }
                    var13++;
                } catch (Exception e) {
                }
            }
        } else {
            filePath = ("testdata" + "/" + className) + "/" + fileName;
        }
        System.out.println("测试驱动数据: " + filePath);
        return new DriverDataProvider(cls, method, filePath);
    }

    public Iterator<Object[]> getDataProvider(Class<?> cls, Method method, String filePath) throws IOException {
        return new DriverDataProvider(cls, method, filePath);
    }
}
