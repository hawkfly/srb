/*    */ package com.shcm.bean;
/*    */ 
/*    */ import java.util.Date;
/*    */ 
/*    */ public class ReplyBean
/*    */ {
/*    */   private int id;
/*    */   private long msgId;
/*    */   private Date replyTime;
/*    */   private String mobile;
/*    */   private String content;
/*    */ 
/*    */   public int getId()
/*    */   {
/* 30 */     return this.id;
/*    */   }
/*    */ 
/*    */   public void setId(int id) {
/* 34 */     this.id = id;
/*    */   }
/*    */ 
/*    */   public Date getReplyTime() {
/* 38 */     return this.replyTime;
/*    */   }
/*    */ 
/*    */   public void setReplyTime(Date replyTime) {
/* 42 */     this.replyTime = replyTime;
/*    */   }
/*    */ 
/*    */   public String getMobile() {
/* 46 */     return this.mobile;
/*    */   }
/*    */ 
/*    */   public void setMobile(String mobile) {
/* 50 */     this.mobile = mobile;
/*    */   }
/*    */ 
/*    */   public String getContent() {
/* 54 */     return this.content;
/*    */   }
/*    */ 
/*    */   public void setContent(String content) {
/* 58 */     this.content = content;
/*    */   }
/*    */ 
/*    */   public long getMsgId() {
/* 62 */     return this.msgId;
/*    */   }
/*    */ 
/*    */   public void setMsgId(long msgId) {
/* 66 */     this.msgId = msgId;
/*    */   }
/*    */ }

/* Location:           E:\hsrb\SendDemo\lib\FYOpenApi.jar
 * Qualified Name:     com.shcm.bean.ReplyBean
 * JD-Core Version:    0.6.2
 */