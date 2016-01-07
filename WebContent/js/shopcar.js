document.write("<script language='javascript' src='/community/js/humane.min.js'></script>");
document.write("<link rel='stylesheet' href='/community/css/jackedup.css'/>");
/***
 * 购物车操作模块
 * 
 */
var shopCart = function(window){
	
	"use strict";
	//全局变量
	// note new new Date("2020-12-23") 在ie下面报错，不支持这样的语法
	var items = [],cartName='shop_cart',expires = new Date( new Date().getTime()+60000*60*24 )
	,debug = true,decimal = 2;
	var options = {
		'cartName' : cartName, //cookie的名字
		'expires' : expires, //cookie失效的时间
		'debug' : debug,  //是否打印调试信息
		'decimal' : decimal, //钱的精确到小数点后的位数
		'callback' : undefined
	};
	//商品类
	/***
	 * @name item
	 * @example 
	   item(sku, name, price, quantity)
	 * @params {string} sku 商品的标示
	 * @params {string} name 商品的名字
	 * @param {number} price 商品的价格
	 * @param {number} quantity 商品的数量
	 */
	function item(sku, name, price, quantity,pics){
		this.sku = sku;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.pics = pics;
	}
	
	
	//暴露给外部的接口方法
	return {
		inited : false,
		init: function(option){
			//判断用户是否禁用cookie
			if(!window.navigator.cookieEnabled ){
				alert('您的浏览器不支持cookie无法使用购物车！,请设置允许设置cookie。');
				return false;
			}
			//从cookie中获取购物车中的数据
			this.inited = true;
			if(option){
				extend(options,option);
			}
			var cookie = getCookie(options.cartName);
			if(typeof cookie == 'undefined'){
				setCookie(options.cartName,'',options.expires); 
			}else{
				//每个item之间用&分开，item的属性之间用|分割
				var cookie = getCookie(options.cartName);
				if(cookie){
					var cItems = cookie.split('&');
					for(var i=0,l=cItems.length;i<l;i++){
						var cItem = cItems[i].split('|');
							var item = {};
							item.sku = cItem[0] || '';
							item.name = cItem[1] || '';
							item.price = cItem[2] || '';
							item.quantity = cItem[3] || '';
							item.pics=cItem[4] || '';
							items.push(item);
					};
				};
				
			};
		},
		findItem: function(sku){//根据sku标示查找商品
			//如果木有提供sku,则返回所有的item
			if(sku){
				for(var i=0,l=items.length;i<l;i++){
					var item = items[i];
					if(item.sku == sku){
						return item;
					}
				}
				return undefined;
			}else{
				return items;
			}
			
		},
		getItem : function(index){
			var item = items[index];
			return item;
		},
		getItemIndex : function(sku){ //获取item在items的数组下标
			for(var i=0,l=items.length;i<l;i++){
				var item = items[i];
				if(item.sku == sku){
					return i;
				}
			}
			//木有找到返回-1
			return -1;
		},
		addItem: function(item){ //增加一个新商品到购物车
			//添加一个商品
			if(this.findItem(item.sku)){
				if(options.debug){
					_log('商品已经存在了');
					return false;
				}
			}
			items.push(item);
			_saveCookie();
			_log('添加商品成功！');
			return true;
		},
		delItem: function(sku){ //从购物车中删除一个商品
			//删除一个商品
			var index = this.getItemIndex(sku);
			if(index > -1){
				items.splice(index,1);
				_saveCookie();
			}else{
				if(options.debug){
					_log('商品不存在');
					return false;
				}
			}
		},
		updateQuantity: function(item){ //更新商品的数量
			//更新一个商品
			var index = this.getItemIndex(item.sku);
			if(index > -1){
				items[index].quantity = item.quantity; 
				_saveCookie();
			}else{
				if(options.debug){
					_log('商品不存在');
					return false;
				}
			}
		},
		emptyCart: function(){
			//清空数组
			items.length = 0;
			_saveCookie();
		},
		checkout: function(){
			//点击结算后的回调函数
			if(options.callback){
				options.callback();
			}
		},
		getTotalCount: function(sku){
			//获取购物车商品的数量，如果传某个商品的id，那么就返回该商品的数量
			var totalCount = 0;
			if(sku){
				totalCount = (typeof this.findItem(sku) == 'undefined' ? 0 : this.findItem(sku).quantity );
			}else{
				for(var i=0,l=items.length;i<l;i++){
//					totalCount += (parseInt(items[i].quantity) == 'NaN' ? 0 : parseInt(items[i].quantity )) ;
					totalCount += 1;
				}
			}
			return totalCount;
		},
		getTotalPrice : function(sku){
			//获取购物车商品的总价格 ,如果传某个商品的id，那么就返回该商品的总价格
			var totalPrice = 0.0;
			if(sku){
				var num = parseInt((typeof this.findItem(sku) == 'undefined' ? 0 : this.findItem(sku).quantity )),
				price = parseFloat((typeof this.findItem(sku) == 'undefined' ? 0 : this.findItem(sku).price ));
				num = num== 'NaN' ? 0 : num;
				price = price == 'NaN' ? 0 : price;
				totalPrice = price * num;
			}else{
				for(var i=0,l=items.length;i<l;i++){
					totalPrice += (parseFloat(items[i].price ) * parseInt(items[i].quantity)); 
				}
			}
			return totalPrice.toFixed(options.decimal);
		},
		getCookie : getCookie,
		setCookie : setCookie
	};
	
	
	/**
	 * 设置cookie
	 * @name setCookie
	 * @example 
	   setCookie(name, value[, options])
	 * @params {string} name 需要设置Cookie的键名
	 * @params {string} value 需要设置Cookie的值
	 * @params {string} [path] cookie路径
	 * @params {Date} [expires] cookie过期时间
	 */
	function setCookie(name, value, options) {
		options = options || {};
		var expires = options.expires || null;
		var path = options.path || "/";
		var domain = options.domain || document.domain;
		var secure = options.secure || null;
		/**
		document.cookie = name + "=" + escape(value) 
		+ ((expires) ? "; expires=" + expires.toGMTString() : "") 
		+ "; path=" + path
		+ "; domain=" + domain ;
		+ ((secure) ? "; secure" : "");
		*/
		var str = name + "=" + encodeURIComponent(value)
		+ ((expires) ? "; expires=" + expires.toGMTString() : "") 
		+ "; path=/";
		document.cookie = str;
	};
	
	/**
	 * 获取cookie的值
	 * @name getCookie
	 * @example 
	   getCookie(name)
	 * @param {string} name 需要获取Cookie的键名
	 * @return {string|null} 获取的Cookie值，获取不到时返回null
	 */
	function getCookie(name) {
		var arr = document.cookie.match(new RegExp("(^| )" + name
				+ "=([^;]*)(;|$)"));
		if (arr != null) {
			return decodeURIComponent(arr[2]);
		}
		return undefined;
	};
	
	//***********************私有方法********************/
	function _saveCookie(){
		var i=0,l=items.length;
		if(l>0){
			var tItems = [];
			for(;i<l;i++){
				var item = items[i];
				tItems[i] = item.sku + '|' +item.name + '|' + item.price + '|' + item.quantity+ '|' + item.pics;
			};
			var str = tItems.join('&');
			setCookie(options.cartName, str, {expires:options.expires});
		}else{
			setCookie(options.cartName, '', {expires:options.expires});
		}
		
	};
	
	//***********************工具方法********************/
	//显示调试信息
	function _log(info){
		if(typeof console != 'undefined'){
			humane.info(info);
		}
	};
	//继承属性
	function extend(destination, source) {
		for ( var property in source) {
			destination[property] = source[property];
		}
	};
}(typeof window == 'undifined' ? this: window);



