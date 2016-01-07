package com.pansoft.plugins.ds.c3p0;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.pansoft.plugins.PropertyUtil;

public class C3p0ConnectionManager {
	private static final Log log = LogFactory.getLog(C3p0ConnectionManager.class);
    private static ComboPooledDataSource ds = new ComboPooledDataSource();
    
    private C3p0ConnectionManager() {
        
    }
    
    static {
        try {
            if(log.isDebugEnabled()) {
                log.debug("正在初始化数据连接池");
            }
            String driver=PropertyUtil.getProperty("jdbc.driver");
            String url = PropertyUtil.getProperty("jdbc.url");
    		String user = PropertyUtil.getProperty("jdbc.user");
    		String password = PropertyUtil.getProperty("jdbc.password");
    		
            ds.setDriverClass(driver);
            ds.setJdbcUrl(url);
            ds.setUser(user);
            ds.setPassword(password);
            ds.setMaxPoolSize(10);
            ds.setMinPoolSize(1);
            if(log.isDebugEnabled()) {
                log.debug("数据连接池初始化完成" +
                		", 最大可用连接数：" +ds.getMaxPoolSize() + 
                        ", 最小连接数：" + ds.getMinPoolSize());
            }
        } catch (Exception e) {
            log.error("连接池初始化失败，原因：" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
    public static void main(String[] args) throws SQLException {
		System.out.println(getConnection());
	}
}
