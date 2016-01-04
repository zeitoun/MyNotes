function DateUtil()
{
	
 /************************************* Functions ***************************/
 this.isDate = isDate;
 this.getWeek = getWeek;
 this.dateAdd = dateAdd;
 this.stringToDate = stringToDate;
 this.dateToString = dateToString;
 this.compareDates = compareDates;
 this.dateToObjDate = dateToObjDate;
 this.getDateDifference = getDateDifference;
 this.getNbDaysInFeb = getNbDaysInFeb;
 this.getNbDaysInMonth = getNbDaysInMonth;
 this.getPreviousDay = getPreviousDay;
 this.getNextDay = getNextDay;
 this.isValidDay = isValidDay;
 /************************************* End Functions ***************************/

//Get the week-number based on a certain date
//In Javascript the months are 0 to 11, for ex. january = 0 .... desember = 11
function getWeek(year,month,day)
{
    //Find JulianDay 
    month += 1; //use 1-12
    var a = Math.floor((14-(month))/12);
    var y = year+4800-a;
    var m = (month)+(12*a)-3;
    var jd = day + Math.floor(((153*m)+2)/5) + 
                 (365*y) + Math.floor(y/4) - Math.floor(y/100) + 
                 Math.floor(y/400) - 32045;      // (gregorian calendar)
    
    //now calc weeknumber according to JD
    var d4 = (jd+31741-(jd%7))%146097%36524%1461;
    var L = Math.floor(d4/1460);
    var d1 = ((d4-L)%365)+L;
    NumberOfWeek = Math.floor(d1/7) + 1;
    return NumberOfWeek;        
}

//Add a number to a date and returns the date in the pattern format
//interval is either 'year','month','day','hour','minute','second'
//number is the number to add
//dt is the first date
//pattern is the pattern of the date, by default it's 'd/m/y'
function dateAdd(interval,number,dt,pattern){//dateAdd('year',2.25,'1/1/2002 12:00')
	var dateInstance = new getDateUtil();
	if(!interval||!number||!dt) return;	
	if(pattern) pattern='d/m/y';
	var s=1,m=1,h=1,dd=1,i=interval;
	if(i=='month'||i=='year'){
		dt = this.stringToDate(dt,pattern);
		dt=new Date(dt);
		if(i=='month') {
			var toMonth = dt.getMonth()+ number ;
			var toDay =  dt.getDate();
			var toYear = dt.getFullYear() + parseInt(number/12);
			if( (toDay > dateInstance.getNbDaysInMonth((toMonth%12) + 1, toYear)) || (dt.getMonth() == 1 && toDay == dateInstance.getNbDaysInMonth(dt.getMonth()+1, toYear))){				
				var day = dateInstance.getNbDaysInMonth((toMonth%12) +1, toYear);
				dt.setMonth(toMonth,parseInt(day));
			}else{
				dt.setMonth(dt.getMonth()+number);
			}
		}
		if(i=='year') dt.setFullYear(dt.getFullYear()+number);		
	}else if (i=='second'||i=='minute'||i=='hour'||i=='day'){
		dt = this.stringToDate(dt,pattern);
		dt=Date.parse(dt);
		if(isNaN(dt)) return;
		if(i=='second') s=number;
		if(i=='minute'){s=60;m=number}
		if(i=='hour'){s=60;m=60;h=number};
		if(i=='day'){s=60;m=60;h=24;dd=number};
		dt+=((((1000*s)*m)*h)*dd);
		dt=new Date(dt);
	}
	return this.dateToString(dt,pattern);
}

//Convert a string to a date object
//The separator of the pattern can be / or -
//Pattern contains the letters D = Day, M = Month, Y = Year
function stringToDate(dateString,datePattern)
{
  var frmSrc, day, month, year, rg0, dateArray, errorMsg;
  datePattern =(datePattern)? datePattern : "D/M/Y";
  
  dateString = dateString.replace(new RegExp("([/-][0-9])$"),"$1-");
  rg0 = new RegExp("[/-]","g");
  frmSrc = "^"+((datePattern)?datePattern.toUpperCase():datePattern)+"$";
  frmSrc = frmSrc.replace(rg0,"");
  
  day = frmSrc.indexOf("D");
  month = frmSrc.indexOf("M");
  year = frmSrc.indexOf("Y");
  
  
  if(day<1 || day>3 || month<1 || month>3 || year<1 || year>3) 
  {
  	errorMsg = "Error : Invalid pattern"
  }
  else 
  {
  	var y = "", m = "", d = "", splitDateArray;
    //Checking the separator of the pattern
    if(datePattern.indexOf("/") > -1)
    {
  	  if(dateString.indexOf("/") > -1)
  	  {
  	    splitDateArray = dateString.split("/");
  	    y = splitDateArray[2]; m = splitDateArray[1]; d = splitDateArray[0];
  	  }
  	  else
  	  {
  		errorMsg = "Error : Date didn't match the specified pattern";
  		return errorMsg;
  	  }
    }
    else if (datePattern.indexOf("-") > -1)
    { 
  	  if(dateString.indexOf("-") > -1)
  	  {
  	    splitDateArray = dateString.split("-");
  	    y = splitDateArray[2]; m = splitDateArray[1]; d = splitDateArray[0];
  	  }
  	  else
  	  {
  		errorMsg = "Error : Date didn't match the specified pattern";
  		return errorMsg;
  	  }  	
    }
   
   if((y.length < 1) && (m.length < 1) && (d.length < 1))
   {
   	errorMsg = "Error : Invalid date";
   	return errorMsg;
   } 
   
   if(!isValidYear(y)|| (y.length < 1) || (y.length > 4))  
   {
   	errorMsg = "Error : Invalid year";
   	return errorMsg;
   }
  
   if(!isValidMonth(m) || (m.length < 1) || (m.length > 2)) 
   {
  	errorMsg =  "Error : Invalid month";
  	return errorMsg;
   }
  
   if(!isValidDay(d, m, y) || (d.length < 1) || (d.length > 2)) 
   {
   	errorMsg = "Error : Invalid day";
   	return errorMsg;
   }
   
   //if(dateArray[2] < 1000) { dateArray[2] += 2000; }
   date = new Date(y, m-1, d);
   if(isNaN(date))
   { 
   	errorMsg = "Error : Invalid date";
   	return errorMsg;
   }
  }
  return date;
}

//Convert a date object to a string
//The separator of the pattern can be / or -
//Pattern contains the letters D = Day, M = Month, Y = Year
function dateToString(dateObj, pattern)
{
  var stringDate, day,month,year;
  stringDate = ((pattern)?pattern.toUpperCase():"D/M/Y");
  if(!dateObj) return "";
  day = dateObj.getDate();
  month = dateObj.getMonth()+1;
  year = dateObj.getFullYear();
  
  stringDate = stringDate.replace("D",((day<10)?"0":"")+day);
  stringDate = stringDate.replace("M",((month<10)?"0":"")+month);
  stringDate = stringDate.replace("Y",""+year);
  return stringDate;
}

//Check if this is a valid date
//If valide date returns true, otherwise return the errorMsg
function isDate(dateString, patternDate)
{
  var date = stringToDate(dateString, patternDate);
  var isError = date.toString().indexOf("Error");
  return (isError != "-1") ? false : true; 
}

//Compare 2 object Dates 
//Returns 0 if they are equals 
//Returns -1 if the first Date is greater than the second Date
//Returns 1 if the first Date is less than second Date
function compareDates(firstDate,secondDate)
{
  var firstDateObj = firstDate.getTime();
  var secondDateObj = secondDate.getTime();
  
  if(firstDateObj ==  secondDateObj)
  {
   return 0;
  }
  else if(firstDateObj < secondDateObj)
  {
   return 1;
  }
  else if(firstDateObj > secondDateObj)
  {
    return -1;
  }
}

//returns the differnce in days between 2 string dates
function getDateDifference(dateFrom,dateTo){
	var dateIns = new getDateUtil();
	if(!dateIns.isDate(dateFrom,"d/m/y")) {
		return false;
	}
	if(!dateIns.isDate(dateTo,"d/m/y")){
		return false;
	}
	
	dateFrom=dateIns.stringToDate(dateFrom,"d/m/y");
	dateTo=dateIns.stringToDate(dateTo,"d/m/y");	
	if (compareDates(dateFrom,dateTo) == -1){
		return false;
	}
	difference = dateTo-dateFrom;
	difference = ((((difference/60000)/60)/24)/1);
	
	return parseInt(Math.round(difference));
}

//February has 29 days in any year evenly divisible by four,
//EXCEPT for centurial years which are not also divisible by 400.
function getNbDaysInFeb(year)
{
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}

//Returns number of days in month
//If it's an INVALID month returns -1
function getNbDaysInMonth(month, year) 
{
  var nbDays = -1;
  if(month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12)
  {
  	 nbDays = 31;
  }
  else if (month==2) 
  {
  	nbDays = (year)?getNbDaysInFeb(year): 29;
  }
  else if (month==4 || month==6 || month==9 || month==11) 
  {
	nbDays = 30
  }
  return nbDays;
}

//Get current date and return previous date as String
function getPreviousDay(objDate)
{
  var prevDateObj   = new Date(objDate.getTime() - 86400000); // to get the date of the previous Day
  //prevDate = new Date(prevDateObj.getFullYear(), prevDateObj.getMonth(), prevDateObj.getDate());
  prevDate = prevDateObj.getDate()+"/"+parseInt(prevDateObj.getMonth()+1)+"/"+prevDateObj.getFullYear();
  return prevDate;
}

//Get current date and return next date as String
function getNextDay(objDate, nbDays)
{
	if(nbDays == null) nbDays = 1;
  var nextDateObj   = new Date(objDate.getTime() + (86400000*nbdays)); // to get the date of the next Day
  nextDate = nextDateObj.getDate()+"/"+parseInt(nextDateObj.getMonth()+1)+"/"+nextDateObj.getYear();
  return nextDate;
} 

//Check if the given text is an numeric value 
//and is included between minLimit and maxLimit
function isIntegerInRange (s, minLimit, maxLimit)
{   
  if ((s == null) || (s.length == 0)) { return false }
  // Catch non-integer strings to avoid creating a NaN below,
  var reInteger = /^\d+$/
  //If non integer value entered
  if(!reInteger.test(s)) {return false }
  //var num = parseInt(s);
  var num = parseInt(eval(s));
  return ((num >= minLimit) && (num <= maxLimit));
}

//Boolean function to check if the given month is a valid
function isValidMonth(month)
{ 
 return (!isIntegerInRange(month, 1, 12)) ? false : true; 
}

//Boolean function to check if a given year is valid
//
function isValidYear(year)
{ 
  return (!isIntegerInRange(year, 1, 9999)) ? false : true;
}

//Check if the given day is valid
//Returns true if the day is valid, otherwise returns false
function isValidDay(day, month, year)
{ 
 return (!isIntegerInRange(day,1, getNbDaysInMonth(month, year))) ? false : true;
}

//replacing the date to object 
function dateToObjDate(theDate)
{
  dateSplit1 = new Array();
  dateSplit1 = theDate.split("/");
  theDateObj = new Date(dateSplit1[2], dateSplit1[1]-1, dateSplit1[0]);
  return theDateObj;
}
}