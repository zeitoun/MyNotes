package com.dev.admin.common.base;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.guhesan.querycrypt.QueryCrypt;
import com.guhesan.querycrypt.beans.RequestParameterObject;
import com.hdc.sysdev.utils.StringUtil;
import com.optica.admin.Business.AdminBO;
import com.optica.bean.user.UserBean;
import com.optica.constants.common.ActivityType;
import com.optica.vo.user.ActivityLogVO;

public class PageSecureServlet extends PageServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PageSecureServlet.class);
	AdminBO adminBO = (AdminBO)application.getAttribute("adminBO");
	

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		Long formLastAccess = null;
		Long loginTime      = null;

		if (session.getAttribute("loginTime") != null)
			loginTime = Long.parseLong(session.getAttribute("loginTime").toString());
		
		

//		Common commonEjb = (Common)application.getAttribute("commonEjb");
//		int companyCode = Integer.parseInt(application.getAttribute("companyCode").toString());
//
//		if(session.getAttribute("securityHack") != null){
//			if(session.getAttribute("cifNo") != null){
//				commonEjb.logout(companyCode,(String)session.getAttribute("cifNo"),session.getId());
//				session.removeAttribute("cifNo");
//			}
//			session.setAttribute("LoginErrorMsg", "unauthorizeActionMessage");
//			response.sendRedirect("Login");
//			return;
//		}
		
		RequestParameterObject rpo = QueryCrypt.decrypt(request);
		String sessionAlive = StringUtil.nullToEmpty(session.getAttribute("sessionAlive"));
				
		if ((rpo.getParameter("formLastAccess") != null) && !StringUtil.nullToEmpty(rpo.getParameter("formLastAccess")).equals("") && !StringUtil.nullToEmpty(rpo.getParameter("formLastAccess")).equals("-1")){
			formLastAccess = Long.parseLong(rpo.getParameter("formLastAccess").toString());
		}
		

		
		if (sessionAlive.equals("true")){ // the session isn't timed out
			logger.info("\nsession is alive...");
			logger.info("\nsession.getAttribute(userID)...:" + session.getAttribute("userID"));
			
			
			if ((session.getAttribute("userID") != null) && (loginTime != null) && (formLastAccess != null) && loginTime.equals(formLastAccess)){
				logger.info("\nuserID is not null, login time is not null && formlastaccess is also not null...");
				int userID = Integer.parseInt(StringUtil.nullToString(session.getAttribute("userID"),"0"));
				UserBean userInfo= adminBO.getUserDetails(userID, null);
				if(userInfo != null && session.getId().equals(userInfo.getSessionID()) && userInfo.getStatus().equals("A") ){ 
					// the user exist and he is active  and has the same session id as in the database that means he is already logged in
					logger.info("\nuserInfo is not null, sessionID is equal to the one stored in DB and userstatus is active...");
					if(isAccessible()){
						logger.info("\n page is accessible...");
						//the user has privilege to access this servlet
						super.doPost(request, response);
					}else{ //the user doesn't have access for this screen
						logger.info("\nuser has no access to this screen...");
						session.setAttribute("LoginErrorMsg", "Access Denied Message");
						response.sendRedirect("Login");
					}
				}else{ //check if he is already logged in in another session or he is deleted or suspended or logout because a shutdown of the server
					if(userInfo == null){// the user doesn't exist
						logger.info("\nuser does not exist...");
						session.setAttribute("LoginErrorMsg", "System Administrator Error");
						response.sendRedirect("Login");
					}else if(!userInfo.getStatus().equals("A")){ //the user isn't active
						logger.info("\nuser is not active...");
						session.setAttribute("LoginErrorMsg", "Suspended User");
						//logout the user if he is suspended on the time of his navigation of the site
						adminBO.logout(userID, session.getId());
						response.sendRedirect("Login");
					}else if(!session.getId().equals(userInfo.getSessionID()) && userInfo.getSessionID()!=null ) {
						logger.info("\nuser is trying to navigate using different session than the logged in one...");
						//the user is already logged in with another session
						session.setAttribute("LoginErrorMsg", "Logged In With Another Ip");
						
						try { // for logging
							ActivityLogVO activityLogVO = new ActivityLogVO();
							activityLogVO.setActivityDate(new Timestamp(new java.util.Date().getTime()));
							activityLogVO.setActivityDesc("loggedout since he is loggedin from another place");
							activityLogVO.setActivityTypeID(ActivityType.LOGOUT_ACT.value());
							activityLogVO.setReference("");
							activityLogVO.setSessionID(session.getId());
							activityLogVO.setUserID(Integer.parseInt(StringUtil.nullOrEmptyToString(session.getAttribute("userID"), "0")));
							adminBO.logActivity(activityLogVO);
						} catch (NumberFormatException exception) {
							logger.error("error",exception);
//						} catch (RemoteException exception) {
//							logger.error("error",exception);
						}

						response.sendRedirect("Login");
						//The return was commented by Ziad because there's not processing under the response sendRdirect
						//return; 
					}else {// the user is logged out because of shutdown of the server 
						logger.info("\nuser has logged out because of server shutdown...");
						session.setAttribute("LoginErrorMsg", "You Have Been Logged Out By Your Administrator");
						response.sendRedirect("Login");
					}
				}
			}else{//the cifNo isn't in the session
				logger.info("\nuser does not exist...");
				session.setAttribute("LoginErrorMsg", "Please Login Correctly");
				response.sendRedirect("Login");
			}

		}else{ //the sessionAlive is null
			logger.info("\nsession is not alive...");
			session.setAttribute("LoginErrorMsg","Session Time Out");
			response.sendRedirect("Login");
		}
	}

	/**
	 * Constructor of the object.
	 */
	public PageSecureServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}
	
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
