// some basic browser detection
var isIE = (navigator.userAgent.toLowerCase().indexOf("msie") > -1) ? true : false;
var isMOZ = (document.implementation && document.implementation.createDocument) ? true : false;
if (isMOZ) {
    
	//============================================
	// Section: Factory methods for Moz
	//============================================
	// @param sUri the namespace of the root node (if any)
	// @param sUri the local name of the root node (if any)
	// @return a DOM Document

	// AFAIK, the object behaves exactly like
	// IE's IXMLHTTPRequest)
	// @return a XmlHttpRequst object suitable for Moz

	//============================================
	// Section: utility functions for internal use
	//============================================
	// Attached by an event handler to the load event.
	function XMLDocument_onload() {
		loadHandler(this);
	}
	// Ensures the document was loaded correctly, otherwise sets the parseError to -1
	// to indicate something went wrong.
	function loadHandler(oDoc) {
		if (!oDoc.documentElement || oDoc.documentElement.tagName == "parsererror") {
			oDoc.parseError = -1;
		}
		setReadyState(oDoc, 4);
	}
	// Sets the readyState property
	function setReadyState(oDoc, iReadyState) {
		oDoc.readyState = iReadyState;
		if (oDoc.onreadystatechange != null && typeof oDoc.onreadystatechange == "function") {
			oDoc.onreadystatechange();
		}
	}
	XMLDocument.prototype.clearDOM = function () {
		while (this.hasChildNodes()) {
			this.removeChild(this.firstChild);
		}
	};
	// Replaces the contents of the object with the contents of
	// the object given as the parameter
	XMLDocument.prototype.copyDOM = function (oDoc) {
		this.clearDOM();

		// importNode is not yet needed in Moz due to a bug but it will be
		// fixed so...
		var oNodes = oDoc.childNodes;
		for (iDom = 0; iDom < oNodes.length; iDom++) {
			var jp = this.importNode(oNodes[iDom], true);
			this.appendChild(jp);
		}
	};
	var WSMULT = new RegExp("^\\s*|\\s*$", "g");
	var WSENDS = new RegExp("\\s\\s+", "g");
	function normalizeText(sIn) {
		return sIn.replace(WSENDS, " ").replace(WSMULT, " ");
	}
	
	XMLDocument.prototype.loadXML = function(strXML)
	{
		setReadyState(this, 1);
		var sOldXML = this.xml;

		var oDoc = (new DOMParser()).parseFromString(strXML, "text/xml");
		setReadyState(this, 2);
		this.copyDOM(oDoc);
		setReadyState(this, 3);
		loadHandler(this);
		return sOldXML;
	};
	
	Element.prototype.selectSingleNode = function (sExpr) {
		var doc = this.ownerDocument;
		if (doc.selectSingleNode) {
			return doc.selectSingleNode(sExpr, this);
		} else {
			throw "XPathOperationException: Method selectSingleNode is only supported by XML Nodes. (original exception: " + e + ")";
		}
	};
	Node.prototype.__defineGetter__("text", function () {
		if (this.firstChild) {
			return (this.firstChild.nodeValue);
		} else {
			return null;
		}
	});
        // Ensures and informs the xml property is read only
	Node.prototype.__defineSetter__("text", function (sText) {
		if (this.firstChild) {
			this.firstChild.nodeValue = sText;
		}
	});

	// Emulates IE's xml property. Gives an XML serialization of the DOM Object
	XMLDocument.prototype.__defineGetter__("xml", function () {
		return (new XMLSerializer()).serializeToString(this);
	});
	// Emulates IE's xml property. Gives an XML serialization of the DOM Object
	Node.prototype.__defineGetter__("xml", function () {
		return (new XMLSerializer()).serializeToString(this);
	});
	// Ensures and informs the xml property is read only
	XMLDocument.prototype.__defineSetter__("xml", function () {
		throw "Invalid assignment on read-only property 'xml'. Hint: Use the 'loadXML(String xml)' method instead. (original exception: " + e + ")";
	});
	// Emulates IE's innerText (write). Note that this removes all childNodes of
	// an Element and just replaces it with a textNode
	HTMLElement.prototype.__defineSetter__("innerText", function (sText) {
		var s = "" + sText;
		this.innerHTML = s.replace(/\&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
	});
	// Emulate IE's innerText (read). Gives the concatenation of all text nodes under the Element
	HTMLElement.prototype.__defineGetter__("innerText", function () {
		return normalizeText(this.innerHTML.replace(/<[^>]+>/g, ""));
	});
	// Emulate IE's onreadystatechange attribute
	// used as a listener to the onreadystatechange event (also emulated)
	Document.prototype.onreadystatechange = null;
    // Emulate IE's parseError attribute
	Document.prototype.parseError = 0;
	// Emulates IE's readyState property, which always gives an integer from 0 to 4:
	// 1 == LOADING
	// 2 == LOADED
	// 3 == INTERACTIVE
	// 4 == COMPLETED
	XMLDocument.prototype.readyState = 0;
	// Emulates IE's async property. It controls whether loading of
	// remote XML files works synchronously or asynchronously.
	// NOTE: setting async to false will only work with documents
	// called over HTTP (meaning a server), not the local file system,
	// unless you are using Moz 1.4.
	// BTW the try>catch block is for 1.4; I haven't found a way to check if the property is implemented without
	// causing an error and I dont want to use user agent stuff for that...
	try {
		XMLDocument.prototype.async = true;
	}
	catch (e) {
		/*trap*/
	}
	// Keeps a handle to the original load() method
	XMLDocument.prototype.load = XMLDocument.prototype.load;
	/** Extends the load method to provide synchronous loading
	* using an XMLHttpRequest object (if async is set to false)
	* @return the DOM Object as it was before the load() call (may be empty)
	*/
	XMLDocument.prototype.load = function (sURI) {
		var oDoc = document.implementation.createDocument("", "", null);
		oDoc.copyDOM(this);
		this.parseError = 0;
		setReadyState(this, 1);
		try {
			if (this.async == false) {
				var tmp = new XMLHttpRequest();
				tmp.open("GET", sURI, false);
				tmp.overrideMimeType("text/xml");
				tmp.send(null);
				setReadyState(this, 2);
				this.copyDOM(tmp.responseXML);
				setReadyState(this, 3);
			} else {
				this.load(sURI);
			}
		}
		catch (objException) {
			this.parseError = -1;
		}
		finally {
			loadHandler(this);
		}
		return oDoc;
	};
	// Emulate IE's transformNodeToObject
	Document.prototype.transformNodeToObject = function (xslDoc, oResult) {
		var xsltProcessor = null;
		try {
			xsltProcessor = new XSLTProcessor();
            //if(xsltProcessor.reset) this was wirtten before in place of if(false)
			if (xsltProcessor.reset) {
                            // new nsIXSLTProcessor is available
				xsltProcessor.importStylesheet(xslDoc);
				var newFragment = xsltProcessor.transformToFragment(this, oResult);
				return newFragment;

                            //oResult.copyDOM(newFragment);
			} else {
				// only nsIXSLTProcessorObsolete is available
				return xsltProcessor.transformDocument(this, xslDoc, oResult, null);
			}
		}
		catch (e) {
			if (xslDoc && oResult) {
				throw "TransformNodeToObjectException: Failed to transform document. (original exception: " + e + ")";
			} else {
				if (!xslDoc) {
					throw "TransformNodeToObjectException: No Stylesheet Document was provided. (original exception: " + e + ")";
				} else {
					if (!oResult) {
						throw "TransformNodeToObjectException: No Result Document was provided. (original exception: " + e + ")";
					} else {
						if (xsltProcessor == null) {
							throw "XSLTProcessorNotAvailableException: Could not instantiate an XSLTProcessor object. (original exception: " + e + ")";
						} else {
							throw e;
						}
					}
				}
			}
		}
	};
	// Emulate IE's transformNode() method. Gives the result XML serialised to a String
	Document.prototype.transformNode = function (xslDoc) {
		/*var out = document.implementation.createDocument("", "", null);
		var reslt = this.transformNodeToObject(xslDoc, out);
               alert(reslt.getElementsByTagName("result"));*/
		var xsltProcessor = new XSLTProcessor();
		var str = null;
		try {
			xsltProcessor.importStylesheet(xslDoc);
			var newFragment = xsltProcessor.transformToDocument(this);

			//var serializer = new XMLSerializer();
			str = newFragment.xml;
		}
		catch (e) {
			throw "TransformNodeException: Failed to serialize result document. (original exception: " + e + ")";
		}
		return str;
	};
	// Extend the Array to behave as a NodeList
	Array.prototype.item = function (i) {
		return this[i];
	};
	// add IE's expr property
	Array.prototype.expr = "";
    // dummy, used to accept IE's stuff without throwing errors
	XMLDocument.prototype.setProperty = function (x, y) {
	};
	// Emulate IE's selectNodes
	XMLDocument.prototype.selectNodes = function (sExpr, contextNode) {
		try {
			var oResult = this.evaluate(sExpr, (contextNode ? contextNode : this), this.createNSResolver(this.documentElement), XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
			var nodeList = new Array(oResult.snapshotLength);
			nodeList.expr = sExpr;
			for (iN = 0; iN < nodeList.length; iN++) {
				nodeList[iN] = oResult.snapshotItem(iN);
			}
			return nodeList;
		}
		catch (e) {
			alert(e);
		}
	};
	

	Element.prototype.selectSingleNode = function (Expression) {
		Expression += "[1]";
		var nodeList = this.selectNodes(Expression);
		if (nodeList.length > 0) {
			return nodeList[0];
		} else {
			return null;
		}
	};
	Element.prototype.selectNodes = function (sExpr) {
		var doc = this.ownerDocument;
		if (doc.selectNodes) {
			return doc.selectNodes(sExpr, this);
		} else {
			throw "XPathOperationException: Method selectNodes is only supported by XML Nodes";
		}
	};
	Element.prototype.__defineSetter__("text", function (sText) {
		if (!this.firstChild) {
			txtN = this.ownerDocument.createTextNode(sText);
			this.appendChild(txtN);
		} else {
			this.firstChild.nodeValue = sText;
		}
	});
	Element.prototype.__defineGetter__("text", function () {
		if (!this.firstChild) {
			return "";
		} else {
			if (this.firstChild.nextSibling != null && this.firstChild.nextSibling.nodeName == "#cdata-section") {
				return this.firstChild.nextSibling.nodeValue;
			} else {
				return this.firstChild.nodeValue;
			}
		}
	});
	// Emulate IE's selectSingleNode
	XMLDocument.prototype.selectSingleNode = function (sExpr, contextNode) {
		if (this.xml == "") {
			return null;
		}
		var ctx = contextNode ? contextNode : null;
		sExpr += "[1]";
		var nodeList = this.selectNodes(sExpr, ctx);
		if (nodeList.length > 0) {
			return nodeList[0];
		} else {
			return null;
		}
	};

        // support microsoft's "all" property
	HTMLDocument.prototype.__defineGetter__("all", function () {
		return this.getElementsByTagName("*");
	});
        //support microsoft's parentWindow property
	HTMLDocument.prototype.__defineGetter__("parentWindow", function () {
		return this.defaultView;
	});
        // mimic the "createEventObject" method for the document object
	HTMLDocument.prototype.createEventObject = function createEventObject() {
		return document.createEvent("Events");
	};
	HTMLTableElement.prototype.createEventObject = function createEventObject() {
		return document.createEvent("Events");
	};
	HTMLElement.prototype.__defineGetter__("all", function () {
		return this.getElementsByTagName("*");
	});
	HTMLTableCellElement.prototype.__defineSetter__("disabled", function (val) {
		if (val) {
			if (this.getAttribute("onclick") != null && this.getAttribute("onclick") != "") {
				this.setAttribute("onclickOld", this.getAttribute("onclick"));
				this.setAttribute("onclick", "");
				if (this.getAttribute("onclick") != null && this.getAttribute("onclick") != "") {
					this.setAttribute("itsColor",this.style.color);
				}
				this.style.color = "grey";
			}
		} else {
			if (this.getAttribute("onclickOld") != null && this.getAttribute("onclickOld") != "") {
				this.setAttribute("onclick", this.getAttribute("onclickOld"));
				this.style.color = this.getAttribute("itsColor");
			}
		}
		return;
	});
	HTMLTableRowElement.prototype.__defineSetter__("disabled", function (val) {
		var events = ["onclick", "onmousedown", "onmouseup", "onscroll"];
		for (var ev = 0; ev < events.length; ev++) {
			if (val) {
				if (this.getAttribute(events[ev]) != null && this.getAttribute(events[ev]) != "") {
					this.setAttribute(events[ev] + "Old", this.getAttribute(events[ev]));
					this.setAttribute("onclick", "");
					if (this.getAttribute(events[ev]) != null && this.getAttribute(events[ev]) != "") {
						this.setAttribute("itsColor", this.style.color);
					}
					this.style.color = "grey";
				}
			} else {
				if (this.getAttribute(events[ev] + "Old") != null && this.getAttribute(events[ev] + "Old") != "") {
					this.setAttribute(events[ev], this.getAttribute(events[ev] + "Old"));
					this.style.color = this.getAttribute("itsColor");
				}
			}
		}
		return;
	});
        // support "parentElement"
	HTMLElement.prototype.__defineGetter__("parentElement", function () {
		return (this.parentNode == this.ownerDocument) ? null : this.parentNode;
	});
	HTMLElement.prototype.attachEvent = function (sType, fHandler) {
		var shortTypeName = sType.replace(/on/, "");
		fHandler._ieEmuEventHandler = function (e) {
			window.event = e;
			return fHandler();
		};
		this.addEventListener(shortTypeName, fHandler._ieEmuEventHandler, false);
	};
	HTMLElement.prototype.blur = function () {
		this.fireEvent("onblur");
	};
	HTMLElement.prototype.focus = function () {
		this.fireEvent("onfocus");
	};
	HTMLElement.prototype.click = function () {
		this.fireEvent("onclick");
	};
	HTMLElement.prototype.detachEvent = function (sType, fHandler) {
		var shortTypeName = sType.replace(/on/, "");
		if (typeof fHandler._ieEmuEventHandler == "function") {
			this.removeEventListener(shortTypeName, fHandler._ieEmuEventHandler, false);
		} else {   // we can always try :-)
		}
		this.removeEventListener(shortTypeName, fHandler, true);
	};
        // mimic the "removeEvent" method
	HTMLElement.prototype.removeEvent = function removeEvent(name, handler) {
		this.removeEventListener(name.slice(2), handler, false);
	};
        // mimic the "createEventObject" method
	HTMLElement.prototype.createEventObject = function createEventObject() {
		return this.ownerDocument.createEventObject();
	};
        // mimic the "fireEvent" method
	HTMLElement.prototype.fireEvent = function fireEvent(name, event) {
		if (!event) {
			event = this.ownerDocument.createEventObject();
		}
		event.initEvent(name.slice(2), false, false);
		this.dispatchEvent(event);
            // not sure that this should be here??
		if (typeof this[name] == "function") {
			this[name]();
		} else {
			if (this.getAttribute(name)) {
				eval(this.getAttribute(name));
			}
		}
	};
	HTMLElement.prototype.insertAdjacentElement = function (where, parsedNode) {
		switch (where) {
		  case "beforeBegin":
			this.parentNode.insertBefore(parsedNode, this);
			break;
		  case "afterBegin":
			this.insertBefore(parsedNode, this.firstChild);
			break;
		  case "beforeEnd":
			this.appendChild(parsedNode);
			break;
		  case "afterEnd":
			if (this.nextSibling) {
				this.parentNode.insertBefore(parsedNode, this.nextSibling);
			} else {
				this.parentNode.appendChild(parsedNode);
			}
			break;
		}
	};
	HTMLElement.prototype.insertAdjacentHTML = function (where, htmlStr) {
		var r = this.ownerDocument.createRange();
		r.setStartBefore(this);
		var parsedHTML = r.createContextualFragment(htmlStr);
		this.insertAdjacentElement(where, parsedHTML);
	};
	var _emptyTags = {"IMG":true, "BR":true, "INPUT":true, "META":true, "LINK":true, "PARAM":true, "HR":true};
	HTMLElement.prototype.__defineGetter__("children", function () {
		var tmp = [];
		var j = 0;
		var n;
		for (var i = 0; i < this.childNodes.length; i++) {
			n = this.childNodes[i];
			if (n.nodeType == 1) {
				tmp[j++] = n;
				if (n.name) {  // named children
					if (!tmp[n.name]) {
						tmp[n.name] = [];
					}
					tmp[n.name][tmp[n.name].length] = n;
				}
				if (n.id) {      // child with id
				}
				tmp[n.id] = n;
			}
		}
		return tmp;
	});
	HTMLElement.prototype.contains = function (oEl) {
		if (oEl == this) {
			return true;
		}
		if (oEl == null) {
			return false;
		}
		return this.contains(oEl.parentNode);
	};
	HTMLElement.prototype.__defineGetter__("outerHTML", function () {
		var attrs = this.attributes;
		var str = "<" + this.tagName;
		for (var i = 0; i < attrs.length; i++) {
			str += " " + attrs[i].name + "=\"" + attrs[i].value + "\"";
		}
		if (_emptyTags[this.tagName]) {
			return str + ">";
		}
		return str + ">" + this.innerHTML + "</" + this.tagName + ">";
	});
	HTMLElement.prototype.__defineSetter__("outerHTML", function (sHTML) {
		var r = this.ownerDocument.createRange();
		r.setStartBefore(this);
		var df = r.createContextualFragment(sHTML);
		this.parentNode.replaceChild(df, this);
	});
	HTMLElement.prototype.insertAdjacentText = function (where, txtStr) {
		var parsedText = document.createTextNode(txtStr);
		this.insertAdjacentElement(where, parsedText);
	};

        // support the "contains" method
	HTMLElement.prototype.contains = function contains(element) {
		return Boolean(element == this || (element && this.contains(element.parentElement)));
	};
        // Event
        // -----
        // support microsoft's proprietary event properties
	Event.prototype.__defineGetter__("srcElement", function () {
		return (this.target.nodeType == Node.ELEMENT_NODE) ? this.target : this.target.parentNode;
	});
//        Event.prototype.__defineGetter__("srcElement", function () {
//									   var node = this.target;
//            while (node.nodeType != 1) node = node.parentNode;
//   									return node;
//									});
	Event.prototype.__defineGetter__("keyCode", function () {
		return (this.which);
	});
	Event.prototype.__defineGetter__("fromElement", function () {
		return (this.type == "mouseover") ? this.relatedTarget : (this.type == "mouseout") ? this.srcElement : null;
	});
	Event.prototype.__defineGetter__("toElement", function () {
		return (this.type == "mouseout") ? this.relatedTarget : (this.type == "mouseover") ? this.srcElement : null;
	});
	Event.prototype.__defineGetter__("button", function () {
		return (this.which == 1) ? 1 : (this.which == 2) ? 4 : 2;
	});
        // mimc "returnValue" (default is "true")
	Event.prototype.__defineGetter__("returnValue", function () {
		return true;
	});
	Event.prototype.__defineSetter__("returnValue", function (value) {
		if (this.cancelable && !value) {
                // this can't be undone!
			this.preventDefault();
			this.__defineGetter__("returnValue", function () {
				return false;
			});
		}
	});
        // mozilla already supports the read-only "cancelBubble"
        //  so we only need to define the setter
	Event.prototype.__defineSetter__("cancelBubble", function (value) {
            // this can't be undone!
		if (value) {
			this.stopPropagation();
		}
	});
	Event.prototype.__defineGetter__("offsetX", function () {
		return this.layerX;
	});
	Event.prototype.__defineGetter__("offsetY", function () {
		return this.layerY;
	});
	CSSStyleDeclaration.prototype.__defineGetter__("pixelLeft", function () {
		return parseInt(this.left) || 0;
	});
	CSSStyleDeclaration.prototype.__defineSetter__("pixelLeft", function (value) {
		this.left = value + "px";
	});
	CSSStyleDeclaration.prototype.__defineGetter__("pixelHeight", function () {
		return parseInt(this.height) || 0;
	});
	CSSStyleDeclaration.prototype.__defineSetter__("pixelHeight", function (value) {
		this.height = value + "px";
	});
	CSSStyleDeclaration.prototype.__defineGetter__("pixelTop", function () {
		return parseInt(this.top) || 0;
	});
	CSSStyleDeclaration.prototype.__defineSetter__("pixelTop", function (value) {
		this.top = value + "px";
	});
	CSSStyleDeclaration.prototype.__defineGetter__("pixelWidth", function () {
		return parseInt(this.width) || 0;
	});
	CSSStyleDeclaration.prototype.__defineSetter__("pixelWidth", function (value) {
		this.width = value + "px";
	});
} else {
	if (isIE) {
	//============================================
	// Section: IE Initialization
	//============================================
	// Add NodeType constants; missing in IE4, 5 and 6
		if (!window.Node) {
			var Node = {ELEMENT_NODE:1, ATTRIBUTE_NODE:2, TEXT_NODE:3, CDATA_SECTION_NODE:4, ENTITY_REFERENCE_NODE:5, ENTITY_NODE:6, PROCESSING_INSTRUCTION_NODE:7, COMMENT_NODE:8, DOCUMENT_NODE:9, DOCUMENT_TYPE_NODE:10, DOCUMENT_FRAGMENT_NODE:11, NOTATION_NODE:12};
		}
	// for XSLT parameter names
		IEPREFIX4XSLPARAM = "xsl:";
	// used to store the most recent ProgID available out of the above
		var DOM_PROGID = "";
		var XMLHTTP_PROGID = "";
	// used to pick most recent ProgIDs
	}
}