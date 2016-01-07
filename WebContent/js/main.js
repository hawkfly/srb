function dyniframesize(down) {
	var pTar = null;
	if (document.getElementById){
	  pTar = document.getElementById(down);
	} else {
	  eval('pTar = ' + down + ';');
	}
	pTar.width="100%";
	pTar.height="100%";
	if (pTar && !window.opera){
	  //begin resizing iframe;
	  pTar.style.display="block";
	  if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight){
		//ns6 syntax
		pTar.height = pTar.contentDocument.body.offsetHeight +20;
		pTar.width = pTar.contentDocument.body.scrollWidth-20;
	  }else if (pTar.Document && pTar.Document.body.scrollHeight){
		//ie5+ syntax 
		pTar.height = pTar.Document.body.scrollHeight;
		pTar.width = pTar.Document.body.scrollWidth-20;
	  }
	  if(pTar.height<800)
		  pTar.height = 800;
	}
  }
  
  var bgObj;
  function lock(){
	var sWidth,sHeight; 
	sWidth=document.body.offsetWidth;
	sHeight=document.body.scrollHeight; 
    bgObj=document.createElement("div");
	bgObj.setAttribute('id','bgDiv'); 
	bgObj.style.position="absolute";
	bgObj.style.top="0";
	bgObj.style.background="#777";
	bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	bgObj.style.opacity="0.6";
	bgObj.style.left="0";
	bgObj.style.width=sWidth + "px";
	bgObj.style.height=sHeight + "px";
	bgObj.style.zIndex = "9000";
	document.body.appendChild(bgObj);
  }
  
  function winOpen(){
	document.getElementById('tzgg_win').style.display='block';
  }

  function closeDiv(){
	document.getElementById('tzgg_win').style.display='none';
	document.body.removeChild(bgObj);
  }
  
  function addNotice(){
	  if($("#title").val() == ""){
			alert("请输入公告标题！");
			$("#title").focus();
			return false;
		}
	  $("#tzgg").submit();
	  closeDiv();
	  $("#ifm")[0].contentWindow.refreshNotice();
  }


