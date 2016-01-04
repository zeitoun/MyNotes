package com.dev.admin.common.listener;


import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;



/**
 * The listener interface for receiving mySession events.
 * The class that is interested in processing a mySession
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addMySessionListener<code> method. When
 * the mySession event occurs, that object's appropriate
 * method is invoked.
 *
 * @see MySessionEvent
 * @author HaythamDouaihy
 */
public final class MySessionListener implements HttpSessionListener {
	//~ Static fields/initializers *******************************************************************

	/** The logger. */
	public static Logger logger = Logger.getLogger(MySessionListener.class);
	
	/** The login logout logging. */
	public static Logger loginLogoutLogging = Logger.getLogger("LoginLogout");

	//~ Instance fields ******************************************************************************

	/** The cif no. */
	String cifNo;
	
	/** The inactive time. */
	int inactiveTime;
	
	/** The application. */
	private ServletContext application = null;

	//~ Methods **************************************************************************************

	/**
	 * Runs when the session is created; Sets the Session Time out Time.
	 *
	 * @param event the event
	 */
	public void sessionCreated(HttpSessionEvent event) {
		logger.info("HttpSession " + event.getSession().getId() + " created at : " + Calendar.getInstance().getTime());
		application = event.getSession().getServletContext();

		int maxInactiveInterval = Integer.parseInt(application.getAttribute("sessionTimeout").toString());
		maxInactiveInterval  = maxInactiveInterval * 60 ;
		event.getSession().setMaxInactiveInterval(maxInactiveInterval);
		logger.info("Set Max Inactive Interval Of user Session : MaxInactiveInterval = " + event.getSession().getMaxInactiveInterval());
	}


	/**
	 * Runs when the session is destroyed; Logged out the related login user.
	 *
	 * @param event the event
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
		application = event.getSession().getServletContext();
		logger.info("HttpSession " + event.getSession().getId() + " destroyed at : " + Calendar.getInstance().getTime());
	}
}
