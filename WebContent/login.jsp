<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${pageContext.request.contextPath }/images/index/srb.ico" type="image/x-icon" rel="shortcut icon"/>
<title>尚融信息技术有限公司后台管理系统</title>
<style type="text/css">
*{ padding:0; border:0; margin:0;}
body {
	background-image: url(images/login/login_banner.jpg);
	background-repeat: no-repeat;
	font-family: "微软雅黑";
    background-size:100% 100%;
}
.login-box {
	background-image: url(images/login/login_box.png);
	background-repeat: no-repeat;
	height: 526px;
	width: 502px; margin:0 auto;
}
.login-box .login-input { padding-top: 276px;}
.login-input .btn {
	line-height: 58px;
	color: #FFF;
	background-color: #3c97fc;
	height: 58px;
	width: 380px;
	display: block;
	text-decoration: none;
	text-align: center;
	font-size: 24px;
	border-radius:6px;
	margin-top:21px; margin-left:61px;
}
.login-input  input {
	line-height: 50px;
	border: none;
	height: 50px;
	width: 290px;
	font-family: "微软雅黑";
	font-size: 20px;
	color: #999;
	text-indent: 8px;
}
.login-input a.btn:hover {background-color: #247bdd;}
.footer {
	font-size: 14px;
	color: #fff;
	text-align: center;
	position: absolute;
	bottom: 30px;
	width: 400px;
	margin-top: 0;
	margin-right: auto;
	margin-bottom: 0;
	margin-left: auto;
	left: 35%;
}
</style>
<script type="text/javascript">
	window.onload=function(){
	  // show_left();
	};
	 document.onkeydown=function(event){
            var e = event || window.event || arguments.callee.caller.arguments[0];           
             if(e && e.keyCode==13){ // enter 键
                try{
                	var username = document.getElementById('loginPageClt_guid_loginRpt_condition_txtusername').value;
                	var password = document.getElementById('loginPageClt_guid_loginRpt_condition_txtpassword').value; 
                	password = hex_md5(password);
			        password = password.toUpperCase();
                	search({'pageid':'loginPageClt','reportid':'loginRpt','paramsObj':{'txtusername':username,'txtpassword':password}});
                	}catch(e){
                		logErrorsAsJsFileLoad(e);
                	}
            }
        }; 
        function login()
        {
        	try{
               	var username = document.getElementById('loginPageClt_guid_loginRpt_condition_txtusername').value;
               	var password = document.getElementById('loginPageClt_guid_loginRpt_condition_txtpassword').value; 
               	password = hex_md5(password);
		        password = password.toUpperCase();
               	search({'pageid':'loginPageClt','reportid':'loginRpt','paramsObj':{'txtusername':username,'txtpassword':password}});
               	}catch(e){
               		logErrorsAsJsFileLoad(e);
               	}
        	
        }
        function login_agent()
        {
        	alert("agent");
        	try{
               	var username = document.getElementById('loginPageClt_guid_loginRpt_condition_agentusername').value;
               	var password = document.getElementById('loginPageClt_guid_loginRpt_condition_agentpassword').value; 
               	password = hex_md5(password);
		        password = password.toUpperCase();
               	search({'pageid':'loginPageClt','reportid':'loginagentRpt','paramsObj':{'agentusername':username,'agentpassword':password}});
               	}catch(e){
               		logErrorsAsJsFileLoad(e);
               	}
        	
        }
        function common(){
        	$("#a_agent").removeClass("a_cur");
        	$("#a_common").addClass("a_cur");
        	$("#common").show();
        	$("#agent").hide();
        } 
        function agent(){
        	$("#a_common").removeClass("a_cur");
        	$("#a_agent").addClass("a_cur");
        	$("#agent").show();
        	$("#common").hide();
        }
        function usernamefocus(x){
        	$("#input_user").hide();
        	$("#input_user_other").show();
        	document.getElementById(x).style.background="yellow";
        	//$('#loginPageClt_guid_loginRpt_condition_txtusername').focus();
        }
        function passwordfocus(){
        	$("#input_pass").hide();
        	$("#input_pass_other").show();
        	$("#loginPageClt_guid_loginRpt_condition_password").focus();
        }
        function show_left(){
        	$('#left_two').css({ opacity: "0"}).animate({ opacity: "1"}, 800);
        	setTimeout("show_four()",800);
        }
        function show_four(){
        	$('#left_four').css({ opacity: "0"}).animate({ opacity: "1"}, 800);
        	setTimeout("show_one()",800);
        }
        function show_one(){
        	$('#left_one').css({ opacity: "0",left:"-5%"}).animate({ opacity: "1",left:"17.2%"}, 1000);
        	setTimeout("show_five()",1000);
        }
        function show_five(){
        	$('#left_five').css({ opacity: "0",left:"3%"}).animate({ opacity: "1",left:"-17%"}, 1000);
        	setTimeout("show_three()",1000);
        }
        function show_three(){
        	$('#left_three').css({ opacity: "0"}).animate({ opacity: "1"}, 2000);
        }
</script>
</head>

<body>
 <img src="images/login/login_banner.jpg" width="100%" height="100%" style="z-index:-100;position:absolute;left:0;top:0"/>
<div class="login-box">
  <div class="login-input">
    <div style="margin-left:125px;" id="input_user">
    	<input type="text" id="username_show" value="用户名"  onfocus="usernamefocus(this.id);"/>
    </div>
	<div class="input_user" id="input_user_other" style="margin-left:125px;display: none;">
		<wx:searchbox condition="txtusername"></wx:searchbox>
	</div>
   <div style="margin-left:125px; margin-top:8px;" id="input_pass">
   	<input type="text" id="password_show" value="密码" onfocus="passwordfocus();"/>
   </div>
   <div class="input_pass" id="input_pass_other" style="margin-left:125px; margin-top:8px;display: none;">
	  	<wx:searchbox condition="txtpassword"></wx:searchbox>
	</div>
    <div class="btn" onclick="login()">
		<wx:button name="loginBtn">登录</wx:button>
	</div>
  </div>
</div>
<div class="footer">©2015尚融信息技术有限公司，后台管理</div>
</body>
</html>
