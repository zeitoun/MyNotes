package com.dev.admin.common.processes;

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
import com.optica.admin.common.base.BaseServlet;
import com.optica.constants.common.ActivityType;
import com.optica.vo.user.ActivityLogVO;

public class LogoutProcess extends BaseServlet {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LogoutProcess.class);
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

		try{
			//Session
			HttpSession session = request.getSession(true);
			String sessionAlive = (String) session.getAttribute("sessionAlive");
			boolean isLoggedOut = false;
			
			Long formLastAccess = null;
			Long loginTime      = null;
			
			RequestParameterObject rpo = QueryCrypt.decrypt(request);

			if (session.getAttribute("loginTime") != null)
				loginTime = Long.parseLong(session.getAttribute("loginTime").toString());
			
			if (!StringUtil.nullToEmpty(rpo.getParameter("formLastAccess")).equals("") && !StringUtil.nullToEmpty(rpo.getParameter("formLastAccess")).equals("-1")){
				formLastAccess = Long.parseLong(rpo.getParameter("formLastAccess").toString());
			}

			if (sessionAlive != null) {
				if(session.getAttribute("userID") != null){
					int userID = Integer.parseInt(StringUtil.nullToString(session.getAttribute("userID"),"0"));
					if ((loginTime != null) && (formLastAccess != null) && (loginTime.toString().equals(formLastAccess.toString()))){
	
						isLoggedOut = adminBO.logout(userID,session.getId());
						logger.info("userID "+session.getAttribute("userID")+" isLoggedOut= " +  isLoggedOut);
						
						ActivityLogVO activityLogVO = new ActivityLogVO();
						activityLogVO.setActivityDate(new Timestamp(new java.util.Date().getTime()));
						activityLogVO.setActivityDesc("Successfully Logged Out");
						activityLogVO.setActivityTypeID(ActivityType.LOGOUT_ACT.value());
						activityLogVO.setReference("");
						activityLogVO.setSessionID(session.getId());
						activityLogVO.setUserID(Integer.parseInt(StringUtil.nullOrEmptyToString(session.getAttribute("userID"), "0")));
						adminBO.logActivity(activityLogVO);
	
						session.removeAttribute("userID");
						session.setAttribute("LoginErrorMsg", "You Have Successfully Logged Out");
						response.sendRedirect("Login");
					}else{
						//(Cross Site Forgery)This is a flag in case there is a hacking the session of the real user won't be invalidated
						String requestString="hacking=1";
						session.setAttribute("LoginErrorMsg", "Please Login Correctly");
						response.sendRedirect("Login?"+requestString);
					}
				}else{//if the user press the Back   -HD- The user is already logout			
					session.setAttribute("LoginErrorMsg", "Please Login Correctly");
					response.sendRedirect("Login");
				}
			}else{
				session.setAttribute("LoginErrorMsg","Session Time Out");
				response.sendRedirect("Login");
			}
		}catch (Exception e) {
			logger.error("ERROR", e); 
			response.sendRedirect("ErrorHandler");
		}
		logger.info("user has been logged out properly");
	}

	/**
	 * Constructor of the object.
	 */
	public LogoutProcess() {
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
