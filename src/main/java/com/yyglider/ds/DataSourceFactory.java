package com.yyglider.ds;

import javax.sql.DataSource;

/**
 * 数据源工厂
 * Created by yyglider on 2017/4/27.
 */
public interface DataSourceFactory {

    DataSource getDataSource();

}
