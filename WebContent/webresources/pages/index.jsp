<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.wbcs.config.Config"%>
<%@ page import="com.wbcs.jbsf.util.Wbcs4JBSFUtil"%>
<%@ page import="com.wbcs.jbsf.util.Consts"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
 		<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
		<META HTTP-EQUIV="Expires" CONTENT="0">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="${pageContext.request.contextPath }/images/index/srb.ico" rel="shortcut icon"/>
		<title>尚融宝后台管理系统</title>
		<script type="text/javascript">
			var tab = null;
			function init(pageid, reportid) {
				//打开页面前先加载待处理数目					
				/* document.getElementById("cmy").href="${pageContext.request.contextPath }/jbsf.sr?PAGEID=enterpriseAuditPage";
				document.getElementById("userCmy").href="${pageContext.request.contextPath }/jbsf.sr?PAGEID=userEnterpriseAuditPage";
				document.getElementById("mana").href="${pageContext.request.contextPath }/jbsf.sr?PAGEID=manaEnterpriseAuditPage"; */
				/* openPage2Tab(pageid, reportid);
				var status = actions({'serverClassName':'com.pansoft.jbsf.wbcs.action.IVOnloadNumOfWaitDealWith','afterCallbackMethod':function(data){
					var strs = data.responseText;
					var str = strs.split(",");

						if(strs.indexOf("left")<=0)
						{
							document.getElementById("tmp").style.display="none";
						}
						if(strs.indexOf("mid")<=0)
						{
							document.getElementById("leftTmp").style.display="none";
						}
						if(strs.indexOf("right")<=0)
						{
							document.getElementById("rightTmp").style.display="none";	
						}					

						document.getElementById("cmy").innerHTML="企业申请待审核";
						document.getElementById("userCmy").innerHTML="用户申请企业待审核";
						document.getElementById("mana").innerHTML="管理员申请待审核";
						document.getElementById("cmynum").innerHTML=""+str[0]+"";
						document.getElementById("userCmynum").innerHTML=""+str[1]+"";
						document.getElementById("mananum").innerHTML=""+str[2]+"";

				}}); */
				$("#layout1").ligerLayout({ leftWidth: 190,topHeight:100, height: '100%',space:0, allowTopResize:false, onHeightChanged: f_heightChanged, onEndResize: resizeFunc});
				var height = $(".l-layout-center").height();
				$("#framecenter").ligerTab({ height: height });
				tab = $("#framecenter").ligerGetTabManager();
				$("#accordion1").load("${pageContext.request.contextPath }/jbsf.sr?PAGEID=leftree");
				$("#pageloading").hide();
				/*iframe高度根据内容进行自适应*/
				iFrameHeight();
				/*处理待办信息*/
				jsfhje(pageid, reportid);
				
				
			}
			
			function iFrameHeight() {   
				/* var ifm= document.getElementById("qxsqframe");   
				var subWeb = document.frames ? document.frames["qxsqframe"].document : ifm.contentDocument;
				if(ifm != null && subWeb != null) {
				   ifm.height = subWeb.body.scrollHeight;
				   ifm.width = subWeb.body.scrollWidth;
				} */
				$("#qxsqframe").load(function(){
					var mainheight = $(this).contents().find("body").height()+30;
					$(this).height(mainheight);
				}); 
			} 
			
			function resizeFunc(parm,e){
				if(parm.diff != null){
					tab.setHeight("100%");
				}
			}
			
			function f_heightChanged(options)
            {
                if (tab)
                    tab.addHeight(options.diff);
            }
			function f_addTab(tabid,text, url)
            { 
                tab.addTabItem({ tabid : tabid,text: text, url: url });
            }
			function f_removeTab(tabid)
			{
				tab.removeTabItem(tabid);
			}
			
			function f_reloadTab(tabid){
				tab.reload(tabid);	
			}
			 
			function modifypwd(){
				var url = $.jbsf.core.contextpath()+"/jbsf.sr?PAGEID=modifypasswordPage";
				art.dialog.open(url,{width:350,height:255,title:'修改密码',lock:true});
			}
			function logout(){
				if(confirm("确定要注销当前账户吗?")){
					location.href="${pageContext.request.contextPath }/jbsf.sr?PAGEID=loginPageClt&srpath=index";
					//window.open("jbsf.sr?PAGEID=loginPage&srpath=index");
					/*$("#logoutform").submitForm({
						url:"jbsf.sr?PAGEID=loginPage"
					}).submit();*/
				}
			}
			
			
			function option_change(){
				alert("正在完善中....");
				
			}
		</script>
<style type="text/css">
.top_title {
	overflow: hidden; 
	height: 74px; 
	width: 100%; 
	background-image: url(images/login/content_title_bg.jpg);
	background-repeat: no-repeat;
	background-color: #84d4f9;
}
.top_title_zi{
	position: absolute;
	top: 20px;
	left: 2%;
}

.mainpage_style {
	height: 100%;
	background-position: center bottom; 
	background-size: cover;
	background-image: url(images/login/zhuye_content_bg.jpg);
	background-repeat: no-repeat;
	position: relative;
}
.right_bottom_title {
	position: absolute;
	right: 70px;
	bottom: 90px;
}
.firsttmp  {
	background-image: url(images/login/login_left_pic.jpg);
	background-repeat: no-repeat;
	width: 357px; 
	height: 168px;
	border: none;
	position: relative;
	left: 2%;
	top: -20px;
}
.firsttmp a {
	position: absolute;
	left: 52%;
	top: 120px;
	color: #e46a79!important;
	font-size: 18px!important;
}
.firsttmp p {
	color: #fff;
	font-size: 40px;
	position: relative;
	left: 62%;
	top: 32px;
	width: 58px;
	text-align: center;
}
.secondtmp {
	background-image: url(images/login/login_center_pic.jpg);
	background-repeat: no-repeat;
	width: 357px; 
	height: 168px;
	border: none;
	position: relative;
	left: 34%;
	top: -188px;
}
.secondtmp a {
	position: absolute;
	left: 48%;
	top: 120px;
	color: #36d493!important;
	font-size: 18px!important;
}
.secondtmp p {
	color: #fff;
	font-size: 40px;
	position: relative;
	left: 62%;
	top: 32px;
	width: 58px;
	text-align: center;
}
.thirdtmp {
	background-image: url(images/login/login_right_pic.jpg);
	background-repeat: no-repeat;
	width: 357px; 
	height: 168px;
	border: none;
	position: relative;
	left: 66%;
	top: -357px;
}
.thirdtmp a {
	position: absolute;
	left: 52%;
	top: 120px;
	color: #4698ec !important;
	font-size: 18px !important;
}
.thirdtmp p {
	color: #fff;
	font-size: 40px;
	position: relative;
	left: 66%;
	top: 32px;
	width: 58px;
	text-align: center;
}
body{
	overflow-x: hidden;
	overflow-y: hidden;
	color: #666;
}
.btn-exit{
	position: absolute;
	left: 95%;
	
}
.welcome{
	position: absolute;
	left: 85.2%;
	background-image: url("images/login/show_name_bg.png");
	background-repeat: no-repeat;
	width: 134px;
	height: 74px;
	cursor: pointer;
}
.option_wel{
	background-color: #89d1f6;
	position: absolute;
	left: 85.2%;
	top: 74px;
	width: 130px;
	height: 0px;
	z-index: 999;
	display: none;
}
.welcome:hover{
	background-color: #89d1f6;
}
.option_wel ul li{
	height: 45px;
	color: #fff;
	font-size: 16px;
	line-height: 45px;
	text-align: center;
	cursor: pointer;
	text-indent: 21px;
}
.welcome p{
	position: absolute;
	left: 27%;
	color: #FFF;
	font-size: 18px;
	top: 22px;
	width: 72px;
	text-align: center;
	height: 30px;
	overflow: hidden;
}
.l-layout-header-toggle{
	background-image: url("images/login/left_arrow.png") !important;
	margin: 7px;
}
.l-layout-collapse-left-toggle{
	background-image: url("images/login/left_arrow_other.png") !important;
	margin: 14px 0px 0px 0px;
}
#WX_CONTENT_leftree_guid_treemenusRpt .ico_docu {
    background-image: url("images/login/xiaotubiao.png") !important;
    background-position: 1px 9px !important
}
.top_title ul li{
	float:left;
	margin-left: 20px;
	width: 80px;
	height: 76px;
	cursor: pointer;
	
}

.top_title ul{
	position: absolute;
	left: 36%;
	top: 0px;
}
.top_cur{
	background-color: #b8dcf9 !important;
}
.p_cur{
	color: #388dd9 !important;	
}
#WX_CONTENT_leftree_guid_treemenusRpt .ico_close{
	background-image: url("images/login/menuopen.png") !important;
	background-position: 11px 10px !important;
}
#WX_CONTENT_leftree_guid_treemenusRpt .ico_open{
	background-image: url("images/login/menuclose.png") !important;
	background-position: 11px 10px !important;
}
#qiye div{
	background-image: url("images/login/jichuzidian.png");
}
#jituan div{
	background-image: url("images/login/qiyeguanli.png");
}
#jichu div{
	background-image: url("images/login/zuzhijiegou.png");
}
#shuju div{
	background-image: url("images/login/shujufenxi.png");
}
.top_title ul li div{
	width: 47px;
	height: 49px;
	position: relative;
	top: 6px;
	left: 19%;
}
.top_title ul li p{
	position: relative;
	top: 7px;
	left: 17%;
	width: 55px;
	color: #fff;
	height: 14px;
	line-height: 14px;
}
#changepass{
	background-image: url("images/login/pass.png");
	background-repeat: no-repeat;
	background-position: 14px 10px;
}
#changebase{
	background-image: url("images/login/xiaotubiao.png");
	background-repeat: no-repeat;
	background-position: 14px 16px;
}
</style>
</head>
	<body>
		<div id="pageloading"></div>
		<div class="option_wel">
             <ul>
                <li onclick="option_change();" id="changepass">修改密码</li>
                <li onclick="option_change1();" id="changebase">修改信息</li>
             </ul>
        </div>
		<div id="layout1" style="width:100%; margin:0 auto; margin-top:0px; " >
			<div position="top" id="header" class="l-topmenu">
				<div class="top_title">
                   <!-- <img class="top_title_zi" src="images/login/content_title.png"/>  -->
                   <!-- <ul>
                   <li id="jichu" onmouseenter="jichuover();" onmouseleave="jichuleave();" onclick="showjichu();" class="top_cur"><div></div><p class="p_cur">基础字典</p></li>
                   <li id="qiye"  onmouseenter="qiyeover();" onmouseleave="qiyeleave();" onclick="showqiye();"><div></div><p>企业管理</p></li>
                   <li id="jituan" onmouseenter="jituanover();" onmouseleave="jituanleave();" onclick="showjituan();"><div></div><p>集团组织</p></li>
                   <li id="shuju" onmouseenter="shujuover();" onmouseleave="shujuleave();" onclick="showshuju();"><div></div><p>数据分析</p></li>
                   </ul>  -->  
                   <div class="welcome" id="welcome" onclick="show_option();" >
                    	<p><%=session.getAttribute("realname") %> </p>
                   </div>
                   
                   <img class="btn-exit" src="images/login/login_out.jpg" onclick="logout()" />
                </div>
			</div>
			<div position="left" title="&lt;div style='text-align:center; width:150px;'&gt;导航菜单&lt;/div&gt;" id="accordion1" class="leftmenu" style="background-color:#DEF2F8;overflow-y: scroll;overflow-x:hidden;height:500px;"></div>
			
			<div position="center" id="framecenter">
				<div tabid="home" title="待办事项" class="mainpage_style" >
				   <!--  <img src="images/index/mainpage_title.png" class="right_bottom_title"/> -->
				   
				   <%
				   Object roles = session.getAttribute("roles");
				   
				   if(roles == null){
					   String loginpage = basePath + "jbsf.sr?PAGEID=loginPageClt";
				   %>   
				   <jsp:forward page="jbsf.sr?PAGEID=loginPageClt"></jsp:forward>
				   <%}else{
				   
				   String rolename = Wbcs4JBSFUtil.getRolenames(roles);
				   if(rolename.indexOf("城市经理") != -1 || rolename.indexOf("出纳") != -1){
				   %>
					    <div style="clear: both;height: 10px;"></div>
	         			<wx:title></wx:title>
	         			<wx:data></wx:data>
	         			<wx:navigate></wx:navigate>
	         			<div style="display: none;">
	         				<wx:button name="passCstmBtn"></wx:button>
	         				<wx:button name="noCstmBtn"></wx:button>
	         				<wx:button name="passsqlBtn"></wx:button>
	         				<wx:button name="nosqlBtn"></wx:button>
	         			</div>
	         			<wx:header></wx:header>
	         			<wx:footer></wx:footer>
	         		<%
	         		}
				 	if(rolename.indexOf("出纳") != -1){
				    %>
         			<div title="取现申请" style="width: 100%;">
						<iframe id="qxsqframe" name="qxsqframe" src="${pageContext.request.contextPath }/jbsf.sr?PAGEID=qxcashPage" frameborder="0" width="100%" height="300"></iframe>
					</div>
					<%} }%>
            	</div> 
			</div>
		</div>

		<div id="logoutDiv" style="display: none;">
			<form action="jbsf.sr?PAGEID=loginPageClt" id="logoutform" method="post">
				<input type="hidden" name="srpath" value="index">
			</form>
		</div>
		
	</body>
</html>