 //these 3 functions are used for the ticker component
function incrementNumber(zValue, scale) {
	/*document.getElementById(zValue.id).onchange();
  document.getElementById(zValue.id).onblur();
  document.getElementById(zValue.id).onkeypress();*/
	var tickerId = zValue.getAttribute("tickerId");
	if (document.getElementById(tickerId).readOnly == false) {
		document.getElementById(tickerId).value = eval(document.getElementById(tickerId).value) + scale;
	}
}

function decrementNumber(zValue, scale) {
	/*document.getElementById(zValue.id).onchange();
   document.getElementById(zValue.id).onblur();
   document.getElementById(zValue.id).onkeypress();*/
	var tickerId = zValue.getAttribute("tickerId");
	if (document.getElementById(tickerId).value != scale && document.getElementById(tickerId).readOnly == false) {
		document.getElementById(tickerId).value = eval(document.getElementById(tickerId).value) - scale;
	}
}
 
//rounding for the numbers to a multiple of 0.25
function roundingNumbers(val) {
	val.value = 0.25 * Math.round(val.value / 0.25);
}

//Returns the current caret position in a text field 
function getCaretPosition(control) {
	var iCaretPos = 0;
	if (document.selection) { 					// For IE
		if (control.type == "text") { // textbox
			var selectionRange = document.selection.createRange();
			selectionRange.moveStart("character", -control.value.length);
			iCaretPos = selectionRange.text.length;
		}
	} else {
		if (window.getSelection()) {// For FireFox
			if (control.type == "text") { // textbox
				if (control.selectionStart || control.selectionStart == "0") {
					iCaretPos = control.selectionStart;
				}
			}
		}
	}
	return iCaretPos;
}