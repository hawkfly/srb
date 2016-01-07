package com.pansoft.jbsf.wbcs.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.pansoft.jbsf.context.SpringContext;
import com.wbcs.config.database.datasource.AbsDataSource;
import com.wbcs.exception.WbcsRuntimeException;

/**
 * @author hawkfly
 */
public class SpringDataSource extends AbsDataSource {
	
	private static Logger logger = Logger.getLogger(SpringDataSource.class);
	private DataSource ds;
	
	public SpringDataSource(){
		this.ds = (DataSource)SpringContext.getContext().getBean("dataSource");
		System.out.println("########"+ this.ds.toString()); 
	}

	/* (non-Javadoc)
	 * @see com.wbcs.config.database.datasource.AbsDataSource#getConnection()
	 */
	public Connection getConnection() {
		// TODO Auto-generated method stub
		try {
            logger.debug("从数据源" + this.getName() + "获取数据库连接");
            return this.ds.getConnection();
	    } catch (SQLException e) {
	            throw new WbcsRuntimeException("获取" + this.getName() + "数据源的数据库连接失败", e);
	    }
	}

	/* (non-Javadoc)
	 * @see com.wbcs.config.database.datasource.AbsDataSource#getDataSource()
	 */
	public DataSource getDataSource() {
		// TODO Auto-generated method stub
		return this.ds;
	}
}
