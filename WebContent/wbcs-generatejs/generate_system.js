var WX_selectedRowBgcolor='#ACFAF0';var WXConfig=new Object();WXConfig.showreport_url='/srb/jbsf.sr';WXConfig.showreport_onpage_url='/srb/jbsf.sr?DISPLAY_TYPE=1';WXConfig.webroot='/srb/';WXConfig.prompt_dialog_type='artdialog';WXConfig.load_error_message='<strong>系统忙，请稍后再试！</strong>';function getInputBoxValue(id,type){if(id==null||id==''||type==null||type=='') return null;  if(type=='textbox')  {    var boxObj=document.getElementById(id);if(boxObj!=null){ return boxObj.value;}return null;  }  if(type=='datepicker')  {    var boxObj=document.getElementById(id);if(boxObj!=null){ return boxObj.value;}return null;  }  if(type=='passwordbox')  {    var boxObj=document.getElementById(id);if(boxObj!=null){ return boxObj.value;}return null;  }  if(type=='textareabox')  {    var boxObj=document.getElementById(id);if(boxObj!=null){ return boxObj.value;}return null;  }  if(type=='file')  {    var obj=document.getElementById(id);if(obj!=null){var valTemp='';if(obj.tagName=='IMG'){valTemp=obj.getAttribute('srcpath');}else{valTemp=obj.value;}return valTemp;}return null;  }  if(type=='radiobox')  {    var radioObjs=document.getElementsByName(id);if(radioObjs!=null){  for(i=0;i<radioObjs.length;i=i+1){      if(radioObjs[i].checked){          return radioObjs[i].value;      }  }  return '';}return '';  }  if(type=='checkbox')  {    var chkObjs=document.getElementsByName(id);if(chkObjs==null||chkObjs.length==0) return '';var value=''; var separator=chkObjs[0].getAttribute('separator');if(separator==null||separator=='') separator=' ';for(i=0,len=chkObjs.length;i<len;i=i+1){    if(chkObjs[i].checked){        value=value+chkObjs[i].value+separator;    }}value=wx_rtrim(value,separator);return value;  }  if(type=='popupbox')  {    var boxObj=document.getElementById(id);if(boxObj!=null){ return boxObj.value;}return null;  }  if(type=='richtextbox')  {    return UE.getEditor(id).getContent();  }  if(type=='selectbox')  {    var selectboxObj=document.getElementById(id);if(selectboxObj==null) return null;if(selectboxObj.options.length==0) return '';var separator=selectboxObj.getAttribute('separator');if(separator==null||separator=='') return selectboxObj.options[selectboxObj.options.selectedIndex].value;var resultVal='';for(var i=0,len=selectboxObj.options.length;i<len;i++){  if(selectboxObj.options[i].selected){resultVal=resultVal+selectboxObj.options[i].value+separator;}}return wx_rtrim(resultVal,separator);  }  if(type=='datepicker2')  {    var boxObj=document.getElementById(id);if(boxObj!=null){ return boxObj.value;}return null;  }return null;}function getInputBoxLabel(id,type){if(id==null||id==''||type==null||type=='') return null;  if(type=='textbox')  {    return getInputBoxValue(id,type);  }  if(type=='datepicker')  {    return getInputBoxValue(id,type);  }  if(type=='passwordbox')  {    return getInputBoxValue(id,type);  }  if(type=='textareabox')  {    return getInputBoxValue(id,type);  }  if(type=='file')  {    return getInputBoxValue(id,type);  }  if(type=='radiobox')  {    var radioObjs=document.getElementsByName(id);if(radioObjs!=null){  for(i=0;i<radioObjs.length;i=i+1){      if(radioObjs[i].checked){          return radioObjs[i].getAttribute('label');      }  }  return '';}return '';  }  if(type=='checkbox')  {    var chkObjs=document.getElementsByName(id);if(chkObjs==null||chkObjs.length==0) return '';var value=''; var separator=chkObjs[0].getAttribute('separator');if(separator==null||separator=='') separator=' ';for(i=0,len=chkObjs.length;i<len;i=i+1){    if(chkObjs[i].checked){        value=value+chkObjs[i].getAttribute('label')+separator;    }}value=wx_rtrim(value,separator);return value;  }  if(type=='popupbox')  {    return getInputBoxValue(id,type);  }  if(type=='richtextbox')  {    return getInputBoxValue(id,type);  }  if(type=='selectbox')  {    var selectboxObj=document.getElementById(id);if(selectboxObj==null) return null;if(selectboxObj.options.length==0) return '';var separator=selectboxObj.getAttribute('separator');if(separator==null||separator=='') return selectboxObj.options[selectboxObj.options.selectedIndex].text;var resultVal='';for(var i=0,len=selectboxObj.options.length;i<len;i++){  if(selectboxObj.options[i].selected){resultVal=resultVal+selectboxObj.options[i].text+separator;}}return wx_rtrim(resultVal,separator);  }  if(type=='datepicker2')  {    return getInputBoxValue(id,type);  }return null;}function setInputBoxValue(id,type,newvalue){if(id==null||id==''||type==null||type=='') return;if(newvalue==null) newvalue='';  if(type=='textbox'){  var boxObj=document.getElementById(id);if(boxObj){boxObj.value=newvalue;}  }  if(type=='datepicker'){  var boxObj=document.getElementById(id);if(boxObj){boxObj.value=newvalue;}  }  if(type=='passwordbox'){  var boxObj=document.getElementById(id);if(boxObj){boxObj.value=newvalue;}  }  if(type=='textareabox'){  var boxObj=document.getElementById(id);if(boxObj){boxObj.value=newvalue;}  }  if(type=='file'){  var boxObj=document.getElementById(id);if(boxObj){if(boxObj.tagName=='IMG'){boxObj.setAttribute('srcpath',newvalue);boxObj.src=newvalue;}else{boxObj.value=newvalue;}}  }  if(type=='radiobox'){  var radioObjs=document.getElementsByName(id);if(radioObjs!=null&&radioObjs.length>0){  for(var i=0,len=radioObjs.length;i<len;i=i+1){      if(radioObjs[i].value==newvalue){radioObjs[i].checked=true;break;}  }}  }  if(type=='checkbox'){  var chkObjs=document.getElementsByName(id);if(chkObjs==null||chkObjs.length==0) return;for(var i=0,len=chkObjs.length;i<len;i=i+1){  if(isSelectedValueForSelectedBox(newvalue,chkObjs[i].value,chkObjs[0])){chkObjs[i].checked=true;}}  }  if(type=='popupbox'){  var boxObj=document.getElementById(id);if(boxObj){boxObj.value=newvalue;}  }  if(type=='richtextbox'){  UE.getEditor(id).setContent(newvalue);  }  if(type=='selectbox'){  var selectboxObj=document.getElementById(id);if(selectboxObj==null||selectboxObj.options.length==0){return;}var separator=selectboxObj.getAttribute('separator');if(separator!=null&&separator!=''){  for(var j=0,len=selectboxObj.options.length;j<len;j++){      if(selectboxObj.options[j].selected&&selectboxObj.options[j].value==newvalue){return;}  }}else{  var oldvalue=selectboxObj.options[selectboxObj.selectedIndex].value;  if(oldvalue&&oldvalue==newvalue){return;}}var i=0;for(len=selectboxObj.options.length;i<len;i=i+1){  if(selectboxObj.options[i].value==newvalue){selectboxObj.options[i].selected=true;break;}}if(i!=selectboxObj.options.length&&selectboxObj.onchange){selectboxObj.onchange();}  }  if(type=='datepicker2'){  var boxObj=document.getElementById(id);if(boxObj){boxObj.value=newvalue;}  }}function fillInputBoxToTd(pageid,reportguid,reportfamily,tdObj,type,name,fillmode,displaymode){   var boxstr='';   var textalign=tdObj.style.textAlign||tdObj.getAttribute('align'); if(textalign==null) textalign='left';   var wid=tdObj.clientWidth-2;   var updateDestTdObj=getUpdateColDestObj(tdObj,reportguid,reportfamily,tdObj);   var boxValue=updateDestTdObj.getAttribute('value');   if(boxValue==null){      boxValue='';   }else{      boxValue=boxValue.replace(/</g,'&lt;');boxValue=boxValue.replace(/>/g,'&gt;');boxValue=boxValue.replace(/\'/g,'&#039;');boxValue=boxValue.replace(/\"/g,'&quot;');   }var boxId=name;if(boxId.lastIndexOf('__')>0) boxId=boxId.substring(0,boxId.lastIndexOf('__'));   var inputboxSpanObj=document.getElementById('span_'+boxId+'_span');   var styleproperty=null;var parentid=null;var childid=null;var onfocusmethod=null;var onblurmethod=null;   var jsvalidateOnblurMethod=null;   var style_propertyvalue=null;   if(inputboxSpanObj!=null){       onfocusmethod=inputboxSpanObj.getAttribute('onfocus_propertyvalue');onblurmethod=inputboxSpanObj.getAttribute('onblur_propertyvalue');       styleproperty=inputboxSpanObj.getAttribute('styleproperty');style_propertyvalue=inputboxSpanObj.getAttribute('style_propertyvalue');       jsvalidateOnblurMethod=inputboxSpanObj.getAttribute('jsvalidate_onblur_method');       parentid=inputboxSpanObj.getAttribute('parentid');       childid=inputboxSpanObj.getAttribute('childid');   }   if(styleproperty==null) styleproperty='';styleproperty=paramdecode(styleproperty);   if(style_propertyvalue==null) style_propertyvalue='';   if(onfocusmethod==null) onfocusmethod='';if(onblurmethod==null) onblurmethod='';   if(jsvalidateOnblurMethod!=null&&jsvalidateOnblurMethod!=''){       if(onblurmethod!=''&&onblurmethod.lastIndexOf(';')!=onblurmethod.length-1) onblurmethod=onblurmethod+';';       onblurmethod=onblurmethod+jsvalidateOnblurMethod;   }   if(onfocusmethod!=''&&onfocusmethod.lastIndexOf(';')!=onfocusmethod.length-1) onfocusmethod+=';';   if(onblurmethod!=''&&onblurmethod.lastIndexOf(';')!=onblurmethod.length-1) onblurmethod+=';';   if(type=='textbox'){ boxstr="<input type='text' value=\""+boxValue+"\"";boxstr=boxstr+" id= '"+name+"' name='"+name+"' typename='textbox' "+styleproperty;boxstr=boxstr+" style=\"text-align:"+textalign+";";if(wid!=null&&parseInt(wid)>0){boxstr=boxstr+"width:"+wid+";";}boxstr=boxstr+style_propertyvalue+"\"";boxstr=boxstr+" onblur=\"try{"+onblurmethod+"fillInputBoxValueToParentTd(this,'textbox','"+reportguid+"','"+reportfamily+"',"+fillmode+");}catch(e){logErrorsAsJsFileLoad(e);}\"";var typePrompt=null;if(inputboxSpanObj!=null){typePrompt=inputboxSpanObj.getAttribute('typePrompt');}if(onfocusmethod!=null&&onfocusmethod!=''||typePrompt!=null&&typePrompt!=''){  boxstr=boxstr+" onfocus=\"try{"+onfocusmethod;   if(typePrompt!=null&&typePrompt!=''){boxstr=boxstr+"initializeTypePromptProperties(this,'"+typePrompt+"');";}  boxstr=boxstr+"}catch(e){logErrorsAsJsFileLoad(e);}\"";}boxstr=boxstr+">";   }   if(type=='datepicker'){var datepickerparams=null;var onclick_propertyvalue=null;if(inputboxSpanObj!=null){  datepickerparams=inputboxSpanObj.getAttribute('inputboxparams');onclick_propertyvalue=inputboxSpanObj.getAttribute('onclick_propertyvalue');}if(onclick_propertyvalue==null) onclick_propertyvalue='';boxstr="<input type='text' value=\""+boxValue+"\"";boxstr=boxstr+" id= '"+name+"' name='"+name+"' typename='datepicker' "+styleproperty;boxstr=boxstr+" style=\"text-align:"+textalign+";";if(wid!=null&&parseInt(wid)>0){boxstr=boxstr+"width:"+wid+";";}boxstr=boxstr+style_propertyvalue+"\"";boxstr=boxstr+" onblur=\"try{fillInputBoxValueToParentTd(this,'datepicker','"+reportguid+"','"+reportfamily+"',"+fillmode+");;"+onblurmethod+"}catch(e){logErrorsAsJsFileLoad(e);}\"";if(onfocusmethod!=null&&onfocusmethod!=''){boxstr=boxstr+" onfocus=\"try{"+onfocusmethod+"}catch(e){logErrorsAsJsFileLoad(e);}\"";}onclick_propertyvalue=onclick_propertyvalue+";WdatePicker(";if(datepickerparams!=null&&datepickerparams!=''){onclick_propertyvalue=onclick_propertyvalue+datepickerparams}onclick_propertyvalue=onclick_propertyvalue+");";boxstr=boxstr+" onclick=\"try{"+onclick_propertyvalue+"}catch(e){logErrorsAsJsFileLoad(e);}\"";boxstr=boxstr+">";   }   if(type=='passwordbox'){ boxstr="<input type='password' value=\""+boxValue+"\"";boxstr=boxstr+" id= '"+name+"' name='"+name+"' typename='passwordbox' "+styleproperty;boxstr=boxstr+" style=\"text-align:"+textalign+";";if(wid!=null&&parseInt(wid)>0){boxstr=boxstr+"width:"+wid+";";}boxstr=boxstr+style_propertyvalue+"\"";boxstr=boxstr+" onblur=\"try{"+onblurmethod+"fillInputBoxValueToParentTd(this,'passwordbox','"+reportguid+"','"+reportfamily+"',"+fillmode+");}catch(e){logErrorsAsJsFileLoad(e);}\"";var typePrompt=null;if(inputboxSpanObj!=null){typePrompt=inputboxSpanObj.getAttribute('typePrompt');}if(onfocusmethod!=null&&onfocusmethod!=''||typePrompt!=null&&typePrompt!=''){  boxstr=boxstr+" onfocus=\"try{"+onfocusmethod;   if(typePrompt!=null&&typePrompt!=''){boxstr=boxstr+"initializeTypePromptProperties(this,'"+typePrompt+"');";}  boxstr=boxstr+"}catch(e){logErrorsAsJsFileLoad(e);}\"";}boxstr=boxstr+">";   }   if(type=='textareabox'){if(fillmode==2){  var textAreabox=document.getElementById('WX_TEXTAREA_BOX');  if(textAreabox==null){      textAreabox=document.createElement('textarea');      textAreabox.className='cls-inputbox-textareabox2';textAreabox.setAttribute('typename','textareabox');      textAreabox.setAttribute('id','WX_TEXTAREA_BOX');      textAreabox.setAttribute('isStoreOldValue','true');      textAreabox.errorPromptObj=createJsValidateTipObj(textAreabox);      document.body.appendChild(textAreabox);  }  textAreabox.onblur=function(){if(onblurmethod!=''){eval(onblurmethod);}fillInputBoxValueToParentTd(this,'textareabox',reportguid,reportfamily,fillmode);;};  textAreabox.onfocus=function(){if(onfocusmethod!='') eval(onfocusmethod);};  textAreabox.value=boxValue;  setTextAreaBoxPosition(textAreabox,tdObj);  textAreabox.focus();textAreabox.dataObj=initInputBoxData(textAreabox,tdObj);boxstr='';}else if(fillmode==1){  boxstr="<textarea  ";boxstr=boxstr+" id= '"+name+"' name='"+name+"' typename='textareabox' "+styleproperty;boxstr=boxstr+" style=\"text-align:"+textalign+";";if(wid!=null&&parseInt(wid)>0){boxstr=boxstr+"width:"+wid+";";}boxstr=boxstr+style_propertyvalue+"\"";  boxstr=boxstr+" onblur=\"try{"+onblurmethod+"fillInputBoxValueToParentTd(this,'textareabox','"+reportguid+"','"+reportfamily+"',"+fillmode+");}catch(e){logErrorsAsJsFileLoad(e);}\"";  if(onfocusmethod!=null&&onfocusmethod!=''){boxstr=boxstr+" onfocus=\"try{"+onfocusmethod+"}catch(e){logErrorsAsJsFileLoad(e);}\"";}  boxstr=boxstr+">"+boxValue+"</textarea>";}   }   if(type=='file'){var displaytype=null;onclick_propertyvalue=null;if(inputboxSpanObj!=null){ var paramsOfGetPageUrl=inputboxSpanObj.getAttribute('paramsOfGetPageUrl');  displaytype=inputboxSpanObj.getAttribute('displaytype');onclick_propertyvalue=inputboxSpanObj.getAttribute('onclick_propertyvalue');}if(displaytype==null||displaytype=='') displaytype='textbox';if(onclick_propertyvalue==null) onclick_propertyvalue='';onclick_propertyvalue="popupPageByFileUploadInputbox('"+name+"','"+paramsOfGetPageUrl+"','"+displaytype+"');"+onclick_propertyvalue;if(displaytype=='image'){ var imgurl=boxValue;if(imgurl==null||imgurl=='') imgurl=WXConfig.webroot+'webresources/skin/nopicture.gif';  boxstr="<img  src=\""+imgurl+"\" srcpath=\""+boxValue+"\"";}else{  boxstr="<input type='text' value=\""+boxValue+"\"";}if(displaytype=='image'){  boxstr=boxstr+" id= '"+name+"' name='"+name+"'  typename='file' "+styleproperty;  boxstr=boxstr+" style=\""+style_propertyvalue+"\"";}else{boxstr=boxstr+" id= '"+name+"' name='"+name+"' typename='file' "+styleproperty;boxstr=boxstr+" style=\"text-align:"+textalign+";";if(wid!=null&&parseInt(wid)>0){boxstr=boxstr+"width:"+wid+";";}boxstr=boxstr+style_propertyvalue+"\"";}if(onfocusmethod!=null&&onfocusmethod!='') boxstr=boxstr+" onfocus=\"try{"+onfocusmethod+"}catch(e){logErrorsAsJsFileLoad(e);}\"";if(onblurmethod!=null&&onblurmethod!='') boxstr=boxstr+" onblur=\"try{"+onblurmethod+"}catch(e){logErrorsAsJsFileLoad(e);}\"";boxstr=boxstr+" onclick=\"try{"+onclick_propertyvalue+"}catch(e){logErrorsAsJsFileLoad(e);}\"/>";   }   if(type=='radiobox'){if(inputboxSpanObj!=null){ var inline_count=inputboxSpanObj.getAttribute('inline_count'); var iinlinecount=0;if(inline_count!=null&&inline_count!='') iinlinecount=parseInt(inline_count,10);  var childs=inputboxSpanObj.getElementsByTagName("span");  if(childs!=null&&childs.length>0){      var optionlabel=null;var optionvalue=null;      for(var i=0,len=childs.length;i<len;i++){           if(iinlinecount>0&&i>0&&i%iinlinecount==0) boxstr=boxstr+"<br>";          optionlabel=childs[i].getAttribute('label'); optionvalue=childs[i].getAttribute('value');          boxstr=boxstr+"<input type='radio'  value=\""+optionvalue+"\" label='"+optionlabel+"'";boxstr=boxstr+" id= '"+name+"' name='"+name+"' typename='radiobox' "+styleproperty;boxstr=boxstr+" style=\""+style_propertyvalue+"\"";          if(isSelectedValueForSelectedBox(boxValue,optionvalue,inputboxSpanObj)) boxstr=boxstr+" checked";          boxstr=boxstr+" onblur=\"try{"+onblurmethod+";fillGroupBoxValue(this,'radiobox','"+name+"','"+reportguid+"','"+reportfamily+"',"+fillmode+");}catch(e){logErrorsAsJsFileLoad(e);}\"";          boxstr=boxstr+" onfocus=\"try{"+onfocusmethod+";setGroupBoxStopFlag('"+name+"');}catch(e){logErrorsAsJsFileLoad(e);}\"";          boxstr=boxstr+">"+optionlabel+"</input>";      }  }}   }   if(type=='checkbox'){if(inputboxSpanObj!=null){ var inline_count=inputboxSpanObj.getAttribute('inline_count'); var iinlinecount=0;if(inline_count!=null&&inline_count!='') iinlinecount=parseInt(inline_count,10);  var childs=inputboxSpanObj.getElementsByTagName("span");  if(childs!=null&&childs.length>0){      var optionlabel=null;var optionvalue=null;      for(var i=0,len=childs.length;i<len;i++){           if(iinlinecount>0&&i>0&&i%iinlinecount==0) boxstr=boxstr+"<br>";          optionlabel=childs[i].getAttribute('label'); optionvalue=childs[i].getAttribute('value');          boxstr=boxstr+"<input type='checkbox'  value=\""+optionvalue+"\" label='"+optionlabel+"'";boxstr=boxstr+" id= '"+name+"' name='"+name+"' typename='checkbox' "+styleproperty;boxstr=boxstr+" style=\""+style_propertyvalue+"\"";          if(isSelectedValueForSelectedBox(boxValue,optionvalue,inputboxSpanObj)) boxstr=boxstr+" checked";          boxstr=boxstr+" onblur=\"try{"+onblurmethod+";fillGroupBoxValue(this,'checkbox','"+name+"','"+reportguid+"','"+reportfamily+"',"+fillmode+");}catch(e){logErrorsAsJsFileLoad(e);}\"";          boxstr=boxstr+" onfocus=\"try{"+onfocusmethod+";setGroupBoxStopFlag('"+name+"');}catch(e){logErrorsAsJsFileLoad(e);}\"";          boxstr=boxstr+">"+optionlabel+"</input>";      }  }}   }   if(type=='popupbox'){if(inputboxSpanObj==null){wx_warn('显示弹出窗口输入框失败，没有取到弹出窗口页面所需的参数'); return false;}var paramsOfGetPageUrl=inputboxSpanObj.getAttribute('paramsOfGetPageUrl');var onclick_propertyvalue=inputboxSpanObj.getAttribute('onclick_propertyvalue');if(onclick_propertyvalue==null) onclick_propertyvalue='';onclick_propertyvalue="popupPageByPopupInputbox('"+name+"','"+paramsOfGetPageUrl+"');"+onclick_propertyvalue;if(inputboxSpanObj.getAttribute('sourcebox')=='textareabox'){boxstr="<textarea";}else{boxstr="<input type='text' value=\""+boxValue+"\"";}boxstr=boxstr+" id= '"+name+"' name='"+name+"' typename='popupbox' "+styleproperty;boxstr=boxstr+" style=\"text-align:"+textalign+";";if(wid!=null&&parseInt(wid)>0){boxstr=boxstr+"width:"+wid+";";}boxstr=boxstr+style_propertyvalue+"\"";if(onfocusmethod!=null&&onfocusmethod!='') boxstr=boxstr+" onfocus=\"try{"+onfocusmethod+"}catch(e){logErrorsAsJsFileLoad(e);}\"";if(onblurmethod!=null&&onblurmethod!='') boxstr=boxstr+" onblur=\"try{"+onblurmethod+"}catch(e){logErrorsAsJsFileLoad(e);}\"";boxstr=boxstr+" onClick=\"try{"+onclick_propertyvalue+"}catch(e){logErrorsAsJsFileLoad(e);}\">";if(inputboxSpanObj.getAttribute('sourcebox')=='textareabox'){boxstr=boxstr+boxValue+"</textarea>";}   }   if(type=='richtextbox'){   }   if(type=='selectbox'){var onchangeEvent='';if(inputboxSpanObj!=null){onchangeEvent=inputboxSpanObj.getAttribute('onchange_propertyvalue');}var boxstr="<select ";boxstr=boxstr+" id= '"+name+"' name='"+name+"' typename='selectbox' "+styleproperty;boxstr=boxstr+" style=\"text-align:"+textalign+";";if(wid!=null&&parseInt(wid)>0){boxstr=boxstr+"width:"+wid+";";}boxstr=boxstr+style_propertyvalue+"\"";boxstr=boxstr+" onblur=\"try{"+onblurmethod+"fillInputBoxValueToParentTd(this,'selectbox','"+reportguid+"','"+reportfamily+"',"+fillmode+");}catch(e){logErrorsAsJsFileLoad(e);}\"";if(onchangeEvent!=null&&onchangeEvent!=''){boxstr=boxstr+" onchange=\"try{"+onchangeEvent+"}catch(e){logErrorsAsJsFileLoad(e);}\"";}  if(onfocusmethod!=null&&onfocusmethod!='') boxstr=boxstr+" onfocus=\"try{"+onfocusmethod+"}catch(e){logErrorsAsJsFileLoad(e);}\"";  boxstr=boxstr+">";  if(inputboxSpanObj!=null){      var optionSpans=inputboxSpanObj.getElementsByTagName("span");      if(optionSpans!=null&&optionSpans.length>0){           var optionlabel=null;var optionvalue=null;          for(var i=0,len=optionSpans.length;i<len;i++){              optionlabel=optionSpans[i].getAttribute('label'); optionvalue=optionSpans[i].getAttribute('value');              boxstr=boxstr+"<option value='"+optionvalue+"'";              if(isSelectedValueForSelectedBox(boxValue,optionvalue,inputboxSpanObj)) boxstr=boxstr+" selected";              boxstr=boxstr+">"+optionlabel+"</option>";          }      }  }boxstr=boxstr+"</select>";   }   if(type=='datepicker2'){var dateformat=null;if(inputboxSpanObj!=null) dateformat=inputboxSpanObj.getAttribute('dateformat');if(dateformat==null||dateformat=='') dateformat='y-mm-dd';boxstr="<input type='text' value=\""+boxValue+"\"";boxstr=boxstr+" id= '"+name+"' name='"+name+"' typename='datepicker2' "+styleproperty;boxstr=boxstr+" style=\"text-align:"+textalign+";";if(wid!=null&&parseInt(wid)>0){boxstr=boxstr+"width:"+wid+";";}boxstr=boxstr+style_propertyvalue+"\"";boxstr=boxstr+" onblur=\"try{fillInputBoxValueToParentTd(this,'datepicker2','"+reportguid+"','"+reportfamily+"',"+fillmode+");;"+onblurmethod+"}catch(e){logErrorsAsJsFileLoad(e);}\"";boxstr=boxstr+" onfocus=\"try{showCalendar('"+name+"', '"+dateformat+"');"+onfocusmethod+"}catch(e){logErrorsAsJsFileLoad(e);}\"";boxstr=boxstr+">";   }   setColDisplayValueToEditable2Td(tdObj,boxstr);var colInnerWrapEle=getColInnerWrapElement(tdObj);if(colInnerWrapEle==null) colInnerWrapEle=tdObj;   if(colInnerWrapEle.children[0]){       colInnerWrapEle.children[0].focus();          if(fillmode==2){              for(var j=0,len=colInnerWrapEle.children.length;j<len;j++){                  if(colInnerWrapEle.children[j].nodeType!=1) continue;                  colInnerWrapEle.children[j].dataObj=initInputBoxData(colInnerWrapEle.children[j],tdObj);              }          }   }}function fillInputBoxValueToParentTd(boxObj,type,reportguid,reportfamily,fillmode){   var parentTdObj=null;   if(fillmode==1){parentTdObj=getParentElementObj(boxObj,'TD');}else if(fillmode==2){parentTdObj=boxObj.dataObj.parentTdObj;}   if(parentTdObj==null) {wx_warn('没有取到输入框所属<td/>对象，设置其值失败');return;}   var label=' ';var value=' ';  if(type=='textbox'){  value=boxObj.value; label=boxObj.value;  }  if(type=='datepicker'){  value=boxObj.value; label=boxObj.value;  }  if(type=='passwordbox'){  value=boxObj.value; label=boxObj.value;  }  if(type=='textareabox'){  if(fillmode==2){var textareaObj=document.getElementById('WX_TEXTAREA_BOX');value=textareaObj.value; label=textareaObj.value;textareaObj.style.display='none';}else if(fillmode==1){value=boxObj.value; label=boxObj.value;}  }  if(type=='file'){  if(boxObj.tagName=='IMG'){value=boxObj.getAttribute('srcpath'); label=value;}else{value=boxObj.value; label=boxObj.value;}  }  if(type=='radiobox'){  var radioname=boxObj.getAttribute('name');if(radioname!=null&&radioname!=''){  var radioObjs=document.getElementsByName(radioname);  if(radioObjs!=null&&radioObjs.length>0){      for(i=0,len=radioObjs.length;i<len;i=i+1){          if(radioObjs[i].checked){              value=radioObjs[i].value;label=radioObjs[i].getAttribute('label');break;          }      }  }}  }  if(type=='checkbox'){  var chkboxname=boxObj.getAttribute('name');if(chkboxname==null||chkboxname=='') return '';var chkObjs=document.getElementsByName(chkboxname);if(chkObjs==null||chkObjs.length==0) return '';var value='';var label=''; var separator=chkObjs[0].getAttribute('separator');if(separator==null||separator=='') separator=' ';for(i=0,len=chkObjs.length;i<len;i=i+1){    if(chkObjs[i].checked){        label=label+chkObjs[i].getAttribute('label')+separator;value=value+chkObjs[i].value+separator;    }}label=wx_rtrim(label,separator);value=wx_rtrim(value,separator);  }  if(type=='popupbox'){  value=boxObj.value; label=boxObj.value;  }  if(type=='richtextbox'){  if(fillmode==2){  value=UE.getEditor('WX_RICHTEXT_BOX').getContent();label=value;}else if(fillmode==1){  value=UE.getEditor(boxObj.getAttribute('id')).getContent(); label=value;}  }  if(type=='selectbox'){  if(boxObj.options.length==0){value='';label='';return;}var separator=boxObj.getAttribute('separator');if(separator==null||separator==''){  value=boxObj.options[boxObj.options.selectedIndex].value;  label=boxObj.options[boxObj.options.selectedIndex].text;}else{  value='';label='';  for(var i=0,len=boxObj.options.length;i<len;i++){      if(boxObj.options[i].selected){          value=value+boxObj.options[i].value+separator;          label=label+boxObj.options[i].text+separator;      }  }  value=wx_rtrim(value,separator);label=wx_rtrim(label,separator);}  }  if(type=='datepicker2'){  value=boxObj.value; label=boxObj.value;  }   if(label==null){label='';}  if(fillmode==2) setColDisplayValueToEditable2Td(parentTdObj,label);  parentTdObj.setAttribute('value',label);   if(value==null){value='';}  var updateDestTdObj=getUpdateColDestObj(parentTdObj,reportguid,reportfamily,parentTdObj);  updateDestTdObj.setAttribute('value',value);}