//This constructor is responsible of making make HTTP requests
 //and handling the response
function AjaxUtil(uri, mode, isAsync)
{
  /************************************* Variables ***************************/
  this.httpObj = null;
  this.errorFunction = null;
  this.handlerFunction = null;
  this.beforeAction = null;
  this.afterAction = null;
  this.query = null;
  this.loadingImage = false;
  this.loadingImagePath = null;
  this.isHTML = false;  
 /************************************* Variables ***************************/
  
 /************************************* Functions ***************************/
  this.makeAjaxRequest = makeAjaxRequest;
 /************************************* End ***************************/

 
 function makeAjaxRequest()
 {
 	try
   {
     if (this.query == null) this.query = "";
     if(this.errorFunction == null) this.errorFunction = function () {};
     this.isAsync = (isAsync == null) ? false: isAsync;
     this.mode = (mode == null) ? "POST" : mode;
     this.httpObj = new createHttpObject();
     if (this.httpObj)
     {
      if(this.loadingImage == true) createLoadingImg();
      if (this.beforeAction == null)  {   this.beforeAction = function () {};   }
      this.beforeAction(); 
      if(this.isHTML == true)
      	this.httpObj.onreadystatechange = onReadyStateFunctionHTML(this.httpObj, this.handlerFunction)
      else
	    this.httpObj.onreadystatechange = onReadyStateFunction(this.httpObj, this.handlerFunction)
      this.httpObj.open(this.mode, uri,this.isAsync);
      this.httpObj.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
      this.httpObj.send(this.query);
      if (this.afterAction == null)  {  this.afterAction = function() {};   }
      this.afterAction();	  	  
      return true;
     }
   }
   catch(ex)
   {
  	 this.errorFunction();
     return false;	
  	}
 }
 
  // this is the case where the response is html not xml
  //the other function will not work in this case because of the
  //test done in responseXML 	
  function onReadyStateFunctionHTML(httpObj, handlerFunction){  
	   return function () {
	     // If the request's status is "complete"	      
	     if (httpObj.readyState == 4) 
	     {
	        // Check that we received a successful response from the server
	        if (httpObj.status == 200) 
	        {
	          // Pass the XML payload of the response to the handler function.
	          //responseXmlHandler(req.responseXML);
	          responseXml = httpObj.responseXML;	          
	  		  responseTxt = httpObj.responseText;	  	  		  	  		  
		      if (handlerFunction == null)  { handlerFunction = function() {}; }
		      handlerFunction(responseTxt,responseXml);
	        } 
	        else 
	        {
	          // An HTTP problem has occurred
	          alert("HTTP error "+httpObj.status+": "+httpObj.statusText);
	          document.location.reload();
	        }
	      }
		if(document.getElementById("loading")!=null && document.getElementById("loading").className == "loadingImg")
			{						
			 document.body.removeChild(document.getElementById("loading"));		
			}	    	      
	    }
	  } 
 
 function onReadyStateFunction(httpObj, handlerFunction){
	   return function () {
	     // If the request's status is "complete"
	     if (httpObj.readyState == 4) 
	     {
	     	if (this.afterAction == null)  {  this.afterAction = function() {};   }
    		this.afterAction();
	       
	        // Check that we received a successful response from the server
	        if (httpObj.status == 200) 
	        {
	        
	        	if(document.getElementById("loading")!=null && document.getElementById("loading").className == "loadingImg")
				{						
			 		document.body.removeChild(document.getElementById("loading"));		
				}	  	      
	    	
	        
	          // Pass the XML payload of the response to the handler function.
	          //responseXmlHandler(req.responseXML);
	          responseXml = httpObj.responseXML;
	  		  responseTxt = httpObj.responseText;
		      if (handlerFunction == null)  { handlerFunction = function() {}; }
		      
		      if(responseXml != null && responseXml.xml != "")
		      	handlerFunction(responseTxt,responseXml);
		      else
		      	 document.location.reload();
	        } 
	        else 
	        {
	          // An HTTP problem has occurred
//	          alert("HTTP error "+httpObj.status+": "+httpObj.statusText);
	          document.location.reload();
	        }
	      }
  	    }
	  }

 function createLoadingImg()
 {
 	if(document.getElementById("loading")==null)
	{
		var divElt = document.createElement("div");
		divElt.className = "loadingImg";
		divElt.id = "loading";
		var pElt = document.createElement("p");
		var imgElt = document.createElement("img");
		imgElt.className = "loadImgStyle";		
		imgElt.src = (this.loadingImagePath != null) ? this.loadingImagePath : "common/images/loading.gif";
		divElt.appendChild(pElt);
		pElt.appendChild(imgElt);
		document.body.appendChild(divElt);
	}
 }


//Creates an HTTP Oject on different browser
function createHttpObject() 
{
  try
  {
    //Used for Internet Explorer
    return (new ActiveXObject("Microsoft.XMLHTTP"));
  }
  catch(exception)
  {
    try
    {
     //Used for Mozilla and others such as Safari, Opera...
	 return(new XMLHttpRequest());
	}
	catch(ex)
	{
      return false;	
	}
  }
}
}