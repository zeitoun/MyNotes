
function NumberUtil() {
	/************************************* Functions ***************************/
	this.doFilterNumber = doFilterNumber;
	this.doFilterNumberWithoutDot = doFilterNumberWithoutDot;
	this.doFilterFloat = doFilterFloat;
	this.isInteger = isInteger;
	this.isDecimal = isDecimal;
	this.isNonnegativeInteger = isNonnegativeInteger;
	this.isNegativeInteger = isNegativeInteger;
	this.isSignedInteger = isSignedInteger;
	this.unformatNumber = unformatNumber;
	this.doFilterNumberSpecific = doFilterNumberSpecific;
	/*************************************** End  ******************************/

// Returns true if all characters in string s are numbers.
// Accepts non-signed integers only. Does not accept floating
// point, exponential notation, etc.
//
// EXAMPLE FUNCTION CALL:     RESULT:
// isInteger ("5")            true
// isInteger ("")             false
// isInteger ("-5")           false
	function isInteger(s) {
		var reInteger = /^\d+$/;
		if (isEmpty(s)) {
			return (isInteger.arguments.length == 1) ? false : (isInteger.arguments[1] == true);
		}
		return reInteger.test(s);
	}
	
	//Tests if s is a decimal
	function isDecimal(s) {
		var reFloat = /^((\d+(\.\d*)?)|((\d*\.)?\d+))$/;
		if (isEmpty(s)) {
			return (isDecimal.arguments.length == 1) ? false : (isDecimal.arguments[1] == true);
		}
		return reFloat.test(s);
	}



	//Returns true if all characters are numbers;
	//first character is allowed to be + or - as well.
	function isSignedInteger(s) {
		var reSignedInteger = /^(\+|-)?\d+$/;
		if (isEmpty(s)) {
			return (isSignedInteger.arguments.length == 1) ? false : (isSignedInteger.arguments[1] == true);
		} else {
			return reSignedInteger.test(s);
		}
	}

//Returns true if string s is an integer >= 0.
	function isNonnegativeInteger(s) {
		var secondArg = false;
		if (isNonnegativeInteger.arguments.length > 1) {
			secondArg = isNonnegativeInteger.arguments[1];
		}
		return (isSignedInteger(s, secondArg) && ((isEmpty(s) && secondArg) || (parseInt(s) >= 0)));
	}

// Returns true if string s is an integer < 0.
	function isNegativeInteger(s) {   
    // a) s must be a signed integer, AND
    // b) one of the following must be true:
    //    i)  s is empty
    //    ii) this is a negative, not positive, number
		return (isSignedInteger(s) && ((isEmpty(s)) || (parseInt(s) < 0)));
	}

	//Check whether string s is empty.
	function isEmpty(s) {
		return ((s == null) || (s.length == 0));
	}
	
	function doFilterNumber(evt) {
		var charCode = (evt.which) ? evt.which : evt.keyCode;
		if ((charCode == 8) || (charCode == 9) || (charCode == 46) || (charCode > 47 && charCode < 58 ) || (charCode == 37) || (charCode == 39)) {
			return true;
		}
		return false;
	}
	
	function doFilterNumberWithoutDot(evt) {
		var charCode = (evt.which) ? evt.which : evt.keyCode;
		if ((charCode == 8) || (charCode == 9) || (charCode > 47 && charCode < 58)) {
			return true;
		}
		return false;
	}
	
	function doFilterFloat(evt) {
		var charCode = (evt.which) ? evt.which : evt.keyCode;
		if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46 && charCode != 45) {
			return false;
		}
		return true;
	}
	
	function unformatNumber(val) {
		newVal = val + "";
		if (newVal.indexOf(",") != -1) {
			newVal = newVal.replace(/\,/g, "");
		}
		return newVal;
	}
	
	// Should be called onkeydown event not onkeypress
	// if integerPart is '' or null don't check it
	function doFilterNumberSpecific(control, e, integerPart, decimalPart) {    	
		var evt;
	    if( window.event) 
	    	evt = event;    	
	    else if(e) 
	    	evt = e;
	    
	    var charCode = (evt.which) ? evt.which : evt.keyCode;     	     	
	    if (charCode==110 || charCode==190){
	    	if (document.selection){ 					// For IE
				var selectionRange = document.selection.createRange();
				selectionRange.moveStart ('character', -control.value.length);
				curCaretPos = selectionRange.text.length;
				if( curCaretPos == 0)
					return false;
	        } else if ( control.selectionStart == 0) { //For FF
	        	return false;
	        }
			
			var patt1=new RegExp("\\.");
			var ch =patt1.exec(control.value);
			
			if(ch=="."){  
				return false;
			}else{
				var iCaretPos = getCaretPosition(control);
				var beforePoint = control.value.substring(0,iCaretPos);
				var afterPoint = control.value.substring(iCaretPos+1);			                
	                
				if(afterPoint.length >= decimalPart){			                	
					return false;                	
				}else if (integerPart != null && integerPart != ''){
					if(beforePoint.length > integerPart){
				    	return false;
					}
				}
				            
			}
				            
	    } else if( (charCode>=48 && charCode<=57)  || (charCode>=96 && charCode<=105)|| charCode==8 || charCode == 46 || charCode == 9){//Numbers or BackSpace  or Delete or Tab
	                
	    	if (control.value.indexOf('.') != -1){//. Exisist in TextBox 
	            
	        	var pointIndex=control.value.indexOf('.');
	            var beforePoint = control.value.substring(0,pointIndex);
	            var afterPoint = control.value.substring(pointIndex+1);
	                
	            var iCaretPos = getCaretPosition(control);
				
				if (integerPart != null && integerPart != ''){
					if (charCode == 8 && ( iCaretPos - 1) == pointIndex  && (( beforePoint.length + afterPoint.length) > integerPart))    
		            	return false
		            if (charCode == 46 &&  iCaretPos  == pointIndex  && (( beforePoint.length + afterPoint.length) > integerPart))                
		                	return false
				}
	                
	            if(iCaretPos > pointIndex && afterPoint.length >= decimalPart &&  charCode != 8 &&  charCode != 46 && charCode != 9 ){
					return false;                	
	            } else if (integerPart != null && integerPart != ''){
		        	if(iCaretPos <= pointIndex && beforePoint.length >= integerPart &&  charCode != 8 &&  charCode != 46 && charCode != 9){
						return false;
		            }
	            }
	        } else{ //. Not Exisist in TextBox
	            
	            if (integerPart != null && integerPart != ''){
		        	if(control.value.length >= integerPart  &&  charCode != 8 &&  charCode != 46 && charCode != 9){
						return false;
		            }
	            }
	        } 
	    } else {        	
	   		if ( charCode != 37 && charCode != 39 && charCode != 46 && charCode != 9 )
				return false;
	    }
	}
}