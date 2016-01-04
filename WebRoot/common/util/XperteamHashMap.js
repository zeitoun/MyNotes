function HashMap()
{
    this.classId = "PathMap";
    this.map = new Array();
    this.get = getParam;
    this.put = setParam;
    this.clear = clearParam;
    this.remove = removeKey;
    this.size = getSize;
    this.getKeyIndex = getKeyIndex;

	function getParam(iKey)
	{
    	var i = this.getKeyIndex(iKey);
    
    	if(i == this.map.length)
	        return null;
    	else
        	return this.map[i].value;
	}

	function setParam(iKey, iValue)
	{
	    var i = this.getKeyIndex(iKey);
    	if(i == this.map.length)
    	{
	        var obj = new Object();
        	obj.name = iKey;
        	obj.value = iValue;
        	this.map.push(obj);
    	}
    	else
        	this.map[i].value = iValue;
    	return null;
	}
    
	function clearParam()
	{
	    this.map = new Array();
	}

	function removeKey(iKey)
	{
	    var i = this.getKeyIndex(iKey);
    	var rtObj = null;
    	if(i < this.map.length)
    	{
	        rtObj = this.map.splice(i,1);
    	    rtObj = rtObj[0].value;
    	}
    	return rtObj;
	}

	function getSize()
	{
    	return this.map.length;
	}

	//Internal Use functions only

	function getKeyIndex(iKey)
	{
	    for(var i=0; i<this.map.length; ++i)
        	if(this.map[i].name == iKey)
	            break;
	    return i;
	}
}