 var decimalPoints = 2;

function errorFunction() {
	return false;
}

function resetForm() {
	document.getElementById("userName").value="";
	document.getElementById("msisdn").value="";
	document.getElementById("otherAccount").value="";
		
}

function submitForm(f, servletNameEnc) {
	var f1 = eval(f);
	f1.fromInterface.value = "true";
	f1.method = "post";
	f1.action = servletNameEnc;
	f1.submit();		
}

function formatNumber(number, currencyDecimalPoint, withComma) {
	var formattedQuantity = new getNumberFormat(number);
	formattedQuantity.setCommas(withComma);
	formattedQuantity.setPlaces(currencyDecimalPoint);
	return (formattedQuantity.toFormatted());
}

function setDropDownData(selectId,selectHiddenId){
	
	var selectObject = document.getElementById(selectId);
	document.getElementById(selectHiddenId).value = selectObject.options[selectObject.selectedIndex].innerHTML;
}

