<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.wbcs.system.ReportRequest,com.wbcs.util.Consts,java.util.*"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目期报编制</title>
<style type="text/css">
.tabletd{
	position:relative;
	top:10px;
	left:35px;
}
.tongyi{
	position:relative;
	left:-8px;
	top:-5px;
	width:200px;
}
.zbnrwd{
	position:relative;
	margin-left:35px;
}
</style>
</head>
<body>
    <table>
		<tr class="tabletd">
			<td><div class='label' align="left" style="width:70px; height:30px">单据编号：</div></td>
			<td class="tongyi"><wx:data col="F_DJBH" showdata="true" showlabel="false"></wx:data></td>
            
            <td><div class='label' align="left" style="width:70px; height:30px">年&nbsp;&nbsp;度：</div></td>
			<td class="tongyi"><wx:data col="F_ND" showdata="true" showlabel="false"></wx:data></td>
            
            <td><div class='label' align="left" style="width:70px; height:30px">期&nbsp;&nbsp;次：</div></td>
			<td class="tongyi"><wx:data col="F_QH" showdata="true" showlabel="false"></wx:data></td>
            
            <td><div class='label' align="left" style="width:70px; height:30px">起始日期：</div></td>
			<td class="tongyi"><wx:data col="F_QSRQ" showdata="true" showlabel="false"></wx:data></td>
            
            <td><div class='label' align="left" style="width:70px; height:30px">结束日期：</div></td>
			<td class="tongyi"><wx:data col="F_ZZRQ" showdata="true" showlabel="false"></wx:data></td>
		</tr>
        <tr class="tabletd">
            <td><div class='label' align="left" style="width:70px; height:30px">录&nbsp;入&nbsp;人：</div></td>
			<td class="tongyi"><wx:data col="F_LRRXM" showdata="true" showlabel="false"></wx:data></td>
            
            <td><div class='label' align="left" style="width:70px; height:30px">录入日期：</div></td>
			<td class="tongyi"><wx:data col="F_LRRQ" showdata="true" showlabel="false"></wx:data></td>
            
            <td><div class='label' align="left" style="width:70px; height:30px">提&nbsp;交&nbsp;人：</div></td>
			<td class="tongyi"><wx:data col="F_TJRXM" showdata="true" showlabel="false"></wx:data></td>
            
            <td><div class='label' align="left" style="width:70px; height:30px">提交日期：</div></td>
			<td class="tongyi"><wx:data col="F_TJRQ" showdata="true" showlabel="false"></wx:data></td>
            
            <td><div class='label' align="left" style="width:70px; height:30px">审&nbsp;核&nbsp;人：</div></td>
			<td class="tongyi"><wx:data col="F_SHRXM" showdata="true" showlabel="false"></wx:data></td>
		</tr>
        <tr class="tabletd">
            <td><div class='label' align="left" style="width:70px; height:30px">审核日期：</div></td>
			<td class="tongyi"><wx:data col="F_SHRQ" showdata="true" showlabel="false"></wx:data></td>
            
            <td><div class='label' align="left" style="width:70px; height:30px">单据状态：</div></td>
			<td class="tongyi"><wx:data col="F_DJZT" showdata="true" showlabel="false"></wx:data></td>

			<td><div align="left">备&nbsp;&nbsp;注：</div></td>
			<td  class="tongyi"><wx:data col="F_NOTE" showdata="true" showlabel="false"></wx:data></td>
        </tr>
	</table>
    <p>&nbsp;</p>
    <div class="zbnrwd"><h2>周报内容：</h2></div>
    <div class="tabletd">
        <wx:data col="F_BZGZNR" showdata="true" showlabel="false"></wx:data>
    </div>
</body>
</html>