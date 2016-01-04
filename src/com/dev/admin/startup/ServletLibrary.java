package com.dev.admin.startup;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.hdc.sysdev.utils.StringUtil;
import com.hdc.sysdev.xml.XmlReader;
import com.optica.admin.Business.AdminBO;


/**
 * The Class ServletLibrary.
 *
 * @author HaythamDouaihy
 */
public class ServletLibrary extends HttpServlet {


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The application. */
	private ServletContext application = null;
	
	/** The logger. */
	private final Logger logger = Logger.getLogger(ServletLibrary.class);


	/**
	 * is invoked at the deployed of the project.
	 *
	 * @throws ServletException the servlet exception
	 */
	public void init() throws ServletException {
		initLog4j();
		loadParametersFromXML();
		showXMLParameters();
		
		createBOInstances();
		
		logger.info("General Parameters are been set");
	}
	
	public void createBOInstances() throws ServletException {
		AdminBO adminBO = 
			new AdminBO(StringUtil.nullToEmpty(application.getAttribute("dbServerIP")), 
					StringUtil.nullToEmpty(application.getAttribute("dbServiceName")),
					StringUtil.nullToEmpty(application.getAttribute("dbUserName")),
					StringUtil.nullToEmpty(application.getAttribute("dbPassword")),
					StringUtil.nullToEmpty(application.getAttribute("oracleDriver")));
		
		application.setAttribute("adminBO", adminBO);
	}
	
	

	/**
	 * configures log4j for logging.
	 */
	private void initLog4j() {
		String prefix = this.getServletContext().getRealPath("/");
		String file = this.getInitParameter("log4j-init-file");

		if (file != null) {
			PropertyConfigurator.configure(prefix + file);
			logger.info("the logger has been initialized properly");
		}
	}

	/**
	 * loads parameters from xml file.
	 */
	private void loadParametersFromXML() {
		logger.info("Begin of Process");
		
		application=this.getServletContext();
		String servletPath = this.getServletContext().getRealPath("/");
		XmlReader xmlReader = new XmlReader(servletPath, "/WEB-INF/xml/Controls.xml");
		

//		String applicationServerProtocolName = xmlReader.getControls("Controls","ApplicationServer", "protocolName");
//		String applicationServerURL = xmlReader.getControls("Controls","ApplicationServer", "url");
//		String applicationServerVendorName = xmlReader.getControls("Controls","ApplicationServer", "vendorName");

        String clientDirName = xmlReader.getControls("Controls","ClientDirName", "value");
        String defaultBranch = xmlReader.getControls("Controls","DefaultBranch", "value");
        String sessionTimeout = xmlReader.getControls("Controls","SessionTimeout", "value");
        
        String defaultDateFormat = xmlReader.getControls("Controls","DefaultDateFormat", "value");
        String defaultTimeFormat = xmlReader.getControls("Controls", "DefaultTimeFormat", "value");
        String defaultDateTimeFormat = xmlReader.getControls("Controls","DefaultDateTimeFormat", "value");
        String roundingMethod = xmlReader.getControls("Controls","RoundingMethod", "value");
        String contentType = xmlReader.getControls("Controls","ContentType", "value");
        String characterEncoding = xmlReader.getControls("Controls","CharacterEncoding", "value");
        String browserEncoding = xmlReader.getControls("Controls","BrowserEncoding", "value");
        String databaseEncoding = xmlReader.getControls("Controls","DatabaseEncoding", "value");
        String defaultLanguage = xmlReader.getControls("Controls","DefaultLanguage", "value");
        String numOfRecordToShow = xmlReader.getControls("Controls","NumOfRecordToShow", "value");
        
        String dbServerIP = xmlReader.getControls("Controls","DbServerIP", "value");
        String dbServiceName = xmlReader.getControls("Controls","DbServiceName", "value");
        String dbUserName = xmlReader.getControls("Controls","DbUserName", "value");
        String dbPassword = xmlReader.getControls("Controls","DbPassword", "value");
        String oracleDriver = xmlReader.getControls("Controls","OracleDriver", "value");
		
//		application.setAttribute("applicationServerProtocolName", applicationServerProtocolName);
//		application.setAttribute("applicationServerURL", applicationServerURL);
//		application.setAttribute("applicationServerVendorName", applicationServerVendorName);
		application.setAttribute("clientDirName", clientDirName);
		application.setAttribute("defaultBranch", defaultBranch);
		application.setAttribute("sessionTimeout", sessionTimeout);

        application.setAttribute("defaultDateFormat", defaultDateFormat);
        application.setAttribute("defaultTimeFormat", defaultTimeFormat);
		application.setAttribute("defaultDateTimeFormat", defaultDateTimeFormat);
		application.setAttribute("roundingMethod",roundingMethod);
		application.setAttribute("contentType",contentType);
		application.setAttribute("characterEncoding", characterEncoding);
		application.setAttribute("browserEncoding", browserEncoding);
		application.setAttribute("databaseEncoding", databaseEncoding);
		application.setAttribute("defaultLanguage", defaultLanguage);
		application.setAttribute("numOfRecordToShow", numOfRecordToShow);
		
		
		application.setAttribute("dbServerIP", dbServerIP);
		application.setAttribute("dbServiceName", dbServiceName);
		application.setAttribute("dbUserName", dbUserName);
		application.setAttribute("dbPassword", dbPassword);
		application.setAttribute("oracleDriver", oracleDriver);
		
	}

	/**
	 * Displays the parameters loaded from xml into ServletContext.
	 */
	private void showXMLParameters(){
		
//		logger.info("applicationServerProtocolName = " +  application.getAttribute("applicationServerProtocolName"));
//		logger.info("applicationServerURL = " +  application.getAttribute("applicationServerURL"));
//		logger.info("applicationServerVendorName = " +  application.getAttribute("applicationServerVendorName"));

        logger.info("clientDirName = " +  application.getAttribute("clientDirName"));
        logger.info("defaultBranch = " +  application.getAttribute("defaultBranch"));
		logger.info("sessionTimeout = " +  application.getAttribute("sessionTimeout"));
		logger.info("defaultDateFormat = " +  application.getAttribute("defaultDateFormat"));
		logger.info("defaultTimeFormat = " + application.getAttribute("defaultTimeFormat"));
		logger.info("defaultDateTimeFormat = " +  application.getAttribute("defaultDateTimeFormat"));
		logger.info("roundingMethod = " +  application.getAttribute("roundingMethod"));
		logger.info("contentType = " +  application.getAttribute("contentType"));
		logger.info("characterEncoding = " +  application.getAttribute("characterEncoding"));
        logger.info("browserEncoding = " +  application.getAttribute("browserEncoding"));
        logger.info("databaseEncoding = " +  application.getAttribute("databaseEncoding"));
        logger.info("defaultLanguage = " +  application.getAttribute("defaultLanguage"));
        logger.info("numOfRecordToShow = " +  application.getAttribute("numOfRecordToShow"));
        
        logger.info("dbServerIP = " 	+ application.getAttribute("dbServerIP"));
        logger.info("dbServiceName = " 	+ application.getAttribute("dbServiceName"));
        logger.info("dbUserName = " 	+ application.getAttribute("dbUserName"));
        logger.info("dbPassword = " 	+ application.getAttribute("dbPassword"));
        logger.info("oracleDriver = " 	+ application.getAttribute("oracleDriver"));
		
	}

}