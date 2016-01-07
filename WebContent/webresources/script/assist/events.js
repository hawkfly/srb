/**
 * 屏蔽退格键
 */
$(document).keydown(
		function(e) {
			var target = e.target;
			var tag = e.target.tagName.toUpperCase();
			if (e.keyCode == 8) {
				if ((tag == 'INPUT' && !$(target).attr("readonly"))
						|| (tag == 'TEXTAREA' && !$(target).attr("readonly"))) {
					if ((target.type.toUpperCase() == "RADIO")
							|| (target.type.toUpperCase() == "CHECKBOX")) {
						return false;
					} else {
						return true;
					}
				} else {
					return false;
				}
			}
	    }
);