<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<style type="text/css">
	.main{
		width:100%;
		background-color:white;
		height:300px;
		margin:0 auto;
	}
	.top{
		border: 2px solid #e8e9e9;
		width:100%;
		margin: 0 auto;
		height:50px;
		border-bottom:1px solid #e8e9e9!important;
	}
	.block{
		float:left;
		width:150px;
		line-height:50px;
		cursor:pointer;
		text-align: center;
		display: block;
	}
	.block .txt{
		font-size:15px;
		color:black;
		font-weight: bold;
	}
	.block:HOVER {
		color:#22d69d;
		border-left: 1px solid #e8e9e9;
		border-right: 1px solid #e8e9e9;
		border-top: 2px solid #70BEE4;
	}
	.cur{
		color:#22d69d;
		border-left: 1px solid #e8e9e9;
		border-right: 1px solid #e8e9e9;
		border-top: 2px solid #70BEE4;
	}
	.right_btn{
		width:100px;
		height:35px;
		background-color: #70BEE4;
		float:right;
		margin-right:10px;
		margin-top:8px;
	}
	.right_btn span{
		line-height: 35px;
		color:#fff;
		margin-left:12px;
		font-weight:bold;
		font-size: 16px;
	}
	.main #file{
		width:100%;
		height:34px;
		border-bottom:2px solid #e8e9e9;
		margin: 0 auto;
		background-color:#F7F8F8;
		float:left;
	}
	.main #database{
		width:100%;
		height:34px;
		border-bottom:2px solid #e8e9e9;
		margin: 0 auto;
		background-color:#F7F8F8;
		float:left;
	}
	.main #interface{
		width:100%;
		height:34px;
		border-bottom:2px solid #e8e9e9;
		margin: 0 auto;
		background-color:#F7F8F8;
		float:left;
	}
	.input_file{
		height:auto;
		margin-top:3px;
		width:200px;
		margin-left:20px;
		float:left;
	}
	.input_file span{color:black !important;}
	.importBtn{
		background-color:#E7ECEC;
    	border: 1px solid rgb(199, 199, 199);
    	cursor: pointer;
    	display: block;
    	font-size: 14px;
    	line-height: 27px;
    	text-align: center;
		color:#000;
    	vertical-align: middle;
    	width: 56px;
    	height: 25px;
    	float:left;
    	margin-top:3px;
    	border-radius: 3px;
    	margin-left: 10px;
	}
	.importBtn:HOVER{background-color: #DEE2E2;}
	.transformBtn{
		background-color:#E7ECEC;
    	border: 1px solid rgb(199, 199, 199);
    	cursor: pointer;
    	display: block;
    	font-size: 14px;
    	line-height: 27px;
    	text-align: center;
		color:#000;
    	vertical-align: middle;
    	width: 56px;
    	height: 27px;
    	float:left;
    	margin-top:3px;
    	border-radius: 3px;
    	margin-left: 10px; 
	}
	.transformBtn:HOVER{background-color: #DEE2E2;}
	.match{
		width:100%;
		height:30px;
		border-bottom:2px solid #e8e9e9;
		border-top:2px solid #e8e9e9;
		margin: 0 auto;
		background-color:#f6fafd;
		margin-top:5px;
		
	}
	.match input{width:50px !important;margin-top:4px;}
	.match span{color:black !important;margin-top:4px;}
	.root{
		width:100%;
		height:80px;
		border-bottom:2px solid #e8e9e9;
		border-top:2px solid #e8e9e9;
		margin: 0 auto;
		background-color:#f6fafd;
		margin-top:5px;
	}
	.root .root_block{
		width:100px;
		line-height: 80px;
		float:left;
		margin-left:45px;
	}
	.root .root_block input{width:50px;}
	.root .root_block span{color:black !important;}
	.root .selType{
		width:400px;
		line-height: 40px;
		float:left;
		margin-left:45px;
		}
	.root .selType input{width:100px !important;color:#CBCBCB;}
	.root .selType span{color:black !important;}
	</style>
	<script type="text/javascript" src="<%=path%>/webresources/script/jquery-1.8.3.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#cur1").addClass('cur');
			$("#database").hide();
			$("#interface").hide();
			$("#file").show();
		});
		function changeTab(id)
		{
			$(".block").removeClass('cur');
			$("#"+id).addClass('cur');
			if(id=='cur1')
			{
				$("#database").hide();
				$("#interface").hide();
				$("#file").show();
			}
			else if(id=='cur2')
			{
				$("#database").show();
				$("#interface").hide();
				$("#file").hide();
			}
			else if(id=='cur3')
			{
				$("#database").hide();
				$("#interface").show();
				$("#file").hide();
			}
		}
		function check(obj)
		{	obj.style="color:black;";
			if(obj.value=="编码结构"||obj.value=="编码列")
			{
				obj.value="";
			}
		}
		function check2(obj)
		{
			if(obj.value.length==0)
			{
				if(obj.name=="bml")
				{
					obj.style="color:#CBCBCB;";
					obj.value="编码列";
				}
				else
				{
					obj.style="color:#CBCBCB;";
					obj.value="编码结构";
				}
			}
		}
		function impt(type)
		{
			if(type=="file")
			{
				var val = document.getElementById("sel").value;
				alert(val);
			}
		}
	</script>
  </head>
  
  <body>
    <div class="top">
    	<a href="#" onclick="changeTab('cur1')"><div class="block" id="cur1"><div class="txt">文件导入</div></div></a>
    	<a href="#" onclick="changeTab('cur2')"><div class="block" id="cur2"><div class="txt">数据库读取</div></div><a>
    	<a href="#" onclick="changeTab('cur3')"><div class="block" id="cur3"><div class="txt">接口读取</div></div><a>
    	<!-- <div class="right_btn"><span>导入/转化</span></div> -->
    </div>
     <!--  -->
   <div class="main">
   		<!-- 文件导入 -->
   		<div id="file">
   			<div class="input_file" style="width:230px;">
   				<input type="file" id="sel" name="file">
   			</div>
   			<a href="#" onclick="impt('file')"><div class="importBtn">导入</div></a>
   			<a href="#" onclick="transform('file')"><div class="transformBtn">转化</div></a>
   		</div>
   		
   		<!-- 数据库读取 -->
   		<div id="database">
   			<div class="input_file" style="width:340px;">
   				<span>数据库地址:</span><input type="text" name="dbAddr" id="dbAddr">
   				<span>表名:</span><input type="text" name="tbName" id="tbName" style="width:50px !important;margin-top:2px;">
   			</div>
   			<a href="#" onclick="impt('')"><div class="importBtn">导入</div></a>
   			<a href="#" onclick="transform('')"><div class="transformBtn">转化</div></a>
   		</div>
   		
   		<!-- 接口读取 -->
   		<div id="interface">
   			<div class="input_file" style="width:230px;">
   				<span>接口地址:</span><input type="text" name="interfaceAddr" id="interfaceAddr">
   			</div>
   			<a href="#" onclick="impt('')"><div class="importBtn">导入</div></a>
   			<a href="#" onclick="transform('')"><div class="transformBtn">转化</div></a>
   		</div>
   </div>
   <!--  -->
   <div class="match">
   		&nbsp;&nbsp;
   		<span>单位编号:</span><input type="text" name="bh" id="bh">
   		<span>单位名称:</span><input type="text" name="mc" id="mc">
   		<span>URL:</span><input type="text" name="url" id="url">
   		<span id="tip" style="display:none;"><font color="red" size="2">* 字段匹配不完整!</font></span>
   </div>
   <!--  -->
   <div class="root">
   		&nbsp;&nbsp;
   		<div class="root_block">
   			<span>ROOT:</span><input type="text" name="root" id="root">
   		</div>
   		<div class="selType">
   			<input type="radio" id="pid" name="pid" value="y" style="width:20px !important;"><span>含有父节点:</span><input type="text" name="y_bmjg" id="y_bmjg" value="编码结构" onfocus="check(this)" onblur="check2(this)"><br/>
   			<input type="radio" id="pid" name="pid" value="n" style="width:20px !important;"><span>不含父节点:</span><input type="text" name="n_bmjg" id="n_bmjg" value="编码结构" onfocus="check(this)" onblur="check2(this)">&nbsp;&nbsp;<input type="text" name="bml" id="bml" value="编码列" onfocus="check(this)" onblur="check2(this)">
   		</div>
   </div>
  </body>
</html>
