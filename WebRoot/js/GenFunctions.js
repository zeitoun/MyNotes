//-------------------Author : Ziad Saade --------------------
//---------------Company : Path-Solutions--------------------

//--------------------------Go to link----------------------------------
function goThere(cobj,toHeader)
{
       document.location =  toHeader+"?"+cobj.id;
}

/*
function goThere(cobj,toHeader)
{
	var windowURL = toHeader + "?" + cobj.id;	
	var windowDimen = 'status=no';
	window.open(windowURL,'',windowDimen);
	return false;
}
*/
//-------------To check if the list box is empty or not------------
function isEmptyListBox(LiBo,str,str2){
   var i=0;
   var j=0;
   if(LiBo.length == 0){
      alert(str2);
      return false;
   }
   
   for (i = 0; i < LiBo.length; i++){
      if (!LiBo.options[i].selected){
         j++;
	   }
     if(j== LiBo.length){
	   alert(str);
	   LiBo.focus();
	   return false;
    }	
  }
}

//-------------to validate an empty textbox------------------
function isFull(s){
if (s==""){
	return false;
	}
else
    {
	return true;
	}
}

//----------when drop down change show Related Div-----------
//indnum ditermine the id of the array div to show and hide----
function showForm(index,indnum){

frms=document.getElementsByTagName('div');
var z=new Array(frms.length);
var j = 0;

for (i=0;i<frms.length;i++){
	if (frms[i].id == indnum)
		{
			z[j] = frms[i];
			j++;
		}
	}

for (i=0; i<j;i++)
	{
		z[i].style.display='none';
		z[index].style.display='block';
	}

}
//-----------------------------------------------------------
//--------------Validate DropDown Box if Selected------------
function isSelected(s){
	if (s=="-1")
		{
			return false;
		}
	else
    	{
			return true;
		}
}
//---------Validate DropDown Box if Selected with message------------
function isSelected2(s,str){
	if (s.value =="-1")
		{ 
            alert(str);
			s.focus();			
			return false;
		}
	else
    	{
			return true;
		}
}

//-----------------------Validate Float----------------------
function isDec(str)
{
	var i;
	var l;
	//function isDec(str1)
	//alert("str1 =" + str1);
	//var str = str1.toString().replace(/\$|\,/g,'');
	//alert("str =" + str);
	l=str.length;
	if (l == 0)
	{
		return false;
	}
	else
	{
    	for (i = 0; i < str.length; i++)
		{   
        	// Check that current character is number.
        	var c = str.charAt(i);
        	if (((c < "0") || (c>"9")) && ( c!= "."))
			{ 
				return false;
			}
		}
    }
	//-------------------test if there is more then one Point----------------------------
	var n=0;
	for (i = 0; i < str.length; i++)
		{   
        	var c1 = str.charAt(i);
        	if ( c1== ".")
			{ 
				n++;
			}
		}
    
	if (n >1){
		return false;
	}	

	// All characters are numbers.
    return true;
}
//----------------------Validate Integer with decimal point-----------------
function isInteger1(s){
	PtPos = s.indexOf(".") + 1
	//alert("PtPos = "  +  PtPos);
	if(PtPos == 0){//if there's no decimal point in number
	
	   for (i = 0; i < s.length; i++){   
           // Check that current character is number.
           var c = s.charAt(i);
           if (((c < "0") || (c > "9"))) return false;
       }
      // All characters are numbers.
       return true;
	}
	else{
		
		StrBfPt = s.substring(PtPos); //get the string before the point
		StrAfPt = s.substring(0, PtPos - 1); // get the string after the point without the point And Minus Signe
		
		//alert("StrAfPt = " + StrAfPt );
		 for (i = 0; i < StrAfPt.length; i++){   
           // Check that current character is number.
           var c = StrAfPt.charAt(i);
           if (((c < "0") || (c > "9"))) return false;
        }
        // All characters are numbers.
	   
	   //alert("StrBfPt = " + StrBfPt );
		for (i = 0; i < StrBfPt.length; i++){   
           // Check that current character is number.
           var c = StrBfPt.charAt(i);
           if (c!=0) return false;
        }
        // All characters are numbers.
		
		
		return true;
	}

}

//----------------Validate Date and Validate Integer----------
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=2100;

function isInteger(s){
	var i;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
		if (i==2) {this[i] = 29}
   } 
   return this
}

function isDate(dtStr){
	var daysInMonth = DaysArray(12)
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strDay=dtStr.substring(0,pos1)
	var strMonth=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		alert(translate('theDateFormatShouldBeDMY'));
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		alert(translate('pleaseEnterAValidMonth'));
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		alert(translate('pleaseEnterAValidDay'));;
		return false
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		alert(translate('pleaseEnterAValid4DigitYearBetween') +" "+minYear +" "+translate('and') +" "+maxYear);
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		alert(translate('pleaseEnterAValidDate'));
		return false
	}
return true
}


function isDateForDiff(dtStr){
	var daysInMonth = DaysArray(12)
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strDay=dtStr.substring(0,pos1)
	var strMonth=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		//alert("The date format should be : dd/mm/yyyy")
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		//alert("Please enter a valid month")
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		//alert("Please enter a valid day")
		return false
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		//alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		//aler t("Please enter a valid date")
		return false
	}
return true
}

//---------------------------------mm/dd/yyyy----------------------------------------
/*function isDate(dtStr){
	var daysInMonth = DaysArray(12)
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strMonth=dtStr.substring(0,pos1)
	var strDay=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		aler t("The date format should be : mm/dd/yyyy")
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		aler t("Please enter a valid month")
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		aler t("Please enter a valid day")
		return false
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		aler t("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		aler t("Please enter a valid date")
		return false
	}
return true
}
*/



//----------------validate number between min value and max value-------------

function isMinMax(val,min,max){
	
	if (val < min || val >max)
	{
		return false;
	}
return true;
}
//-----------------------function for round--------------
function GetNumForRound(cy_dec){
	var i;
	var res = 1;
	for(i=0; i < cy_dec; i++ )
	{res = res * 10;}
	return res;
}
//-------------------------------------------
function truncate(x)
{
  return x < 0 ? Math.ceil(x) : Math.floor(x)
}
//---------------------------Format Currency--------------------------------
function fromat_Currency(InDbl,DecPoint) {
    var OutStr = "";
    var PtPos; //Point Position in the Input Number
    var MinPos; //Minus Sign Position
    var StrBfPt; //String before the point
    var StrAfPt; //String after the point
    var LenStrAfPt; //lenth of the string after the point
    var Buf; //Buf
    var Counter; //counter
    var StrNum;

	
	//Round the object in other way
    //BigDecimal rnd_InDbl = new BigDecimal(InDbl);
    //rnd_InDbl = rnd_InDbl.setScale(DecPoint, BigDecimal.ROUND_HALF_UP);
	
	if(isNaN(InDbl)){
	   InDbl = InDbl.toString().replace(/\$|\,/g,'');
	   if(isNaN(InDbl)){
       }
	   else{
	      InDbl = Math.round(InDbl * Math.pow(10,DecPoint)) / Math.pow(10,DecPoint);
	   }
	}
	else{
	   InDbl = Math.round(InDbl * Math.pow(10,DecPoint)) / Math.pow(10,DecPoint);
	}
	
	//alert("InDbl = " + InDbl);
	
	var InStr = InDbl.toString().replace(/\$|\,/g,'');
	//alert("InStr = " + InStr);
	if(isNaN(InStr))InStr = "0";
	
	PtPos = InStr.indexOf(".") + 1; // get the positon of the point
	
	if (PtPos == 0){
		InStr = InStr + ".0";
		PtPos = InStr.indexOf(".") + 1;
	}
    
	StrBfPt = InStr.substring(PtPos - 1); //get the string before the point
    //alert("StrBfPt = " + StrBfPt);
	//set OutStr = strBfpt;

    MinPos = InStr.indexOf('-'); //get the position of the  - signe
	//alert("MinPos = " + MinPos);
	
    if (MinPos == 0) {
      StrAfPt = InStr.substring(1, PtPos - 1); // get the string after the point without the point And Minus Signe
    }
    else {
      StrAfPt = InStr.substring(0, PtPos - 1); // get the string after the point without the point And Minus Signe
    }
	//alert("StrAfPt = " + StrAfPt);

    LenStrAfPt = StrAfPt.length; //get the length of the string afthe the point
	//alert("LenStrAfPt = " + LenStrAfPt);
    
	StrNum = LenStrAfPt / 3;
    //alert("StrNum =  LenStrAfPt / 3 = " + StrNum);
	Counter = 0;

    if (LenStrAfPt == 0) {
      StrAfPt = "0";
      OutStr = StrAfPt + StrBfPt;
    }
    else if (LenStrAfPt == 1 || LenStrAfPt == 2 || LenStrAfPt == 3) {
      OutStr = StrAfPt + StrBfPt;
    }
    else {

      if (LenStrAfPt % 3 == 0) {
        while (Counter < StrNum) {
          Buf = StrAfPt.substring(Counter * 3, (Counter * 3) + 3);
          OutStr = OutStr + ',' + Buf;
          Counter++;
        }
      }
      else {

        //StrNum = StrNum + 1;

        while (Counter < StrNum) {
          if (Counter == 0) {
            Buf = StrAfPt.substring(0, LenStrAfPt % 3);
          }
          else {
            Buf = StrAfPt.substring( (LenStrAfPt % 3) + (Counter - 1) * 3,
                                    ( (LenStrAfPt % 3) + (Counter - 1) * 3) + 3);
          }
          OutStr = OutStr + ',' + Buf;
          Counter = Counter + 1;
        }

      }

      OutStr = OutStr + StrBfPt;
      OutStr = OutStr.substring(1);
    }

    if (MinPos == 0) {
      OutStr = '-' + OutStr;
    }

    return OutStr;
  }
 //onBlur="this.value=fromat_Currency(this.value,2);"
 
 
//-----Get part of a StringTokenizer with - depending on the number Start count by 0------------
function getStringToken(str,num){
   //get the number of - in ther String
  str = str + "-";
   var j=0; //number of - in the string
   
   for (i = 0; i < str.length; i++){   
      var c = str.charAt(i);
      if (c=="-"){ 
         j++;
      }//end if
   }//end for
   
   //alert("input String = " + str);
   //alert("number of - in the input string = " + j);

   var StrArray=[];//string of arrays
   var f;//index of -

   //fill the array of strings
   for (i=0;i <j;i++){
      f=str.indexOf("-");
	  //alert("f = " + f);
	
	  StrArray[i] = str.substring(0,f);
	  //alert("StrArray["+ i +"] = " + StrArray[i]);
	
	  str=str.substring(f+1);	
  	  //alert("str = " + str);
  }

//alert("num = " + num);
//alert("StrArray[num] = " + StrArray[num]);

return  StrArray[num];
}//end function
//alert("f =" + getStringToken(str,num));
//
//------------------------------------DATE DIFF----------------------------------

function handleEncryptedURL(formName, destination, parameters) {
	formName.action = destination + "?" + parameters;
	formName.method = "post";
	formName.submit();	
}

function textCounter(field, maxlimit, hidfield, mainFiled) {// myust have a hidden field 
	
	var x = document.getElementById(field);
	if(x.value.length > maxlimit){
		x.focus();
		alert(translate('youHaveReachTheMaximumLenghtAllowed'));
//		alert(translate('maxLengthOfThe') + ' ' + translate(mainFiled) + ' ' + translate('FieldIs')+ ' ' +maxlimit);
		document.getElementById(field).value = x.value.substr(0,maxlimit)
		
	}
	document.getElementById(hidfield).value = x.value.length;
}









