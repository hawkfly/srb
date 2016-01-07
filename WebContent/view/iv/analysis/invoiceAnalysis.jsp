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
		    url: "/prjowner/jf/invoiceAnalysis",
		    data: "key=" + nowDate,
		    dataType: "json",
		    async: false,
		    success: function (data) {
		    $("#count").text(data[0].allcount+491);
		    $("#countrz").text(data[1].allcountrz);
		    $('#container').highcharts({                                          
		        chart: {                                                          
		        },                                                                
		        title: {                                                          
		            text: '发票采集认证情况'                                     
		        },                                                                
		        xAxis: {                                                          
		        	categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月 ', '十二月']
		        }, 
		        yAxis: {
			    	  min:0, // 定义最小值  
			          title: {
			              text: '数量：张'
			          }
			    },
			    credits: {
			          enabled:false
				},
		        series: data                                                                
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
  .show_count{
	 	color: #F00;
		font-weight: bold;
		font-size: 20px;
		padding: 5px;
  }
  .topCount{
		position: relative;
		top: -15px;
		left: 75%;
		width: 180px;
		border: 1px solid #c7c7c7;
		padding: 10px;
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
  	<div class="topCount">
  	共采集<span id="count" class="show_count"></span>张发票<br/>
  	共认证<span id="countrz" class="show_count" style="color:#22D69D;text-indent:4px"></span>张发票
  	</div>
    <div id="container" style="min-width:700px;height:400px;margin-top: -14px;"></div>
  </body>
</html>
