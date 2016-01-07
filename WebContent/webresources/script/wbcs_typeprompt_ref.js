var TYPE_PROMPT_theTextBox;
var TYPE_PROMPT_objLastActive;
var TYPE_PROMPT_selectSpanTitleStart = "<span style='width:100%;display:block;' class='spanOutputTitleElement'>";
var TYPE_PROMPT_selectSpanStart = "<span style='width:100%;display:block;' class='spanOutputNormalElement' onmouseover='setTypePromptHighColor(this)' ";
var TYPE_PROMPT_selectSpanEnd = "</span>";

function initializeTypePromptProperties(a, b) {
	if (a.typePromptObj == null) {
		var c = {
			elem: a,
			paramsObj: null,
			currentRecordCount: 0,
			spanOutputId: "",
			timer: false,
			resultItemsXmlRoot: null,
			resultItemsCount: 0,
			strLastValue: "",
			bMadeRequest: false,
			currentValueSelected: -1,
			bNoResults: false
		};
		if (a.id) {
			c.spanOutputId = "spanOutput_" + a.id
		} else {
			c.spanOutputId = "spanOutput_" + a.name
		}
		b = paramdecode(b);
		c.paramsObj = eval("(" + b + ")");
		var d = document.getElementById(c.spanOutputId);
		if (d == null) {
			var e = document.createElement("span");
			e.id = c.spanOutputId;
			e.className = "spanOutputTextTypePrompt";
			document.body.appendChild(e)
		}
		if (c.paramsObj.spanOutputWidth == null || c.paramsObj.spanOutputWidth <= 0) {
			c.paramsObj.spanOutputWidth = a.offsetWidth
		}
		if (c.paramsObj.resultCount == null || c.paramsObj.resultCount <= 0) {
			c.paramsObj.resultCount = 15
		}
		a.onkeyup = doKeyUpEvent;
		a.onkeydown = doKeyDownEvent;
		a.typePromptObj = c
	}
	if (a.typePromptObj.paramsObj.isSelectBox === true) {
		getTypeAheadDataOnFocus(a)
	}
}
function getTypeAheadDataOnFocus(b) {
	TYPE_PROMPT_theTextBox = b;
	TYPE_PROMPT_objLastActive = TYPE_PROMPT_theTextBox;
	TYPE_PROMPT_theTextBox.typePromptObj.bMadeRequest = true;
	var a = TYPE_PROMPT_theTextBox.value;
	if (a == TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.conditionlabel) {
		a = ""
	}
	getTypeAheadData(a);
	TYPE_PROMPT_theTextBox.typePromptObj.strLastValue = a
}
function doKeyDownEvent(c) {
	var b;
	var a;
	if (window.event) {
		b = window.event;
		a = window.event.keyCode
	} else {
		b = c;
		a = c.which
	}
	if (TYPE_PROMPT_objLastActive == TYPE_PROMPT_theTextBox) {
		if (a == 38) {
			moveTypePromptHighlight(-1)
		} else {
			if (a == 40) {
				moveTypePromptHighlight(1)
			} else {
				if (a == 27) {
					TYPE_PROMPT_theTextBox.typePromptObj.currentValueSelected = -1;
					hideTypePromptTheBox(TYPE_PROMPT_theTextBox);
					TYPE_PROMPT_theTextBox.value = TYPE_PROMPT_theTextBox.typePromptObj.strLastValue
				} else {
					if (a == 13) {
						grabTypePromptHighlighted();
						TYPE_PROMPT_theTextBox.blur();
						b.returnValue = false;
						if (b.preventDefault) {
							b.preventDefault()
						}
					}
				}
			}
		}
	}
}
function doKeyUpEvent(b) {
	var a = -1;
	if (window.event) {
		a = window.event.keyCode;
		TYPE_PROMPT_theTextBox = window.event.srcElement
	} else {
		a = b.which;
		TYPE_PROMPT_theTextBox = b.target
	}
	if (TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.timeoutSecond > 0) {
		if (TYPE_PROMPT_theTextBox.typePromptObj.timer) {
			eraseTypePromptTimeout()
		}
		startTypePromptTimeout()
	}
	if (TYPE_PROMPT_theTextBox.value.length == 0 && TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.isSelectBox !== true) {
		TYPE_PROMPT_theTextBox.typePromptObj.resultItemsXmlRoot = null;
		hideTypePromptTheBox(TYPE_PROMPT_theTextBox);
		TYPE_PROMPT_theTextBox.typePromptObj.strLastValue = "";
		return false
	}
	if (TYPE_PROMPT_objLastActive == TYPE_PROMPT_theTextBox) {
		if ((a < 32 && a != 8) || (a >= 33 && a < 46) || (a >= 112 && a <= 123)) {
			return false
		}
	}
	if (TYPE_PROMPT_objLastActive != TYPE_PROMPT_theTextBox || TYPE_PROMPT_theTextBox.value.indexOf(TYPE_PROMPT_theTextBox.typePromptObj.strLastValue) != 0 || ((TYPE_PROMPT_theTextBox.typePromptObj.resultItemsCount == 0 || TYPE_PROMPT_theTextBox.typePromptObj.resultItemsCount == TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.resultCount) && !TYPE_PROMPT_theTextBox.typePromptObj.bNoResults) || TYPE_PROMPT_theTextBox.value.length < TYPE_PROMPT_theTextBox.typePromptObj.strLastValue.length || TYPE_PROMPT_theTextBox.typePromptObj.strLastValue == "") {
		TYPE_PROMPT_objLastActive = TYPE_PROMPT_theTextBox;
		TYPE_PROMPT_theTextBox.typePromptObj.bMadeRequest = true;
		getTypeAheadData(TYPE_PROMPT_theTextBox.value)
	} else {
		if (!TYPE_PROMPT_theTextBox.typePromptObj.bMadeRequest) {
			buildTypeAheadBox(TYPE_PROMPT_theTextBox.value)
		}
	}
	TYPE_PROMPT_theTextBox.typePromptObj.strLastValue = TYPE_PROMPT_theTextBox.value
}
function getTypeAheadData(h) {
	if ((h == null || h == "") && TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.isSelectBox !== true) {
		return
	}
	if (h == null) {
		h = ""
	}
	var a = TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.pageid;
	var d = TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.reportid;
	var f = TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.inputboxid;
	var c = getComponentGuidById(a, d);
	var b = getReportMetadataObj(c);
	var e = getComponentUrl(a, b.refreshComponentGuid, b.slave_reportid);
	e = replaceUrlParamValue(e, "REPORTID", d);
	e = replaceUrlParamValue(e, "INPUTBOXID", f);
	e = replaceUrlParamValue(e, "ACTIONTYPE", "GetTypePromptDataList");
	e = replaceUrlParamValue(e, "TYPE_PROMPT_TXTVALUE", h);
	var g = e.substring(e.indexOf("?") + 1);
	e = e.substring(0, e.indexOf("?"));
	XMLHttpREPORT.sendReq("POST", e, g, buildChoices, onErrorMethodTypeAhead, null)
}
function buildChoices(c, b) {
	var a = c.responseXML;
	TYPE_PROMPT_theTextBox.typePromptObj.resultItemsXmlRoot = a.getElementsByTagName("items")[0];
	if (TYPE_PROMPT_theTextBox.typePromptObj.resultItemsXmlRoot != null) {
		TYPE_PROMPT_theTextBox.typePromptObj.resultItemsCount = TYPE_PROMPT_theTextBox.typePromptObj.resultItemsXmlRoot.childNodes.length
	} else {
		TYPE_PROMPT_theTextBox.typePromptObj.resultItemsCount = 0
	}
	TYPE_PROMPT_theTextBox.typePromptObj.bMadeRequest = false;
	buildTypeAheadBox(TYPE_PROMPT_theTextBox.typePromptObj.strLastValue)
}
function onErrorMethodTypeAhead(a) {
	TYPE_PROMPT_theTextBox.typePromptObj.bMadeRequest = false;
	window.status = "ERROR：从服务器端取输入前提示信息失败"
}
function buildTypeAheadBox(e) {
	var c = makeMatches(e);
	if (c.length > 0) {
		var d = document;
		setTypePromptOutputSpanPosition(TYPE_PROMPT_theTextBox);
		var b = d.getElementById(TYPE_PROMPT_theTextBox.typePromptObj.spanOutputId);
		b.innerHTML = c;
		var a = d.getElementById(TYPE_PROMPT_theTextBox.typePromptObj.spanOutputId + "_inner");
		if (a != null) {
			var f = TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.spanOutputMaxheight;
			if (f == null || f < 15) {
				f = 350
			}
			if (a.offsetHeight < f - 10) {
				b.style.height = (a.offsetHeight + 10) + "px"
			} else {
				b.style.height = f + "px"
			}
		}
		d.getElementById(TYPE_PROMPT_theTextBox.typePromptObj.spanOutputId + "_0").className = "spanOutputHighElement";
		TYPE_PROMPT_theTextBox.typePromptObj.currentValueSelected = 0;
		TYPE_PROMPT_theTextBox.typePromptObj.bNoResults = false;
		EventTools.addEventHandler(window.document, "mousedown", hideTypePromptTheBoxEvent)
	} else {
		TYPE_PROMPT_theTextBox.typePromptObj.currentValueSelected = -1;
		TYPE_PROMPT_theTextBox.typePromptObj.bNoResults = true;
		hideTypePromptTheBox(TYPE_PROMPT_theTextBox)
	}
}
function hideTypePromptTheBoxEvent(b) {
	var a = window.event ? window.event.srcElement : b.target;
	if (a != null) {
		if (a.getAttribute("id") == TYPE_PROMPT_theTextBox.getAttribute("id")) {
			return
		}
		if (isElementOrChildElement(a, TYPE_PROMPT_theTextBox.typePromptObj.spanOutputId)) {
			return
		}
	}
	hideTypePromptTheBox(TYPE_PROMPT_theTextBox)
}
function setTypePromptOutputSpanPosition(a) {
	var c = getElementAbsolutePosition(a);
	var b = document.getElementById(a.typePromptObj.spanOutputId);
	b.style.left = c.left + "px";
	b.style.width = parseInt(a.typePromptObj.paramsObj.spanOutputWidth) + "px";
	b.style.top = (c.top + c.height) + "px";
	b.style.display = "block";
	if (a.typePromptObj.paramsObj.timeoutSecond > 0) {
		b.onmouseout = startTypePromptTimeout;
		b.onmouseover = eraseTypePromptTimeout
	} else {
		b.onmouseout = null;
		b.onmouseover = null
	}
}
function makeMatches(g) {
	TYPE_PROMPT_theTextBox.typePromptObj.currentRecordCount = 0;
	if (TYPE_PROMPT_theTextBox.typePromptObj.resultItemsXmlRoot == null) {
		return ""
	}
	if (TYPE_PROMPT_theTextBox.typePromptObj.resultItemsCount == 0) {
		return ""
	}
	var d = "";
	var e = TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.colsArray;
	var a = 0;
	for (var u = 0, v = e.length; u < v; u++) {
		if (e[u].hidden !== true) {
			a++
		}
	}
	if (a == 0) {
		return ""
	}
	if (TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.isShowTitle) {
		d = TYPE_PROMPT_selectSpanTitleStart;
		for (var u = 0, v = e.length; u < v; u = u + 1) {
			if (e[u].hidden !== true) {
				d = d + "<span style='width:" + 99 / a + "%;display:inline-block;'>" + e[u].coltitle + "</span>"
			}
		}
		d = d + TYPE_PROMPT_selectSpanEnd
	}
	if (g == null) {
		g = ""
	}
	var l = TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.isSelectBox;
	var s = TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.isCasesensitive;
	if (s !== true) {
		g = g.toLowerCase()
	}
	var q = TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.clientMatchMethod;
	var k = TYPE_PROMPT_theTextBox.typePromptObj.resultItemsXmlRoot.childNodes;
	for (var u = 0, v = k.length; u < v; u = u + 1) {
		var w = k.item(u);
		var n = w.attributes;
		var r = "";
		var x = "";
		for (var t = 0, f = e.length; t < f; t = t + 1) {
			var o = e[t].matchmode;
			if (o == null || o == "") {
				o = "0"
			}
			var b = n.getNamedItem(e[t].collabel).value;
			if (e[t].hidden !== true) {
				if (g != "" && q == null) {
					b = formatMatchText(b, g, parseInt(o), s)
				}
				r = r + "<span style='width:" + 99 / a + "%;display:inline-block;'>" + b + "</span>"
			}
			x += b;
			if (e[t].colvalue != null) {
				x += ";;;" + n.getNamedItem(e[t].colvalue).value
			}
			x += "|||"
		}
		if (x.indexOf(x.length - 3, x.length) == "|||") {
			x = x.substring(0, x.length - 3)
		}
		for (var t = 0, f = e.length; t < f; t = t + 1) {
			var o = e[t].matchmode;
			if (o == null || o == "") {
				continue
			}
			var m = parseInt(o);
			if (m <= 0) {
				continue
			}
			var h = n.getNamedItem(e[t].collabel).value;
			var p = n.getNamedItem(e[t].colvalue).value;
			if (h == null) {
				h = ""
			}
			if (s !== true) {
				h = h.toLowerCase()
			}
			if (g == "" && l === true || q == null && defaultMatcherMethod(h, g, m) || q != null && q(h, g, m)) {
				var c = ' colparamvalues="' + x + '" onmousedown="setTypePromptText(\'' + p + "',this)\" id='" + TYPE_PROMPT_theTextBox.typePromptObj.spanOutputId + "_" + TYPE_PROMPT_theTextBox.typePromptObj.currentRecordCount + "' value='" + p + "'>" + r;
				d += TYPE_PROMPT_selectSpanStart + c + TYPE_PROMPT_selectSpanEnd;
				TYPE_PROMPT_theTextBox.typePromptObj.currentRecordCount++;
				break
			}
		}
	}
	if (TYPE_PROMPT_theTextBox.typePromptObj.currentRecordCount <= 0) {
		return ""
	}
	d = "<div id='" + TYPE_PROMPT_theTextBox.typePromptObj.spanOutputId + "_inner'>" + d + "</div>";
	return d
}
function defaultMatcherMethod(b, a, c) {
	return (c == 1 && b.indexOf(a) == 0) || (c == 2 && b.indexOf(a) >= 0)
}
function formatMatchText(b, f, e, d) {
	if (e != 1 && e != 2) {
		return b
	}
	var a = null;
	if (e == 1 && b.indexOf(f) == 0) {
		if (d == true) {
			a = new RegExp("^" + f)
		} else {
			a = new RegExp("^" + f, "i")
		}
	} else {
		if (e == 2 && b.indexOf(f) >= 0) {
			if (d == true) {
				a = new RegExp(f)
			} else {
				a = new RegExp(f, "i")
			}
		}
	}
	if (a == null) {
		return b
	}
	var c = "";
	var g = splitTextValues(b, a, f.length);
	while (g != null) {
		c = c + g.start + "<u>" + g.mid + "</u>";
		b = g.end;
		g = splitTextValues(b, a, f.length)
	}
	c = c + b;
	return c
}
function moveTypePromptHighlight(a) {
	if (TYPE_PROMPT_theTextBox.typePromptObj.currentValueSelected >= 0) {
		newValue = parseInt(TYPE_PROMPT_theTextBox.typePromptObj.currentValueSelected) + parseInt(a);
		if (newValue == TYPE_PROMPT_theTextBox.typePromptObj.currentRecordCount) {
			newValue = 0
		}
		if (newValue < 0) {
			newValue = TYPE_PROMPT_theTextBox.typePromptObj.currentRecordCount - 1
		}
		TYPE_PROMPT_theTextBox.typePromptObj.currentValueSelected = newValue;
		setTypePromptHighColor(null)
	}
}
function setTypePromptText(a, b) {
	var j = b.getAttribute("colparamvalues");
	var h = new Array();
	if (j != null && j != "") {
		var g = j.split("|||");
		var k;
		for (var d = 0, e = g.length; d < e; d++) {
			if (g[d] == null || g[d] == "") {
				continue
			}
			k = new Object();
			var f = g[d].indexOf(";;;");
			if (f > 0) {
				k.label = g[d].substring(0, f);
				k.value = g[d].substring(f + 3)
			} else {
				k.label = g[d];
				k.value = null
			}
			h[h.length] = k
		}
	}
	TYPE_PROMPT_theTextBox.value = a;
	hideTypePromptTheBox(TYPE_PROMPT_theTextBox);
	var c = TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.callbackmethod;
	if (c != null) {
		c(TYPE_PROMPT_theTextBox, h)
	}
}
function setTypePromptHighColor(b) {
	if (b) {
		var d = b.id.split("_");
		TYPE_PROMPT_theTextBox.typePromptObj.currentValueSelected = d[d.length - 1]
	}
	for (var c = 0; c < TYPE_PROMPT_theTextBox.typePromptObj.currentRecordCount; c++) {
		document.getElementById(TYPE_PROMPT_theTextBox.typePromptObj.spanOutputId + "_" + c).className = "spanOutputNormalElement"
	}
	var a = document.getElementById(TYPE_PROMPT_theTextBox.typePromptObj.spanOutputId + "_" + TYPE_PROMPT_theTextBox.typePromptObj.currentValueSelected);
	if (a) {
		a.className = "spanOutputHighElement"
	}
}
function grabTypePromptHighlighted() {
	if (TYPE_PROMPT_theTextBox.typePromptObj.currentValueSelected >= 0) {
		var a = document.getElementById(TYPE_PROMPT_theTextBox.typePromptObj.spanOutputId + "_" + TYPE_PROMPT_theTextBox.typePromptObj.currentValueSelected);
		if (a) {
			var b = a.getAttribute("value");
			setTypePromptText(b, a)
		}
	}
}
function hideTypePromptTheBox(a) {
	document.getElementById(a.typePromptObj.spanOutputId).style.display = "none";
	a.typePromptObj.currentValueSelected = -1;
	a.typePromptObj.bNoResults = false;
	clearTimeout(a.typePromptObj.timer);
	a.typePromptObj.timer = false;
	EventTools.removeEventHandler(window.document, "mousedown", hideTypePromptTheBoxEvent)
}
function startTypePromptTimeout() {
	TYPE_PROMPT_theTextBox.typePromptObj.timer = setTimeout(function() {
		hideTypePromptTheBox(TYPE_PROMPT_theTextBox)
	}, TYPE_PROMPT_theTextBox.typePromptObj.paramsObj.timeoutSecond * 1000)
}
function eraseTypePromptTimeout() {
	clearTimeout(TYPE_PROMPT_theTextBox.typePromptObj.timer);
	TYPE_PROMPT_theTextBox.typePromptObj.timer = false
}
var WX_TYPEPROMPT_LOADED = true;