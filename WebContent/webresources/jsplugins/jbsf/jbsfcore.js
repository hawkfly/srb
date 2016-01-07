/*使用jquery扩展的jbsf js通用插件*/
(function($){
	$.jbsf = window.jbsf = {
			version : 'V1.0',
			//扩展(1,默认参数   2,本地化扩展)
	        defaults: {},
	        //3,方法接口扩展
	        methods: {},
	        params:{},
	        //命名空间
	        //核心控件,封装了一些常用方法
	        core: {
	        	num: 0,
	        	contextpath: function(){//window.location.origin=http://localhost:8080
	        		var pathName = document.location.pathname;
	        	    var index = pathName.substr(1).indexOf("/");
	        	    var result = pathName.substr(0,index+1);
	        	    return result;
	        	},
	        	getParameter: function(name){
	        		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	        	    var r = window.location.search.substr(1).match(reg);
	        	    if (r != null) return unescape(r[2]); return null;
	        	},
	        	setParameter: function(param, value){
	        		/* var query = location.search.substring(1);
	        		 var p = new RegExp("(^|&"+param+")=[^&]*");
	        		 if(p.test(query)){
	        		    query = query.replace(p,"$1="+value);
	        		    location.search = '?'+query;
	        		 }else{
        		        if(query == ''){
        		            location.search = '?'+param+'='+value;
        		        }else{
        		            location.search = '?'+query+'&'+param+'='+value;
        		        }
	        		 }
	        		 alert(location.search);*/
	        	},
	        	ajaxLogin: function(formid, callback){//封装当前页面地址及用户指定参数请求AjaxLoginServlet
	        		//向ajax发送表单数据 $.post("test.php", $("#testform").serialize());
	        		$("#"+formid).append("<input type='hidden' name='requestpageuri' value='"+document.location.pathname+"'>");
	        		$.post($.jbsf.core.contextpath()+"/ajaxlogin",$("#"+formid).serialize(), callback);
	        	},
	        	httpLogin: function(fromid, redirecturl){//只需传入form id就会自动提交普通登录请求，当然还需注入跳转页面地址
	        		$("#"+formid).append("<input type='hidden' name='redirecturl' value='"+redirecturl+"'>");
	        		$("#"+formid).attr("action",$.jbsf.core.contextpath()+"/httplogin");
	        		$("#"+formid).submit();
	        	}
	        },
	        validate: {
	        	isDigit: function(str){
	        	    var patrn=/^\d*$/;
        		    if (patrn.test(str)) {   
        		      return true;
        		    } else {
        		      return false;
        		    }
        		    return false;
	        	}
	        },
	        //命名空间
	        //组件的集合
	        controls: {},
	        //plugin 插件的集合
	        plugins: {},
	        imageUploader: {
	        	//上传完插入待排序的图片对象
	        	insertImg2Sortpanel: function(){
	        		
	        	}
	        	//
	        },
	        util:/**
	         * 
	         */
	        {
	        	createCustomizeComponent : function(pagewinObj, contentjson, rowkey){
        			for(var item in contentjson){
        				if(rowkey != null){
        					pagewinObj.$("body").append("<input type='hidden' id='"+rowkey+"_"+item+"' name='"+item+"' class='"+rowkey+"' value='"+contentjson[item]+"'></input>");
        				}else{
        					pagewinObj.$("body").append("<input type='hidden' id='"+item+"' name='"+item+"' value='"+contentjson[item]+"'></input>");
        				}
        			}
	        	},
	        	getCustomizeComponentValue : function(componentid){
	        		var cvalue = $('#'+componentid).val();
	        		$('#'+componentid).remove();
	        		if(cvalue == null && parent != null){
	        			cvalue = parent.$('#'+componentid).val();
	        			parent.$('#'+componentid).remove();
	        		}
	        		return cvalue;
	        	},
	        	getCustomizeComponentRows : function(rowkey){
	        		var row = {};
	        		$("."+rowkey).each(function(){
	        			var key = this.name;
	        			var val = $(this).val();
	        			row[key] = val;
	        			$(this).remove();
	        		});
	        		
	        		return row;
	        	},
	        	getParentCustomizeComponentRows : function(rowkey){
	        		var row = {};
	        		if(parent != null){
	        			parent.$("."+rowkey).each(function(){
	        				var key = this.name;
		        			var val = $(this).val();
		        			row[key] = val;
		        			$(this).remove();
	        			});
	        		}else{
	        			alert("parent = " + parent);
	        			return null;
	        		}
	        		return row;
	        	},
	        	print:function(srcobj){
	        		for(var item in srcobj){
	        			document.write(item + " = " + srcobj[item] + "---->");
	        		}
	        	},
	        	md5:function(str){
	        		
	        	},
	        	/**
	        	 * 求开始时间和当前时间的差值，精确到天
	        	 * @param startime 开始时间，时间格式：2015-12-07 10:04:23
	        	 * @return 保留两位小数后的天，比如1.32天
	        	 */
	        	timediff:function(startime){
	        		var tmpBeginTime = new Date(startime.replace(/-/g, "\/")); //时间转换
	        		var tmpEndTime = new Date();
	        		var day = (tmpEndTime - tmpBeginTime) / (1000 * 60 * 60 * 24);
	        		return day.toFixed(2);
	        	}
	        },
	        //ztree控制
	        ztree:{},
	        wbcs:{
	        	getpageid: function(openerObj){//获取指定页面对象的页面编号
	        		if(openerObj != null){
	        			var locparams = openerObj.document.location.search;
	        			$.jbsf.wbcs.thisopener = openerObj;
	        		}else
	        			var locparams = document.location.search;
		    		var sublocparams = locparams.substring(locparams.indexOf('PAGEID'));
		    		var pageid = '';
		    		if(sublocparams.indexOf('&') == -1){
		    			pageid = sublocparams.substring(sublocparams.indexOf('=')+1);
		    		}else{
		    			pageid = sublocparams.substring(sublocparams.indexOf('=')+1, sublocparams.indexOf('&'));
		    		}
		    		$.jbsf.wbcs.parentpageid = pageid;
		    		return pageid;
	        	},
	        	refreshParentKeepOnPageNo: function(){//刷新打开页面的父页面数据并保持在当前分页上
	        		if($.jbsf.wbcs.thisopener != null && $.jbsf.wbcs.parentpageid != null){
		    			var parentpageid = $.jbsf.wbcs.parentpageid;
		    			var thisopener = $.jbsf.wbcs.thisopener;
		    		}else{
		    			var parentpageid = $.jbsf.wbcs.getpageid(opener);
		    			var thisopener = opener;
		    		}
		    		var url = thisopener.$('#'+parentpageid+'_url_id').attr("value");
		    		thisopener.$('body').load(document.location.origin + url);
	        	},
	        	/**
	        	 * AJAX抓取网页内容确认对话框
	        	 * url: example('/webresources/pages/newspreview.jsp')
	        	 * options: {okval, cancelval, title, width, height}
	        	 * pageid: 页面编号
	        	 * reportid: 单据编号
	        	 * params: 要抓取的JAVABEAN propertyName, 数组类型, example(['XWNR', 'ZYXX'])
	        	 * okfunc: 点击ok按键的实现方法
	        	 * cancelfunc: 点击取消按键实现方法
	        	 */
	        	ldconfirm: function(url, options, pageid, reportid, params, okfunc, cancelfunc){
	        		var prjname = $.jbsf.core.contextpath();
	        		var rtnstatus = 0;//0=保存并关闭当前页面(default),1=保存并继续编辑
	        		if(params != null){
	        			var datas = {};
	        			var datasArr = null;
	        			var componentguid = getComponentGuidById(pageid, reportid);
	        			if (WX_ALL_SAVEING_DATA != null){
	        				datasArr = WX_ALL_SAVEING_DATA[componentguid];
	        				if(datasArr == null)
	        				{
	        					return rtnstatus;
	        				}
        					for ( var i = 0; i < params.length; i++) {
								var item = params[i];
								//var guid = pageid + '_guid_' + reportid + '_' + item;
								datas[item] = datasArr[0][item];
								art.dialog.data(item,datas[item]);
							}
	        					
	        			}
	        		}
	        		art.dialog.open(prjname+url,{
	        			    title: options.title,
	        			    fixed: true,
	        			    width: 600,
	        			    height: 480,
	        			    id: $.jbsf.core.num++,
	        			    okVal: options.okval == null ? '确定' : options.okval,
	        			    cancelVal: options.cancelval == null ? '取消' : options.cancelval,
	        			    ok: function () {
	        			    	rtnstatus = okfunc(rtnstatus);
	        			    	return true;
	        			    },
	        			    cancel: function(){
	        			    	rtnstatus = cancelfunc(rtnstatus);
	        			    	return true;
	        			    }
	        			}, false);
	        		
	        		return rtnstatus;
	        	},
	        	openTab: function(url,title, parentabid, tabid){
	        		if(tabid == null){
	        			tabid = $.jbsf.util.uuidFast;
	        		}
					url = url + '&tabid='+tabid+'&parenttabid='+parentabid;
					if(parent != null){
						parent.f_addTab(tabid, title, url);
					}else{
						f_addTab(tabid, title, url);
					}
	        	},
	        	getPageid : function(){
	        		return $.jbsf.core.getParameter("PAGEID");
	        	}
	        }
	};
    //ztree的级联点击事件，用于级联wbcsReport
    //options[iframeid, iframelinkpageid, ztreenode, accessmode]
    $.jbsf.ztree.clickCascadeWbcsReport = function(options){
    	var pcode = '';
    	if(options.ztreenode.pId != null){
    		pcode = options.ztreenode.pId;
    	}
    	$("#"+options.iframeid).attr({src:$.jbsf.core.contextpath()+"/jbsf.sr?PAGEID="+ options.iframelinkpageid+"&txtcode="+options.ztreenode.id+"&txtpcode="+pcode+"&editReport_ACCESSMODE="+options.accessmode});
    };
    
    $.jbsf.wbcs.Report = {
		doSearchHidden : function(){
			
		}
    };
    
    Array.prototype.remove=function(dx)
    {
    	if(isNaN(dx)||dx>this.length){return false;}
    	for(var i=0,n=0;i<this.length;i++)
    	{
    	 if(this[i]!=this[dx])
    	 {
    		 this[n++]=this[i];
    	 }
    	}
    	this.length-=1;
    };
	
	String.prototype.endWith=function(str){
		if(str==null||str==""||this.length==0||str.length>this.length)
		  return false;
		if(this.substring(this.length-str.length)==str)
		  return true;
		else
		  return false;
		return true;
	};

	String.prototype.startWith=function(str){
		if(str==null||str==""||this.length==0||str.length>this.length)
		  return false;
		if(this.substr(0,str.length)==str)
		  return true;
		else
		  return false;
		return true;
	};
	
	String.prototype.trim=function(){
		return this.replace(/(^\s*)|(\s*$)/g, "");
	};
	
	String.prototype.ltrim=function(){
	 return this.replace(/(^\s*)/g,"");
	};
	String.prototype.rtrim=function(){
		return this.replace(/(\s*$)/g,"");
	};
	
	(function(){
		var CHARS = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
		$.jbsf.util.uuid = function(len, radix){
			var chars = CHARS, uuid = [], i;
		    radix = radix || chars.length;
		 
		    if (len) {
		      // Compact form
		      for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
		    } else {
		      // rfc4122, version 4 form
		      var r;
		 
		      // rfc4122 requires these characters
		      uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
		      uuid[14] = '4';
		 
		      // Fill in random data.  At i==19 set the high bits of clock sequence as
		      // per rfc4122, sec. 4.1.5
		      for (i = 0; i < 36; i++) {
		        if (!uuid[i]) {
		          r = 0 | Math.random()*16;
		          uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
		        }
		      }
		    }
		    return uuid.join('');
		};
		$.jbsf.util.uuidFast = function() {
		    var chars = CHARS, uuid = new Array(36), rnd=0, r;
		    for (var i = 0; i < 36; i++) {
		      if (i==8 || i==13 ||  i==18 || i==23) {
		        uuid[i] = '-';
		      } else if (i==14) {
		        uuid[i] = '4';
		      } else {
		        if (rnd <= 0x02) rnd = 0x2000000 + (Math.random()*0x1000000)|0;
		        r = rnd & 0xf;
		        rnd = rnd >> 4;
		        uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
		      }
		    }
		    return uuid.join('');
		};
		$.jbsf.util.uuidCompact = function() {
		    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		      var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
		      return v.toString(16);
		    });
		  };
	})();
})(jQuery)