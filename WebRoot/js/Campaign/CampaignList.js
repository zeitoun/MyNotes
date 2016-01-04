 var decimalPoints = 2;

function errorFunction() {
	return false;
}

function resetForm() {
	document.getElementById("code").value="";
	document.getElementById("title").value="";
	document.getElementById("note").value="";
	document.getElementById("productType").value="";
	document.getElementById("effectiveDate").value="";
	document.getElementById("enrollmentPoints").value="";
	document.getElementById("accruedPointsExpiryNoOfDays").value="";
//	document.getElementById("excludedRules").value="";
	
}

function submitForm(f, servletNameEnc, action) {
	var f1 = eval(f);
	f1.method = "post";
	f1.actionType.value = action;
	f1.submit();		
}

function submitEnter(E)
{
	var keyCode;
	if(E.keyCode)
		keyCode = E.keyCode;
	else if(E.which)
		keyCode = E.which;
	else
		return true; 
	if (keyCode == 13)
   	{
   		if(enterOnce==0)
   			ValidateForm();
   		//if(ValidateForm()){
   			//because it is already called from the ValidateForm()
   			//document.form1.submit()
   		//}
   		//return false;
   	}
	else
   		return true;
}


function checkRequiredFields(){
	formIns.validationType = "form";
//	return formIns.checkRequired(translate("missing"));
	return formIns.checkRequired("Missing");
}

function ValidateForm() {
	/*if (checkRequiredFields()) {
		return false;
	}*/
		//to net let the user enter amount as zero 
	/*if(!numberInstance.isDecimal(document.getElementById("amount").value)){
		alert(translate('theInputTypeFieldAmountMustContainADecimalValueOnly'));
		formIns.setFieldValue("amount","");
		document.getElementById("amount").focus();
		return false;
	}else if(parseFloat(document.getElementById("amount").value) <= 0){
		alert(translate('theAmountShouldBeGreaterThanZero') );
		formIns.setFieldValue("amount","");
		document.getElementById("amount").focus();
		return false;
		
	}*/
	/*formIns.disableFormButtons(true);*/
	return true;
}



/*function selectAll(selectBox,selectAll) { 
    // have we been passed an ID 
    if (typeof selectBox == "string") { 
        selectBox = document.getElementById(selectBox);
    } 
    // is the select box a multiple select box? 
    if (selectBox.type == "select-multiple") { 
        for (var i = 0; i < selectBox.options.length; i++) { 
             selectBox.options[i].selected = selectAll; 
        } 
    }
}*/

function selectAllOptions(selStr)
{
  var selObj = document.getElementById(selStr);
  for (var i=0; i<selObj.options.length; i++) {
    selObj.options[i].selected = true;
  }
}


