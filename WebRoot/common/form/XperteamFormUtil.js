function XperteamFormUtil(formId)
{

 /************************************* Variables ***************************/
  this.formId = formId;
  this.ownerDoc = document;
  this.formObj = this.ownerDoc.getElementById(this.formId);
  this.validationType = null;
  
  
 /************************************* Functions ***************************/
  this.disableField = disableField;
  this.disableForm = disableForm;
  this.setFieldValue = setFieldValue;
  this.doCustomFilter = doCustomFilter;
  this.doSecurityFilter = doSecurityFilter;
  this.checkRequired = checkRequired;
  this.setFieldRequired = setFieldRequired;
  this.populateDropDown=populateDropDown;
  this.doFilterString = doFilterString;  
  this.doSecurityFilterOnTextarea = doSecurityFilterOnTextarea;
  this.doLowSecurityFilter = doLowSecurityFilter;
  this.dragAndDropNumEvt = dragAndDropNumEvt;
  this.dragAndDropLowSecurityEvt = dragAndDropLowSecurityEvt;
  this.dragAndDropSecurityEvt = dragAndDropSecurityEvt;
  this.dragAndDropTxtAreaSecurityEvt = dragAndDropTxtAreaSecurityEvt;
  this.testSpecialCharater = testSpecialCharater;
  this.disableFormButtons=disableFormButtons;
  this.selectMoveRows=selectMoveRows;
  this.selectedOptions=selectedOptions;
  this.isLowSecurityFiltered=isLowSecurityFiltered;
  
//Disable or enable a field
//If status is equal to true, it will disable the specified field
//If status is equal to false, it will enable the specified field
function disableField(fieldObj,status)
{
    fieldObj = this.ownerDoc.getElementById(fieldObj);
    var eltTag = fieldObj.tagName;
    var eltType = fieldObj.type;

    if (eltTag == "INPUT" && 
          (eltType == "" || eltType == "text" || eltType == "hidden" || eltType == "password"))
    {
        fieldObj.readOnly = status;
         
    }
   else if (eltTag == "INPUT" && eltType == "checkbox")
   {
     fieldObj.disabled = status;
   }
   else if (eltTag == "INPUT" && eltType == "file")
   {
     fieldObj.readOnly = status;
     
   }
   else if(eltTag == "INPUT" && eltType == "button")
   {
    	fieldObj.disabled = status;
   }
   else if(eltTag == "INPUT" && eltType == "submit")
   {
    	fieldObj.disabled = status;
   }      
   else if (eltTag == "INPUT" && eltType == "radio")
   {
   	 var eltName = fieldObj.name;
     var radioLst = eval("this.formObj."+eltName);
     for(var r = 0 ; r < radioLst.length ; r++)
     {
       radioLst[r].disabled = status;
     }
   }
   else if (eltTag == "TEXTAREA")
   {
     fieldObj.readOnly = status;
   }
   else if (eltTag == "SELECT")
   {
     fieldObj.disabled = status;
   }
}

//Disable or enable a form
//If status is equla to true, it will disable the specified form
//If status is equal to false, it will enable the specified form
function disableForm(status)
{
  var formElts = this.formObj.elements;
  for(var i = 0 ; i < formElts.length ; i++)
  {
    eltObj = formElts[i];
    this.disableField(eltObj.id,status);
  }
}

//Set the value of a given field(input, checkbox, radio, textarea, combo,...)
function setFieldValue(eltName,theVal,fireEvent)
{
  var theId = null;
  if(typeof(theVal) == "string") theVal = theVal;
  else if(typeof(theVal) == "object") 
  {
    theId = theVal.id;
    theVal = theVal.value;
  }

  var theReturnObj = new Object();
  var theForm = this.formObj;
  var formElts = theForm.elements;
 
  for(var i = 0 ; i < formElts.length ; i++)
  {
    if(formElts[i].id == eltName)
    {
  	  curEltObj = formElts[i];
      if(!fireEvent)fireEvent = false;
	  if(!curEltObj)
	   {
		  alert("ERROR : Form element ["+eltName+"] not found")
 		  return;
	   }
  	  var eltTag = curEltObj.tagName;
  	  var eltType = curEltObj.type;
  	   var eltName = curEltObj.name;
  	  var oldVal = null;
      if (eltTag == "INPUT" &&
      (eltType == "text" || eltType == "hidden" || eltType == "password"))
	  {
	    curEltObj.value = theVal;
        if(fireEvent)
        {
          curEltObj.onchange();
        }
	  }
	  else if (eltTag == "INPUT" && eltType == "checkbox")
      {
       oldVal = curEltObj.checked
       if(oldVal == eval(theVal))return;
       curEltObj.checked = eval(theVal);
       if(fireEvent)
       {
         curEltObj.onchange();
       }
      }
	  else if (eltTag == "INPUT" && eltType == "radio")
	  {
	   var radioLst = eval("this.formObj."+eltName);
	   var changed = false;
	   for(var r = 0 ; r < radioLst.length ; r++)
	   {
	     if(radioLst[r].id == theVal && !radioLst[r].checked)
	     {
	      radioLst[r].checked = true;
	      changed = true;
	      break;
	     }
	     else
	     { 
	     	radioLst[r].checked = false;
	     }
	   }
       if(changed && fireEvent)
       	curEltObj.onchange();
      }
      else if (eltTag == "TEXTAREA")
      {
       //oldVal = curEltObj.innerText;
       oldVal = curEltObj.value;
       if(oldVal == theVal)return;
       //curEltObj.innerText = theVal
       curEltObj.value = theVal
       if(fireEvent)
       	curEltObj.onchange() ;
      }
      else if (eltTag == "SELECT")
      {
        if(curEltObj.selectedIndex == -1) oldVal = "";
        else oldVal = curEltObj.options[curEltObj.selectedIndex].getAttribute("value");
        if(oldVal == theVal)return;
        if(theVal == "")
        {
          curEltObj.selectedIndex = -1;
          if(fireEvent)
          	curEltObj.onchange();
          return;
        }
        
	    for (var i=0 ; i < curEltObj.options.length ; i++)
	    {
	      if (curEltObj.options[i].value == theVal)
	      {
	        curEltObj.options[i].selected = true;
	        if(fireEvent)
	        {
	         curEltObj.onchange();
	        }
		    break;
		  }
		}
	  }
      return;
	}
  }	
}

//Set a field required
function setFieldRequired(eltName,state)
{
  var formElts = this.formObj.elements;

 for(var i = 0 ; i < formElts.length ; i++)
 {
  if(formElts[i].id == eltName)
  {
   	formElts[i].setAttribute("required", state);
    break;
  }
 }
}

//Check all required fields in the form and alert the user of them.
//missingVal parameter is the message to be displayed before each missing field 
//If skipAlert parameter is set to true, this function didn't alert just return a boolean value
//If the attribute label of the required fied is not set, this function didn't alert of this field 
//It has two types of validation : 
//       1- Field by field (Alert the missing fields one by one)
//		 2- All the field in one alert
//The validation types are managed by the validationType parameter. 
//By default the validation type is set to Field by Field. 
//To validate all the field we just need to set validationType parameter to 'form'
//
function checkRequired(missingVal,skipAlert)
{
  var currentObj = null;
  var currentObjAtt = null;
  var eltName = null;
  var eltTag = null;
  var eltType = null;
  var required = null;
  var testValue = null;
  var label = null;
  var spaceRegExp = new RegExp(" ","g");
  var alerts = "";
  
  var theForm = this.formObj
  var formElts = theForm.elements;
//  if (missingVal == null || missingVal == '') missingVal = "Missing : ";
  
 for(var i = 0 ; i < formElts.length ; i++)
 {
	currentObj = formElts[i];
    currentObjAtt = currentObj.attributes;

    required = currentObjAtt.getNamedItem("required");
    required = (required && required.value == "true")?true:false;
    if(!required)continue;

    eltName = formElts[i].name;
    eltTag = formElts[i].tagName;
    eltType = formElts[i].type;

    if (eltTag == "INPUT" &&
        (eltType == "text" || eltType == "hidden" || eltType == "password"))
    {
     testValue = formElts[i].value.replace(spaceRegExp, "");
     if(testValue == "")
     {
      label = currentObjAtt.getNamedItem("label")
      if(label)
      {
        if(this.validationType == "form")
        {
          alerts += missingVal + " " + label.value + "\n";
        }
        else
        {
        	if(!currentObj.readOnly && !currentObj.disabled)currentObj.focus();
        	if(!skipAlert) 
        	{
        	  alert(missingVal + label.value);
        	}
            return true;
        }
      }
     }
    }
    else if (eltTag == "INPUT" && eltType == "checkbox")
    {
     testValue = currentObj.checked;
     if(!testValue)
     {
      label = currentObjAtt.getNamedItem("label")
      if(label)
      {
        if(this.validationType == "form")
        {
        	alerts += missingVal + " " + label.value + "\n";
        }
        else 
        {
         if(!currentObj.disabled)
         {
         	currentObj.focus();
         }
         if(!skipAlert) 
         {
         	alert(missingVal + label.value);
         }
         return true;
        }
      }
     }
    }
    else if (eltTag == "INPUT" && eltType == "file")
    {
         testValue = currentObj.value.replace(spaceRegExp, "");
         if(testValue == "")
         {
          label = currentObjAtt.getNamedItem("label");
          if(label)
          {
            if(this.validationType == "form")
            {
            	alerts += missingVal + " " + label.value + "\n";
            }
            else
            {
             if(!currentObj.readOnly && !currentObj.disabled)
             {
              currentObj.focus();
             }
             if(!skipAlert) 
             {
               alert(missingVal + label.value);
             }
             return true;
            }
          }
       }
    }
   else if (eltTag == "INPUT" && eltType == "radio")
   {
    testValue = currentObj.checked;
    var radio_choice = false;
    if(!testValue)
    {
        label = currentObjAtt.getNamedItem("label")
        var radioLst = eval("this.formObj."+eltName);
        if(label)
        {
	        if(this.validationType == "form")
	        { 	
				for (var counter = 0; counter < radioLst.length; counter++)
				{
					if (radioLst[counter].checked)
						radio_choice = true;
				}
			}
			if(!radio_choice)
			{
        		alerts += missingVal + " " + label.value + "\n";
        		//return true;
      		}
      		else 
      			null;
      			//return false;    	
        }
        else 
        {
            if(!currentObj.disabled)
			{
				currentObj.focus();
			}
			if(!skipAlert) 
			{
				alert(missingVal + label.value);
			}
			return true;
        }
    }
}
    else if (eltTag == "TEXTAREA")
    {
     //testValue = (currentObj.innerText)?testValue.replace(spaceRegExp,""):"";
     testValue = (currentObj.value)?currentObj.value.replace(spaceRegExp,""):"";
     if(testValue == "")
     {
      label = currentObjAtt.getNamedItem("label");
      if(label)
      {
       if(this.validationType == "form")
       {
       	alerts += missingVal + " " + label.value + "\n";
       }
       else
       {
         if(!currentObj.readOnly && !currentObj.disabled)
         {
          currentObj.focus();
         }
         if(!skipAlert) 
         {
         	alert(missingVal + label.value);
         }
         return true;
       }
      }
     }
    }
    else if (eltTag == "SELECT")
    {
     if(currentObj.selectedIndex == -1 || currentObj.value == 0)     
     {
       testValue = "";
      }
     else
     {
       //testValue = currentObj.options[currentObj.selectedIndex].innerText;
	   testValue = currentObj.options[currentObj.selectedIndex].innerHTML;
       testValue = (testValue)?testValue.replace(spaceRegExp,""):"";
     }
     if(testValue == "")
     {
      label = currentObjAtt.getNamedItem("label");
      if(label)
      {
        if(this.validationType == "form")
       	{
       		alerts += missingVal + " " + label.value + "\n";
       	}
       	else
       	{
          if(!currentObj.disabled)
          {
           currentObj.focus();
          }
          if(!skipAlert) 
          {
           alert(missingVal + label.value);
          }
          return true;
        }
      }
     }
   }
 }
 if(this.validationType == "form" && alerts != "")
 {
  if(!skipAlert) 
  {
   alert("Please enter mandatory fields:\n" + alerts);
  }
  return true;
 }
 return false;
}
//this function populate a drop down list from an xmlDocument
//the selectId is the id of the select element
//the elementTag is the name of the element in the xml document which contain the select html 
//the xmlDoc is the xmlDocument
//withFirstValueEmpty is boolean if true then it will generate drop down with empty value for the first one
//else it will not generate an empty value
//the xml format must be in this form
//<elementTag>
//<option value="optionvalue">displayContent</option>
//</elementTag>
function populateDropDown(selectId,elementTag,xmlDoc,withoutFirstValueEmpty){
	
	if(xmlDoc.xml != ""){
		var root=xmlDoc.getElementsByTagName(elementTag)[0];
		if(root!=null){
		var selectOptions=root.getElementsByTagName("option");
		var value,displayContent,selectContent;
		//reinitialise
		document.getElementById(selectId).options.length=0
		if(!withoutFirstValueEmpty) withoutFirstValueEmpty = false;
		if(withoutFirstValueEmpty == false)
			document.getElementById(selectId).options[0]=new Option("", -1, true, false);
			for(var i=0;i < selectOptions.length; i++){
				value = selectOptions[i].getAttribute("value");
				displayContent=selectOptions[i].firstChild.nodeValue;
				if(withoutFirstValueEmpty == false)
					document.getElementById(selectId).options[i+1]=new Option(displayContent, value, false, false);
				else
				    document.getElementById(selectId).options[i]=new Option(displayContent, value, false, false);
			}
		}
	}else{
		document.location.reload();
	}
}

function doCustomFilter(evt,customKey)
{
	var charCode = (evt.which) ? evt.which : evt.keyCode
	len = customKey.split(",");
	var str = "";
	
	for(i=0;i<len.length;i++)
	{
	  str+= " (charCode == "+len[i]+" ) || ";
	}
	str = str.substring(0,str.length-4)
	return (eval(str)) ? false : true;
}
/**
Prevent the Hacker from entering  characters that he can use to make XSS request
*/
function doSecurityFilter(evt)
{
	//refer to http://www.ascii-code.com/
	return (doCustomFilter(evt,'33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,58,59,60,61,62,63,64,91,92,93,94,95,123,124,125,126'));
}

 function doFilterString(evt) {
  var charCode = (evt.which) ? evt.which : evt.keyCode;
  if( (charCode > 47 && charCode < 58) || !doSecurityFilter(evt)) 
   {
		return false;
	}
	return true;
  }
  
function doSecurityFilterOnTextarea(evt)
{ 
     //These are allowed @ . , / - ? % + &     
     return (doCustomFilter(evt,'124,92,34,58,60,62,91,93,123,125,92,59,40,41,36,35,39,126,33,94,95,61'));
}
  
function doLowSecurityFilter(evt)
{
	//refer to http://www.ascii-code.com/
	return (doCustomFilter(evt,'60,62,40,41,91,93,34,39,59,58,38,35'));
}  

function dragAndDropNumEvt(obj,numberInstance,stringInstance)
{ 
if((stringInstance.isString(obj.value) || stringInstance.isStringAndNum(obj.value)) && !numberInstance.isDecimal(obj.value))
    obj.value="";
}  

function dragAndDropLowSecurityEvt(obj)
{
 if(testSpecialCharater(obj,'<;>;";[;];(;);:;#;&'))
   obj.value = "";
}

function dragAndDropSecurityEvt(obj)
{
 if(testSpecialCharater(obj,'!;";#;$;%;&;(;);*;+;-;.;/;:;,;<;=;>;?;@;[;\;];^;_;{;|;};~'))
   obj.value = "";
}

function dragAndDropTxtAreaSecurityEvt(obj)
{
 if(testSpecialCharater(obj,'|;\;";:;<;>;[;];{;};(;);#;$;~;!;^;_;='))
   obj.value = "";
}

function testSpecialCharater(obj,charList)
{
	var len = charList.split(";");
	var str = false;
	for(i=0;i<len.length;i++)
	{
	  if((len[i] != '' && obj.value.indexOf(len[i])!=-1) || obj.value.indexOf("'") != -1 || obj.value.indexOf(";") != -1 )
	  {
		str = true;
		break;
	  }
	}
	return str;
}
 
/* 
	disable/enable buttons on a form	
*/
function disableFormButtons(status)
{	
  var inputElts = this.ownerDoc.getElementsByTagName("INPUT");
  for(var i = 0 ; i < inputElts.length ; i++){
  	eltObj = inputElts[i];
  	if(eltObj.type == "button" || eltObj.type == "submit"){
  		if(eltObj.id != "")
  			this.disableField(eltObj.id,status);
  	}
  }
}
 /**
 * @author Georges Salameh
 * This function makes all the values in the listBox selected so 
 * they can be retrieved in java using the request.getParameterValues
 */
function selectedOptions(allListBoxes)
{
	listBoxNames = allListBoxes.split(",");	
    for(i=0; i<listBoxNames.length; i++){
    	singleListBox = document.getElementById(listBoxNames[i]);
    	 for(j=0; j<singleListBox.length; j++){
     		singleListBox.options[j].selected="selected";
    	 }
  	}
}
 
 /**
 * @author Georges Salameh
 * This function handles managing the values between diffrent multiple select boxes
 */
function selectMoveRows(SS1,SS2,removeOriginalValue,removeDestinationValue){
    var selID='';
    var selText='';
    // Move rows from SS1 to SS2 from bottom to top
    for (i=SS1.options.length - 1; i>=0; i--){
        if (SS1.options[i].selected == true && removeDestinationValue==true){
            selID=SS1.options[i].value;
            selText=SS1.options[i].text;
        	var newRow = new Option(selText,selID);
        	SS2.options[SS2.length]=newRow;
            //If true the value in the original select will be removed
            if(removeOriginalValue){
            	SS1.options[i]=null;
			}
        }
        else if(SS1.options[i].selected == true && removeDestinationValue==false){
        	SS1.options[i]=null;
        }
    }
    if(removeDestinationValue){
    	SelectSort(SS2);
    	removeDuplicates(SS2);
    }
}

 /**
 * @author Georges Salameh
 * This function handles sorting the values in the destination select
 */
function SelectSort(selList){
    var ID='';
    var Text='';
    for (x=0; x < selList.length - 1; x++){
        for (y=x + 1; y < selList.length; y++){
            if (selList[x].text > selList[y].text){
                // Swap rows
                ID=selList[x].value;
                Text=selList[x].text;
                selList[x].value=selList[y].value;
                selList[x].text=selList[y].text;
                selList[y].value=ID;
                selList[y].text=Text;
            }
        }
    }
}

/**
 * @author Georges Salameh
 * This function handles duplicate values in the destination select
 */
function  removeDuplicates(selList){
	if (selList.length > 1){
	    var subArray = [selList.length - 1];
	    for (i = 1; i < selList.length; i++){
	        if (selList[i-1].value == selList[i].value){
				selList[i] = null;				
	        }
	    }
	    return removeDuplicates(subArray);
	}
}
 
/**
 * @author Georges Salameh
 * This Function has been added to avoid XSS after the penetration testing
*/
function isLowSecurityFiltered(str){
    var filterKeys = [ "<", ">", "(", ")", "[", "]", "\"", "'", ";", "&", "#" ];
    return isCustomFiltered(str, filterKeys);
}

/**
 * @author Georges Salameh
 * This Function to check if the forbidden characters is available in the string
*/
function isCustomFiltered(str,filterKeys){
    str = (str != null) ? str : "";
    for (i = 0; i < filterKeys.length; i++){
      if (str.indexOf(filterKeys[i]) != -1){
        return false;
      }
    }
    return true;
}
}