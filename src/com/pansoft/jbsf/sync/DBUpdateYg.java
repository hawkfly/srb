package com.pansoft.jbsf.sync;

import java.sql.SQLException;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.IAtom;


public class DBUpdateYg{

	public void executeProcessService() throws Exception {
		
			boolean isSuccess = Db.tx(new IAtom(){
				public boolean run() throws SQLException{
					
					boolean c = updateYg();
					
					return c;
				}
			});
		
	}
	
	/**
	 * 更新员工
	 * 
	 * @param stmt
	 * @throws SQLException
	 */
	private boolean updateYg() throws SQLException {
		StringBuffer sb = new StringBuffer();
		
		sb.setLength(0);
		sb.append(" DELETE FROM SSF_USERS_TEMP");
		int i = Db.update(sb.toString());
		
		/**
		 * 1、把符合条件的用户插入临时表
		 */
		sb.setLength(0);
		sb.append(" INSERT INTO SSF_USERS_TEMP                                                    ");
		sb.append(" (USR_USRID,USR_TYPE,USR_CAPTION,USR_DESCRIPTION,USR_PASSWORD,                 ");
		sb.append(" USR_PASSWORDEXT,USR_ADMIN,USR_DISABLE,USR_SECRITY_MODE,USR_SECRITY_ID,        ");
		sb.append(" USR_SECRITY_PK_FILE,USR_SECRITY_PK_TEXT,USR_EMAIL1,USR_EMAIL2,USR_PHONE1,     ");
		sb.append(" USR_PHONE2,USR_ADDRESS1,USR_ADDRESS2,USR_ORGID,USR_ERPUSERID,                 ");
		sb.append(" USR_T01,USR_T02,USR_T03,USR_T04,USR_T05,                                      ");
		sb.append(" USR_T06,USR_T07,USR_T08,USR_T09,USR_T10,                                      ");
		sb.append(" USR_N01,USR_N02,USR_N03,USR_N04,USR_N05,                                      ");
		sb.append(" USR_N06,USR_N07,USR_N08,USR_N09,USR_N10,                                      ");
		sb.append(" USR_I01,USR_I02,USR_I03,USR_I04,USR_I05,                                      ");
		sb.append(" USR_I06,USR_I07,USR_I08,USR_I09,USR_I10,                                      ");
		sb.append(" F_LEVEL,F_LEAF,F_PARENT)                                                      ");
		sb.append(" SELECT USR_USRID,USR_TYPE,USR_CAPTION,USR_DESCRIPTION,USR_PASSWORD,           ");
		sb.append(" USR_PASSWORDEXT,USR_ADMIN,USR_DISABLE,USR_SECRITY_MODE,USR_SECRITY_ID,        ");
		sb.append(" USR_SECRITY_PK_FILE,USR_SECRITY_PK_TEXT,USR_EMAIL1,USR_EMAIL2,USR_PHONE1,     ");
		sb.append(" USR_PHONE2,USR_ADDRESS1,USR_ADDRESS2,USR_ORGID,USR_ERPUSERID,                 ");
		sb.append(" USR_T01,USR_T02,USR_T03,USR_T04,USR_T05,                                      ");
		sb.append(" USR_T06,USR_T07,USR_T08,USR_T09,USR_T10,                                      ");
		sb.append(" USR_N01,USR_N02,USR_N03,USR_N04,USR_N05,                                      ");
		sb.append(" USR_N06,USR_N07,USR_N08,USR_N09,USR_N10,                                      ");
		sb.append(" USR_I01,USR_I02,USR_I03,USR_I04,USR_I05,                                      ");
		sb.append(" USR_I06,USR_I07,USR_I08,USR_I09,USR_I10,                                      ");
		sb.append(" F_LEVEL,F_LEAF,F_PARENT                                                       ");
		sb.append(" FROM PANICS.SSF_USERS@GSNKLINK RS1                                            ");
//		sb.append(" WHERE NOT EXISTS (SELECT 1                                                    ");
//		sb.append(" FROM SSF_USERS_TEMP RS2                                                       ");
//		sb.append(" WHERE RS1.USR_USRID = RS2.USR_USRID)                                          ");
		sb.append(" WHERE (usr_orgid LIKE '101050%' OR usr_orgid LIKE '13%' OR usr_orgid LIKE '21%')");
		int j = Db.update(sb.toString());
		
		
		/**
		 * 2、把不存在的用户插入实表BSUSER
		 */
		
		sb.setLength(0);
		sb.append(" INSERT INTO BSUSER");
		sb.append(" (F_NAME,F_YGZT,F_EMAIL,F_TEL,F_PLACE,F_XMBH,F_SEX,F_SFZ,F_GZK,F_PASS,F_MANA,F_ISROLE,F_ZGBH,F_LSJG,F_SFYG)");
		sb.append(" SELECT RS.USR_CAPTION,RS.USR_DISABLE,RS.USR_EMAIL1,RS.USR_PHONE1,RS.USR_ADDRESS2,RS.USR_T03,RS.USR_T05,RS.USR_T06,RS.USR_T07,RS.USR_PASSWORD,'0','0',RS.USR_USRID,");
		sb.append(" CASE WHEN USR_ORGID LIKE '101050%' THEN  '1010'");
		sb.append(" WHEN USR_ORGID LIKE '13%' THEN '1020'");
		sb.append(" WHEN USR_ORGID LIKE '21%' THEN  '1030'");
		sb.append(" END F_LSJG,'1'");
		sb.append(" FROM SSF_USERS_TEMP RS");
		sb.append(" WHERE NOT EXISTS (SELECT 1");
		sb.append(" FROM BSUSER ER");
		sb.append(" WHERE ER.F_ZGBH = RS.USR_USRID)");
		int a = Db.update(sb.toString());
		

		/**
		 * 3、// 调到别的部门的员工按离职处理
		 */
		
		sb.setLength(0);
		sb.append(" UPDATE BSUSER ER");
		sb.append(" SET F_YGZT = '1'");
		sb.append(" WHERE NOT EXISTS (SELECT 1");
		sb.append(" FROM SSF_USERS_TEMP RS");
		sb.append(" WHERE ER.F_ZGBH = RS.USR_USRID)");
		sb.append(" AND ER.F_SFYG = '1'");
		int c = Db.update(sb.toString());
		
		sb.setLength(0);
		sb.append(" UPDATE BSUSER ER");
		sb.append(" SET (ER.F_TEL,ER.F_XMBH,ER.F_GZK,ER.F_PASS,F_YGZT)");
		sb.append("  = ");
		sb.append(" (SELECT RS.USR_PHONE1,RS.USR_T03,RS.USR_T07,RS.USR_PASSWORD,RS.USR_DISABLE");
		sb.append(" FROM SSF_USERS_TEMP RS");
		sb.append(" WHERE ER.F_ZGBH = RS.USR_USRID)");
		sb.append(" WHERE EXISTS (SELECT 1");
		sb.append(" FROM SSF_USERS_TEMP RS");
		sb.append(" WHERE ER.F_ZGBH = RS.USR_USRID)");
		int b = Db.update(sb.toString());
		
	
		
		// 默认项目为已关闭的项目删掉
		sb.setLength(0);
		sb.append(" UPDATE BSUSER ER");
		sb.append(" SET ER.F_XMBH = '',ER.F_XMMC = ''");
		sb.append(" WHERE EXISTS (SELECT 1");
		sb.append(" FROM IC_PROJECT_STO STO");
		sb.append(" WHERE ER.F_XMBH = STO.F_PROJECT_ID");
		sb.append(" AND NVL(F_PROJECT_STATUS,' ') = '4')");
		sb.append(" AND ER.F_SFYG = '1'");
		sb.append(" AND TRIM(ER.F_XMBH) IS NOT NULL");
		int e = Db.update(sb.toString());
		
		// 每个用户，如果默认项目在IC_PROJECT_USER不存在，清除默认项目
		sb.setLength(0);
		sb.append(" UPDATE BSUSER ER1");
		sb.append(" SET F_XMBH = '',F_XMMC = ''");
		sb.append(" WHERE NOT EXISTS (SELECT 1");
		sb.append(" FROM IC_PROJECT_USER ER2");
		sb.append(" WHERE ER1.F_XMBH = ER2.F_PROJECT_ID");
		sb.append(" AND ER1.F_ZGBH = ER2.F_USER_ID)");
		sb.append(" AND ER1.F_SFYG = '1'");
		sb.append(" AND TRIM(ER1.F_XMBH) IS NOT NULL");
		int f = Db.update(sb.toString());
		
	   /**
	    * 同步至微信号
	    */
		
		
//		//更新jbsf_user
//		sb.setLength(0);
//		sb.append(" insert into jbsf_user");
//		sb.append(" (id,username,password,email,islock,realname)");
//		sb.append(" select '0'||F_ZGBH,F_ZGBH,F_PASS,F_EMAIL,F_YGZT,F_NAME");
//		sb.append(" from bsuser bs ");
//		sb.append(" where not exists (select 1");
//		sb.append(" from jbsf_user jb ");
//		sb.append(" where jb.username=bs.f_zgbh)");
//		int g=Db.update(sb.toString());
//		
//		
//		sb.setLength(0);
//		sb.append(" update jbsf_user jb");
//		sb.append(" set (jb.password,jb.islock) ");
//		sb.append(" = ");
//		sb.append(" (select bs.f_pass,bs.f_ygzt");
//		sb.append(" from bsuser bs ");
//		sb.append(" where jb.username=bs.f_zgbh)");
//		sb.append(" where exists ( select 1");
//		sb.append(" from bsuser bs ");
//		sb.append(" where jb.username=bs.f_zgbh)");
//		int h=Db.update(sb.toString());
	
		
		if(j>=0&&i>=0&&a>=0&&b>=0&&c>=0&&e>=0&&f>=0){
			return true;
		}
		return false;
	}
	

	
}
