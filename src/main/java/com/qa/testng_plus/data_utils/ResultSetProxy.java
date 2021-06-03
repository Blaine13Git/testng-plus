package com.qa.testng_plus.data_utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;

public class ResultSetProxy implements InvocationHandler {
    private DBConn conn;
    private Object proxy;
    private ResultSet resultSet;
    private Class<?> targetClass = ResultSet.class;

    public ResultSetProxy(ResultSet resultSet2, DBConn conn2) {
        this.resultSet = resultSet2;
        this.conn = conn2;
        this.proxy = Proxy.newProxyInstance(this.targetClass.getClassLoader(), new Class[]{this.targetClass}, this);
    }

    public ResultSet getProxy() {
        return (ResultSet) this.proxy;
    }

    public Object invoke(Object proxy2, Method method, Object[] args) throws Throwable {
        if (!"close".equals(method.getName())) {
            return method.invoke(this.resultSet, args);
        }
        if (this.conn != null) {
            this.conn.close();
        }
        return null;
    }
}