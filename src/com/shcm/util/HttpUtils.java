/*    */ package com.shcm.util;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ 
/*    */ public class HttpUtils
/*    */ {
/*    */   public static String post(String urlPost, String param, String method, String encoding)
/*    */     throws Exception
/*    */   {
/* 23 */     URL url = new URL(urlPost);
/* 24 */     HttpURLConnection http = (HttpURLConnection)url.openConnection();
/* 25 */     http.setDoOutput(true);
/* 26 */     http.setDoInput(true);
/* 27 */     http.setRequestMethod(method);
/* 28 */     http.setUseCaches(false);
/* 29 */     http.setConnectTimeout(10000);
/* 30 */     http.setReadTimeout(10000);
/* 31 */     http.setInstanceFollowRedirects(true);
/* 32 */     http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
/*    */ 
/* 36 */     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(http.getOutputStream(), encoding));
/* 37 */     out.write(param);
/* 38 */     out.flush();
/* 39 */     out.close();
/*    */ 
/* 42 */     BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), encoding));
/* 43 */     String line = null;
/* 44 */     StringBuilder sb = new StringBuilder();
/* 45 */     while ((line = in.readLine()) != null)
/*    */     {
/* 47 */       sb.append(line);
/*    */     }
/* 49 */     in.close();
/* 50 */     http.disconnect();System.out.println("line"+line);
/*    */ System.out.println("sb.tostr"+sb.toString());
/* 52 */     return sb.toString();
/*    */   }
/*    */ }

/* Location:           E:\hsrb\SendDemo\lib\FYOpenApi.jar
 * Qualified Name:     com.shcm.util.HttpUtils
 * JD-Core Version:    0.6.2
 */