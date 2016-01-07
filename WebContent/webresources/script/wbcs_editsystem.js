var WX_SAVE_IGNORE = 0;
var WX_SAVE_TERMINAT = -1;
var WX_SAVE_CONTINUE = 1;
var WX_CUSTOMIZE_DATAS;

function initInputBoxData(a, c) {
	var b = {
		me: a,
		parentTdObj: c
	};
	return b
}

function fillInputBoxOnClick(c, b, d, a, g, e) {
	var h = c || window.event;
	if (h.ctrlKey || h.shiftKey) {
		return false
	}
	var f = h.srcElement || h.target;
	if (f.tagName != "TD") {
		if (isWXInputBoxNode(f)) {
			return false
		}
		f = getParentElementObj(f, "TD");
		if (f == null) {
			return false
		}
	}
	if (getWXInputBoxChildNode(f) != null) {
		return false
	}
	fillInputBoxToTd(b, d, a, f, g, e, 2, 2)
}

function resetChildSelectBoxData(h) {
	var e = h.getAttribute("id");
	if (e == null || e == "") {
		return
	}
	var d = getReportGuidByInputboxId(e);
	var c = getReportMetadataObj(d);
	var f = getParentElementObj(h, "TD");
	var a = getUpdateColDestObj(f, c.reportguid, c.reportfamily, f);
	var g = a.getAttribute("oldvalue");
	var b = getInputBoxValue(h.getAttribute("id"), h.getAttribute("typename"));
	if (g == null) {
		g = ""
	}
	if (b == null) {
		b = ""
	}
	resetTdValue(f, b == g)
}

function resetTdValue(f, h) {
	var j = f.getAttribute("id");
	if (j == null || j == "") {
		return
	}
	var m = j.lastIndexOf("__td");
	if (m <= 0) {
		return
	}
	var l = document;
	var b = getReportGuidByInputboxId(j.substring(0, m));
	var o = getReportMetadataObj(b);
	var e = l.getElementById("span_" + j.substring(0, m) + "_span");
	if (e == null) {
		return
	}
	var c = e.getAttribute("childboxids");
	if (c == null || g == "") {
		return
	}
	var p = c.split(";");
	for (var d = 0; d < p.length; d++) {
		var g = p[d];
		if (g == null || g == "") {
			continue
		}
		var i = g + "__td";
		var k = j.lastIndexOf("__td");
		if (k > 0) {
			var q = j.substring(k + 4);
			if (q != null && q != "") {
				i = i + q
			}
		}
		var a = l.getElementById(i);
		if (a != null) {
			resetChildTdValue(o, a, h)
		}
	}
}

function resetChildTdValue(c, e, b) {
	var f = getUpdateColSrcObj(e, c.reportguid, c.reportfamily, e);
	var a = getUpdateColDestObj(e, c.reportguid, c.reportfamily, e);
	var d = f.getAttribute("oldInnerHTMLValue");
	if (d == null) {
		f.setAttribute("oldInnerHTMLValue", f.innerHTML)
	}
	if (b) {
		if (d == null) {
			return
		}
		f.setAttribute("value", f.getAttribute("oldvalue"));
		f.innerHTML = d;
		a.setAttribute("value", a.getAttribute("oldvalue"))
	} else {
		f.setAttribute("value", "");
		setColDisplayValueToEditable2Td(f, "&nbsp;");
		a.setAttribute("value", "")
	}
	resetTdValue(e, b)
}
var WX_MAP_INPUTBOXOLDVALUES = new Object();

function storeInputboxOldValue(a, b) {
	var c = b.getAttribute("id");
	if (c == null || c == "") {
		b.setAttribute("box_oldvalue", getInputBoxValue(c, b.getAttribute("typename")))
	} else {
		WX_MAP_INPUTBOXOLDVALUES[a + "_" + c] = getInputBoxValue(c, b.getAttribute("typename"))
	}
}

function addInputboxDataForSaving(a, g) {
	var h = g.getAttribute("typename");
	var e = g.getAttribute("id");
	var i = getReportMetadataObj(a);
	var c = "";
	var b = g;
	var f = null;
	if (i.reportfamily == ReportFamily.EDITABLELIST2 || i.reportfamily == ReportFamily.EDITABLEDETAIL2 || i.reportfamily == ReportFamily.LISTFORM) {
		if (h == "textareabox" && i.reportfamily != ReportFamily.LISTFORM) {
			f = g.dataObj.parentTdObj
		} else {
			f = getParentElementObj(g, "TD")
		}
		b = f
	} else {
		if (h == "checkbox" || h == "radiobox" || (h == "file" && g.tagName == "IMG")) {
			b = g.parentNode;
			while (b != null && b.tagName == "FONT") {
				b = b.parentNode
			}
		}
		f = getParentElementObj(g, "FONT")
	}
	if (g.getAttribute("isStoreOldValue") == "true") {
		if (e == null || e == "") {
			c = g.getAttribute("box_oldvalue")
		} else {
			c = WX_MAP_INPUTBOXOLDVALUES[a + "_" + e]
		}
	} else {
		if (f == null) {
			return
		}
		f = getUpdateColDestObj(f, a, i.reportfamily, f);
		if (f.getAttribute("oldvalue_name") != null) {
			c = f.getAttribute("oldvalue")
		}
	}
	if (c == null) {
		c = ""
	}
	var d = getInputBoxValue(e, h);
	if (d == null) {
		d = ""
	}
	if (d == c) {
		return
	}
	addDataForSaving(a, b)
}

function addElementDataForSaving(c, e) {
	var d = null;
	var b = getReportMetadataObj(c);
	e = getUpdateColSrcObj(e, c, b.reportfamily, e);
	if (b.reportfamily == ReportFamily.EDITABLELIST2 || b.reportfamily == ReportFamily.EDITABLEDETAIL2 || b.reportfamily == ReportFamily.LISTFORM) {
		d = e
	} else {
		d = getWXInputBoxChildNode(e);
		if (d == null) {
			d = getInputBoxChildNode(e)
		}
		if (d == null) {
			d = e
		}
		var a = d.getAttribute("typename");
		if (a == "checkbox" || a == "radiobox" || (a == "file" && inputboxObj.tagName == "IMG")) {
			d = e.parentNode;
			while (d != null && d.tagName == "FONT") {
				d = d.parentNode
			}
		}
	}
	addDataForSaving(c, d)
}

function addDataForSaving(c, d) {
	d.style.backgroundColor = "lightblue";
	var b = getReportMetadataObj(c);
	var a = null;
	if (b.reportfamily == ReportFamily.EDITABLELIST2 || b.reportfamily == ReportFamily.LISTFORM) {
		a = getParentElementObj(d, "TR")
	} else {
		a = "true"
	}
	doAddDataForSaving(c, a)
}

function doAddDataForSaving(a, c) {
	if (WX_UPDATE_ALLDATA == null) {
		WX_UPDATE_ALLDATA = new Object()
	}
	var d = WX_UPDATE_ALLDATA[a];
	if (d == null) {
		d = new Array();
		WX_UPDATE_ALLDATA[a] = d
	}
	var b = 0;
	for (; b < d.length; b = b + 1) {
		if (d[b] == c) {
			break
		}
	}
	if (b == d.length) {
		d[d.length] = c
	}
}

function initTinymce(a) {
	a.getBody().onfocus = function(b) {
		return function() {
			tinymceBoxOnfocus(b)
		}
	}(a);
	a.getBody().onblur = function(b) {
		return function() {
			tinymceBoxOnblur(b)
		}
	}(a);
	a = null
}

function tinymceBoxOnfocus(a) {
	WX_MAP_INPUTBOXOLDVALUES[getReportGuidByInputboxId(a.id) + "_" + a.id] = a.getContent()
}

function tinymceBoxOnblur(c) {
	var d = c.id;
	var b = getReportGuidByInputboxId(c.id);
	var e = WX_MAP_INPUTBOXOLDVALUES[b + "_" + d];
	var a = c.getContent();
	if (e == null) {
		e = ""
	}
	if (a == null) {
		a = ""
	}
	if (a != e) {
		addDataForSaving(b, c.getBody())
	}
}

function addNewDataRow(m, g, x) {
	var H = document;
	var c = H.getElementById(g + "_data");
	if (c == null) {
		wx_warn("此报表所在表格的ID不合法，无法增加新行");
		return false
	}
	var y = getReportMetadataObj(g);
	var F = y.metaDataSpanObj.getAttribute("newRowCols");
	if (F == null || F == "") {
		wx_error("没有取到报表添加记录信息，可能没有为此报表配置添加功能");
		return false
	}
	var E = getObjectByJsonString(F);
	var o = E.currentRecordCount;
	var C = E.maxRecordCount;
	if (C != null && C > 0) {
		var f = c.getAttribute("wx_MyChangedRowNumInClient");
		if (f == null) {
			f = "0"
		}
		var v = parseInt(f);
		if (o + v >= C) {
			wx_warn("此报表限制最大记录数：" + C);
			return false
		}
	}
	var p = H.getElementById(g + "_nodata_tr");
	if (p != null) {
		p.parentNode.removeChild(p)
	}
	var w = c.rows.length;
	w = w + 1;
	var e = g + "_tr_" + w;
	var q = new Array();
	var r = new Array();
	var a = H.createElement("tbody");
	var k = H.createElement("tr");
	k.className = "cls-data-tr";
	k.setAttribute("id", e);
	k.setAttribute("EDIT_TYPE", "add");
	var z;
	var u;
	var t;
	var j = E.cols;
	if (j == null || j.length == 0) {
		return false
	}
	var l = new Array();
	var n, d;
	var b = false;
	for (var B = 0; B < j.length; B = B + 1) {
		t = j[B];
		var h = H.createElement("td");
		h.appendChild(H.createTextNode(" "));
		k.appendChild(h);
		if (y.reportfamily == ReportFamily.LISTFORM) {
			h.className = "cls-data-td-listform"
		} else {
			h.className = "cls-data-td-editlist"
		}
		if (t.updatecolSrc != null && t.updatecolSrc != "") {
			h.setAttribute("updatecolSrc", t.updatecolSrc)
		}
		if (t.updatecolDest != null && t.updatecolDest != "") {
			h.setAttribute("updatecolDest", t.updatecolDest)
		}
		if (t.hidden == "true") {
			h.style.display = "none"
		}
		if (t.colWrapStart != null && t.colWrapStart != "" && t.colWrapEnd != null && t.colWrapEnd != "") {
			h.innerHTML = t.colWrapStart + t.colWrapEnd
		}
		if (t.coltype == "EMPTY") {
			continue
		}
		if (t.coltype == "ROWSELECTED-RADIOBOX") {
			setColDisplayValueToEditable2Td(h, '<input type="radio" onclick="doSelectedDataRowChkRadio(this)" name="' + g + '_rowselectbox_col">');
			continue
		} else {
			if (t.coltype == "ROWSELECTED-CHECKBOX") {
				setColDisplayValueToEditable2Td(h, '<input type="checkbox" onclick="doSelectedDataRowChkRadio(this)" name="' + g + '_rowselectbox_col">');
				continue
			}
		}
		if (t.value_name != null && t.value_name != "") {
			h.setAttribute("value_name", t.value_name)
		}
		var G = null;
		if (t.boxid != null && t.boxid != "") {
			h.setAttribute("id", t.boxid + "__td" + w);
			G = t.boxid + "__" + w
		}
		n = "";
		d = "";
		if (x != null && x[t.value_name] != null) {
			n = jsonParamDecode(x[t.value_name]);
			d = jsonParamDecode(x[t.value_name + "$label"]);
			if (d == null) {
				d = n
			}
		} else {
			if (t.defaultvalue) {
				n = t.defaultvalue;
				d = t.defaultvaluelabel;
				if (d == null) {
					d = n
				}
			}
		}
		if (n != null && n != "") {
			h.setAttribute("value", n);
			if (!b) {
				doAddDataForSaving(g, k);
				b = true
			}
		}
		if (t.coltype == "NONE-EDITABLE" || t.hidden == "true") {
			if (d != null && d != "") {
				setColDisplayValueToEditable2Td(h, d)
			}
			continue
		}
		z = t.boxtype;
		if (z == null || z == "") {
			z = "textbox"
		} else {
			z = t.boxtype
		}
		if (t.attachinfo != null && t.attachinfo != "") {
			h.setAttribute("attachinfo", t.attachinfo)
		}
		u = t.fillmode;
		if (u == null || u == "" || (u != "1" && u != "2")) {
			u = "1"
		}
		if (u == "1" || y.reportfamily == ReportFamily.LISTFORM || E.insertstyle == "listform") {
			if ((t.hasParent == null || t.hasParent != "true") && t.hasChild == "true") {
				if (z && z == "selectbox") {
					q[q.length] = G
				} else {
					r[r.length] = G
				}
			}
			var s = 1;
			if (y.reportfamily != ReportFamily.LISTFORM && E.insertstyle != "listform") {
				s = 2
			}
			var A = new Object();
			A.tdObj = h;
			A.inputboxtype = z;
			A.inputboxid = G;
			A.displaymode = s;
			l[l.length] = A
		} else {
			if (d && d != "") {
				setColDisplayValueToEditable2Td(h, d)
			}
			if (window.event) {
				h.onclick = function(I, J, i, K, L) {
					return function() {
						fillInputBoxOnClick(null, I, J, i, K, L)
					}
				}(m, g, y.reportfamily, z, G)
			} else {
				h.setAttribute("onclick", "fillInputBoxOnClick(event,'" + m + "','" + g + "','" + y.reportfamily + "','" + z + "','" + G + "')")
			}
		}
		h = null
	}
	for (var B = 0, D = l.length; B < D; B++) {
		fillInputBoxToTd(m, g, y.reportfamily, l[B].tdObj, l[B].inputboxtype, l[B].inputboxid, 1, l[B].displaymode)
	}
	a.appendChild(k);
	c.appendChild(a);
	for (var B = 0; B < q.length; B = B + 1) {
		H.getElementById(q[B]).onchange()
	}
	for (var B = 0, D = r.length; B < D; B++) {
		H.getElementById(r[B]).onblur()
	}
	if (C != null && C > 0) {
		setListReportChangedRowNumInClient(g, 1, true)
	}
	H = null;
	c = null;
	a = null;
	k = null;
	y = null
}

function setListReportChangedRowNumInClient(d, b, a) {
	var f = document.getElementById(d + "_data");
	var e = f.getAttribute("wx_MyChangedRowNumInClient");
	if (e == null || e == "") {
		if (!a) {
			return
		}
		e = "0"
	}
	var c = parseInt(e) + b;
	if (c < 0) {
		c = 0
	}
	f.setAttribute("wx_MyChangedRowNumInClient", c)
}

function onKeyEvent(c) {
	var a = -1;
	var b;
	if (window.event) {
		a = window.event.keyCode;
		b = window.event.srcElement
	} else {
		a = c.which;
		b = c.target
	}
	if (a == 13) {
		b.blur();
		return false
	}
	return true
}

function setTextAreaBoxPosition(b, a) {
	var d = 0;
	var c = 0;
	if (!a) {
		return
	}
	var e = getElementAbsolutePosition(a);
	b.style.left = e.left + "px";
	b.style.top = e.top + "px";
	b.style.display = "block";
	if (!e.width || e.width < 100) {
		b.style.width = "200px"
	} else {
		b.style.width = e.width + "px"
	}
}
var WX_MGROUP_NAME_FLAG = new Object();

function fillGroupBoxValue(d, e, c, b, a, f) {
	WX_MGROUP_NAME_FLAG[c] = "";
	setTimeout(function() {
		doFillGroupBoxValue(d, e, c, b, a, f)
	}, 130)
}

function doFillGroupBoxValue(e, f, c, b, a, g) {
	var d = WX_MGROUP_NAME_FLAG[c];
	if (!d || d != "true") {
		if (g == 1 || g == 2) {
			fillInputBoxValueToParentTd(e, f, b, a, g)
		}
	}
}

function setGroupBoxStopFlag(a) {
	WX_MGROUP_NAME_FLAG[a] = "true"
}

function getEditableListReportColValues(b, a, c, h) {
	var g = getAllEditableList2DataTrObjs(a, h);
	if (g == null || g.length == 0) {
		return null
	}
	var j = new Array();
	for (var e = 0, f = g.length; e < f; e = e + 1) {
		var d = getAllColValueByParentElementObjs(g[e].getElementsByTagName("TD"), c);
		if (d != null) {
			j[j.length] = d
		}
	}
	return j
}

function setEditableListReportColValue(b, d, f, c) {
	if (d == null || isEmptyMap(d)) {
		return false
	}
	var g = getAllEditableList2DataTrObjs(b, f);
	if (g == null || g.length == 0) {
		return false
	}
	for (var e = 0, a = g.length; e < a; e = e + 1) {
		setEditableListReportColValueInRowImpl(b, g[e], d, c)
	}
	return true
}

function getAllEditableList2DataTrObjs(b, k) {
	var d = document.getElementById(b + "_data");
	if (d == null || d.rows.length <= 0) {
		return null
	}
	var j = new Array();
	var e = null;
	for (var c = 0, f = d.rows.length; c < f; c = c + 1) {
		e = d.rows[c];
		if (!isEditableListReportTr(b, e)) {
			continue
		}
		j[j.length] = e
	}
	if (j.length == 0) {
		return null
	}
	if (k == null) {
		return j
	}
	var a = convertToArray(k);
	var g = null;
	if (a.length == 1 && a[0].name == "SELECTEDROW") {
		if (a[0].value == true || a[0].value == "true") {
			g = "true"
		} else {
			g = "false"
		}
	}
	var h = new Array();
	for (var c = 0, f = j.length; c < f; c = c + 1) {
		if ((g == "true" && isSelectedRow(j[c])) || (g == "false" && !isSelectedRow(j[c])) || isMatchAllOldValues(j[c], a)) {
			h[h.length] = j[c]
		}
	}
	return h.length == 0 ? null : h
}

function isEditableListReportTr(b, d) {
	var f = d.getAttribute("id");
	if (f == null || f.indexOf(b) < 0 || f.indexOf("_tr_") < 0) {
		return false
	}
	var a, e;
	for (var c = 0; c < d.cells.length; c = c + 1) {
		a = d.cells[c].getAttribute("value_name");
		if (a != null && a != "") {
			return true
		}
		e = d.cells[c].getAttribute("oldvalue_name");
		if (e != null && e != "") {
			return true
		}
	}
	return false
}

function isMatchAllOldValues(n, a) {
	var r = null;
	var k = null;
	for (var h = 0, m = a.length; h < m; h = h + 1) {
		k = a[h];
		var b = k.name;
		if (b == null || b == "") {
			continue
		}
		var l = k.matchmode;
		if (l == null || l == "") {
			l = "equals"
		}
		var f = 0;
		for (var d = n.cells.length; f < d; f = f + 1) {
			r = n.cells[f];
			var o = false;
			if (k.oldvalue) {
				var g = r.getAttribute("oldvalue_name");
				if (g == null || g == "") {
					continue
				}
				var q = g.lastIndexOf("__old");
				if (q > 0 && q == g.length - "__old".length) {
					g = g.substring(0, q)
				}
				if (g != b) {
					continue
				}
				var c = r.getAttribute("oldvalue");
				if (!matchOldValue(k.oldvalue, c, l)) {
					return false
				}
				o = true
			}
			if (k.value) {
				var e = r.getAttribute("value_name");
				var p;
				if (e && e != "") {
					if (e != b) {
						continue
					}
					p = getColConditionValueByParentElementObj(r)
				} else {
					e = r.getAttribute("oldvalue_name");
					if (!e || e != b) {
						continue
					}
					p = r.getAttribute("oldvalue")
				}
				if (!matchOldValue(k.value, p, l)) {
					return false
				}
				o = true
			}
			if (o) {
				break
			}
		}
		if (f == n.cells.length) {
			return false
		}
	}
	return true
}

function matchOldValue(a, c, b) {
	if (!a) {
		a = ""
	}
	if (!c) {
		c = ""
	}
	if (a == "" && c == "") {
		return true
	}
	if (b == "include" && c.indexOf(a) >= 0) {
		return true
	}
	if (b == "exclude" && c.indexOf(a) < 0) {
		return true
	}
	if (b == "start" && c.indexOf(a) == 0) {
		return true
	}
	if (b == "end" && c.lastIndexOf(a) == c.length - a.length) {
		return true
	}
	if (b == "regex" && c.match(a)) {
		return true
	}
	if (a == c) {
		return true
	}
	return false
}

function setEditableListReportColValueInRowImpl(d, h, f, e) {
	if (f == null || isEmptyMap(f)) {
		return
	}
	var c = getReportMetadataObj(d);
	var b;
	for (var g = 0, a = h.cells.length; g < a; g = g + 1) {
		setEditable2ColVaue(c, h.cells[g], f, e)
	}
}

function setEditableDetail2ReportColValue(d, f, e) {
	if (f == null || isEmptyMap(f)) {
		return false
	}
	var h = document.getElementById(d + "_data");
	if (h == null) {
		wx_warn("没有取到报表表格对象，设置编辑列值失败");
		return false
	}
	if (h.rows.length <= 0) {
		return false
	}
	var c = h.getElementsByTagName("TD");
	if (c == null || c.length == 0) {
		return
	}
	var b = getReportMetadataObj(d);
	for (var g = 0, a = c.length; g < a; g = g + 1) {
		setEditable2ColVaue(b, c[g], f, e)
	}
}

function setEditable2ColVaue(b, f, d, c) {
	var h = f.getAttribute("value_name");
	if ((h == null || h == "" || (f.style.display == "none" && (f.getAttribute("updatecolSrc") == null || f.getAttribute("updatecolSrc") == "")))) {
		if (c === true) {
			setHiddenOrNonEditableColValue(b, f, d)
		}
	} else {
		var g = jsonParamDecode(d[h]);
		if (g == null) {
			return
		}
		var a = f.getAttribute("value");
		if (a == null) {
			a = ""
		}
		f.setAttribute("value", g);
		if (a == g) {
			return
		}
		var e = jsonParamDecode(d[h + "$label"]);
		if (e == null) {
			e = g
		}
		setEditable2InputboxValue(b, f, g, e)
	}
}

function setEditable2InputboxValue(l, g, c, a) {
	var d = false;
	if (g.getAttribute("updatecolSrc") != null && g.getAttribute("updatecolSrc") != "") {
		g = getUpdateColSrcObj(g, l.reportguid, l.reportfamily, g);
		var k = getWXInputBoxChildNode(g);
		if (k != null) {
			setInputBoxValue(k.getAttribute("id"), k.getAttribute("typename"), c);
			d = true
		}
	} else {
		var j = null;
		if (g.getAttribute("updatecolDest") != null && g.getAttribute("updatecolDest") != "") {
			k = getWXInputBoxChildNode(g);
			if (k == null) {
				setColDisplayValueToEditable2Td(g, c);
				d = true
			}
		} else {
			k = getWXInputBoxChildNode(g);
			if (k == null) {
				setColDisplayValueToEditable2Td(g, a)
			} else {
				setInputBoxValue(k.getAttribute("id"), k.getAttribute("typename"), c)
			}
			d = true
		}
		if (j == null) {
			var i = g.getAttribute("id");
			if (i == null) {
				return
			}
			if (i.lastIndexOf("__td") > 0) {
				i = i.substring(0, i.lastIndexOf("__td"))
			}
			var b = document.getElementById("span_" + i + "_span");
			if (b == null) {
				return
			}
			var e = b.getAttribute("childid");
			if (e != null && e != "") {
				var f = g.getAttribute("oldvalue");
				var h = false;
				if (value == f) {
					h = true
				}
				resetTdValue(g, h)
			}
		}
	}
	if (d) {
		addElementDataForSaving(l.reportguid, g)
	}
}

function preSaveEditableListReportTypeData(k) {
	if (WX_UPDATE_ALLDATA == null) {
		return false
	}
	var e = new Array();
	var b;
	var g = WX_UPDATE_ALLDATA[k.reportguid];
	if (g == null || g.length == 0) {
		return false
	}
	var h;
	var j;
	var c = false;
	for (var f = 0, d = g.length; f < d; f = f + 1) {
		h = g[f];
		if (!hasEditDataForSavingRow(h)) {
			continue
		}
		var a = h.getElementsByTagName("TD");
		b = new Object();
		j = h.getAttribute("EDIT_TYPE");
		if (j == "add") {
			b.WX_TYPE = "add"
		} else {
			b.WX_TYPE = "update"
		}
		if (getAllSavingData(a, b) === false) {
			continue
		}
		e[e.length] = b;
		c = true
	}
	storeSavingData(k.reportguid, e);
	return c
}

function deleteListReportTypeData(b, e) {
	var a = e.lastIndexOf("|");
	var g = "";
	if (a > 0) {
		g = e.substring(a + 1)
	}
	var f = null;
	if (g == "all") {
		f = getAllEditableList2DataTrObjs(b.reportguid, null)
	} else {
		if (WX_selectedTrObjs == null) {
			return false
		}
		var d = WX_selectedTrObjs[b.reportguid];
		f = new Array();
		for (var c in d) {
			f[f.length] = d[c]
		}
	}
	return preDeleteListReportTrObjs(b, f)
}

function deleteEditableListReportRowsImpl(d, a) {
	if (a == null || a.length == 0) {
		return
	}
	var c = getReportMetadataObj(d);
	if (c == null) {
		wx_warn("删除数据失败，没有取到guid为" + d + "的元数据");
		return
	}
	if (!preDeleteListReportTrObjs(c, a)) {
		return
	}
	var b = addEditableReportSaveDataParams(c);
	if (b == "") {
		return
	}
	var e = getComponentUrl(c.pageid, c.refreshComponentGuid, c.slave_reportid);
	if (e == null || e == "") {
		wx_warn("保存数据失败，没有取到guid为" + c.reportguid + "的URL");
		return
	}
	e = removeReportNavigateInfosFromUrl(e, c, null);
	e = e + b;
	WX_saveWithDeleteUrl = e;
	if (WX_deleteConfirmessage == null || WX_deleteConfirmessage == "" || WX_deleteConfirmessage == "none") {
		doSaveEditableWithDelete("ok")
	} else {
		wx_confirm(WX_deleteConfirmessage, "删除", null, null, doSaveEditableWithDelete)
	}
}

function preDeleteListReportTrObjs(l, j) {
	if (j == null || j.length == 0) {
		return false
	}
	var a = l.metaDataSpanObj.getAttribute("deleteconfirmmessage");
	addDeleteConfirmMessage(getEditableListReportDeleteConfirmMessage(j, a));
	var d = new Array();
	var g = new Array();
	var k;
	var h;
	var f = false;
	for (var e = 0; e < j.length; e = e + 1) {
		h = j[e];
		if (h == null) {
			continue
		}
		f = true;
		k = h.getAttribute("EDIT_TYPE");
		if (k == "add") {
			d[d.length] = h
		} else {
			var c = new Object();
			c.WX_TYPE = "delete";
			if (getAllSavingData(h.getElementsByTagName("TD"), c) === false) {
				continue
			}
			g[g.length] = c
		}
	}
	var b = new Object();
	b.metadataObj = l;
	if (d.length > 0) {
		b.delNewTrObjArr = d
	}
	if (WX_listReportDeleteInfo == null) {
		WX_listReportDeleteInfo = new Array()
	}
	WX_listReportDeleteInfo[WX_listReportDeleteInfo.length] = b;
	storeSavingData(l.reportguid, g);
	return f
}

function getEditableListReportDeleteConfirmMessage(n, b) {
	if (b == null || b.indexOf("@{") < 0 || n == null || n.length == 0) {
		return b
	}
	var e = new Object();
	b = parseDeleteConfirmInfo(b, e);
	var h, k;
	for (var m in e) {
		for (var g = 0, l = n.length; g < l; g++) {
			h = n[g];
			k = h.getElementsByTagName("TD");
			var o;
			for (var f = 0, d = k.length; f < d; f++) {
				o = k[f];
				var c = getEditableReportDeleteConfirmDynvalue(o, e[m]);
				if (c != null) {
					if (c != "") {
						if (e[m + "_value"] == null) {
							e[m + "_value"] = ""
						}
						e[m + "_value"] = e[m + "_value"] + c + ","
					}
					break
				}
			}
		}
	}
	var a;
	for (var m in e) {
		a = e[m + "_value"];
		if (a == null) {
			a = ""
		}
		if (a.lastIndexOf(",") == a.length - 1) {
			a = a.substring(0, a.length - 1)
		}
		b = b.replace(m, a)
	}
	return b
}

function preSaveEditableDetail2ReportData(c, g) {
	var f = document.getElementById(c.reportguid + "_data");
	if (f == null) {
		wx_warn("没有取到需要保存/删除数据的报表对象");
		return null
	}
	var a = new Object();
	var b = f.getElementsByTagName("TD");
	if (b == null || b.length == 0) {
		wx_warn("没有需要保存/删除的数据");
		return null
	}
	if (g == WX_SAVETYPE_DELETE) {
		var d = c.metaDataSpanObj.getAttribute("deleteconfirmmessage");
		addDeleteConfirmMessage(getEditableDetailReportDeleteConfirmMessage(b, d));
		a.WX_TYPE = "delete"
	} else {
		if (WX_UPDATE_ALLDATA == null || WX_UPDATE_ALLDATA[c.reportguid] == null || WX_UPDATE_ALLDATA[c.reportguid] == "") {
			return null
		}
		a.WX_TYPE = "update"
	}
	if (getAllSavingData(b, a) === false) {
		return null
	}
	var e = new Array();
	e[e.length] = a;
	storeSavingData(c.reportguid, e);
	return a
}

function setColDisplayValueToEditable2Td(b, c) {
	var a = getColInnerWrapElement(b);
	if (a == null) {
		b.innerHTML = c
	} else {
		a.innerHTML = c
	}
}

function getColInnerWrapElement(e) {
	var d = e.childNodes;
	if (d == null || d.length == 0) {
		return null
	}
	var c;
	for (var b = 0, a = d.length; b < a; b++) {
		c = d.item(b);
		if (c.nodeType != 1) {
			continue
		}
		if (c.getAttribute("tagtype") == "COL_CONTENT_WRAP") {
			return c
		}
		c = getColInnerWrapElement(c);
		if (c != null) {
			return c
		}
	}
	return null
}

function setEditableDetailReportColValue(c, j, d) {
	if (j == null || isEmptyMap(j)) {
		return false
	}
	var p = document.getElementsByName("font_" + c);
	if (p == null || p.length == 0) {
		return false
	}
	var o = getReportMetadataObj(c);
	var h;
	var k;
	for (var f = 0, g = p.length; f < g; f = f + 1) {
		var b = false;
		h = p[f];
		k = h.getAttribute("value_name");
		if (k == null || k == "" || (h.style.display == "none" && (h.getAttribute("updatecolSrc") == null || h.getAttribute("updatecolSrc") == ""))) {
			if (d === true) {
				setHiddenOrNonEditableColValue(o, h, j)
			}
		} else {
			var e = jsonParamDecode(j[k]);
			if (e == null) {
				continue
			}
			var n = h.getAttribute("value");
			if (h.getAttribute("updatecolSrc") != null && h.getAttribute("updatecolSrc") != "") {
				if (n == e) {
					continue
				}
				b = true;
				h.setAttribute("value", e);
				h = getUpdateColSrcObj(h, c, o.reportfamily, null);
				var m = getWXInputBoxChildNode(h);
				if (m != null) {
					setInputBoxValue(m.getAttribute("id"), m.getAttribute("typename"), e)
				}
			} else {
				if (n != null) {
					if (n == e) {
						continue
					}
					b = true;
					h.setAttribute("value", e);
					if (h.style.display != "none") {
						var a = h.getAttribute("customized_inputbox");
						if (a == null || a != "true" || (h.getAttribute("updatecolDest") != null && h.getAttribute("updatecolDest") != "")) {
							var l = jsonParamDecode(j[k + "$label"]);
							if (l == null) {
								l = e
							}
							h.innerHTML = l
						}
					}
				} else {
					var m = getWXInputBoxChildNode(h);
					if (h.getAttribute("updatecolDest") != null && h.getAttribute("updatecolDest") != "") {
						var n = getInputBoxLabel(m.getAttribute("id"), m.getAttribute("typename"));
						if (n == e) {
							continue
						}
					} else {
						var n = getInputBoxValue(m.getAttribute("id"), m.getAttribute("typename"));
						if (n == e) {
							continue
						}
						setInputBoxValue(m.getAttribute("id"), m.getAttribute("typename"), e)
					}
					b = true
				}
			}
		}
		if (b) {
			addElementDataForSaving(c, h)
		}
	}
}

function preSaveEditableDetailReportData(d, h) {
	var c = d.reportguid;
	var b = document.getElementsByName("font_" + c);
	if (b == null || b.length == 0) {
		return null
	}
	var a = new Object();
	if (h == WX_SAVETYPE_DELETE) {
		var e = d.metaDataSpanObj.getAttribute("deleteconfirmmessage");
		addDeleteConfirmMessage(getEditableDetailReportDeleteConfirmMessage(b, e));
		a.WX_TYPE = "delete"
	} else {
		if (WX_UPDATE_ALLDATA == null || WX_UPDATE_ALLDATA[c] == null || WX_UPDATE_ALLDATA[c] == "") {
			return null
		}
		var g = d.metaDataSpanObj.getAttribute("current_accessmode");
		if (g == WX_ACCESSMODE_ADD) {
			h = "add"
		} else {
			if (g == WX_ACCESSMODE_UPDATE) {
				h = "update"
			} else {
				return null
			}
		}
		a.WX_TYPE = h
	}
	if (getAllSavingData(b, a) === false) {
		return null
	}
	var f = new Array();
	f[f.length] = a;
	storeSavingData(c, f);
	return a
}

function changeReportAccessMode(c, e) {
	var b = getReportMetadataObj(c);
	if (b == null) {
		return
	}
	var d = getComponentUrl(b.pageid, b.refreshComponentGuid, b.slave_reportid);
	if (d == null || d == "") {
		return
	}
	if (e == "") {
		e = null
	}
	var a = b.metaDataSpanObj.getAttribute("current_accessmode");
	if (e == null) {
		if (a == WX_ACCESSMODE_ADD) {
			d = removeReportNavigateInfosFromUrl(d, b, null)
		}
	} else {
		if (e != a && (a == WX_ACCESSMODE_ADD || e == WX_ACCESSMODE_ADD)) {
			d = removeReportNavigateInfosFromUrl(d, b, null)
		}
	}
	d = replaceUrlParamValue(d, b.reportid + "_ACCESSMODE", e);
	refreshComponent(d)
}

function closeMeAndRefreshParentReport(a) {
	if (a == null) {
		return
	}
	parent.refreshParentEditableListReport(a.pageid, a.reportid, a.edittype)
}

function refreshParentEditableListReport(a, c, e) {
	if (a == null || a == "" || c == null || c == "") {
		return
	}
	var b = getComponentGuidById(a, c);
	var f = getReportMetadataObj(b);
	if (f == null) {
		return
	}
	closePopupWin(1);
	if (f.slave_reportid == f.reportid) {
		var h = f.metaDataSpanObj.getAttribute("refreshParentReportidOnSave");
		if (h != null && h != "") {
			var g = getReportMetadataObj(getComponentGuidById(a, h));
			var d = getComponentUrl(a, g.refreshComponentGuid, g.slave_reportid);
			if (f.metaDataSpanObj.getAttribute("refreshParentReportTypeOnSave") == "true") {
				d = removeReportNavigateInfosFromUrl(d, g, null)
			}
			refreshComponent(d);
			return
		}
	}
	if (e == "add") {
		refreshComponentDisplay(a, c, true)
	} else {
		var d = getComponentUrl(a, f.refreshComponentGuid, f.slave_reportid);
		if (d == null || d == "") {
			return
		}
		refreshComponent(d)
	}
}

function getEditableDetailReportColValues(a, d, c, b) {
	var e = null;
	if (b === true) {
		var f = document.getElementById(d + "_data");
		if (f == null) {
			return null
		}
		if (f.rows.length <= 0) {
			return null
		}
		e = getAllColValueByParentElementObjs(f.getElementsByTagName("TD"), c)
	} else {
		e = getAllColValueByParentElementObjs(document.getElementsByName("font_" + d), c)
	}
	return e
}

function getAllColValueByParentElementObjs(f, c) {
	if (f == null || f.length == 0) {
		return null
	}
	var j = null;
	var h = null;
	var d = null;
	var l, b, a, k = null;
	for (var e = 0, g = f.length; e < g; e = e + 1) {
		h = f[e];
		k = null;
		b = null;
		a = h.getAttribute("value_name");
		l = h.getAttribute("oldvalue_name");
		if ((a == null || a == "") && (l == null || l == "")) {
			continue
		}
		if (a != null && a != "") {
			k = getColConditionValueByParentElementObj(h)
		}
		if (l != null && l != "") {
			b = h.getAttribute("oldvalue")
		}
		d = getColValueObj(a, k, l, b);
		if (d == null) {
			continue
		}
		if (c != null && c[d.name] != "true" && c[d.name] !== true) {
			continue
		}
		if (j == null) {
			j = new Object()
		}
		j[d.name] = d
	}
	return j
}

function getColValueObj(d, i, j, e) {
	if ((d == null || d == "") && (j == null || j == "")) {
		return null
	}
	var g = null,
		f = null,
		b = null,
		c = null;
	var a = new Object();
	if (d != null && d != "") {
		g = d;
		f = i;
		if (f == null) {
			f = ""
		}
		a.value_name = d
	} else {
		a.value_name = null
	}
	if (j != null && j != "") {
		b = j;
		c = e;
		if (c == null) {
			c = ""
		}
		if (f == null) {
			f = c
		}
		if (g == null) {
			g = j;
			var h = g.lastIndexOf("__old");
			if (h > 0 && h == g.length - 5) {
				g = g.substring(0, h)
			}
		}
		a.oldvalue_name = j
	} else {
		a.oldvalue_name = null
	}
	a.name = g;
	a.value = f;
	a.oldname = b;
	a.oldvalue = c;
	return a
}

function setHiddenOrNonEditableColValue(h, e, f) {
	var c = e.getAttribute("oldvalue_name");
	if (c == null || c == "") {
		return false
	}
	var g = c.lastIndexOf("__old");
	if (g > 0 && g == c.length - "__old".length) {
		c = c.substring(0, g)
	}
	var b = jsonParamDecode(f[c]);
	if (b == null) {
		return false
	}
	var i = e.getAttribute("oldvalue");
	if (i == null) {
		i = ""
	}
	if (i == b) {
		return false
	}
	e.setAttribute("oldvalue", b);
	if (e.style.display != "none") {
		var d = jsonParamDecode(f[c + "$label"]);
		if (d == null) {
			d = newValueTmp
		}
		if (e.tagName == "TD") {
			setColDisplayValueToEditable2Td(e, d)
		} else {
			e.innerHTML = d
		}
	}
	var a = null;
	if (h.reportfamily == ReportFamily.EDITABLELIST2 || h.reportfamily == ReportFamily.LISTFORM) {
		a = getParentElementObj(e, "TR")
	} else {
		a = "true"
	}
	doAddDataForSaving(h.reportguid, a);
	return true
}

function saveEditableReportDataImpl(w) {
	if (w == null) {
		return
	}
	WX_deleteConfirmessage = null;
	var b = "";
	var k = w.pageid;
	var h = false;
	var q = false;
	var l = false;
	var r = false;
	var j;
	var a;
	var d;
	var p;
	var o;
	var c = w.savingReportIds;
	if (c == null || c.length == 0) {
		return
	}
	var u = new Object();
	if (c.length == 1) {
		o = c[0];
		if (o == null || !isValidSaveReportParamObj(o)) {
			return
		}
		j = o.reportid;
		a = o.updatetype;
		if (a != null && a.indexOf("|") > 0) {
			a = a.substring(0, a.indexOf("|"))
		}
		d = getComponentGuidById(k, j);
		p = getReportMetadataObj(d);
		if (p == null) {
			wx_warn("保存数据失败，没有取到页面ID为" + k + ",报表ID为" + j + "的元数据");
			return
		}
		if (p.slave_reportid == p.reportid) {
			var e = p.metaDataSpanObj.getAttribute("refreshParentReportidOnSave");
			if (e != null && e != "") {
				var f = getReportMetadataObj(getComponentGuidById(k, e));
				b = getComponentUrl(k, f.refreshComponentGuid, f.slave_reportid);
				if (p.metaDataSpanObj.getAttribute("refreshParentReportTypeOnSave") == "true") {
					b = removeReportNavigateInfosFromUrl(b, f, null)
				}
				b = b + "&SAVEDSLAVEREPORT_ROOTREPORT_ID=" + e
			} else {
				b = getComponentUrl(p.pageid, p.refreshComponentGuid, p.slave_reportid)
			}
		} else {
			var t = p.metaDataSpanObj.getAttribute("isSlaveDetailReport");
			var e = p.metaDataSpanObj.getAttribute("refreshParentReportidOnSave");
			if (t == "true" && e != null && e != "") {
				var f = getReportMetadataObj(getComponentGuidById(k, e));
				b = getComponentUrl(k, f.refreshComponentGuid, f.slave_reportid);
				if (p.metaDataSpanObj.getAttribute("refreshParentReportTypeOnSave") == "true") {
					b = removeReportNavigateInfosFromUrl(b, f, null)
				}
			} else {
				b = getComponentUrl(p.pageid, p.refreshComponentGuid, null)
			}
		}
		if (b == null || b == "") {
			wx_warn("保存数据失败，没有取到页面ID为" + k + ",报表ID为" + j + "的URL");
			return
		}
		if (isValidUpdateType(p, a)) {
			var v = doSaveEditableReportData(p, o.updatetype);
			if (v === WX_SAVE_IGNORE || v === WX_SAVE_TERMINAT) {
				return
			}
			if (a == WX_SAVETYPE_DELETE) {
				h = true
			} else {
				l = true
			}
			if (v != null && v != "") {
				b = removeNavigateInfoBecauseOfSave(p, a, b);
				b = removeLazyLoadParamsFromUrl(b, p, false);
				b = b + v;
				if (a == WX_SAVETYPE_DELETE) {
					q = true
				} else {
					r = true;
					u[j] = "true"
				}
			} else {
				if (WX_listReportDeleteInfo != null && WX_listReportDeleteInfo.length > 0) {
					q = true
				}
			}
		}
	} else {
		b = getComponentUrl(k, null, null);
		if (b == null || b == "") {
			wx_warn("保存数据失败，没有取到页面ID为" + k + "的URL");
			return
		}
		var m = "";
		var v = null;
		for (var s = 0, g = c.length; s < g; s = s + 1) {
			if (c[s] == null || !isValidSaveReportParamObj(c[s])) {
				continue
			}
			j = c[s].reportid;
			a = c[s].updatetype;
			if (a != null && a.indexOf("|") > 0) {
				a = a.substring(0, a.indexOf("|"))
			}
			d = getComponentGuidById(k, j);
			p = getReportMetadataObj(d);
			if (p == null) {
				wbcs_info("没有取到页面ID为" + k + ",报表ID为" + j + "的元数据，无法对其进行保存操作，可能此报表没有显示出来");
				continue
			}
			if (!isValidUpdateType(p, a)) {
				continue
			}
			v = doSaveEditableReportData(p, c[s].updatetype);
			if (v === WX_SAVE_IGNORE) {
				continue
			}
			if (v === WX_SAVE_TERMINAT) {
				return
			}
			if (a == WX_SAVETYPE_DELETE) {
				h = true
			} else {
				l = true
			}
			if (v != null && v != "") {
				if (a == WX_SAVETYPE_DELETE) {
					q = true
				} else {
					r = true;
					u[j] = "true"
				}
				b = removeNavigateInfoBecauseOfSave(p, a, b);
				b = removeLazyLoadParamsFromUrl(b, p, false);
				b = b + v;
				m = m + j + ";";
				if (p.slave_reportid == j) {
					var e = p.metaDataSpanObj.getAttribute("refreshParentReportidOnSave");
					if (e != null && e != "") {
						m = m + e + ";";
						if (p.metaDataSpanObj.getAttribute("refreshParentReportTypeOnSave") == "true") {
							var f = getReportMetadataObj(getComponentGuidById(k, e));
							b = removeReportNavigateInfosFromUrl(b, f, null)
						}
						if (b.indexOf("&SAVEDSLAVEREPORT_ROOTREPORT_ID=") < 0) {
							b = b + "&SAVEDSLAVEREPORT_ROOTREPORT_ID=" + e
						}
					} else {
						var n = getComponentUrl(k, null, j);
						b = mergeUrlParams(b, n, false);
						b = replaceUrlParamValue(b, "SLAVE_REPORTID", null)
					}
				} else {
					var t = p.metaDataSpanObj.getAttribute("isSlaveDetailReport");
					var e = p.metaDataSpanObj.getAttribute("refreshParentReportidOnSave");
					if (t == "true" && e != null && e != "") {
						m = m + e + ";";
						if (p.metaDataSpanObj.getAttribute("refreshParentReportTypeOnSave") == "true") {
							var f = getReportMetadataObj(getComponentGuidById(k, e));
							b = removeReportNavigateInfosFromUrl(b, f, null)
						}
					}
				}
			} else {
				if (WX_listReportDeleteInfo != null && WX_listReportDeleteInfo.length > 0) {
					q = true
				}
			}
		}
		if (m != "") {
			b = replaceUrlParamValue(b, "refreshComponentGuid", "[DYNAMIC]" + m)
		}
	}
	if (!hasSaveData(b) && (WX_listReportDeleteInfo == null || WX_listReportDeleteInfo.length == 0)) {
		if (l === true && h === true) {
			wx_warn("没有要保存和删除的数据")
		} else {
			if (l === true) {
				wx_warn("没有要保存的数据")
			} else {
				wx_warn("没有要删除的数据")
			}
		}
		return
	}
	if (r === true) {
		WX_IS_SAVE = true
	}
	if (q) {
		WX_saveWithDeleteUrl = b;
		WX_mSaveReportIds = u;
		if (WX_deleteConfirmessage == null || WX_deleteConfirmessage == "" || WX_deleteConfirmessage == "none") {
			doSaveEditableWithDelete("ok")
		} else {
			wx_confirm(WX_deleteConfirmessage, "删除", null, null, doSaveEditableWithDelete)
		}
	} else {
		if (hasSaveData(b)) {
			refreshComponent(b, u)
		}
	}
	WX_deleteConfirmessage = null
}
var WX_mSaveReportIds;
var WX_deleteConfirmessage;
var WX_saveWithDeleteUrl;
var WX_listReportDeleteInfo;

function doSaveEditableWithDelete(m) {
	if (wx_isOkConfirm(m)) {
		if (WX_listReportDeleteInfo != null && WX_listReportDeleteInfo.length > 0) {
			var a;
			for (var h = 0, f = WX_listReportDeleteInfo.length; h < f; h = h + 1) {
				a = WX_listReportDeleteInfo[h];
				if (a == null || a.metadataObj == null) {
					continue
				}
				var l = null;
				if (WX_UPDATE_ALLDATA != null) {
					l = WX_UPDATE_ALLDATA[a.metadataObj.reportguid]
				}
				if (l != null && l.length == 0) {
					l = null
				}
				if (a.delNewTrObjArr != null && a.delNewTrObjArr.length > 0) {
					var d;
					for (var g = 0, e = a.delNewTrObjArr.length; g < e; g = g + 1) {
						d = a.delNewTrObjArr[g];
						if (l != null) {
							for (var c = 0, b = l.length; c < b; c = c + 1) {
								if (l[c] == d) {
									l.splice(c, 1);
									if (l.length == 0) {
										if (WX_UPDATE_ALLDATA != null) {
											delete WX_UPDATE_ALLDATA[a.metadataObj.reportguid]
										}
										break
									}
								}
							}
						}
						d.parentNode.parentNode.removeChild(d.parentNode);
						setListReportChangedRowNumInClient(a.metadataObj.reportguid, -1, false)
					}
				}
				if (WX_selectedTrObjs != null) {
					delete WX_selectedTrObjs[a.metadataObj.reportguid]
				}
				if (WX_startTrIdxMap != null) {
					delete WX_startTrIdxMap[a.metadataObj.reportguid]
				}
			}
		}
		if (hasSaveData(WX_saveWithDeleteUrl)) {
			refreshComponent(WX_saveWithDeleteUrl, WX_mSaveReportIds)
		}
	}
	WX_saveWithDeleteUrl = null;
	WX_listReportDeleteInfo = null;
	WX_deleteConfirmessage = null;
	WX_mSaveReportIds = null
}

function isValidSaveReportParamObj(c) {
	var a = c.reportid;
	var b = c.updatetype;
	if (a == null || a == "") {
		wx_warn("保存数据失败,参数中没有取到报表ID");
		return false
	}
	if (b == null || b == "") {
		wx_warn("保存数据失败，没有取到保存类型");
		return false
	}
	return true
}

function isValidUpdateType(b, c) {
	var a = b.reportfamily;
	if (a != ReportFamily.EDITABLELIST2 && a != ReportFamily.LISTFORM && a != ReportFamily.EDITABLEDETAIL2 && a != ReportFamily.EDITABLELIST && a != ReportFamily.EDITABLEDETAIL && a != ReportFamily.FORM) {
		wx_warn(b.reportid + "对应的报表类型不可编辑，不能保存其数据");
		return false
	}
	var d = b.metaDataSpanObj.getAttribute("current_accessmode");
	if (d == WX_ACCESSMODE_READONLY) {
		return false
	}
	if (a == ReportFamily.EDITABLEDETAIL || a == ReportFamily.FORM) {
		if (c == WX_SAVETYPE_DELETE) {
			if (d == WX_ACCESSMODE_ADD) {
				return false
			}
		} else {
			if (d != WX_ACCESSMODE_ADD && d != WX_ACCESSMODE_UPDATE) {
				return false
			}
		}
	}
	if (a == ReportFamily.EDITABLELIST && c != WX_SAVETYPE_DELETE) {
		wx_warn(b.reportid + "报表为editablelist类型，不能直接对其做添加修改操作");
		return false
	}
	return true
}

function removeNavigateInfoBecauseOfSave(b, d, c) {
	var a = b.reportfamily;
	if (a == ReportFamily.EDITABLELIST || a == ReportFamily.EDITABLELIST2 || a == ReportFamily.LISTFORM) {
		c = removeReportNavigateInfosFromUrl(c, b, null)
	} else {
		if (a == ReportFamily.EDITABLEDETAIL || a == ReportFamily.FORM || a == ReportFamily.EDITABLEDETAIL2) {
			var f = b.metaDataSpanObj.getAttribute("navigate_reportid");
			if (f == null || f == "") {
				return c
			}
			var g = b.metaDataSpanObj.getAttribute("current_accessmode");
			if (g == WX_ACCESSMODE_READONLY) {
				return c
			}
			if (d == WX_SAVETYPE_DELETE || g == WX_ACCESSMODE_ADD) {
				var e = getParamValueFromUrl(c, f + "_PAGENO");
				c = removeReportNavigateInfosFromUrl(c, b, null);
				if (e != null && e != "") {
					c = c + "&" + f + "_DYNPAGENO=" + e
				}
			}
		}
	}
	return c
}

function doSaveEditableReportData(i, h) {
	var a = h;
	if (a != null && a.indexOf("|") > 0) {
		a = a.substring(0, a.indexOf("|"))
	}
	if (a != WX_SAVETYPE_DELETE) {
		var j = i.metaDataSpanObj.getAttribute("validateSaveMethod");
		var b = getObjectByJsonString(j);
		if (b != null && b.method != null) {
			var m = b.method(i);
			if (!m) {
				return WX_SAVE_TERMINAT
			}
		}
	}
	var g = i.reportfamily;
	var c = i.reportguid;
	if (g == ReportFamily.EDITABLEDETAIL || g == ReportFamily.FORM) {
		var e = preSaveEditableDetailReportData(i, a);
		if (e == null) {
			return ""
		}
		a = e.WX_TYPE
	} else {
		if (g == ReportFamily.EDITABLELIST2 || g == ReportFamily.LISTFORM) {
			if (a == WX_SAVETYPE_DELETE) {
				if (deleteListReportTypeData(i, h) === false) {
					return ""
				}
			} else {
				if (preSaveEditableListReportTypeData(i) === false) {
					return ""
				}
				a = ""
			}
		} else {
			if (g == ReportFamily.EDITABLEDETAIL2) {
				var e = preSaveEditableDetail2ReportData(i, a);
				if (e == null) {
					return ""
				}
				a = e.WX_TYPE
			} else {
				if (g == ReportFamily.EDITABLELIST) {
					if (deleteListReportTypeData(i, h) === false) {
						return ""
					}
				}
			}
		}
	}
	var l = i.metaDataSpanObj.getAttribute("beforeSaveAction");
	var k = getObjectByJsonString(l);
	if (WX_ALL_SAVEING_DATA == null) {
		WX_ALL_SAVEING_DATA = new Object()
	}
	if (k != null && k.method != null) {
		var m = k.method(i.pageid, i.reportid, WX_ALL_SAVEING_DATA[i.reportguid]);
		if (m === WX_SAVE_IGNORE || m === WX_SAVE_TERMINAT) {
			return m
		}
	}
	var f = addEditableReportSaveDataParams(i);
	var d = addAllCustomizeParamValues(i, WX_ALL_SAVEING_DATA[i.reportguid], a);
	if (d != null && d != "") {
		f = f + "&" + d
	}
	return f
}

function addEditableReportSaveDataParams(l) {
	var d = WX_ALL_SAVEING_DATA[l.reportguid];
	if (d == null || d.length == 0) {
		return ""
	}
	var f;
	var m = "",
		a = "",
		b = "";
	for (var e = 0, g = d.length; e < g; e++) {
		f = d[e];
		if (f == null) {
			continue
		}
		var k = "",
			c = "";
		for (var j in f) {
			if (j == "WX_TYPE") {
				k = f[j]
			} else {
				if (f[j] == null) {
					continue
				}
				c += j + SAVING_NAMEVALUE_SEPERATOR + encodeURIComponent(f[j]) + SAVING_COLDATA_SEPERATOR
			}
		}
		if (c.lastIndexOf(SAVING_COLDATA_SEPERATOR) == c.length - SAVING_COLDATA_SEPERATOR.length) {
			c = c.substring(0, c.length - SAVING_COLDATA_SEPERATOR.length)
		}
		if (c == "" || k == "" || (k != "add" && k != "update" && k != "delete")) {
			continue
		}
		if (k == "add") {
			m += c + SAVING_ROWDATA_SEPERATOR
		} else {
			if (k == "delete") {
				b += c + SAVING_ROWDATA_SEPERATOR
			} else {
				a += c + SAVING_ROWDATA_SEPERATOR
			}
		}
	}
	if (m.lastIndexOf(SAVING_ROWDATA_SEPERATOR) == m.length - SAVING_ROWDATA_SEPERATOR.length) {
		m = m.substring(0, m.length - SAVING_ROWDATA_SEPERATOR.length)
	}
	if (a.lastIndexOf(SAVING_ROWDATA_SEPERATOR) == a.length - SAVING_ROWDATA_SEPERATOR.length) {
		a = a.substring(0, a.length - SAVING_ROWDATA_SEPERATOR.length)
	}
	if (b.lastIndexOf(SAVING_ROWDATA_SEPERATOR) == b.length - SAVING_ROWDATA_SEPERATOR.length) {
		b = b.substring(0, b.length - SAVING_ROWDATA_SEPERATOR.length)
	}
	var h = "";
	if (m != "") {
		h += "&" + l.reportid + "_INSERTDATAS=" + m
	}
	if (a != "") {
		h += "&" + l.reportid + "_UPDATEDATAS=" + a
	}
	if (b != "") {
		h += "&" + l.reportid + "_DELETEDATAS=" + b
	}
	return h
}

function addAllCustomizeParamValues(b, e, d) {
	if (WX_CUSTOMIZE_DATAS == null) {
		return ""
	}
	var a = WX_CUSTOMIZE_DATAS[b.reportguid];
	if (a == null) {
		return ""
	}
	var f = "";
	for (var c in a) {
		if (a[c] == null || a[c] == "") {
			continue
		}
		f = f + c + SAVING_NAMEVALUE_SEPERATOR + encodeURIComponent(a[c]) + SAVING_COLDATA_SEPERATOR
	}
	if (f.lastIndexOf(SAVING_COLDATA_SEPERATOR) == f.length - SAVING_COLDATA_SEPERATOR.length) {
		f = f.substring(0, f.length - SAVING_COLDATA_SEPERATOR.length)
	}
	if (f == "") {
		return ""
	}
	if (d != null && d != "") {
		f = f + SAVING_COLDATA_SEPERATOR + "WX_UPDATETYPE" + SAVING_NAMEVALUE_SEPERATOR + d;
		a.WX_TYPE = "customize." + d
	} else {
		a.WX_TYPE = "customize"
	}
	e[e.length] = a;
	delete WX_CUSTOMIZE_DATAS[b.reportguid];
	return b.reportid + "_CUSTOMIZEDATAS=" + f
}

function setCustomizeParamValue(c, e, b, f) {
	if (WX_CUSTOMIZE_DATAS == null) {
		WX_CUSTOMIZE_DATAS = new Object()
	}
	var d = getComponentGuidById(c, e);
	var a = WX_CUSTOMIZE_DATAS[d];
	if (a == null) {
		a = new Object();
		WX_CUSTOMIZE_DATAS[d] = a
	}
	a[b] = f
}

function hasSaveData(a) {
	if (!a || (a.indexOf("_INSERTDATAS=") < 0 && a.indexOf("_UPDATEDATAS=") < 0 && a.indexOf("_DELETEDATAS=") < 0 && a.indexOf("_CUSTOMIZEDATAS=") < 0)) {
		return false
	}
	return true
}

function getAllSavingData(b, c) {
	if (b == null || b.length == 0) {
		return false
	}
	var h;
	var a = false;
	for (var f = 0, g = b.length; f < g; f++) {
		h = b[f];
		var j = h.getAttribute("oldvalue_name");
		if (j != null && j != "") {
			var d = h.getAttribute("oldvalue");
			if (d == null) {
				d = ""
			}
			c[j] = d;
			a = true
		}
		var e = h.getAttribute("value_name");
		if (e != null && e != "") {
			var k = getColConditionValueByParentElementObj(h);
			if (k == null) {
				k = ""
			}
			c[e] = k;
			a = true
		}
	}
	return a
}

function storeSavingData(a, b) {
	if (WX_ALL_SAVEING_DATA == null) {
		WX_ALL_SAVEING_DATA = new Object()
	}
	WX_ALL_SAVEING_DATA[a] = b
}

function getEditableDetailReportDeleteConfirmMessage(h, e) {
	if (e == null || e.indexOf("@{") < 0) {
		return e
	}
	var d = new Object();
	e = parseDeleteConfirmInfo(e, d);
	for (var c in d) {
		for (var b = 0, a = h.length; b < a; b++) {
			var f = getEditableReportDeleteConfirmDynvalue(h[b], d[c]);
			if (f != null) {
				if (f != "") {
					d[c + "_value"] = f
				}
				break
			}
		}
	}
	var g;
	for (var c in d) {
		g = d[c + "_value"];
		if (g == null) {
			g = ""
		}
		e = e.replace(c, g)
	}
	return e
}

function getEditableReportDeleteConfirmDynvalue(g, b) {
	if (g == null || b == null || b == "") {
		return null
	}
	var c = g.getAttribute("oldvalue_name");
	var a = g.getAttribute("value_name");
	if ((c == null || c == "") && (a == null || a == "")) {
		return null
	}
	var d = b;
	if (d.lastIndexOf("__old") > 0) {
		d = d.substring(0, d.lastIndexOf("__old"))
	}
	if (d != c && d + "__old" != c && d != a) {
		return null
	}
	var f = null;
	if (c != null && c != "") {
		f = g.getAttribute("oldvalue")
	}
	var e = null;
	if (a != null && a != "") {
		e = getColConditionValueByParentElementObj(g)
	}
	if (b.indexOf("__old") >= 0) {
		dynvalue = f != null && f != "" ? f : e
	} else {
		dynvalue = e != null && e != "" ? e : f
	}
	if (dynvalue == null) {
		dynvalue = ""
	}
	return dynvalue
}

function parseDeleteConfirmInfo(c, b) {
	if (c == null || c.indexOf("@{") < 0) {
		return c
	}
	var a = c.indexOf("@{");
	var f, d;
	var e = 0;
	while (a >= 0) {
		f = c.substring(0, a);
		c = c.substring(a + 2);
		a = c.indexOf("}");
		if (a < 0) {
			break
		}
		d = c.substring(0, a);
		c = c.substring(a + 1);
		b["PLACE_HOLDER_" + e] = d;
		c = f + "PLACE_HOLDER_" + e + c;
		e++;
		a = c.indexOf("@{")
	}
	return c
}

function addDeleteConfirmMessage(a) {
	if (a == null || a == "") {
		return
	}
	if (a == WX_deleteConfirmessage) {
		return
	}
	if (WX_deleteConfirmessage == null) {
		WX_deleteConfirmessage = ""
	}
	if (a == "none") {
		if (WX_deleteConfirmessage == "") {
			WX_deleteConfirmessage = "none"
		}
	} else {
		if (WX_deleteConfirmessage == "none") {
			WX_deleteConfirmessage = a
		} else {
			if (WX_deleteConfirmessage == "") {
				WX_deleteConfirmessage = a
			} else {
				WX_deleteConfirmessage = WX_deleteConfirmessage + "\n" + a
			}
		}
	}
}
var autoComplete_oldData;

function loadAutoCompleteInputboxData(m, l, w, p, r, g) {
	if (w == "WX_TEXTAREA_BOX") {
		wx_warn("editablelist2/editabledetail2两种报表类型的textarea输入框类型不支持做为自动填充其它列数据的源输入框");
		return
	}
	var h = getComponentGuidById(m, l);
	var s = getReportMetadataObj(h);
	var u = "###boxobj-dismissed###";
	var o = "###boxobj-dismissed###";
	var b = document.getElementById(w);
	if (b != null) {
		try {
			u = getInputBoxValue(w, b.getAttribute("typename"));
			o = getInputBoxLabel(w, b.getAttribute("typename"))
		} catch (v) {
			u = "###boxobj-dismissed###";
			o = "###boxobj-dismissed###"
		}
	}
	var n = createGetColDataObjByString(g);
	var q = w.lastIndexOf("__");
	var c = "";
	var k = null;
	if (q > 0) {
		c = w.substring(q + 2);
		w = w.substring(0, q);
		var j = document.getElementById(h + "_tr_" + c);
		k = getEditableListReportColValuesInRow(j, n)
	} else {
		k = getEditableReportColValues(m, l, n, null)
	}
	if (u == "###boxobj-dismissed###") {
		if (r != null && r != "") {
			o = k[p].value;
			u = k[r].value
		} else {
			u = k[p].value;
			o = null
		}
	}
	if (u == null || u == "" || u == autoComplete_oldData) {
		return
	}
	var x = "";
	var f;
	var a, i;
	for (var y in k) {
		f = k[y];
		if (f == null) {
			continue
		}
		a = f.value_name;
		if (a == p) {
			if (r != null && r != "") {
				i = o
			} else {
				i = u
			}
		} else {
			if (r != null && r != "" && a == r) {
				i = u
			} else {
				i = f.value;
				if ((i == null || i == "") && (f.value_name == null || f.value_name == "")) {
					i = f.oldvalue
				}
			}
		}
		if (i == null) {
			i = ""
		}
		x = x + y + SAVING_NAMEVALUE_SEPERATOR + encodeURIComponent(i) + SAVING_COLDATA_SEPERATOR
	}
	if (x.lastIndexOf(SAVING_COLDATA_SEPERATOR) == x.length - SAVING_COLDATA_SEPERATOR.length) {
		x = x.substring(0, x.length - SAVING_COLDATA_SEPERATOR.length)
	}
	var d = getComponentUrl(m, s.refreshComponentGuid, s.slave_reportid);
	d = replaceUrlParamValue(d, "REPORTID", l);
	d = replaceUrlParamValue(d, "ACTIONTYPE", "GetAutoCompleteFormData");
	d = replaceUrlParamValue(d, "INPUTBOXID", w);
	d = replaceUrlParamValue(d, "AUTOCOMPLETE_COLCONDITION_VALUES", x);
	var t = new Object();
	t.pageid = m;
	t.reportid = l;
	t.rowindex = c;
	sendAsynRequestToServer(d, fillAutoCompleteColsMethod, onGetAutoCompleteDataErrorMethod, t)
}

function fillAutoCompleteColsMethod(a, b) {
	var c = a.responseText;
	if (c == null || c == " " || c == "") {
		return
	}
	var d = null;
	try {
		d = eval("(" + c + ")")
	} catch (e) {
		return
	}
	if (b.rowindex != null && b.rowindex != "") {
		var f = getComponentGuidById(b.pageid, b.reportid);
		var g = document.getElementById(f + "_tr_" + b.rowindex);
		setEditableListReportColValueInRow(b.pageid, b.reportid, g, d)
	} else {
		setEditableReportColValue(b.pageid, b.reportid, d, null)
	}
}

function onGetAutoCompleteDataErrorMethod(a) {
	wx_error("获取自动填充表单域数据失败")
}

function stopSaveForDemo() {
	wx_win("<div style='font-size:15px;color:#CC3399'><br>这里是公共演示，不允许保存数据到后台<br><br>您可以在本地部署WbcsDemo演示项目，进行完全体验，只需几步即可部署完成<br><br>WbcsDemo.war位于下载包的samples/目录中</div>", {
		lock: true
	});
	return false
}
var WX_EDITSYSTEM_LOADED = true;