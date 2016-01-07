var finalpage="";
function backpage(){
	try{
		navigateReportPage('groupInvoicePage','groupInvoiceRpt',finalpage);
	}
	catch(e){
		logErrorsAsJsFileLoad(e);
	}
}
function getpage(){
	var page = $(".cls-inputbox-normal").val();
	finalpage = page;
	return true;
}