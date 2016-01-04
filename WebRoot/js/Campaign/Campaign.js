var decimalPoints = 2;

function errorFunction() {
	return false;
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

function formatNumber(number, currencyDecimalPoint, withComma) {
	var formattedQuantity = new getNumberFormat(number);
	formattedQuantity.setCommas(withComma);
	formattedQuantity.setPlaces(currencyDecimalPoint);
	return (formattedQuantity.toFormatted());
}

function selectAllOptions(selStr)
{
  var selObj = document.getElementById(selStr);
  for (var i=0; i<selObj.options.length; i++) {
    selObj.options[i].selected = true;
  }
}

