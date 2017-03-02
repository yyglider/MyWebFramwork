package com.yyglider.mvc.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装视图对象
 */
public class View{

    private String path;              // 视图路径
    private Map<String, Object> attribution; // 相关属性

    public View(String path) {
        this.path = path;
        attribution = new HashMap<String, Object>();
    }

    public View data(String key, Object value) {
        attribution.put(key, value);
        return this;
    }

    public boolean isRedirect() {
        return path.startsWith("/");
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getAttribution() {
        return attribution;
    }

    public void setAttribution(Map<String, Object> attribution) {
        this.attribution = attribution;
    }
}
