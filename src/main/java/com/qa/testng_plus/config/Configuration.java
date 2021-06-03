package com.qa.testng_plus.config;

import java.util.Map;

public interface Configuration {

    Map<String, String> getConfig();

    String getPropertyValue(String str);

    String getPropertyValue(String str, String str2);

    void setProperty(String str, String str2);
}
