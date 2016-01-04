  var currentDivId = "";
  function selectTabs(obj)
  {
   currentDivId = obj.parentNode.id.substring(0,obj.parentNode.id.lastIndexOf("_"));        
   document.getElementById(currentDivId+"_tabsFrame").style.display =  "block";
   if(obj.getAttribute("param")!=null)
     document.getElementById(currentDivId+"_tabsFrame").src = obj.getAttribute("src")+"?"+obj.param;
   else
     document.getElementById(currentDivId+"_tabsFrame").src = obj.getAttribute("src");
   document.getElementById(currentDivId+"_dataDiv").innerHTML =  "";
   document.getElementById(currentDivId+"_dataDiv").style.display =  "none";   
   var tabsNb = document.getElementsByTagName("a").length;
	for(var i = 1; i<=tabsNb ;i++)
	{
	 var linkId = document.getElementsByTagName("a")[i-1].parentNode.id;
	 var objDivId = linkId.substring(0,linkId.lastIndexOf("_"));
	 if(objDivId == currentDivId)
	 {
		 document.getElementById(linkId).className = "";
		 if(linkId == obj.parentNode.id)
		 {   	 
		   document.getElementById(linkId).className = "selected";
		 }
	 }
	}
  }
  