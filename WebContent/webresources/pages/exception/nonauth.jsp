<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.wbcs.config.Config"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>无访问权限</title>
		<style>
			*{padding: 0;margin: 0;}
			html,body{height: 100%;}
			#outer{height: 100%;width: 100%;display: table;*position: relative;*overflow: hidden;}
			#middle{display: table-cell;vertical-align: middle;*position: absolute;*top: 50%;}
			#inner {width: 400px;margin: 0 auto;*position: relative;*top: -50%;}
		</style>
		<script src="${pageContext['request'].contextPath }/webresources/script/jquery-1.8.3.js" charset="utf-8"></script>
		<script src="${pageContext['request'].contextPath }/webresources/jsplugins/jbsf/jbsfcore.js" charset="utf-8"></script>
		<script type="text/javascript">
			$(function(){
				setTimeout(function(){
					location.href = document.location.origin+$.jbsf.core.contextpath()+"/index.html";
				},2000);
			});
		</script>
	</head>
	<body>
		<div id="outer">
		  <div id="middle">
		      <div id="inner" style="font: red;">
		          <p>对当前页面无访问权限....2秒钟后将自动跳转到首页</p>
		      </div>
		  </div>
		</div>
	</body>
</html>