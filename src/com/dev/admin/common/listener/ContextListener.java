package com.dev.admin.common.listener;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


/**
 * The listener interface for receiving context events.
 * The class that is interested in processing a context
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addContextListener<code> method. When
 * the context event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ContextEvent
 * @author HaythamDouaihy
 */
public final class ContextListener implements ServletContextListener {
	//~ Static fields/initializers *******************************************************************

	/** The logger. */
	public static Logger logger = Logger.getLogger(ContextListener.class);
	
	/** The login logout logging. */
	public static Logger loginLogoutLogging = Logger.getLogger("LoginLogout");
	
	/** The application. */
	protected static ServletContext application = null;

	//~ Methods **************************************************************************************

	/**
	 * Gets the Application Context.
	 *
	 * @return application
	 */
	public static ServletContext getApplication() {
		return application;
	}


	/**
	 * Runs at the application Deployment Time; set the Application Context and log the action.
	 *
	 * @param event the event
	 */
	public void contextInitialized(ServletContextEvent event) {
		application = event.getServletContext();
		BasicConfigurator.configure();
		logger.info("Context opened");
		loginLogoutLogging.info("the server is up");
	}


	/**
	 * Runs at the application Undeployment Time; set Logout all logged in users and log the action.
	 *
	 * @param event the event
	 */
	public void contextDestroyed(ServletContextEvent event) {
		application = getApplication();
//		loginLogoutLogging.info("===========================");
//		loginLogoutLogging.info("===========================");
//		loginLogoutLogging.info("the web application is down , maybe the server was shutdown or the war is undeployed");
//		loginLogoutLogging.info("===========================");
//		loginLogoutLogging.info("===========================");
		logger.info("Context destroyed");
	}
}
