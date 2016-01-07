<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>燃煤采购优化决策系统</title>
<link href="wbcsdemo/css/css.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
		body {
			background-image: url(wbcsdemo/images/login-bg.gif);
			background-repeat: repeat-x;
		}
		.login {
			height: 318px;
			width: 817px;
			background-image: url(wbcsdemo/images/login-img.jpg);
			margin-top: 100px;
			margin-right: auto;
			margin-bottom: 0px;
			margin-left: auto;
		}
		.login ul {
			font-size: 14px;
			line-height: 36px;
			color: #01185c;
			display: block;
			width: 276px;
			margin-left: 65px;
		}
		.login ul li input {
			font-size: 14px;
			line-height: 23px;
			height: 23px;
			width: 170px;
			border: 1px solid #578bc5;
			text-indent: 3px;
			font-family: Verdana, Geneva, sans-serif, "黑体";
		}
		.btn-login {
			height: 33px;
			width: 105px;
			display: block;
			background-image: url(wbcsdemo/images/btn-login.gif);
			background-repeat: no-repeat;
			margin-left: 60px;
			margin-top: 10px;
		}
		a.btn-login:hover {
			background-image: url(wbcsdemo/images/btn-login-hover.gif);
			background-repeat: no-repeat;
		}
		p {
			line-height: 30px;
			color: #333;
			width: 817px;
			margin-top: 0px;
			margin-right: auto;
			margin-bottom: 0px;
			margin-left: auto;
			text-align: center;
		}
	</style>
</head>

<body>
	<div class="login">
	<div style="height:167px;"></div>
	<ul>
	<li>用户名：<wx:searchbox condition="txtusername"></wx:searchbox></li>
	<li>密&nbsp;&nbsp;码：<wx:searchbox condition="txtpassword"></wx:searchbox></li>
	<li>角&nbsp;&nbsp;色：<wx:searchbox condition="sltrolecode"></wx:searchbox></li>
	<li><wx:button name="loginBtn"></wx:button></li>
	</ul>
	</div>
	<p>华电国际电力股份有限公司 - 版权所有 </p>
</body>
</html>