function XperteamXMLUtil(xmlFile, afterXMLLoadFunction) 
{
	this.loadXML = loadXML;
	this.createXmlObject = createXmlObject;
	this.afterXMLLoadFunction = afterXMLLoadFunction;
	
	
	function loadXML() 
	{
		xmlDoc = this.createXmlObject();
		xmlDoc.async = false;
		xmlDoc.load(xmlFile);
		
		if (this.afterXMLLoadFunction == null)  { this.afterXMLLoadFunction = function() {}; }
        this.afterXMLLoadFunction(xmlDoc);
	}
	
	function createXmlObject() 
	{
		if (isMOZ) 
		{
			return document.implementation.createDocument("", "", null);
		} 
		else 
		{
			return new ActiveXObject("Microsoft.XMLDOM");
		}
	}
	
}
