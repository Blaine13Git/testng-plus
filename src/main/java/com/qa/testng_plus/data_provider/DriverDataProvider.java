package com.qa.testng_plus.data_provider;


import au.com.bytecode.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;

import com.qa.testng_plus.common_utils.DateUtil;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

public class DriverDataProvider implements Iterator<Object[]> {
    private String[] last;
    private Converter[] parameterConverters;
    private Class<?>[] parameterTypes;
    CSVReader reader = null;
    public int sum = 0;

    public DriverDataProvider(Class<?> cls, Method method, String csvFilePath) {
        String absoluteProviderFilePath = cls.getClassLoader().getResource(csvFilePath).getPath();
        InputStream is = cls.getClassLoader().getResourceAsStream(csvFilePath);
        RuntimeContextHolder.setDataProviderPath(absoluteProviderFilePath);
        try {
            this.reader = new CSVReader(new InputStreamReader(is), ',', '\"', 0);
            this.parameterTypes = method.getParameterTypes();
            int len = this.parameterTypes.length;
            this.parameterConverters = new Converter[len];
            for (int i = 0; i < len; i++) {
                this.parameterConverters[i] = ConvertUtils.lookup(this.parameterTypes[i]);
            }
        } catch (RuntimeException e) {
            System.out.println(cls.getName() + "." + method.getName() + " TestData is not exist!");
        }
    }

    public boolean hasNext() {
        try {
            if (this.reader == null) {
                return false;
            }
            this.last = this.reader.readNext();
            if (this.last != null) {
                return true;
            }
            return false;
        } catch (IOException e) {
            System.out.println("Read row data error!");
        }
        return false;
    }

    private String[] getNextLine() {
        if (this.last == null) {
            try {
                this.last = this.reader.readNext();
            } catch (IOException var2) {
                System.out.println("get next line error!");
                throw new RuntimeException(var2);
            }
        }
        return this.last;
    }

    public Object[] next() {
        String[] next;
        if (this.last != null) {
            next = this.last;
        } else {
            next = getNextLine();
        }
        this.last = null;
        return parseLine(next);
    }

    private Object[] parseLine(String[] svals) {
        if (svals.length != this.parameterTypes.length) {
            System.err.println("驱动数据个数 [" + svals.length + "] 与参数个数 [" + this.parameterTypes.length + "] 不相等 , " + svals[0]);
            return null;
        }
        int len = svals.length;
        if (len > 0) {
            RuntimeContextHolder.setCaseId(svals[0]);
        }
        Object[] result = new Object[len];
        System.out.println("=============== START [" + svals[0] + "] ===============");
        System.out.println("======> 测试数据 用例ID [" + svals[0] + "] <======");
        for (int i = 0; i < len; i++) {
            String curSval = svals[i];
            if (curSval.equals("null")) {
                result[i] = null;
            } else if (this.parameterTypes[i].getName().contains("Date")) {
                Date newDate = DateUtil.parseDateNewFormat(curSval);
                if (newDate != null) {
                    result[i] = newDate;
                } else {
                    result[i] = null;
                }
            } else {
                try {
                    if (this.parameterTypes[i].getName().contains("Integer")) {
                        Integer.valueOf(curSval);
                    }
                    if (this.parameterTypes[i].getName().contains("Long")) {
                        Long.valueOf(curSval);
                    }
                    if (this.parameterTypes[i].getName().contains("Float")) {
                        Float.valueOf(curSval);
                    }
                    if (this.parameterTypes[i].getName().contains("Double")) {
                        Double.valueOf(curSval);
                    }
                } catch (NumberFormatException e) {
                    result[i] = "null";
                }
                result[i] = this.parameterConverters[i].convert(this.parameterTypes[i], curSval);
            }
        }
        return result;
    }

    public void remove() {
    }

    public CSVReader getReader() {
        return this.reader;
    }
}
