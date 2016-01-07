function initCompanyRpt(pageid, reportid){
	var tdid = getComponentGuidById(pageid, reportid) + "_F_ISAVE";
	$("td[id ^='"+tdid+"__td']").each(function(){
		var isaveval = $(this).attr("value");
		if(isaveval == 1){
			$(this).css("background-color","#a4e0f1");
		}else{
			$(this).css("background-color","#fffe6b");
			$(this).css("font","#333");
		}
	});
	var tdidjt = getComponentGuidById(pageid, reportid) + "_F_ISJT";
	$("td[id ^='"+tdidjt+"__td']").each(function(){
		var isaveval = $(this).attr("value");
		if(isaveval == 1){
			$(this).css("background-color","#a4e0f1");
		}else{
			$(this).css("background-color","#fffe6b");
		}
	});
}



function initInvoiceRpt(pageid, reportid){
	var tdid = getComponentGuidById(pageid, reportid) + "_GFDM";
	$("td[id ^='"+tdid+"__td']").each(function(){
		var isaveval = $(this).attr("value");
		var id =this.id;
		$.ajax({
		    data: "get",
		    url: "jf/yj",
		    data: "key=" + isaveval,
		    cache: false,
		    async: false,
		    success: function (data) {
		    	if(data == 0 ){
					$("#"+id).css("background-color","#fffe6b");
				}
		    }
		});
		
	});
	
}