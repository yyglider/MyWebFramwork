package com.yyglider.ds.impl;

import com.yyglider.core.ConfigHelper;
import com.yyglider.ds.DataSourceFactory;

import javax.sql.DataSource;

/**
 * Created by yyglider on 2017/4/27.
 */
public abstract class AbstractDataSourceFactory<T extends DataSource> implements DataSourceFactory {

    protected final String driver = ConfigHelper.getString("jdbc.driver");
    protected final String url = ConfigHelper.getString("jdbc.url");
    protected final String username = ConfigHelper.getString("jdbc.username");
    protected final String password = ConfigHelper.getString("jdbc.password");

    @Override
    public final T getDataSource() {
        // 创建数据源对象
        T ds = createDataSource();
        // 设置基础属性
        setDriver(ds, driver);
        setUrl(ds, url);
        setUsername(ds, username);
        setPassword(ds, password);
        // 设置高级属性
        setAdvancedConfig(ds);
        return ds;
    }

    public abstract T createDataSource();

    public abstract void setDriver(T ds, String driver);

    public abstract void setUrl(T ds, String url);

    public abstract void setUsername(T ds, String username);

    public abstract void setPassword(T ds, String password);

    public abstract void setAdvancedConfig(T ds);
}
