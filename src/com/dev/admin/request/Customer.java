package com.dev.admin.request;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.guhesan.querycrypt.QueryCrypt;
import com.guhesan.querycrypt.beans.RequestParameterObject;
import com.hdc.sysdev.utils.StringUtil;
import com.optica.admin.Business.AdminBO;
import com.optica.admin.common.base.PageServlet;
import com.optica.admin.common.design.HTMLDesign;
import com.optica.bean.common.BranchBean;
import com.optica.bean.common.CountryBean;
import com.optica.bean.common.PersonTitleBean;
import com.optica.bean.common.ProfessionBean;
import com.optica.bean.common.ProfessionalFieldBean;
import com.optica.bean.common.RatingBean;
import com.optica.bean.customer.CustomerAddressBean;
import com.optica.bean.customer.CustomerBean;
import com.optica.bean.customer.CustomerIdentityBean;
import com.optica.bean.customer.CustomerOldDBBean;
import com.optica.bean.customer.CustomerProfileBean;
import com.optica.bean.customer.CustomerTypeBean;
import com.optica.bean.customer.CustomerWorkBean;

public class Customer extends PageServlet {
	/**
	 * 
	 * 
	 * 
	 * ATTRIBUTES 
	 */
	private static final long serialVersionUID = 1L;
	//	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Plan.class);
	private final Logger logger = Logger.getLogger(Customer.class);
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(StringUtil.nullOrEmptyToString(application.getAttribute("defaultDateFormat"), "dd/MM/yyyy"));
	AdminBO adminBO = (AdminBO)application.getAttribute("adminBO");
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
	 * Constructor of the object.
	 */
	public Customer() {
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

				StringBuffer scriptIncludeBuffer = new StringBuffer();
				scriptIncludeBuffer.append("			<script language=\"Javascript\" src=\"js/Customer.js\"></script> ");
				scriptIncludeBuffer.append("		<link rel=\"stylesheet\" type=\"text/css\"  href=\"clientassets/files_optica/css/font.css\">");
				setScriptIncludes(new String(scriptIncludeBuffer));

				StringBuffer jsInstanceBuffer = new StringBuffer();
				jsInstanceBuffer.append(" formIns = getFormUtil(\"reqForm\");");
				jsInstanceBuffer.append(" numberInstance = getNumberUtil();");
				setJsInstance(new String(jsInstanceBuffer));
				jsInstanceBuffer = null;

				setOnLoadFunctions(
						"document.getElementById('customerSection').className += 'CURRENT';" +
						"checkDivToDisplay();"); 
		    
				CustomerBean customerBean= new CustomerBean();

				
				RequestParameterObject rpo= QueryCrypt.decrypt(request);
				String reportAction = StringUtil.nullToEmpty(rpo.getParameter("repAction"));

				String actionType = StringUtil.nullToEmpty(request.getParameter("actionType"));
				logger.info("actionType: " + actionType);



				setTitle("Client");

				constructPage(request, response, rpo, requestString, customerBean, reportAction, actionType);
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
	 * 
	 * 
	 * 
	 * 
	 * Construct page.
	 *
	 * @param request the request
	 * @param session the session
	 * @param response the response
	 */
	private void constructPage(HttpServletRequest request,HttpServletResponse response, RequestParameterObject rpo, String requestString, CustomerBean customerBean,
				String repAction, String actionType) throws Exception{
		StringBuffer pageConstructor = new StringBuffer();
		String encryptedString = QueryCrypt.encrypt(request, requestString);
		
		String viewType = StringUtil.nullToEmpty(rpo.getParameter("viewType"));
		String userType = StringUtil.nullToString(request.getSession().getAttribute("userType"), "user");
		
		
		ArrayList<CustomerTypeBean> customerTypeList = adminBO.getCustTypeRecordListFiltered(null, "", "", "", "");
		CustomerTypeBean customerTypeBean=null;
		
		

		BranchBean branchBean = customerBean.getBranchBean();
		CustomerIdentityBean customerIdentityBean = customerBean.getCustomerIdentityBean();
		CustomerAddressBean customerAddressBean = customerBean.getCustomerAddressBean();
		CustomerOldDBBean customerOldDBBean = customerBean.getCustomerOldDBBean();
		CustomerProfileBean customerProfileBean = customerBean.getCustomerProfileBean();
		CustomerWorkBean customerWorkBean = customerBean.getCustomerWorkBean();
		Integer customerTypeID = (customerBean.getCustTypeBean()==null || customerBean.getCustTypeBean().getCustTypeID()==null)?0:Integer.parseInt(customerBean.getCustTypeBean().getCustTypeID());

		
		pageConstructor.append("<input name=\"systemDate\" id=\"systemDate\" type=\"hidden\" onpaste=\"return false\" value=\""+new java.util.Date()+"\" /> ");
		pageConstructor.append("<table border=0 class=\"mainTableWrapper\">");
		pageConstructor.append("<tr>");
		pageConstructor.append("	<td width=\"80%\" valign=\"top\" align=\"right\">");
		pageConstructor.append("		<table border=0 class=\"mainFSetTable\">");
		
		pageConstructor.append("			<tr>");
		pageConstructor.append("				<td width=\"100%\" align=\"center\">");
		pageConstructor.append("					<table class=\"buttonsTableUp\" >");
		pageConstructor.append("					<tr>");
		pageConstructor.append("						<td align=\"right\">");
		pageConstructor.append( constructDuplicateButton(request, requestString, userType, repAction, actionType, viewType) );
		pageConstructor.append( constructEditTermsButton(request, requestString, userType, repAction, actionType, viewType) );
		pageConstructor.append("						</td>");
		pageConstructor.append("					</tr>");
		pageConstructor.append("  					</table>");
		pageConstructor.append("					<hr />");
		pageConstructor.append("				</td>");
		pageConstructor.append("			</tr>");

		//Main Form table
		pageConstructor.append("	  	<tr valign=\"top\">");
		pageConstructor.append("			<td >");
		pageConstructor.append("				<table class=\"mainReqTable\" border=0>");
		pageConstructor.append("				<tr class=\"FrmCol2\">");
		pageConstructor.append("    	   			<td colspan=\"4\" >");
		pageConstructor.append("						<fieldset class=\"subFSet\">");
		pageConstructor.append("							<legend>HEADER</legend>");
		pageConstructor.append("		 					<table class=\"subFSetTable\">");
		pageConstructor.append("							<tr>");
		pageConstructor.append("               					<td width=\"10%\" align=\"right\">");
		pageConstructor.append("									<span class=\"FrmCompTitle\">");
		pageConstructor.append("										Name");
		pageConstructor.append("                       				</span>");
		pageConstructor.append("                       			</td>");
		pageConstructor.append("               					<td width=\"15%\" align=\"center\">");
//		if(repAction.equals("V") || actionType.equals("edit")){
			pageConstructor.append("										<span class=\"note\">");
			pageConstructor.append( (customerIdentityBean==null || StringUtil.nullToEmpty(customerIdentityBean.getFirstName()).equals("")) ? "-" : StringUtil.nullToEmpty(customerIdentityBean.getFirstName()).toUpperCase());
			pageConstructor.append("                          				</span>");	
//			pageConstructor.append("										<input type=\"hidden\" name=\"customerName\" id=\"customerName\" value=\""+StringUtil.nullToEmpty(customerIdentityBean.getFirstName())+"\">");
//		} else{
//			pageConstructor.append("										<input name=\"customerName\" id=\"customerName\" type=\"text\" required=\"true\" maxlength=\"20\" size=\"30\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"11\" value=\"\" /> ");
//		}
		pageConstructor.append("                       			</td>");
		pageConstructor.append("               					<td width=\"15%\" align=\"right\">");
		pageConstructor.append("									<span class=\"FrmCompTitle\">");
		pageConstructor.append("										Customer Type");
		pageConstructor.append("                       				</span>");
		pageConstructor.append("        	               		</td>");
		pageConstructor.append("               					<td width=\"15%\" align=\"center\">");
		pageConstructor.append("									<select name=\"customerTypeID\" id=\"customerTypeID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('customerTypeID','customerTypeName'); showHide('tabHeader_2')\" required=\"true\" tabindex=\"8\">");
		for(int i=0; (customerTypeList!=null && i<customerTypeList.size()); i++){
			customerTypeBean = customerTypeList.get(i);
			pageConstructor.append("									<option value=\""+customerTypeBean.getCustTypeID()+"\" "+(customerTypeBean.getCustTypeID().equals(customerTypeID.toString())?" selected":"")+">"+customerTypeBean.getCustTypeDesc()+"</option>");
		}
		pageConstructor.append("									</select>");
		pageConstructor.append("										<input type=\"hidden\" name=\"customerTypeName\" id=\"customerTypeName\" value=\"\" />");
		pageConstructor.append("                           		</td>");
		pageConstructor.append("                   				<td width=\"10%\" align=\"right\">");
		pageConstructor.append("									<span class=\"FrmCompTitle\">");
		pageConstructor.append("										File Number");
		pageConstructor.append("                           			</span>");
		pageConstructor.append("                           		</td >");
		pageConstructor.append("                   				<td width=\"15%\" align=\"center\">");
		pageConstructor.append("									<span class=\"note\">");
		pageConstructor.append(StringUtil.nullToEmpty(customerBean.getCustomerID()).equals("")?"-":StringUtil.nullToEmpty(customerBean.getCustomerID()));
//		pageConstructor.append("										<input name=\"fileNo\" id=\"fileNo\" type=\"text\" required=\"true\" maxlength=\"20\" size=\"15\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"11\" value=\"\" /> ");
		pageConstructor.append("                       				</span>");
		pageConstructor.append("        	               		</td>");
		pageConstructor.append("                				<td width=\"10%\" align=\"right\">");
		pageConstructor.append("									<span class=\"FrmCompTitle\">");
		pageConstructor.append("										Status");
		pageConstructor.append("    	                   			</span>");
		pageConstructor.append("        	               		</td>");
		pageConstructor.append("            	    			<td width=\"10%\" align=\"center\">");
		pageConstructor.append("									<span class=\"note\">");
		pageConstructor.append("										New ");
		pageConstructor.append("                        			</span>");
		pageConstructor.append("								</td>");
		pageConstructor.append("							</tr>");
		/*pageConstructor.append("							</table>");
		pageConstructor.append("						</fieldset >");
		pageConstructor.append("					</td>");
		pageConstructor.append("				</tr>");


		
		
		pageConstructor.append("				<tr class=\"FrmCol2\">");
		pageConstructor.append("   	       			<td colspan=\"4\" >");
		pageConstructor.append("						<fieldset class=\"subFSet\">");
		pageConstructor.append("							<legend>Client file info</legend>");
		pageConstructor.append("		 					<table class=\"subFSetTable\" border=0>");*/
		pageConstructor.append("							<tr>");
		pageConstructor.append("               					<td colspan=8>&nbsp;");
		pageConstructor.append("								</td>");
		pageConstructor.append("							</tr>");
		pageConstructor.append("							<tr>");
		pageConstructor.append("               					<td colspan=8>");
		pageConstructor.append("									<div id=\"wrapper\"> ");
//		pageConstructor.append("										<h1>Pure Javascript, HTML 5 &amp; CSS3 Tabs</h1> ");
		pageConstructor.append("										<div id=\"tabContainer\"> ");
		pageConstructor.append("											<div id=\"tabs\"> ");
		pageConstructor.append("												<ul> ");
		pageConstructor.append("													<li id=\"tabHeader_1\">Identity Info</li> ");
		pageConstructor.append("													<li id=\"tabHeader_2\">Identity & Contact</li> ");
		pageConstructor.append("													<li id=\"tabHeader_3\">Profile</li> ");
		pageConstructor.append("													<li id=\"tabHeader_4\">Purchases</li> ");
		pageConstructor.append("													<li id=\"tabHeader_5\">FollowUps</li> ");
		pageConstructor.append("													<li id=\"tabHeader_6\">Fidelity</li> ");
		pageConstructor.append("													<li id=\"tabHeader_7\">Customer Account</li> ");
		pageConstructor.append("													<li id=\"tabHeader_8\">Insurance & Reimbursements</li> ");
		pageConstructor.append("	      										</ul> ");
		pageConstructor.append("											</div> ");
		pageConstructor.append("											<div id=\"tabscontent\"> ");
		pageConstructor.append("												<div class=\"tabpage\" id=\"tabpage_1\"> ");
		pageConstructor.append(createIdentityInfoTab(customerIdentityBean, repAction, actionType));
		pageConstructor.append("        											<hr /> ");
		pageConstructor.append("        											<h2>Additional info</h2> ");
		pageConstructor.append(createIdentityInfoAdditionTab());
		pageConstructor.append("        											<h2>File Management</h2> ");
		pageConstructor.append(createIdentityInfoFileManagementTab());
		pageConstructor.append("        											<h2>Old DB Info</h2> ");
		pageConstructor.append(createIdentityInfoOldDBInfo());
//		pageConstructor.append("														<h2>Page 1</h2> ");
//		pageConstructor.append("														<p>test</p> ");
		pageConstructor.append("												</div> ");
		pageConstructor.append("												<div class=\"tabpage\" id=\"tabpage_2\"> ");
		pageConstructor.append(createIdentityContactTab());
		pageConstructor.append("        											<hr /> ");
		pageConstructor.append("        											<h2>Address</h2> ");
		pageConstructor.append(createAddressTab());
		pageConstructor.append("        											<hr /> ");
		pageConstructor.append("        											<h2>Additional Info</h2> ");
		pageConstructor.append(createAdditionalInfoTab());
		/*pageConstructor.append("		        										<p>test2</p> ");
		pageConstructor.append("														<p> test3</p> ");*/
		pageConstructor.append("												</div> ");
		pageConstructor.append("												<div class=\"tabpage\" id=\"tabpage_3\"> ");
		pageConstructor.append(createProfileTab());
		/*	pageConstructor.append("														<h2>Page 3</h2> ");
		pageConstructor.append(" 														<p>test4</p> ");
		pageConstructor.append("														<p>test5</p> ");
		pageConstructor.append("														<p>test6</p> ");*/
		pageConstructor.append("												</div> ");
		pageConstructor.append("												<div class=\"tabpage\" id=\"tabpage_4\"> ");
		pageConstructor.append(createPurchaseTab());
		pageConstructor.append("													<h2>Page 1</h2> ");
		pageConstructor.append("													<p>test</p> ");
		pageConstructor.append("												</div> ");
		pageConstructor.append("												<div class=\"tabpage\" id=\"tabpage_5\"> ");
		pageConstructor.append(createFollowupTab());
		pageConstructor.append("													<h2>Page 1</h2> ");
		pageConstructor.append("													<p>test</p> ");
		pageConstructor.append("												</div> ");
		pageConstructor.append("												<div class=\"tabpage\" id=\"tabpage_6\"> ");
		pageConstructor.append(createFidelityTab());
		pageConstructor.append("													<h2>Page 1</h2> ");
		pageConstructor.append("													<p>test</p> ");
		pageConstructor.append("												</div> ");
		pageConstructor.append("												<div class=\"tabpage\" id=\"tabpage_7\"> ");
		pageConstructor.append(createCustomerAccountTab());
		pageConstructor.append("													<h2>Page 1</h2> ");
		pageConstructor.append("													<p>test</p> ");
		pageConstructor.append("												</div> ");
		pageConstructor.append("												<div class=\"tabpage\" id=\"tabpage_8\"> ");
		pageConstructor.append(createInsuranceTab());
		pageConstructor.append("													<h2>Page 1</h2> ");
		pageConstructor.append("													<p>test</p> ");
		pageConstructor.append("												</div> ");
		pageConstructor.append("	    									</div> ");
		pageConstructor.append("										</div> ");
//		pageConstructor.append("  										<a href=\"#\">Back to List</a>");
		pageConstructor.append(" 									</div>");
		pageConstructor.append("								</td>"); 
		pageConstructor.append("							</tr>"); 
		pageConstructor.append("							</table>");
		pageConstructor.append("						</fieldset >");
		pageConstructor.append("					</td>");
		pageConstructor.append("				</tr>");
		



		pageConstructor.append("				</table>");
		pageConstructor.append("				<hr />");
		pageConstructor.append("			</td>");
		pageConstructor.append("		</tr>");
		//Bottom Buttons Table
		pageConstructor.append("		<tr>");
		pageConstructor.append("			<td align=\"center\">");
		pageConstructor.append("				<table class=\"buttonsTableDown\">");
		pageConstructor.append("				<tr>");
		pageConstructor.append("					<input type=\"hidden\" name=\"actionType\" id=\"actionType\" value=\"N\">");
		pageConstructor.append("					<td width=\"100%\" align=\"right\">");
		pageConstructor.append("						<a href=\"#\" class=\"btn_large btn_blue btn_awesome\" rel=\"nofollow\" onClick=\"window.location.href='CustomerList?"+encryptedString+"'\">Cancel</a>");
		pageConstructor.append("						<a href=\"#\" class=\"btn_large btn_blue btn_awesome\" rel=\"nofollow\" onClick=\"window.location.href='CustomerList?"+encryptedString+"'\">Submit</a>");
		pageConstructor.append("					</td>");
		pageConstructor.append("				</tr>");
		pageConstructor.append("				</table>");
		pageConstructor.append("			</td>");
		pageConstructor.append("		</tr>");
		pageConstructor.append("		</table>");
		pageConstructor.append("	</td>");
		pageConstructor.append("	<td width=\"20%\" valign=\"top\" align=\"center\">");
		pageConstructor.append("		<div style=\"height: 550px; overflow: auto;\">");
		pageConstructor.append("			<table class=\"rightLinksTable\" border=0>");
		pageConstructor.append("			<tr>");
		pageConstructor.append("				<td align=\"center\" width=\"95%\">");
		pageConstructor.append("					<fieldset class=\"subFSet\">");
		pageConstructor.append("						<legend class=\"subFSet\" >Quick Links</legend>");
		pageConstructor.append("						<table class=\"subFSetTable\">");
		pageConstructor.append("							<div style =\"float:left;\" valign=\"bottom\" style=\"font-weight:bold\">");

		pageConstructor.append("								<ul>");
		pageConstructor.append("									<li>");
		pageConstructor.append("							<a href=\"#\" class=\"linkStyle\" rel=\"nofollow\" onClick=\"window.location.href='ClientList?"+requestString+"'\">Change Customer Type</a>");
		pageConstructor.append("									</li>");
		pageConstructor.append("									<hr style=\"width: 100%; height:2px; background: red repeat scroll center; \" />");
		pageConstructor.append("									<li>");
		pageConstructor.append("							<a href=\"#\" class=\"linkStyle\" rel=\"nofollow\" onClick=\"window.location.href='ClientList?"+requestString+"'\">Archive</a>");
		pageConstructor.append("									</li>");
		pageConstructor.append("									<li>");
		pageConstructor.append("							<a href=\"#\" class=\"linkStyle\" rel=\"nofollow\" onClick=\"window.location.href='ClientList?"+requestString+"'\">Reactivate</a>");
		pageConstructor.append("									</li>");
		pageConstructor.append("									<li>");
		pageConstructor.append("							<a href=\"#\" class=\"linkStyle\" rel=\"nofollow\" onClick=\"window.location.href='ClientList?"+requestString+"'\">Delete</a>");
		pageConstructor.append("									</li>");
		pageConstructor.append("									<hr style=\"width: 100%; height:2px; background: red repeat scroll center; \" />");
		pageConstructor.append("									<li>");
		pageConstructor.append("							<a href=\"#\" class=\"linkStyle\" rel=\"nofollow\" onClick=\"window.location.href='ClientList?"+requestString+"'\">Activity History</a>");
		pageConstructor.append("									</li>");
		pageConstructor.append("									<li>");
		pageConstructor.append("							<a href=\"#\" class=\"linkStyle\" rel=\"nofollow\" onClick=\"window.location.href='ClientList?"+requestString+"'\">Print Page</a>");		
		pageConstructor.append("									</li>");
		pageConstructor.append("									<hr style=\"width: 100%; height:2px; background: red repeat scroll center; \" />");
		pageConstructor.append("									<li>");
		pageConstructor.append("							<a href=\"LogoutProcess?"+encryptedString+"\" class=\"linkStyle\" rel=\"nofollow\">Logout</a>");
		pageConstructor.append("									</li>");
		pageConstructor.append("								</ul>");
		pageConstructor.append("							</div>");
		pageConstructor.append("						</table>");
		pageConstructor.append("					</fieldset>");
		pageConstructor.append("				</td>");
		pageConstructor.append("				<td align=\"center\" width=\"5%\"></td>");
		pageConstructor.append("			</tr>");

		pageConstructor.append("			<tr>");
		pageConstructor.append("				<td colspan=\"2\">");
		pageConstructor.append("				<br />");
		pageConstructor.append("				</td>");
		pageConstructor.append("			</tr>");

		pageConstructor.append("			</table>");
		pageConstructor.append("		</div>");
		pageConstructor.append("	</td>");
		pageConstructor.append("</tr>");
		pageConstructor.append("</table>");


		pageConstructor.append(HTMLDesign.getDateRangeCalendarIFrame());


		setPageConstructor(pageConstructor);
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	String createIdentityInfoTab(CustomerIdentityBean customerIdentityBean, String reportAction, String actionType){
		StringBuffer identityTableConstructor = new StringBuffer();
		
		Integer branchID = Integer.parseInt(StringUtil.nullOrEmptyToString(application.getAttribute("defaultBranch"), "0"));
		ArrayList<BranchBean> branchList=adminBO.getBranchRecordListFiltered(branchID, "", "", "", "");
		BranchBean branchBean=null;
		
		if(branchList.size()>=0)
			branchBean = branchList.get(0);
		
		
		identityTableConstructor.append("					<table border=0>");
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("    	       			<td width=\"15%\" align=\"left\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								Branch Name*");
		identityTableConstructor.append("                           </span>");
		identityTableConstructor.append("						</td>");		
		identityTableConstructor.append("						<td width=\"30%\" align=\"left\">");	
		if(reportAction.equals("V") || actionType.equals("edit")){
			identityTableConstructor.append("										<span class=\"note\">");
			identityTableConstructor.append(StringUtil.nullToEmpty(branchBean.getBranchCode()).toUpperCase());
			identityTableConstructor.append("                          				</span>");	
			identityTableConstructor.append("										<input type=\"hidden\" name=\"branchID\" id=\"branchID\" value=\""+StringUtil.nullToEmpty(branchBean.getBranchID())+"\">");
		} else{
			identityTableConstructor.append("							<select name=\"branchID\" id=\"branchID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('branchID','branchName')\" required=\"true\" tabindex=\"8\">");
			for(int i=0; (branchList!=null && i<branchList.size()); i++){
				branchBean = branchList.get(i);
				identityTableConstructor.append("							<option value=\""+branchBean.getBranchID()+"\" "+(branchBean.getBranchID().equals(branchID.toString())?" selected" : "" )+">"+branchBean.getBranchCode()+"</option>");
			}
		}
		identityTableConstructor.append("							</select>");
		identityTableConstructor.append("							<input type=\"hidden\" name=\"branchName\" id=\"branchName\" value=\"\" />");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("    	       			<td width=\"10%\" align=\"right\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								File Number*");
		identityTableConstructor.append("                           </span>");
		identityTableConstructor.append("						</td>");		
		identityTableConstructor.append("						<td width=\"10%\" align=\"left\">");			
		identityTableConstructor.append("							<input name=\"fileNo\" class=\"note\" id=\"fileNo\" type=\"text\" required=\"true\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"4\" value=\"\"  /> ");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("					</tr>");
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("    	       			<td width=\"15%\"  align=\"left\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								Old Key Nyumber*");
		identityTableConstructor.append("                           </span>");
		identityTableConstructor.append("						</td>");		
		identityTableConstructor.append("						<td width=\"20%\" align=\"left\">");			
		identityTableConstructor.append("							<input name=\"oldKeyNo\" class=\"note\" id=\"oldKeyNo\" type=\"text\" required=\"true\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"4\" value=\"\"  /> ");
		identityTableConstructor.append("						</td>");	
		identityTableConstructor.append("               		<td align=\"right\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								File Starting Date*");
		identityTableConstructor.append("                       	</span>");
		identityTableConstructor.append("               		</td>");
		identityTableConstructor.append("						<td width=\"20%\" align=\"left\">");			
		identityTableConstructor.append("							<input name=\"fileStartingDate\" class=\"note\" id=\"fileStartingDate\" type=\"text\" required=\"true\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"4\" value=\"\"  /> ");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("					</tr>");
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("						<td align=\"left\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								Photo: ");
		identityTableConstructor.append("                        	</span>");
		identityTableConstructor.append("		  				</td>");
		identityTableConstructor.append("						<td align=\"left\">");
		identityTableConstructor.append("							<input name=\"photo\" id=\"photo\" type=\"file\" required=\"true\" maxlength=\"15\" size=\"20\" autocomplete=\"off\" onkeypress=\"return (formIns.doLowSecurityFilter(event) && numberInstance.doFilterNumber(event))\"  onpaste=\"return false\" tabindex=\"6\" value=\"\" /> ");
		identityTableConstructor.append("                       </td>");
		identityTableConstructor.append("                       <td align=\"right\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								Full Name");
		identityTableConstructor.append("							</span>");
		identityTableConstructor.append("                        </td>");
		identityTableConstructor.append("						<td align=\"left\">");
		identityTableConstructor.append("							<input name=\"fullName\" id=\"fullName\" type=\"text\" required=\"true\" maxlength=\"15\" size=\"20\" autocomplete=\"off\" onkeypress=\"return (formIns.doLowSecurityFilter(event) && numberInstance.doFilterNumber(event))\"  onpaste=\"return false\" tabindex=\"6\" value=\"\" /> ");
		identityTableConstructor.append("                       </td>");
		identityTableConstructor.append("					</tr>");
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("                      	<td align=\"left\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								Code Name ");
		identityTableConstructor.append("							</span>");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("						<td align=\"left\">");
		identityTableConstructor.append("							<input name=\"codeName\" id=\"codeName\" type=\"text\" required=\"true\" maxlength=\"15\" size=\"10\" autocomplete=\"off\" onkeypress=\"return (formIns.doLowSecurityFilter(event) && numberInstance.doFilterNumber(event))\"  onpaste=\"return false\" tabindex=\"6\" value=\"\" /> ");
		identityTableConstructor.append("                       </td>");
		identityTableConstructor.append("               		<td align=\"right\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								File Aging Date*");
		identityTableConstructor.append("                       	</span>");
		identityTableConstructor.append("               		</td>");
		identityTableConstructor.append("						<td align=\"left\">");
		identityTableConstructor.append("							<input name=\"fileAgingDate\" id=\"fileAgingDate\" type=\"text\" required=\"true\" maxlength=\"15\" size=\"10\" autocomplete=\"off\" onkeypress=\"return (formIns.doLowSecurityFilter(event) && numberInstance.doFilterNumber(event))\"  onpaste=\"return false\" tabindex=\"6\" value=\"\" /> ");
		identityTableConstructor.append("                       </td>");
		identityTableConstructor.append("					</tr>");
		
		
		
		
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("    	       			<td align=\"left\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								File Status*");
		identityTableConstructor.append("                           </span>");
		identityTableConstructor.append("						</td>");		
		identityTableConstructor.append("						<td align=\"left\">");			
		identityTableConstructor.append("							<input name=\"fileStatus\" class=\"note\" id=\"fileStatus\" type=\"text\" required=\"true\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"4\" value=\"\"  /> ");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("						<td  align=\"right\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								First Visit*");
		identityTableConstructor.append("                           </span>");
		identityTableConstructor.append("						</td>");		
		identityTableConstructor.append("						<td align=\"left\">");			
		identityTableConstructor.append("							<input name=\"firstVisit\" class=\"note\" id=\"firstVisit\" type=\"text\" required=\"true\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"4\" value=\"\"  /> ");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("					</tr>");
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("						<td align=\"left\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								Previous Visit*");
		identityTableConstructor.append("                           </span>");
		identityTableConstructor.append("						</td>");		
		identityTableConstructor.append("						<td align=\"left\">");			
		identityTableConstructor.append("							<input name=\"previousVisit\" class=\"note\" id=\"previousVisit\" type=\"text\" required=\"true\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"4\" value=\"\"  /> ");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("						<td align=\"right\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								last Visit*");
		identityTableConstructor.append("                           </span>");
		identityTableConstructor.append("						</td>");		
		identityTableConstructor.append("						<td align=\"left\">");			
		identityTableConstructor.append("							<input name=\"lastVisit\" class=\"note\" id=\"lastVisit\" type=\"text\" required=\"true\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"4\" value=\"\"  /> ");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("					</tr>");
		
		
		identityTableConstructor.append("					</table>");

		
		return identityTableConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	String createIdentityInfoAdditionTab(){
		
		StringBuffer identityTableConstructor = new StringBuffer();
		
		identityTableConstructor.append("					<table border=0>");
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("    	       			<td width=\"10%\" align=\"left\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("							Client For Dep");
		identityTableConstructor.append("							</span>");	
		identityTableConstructor.append("						</td>");	
		identityTableConstructor.append("					</tr>");
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("    	       			<td width=\"90%\" align=\"left\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("							<input type=\"radio\" name=\"clientForDepartment\" id=\"clientForDepartment\" value=\"\"  tabindex=\"9\" > <span class=\"FrmCompTitle\">Info Test OR RX Only&nbsp;</span>");
		identityTableConstructor.append("							<input type=\"radio\" name=\"clientForDepartment\" id=\"clientForDepartment\" value=\"\"  tabindex=\"9\" > <span class=\"FrmCompTitle\">DirectSales&nbsp;</span>");
		identityTableConstructor.append("							<input type=\"radio\" name=\"clientForDepartment\" id=\"clientForDepartment\" value=\"\"  tabindex=\"9\" > <span class=\"FrmCompTitle\">Sales Lenses&nbsp;</span>");
		identityTableConstructor.append("							<input type=\"radio\" name=\"clientForDepartment\" id=\"clientForDepartment\" value=\"\"  tabindex=\"9\" > <span class=\"FrmCompTitle\">Sales Contact Lenses&nbsp;</span>");
		identityTableConstructor.append("							<input type=\"radio\" name=\"clientForDepartment\" id=\"clientForDepartment\" value=\"\"  tabindex=\"9\" > <span class=\"FrmCompTitle\">Sales Hearing Aids&nbsp;</span>");
		identityTableConstructor.append("                           </span>");
		identityTableConstructor.append("						</td>");		
		identityTableConstructor.append("					</tr>");
		identityTableConstructor.append("					</table>");

		
		return identityTableConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	String createIdentityInfoFileManagementTab(){
		StringBuffer identityTableConstructor = new StringBuffer();
		
		identityTableConstructor.append("					<table border=0>");
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("    	       			<td width=\"15%\" align=\"left\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("							<input type=\"checkbox\" name=\"idUpdate\" id=\"idUpdate\" value=\"\" tabindex=\"8\" >ID Update ");
		identityTableConstructor.append("                           </span>");
		identityTableConstructor.append("						</td>");		
		identityTableConstructor.append("						<td width=\"30%\" align=\"left\">");			
		identityTableConstructor.append("							<input type=\"checkbox\" name=\"customerProfileUpdate\" id=\"customerProfileUpdate\" value=\"\" tabindex=\"8\" >Customer Profile Update ");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("    	       			<td width=\"10%\" align=\"right\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("							<input type=\"checkbox\" name=\"toBeDeleted\" id=\"toBeDeleted\" value=\"\" tabindex=\"8\" >To Delete");
		identityTableConstructor.append("                           </span>");
		identityTableConstructor.append("						</td>");		
		identityTableConstructor.append("					</tr>");
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("						<td width=\"10%\" align=\"left\">");			
		identityTableConstructor.append("							<input type=\"checkbox\" name=\"toBeDoubleFileCheck\" id=\"toBeDoubleFileCheck\" value=\"\" tabindex=\"8\" >Double File Check");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("						<td width=\"10%\" align=\"left\">");			
		identityTableConstructor.append("							<input type=\"checkbox\" name=\"toBeArchived\" id=\"toBeArchived\" value=\"\" tabindex=\"8\" >To Be Archived");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("					</tr>");
		identityTableConstructor.append("					</table>");

		
		return identityTableConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	String createIdentityInfoOldDBInfo(){
		StringBuffer identityTableConstructor = new StringBuffer();
		
		identityTableConstructor.append("					<table border=0>");
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("    	       			<td width=\"15%\" align=\"left\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								First-Mid-Last*");
		identityTableConstructor.append("                           </span>");
		identityTableConstructor.append("						</td>");		
		identityTableConstructor.append("						<td width=\"30%\" align=\"left\">");			
		identityTableConstructor.append("							<input name=\"firstMidLast\" class=\"note\" id=\"firstMidLast\" type=\"text\" required=\"true\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"4\" value=\"\"  /> ");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("    	       			<td width=\"10%\" align=\"right\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								Center*");
		identityTableConstructor.append("                           </span>");
		identityTableConstructor.append("						</td>");		
		identityTableConstructor.append("						<td width=\"10%\" align=\"left\">");			
		identityTableConstructor.append("							<input name=\"center\" class=\"note\" id=\"center\" type=\"text\" required=\"true\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"4\" value=\"\"  /> ");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("					</tr>");
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("    	       			<td width=\"15%\"  align=\"left\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								Old File Creation Date*");
		identityTableConstructor.append("                           </span>");
		identityTableConstructor.append("						</td>");		
		identityTableConstructor.append("						<td width=\"20%\" align=\"left\">");			
		identityTableConstructor.append("							<input name=\"oldFileCreationDate\" class=\"note\" id=\"oldFileCreationDate\" type=\"text\" required=\"true\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"4\" value=\"\"  /> ");
		identityTableConstructor.append("						</td>");	
		identityTableConstructor.append("               		<td align=\"right\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								Old File First Date*");
		identityTableConstructor.append("                       	</span>");
		identityTableConstructor.append("               		</td>");
		identityTableConstructor.append("						<td width=\"20%\" align=\"left\">");			
		identityTableConstructor.append("							<input name=\"oldFileFirstDate\" class=\"note\" id=\"oldFileFirstDate\" type=\"text\" required=\"true\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"4\" value=\"\"  /> ");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("					</tr>");
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("						<td align=\"left\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								Old File No ");
		identityTableConstructor.append("                        	</span>");
		identityTableConstructor.append("		  				</td>");
		identityTableConstructor.append("						<td align=\"left\">");
		identityTableConstructor.append("							<input name=\"oldFileNo\" id=\"oldFileNo\" type=\"text\" required=\"true\" maxlength=\"15\" size=\"10\" autocomplete=\"off\" onkeypress=\"return (formIns.doLowSecurityFilter(event) && numberInstance.doFilterNumber(event))\"  onpaste=\"return false\" tabindex=\"6\" value=\"\" /> ");
		identityTableConstructor.append("                       </td>");
		identityTableConstructor.append("                       <td align=\"right\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								Mobile No");
		identityTableConstructor.append("							</span>");
		identityTableConstructor.append("                        </td>");
		identityTableConstructor.append("						<td align=\"left\">");
		identityTableConstructor.append("							<input name=\"mobileNo\" id=\"mobileNo\" type=\"text\" required=\"true\" maxlength=\"15\" size=\"20\" autocomplete=\"off\" onkeypress=\"return (formIns.doLowSecurityFilter(event) && numberInstance.doFilterNumber(event))\"  onpaste=\"return false\" tabindex=\"6\" value=\"\" /> ");
		identityTableConstructor.append("                       </td>");
		identityTableConstructor.append("					</tr>");
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("                      	<td align=\"left\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								Old Contact Lenses");
		identityTableConstructor.append("							</span>");
		identityTableConstructor.append("						</td>");
		identityTableConstructor.append("						<td align=\"left\">");
		identityTableConstructor.append("							<input name=\"oldContactLenses\" id=\"oldContactLenses\" type=\"text\" required=\"true\" maxlength=\"15\" size=\"10\" autocomplete=\"off\" onkeypress=\"return (formIns.doLowSecurityFilter(event) && numberInstance.doFilterNumber(event))\"  onpaste=\"return false\" tabindex=\"6\" value=\"\" /> ");
		identityTableConstructor.append("                       </td>");
		identityTableConstructor.append("               		<td align=\"left\" colspan=\"2\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("								<input type=\"checkbox\" name=\"importedOldOptical\" id=\"importedOldOptical\" value=\"\" tabindex=\"8\" >Imported Old Optical ");
		identityTableConstructor.append("                       	</span>");
		identityTableConstructor.append("               		</td>");
		identityTableConstructor.append("					</tr>");
		identityTableConstructor.append("					<tr class=\"FrmCol2\">");
		identityTableConstructor.append("						<td align=\"left\" colspan=\"2\">&nbsp;");
		identityTableConstructor.append("                       </td>");
		identityTableConstructor.append("						<td align=\"left\" colspan=\"2\">");
		identityTableConstructor.append("							<span class=\"FrmCompTitle\">");
		identityTableConstructor.append("							<input type=\"checkbox\" name=\"importedOldContactLenses\" id=\"importedOldContactLenses\" value=\"\" tabindex=\"8\" >Imported Old Contact Lenses ");
		identityTableConstructor.append("                       	</span>");
		identityTableConstructor.append("                       </td>");
		identityTableConstructor.append("					</tr>");
		identityTableConstructor.append("					</table>");

		
		return identityTableConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	String createIdentityContactTab(){
		
		ArrayList<PersonTitleBean> personTitleList = adminBO.getPersonTitleRecordListFiltered(null, "", "", "", "");
		PersonTitleBean personTitleBean=null;
		
		StringBuffer identityContactTabConstructor = new StringBuffer();
		identityContactTabConstructor.append("			<table border=0>");
		identityContactTabConstructor.append("    	    <tr>");
		identityContactTabConstructor.append("    	     	<td width=\"10%\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Title*");
		identityContactTabConstructor.append("					</span>");
		identityContactTabConstructor.append("				</td>");		
		identityContactTabConstructor.append("				<td width=\"15%\" align=\"left\">");			
		identityContactTabConstructor.append("					<select name=\"personTitleID\" id=\"personTitleID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('personTitleID','personTitleName')\" required=\"true\" tabindex=\"8\">");
		for(int i=0; (personTitleList!=null && i<personTitleList.size()); i++){
			personTitleBean = personTitleList.get(i);
			identityContactTabConstructor.append("						<option value=\""+personTitleBean.getPersonTitleID()+"\">"+personTitleBean.getPersonTitleCode()+"</option>");
		}
		identityContactTabConstructor.append("					</select>");
		identityContactTabConstructor.append("					<input type=\"hidden\" name=\"personTitleName\" id=\"personTitleName\" value=\"\" />");
		identityContactTabConstructor.append("				</td>");
		
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"right\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						First");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"center\">");
		identityContactTabConstructor.append("					<input name=\"firstName\" size=\"20\" required=\"true\" type=\"text\" id=\"firstName\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
		identityContactTabConstructor.append("               </td>");

		identityContactTabConstructor.append("               <td width=\"10%\" align=\"right\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Mid");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"center\">");
		identityContactTabConstructor.append("					<input name=\"midName\" size=\"20\" required=\"true\" type=\"text\" id=\"midName\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
		identityContactTabConstructor.append("               </td>");
		
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"right\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Last");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"center\">");
		identityContactTabConstructor.append("					<input name=\"lastName\" size=\"20\" required=\"true\" type=\"text\" id=\"lastName\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("    	    </tr>");
		
		
		
		identityContactTabConstructor.append("    	    <tr>");
		identityContactTabConstructor.append("    	     	<td width=\"10%\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Birth Date*");
		identityContactTabConstructor.append("					</span>");
		identityContactTabConstructor.append("				</td>");		
		identityContactTabConstructor.append("             	<td width=\"15%\" align=\"center\">");
		identityContactTabConstructor.append("					<input class=\"dateStyle\" name=\"effectiveDate\" readonly size=\"15\" required=\"true\" type=\"text\" id=\"effectiveDate\" maxlength=\"20\" value=\""+simpleDateFormat.format(new java.util.Date())+"\" onfocus=\"this.blur()\"> ");
		/*identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						<a href=\"javascript:void(0)\" onclick=\"if(self.gfPop)gfPop.fEndPop(document.reqForm.systemDate,document.reqForm.effectiveDate);return false;\" tabindex=\"7\">");
		identityContactTabConstructor.append("							<img class=\"PopcalTrigger\" align=\"absmiddle\" src=\"common/calendar/DateRange/calbtn.gif\" width=\"34\" height=\"22\" border=\"0\" alt=\"\">");
		identityContactTabConstructor.append("						</a>");
		identityContactTabConstructor.append("					</span>");*/
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("    	     	<td width=\"10%\" align=\"right\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Age");
		identityContactTabConstructor.append("					</span>");
		identityContactTabConstructor.append("				</td>");
		identityContactTabConstructor.append("    	     	<td width=\"15%\">");
		identityContactTabConstructor.append("					<span class=\"note\">");
		identityContactTabConstructor.append("						");
		identityContactTabConstructor.append("					</span>");
		identityContactTabConstructor.append("				</td>");
		identityContactTabConstructor.append("    	     	<td width=\"10%\" align=\"right\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Age Group");
		identityContactTabConstructor.append("					</span>");
		identityContactTabConstructor.append("				</td>");
		identityContactTabConstructor.append("    	     	<td width=\"15%\">");
		identityContactTabConstructor.append("					<span class=\"note\">");
		identityContactTabConstructor.append("						");
		identityContactTabConstructor.append("					</span>");
		identityContactTabConstructor.append("				</td>");
		
		
		
		
		identityContactTabConstructor.append("    	    </tr>");
		identityContactTabConstructor.append("			</table>");


		return identityContactTabConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	String createAddressTab(){
		
		ArrayList<CountryBean> countryList = adminBO.getCountryRecordListFiltered(null, "", "", "", "");
		CountryBean countryBean=null;
		
		StringBuffer identityContactTabConstructor = new StringBuffer();
		identityContactTabConstructor.append("			<table border=0");
		identityContactTabConstructor.append("    	    <tr>");
		identityContactTabConstructor.append("    	     	<td width=\"10%\" align=\"left\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Zone*");
		identityContactTabConstructor.append("					</span>");
		identityContactTabConstructor.append("				</td>");		
		identityContactTabConstructor.append("				<td width=\"15%\" align=\"left\">");			
		identityContactTabConstructor.append("					<input name=\"zone\" size=\"20\" required=\"true\" type=\"text\" id=\"zone\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
		identityContactTabConstructor.append("				</td>");
		
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"right\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Sector");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		identityContactTabConstructor.append("					<input name=\"sector\" size=\"20\" required=\"true\" type=\"text\" id=\"sector\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("    	    </tr>");
		
		identityContactTabConstructor.append("    	    <tr>");
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"left\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						City");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		identityContactTabConstructor.append("					<input name=\"city\" size=\"20\" required=\"true\" type=\"text\" id=\"city\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
		identityContactTabConstructor.append("               </td>");
		
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"right\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Country");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		identityContactTabConstructor.append("					<select name=\"countryID\" id=\"countryID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('countryID','countryName')\" required=\"true\" tabindex=\"8\">");
		for(int i=0; (countryList!=null && i<countryList.size()); i++){
			countryBean = countryList.get(i);
			identityContactTabConstructor.append("						<option value=\""+countryBean.getCountryID()+"\">"+countryBean.getCountryDesc()+"</option>");
		}
		identityContactTabConstructor.append("					</select>");
		identityContactTabConstructor.append("					<input type=\"hidden\" name=\"countryName\" id=\"countryName\" value=\"\" />");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("    	    </tr>");

		identityContactTabConstructor.append("    	    <tr>");
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"left\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Full Address");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		identityContactTabConstructor.append("					<input name=\"fullAddress\" size=\"20\" required=\"true\" type=\"text\" id=\"fullAddress\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"right\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Proximity");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		identityContactTabConstructor.append("					<select name=\"proximityID\" id=\"proximityID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('proximityID','proximityName')\" required=\"true\" tabindex=\"8\">");
		identityContactTabConstructor.append("						<option value=\"1\">Near</option>");
		identityContactTabConstructor.append("						<option value=\"2\">Far</option>");
		identityContactTabConstructor.append("						<option value=\"3\">Abroad</option>");
		identityContactTabConstructor.append("						<option value=\"4\">Tourist</option>");
		identityContactTabConstructor.append("					</select>");
		identityContactTabConstructor.append("					<input type=\"hidden\" name=\"proximityName\" id=\"proximityName\" value=\"\" />");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("    	    </tr>");
		
		identityContactTabConstructor.append("          <tr>");
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"left\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Note");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		identityContactTabConstructor.append("						<textarea wrap=\"hard\" name=\"note\" id=\"note\" rows=\"2\" cols=\"50\" required=\"true\" onkeypress=\"return ( formIns.doLowSecurityFilter(event));\" onpaste=\"return false;\" tabindex=\"3\"></textarea>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("          </tr>");
		
		
		identityContactTabConstructor.append("			</table>");


		return identityContactTabConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	String createAdditionalInfoTab(){
		ArrayList<PersonTitleBean> personTitleList = adminBO.getPersonTitleRecordListFiltered(null, "", "", "", "");
		PersonTitleBean personTitleBean=null;
		
		StringBuffer identityContactTabConstructor = new StringBuffer();
		identityContactTabConstructor.append("			<table border=0");
		identityContactTabConstructor.append("    	    <tr>");
		identityContactTabConstructor.append("    	     	<td width=\"10%\" align=\"left\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Company/Organization Name*");
		identityContactTabConstructor.append("					</span>");
		identityContactTabConstructor.append("				</td>");		
		identityContactTabConstructor.append("				<td width=\"15%\" align=\"left\">");			
		identityContactTabConstructor.append("					<input name=\"companyName\" size=\"20\" required=\"true\" type=\"text\" id=\"companyName\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
		identityContactTabConstructor.append("				</td>");
		
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"right\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Email");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		identityContactTabConstructor.append("					<input name=\"email\" size=\"20\" required=\"true\" type=\"text\" id=\"email\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("    	    </tr>");
		
		identityContactTabConstructor.append("    	    <tr>");
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"left\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Website");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		identityContactTabConstructor.append("					<input name=\"website\" size=\"20\" required=\"true\" type=\"text\" id=\"website\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
		identityContactTabConstructor.append("               </td>");
		
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"right\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Twitter");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		identityContactTabConstructor.append("					<input name=\"twitter\" size=\"20\" required=\"true\" type=\"text\" id=\"twitter\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("    	    </tr>");

		identityContactTabConstructor.append("    	    <tr>");
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"left\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Facebook");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		identityContactTabConstructor.append("					<input name=\"facebook\" size=\"20\" required=\"true\" type=\"text\" id=\"facebook\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"right\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						WhatsUp");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		identityContactTabConstructor.append("					<input name=\"whatsup\" size=\"20\" required=\"true\" type=\"text\" id=\"whatsup\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("    	    </tr>");
		
		identityContactTabConstructor.append("          <tr>");
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"left\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("						Note");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		identityContactTabConstructor.append("						<textarea wrap=\"hard\" name=\"note\" id=\"note\" rows=\"2\" cols=\"50\" required=\"true\" onkeypress=\"return ( formIns.doLowSecurityFilter(event));\" onpaste=\"return false;\" tabindex=\"3\"></textarea>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("          </tr>");
		
		identityContactTabConstructor.append("          <tr>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("					<input type=\"checkbox\" name=\"addToMoursel\" id=\"addToMoursel\" value=\"\" tabindex=\"8\" >Add To Moursel ");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"10%\" align=\"left\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("					<input type=\"checkbox\" name=\"addToFamily\" id=\"addToFamily\" value=\"\" tabindex=\"8\" >Add To Family ");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		identityContactTabConstructor.append("					<span class=\"FrmCompTitle\">");
		identityContactTabConstructor.append("					<input type=\"checkbox\" name=\"addToGroup\" id=\"addToGroup\" value=\"\" tabindex=\"8\" >Add To Group ");
		identityContactTabConstructor.append("                  </span>");
		identityContactTabConstructor.append("               </td>");
		identityContactTabConstructor.append("          </tr>");
		
		
		identityContactTabConstructor.append("			</table>");


		return identityContactTabConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	String createProfileTab(){
		StringBuffer profileTabConstructor = new StringBuffer();
		
		ArrayList<RatingBean> ratingList = adminBO.getRatingRecordListFiltered(null, "", "", "", "");
		RatingBean ratingBean=null;
		
		ArrayList<ProfessionalFieldBean> professionalFieldList = adminBO.getProfessionalFieldRecordListFiltered(null, "", "", "", "");
		ProfessionalFieldBean professionalFieldBean=null;
		
		ArrayList<ProfessionBean> professionList = adminBO.getProfessionRecordListFiltered(null, "", "", "", "");
		ProfessionBean professionBean=null;
		
		
		profileTabConstructor.append("			<table border=0>");
		profileTabConstructor.append("    	    <tr>");
		profileTabConstructor.append("    	     	<td width=\"10%\" align=\"left\">");
		profileTabConstructor.append("					<span class=\"FrmCompTitle\">");
		profileTabConstructor.append("						Service Cagetory*");
		profileTabConstructor.append("					</span>");
		profileTabConstructor.append("				</td>");		
		profileTabConstructor.append("				<td width=\"15%\" align=\"left\">");			
		profileTabConstructor.append("					<select name=\"profCategoryID\" id=\"profCategoryID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('profCategoryID','profCategoryName')\" required=\"true\" tabindex=\"8\">");
		for(int i=0; (ratingList!=null && i<ratingList.size()); i++){
			ratingBean = ratingList.get(i);
			profileTabConstructor.append("						<option value=\""+ratingBean.getRatingID()+"\">"+ratingBean.getRatingDesc()+"</option>");
		}
		profileTabConstructor.append("					</select>");
		profileTabConstructor.append("					<input type=\"hidden\" name=\"profCategoryName\" id=\"Name\" value=\"\" />");
		profileTabConstructor.append("				</td>");

		profileTabConstructor.append("    	     	<td width=\"10%\" align=\"left\">");
		profileTabConstructor.append("					<span class=\"FrmCompTitle\">");
		profileTabConstructor.append("						Social Cagetory*");
		profileTabConstructor.append("					</span>");
		profileTabConstructor.append("				</td>");		
		profileTabConstructor.append("				<td width=\"15%\" align=\"left\">");			
		profileTabConstructor.append("					<select name=\"profCategoryID\" id=\"profCategoryID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('profCategoryID','profCategoryName')\" required=\"true\" tabindex=\"8\">");
		for(int i=0; (ratingList!=null && i<ratingList.size()); i++){
			ratingBean = ratingList.get(i);
			profileTabConstructor.append("						<option value=\""+ratingBean.getRatingID()+"\">"+ratingBean.getRatingDesc()+"</option>");
		}
		profileTabConstructor.append("					</select>");
		profileTabConstructor.append("					<input type=\"hidden\" name=\"profCategoryName\" id=\"Name\" value=\"\" />");
		profileTabConstructor.append("				</td>");
		profileTabConstructor.append("          </tr>");

		profileTabConstructor.append("          <tr>");
		profileTabConstructor.append("    	     	<td width=\"10%\" align=\"left\">");
		profileTabConstructor.append("					<span class=\"FrmCompTitle\">");
		profileTabConstructor.append("						Financial Cagetory*");
		profileTabConstructor.append("					</span>");
		profileTabConstructor.append("				</td>");		
		profileTabConstructor.append("				<td width=\"15%\" align=\"left\">");			
		profileTabConstructor.append("					<select name=\"profCategoryID\" id=\"profCategoryID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('profCategoryID','profCategoryName')\" required=\"true\" tabindex=\"8\">");
		for(int i=0; (ratingList!=null && i<ratingList.size()); i++){
			ratingBean = ratingList.get(i);
			profileTabConstructor.append("						<option value=\""+ratingBean.getRatingID()+"\">"+ratingBean.getRatingDesc()+"</option>");
		}
		profileTabConstructor.append("					</select>");
		profileTabConstructor.append("					<input type=\"hidden\" name=\"profCategoryName\" id=\"Name\" value=\"\" />");
		profileTabConstructor.append("				</td>");
		
		profileTabConstructor.append("               <td width=\"10%\" align=\"left\">");
		profileTabConstructor.append("					<span class=\"FrmCompTitle\">");
		profileTabConstructor.append("						Professional Field");
		profileTabConstructor.append("                  </span>");
		profileTabConstructor.append("               </td>");
		profileTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		profileTabConstructor.append("					<select name=\"profFieldID\" id=\"profFieldID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('profFieldID','profFieldName')\" required=\"true\" tabindex=\"8\">");
		for(int i=0; (professionalFieldList!=null && i<professionalFieldList.size()); i++){
			professionalFieldBean = professionalFieldList.get(i);
			profileTabConstructor.append("						<option value=\""+professionalFieldBean.getProFieldID()+"\">"+professionalFieldBean.getProFieldID()+"</option>");
		}
		profileTabConstructor.append("					</select>");
		profileTabConstructor.append("					<input type=\"hidden\" name=\"profFieldName\" id=\"Name\" value=\"\" />");
		profileTabConstructor.append("               </td>");
		profileTabConstructor.append("          </tr>");

		profileTabConstructor.append("          <tr>");
		profileTabConstructor.append("               <td width=\"10%\" align=\"left\">");
		profileTabConstructor.append("					<span class=\"FrmCompTitle\">");
		profileTabConstructor.append("						Profession");
		profileTabConstructor.append("                  </span>");
		profileTabConstructor.append("               </td>");
		profileTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		profileTabConstructor.append("					<select name=\"professionID\" id=\"professionID\" onkeypress=\"this.onchange()\" onChange=\"setDropDownData('professionID','professionName')\" required=\"true\" tabindex=\"8\">");
		for(int i=0; (professionList!=null && i<professionList.size()); i++){
			professionBean = professionList.get(i);
			profileTabConstructor.append("						<option value=\""+professionBean.getProfessionID()+"\">"+professionBean.getProfessionID()+"</option>");
		}
		profileTabConstructor.append("					</select>");
		profileTabConstructor.append("					<input type=\"hidden\" name=\"professionName\" id=\"professionName\" value=\"\" />");
		profileTabConstructor.append("               </td>");
		
		
		profileTabConstructor.append("               <td width=\"10%\" align=\"left\">");
		profileTabConstructor.append("					<span class=\"FrmCompTitle\">");
		profileTabConstructor.append("						Job Position");
		profileTabConstructor.append("                  </span>");
		profileTabConstructor.append("               </td>");
		profileTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		profileTabConstructor.append("					<input name=\"jobPosition\" size=\"20\" required=\"true\" type=\"text\" id=\"jobPosition\" maxlength=\"20\" value=\"\" onfocus=\"this.blur()\"> ");
		profileTabConstructor.append("               </td>");
		profileTabConstructor.append("          </tr>");
		
		profileTabConstructor.append("          <tr>");
		profileTabConstructor.append("               <td width=\"10%\" align=\"left\">");
		profileTabConstructor.append("					<span class=\"FrmCompTitle\">");
		profileTabConstructor.append("						Note");
		profileTabConstructor.append("                  </span>");
		profileTabConstructor.append("               </td>");
		profileTabConstructor.append("               <td width=\"15%\" align=\"left\">");
		profileTabConstructor.append("						<textarea wrap=\"hard\" name=\"note\" id=\"note\" rows=\"2\" cols=\"50\" required=\"true\" onkeypress=\"return ( formIns.doLowSecurityFilter(event));\" onpaste=\"return false;\" tabindex=\"3\"></textarea>");
		profileTabConstructor.append("               </td>");
		profileTabConstructor.append("          </tr>");
		profileTabConstructor.append("          </table>");

		return profileTabConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	String createPurchaseTab(){
		StringBuffer purchaseTabConstructor = new StringBuffer();

		return purchaseTabConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	String createFollowupTab(){
		StringBuffer followupTabConstructor = new StringBuffer();

		return followupTabConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	String createFidelityTab(){
		StringBuffer fidelityTabConstructor = new StringBuffer();

		return fidelityTabConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	String createCustomerAccountTab(){
		StringBuffer customerAccountTabConstructor = new StringBuffer();

		return customerAccountTabConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	String createInsuranceTab(){
		StringBuffer insuranceTabConstructor = new StringBuffer();

		return insuranceTabConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/**
	 * 
	 * 
	 * 
	 * @param request
	 * @param parameterList
	 * @param userType
	 * @param reportAction
	 * @param actionType
	 * @param viewType
	 * @param recordStatus
	 * @return
	 */
	private String constructDuplicateButton(HttpServletRequest request, String parameterList, String userType, String reportAction, String actionType, String viewType){		
		/**
		 *  DUPLICATE BUTTON SHOULD BE SHOWN IF:
		 *  1-USERTYPE = user
		 *  2-Report Action: 'V'
		 *  3-Action Type is Empty
		 *  4-ViewType Is Not 'H'
		 *  5-Status in 'A' Or 'I'
		 */

		StringBuffer buttonConstructor = new StringBuffer();
		String encryptedString = QueryCrypt.encrypt(request, parameterList.toString());

		if( userType.equals("user") && reportAction.equals("V") && actionType.equals("") 
				&& !viewType.equals("H") ){

			buttonConstructor.append("	<a href=\"#\" class=\"btn_large btn_blue btn_awesome\" rel=\"nofollow\" ");
			buttonConstructor.append("			onClick=\"var x = confirm('are you sure?'); if(x){this.disabled='disabled';createLoadingImg();submitForm('document.reqForm','"+this.getServletName()+"?" + encryptedString + "','duplicate');}\" >");
			buttonConstructor.append("		Duplicate");
			buttonConstructor.append("	</a>");

		}else{
			buttonConstructor.append("&nbsp;");
		}


		return buttonConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * @param request
	 * @param parameterList
	 * @param userType
	 * @param reportAction
	 * @param actionType
	 * @param viewType
	 * @param recordStatus
	 * @return
	 */
	private String constructBackButton(HttpServletRequest request, String parameterList, String userType, String reportAction, String actionType, String viewType){
		/**
		 * ViewType != 'H'
		 * 
		 */

		StringBuffer buttonConstructor = new StringBuffer();
		String encryptedParameterList = QueryCrypt.encrypt(request, parameterList.toString());

		if(!viewType.equals("H")){
			buttonConstructor.append("	<a href=\"#\" class=\"btn_large btn_blue btn_awesome\" rel=\"nofollow\" ");
			buttonConstructor.append("			onClick=\"this.disabled='disabled';createLoadingImg();window.location.href='CustomerList?"+encryptedParameterList+"';\" >");
			buttonConstructor.append("		BACK");
			buttonConstructor.append("	</a>");
		} else{
			buttonConstructor.append("&nbsp;");
		}

		return buttonConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * @param request
	 * @param parameterList
	 * @param userType
	 * @param reportAction
	 * @param actionType
	 * @param viewType
	 * @param recordStatus
	 * @return
	 */
	private String constructEditTermsButton(HttpServletRequest request, String parameterList, String userType, String reportAction, String actionType, String viewType){
		/**
		 * USERTYPE = 'user'
		 * repAction='V'
		 * actionType is empty
		 * viewType != 'H'
		 * status in 'A', 'N', 'R', 'P'
		 */

		StringBuffer buttonConstructor = new StringBuffer();
		String encryptedParameterList = QueryCrypt.encrypt(request, parameterList.toString());

		if( userType.equals("user") &&  reportAction.equals("V") && actionType.equals("") && !viewType.equals("H")
				 ){
			buttonConstructor.append("	<a href=\"#\" class=\"btn_large btn_blue btn_awesome\" rel=\"nofollow\" ");
			buttonConstructor.append("			 onClick=\"this.disabled='disabled';createLoadingImg();submitForm('document.reqForm','"+this.getServletName()+"?" + encryptedParameterList + "','edit');\" >");
			buttonConstructor.append("		Edit Terms");
			buttonConstructor.append("	</a>");
		} else{
			buttonConstructor.append("&nbsp;");
		}

		return buttonConstructor.toString();
	}
	/**
	 *
	 * 
	 * 
	 * @param request
	 * @param parameterList
	 * @param userType
	 * @param reportAction
	 * @param actionType
	 * @param viewType
	 * @param recordStatus
	 * @return
	 */
	private String constructSubmitButton(HttpServletRequest request, String parameterList, String userType, String reportAction, String actionType, String viewType){
		/**
		 * USERTYPE = user
		 * report action in (empty, 'N')
		 */
		StringBuffer buttonConstructor = new StringBuffer();
		String encryptedParameterList = QueryCrypt.encrypt(request, parameterList.toString());

		if( userType.equals("user") && (reportAction.equals("") || reportAction.equals("N")) ){
			buttonConstructor.append("	<a href=\"#\" class=\"btn_large btn_blue btn_awesome\" rel=\"nofollow\"");
			buttonConstructor.append("			onclick=\"if(validateForm()) {this.disabled='disabled';createLoadingImg();submitForm('document.reqForm','"+this.getServletName()+"?" + encryptedParameterList + "','new');}\">");
			buttonConstructor.append("		SUBMIT");
			buttonConstructor.append("	</a>");
		} else{
			buttonConstructor.append("&nbsp;");
		}


		return buttonConstructor.toString();
	}
	/**
	 *
	 * 
	 * 
	 * @param request
	 * @param parameterList
	 * @param userType
	 * @param reportAction
	 * @param actionType
	 * @param viewType
	 * @param recordStatus
	 * @return
	 */
	private String constructCancelButton(HttpServletRequest request, String parameterList, String userType, String reportAction, String actionType, String viewType){
		/**
		 * USERTYPE = user
		 * report action in (empty, 'N')
		 * 
		 */
		StringBuffer buttonConstructor = new StringBuffer();
		String encryptedParameterList = QueryCrypt.encrypt(request, parameterList.toString());

		if(userType.equals("user") && (reportAction.equals("") || reportAction.equals("N")) ){
			buttonConstructor.append("	<a href=\"#\" class=\"btn_large btn_blue btn_awesome\" rel=\"nofollow\"");
			buttonConstructor.append("			onClick=\"this.disabled='disabled';createLoadingImg();window.location.href='CustomerList?"+encryptedParameterList+"';\">");
			buttonConstructor.append("		CANCEL");
			buttonConstructor.append("	</a>");
		} else{
			buttonConstructor.append("&nbsp;");
		}

		return buttonConstructor.toString();
	}
	/**
	 * 
	 * 
	 * 
	 * @param request
	 * @param parameterList
	 * @param userType
	 * @param reportAction
	 * @param actionType
	 * @param viewType
	 * @param recordStatus
	 * @return
	 */
	private String constructDeleteButton(HttpServletRequest request, String parameterList, String userType, String reportAction, String actionType, String viewType){
		/**
		 * userType: user
		 * report action: 'V'
		 * view tpye != 'H'
		 * status in (N,R)
		 * 
		 */

		StringBuffer buttonConstructor = new StringBuffer();
		String encryptedParameterList = QueryCrypt.encrypt(request, parameterList.toString());

		if(userType.equals("user") && reportAction.equals("V") && !viewType.equals("H") 
				 ){
			buttonConstructor.append("	<a href=\"#\" class=\"btn_large btn_blue btn_awesome\" rel=\"nofollow\"");
			buttonConstructor.append("			onclick=\"var x = confirm('Are you sure?'); if(x){this.disabled='disabled';createLoadingImg();submitForm('document.reqForm','"+this.getServletName()+"?" + encryptedParameterList + "','delete');}\">");
			buttonConstructor.append("		DELETE");
			buttonConstructor.append("	</a>");
		} else{
			buttonConstructor.append("&nbsp;");
		}

		return buttonConstructor.toString();
	}
}
