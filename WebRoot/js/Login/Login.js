// enterOnce is a counter used to check if the user presses "enter" 
// the process won't be disrupted
var enterOnce = 0;

function setDropDownData(selectId, selectHiddenId) {

	var selectObject = document.getElementById(selectId);
	document.getElementById(selectHiddenId).value = selectObject.options[selectObject.selectedIndex].innerHTML;
}

function ValidateForm(){
	/*if(enterOnce!=0)
		return false;
	
	enterOnce ++;
	var userID = document.forms[0].userId;
	var userPassword = document.forms[0].password;
	
	
	enterOnce = 0;*/
	
	return true;
}

function submitForm(f, action) {
	var f1 = eval(f);
	f1.method = "post";
	f1.actionType.value = action;
	f1.submit();
}

function popitup(url) {
	newwindow = window
			.open(url, 'name', 'height=500,width=500', 'resizable=no');
	if (window.focus) {
		newwindow.focus()
	}
	return false;
}

function submitEnter(E) {
	var keyCode;
	if (E.keyCode)
		keyCode = E.keyCode;
	else if (E.which)
		keyCode = E.which;
	else
		return true;
	if (keyCode == 13) {
		
		//if (enterOnce == 0)
			
		// if(ValidateForm()){
		// because it is already called from the ValidateForm()
		 submitForm(document.reqForm, 'login');
		 return true;
		// }
		// return false;
	} else
		return true;
}