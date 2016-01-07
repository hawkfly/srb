<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试服务器</title>
</head>
<style stype="css/text">
#sty{
border: 0px solid #000000;
width:380px;
height:auto;
}
body{
	background-color:#DDDDDD;
}
.aa{border: 1px solid #BBBBBB;}
</style>
<script type="text/javascript" src="<%=path%>/webresources/script/jquery-1.2.1.js"></script>
<script type="text/javascript" src="<%=path%>/webresources/jsplugins/ajaxfileupload.js"></script>
<script type="text/javascript"> 
 $.ajaxFileUpload({
		url:"jf/testServer",
		secureuri:false,
		dataType: 'HTML',
		cache:  false,
		success:function (data, textStatus) {
		data = data.replace("<pre>","");
		data = data.replace("</pre>","");
  		var team = data.split(";");
  		var formDiv="<form action='#'><table id='sty'>";
  		formDiv+="<tr><th class='aa'>服务商名称</th><th class='aa'>网络延迟</th><th class='aa'>连接状态</th></tr>";
  		for(var i=0;i<team.length-1;i++)
  		{
  			var ss = team[i].split(",");
  			formDiv+="<tr style='height:30px;'>";
  			for(var j=0;j<ss.length;j++)
  			{
  				if(ss[j]=="连接成功")
  				{
  				formDiv+="<td class='aa'><font color='green'>"+ss[j]+"</font></td>";
  				}
  				else if(ss[j]=="连接失败")
  				{
  				formDiv+="<td class='aa'><font color='red'>"+ss[j]+"</font></td>";
  				}
  				else
  				{
  				formDiv+="<td class='aa'>"+ss[j]+"</td>";
  				}
  			}
  			formDiv+="</tr>";
  		}
  		formDiv+="</table></form>";
  		document.getElementById("aa").innerHTML=formDiv;
  		
  		
		},
		timeout:6000000,
		error:function(XMLHttpRequest, textStatus, errorThrown){
			MyLoading(false,"");
			if(textStatus=="timeout"){
				alert("获取数据超时");
			}
		}
	}); 
</script>
     
<body>
<span id="aa"></span>
</body>
</html>