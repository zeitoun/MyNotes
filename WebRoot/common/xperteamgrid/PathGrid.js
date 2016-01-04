var selectedColor = "#D0DEEF";
var order = "";
var divId = "";
function sortColumn(obj)
{   
   var gridId = obj.getAttribute("gridId");
   divId = "mainDiv_"+gridId;  
   var orderBy = obj.getAttribute("property");          
   var ajaxInstance = getAjaxUtil(obj.getAttribute("url"), "POST", true); 
   ajaxInstance.isHTML = true;
   ajaxInstance.loadingImage = true;	
   ajaxInstance.loadingImagePath = "../common/images/loading.gif";     
   ajaxInstance.handlerFunction = handleSortingResponse;   
         
   if(order == "DESC")
   	  order = "ASC";
   	 else  
   	 if(obj.getAttribute("order") == "ASC" || order == "ASC")
   	 order = "DESC";  
   obj.setAttribute("order",order);
   
   var query = "order="+order+"&orderBy="+orderBy;
   if(document.getElementById("pageNum_"+gridId) != null && document.getElementById("nbRecPerPage_"+gridId)!= null) 
   {  
	   var nbRecPerPage = document.getElementById("nbRecPerPage_"+gridId).value;	   	   
   	   query += "&pageNum=1&nbRecPerPage="+nbRecPerPage;   
   }
   
   ajaxInstance.query = query; 
   ajaxInstance.makeAjaxRequest();   
   return false;
} 

function handleSortingResponse(responseText,responseXML)
{
	document.getElementById(divId).innerHTML =  responseText;
}

function changePage(obj,pageNb)
{	
	var stringUtil = getStringUtil();
    var gridId = obj.parentNode.getAttribute("gridId");
	divId = "mainDiv_"+gridId;
   	var pageNum = document.getElementById("pageNum_"+gridId).value;   	
   	pageNum = pageNum.substring(pageNum.lastIndexOf(":")+1,pageNum.length);  	   	
   	if(pageNum.lastIndexOf("/") != -1)
   		pageNum = pageNum.substring(0,pageNum.lastIndexOf("/")-1);   		   	   
   	var nbRecPerPage = stringUtil.trim(document.getElementById("nbRecPerPage_"+gridId).value);  	   
   	if(nbRecPerPage == "")
   	   nbRecPerPage = 10;   	
   	var zOrder = document.getElementById("order_"+gridId).value;
   	var zOrderBy = document.getElementById("orderBy_"+gridId).value;  	

	var ajaxInstance = getAjaxUtil(obj.parentNode.getAttribute("url"), "POST", true); 
    ajaxInstance.isHTML = true;
    ajaxInstance.loadingImage = true;	
    ajaxInstance.loadingImagePath = "../common/images/loading.gif";	
  	ajaxInstance.handlerFunction = handleFlippingResponse;    	       	   
   	
   	switch(obj.id)
   	{
   	 case "pPage_"+gridId:
   	 	pageNum = (eval(pageNum)-1);
   	 	break;
   	 case "nPage_"+gridId:
   	  	pageNum = (eval(pageNum)+1);
   	  	break;
   	 case "fPage_"+gridId: 	
   	 	pageNum = 1;
   	 	break;
	case "lPage_"+gridId: 	
   	 	pageNum = pageNb;
   	 	break;
	case "nbRecPerPage_"+gridId:		    
   	 	pageNum = 1;	
   	 	break;     	 	   	 	 	
   	}   	
  				  	
  	var query = "pageNum="+pageNum+"&nbRecPerPage="+nbRecPerPage;
  	if(zOrder != "")
  	   query += "&order="+zOrder;
  	if(zOrderBy != "")
  	   	query += "&orderBy="+zOrderBy;  	   		  	   
  	    
  	ajaxInstance.query = query; 
  	ajaxInstance.makeAjaxRequest();   
  	return false;
}

function handleFlippingResponse(responseText,responseXML)
{	
	document.getElementById(divId).innerHTML =  responseText;
}

function setCursor(obj)
{
  obj.style.cursor = "pointer";
}


var lastBgColor = "";
var lastObj = "";
function rightClick(obj)
{   
   if(lastBgColor != "") lastObj.style.backgroundColor = lastBgColor;
   if(!(obj.style.backgroundColor.toUpperCase() == selectedColor.toUpperCase()))
   {  
     lastObj = obj;
     lastBgColor = obj.style.backgroundColor;    
   }  
   obj.style.backgroundColor = selectedColor;
}

function checkClick(obj)
{
  var gridId = obj.name.substring(0,obj.name.indexOf("_"));  
  var selectedRow = getSelectedRowIds(gridId);
  var rowIdsArr = selectedRow.split("&rowId="); 
  var imagePath = "../common/pathgrid/img/";  
 //in case no record checked the add btn will be active only
 if(obj.getAttribute("multiselection")== "false")
 {
	 if(rowIdsArr.length-1 == 0)
	 {   
	   manageBtns("imgAdd_"+gridId,imagePath+"add.png","pointer",false);
	   manageBtns("imgEdit_"+gridId,imagePath+"white/edit.png","default",true);	
	   manageBtns("imgDelete_"+gridId,imagePath+"white/delete.png","default",true);
	 }
	 else
	 //only one checked the btns edit and delete will be active
	 if(rowIdsArr.length-1 == 1)
	 {      
	    manageBtns("imgAdd_"+gridId,imagePath+"white/add.png","default",true);
	    manageBtns("imgEdit_"+gridId,imagePath+"edit.gif","pointer",false);	
	    manageBtns("imgDelete_"+gridId,imagePath+"delete.gif","pointer",false); 
	 }
	 else
		 //more than one checked the delete btn will be active
		 if(rowIdsArr.length-1 > 1)
		 {
		    manageBtns("imgAdd_"+gridId,imagePath+"white/add.png","default",true);
		    manageBtns("imgEdit_"+gridId,imagePath+"white/edit.png","default",true);	
		    manageBtns("imgDelete_"+gridId,imagePath+"delete.gif","pointer",false); 	   
		 }	
 }
}

function manageBtns(btnId,btnSrc,imgCursor,isDisable)
{       
   if(document.getElementById(btnId) != null)
   {     
	   document.getElementById(btnId).src = btnSrc;   
	   document.getElementById(btnId).style.cursor = imgCursor;
	   document.getElementById(btnId).disabled = isDisable;
   }
}


//get the selected rows seperated by comma in order to use it for modify and delete
function getSelectedRowIds(gridId)
{
  var selectedRow = "";  
  var rows = document.getElementsByName(gridId+"_checkRow");  
  for(i = 0; i < rows.length ; i++)
  {    
    if(rows[i].checked)
    {    	            
      var rowIdArr = rows[i].parentNode.parentNode.getAttribute("rowId");
      rowIdArr = rowIdArr.substring(rowIdArr.lastIndexOf("/")+1,rowIdArr.length);
      selectedRow += "&rowId="+rowIdArr;
    }
  }  
  return selectedRow;
}

//get the rowId for the selected row in case we don't have checkbox 
function getRowId(obj)
{ 
  var rowObj = obj.getAttribute("rowId"); 
  var rowId = rowObj.substring(rowObj.lastIndexOf("/")+1,rowObj.length);  
  return rowId;
}

//get the cell value specific gridId, rowId and colId  
function getCellObj(gridId, rowId, colId)
{
 return document.getElementById(gridId + "/" + rowId + "/" +colId); 
}

//get the cell value specific gridId, rowId and colId  
function getCellValue(gridId, rowId, colId)
{
 return document.getElementById(gridId + "/" + rowId + "/" +colId).getAttribute("value"); 
}

function getNbRecords(gridId)
{
 var tbody = document.getElementById(gridId+"_tbody");  
 return (tbody.rows.length); 
}

function reloadGrid(gridId,xmlData)
{
  document.getElementById("mainDiv_"+gridId).innerHTML = xmlData;
}

function changeRowBgColor(obj,className)
{
 var gridId = obj.getAttribute("rowId").substring(0,obj.getAttribute("rowId").lastIndexOf("/"))
 if(className == null)
   className = "obj_tr_selected"; 
 var rows = document.getElementsByName(gridId+"_checkRow");
 for (var i=0;i<rows.length;i++)
 { 
  var rowIdArr = rows[i].parentNode.parentNode.getAttribute("rowId");  
  if(rows[i].parentNode.parentNode.className == className && rows.length > 1)
  {
   if(i == 0)
   {
	if(rows[i+1].parentNode.parentNode.className == "obj_odd_tr" || (rows.length >=3 && rows[i+2].parentNode.parentNode.className == "obj_tr"))
		rows[i].parentNode.parentNode.className = "obj_tr";
	else if(rows[i+1].parentNode.parentNode.className == "obj_tr" || (rows.length >=3 && rows[i+2].parentNode.parentNode.className == "obj_odd_tr"))
			rows[i].parentNode.parentNode.className = "obj_odd_tr";
   }
   else
	   if(i==rows.length-1)
	   {
		if(rows[i-1].parentNode.parentNode.className == "obj_odd_tr" || rows[i-2].parentNode.parentNode.className == "obj_tr")
			rows[i].parentNode.parentNode.className = "obj_tr";
		else if(rows[i-1].parentNode.parentNode.className == "obj_tr" || rows[i-2].parentNode.parentNode.className == "obj_odd_tr")
				rows[i].parentNode.parentNode.className = "obj_odd_tr";
	   }
	  else
	   {
		 if(rows[i-1].parentNode.parentNode.className == "obj_odd_tr" || rows[i+1].parentNode.parentNode.className == "obj_odd_tr")
			rows[i].parentNode.parentNode.className = "obj_tr";
		 else if(rows[i-1].parentNode.parentNode.className == "obj_tr" || rows[i+1].parentNode.parentNode.className == "obj_tr")
			 rows[i].parentNode.parentNode.className = "obj_odd_tr";
	   }
  }
 }
 obj.className = className;
}

function selectAllRows(obj)
{  
  var imagePath = "../common/pathgrid/img/";
  var gridId = obj.getAttribute("name").substring(0,obj.getAttribute("name").lastIndexOf("_"));
  var rows = document.getElementsByName(gridId+"_checkRow");  
  for(i = 0; i < rows.length ; i++)
  {    
    rows[i].checked = obj.checked; 
    if(obj.checked)
    {
	    manageBtns("imgAdd_"+gridId,imagePath+"white/add.png","default",true);
	    manageBtns("imgEdit_"+gridId,imagePath+"white/edit.png","default",true);	
	    manageBtns("imgDelete_"+gridId,imagePath+"delete.gif","pointer",false);    
    }
  }  	
}












