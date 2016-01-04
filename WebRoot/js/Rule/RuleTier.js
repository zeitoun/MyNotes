

function setDropDownData(selectId,selectHiddenId){
		
	var selectObject = document.getElementById(selectId);
	document.getElementById(selectHiddenId).value = selectObject.options[selectObject.selectedIndex].innerHTML;
}

function submitForm(f, servletNameEnc, action) {
	var f1 = eval(f);
	f1.method = "post";
	f1.actionType.value = action;
	f1.submit();		
}

function popitup(url) {
	newwindow=window.open(url,'name','height=500,width=500','resizable=no');
	if (window.focus) {newwindow.focus()}
	return false;
}



function closePopup() {

	var fromValue = document.getElementById("fromValue").value;
	var toValue = document.getElementById("toValue").value;
//	var includedActions = document.getElementById("includedActions").value;
	
	var includedActions="";
	var selObj = document.getElementById("includedActions");
	for (var i=0; i<selObj.options.length; i++) {
	    selObj.options[i].selected = true;
	    includedActions = includedActions + ","+selObj.options[i].value;
	}
	
	
	var allvalues= fromValue+'&' + toValue + '&' + includedActions;
	
//	alert("returnedValue: " + allvalues);
	
	window.returnValue = allvalues;
	window.close();
	return false;
}


function selectAllOptions(selStr){
	
  var selObj = document.getElementById(selStr);
  for (var i=0; i<selObj.options.length; i++) {
    selObj.options[i].selected = true;
  }
}

