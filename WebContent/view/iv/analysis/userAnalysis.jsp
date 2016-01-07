<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <script type="text/javascript" src="<%=path%>/webresources/script/jquery-1.8.3.js"></script>
  <script type="text/javascript" src="<%=path%>/webresources/jsplugins/charts/highcharts.js"></script>
   <script type="text/javascript" src="<%=path%>/webresources/jsplugins/charts/highcharts-3d.js"></script>
  <script>
  $(function () {
	    // Set up the chart
	    var mydate = new Date();
		var nowDate = mydate.getFullYear(); //获取当前年
		$("#select_id ").val(nowDate);   // 设置Select的Value值为当前年的项选中
		process(nowDate);
  });
  function process(nowDate){
	   $.ajax({
		    data: "get",
		    url: "/prjowner/jf/userAnalysis",
		    data: "key=" + nowDate,
		    dataType: "json",
		    async: false,
		    success: function (data) {
		    $("#count").text(data.usercount); 
		    $('#container').highcharts({
		      chart: {
		          type: 'line'
		      },
		      title: {
		          text: nowDate+'年用户注册情况'
		      },
		      subtitle: {
		          text: ''
		      },
		      xAxis: {
		    	  categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月 ', '十二月']
		      },
		      yAxis: {
		    	  tickInterval:1, // 刻度值  
		    	  min:0, // 定义最小值  
		          title: {
		              text: '数量：个'
		          }
		      },
		      plotOptions: {
		          line: {
		              dataLabels: {
		                  enabled: true
		              },
		              enableMouseTracking: false
		          }
		      },
		      credits: {
		          enabled:false
			  },
		      series:[data]
		  });
		 	}
		}); 
 	 }
		function changeyear(){
			var checkValue=$("#select_id").val();  //获取Select选择的Value
			process(checkValue);
		}
	


	  </script>
  </head>
  <style>
  #count{
	 	color: #F00;
		font-weight: bold;
		font-size: 20px;
		padding: 5px;
  }
  .topCount{
		position: relative;
		top: -5px;
		left: 80%;
		width: 145px;
		border: 1px solid #c7c7c7;
		padding: 10px;
		text-indent: 4px;
		background-color: #ffefed;
		color: #666;	
  }
  .top_tool{
 	 position: relative;
	 top: 10px;
  }
  </style>
  <body>
  	<div class="top_tool">
  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日期：<select id="select_id" onchange="changeyear()">
  	<option value="2014">2014</option>
  	<option value="2015">2015</option>
  	<option value="2016">2016</option>
  	</select>&nbsp;年
  	</div>
  	<div class="topCount">已注册<span id="count"></span>位用户</div>
    <div id="container" style="min-width:700px;height:400px"></div>
  </body>
</html>
