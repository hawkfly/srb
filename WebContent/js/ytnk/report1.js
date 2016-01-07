//将区域字典的区域编号设置为不可编辑
function setNotEditable() {
	var input = document.getElementsByTagName("input");
	for ( var i = 0; i < input.length; i++) {
		var id = input[i].id;
		if (id.indexOf("F_QYBH") >= 0) {
			$(input[i]).attr("disabled", true);
			$(input[i]).css("background-color", "#fff");
		}
		if (id.indexOf("F_FYBH") >= 0) {
			$(input[i]).attr("disabled", true);
			$(input[i]).css("background-color", "#fff");
		}
	}

}
