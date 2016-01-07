/*    */ package com.shcm.send;
/*    */ 
/*    */ import com.shcm.bean.ReplyBean;
/*    */ import com.shcm.bean.SendStateBean;
/*    */ import com.shcm.util.HttpUtils;
/*    */ import com.shcm.util.PublicUtils;
/*    */ import java.util.List;
/*    */ 
/*    */ public class DataApi
/*    */ {
/* 19 */   private static String sDataUrl = "http://smsapi.c123.cn/DataPlatform/DataApi";
/* 20 */   private static String sAccount = "";
/* 21 */   private static String sAuthKey = "";
/*    */ 
/*    */   public static void initialzeAccount(String url, String account, String authkey)
/*    */   {
/* 30 */     sDataUrl = url;
/* 31 */     sAccount = account;
/* 32 */     sAuthKey = authkey;
/*    */   }
/*    */ 
/*    */   public static String querySendState()
/*    */     throws Exception
/*    */   {
/* 42 */     StringBuilder sb = new StringBuilder();
/* 43 */     sb.append("action=getSendState&ac=");
/* 44 */     sb.append(sAccount);
/* 45 */     sb.append("&authkey=");
/* 46 */     sb.append(sAuthKey);
/* 47 */     return HttpUtils.post(sDataUrl, sb.toString(), "GET", "UTF-8");
/*    */   }
/*    */ 
/*    */   public static String queryReply()
/*    */     throws Exception
/*    */   {
/* 57 */     StringBuilder sb = new StringBuilder();
/* 58 */     sb.append("action=getReply&ac=");
/* 59 */     sb.append(sAccount);
/* 60 */     sb.append("&authkey=");
/* 61 */     sb.append(sAuthKey);
/* 62 */     return HttpUtils.post(sDataUrl, sb.toString(), "GET", "UTF-8");
/*    */   }
/*    */ 
/*    */   public static List<SendStateBean> getSendState()
/*    */   {
/*    */     try
/*    */     {
/* 73 */       String sRet = querySendState();
/* 74 */       return PublicUtils.parseSendState(sRet);
/*    */     }
/*    */     catch (Exception localException)
/*    */     {
/*    */     }
/*    */ 
/* 80 */     return null;
/*    */   }
/*    */ 
/*    */   public static List<ReplyBean> getReply()
/*    */   {
/*    */     try
/*    */     {
/* 91 */       String sRet = queryReply();
/* 92 */       return PublicUtils.parseReply(sRet);
/*    */     }
/*    */     catch (Exception localException)
/*    */     {
/*    */     }
/*    */ 
/* 98 */     return null;
/*    */   }
/*    */ }

/* Location:           E:\hsrb\SendDemo\lib\FYOpenApi.jar
 * Qualified Name:     com.shcm.send.DataApi
 * JD-Core Version:    0.6.2
 */