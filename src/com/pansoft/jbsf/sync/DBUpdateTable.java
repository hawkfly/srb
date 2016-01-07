package com.pansoft.jbsf.sync;

import java.sql.SQLException;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.IAtom;


public class DBUpdateTable{

	public void executeProcessService() throws Exception {
		
			boolean isSuccess = Db.tx(new IAtom(){
				public boolean run() throws SQLException{
					boolean a = updateCity();
					boolean b = updateZzjg();
					boolean d = updateProject();
					boolean e = updateXmyg();
					boolean f = updateQx();
					return a&&b&&d&&e&&f;
				}
			});
		
	}
	
	/**
	 * 更新地点
	 * 
	 * @param stmt
	 * @throws SQLException
	 */
	private boolean updateCity () throws SQLException{
		
		StringBuffer sb = new StringBuffer();
		sb.setLength(0);
		sb.append(" INSERT INTO IC_CITY_TEMP(F_ID,F_NAME,F_DISABLE)");
		sb.append(" SELECT F_ID,F_NAME,F_DISABLE                   ");
		sb.append(" FROM PANICS.IC_CITY@GSNKLINK C1                ");   /////////////
		sb.append(" WHERE NOT EXISTS (SELECT 1                     ");
		sb.append(" FROM IC_CITY_TEMP C2                           ");
		sb.append(" WHERE C1.F_ID = C2.F_ID)                       ");
		//Db.update(sb.toString());
		int j = Db.update(sb.toString());
		
		sb.setLength(0);
		sb.append(" INSERT INTO IC_CITY(F_ID,F_NAME,F_DISABLE)     ");
		sb.append(" SELECT F_ID,F_NAME,'1'                         ");
		sb.append(" FROM IC_CITY_TEMP C1                           ");
		sb.append(" WHERE NOT EXISTS (SELECT 1                     ");
		sb.append(" FROM IC_CITY C2                                ");
		sb.append(" WHERE C1.F_ID = C2.F_ID)                       ");
		
		int i = Db.update(sb.toString());
		
		if(j>=0&&i>=0){
			return true;
		}
		return false;
		
	}
		//	/**
//	 * 更新工程
//	 * 
//	 * @param stmt
//	 * @throws SQLException
//	 */
//	private void updateProject(Statement stmt) throws SQLException {
//		StringBuffer sb = new StringBuffer();
//		
//		sb.setLength(0);
//		sb.append(" INSERT INTO IC_PROJECT_STO_TEMP                                           ");
//		sb.append(" (F_PROJECT_ID,F_PROJECT_NAME,F_MANAGER,F_COMPANY,F_ORG,                   ");
//		sb.append(" F_START_TIME,F_END_TIME,F_DESC,F_TOTAL_AMT,F_PROJECT_BASIS,               ");
//		sb.append(" F_FINANCE_TYPE,F_CUSTOMER_TYPE,F_PROJECT_KIND,F_LINK_PRODUCT,F_CLOSE_TIME,");
//		sb.append(" F_CLOSE_USER,F_OPEN_TIME,F_OPEN_USER,F_CONFIRMED_AMT,F_INIT_CONFIRM_AMT,  ");
//		sb.append(" F_INVOICE_AMT,F_INIT_INVOICE_AMT,F_PAY_AMT,F_INIT_PAY_AMT,F_CONTRACT_AMT, ");
//		sb.append(" F_CUSTOMER_ID,F_PLAN_TIME,F_ACTUAL_TIME,F_ACCEPT_DATE,F_FMIS_PRJNO,       ");
//		sb.append(" F_PROJECT_STATUS,F_CRTTIME,F_CRTUSER,F_UPTIME,F_UPUSER,                   ");
//		sb.append(" F_VCHRSTATUS,F_ADDED,F_RD,F_CLOSE_REASON,F_APPROVE_STATUS,                ");
//		sb.append(" F_ISPUB,F_SMALLLIMIT)                                                     ");
//		sb.append(" SELECT                                                                    ");
//		sb.append(" F_PROJECT_ID,F_PROJECT_NAME,F_MANAGER,F_COMPANY,F_ORG,                    ");
//		sb.append(" F_START_TIME,F_END_TIME,F_DESC,F_TOTAL_AMT,F_PROJECT_BASIS,               ");
//		sb.append(" F_FINANCE_TYPE,F_CUSTOMER_TYPE,F_PROJECT_KIND,F_LINK_PRODUCT,F_CLOSE_TIME,");
//		sb.append(" F_CLOSE_USER,F_OPEN_TIME,F_OPEN_USER,F_CONFIRMED_AMT,F_INIT_CONFIRM_AMT,  ");
//		sb.append(" F_INVOICE_AMT,F_INIT_INVOICE_AMT,F_PAY_AMT,F_INIT_PAY_AMT,F_CONTRACT_AMT, ");
//		sb.append(" F_CUSTOMER_ID,F_PLAN_TIME,F_ACTUAL_TIME,F_ACCEPT_DATE,F_FMIS_PRJNO,       ");
//		sb.append(" F_PROJECT_STATUS,F_CRTTIME,F_CRTUSER,F_UPTIME,F_UPUSER,                   ");
//		sb.append(" F_VCHRSTATUS,F_ADDED,F_RD,F_CLOSE_REASON,F_APPROVE_STATUS,                ");
//		sb.append(" F_ISPUB,F_SMALLLIMIT                                                      ");
//		sb.append(" FROM PANICS.IC_PROJECT_STO@GSNKLINK PR1                                   ");
//		sb.append(" WHERE (F_ORG LIKE '101050%' OR F_ORG LIKE '13%' OR F_ORG LIKE '21%')      ");
//		sb.append(" AND NVL(F_VCHRSTATUS,' ') = '3'                                           ");
//		sb.append(" AND NOT EXISTS (SELECT 1                                                  ");
//		sb.append(" FROM IC_PROJECT_STO_TEMP PR2                                              ");
//		sb.append(" WHERE PR1.F_PROJECT_ID = PR2.F_PROJECT_ID)                                ");
//		Db.update(sb.toString());
//		
//		sb.setLength(0);
//		sb.append(" UPDATE IC_PROJECT_STO_TEMP TEMP                                           ");
//		sb.append(" SET (F_VCHRSTATUS,F_MANAGER,F_PROJECT_STATUS,F_PROJECT_NAME) = (SELECT F_VCHRSTATUS,F_MANAGER,F_PROJECT_STATUS,F_PROJECT_NAME");
//		sb.append(" FROM PANICS.IC_PROJECT_STO@GSNKLINK STO                                   ");
//		sb.append(" WHERE TEMP.F_PROJECT_ID = STO.F_PROJECT_ID)                               ");
//		sb.append(" WHERE EXISTS (SELECT 1                                                    ");
//		sb.append(" FROM PANICS.IC_PROJECT_STO@GSNKLINK STO                                   ");
//		sb.append(" WHERE TEMP.F_PROJECT_ID = STO.F_PROJECT_ID)                               ");
//		Db.update(sb.toString());
//		
//		sb.setLength(0);
//		sb.append(" INSERT INTO IC_PROJECT_STO                                                ");
//		sb.append(" (F_PROJECT_ID,F_PROJECT_NAME,F_MANAGER,F_COMPANY,F_ORG,                   ");
//		sb.append(" F_START_TIME,F_END_TIME,F_DESC,F_TOTAL_AMT,F_PROJECT_BASIS,               ");
//		sb.append(" F_FINANCE_TYPE,F_CUSTOMER_TYPE,F_PROJECT_KIND,F_LINK_PRODUCT,F_CLOSE_TIME,");
//		sb.append(" F_CLOSE_USER,F_OPEN_TIME,F_OPEN_USER,F_CONFIRMED_AMT,F_INIT_CONFIRM_AMT,  ");
//		sb.append(" F_INVOICE_AMT,F_INIT_INVOICE_AMT,F_PAY_AMT,F_INIT_PAY_AMT,F_CONTRACT_AMT, ");
//		sb.append(" F_CUSTOMER_ID,F_PLAN_TIME,F_ACTUAL_TIME,F_ACCEPT_DATE,F_FMIS_PRJNO,       ");
//		sb.append(" F_PROJECT_STATUS,F_CRTTIME,F_CRTUSER,F_UPTIME,F_UPUSER,                   ");
//		sb.append(" F_VCHRSTATUS,F_ADDED,F_RD,F_CLOSE_REASON,F_APPROVE_STATUS,                ");
//		sb.append(" F_ISPUB,F_SMALLLIMIT,F_SFBZXMQB)                                                     ");
//		sb.append(" SELECT                                                                    ");
//		sb.append(" F_PROJECT_ID,F_PROJECT_NAME,F_MANAGER,F_COMPANY,F_ORG,                    ");
//		sb.append(" F_START_TIME,F_END_TIME,F_DESC,F_TOTAL_AMT,F_PROJECT_BASIS,               ");
//		sb.append(" F_FINANCE_TYPE,F_CUSTOMER_TYPE,F_PROJECT_KIND,F_LINK_PRODUCT,F_CLOSE_TIME,");
//		sb.append(" F_CLOSE_USER,F_OPEN_TIME,F_OPEN_USER,F_CONFIRMED_AMT,F_INIT_CONFIRM_AMT,  ");
//		sb.append(" F_INVOICE_AMT,F_INIT_INVOICE_AMT,F_PAY_AMT,F_INIT_PAY_AMT,F_CONTRACT_AMT, ");
//		sb.append(" F_CUSTOMER_ID,F_PLAN_TIME,F_ACTUAL_TIME,F_ACCEPT_DATE,F_FMIS_PRJNO,       ");
//		sb.append(" F_PROJECT_STATUS,F_CRTTIME,F_CRTUSER,F_UPTIME,F_UPUSER,                   ");
//		sb.append(" F_VCHRSTATUS,F_ADDED,F_RD,F_CLOSE_REASON,F_APPROVE_STATUS,                ");
//		sb.append(" F_ISPUB,F_SMALLLIMIT,'1'                                                      ");
//		sb.append(" FROM IC_PROJECT_STO_TEMP PR1                                              ");
//		sb.append(" WHERE NOT EXISTS (SELECT 1                                                ");
//		sb.append(" FROM IC_PROJECT_STO PR2                                                   ");
//		sb.append(" WHERE PR1.F_PROJECT_ID = PR2.F_PROJECT_ID)                                ");
//		Db.update(sb.toString());
//		
//		sb.setLength(0);
//		sb.append(" UPDATE IC_PROJECT_STO STO                                                 ");
//		sb.append(" SET (F_VCHRSTATUS,F_MANAGER,F_PROJECT_STATUS,F_PROJECT_NAME) = (SELECT F_VCHRSTATUS,F_MANAGER,F_PROJECT_STATUS,F_PROJECT_NAME");
//		sb.append(" FROM IC_PROJECT_STO_TEMP TEMP                                             ");
//		sb.append(" WHERE STO.F_PROJECT_ID = TEMP.F_PROJECT_ID)                               ");
//		sb.append(" WHERE EXISTS (SELECT 1                                                    ");
//		sb.append(" FROM IC_PROJECT_STO_TEMP TEMP                                             ");
//		sb.append(" WHERE STO.F_PROJECT_ID = TEMP.F_PROJECT_ID)                               ");
//		Db.update(sb.toString());
//		
//		
//	}
	
	/**
	 * 更新工程
	 * 
	 * @param stmt
	 * @throws SQLException
	 */
	private boolean updateProject() throws SQLException {
		StringBuffer sb = new StringBuffer();
		
		sb.setLength(0);
		sb.append(" DELETE FROM IC_PROJECT_STO_TEMP");
		int i = Db.update(sb.toString());
		
		sb.setLength(0);
		sb.append(" INSERT INTO IC_PROJECT_STO_TEMP                                           ");
		sb.append(" (F_PROJECT_ID,F_PROJECT_NAME,F_MANAGER,F_COMPANY,F_ORG,                   ");
		sb.append(" F_START_TIME,F_END_TIME,F_DESC,F_TOTAL_AMT,F_PROJECT_BASIS,               ");
		sb.append(" F_FINANCE_TYPE,F_CUSTOMER_TYPE,F_PROJECT_KIND,F_LINK_PRODUCT,F_CLOSE_TIME,");
		sb.append(" F_CLOSE_USER,F_OPEN_TIME,F_OPEN_USER,F_CONFIRMED_AMT,F_INIT_CONFIRM_AMT,  ");
		sb.append(" F_INVOICE_AMT,F_INIT_INVOICE_AMT,F_PAY_AMT,F_INIT_PAY_AMT,F_CONTRACT_AMT, ");
		sb.append(" F_CUSTOMER_ID,F_PLAN_TIME,F_ACTUAL_TIME,F_ACCEPT_DATE,F_FMIS_PRJNO,       ");
		sb.append(" F_PROJECT_STATUS,F_CRTTIME,F_CRTUSER,F_UPTIME,F_UPUSER,                   ");
		sb.append(" F_VCHRSTATUS,F_ADDED,F_RD,F_CLOSE_REASON,F_APPROVE_STATUS,                ");
		sb.append(" F_ISPUB,F_SMALLLIMIT)                                                     ");
		sb.append(" SELECT                                                                    ");
		sb.append(" F_PROJECT_ID,F_PROJECT_NAME,F_MANAGER,F_COMPANY,F_ORG,                    ");
		sb.append(" F_START_TIME,F_END_TIME,F_DESC,F_TOTAL_AMT,F_PROJECT_BASIS,               ");
		sb.append(" F_FINANCE_TYPE,F_CUSTOMER_TYPE,F_PROJECT_KIND,F_LINK_PRODUCT,F_CLOSE_TIME,");
		sb.append(" F_CLOSE_USER,F_OPEN_TIME,F_OPEN_USER,F_CONFIRMED_AMT,F_INIT_CONFIRM_AMT,  ");
		sb.append(" F_INVOICE_AMT,F_INIT_INVOICE_AMT,F_PAY_AMT,F_INIT_PAY_AMT,F_CONTRACT_AMT, ");
		sb.append(" F_CUSTOMER_ID,F_PLAN_TIME,F_ACTUAL_TIME,F_ACCEPT_DATE,F_FMIS_PRJNO,       ");
		sb.append(" F_PROJECT_STATUS,F_CRTTIME,F_CRTUSER,F_UPTIME,F_UPUSER,                   ");
		sb.append(" F_VCHRSTATUS,F_ADDED,F_RD,F_CLOSE_REASON,F_APPROVE_STATUS,                ");
		sb.append(" F_ISPUB,F_SMALLLIMIT                                                      ");
		sb.append(" FROM PANICS.IC_PROJECT_STO@GSNKLINK PR1                                   ");  ////////////
		sb.append(" WHERE (F_ORG LIKE '101050%' OR F_ORG LIKE '13%' OR F_ORG LIKE '21%')      ");
		sb.append(" AND NVL(F_VCHRSTATUS,' ') = '3'                                           ");    
//		sb.append(" AND NOT EXISTS (SELECT 1                                                  ");
//		sb.append(" FROM IC_PROJECT_STO_TEMP PR2                                              ");
//		sb.append(" WHERE PR1.F_PROJECT_ID = PR2.F_PROJECT_ID)                                ");
		int j = Db.update(sb.toString());
		
//		sb.setLength(0);
//		sb.append(" UPDATE IC_PROJECT_STO_TEMP TEMP                                           ");
//		sb.append(" SET (F_VCHRSTATUS,F_MANAGER,F_PROJECT_STATUS,F_PROJECT_NAME) = (SELECT F_VCHRSTATUS,F_MANAGER,F_PROJECT_STATUS,F_PROJECT_NAME");
//		sb.append(" FROM PANICS.IC_PROJECT_STO@GSNKLINK STO                                   ");
//		sb.append(" WHERE TEMP.F_PROJECT_ID = STO.F_PROJECT_ID)                               ");
//		sb.append(" WHERE EXISTS (SELECT 1                                                    ");
//		sb.append(" FROM PANICS.IC_PROJECT_STO@GSNKLINK STO                                   ");
//		sb.append(" WHERE TEMP.F_PROJECT_ID = STO.F_PROJECT_ID)                               ");
//		Db.update(sb.toString());
		
		sb.setLength(0);
		sb.append(" INSERT INTO IC_PROJECT_STO                                                ");
		sb.append(" (F_PROJECT_ID,F_PROJECT_NAME,F_MANAGER,F_COMPANY,F_ORG,                   ");
		sb.append(" F_START_TIME,F_END_TIME,F_DESC,F_TOTAL_AMT,F_PROJECT_BASIS,               ");
		sb.append(" F_FINANCE_TYPE,F_CUSTOMER_TYPE,F_PROJECT_KIND,F_LINK_PRODUCT,F_CLOSE_TIME,");
		sb.append(" F_CLOSE_USER,F_OPEN_TIME,F_OPEN_USER,F_CONFIRMED_AMT,F_INIT_CONFIRM_AMT,  ");
		sb.append(" F_INVOICE_AMT,F_INIT_INVOICE_AMT,F_PAY_AMT,F_INIT_PAY_AMT,F_CONTRACT_AMT, ");
		sb.append(" F_CUSTOMER_ID,F_PLAN_TIME,F_ACTUAL_TIME,F_ACCEPT_DATE,F_FMIS_PRJNO,       ");
		sb.append(" F_PROJECT_STATUS,F_CRTTIME,F_CRTUSER,F_UPTIME,F_UPUSER,                   ");
		sb.append(" F_VCHRSTATUS,F_ADDED,F_RD,F_CLOSE_REASON,F_APPROVE_STATUS,                ");
		sb.append(" F_ISPUB,F_SMALLLIMIT,F_SFBZXMQB)                                                     ");
		sb.append(" SELECT                                                                    ");
		sb.append(" F_PROJECT_ID,F_PROJECT_NAME,F_MANAGER,F_COMPANY,F_ORG,                    ");
		sb.append(" F_START_TIME,F_END_TIME,F_DESC,F_TOTAL_AMT,F_PROJECT_BASIS,               ");
		sb.append(" F_FINANCE_TYPE,F_CUSTOMER_TYPE,F_PROJECT_KIND,F_LINK_PRODUCT,F_CLOSE_TIME,");
		sb.append(" F_CLOSE_USER,F_OPEN_TIME,F_OPEN_USER,F_CONFIRMED_AMT,F_INIT_CONFIRM_AMT,  ");
		sb.append(" F_INVOICE_AMT,F_INIT_INVOICE_AMT,F_PAY_AMT,F_INIT_PAY_AMT,F_CONTRACT_AMT, ");
		sb.append(" F_CUSTOMER_ID,F_PLAN_TIME,F_ACTUAL_TIME,F_ACCEPT_DATE,F_FMIS_PRJNO,       ");
		sb.append(" F_PROJECT_STATUS,F_CRTTIME,F_CRTUSER,F_UPTIME,F_UPUSER,                   ");
		sb.append(" F_VCHRSTATUS,F_ADDED,F_RD,F_CLOSE_REASON,F_APPROVE_STATUS,                ");
		sb.append(" F_ISPUB,F_SMALLLIMIT,'1'                                                      ");
		sb.append(" FROM IC_PROJECT_STO_TEMP PR1                                              ");
		sb.append(" WHERE NOT EXISTS (SELECT 1                                                ");
		sb.append(" FROM IC_PROJECT_STO PR2                                                   ");
		sb.append(" WHERE PR1.F_PROJECT_ID = PR2.F_PROJECT_ID)                                ");
		int a = Db.update(sb.toString());
		
		sb.setLength(0);
		sb.append(" UPDATE IC_PROJECT_STO STO                                                 ");
		sb.append(" SET (F_VCHRSTATUS,F_MANAGER,F_PROJECT_STATUS,F_PROJECT_NAME) = (SELECT F_VCHRSTATUS,F_MANAGER,F_PROJECT_STATUS,F_PROJECT_NAME");
		sb.append(" FROM IC_PROJECT_STO_TEMP TEMP                                             ");
		sb.append(" WHERE STO.F_PROJECT_ID = TEMP.F_PROJECT_ID)                               ");
		sb.append(" WHERE EXISTS (SELECT 1                                                    ");
		sb.append(" FROM IC_PROJECT_STO_TEMP TEMP                                             ");
		sb.append(" WHERE STO.F_PROJECT_ID = TEMP.F_PROJECT_ID)                               ");
		int b = Db.update(sb.toString());

		
		sb.setLength(0);
		sb.append(" UPDATE BSUSER ER                         ");     //////////////////////////////
		sb.append(" SET ER.F_XMMC = (SELECT ST.F_PROJECT_NAME");
		sb.append(" FROM IC_PROJECT_STO ST                   ");
		sb.append(" WHERE ER.F_XMBH = ST.F_PROJECT_ID)       ");
		sb.append(" WHERE EXISTS (SELECT 1                   ");
		sb.append(" FROM IC_PROJECT_STO ST                   ");
		sb.append(" WHERE ER.F_XMBH = ST.F_PROJECT_ID)       ");
		sb.append(" AND ER.F_SFYG = '1'                      ");
		sb.append(" AND TRIM(ER.F_XMBH) IS NOT NULL          ");
		int d = Db.update(sb.toString());
		
		if(j>=0&&i>=0&&a>=0&&b>=0&&d>=0){
			return true;
		}
		return false;
		
	}
	
	/**
	 * 更新组织机构
	 * 
	 * @param stmt
	 * @throws SQLException
	 */
	private boolean updateZzjg(){
		StringBuffer sb = new StringBuffer();
		
		sb.setLength(0);
		sb.append(" INSERT INTO BF_ORG_TEMP                     ");
		sb.append(" (F_ID,F_KEY,F_CAPTION,F_TITLE,F_LEVEL,      ");
		sb.append(" F_LEAF,F_PARENT,F_ADDRESS,F_COUNTRY,F_PROV, ");
		sb.append(" F_CITY,F_AREA,F_STREET,F_BUILDING,F_HOUSE,  ");
		sb.append(" F_EMAIL,F_OFFICER,F_TEL,F_POSTCODE,F_STID1, ");
		sb.append(" F_STID2,F_STID3,F_STID4,F_CHR0,F_CHR1,      ");
		sb.append(" F_CHR2,F_CHR3,F_CHR4,F_CHR5,F_CHR6,         ");
		sb.append(" F_CHR7,F_CHR8,F_CHR9,F_NUM0,F_NUM1,         ");
		sb.append(" F_NUM2,F_NUM3,F_NUM4,F_NUM5,F_NUM6,         ");
		sb.append(" F_NUM7,F_NUM8,F_NUM9,F_UPTIME,F_STATUS,     ");
		sb.append(" F_DISABLE)                                  ");
		sb.append(" SELECT                                      ");
		sb.append(" F_ID,F_KEY,F_CAPTION,F_TITLE,F_LEVEL,       ");
		sb.append(" F_LEAF,F_PARENT,F_ADDRESS,F_COUNTRY,F_PROV, ");
		sb.append(" F_CITY,F_AREA,F_STREET,F_BUILDING,F_HOUSE,  ");
		sb.append(" F_EMAIL,F_OFFICER,F_TEL,F_POSTCODE,F_STID1, ");
		sb.append(" F_STID2,F_STID3,F_STID4,F_CHR0,F_CHR1,      ");
		sb.append(" F_CHR2,F_CHR3,F_CHR4,F_CHR5,F_CHR6,         ");
		sb.append(" F_CHR7,F_CHR8,F_CHR9,F_NUM0,F_NUM1,         ");
		sb.append(" F_NUM2,F_NUM3,F_NUM4,F_NUM5,F_NUM6,         ");
		sb.append(" F_NUM7,F_NUM8,F_NUM9,F_UPTIME,F_STATUS,     ");
		sb.append(" F_DISABLE                                   ");
		sb.append(" FROM PANICS.BF_ORG@GSNKLINK ORG1            ");
		sb.append(" WHERE NOT EXISTS (SELECT 1                  ");
		sb.append(" FROM BF_ORG_TEMP ORG2                       ");
		sb.append(" WHERE ORG1.F_ID = ORG2.F_ID)                ");
		int i = Db.update(sb.toString());
		
		sb.setLength(0);
		sb.append(" INSERT INTO BF_ORG                          ");
		sb.append(" (F_ID,F_KEY,F_CAPTION,F_TITLE,F_LEVEL,      ");
		sb.append(" F_LEAF,F_PARENT,F_ADDRESS,F_COUNTRY,F_PROV, ");
		sb.append(" F_CITY,F_AREA,F_STREET,F_BUILDING,F_HOUSE,  ");
		sb.append(" F_EMAIL,F_OFFICER,F_TEL,F_POSTCODE,F_STID1, ");
		sb.append(" F_STID2,F_STID3,F_STID4,F_CHR0,F_CHR1,      ");
		sb.append(" F_CHR2,F_CHR3,F_CHR4,F_CHR5,F_CHR6,         ");
		sb.append(" F_CHR7,F_CHR8,F_CHR9,F_NUM0,F_NUM1,         ");
		sb.append(" F_NUM2,F_NUM3,F_NUM4,F_NUM5,F_NUM6,         ");
		sb.append(" F_NUM7,F_NUM8,F_NUM9,F_UPTIME,F_STATUS,     ");
		sb.append(" F_DISABLE)                                  ");
		sb.append(" SELECT                                      ");
		sb.append(" F_ID,F_KEY,F_CAPTION,F_TITLE,F_LEVEL,       ");
		sb.append(" F_LEAF,F_PARENT,F_ADDRESS,F_COUNTRY,F_PROV, ");
		sb.append(" F_CITY,F_AREA,F_STREET,F_BUILDING,F_HOUSE,  ");
		sb.append(" F_EMAIL,F_OFFICER,F_TEL,F_POSTCODE,F_STID1, ");
		sb.append(" F_STID2,F_STID3,F_STID4,F_CHR0,F_CHR1,      ");
		sb.append(" F_CHR2,F_CHR3,F_CHR4,F_CHR5,F_CHR6,         ");
		sb.append(" F_CHR7,F_CHR8,F_CHR9,F_NUM0,F_NUM1,         ");
		sb.append(" F_NUM2,F_NUM3,F_NUM4,F_NUM5,F_NUM6,         ");
		sb.append(" F_NUM7,F_NUM8,F_NUM9,F_UPTIME,F_STATUS,     ");
		sb.append(" F_DISABLE                                   ");
		sb.append(" FROM BF_ORG_TEMP ORG1                       ");
		sb.append(" WHERE NOT EXISTS (SELECT 1                  ");
		sb.append(" FROM BF_ORG ORG2                            ");
		sb.append(" WHERE ORG1.F_ID = ORG2.F_ID)                ");
		int j = Db.update(sb.toString());
		
		if(j>=0&&i>=0){
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * 更新项目员工
	 * 
	 * @param stmt
	 * @throws SQLException
	 */
	private boolean updateXmyg() throws SQLException {
		StringBuffer sb = new StringBuffer();
		
		sb.setLength(0);
		sb.append(" DELETE FROM IC_PROJECT_USER  WHERE F_USER_ID != '9999'");
		int i = Db.update(sb.toString());
		
		sb.setLength(0);
		sb.append(" INSERT INTO IC_PROJECT_USER                          ");
		sb.append(" (F_PROJECT_ID,F_USER_ID,F_PROJECT_ROLE,F_ATTEND_TIME)     ");
		sb.append(" SELECT F_PROJECT_ID,F_USER_ID,F_PROJECT_ROLE,F_ATTEND_TIME"); 
		sb.append(" FROM PANICS.IC_PROJECT_USER@GSNKLINK ER1                  ");
		sb.append(" WHERE EXISTS (SELECT 1                                      ");
		sb.append(" FROM IC_PROJECT_STO_TEMP STO                              ");
		sb.append(" WHERE ER1.F_PROJECT_ID = STO.F_PROJECT_ID)                ");
		int j = Db.update(sb.toString());
		if(j>=0&&i>=0){
			return true;
		}
		return false;
	}
	
	/**
	 * 更新权限
	 * 
	 * @param stmt
	 * @throws SQLException
	 */
	private boolean updateQx() throws SQLException {
		StringBuffer sb = new StringBuffer();
		
		sb.setLength(0);
		sb.append(" INSERT INTO BSUSERROLE                           ");
		sb.append(" (F_ROLECODE,F_ZGBH,F_SH)                         ");
		sb.append(" SELECT 'JS0012',F_ZGBH,'1'                       ");
		sb.append(" FROM BSUSER ER                                   ");
		sb.append(" WHERE F_SFYG = '1'                               ");
		sb.append(" AND F_YGZT = '0'                                 ");
		sb.append(" AND NOT EXISTS (SELECT 1                         ");
		sb.append(" FROM BSUSERROLE LE                               ");
		sb.append(" WHERE F_ROLECODE = 'JS0012'                      ");
		sb.append(" AND ER.F_ZGBH = LE.F_ZGBH)                       ");
		int i = Db.update(sb.toString());
		
		sb.setLength(0);
		sb.append(" INSERT INTO BSUSSJ                               ");
		sb.append(" (F_ZGBH,F_QXBH,F_SJBH,F_GRAN,F_G1,F_G2,F_SH)     ");
		sb.append(" SELECT F_ZGBH,'YT_ZZJGZD',F_LSJG,'11','1','1','1'");
		sb.append(" FROM BSUSER ER                                   ");
		sb.append(" WHERE F_SFYG = '1'                               ");
		sb.append(" AND F_YGZT = '0'                                 ");
		sb.append(" AND NOT EXISTS (SELECT 1                         ");
		sb.append(" FROM BSUSSJ SJ                                   ");
		sb.append(" WHERE ER.F_ZGBH = SJ.F_ZGBH                      ");
		sb.append(" AND F_QXBH = 'YT_ZZJGZD')                        ");
		int j = Db.update(sb.toString());
		
		sb.setLength(0);
		sb.append(" INSERT INTO BSUSERROLE                           ");
		sb.append(" (F_ROLECODE,F_ZGBH,F_SH)                         ");
		sb.append(" SELECT 'JS0008',F_MANAGER,'1'                    ");
		sb.append(" FROM IC_PROJECT_STO STO                          ");
		sb.append(" WHERE NOT EXISTS (SELECT 1                       ");
		sb.append(" FROM BSUSERROLE LE                               ");
		sb.append(" WHERE STO.F_MANAGER = LE.F_ZGBH                  ");
		sb.append(" AND LE.F_ROLECODE = 'JS0008')                    ");
		sb.append(" GROUP BY F_MANAGER                               ");
		int a = Db.update(sb.toString());
		if(j>=0&&i>=0&&a>=0){
			return true;
		}
		return false;
	}
	
}
