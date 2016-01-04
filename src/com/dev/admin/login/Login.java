package com.dev.admin.login;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hdc.sysdev.utils.StringUtil;
import com.optica.admin.Business.AdminBO;
import com.optica.admin.common.base.PageServlet;

public class Login extends PageServlet {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Login.class);
	AdminBO adminBO = (AdminBO)application.getAttribute("adminBO");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyDDMM");

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

		logger.info("\n\na new request reached Login Page...");

		HttpSession session = request.getSession(true);
		try{

			this.setLanguage(session, request);
			this.setIncludeMenu(false);

			this.setBrowserEncoding();
			this.setDatabaseEncoding();
			this.setIncludeHorizontalUserInfo(false);


			StringBuffer scriptIncludeBuffer = new StringBuffer();
			scriptIncludeBuffer.append("	<script language=\"Javascript\" src=\"js/Login/Login.js\"></script>");
			scriptIncludeBuffer.append("		<link rel=\"stylesheet\" type=\"text/css\"  href=\"clientassets/files_optica/css/font.css\">");
			setScriptIncludes(new String(scriptIncludeBuffer));
			scriptIncludeBuffer = null;

			StringBuffer jsInstanceBuffer = new StringBuffer();
			jsInstanceBuffer.append(" 		formIns = getFormUtil(\"reqForm\");");
			jsInstanceBuffer.append(" 		numberInstance = getNumberUtil();");
			setJsInstance(new String(jsInstanceBuffer));
			jsInstanceBuffer = null;

			String actionType = StringUtil.nullToEmpty(request.getParameter("actionType"));

			if(actionType.equals("login")){
				String userName = StringUtil.nullToEmpty(request.getParameter("userName"));
				String password = StringUtil.nullToEmpty(request.getParameter("password"));
/*				logger.info("username: "+userName + "\npassword: "+password);

				boolean isAuthenticated=true;
				if (isAuthenticated){

					Timestamp loginTime = new Timestamp(new java.util.Date().getTime());
					String loginTimeStr = sdf.format(loginTime);
					session.setAttribute("loginTime", loginTimeStr);*/

					StringBuffer requestString=new StringBuffer();
					requestString.append("userName="+userName);
					requestString.append("&password="+password);
					
					
//					session.setAttribute("userID", userName.equals("user") ? "1" : "2");

					response.sendRedirect("LoginProcess?"+requestString);

				/*} else{
					session.setAttribute("LoginErrorMsg", "Invalid Username/Password, please try again");

				}*/
			}

			setTitle("Login");
			setValueBean("LoginBean");

			this.constructPage(request, session, response);

			super.doPost(request, response);

		}catch (Exception e) {
			logger.error("ERROR", e); 
			response.sendRedirect("ErrorHandler");
		}
	}



	public void constructPage(HttpServletRequest request, HttpSession session, HttpServletResponse response){
		StringBuffer pageConstructor = new StringBuffer();

			//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat((String) application.getAttribute("defaultDateFormat"));


			String LoginErrorMsg = StringUtil.nullToEmpty(session.getAttribute("LoginErrorMsg"));
			
			pageConstructor.append("<input name=\"systemDate\" id=\"systemDate\" type=\"hidden\" onpaste=\"return false\" value=\""+new java.util.Date()+"\" /> ");
//			pageConstructor.append("<table class=\"mainTableWrapper\">");
			pageConstructor.append("<table width=\"100%\" align=\"top\" border=\"0\">");
			pageConstructor.append("<tr>");
			pageConstructor.append("	<td width=\"60%\" valign=\"top\" align=\"left\">");
			pageConstructor.append("		<table style=\"border-spacing: 10px; width: 80%; border: 0px;\">");
			pageConstructor.append("		<tr>");
			pageConstructor.append("			<td width=\"100%\" align=\"center\">");
			pageConstructor.append("				<h1>INTRODUCTION TO OPTICA</h1>");
			pageConstructor.append("			</td>");
			pageConstructor.append("		</tr>");
			pageConstructor.append("		<tr>");
			pageConstructor.append("			<td width=\"100%\" align=\"center\">");
			pageConstructor.append("				<video poster=\""+clientDirName+"/images/LoyaltyPoster.jpg\" controls=\"controls\" width=\"500px\" height=\"280px\" >");
			pageConstructor.append("					<source src=\""+clientDirName+"/videos/test.mp4\" type=\"video/mp4\">");
			pageConstructor.append("						Your browser does not support the video tag.");
			pageConstructor.append("				</video>");
			pageConstructor.append("			</td>");
			pageConstructor.append("		</tr>");
			pageConstructor.append("		</table>");
			pageConstructor.append("	</td>");
			pageConstructor.append("	<td width=\"40%\" valign=\"top\" align=\"left\">");
			pageConstructor.append("		<table style=\"border-spacing: 10px; width: 80%; border: 0px;\">");
			pageConstructor.append("		<tr>");
			pageConstructor.append("			<td width=\"100%\" align=\"center\">");
			pageConstructor.append("				<h1>LOGIN</h1>");
			pageConstructor.append("			</td>");
			pageConstructor.append("		</tr>");
			pageConstructor.append("		<tr>");
			pageConstructor.append("			<td width=\"100%\" align=\"center\">");
			pageConstructor.append("				<fieldset class=\"subFSet\">");
			pageConstructor.append("					<legend class=\"subFSet\" >Login</legend>");
			pageConstructor.append("					<table class=\"mainFSetTable\" >");
			pageConstructor.append("					<tr>");
			pageConstructor.append("						<td width=\"100%\" align=\"center\">");
			pageConstructor.append("							<table class=\"buttonsTableUp\" border=\"0\" >");
			if (!LoginErrorMsg.equals(""))
			{
				pageConstructor.append("							<tr>");
				pageConstructor.append("              					<td colspan=2 align=\"center\" class=\"warning\" nowrap=\"nowrap\"   >");
				pageConstructor.append(LoginErrorMsg);
				pageConstructor.append("								</td>");
				pageConstructor.append("							</tr>");
				session.removeAttribute("LoginBean");
			}
			pageConstructor.append("							<tr>");
			pageConstructor.append("								<td align=\"left\">");
			pageConstructor.append("<input class=\"loginInput\" placeholder=\"Username\" name=\"userName\" id=\"userName\" type=\"text\" required=\"true\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return (submitEnter(event) && formIns.doLowSecurityFilter(event))\" onpaste=\"return false\" tabindex=\"1\" autofocus=\"true\" /> ");
			pageConstructor.append("								</td>");
			pageConstructor.append("							</tr>");
			pageConstructor.append("							<tr>");
			pageConstructor.append("                    			<td align=\"left\">");
			pageConstructor.append("<input class=\"loginInput\" placeholder=\"Password\" name=\"password\" type=\"password\" required=\"true\" id=\"password\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return (submitEnter(event) && formIns.doLowSecurityFilter(event))\" onpaste=\"return false\" tabindex=\"2\"  /> ");
			pageConstructor.append("								</td>");
			
			pageConstructor.append("							</tr>");
			pageConstructor.append("  							</table>");
			pageConstructor.append("							<hr />");
			pageConstructor.append("						</td>");
			pageConstructor.append("					</tr>");
			pageConstructor.append("					<tr>");
			pageConstructor.append("               			<td align=\"center\">");
//			pageConstructor.append("<input type=\"button\" id=\"submitButton\" name=\"submitButton\" class=\"standardBoldButton\" tabindex=50 value=\"LOGIN\" onclick=\"submitForm('document.reqForm', 'login');\" tabindex=\"22\" />");
			pageConstructor.append("<a href=\"#\" class=\"btn_large btn_blue btn_awesome\" rel=\"nofollow\" onclick=\"submitForm('document.reqForm', 'login');\">LOGIN</a>");
			pageConstructor.append("						</td>");
			pageConstructor.append("					</tr>");
			pageConstructor.append("					</table>");
			pageConstructor.append("				</fieldset>");
			pageConstructor.append("				<input type=\"hidden\" name=\"actionType\" id=\"actionType\" value=\"\">");
			pageConstructor.append("			</td>");
			pageConstructor.append("		</tr>");
			pageConstructor.append("	</table>");
			pageConstructor.append("</tr>");
			pageConstructor.append("</table>");

			setPageConstructor(pageConstructor);
	}


	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

	/**
	 * Constructor of the object.
	 */
	public Login() {
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

}
