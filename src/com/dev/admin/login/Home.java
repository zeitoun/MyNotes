package com.dev.admin.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.guhesan.querycrypt.QueryCrypt;
import com.guhesan.querycrypt.beans.RequestParameterObject;
import com.hdc.sysdev.utils.StringUtil;
import com.optica.admin.common.base.PageSecureServlet;

public class Home extends PageSecureServlet {
	/**
	 * 
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Logger logger = Logger.getLogger(Home.class);
	/**
	 * 
	 * 
	 * 
	 * Constructor of the object.
	 */
	public Home() {
		super();
	}
	/**
	 * 
	 * 
	 * 
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	/**
	 * 
	 * 
	 * 
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	/**
	 * 
	 * 
	 * 
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
	 * 
	 * 
	 * 
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
		try{
			if(session.getAttribute("userID")!= null ){


				String requestString = "formLastAccess=" + session.getAttribute("loginTime");

				setLanguage(session,request);
				setBrowserEncoding();
				setDatabaseEncoding();
				this.setIncludeMenu(false);
				setIncludeHorizontalUserInfo(true);

				StringBuffer scriptIncludeBuffer = new StringBuffer();
				scriptIncludeBuffer.append("			<script language=\"Javascript\" src=\"js/Bundle/Bundle.js\"></script> ");
				scriptIncludeBuffer.append("		<link rel=\"stylesheet\" type=\"text/css\"  href=\"clientassets/files_xperteam/css/font.css\">");
				setScriptIncludes(new String(scriptIncludeBuffer));

				StringBuffer jsInstanceBuffer = new StringBuffer();
				jsInstanceBuffer.append(" formIns = getFormUtil(\"reqForm\");");
				jsInstanceBuffer.append(" numberInstance = getNumberUtil();");
				setJsInstance(new String(jsInstanceBuffer));
				jsInstanceBuffer = null;

				setOnLoadFunctions("document.getElementById('configSection').className += 'CURRENT';");


				String repAction = "";
				RequestParameterObject rpo = QueryCrypt.decrypt(request);

				String actionType = StringUtil.nullToEmpty(request.getParameter("actionType"));
				logger.info("actionType: " + actionType);

				if(actionType.equals(""))
					repAction = StringUtil.nullToEmpty(rpo.getParameter("repAction"));



				setTitle("Home");

				constructPage(request, requestString, repAction, actionType);

			}

			super.doPost(request, response);	
		} catch (Exception e) {
			logger.error("ERROR", e); 
			response.sendRedirect("ErrorHandler");
		}
	}
	/**
	 * 
	 * 
	 * 
	 * @param request
	 * @param requestString
	 * @param repAction
	 * @param actionType
	 * @throws Exception
	 */
	private void constructPage(HttpServletRequest request, String requestString, String repAction, String actionType) throws Exception{

		StringBuffer pageConstructor = new StringBuffer();
		RequestParameterObject rpo = QueryCrypt.decrypt(request);
		String encryptedString = QueryCrypt.encrypt(request, requestString);

		String userType = StringUtil.nullToString(request.getSession().getAttribute("userType"), "user");
		
		

		pageConstructor.append("<input type=\"hidden\" name=\"actionType\" id=\"actionType\" value=\"\">");
		pageConstructor.append("<input type=\"hidden\" name=\"rootActionType\" id=\"rootActionType\" value=\""+actionType+"\">");
		pageConstructor.append("<table class=\"mainTableWrapper\">");
		pageConstructor.append("<tr>");
		pageConstructor.append("	<td width=\"40%\" align=\"center\" valign=\"top\">");
		pageConstructor.append("		<fieldset class=\"mainFSet\">");
		pageConstructor.append("			<legend class=\"mainFSet\" >Home</legend>");
		pageConstructor.append("			<table class=\"mainFSetTable\">");
		pageConstructor.append("	  		<tr>");
		pageConstructor.append("				<td valign=\"top\">");
		pageConstructor.append("		 			<table class=\"mainReqTable\">");
		pageConstructor.append("					<tr>");
		pageConstructor.append("    	       			<td width=\"50%\" >");
		pageConstructor.append("							<fieldset class=\"subFSet\">");
		pageConstructor.append("								<legend>Customers</legend>");
		pageConstructor.append("			 					<table class=\"subFSetTable\">");
		pageConstructor.append("								<tr>");
		pageConstructor.append("                   					<td align=\"center\">");
		pageConstructor.append("										<span class=\"FrmCompTitle\">");
		pageConstructor.append("											CUSTOMER");
		pageConstructor.append("                           				</span>");
		pageConstructor.append("                           			</td>");
		pageConstructor.append("								</tr>");
		pageConstructor.append("								<tr>");
		pageConstructor.append("                   					<td align=\"center\" >");
		pageConstructor.append("                   						<table>");
		pageConstructor.append("                   							<tr>");
		pageConstructor.append("                   								<td>");
		pageConstructor.append("													<span class=\"FrmCompTitle\">");
		pageConstructor.append("    							 						<a href=\"CustomerList?"+encryptedString+"\"> <img src=\""+clientDirName+"/icons/customer.png\" height=\"80\" width=\"140px\" /></a>");
		pageConstructor.append("   	    	               							</span>");
		pageConstructor.append("            	           						</td>");
		pageConstructor.append("                   								<td>&nbsp;");
		pageConstructor.append("                   								</td>");
		pageConstructor.append("                   								<td>");
		pageConstructor.append("                   									<ul>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">PROFILE</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">OPTOMETRY FILE</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">PRESCRIPTIONS</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a class=\"tooltip\" href=\"#?"+encryptedString+"\">");
		pageConstructor.append("																OTHERS");
		pageConstructor.append("																<span class=\"FrmCompTitle\">");
		pageConstructor.append("																<table border=\"0\" cellpadding=0 cellspacing=0>");
		pageConstructor.append("																	<tr>");
		pageConstructor.append("																		<td>");
		pageConstructor.append("																			<img class=\"callout\" src=\""+clientDirName+"/icons/images.jpg\" width=\"30px\" height=\"50px\" /> ");
		pageConstructor.append("																		</td>");
		pageConstructor.append("																	</tr>");
		pageConstructor.append("																	<tr>");
		pageConstructor.append("																		<td>");
		pageConstructor.append("																	ORDERS");
		pageConstructor.append("																		</td>");
		pageConstructor.append("																	</tr>");
		pageConstructor.append("																	<tr>");
		pageConstructor.append("																		<td>");
		pageConstructor.append("																	BILLS");
		pageConstructor.append("																		</td>");
		pageConstructor.append("																	</tr>");
		pageConstructor.append("																	<tr>");
		pageConstructor.append("																		<td>");
		pageConstructor.append("																	PROFESSIONS");
		pageConstructor.append("																		</td>");
		pageConstructor.append("																	</tr>");
		pageConstructor.append("																	<tr>");
		pageConstructor.append("																		<td>");
		pageConstructor.append("																	VISITS");
		pageConstructor.append("																		</td>");
		pageConstructor.append("																	</tr>");
		pageConstructor.append("																</table>");
		pageConstructor.append("																</span>");
		pageConstructor.append("															</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("   	    	               							</ul>");
		pageConstructor.append("            	           						</td>");
		pageConstructor.append("											</tr>");
		pageConstructor.append("										</table>");
		pageConstructor.append("            	           			</td>");
		pageConstructor.append("								</tr>");
		pageConstructor.append("								</table>");
		pageConstructor.append("							</fieldset >");
		pageConstructor.append("						</td>");
		pageConstructor.append("    	       			<td width=\"50%\" >");
		pageConstructor.append("							<fieldset class=\"subFSet\">");
		pageConstructor.append("								<legend>Suppliers</legend>");
		pageConstructor.append("			 					<table class=\"subFSetTable\">");
		pageConstructor.append("								<tr>");
		pageConstructor.append("                   					<td align=\"center\">");
		pageConstructor.append("										<span class=\"FrmCompTitle\">");
		pageConstructor.append("											SUPPLIER");
		pageConstructor.append("                           				</span>");
		pageConstructor.append("                           			</td>");
		pageConstructor.append("								</tr>");
		pageConstructor.append("								<tr>");
		pageConstructor.append("                   					<td align=\"center\" >");
		pageConstructor.append("                   						<table>");
		pageConstructor.append("                   							<tr>");
		pageConstructor.append("                   								<td>");
		pageConstructor.append("													<span class=\"FrmCompTitle\">");
		pageConstructor.append("    							 						<a href=\"#\"> <img src=\""+clientDirName+"/icons/supplier.png\" height=\"80\" width=\"140px\" /></a>");
		pageConstructor.append("   	    	               							</span>");
		pageConstructor.append("            	           						</td>");
		pageConstructor.append("                   								<td>&nbsp;");
		pageConstructor.append("                   								</td>");
		pageConstructor.append("                   								<td>");
		pageConstructor.append("                   									<ul>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#\">SUPPLIERS</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#\">BRANDS</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#\">INVOICES</a>");
		pageConstructor.append("   	    	               								</li>");				
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#\">OTHERS</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("   	    	               							</ul>");
		pageConstructor.append("            	           						</td>");
		pageConstructor.append("											</tr>");
		pageConstructor.append("										</table>");
		pageConstructor.append("            	           			</td>");
		pageConstructor.append("								</tr>");
		pageConstructor.append("								</table>");
		pageConstructor.append("							</fieldset >");
		pageConstructor.append("						</td>");
		pageConstructor.append("					</tr>");
		
		
		pageConstructor.append("					<tr>");
		pageConstructor.append("    	       			<td>");
		pageConstructor.append("							<fieldset class=\"subFSet\">");
		pageConstructor.append("								<legend>Products</legend>");
		pageConstructor.append("			 					<table border=\"0\" class=\"subFSetTable\">");
		pageConstructor.append("								<tr>");
		pageConstructor.append("                   					<td align=\"center\">");
		pageConstructor.append("										<span class=\"FrmCompTitle\">");
		pageConstructor.append("											PRODUCT");
		pageConstructor.append("                           				</span>");
		pageConstructor.append("                           			</td>");
		pageConstructor.append("								</tr>");
		pageConstructor.append("								<tr>");
		pageConstructor.append("                   					<td align=\"center\">");
		pageConstructor.append("                   						<table>");
		pageConstructor.append("                   							<tr>");
		pageConstructor.append("                   								<td>");
		pageConstructor.append("													<span class=\"FrmCompTitle\">");
		pageConstructor.append("    							 						<a href=\"#?"+encryptedString+"\" > <img src=\""+clientDirName+"/icons/Product.jpg\" height=\"80\" width=\"140px\" /></a>");
		pageConstructor.append("   	    	               							</span>");
		pageConstructor.append("            	           						</td>");
		pageConstructor.append("                   								<td>&nbsp;");
		pageConstructor.append("                   								</td>");
		pageConstructor.append("                   								<td>");
		pageConstructor.append("                   									<ul>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">LENSES</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">CONTACT LENSES</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">FRAMES</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">OTHERS</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("   	    	               							</ul>");
		pageConstructor.append("            	           						</td>");
		pageConstructor.append("											</tr>");
		pageConstructor.append("										</table>");
		pageConstructor.append("            	           			</td>");
		pageConstructor.append("								</tr>");
		pageConstructor.append("								</table>");
		pageConstructor.append("							</fieldset >");
		pageConstructor.append("						</td>");
		pageConstructor.append("    	       			<td>");
		pageConstructor.append("							<fieldset class=\"subFSet\">");
		pageConstructor.append("								<legend>Accounting</legend>");
		pageConstructor.append("			 					<table class=\"subFSetTable\">");
		pageConstructor.append("								<tr>");
		pageConstructor.append("                   					<td align=\"center\" width=\"10%\">");
		pageConstructor.append("										<span class=\"FrmCompTitle\">");
		pageConstructor.append("											STOCK");
		pageConstructor.append("                           				</span>");
		pageConstructor.append("                           			</td>");
		pageConstructor.append("								</tr>");
		pageConstructor.append("								<tr>");
		pageConstructor.append("                   					<td align=\"center\">");
		pageConstructor.append("                   						<table>");
		pageConstructor.append("                   							<tr>");
		pageConstructor.append("                   								<td>");
		pageConstructor.append("													<span class=\"FrmCompTitle\">");
		pageConstructor.append("    							 						<a href=\"#\" > <img src=\""+clientDirName+"/icons/Stock.png\" height=\"80\" width=\"140px\" /></a>");
		pageConstructor.append("   	    	               							</span>");
		pageConstructor.append("            	           						</td>");
		pageConstructor.append("                   								<td>&nbsp;");
		pageConstructor.append("                   								</td>");
		pageConstructor.append("                   								<td>");
		pageConstructor.append("                   									<ul>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">ITEMS</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">ITEM PACKS</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">SALES</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">OTHERS</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("   	    	               							</ul>");
		pageConstructor.append("            	           						</td>");
		pageConstructor.append("											</tr>");
		pageConstructor.append("										</table>");
		pageConstructor.append("            	           			</td>");
		pageConstructor.append("								</tr>");
		pageConstructor.append("								</table>");
		pageConstructor.append("							</fieldset >");
		pageConstructor.append("						</td>");
		pageConstructor.append("					</tr>");
		
		
		pageConstructor.append("					<tr>");
		pageConstructor.append("    	       			<td>");
		pageConstructor.append("							<fieldset class=\"subFSet\">");
		pageConstructor.append("								<legend>Analytics</legend>");
		pageConstructor.append("			 					<table class=\"subFSetTable\">");
		pageConstructor.append("								<tr>");
		pageConstructor.append("                   					<td align=\"center\" width=\"10%\">");
		pageConstructor.append("										<span class=\"FrmCompTitle\">");
		pageConstructor.append("											ANALYTICS AND MARKETING");
		pageConstructor.append("                           				</span>");
		pageConstructor.append("                           			</td>");
		pageConstructor.append("								</tr>");
		pageConstructor.append("								<tr>");
		pageConstructor.append("                   					<td align=\"center\">");
		pageConstructor.append("                   						<table>");
		pageConstructor.append("                   							<tr>");
		pageConstructor.append("                   								<td>");
		pageConstructor.append("													<span class=\"FrmCompTitle\">");
		pageConstructor.append("    							 						<a href=\"#\" > <img src=\""+clientDirName+"/icons/marketing.png\" height=\"80\" width=\"140px\" /></a>");
		pageConstructor.append("   	    	               							</span>");
		pageConstructor.append("            	           						</td>");
		pageConstructor.append("                   								<td>&nbsp;");
		pageConstructor.append("                   								</td>");
		pageConstructor.append("                   								<td>");
		pageConstructor.append("                   									<ul>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">NOTIFICATIONS</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">PROMOTIONS</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">OTHERS</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("   	    	               							</ul>");
		pageConstructor.append("            	           						</td>");
		pageConstructor.append("											</tr>");
		pageConstructor.append("										</table>");
		pageConstructor.append("            	           			</td>");
		pageConstructor.append("								</tr>");
		pageConstructor.append("								</table>");
		pageConstructor.append("							</fieldset >");
		pageConstructor.append("						</td>");
		pageConstructor.append("    	       			<td>");
		pageConstructor.append("							<fieldset class=\"subFSet\">");
		pageConstructor.append("								<legend>Users</legend>");
		pageConstructor.append("			 					<table class=\"subFSetTable\">");
		pageConstructor.append("								<tr>");
		pageConstructor.append("                   					<td align=\"center\" width=\"10%\">");
		pageConstructor.append("										<span class=\"FrmCompTitle\">");
		pageConstructor.append("											ADMINISTRATION");
		pageConstructor.append("                           				</span>");
		pageConstructor.append("                           			</td>");
		pageConstructor.append("								</tr>");
		pageConstructor.append("								<tr>");
		pageConstructor.append("                   					<td align=\"center\">");
		pageConstructor.append("                   						<table>");
		pageConstructor.append("                   							<tr>");
		pageConstructor.append("                   								<td>");
		pageConstructor.append("													<span class=\"FrmCompTitle\">");
		pageConstructor.append("    							 						<a href=\"#?"+encryptedString+"\" > <img src=\""+clientDirName+"/icons/business_user.png\" height=\"80\" width=\"140px\" /></a>");
		pageConstructor.append("   	    	               							</span>");
		pageConstructor.append("            	           						</td>");
		pageConstructor.append("                   								<td>&nbsp;");
		pageConstructor.append("                   								</td>");
		pageConstructor.append("                   								<td>");
		pageConstructor.append("                   									<ul>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">CREATE USER</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">ACCESS RIGHTS</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">FUNCTIONS</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("														<li>");
		pageConstructor.append("    							 							<a href=\"#?"+encryptedString+"\">OTHERS</a>");
		pageConstructor.append("   	    	               								</li>");
		pageConstructor.append("   	    	               							</ul>");
		pageConstructor.append("            	           						</td>");
		pageConstructor.append("											</tr>");
		pageConstructor.append("										</table>");
		pageConstructor.append("            	           			</td>");
		pageConstructor.append("								</tr>");
		pageConstructor.append("								</table>");
		pageConstructor.append("							</fieldset >");
		pageConstructor.append("						</td>");
		pageConstructor.append("					</tr>");
		

		pageConstructor.append("					</table>");
		pageConstructor.append("					<hr />");
		pageConstructor.append("				</td>");
		pageConstructor.append("			</tr>");
		pageConstructor.append("			</table>");
		pageConstructor.append("		</fieldset>");
		pageConstructor.append("	</td>");

		pageConstructor.append("</tr>");
		pageConstructor.append("</table>");


		setPageConstructor(pageConstructor);
	}

}
