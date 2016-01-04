

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

function selectAllOptions(selStr)
{
  var selObj = document.getElementById(selStr);
  for (var i=0; i<selObj.options.length; i++) {
    selObj.options[i].selected = true;
  }
}



function openPopUpWithSize(theURL, title, heightInPX, widthInPX)
{
	   var returnedValue = window.showModalDialog(theURL, title, 'dialogHeight=' + heightInPX + 'px;dialogWidth='+ widthInPX + 'px');
	   
	   var returnedValueArray = returnedValue.split("&");
	   
	   alert("returnedValuesssssss: " + returnedValue);
	   
	   var doc = document;
	   var f = doc.getElementById('reqForm');
	   var newInput = doc.createElement("input");
	   newInput.name = "newInput";
	   newInput.type = "text";
	   newInput.value=returnedValue;
	   
//	   var tieredDiv = doc.getElementById('tieredDiv');	   
//	   document.getElementById('tieredDiv').appendChild(newInput);
	   
	   
	   
	   /*tr2 = document.createElement("tr");
	   td2 = document.createElement("td");
	   
	   tr2.appendChild(td2);
	   td2.appendChild('heloooooooooo');
	   document.getElementById('tieredDivTable').appendChild(tr2);*/
	   
	   
	   var fromvalue = returnedValueArray[0];
//	   alert("fromval: " + fromvalue);
	   var tovalue = returnedValueArray[1];
//	   alert("toval: " + tovalue);
	   var actionIDs = returnedValueArray[2];
//	   alert("actionids: " + actionIDs);
	   
	   var theTable = doc.getElementById('tieredDivTable');
	   /*for( var x = 0; x < theTable.tHead.rows.length; x++ ) {
	     var y = document.createElement('td');
	     y.appendChild(document.createTextNode('Thead cell text'));
	     theTable.tHead.rows[x].appendChild(y);
	   }*/
	  /* for( var z = 0; z < theTable.tBodies.length; z++ ) {
	     for( var x = 0; x < theTable.tBodies[z].rows.length+4; x++ ) {
	       var y = doc.createElement('td');
	       y.appendChild(document.createTextNode('Tbody cell text'));
	       theTable.tBodies[z].rows[x].appendChild(y);
	     }
	   }*/
	   /*for( var x = 0; x < theTable.tFoot.rows.length; x++ ) {
	     var y = document.createElement('td');
	     y.appendChild(document.createTextNode('Tfoot cell text'));
	     theTable.tFoot.rows[x].appendChild(y);
	   }*/
	   
	   
	   tr = doc.createElement("tr");
	   td = doc.createElement("td");
	   td2 = doc.createElement("td");
	   td3 = doc.createElement("td");
	   td4 = doc.createElement("td");
	   
	   sptd1 = doc.createElement("span");
	   sptd1.className = 'FrmCompTitle';
	   
	   sptd2 = doc.createElement("span");
	   sptd2.className = 'FrmCompTitle';
	   
	   sptd3 = doc.createElement("span");
	   sptd3.className = 'FrmCompTitle';
	   
	   sptd4 = doc.createElement("span");
	   sptd4.className = 'FrmCompTitle';
	   
	   
	   
//	   <a class=\"linkStyle\" href=\"ActionTieredView?RuleTierID="+ruleTieredVO.getRuleTierID()+"\" onclick=\"return popitup('ActionTieredView?RuleTierID="+ruleTieredVO.getRuleTierID()+"&fromAmt="+ruleTieredVO.getFromAmount()+"&toAmt="+ruleTieredVO.getToAmount()+"')\">More Details...</a>");

	   var anchorTag = doc.createElement("a");
       anchorTag.appendChild(document.createTextNode("more details"));
       anchorTag.setAttribute('name', 'Example');
//       anchorTag.setAttribute('href', 'ActionTieredView?RuleTierID=1&fromAmt=1&toAmt=1');
       anchorTag.setAttribute('href', 'ActionTieredView');
       anchorTag.setAttribute('onclick', 'return popitup("ActionTieredView?RuleTierID=1&fromAmt=1&toAmt=1")');
       anchorTag.setAttribute('target', '_blank');
       anchorTag.setAttribute('className', 'linkStyle');
//       anchorTag.setAttribute('onmouseover', 'Tip("test")');
//       anchorTag.setAttribute('onmouseout', 'UnTip()');

       sptd4.appendChild(anchorTag);
       td4.appendChild(sptd4);    
       
       td.appendChild(sptd1);
       td2.appendChild(sptd2);
       td3.appendChild(sptd3);

	   
	   doc.getElementById('tieredDivTable').appendChild(tr);
	   tr.appendChild(td);
	   tr.appendChild(td2);
	   tr.appendChild(td3);
	   tr.appendChild(td4);
	   
	   sptd1.innerHTML = fromvalue;
	   sptd2.innerHTML = tovalue;
	   sptd3.innerHTML = actionIDs;
//	   td2.appendChild(newInput);	   
//	   td3.appendChild(newInput);
//	   td4.innerHTML = "more details...";
	   
	   
	   
	   
	   
//	   doc.getElementById('tieredDivTable').appendChild(tr);
	   
//	   f.appendChild(newInput);

}


/*function doIt()
{
var doc = document;
var f = doc.getElementById('myForm');

// show hidden
var el = f.elements.unseen;
el.style.display = "";

// create/insert new
el = doc.createElement("input");
el = f.appendChild(el);
el.name = "newinput";
el.type = "text";
el.value = "added on the fly";
}*/
