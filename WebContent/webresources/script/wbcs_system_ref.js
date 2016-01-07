var WX_GUID_SEPERATOR = "_guid_";
var ISOPERA = (navigator.userAgent.toLowerCase().indexOf("opera") != -1);
var isChrome = (navigator.userAgent.indexOf("Chrome") !== -1);
var isIE = (navigator.userAgent.toLowerCase().indexOf("msie") != -1);
var WX_UPDATE_ALLDATA;
var WX_IS_SAVE;
var WX_showProcessingBar = true;
var WX_serverUrl;

function refreshComponent(a, b) {
	WX_serverUrl = paramdecode(a);
	WX_serverUrl = replaceUrlParamValue(WX_serverUrl, "WX_ISAJAXLOAD", "true");
	if (!isHasIgnoreReportsSavingData(WX_serverUrl, b)) {
		loadPage("ok")
	} else {
		//wx_confirm("是否放弃对报表数据的修改？!", null, null, null, loadPage)
		loadPage("ok")
	}
}

function loadPage(e) {
	WX_IS_SAVE = false;
	if (!wx_isOkConfirm(e)) {
		return false
	}
	var b = WX_serverUrl;
	var d = new Object();
	d.serverUrl = b;
	var a = getParamValueFromUrl(b, "PAGEID");
	if (a == "") {
		wx_warn("在URL中没有取到PAGEID，无法加载");
		return
	}
	d.pageid = a;
	var g = getParamValueFromUrl(b, "refreshComponentGuid");
	if (g == null || g == "") {
		g = a
	} else {
		if (g.indexOf("[OUTERPAGE]") == 0) {
			b = replaceUrlParamValue(b, "refreshComponentGuid", null);
			b = replaceUrlParamValue(b, "WX_ISOUTERPAGE", "true")
		}
	}
	d.refreshComponentGuid = g;
	var h = getParamValueFromUrl(b, "SLAVE_REPORTID");
	d.slave_reportid = h;
	var f = b.split("?");
	if (f == null || f.length <= 1) {
		f[1] = ""
	} else {
		if (f.length > 2) {
			for (var c = 2; c < f.length; c = c + 1) {
				f[1] = f[1] + "?" + f[c]
			}
		}
	}
	XMLHttpREPORT.sendReq("POST", f[0], f[1], callBack, onErrorMethod, d);
	if (WX_showProcessingBar) {
		displayLoadingMessage()
	}
	WX_showProcessingBar = true;
	closeAllColFilterResultSpan()
}
var responseStateCode = {
	NONREFRESHPAGE: 0,
	FAILED: -1,
	SUCCESS: 1
};
var callback_jsonResultsObj = null;

function callBack(o, a) {
	hideLoadingMessage();
	WX_showProcessingBar = true;
	var i = o.responseText;
	var c = a.pageid;
	var d = a.serverUrl;
	var l = a.refreshComponentGuid;
	var h = i.indexOf("<RESULTS_INFO-" + c + ">");
	var g = i.indexOf("</RESULTS_INFO-" + c + ">");
	var k = null;
	if (h >= 0 && g >= 0 && g > h) {
		k = i.substring(h + ("<RESULTS_INFO-" + c + ">").length, g);
		i = i.substring(0, h) + i.substring(g + ("</RESULTS_INFO-" + c + ">").length)
	}
	callback_jsonResultsObj = getObjectByJsonString(k);
	if (callback_jsonResultsObj == null) {
		wx_error("更新页面数据失败");
		return
	}
	var n = null;
	if (l.indexOf("[DYNAMIC]") == 0) {
		l = callback_jsonResultsObj.dynamicRefreshComponentGuid;
		n = callback_jsonResultsObj.dynamicSlaveReportId
	} else {
		n = a.slave_reportid
	}
	if (n != null && n != "") {
		l = getComponentGuidById(c, n)
	}
	var f = l;
	if (f.indexOf("[OUTERPAGE]") == 0) {
		f = f.substring("[OUTERPAGE]".length);
		l = c
	}
	if (callback_jsonResultsObj.errormess != null && callback_jsonResultsObj.errormess != "") {
		wx_error(callback_jsonResultsObj.errormess);
		return
	}
	if (callback_jsonResultsObj.statecode == responseStateCode.FAILED) {
		if (callback_jsonResultsObj.warnmess != null && callback_jsonResultsObj.warnmess != "") {
			wx_warn(callback_jsonResultsObj.warnmess)
		}
		return
	}
	if (callback_jsonResultsObj.statecode != responseStateCode.NONREFRESHPAGE) {
		var p = document.getElementById("WX_CONTENT_" + f);
		if (p == null) {
			wx_error("更新页面失败");
			return
		}
		if (f != l) {
			if (p.outerHTML != null && p.outerHTML != undefined) {
				p.outerHTML = i
			} else {
				p.innerHTML = i
			}
		} else {
			var e = '<span id="WX_CONTENT_' + l + '">';
			var j = "</span>";
			var m = i.indexOf(e);
			if (m >= 0) {
				i = i.substring(m + e.length);
				m = i.lastIndexOf(j);
				if (m > 0) {
					i = i.substring(0, m)
				}
			}
			p.innerHTML = i
		}
		if (n == null || n == "") {
			var b = document.getElementById(c + "_url_id");
			b.setAttribute("value", callback_jsonResultsObj.pageurl);
			if (callback_jsonResultsObj.pageEncodeUrl != null && callback_jsonResultsObj.pageEncodeUrl != "") {
				b.setAttribute("encodevalue", callback_jsonResultsObj.pageEncodeUrl)
			}
		}
	}
	callback_warn()
}

function callback_warn() {
	if (callback_jsonResultsObj.warnmess == null || callback_jsonResultsObj.warnmess == "") {
		callback_alert()
	} else {
		if (WXConfig.prompt_dialog_type == "ymprompt") {
			wx_warn(callback_jsonResultsObj.warnmess, {
				handler: callback_alert
			})
		} else {
			wx_warn(callback_jsonResultsObj.warnmess, null, null, null, null);
			callback_alert()
		}
	}
}

function callback_alert() {
	if (callback_jsonResultsObj.alertmess == null || callback_jsonResultsObj.alertmess == "") {
		callback_success()
	} else {
		if (WXConfig.prompt_dialog_type == "ymprompt") {
			wx_alert(callback_jsonResultsObj.alertmess, {
				handler: callback_success
			})
		} else {
			wx_alert(callback_jsonResultsObj.alertmess, null, null, null, null);
			callback_success()
		}
	}
}

function callback_success() {
	if (callback_jsonResultsObj.successmess == null || callback_jsonResultsObj.successmess == "") {
		doPostCallback()
	} else {
		if (WXConfig.prompt_dialog_type == "ymprompt") {
			wx_success(callback_jsonResultsObj.successmess, {
				handler: doPostCallback
			})
		} else {
			wx_success(callback_jsonResultsObj.successmess, null, null, null, null);
			doPostCallback()
		}
	}
}

function doPostCallback() {
	var e = callback_jsonResultsObj.updateReportGuids;
	if (e != null && e != "") {
		var d;
		for (var b = 0; b < e.length; b = b + 1) {
			d = e[b].value;
			if (WX_selectedTrObjs && WX_selectedTrObjs[d]) {
				delete WX_selectedTrObjs[d]
			}
			if (WX_startTrIdxMap && WX_startTrIdxMap[d]) {
				delete WX_startTrIdxMap[d]
			}
			if (WX_Current_Slave_TrObj && WX_Current_Slave_TrObj[d]) {
				delete WX_Current_Slave_TrObj[d]
			}
			if (WX_UPDATE_ALLDATA && WX_UPDATE_ALLDATA[d]) {
				delete WX_UPDATE_ALLDATA[d]
			}
		}
	}
	var c = callback_jsonResultsObj.onloadMethods;
	if (c && c != "") {
		var a;
		for (var b = 0; b < c.length; b = b + 1) {
			a = c[b];
			if (!a || !a.methodname) {
				continue
			}
			if (a.methodparams) {
				a.methodname(a.methodparams)
			} else {
				a.methodname()
			}
		}
	}
	if (e != null && e != "") {
		for (var b = 0; b < e.length; b = b + 1) {
			d = e[b].value;
			if (WX_ALL_SAVEING_DATA && WX_ALL_SAVEING_DATA[d]) {
				delete WX_ALL_SAVEING_DATA[d]
			}
		}
	}
}

function onErrorMethod(b, a) {
	hideLoadingMessage();
	if (WXConfig.load_error_message != null && WXConfig.load_error_message != "") {
		wx_error(WXConfig.load_error_message)
	}
}

function isHasIgnoreReportsSavingData(c, n) {
	if (WX_UPDATE_ALLDATA == null || isEmptyMap(WX_UPDATE_ALLDATA)) {
		return false
	}
	var b = getParamValueFromUrl(c, "PAGEID");
	var h = getParamValueFromUrl(c, "refreshComponentGuid");
	var m = getParamValueFromUrl(c, "SLAVE_REPORTID");
	var e = getParamValueFromUrl(c, "WX_ISREFRESH_BY_MASTER");
	if (e === "true") {
		return false
	}
	var a = new Array();
	var o = new Object();
	if (m != null && m != "") {
		o[m] = "true"
	} else {
		if (h.indexOf("[DYNAMIC]") == 0) {
			var g = h.substring("[DYNAMIC]".length);
			var f = g.split(";");
			for (var d = 0; d < f.length; d++) {
				if (f[d] == null || f[d] == "") {
					continue
				}
				var l = getComponentMetadataObj(getComponentGuidById(b, f[d]));
				if (l != null) {
					if (l.refreshComponentGuid == null || l.refreshComponentGuid == "") {
						a[a.length] = getComponentGuidById(b, f[d])
					} else {
						a[a.length] = l.refreshComponentGuid
					}
				}
			}
		} else {
			if (h == null || h == "" || h.indexOf("[OUTERPAGE]") == 0) {
				a[a.length] = b
			}
		}
		a[a.length] = h;
		getAllRefreshReportids(b, a, o)
	}
	if (n == null) {
		n = new Object()
	}
	var k = new Array();
	for (var j in o) {
		if (n[j] == "true") {
			continue
		}
		k[k.length] = getComponentGuidById(b, j)
	}
	if (k.length == 0) {
		return false
	}
	for (var d = 0; d < k.length; d++) {
		if (WX_UPDATE_ALLDATA[k[d]] != null && WX_UPDATE_ALLDATA[k[d]] != "") {
			return true
		}
	}
	return false
}

function getAllRefreshReportids(d, a, o) {
	if (a == null || a.length == 0) {
		return
	}
	var c;
	for (var h = 0; h < a.length; h++) {
		c = a[h];
		var e = getComponentMetadataObj(c);
		if (e == null) {
			continue
		}
		if (e.componentTypeName == "application.report") {
			o[e.componentid] = "true";
			var n = e.metaDataSpanObj.getAttribute("dependingChildReportIds");
			if (n != null && n != "") {
				var l = n.split(";");
				for (var g = 0; g < l.length; g++) {
					if (l[g] == null || l[g] == "") {
						continue
					}
					o[l[g]] = "true";
					getAllInheritDependingChildReportIds(d, l[g], o)
				}
			}
		} else {
			var f = e.metaDataSpanObj.getAttribute("childComponentIds");
			if (f != null && f != "") {
				var b = f.split(";");
				for (var h = 0, k = b.length; h < k; h++) {
					if (b[h] == null || b[h] == "") {
						continue
					}
					var m = new Array();
					m[m.length] = getComponentGuidById(d, b[h]);
					getAllRefreshReportids(d, m, o)
				}
			}
		}
	}
}

function isHasIgnoreSlaveReportsSavingData(f) {
	if (WX_UPDATE_ALLDATA == null || isEmptyMap(WX_UPDATE_ALLDATA)) {
		return false
	}
	var b = getPageIdByComponentGuid(f);
	var d = getComponentIdByGuid(f);
	var a = new Object();
	getAllInheritDependingChildReportIds(b, d, a);
	var c;
	for (var e in a) {
		c = getComponentGuidById(b, e);
		if (WX_UPDATE_ALLDATA[c] != null && WX_UPDATE_ALLDATA[c] != "") {
			return true
		}
	}
	return false
}

function getAllInheritDependingChildReportIds(a, c, h) {
	var b = getComponentGuidById(a, c);
	var f = getReportMetadataObj(b);
	if (f == null) {
		return
	}
	var g = f.metaDataSpanObj.getAttribute("dependingChildReportIds");
	if (g == null || g == "") {
		return
	}
	var e = g.split(";");
	for (var d = 0; d < e.length; d++) {
		if (e[d] == null || e[d] == "") {
			continue
		}
		h[e[d]] = "true";
		getAllInheritDependingChildReportIds(a, e[d], h)
	}
}

function hasEditDataForSavingRow(f) {
	if (f == null) {
		return false
	}
	var e = f.getAttribute("id");
	if (e == null || e.indexOf("_tr_") < 0) {
		return false
	}
	var a = f.getElementsByTagName("TD");
	if (a == null || a.length == 0) {
		return false
	}
	var h = f.getAttribute("EDIT_TYPE");
	if (h && h == "add") {
		var j;
		var b;
		var g;
		for (var c = 0, d = a.length; c < d; c = c + 1) {
			j = a[c];
			b = j.getAttribute("value_name");
			if (b == null || b == "") {
				continue
			}
			g = getColConditionValueByParentElementObj(j);
			if (g != null && g != "") {
				return true
			}
		}
		return false
	} else {
		return isChangedForThisEditRow(a)
	}
}

function isChangedForThisEditRow(c) {
	if (c == null || c.length == 0) {
		return false
	}
	var b;
	var a;
	var e;
	var f;
	for (var d = 0; d < c.length; d = d + 1) {
		b = c[d];
		a = b.getAttribute("value_name");
		if (a == null || a == "") {
			continue
		}
		e = getColConditionValueByParentElementObj(b);
		f = b.getAttribute("oldvalue");
		if (e == null) {
			e = ""
		}
		if (f == null) {
			f = ""
		}
		if (e != f) {
			return true
		}
	}
	return false
}

function searchReportData(k, j, x) {
	var g = getComponentGuidById(k, j);
	var o = getReportMetadataObj(g);
	var p = o.metaDataSpanObj.getAttribute("validateSearchMethod");
	if (p != null && p != "") {
		var h = getObjectByJsonString(p);
		var l = h.method(o);
		if (!l) {
			return
		}
	}
	var d = getComponentUrl(k, o.refreshComponentGuid, o.slave_reportid);
	if (d == null) {
		return
	}
	var c = document.getElementsByName("font_" + g + "_conditions");
	if (c != null) {
		var a = null;
		var f = null;
		var m = null;
		var w = null;
		for (var q = 0, r = c.length; q < r; q = q + 1) {
			a = c[q];
			m = a.getAttribute("value_name");
			if (m == null || m == "") {
				continue
			}
			w = a.getAttribute("iteratorindex");
			if (w != null && w != "" && parseInt(w, 10) >= 0) {
				m = m + "_" + parseInt(w, 10)
			}
			f = getColConditionValueByParentElementObj(a);
			if (f == null || f == "") {
				f = null
			} else {
				f = encodeURIComponent(f)
			}
			d = replaceUrlParamValue(d, m, f);
			var t = a.getAttribute("innerlogicid");
			if (t != null && t != "") {
				d = replaceUrlParamValue(d, t, getConditionSelectedValue(t, a.getAttribute("innerlogicinputboxtype")))
			}
			var u = a.getAttribute("columnid");
			if (u != null && u != "") {
				d = replaceUrlParamValue(d, u, getConditionSelectedValue(u, a.getAttribute("columninputboxtype")))
			}
			var e = a.getAttribute("valueid");
			if (e != null && e != "") {
				d = replaceUrlParamValue(d, e, getConditionSelectedValue(e, a.getAttribute("valueinputboxtype")))
			}
		}
	}
	d = removeReportNavigateInfosFromUrl(d, o, null);
	d = removeLazyLoadParamsFromUrl(d, o, true);
	var s = o.metaDataSpanObj.getAttribute("current_accessmode");
	if (s === WX_ACCESSMODE_ADD) {
		d = replaceUrlParamValue(d, j + "_ACCESSMODE", null)
	}
	d = replaceUrlParamValue(d, "SEARCHREPORT_ID", o.reportid);
	if (x != null) {
		for (var v in x) {
			if (v == null) {
				continue
			}
			d = replaceUrlParamValue(d, v, x[v])
		}
	}
	var n = o.metaDataSpanObj.getAttribute("beforeSearchMethod");
	if (n != null && n != "") {
		var b = getObjectByJsonString(n);
		d = b.method(k, j, d);
		if (d == null || d == "") {
			return
		}
	}
	refreshComponent(d)
}

function removeReportConditionBoxValuesFromUrl(b, j) {
	var h = document.getElementsByName("font_" + j.reportguid + "_conditions");
	if (h == null || h.length == 0) {
		return b
	}
	var f = null;
	var g = null;
	var m = null;
	var k = null;
	for (var d = 0, e = h.length; d < e; d = d + 1) {
		f = h[d];
		m = f.getAttribute("value_name");
		if (m == null || m == "") {
			continue
		}
		k = f.getAttribute("iteratorindex");
		if (k != null && k != "" && parseInt(k, 10) >= 0) {
			m = m + "_" + parseInt(k, 10)
		}
		b = replaceUrlParamValue(b, m, null);
		var c = f.getAttribute("innerlogicid");
		if (c != null && c != "") {
			b = replaceUrlParamValue(b, c, null)
		}
		var l = f.getAttribute("columnid");
		if (l != null && l != "") {
			b = replaceUrlParamValue(b, l, null)
		}
		var a = f.getAttribute("valueid");
		if (a != null && a != "") {
			b = replaceUrlParamValue(b, a, null)
		}
	}
	return b
}

function getConditionSelectedValue(d, e) {
	if (e == "radiobox") {
		var c = document.getElementsByName(d);
		if (c != null) {
			for (var b = 0; b < c.length; b++) {
				if (c[b].checked) {
					return c[b].value
				}
			}
		}
	} else {
		var a = document.getElementById(d);
		if (a != null) {
			return a.options[a.options.selectedIndex].value
		}
	}
	return null
}

function setConditionInputBoxValue(b, g) {
	if (g == null || isEmptyMap(g)) {
		return false
	}
	var j = document.getElementsByName("font_" + b + "_conditions");
	if (j == null) {
		return false
	}
	var k = null;
	var d = null;
	var f = null;
	for (var c = 0, e = j.length; c < e; c = c + 1) {
		f = j[c];
		k = f.getAttribute("value_name");
		if (k == null || k == "") {
			continue
		}
		d = g[k];
		if (d == null) {
			continue
		}
		var a = f.getAttribute("customized_inputbox");
		if (a == "true") {
			f.setAttribute("value", d)
		} else {
			var h = getWXInputBoxChildNode(f);
			if (h == null) {
				continue
			}
			setInputBoxValue(h.getAttribute("id"), h.getAttribute("typename"), d)
		}
	}
}

function navigateReportPage(e, l, a) {
	var m = getReportMetadataObj(getComponentGuidById(e, l));
	var c = m.metaDataSpanObj.getAttribute("navigate_reportid");
	var d = getComponentUrl(e, m.refreshComponentGuid, m.slave_reportid);
	d = replaceUrlParamValue(d, c + "_PAGENO", a);
	var n = m.metaDataSpanObj.getAttribute("dependingChildReportIds");
	var h = m.metaDataSpanObj.getAttribute("reportfamily");
	if (n != null && n != "" && (h == null || h.indexOf("list") < 0)) {
		var g = n.split(";");
		var b;
		for (var j = 0, k = g.length; j < k; j = j + 1) {
			b = g[j];
			if (b == null || b == "") {
				continue
			}
			var f = getReportMetadataObj(getComponentGuidById(e, b));
			if (f == null) {
				continue
			}
			d = removeReportNavigateInfosFromUrl(d, f, null)
		}
	}
	refreshComponent(d)
}

function removeReportNavigateInfosFromUrl(c, n, e) {
	if (e != 2) {
		var b = n.metaDataSpanObj.getAttribute("navigate_reportid");
		if (b != null && b != "") {
			c = replaceUrlParamValue(c, b + "_PAGENO", null);
			c = replaceUrlParamValue(c, b + "_PAGECOUNT", null);
			c = replaceUrlParamValue(c, b + "_RECORDCOUNT", null)
		}
	}
	if (e != 1) {
		var j = n.metaDataSpanObj.getAttribute("relateConditionReportNavigateIds");
		if (j != null && j != "") {
			var k = j.split(";");
			var m;
			for (var h = 0; h < k.length; h = h + 1) {
				m = k[h];
				if (m == null || m == "") {
					continue
				}
				c = replaceUrlParamValue(c, m + "_PAGENO", null);
				c = replaceUrlParamValue(c, m + "_PAGECOUNT", null);
				c = replaceUrlParamValue(c, m + "_RECORDCOUNT", null)
			}
		}
		var l = n.metaDataSpanObj.getAttribute("dependingChildReportIds");
		var g = n.metaDataSpanObj.getAttribute("reportfamily");
		if (l != null && l != "" && (g == null || g.indexOf("list") < 0)) {
			var f = l.split(";");
			var a;
			for (var h = 0; h < f.length; h = h + 1) {
				a = f[h];
				if (a == null || a == "") {
					continue
				}
				var d = getReportMetadataObj(getComponentGuidById(n.pageid, a));
				if (d == null) {
					continue
				}
				c = removeReportNavigateInfosFromUrl(c, d, e)
			}
		}
	}
	return c
}
var WX_SCROLL_DELAYTIME = 50;

function showComponentScroll(a, c, b) {
	if (c != null && isWXNumber(c)) {
		c = c + "px"
	}
	if (typeof(fleXenv) == "undefined") {
		wx_error("框架没有为此页面导入/webresources/scroll/scroll.js文件，请在＜page/＞的js属性中显式导入");
		return false
	}
	if (b == 11) {
		setTimeout(function() {
			showComponentVerticalScroll("vscroll_" + a, c)
		}, WX_SCROLL_DELAYTIME)
	} else {
		if (b == 12) {
			setTimeout(function() {
				showComponentHorizontalScroll("hscroll_" + a)
			}, WX_SCROLL_DELAYTIME)
		} else {
			if (b == 13) {
				showComponentVerticalScroll("vscroll_" + a, c);
				showComponentHorizontalScroll("hscroll_" + a)
			} else {
				if (b == 21) {
					setTimeout(function() {
						showComponentVerticalScroll("scroll_" + a, c)
					}, WX_SCROLL_DELAYTIME)
				} else {
					if (b == 22) {
						setTimeout(function() {
							showComponentHorizontalScroll("scroll_" + a)
						}, WX_SCROLL_DELAYTIME)
					} else {
						if (b == 23) {
							setTimeout(function() {
								showComponentAllScroll("scroll_" + a, c)
							}, WX_SCROLL_DELAYTIME)
						}
					}
				}
			}
		}
	}
}

function showComponentVerticalScroll(a, b) {
	if (!b || parseInt(b) <= 0) {
		return false
	}
	var c = document.getElementById(a);
	if (!c) {
		return false
	}
	if (c.scrollHeight > parseInt(b)) {
		c.style.height = b;
		fleXenv.fleXcrollMain(c);
		document.getElementById(a + "_hscrollerbase").className = "hscrollerbase_hidden";
		c.fleXcroll.updateScrollBars()
	} else {
		c.style.height = c.scrollHeight + "px"
	}
}

function showComponentHorizontalScroll(a) {
	var b = document.getElementById(a);
	if (!b) {
		return false
	}
	b.style.height = (b.scrollHeight + 15) + "px";
	fleXenv.fleXcrollMain(b);
	document.getElementById(a + "_vscrollerbase").className = "vscrollerbase_hidden";
	b.fleXcroll.updateScrollBars()
}

function showComponentAllScroll(a, b) {
	if (!b || parseInt(b) <= 0) {
		return false
	}
	var c = document.getElementById(a);
	if (!c) {
		return false
	}
	if (c.scrollHeight > parseInt(b)) {
		c.style.height = b
	} else {
		c.style.height = (c.scrollHeight + 15) + "px"
	}
	fleXenv.fleXcrollMain(c);
	c.fleXcroll.updateScrollBars()
}

function createJsValidateTipObj(a) {
	var c = {
		owner: a,
		myContainer: null
	};
	var b = document.createElement("span");
	b.className = "spanJsValidateErrorTip";
	document.body.appendChild(b);
	c.myContainer = b;
	c.show = function(f) {
		if (!this.myContainer) {
			return
		}
		this.myContainer.innerHTML = f;
		var d = this.owner;
		if (!d) {
			return
		}
		var g = getElementAbsolutePosition(d);
		this.myContainer.style.display = "block";
		this.myContainer.style.position = "absolute";
		this.myContainer.style.left = g.left + "px";
		var e = a.getAttribute("id");
		if (e && e == "WX_TEXTAREA_BOX") {
			this.myContainer.style.top = g.top + "px"
		} else {
			this.myContainer.style.top = (g.top + g.height) + "px"
		}
		this.myContainer.setAttribute("WX_NOT_HIDE", "true");
		if (WX_tip_Obj) {
			WX_tip_Obj.innerHTML = "";
			WX_tip_Obj.style.display = "none";
			WX_tip_Obj = null;
			document.body.onclick = null
		}
		WX_tip_Obj = this.myContainer;
		WX_bol_tip_show = true;
		document.body.onclick = checkErrorTipClick
	};
	c.hide = function() {
		if (!this.myContainer) {
			return
		}
		this.myContainer.innerHTML = "";
		this.myContainer.style.display = "none"
	};
	return c
}
var WX_tip_Obj;
var WX_bol_tip_show = false;

function checkErrorTipClick(b) {
	var a = b ? b.target : event.srcElement;
	var c = a.getAttribute("WX_NOT_HIDE");
	if ((!c || c != "true") && !WX_bol_tip_show) {
		WX_tip_Obj.innerHTML = "";
		WX_tip_Obj.style.display = "none";
		WX_tip_Obj = null;
		document.body.onclick = null
	}
	WX_bol_tip_show = false
}
var WX_startTrIdxMap;

function initRowSelect() {
	if (WX_selectedTrObjs == null) {
		WX_selectedTrObjs = new Object()
	}
	if (WX_startTrIdxMap == null) {
		WX_startTrIdxMap = new Object()
	}
}
var WX_selectedTrObj_temp;
var WX_shouldInvokeOnloadMethod_temp;

function doSelectReportDataRowImpl(g) {
	if (!wx_isOkConfirm(g)) {
		return
	}
	var f = WX_selectedTrObj_temp;
	var i = WX_shouldInvokeOnloadMethod_temp;
	WX_selectedTrObj_temp = null;
	WX_shouldInvokeOnloadMethod_temp = null;
	if (f == null) {
		return
	}
	var e = f.getAttribute("id");
	if (e == null || e == "") {
		return
	}
	var b = e.substring(0, e.lastIndexOf("_tr_"));
	if (b == "") {
		return
	}
	var h = getRowSelectType(b);
	if (h != "checkbox" && h != "radiobox" && h != "single" && h != "multiply") {
		return
	}
	if (h == "checkbox" || h == "radiobox") {
		var c = null;
		if (h == "checkbox") {
			c = getSelectCheckBoxObj(f)
		} else {
			c = getSelectRadioBoxObj(f)
		}
		if (c == null) {
			return
		}
		c.checked = true;
		var d = "false";
		if (i == true) {
			d = "true"
		}
		doSelectedDataRowChkRadio(c, d, true)
	} else {
		if (!isListReportDataTrObj(f)) {
			return
		}
		initRowSelect();
		var a = null;
		if (h == "single") {
			a = deselectAllDataRow(b)
		}
		selectDataRow(f, true);
		if (i == true) {
			var j = new Array();
			j[0] = f;
			a = getRealDeselectedTrObjs(j, a);
			invokeRowSelectedMethods(getPageIdByComponentGuid(b), getComponentIdByGuid(b), j, a)
		}
	}
}

function doDeselectReportDataRowImpl(g) {
	if (!wx_isOkConfirm(g)) {
		return
	}
	var f = WX_selectedTrObj_temp;
	var i = WX_shouldInvokeOnloadMethod_temp;
	WX_selectedTrObj_temp = null;
	WX_shouldInvokeOnloadMethod_temp = null;
	if (f == null || !isSelectedRow(f)) {
		return
	}
	var e = f.getAttribute("id");
	if (e == null || e == "") {
		return
	}
	var b = e.substring(0, e.lastIndexOf("_tr_"));
	if (b == "") {
		return
	}
	var h = getRowSelectType(b);
	if (h != "checkbox" && h != "radiobox" && h != "single" && h != "multiply") {
		return
	}
	if (h == "checkbox") {
		var c = getSelectCheckBoxObj(f);
		if (c == null) {
			return
		}
		c.checked = false;
		var d = "false";
		if (i == true) {
			d = "true"
		}
		doSelectedDataRowChkRadio(c, d, true)
	} else {
		deselectDataRow(f);
		if (h == "radiobox") {
			var c = getSelectRadioBoxObj(f);
			if (c != null) {
				c.checked = false
			}
		}
		if (i == true) {
			var a = new Array();
			a[0] = f;
			invokeRowSelectedMethods(getPageIdByComponentGuid(b), getComponentIdByGuid(b), null, a)
		}
	}
}
var WX_selected_shiftKey_temp;
var WX_selected_ctrlKey_temp;
var WX_selected_selecttype_temp;

function doSelectDataRowEvent(a, d) {
	var f = a || window.event;
	var c = f.srcElement || f.target;
	var e = getSelectedTrParent(c);
	if (e == null) {
		return
	}
	var g = e.getAttribute("id");
	var b = g.substring(0, g.lastIndexOf("_tr_"));
	if (b == "") {
		return
	}
	WX_selected_shiftKey_temp = f.shiftKey;
	WX_selected_ctrlKey_temp = f.ctrlKey;
	WX_selectedTrObj_temp = e;
	WX_selected_selecttype_temp = d;
	if (isHasIgnoreSlaveReportsSavingData(b)) {
		wx_confirm("本操作可能会丢失对从报表数据的修改，是否继续？", null, null, null, doSelectDataRowEventImpl)
	} else {
		doSelectDataRowEventImpl("ok")
	}
}

function doSelectDataRowEventImpl(l) {
	if (!wx_isOkConfirm(l)) {
		return
	}
	var f = WX_selectedTrObj_temp;
	WX_selectedTrObj_temp = null;
	var k = f.getAttribute("id");
	var d = k.substring(0, k.lastIndexOf("_tr_"));
	if (d == "") {
		return
	}
	initRowSelect();
	var b = k.substring(k.lastIndexOf("_") + 1);
	var p = new Array();
	var c = new Array();
	if (WX_selected_selecttype_temp == "single" || (!WX_selected_ctrlKey_temp && !WX_selected_shiftKey_temp)) {
		c = deselectAllDataRow(d);
		selectDataRow(f, true);
		p[p.length] = f
	} else {
		var q = WX_selectedTrObjs[d];
		if (q != null) {
			if (WX_selected_shiftKey_temp) {
				var o = WX_startTrIdxMap[d];
				c = deselectAllDataRow(d);
				WX_startTrIdxMap[d] = o;
				if (o != null) {
					var e = parseInt(o, 10);
					var n = parseInt(b, 10);
					var a = d + "_tr_";
					q = new Object();
					WX_selectedTrObjs[d] = q;
					var g = e;
					var m = document;
					while (true) {
						var h = m.getElementById(a + g);
						if (h != null) {
							selectDataRow(h, false);
							p[p.length] = h
						}
						if (n == e) {
							break
						} else {
							if (n > e) {
								g = g + 1;
								if (g > n) {
									break
								}
							} else {
								g = g - 1;
								if (g < n) {
									break
								}
							}
						}
					}
				} else {
					selectDataRow(f, true);
					p[p.length] = f
				}
			} else {
				var j = q[k];
				if (j != null) {
					deselectDataRow(f);
					c[c.length] = f
				} else {
					selectDataRow(f, true);
					p[p.length] = f
				}
			}
		} else {
			selectDataRow(f, true);
			p[p.length] = f
		}
	}
	c = getRealDeselectedTrObjs(p, c);
	invokeRowSelectedMethods(getPageIdByComponentGuid(d), getComponentIdByGuid(d), p, c)
}
var WX_selected_selectBoxObj_temp;

function doSelectedDataRowChkRadio(b, d, g) {
	var f = b.type;
	var h = b.checked;
	if (f != "radio" && f != "RADIO" && f != "checkbox" && f != "CHECKBOX") {
		return
	}
	var c = b.getAttribute("name");
	var a = c.lastIndexOf("_rowselectbox_col");
	if (a <= 0) {
		return
	}
	var e = c.substring(0, a);
	if (e == null || e == "") {
		return
	}
	WX_selected_selectBoxObj_temp = b;
	WX_shouldInvokeOnloadMethod_temp = d;
	if (g !== true && isHasIgnoreSlaveReportsSavingData(e)) {
		wx_confirm("本操作可能会丢失对从报表数据的修改，是否继续？", null, null, null, doSelectedDataRowChkRadioImpl, cancelSelectDeselectChkRadio)
	} else {
		doSelectedDataRowChkRadioImpl("ok")
	}
}

function cancelSelectDeselectChkRadio() {
	if (WX_selected_selectBoxObj_temp == null) {
		return
	}
	if (WX_selected_selectBoxObj_temp.checked) {
		WX_selected_selectBoxObj_temp.checked = false
	} else {
		WX_selected_selectBoxObj_temp.checked = true
	}
	WX_selected_selectBoxObj_temp = null
}

function doSelectedDataRowChkRadioImpl(n) {
	if (!wx_isOkConfirm(n)) {
		wx_callCancelEvent();
		return
	}
	var m = WX_selected_selectBoxObj_temp;
	var t = WX_shouldInvokeOnloadMethod_temp;
	WX_selected_selectBoxObj_temp = null;
	WX_shouldInvokeOnloadMethod_temp = null;
	var j = m.type;
	var g = m.checked;
	initRowSelect();
	var a;
	var p = new Array();
	var c = new Array();
	var s = m.getAttribute("name");
	var o = s.lastIndexOf("_rowselectbox_col");
	if (o <= 0) {
		return
	}
	var e = s.substring(0, o);
	if (e == null || e == "") {
		return
	}
	var l;
	var u;
	var d = m.getAttribute("rowgroup");
	if (d == "true") {
		var b = getTreeGroupRowObj(e, m);
		if (b == null) {
			return
		}
		selectChildDataRows(b, p, c, g);
		a = getParentElementObj(b, "TABLE");
		var q = b.getAttribute("id");
		var o = q.lastIndexOf("trgroup_");
		l = q.substring(0, o);
		u = b.getAttribute("parenttridsuffix")
	} else {
		var h = getSelectedTrParent(m);
		if (h == null) {
			return
		}
		var v = h.getAttribute("id");
		if (j == "radio" || j == "RADIO") {
			c = deselectAllDataRow(e);
			selectDataRow(h, false);
			p[p.length] = h
		} else {
			if (g) {
				selectDataRow(h, false);
				p[p.length] = h
			} else {
				deselectDataRow(h);
				c[c.length] = h
			}
			var o = v.lastIndexOf("tr_");
			l = v.substring(0, o);
			u = h.getAttribute("parenttridsuffix")
		}
		a = getSelectedTableParent(h)
	}
	if (j == "checkbox" || j == "CHECKBOX") {
		var s = m.getAttribute("name");
		var o = s.lastIndexOf("_col");
		if (o <= 0) {
			return
		}
		var i = s.substring(0, o);
		var r = document.getElementsByName(i);
		var f = null;
		if (r && r.length > 0) {
			f = r[0]
		}
		if (f) {
			var k = WX_selectedTrObjs[e];
			if (k == null || isEmptyMap(k)) {
				f.checked = false
			} else {
				f.checked = true
			}
		}
		if (u != null && u != "") {
			selectParentDataRows(l, u, g)
		}
	}
	if (!t || t != "false") {
		c = getRealDeselectedTrObjs(p, c);
		invokeRowSelectedMethods(getPageIdByComponentGuid(e), getComponentIdByGuid(e), p, c)
	}
}

function getTreeGroupRowObj(c, d) {
	var b = d.parentNode;
	while (b != null) {
		if (b.tagName == "TR") {
			var a = b.getAttribute("id");
			if (a != null && a.indexOf(c) >= 0 && a.indexOf("_trgroup_") > 0) {
				break
			}
		}
		b = b.parentNode
	}
	return b
}

function selectChildDataRows(e, r, d, f) {
	var h = e.getAttribute("id");
	var p = h.lastIndexOf("trgroup_");
	var a = h.substring(0, p);
	var q = e.getAttribute("childdataidsuffixes");
	if (q && q != "") {
		var o = q.split(";");
		for (var g = 0, j = o.length; g < j; g = g + 1) {
			var c = o[g];
			if (!c || c == "") {
				continue
			}
			var m = document.getElementById(a + c);
			if (!m) {
				continue
			}
			var n = getSelectCheckBoxObj(m);
			var b = n.checked;
			if (f) {
				n.checked = true;
				selectDataRow(m, false);
				if (!b) {
					r[r.length] = m
				}
			} else {
				n.checked = false;
				deselectDataRow(m);
				if (b) {
					d[d.length] = m
				}
			}
		}
	}
	var l = e.getAttribute("childgroupidsuffixes");
	if (l != null && l != "") {
		var k = l.split(";");
		for (var g = 0; g < k.length; g = g + 1) {
			var c = k[g];
			if (!c || c == "") {
				continue
			}
			var m = document.getElementById(a + c);
			if (!m) {
				continue
			}
			var n = getSelectCheckBoxObj(m);
			if (f) {
				n.checked = true
			} else {
				n.checked = false
			}
		}
	}
}

function selectParentDataRows(a, f, c) {
	if (!f || f == "") {
		return
	}
	var d = document.getElementById(a + f);
	if (!d) {
		return
	}
	var n = getSelectCheckBoxObj(d);
	if (!n) {
		return
	}
	if (c) {
		n.checked = true
	} else {
		var j = d.getAttribute("childgroupidsuffixes");
		if (j && j != "") {
			var h = j.split(";");
			for (var e = 0; e < h.length; e = e + 1) {
				var b = h[e];
				if (!b || b == "") {
					continue
				}
				var g = document.getElementById(a + b);
				if (!g) {
					continue
				}
				var k = getSelectCheckBoxObj(g);
				if (k.checked) {
					c = true;
					break
				}
			}
		} else {
			var m = d.getAttribute("childdataidsuffixes");
			if (m && m != "") {
				var l = m.split(";");
				for (var e = 0; e < l.length; e = e + 1) {
					var b = l[e];
					if (!b || b == "") {
						continue
					}
					var g = document.getElementById(a + b);
					if (!g) {
						continue
					}
					var k = getSelectCheckBoxObj(g);
					if (k.checked) {
						c = true;
						break
					}
				}
			}
		}
		if (c) {
			n.checked = true
		} else {
			n.checked = false
		}
	}
	selectParentDataRows(a, d.getAttribute("parenttridsuffix"), c)
}

function getSelectCheckBoxObj(e) {
	var g = e.getElementsByTagName("INPUT");
	var f = null;
	for (var c = 0, a = g.length; c < a; c = c + 1) {
		var b = g[c].getAttribute("name");
		var d = g[c].getAttribute("type");
		if (!d || d != "checkbox") {
			continue
		}
		if (!b || b.indexOf("_rowselectbox_col") <= 0) {
			continue
		}
		f = g[c];
		break
	}
	return f
}

function getSelectRadioBoxObj(f) {
	var g = f.getElementsByTagName("INPUT");
	var e = null;
	for (var c = 0, a = g.length; c < a; c = c + 1) {
		var b = g[c].getAttribute("name");
		var d = g[c].getAttribute("type");
		if (!d || d != "radio") {
			continue
		}
		if (!b || b.indexOf("_rowselectbox_col") <= 0) {
			continue
		}
		e = g[c];
		break
	}
	return e
}

function doSelectedAllDataRowChkRadio(b) {
	var c = b.getAttribute("name");
	var a = c.lastIndexOf("_rowselectbox");
	if (a <= 0) {
		return
	}
	var d = c.substring(0, a);
	WX_selected_selectBoxObj_temp = b;
	if (isHasIgnoreSlaveReportsSavingData(d)) {
		wx_confirm("本操作可能会丢失对从报表数据的修改，是否继续？", null, null, null, doSelectedAllDataRowChkRadioImpl, cancelSelectDeselectChkRadio)
	} else {
		doSelectedAllDataRowChkRadioImpl("ok")
	}
}

function doSelectedAllDataRowChkRadioImpl(m) {
	if (!wx_isOkConfirm(m)) {
		wx_callCancelEvent();
		return
	}
	var e = WX_selected_selectBoxObj_temp;
	WX_selected_selectBoxObj_temp = null;
	initRowSelect();
	var o = new Array();
	var b = new Array();
	var q = e.getAttribute("name");
	var n = q.lastIndexOf("_rowselectbox");
	if (n <= 0) {
		return
	}
	var c = q.substring(0, n);
	var l = document.getElementsByName(q + "_col");
	if (!l || l.length == 0) {
		return
	}
	var f = false;
	if (e.checked) {
		f = true
	}
	var h;
	var k;
	var a;
	var p = new Object();
	for (var g = 0, j = l.length; g < j; g = g + 1) {
		h = l[g];
		if (h == null) {
			continue
		}
		var d = h.getAttribute("rowgroup");
		if (d == "true") {
			h.checked = f;
			continue
		}
		k = getSelectedTrParent(h);
		if (k == null) {
			continue
		}
		a = h.checked;
		if (f) {
			selectDataRow(k, false);
			h.checked = true;
			if (!a && p[k.getAttribute("id")] != "true") {
				o[o.length] = k;
				p[k.getAttribute("id")] = "true"
			}
		} else {
			deselectDataRow(k);
			h.checked = false;
			if (a && p[k.getAttribute("id")] != "true") {
				b[b.length] = k;
				p[k.getAttribute("id")] = "true"
			}
		}
	}
	invokeRowSelectedMethods(getPageIdByComponentGuid(c), getComponentIdByGuid(c), o, b)
}

function selectDataRow(b, d) {
	var e = b.getAttribute("id");
	var a = e.substring(0, e.lastIndexOf("_tr_"));
	if (a == "") {
		return
	}
	var f = WX_selectedTrObjs[a];
	if (!f) {
		f = new Object();
		WX_selectedTrObjs[a] = f
	}
	f[e] = b;
	if (d) {
		var c = e.substring(e.lastIndexOf("_") + 1);
		WX_startTrIdxMap[a] = c
	}
	setTrBgcolorInSelect(b, a)
}

function deselectDataRow(b) {
	resetTrBgcolorInSelect(b);
	if (!WX_selectedTrObjs) {
		return
	}
	var c = b.getAttribute("id");
	var a = c.substring(0, c.lastIndexOf("_tr_"));
	if (a == "") {
		return
	}
	var d = WX_selectedTrObjs[a];
	if (d != null && d[c] != null) {
		delete d[c]
	}
	if (WX_startTrIdxMap != null && WX_startTrIdxMap[a] != null) {
		delete WX_startTrIdxMap[a]
	}
}

function deselectAllDataRow(a) {
	var d = null;
	var b = new Object();
	if (WX_selectedTrObjs != null) {
		var e = WX_selectedTrObjs[a];
		if (e != null) {
			d = new Array();
			for (var c in e) {
				resetTrBgcolorInSelect(e[c]);
				if (e[c] == null) {
					continue
				}
				if (b[e[c].getAttribute("id")] != "true") {
					d[d.length] = e[c];
					b[e[c].getAttribute("id")] = "true"
				}
			}
		}
		delete WX_selectedTrObjs[a]
	}
	if (WX_startTrIdxMap != null) {
		delete WX_startTrIdxMap[a]
	}
	return d
}

function getRealDeselectedTrObjs(c, f) {
	if (c == null || c.length == 0) {
		return f
	}
	if (f == null || f.length == 0) {
		return f
	}
	var b = new Object();
	for (var d = 0, a = c.length; d < a; d++) {
		b[c[d].getAttribute("id")] = "true"
	}
	var e = new Array();
	for (var d = 0, a = f.length; d < a; d++) {
		if (b[f[d].getAttribute("id")] == "true") {
			continue
		}
		e[e.length] = f[d]
	}
	return e
}

function getSelectedTrParent(a) {
	if (!a) {
		return null
	}
	while (a != null) {
		if (isListReportDataTrObj(a)) {
			return a
		}
		a = a.parentNode
	}
	return null
}

function isListReportDataTrObj(b) {
	if (b.tagName != "TR") {
		return false
	}
	var c = b.getAttribute("id");
	if (!c || c == "") {
		return false
	}
	var a = c.lastIndexOf("_tr_");
	if (a <= 0) {
		return false
	}
	if (c.indexOf("trgoup_") > 0) {
		return false
	}
	if (c.substring(0, a).indexOf(WX_GUID_SEPERATOR) > 0 && parseInt(c.substring(a + 4)) >= 0) {
		return true
	}
	return false
}

function getSelectedTableParent(e) {
	if (!e) {
		return null
	}
	var f = e.getAttribute("id");
	var c = f.substring(0, f.lastIndexOf("_tr_"));
	if (c == "") {
		return
	}
	var d = c + "_data";
	var b = e.parentNode;
	while (b != null) {
		if (b.tagName == "TABLE") {
			var a = b.getAttribute("id");
			if (!a || a != d) {
				return null
			}
			return b
		}
		b = b.parentNode
	}
	return null
}

function getRowSelectType(b) {
	var a = getReportMetadataObj(b);
	if (a == null) {
		return ""
	}
	return a.metaDataSpanObj.getAttribute("rowselecttype")
}

function invokeRowSelectedMethods(d, f, j, k) {
	if ((j == null || j.length == 0) && (k == null || k.length == 0)) {
		return
	}
	var c = getComponentGuidById(d, f);
	var g = getReportMetadataObj(c);
	var b = g.metaDataSpanObj.getAttribute("rowSelectMethods");
	var h = getObjectByJsonString(b);
	if (h == null) {
		return
	}
	var a = h.rowSelectMethods;
	if (a == null || a.length == 0) {
		return
	}
	for (var e = 0; e < a.length; e = e + 1) {
		a[e].value(d, f, j, k)
	}
}

function setTrBgcolorInSelect(b, a) {
	if (b == null) {
		return
	}
	storeTdsOriginalBgColorInTr(b);
	setTdsBgColorInTr(b, WX_selectedRowBgcolor)
}

function resetTrBgcolorInSelect(a) {
	if (a == null) {
		return
	}
	resetTdsBgColorInTr(a)
}

function changeRowBgcolorOnMouseOver(b, a) {
	if (b == null || !isListReportDataTrObj(b)) {
		return
	}
	storeTdsOriginalBgColorInTr(b);
	setTdsBgColorInTr(b, a)
}

function resetRowBgcolorOnMouseOver(a) {
	if (isSelectedRow(a)) {
		setTdsBgColorInTr(a, WX_selectedRowBgcolor)
	} else {
		resetTdsBgColorInTr(a)
	}
}

function storeTdsOriginalBgColorInTr(g) {
	if (g == null) {
		return
	}
	if (isSetTrBgColorOnly(g) === true) {
		var f = g.getAttribute("tr_original_bgcolor");
		if (f != null && f != "") {
			return
		}
		f = getElementBgColor(g);
		if (f == null || f == "") {
			f = "#ffffff"
		}
		g.setAttribute("tr_original_bgcolor", f)
	} else {
		var a, d, f, j;
		var h = g.cells;
		var b = false;
		for (var c = 0, e = h.length; c < e; c++) {
			j = h[c];
			a = j.getAttribute("groupcol");
			if (a == "true") {
				continue
			}
			d = j.getAttribute("isFixedCol");
			if (d == "true") {
				continue
			}
			if (!b) {
				f = j.getAttribute("td_original_bgcolor");
				if (f != null && f != "") {
					return
				}
				b = true
			}
			f = getElementBgColor(j);
			if (f == null || f == "") {
				f = "#ffffff"
			}
			j.setAttribute("td_original_bgcolor", f)
		}
	}
}

function setTdsBgColorInTr(f, h) {
	if (f == null) {
		return
	}
	if (isSetTrBgColorOnly(f) === true) {
		f.style.backgroundColor = h
	} else {
		var g = f.cells;
		var b, d, c;
		for (var e = 0, a = g.length; e < a; e++) {
			c = g[e];
			b = c.getAttribute("groupcol");
			if (b == "true") {
				continue
			}
			d = c.getAttribute("isFixedCol");
			if (d == "true") {
				continue
			}
			c.style.backgroundColor = h
		}
	}
}

function resetTdsBgColorInTr(f) {
	if (isSetTrBgColorOnly(f) === true) {
		var b = f.getAttribute("tr_original_bgcolor");
		if (b == null || b == "") {
			return
		}
		f.style.backgroundColor = b
	} else {
		var a, d, h = 0,
			b;
		var g = f.cells,
			j;
		for (var c = 0, e = g.length; c < e; c++) {
			j = g[c];
			a = j.getAttribute("groupcol");
			if (a == "true") {
				continue
			}
			d = j.getAttribute("isFixedCol");
			if (d == "true") {
				continue
			}
			b = j.getAttribute("td_original_bgcolor");
			if (b == null || b == "") {
				return
			}
			j.style.backgroundColor = b
		}
	}
}

function isSetTrBgColorOnly(e) {
	if (e == null) {
		return true
	}
	var b = e.getAttribute("wx_isSetTrBgColorOnly_flag");
	if (b != null && b != "") {
		return b == "true"
	}
	var f = e.cells,
		c;
	for (var d = 0, a = f.length; d < a; d++) {
		c = f[d];
		isgroupcol = c.getAttribute("groupcol");
		isFixedCol = c.getAttribute("isFixedCol");
		if (isgroupcol == "true" || isFixedCol == "true") {
			e.setAttribute("wx_isSetTrBgColorOnly_flag", "false");
			return false
		}
	}
	e.setAttribute("wx_isSetTrBgColorOnly_flag", "true");
	return true
}
var WX_Current_Slave_TrObj;

function shouldRefreshSlaveReportsForThisRow(c) {
	var d = c.getAttribute("id");
	if (!d || d == "") {
		return true
	}
	var b = d.substring(0, d.lastIndexOf("_tr_"));
	if (WX_Current_Slave_TrObj == null) {
		WX_Current_Slave_TrObj = new Object()
	}
	var a = WX_Current_Slave_TrObj[b];
	if (a != d) {
		WX_Current_Slave_TrObj[b] = d;
		return true
	}
	return false
}

function clearCurrentSlaveTrObjForReport(a, c) {
	if (WX_Current_Slave_TrObj == null) {
		return
	}
	var b = getComponentGuidById(a, c);
	delete WX_Current_Slave_TrObj[b]
}

function getRefreshSlaveReportsHrefParams(g) {
	var d = g.getElementsByTagName("TD");
	var c = "";
	var b;
	var e;
	for (var f = 0, a = d.length; f < a; f = f + 1) {
		b = d[f].getAttribute("slave_paramname");
		if (!b || b == "") {
			continue
		}
		e = d[f].getAttribute("value");
		if (!e) {
			e = ""
		}
		if (c != "") {
			c = c + "&"
		}
		c = c + b + "=" + e
	}
	return c
}

function initdrag(b, a) {
	b.isInit = true;
	b.getTdWidthByIndex = function(d) {
		var g = b.parentNode.parentNode.cells[b.parentNode.cellIndex + d];
		var e = g.style.width;
		if (e && e != "" && e.indexOf("%") < 0) {
			return parseInt(e)
		}
		if (window.ActiveXObject || isChrome || ISOPERA) {
			return g.offsetWidth
		} else {
			var f = b.parentNode.parentNode.parentNode.parentNode.cellPadding;
			if (!f) {
				f = 1
			}
			var c = b.parentNode.parentNode.parentNode.parentNode.border;
			if (!c) {
				c = 1
			} else {
				if (parseInt(c) >= 1) {}
			}
			return parseInt(b.parentNode.parentNode.cells[b.parentNode.cellIndex + d].offsetWidth) - parseInt(f) * 2 - parseInt(c)
		}
	};
	b.setTdWidthByIndex = function(d, c) {
		b.parentNode.parentNode.cells[d].style.width = c + "px"
	};
	b.firstChild.onmousedown = function() {
		return false
	};
	b.onmousedown = function(c) {
		var g = document;
		if (!c) {
			c = window.event
		}
		var f = c.clientX;
		var e = 0;
		if (a) {
			e = b.getTdWidthByIndex(0) + b.getTdWidthByIndex(1)
		}
		if (b.setCapture) {
			b.setCapture()
		} else {
			if (window.captureEvents) {
				window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP)
			}
		}
		g.onmousemove = function(d) {
			if (!d) {
				d = window.event
			}
			if (a && b.getTdWidthByIndex(0) + b.getTdWidthByIndex(1) > e) {
				b.setTdWidthByIndex(b.parentNode.cellIndex + 1, e - b.getTdWidthByIndex(0));
				return
			}
			var h = d.clientX - f;
			if (h > 0) {
				if (a && parseInt(b.parentNode.parentNode.cells[b.parentNode.cellIndex + 1].style.width) - h < 10) {
					return
				}
				b.setTdWidthByIndex(b.parentNode.cellIndex, b.getTdWidthByIndex(0) + h);
				if (a) {
					b.setTdWidthByIndex(b.parentNode.cellIndex + 1, b.getTdWidthByIndex(1) - h)
				} else {
					b.parentNode.parentNode.parentNode.parentNode.style.width = (b.parentNode.parentNode.parentNode.parentNode.offsetWidth + h) + "px"
				}
			} else {
				if (parseInt(b.parentNode.parentNode.cells[b.parentNode.cellIndex].style.width) + h < 10) {
					return
				}
				b.setTdWidthByIndex(b.parentNode.cellIndex, b.getTdWidthByIndex(0) + h);
				if (a) {
					b.setTdWidthByIndex(b.parentNode.cellIndex + 1, b.getTdWidthByIndex(1) - h)
				} else {
					b.parentNode.parentNode.parentNode.parentNode.style.width = (b.parentNode.parentNode.parentNode.parentNode.offsetWidth + h) + "px"
				}
			}
			f = d.clientX
		};
		g.onmouseup = function() {
			if (b.releaseCapture) {
				b.releaseCapture()
			} else {
				if (window.captureEvents) {
					window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP)
				}
			}
			g.onmousemove = null;
			g.onmouseup = null
		}
	}
}
var dragrow_table;
var dragrow_pageid;
var dragrow_reportid;
var _tempDragrowTarget;
var _fromDragrowTarget;
var _toDroprowTarget;
var _isDragRow = false;
var dragrow_enabled = true;

function handleRowDragMouseDown(a, b, c) {
	if (a.getAttribute("EDIT_TYPE") == "add") {
		return
	}
	if (_tempDragrowTarget != null) {
		handleRowMouseUp(a);
		return
	}
	if (!dragrow_enabled) {
		return
	}
	_isDragRow = true;
	_fromDragrowTarget = a;
	_toDroprowTarget = a;
	dragrow_pageid = b;
	dragrow_reportid = c;
	dragrow_table = getParentElementObj(a, "TABLE");
	if (dragrow_table == null) {
		return false
	}
	document.body.onselectstart = function() {
		return false
	};
	document.body.style.MozUserSelect = "none";
	if (!dragrow_table.isInitRowDrag) {
		dragrow_table.isInitRowDrag = true;
		var d;
		var e = dragrow_table.tBodies[0].rows;
		for (var i = 0, len = e.length; i < len; i++) {
			d = e[i];
			d.onmouseup = function() {
				handleRowMouseUp(this)
			}
		}
	}
	document.body.style.cursor = "move";
	EventTools.addEventHandler(window.document.body, "mousemove", handleRowMouseMove);
	EventTools.addEventHandler(window.document.body, "mouseup", handleBodyRowMouseUp);
	_tempDragrowTarget = document.createElement("TABLE");
	_tempDragrowTarget.className = dragrow_table.className;
	var f = document.createElement("TBODY");
	f.appendChild(a.cloneNode(true));
	_tempDragrowTarget.appendChild(f);
	with(_tempDragrowTarget.style) {
		display = "none";
		position = "absolute";
		zIndex = 101;
		filter = "Alpha(style=0,opacity=50)";
		opacity = 0.5;
		width = dragrow_table.clientWidth;
		left = getElementAbsolutePosition(dragrow_table).left
	}
	if (dragrow_table.style.tableLayout != null) {
		_tempDragrowTarget.style.tableLayout = dragrow_table.style.tableLayout
	}
	document.body.appendChild(_tempDragrowTarget)
}

function handleBodyRowMouseUp() {
	document.body.style.cursor = "";
	document.body.onselectstart = function() {
		return true
	};
	document.body.style.MozUserSelect = "";
	_isDragRow = false;
	removeTempDragrowTarget();
	EventTools.removeEventHandler(window.document.body, "mousemove", handleRowMouseMove);
	EventTools.removeEventHandler(window.document.body, "mouseup", handleBodyRowMouseUp);
	_toDroprowTarget = null
}

function handleRowMouseUp(d) {
	document.body.style.cursor = "";
	removeTempDragrowTarget();
	if (!_isDragRow) {
		return false
	}
	_isDragRow = false;
	EventTools.removeEventHandler(window.document.body, "mousemove", handleRowMouseMove);
	EventTools.removeEventHandler(window.document.body, "mouseup", handleBodyRowMouseUp);
	if (_toDroprowTarget != null) {
		var a = getParentElementObj(d, "TABLE");
		if (a == dragrow_table) {
			if (d.getAttribute("EDIT_TYPE") == "add") {
				var c = dragrow_table.tBodies[0].rows;
				for (var b = c.length - 1; b >= 0; b--) {
					if (c[b].getAttribute("EDIT_TYPE") == "add") {
						continue
					}
					if (!isListReportDataTrObj(c[b])) {
						continue
					}
					d = c[b];
					break
				}
			}
			_toDroprowTarget = d;
			changeListReportRoworderByDrag()
		}
		_toDroprowTarget = null
	}
	document.body.onselectstart = function() {
		return true
	};
	document.body.style.MozUserSelect = ""
}

function removeTempDragrowTarget() {
	if (_tempDragrowTarget != null) {
		if (_tempDragrowTarget.parentNode != null) {
			_tempDragrowTarget.parentNode.removeChild(_tempDragrowTarget)
		} else {
			_tempDragrowTarget.style.display = "none"
		}
		_tempDragrowTarget = null
	}
}

function handleRowMouseMove(a) {
	a = EventTools.getEvent(a);
	if (a.type.indexOf("mousemove") == -1) {
		EventTools.removeEventHandler(window.document.body, "mousemove", handleRowMouseMove);
		EventTools.removeEventHandler(window.document.body, "mouseup", handleBodyRowMouseUp);
		return false
	}
	with(_tempDragrowTarget.style) {
		top = (a.pageY + 5) + "px";
		display = ""
	}
}

function changeListReportRoworderByDrag() {
	if (_fromDragrowTarget == null || _toDroprowTarget == null || _fromDragrowTarget == _toDroprowTarget) {
		return false
	}
	if (!isListReportDataTrObj(_toDroprowTarget)) {
		return false
	}
	var d = _fromDragrowTarget.getAttribute("id");
	var f = _toDroprowTarget.getAttribute("id");
	if (d == f) {
		return false
	}
	var e = parseInt(d.substring(d.lastIndexOf("_tr_") + "_tr_".length), 10);
	var c = parseInt(f.substring(f.lastIndexOf("_tr_") + "_tr_".length), 10);
	var g = e > c;
	var a = getRoworderUrl(dragrow_pageid, dragrow_reportid, _fromDragrowTarget);
	if (a == null || a == "") {
		return false
	}
	var b = getRoworderParamsInTr(_toDroprowTarget);
	a = a + "&" + dragrow_reportid + "_ROWORDERTYPE=drag";
	a = a + "&" + dragrow_reportid + "_DESTROWPARAMS=" + b;
	a = a + "&" + dragrow_reportid + "_ROWORDERDIRECT=" + g;
	refreshComponent(a)
}

function changeListReportRoworderByArrow(b, f, e, c) {
	var h = getValidParentTrObj(e);
	if (h == null) {
		return false
	}
	var a = getRoworderUrl(b, f, h);
	var g = h.getAttribute("id");
	var j = parseInt(g.substring(g.lastIndexOf("_tr_") + "_tr_".length), 10);
	var d = null;
	if (c && j > 0) {
		d = document.getElementById(g.substring(0, g.lastIndexOf("_tr_") + "_tr_".length) + (j - 1))
	} else {
		if (!c) {
			d = document.getElementById(g.substring(0, g.lastIndexOf("_tr_") + "_tr_".length) + (j + 1))
		}
	}
	if (d != null && d.getAttribute("EDIT_TYPE") != "add") {
		var i = getRoworderParamsInTr(d);
		a = a + "&" + f + "_DESTROWPARAMS=" + i
	}
	a = a + "&" + f + "_ROWORDERTYPE=arrow";
	a = a + "&" + f + "_ROWORDERDIRECT=" + c;
	refreshComponent(a)
}
var WX_Roworder_inputbox_pageid;
var WX_Roworder_inputbox_reportid;
var WX_Roworder_inputbox_trObj;
var WX_Roworder_inputbox_newordervalue;

function changeListReportRoworderByInputbox(a, b, c, e) {
	if (c == null || c.value == null || c.value == "" || c.value == e) {
		return
	}
	var d = getValidParentTrObj(c);
	if (d == null) {
		return
	}
	WX_Roworder_inputbox_pageid = a;
	WX_Roworder_inputbox_reportid = b;
	WX_Roworder_inputbox_trObj = d;
	WX_Roworder_inputbox_newordervalue = c.value;
	wx_confirm("确认修改此行排序值为" + c.value + "?", "排序", null, null, doChangeListReportRoworderByInputbox)
}

function doChangeListReportRoworderByInputbox(a) {
	if (wx_isOkConfirm(a)) {
		var b = getRoworderUrl(WX_Roworder_inputbox_pageid, WX_Roworder_inputbox_reportid, WX_Roworder_inputbox_trObj);
		if (b == null || b == "") {
			wx_warn("没有取到行排序所需的参数");
			return
		}
		b = b + "&" + WX_Roworder_inputbox_reportid + "_ROWORDERTYPE=inputbox";
		b = b + "&" + WX_Roworder_inputbox_reportid + "_ROWORDERVALUE=" + WX_Roworder_inputbox_newordervalue;
		refreshComponent(b)
	}
	WX_Roworder_inputbox_pageid = null;
	WX_Roworder_inputbox_reportid = null;
	WX_Roworder_inputbox_trObj = null;
	WX_Roworder_inputbox_newordervalue = null
}

function changeListReportRoworderByTop(a, b, d) {
	var e = getValidParentTrObj(d);
	var c = getRoworderUrl(a, b, e);
	if (c == null || c == "") {
		return
	}
	c = c + "&" + b + "_ROWORDERTYPE=top";
	refreshComponent(c)
}

function getValidParentTrObj(a) {
	while (a != null) {
		if (a.tagName == "TR" && isListReportDataTrObj(a) && a.getAttribute("EDIT_TYPE") != "add") {
			return a
		}
		a = getParentElementObj(a, "TR")
	}
	return null
}

function getRoworderUrl(a, d, f) {
	var c = getComponentGuidById(a, d);
	var g = getReportMetadataObj(c);
	var e = getComponentUrl(a, g.refreshComponentGuid, g.slave_reportid);
	var b = getRoworderParamsInTr(f);
	if (b == null || b == "") {
		return null
	}
	e = e + "&" + d + "_ROWORDERPARAMS=" + b;
	return e
}

function getRoworderParamsInTr(f) {
	var b = "";
	var c;
	var d, g;
	for (var e = 0, a = f.cells.length; e < a; e++) {
		c = f.cells[e];
		d = c.getAttribute("name");
		if (d == null || d == "") {
			continue
		}
		g = c.getAttribute("value");
		if (g == null) {
			g = ""
		}
		b = b + d + SAVING_NAMEVALUE_SEPERATOR + encodeURIComponent(g) + SAVING_COLDATA_SEPERATOR
	}
	if (b.lastIndexOf(SAVING_COLDATA_SEPERATOR) == b.length - SAVING_COLDATA_SEPERATOR.length) {
		b = b.substring(0, b.length - SAVING_COLDATA_SEPERATOR.length)
	}
	return b
}
var drag_table;
var drag_pageid;
var drag_reportid;
var _tempDragTarget;
var _fromDragTarget;
var _toDropTarget;
var _isDrag = false;
var drag_enabled = true;

function insertAfter(c, a) {
	var b = a.parentNode;
	if (b.lastChild == a) {
		b.appendChild(c)
	} else {
		b.insertBefore(c, a.nextSibling)
	}
}

function setTempDragTarget(a) {
	_tempDragTarget.innerHTML = a.innerHTML;
	_tempDragTarget.className = "dragShadowOnMove";
	_tempDragTarget.style.display = "none";
	_tempDragTarget.style.height = a.clientHeight + "px";
	_tempDragTarget.style.width = a.clientWidth + "px"
}

function handleCellDragMouseDown(b, a, f) {
	if (!drag_enabled) {
		return false
	}
	_isDrag = true;
	_tempDragTarget = document.getElementById("dragShadowObjId");
	if (!_tempDragTarget) {
		_tempDragTarget = document.createElement("DIV");
		_tempDragTarget.style.display = "none";
		_tempDragTarget.id = "dragShadowObjId";
		document.body.appendChild(_tempDragTarget)
	}
	setTempDragTarget(b);
	_fromDragTarget = b;
	_toDropTarget = b;
	drag_pageid = a;
	drag_reportid = f;
	drag_table = getParentElementObj(b, "TABLE");
	if (!drag_table) {
		return false
	}
	document.body.onselectstart = function() {
		return false
	};
	document.body.style.MozUserSelect = "none";
	if (!drag_table.isInitCellDrag) {
		drag_table.isInitCellDrag = true;
		for (var e = 0, g = drag_table.tBodies[0].rows.length; e < g; e++) {
			var k = drag_table.tBodies[0].rows[e].cells;
			for (var d = 0, c = k.length; d < c; d++) {
				var h = k[d];
				h.onmouseover = function() {
					handleMouseOver(this)
				};
				h.onmouseout = function() {
					handleMouseOut()
				};
				h.onmouseup = function() {
					handleMouseUp(this)
				}
			}
		}
	}
	document.body.style.cursor = "move";
	EventTools.addEventHandler(window.document.body, "mousemove", handleMouseMove);
	EventTools.addEventHandler(window.document.body, "mouseup", handleBodyMouseUp)
}

function handleBodyMouseUp() {
	document.body.style.cursor = "";
	document.body.onselectstart = function() {
		return true
	};
	document.body.style.MozUserSelect = "";
	_isDrag = false;
	EventTools.removeEventHandler(window.document.body, "mousemove", handleMouseMove);
	EventTools.removeEventHandler(window.document.body, "mouseup", handleBodyMouseUp);
	_tempDragTarget.style.display = "none";
	if (_toDropTarget) {
		_toDropTarget = null
	}
}

function handleMouseUp(b) {
	document.body.style.cursor = "";
	if (!_isDrag) {
		return false
	}
	_isDrag = false;
	EventTools.removeEventHandler(window.document.body, "mousemove", handleMouseMove);
	EventTools.removeEventHandler(window.document.body, "mouseup", handleBodyMouseUp);
	_tempDragTarget.style.display = "none";
	if (_toDropTarget) {
		var a = getParentElementObj(b, "TABLE");
		if (a == drag_table) {
			_toDropTarget = b;
			moveTargetToByServer()
		}
		_toDropTarget = null
	}
	document.body.onselectstart = function() {
		return true
	};
	document.body.style.MozUserSelect = ""
}

function handleMouseOver(b) {
	if (!_isDrag) {
		return false
	}
	var a = getParentElementObj(b, "TABLE");
	if (a == drag_table) {
		document.body.style.cursor = "move";
		_toDropTarget = b
	}
}

function handleMouseOut() {
	if (!_toDropTarget) {
		return false
	}
	if (_isDrag) {
		document.body.style.cursor = "not-allowed"
	}
	_toDropTarget = null
}

function handleMouseMove(a) {
	a = EventTools.getEvent(a);
	if (a.type.indexOf("mousemove") == -1) {
		EventTools.removeEventHandler(window.document.body, "mousemove", handleMouseMove);
		EventTools.removeEventHandler(window.document.body, "mouseup", handleBodyMouseUp);
		return false
	}
	var x = a.pageX + 10;
	var y = a.pageY + 10;
	with(_tempDragTarget.style) {
		left = x + "px";
		top = y + "px";
		display = ""
	}
}

function moveTargetToByClient() {
	if (!_fromDragTarget || !_toDropTarget || _fromDragTarget == _toDropTarget) {
		return false
	}
	var c = _fromDragTarget.cellIndex;
	var d = _toDropTarget.cellIndex;
	if (c == d) {
		return
	}
	var g = true;
	if (c > d) {
		g = false
	}
	for (var b = 0; b < drag_table.tBodies[0].rows.length; b++) {
		var f = drag_table.tBodies[0].rows[b];
		var e = f.cells[c];
		var a = f.cells[d];
		if (!g) {
			f.insertBefore(e, a)
		} else {
			if (a.cellIndex == f.cells.length - 1) {
				f.appendChild(e)
			} else {
				f.insertBefore(e, f.cells[d + 1])
			}
		}
	}
}

function moveTargetToByServer() {
	if (!_fromDragTarget || !_toDropTarget || _fromDragTarget == _toDropTarget) {
		return false
	}
	var f = getThOfDropTargetTd();
	if (f == 0) {
		return
	}
	var a = _fromDragTarget.getAttribute("dragcolid");
	var d = _toDropTarget.getAttribute("dragcolid");
	if (!a || !d || a == d) {
		return
	}
	var b = getComponentGuidById(drag_pageid, drag_reportid);
	var e = getReportMetadataObj(b);
	var c = getComponentUrl(drag_pageid, e.refreshComponentGuid, e.slave_reportid);
	c = replaceUrlParamValue(c, drag_reportid + "_DRAGCOLS", a + ";" + d);
	c = c + "&" + drag_reportid + "_DRAGDIRECT=" + f;
	WX_showProcessingBar = false;
	refreshComponent(c)
}

function getThOfDropTargetTd() {
	var g;
	var d;
	var m = _fromDragTarget.parentNode;
	for (var c = 0, e = m.cells.length; c < e; c++) {
		g = m.cells[c];
		d = g.getAttribute("dragcolid");
		if (d == null || d == "") {
			continue
		}
		var l = getElementAbsolutePosition(_fromDragTarget).left;
		var h = _toDropTarget.offsetWidth;
		var f = getElementAbsolutePosition(_toDropTarget).left;
		var k = g.offsetWidth;
		var a = getElementAbsolutePosition(g).left;
		if (_fromDragTarget == g || l == a) {
			continue
		}
		if (k < h) {
			continue
		}
		if (a > f) {
			continue
		}
		if (a <= f && a + k >= f + h) {
			var j = _fromDragTarget.getAttribute("parentGroupid");
			var b = g.getAttribute("parentGroupid");
			if (!j && !b || j == b) {
				_toDropTarget = g;
				if (l == a) {
					return 0
				}
				if (l > a) {
					return -1
				}
				if (l < a) {
					return 1
				}
			}
		}
	}
	return 0
}

function clickorderby(c, d) {
	var i = getParentElementObj(c, "TABLE");
	if (!i) {
		return false
	}
	var b = i.getAttribute("pageid");
	var f = i.getAttribute("reportid");
	var g = i.getAttribute("refreshComponentGuid");
	var e = i.getAttribute("isSlave");
	var h = null;
	if (e && e == "true") {
		h = f
	}
	var a = getComponentUrl(b, g, h);
	a = replaceUrlParamValue(a, f + "ORDERBY", d);
	a = replaceUrlParamValue(a, f + "ORDERBY_ACTION", "true");
	refreshComponent(a)
}
var COL_FILTER_btnObj;
var COL_FILTER_selectSpanTitleStart = "<span style='width:100%;display:block;' class='spanOutputTitleElement'>";
var COL_FILTER_selectSpanStart = "<span style='width:100%;display:block;'  class='spanOutputNormalElement' onmouseover='setHighColor(this)' ";
var COL_FILTER_selectSpanEnd = "</span>";

function closeAllColFilterResultSpan() {
	if (COL_FILTER_btnObj) {
		var b = document.getElementById(COL_FILTER_btnObj.obj.spanOutputId);
		if (b) {
			b.style.display = "none"
		}
		COL_FILTER_btnObj.obj.currentValueSelected = -1
	}
	COL_FILTER_btnObj = null;
	var a = document.getElementById("wx_titletree_container");
	if (a != null) {
		closeSelectedTree()
	}
}

function getFilterDataList(m, f) {
	closeAllColFilterResultSpan();
	COL_FILTER_btnObj = m;
	var g = getObjectByJsonString(f);
	var b = g.filterwidth;
	var d = getParentElementObj(m, "TD");
	if (m.obj == null || ((b == null || b <= 0) && m.obj.spanOutputWidth != d.offsetWidth)) {
		m.obj = initializeFilter(m, g, -1)
	}
	m.obj.paramsObj = g;
	var l = getReportMetadataObj(g.reportguid);
	m.obj.metadataObj = l;
	var c = l.pageid;
	var h = l.reportid;
	var i = l.refreshComponentGuid;
	var j = l.slave_reportid;
	var a = getComponentUrl(c, i, j);
	a = replaceUrlParamValue(a, "REPORTID", h);
	a = replaceUrlParamValue(a, "ACTIONTYPE", "GetFilterDataList");
	a = replaceUrlParamValue(a, "FILTER_COLPROP", g.property);
	if (a != null && a != "") {
		var n = a.split("?");
		if (n == null || n.length <= 1) {
			n[1] = ""
		} else {
			if (n.length >= 2) {
				if (n.length > 2) {
					for (var e = 2; e < n.length; e = e + 1) {
						n[1] = n[1] + "?" + n[e]
					}
				}
			}
		}
		XMLHttpREPORT.sendReq("POST", n[0], n[1], buildFilterItems, onGetDataErrorMethod, "")
	}
}

function initializeFilter(g, f, b) {
	var d = {
		elem: g,
		paramsObj: null,
		metadataObj: null,
		spanOutputWidth: null,
		resultItemsXmlRoot: null,
		recordCount: 0,
		treeNodesArr: null,
		spanOutputId: "",
		currentValueSelected: -1,
		prevValueSelected: -1
	};
	if (g.id) {
		d.spanOutputId = "spanOutput_" + g.id
	} else {
		alert("必须给用于点击的按钮对象分配一id属性")
	}
	var c = getParentElementObj(g, "TD");
	var e = f.filterwidth;
	if (e != null && e <= 0) {
		d.spanOutputWidth = c.offsetWidth
	} else {
		d.spanOutputWidth = e
	}
	if (f.multiply && f.multiply == "false") {
		var a = document.getElementById(d.spanOutputId);
		if (a == null) {
			var h = document.createElement("span");
			h.id = d.spanOutputId;
			h.className = "spanOutputTextDropdown";
			document.body.appendChild(h);
			a = h
		}
	}
	d.paramsObj = f;
	return d
}

function setHighColor(b) {
	if (b) {
		var c = b.id.split("_");
		COL_FILTER_btnObj.obj.prevValueSelected = COL_FILTER_btnObj.obj.currentValueSelected;
		COL_FILTER_btnObj.obj.currentValueSelected = c[c.length - 1]
	}
	if (parseInt(COL_FILTER_btnObj.obj.prevValueSelected) >= 0) {
		document.getElementById(COL_FILTER_btnObj.obj.spanOutputId + "_" + COL_FILTER_btnObj.obj.prevValueSelected).className = "spanOutputNormalElement"
	}
	var a = document.getElementById(COL_FILTER_btnObj.obj.spanOutputId + "_" + COL_FILTER_btnObj.obj.currentValueSelected);
	if (a) {
		a.className = "spanOutputHighElement"
	}
}

function onGetDataErrorMethod(a) {
	if (WXConfig.load_error_message != null && WXConfig.load_error_message != "") {
		wx_error(WXConfig.load_error_message)
	}
	COL_FILTER_btnObj.obj.currentValueSelected = -1
}

function buildFilterItems(b) {
	var a = b.responseXML;
	COL_FILTER_btnObj.obj.resultItemsXmlRoot = a.getElementsByTagName("items")[0];
	if (COL_FILTER_btnObj.obj.resultItemsXmlRoot) {
		COL_FILTER_btnObj.obj.recordCount = COL_FILTER_btnObj.obj.resultItemsXmlRoot.childNodes.length
	} else {
		COL_FILTER_btnObj.obj.recordCount = 0
	}
	buildSelectListBox()
}

function buildSelectListBox() {
	var d = getElementAbsolutePosition(getParentElementObj(COL_FILTER_btnObj, "TD"));
	var x = document;
	if (COL_FILTER_btnObj.obj.paramsObj.multiply == "false") {
		var m = makeFilterSelectList();
		if (m.length > 0) {
			var l = x.getElementById(COL_FILTER_btnObj.obj.spanOutputId);
			l.innerHTML = m;
			x.getElementById(COL_FILTER_btnObj.obj.spanOutputId + "_0").className = "spanOutputHighElement";
			COL_FILTER_btnObj.obj.currentValueSelected = 0;
			l.style.display = "block";
			setOutputPosition(d, l, l, x.getElementById(COL_FILTER_btnObj.obj.spanOutputId + "_inner"));
			EventTools.addEventHandler(window.document, "mousedown", handleDocumentMouseDownForSingleColFilter)
		} else {
			hideSingleColFilterSelectBox()
		}
	} else {
		var r = COL_FILTER_btnObj.obj.recordCount;
		if (r <= 0) {
			return
		}
		var y = COL_FILTER_btnObj.obj.paramsObj;
		var n = getReportMetadataObj(y.reportguid);
		var q = n.metaDataSpanObj.getAttribute("lazydataload") == "true";
		var f = y.webroot;
		var p = y.skin;
		var k = f + "webresources/skin/" + p + "/images/coltitle_selected/";
		var a = '{img_rooturl:"' + k + '"';
		a = a + ',checkbox:"true"';
		a = a + ',treenodeimg:"false"';
		a = a + ",nodes:[";
		var j = new Array();
		var w;
		var c = COL_FILTER_btnObj.obj.resultItemsXmlRoot.childNodes;
		var o = "";
		var h = "";
		for (var s = 0; s < r; s = s + 1) {
			var b = c.item(s);
			if (b.childNodes.length <= 0) {
				continue
			}
			o = b.firstChild.childNodes[0].nodeValue;
			var e = b.firstChild.getAttribute("isChecked");
			if (q || e == null || e == "") {
				e = "false"
			}
			if (b.childNodes.length == 1) {
				h = o
			} else {
				h = b.lastChild.childNodes[0].nodeValue
			}
			if (o && (o == "[nodata]" || o == "[error]")) {
				a = h;
				break
			}
			var g = "col_filter_" + s;
			w = new Object();
			w.nodeid = g;
			w.nodevalue = o;
			j[j.length] = w;
			a = a + '{nodeid:"' + g + '"';
			a = a + ',title:"' + h + '"';
			a = a + ',checked:"' + e + '"';
			a = a + "},"
		}
		if (a.lastIndexOf(",") == a.length - 1) {
			a = a.substring(0, a.length - 1)
		}
		COL_FILTER_btnObj.obj.treeNodesArr = j;
		var u = '<ul class="bbit-tree-root bbit-tree-lines">';
		if (a.indexOf("nodes:[") < 0 && a.indexOf("{img_rooturl:") < 0) {
			u = u + a
		} else {
			a = a + "]}";
			u = u + showTreeNodes(a)
		}
		u = u + "</ul>";
		var v = x.getElementById("wx_titletree_content");
		v.innerHTML = u;
		var t = x.getElementById("wx_titletree_container");
		t.style.display = "";
		x.getElementById("wx_titletree_buttoncontainer").innerHTML = '<img src="' + k + 'submit.gif" onclick="okSelectedColFilter()">';
		setOutputPosition(d, t, x.getElementById("titletree_container_inner"), v);
		EventTools.addEventHandler(window.document, "mousedown", handleDocumentMouseDownForSelectedTree)
	}
}

function setOutputPosition(e, d, b, a) {
	d.style.width = COL_FILTER_btnObj.obj.spanOutputWidth + "px";
	d.style.top = (e.top + e.height) + "px";
	d.style.left = (e.left + e.width - d.offsetWidth) + "px";
	var c = COL_FILTER_btnObj.obj.paramsObj.filtermaxheight;
	if (c == null || c < 15) {
		c = 350
	}
	if (a.offsetHeight < c - 10) {
		b.style.height = (a.offsetHeight + 10) + "px"
	} else {
		b.style.height = c + "px"
	}
}

function okSelectedColFilter() {
	var f = COL_FILTER_btnObj.obj.treeNodesArr;
	var e = "";
	if (f && f.length > 0) {
		var b;
		var g;
		var c = 0;
		for (var d = 0; d < f.length; d++) {
			b = f[d];
			g = document.getElementById(b.nodeid);
			var a = g.getAttribute("checked");
			if (a && a == "true") {
				e = e + b.nodevalue + ";;";
				c++
			}
		}
		if (c == f.length) {
			e = ""
		} else {
			if (e.lastIndexOf(";;") == e.length - 2) {
				e = e.substring(0, e.length - 2)
			}
			if (e == "") {
				wx_warn("请选择要过滤的数据");
				return false
			}
		}
		filterReportData(e)
	}
	closeSelectedTree()
}

function makeFilterSelectList() {
	var g = new Array();
	if (!COL_FILTER_btnObj.obj.resultItemsXmlRoot) {
		return ""
	}
	var d = "";
	var h = "";
	var f = "";
	var a = COL_FILTER_btnObj.obj.recordCount;
	if (a <= 0) {
		return ""
	}
	var j = COL_FILTER_btnObj.obj.resultItemsXmlRoot.childNodes;
	for (var b = 0; b < a; b = b + 1) {
		var c = j.item(b);
		if (c.childNodes.length <= 0) {
			COL_FILTER_btnObj.obj.recordCount--;
			continue
		}
		h = c.firstChild.childNodes[0].nodeValue;
		if (c.childNodes.length == 1) {
			f = h
		} else {
			f = c.lastChild.childNodes[0].nodeValue
		}
		var e = " id='" + COL_FILTER_btnObj.obj.spanOutputId + "_" + b + "'";
		if (h && h != "" && h != "[nodata]" && h != "[error]") {
			e = e + " onmousedown=\"filterReportData('" + h + "')\""
		}
		e = e + ">" + f;
		d += COL_FILTER_selectSpanStart + e + COL_FILTER_selectSpanEnd
	}
	if (COL_FILTER_btnObj.obj.recordCount <= 0) {
		return ""
	}
	d = "<div id='" + COL_FILTER_btnObj.obj.spanOutputId + "_inner'>" + d + "</div>";
	return d
}

function filterReportData(d) {
	var a = COL_FILTER_btnObj.obj.metadataObj;
	var b = getComponentUrl(a.pageid, a.refreshComponentGuid, a.slave_reportid);
	var c = COL_FILTER_btnObj.obj.paramsObj.urlParamName;
	b = replaceUrlParamValue(b, c, d);
	if (COL_FILTER_btnObj.obj.paramsObj.multiply != "false") {
		b = removeReportNavigateInfosFromUrl(b, a, 1);
		b = replaceUrlParamValue(b, a.reportid + "_COL_FILTERID", c)
	} else {
		b = removeReportNavigateInfosFromUrl(b, a, null);
		hideSingleColFilterSelectBox()
	}
	refreshComponent(b)
}

function handleDocumentMouseDownForSingleColFilter(b) {
	var a = window.event ? window.event.srcElement : b.target;
	if (a == null) {
		hideSingleColFilterSelectBox()
	} else {
		while (a != null) {
			try {
				if (a.getAttribute("id") == COL_FILTER_btnObj.obj.spanOutputId) {
					return
				}
				a = a.parentNode
			} catch (c) {
				break
			}
		}
		hideSingleColFilterSelectBox()
	}
}

function hideSingleColFilterSelectBox() {
	if (COL_FILTER_btnObj != null) {
		document.getElementById(COL_FILTER_btnObj.obj.spanOutputId).style.display = "none";
		COL_FILTER_btnObj.obj.currentValueSelected = -1
	}
	EventTools.removeEventHandler(window.document, "mousedown", handleDocumentMouseDownForSingleColFilter)
}
var WX_colSeletedParamsObj = null;

function createTreeObjHtml(p, D, C) {
	var I = document;
	var K = getObjectByJsonString(D);
	var v = K.skin;
	var l = K.webroot;
	var g = K.reportguid;
	var u = getReportMetadataObj(g);
	var d = {
		metadataObj: u,
		paramsObj: K
	};
	if (WX_colSeletedParamsObj == null) {
		WX_colSeletedParamsObj = new Object()
	}
	WX_colSeletedParamsObj[g] = d;
	var m = I.getElementById(g + "_col_titlelist");
	if (m == null) {
		return
	}
	var h = m.getElementsByTagName("ITEM");
	var n;
	var J;
	var s;
	var F;
	var z;
	var H;
	var k;
	var E;
	var c;
	var o = l + "webresources/skin/" + v + "/images/coltitle_selected/";
	var a = '{img_rooturl:"' + o + '"';
	a = a + ',checkbox:"true"';
	a = a + ',treenodeimg:"true"';
	a = a + ",nodes:[";
	var q = "";
	for (var x = 0, y = h.length; x < y; x = x + 1) {
		n = h[x];
		s = n.getAttribute("nodeid");
		a = a + '{nodeid:"' + s + '"';
		a = a + ',title:"' + n.getAttribute("title") + '"';
		F = n.getAttribute("parentgroupid");
		if (F != null && F != "") {
			a = a + ',parentgroupid:"' + F + '"';
			q = q + s + ':"' + F + '",'
		}
		z = n.getAttribute("childids");
		if (z && z != "") {
			a = a + ',childids:"' + z + '"'
		}
		H = parseInt(n.getAttribute("layer"), 10);
		if (!H) {
			H = 0
		}
		a = a + ',layer:"' + H + '"';
		k = n.getAttribute("checked");
		if (!k) {
			k = "false"
		}
		a = a + ',checked:"' + k + '"';
		E = n.getAttribute("isControlCol");
		if (E == "true") {
			c = "hidden"
		} else {
			c = n.getAttribute("always");
			if (c == null) {
				c = "false"
			}
		}
		a = a + ',isalway:"' + c + '"';
		a = a + "},"
	}
	if (a.lastIndexOf(",") == a.length - 1) {
		a = a.substring(0, a.length - 1)
	}
	a = a + "]";
	if (q.lastIndexOf(",") == q.length - 1) {
		q = q.substring(0, q.length - 1)
	}
	a = a + ",parentidsMap:{" + q + "}";
	a = a + "}";
	var B = '<ul class="bbit-tree-root bbit-tree-lines">';
	B = B + showTreeNodes(a);
	B = B + "</ul>";
	var G = I.getElementById("wx_titletree_content");
	G.innerHTML = B;
	I.getElementById("wx_titletree_buttoncontainer").innerHTML = '<img src="' + o + 'submit.gif" onclick="okSelected(\'' + g + "')\">";
	var j = getElementAbsolutePosition(p);
	var A = I.getElementById("wx_titletree_container");
	if (K.width != null && K.width > 0) {
		A.style.width = K.width + "px"
	}
	A.style.display = "";
	var f = K.maxheight;
	if (f == null || f < 15) {
		f = 350
	}
	if (G.offsetHeight < f - 10) {
		I.getElementById("titletree_container_inner").style.height = (G.offsetHeight + 10) + "px"
	} else {
		I.getElementById("titletree_container_inner").style.height = f + "px"
	}
	var w = C || window.event;
	var b = getDocumentSize();
	var t = b.width - w.clientX;
	var r = b.height - w.clientY;
	if (t < A.offsetWidth) {
		A.style.left = (j.left - A.offsetWidth) + "px"
	} else {
		A.style.left = (j.left + j.width) + "px"
	}
	if (r < A.offsetHeight && w.clientY > A.offsetHeight) {
		A.style.top = (j.top - A.offsetHeight + j.height) + "px"
	} else {
		A.style.top = (j.top) + "px"
	}
	EventTools.addEventHandler(window.document, "mousedown", handleDocumentMouseDownForSelectedTree)
}

function okSelected(c) {
	var n = document;
	var h = WX_colSeletedParamsObj[c];
	var g = n.getElementById(c + "_col_titlelist");
	var p = g.getElementsByTagName("ITEM");
	var l = "";
	var k;
	var f;
	var b;
	var d = false;
	for (var e = 0, j = p.length; e < j; e++) {
		nodeItemObj = p[e];
		b = nodeItemObj.getAttribute("nodeid");
		f = n.getElementById(b);
		if (f == null) {
			continue
		}
		k = f.getAttribute("checked");
		if (k && k == "true") {
			l = l + b + ";";
			if (!d && nodeItemObj.getAttribute("isNonFixedCol") == "true") {
				d = true
			}
		}
	}
	if (!d) {
		wx_warn("至少选中一个非冻结数据列");
		return false
	}
	if (l.lastIndexOf(";") == l.length - 1) {
		l = l.substring(0, l.length - 1)
	}
	if (l == "") {
		wx_warn("请选择要显示/下载的列");
		return false
	}
	var o = h.metadataObj;
	var a = getComponentUrl(o.pageid, o.refreshComponentGuid, o.slave_reportid);
	a = replaceUrlParamValue(a, o.reportid + "_DYNDISPLAY_COLIDS", l);
	a = replaceUrlParamValue(a, o.reportid + "_DYNDISPLAY_COLIDS_ACTION", "true");
	var m = h.paramsObj.showreport_onpage_url;
	var q = h.paramsObj.showreport_dataexport_url;
	if (m == null || q == null || q == "" || m == q) {
		refreshComponent(a)
	} else {
		exportData(o.pageid, o.reportid, h.paramsObj.includeApplicationids, m, q, a)
	}
	closeSelectedTree()
}
var WX_rootTreeNodeId = "root_treenode_id";

function showTreeNodes(a) {
	var b = eval("(" + a + ")");
	var c = b.img_rooturl;
	var d = b.nodes;
	if (!d || d.length == 0) {
		return ""
	}
	var e = b.parentidsMap;
	var f;
	var g = "";
	var h = false;
	var j = false;
	var k = "";
	var l = new Object();
	var m = b.checkbox;
	var n = b.treenodeimg;
	for (var i = 0; i < d.length; i = i + 1) {
		f = d[i];
		id = f.nodeid;
		title = f.title;
		parentgroupid = f.parentgroupid;
		if (!parentgroupid || parentgroupid == "" || parentgroupid == WX_rootTreeNodeId) {
			parentgroupid = WX_rootTreeNodeId;
			k = k + id + ","
		}
		childids = f.childids;
		if (!childids) {
			childids = ""
		}
		layer = parseInt(f.layer);
		if (!layer) {
			layer = 0
		}
		checked = f.checked;
		if (!checked) {
			checked = "false"
		}
		if (checked == "true") {
			j = true
		}
		isalway = f.isalway;
		if (isalway == "hidden") {
			continue
		}
		if (isalway == null) {
			isalway = "false"
		}
		if (isalway == "true") {
			h = true
		}
		g = g + "<div ";
		g = g + ' title="' + title + '" id="' + id + '" parentgroupid="' + parentgroupid + '" childids="' + childids + '" layer="' + layer + '" always="' + isalway + '" checked="' + checked + '">';
		var o = "";
		var p = "";
		if (e && parentgroupid != WX_rootTreeNodeId) {
			var q = e[parentgroupid];
			while (true) {
				if (!q || q == "") {
					q = WX_rootTreeNodeId
				}
				var r = l[q];
				if (!r || r != "true") {
					o = "elbow-line.gif"
				} else {
					o = "elbow-line-none.gif"
				}
				p = "<img style='vertical-align:middle;' src=\"" + c + o + '">' + p;
				if (q == WX_rootTreeNodeId) {
					break
				}
				q = e[q]
			}
		}
		g = g + p;
		g = g + "<img ";
		if (isLastNodeInThisLayer(parentgroupid, i, d)) {
			o = "elbow-end.gif";
			l[parentgroupid] = "true"
		} else {
			o = "elbow.gif"
		}
		g = g + " style='vertical-align:middle;' src=\"" + c + o + '">';
		if (n && n == "true") {
			if (childids && childids != "") {
				o = "folder-open.gif"
			} else {
				o = "leaf.gif"
			}
			g = g + "<img  style='vertical-align:middle;' src=\"" + c + o + '">'
		}
		if (m && m == "true") {
			if (isalway && isalway == "true") {
				o = "checkbox_3.gif"
			} else {
				if (checked && checked == "true") {
					o = "checkbox_1.gif"
				} else {
					o = "checkbox_0.gif"
				}
			}
			g = g + '<img id="' + id + "_cb\" style='vertical-align:middle;'";
			if (!isalway || isalway == "false") {
				g = g + " onclick=\"processTreeNodeselected(this.id,'" + c + "')\""
			}
			g = g + ' src="' + c + o + '">'
		}
		g = g + title;
		g = g + "</div>"
	}
	if (k.lastIndexOf(",") == k.length - 1) {
		k = k.substring(0, k.length - 1)
	}
	var s = '<div id="' + WX_rootTreeNodeId + '" childids="' + k + '" always="' + h + '" checked="' + j + "\"><img style='vertical-align:middle;' src=\"" + c + 'root.gif">';
	if (m && m == "true") {
		var t = "";
		if (h == true) {
			t = "checkbox_3.gif"
		} else {
			if (j == true) {
				t = "checkbox_1.gif"
			} else {
				t = "checkbox_0.gif"
			}
		}
		s = s + '<img id="' + WX_rootTreeNodeId + "_cb\" style='vertical-align:middle;'";
		if (h != true) {
			s = s + " onclick=\"processTreeNodeselected(this.id,'" + c + "')\""
		}
		s = s + ' src="' + c + t + '">'
	}
	s = s + "</div>";
	g = s + g;
	return g
}

function handleDocumentMouseDownForSelectedTree(b) {
	var a = window.event ? window.event.srcElement : b.target;
	if (a == null || !isElementOrChildElement(a, "wx_titletree_container")) {
		closeSelectedTree()
	}
}

function closeSelectedTree() {
	document.getElementById("wx_titletree_container").style.display = "none";
	EventTools.removeEventHandler(window.document, "mousedown", handleDocumentMouseDownForSelectedTree)
}

function isLastNodeInThisLayer(a, c, b) {
	if (c == b.length - 1) {
		return true
	}
	if (!a) {
		a = ""
	}
	var f;
	for (var e = c + 1; e < b.length; e++) {
		f = b[e].parentgroupid;
		var d = b[e].isalway;
		if (d == "hidden") {
			continue
		}
		if (!f || f == "") {
			f = WX_rootTreeNodeId
		}
		if (f != a) {
			continue
		}
		return false
	}
	return true
}

function processTreeNodeselected(b, e) {
	var a = document.getElementById(b);
	var f = document.getElementById(b.substring(0, b.indexOf("_cb")));
	var c;
	var d = f.getAttribute("checked");
	if (d && d == "true") {
		c = false;
		a.src = e + "checkbox_0.gif";
		f.setAttribute("checked", "false")
	} else {
		c = true;
		a.src = e + "checkbox_1.gif";
		f.setAttribute("checked", "true")
	}
	processChildNodesSelected(f, c, e);
	processParentNodesSelected(f, c, e)
}

function processChildNodesSelected(h, c, g) {
	var a = h.getAttribute("childids");
	if (a && a != "") {
		var b = a.split(",");
		var f;
		var e;
		for (var d = 0; d < b.length; d++) {
			if (b[d] == "") {
				continue
			}
			f = document.getElementById(b[d]);
			if (!f) {
				continue
			}
			e = document.getElementById(b[d] + "_cb");
			if (!e) {
				continue
			}
			if (c) {
				e.src = g + "checkbox_1.gif";
				f.setAttribute("checked", "true")
			} else {
				e.src = g + "checkbox_0.gif";
				f.setAttribute("checked", "false")
			}
			processChildNodesSelected(f, c, g)
		}
	}
}

function processParentNodesSelected(l, j, m) {
	var n = l.getAttribute("parentgroupid");
	if (n && n != "") {
		var d = document.getElementById(n);
		if (!d) {
			return
		}
		var a = d.getAttribute("always");
		if (a && a == "true") {
			return
		}
		if (j) {
			d.setAttribute("checked", "true");
			var g = document.getElementById(n + "_cb");
			g.src = m + "checkbox_1.gif"
		} else {
			var b = d.getAttribute("childids");
			var k = b.split(",");
			var f;
			var e = false;
			var h;
			for (var c = 0; c < k.length; c++) {
				if (k[c] == "") {
					continue
				}
				f = document.getElementById(k[c]);
				if (!f) {
					continue
				}
				h = f.getAttribute("checked");
				if (h && h == "true") {
					e = true;
					break
				}
			}
			if (!e) {
				d.setAttribute("checked", "false");
				var g = document.getElementById(n + "_cb");
				g.src = m + "checkbox_0.gif"
			}
		}
		processParentNodesSelected(d, j, m)
	}
}

function expandOrCloseTreeNode(h, o, s, l, c) {
	var j = getTrObjOfTreeGroupRow(s);
	var n;
	var a = j.getAttribute("childDataIdSuffixes");
	var q = j.getAttribute("childGroupIdSuffixes");
	if (!a || a == "") {
		n = q
	} else {
		if (!q || q == "") {
			n = a
		} else {
			n = q + ";" + a
		}
	}
	if (n == null || n == "") {
		return false
	}
	var f = j.getAttribute("state");
	if (!f || f == "") {
		f = "open"
	}
	var g = document.getElementById(j.getAttribute("id") + "_img");
	if (f == "open") {
		j.setAttribute("state", "close");
		g.src = h + "webresources/skin/" + o + "/images/nodeclosed.gif"
	} else {
		j.setAttribute("state", "open");
		g.src = h + "webresources/skin/" + o + "/images/nodeopen.gif"
	}
	var m = new Object();
	var t = n.split(";");
	for (var p = 0; p < t.length; p = p + 1) {
		if (t[p] == "") {
			continue
		}
		var b = document.getElementById(l + t[p]);
		if (!b) {
			continue
		}
		var d = isExistParentStateClosed(l, b, m);
		if (!d) {
			b.style.display = ""
		} else {
			b.style.display = "none"
		}
	}
	if (c && c != "") {
		try {
			var k = document.getElementById(c);
			if (k) {
				k.fleXcroll.updateScrollBars()
			}
		} catch (r) {}
	}
}

function isExistParentStateClosed(f, e, d) {
	var b = e.getAttribute("parentTridSuffix");
	if (!b || b == "") {
		return false
	}
	if (d[b]) {
		return d[b] == "1"
	}
	var c = document.getElementById(f + b);
	var g = c.getAttribute("state");
	if (g && g == "close") {
		d[b] = "1";
		return true
	}
	var a = isExistParentStateClosed(f, c, d);
	if (a) {
		d[b] = "1"
	} else {
		d[b] = "0"
	}
	return d[b] == "1"
}

function getTrObjOfTreeGroupRow(b) {
	var a = b.parentNode;
	if (!a) {
		return null
	}
	if (a.tagName == "TR") {
		var c = a.getAttribute("id");
		if (c && c.indexOf("trgroup_") > 0) {
			return a
		}
	}
	return getTrObjOfTreeGroupRow(a)
}

function exportData(e, o, n, b, m, c) {
	if (n == null || n == "") {
		return
	}
	if (c == null || c == "") {
		var j = new Array();
		var g = n.split(";");
		var a = new Object();
		for (var h = 0, l = g.length; h < l; h++) {
			if (g[h] != null && g[h] != "" && a[g] != "true") {
				a[g[h]] = "true";
				j[j.length] = g[h]
			}
		}
		if (j != null && j.length == 1) {
			var r = getComponentGuidById(e, j[0]);
			var q = getReportMetadataObj(r);
			if (q == null) {
				c = getComponentUrl(e, null, null)
			} else {
				c = getComponentUrl(e, q.refreshComponentGuid, q.slave_reportid)
			}
		} else {
			c = getComponentUrl(e, null, null);
			var k = null;
			var d = null;
			var f = null;
			for (var h = 0, l = j.length; h < l; h++) {
				d = getComponentGuidById(e, j[h]);
				k = getReportMetadataObj(d);
				if (k == null || k.slave_reportid == null || k.slave_reportid == "") {
					continue
				}
				f = getComponentUrl(e, k.refreshComponentGuid, k.slave_reportid);
				c = mergeUrlParams(c, f, false)
			}
		}
	}
	c = replaceUrlParamValue(c, "COMPONENTIDS", o);
	c = replaceUrlParamValue(c, "INCLUDE_APPLICATIONIDS", n);
	if (b.indexOf("?") > 0) {
		if (b.lastIndexOf("&") != b.length - 1) {
			b = b + "&"
		}
	} else {
		if (b.lastIndexOf("?") != b.length - 1) {
			b = b + "?"
		}
	}
	if (m.indexOf("?") > 0) {
		if (m.lastIndexOf("&") != m.length - 1) {
			m = m + "&"
		}
	} else {
		if (m.lastIndexOf("?") != m.length - 1) {
			m = m + "?"
		}
	}
	var p = c.indexOf(b);
	c = c.substring(0, p) + m + c.substring(p + b.length);
	postlinkurl(c, true)
}

function mergeUrlParams(l, h, f) {
	if (l == null || l == "" || h == null || h == "") {
		return l
	}
	var g = splitUrlAndParams(l, false);
	var d = splitUrlAndParams(h, false);
	var j = g[0];
	var e = g[1];
	var c = d[1];
	if (c == null) {
		return l
	}
	var b = null;
	var a = null;
	if (f) {
		b = c;
		a = e
	} else {
		b = e;
		a = c
	}
	var i = "?";
	if (b != null) {
		for (var k in b) {
			j = j + i + k + "=" + b[k];
			i = "&";
			if (a != null) {
				delete a[k]
			}
		}
	}
	if (a != null) {
		for (var k in a) {
			j = j + i + k + "=" + a[k];
			i = "&"
		}
	}
	return j
}

function reloadSelectBoxData(k, h, b, n, a) {
	var e = getComponentGuidById(k, h);
	var m = getReportMetadataObj(e);
	var d = getComponentUrl(k, m.refreshComponentGuid, m.slave_reportid);
	var r = "";
	if (!a) {
		for (var c in n) {
			if (c == null || c == "") {
				continue
			}
			if (n[c] == null || n[c] == "") {
				continue
			}
			n[c] = createGetColDataObjByString(n[c])
		}
		r = getChildIdAndParentValues(m, b, n)
	} else {
		var s = new Array();
		for (var t in n) {
			r += t + ";";
			if (n[t] != null && n[t] != "") {
				var l = n[t].split(";");
				for (var o = 0; o < l.length; o++) {
					if (l[o] != null && l[o] != "") {
						s[s.length] = l[o]
					}
				}
			}
		}
		if (r != "") {
			r = "condition:" + r
		}
		var f;
		var g;
		for (var p = 0, q = s.length; p < q; p++) {
			g = e + "_condition_" + s[p];
			f = document.getElementById(g);
			if (f == null) {
				continue
			}
			d = replaceUrlParamValue(d, s[p], getInputBoxValue(g, f.getAttribute("typename")))
		}
	}
	d = replaceUrlParamValue(d, "SELECTBOXIDS_AND_PARENTVALUES", r);
	d = replaceUrlParamValue(d, "ACTIONTYPE", "GetSelectBoxDataList");
	d = replaceUrlParamValue(d, "REPORTID", h);
	sendAsynRequestToServer(d, refreshSelectBoxData, onRefreshSelectBoxDataErrorMethod, true)
}

function getChildIdAndParentValues(o, i, h) {
	var b = null,
		q = null,
		p = null,
		m = null;
	var k = i.getAttribute("id");
	var l = "";
	if (o.reportfamily == ReportFamily.EDITABLELIST2 || o.reportfamily == ReportFamily.LISTFORM || o.reportfamily == ReportFamily.EDITABLEDETAIL2 || o.reportfamily == ReportFamily.EDITABLELIST) {
		l = "TD"
	} else {
		l = "FONT"
	}
	var a = getInputboxParentElementObj(i, l);
	b = a.getAttribute("value_name");
	var d = getUpdateColDestObj(a, o.reportguid, o.reportfamily, null);
	if (d == null) {
		q = getInputBoxValue(k, i.getAttribute("typename"))
	} else {
		q = getInputBoxLabel(k, i.getAttribute("typename"));
		m = getInputBoxValue(k, i.getAttribute("typename"));
		p = d.getAttribute("value_name")
	}
	var c = "";
	var n = k.lastIndexOf("__");
	if (n > 0) {
		c = k.substring(n + 2)
	}
	var j = null;
	if (c != "") {
		j = document.getElementById(o.reportguid + "_tr_" + c)
	}
	childSelectBoxParams = "";
	var g;
	for (var e in h) {
		if (j != null) {
			g = getEditableListReportColValuesInRow(j, h[e])
		} else {
			g = getEditableReportColValues(o.pageid, o.reportid, h[e], null)
		}
		var f = formatParentColValuesToParamsString(g, b, q, p, m);
		if (f == null || f == "") {
			continue
		}
		childSelectBoxParams += "wx_inputboxid" + SAVING_NAMEVALUE_SEPERATOR + e;
		if (c != "") {
			childSelectBoxParams += "__" + c
		}
		childSelectBoxParams += SAVING_COLDATA_SEPERATOR + f + SAVING_ROWDATA_SEPERATOR
	}
	if (childSelectBoxParams.lastIndexOf(SAVING_ROWDATA_SEPERATOR) == childSelectBoxParams.length - SAVING_ROWDATA_SEPERATOR.length) {
		childSelectBoxParams = childSelectBoxParams.substring(0, childSelectBoxParams.length - SAVING_ROWDATA_SEPERATOR.length)
	}
	return childSelectBoxParams
}

function reloadSelectBoxDataByFocus(d, i, e, a) {
	var h = createGetColDataObjByString(a);
	var g = e.getAttribute("id");
	var l = null;
	if (g.indexOf("__") > 0) {
		l = getEditableListReportColValuesInRow(getParentElementObj(e, "TR"), h)
	} else {
		l = getEditableReportColValues(d, i, h, null)
	}
	if (e.tagName == "TD") {
		var j = g.lastIndexOf("__td");
		g = g.substring(0, j) + g.substring(j + "__td".length)
	}
	var f = formatParentColValuesToParamsString(l, null, null, null, null);
	var c = getComponentGuidById(d, i);
	var k = getReportMetadataObj(c);
	var b = getComponentUrl(d, k.refreshComponentGuid, k.slave_reportid);
	f = "wx_inputboxid" + SAVING_NAMEVALUE_SEPERATOR + g + SAVING_COLDATA_SEPERATOR + f;
	b = replaceUrlParamValue(b, "SELECTBOXIDS_AND_PARENTVALUES", f);
	b = replaceUrlParamValue(b, "ACTIONTYPE", "GetSelectBoxDataList");
	b = replaceUrlParamValue(b, "REPORTID", i);
	sendAsynRequestToServer(b, refreshSelectBoxData, onRefreshSelectBoxDataErrorMethod, false)
}

function formatParentColValuesToParamsString(h, a, i, b, c) {
	var e = "";
	var d;
	var f;
	for (var g in h) {
		d = h[g];
		if (d == null) {
			continue
		}
		if (g == a) {
			f = i
		} else {
			if (b != null && g == b) {
				f = c
			} else {
				f = d.value;
				if ((f == null || f == "") && (d.value_name == null || d.value_name == "")) {
					f = d.oldvalue
				}
				if (f == null) {
					f = ""
				}
			}
		}
		e += g + SAVING_NAMEVALUE_SEPERATOR + encodeURIComponent(f) + SAVING_COLDATA_SEPERATOR
	}
	if (e.lastIndexOf(SAVING_COLDATA_SEPERATOR) == e.length - SAVING_COLDATA_SEPERATOR.length) {
		e = e.substring(0, e.length - SAVING_COLDATA_SEPERATOR.length)
	}
	return e
}

function refreshSelectBoxData(a, b) {
	var c = a.responseText;
	if (c == null || c == " " || c == "") {
		return
	}
	var d = null;
	try {
		d = eval("(" + c + ")")
	} catch (e) {
		wx_error("获取子下拉框选项失败");
		throw e
	}
	var f = d.pageid;
	var g = d.reportid;
	var h = d.isConditionBox;
	delete d.pageid;
	delete d.reportid;
	delete d.isConditionBox;
	var i = getComponentGuidById(f, g);
	var j = getReportMetadataObj(i);
	var k;
	var l = document;
	for (var m in d) {
		k = d[m];
		if (k == null || k.length == 0) {
			continue
		}
		if (k[0].selectboxtype == "selectbox") {
			filledChildSelectboxOptions(j, m, k, b)
		} else {
			if (k[0].selectboxtype == "checkbox") {} else {
				if (k[0].selectboxtype == "radiobox") {} else {
					continue
				}
			}
		}
	}
}

function filledChildSelectboxOptions(h, c, e, k) {
	var a = document.getElementById(c);
	if (a == null) {
		return
	}
	var j;
	if (k) {
		j = getInputBoxValue(c, a.getAttribute("typename"))
	} else {
		var b = getParentElementObj(a, "TD");
		b = getUpdateColDestObj(b, h.reportguid, h.reportfamily, b);
		j = b.getAttribute("value")
	}
	if (j == null) {
		j = ""
	}
	if (a.options.length > 0) {
		for (var f = 0, d = a.options.length; f < d; f++) {
			a.remove(a.options[f])
		}
	}
	if (e.length <= 1) {
		return
	}
	var g;
	for (var f = 1; f < e.length; f++) {
		g = new Option(e[f].name, e[f].value);
		a.options.add(g);
		if (isSelectedValueForSelectedBox(j, e[f].value, a)) {
			g.selected = true
		}
	}
	if (k) {
		a.focus();
		if (a.onchange) {
			a.onchange()
		}
	}
}

function onRefreshSelectBoxDataErrorMethod(a) {
	wx_error("获取子选择框选项数据失败")
}

function createGetColDataObjByString(d) {
	var c = new Object();
	var a = d.split(";");
	for (var b = 0; b < a.length; b++) {
		if (a[b] == null || a[b] == "") {
			continue
		}
		c[a[b]] = true
	}
	return c
}

function popupPageByPopupInputbox(k, f) {
	var i = document.getElementById(k);
	if (i == null) {
		wx_warn("没有取到id为" + k + "的弹出输入框");
		return
	}
	var d = i.value;
	if (d == null) {
		d = ""
	}
	var g = getObjectByJsonString(f);
	if (g == null) {
		wx_warn("没有取到id为" + inputboxid + "的弹出输入框参数，无法弹出窗口");
		return
	}
	var c = g.pageid;
	var h = g.reportid;
	var e = g.popupPageUrl;
	var b = getComponentGuidById(c, h);
	var j = getReportMetadataObj(b);
	e = replaceDynParamsInPoppageUrl(j, g, i, e);
	var a = "?";
	if (e.indexOf("?") > 0) {
		a = "&"
	}
	e = e + a + "SRC_PAGEID=" + c;
	e = e + "&SRC_REPORTID=" + h;
	e = e + "&INPUTBOXID=" + k;
	e = e + "&OLDVALUE=" + encodeURIComponent(d);
	wx_winpage(e, getObjectByJsonString(g.popupparams))
}

function popupPageByFileUploadInputbox(m, h, a) {
	var k = document.getElementById(m);
	if (k == null) {
		wx_warn("没有取到id为" + m + "的文件上传输入框");
		return
	}
	var g = null;
	if (a == "image") {
		g = k.getAttribute("srcpath")
	} else {
		g = k.value
	}
	if (g == null) {
		g = ""
	}
	var i = getObjectByJsonString(h);
	if (i == null) {
		wx_warn("没有取到id为" + inputboxid + "的文件上传输入框参数，无法弹出窗口");
		return
	}
	var d = i.pageid;
	var j = i.reportid;
	var e = i.popupPageUrl;
	var c = getComponentGuidById(d, j);
	var l = getReportMetadataObj(c);
	e = replaceDynParamsInPoppageUrl(l, i, k, e);
	var f = WXConfig.showreport_url;
	var b = "?";
	if (f.indexOf("?") > 0) {
		b = "&"
	}
	if (e != null && e != "") {
		f += b + e;
		b = "&"
	}
	f = f + b + "PAGEID=" + d + "&REPORTID=" + j + "&INPUTBOXID=" + m + "&ACTIONTYPE=ShowUploadFilePage&FILEUPLOADTYPE=fileinputbox";
	f = f + "&OLDVALUE=" + encodeURIComponent(g);
	wx_winpage(f, getObjectByJsonString(i.popupparams))
}

function replaceDynParamsInPoppageUrl(o, u, m, j) {
	if (j == null || j == "") {
		return j
	}
	var h = o.pageid;
	var d = o.reportguid;
	var e = u.paramColumns;
	var g = u.paramConditions;
	if (e != null && e != "") {
		var s = createGetColDataObjByString(e);
		var n = null;
		if (o.reportfamily == ReportFamily.EDITABLELIST2 || o.reportfamily == ReportFamily.LISTFORM) {
			var k = getTrDataObj(d, m);
			if (k != null) {
				n = getEditableListReportColValuesInRow(k, s)
			}
		} else {
			if (o.reportfamily == ReportFamily.EDITABLEDETAIL2) {
				n = getEditableDetailReportColValues(h, d, s, true)
			} else {
				if (o.reportfamily == ReportFamily.EDITABLEDETAIL || o.reportfamily == ReportFamily.FORM) {
					n = getEditableDetailReportColValues(h, d, s, false)
				} else {
					return
				}
			}
		}
		if (n != null) {
			var c, f;
			for (var t in n) {
				c = n[t];
				var a = "@{" + c.name + "}";
				f = c.value;
				if ((f == null || f == "") && (c.value_name == null || c.value_name == "")) {
					f = c.oldvalue
				}
				if (f == null) {
					f = ""
				}
				while (j.indexOf(a) >= 0) {
					j = j.replace(a, encodeURIComponent(f))
				}
			}
		}
		for (var t in s) {
			var a = "@{" + t + "}";
			while (j.indexOf(a) >= 0) {
				j = j.replace(a, "")
			}
		}
	}
	if (g != null && g != "") {
		var q = g.split(";");
		var b, l, p;
		for (var r = 0; r < q.length; r++) {
			b = q[r];
			if (b == null || b == "") {
				continue
			}
			p = document.getElementById(d + "_condition_" + b);
			if (p == null) {
				l = ""
			} else {
				l = getInputBoxValue(p.getAttribute("id"), p.getAttribute("typename"));
				if (l == null) {
					l = ""
				}
			}
			var a = "condition{" + b + "}";
			while (j.indexOf(a) >= 0) {
				j = j.replace(a, encodeURIComponent(l))
			}
		}
	}
	return j
}

function getTrDataObj(a, b) {
	if (b == null) {
		return null
	}
	var c = getParentElementObj(b, "TR");
	while (c != null) {
		if (isEditableListReportTr(a, c)) {
			return c
		}
		c = getParentElementObj(c, "TR")
	}
	return null
}
var WX_PARENT_INPUTBOXID = "";

function setPopUpBoxValueToParent(e, c, d, b, g) {
	var f = document.getElementById(c);
	if (!f) {
		wx_warn("没有取到id为" + c + "的弹出源输入框");
		return false
	}
	WX_PARENT_INPUTBOXID = c;
	if (f.tagName == "IMG") {
		f.src = e;
		f.setAttribute("srcpath", e)
	} else {
		f.value = e
	}
	if (c.indexOf(b + "_condition_") < 0) {
		addInputboxDataForSaving(b, f);
		var a = getReportMetadataObj(b);
		if (a.reportfamily == ReportFamily.EDITABLELIST2 || a.reportfamily == ReportFamily.LISTFORM || a.reportfamily == ReportFamily.EDITABLEDETAIL2) {
			fillInputBoxValueToParentTd(f, g, b, a.reportfamily, parseInt(d))
		}
	}
}

function closePopUpPageEvent(a) {
	if (WX_PARENT_INPUTBOXID && WX_PARENT_INPUTBOXID != "") {
		var b = document.getElementById(WX_PARENT_INPUTBOXID);
		if (b) {
			b.focus()
		}
	}
	WX_PARENT_INPUTBOXID = ""
}

function isSelectedValueForSelectedBox(d, c, f) {
	if (d == null || c == null) {
		return false
	}
	var e = f.getAttribute("separator");
	if (e == null || e == "") {
		return d == c
	} else {
		if (d == c) {
			return true
		}
		if (e != " ") {
			d = wx_trim(d)
		}
		var a = d.split(e);
		for (var b = 0; b < a.length; b++) {
			if (a[b] == c) {
				return true
			}
		}
		return false
	}
}
var WX_contextmenuObj = null;

function showcontextmenu(g, f) {
	if (WX_contextmenuObj != null) {
		WX_contextmenuObj.style.visibility = "hidden"
	}
	WX_contextmenuObj = document.getElementById(g);
	if (WX_contextmenuObj == null) {
		return
	}
	var h = WX_contextmenuObj.getAttribute("isEmpty");
	if (h == "true") {
		return
	}
	var c = f || window.event;
	var i = getDocumentSize();
	var b = i.width - c.clientX;
	var a = i.height - c.clientY;
	var d = getDocumentScroll();
	if (b < WX_contextmenuObj.offsetWidth) {
		WX_contextmenuObj.style.left = (d.scrollLeft + c.clientX - WX_contextmenuObj.offsetWidth) + "px"
	} else {
		WX_contextmenuObj.style.left = (d.scrollLeft + c.clientX) + "px"
	}
	if (a < WX_contextmenuObj.offsetHeight) {
		WX_contextmenuObj.style.top = (d.scrollTop + c.clientY - WX_contextmenuObj.offsetHeight) + "px"
	} else {
		WX_contextmenuObj.style.top = (d.scrollTop + c.clientY) + "px"
	}
	WX_contextmenuObj.style.visibility = "visible";
	if (window.event) {
		c.returnValue = false
	} else {
		c.preventDefault()
	}
	document.onclick = hidecontextmenu;
	return false
}

function hidecontextmenu() {
	WX_contextmenuObj.style.visibility = "hidden"
}

function highlightmenuitem(a) {
	var c = a || window.event;
	var b = c.srcElement || c.target;
	if (b.className == "contextmenuitems_enabled") {
		b.style.backgroundColor = "highlight";
		b.style.color = "white"
	} else {
		if (b.className == "contextmenuitems_disabled") {
			b.style.backgroundColor = "highlight"
		}
	}
}

function lowlightmenuitem(a) {
	var c = a || window.event;
	var b = c.srcElement || c.target;
	if (b.className == "contextmenuitems_enabled") {
		b.style.backgroundColor = "";
		b.style.color = "black"
	} else {
		if (b.className == "contextmenuitems_disabled") {
			b.style.backgroundColor = ""
		}
	}
}

function forwardPageWithBack(b, a, d) {
	a = paramdecode(a);
	var f = document.getElementById(b + "_url_id");
	var c = f.getAttribute("encodevalue");
	if (c == null || c == "") {
		wx_error("没有取到本页面的URL，跳转失败");
		return
	}
	var e = f.getAttribute("ancestorPageUrls");
	if (e == null || e == "") {
		e = c
	} else {
		e = c + "||" + e
	}
	a = replaceUrlParamValue(a, "ancestorPageUrls", e);
	a = replaceUrlParamValue(a, "refreshComponentGuid", "[OUTERPAGE]" + b);
	a = replaceUrlParamValue(a, "SLAVE_REPORTID", null);
	if (d != null && d != "") {
		a = d(a)
	}
	if (a != null && a != "") {
		refreshComponent(a)
	}
}

function removeLazyLoadParamsFromUrl(a, e, c) {
	a = replaceUrlParamValue(a, e.reportid + "_lazyload", null);
	a = replaceUrlParamValue(a, e.reportid + "_lazyloadmessage", null);
	if (c) {
		var g = e.metaDataSpanObj.getAttribute("relateConditionReportIds");
		if (g != null && g != "") {
			var f = g.split(";");
			var d;
			for (var b = 0; b < f.length; b = b + 1) {
				d = f[b];
				if (d == null || d == "") {
					continue
				}
				a = replaceUrlParamValue(a, d + "_lazyload", null);
				a = replaceUrlParamValue(a, d + "_lazyloadmessage", null)
			}
		}
	}
	return a
}

function invokeServerActionForReportDataImpl(l, k, p, d, x, m, v, a) {
	var h = getComponentGuidById(l, k);
	var q = getReportMetadataObj(h);
	if (q == null) {
		return
	}
	if (v == "") {
		v = null
	}
	if (v != null && typeof v != "function") {
		wx_warn("传入的beforeCallbackMethod参数不是函数对象");
		return
	}
	if (a == "") {
		a = null
	}
	if (a != null && typeof a != "function") {
		wx_warn("传入的afterCallbackMethod参数不是函数对象");
		return
	}
	var s = null;
	if (p.indexOf("button{") == 0) {
		s = d
	} else {
		if (p.indexOf("button_autoreportdata{") == 0) {
			var o = p.indexOf("button_autoreportdata{");
			p = p.substring(0, o) + "button{" + p.substring(o + "button_autoreportdata{".length)
		}
		s = new Array();
		var j = convertToArray(getEditableReportColValues(l, k, null, d));
		if (j != null && j.length > 0) {
			var f = null;
			var c = null;
			for (var r = 0, u = j.length; r < u; r++) {
				f = new Object();
				var t = false;
				for (var w in j[r]) {
					c = j[r][w];
					if (c == null || c.name == null || c.name == "") {
						continue
					}
					t = true;
					if (c.value == null) {
						j[r].value = ""
					}
					f[c.name] = c.value;
					if (c.oldname == null || c.oldname == "" || c.oldname == c.name) {
						continue
					}
					if (c.oldvalue == null) {
						c.oldvalue = ""
					}
					f[c.oldname] = c.oldvalue
				}
				if (t) {
					s[s.length] = f
				}
			}
		}
		if (s.length == 0) {
			wx_warn("没有取到要操作的报表数据！");
			return
		}
	}
	if (v != null) {
		if (s == null) {
			s = new Array()
		}
		if (x == null) {
			x = new Object()
		}
		var g = v(s, x);
		if (g !== true) {
			return
		}
	}
	var n = null;
	if (a != null) {
		n = h
	}
	var b = assempleServerActionDataParams(n, s);
	var e = getComponentUrl(q.pageid, q.refreshComponentGuid, q.slave_reportid);
	if (m) {
		e = removeReportNavigateInfosFromUrl(e, q, null);
		e = e + "&WX_SERVERACTION_SHOULDREFRESH=true"
	}
	if (b != null && b != "") {
		e = e + "&WX_SERVERACTION_PARAMS=" + b
	}
	b = getCustomizedParamsObjAsString(x);
	if (b != null && b != "") {
		e = e + "&WX_SERVERACTION_CUSTOMIZEDPARAMS=" + b
	}
	if (a != null) {
		e = e + "&WX_SERVERACTION_CALLBACKMETHOD=" + getFunctionNameByFunctionObj(a)
	}
	e = e + "&WX_SERVERACTION_COMPONENTID=" + k;
	e = e + "&WX_SERVERACTION_SERVERCLASS=" + p;
	refreshComponent(e)
}

function getCustomizedParamsObjAsString(c) {
	if (c == null || c == "") {
		return ""
	}
	var b = "";
	for (var a in c) {
		if (c[a] == null || c[a] == "") {
			continue
		}
		b += a + SAVING_NAMEVALUE_SEPERATOR + encodeURIComponent(c[a]) + SAVING_COLDATA_SEPERATOR
	}
	if (b.lastIndexOf(SAVING_COLDATA_SEPERATOR) == b.length - SAVING_COLDATA_SEPERATOR.length) {
		b = b.substring(0, b.length - SAVING_COLDATA_SEPERATOR.length)
	}
	return b
}

function invokeServerActionForComponentImpl(c, i, d, g, b, f) {
	if (f != null && typeof f != "function") {
		wx_warn("传入的回调函数不是函数对象");
		return
	}
	var k = getComponentGuidById(c, i);
	var e = getComponentMetadataObj(k);
	if (e == null) {
		return
	}
	var a = getComponentUrl(c, e.refreshComponentGuid, e.metaDataSpanObj.getAttribute("slave_reportid"));
	if (a == null || a == "") {
		return
	}
	if (b) {
		a = resetComponentUrl(c, i, a, "navigate");
		a = a + "&WX_SERVERACTION_SHOULDREFRESH=true"
	}
	if (i == null || i == "") {
		i = c
	}
	a = a + "&WX_SERVERACTION_COMPONENTID=" + i;
	var h = null;
	if (f != null) {
		a = a + "&WX_SERVERACTION_CALLBACKMETHOD=" + getFunctionNameByFunctionObj(f);
		h = k
	}
	var j = assempleServerActionDataParams(h, g);
	if (j != null && j != "") {
		a = a + "&WX_SERVERACTION_PARAMS=" + j
	}
	a = a + "&WX_SERVERACTION_SERVERCLASS=" + d;
	refreshComponent(a)
}

function getFunctionNameByFunctionObj(b) {
	if (b == null || typeof b != "function") {
		return ""
	}
	var c = wx_trim(b.toString());
	var a = c.indexOf("function");
	if (a < 0) {
		return ""
	}
	c = c.substring(a + "function".length);
	c = c.substring(0, c.indexOf("("));
	return wx_trim(c)
}

function invokeServerActionImpl(g, a, c, f) {
	if (c != null && typeof c != "function") {
		wx_warn("传入的回调函数不是函数对象");
		return
	}
	if (f != null && typeof f != "function") {
		wx_warn("传入的出错处理函数不是函数对象");
		return
	}
	var b = WXConfig.showreport_url;
	var d = "?";
	if (b.indexOf("?") > 0) {
		d = "&"
	}
	b = b + d + "ACTIONTYPE=invokeServerAction";
	var h = assempleServerActionDataParams(null, a);
	if (h != null && h != "") {
		b = b + "&WX_SERVERACTION_PARAMS=" + h
	}
	b = b + "&WX_SERVERACTION_SERVERCLASS=" + g;
	var e = b.substring(b.indexOf("?") + 1);
	b = b.substring(0, b.indexOf("?"));
	XMLHttpREPORT.sendReq("POST", b, e, c, f, a)
}

function assempleServerActionDataParams(e, b) {
	if (b == null || b == "") {
		return ""
	}
	var f = convertToArray(b);
	var g = "";
	for (var d = 0, a = f.length; d < a; d++) {
		var h = "";
		for (var c in f[d]) {
			if (c == null || c == "") {
				continue
			}
			if (f[d][c] == null) {
				f[d][c] = ""
			}
			h = h + c + SAVING_NAMEVALUE_SEPERATOR + encodeURIComponent(f[d][c]) + SAVING_COLDATA_SEPERATOR
		}
		if (h.lastIndexOf(SAVING_COLDATA_SEPERATOR) == h.length - SAVING_COLDATA_SEPERATOR.length) {
			h = h.substring(0, h.length - SAVING_COLDATA_SEPERATOR.length)
		}
		if (h == "") {
			continue
		}
		g = g + h + SAVING_ROWDATA_SEPERATOR
	}
	if (g.lastIndexOf(SAVING_ROWDATA_SEPERATOR) == g.length - SAVING_ROWDATA_SEPERATOR.length) {
		g = g.substring(0, g.length - SAVING_ROWDATA_SEPERATOR.length)
	}
	if (g == "") {
		return ""
	}
	if (e != null && e != "") {
		if (WX_ALL_SAVEING_DATA == null) {
			WX_ALL_SAVEING_DATA = new Object()
		}
		WX_ALL_SAVEING_DATA[e] = f
	}
	return g
}

function fixedRowColTable(a) {
	new fixedRowColTableObj(a)
}
var WX_M_FIXEDCOLSWIDTH;

function fixedRowColTableObj(E) {
	var m = E.pageid;
	var l = E.reportid;
	var e = getComponentGuidById(m, l);
	var o = getReportMetadataObj(e);
	var a = o.metaDataSpanObj.getAttribute("ft_fixedRowsCount");
	var D = o.metaDataSpanObj.getAttribute("ft_fixedColids");
	var d = o.metaDataSpanObj.getAttribute("ft_totalColCount");
	if ((a == null || a == "") && (D == null || D == "")) {
		return
	}
	if (d == null || d == "") {
		return
	}
	this.fixedRowsCount = parseInt(a);
	if (D != null && D != "") {
		var u = new Array();
		var r = D.split(";");
		for (var z = 0, A = r.length; z < A; z++) {
			if (r[z] == null || r[z] == "") {
				continue
			}
			u[u.length] = r[z]
		}
		this.fixedColidsArr = u;
		this.fixedColsCount = u.length
	} else {
		this.fixedColsCount = 0;
		this.fixedColidsArr = null
	}
	this.colcnt = parseInt(d);
	if (this.fixedRowsCount < 0) {
		this.fixedRowsCount = 0
	}
	if (this.fixedColsCount < 0) {
		this.fixedColsCount = 0
	}
	if (this.colcnt <= 0) {
		return
	}
	this.tableObj = document.getElementById(e + "_data");
	var b = this.tableObj.tBodies[0].rows;
	if (b == null || b.length <= 0) {
		return
	}
	if (this.fixedRowsCount > b.length) {
		this.fixedRowsCount = b.length
	}
	this.divContainerObj = document.createElement("DIV");
	this.divFixedHeaderObj = this.divContainerObj.cloneNode(false);
	this.divHeaderObj = this.divContainerObj.cloneNode(false);
	this.divHeaderInnerObj = this.divContainerObj.cloneNode(false);
	this.divFixedDataObj = this.divContainerObj.cloneNode(false);
	this.divFixedDataInnerObj = this.divContainerObj.cloneNode(false);
	this.divDataObj = this.divContainerObj.cloneNode(false);
	this.colGroupObj = document.createElement("COLGROUP");
	this.tableObj.style.margin = "0px";
	if (this.tableObj.getElementsByTagName("COLGROUP").length > 0) {
		this.tableObj.removeChild(this.tableObj.getElementsByTagName("COLGROUP")[0])
	}
	this.parentDivObj = this.tableObj.parentNode;
	this.tableParentDivHeight = this.parentDivObj.offsetHeight;
	this.tableParentDivWidth = this.parentDivObj.offsetWidth;
	if (this.parentDivObj.style.height == null || this.parentDivObj.style.height == "") {
		this.parentDivObj.style.height = this.tableParentDivHeight + "px"
	}
	this.divContainerObj.className = "cls-fixed-divcontainer";
	this.divFixedHeaderObj.className = "cls-fixed-fixedHeader";
	this.divHeaderObj.className = "cls-fixed-header";
	this.divHeaderInnerObj.className = "cls-fixed-headerInner";
	this.divFixedDataObj.className = "cls-fixed-fixeddata";
	this.divFixedDataInnerObj.className = "cls-fixed-fixeddataInner";
	this.divDataObj.className = "cls-fixed-data";
	this.divDataObj.id = e + "_fixeddata";
	var c, C;
	this.headerTableObj = this.tableObj.cloneNode(false);
	if (this.tableObj.tHead) {
		c = this.tableObj.tHead;
		this.headerTableObj.appendChild(c.cloneNode(false));
		C = this.headerTableObj.tHead
	} else {
		c = this.tableObj.tBodies[0];
		this.headerTableObj.appendChild(c.cloneNode(false));
		C = this.headerTableObj.tBodies[0]
	}
	c = c.rows;
	for (var z = 0; z < this.fixedRowsCount; z++) {
		C.appendChild(c[z].cloneNode(true))
	}
	this.divHeaderInnerObj.appendChild(this.headerTableObj);
	if (this.fixedColsCount > 0) {
		this.fixedHeaderTableObj = this.headerTableObj.cloneNode(true);
		this.divFixedHeaderObj.appendChild(this.fixedHeaderTableObj);
		this.sFDataTable = this.tableObj.cloneNode(true);
		this.divFixedDataInnerObj.appendChild(this.sFDataTable)
	}
	var p = getValidRowidx(b, this.colcnt);
	var y = 0;
	for (var z = 0, A = p.cells.length; z < A; z++) {
		var g = p.cells[z].colSpan;
		var B = p.cells[z].style.display == "none";
		var v = p.cells[z].offsetWidth;
		if (g > 1) {
			if (!B) {
				v = v / g
			}
		} else {
			g = 1
		}
		for (var w = 0; w < g; w++) {
			var t = document.createElement("COL");
			if (B) {
				if (!isIE) {
					this.colGroupObj.appendChild(t);
					this.colGroupObj.lastChild.style.display = "none"
				}
			} else {
				this.colGroupObj.appendChild(t);
				if (y < this.fixedColsCount) {
					if (WX_M_FIXEDCOLSWIDTH == null) {
						WX_M_FIXEDCOLSWIDTH = new Object()
					}
					var n = WX_M_FIXEDCOLSWIDTH[e + this.fixedColidsArr[y]];
					if (n == null || n == "") {
						WX_M_FIXEDCOLSWIDTH[e + this.fixedColidsArr[y]] = v;
						n = v
					}
					this.colGroupObj.lastChild.setAttribute("width", n)
				} else {
					this.colGroupObj.lastChild.setAttribute("width", v)
				}
				y++
			}
		}
	}
	this.tableObj.insertBefore(this.colGroupObj.cloneNode(true), this.tableObj.firstChild);
	this.headerTableObj.insertBefore(this.colGroupObj.cloneNode(true), this.headerTableObj.firstChild);
	if (this.fixedColsCount > 0) {
		this.sFDataTable.insertBefore(this.colGroupObj.cloneNode(true), this.sFDataTable.firstChild);
		this.fixedHeaderTableObj.insertBefore(this.colGroupObj.cloneNode(true), this.fixedHeaderTableObj.firstChild)
	}
	if (this.fixedColsCount > 0) {
		this.sFDataTable.className += " cls-fixed-cols"
	}
	if (this.fixedColsCount > 0) {
		this.divContainerObj.appendChild(this.divFixedHeaderObj)
	}
	this.divHeaderObj.appendChild(this.divHeaderInnerObj);
	this.divContainerObj.appendChild(this.divHeaderObj);
	if (this.fixedColsCount > 0) {
		this.divFixedDataObj.appendChild(this.divFixedDataInnerObj);
		this.divContainerObj.appendChild(this.divFixedDataObj)
	}
	this.divContainerObj.appendChild(this.divDataObj);
	this.parentDivObj.insertBefore(this.divContainerObj, this.tableObj);
	this.divDataObj.appendChild(this.tableObj);
	var q, s;
	this.sHeaderHeight = this.tableObj.tBodies[0].rows[(this.tableObj.tHead) ? 0 : this.fixedRowsCount].offsetTop;
	s = "margin-top: " + (this.sHeaderHeight * -1) + "px;";
	q = "margin-top: " + this.sHeaderHeight + "px;";
	q += "height: " + (this.tableParentDivHeight - this.sHeaderHeight) + "px;";
	if (this.fixedColsCount > 0) {
		var f = null;
		for (var z = 0, A = b.length; z < A; z++) {
			for (var x = 0, h = b[z].cells.length; x < h; x++) {
				if (b[z].cells[x].getAttribute("first_nonfixed_col") == "true") {
					f = b[z].cells[x];
					break
				}
			}
			if (f != null) {
				break
			}
		}
		this.sFHeaderWidth = -1;
		if (f != null) {
			this.sFHeaderWidth = f.offsetLeft
		}
		if (window.getComputedStyle) {
			c = document.defaultView;
			C = this.tableObj.tBodies[0].rows[0].cells[0];
			if (navigator.taintEnabled) {
				this.sFHeaderWidth += Math.ceil(parseInt(c.getComputedStyle(C, null).getPropertyValue("border-right-width")) / 2)
			} else {
				this.sFHeaderWidth += parseInt(c.getComputedStyle(C, null).getPropertyValue("border-right-width"))
			}
		} else {
			if (isIE) {
				c = this.tableObj.tBodies[0].rows[0].cells[0];
				C = [c.currentStyle.borderRightWidth, c.currentStyle.borderLeftWidth];
				if (/px/i.test(C[0]) && /px/i.test(C[1])) {
					C = [parseInt(C[0]), parseInt(C[1])].sort();
					this.sFHeaderWidth += Math.ceil(parseInt(C[1]) / 2)
				}
			}
		}
		if (window.opera) {
			this.divFixedDataObj.style.height = this.tableParentDivHeight + "px"
		}
		if (this.sFHeaderWidth >= 0) {
			this.divFixedHeaderObj.style.width = this.sFHeaderWidth + "px";
			s += "margin-left: " + (this.sFHeaderWidth * -1) + "px;";
			q += "margin-left: " + this.sFHeaderWidth + "px;";
			q += "width: " + (this.tableParentDivWidth - this.sFHeaderWidth) + "px;"
		}
	} else {
		q += "width: " + this.tableParentDivWidth + "px;"
	}
	this.divDataObj.style.cssText = q;
	this.tableObj.style.cssText = s;
	(function(i) {
		if (i.fixedColsCount > 0) {
			i.divDataObj.onscroll = function() {
				i.divHeaderInnerObj.style.right = i.divDataObj.scrollLeft + "px";
				i.divFixedDataInnerObj.style.top = (i.divDataObj.scrollTop * -1) + "px"
			}
		} else {
			i.divDataObj.onscroll = function() {
				i.divHeaderInnerObj.style.right = i.divDataObj.scrollLeft + "px"
			}
		}
		if (isIE) {
			window.attachEvent("onunload", function() {
				i.divDataObj.onscroll = null;
				i = null
			})
		}
	})(this)
}

function getValidRowidx(g, c) {
	var a = -1;
	var h;
	for (var f = 0, k = g.length; f < k; f++) {
		h = g[f];
		if (a < 0 || h.cells.length > g[a].cells.length) {
			a = f
		}
		var l = 1;
		var b = false;
		for (var e = 0, d = h.cells.length; e < d; e++) {
			if (h.cells[e].rowSpan > l) {
				l = h.cells[e].rowSpan
			}
			if (b) {
				continue
			}
			if (h.cells[e].colSpan > 1) {
				b = true
			}
		}
		if (!b && c == h.cells.length) {
			return h
		}
		f += l - 1
	}
	return g[a]
}

function getParentFixedDataObj(b, a) {
	while (true) {
		if (a == null) {
			return null
		}
		if (a.getAttribute("id") == "WX_CONTENT_" + b) {
			return null
		}
		if (a.className == "cls-fixed-divcontainer") {
			return document.getElementById(b + "_fixeddata")
		}
		a = a.parentNode
	}
}

function shiftTabPanelItemAsyn(a, c, e, f, d) {
	var b = getComponentUrl(a, e, null);
	b = replaceUrlParamValue(b, c + "_selectedIndex", f);
	if (d != null && d != "") {
		b = d(a, c, b);
		if (b == null || b == "") {
			return
		}
	}
	refreshComponent(b)
}

function shiftTabPanelItemSyn(g, f, k, j) {
	var b = getComponentGuidById(g, f);
	var h = document.getElementById(b + "_" + j + "_title");
	var i = getParentElementObj(h, "TABLE");
	var m = i.getAttribute("selectedItemIndex");
	if (m == null || m == "") {
		m = "0"
	}
	if (j == m) {
		return
	}
	var l = document.getElementById(b + "_" + m + "_content");
	l.style.display = "none";
	var a = document.getElementById(b + "_" + j + "_content");
	a.style.display = "";
	var n = document.getElementById(b + "_" + m + "_title");
	var h = document.getElementById(b + "_" + j + "_title");
	var e = n.className;
	n.className = h.className;
	h.className = e;
	var o = h.getAttribute("tabitem_position_type");
	var p = n.getAttribute("tabitem_position_type");
	if (o == "first" || o == "middle" || o == "last") {
		changeImgForTabItemTitle(b, j, m, o, p)
	}
	i.setAttribute("selectedItemIndex", j);
	var d = getComponentUrl(g, k, null);
	d = replaceUrlParamValue(d, f + "_selectedIndex", j);
	var c = document.getElementById(g + "_url_id");
	c.setAttribute("value", d)
}

function changeImgForTabItemTitle(a, g, f, l, m) {
	var j = document;
	var d = j.getElementById(a + "_" + f + "_rightimg");
	var h = j.getElementById(a + "_" + g + "_rightimg");
	var k = d.src;
	var i = k.lastIndexOf("/");
	var e = k.substring(0, i) + "/";
	var c = parseInt(g, 10);
	var b = parseInt(f, 10);
	if (c - b == 1) {
		d.src = e + "title2_deselected_selected.gif";
		if (m != "first") {
			j.getElementById(a + "_" + (b - 1) + "_rightimg").src = e + "title2_deselected_deselected.gif"
		}
		if (l == "last") {
			h.src = e + "title2_selected.gif"
		} else {
			h.src = e + "title2_selected_deselected.gif"
		}
	} else {
		if (b - c == 1) {
			h.src = e + "title2_selected_deselected.gif";
			if (l != "first") {
				j.getElementById(a + "_" + (c - 1) + "_rightimg").src = e + "title2_deselected_selected.gif"
			}
			if (m == "last") {
				d.src = e + "title2_deselected.gif"
			} else {
				d.src = e + "title2_deselected_deselected.gif"
			}
		} else {
			if (m == "first") {
				d.src = e + "title2_deselected_deselected.gif"
			} else {
				if (m == "middle") {
					d.src = e + "title2_deselected_deselected.gif";
					j.getElementById(a + "_" + (b - 1) + "_rightimg").src = e + "title2_deselected_deselected.gif"
				} else {
					if (m == "last") {
						d.src = e + "title2_deselected.gif";
						j.getElementById(a + "_" + (b - 1) + "_rightimg").src = e + "title2_deselected_deselected.gif"
					}
				}
			}
			if (l == "first") {
				h.src = e + "title2_selected_deselected.gif"
			} else {
				if (l == "middle") {
					h.src = e + "title2_selected_deselected.gif";
					j.getElementById(a + "_" + (c - 1) + "_rightimg").src = e + "title2_deselected_selected.gif"
				} else {
					if (l == "last") {
						h.src = e + "title2_selected.gif";
						j.getElementById(a + "_" + (c - 1) + "_rightimg").src = e + "title2_deselected_selected.gif"
					}
				}
			}
		}
	}
}

function adjustTabItemTitleImgHeight(d) {
	if (d == null || d.tabpanelguid == null || d.tabpanelguid == "" || d.tabitemcount == null) {
		return
	}
	var c = document;
	var b;
	for (var a = 0; a < d.tabitemcount; a++) {
		b = c.getElementById(d.tabpanelguid + "_" + a + "_rightimg");
		if (b != null) {
			b.style.height = b.parentNode.clientHeight + "px"
		}
	}
}
var LODOP_OBJ;

function printComponentsData(d, k, l, f) {
	if (l == null || l == "") {
		return
	}
	var h = parseStringToArray(l, ";", false);
	if (h == null || h.length == 0) {
		return
	}
	var c = "";
	if (h.length == 1) {
		var o = getComponentGuidById(d, h[0]);
		var n = getReportMetadataObj(o);
		if (n == null) {
			c = getComponentUrl(d, null, null)
		} else {
			c = getComponentUrl(d, n.refreshComponentGuid, n.slave_reportid)
		}
	} else {
		c = getComponentUrl(d, null, null);
		var m = null;
		for (var g = 0, j = h.length; g < j; g++) {
			m = getReportMetadataObj(getComponentGuidById(d, h[g]));
			if (m == null) {
				continue
			}
			if (m.slave_reportid == h[g]) {
				var b = getComponentUrl(d, null, h[g]);
				c = mergeUrlParams(c, b, false);
				c = replaceUrlParamValue(c, "SLAVE_REPORTID", null)
			}
		}
	}
	c = replaceUrlParamValue(c, "COMPONENTIDS", k);
	c = replaceUrlParamValue(c, "INCLUDE_APPLICATIONIDS", l);
	c = replaceUrlParamValue(c, "DISPLAY_TYPE", "6");
	c = replaceUrlParamValue(c, "WX_ISAJAXLOAD", "true");
	var e = c.substring(c.indexOf("?") + 1);
	c = c.substring(0, c.indexOf("?"));
	var a = new Object();
	a.pageid = d;
	a.printtype = f;
	XMLHttpREPORT.sendReq("POST", c, e, printCallBack, onPrintErrorMethod, a)
}

function printCallBack(k, a) {
	var h = k.responseText;
	var b = a.pageid;
	var d = a.printtype;
	var f = h.indexOf("<RESULTS_INFO-" + b + ">");
	var e = h.indexOf("</RESULTS_INFO-" + b + ">");
	var i = null;
	if (f >= 0 && e >= 0 && e > f) {
		i = h.substring(f + ("<RESULTS_INFO-" + b + ">").length, e);
		h = h.substring(0, f) + h.substring(e + ("</RESULTS_INFO-" + b + ">").length)
	}
	var g = getObjectByJsonString(i);
	var j = g.onloadMethods;
	if (j && j != "") {
		var c = "";
		f = h.indexOf("<print-jobname-" + b + ">");
		e = h.indexOf("</print-jobname-" + b + ">");
		if (f >= 0 && e > f) {
			c = h.substring(f + ("<print-jobname-" + b + ">").length, e);
			h = h.substring(0, f) + h.substring(e + ("</print-jobname-" + b + ">").length)
		}
		j[0].methodname(c, h, d)
	}
}

function onPrintErrorMethod(a) {
	wx_error("打印失败")
}

function getAdvancedPrintRealValue(b, e) {
	if (b == null || b == "" || e == null || e == "") {
		return ""
	}
	var d = "<" + e + ">";
	var c = "</" + e + ">";
	var a = b.indexOf(d);
	if (a < 0) {
		return ""
	}
	b = b.substring(a + d.length);
	a = b.indexOf(c);
	if (a < 0) {
		return b
	}
	return b.substring(0, a)
}

function getAdvancedPrintRealValueForPage(b, f, e) {
	if (b == null || b == "" || f == null || f == "") {
		return ""
	}
	var d = "<" + f + ">";
	var c = "</" + f + ">";
	var a = b.indexOf(d);
	if (a < 0) {
		return ""
	}
	b = b.substring(a + d.length);
	a = b.indexOf(c);
	if (a >= 0) {
		b = b.substring(0, a)
	}
	if (e == null || e == "") {
		return b
	}
	d = "<" + e + ">";
	c = "</" + e + ">";
	a = b.indexOf(d);
	if (a < 0) {
		return ""
	}
	b = b.substring(a + d.length);
	a = b.indexOf(c);
	if (a < 0) {
		return b
	}
	return b.substring(0, a)
}

function getPrintPageCount(b, e) {
	if (b == null || b == "" || e == null || e == "") {
		return 0
	}
	var d = "<" + e + "_pagecount>";
	var c = "</" + e + "_pagecount>";
	var a = b.indexOf(d);
	if (a < 0) {
		return 0
	}
	b = b.substring(a + d.length);
	a = b.indexOf(c);
	if (a < 0) {
		return 0
	}
	return parseInt(b.substring(0, a), 10)
}

function wx_sendRedirect(a) {
	if (a == null || a.url == null || a.url == "") {
		return
	}
	window.location.href = a.url
}
var WX_ISSYSTEM_LOADED = true;