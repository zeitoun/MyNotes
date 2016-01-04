 var decimalPoints = 2;

function errorFunction() {
	return false;
}


function setDropDownData(selectId,selectHiddenId){
		
	var selectObject = document.getElementById(selectId);
	document.getElementById(selectHiddenId).value = selectObject.options[selectObject.selectedIndex].innerHTML;
}


function resetForm() {
	document.getElementById("code").value="";
	document.getElementById("title").value="";
	document.getElementById("note").value="";
	document.getElementById("productType").value="";
	document.getElementById("effectiveDate").value="";
	document.getElementById("enrollmentPoints").value="";
	document.getElementById("accruedPointsExpiryNoOfDays").value="";
//	document.getElementById("excludedRules").value="";
	
}

function submitForm(f, servletNameEnc, action) {
	var f1 = eval(f);
	f1.method = "post";
	f1.actionType.value = action;
	f1.submit();		
}

function submitEnter(E)
{
	var keyCode;
	if(E.keyCode)
		keyCode = E.keyCode;
	else if(E.which)
		keyCode = E.which;
	else
		return true; 
	if (keyCode == 13)
   	{
   		if(enterOnce==0)
   			ValidateForm();
   		//if(ValidateForm()){
   			//because it is already called from the ValidateForm()
   			//document.form1.submit()
   		//}
   		//return false;
   	}
	else
   		return true;
}



/*function selectAll(selectBox,selectAll) { 
    // have we been passed an ID 
    if (typeof selectBox == "string") { 
        selectBox = document.getElementById(selectBox);
    } 
    // is the select box a multiple select box? 
    if (selectBox.type == "select-multiple") { 
        for (var i = 0; i < selectBox.options.length; i++) { 
             selectBox.options[i].selected = selectAll; 
        } 
    }
}*/

function selectAllOptions(selStr)
{
  var selObj = document.getElementById(selStr);
  for (var i=0; i<selObj.options.length; i++) {
    selObj.options[i].selected = true;
  }
}





//window.onload=function() {
	function checkDivToDisplay(){

	  // get tab container
	  	var container = document.getElementById("tabContainer");
			var tabcon = document.getElementById("tabscontent");
			//alert(tabcon.childNodes.item(1));
	    // set current tab
	    var navitem = document.getElementById("tabHeader_1");
			
	    //store which tab we are on
	    var ident = navitem.id.split("_")[1];
			//alert(ident);
	    navitem.parentNode.setAttribute("data-current",ident);
	    //set current tab with class of activetabheader
	    navitem.setAttribute("class","tabActiveHeader");

	    //hide two tab contents we don't need
	   	 var pages = tabcon.getElementsByTagName("div");
	    	for (var i = 1; i < pages.length; i++) {
	     	 pages.item(i).style.display="none";
			};

	    //this adds click event to tabs
	    var tabs = container.getElementsByTagName("li");
	    for (var i = 0; i < tabs.length; i++) {
	      tabs[i].onclick=displayPage;
	    }
	}

	// on click of one of tabs
	function displayPage() {
	  var current = this.parentNode.getAttribute("data-current");
	  //remove class of activetabheader and hide old contents
	  document.getElementById("tabHeader_" + current).removeAttribute("class");
	  document.getElementById("tabpage_" + current).style.display="none";

	  var ident = this.id.split("_")[1];
	  //add class of activetabheader to new active tab and show contents
	  this.setAttribute("class","tabActiveHeader");
	  document.getElementById("tabpage_" + ident).style.display="block";
	  this.parentNode.setAttribute("data-current",ident);
	}

	
	
	function showHide(menuID){
		alert("menuID: "+ menuID);
		var menuID = document.getElementById(menuID);
		menuID.style.visibility="hidden";
	}
	