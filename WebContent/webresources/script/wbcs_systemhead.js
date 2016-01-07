function isLoadedSystemJs(){if(typeof WX_ISSYSTEM_LOADED=="undefined"){return false}return true}function isLoadedApiJs(){if(typeof WX_API_LOADED=="undefined"){return false}return true}function isLoadedEditSystemJs(){if(typeof WX_EDITSYSTEM_LOADED=="undefined"){return false}return true}function isLoadedTypepromptJs(){if(typeof WX_TYPEPROMPT_LOADED=="undefined"){return false}return true}function isLoadedUtilJs(){if(typeof WX_UTIL_LOADED=="undefined"){return false}return true}function isLoadedAllBuiltinJs(){return isLoadedSystemJs()&&isLoadedApiJs()&&isLoadedEditSystemJs()&&isLoadedTypepromptJs()&&isLoadedUtilJs()}function logErrorsAsJsFileLoad(b){if(b==null){return}if(b instanceof TypeError||b instanceof ReferenceError){if(!isLoadedSystemJs()||!isLoadedUtilJs()||!isLoadedApiJs()){wbcs_log(b+"，可能是页面还没有加载完时进行的操作，不用处理")}else{throw b}}else{throw b}}function wbcs_log(b){if(typeof console=="object"){console.log(b)}else{if(typeof opera=="object"){opera.postError(b)}else{if(typeof java=="object"&&typeof java.lang=="object"){java.lang.System.out.println(b)}}}}function wbcs_info(b){if(typeof console=="object"){console.info(b)}else{if(typeof opera=="object"){opera.postError(b)}else{if(typeof java=="object"&&typeof java.lang=="object"){java.lang.System.out.println(b)}}}}function wbcs_warn(b){if(typeof console=="object"){console.warn(b)}else{if(typeof opera=="object"){opera.postError(b)}else{if(typeof java=="object"&&typeof java.lang=="object"){java.lang.System.out.println(b)}}}}function wbcs_error(b){if(typeof console=="object"){console.error(b)}else{if(typeof opera=="object"){opera.postError(b)}else{if(typeof java=="object"&&typeof java.lang=="object"){java.lang.System.out.println(b)}}}};