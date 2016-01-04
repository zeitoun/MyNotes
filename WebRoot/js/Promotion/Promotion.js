

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
	newwindow=window.open(url,'name','height=500,width=1000','resizable=no');
	if (window.focus) {newwindow.focus()}
	return false;
}