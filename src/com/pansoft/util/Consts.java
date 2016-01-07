package com.pansoft.util;

import java.sql.Connection;

import com.wbcs.system.intercept.IInterceptor;


public class Consts {

	public static int notice_num_per_page = 10;
    public static int payinfo_num_per_page = 10;
    public static int yzpayinfo_num_per_page = 6;
	public static int TRANSACTION_REPEATABLE_READ = 4;
	//private static int transactionLevel = Connection.TRANSACTION_REPEATABLE_READ;
	
	public static final String TYPE_YGEXCLUDE = "jxexclude";
	public static final String TYPE_INDEXIMG = "indexlbimg";
	
	public static final String Flow_SHSQ = "赎回申请"; 
	public static final String FLOW_READY = "等待审核";
	public static final String FLOW_OK = "审核通过";
	public static final String FLOW_NO = "审核未通过";
	public static final String FLOW_REJECT = "申请驳回";
	public static final String FLOW_FINAL = "流程终结";
	
	public static final String BTN_PASS = "pass";
	public static final String BTN_NO = "no";
	
	public static final String FLOWFINAL = "f";
	public static final String FLOWCHECK = "c";
	
	public static final String ACTIVI = "1";
	public static final String UNACTIVI = "0";
	
	//public static final String UNCSTMID = "777290058120154";
	public static final String UNCSTMID = "802370048160534";
	//teetetetetete
	
	/*Log types*/
	public static final String PAYCALLBACK = "unpay_backRcvResponse";
	public static final String PAYPREACTIONE = "unpay_prepayaction";
	public static final String QUERYORDERSUCESS = "queryorder_success";
	public static final String QUERYORDERERROR345 = "queryorder_error345";
	public static final String QUERYORDERERROROTHER = "queryorder_errorother";
	public static final String UNLOCKACCOUNTSUCCESS = "unlockaccount_success";
	public static final String UNLOCKACCOUNTERROR = "unlockaccount_error";
	public static final String NETWORKERROR = "network_error";
	
	/*system*/
	public static final int PER_SUCCESS = IInterceptor.WX_RETURNVAL_SUCCESS;
	public static final int PER_SKIP = IInterceptor.WX_RETURNVAL_SKIP;
	
	/*error*/
	public static final String PREPAYERROR = "系统错误-prepayerror!";
	
	/*角色编码*/
	public static final String ADMIN = "admin,9999";
	public static final String CSJL = "csjl";
	public static final String ZHZG = "zhzg";
	public static final String YYBZ = "yybz";
	public static final String TDJL = "tdjl";
}
