<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>聚赢社区欢迎你！</title>
<link href="css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.9.1.min.js"></script>

<script type="text/javascript">

$(document).ready(function () {
    $(this).keypress(function (e) {
        switch (e.which) {
            case 13:
            	if($("#usercode").val() == null || $("#usercode").val() == ""){
            		$("#usercode").focus();
            	}else if($("#password").val() == null || $("#password").val() == ""){
            		$("#password").focus();
            	}else{
            		loginBtnFun();
            	}
                break;
        }
    });
});

function loginBtnFun()
{
   	try{
   		
			var usercode = document.getElementById('username').value;
			var password = document.getElementById('password').value;
			if(usercode == null || usercode == ""){
				alert("请输入用户名");
				$("#usercode").focus();
				return;
			}
			if(password == null || password==""){
				alert("请输入密码");
				$("#password").focus();
				return;
			}
			$.ajax({
				url: "login.do?username="+usercode+"&password="+password,
				type: "get",
				dataType: "json",
				cache: true,
				success: function(result) {
					if(result.code == "success"){
						window.location.href="page.jsp";
					}else{
						alert("用户名或密码不存在！");
					}
				},
				error: function(result, status) {
					if (status == 'error') {
						alert("系统发生错误！请刷新页面后重试！");
					}
				}
			});
			//var cookieTime=cookie("upDownDt1");
	}
	catch(e)
    	{
		 alert(e.toString());
    		//logErrorsAsJsFileLoad(e); 
    	}
	finally{
	
		
	}
	
}

</script>
</head>
<body>
	<div class="loginwrap">
		<div style="height: 101px;"></div>
		<div class="inputbox">
			<div style="margin-left: 96px;">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>用户名：</td>
						<td><input name="username" id="username" type="text"
							class="inptext" />
						</td>
					</tr>
					<tr>
						<td height="50">密&nbsp;&nbsp;&nbsp;码：</td>
						<td><input name="password" id="password" type="password"
							class="inptext" />
						</td>
					</tr>
  
  <tr>
    <td>&nbsp;</td>
    <td><a href="javascript:loginBtnFun();" class="btn_login">登录</a></td>
  </tr>
</table>
   </div>
 </div>
 <p style="color: #fff;"><!-- 普联软件 ©版权所有 --></p>
 <div class="down">

  <div style="width: 90px; float: right;  text-align: center;" align="center">
  <img src="images/erweima.png" width="90px" height="90px" />
  
  <a href="app/sqshmobile.apk"><span style="color: #fff; line-height: 24px; text-align: center;">Android客户端</span></a>
  </div>
</div>
</div>

<div style="clear: both;"></div>
</body>
</html>