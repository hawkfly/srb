function isLoadedSystemJs() {
	if (typeof WX_ISSYSTEM_LOADED == "undefined") {
		return false
	}
	return true
}
function isLoadedApiJs() {
	if (typeof WX_API_LOADED == "undefined") {
		return false
	}
	return true
}
function isLoadedEditSystemJs() {
	if (typeof WX_EDITSYSTEM_LOADED == "undefined") {
		return false
	}
	return true
}
function isLoadedTypepromptJs() {
	if (typeof WX_TYPEPROMPT_LOADED == "undefined") {
		return false
	}
	return true
}
function isLoadedUtilJs() {
	if (typeof WX_UTIL_LOADED == "undefined") {
		return false
	}
	return true
}
function isLoadedAllBuiltinJs() {
	return isLoadedSystemJs() && isLoadedApiJs() && isLoadedEditSystemJs() && isLoadedTypepromptJs() && isLoadedUtilJs()
}
function logErrorsAsJsFileLoad(a) {
	if (a == null) {
		return
	}
	if (a instanceof TypeError || a instanceof ReferenceError) {
		if (!isLoadedSystemJs() || !isLoadedUtilJs() || !isLoadedApiJs()) {
			wbcs_log(a + "，可能是页面还没有加载完时进行的操作，不用处理")
		} else {
			throw a
		}
	} else {
		throw a
	}
}
function wbcs_log(a) {
	if (typeof console == "object") {
		console.log(a)
	} else {
		if (typeof opera == "object") {
			opera.postError(a)
		} else {
			if (typeof java == "object" && typeof java.lang == "object") {
				java.lang.System.out.println(a)
			}
		}
	}
}
function wbcs_info(a) {
	if (typeof console == "object") {
		console.info(a)
	} else {
		if (typeof opera == "object") {
			opera.postError(a)
		} else {
			if (typeof java == "object" && typeof java.lang == "object") {
				java.lang.System.out.println(a)
			}
		}
	}
}
function wbcs_warn(a) {
	if (typeof console == "object") {
		console.warn(a)
	} else {
		if (typeof opera == "object") {
			opera.postError(a)
		} else {
			if (typeof java == "object" && typeof java.lang == "object") {
				java.lang.System.out.println(a)
			}
		}
	}
}
function wbcs_error(a) {
	if (typeof console == "object") {
		console.error(a)
	} else {
		if (typeof opera == "object") {
			opera.postError(a)
		} else {
			if (typeof java == "object" && typeof java.lang == "object") {
				java.lang.System.out.println(a)
			}
		}
	}
};