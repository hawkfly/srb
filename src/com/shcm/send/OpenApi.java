/*     */ package com.shcm.send;
/*     */ 
/*     */ import com.shcm.bean.BalanceResultBean;
/*     */ import com.shcm.bean.SendResultBean;
/*     */ import com.shcm.bean.UpdateResultBean;
/*     */ import com.shcm.util.HttpUtils;
/*     */ import com.shcm.util.PublicUtils;
/*     */ import java.util.List;
/*     */ 
/*     */ public class OpenApi
/*     */ {
/*  20 */   private static String sOpenUrl = "http://smsapi.c123.cn/OpenPlatform/OpenApi";
/*  21 */   private static String sAccount = "";
/*  22 */   private static String sAuthKey = "";
/*  23 */   private static int nCgid = 0;
/*  24 */   private static int nCsid = 0;
/*     */ 
/*     */   public static void initialzeAccount(String url, String account, String authkey, int cgid, int csid)
/*     */   {
/*  35 */     sOpenUrl = url;
/*  36 */     sAccount = account;
/*  37 */     sAuthKey = authkey;
/*  38 */     nCgid = cgid;
/*  39 */     nCsid = csid;
/*     */   }
/*     */ 
/*     */   public static String querySendOnce(String mobile, String content, int cgid, int csid, String time)
/*     */     throws Exception
/*     */   {
/*  53 */     StringBuilder sb = new StringBuilder();
/*  54 */     sb.append("action=sendOnce&ac=");
/*  55 */     sb.append(sAccount);
/*  56 */     sb.append("&authkey=");
/*  57 */     sb.append(sAuthKey);
/*  58 */     sb.append("&m=");
/*  59 */     sb.append(mobile);
/*  60 */     sb.append("&c=");
/*  61 */     sb.append(PublicUtils.UrlEncode(content, null));
System.out.println("sOpenUrl:"+sOpenUrl);
System.out.println("sAccount:"+sAccount);
System.out.println("sAuthKey:"+sAuthKey);
System.out.println("nCgid:"+nCgid);
System.out.println("nCsid:"+nCsid);
System.out.println("mobile:"+mobile);
System.out.println("content:"+content);



/*  62 */     if ((cgid > 0) || (nCgid > 0))
/*     */     {
/*  64 */       sb.append("&cgid=");
/*  65 */       sb.append(cgid > 0 ? cgid : nCgid);
/*     */     }
/*     */ 
/*  68 */     if ((csid > 0) || (nCsid > 0))
/*     */     {
/*  70 */       sb.append("&csid=");
/*  71 */       sb.append(csid > 0 ? csid : nCsid);
/*     */     }
/*  73 */     if (time != null)
/*     */     {
/*  75 */       sb.append("&t=");
/*  76 */       sb.append(time);
/*     */     }
System.out.println("sb:"+sb.toString());
/*  78 */     return HttpUtils.post(sOpenUrl, sb.toString(), "POST", "UTF-8");
/*     */   }
/*     */ 
/*     */   public static String querySendBatch(String mobile, String content, int cgid, int csid, String time)
/*     */     throws Exception
/*     */   {
/*  92 */     StringBuilder sb = new StringBuilder();
/*  93 */     sb.append("action=sendBatch&ac=");
/*  94 */     sb.append(sAccount);
/*  95 */     sb.append("&authkey=");
/*  96 */     sb.append(sAuthKey);
/*  97 */     sb.append("&m=");
/*  98 */     sb.append(mobile);
/*  99 */     sb.append("&c=");
/* 100 */     sb.append(PublicUtils.UrlEncode(content, null));
/* 101 */     if ((cgid > 0) || (nCgid > 0))
/*     */     {
/* 103 */       sb.append("&cgid=");
/* 104 */       sb.append(cgid > 0 ? cgid : nCgid);
/*     */     }
/*     */ 
/* 107 */     if ((csid > 0) || (nCsid > 0))
/*     */     {
/* 109 */       sb.append("&csid=");
/* 110 */       sb.append(csid > 0 ? csid : nCsid);
/*     */     }
/* 112 */     if (time != null)
/*     */     {
/* 114 */       sb.append("&t=");
/* 115 */       sb.append(time);
/*     */     }
/* 117 */     return HttpUtils.post(sOpenUrl, sb.toString(), "POST", "UTF-8");
/*     */   }
/*     */ 
/*     */   public static String querySendParam(String mobile, String content, String[] paramArray, int cgid, int csid, String time)
/*     */     throws Exception
/*     */   {
/* 132 */     StringBuilder sb = new StringBuilder();
/* 133 */     sb.append("action=sendParam&ac=");
/* 134 */     sb.append(sAccount);
/* 135 */     sb.append("&authkey=");
/* 136 */     sb.append(sAuthKey);
/* 137 */     sb.append("&m=");
/* 138 */     sb.append(mobile);
/* 139 */     sb.append("&c=");
/* 140 */     sb.append(PublicUtils.UrlEncode(content, null));
/*     */ 
/* 142 */     int nCount = Math.min(paramArray.length, 10);
/* 143 */     for (int i = 0; i < nCount; i++)
/*     */     {
/* 145 */       if (paramArray[i] != null)
/*     */       {
/* 147 */         sb.append("&p");
/* 148 */         sb.append(i + 1);
/* 149 */         sb.append("=");
/* 150 */         sb.append(PublicUtils.UrlEncode(paramArray[i], null));
/*     */       }
/*     */     }
/* 153 */     if ((cgid > 0) || (nCgid > 0))
/*     */     {
/* 155 */       sb.append("&cgid=");
/* 156 */       sb.append(cgid > 0 ? cgid : nCgid);
/*     */     }
/*     */ 
/* 159 */     if ((csid > 0) || (nCsid > 0))
/*     */     {
/* 161 */       sb.append("&csid=");
/* 162 */       sb.append(csid > 0 ? csid : nCsid);
/*     */     }
/* 164 */     if (time != null)
/*     */     {
/* 166 */       sb.append("&t=");
/* 167 */       sb.append(time);
/*     */     }
/* 169 */     return HttpUtils.post(sOpenUrl, sb.toString(), "POST", "UTF-8");
/*     */   }
/*     */ 
/*     */   public static String queryBalance() throws Exception
/*     */   {
/* 174 */     StringBuilder sb = new StringBuilder();
/* 175 */     sb.append("action=getBalance&ac=");
/* 176 */     sb.append(sAccount);
/* 177 */     sb.append("&authkey=");
/* 178 */     sb.append(sAuthKey);
/* 179 */     return HttpUtils.post(sOpenUrl, sb.toString(), "GET", "UTF-8");
/*     */   }
/*     */ 
/*     */   public static String postUpdateKey() throws Exception
/*     */   {
/* 184 */     StringBuilder sb = new StringBuilder();
/* 185 */     sb.append("action=updateKey&ac=");
/* 186 */     sb.append(sAccount);
/* 187 */     sb.append("&authkey=");
/* 188 */     sb.append(sAuthKey);
/* 189 */     return HttpUtils.post(sOpenUrl, sb.toString(), "GET", "UTF-8");
/*     */   }
/*     */ 
/*     */   public static BalanceResultBean getBalance()
/*     */   {
/*     */     try
/*     */     {
/* 200 */       String sRet = queryBalance();
/* 201 */       return PublicUtils.parseBalance(sRet);
/*     */     } catch (Exception localException) {
/*     */     }
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */   public static UpdateResultBean updateKey()
/*     */   {
/*     */     try
/*     */     {
/* 215 */       String sRet = postUpdateKey();
/* 216 */       return PublicUtils.parseAuthKey(sRet);
/*     */     } catch (Exception localException) {
/*     */     }
/* 219 */     return null;
/*     */   }
/*     */ 
/*     */   public static List<SendResultBean> sendOnce(String mobile, String content, int cgid, int csid, String time)
/*     */   {
/*     */     try
/*     */     {
	
/* 235 */       String sRet = querySendOnce(mobile, content, cgid, csid, time);
/* 236 */       return PublicUtils.parseResult(sRet);
/*     */     } catch (Exception localException) {
/*     */     }
/* 239 */     return null;
/*     */   }
/*     */ 
/*     */   public static List<SendResultBean> sendOnce(String[] mobileArray, String content, int cgid, int csid, String time)
/*     */   {
/*     */     try
/*     */     {
/* 255 */       String mobile = PublicUtils.joinArray(mobileArray, ",");
/* 256 */       String sRet = querySendOnce(mobile, content, cgid, csid, time);
/* 257 */       return PublicUtils.parseResult(sRet);
/*     */     } catch (Exception localException) {
/*     */     }
/* 260 */     return null;
/*     */   }
/*     */ 
/*     */   public static List<SendResultBean> sendBatch(String mobile, String content, int cgid, int csid, String time)
/*     */   {
/*     */     try
/*     */     {
/* 276 */       String sRet = querySendBatch(mobile, content, cgid, csid, time);
/* 277 */       return PublicUtils.parseResult(sRet);
/*     */     } catch (Exception localException) {
/*     */     }
/* 280 */     return null;
/*     */   }
/*     */ 
/*     */   public static List<SendResultBean> sendBatch(String[] mobileArray, String[] contentArray, int cgid, int csid, String time)
/*     */   {
/*     */     try
/*     */     {
/* 296 */       String mobile = PublicUtils.joinArray(mobileArray, ",");
/* 297 */       String content = PublicUtils.joinArray(contentArray, "{|}");
/* 298 */       String sRet = querySendBatch(mobile, content, cgid, csid, time);
/* 299 */       return PublicUtils.parseResult(sRet);
/*     */     } catch (Exception localException) {
/*     */     }
/* 302 */     return null;
/*     */   }
/*     */ 
/*     */   public static List<SendResultBean> sendParam(String mobile, String content, String[] paramArray, int cgid, int csid, String time)
/*     */   {
/*     */     try
/*     */     {
/* 319 */       String sRet = querySendParam(mobile, content, paramArray, cgid, csid, time);
/* 320 */       return PublicUtils.parseResult(sRet);
/*     */     } catch (Exception localException) {
/*     */     }
/* 323 */     return null;
/*     */   }
/*     */ 
/*     */   public static List<SendResultBean> sendParam(String[] mobileArray, String content, String[] paramArray, int cgid, int csid, String time)
/*     */   {
/*     */     try
/*     */     {
/* 340 */       String mobile = PublicUtils.joinArray(mobileArray, ",");
/* 341 */       String sRet = querySendParam(mobile, content, paramArray, cgid, csid, time);
/* 342 */       return PublicUtils.parseResult(sRet);
/*     */     } catch (Exception localException) {
/*     */     }
/* 345 */     return null;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ }

/* Location:           E:\hsrb\SendDemo\lib\FYOpenApi.jar
 * Qualified Name:     com.shcm.send.OpenApi
 * JD-Core Version:    0.6.2
 */