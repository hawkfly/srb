<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>查看图片</title>
<script src="<%=path%>/js/iv/img.js"></script>
<script src="<%=path%>/js/iv/ImageTrans.js"></script>
<script src="<%=path%>/webresources/script/jquery-1.8.3.js"></script>
</head>
<body>
<style>
#idContainer{
width:1024px;
height:2468px; 
background:#696761 center no-repeat;
margin-left:170px;
margin-top:5px;
}
body{
background-color:#696761;
}
</style>

<div align="center">
<input id="idLeft" type="button" value="向左旋转" />
<input id="idRight" type="button" value="向右旋转" />
<input id="idVertical" type="button" value="垂直翻转" />
<input id="idHorizontal" type="button" value="水平翻转" />
</div>
<div id="idContainer" align="center"> </div>
<br>

<script>
(function(){
	var url = location.search;
	var str;
	if (url.indexOf("?") != -1)
	{
    	str = url.substr(1);
    }
    var id = str.substr(str.lastIndexOf('=')+1);
	var obj = window.opener.document.getElementById(id);
	var val = obj.src;
	var container = $$("idContainer"), src = val,
	options = 
	{		
		onLoad: function(){ container.style.backgroundImage = ""; }
	},
	it = new ImageTrans( container, options );
it.load(src);
//垂直翻转
$$("idVertical").onclick = function(){ it.vertical(); }
//水平翻转
$$("idHorizontal").onclick = function(){ it.horizontal(); }
//左旋转
$$("idLeft").onclick = function(){ it.left(); }
//右旋转
$$("idRight").onclick = function(){ it.right(); }

})()
</script>
</body>
</html>
