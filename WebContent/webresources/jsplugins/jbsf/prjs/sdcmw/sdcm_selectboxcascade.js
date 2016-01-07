//执行组件绑定
function bindselectFunc(pageid, reportid){
	var componentpreid = getComponentGuidById(pageid, reportid);
	var tohomesiteid = componentpreid + "_tohomesite";
	var tohomesiteczr = componentpreid + "_tohomesiteczr";
	var tothesectionid = componentpreid + "_tothesection";
	var tosectionczr = componentpreid + "_tosectionczr";
	//init
	var sectionid4homesite = $('#'+tohomesiteid).find('option:selected').attr('value');
	invokeServerAction('com.hawkfly.action.SelectCascadeAction4sdcm',{'sectionid':sectionid4homesite},function(xmlHttpObj){
		var rtnJson = eval('('+xmlHttpObj.responseText+')');
		for(var item in rtnJson){
			$('#'+tohomesiteczr).empty();
			$('#'+tohomesiteczr).html('<option selected=true value='+item+'>'+rtnJson[item]+'</option>');
		}
	});
	var sectionid4section = $('#'+tothesectionid).find('option:selected').attr('value');
	invokeServerAction('com.hawkfly.action.SelectCascadeAction4sdcm',{'sectionid':sectionid4section,'towhere':'thesection'},function(xmlHttpObj){
		var rtnJson = eval('('+xmlHttpObj.responseText+')');
		for(var item in rtnJson){
			$('#'+tosectionczr).empty();
			$('#'+tosectionczr).html('<option selected=true value='+item+'>'+rtnJson[item]+'</option>');
		}
	});
	//service
	$("#"+tohomesiteid).attr('onblur','').attr('onmouseover','').attr('onfocus','').attr('onmouseout','');
	$("#"+tohomesiteid).attr('onchange',
			"var sectionid = $(this).children('option:selected').val();"+
			"invokeServerAction('com.hawkfly.action.SelectCascadeAction4sdcm',{'sectionid':sectionid},function(xmlHttpObj){" +
			"var rtnJson = eval('('+xmlHttpObj.responseText+')');" +
			"for(var item in rtnJson){" +
			"$('#"+tohomesiteczr+"').empty();" +
			"$('#"+tohomesiteczr+"').html('<option selected=true value='+item+'>'+rtnJson[item]+'</option>');" +
			"}" +
			"});"
	);
	
	$("#"+tothesectionid).attr('onblur','').attr('onmouseover','').attr('onfocus','').attr('onmouseout','');
	$("#"+tothesectionid).attr('onchange',
			"var sectionid = $(this).children('option:selected').val();"+
			"invokeServerAction('com.hawkfly.action.SelectCascadeAction4sdcm',{'sectionid':sectionid,'towhere':'thesection'},function(xmlHttpObj){" +
			"var rtnJson = eval('('+xmlHttpObj.responseText+')');" +
			"for(var item in rtnJson){" +
			"$('#"+tosectionczr+"').empty();" +
			"$('#"+tosectionczr+"').html('<option selected=true value='+item+'>'+rtnJson[item]+'</option>');" +
			"}" +
			"});"
	);
	//$("#"+tohomesiteid)[0].onchange=function(){alert(1);}
	
	//newstxtPage_guid_newstxtRpt_tohomesite
}