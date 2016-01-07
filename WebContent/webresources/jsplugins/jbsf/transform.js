	function show_upload_grid(){
		$("#grid_upload").jqGrid({
			url:"",
			datatype:"json", //数据来源，本地数据
			mtype:"POST",//提交方式
			height:400,//高度，表格高度。可为数值、百分比或'auto'
			//width:1000,//这个宽度不能为百分比
			autowidth:true,
			colNames:['图片', '图片', '图片','上传日期','图片大小'],
			colModel:[
			    //{name:'id',index:'id', width:'10%', align:'center' },
			    {name:'num',index:'createDate', width:'20%',align:'center'},
			    {name:'phoneNo',index:'phoneNo', width:'15%',align:'center'},
			    {name:'cardNo',index:'cardNo', width:'20%', align:"center"},
			    {name:'remark',index:'remark', width:'35%', align:"left", sortable:false},
			    {name:'del',index:'del', width:'10%',align:"center", sortable:false}
			],
			rownumbers:true,//添加左侧行号
			altRows:true,//设置为交替行表格,默认为false
			//sortname:'createDate',
			//sortorder:'asc',
			viewrecords: true,//是否在浏览导航栏显示记录总数
			rowNum:15,//每页显示记录数
			rowList:[15,20,25],//用于改变显示行数的下拉列表框的元素数组。
			jsonReader:{
			    id: "blackId",//设置返回参数中，表格ID的名字为blackId
			    repeatitems : false
			},
			pager:$('#grid_pager_upload')
		});
	}
	
	
	
	function show_transform_grid(){
		$("#grid_transform").jqGrid({
			url:"",
			datatype:"json", //数据来源，本地数据
			mtype:"POST",//提交方式
			height:400,//高度，表格高度。可为数值、百分比或'auto'
			//width:1000,//这个宽度不能为百分比
			autowidth:true,//自动宽
			colNames:['发票', '发票', '发票','上传日期','图片大小'],
			colModel:[
			    //{name:'id',index:'id', width:'10%', align:'center' },
			    {name:'num',index:'createDate', width:'20%',align:'center'},
			    {name:'phoneNo',index:'phoneNo', width:'15%',align:'center'},
			    {name:'cardNo',index:'cardNo', width:'20%', align:"center"},
			    {name:'remark',index:'remark', width:'35%', align:"left", sortable:false},
			    {name:'del',index:'del', width:'10%',align:"center", sortable:false}
			],
			rownumbers:true,//添加左侧行号
			altRows:true,//设置为交替行表格,默认为false
			//sortname:'createDate',
			//sortorder:'asc',
			viewrecords: true,//是否在浏览导航栏显示记录总数
			rowNum:15,//每页显示记录数
			rowList:[15,20,25],//用于改变显示行数的下拉列表框的元素数组。
			jsonReader:{
			    id: "blackId",//设置返回参数中，表格ID的名字为blackId
			    repeatitems : false
			},
			pager:$('#grid_pager_transform')
		});
	}