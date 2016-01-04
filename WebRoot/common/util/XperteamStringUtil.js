function StringUtil()
{
	
  /************************************* Functions ***************************/
  this.checkEmail = checkEmail;
  this.checkIP = checkIP;
  this.isString = isString;
  this.isStringWithNumNoSpace = isStringWithNumNoSpace;
  this.isStringWithNum = isStringWithNum;
  this.isStringAndNum = isStringAndNum;
  this.isUpperStringAndLowerStringAndNumAndSpecialCharacter = isUpperStringAndLowerStringAndNumAndSpecialCharacter;
  this.isStringWithoutSpace = isStringWithoutSpace;
  this.replaceString = replaceString;
  this.trim = trim;
  this.lTrim = lTrim;
  this.rTrim = rTrim;
  this.isWhitespace = isWhitespace;
  /************************************* End Functions ***************************/
  //Check if the given email is in the proper email format.
  //If the email has proper format, will return true, otherwise will return false.
  function checkEmail(email)
  {
	if(email == "" || email == null) return true;
	//var emailRegularExp = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
	
	var emailRegularExp = /^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$/
	return emailRegularExp.test(email);
  }

  //Check the given IP is in the international IP
  //If the IP has proper format, will return true, otherwise will return false.
  function checkIP(ip)
  {
    if(ip == "" || ip == null) return true;
    var ipRegularExp = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
    return ipRegularExp.test(ip);
  }

  //Check if the given text just contains lower or upper character
  function isString(text)
  {
    var strRegularExp = /^[a-zA-Z ]*$/
    return strRegularExp.test(text);
  }
  
  //Check if the given text just contains lower or upper character, numbers and space
  function isStringWithNum(text)
  {
    var strRegularExp = /^[a-zA-Z0-9 ]*$/
    return strRegularExp.test(text);
  }

  //Check if the given text is number and character in the same time
  function isStringAndNum(text)
  {
  	var strRegularExp = /^[a-zA-Z]*$/
  	//true if strig with no space and false if String with space
    var strNoSpaceRegularExp = /^[a-zA-Z^<>(){}[\]\\.,;:@\"*!~#$%&-_=+`|]*$/;    
    var numRegularExp = /^[0-9]*$/;
    return !(strRegularExp.test(text) || numRegularExp.test(text) || !strNoSpaceRegularExp.test(text)); 	    
  } 
  
  //Check if the given text contains at least one number , one upper case , one lower case ,one special character
 function isUpperStringAndLowerStringAndNumAndSpecialCharacter(text)
  {
  	var strRegularExp = /^[a-zA-Z0-9 ]*$/
  	var alphaWithSpecialCharacterRegularExp = /^[a-zA-Z<>(){}[\]\\.,;:@\"*!~#$%&-_=+`|]*$/
  	var UpperAlphaWithNumberWithSpecialCharacterRegularExp = /^[A-Z0-9<>(){}[\]\\.,;:@\"*!~#$%&-_=+`|]*$/
  	var LowerAlphaWithNumberWithSpecialCharacterRegularExp = /^[a-z0-9<>(){}[\]\\.,;:@\"*!~#$%&-=+`|]*$/
  	var UpperLowerAphaWithNumberWithSpecialCharacterWithoutSpace =  /^[a-zA-Z0-9\\<>(){}[\]\\.,;:@\"*!~#$%&-_=+`|]*$/
  	return (! strRegularExp.test(text) &&
  			! UpperAlphaWithNumberWithSpecialCharacterRegularExp.test(text) &&
  			! LowerAlphaWithNumberWithSpecialCharacterRegularExp.test(text) &&
  			  UpperLowerAphaWithNumberWithSpecialCharacterWithoutSpace.test(text)) 
  }   
  

  //Check if the given text just contains lower or upper character and numbers
  function isStringWithNumNoSpace(text)
  {  
    var strRegularExp = /^[a-zA-Z0-9]*$/
    return strRegularExp.test(text);
  }

  //Check if the given text didn't contain a blank space
  //If the given text contains blank space, will return false, otherwise will return true
  function isStringWithoutSpace(text)
  {
    var strRegularExp = /^[a-zA-Z0-9^<>(){}[\]\\.,;:@\"*!~#$%&-_=+`|]*$/
    return strRegularExp.test(text)
  }

  //Replaces all occurrences of a specified String in this instance, 
  //with another specified String.
  function replaceString(srcString, oldStr, newStr)
  {
   for (var i=0; i<srcString.length; i++)
   {
    if (srcString.substring(i,i+oldStr.length) == oldStr)
    {
      srcString = srcString.substring(0,i)+newStr+srcString.substring(i+oldStr.length,srcString.length)
    }
   }
   return srcString
  }

  //Strip whitespace from the beginning and end of a string
  function trim(text)
  {
    return lTrim(rTrim(text));
  }
  
  //Strip whitespace from the beginning of a string
  function lTrim(text)
  {
	for(var k = 0; k < text.length && isWhitespace(text.charAt(k)); k++);
	return text.substring(k, text.length);
  }

   //Strip whitespace from the end of a string 
  function rTrim(text)
  {
	for(var j=text.length-1; j>=0 && isWhitespace(text.charAt(j)) ; j--) ;
	return text.substring(0,j+1);
  }
  
  // Checks if the sent character is a whitespace
  function isWhitespace(charToCheck) 
  {
	var whitespaceChars = " \t\n\r\f";
	return (whitespaceChars.indexOf(charToCheck) != -1);
  }
  
  // function to encode the URL which may contain special character as  + or /*/
  function encodeURL(url)
  {
	var encodedInputString = escape(url);
	encodedInputString = encodedInputString.replace("*", "%2A");
	encodedInputString = encodedInputString.replace("+", "%2B");
	encodedInputString = encodedInputString.replace("-", "%2D");
	encodedInputString = encodedInputString.replace("/", "%2F");
	encodedInputString = encodedInputString.replace(".", "%2E");
	encodedInputString = encodedInputString.replace("_", "%5F");
	encodedInputString = encodedInputString.replace("@", "%40");
	  
	return encodedInputString;
   }
}