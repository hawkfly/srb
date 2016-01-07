/*************************************************************
 * �ļ�˵����
 *		��������ȫ�ֱ����ͺ��������Թ��ͻ��˵��á�
 *************************************************************/

 /**
  * ����ɱ༭����ķ���ģʽ
  */
 var WX_ACCESSMODE_READ='read';//���ģʽ��ֻ��editabledetail����������Ч
 
 var WX_ACCESSMODE_ADD='add';//���ģʽ����editabledetail��form����������Ч
 
 var WX_ACCESSMODE_UPDATE='update';//�޸�ģʽ����editabledetail��form����������Ч
 
 var WX_ACCESSMODE_READONLY='readonly';//ֻ��ģʽ�������пɱ༭����������Ч
 
 /**
  * ����ɱ༭����ı��涯������
  */
 var WX_SAVETYPE_SAVE='save';//�������
 
 var WX_SAVETYPE_DELETE='delete';//ɾ������

/**
 * ���ͬһҳ�������������Զ��б���/���б�ѡ�е��ж���
 * �˶���Ϊһ����JAVA�е�Map<reportGuid,Map<trid,trObj>>��������reportGuidΪ�����guid��Map<trid,trObj>Ϊ�˱������б�ѡ�е�<tr/>������<tr/>��idΪ����<tr/>����Ϊֵ��
 * ���Ҫ��ȡĳ���������б�ѡ�е��ж���ʱ��ֻ�������������<report/>��guid�õ���ѡ���е�<tr/>���󼯺ϣ�ѭ�����Ϳ��Եõ����б�ѡ���еĶ���
 * �����ѡ�е��ж����ǿɱ༭�����Զ��б���������<tr/>����trObj.getAttribute('EDIT_TYPE')=='add'
 * һ������ѡ�лص������п��ܻ���Ҫȡ����Щ��ѡ���ж���
 * ����getAllSelectedTrObjs(pageid,reportid)������Ϊ��ȡĳ������/������ѡ���ж���
 */
var WX_selectedTrObjs;

/**
 * ˢ��ĳ���������ʾ
 * @param pageid ҳ��ID
 * @param componentid Ҫˢ�µ���������Ϊ�ջ�Ϊpageid����ˢ������ҳ�����ʾ
 * @param isReset �Ƿ����ñ�ˢ�������״̬
 */
function refreshComponentDisplay(pageid,componentid,isReset)
{
	var componentguid=getComponentGuidById(pageid,componentid);
	var cmetaDataObj=getReportMetadataObj(componentguid);//��Ȼ���ܲ��Ǳ������Ա������ʽ��ȡ���������ȡ�ӱ���ID
	if(cmetaDataObj==null) return;
	var url=getComponentUrl(pageid,cmetaDataObj.refreshComponentGuid,cmetaDataObj.slave_reportid);
	if(url==null||url=='') return;
	if(isReset===true)
	{
		url=resetComponentUrl(pageid,componentid,url,null);
	}
	refreshComponent(url);
}

/**
 *����������onKeyPress�¼�
 */
function onInputBoxKeyPress(event)
{
 	var intKey=-1;
	var srcObj;
	if(window.event)
	{
		intKey=window.event.keyCode;
		srcObj=window.event.srcElement;
	}else
	{
		intKey=event.which;
		srcObj=event.target;
	}
	if(intKey==13)
	{
		var frmObj=getParentElementObj(srcObj,'FORM');
		if(frmObj!=null)
		{
			var idx=0;
			for(;idx<frmObj.elements.length;idx++)
			{
				if(frmObj.elements[idx]==srcObj)
				  break;
			}
			if(idx==frmObj.elements.length) return false;
			var nextObj;
			var times=0;
			while(true)
			{//������ת����ǰ��������һ���������
			   idx=(idx+1)%frmObj.elements.length;
				nextObj=frmObj.elements[idx];
				if(++times==frmObj.elements.length||nextObj==srcObj) break;//�ֻص����ʼ�������������
				if(!nextObj||nextObj.disabled)
				{//��ǰ������ǽ���״̬����ȡ��һ��
					continue;
				}
				nextObj.focus();
				break;
			}
		}
		return false;
	}else
 	{
 		return true;
 	}
}


/**
 * ��ȡĳ����������ѡ���е�<tr/>����
 * @param pageid ��������ҳ��ID
 * @param reportid ����id
 * @return ���ر�����ѡ���еĶ������飬�Ұ��кŴ�С�����ź���
 */
function getAllSelectedTrObjs(pageid,reportid)
{
	if(WX_selectedTrObjs==null) return null; 
	var reportguid=getComponentGuidById(pageid,reportid);
	var selectTrObjs=WX_selectedTrObjs[reportguid];
	if(selectTrObjs==null) return null;
	var tridPrex=null;
	var maxRownum=-1;//�������ѡ�е����������к�
	var idxTmp;
	for(var key in selectTrObjs)
	{
		idxTmp=key.lastIndexOf('_tr_');
		if(idxTmp<0) continue;
		var myrownum=parseInt(key.substring(idxTmp+'_tr_'.length),10);//ȡ����tr���к�
		if(myrownum>maxRownum) maxRownum=myrownum;
		if(tridPrex==null) tridPrex=key.substring(0,idxTmp+'_tr_'.length);
	}
	var resultsArr=new Array();
	if(maxRownum<0)
	{//���û��ȡ������кţ�����û��ѡ���У���selectTrObjsΪ�գ�����<tr/>��id���Ϸ�
		for(var key in selectTrObjs)
		{
			resultsArr[resultsArr.length]=selectTrObjs[key];
		}
	}else
	{
		for(var i=0;i<=maxRownum;i++)
		{//��ѡ���и����кŴ�С�������������
			if(selectTrObjs[tridPrex+i]!=null) resultsArr[resultsArr.length]=selectTrObjs[tridPrex+i];
		}
	}
	return resultsArr;
}

/**
 * �ж�ĳ���Ƿ�ѡ��
 */
function isSelectedRow(trObj)
{
	if(WX_selectedTrObjs==null) return false;
	if(trObj==null||!isListReportDataTrObj(trObj)) return false;
	var trid=trObj.getAttribute('id');
	var reportguid=trid.substring(0,trid.lastIndexOf('_tr_'));//�����guid
  	if(reportguid=='') return false;
	var allSelectedTrObjs=WX_selectedTrObjs[reportguid];
	if(allSelectedTrObjs==null||allSelectedTrObjs[trid]==null) return false;
	return true;
}

/**
 * ѡ���ж���
 * @param trObj ��ѡ�е�<tr/>����
 * @param shouldInvokeOnloadMethod �Ƿ���Ҫִ����ѡ�еĻص��������������õĻ����Զ����ɵģ�����ˢ�´ӱ���ȣ���Ĭ��Ϊ��ִ�У�����true��ִ��
 */
function selectReportDataRow(trObj,shouldInvokeOnloadMethod)
{
	if(trObj==null) return;
	var trid=trObj.getAttribute("id");
	if(trid==null||trid=='') return;
  	var reportguid=trid.substring(0,trid.lastIndexOf('_tr_'));//�����guid
  	if(reportguid=='') return;
  	var rowselecttype=getRowSelectType(reportguid);
  	if(rowselecttype!='checkbox'&&rowselecttype!='radiobox'&&rowselecttype!='single'&&rowselecttype!='multiply') return;
  	WX_selectedTrObj_temp=trObj;
  	WX_shouldInvokeOnloadMethod_temp=shouldInvokeOnloadMethod;
  	if(isHasIgnoreSlaveReportsSavingData(reportguid))
  	{
  		wx_confirm('���������ܻᶪʧ�Դӱ������ݵ��޸ģ��Ƿ������',null,null,null,doSelectReportDataRowImpl);
  	}else
  	{
  		doSelectReportDataRowImpl('ok');
  	}
}

/**
 * ȡ��ĳ�е�ѡ��״̬
 */
function deselectReportDataRow(trObj,shouldInvokeOnloadMethod)
{
	if(trObj==null||!isSelectedRow(trObj)) return;
	var trid=trObj.getAttribute("id");
	if(trid==null||trid=='') return;
  	var reportguid=trid.substring(0,trid.lastIndexOf('_tr_'));//�����guid
  	if(reportguid=='') return;
  	var rowselecttype=getRowSelectType(reportguid);
  	if(rowselecttype!='checkbox'&&rowselecttype!='radiobox'&&rowselecttype!='single'&&rowselecttype!='multiply') return;
  	WX_selectedTrObj_temp=trObj;
  	WX_shouldInvokeOnloadMethod_temp=shouldInvokeOnloadMethod;
  	if(isHasIgnoreSlaveReportsSavingData(reportguid))
  	{
  		wx_confirm('���������ܻᶪʧ�Դӱ������ݵ��޸ģ��Ƿ������',null,null,null,doDeselectReportDataRowImpl);
  	}else
  	{
  		doDeselectReportDataRowImpl('ok');
  	}  	
}

/*******************************************************************************
 *���汾�ν��з���������ɾ�Ĳ��������ݣ������ڱ����Ļص�������aftersave���ʵ�
 *WX_ALL_SAVEING_DATAΪһMap���󣬼�Ϊ��ǰ��������б���guid����Ϊ���ܰ󶨱�����������
 *                              ֵΪ��Ӧ��������������ݣ���Ϊһ��Array����������ÿ��Object�����ű����һ�м�¼����Object���Բ�����Ϊ��������ֵΪֵ
 *		���ڿɱ༭ϸ���������ͣ���Array���������������ֻ��һ��Object������Ϊ���ֱ�������һ��ֻ������ɾ����һ����¼��
 *      ���������Զ��б���������ж��Object������Ϊ������ͬʱ����ɾ���Ķ�����¼��							
 *      ���ĳ�������ڱ���ʱ�����û��Լ���ӵ�WX_CUSTOMIZE_DATAS��׼����������ݣ������Ǵ����һ��������Object�����У���������Array�����С�
 *			��ʱ�����ϸ����������䱣��ֵ��Array����Ҳ������Ԫ���ˡ�
 *		���ÿ����¼��Object������һ������ļ�WX_TYPE,����ֵΪadd��update��delete��customize֮һ��ǰ�������Ǳ�ʾ�Ե�ǰ��¼�������ֲ�����
 *          �����customize��ʾ��ǰ��¼�Ǵ�������û��Զ���ı������ֵ�������EditableDetailReportType���������ͣ�������ͨ�������û��Զ��屣�����ݣ�
 *                                                                    ��WX_TYPEֵΪcustomize.add��customize.update��customize.delete
 ******************************************************************************/
var WX_ALL_SAVEING_DATA;

/**
 * @param dynDefaultValues����ӵ�ǰ����ʱĳ����ĳЩ�е�Ĭ��ֵ�����ڿɱ༭�б������͵�������/��ѡ�򣬻�����ָ��Ĭ��ֵ��<td/>�е���ʾlabel����Ϊ�����ǵ��ʱ�Ż���ʾ���������Ҫ����ʾĬ��ֵ��Ӧ��label����
 *									ָ����ʽΪjson�ַ�������ʽ������ʾ:
 *										{col1:value1,col1$label:label1,col3:value3,...}��
 *										����col1��col3Ϊ��Ӧ�е�column��������ֵ�����ڿɱ༭�б�����������ѡ�����Ҫָ��ĳ����Ĭ��ֵ��Ӧ����ʾֵ����ͨ������column$label����ָ��
 *			��������¼�¼�����ĸ�����ܣ���ο������ýӿڷ�������ؽ���
 */
function addListReportNewDataRow(pageid,reportid,dynDefaultValues)
{
	var reportguid=getComponentGuidById(pageid,reportid);
	addNewDataRow(pageid,reportguid,dynDefaultValues);
}

/**
 * ����һ������������ֵ��������ɱ༭�������ͼ���ѯ������������Ч��
 * @param isConditionbox Ҫ����ֵ��������Ƿ��ǲ�ѯ���������true���ǲ�ѯ���������false���ɱ༭����༭�����
 * @param newvaluesObj ��������Ҫ������ֵ���������Ӧ����ֵ��ͨ��json��֯����ʽΪ��{name1:"newvalue1",name2:"newvalue2",...}�����е�name�����������
 *								�����ǰ�����ò�ѯ����������ֵ�������е�name1��name2�ȷֱ�Ϊ��������ڲ�ѯ����<condition/>��name���ԣ�
 *								�����ǰ�����ÿɱ༭�������ϵ�������ֵ�������е�name1��name2�ȷֱ�ΪҪ������ֵ����������е�<col/>��column���ԡ�
 *							newvalue1��newvalue2��ΪΪ�������õ���ֵ
 * @param conditionsObj �˲���ֻ������editablelist2/listform���ֱ������͵ı༭���������ֵ��Ч�����ڶ�λ��Щ������Ӧ������ֵ�����ûָ�������������������м�¼������Щ�е�ֵ��
 *             �˲���Ҳ��ͨ��json��ʽ��֯�����ֻ��һ�����������ʽΪ{name:"column1",value:"value1",matchmode:"equals"}����Ϊ[{name:"column1",value:"value1",matchmode:"equals"}]��
 *												   ����ж�����������ʽΪ��[{name:"column1",value:"value1",matchmode:"equals"},{name:"column2",oldvalue:"value2",matchmode:"include"},...]
 *					���ڴ˷����ĸ���ʹ��˵������ο������ýӿڷ�������ؽ���
 */
function setReportInputBoxValue(pageid,reportid,isConditionbox,newvaluesObj,conditionsObj)
{
	var reportguid=getComponentGuidById(pageid,reportid);
	if(isConditionbox)
	{//��ǰ�����ò�ѯ����������ֵ
		setConditionInputBoxValue(reportguid,newvaluesObj);
	}else
	{//�༭�����
		var metadataObj=getReportMetadataObj(reportguid);
		if(metadataObj.reportfamily==ReportFamily.EDITABLELIST2||metadataObj.reportfamily==ReportFamily.LISTFORM)
		{
			setEditableListReportColValue(reportguid,newvaluesObj,conditionsObj,false);
		}else if(metadataObj.reportfamily==ReportFamily.EDITABLEDETAIL2)
		{
			setEditableDetail2ReportColValue(reportguid,newvaluesObj,false);
		}else if(metadataObj.reportfamily==ReportFamily.EDITABLEDETAIL||metadataObj.reportfamily==ReportFamily.FORM)
		{
			setEditableDetailReportColValue(reportguid,newvaluesObj,false);
		}
	}
}

/**
 * ����һ�������༭���ϵ�ֵ�������пɱ༭�������Ͷ���Ч
 * ����������������setReportInputBoxValue()������ȫ��ͬ
 */
function setEditableReportColValue(pageid,reportid,newvaluesObj,conditionsObj)
{
	var reportguid=getComponentGuidById(pageid,reportid);
	var metadataObj=getReportMetadataObj(reportguid);
	if(metadataObj.reportfamily==ReportFamily.EDITABLELIST2||metadataObj.reportfamily==ReportFamily.LISTFORM)
	{
		setEditableListReportColValue(reportguid,newvaluesObj,conditionsObj,true);
	}else if(metadataObj.reportfamily==ReportFamily.EDITABLEDETAIL2)
	{
		setEditableDetail2ReportColValue(reportguid,newvaluesObj,true);
	}else if(metadataObj.reportfamily==ReportFamily.EDITABLEDETAIL||metadataObj.reportfamily==ReportFamily.FORM)
	{
		setEditableDetailReportColValue(reportguid,newvaluesObj,true);
	}
}

/**
 * ���ò�ѯ����������ֵ
 * @param paramname Ҫ������ֵ�Ĳ�ѯ����<condition/>��name����ֵ
 * @param paramvalue Ϊ�˲�ѯ������������õ���ֵ
 */
function setInputboxValueForCondition(pageid,reportid,paramname,paramvalue)
{
	if(paramname==null||paramname=='') return;
	if(paramvalue==null) paramvalue='';
	var reportguid=getComponentGuidById(pageid,reportid);
	var newvalue='{'+paramname+':"'+jsonParamEncode(paramvalue)+'"}';
	setConditionInputBoxValue(reportguid,getObjectByJsonString(newvalue));
}

/**
 * ���ÿɱ༭����ϸ��������ͨ��ĳ��������ֵ������edtaibledetail/form����������editabledetail2��
 * @param paramname ��<col/>��column��������ֵ
 * @param paramvalue Ϊ������������õ���ֵ
 */
function setInputboxValueForDetailReport(pageid,reportid,paramname,paramvalue)
{
	if(paramname==null||paramname=='') return;
	if(paramvalue==null) paramvalue='';
	var reportguid=getComponentGuidById(pageid,reportid);
	var newvalue='{'+paramname+':"'+jsonParamEncode(paramvalue)+'"}';
	setEditableDetailReportColValue(reportguid,getObjectByJsonString(newvalue),false);
}

/**
 * ���ÿɱ༭�����Զ��б���editablelist2���б��listform��ĳ���ϵ���ֵ
 * @param trObj Ҫ������ֵ���ж���
 * @param newvaluesObj Ҫ���õ��е�column����Ӧ����ֵ����json�ַ�����֯���������setReportInputBoxValue()�����е�newvaluesObj��ʽһ�£�
 *						  ���ﲻ��ָ��������������Ϊ�Ѿ�������Ҫ����ֵ��<tr/>������ 
 */
function setEditableListReportColValueInRow(pageid,reportid,trObj,newvaluesObj)
{
	var reportguid=getComponentGuidById(pageid,reportid);
	if(!isEditableListReportTr(reportguid,trObj)) return;
	setEditableListReportColValueInRowImpl(reportguid,trObj,newvaluesObj,true);
}

/**
 * ��ȡ�ɱ༭����ĸ����¾�����
 * @param columnsObj ָ��Ҫ��ȡ�����ݵ�<col/>��column���ϣ�ָ����ʽΪ��{column1:true,column2:true,......}������column1��column2��ΪҪ��ȡ���ݵ�<col/>��column����ֵ
 *						���û��ָ������ָ��Ϊnull����ȡ����������
 *	@return	����ǿɱ༭ϸ�������򷵻�һ��Object����������������Ҫ��ȡ�������ݶ�������������������column:���<col/>��column��������ֵ��value��ŵ�ǰ�е��������ݣ�oldvalue��ŵ�ǰ�е�ԭʼ����
 *				����ǿɱ༭�����б�����б�����򷵻�Array��������ÿ��Ԫ���Ƕ�Ӧһ�м�¼�����ݶ������ݽṹ������ϸ�������Object����һ��
 * ��������������������setEditableReportColValue()��������Ӧ������ͬ
 */
function getEditableReportColValues(pageid,reportid,columnsObj,conditionsObj)
{
	var reportguid=getComponentGuidById(pageid,reportid);
	var metadataObj=getReportMetadataObj(reportguid);
	if(metadataObj.reportfamily==ReportFamily.EDITABLELIST||
		metadataObj.reportfamily==ReportFamily.EDITABLELIST2||
		metadataObj.reportfamily==ReportFamily.LISTFORM)
	{
		return getEditableListReportColValues(pageid,reportguid,columnsObj,conditionsObj);
	}else if(metadataObj.reportfamily==ReportFamily.EDITABLEDETAIL2)
	{
		return getEditableDetailReportColValues(pageid,reportguid,columnsObj,true);
	}else if(metadataObj.reportfamily==ReportFamily.EDITABLEDETAIL||metadataObj.reportfamily==ReportFamily.FORM)
	{
		return getEditableDetailReportColValues(pageid,reportguid,columnsObj,false);
	}
	return null;
}

/**
 * ��ȡָ������ָ���е�����
 * @param columnsObj ����ҳ�ӿڷ�����columnsObj��ȫһ�������ֵΪnull�����ȡ���������е�����
 */
function getEditableListReportColValuesInRow(trObj,columnsObj)
{
	return getAllColValueByParentElementObjs(trObj.getElementsByTagName('TD'),columnsObj);
}

/**
 * ����һ�������ɱ༭���������
 * paramsObj�ṹΪ
 *		pageid:"pageid",
 *		savingReportIds:[{reportid:"reportid1",reporttype:"reportype1",updatetype:"save"},{reportid:"reportid2",reporttype:"reportype2",updatetype:"delete|all"},...]
 *		updatetype��save��delete����ȡֵ������editablelist2/listform���������Զ��б�����deleteʱ������Ը���|all��ʾɾ����ҳ���ϴ˱����������ݣ����ûָ������ֻɾ��ѡ�е��С�
 */
function saveEditableReportData(paramsObj)
{
	//stopSaveForDemo();return;
	saveEditableReportDataImpl(paramsObj);
}

/**
 * ɾ���ɱ༭�����Զ��б����������
 * @param trObjArray ��ɾ�����ж�����ж������飬��ɾ�����п��������е��У�Ҳ��������������
 */
function deleteEditableListReportRows(pageid,reportid,trObjArray)
{
	//stopSaveForDemo();return;
	if(trObjArray==null) return;
	var reportguid=getComponentGuidById(pageid,reportid);
	deleteEditableListReportRowsImpl(reportguid,convertToArray(trObjArray));
}

/**
 * ���ĳ��������÷������˵�JAVA����д����ڵ���JAVA��ʱ�ᴫ��˱��������
 *
 * @param pageid Ҫ�����ı�������ҳ��<page/>��id���ԣ��������
 * @param reportid Ҫ�����ı���ID���������
 * @param serverClassName Ҫ���õķ����������ȫ�޶��������������
 * @param shouldRefreshPage �����걨�����ݺ��Ƿ�Ҫˢ�±�����ʾ����ѡ������Ĭ��ֵΪtrue
 * @param conditionsObj ����editablelist2/listform���ֱ������ͣ�ָ��Ҫ������Щ�е����ݵ����������ָ࣬����ʽ�������ȡ�����������ֱ�������������ʱ��conditionsObj������ȫһ�£���ѡ����
 * @param paramsObj ��json��ʽָ�����Ǳ������ݵ��������ݣ��ڱ����õ�JAVA��ķ����л���һ��ר�ŵĲ���������JS���������ֵ
 * @param beforeCallbackMethod ��ȡ���������ݺ�׼�������̨����ǰִ�У������ڴ˷�����ȡ���������ݣ�������ֹ���õ�ִ��
 * @param afterCallbackMethod ����������������Ҫִ�еĿͻ��˻ص�����
 */
function invokeServerActionForReportData(pageid,reportid,serverClassName,conditionsObj,paramsObj,shouldRefreshPage,beforeCallbackMethod,afterCallbackMethod)
{
	if(pageid==null||pageid=='')
	{
		wx_warn('pageid��������Ϊ�գ�����ָ��ҳ��ID');
		return;
	}
	if(reportid==null||reportid=='')
	{
		wx_warn('reportid��������Ϊ�գ�����ָ������ID');
		return;
	}
	if(serverClassName==null||serverClassName=='')
	{
		wx_warn('serverClassName��������Ϊ�գ�����ָ��Ҫ���õķ����������ȫ�޶�����');
		return;
	}
	if(shouldRefreshPage==null) shouldRefreshPage=true;
	invokeServerActionForReportDataImpl(pageid,reportid,serverClassName,conditionsObj,paramsObj,shouldRefreshPage,beforeCallbackMethod,afterCallbackMethod);
}

/**
 * ���ҳ���ҳ����ĳ��������÷������˵Ĳ����������������
 * 	����Ĵ�������IDֻӰ��ִ��ʱ����ˢ��ҳ��ʱˢ�·�Χ���������ĸ�����ĳ�ʼ�������е��ô˷��������࣬�Լ������������ˢ���ĸ������
 * @param pageid ҳ��ID
 * @param componentid ��ǰ��������Ե������id�����Ϊ�գ����൱���������ҳ��
 * @param datas ���÷������˷���ʱ��������ݣ�������һ������Ҳ������һ�����飬�����һ�����������ʽΪ{a1:'b1',a2:'b2',...}�������һ���������飬���ʽΪ[{a1:'b1',a2:'b2',...},{c1:'d1',c2:'d2',...}]
 */
function invokeServerActionForComponent(pageid,componentid,serverClassName,datas,shouldRefreshPage,afterCallbackMethod)
{
	if(pageid==null||pageid=='')
	{
		wx_warn('pageid��������Ϊ�գ�����ָ��ҳ��ID');
		return;
	}
	if(serverClassName==null||serverClassName=='')
	{
		wx_warn('serverClassName��������Ϊ�գ�����ָ��Ҫ���õķ����������ȫ�޶�����');
		return;
	}
	if(shouldRefreshPage==null) shouldRefreshPage=true;
	invokeServerActionForComponentImpl(pageid,componentid,serverClassName,datas,shouldRefreshPage,afterCallbackMethod);
}

/**
 * ����componentid����������Ǳ������������������������õ�ServerSQLActionButton��ť�¼�����������ִ�������õ�SQL���
 * ע�⣺���ͻ��˵��õ�ServerSQLActionButton��ť���õ�shouldRefreshPage��callbackMethod��Ч������ͨ�����ﴫ��������������ΪshouldRefreshPage�ڿͻ�����Ҫʹ�ã�callbackMethod�������ﴫ�Ƚ����
 *	@param componentid �����õİ�ť���������ID
 * @param buttonname �����ð�ť��Ӧ<button/>��ǩ��name����
 * @param datas �������������������
 */
function invokeComponentSqlActionButton(pageid,componentid,buttonname,datas,paramsObj,shouldRefreshPage,afterCallbackMethod)
{
	if(buttonname==null)
	{
		wx_warn('����ָ�������ð�ť��name');
		return;
	}
	invokeServerActionForReportData(pageid,componentid,'button{'+buttonname+'}',datas,paramsObj,shouldRefreshPage,null,afterCallbackMethod);
}

/**
 * ���β������ڿͻ�����������˷���һ����ͨ��������ĳ������򱨱�ҳ��û�й�ϵ
 * ���ǵ���һ�η�������serverClassName���executeServerAction()������������datas����
 *
 * @param datas  ���÷������˷���ʱ��������ݣ�������һ������Ҳ������һ�����飬�����һ�����������ʽΪ{a1:'b1',a2:'b2',...}�������һ���������飬���ʽΪ[{a1:'b1',a2:'b2',...},{c1:'d1',c2:'d2',...}]
 *						ע�⣺���ﴫ����json���ݣ���������治����������ס
 * @param callbackMethod ��������ִ����󷵻�ʱ����ûص�������ִ��ʱ���Զ�����������˷��ص��ַ�����Ϊ����
 * @param onErrorMethod ���÷������˷�������ʱ�Ĵ�����
 */
function invokeServerAction(serverClassName,datas,afterCallbackMethod,onErrorMethod)
{
	if(serverClassName==null||serverClassName=='')
	{
		wx_warn('serverClassName��������Ϊ�գ�����ָ��Ҫ���õķ����������ȫ�޶�����');
		return;
	}
	invokeServerActionImpl(serverClassName,datas,afterCallbackMethod,onErrorMethod);
}

/**************************************************
 * ��ʾ/�������ڼ���ҳ����ʾ�ӿڷ���
 *************************************************/
/**
 * ��ʾ���ڼ�����ʾ��Ϣ
 */
function displayLoadingMessage()
{
	window.status='loading...';
   var imgobj=document.getElementById('LOADING_IMG_ID');
	if(imgobj!=null)
	{
		imgobj.style.display='block';
		var documentSize=getDocumentSize();
		var documentScrollSize=getDocumentScroll();
		imgobj.style.top = (documentSize.height+documentScrollSize.scrollTop-imgobj.clientHeight) + "px";
   	imgobj.style.left = (documentSize.width+documentScrollSize.scrollLeft-imgobj.clientWidth) + "px";
	}
} 

/**
 * �������ڼ�����ʾ��Ϣ
 */
function hideLoadingMessage()
{
	window.status='';
	var imgobj=document.getElementById('LOADING_IMG_ID');
   if(imgobj!=null) imgobj.style.display='none';
}
 
/**************************************************
 * ��Ϣ��ʾ�ӿڷ���
 *************************************************/

/**
 * ͨ��alert��ʽ��ʾ��Ϣ������Ĳ�����message�ⶼ��Ĭ��ֵ�����Բ���
 * @param message ��ʾ����
 * @param title ��ʾ���ڵı���
 *	@param width ��ʾ���ڵĿ��
 * @param height ��ʾ���ڵĸ߶�
 * @param handler ��ʾʱ�ص����� Ĭ��ֵΪnull
 * @param showMask �Ƿ���ʾ����
 * @param interval �Ƿ��Զ����أ����ָ��Ϊ>0��������ָ������Զ�����
 */
//function wx_alert(message,title,width,height,handler,showMask,interval)
function wx_alert(message,paramsObj)
{
	if(paramsObj==null) paramsObj=new Object();
	if(!isExistPromptObj())
	{
		alert(message);
	}else if(WXConfig.prompt_dialog_type=='ymprompt')
	{
		if(message!=null) paramsObj.message=message;
		ymPrompt.alert(paramsObj);
	}else
	{
		if(message!=null) paramsObj.content=message;
		if(paramsObj.lock==null) paramsObj.lock=true;
		art.dialog(paramsObj);
	}
}

/**
 * ͨ��alert��ʽ��ʾ��Ϣ������Ĳ�����message�ⶼ��Ĭ��ֵ�����Բ���
 * @param message ��ʾ����
 * @param title ��ʾ���ڵı���
 *	@param width ��ʾ���ڵĿ��
 * @param height ��ʾ���ڵĸ߶�
 * @param handler ��ʾʱ�ص����� Ĭ��ֵΪnull
 * @param showMask �Ƿ���ʾ����
 * @param interval �Ƿ��Զ����أ����ָ��Ϊ>0��������ָ������Զ�����
 */
//function wx_warn(message,title,width,height,handler,showMask,interval)
function wx_warn(message,paramsObj)
{
	if(paramsObj==null) paramsObj=new Object();
	if(!isExistPromptObj())
	{
		alert(message);
	}else if(WXConfig.prompt_dialog_type=='ymprompt')
	{
		if(message!=null) paramsObj.message=message;
		ymPrompt.alert(paramsObj);
	}else
	{
		if(message!=null) paramsObj.content=message;
		if(paramsObj.lock==null) paramsObj.lock=true;
		if(paramsObj.time==null) paramsObj.time=2;
		if(paramsObj.icon==null) paramsObj.icon='warning';
		art.dialog(paramsObj);
	}
}

/**
 * ��ʾ������Ϣ
 * ����������wx_alert��ͬ
 */
//function wx_error(message,title,width,height,handler,showMask,interval)
function wx_error(message,paramsObj)
{
	if(paramsObj==null) paramsObj=new Object();
	if(!isExistPromptObj())
	{
		alert(message);
	}else if(WXConfig.prompt_dialog_type=='ymprompt')
	{
		if(message!=null) paramsObj.message=message;
		ymPrompt.errorInfo(paramsObj);
	}else
	{
		if(message!=null) paramsObj.content=message;
		if(paramsObj.lock==null) paramsObj.lock=true;
		if(paramsObj.time==null) paramsObj.time=2;
		if(paramsObj.icon==null) paramsObj.icon='error';
		art.dialog(paramsObj);
	}
}

/**
 * ��ʾ�ɹ���Ϣ
 * ����������wx_alert��ͬ
 */
//function wx_success(message,title,width,height,handler,showMask,interval)
function wx_success(message,paramsObj)
{
	if(paramsObj==null) paramsObj=new Object();
	if(!isExistPromptObj())
	{
		alert(message);
	}else if(WXConfig.prompt_dialog_type=='ymprompt')
	{
		if(message!=null) paramsObj.message=message;
		ymPrompt.succeedInfo(paramsObj);
	}else
	{
		if(message!=null) paramsObj.content=message;
		if(paramsObj.lock==null) paramsObj.lock=true;
		if(paramsObj.time==null) paramsObj.time=2;
		if(paramsObj.icon==null) paramsObj.icon='succeed';
		art.dialog(paramsObj);
	}
}

/**
 * ��ʾȷ����Ϣ����handler�����⣬��������������wx_alert��ͬ
 * �����������handler�����У���������һ����������������Ϊinput�����û������ʾ���ڵġ�ȷ������ťʱ���ᴫ��'ok'�����û������ȡ������ťʱ���ᴫ��'cancel'
 * �����û��Ϳ�����handler�������и����û�����İ�ť������Ҫ�����ִ�����
 */
var WX_CANCEL_HANDLER;
function wx_confirm(message,title,width,height,okHandler,cancelHandler)
{
	if(!isExistPromptObj()) return;
	if(WXConfig.prompt_dialog_type=='ymprompt')
	{
		WX_CANCEL_HANDLER=cancelHandler;//����������ʾ���������ֻ�ܴ�һ���������û�ѡ��ȷ�������ǡ�ȡ�������������������inputֵ�����ģ����Է���ȫ�ֱ����У��û���Ҫ��ʱ����Ե���
		ymPrompt.confirmInfo({message:message,title:title,width:width,height:height,handler:okHandler});
	}else
	{
		art.dialog.confirm(message,okHandler,cancelHandler);
	}
}

/**
 * �����wx_confirm()��������ȷ�ϴ��ں��û��Ƿ�ѡ���ˡ��ǡ�
 */
function wx_isOkConfirm(input)
{
	//alert(input);
	if(WXConfig.prompt_dialog_type=='ymprompt')
	{
		return input=='ok';
	}
	return true;
}

/**
 * �û������wx_confirm()���������е��ȡ��ʱ���ô˷�������ִ��ȡ���¼���ֻ��ymPrompt��ʾ�����Ҫ
 */
function wx_callCancelEvent()
{
	if(WXConfig.prompt_dialog_type=='ymprompt')
	{//ֻ��������ʾ�����Ҫ���ã�artdialog��ʾ����ڵ�confirmʱΪȡ���¼���һ��������λ��ָ������ʾ�����ȡ��ʱ���Զ�����
		if(WX_CANCEL_HANDLER!=null) WX_CANCEL_HANDLER();
	}
}

/**
 * �Դ��ڵ���ʽ��ʾ��ʾ��Ϣ
 * @param maxbtn true����ʾ������󻯰�ť��false������ʾ������󻯰�ť
 *	@param minbtn true����ʾ������С����ť��false������ʾ������С����ť
 */
//function wx_win(message,title,width,height,maxbtn,minbtn,handler,showMask,interval)
function wx_win(message,paramsObj)
{
	if(!isExistPromptObj()) return;
	if(paramsObj==null) paramsObj=new Object();
	if(WXConfig.prompt_dialog_type=='ymprompt')
	{
		if(message!=null) paramsObj.message=message;
		ymPrompt.win(paramsObj);
	}else
	{
		if(message!=null) paramsObj.content=message;
		if(paramsObj.lock==null) paramsObj.lock=true;
		art.dialog(paramsObj);
	}
}

/**
 * �Դ�����ʽ��ʾ��ҳ��
 * @param url��Ҫ��ʾ����ҳ��URL
 */
//function wx_winpage(url,title,width,height,maxbtn,minbtn,handler)
function wx_winpage(url,paramsObj)
{
	if(!isExistPromptObj()) return;
	if(paramsObj==null) paramsObj=new Object();
	if(WXConfig.prompt_dialog_type=='ymprompt')
	{
		paramsObj.iframe=true;
		if(url!=null) paramsObj.message=url;
		ymPrompt.win(paramsObj);
		if(paramsObj.initsize=='max')
		{//��󻯴���
			ymPrompt.max();
		}else if(paramsObj.initsize=='min')
		{//��С������
			ymPrompt.min();
		}
	}else
	{
		if(paramsObj.lock==null) paramsObj.lock=true;
		if(paramsObj.initsize=='max')
		{//ָ������󻯴�����ʾ
			 paramsObj.width='100%';
		    paramsObj.height='100%';
		    paramsObj.left='0%';
		    paramsObj.top='0%';
		    paramsObj.fixed=true;
		    paramsObj.drag=false;
		}else if(paramsObj.initsize=='min')
		{//��С������
			 paramsObj.width='0';
		    paramsObj.height='0';
		}
		ART_DIALOG_OBJ=art.dialog.open(url,paramsObj);
	}
}
/**
 * �ر���wx_winpage()�����Ĵ���
 * @param delaytime �������>0��������ʾ�ӳ�delaytime����ٹر�
 */
function closePopupWin(delaytime)
{
	if(!isExistPromptObj()) return;
	if(WXConfig.prompt_dialog_type=='ymprompt')
	{
		if(ymPrompt==null) return;
		if(delaytime!=null&&delaytime>0)
		{	
			//setTimeout(function(){ymPrompt.doHandler('close',null);},delaytime*1000);
			ymPrompt.doHandler('close',null);
		}else
		{
			ymPrompt.doHandler('close',null);
		}
	}else
	{
		if(ART_DIALOG_OBJ==null) return;
		if(delaytime!=null&&delaytime>0)
		{	
			setTimeout(function(){ART_DIALOG_OBJ.close();},delaytime*1000);
		}else
		{
			ART_DIALOG_OBJ.close();
		}
	}
}
/**
 * �ж��Ƿ��������Ӧ���͵���ʾ�������
 */
function isExistPromptObj()
{
	if((WXConfig.prompt_dialog_type=='ymprompt'&&(typeof ymPrompt!='undefined'))) return true;
	if(typeof art!='undefined') return true;
	return false;
}

/*********************************************
 * ���ù��ܺ���
 ********************************************/
 
var WX_API_LOADED=true;//���ڱ�ʶ��js�ļ������� 