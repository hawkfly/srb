//地区发票认证地址页面,下拉框切换时调用
function identifyAddrChange(selectBoxOBJ){
	search({pageid:'identifyAddrPage',reportid:'identifyAddrRpt'});
}
//认证通道配置关系页面,下拉框切换时调用
function identifyChannelChange(selectBoxOBJ){
	search({pageid:'identifyChannelPage',reportid:'identifyChannelRpt'});
}
//发票格式配置关系页面,下拉框切换时调用
function invoiceRelationChange(selectBoxOBJ){
	search({pageid:'invoiceFormatRelationPage',reportid:'invoiceFormatRelationRpt'});
}
//发票格式页面,下拉框切换时调用
function invoiceFormatChange(selectBoxOBJ){
	search({pageid:'invoiceFormatPage',reportid:'invoiceFormatRpt'});
}
//审核状态,下拉框切换时调用
function checkstatus(selectBoxOBJ){
	search({pageid:'enterpriseAuditPage',reportid:'enterpriseAuditSecRpt'});
	setTimeout("checkTimeOut()",100);
}
function checkTimeOut()
{
	var obj = document.getElementById('enterpriseAuditPage_guid_enterpriseAuditSecRpt_condition_ivtypeCdt');
	var val = obj.value;
	var input = document.getElementsByTagName('input');
	if(val=='1'||val=='2')
	{
		for(var i =0;i<input.length;i++)
		{
			if(input[i].value=='审批')
			{
				//alert($);
				//input[i].disabled='disabled';
				//$(input[i]).addClass("hidden"); 
				$(input[i]).attr("class", "hidden");
				
			}
		}
	}
}
//审核类型，下拉框切换时调用
function checkType(selectBoxOBJ){
	search({pageid:'enterpriseAuditPage',reportid:'enterpriseAuditSecRpt'});
	 setTimeout("checkTypeTimeOut()",100);	

}
//企业审核页面，屏蔽按钮 时间延迟100毫秒
//enterpriseAuditPage_guid_enterpriseAuditSecRpt_condition_ivtypeCdt
function checkTypeTimeOut(){
	var obj = document.getElementById('enterpriseAuditPage_guid_enterpriseAuditSecRpt_condition_checkType');
	var val = obj.value;
	var input = document.getElementsByTagName('input');
	if(val=='N'||val=='O')
	{
		for(var i =0;i<input.length;i++)
		{
			if(input[i].value=='审批')
			{
				//alert($);
				//input[i].disabled='disabled';
				//$(input[i]).addClass("hidden"); 
				$(input[i]).attr("class", "hidden");
				
			}
		}
	}	
}
//用户企业关系审核，下拉框时调用
// 
function usercheckstatus(selectBoxOBJ){
	search({pageid:'userEnterpriseAuditPage',reportid:'userEnterpriseAuditRpt'});
	setTimeout("userCheckTimeOut()", 100);
}
//用户企业关系审核,屏蔽按钮时的时间延迟
function userCheckTimeOut()
{
	var obj = document.getElementById('userEnterpriseAuditPage_guid_userEnterpriseAuditRpt_condition_ivtypeCdt');
	var val = obj.value;
	var input = document.getElementsByTagName('input');
	if(val=='1'||val=='2')
	{
		for(var i =0;i<input.length;i++)
		{
			if(input[i].value=='审批')
			{
				//alert($);
				//input[i].disabled='disabled';
				//$(input[i]).addClass("hidden"); 
				$(input[i]).attr("class", "hidden");
				
			}
		}
	}
}
//管理员审核，下拉框时调用
// 
function manacheckstatus(selectBoxOBJ){
	search({pageid:'manaEnterpriseAuditPage',reportid:'manaEnterpriseAuditRpt'});
	setTimeout("manaCheckTimeOut()", 100);
}
//管理员审核，屏蔽按钮时的时间延迟100毫秒
function manaCheckTimeOut()
{
	var obj = document.getElementById('manaEnterpriseAuditPage_guid_manaEnterpriseAuditRpt_condition_ivtypeCdt');
	var val = obj.value;
	var input = document.getElementsByTagName('input');
	if(val=='1'||val=='2')
	{
		for(var i =0;i<input.length;i++)
		{
			if(input[i].value=='审批')
			{
				//alert($);
				//input[i].disabled='disabled';
				//$(input[i]).addClass("hidden"); 
				$(input[i]).attr("class", "hidden");
				
			}
		}
	}
}
//单位字典页面,查询框的enter事件
function compantEnter(selectBoxOBJ){
	search({pageid:'companyPage',reportid:'companyRpt'});
}
function compantEnter2(selectBoxOBJ){
	search({pageid:'companyHelpPage',reportid:'companyHelpRpt'});
}
//用户企业关系审核，查询框enter事件
function userCompanyEnter(selectBoxOBJ){
	search({pageid:'userEnterpriseAuditPage',reportid:'userEnterpriseAuditRpt'});
}
//用户字典页面,查询框的enter事件
function userEnter(selectBoxOBJ){
	search({pageid:'userPage',reportid:'userRpt'});
}
//用户单位关系字典页面,查询框的enter事件
function userCompanyEnter(selectBoxOBJ){
	search({pageid:'userCompanyPage',reportid:'userCompanyRpt'});
}
//税务用户名密码页面,查询框的enter事件
function userPwdEnter(selectBoxOBJ){
	search({pageid:'swusernamepwd1Page',reportid:'swusernamepwd1Rpt'});
}
//角色维护界面，查询框enter事件
function searchRole(selectBoxOBJ){
	search({pageid:'selectAuthes2RolePage',reportid:'rolelstRpt'});
}
//用户维护与赋权enter事件
function userMana(selectBoxOBJ){
	search({pageid:'roles2userPage',reportid:'roles2userRpt'});
}
