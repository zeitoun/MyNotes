var PATHNAME = document.location.pathname;
var APP_ROOT = (PATHNAME.split("/"))?"/"+( (PATHNAME.split("/"))[0].length>0? (PATHNAME.split("/"))[0]:(PATHNAME.split("/"))[1])+"/":"";

/**
** Used to take an instance of the form utility
**/
function getFormUtil(formId)
{
  var formInstance = null;
  try
  {
    loadScript(APP_ROOT+"common/form/XperteamFormUtil.js");
    formInstance = new XperteamFormUtil(formId);
  }
  catch(e)
  {
    alert("ERROR getFormUtil")
    throw e;
  }
  return formInstance
}

/**
** Used to take an instance of the date utility
**/
function getDateUtil()
{
  var dateInstance = null;
  try
  {
    loadScript(APP_ROOT+"common/util/XperteamDateUtil.js");
    dateInstance = new DateUtil();
  }
  catch(e)
  {
    alert("ERROR getDateUtil")
    throw e;
  }
  return dateInstance
}

/**
** Used to take an instance of the string utility
**/
function getStringUtil()
{
  var stringInstance = null;
  try
  {
     loadScript(APP_ROOT+"common/util/XperteamStringUtil.js");
    stringInstance = new StringUtil();
  }
  catch(e)
  {
    throw e;
  }
  return stringInstance
}

/**
** Used to take an instance of the number utility
**/
function getNumberUtil()
{
  var numberInstance = null;
  try
  {
     loadScript(APP_ROOT+"common/util/XperteamNumberUtil.js");
    numberInstance = new NumberUtil();
  }
  catch(e)
  {
    throw e;
  }
  return numberInstance
}

/**
** Used to take an instance of the number format utility
**/
function getNumberFormat(num, inputDecimal)
{
  var numberFormatInstance = null;
  try
  {
     loadScript(APP_ROOT+"common/util/XperteamNumberFormat.js");
     
    numberFormatInstance = new NumberFormat(num, inputDecimal);
  }
  catch(e)
  {
    throw e;
  }
  return numberFormatInstance
}

/**
** Used to make ajax request
**/
function getAjaxUtil(uri, mode, isAsync)
{
  var ajaxInstance = null;
  try
  {
  	 loadScript(APP_ROOT+"common/util/XperteamDOM.js");
     loadScript(APP_ROOT+"common/util/XperteamAjaxUtil.js");
      ajaxInstance = new AjaxUtil(uri, mode, isAsync);
  }
  catch(e)
  {
    throw e;
  }
  return ajaxInstance
}

/**
** Used to make XML request
**/
function getXMLUtil(xmlFile, afterXMLLoadFunction)
{
	var xmlInstance = null;
	try
    {
      loadScript(APP_ROOT+"common/util/XperteamDOM.js");
      loadScript(APP_ROOT+"common/util/XperteamXmlUtil.js");
      xmlInstance = new XperteamXMLUtil(xmlFile, afterXMLLoadFunction);
    }
     catch(e)
    {
      throw e;
    }
  return xmlInstance;	
}

/**
 * Gets an instance of a HashMap Object
 */
 function getHashMap()
 {
 	var mapInstance = null;
 	try
 	{
 		loadScript(APP_ROOT+"common/util/XperteamHashMap.js");
        mapInstance = new HashMap();
 	}
 	catch(e)
    {
       throw e;
    }
  	return mapInstance;	
 }

/**
** Load a script
**/
function loadScript(scriptPath)
{
	var httpObj = new createHttpObject();
	httpObj.open("GET", scriptPath, false);
	httpObj.send(null);
	var zScript = document.createElement("script");
	zScript.setAttribute( "type", "text/javascript");
	zScript.text = httpObj.responseText;
	document.getElementsByTagName("head")[0].appendChild(zScript);
	return zScript;
}

/**
** Create an HTTP Object
**/
function createHttpObject()
 {
   try
   {
      return (new ActiveXObject("Msxml2.XMLHTTP"));
   }
   catch(E)
   {
     try
     {
         return(new XMLHttpRequest());
     }
     catch(e)
     {
         return(false);
     }
   }
 }
