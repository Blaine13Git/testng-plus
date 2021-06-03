package com.qa.testng_plus.csv_utils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import com.qa.testng_plus.common_utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.util.StringUtil;

public class CsvParser {
    private static final String DEFAULT_PATH = "/src/test/resources/";
    private static final Log LOG = LogFactory.getLog(CsvParser.class);

    public File getCsvFile(String csvPath) {
        return new File(System.getProperty("user.dir") + DEFAULT_PATH + csvPath);
    }

    public String getCsvFileName(Class<?> objClass, String csvPath) {
        String[] paths = csvPath.split("/");
        ArrayUtils.reverse(paths);
        String className = objClass.getSimpleName() + ".csv";
        if (!StringUtils.equals(className, paths[0])) {
            return StringUtil.replace(csvPath, paths[0], className);
        }
        return csvPath;
    }

    public void writeToCsv(File file, List<String[]> outputValues) throws Exception {
        try {
            try {
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
                try {
                    CSVWriter csvWriter = new CSVWriter(osw);
                    csvWriter.writeAll(outputValues);
                    csvWriter.close();
                } catch (Exception e) {
                    LOG.error("通过文件流输出数据失败", e);
                    throw e;
                }
            } catch (Exception e2) {
                LOG.error("通过文件流输出数据失败", e2);
                throw e2;
            }
        } catch (Exception var7) {
            LOG.error("写入文件【" + file.getName() + "】初始化失败", var7);
            throw var7;
        }
    }

    public List readFromCsv(File file) throws Exception {
        try {
            try {
                return new CSVReader(new InputStreamReader(new FileInputStream(file))).readAll();
            } catch (Exception var6) {
                LOG.error("通过CSV文件流读入数据失败", var6);
                throw var6;
            }
        } catch (Exception var7) {
            LOG.error("读入文件【" + file.getName() + "】初始化失败", var7);
            throw var7;
        }
    }
}
