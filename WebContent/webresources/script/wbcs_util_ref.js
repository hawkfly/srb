var SAVING_ROWDATA_SEPERATOR = "_ROTAREPES_ATADWOR_GNIVAS_";
var SAVING_COLDATA_SEPERATOR = "_ROTAREPES_ATADLOC_GNIVAS_";
var SAVING_NAMEVALUE_SEPERATOR = "_ROTAREPES_EULAVEMAN_GNIVAS_";
var COL_NONDISPLAY_PERMISSION_PREX = "[NOISSIMREP_YALPSIDNON]";
var ReportFamily = {
	LIST: "list",
	DETAIL: "detail",
	EDITABLELIST: "editablelist",
	EDITABLELIST2: "editablelist2",
	LISTFORM: "listform",
	EDITABLEDETAIL2: "editabledetail2",
	EDITABLEDETAIL: "editabledetail",
	FORM: "form"
};
var ART_DIALOG_OBJ = null;
var EventTools = new Object;
EventTools.addEventHandler = function(b, c, a) {
	if (b.addEventListener) {
		b.addEventListener(c, a, false)
	} else {
		if (b.attachEvent) {
			b.attachEvent("on" + c, a)
		} else {
			b["on" + c] = a
		}
	}
};
EventTools.removeEventHandler = function(b, c, a) {
	if (b.removeEventListener) {
		b.removeEventListener(c, a, false)
	} else {
		if (b.detachEvent) {
			b.detachEvent("on" + c, a)
		} else {
			b["on" + c] = null
		}
	}
};
EventTools.formatEvent = function(b) {
	if (typeof b.charCode == "undefined") {
		b.charCode = (b.type == "keypress") ? b.keyCode : 0;
		b.isChar = (b.charCode > 0)
	}
	if (b.srcElement && !b.target) {
		b.eventPhase = 2;
		var a = getDocumentScroll();
		b.pageX = b.clientX + a.scrollLeft - document.body.clientLeft;
		b.pageY = b.clientY + a.scrollTop - document.body.clientTop;
		if (!b.preventDefault) {
			b.preventDefault = function() {
				this.returnValue = false
			}
		}
		if (b.type == "mouseout") {
			b.relatedTarget = b.toElement
		} else {
			if (b.type == "mouseover") {
				b.relatedTarget = b.fromElement
			}
		}
		if (!b.stopPropagation) {
			b.stopPropagation = function() {
				this.cancelBubble = true
			}
		}
		if (typeof b.button == " undefined ") {
			b.button = b.which
		}
		b.target = b.srcElement;
		b.time = (new Date).getTime()
	}
	return b
};
EventTools.getEvent = function() {
	if (window.event) {
		return this.formatEvent(window.event)
	} else {
		return EventTools.getEvent.caller.arguments[0]
	}
};

function checkExp(b, a) {
	return a.match(b)
}
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "")
};

function isWXNumber(a) {
	if (a == null || a == "") {
		return false
	}
	if (!checkExp(/^[+-]?\d+(\.\d+)?$/g, a)) {
		return false
	}
	return true
}
String.prototype.getBytesLength = function() {
	return this.replace(/[^\x00-\xff]/gi, "--").length
};
var XMLHttpREPORT = {
	_objPool: [],
	_getInstance: function() {
		for (var a = 0; a < this._objPool.length; a++) {
			if (this._objPool[a].readyState == 0 || this._objPool[a].readyState == 4) {
				return this._objPool[a]
			}
		}
		this._objPool[this._objPool.length] = this._createObj();
		return this._objPool[this._objPool.length - 1]
	},
	_createObj: function() {
		if (window.XMLHttpRequest) {
			var a = new XMLHttpRequest()
		} else {
			var a = new ActiveXObject("Microsoft.XMLHTTP")
		}
		if (a.readyState == null) {
			a.readyState = 0;
			a.addEventListener("load", function() {
				a.readyState = 4;
				if (typeof a.onreadystatechange == "function") {
					a.onreadystatechange()
				}
			}, false)
		}
		return a
	},
	sendReq: function(a, b, c, d, f, g) {
		var h = this._createObj();
		with(h) {
			try {
				if (b.indexOf("?") > 0) {
					b += "&randnum=" + Math.random()
				} else {
					b += "?randnum=" + Math.random()
				}
				open(a, b, true);
				setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
				send(c);
				onreadystatechange = function() {
					if (h.readyState == 4) {
						if (h.status == 200 || h.status == 304) {
							if (d != null) {
								d(h, g)
							}
						} else {
							if (f != null) {
								f(h, g)
							}
						}
					}
				}
			} catch (e) {
				alert(e)
			} finally {}
		}
	}
};

function sendAsynRequestToServer(e, b, d, a) {
	if (e == null || e == "") {
		return
	}
	var f = e.split("?");
	if (f == null || f.length <= 1) {
		f[1] = ""
	} else {
		if (f.length >= 2) {
			if (f.length > 2) {
				for (var c = 2; c < f.length; c = c + 1) {
					f[1] = f[1] + "?" + f[c]
				}
			}
		}
	}
	XMLHttpREPORT.sendReq("POST", f[0], f[1], b, d, a)
}
function getDocumentSize() {
	if (document.compatMode == "BackCompat" && document.body) {
		return {
			width: document.body.clientWidth,
			height: document.body.clientHeight
		}
	} else {
		return {
			width: document.documentElement.clientWidth,
			height: document.documentElement.clientHeight
		}
	}
}
function getDocumentScroll() {
	var c = 0,
		b = 0,
		a = 0,
		d = 0;
	if (document.compatMode == "BackCompat" && document.body || isChrome) {
		c = document.body.scrollTop;
		b = document.body.scrollLeft;
		a = document.body.scrollWidth;
		d = document.body.scrollHeight
	} else {
		if (document.documentElement) {
			c = document.documentElement.scrollTop;
			b = document.documentElement.scrollLeft;
			a = document.documentElement.scrollWidth;
			d = document.documentElement.scrollHeight
		}
	}
	return {
		scrollTop: c,
		scrollLeft: b,
		scrollWidth: a,
		scrollHeight: d
	}
}
function getAllParentScrollOffsetValue(a) {
	var c = 0;
	var b = 0;
	a = a.offsetParent;
	while (a != null) {
		if (a.scrollLeft) {
			c += a.scrollLeft
		}
		if (a.scrollTop) {
			b += a.scrollTop
		}
		a = a.parentNode
	}
	return {
		scrollWidth: c,
		scrollHeight: b
	}
}
function getElementAbsolutePosition(d) {
	if (!d) {
		return null
	}
	var c = d;
	var b = c.offsetHeight;
	var g = c.offsetWidth;
	var f = 0;
	var e = 0;
	while (c != null) {
		f += c.offsetLeft;
		if (c.className == "cls-fixed-headerInner" && isIE) {
			if (c.style.right != null && c.style.right != "") {
				f = f - parseInt(c.style.right)
			}
		}
		e += c.offsetTop;
		c = c.offsetParent
	}
	var a = getAllParentScrollOffsetValue(d);
	if (a) {
		f = f - a.scrollWidth;
		e = e - a.scrollHeight
	}
	a = getDocumentScroll();
	if (a) {
		f = f + a.scrollLeft;
		e = e + a.scrollTop
	}
	return {
		top: e,
		left: f,
		width: g,
		height: b
	}
}
function removeSubStr(g, f, e, d) {
	if (g == null || g == "" || f == null || f == "" || e == null || e == "") {
		return g
	}
	if (d == null) {
		d = ""
	}
	var b = g.indexOf(f);
	while (b >= 0) {
		var c = g.substring(0, b);
		g = g.substring(b + f.length);
		b = g.indexOf(e);
		if (b < 0) {
			g = c;
			break
		}
		var a = g.substring(b + e.length);
		if (a == "") {
			g = c;
			break
		}
		g = c + d + a;
		b = g.indexOf(f)
	}
	return g
}
function getSubStrValue(e, d, c) {
	if (e == null || e == "" || d == null || d == "" || c == null || c == "") {
		return ""
	}
	var b = e.indexOf(d);
	if (b < 0) {
		return ""
	}
	var a = e.substring(b + d.length);
	b = a.indexOf(c);
	if (b >= 0) {
		a = a.substring(0, b)
	}
	return a
}
function isPositiveInteger(b) {
	if (b == null || b == "") {
		return false
	}
	var a = "^[1-9][0-9]*$";
	return b.match(a)
}
function paramdecode(a) {
	if (a == null || a == "") {
		return a
	}
	a = a.replace(/wx_QUOTE_wx/g, "'");
	a = a.replace(/wx_DBLQUOTE_wx/g, '"');
	a = a.replace(/wx_DOLLAR_wx/g, "$");
	return a
}
function jsonParamEncode(a, c) {
	if (a == null || a == "") {
		return a
	}
	var b = null;
	if (!c) {
		b = new RegExp("'", "g");
		a = a.replace(b, "wx_json_QUOTE_wx");
		b = new RegExp('"', "g");
		a = a.replace(b, "wx_json_DBLQUOTE_wx")
	}
	b = new RegExp("\n", "g");
	a = a.replace(b, "wx_json_NEWLINE_wx");
	return a
}
function jsonParamDecode(a) {
	if (a == null || a == "") {
		return a
	}
	var b = new RegExp("wx_json_QUOTE_wx", "g");
	a = a.replace(b, "'");
	b = new RegExp("wx_json_DBLQUOTE_wx", "g");
	a = a.replace(b, '"');
	b = new RegExp("wx_json_NEWLINE_wx", "g");
	a = a.replace(b, "\n");
	return a
}
function isEmptyMap(a) {
	if (a == null) {
		return true
	}
	for (var b in a) {
		return false
	}
	return true
}
function getElementBgColor(b) {
	var a = b.style.backgroundColor;
	if (a != null && a != "") {
		return a
	}
	if (document.defaultView && document.defaultView.getComputedStyle) {
		var c = document.defaultView.getComputedStyle(b, null);
		if (c) {
			a = c.backgroundColor
		}
	} else {
		if (b.currentStyle) {
			a = b.currentStyle.backgroundColor
		}
	}
	if (a != null && a != "") {
		return a
	}
	return b.bgColor
}
function getParentElementObj(c, b) {
	if (!c || !b) {
		return null
	}
	var a = c.parentNode;
	if (!a) {
		return null
	}
	if (a.tagName == b) {
		return a
	} else {
		return getParentElementObj(a, b)
	}
}
function isElementOrChildElement(b, c) {
	while (b != null) {
		try {
			if (b.getAttribute("id") == c) {
				return true
			}
		} catch (a) {
			break
		}
		b = b.parentNode
	}
	return false
}
function getWXInputBoxChildNode(e) {
	var c = e.childNodes;
	if (!c || c.length == 0) {
		return null
	}
	for (var b = 0, a = c.length; b < a; b++) {
		if (c.item(b).nodeType != 1) {
			continue
		}
		if (isWXInputBoxNode(c.item(b))) {
			return c.item(b)
		}
		var d = getWXInputBoxChildNode(c.item(b));
		if (d != null) {
			return d
		}
	}
	return null
}
function isWXInputBoxNode(b) {
	if (b == null || b.nodeType != 1) {
		return false
	}
	var c = b.getAttribute("id");
	var a = b.getAttribute("typename");
	if (c != null && c.indexOf("_guid_") > 0 && a != null && a != "") {
		return true
	}
	return false
}
function getInputBoxChildNode(d) {
	var c = d.childNodes;
	if (!c || c.length == 0) {
		return null
	}
	for (var b = 0, a = c.length; b < a; b++) {
		if (c.item(b).nodeType != 1) {
			continue
		}
		return c.item(b)
	}
	return null
}
function getComponentMetadataObj(c) {
	if (c == null || c == "") {
		return null
	}
	var a = document.getElementById(c + "_metadata");
	if (a == null) {
		return null
	}
	var b = new Object();
	b.pageid = a.getAttribute("pageid");
	b.componentid = a.getAttribute("componentid");
	b.componentguid = c;
	b.refreshComponentGuid = a.getAttribute("refreshComponentGuid");
	b.componentTypeName = a.getAttribute("componentTypeName");
	b.metaDataSpanObj = a;
	return b
}
function getReportMetadataObj(a) {
	if (a == null || a == "") {
		return null
	}
	var c = getComponentMetadataObj(a);
	if (c == null) {
		return null
	}
	c.reportid = c.componentid;
	c.reportguid = a;
	c.reportfamily = c.metaDataSpanObj.getAttribute("reportfamily");
	var b = c.metaDataSpanObj.getAttribute("isSlaveReport");
	if (b == "true") {
		c.slave_reportid = c.reportid
	} else {
		c.slave_reportid = null
	}
	return c
}
function getComponentUrl(a, d, f) {
	var b = document.getElementById(a + "_url_id");
	var e = b.getAttribute("value");
	if (f != null && f != "") {
		e = document.getElementById(getComponentGuidById(a, f) + "_url_id").getAttribute("value")
	}
	if (!e || e == "") {
		wx_warn("获取组件URL失败，没有取到其原始url");
		return null
	}
	e = paramdecode(e);
	var c = b.getAttribute("ancestorPageUrls");
	if (c != null && c != "") {
		e = replaceUrlParamValue(e, "ancestorPageUrls", c)
	}
	if (d != null && d != "") {
		e = replaceUrlParamValue(e, "refreshComponentGuid", d)
	}
	if (f != null && f != "") {
		e = replaceUrlParamValue(e, "SLAVE_REPORTID", f)
	}
	return e
}
function resetComponentUrl(c, j, b, g) {
	if (b == null || b == "") {
		return b
	}
	var l = document;
	var m = getComponentGuidById(c, j);
	var d = getComponentMetadataObj(m);
	if (d == null) {
		return b
	}
	if (d.componentTypeName == "application.report") {
		var k = getReportMetadataObj(m);
		if (g == null || g == "" || g.indexOf("navigate") >= 0) {
			b = removeReportNavigateInfosFromUrl(b, k, null)
		}
		if (g == null || g == "" || g.indexOf("searchbox") >= 0) {
			b = removeReportConditionBoxValuesFromUrl(b, k)
		}
	} else {
		if (d.componentTypeName == "container.tabspanel") {
			b = replaceUrlParamValue(b, j + "_selectedIndex", null)
		}
	}
	var e = d.metaDataSpanObj.getAttribute("childComponentIds");
	if (e != null && e != "") {
		var a = e.split(";");
		for (var f = 0, h = a.length; f < h; f++) {
			if (a[f] == null || a[f] == "") {
				continue
			}
			b = resetComponentUrl(c, a[f], b)
		}
	}
	return b
}
function getInputboxParentElementObj(c, d) {
	if (c == null || d == null || d == "") {
		return null
	}
	var a = c.parentNode;
	if (a == null) {
		return null
	}
	if (a.tagName != d) {
		return getInputboxParentElementObj(a, d)
	}
	if (d == "FONT") {
		var b = a.getAttribute("id");
		if (b == null || b.indexOf("font_") < 0 || b.indexOf("_guid_") < 0) {
			return getInputboxParentElementObj(a, d)
		}
	} else {
		if (d == "TD") {
			var e = a.getAttribute("id");
			if (e == null || e.indexOf("_td") < 0 || e.indexOf("_guid_") < 0) {
				return getInputboxParentElementObj(a, d)
			}
		}
	}
	return a
}
function getColConditionValueByParentElementObj(c) {
	if (c == null) {
		return null
	}
	var f = null;
	if (c.tagName == "FONT") {
		var e = c.getAttribute("id");
		if (e == null || e.indexOf("font_") < 0) {
			return null
		}
		f = e.substring("font_".length);
		if (f.lastIndexOf("_conditions") == f.length - "_conditions".length) {
			var a = getWXInputBoxChildNode(c);
			if (a == null) {
				return c.getAttribute("value")
			}
			return getInputBoxValue(a.getAttribute("id"), a.getAttribute("typename"))
		}
	} else {
		var h = c.getAttribute("id");
		if (h == null || h == "") {
			return null
		}
		f = getReportGuidByInputboxId(h)
	}
	if (f == null || f == "") {
		return null
	}
	var d = getReportMetadataObj(f);
	var b = getUpdateColSrcObj(c, d.reportguid, d.reportfamily, null);
	if (b != null) {
		var a = getWXInputBoxChildNode(b);
		if (a == null) {
			return c.getAttribute("value")
		}
		return getInputBoxValue(a.getAttribute("id"), a.getAttribute("typename"))
	} else {
		var a = getWXInputBoxChildNode(c);
		if (a == null) {
			return c.getAttribute("value")
		}
		var g = getUpdateColDestObj(c, d.reportguid, d.reportfamily, null);
		if (g != null) {
			return getInputBoxLabel(a.getAttribute("id"), a.getAttribute("typename"))
		} else {
			return getInputBoxValue(a.getAttribute("id"), a.getAttribute("typename"))
		}
	}
}
function getUpdateColDestObj(h, b, d, j) {
	var a = h.getAttribute("updatecolDest");
	if (a == null || a == "") {
		return j
	}
	var c = null;
	if (d == ReportFamily.EDITABLELIST2 || d == ReportFamily.LISTFORM) {
		var g = h.parentNode;
		c = g.getElementsByTagName("TD")
	} else {
		if (d == ReportFamily.EDITABLEDETAIL2) {
			var f = getParentElementObj(h, "TABLE");
			c = f.getElementsByTagName("TD")
		} else {
			if (d == ReportFamily.EDITABLEDETAIL || d == ReportFamily.FORM) {
				c = document.getElementsByName("font_" + b)
			}
		}
	}
	if (c == null) {
		return j
	}
	for (var e = 0; e < c.length; e++) {
		if (c[e].getAttribute("value_name") == a) {
			return c[e]
		}
	}
	return j
}
function getUpdateColSrcObj(g, a, c, j) {
	var h = g.getAttribute("updatecolSrc");
	if (h == null || h == "") {
		return j
	}
	var b = null;
	if (c == ReportFamily.EDITABLELIST2 || c == ReportFamily.LISTFORM) {
		var f = g.parentNode;
		b = f.getElementsByTagName("TD")
	} else {
		if (c == ReportFamily.EDITABLEDETAIL2) {
			var e = getParentElementObj(g, "TABLE");
			b = e.getElementsByTagName("TD")
		} else {
			if (c == ReportFamily.EDITABLEDETAIL || c == ReportFamily.FORM) {
				b = document.getElementsByName("font_" + a)
			}
		}
	}
	if (b == null) {
		return j
	}
	for (var d = 0; d < b.length; d++) {
		if (b[d].getAttribute("value_name") == h) {
			return b[d]
		}
	}
	return j
}
function getObjectByJsonString(a) {
	if (a == null || a == "") {
		return null
	}
	a = paramdecode(a);
	a = jsonParamEncode(a, true);
	var b = eval("(" + a + ")");
	return b
}
function getComponentGuidById(a, b) {
	if (b == null || b == "" || b == a) {
		return a
	}
	return a + WX_GUID_SEPERATOR + b
}
function getComponentIdByGuid(b) {
	if (b == null || b == "") {
		return null
	}
	var a = b.lastIndexOf(WX_GUID_SEPERATOR);
	if (a > 0) {
		return b.substring(a + WX_GUID_SEPERATOR.length)
	}
	return b
}
function getPageIdByComponentGuid(b) {
	if (b == null || b == "") {
		return null
	}
	var a = b.lastIndexOf(WX_GUID_SEPERATOR);
	if (a > 0) {
		return b.substring(0, a)
	}
	return b
}
function getReportGuidByInputboxId(d) {
	var b = d.indexOf(WX_GUID_SEPERATOR);
	var a = d.substring(0, b);
	var c = d.substring(b + WX_GUID_SEPERATOR.length);
	b = c.indexOf("_");
	c = c.substring(0, b);
	return reportguid = a + WX_GUID_SEPERATOR + c
}
function postlinkurl(a, f) {
	if (a == null || a == "") {
		return
	}
	var i = document;
	var g = i.createElement("Form");
	g.method = "post";
	a = paramdecode(a);
	var b = splitUrlAndParams(a, true);
	var c = b[0];
	var j = b[1];
	if (j != null) {
		var h;
		for (var d in j) {
			if (d == null || d == "") {
				continue
			}
			h = j[d];
			if (h == null) {
				h = ""
			}
			var e = i.createElement("input");
			e.setAttribute("name", d);
			e.setAttribute("value", h);
			e.setAttribute("type", "hidden");
			g.appendChild(e)
		}
	}
	g.action = c;
	i.body.appendChild(g);
	g.submit();
	i.body.removeChild(g)
}
function replaceUrlParamValue(c, a, b) {
	if (c == null || c == "") {
		return c
	}
	if (a == null || a == "") {
		return c
	}
	c = removeSubStr(c, "?" + a + "=", "&", "?");
	c = removeSubStr(c, "&" + a + "=", "&", "&");
	if (b != null) {
		if (c.indexOf("?") > 0) {
			c = c + "&"
		} else {
			c = c + "?"
		}
		c = c + a + "=" + b
	}
	return c
}
function getParamValueFromUrl(c, a) {
	if (c == null || c == "") {
		return ""
	}
	if (a == null || a == "") {
		return ""
	}
	var b = getSubStrValue(c, "?" + a + "=", "&");
	if (b == "") {
		b = getSubStrValue(c, "&" + a + "=", "&")
	}
	return b
}
function splitUrlAndParams(a, k) {
	if (a == null || a == "") {
		return null
	}
	var l = new Array();
	var j = a.indexOf("?");
	if (j == 0) {
		return null
	}
	if (j < 0) {
		l[0] = a;
		l[1] = null;
		return l
	}
	var m = a.substring(j + 1);
	a = a.substring(0, j);
	if (m == "") {
		l[0] = a;
		l[1] = null;
		return l
	}
	var e = new Object();
	var c = m.split("&");
	var b, g, h;
	for (var d = 0, f = c.length; d < f; d = d + 1) {
		b = c[d];
		if (b == null || b == "") {
			continue
		}
		j = b.indexOf("=");
		if (j == 0) {
			continue
		}
		if (j > 0) {
			g = b.substring(0, j);
			h = b.substring(j + 1);
			if (k) {
				h = decodeURIComponent(h)
			}
		} else {
			g = b;
			h = ""
		}
		e[g] = h
	}
	l[0] = a;
	l[1] = e;
	return l
}
function wx_ltrim(c, b) {
	if (c == null || c == "") {
		return c
	}
	if (b == null || b == "") {
		b = " "
	}
	var a = c.indexOf(b);
	while (a == 0) {
		c = c.substring(b.length);
		a = c.indexOf(b)
	}
	return c
}
function wx_rtrim(c, b) {
	if (c == null || c == "") {
		return c
	}
	if (b == null || b == "") {
		b = " "
	}
	var a = c.lastIndexOf(b);
	while (a >= 0 && a == c.length - b.length) {
		c = c.substring(0, c.length - b.length);
		a = c.lastIndexOf(b)
	}
	return c
}
function wx_trim(b, a) {
	b = wx_ltrim(b, a);
	b = wx_rtrim(b, a);
	return b
}
function isArray(a) {
	return Object.prototype.toString.call(a) == "[object Array]"
}
function convertToArray(b) {
	if (b == null) {
		return null
	}
	if (isArray(b)) {
		return b
	}
	var a = new Array();
	a[a.length] = b;
	return a
}
function splitTextValues(g, e, a) {
	var d = g.search(e);
	if (d < 0) {
		return null
	}
	var b = g.substring(d, d + a);
	var f = "";
	if (d > 0) {
		f = g.substring(0, d)
	}
	var c = g.substring(d + a);
	return {
		start: f,
		mid: b,
		end: c
	}
}
function parseStringToArray(f, c, a) {
	if (f == null || f == "" || c == null) {
		return null
	}
	if (c == "") {
		return f
	}
	var e = new Array();
	var d = f.split(c);
	for (var b = 0; b < d.length; b++) {
		if (d[b] == null || d[b] == "") {
			continue
		}
		if (a === false && wx_trim(d[b]) == "") {
			continue
		}
		e[e.length] = d[b]
	}
	return e
}
var WX_UTIL_LOADED = true;