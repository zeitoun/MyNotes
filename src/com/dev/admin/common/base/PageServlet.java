/*
 * Copyright
 * ------------------------------------------------------------------------
 * (C) Copyright 2006, Xperteam
 *
 * Xperteam retains all ownership rights to this source code. No
 * warranty is expressed or implied by Xperteam, if Xperteam grants
 * the right to use or re-use this source code.
 * ------------------------------------------------------------------------
 */
package com.dev.admin.common.base;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.guhesan.querycrypt.QueryCrypt;
import com.hdc.sysdev.utils.StringUtil;


/**
 * This class is the base class of all pages with a common design of Header+Page+Footer
 * It draws the general structure of the page
 * 
 * @author Haythamdouaihy
 */
public class PageServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	//	private static final Logger logger=Logger.getLogger(PageServlet.class);
	
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(StringUtil.nullOrEmptyToString(application.getAttribute("defaultDateFormat"), "dd/MM/yyyy"));
	SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(StringUtil.nullOrEmptyToString(application.getAttribute("defaultDateTimeFormat"), "dd/MM/yyyy HH:Mi:SS"));

	private Long formLastAccess;

	private boolean includeLeftMenu = false;
	private boolean includeRightMenu = false;
	private boolean includeRightNewsFeed = false;
	private boolean includeHeader = true;
	private boolean includeFooter = true;
	private boolean includeHorizontalUserInfo = true;

	//these parameters are for Header
	private String title;
	private String currentServletName;
	private boolean includeMenu = true;
	private boolean includeLanguage = false;

	private boolean includeJsAlertTranslation = false;

	private String scriptIncludes  = new String();
	private String scriptFunctions = new String();
	private String jsInstance = new String();

	private StringBuffer pageConstructor = new StringBuffer();
	private StringBuffer leftMenuConstructor = new StringBuffer();
	private StringBuffer rightMenuConstructor = new StringBuffer();

	String  onLoadFunctions   = "";
	String  onUnloadFunctions = "";



	/**
	 * Builds the whole page including the header, page, footer
	 * It includes the different encoding and all common settings.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException{

		super.doPost(request, response);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache,post-check=0,pre-check=0,no-store,private");
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
		String htmlDirection = "ltr";
//		HttpSession session = request.getSession(true);

		PrintWriter out = response.getWriter();
		
		//begin html5 tag
//		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
		out.println("<!DOCTYPE HTML>");
		//end html5 tag
		out.println("<html "+htmlDirection+" " + "xmlns=\"http://www.w3.org/1999/xhtml>");
		out.println("<head>");
		out.println("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1; no-cache; \" />");
//		out.println("		<link rel=\"icon\" href=\"clientassets/files_optica/assets/common/favicon.ico\" />");
		out.println("		<base href=\"#\" target=\"_self\"> ");
		out.println(" 		<!-- OPTICA 0.0.0.1-->");
		out.println("       <title> OPTICA | "+getTitle()+" </title>");		
		out.println(		new String(includeScripts()));
		out.println(		new String(constructScriptFunctions()));
		out.println("</head>");

		out.println("<body style=\"overflow: auto;\" onload=\""+ includeOnLoadFunctions() +"\" onunload=\""+ includeOnUnLoadFunctions() +"\">");

		out.println("	<form name=\"reqForm\" method=\"post\" id=\"reqForm\" >");
		
		out.println("	<div id=\"header-wrap\"> ");
		out.println("	<table width=\"100%\">");
		out.println("		<tr>");
		out.println("			<td width=\"100%\">");
		if(isIncludeHeader())
			out.println(        	includeHeader(request));
		out.println("			</td>");
		out.println("		</tr>");
		out.println("	</table>");
		out.println("	</div> ");
		
		out.println("	<div id=\"container\"> ");
		out.println("	<table border=1 class=\"wrapperAllTable\">");
		


		//Spacing Between Header and Content
		/*out.println("		<tr height=\"200px\">");
		out.println("			<td>&nbsp;</td>");
		out.println("		</tr>");*/


		//Content
		out.println("		<tr height=\"500px\" valign=\"top\" >");
		out.println("			<td width=\"100%\">");
		out.println("				<table border=0 class=\"bodyTable\">");
		out.println("					<tr valign=\"top\">");
		if (isIncludeLeftMenu()){
			out.println("						<td width=\"10%\" id=\"mainLeftMenuTD\">");
			out.println(							new String(constructLeftMenu()));
			out.println("						</td>");
		}
		out.println("						<td width=\"75%\" id=\"mainBodyTD\">");
		out.println(							new String(this.constructPage()));
		out.println("						</td>");
		if (isIncludeRightMenu()){
			out.println("						<td width=\"15%\" id=\"mainRightMenuTD\">");
			out.println(							new String(constructLeftMenu()));
			out.println("						</td>");
		}
		out.println("					</tr>");
		out.println("				</table>");
		out.println("			</td>");
		out.println("   	</tr>");

		//Spacing Between Content and Footer
		out.println("		<tr height=\"2%\">");
		out.println("			<td><br/><br/></td>");
		out.println("		</tr>");
		out.println("	</table>");
		
		//Footer
		/*out.println("		<tr height=\"10%\">");
		out.println("			<td width=\"100%\" align=\"center\">");
		if(isIncludeFooter())
			out.println(        	includeFooter());
		out.println("	    	</td>");
		out.println("		</tr>");*/
		
		out.println("	</div>");
		
		
		out.println("	<div id=\"footer\"> ");
		out.println("	<table style=\"font-size: 12px; text-align: center; width:100%;\">");
		out.println("		<tr>");
		out.println("			<td width=\"100%\">");
		if(isIncludeFooter())
			out.println(        	includeFooter());;
		out.println("			</td>");
		out.println("		</tr>");
		out.println("	</table>");
		out.println("	</div> ");

		
		out.println("	</form>");
		out.println("	<script>");
		out.println(		includeJSInstance()	);
		out.println("	</script>");
		out.println("</body>");
		out.println("</html>");

		out.flush();
		out.close();
	}

	/**
	 * Gets the scripts included in the subclasses along with the
	 * common scriptat are common to all pages and that are defined here
	 * 
	 * @return
	 */
	protected StringBuffer includeScripts(){

		StringBuffer scripts = new StringBuffer();

		scripts.append("<link href=\"clientassets/files_optica/css/styles.css\" rel=\"stylesheet\" type=\"text/css\" />");
		scripts.append("<script language=\"Javascript\" src=\"common/XperteamLibrary.js\" type=\"text/javascript\"></script>");
		scripts.append("<script language=\"Javascript\" src=\"common/util/PopupWindow.js\" type=\"text/javascript\"></script>");
		scripts.append("<script language=\"Javascript\" src=\"common/Common.js\" type=\"text/javascript\"></script>");
		
		//		if(isIncludeJsAlertTranslation()){
		//			scripts.append("		<script language=\"JavaScript\" src=\"js/xmlAlertParsing.js\"></script>");
		//			scripts.append("		<script language=\"Javascript\" type=\"text/javascript\">");
		//			// to initialize the xmlTitleDoc , this functions is on xmlAlertParsing.js
		//			scripts.append("			initTitlesXML('"+ clientDirName +"/xml/AlertJsTitles.xml','"+getLanguage()+"');");
		//			scripts.append("		</script>");
		//		}

		if(isIncludeMenu()){
			scripts.append("<script type=\"text/javascript\" src=\""+ clientDirName  +"/menu/c_config.js\"></script>");
			scripts.append("<script type=\"text/javascript\" src=\""+ clientDirName  +"/menu/c_smartmenus.js\"></script>");
		}
		
		return scripts.append(new String(getScriptIncludes()));
	}

	/**
	 * Gets the scripts included such as formIns - dateIns - numberFormat... 
	 * 
	 * @return
	 */
	private StringBuffer includeJSInstance(){

		StringBuffer scripts = new StringBuffer();
		return scripts.append(new String(this.getJsInstance()));
	}

	/**
	 * Gets the script functions included in the subclasses along with
	 * the common script functions that are common to all pages and that are defined
	 * here
	 * 
	 * @return
	 */
	private String constructScriptFunctions(){
		return getScriptFunctions();
	}

	/**
	 * Gets the page constructor from the different subclasses and include them
	 * in the doPost
	 * 
	 * @return
	 */
	private StringBuffer constructPage(){
		return getPageConstructor();
	}

	/**
	 * Returns the html code of the common header for all pages
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return String
	 */
	private String includeHeader(HttpServletRequest request){

		HttpSession session = request.getSession(true);

		// Added by Maher to check the session of the bean(sessionBean) of each section
		String sessionBean = (getValueBean() == null ? "": getValueBean() );
		String theSessionBean = (session.getAttribute("sessionBean") == null ? "" :(String)session.getAttribute("sessionBean"));

		session.setAttribute("sessionBean", sessionBean);

		if(! sessionBean.equals(theSessionBean)){
			session.removeAttribute(""+theSessionBean+"");
		}


		StringBuffer headerConstructor  = new StringBuffer();
		headerConstructor.append("	<table border=0 class=\"headerTable\">");
		headerConstructor.append("		<tbody>");
		headerConstructor.append("			<tr>");
		headerConstructor.append("				<td  width=\"100%\">");
		//		BEGIN Header
		headerConstructor.append("					<table class=\"headerMainTable\">");
//		headerConstructor.append("						<tbody>");
		headerConstructor.append("  						<tr>");
		headerConstructor.append("    							 <td style=\"font-size: 35px; color: #FFFFFF;\" align=\"center\" width=\"20%\"><b>OPTICA</b></td>");	
		headerConstructor.append("    							 <td align=\"right\" valign=\"center\" width=\"80%\"><img src=\""+clientDirName+"/images/opticaBanner.jpg\" width=\"150px\" height=\"40px\" /></td>");		
		headerConstructor.append("  						</tr>");
		/*headerConstructor.append("  						<tr>");
		headerConstructor.append("  							<td>");
		headerConstructor.append("  								<br />");
		headerConstructor.append("  							</td>");
		headerConstructor.append("  						</tr>");*/
//		headerConstructor.append("						</tbody>");
		headerConstructor.append("					</table>");
		headerConstructor.append("				</td>");
		headerConstructor.append("			</tr>");
		if (isIncludeHorizontalUserInfo()){
			headerConstructor.append("		<tr valign=\"top\">");
			headerConstructor.append("			<td align=\"right\" width=\"100%\" >");
			headerConstructor.append(				includeHorizontalUserInfo(request));
			headerConstructor.append("			</td>");
			headerConstructor.append("		</tr>"); 
		}
		headerConstructor.append("			<tr>");
		headerConstructor.append("				 <td>");
		if(includeMenu){

			String requestString = "formLastAccess=" + session.getAttribute("loginTime");
			String encryptedString = QueryCrypt.encrypt(request, requestString);

			headerConstructor.append("				<table width=\"100%\"  class=\"horizontalMenu\">");
			headerConstructor.append("					<tr>");
			headerConstructor.append("						<td width=\"80%\">");
			headerConstructor.append("							<div style =\"float:left;\" valign=\"bottom\" style=\"font-weight:bold\">");

			headerConstructor.append("								<ul id=\"Menu1\" class=\"MM\">");

			
			headerConstructor.append("									<li>");
			headerConstructor.append("										<a id=\"customerSection\" href=\"CustomerList?"+ encryptedString +"\">Customer</a>");
			headerConstructor.append("									</li>");


			headerConstructor.append("									<li>");
			headerConstructor.append("										<a id=\"optoSection\" href=\"#?"+ encryptedString +"\">Optometry File</a>");
			headerConstructor.append("									</li>");

			
			headerConstructor.append("									<li>");
			headerConstructor.append("										<a id=\"prescSection\" href=\"#?"+ encryptedString +"\">Prescriptions</a>");
			headerConstructor.append("									</li>");

			
			headerConstructor.append("									<li>");
			headerConstructor.append("										<a id=\"orderSection\" href=\"#?"+ encryptedString +"\">Orders</a>");
			headerConstructor.append("									</li>");

			
			headerConstructor.append("									<li>");
			headerConstructor.append("										<a id=\"jobSection\" href=\"#?"+ encryptedString +"\">Jobs</a>");
			headerConstructor.append("									</li>");
			
			headerConstructor.append("									<li>");
			headerConstructor.append("										<a id=\"configSection\" href=\"#\">Configuration</a>");
			headerConstructor.append("										<ul>");
			headerConstructor.append("											<li>");
			headerConstructor.append("												<a href=\"CustomerTypeList?"+ encryptedString +"\">Customer Type</a>");
			headerConstructor.append("											</li>");
			headerConstructor.append("											<li>");
			headerConstructor.append("												<a href=\"RatingList?"+ encryptedString +"\">Rating</a>");
			headerConstructor.append("											</li>");
			headerConstructor.append("											<li>");
			headerConstructor.append("												<a href=\"ProfessionalFieldList?"+ encryptedString +"\">Professional Field</a>");
			headerConstructor.append("											</li>");
			headerConstructor.append("											<li>");
			headerConstructor.append("												<a href=\"ProfessionList?"+ encryptedString +"\">Profession</a>");
			headerConstructor.append("											</li>");
			headerConstructor.append("											<li>");
			headerConstructor.append("												<a href=\"AddressTypeList?"+ encryptedString +"\">Address Type</a>");
			headerConstructor.append("											</li>");
			headerConstructor.append("											<li>");
			headerConstructor.append("												<a href=\"PhoneTypeList?"+ encryptedString +"\">Phone Type</a>");
			headerConstructor.append("											</li>");			
			headerConstructor.append("											<li>");
			headerConstructor.append("												<a href=\"VisitTypeList?"+ encryptedString +"\">Visit Type</a>");
			headerConstructor.append("											</li>");
			headerConstructor.append("											<li>");
			headerConstructor.append("												<a href=\"PersonTitleList?"+ encryptedString +"\">Person Title</a>");
			headerConstructor.append("											</li>");
			headerConstructor.append("										</ul>");
			headerConstructor.append("									</li>");
			
			headerConstructor.append("									<li>");
			headerConstructor.append("										<a id=\"activitySection\" href=\"ActivityLogging?"+ encryptedString +"\">Activity Log</a>");
			headerConstructor.append("									</li>");
			
			
			headerConstructor.append("								</ul>");

			headerConstructor.append("							</div>");
			headerConstructor.append("						</td>");

			headerConstructor.append("					</tr>");
			headerConstructor.append("				</table>");
		
			headerConstructor.append("			</td>");
			headerConstructor.append("		</tr>");
		}
		headerConstructor.append("		</tbody>");
		headerConstructor.append("	</table>");

		
		return (new String(headerConstructor));

	}


	/**
	 * Returns the html code of the left Menu
	 * 
	 * @param request
	 * @param response
	 * @return String
	 */
	private StringBuffer constructLeftMenu(){

		StringBuffer leftMenuConstructor = new StringBuffer();
		
		
		leftMenuConstructor.append("	<table class=\"searchBarTable\" border=0 >");
		leftMenuConstructor.append("		<tr>");
		leftMenuConstructor.append("			<td style=\"text-align: center; font-size: 14px;\">");
		leftMenuConstructor.append("				SEARCH BAR");
		leftMenuConstructor.append("			</td>");
		leftMenuConstructor.append("		</tr>");
		leftMenuConstructor.append("		<tr style=\"text-align: center; vertical-align: top; height: 80%;\">");
		leftMenuConstructor.append("			<td>");
		leftMenuConstructor.append("				<div class=\"leftMenuDiv\">");
		leftMenuConstructor.append("					<table border=\"0\" class=\"leftMenuTable\">");
		leftMenuConstructor.append("						<tr>");
		leftMenuConstructor.append("							<td align=\"center\">");
		leftMenuConstructor.append("								<input id=\"keywords-search\" type=\"text\" name=\"keywords\" placeholder=\"Search Keywords\" class=\"hint\">");
		leftMenuConstructor.append("							</td>");
		leftMenuConstructor.append("							<td>");			
		leftMenuConstructor.append("								<select name=\"productTypeID\" id=\"productTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('productTypeID','productTypeName')\" required=\"true\" tabindex=\"4\" >");
		leftMenuConstructor.append("									<option title=\"0\" value=\"0\">File No</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"1\">File Starting date</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">File status</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">First name</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Last name</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Add in Full address</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Company</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Mail</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Profession</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Job title position</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Profession field</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Profession </option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Job title</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Old file number</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">phone number</option>");
		leftMenuConstructor.append("								</select>");
		leftMenuConstructor.append("							</td>");
		leftMenuConstructor.append("						</tr>");
		leftMenuConstructor.append("						<tr>");
		leftMenuConstructor.append("							<td align=\"center\">");
		leftMenuConstructor.append("								<input id=\"keywords-search\" type=\"text\" name=\"keywords\" placeholder=\"And Keyword\" class=\"hint\">");
		leftMenuConstructor.append("							</td>");
		leftMenuConstructor.append("							<td>");			
		leftMenuConstructor.append("								<select name=\"productTypeID\" id=\"productTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('productTypeID','productTypeName')\" required=\"true\" tabindex=\"4\" >");
		leftMenuConstructor.append("									<option title=\"0\" value=\"0\">File No</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"1\">File Starting date</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">File status</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">First name</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Last name</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Add in Full address</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Company</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Mail</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Profession</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Job title position</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Profession field</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Profession </option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Job title</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">Old file number</option>");
		leftMenuConstructor.append("									<option title=\"0\" value=\"2\">phone number</option>");
		leftMenuConstructor.append("								</select>");
		leftMenuConstructor.append("							</td>");
		leftMenuConstructor.append("						</tr>");	
		leftMenuConstructor.append("						<tr colspan=2 >");
		leftMenuConstructor.append("							<td >");
		leftMenuConstructor.append("								<hr  /> ");
		leftMenuConstructor.append("							</td>");
		leftMenuConstructor.append("						</tr>");			
		leftMenuConstructor.append("						<tr colspan=2>");
		leftMenuConstructor.append("							<td align=\"left\">");
		leftMenuConstructor.append("								<details>");
		leftMenuConstructor.append("									<summary>Filter By Date</summary>");
		leftMenuConstructor.append("									<table border=\"0\">");
		leftMenuConstructor.append("										<tr>");
		leftMenuConstructor.append("											<td align=\"right\">");
		leftMenuConstructor.append("												Criteria");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("											<td align=\"left\">");			
		leftMenuConstructor.append("												<select name=\"productTypeID\" id=\"productTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('productTypeID','productTypeName')\" required=\"true\" tabindex=\"4\" >");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\"></option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\">First Visit</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"1\">Last Visit</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"2\">Birthday</option>");
		leftMenuConstructor.append("												</select>");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("										</tr>");
		leftMenuConstructor.append("										<tr>");
		leftMenuConstructor.append("											<td align=\"right\">");
		leftMenuConstructor.append("												As Of");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("											<td align=\"left\">");
		leftMenuConstructor.append("												<input name=\"systemDate\" id=\"systemDate\" type=\"hidden\" onpaste=\"return false\" value=\""+new java.util.Date()+"\" /> ");
		leftMenuConstructor.append("												<input class=\"dateStyle\" name=\"effectiveDate\" readonly size=\"12\" required=\"true\" type=\"text\" id=\"effectiveDate\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
//		leftMenuConstructor.append("												<span class=\"FrmCompTitle\">");
		leftMenuConstructor.append("													<a href=\"javascript:void(0)\" onclick=\"if(self.gfPop)gfPop.fEndPop(document.reqForm.systemDate,document.reqForm.effectiveDate);return false;\">");
		leftMenuConstructor.append("														<img class=\"PopcalTrigger\" align=\"absmiddle\" src=\"common/calendar/DateRange/calbtn.gif\" width=\"34\" height=\"22\" border=\"0\" alt=\"\">");
		leftMenuConstructor.append("													</a>");
//		leftMenuConstructor.append("                        						</span>");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("										</tr>");		
		leftMenuConstructor.append("									</table>");
		leftMenuConstructor.append("								</details>");
		leftMenuConstructor.append("							</td>");
		leftMenuConstructor.append("						</tr>");
		leftMenuConstructor.append("						<tr colspan=2>");
		leftMenuConstructor.append("							<td >");
		leftMenuConstructor.append("								<hr  /> ");
		leftMenuConstructor.append("							</td>");
		leftMenuConstructor.append("						</tr>");
		leftMenuConstructor.append("						<tr colspan=2>");
		leftMenuConstructor.append("							<td align=\"left\">");
		leftMenuConstructor.append("								<details>");
		leftMenuConstructor.append("									<summary>Filter More...</summary>");
		leftMenuConstructor.append("									<table border=\"0\">");
		leftMenuConstructor.append("										<tr>");
		leftMenuConstructor.append("											<td align=\"right\">");
		leftMenuConstructor.append("												Departement");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("											<td align=\"left\">");			
		leftMenuConstructor.append("												<select name=\"productTypeID\" id=\"productTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('productTypeID','productTypeName')\" required=\"true\" tabindex=\"4\" >");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\"></option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\">RX Only</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"1\">Sales</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"2\">Contact Lenses</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"2\">Hearing</option>");
		leftMenuConstructor.append("												</select>");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("										</tr>");		
		leftMenuConstructor.append("										<tr>");
		leftMenuConstructor.append("											<td align=\"right\">");
		leftMenuConstructor.append("												To Delete");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("											<td align=\"left\">");
		leftMenuConstructor.append("												<select name=\"productTypeID\" id=\"productTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('productTypeID','productTypeName')\" required=\"true\" tabindex=\"4\" >");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\"></option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\">True</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"1\">False</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"2\">None</option>");
		leftMenuConstructor.append("												</select>");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("										</tr>");
		leftMenuConstructor.append("										<tr>");
		leftMenuConstructor.append("											<td align=\"right\">");
		leftMenuConstructor.append("												To Archive");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("											<td align=\"left\">");	
		leftMenuConstructor.append("												<select name=\"productTypeID\" id=\"productTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('productTypeID','productTypeName')\" required=\"true\" tabindex=\"4\" >");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\"></option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\">True</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"1\">False</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"2\">None</option>");
		leftMenuConstructor.append("												</select>");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("										</tr>");
		leftMenuConstructor.append("										<tr>");
		leftMenuConstructor.append("											<td align=\"right\">");
		leftMenuConstructor.append("												Double File Check");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("											<td align=\"left\">");				
		leftMenuConstructor.append("												<select name=\"productTypeID\" id=\"productTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('productTypeID','productTypeName')\" required=\"true\" tabindex=\"4\" >");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\"></option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\">True</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"1\">False</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"2\">None</option>");
		leftMenuConstructor.append("												</select>");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("										</tr>");		
		leftMenuConstructor.append("										<tr>");
		leftMenuConstructor.append("											<td align=\"right\">");
		leftMenuConstructor.append("												Moursel Peoples");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("											<td align=\"left\">");				
		leftMenuConstructor.append("												<select name=\"productTypeID\" id=\"productTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('productTypeID','productTypeName')\" required=\"true\" tabindex=\"4\" >");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\"></option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\">True</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"1\">False</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"2\">None</option>");
		leftMenuConstructor.append("												</select>");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("										</tr>");		
		leftMenuConstructor.append("										<tr>");
		leftMenuConstructor.append("											<td align=\"right\">");
		leftMenuConstructor.append("												Service Peoples");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("											<td align=\"left\">");
		leftMenuConstructor.append("												<select name=\"productTypeID\" id=\"productTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('productTypeID','productTypeName')\" required=\"true\" tabindex=\"4\" >");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\"></option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\">True</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"1\">False</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"2\">None</option>");
		leftMenuConstructor.append("												</select>");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("										</tr>");				
		leftMenuConstructor.append("										<tr>");
		leftMenuConstructor.append("											<td align=\"right\">");
		leftMenuConstructor.append("												ID Update");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("											<td align=\"left\">");			
		leftMenuConstructor.append("												<select name=\"productTypeID\" id=\"productTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('productTypeID','productTypeName')\" required=\"true\" tabindex=\"4\" >");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\"></option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\">True</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"1\">False</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"2\">None</option>");
		leftMenuConstructor.append("												</select>");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("										</tr>");				
		leftMenuConstructor.append("										<tr>");
		leftMenuConstructor.append("											<td align=\"right\">");
		leftMenuConstructor.append("												Customer Profile Update");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("											<td align=\"left\">");	
		leftMenuConstructor.append("												<select name=\"productTypeID\" id=\"productTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('productTypeID','productTypeName')\" required=\"true\" tabindex=\"4\" >");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\"></option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\">True</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"1\">False</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"2\">None</option>");
		leftMenuConstructor.append("												</select>");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("										</tr>");		
		leftMenuConstructor.append("										<tr>");
		leftMenuConstructor.append("											<td align=\"right\">");
		leftMenuConstructor.append("												Control");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("											<td align=\"left\">");			
		leftMenuConstructor.append("												<select name=\"productTypeID\" id=\"productTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('productTypeID','productTypeName')\" required=\"true\" tabindex=\"4\" >");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\"></option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\">True</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"1\">False</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"2\">None</option>");
		leftMenuConstructor.append("												</select>");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("										</tr>");				
		leftMenuConstructor.append("										<tr>");
		leftMenuConstructor.append("											<td align=\"right\">");
		leftMenuConstructor.append("												Copy To Mobile");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("											<td align=\"left\">");			
		leftMenuConstructor.append("												<select name=\"productTypeID\" id=\"productTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('productTypeID','productTypeName')\" required=\"true\" tabindex=\"4\" >");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\"></option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\">True</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"1\">False</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"2\">None</option>");
		leftMenuConstructor.append("												</select>");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("										</tr>");				
		leftMenuConstructor.append("										<tr>");
		leftMenuConstructor.append("											<td align=\"right\">");
		leftMenuConstructor.append("												Ok Copied");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("											<td align=\"left\">");			
		leftMenuConstructor.append("												<select name=\"productTypeID\" id=\"productTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('productTypeID','productTypeName')\" required=\"true\" tabindex=\"4\" >");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\"></option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"0\">True</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"1\">False</option>");
		leftMenuConstructor.append("													<option title=\"0\" value=\"2\">None</option>");
		leftMenuConstructor.append("												</select>");
		leftMenuConstructor.append("											</td>");
		leftMenuConstructor.append("										</tr>");		
		leftMenuConstructor.append("									</table>");
		leftMenuConstructor.append("								</details>");
		leftMenuConstructor.append("							</td>");
		leftMenuConstructor.append("						</tr>");								
		leftMenuConstructor.append("					</table>");
		leftMenuConstructor.append("				</div>");
		leftMenuConstructor.append("			</td>");
		leftMenuConstructor.append("		</tr>");
		leftMenuConstructor.append("		<tr>");
		leftMenuConstructor.append("			<td style=\"text-align: center; font-size: 14px;\">");
//		leftMenuConstructor.append("				<input id=\"mdys-go\" class=\"btn-primary modify-search\" type=\"submit\" value=\"SEARCH\" name=\"search\">");
		leftMenuConstructor.append("							<a href=\"#\" class=\"btn_large btn_orange btn_awesome\" rel=\"nofollow\" onClick=\"#\">SEARCH</a>");
		leftMenuConstructor.append("			</td>");
		leftMenuConstructor.append("		</tr>");
		leftMenuConstructor.append("	</table>");
		
		
		leftMenuConstructor.append("<iframe width=132 height=142 name=\"gToday:contrast:agenda.js\" id=\"gToday:contrast:agenda.js\"");
		leftMenuConstructor.append("	src=\"common/calendar/DateRange/ipopeng.htm\" scrolling=\"no\" frameborder=\"0\"");
		leftMenuConstructor.append("	style=\"visibility: visible; z-index: 999; position: absolute; top: -500px;\">");
		leftMenuConstructor.append("</iframe>");
		
		return leftMenuConstructor;
		
//		return getLeftMenuConstructor();

	}



	/**
	 * Returns Footer Html Code as String
	 * 
	 * @param request
	 * @param response
	 */
	private String includeFooter()
	{
		StringBuffer footerConstructor = new StringBuffer();
		try {
//			String language = getLanguage();
//			String target = application.getRealPath(clientDirName + "/html/" + getLanguage() + "/footerLinks.html");

//			<  footer >");
			footerConstructor.append("						<table border=\"0\" class=\"FooterTable\">");
			footerConstructor.append("							<tbody>");
			footerConstructor.append("								<tr>");
			footerConstructor.append("									<td align=\"center\">");
			footerConstructor.append("											<p>Copyright ©2013 <b>Pro-Soft</b>. All rights reserved.</p>");
			footerConstructor.append("									</td>");
			footerConstructor.append("								</tr>");
			footerConstructor.append("							</tbody>");
			footerConstructor.append("						</table>");

//			<  END  footer >");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (new String(footerConstructor));
	}

	/**
	 * Includes onLoad functions
	 * 
	 * @return
	 */
	private StringBuffer includeOnLoadFunctions(){
		StringBuffer functions = new StringBuffer();
		return functions.append(getOnLoadFunctions().toString());
	}

	/**
	 * Includes onUnLoad functions
	 * 
	 * @return 
	 */
	private StringBuffer includeOnUnLoadFunctions(){
		StringBuffer functions = new StringBuffer();
		return functions.append(getOnUnloadFunctions().toString());
	}

	/**
	 * @return the pageConstructor
	 */
	public StringBuffer getPageConstructor(){
		return pageConstructor;
	}

	/**
	 * @param pageConstructor the pageConstructor to set 
	 */
	public void setPageConstructor(StringBuffer pageConstructor){
		this.pageConstructor = pageConstructor;
	}

	/**
	 * @return the scriptFunctions
	 */
	public String getScriptFunctions(){
		return scriptFunctions;
	}

	/**
	 * @param scriptFunctions the scriptFunctions to set 
	 */
	public void setScriptFunctions(String scriptFunctions){
		this.scriptFunctions = scriptFunctions;
	}

	/**
	 * @return the scriptIncludes 
	 */
	public String getScriptIncludes(){
		return scriptIncludes;
	}

	/**
	 * @param scriptIncludes the scriptIncludes to set 
	 */
	public void setScriptIncludes(String scriptIncludes){
		this.scriptIncludes = scriptIncludes;
	}
	/**
	 * @return the onLoadFunctions
	 */
	public String getOnLoadFunctions() {
		return onLoadFunctions;
	}

	/**
	 * @param onLoadFunctions the onLoadFunctions to set
	 */
	public void setOnLoadFunctions(String onLoadFunctions) {
		this.onLoadFunctions = onLoadFunctions;
	}

	/**
	 * @return the onUnloadFunctions
	 */
	public String getOnUnloadFunctions() {
		return onUnloadFunctions;
	}

	/**
	 * @param onUnloadFunctions the onUnloadFunctions to set
	 */
	public void setOnUnloadFunctions(String onUnloadFunctions) {
		this.onUnloadFunctions = onUnloadFunctions;
	}

	/**
	 * @return the jsInstance
	 */
	public String getJsInstance() {
		return jsInstance;
	}

	/**
	 * @param jsInstance the jsInstance to set
	 */
	public void setJsInstance(String jsInstance) {
		this.jsInstance = jsInstance;
	}
	/**
	 * @return the formLastAccess
	 */
	public Long getFormLastAccess() {
		return formLastAccess;
	}

	/**
	 * @param formLastAccess the formLastAccess to set
	 */
	public void setFormLastAccess(Long formLastAccess) {
		this.formLastAccess = formLastAccess;
	}

	/**
	 * @return the includeLeftMenu
	 */
	public boolean isIncludeLeftMenu() {
		return includeLeftMenu;
	}

	/**
	 * @return the includeRightMenu
	 */
	public boolean isIncludeRightMenu() {
		return includeRightMenu;
	}
	/**
	 * 
	 * @return the includeRightNewsFeed
	 */
	public boolean isIncludeRightNewsFeed(){
		return includeRightNewsFeed;
	}

	/**
	 * @param includeLeftMenu the includeLeftMenu to set
	 */
	public void setIncludeLeftMenu(boolean includeLeftMenu) {
		this.includeLeftMenu = includeLeftMenu;
	}

	/**
	 * @param includeRightMenu the includeRightMenu to set
	 */
	public void setIncludeRightMenu(boolean includeRightMenu) {
		this.includeRightMenu = includeRightMenu;
	}

	/**
	 * 
	 * @param includeRightNewsFeed the includeRightNewsFeed to set
	 */
	public void setIncludeRightNewsFeed(boolean includeRightNewsFeed){
		this.includeRightNewsFeed = includeRightNewsFeed;
	}

	/**
	 * @return the includeHeader
	 */
	public boolean isIncludeHeader() {
		return includeHeader;
	}

	/**
	 * @param includeHeader the includeHeader to set
	 */
	public void setIncludeHeader(boolean includeHeader) {
		this.includeHeader = includeHeader;
	}

	/**
	 * @return the includeFooter
	 */
	public boolean isIncludeFooter() {
		return includeFooter;
	}

	/**
	 * @param includeFooter the includeFooter to set
	 */
	public void setIncludeFooter(boolean includeFooter) {
		this.includeFooter = includeFooter;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the currentServletName
	 */
	public String getCurrentServletName() {
		return currentServletName;
	}

	/**
	 * @param currentServletName the currentServletName to set
	 */
	public void setCurrentServletName(String currentServletName) {
		this.currentServletName = currentServletName;
	}

	
	/**
	 * @return the includeMenu
	 */
	public boolean isIncludeMenu() {
		return includeMenu;
	}

	/**
	 * @param includeMenu the includeMenu to set
	 */
	public void setIncludeMenu(boolean includeMenu) {
		this.includeMenu = includeMenu;
	}

	/**
	 * @return the includeJsAlertTranslation
	 */
	public boolean isIncludeJsAlertTranslation() {
		return includeJsAlertTranslation;
	}

	/**
	 * @param includeJsAlertTranslation the includeJsAlertTranslation to set
	 */
	public void setIncludeJsAlertTranslation(boolean includeJsAlertTranslation) {
		this.includeJsAlertTranslation = includeJsAlertTranslation;
	}

	/**
	 * @return the includeLanguage
	 */
	public boolean isIncludeLanguage() {
		return includeLanguage;
	}

	/**
	 * @param includeLanguage the includeLanguage to set
	 */
	public void setIncludeLanguage(boolean includeLanguage) {
		this.includeLanguage = includeLanguage;
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
	public synchronized void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException{
		this.doPost(request, response);
	}


	public StringBuffer getLeftMenuConstructor() {
		return leftMenuConstructor;
	}

	public void setLeftMenuConstructor(StringBuffer leftMenuConstructor) {
		this.leftMenuConstructor = leftMenuConstructor;
	}

	public StringBuffer getRightMenuConstructor() {
		return rightMenuConstructor;
	}

	public void setRightMenuConstructor(StringBuffer rightMenuConstructor) {
		this.rightMenuConstructor = rightMenuConstructor;
	}
	
	
	private String includeHorizontalUserInfo(HttpServletRequest request){

		HttpSession session = request.getSession(true);
		StringBuffer horizontalUserInfoConstructor = new StringBuffer();
		Date date = new Date();
		
		String requestString = "formLastAccess=" + session.getAttribute("loginTime");
		String encryptedString = QueryCrypt.encrypt(request, requestString);

//		< Begin horizontal user Info>"); 
		horizontalUserInfoConstructor.append("		<table border=0 class=\"HeaderUserInfoTable\" cellpadding=0 cellspacing=0>");
		
		horizontalUserInfoConstructor.append("		<tr>");
		horizontalUserInfoConstructor.append("			<td width=\"20%\" align=\"left\">");
		horizontalUserInfoConstructor.append("&nbsp;&nbsp;&nbsp;" + simpleDateTimeFormat.format(date));
		horizontalUserInfoConstructor.append("			</td>");
		horizontalUserInfoConstructor.append("			<td width=\"10%\" align=\"right\">");
		horizontalUserInfoConstructor.append("Welcome&nbsp;&nbsp;&nbsp;"); 
		horizontalUserInfoConstructor.append("			</td>");
		horizontalUserInfoConstructor.append("			<td width=\"20%\" align=\"left\">");
		horizontalUserInfoConstructor.append("				<strong>");
		horizontalUserInfoConstructor.append(getName(StringUtil.nullToEmpty(session.getAttribute("userName"))) + "&nbsp;" + "</strong>");
		horizontalUserInfoConstructor.append("			</td>");
		horizontalUserInfoConstructor.append("			<td width=\"20%\" align=\"right\">");
		if (session.getAttribute("lastAccessTime")!=null){
			horizontalUserInfoConstructor.append("Your Last Logon Was");
		}else{
			horizontalUserInfoConstructor.append("This is your first Login");
		}
		horizontalUserInfoConstructor.append("			&nbsp;&nbsp;&nbsp;</td>");
		horizontalUserInfoConstructor.append("			<td width=\"20%\" align=\"left\">");
		horizontalUserInfoConstructor.append("				<b>");
		horizontalUserInfoConstructor.append((session.getAttribute("lastAccessTime")!=null)?(simpleDateTimeFormat.format(session.getAttribute("lastAccessTime"))):"");
		horizontalUserInfoConstructor.append("				<b>");
		horizontalUserInfoConstructor.append("			</td>");
		horizontalUserInfoConstructor.append("			<td align=\"right\">");
		horizontalUserInfoConstructor.append("				<span class=\"FrmCompTitle\">");
		if(!this.getServletName().equals("Home"))
			horizontalUserInfoConstructor.append("    				<a title=\"Home Page\" href=\"Home?"+encryptedString+"\" > <img src=\""+clientDirName+"/icons/home_w.png\" height=\"20\" width=\"35px\" /></a>");
		horizontalUserInfoConstructor.append("    				<a title=\"Setup\" href=\"#\" > <img src=\""+clientDirName+"/icons/settings.png\" height=\"20\" width=\"35px\" /></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		horizontalUserInfoConstructor.append("    				<a title=\"Logout\" href=\"LogoutProcess?"+encryptedString+"\" > <img src=\""+clientDirName+"/icons/logout-icon.png\" height=\"20\" width=\"35px\" /></a>");		
		horizontalUserInfoConstructor.append("				</span>");
		horizontalUserInfoConstructor.append("			</td>");
		horizontalUserInfoConstructor.append("		</tr>");

		horizontalUserInfoConstructor.append("		</table>");
		

		return horizontalUserInfoConstructor.toString();
	}
	
	
	public boolean isIncludeHorizontalUserInfo() {
		return includeHorizontalUserInfo;
	}

	public void setIncludeHorizontalUserInfo(boolean includeHorizontalUserInfo) {
		this.includeHorizontalUserInfo = includeHorizontalUserInfo;
	}
	
	public String getName(String name){

		if(name == null) return "";
		
		return name.toUpperCase();
	}
}