<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PC跳转银联页面接入示例</title>

<style type="text/css">
<!--
html,body  
{  
    width:100%;  
    height:100%;  
} 

a:link {
	color: #003399;
}
.px12 {
	font-size: 12px;
}
a:visited {
	color: #003399;
}
a:hover {
	color: #FF6600;
}
.px14 {
	font-size: 14px;
}
.px12hui {
	font-size: 12px;
	color: #999999;
}
.STYLE2 {font-size: 14px; font-weight: bold; }
-->
</style>

<script type="text/javascript">  
    function onPayForm(){  
       document.all.payForm.submit();
  }
    function displayConsumeDiv(){
    	document.getElementById("consumeDiv").style.display = "block";
    	document.getElementById("consumeUndoDiv").style.display = "none";
    }
    
    function displayConsumeUndoDiv(){
    	document.getElementById("consumeDiv").style.display = "none";
    	document.getElementById("consumeUndoDiv").style.display = "block";
    }
</script>

</head>
<body>
<table width="100%" height="40%" border="1" align="center" cellpadding="0" cellspacing="0" >
<tr>
<td align="left" width="35%">
<h3> PC跳转银联页面接入示例 </h3>
<h4>前台跳转消费</h4>
<ul>
<li><a href="#" target="blank" onclick="displayConsumeDiv();">跳转网关页面支付</a></li>

</ul>

<!--
<h4>前台跳转预授权</h4>
<ul>
<li><a href="<%request.getContextPath();%>/ACPSample-PCGate-UTF8/form_6_7_1_AuthDeal_Front" target="blank">跳转页面预授权</a></li>
</ul>
-->

<h4>后续交易（根据商户需求自行选择是否测试）</h4>
<ul>
<li><a href="#" onclick="displayConsumeUndoDiv();" target="blank">消费撤销</a></li>
</br>
<li><a href="<%request.getContextPath();%>/ACPSample-PCGate-UTF8/form_6_4_Refund" target="blank">退货</a></li>

<!--
<li><a href="<%request.getContextPath();%>/ACPSample-PCGate-UTF8/form_6_7_2_AuthUndo" target="blank">预授权撤销</a></li>
<li><a href="<%request.getContextPath();%>/ACPSample-PCGate-UTF8/form_6_7_3_AuthFinish" target="blank">预授权完成</a></li>
<li><a href="<%request.getContextPath();%>/ACPSample-PCGate-UTF8/form_6_7_4_AutnFinishUndo" target="blank">预授权完成撤销</a></li>
-->
</br>
<li><a href="<%request.getContextPath();%>/ACPSample-PCGate-UTF8/form_6_5_Query" target="blank">交易状态查询</a></li>
</br>
<li><a href="<%request.getContextPath();%>/ACPSample-PCGate-UTF8/form_6_6_FileTransfer" target="blank">对账文件下载（文件传输类交易）</a></li>
</br>
</ul>
</td>

<td align="left" width="65%">
<div id="consumeDiv" style="display:block">
  <table width="700" height="30" border="0" align="left" cellpadding="0" cellspacing="1" bgcolor="#DDCC00">
<form action="<%request.getContextPath();%>/ACPSample-PCGate-UTF8/form_6_2_FrontConsume2" method="post" id="payForm" target="_blank">
  <tr> <td height="30" ><span class="STYLE2">跳转网关页面支付,填写订单信息</span></td></tr>
  <tr>
  <td width="40%">商户号:</td>
  <td> <input name="merId" value="777290058110097"></input></td>
  </tr>
   <tr>
  <td width="40%">交易金额:</td>
  <td> <input name="txnAmt" value="1000">(分)</input></td>
  </tr>
  <tr height="30"></tr>
   <tr>
   <td>    </td>
  <td> <input name="button" value="支  付" type="button"  onClick="onPayForm();"></input></td>
  </tr>
 </table>
</form>
</div>

<div id="consumeUndoDiv" style="display:none">
  <table width="700" height="30" border="0" align="left" cellpadding="0" cellspacing="1" bgcolor="#DDCC00">
<form action="<%request.getContextPath();%>/ACPSample-PCGate-UTF8/form_6_3_ConsumeUndo" method="post" id="payUndoForm" target="_blank">
  <tr> <td height="30" ><span class="STYLE2">消费撤销</span></td></tr>
  <tr>
  <td width="40%">被撤销交易的queryId:</td>
  <td> <input name="queryId" value=""></input></td>
  </tr>
  <tr height="30"></tr>
   <tr>
   <td>    </td>
  <td> <input name="button" value="撤  销" type="button"  onClick="onPayForm();"></input></td>
  </tr>
 </table>
</form>
</div>

</td>
</tr>

</table>

</body>
</html>