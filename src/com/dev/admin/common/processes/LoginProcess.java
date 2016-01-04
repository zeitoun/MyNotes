package com.dev.admin.common.processes;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.guhesan.querycrypt.QueryCrypt;
import com.hdc.sysdev.utils.SecurityUtil;
import com.hdc.sysdev.utils.StringUtil;
import com.optica.admin.Business.AdminBO;
import com.optica.admin.common.base.BaseServlet;
import com.optica.bean.user.UserBean;
import com.optica.constants.common.ActivityType;
import com.optica.vo.user.ActivityLogVO;


public class LoginProcess extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(LoginProcess.class);
	//used to register only the login , logout actions
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
	public synchronized void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		try{
			setLanguage(request.getSession(),request);
			AdminBO adminBO = (AdminBO)application.getAttribute("adminBO");


			HttpSession session = request.getSession(true);
			String language = (String)session.getAttribute("language");

			//check if the session is New if it's not we will a new session to avoid primary key conflict on the tabel C_USER_SESSIONS
			if(!session.isNew()){
				logger.info("user userName" + session.getAttribute("userName")  + "is trying to log In using old session : old session Id = " + session.getId());
				session.setAttribute("SESSION_INVALIDATED","true");
				session.invalidate();
				session = request.getSession(true);
				if(language != null){
					session.setAttribute("language", language);
				}
				logger.info("invalidating old Session And Creating a New Session : new  session Id = " + session.getId());
			}else{
				logger.info("session is New : session Id =" + session.getId() + " for the user: " + session.getAttribute("userName"));
			}

			// removing 'siteAccessTime' parameter that is used only for outside the LOGGED IN area
			session.removeAttribute("siteAccessTime");
			

			String userName = StringUtil.nullToEmpty(request.getParameter("userName"));
//			String password = StringUtil.nullToEmpty(request.getParameter("password"));
			String encryptedPassword = SecurityUtil.byteArrayToHexString(SecurityUtil.getEncryptedString((request.getParameter("password")!= null ? request.getParameter("password").toString()+userName : "")));

			boolean isAuthenticated = adminBO.login(userName, encryptedPassword);

			if (isAuthenticated){

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyDDMM");
				UserBean userBean = adminBO.getUserDetails(0, userName);
				
				logger.info("\nLOGINPAGE userID: "+userBean.getUserID());
				logger.info("\nLOGINPAGE lastAccessTime: "+userBean.getLastAccessTime());
				session.setAttribute("userID", userBean.getUserID());
				session.setAttribute("userName", userBean.getUserName());
				session.setAttribute("userRole", userBean.getRoleBean().getRoleID());
				session.setAttribute("sessionAlive", "true");
				session.setAttribute("lastAccessTime", userBean.getLastAccessTime());
				
				Timestamp loginTime = new Timestamp(new java.util.Date().getTime());
				String loginTimeStr = sdf.format(loginTime);
				session.setAttribute("loginTime", loginTimeStr);
				
									
				adminBO.updateUserSession(Integer.parseInt(userBean.getUserID()), session.getId(), loginTime/*, request.getRemoteHost(), */);
				
				
				StringBuffer requestString=new StringBuffer();
				requestString.append("formLastAccess="+loginTimeStr);
				String encryptedString = QueryCrypt.encrypt(request, requestString.toString());
				
				
				ActivityLogVO activityLogVO = new ActivityLogVO();
				activityLogVO.setActivityDate(new Timestamp(new java.util.Date().getTime()));
				activityLogVO.setActivityDesc("User Successfully LoggedIn");
				activityLogVO.setActivityTypeID(ActivityType.LOGIN_ACT.value());
				activityLogVO.setReference("");
				activityLogVO.setSessionID(session.getId());
				activityLogVO.setUserID(Integer.parseInt(StringUtil.nullOrEmptyToString(session.getAttribute("userID"), "0")));
				adminBO.logActivity(activityLogVO);
				
				
				response.sendRedirect("Home?"+encryptedString);
				

			} else{
				session.setAttribute("LoginErrorMsg", "Invalid Username/Password, please try again");
				response.sendRedirect("Login");

			}
		}catch (Exception e) {
			logger.error("ERROR", e); 
			response.sendRedirect("ErrorHandler");
		}
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
	public synchronized void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
