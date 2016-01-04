package com.dev.admin.report;

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
import com.hdc.sysdev.utils.HTMLUtil;
import com.hdc.sysdev.utils.NavigationSortingUtil;
import com.hdc.sysdev.utils.StringUtil;
import com.hdc.sysdev.utils.beans.NavigationBean;
import com.optica.admin.Business.AdminBO;
import com.optica.admin.common.base.PageSecureServlet;
import com.optica.admin.common.design.HTMLDesign;
import com.optica.admin.formbeans.ResultMessageBean;
import com.optica.bean.customer.CustomerBean;
import com.optica.bean.customer.CustomerIdentityBean;

public class CustomerList extends PageSecureServlet {
	/**
	 * 
	 * 
	 * 
	 */
	private final Logger logger = Logger.getLogger(CustomerList.class);
	AdminBO adminBO = (AdminBO)application.getAttribute("adminBO");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(StringUtil.nullOrEmptyToString(application.getAttribute("defaultDateFormat"), "dd/MM/yyyy"));
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
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * 
	 * 
	 * Constructor of the object.
	 */
	public CustomerList() {
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

		logger.info("\n\na Customer List...");

		HttpSession session = request.getSession(true);
		
		session.removeAttribute("oldCustomerID");
		session.removeAttribute("customerID");
		session.removeAttribute("customerBean");

		try{
			if(session.getAttribute("userID")!= null ){

				this.setLanguage(session, request);
				this.setIncludeMenu(true);
				this.setBrowserEncoding();
				this.setDatabaseEncoding();

				StringBuffer scriptIncludeBuffer = new StringBuffer();
				scriptIncludeBuffer.append("	<script language=\"Javascript\" src=\"js/Plan/PlanList.js\"></script>");
				scriptIncludeBuffer.append("	<link rel=\"stylesheet\" type=\"text/css\"  href=\"clientassets/files_optica/css/font.css\">");
				setScriptIncludes(new String(scriptIncludeBuffer));
				scriptIncludeBuffer = null;

				StringBuffer jsInstanceBuffer = new StringBuffer();
				jsInstanceBuffer.append(" 		formIns = getFormUtil(\"reqForm\");");
				//			jsInstanceBuffer.append(" 		numberInstance = getNumberUtil();");
				setJsInstance(new String(jsInstanceBuffer));
				jsInstanceBuffer = null;

				this.setIncludeLeftMenu(true);

				setOnLoadFunctions("document.getElementById('customerSection').className += 'CURRENT';");

				RequestParameterObject rpo = QueryCrypt.decrypt(request);
				ArrayList<CustomerBean> customerList = null;

				String actionType = StringUtil.nullToEmpty(request.getParameter("actionType"));
				logger.info("actionType: " + actionType);


				//			Get the parameter of sorting and navigation
				String sort = (String) rpo.getParameter("sort");
				if (sort == null)
					sort = "2";
				String formAction = rpo.getParameter("formAction");
				if (formAction == null)
					formAction = "0";
				String direction = (String) rpo.getParameter("direction");
				if (direction == null)
					direction = "asc";

				if(formAction.equals("Sort")){
					if (sort != null && sort.equals("2")) {
						direction = NavigationSortingUtil.getSortingDirection("code",request);
					} else if (sort != null && sort.equals("3")) {
						direction = NavigationSortingUtil.getSortingDirection("title", request);
					} else if (sort != null && sort.equals("4")) {
						direction = NavigationSortingUtil.getSortingDirection("productName", request);
					} else if (sort != null && sort.equals("5")) {
						direction = NavigationSortingUtil.getSortingDirection("effectiveDate", request);
					} else if (sort != null && sort.equals("9")) {
						direction = NavigationSortingUtil.getSortingDirection("status", request);
					}
				}

				String criteriaType = "";;
				String criteriaValue = "";
				if (actionType.equals("search") ) {

					criteriaType = StringUtil.nullToEmpty(request.getParameter("filterCriteriaID"));
					criteriaValue = StringUtil.nullToEmpty(request.getParameter("filterBy"));

					logger.info("criteriaType: " + criteriaType + "\ncriteriaValue: " + criteriaValue);

					if(!criteriaType.equals("")){

						if(criteriaType.equals("1")) // code
							criteriaType = "PLAN_CODE";
						else if(criteriaType.equals("2")) // title
							criteriaType = "PLAN_DESC";
						else if(criteriaType.equals("3")) // product name
							criteriaType = "PRODUCT_CODE";
						else if(criteriaType.equals("4")) // status
							criteriaType = "PSTATUS";
					}
					customerList = adminBO.getCustomerRecordListFiltered(null, criteriaType, criteriaValue, sort, direction);
					
				}else {
					customerList = adminBO.getCustomerRecordListFiltered(null, null, null, sort, direction);

				}


				setTitle("Customer List");
				setValueBean("customerListBean");

				constructPage(request, session, response, customerList, rpo, sort, direction, actionType, criteriaValue);
			}
			super.doPost(request, response);
		}catch (Exception e) {
			logger.error("ERROR", e); 
			response.sendRedirect("ErrorHandler");
		}
	}
	/**
	 * 
	 * 
	 * 	
	 * @param request
	 * @param session
	 * @param response
	 * @param ruleList
	 * @param rpo
	 * @param sort
	 * @param direction
	 * @param actionType
	 * @param criteriaTypeID
	 * @param criteriaValue
	 * @param isIncludingInactive
	 */
	public void constructPage(HttpServletRequest request, HttpSession session, HttpServletResponse response, ArrayList<CustomerBean> customerList, 
			RequestParameterObject rpo, String sort, String direction, String actionType, String criteriaValue){

		StringBuffer pageConstructor = new StringBuffer();
		CustomerBean customerBean = null;

		String roleID = StringUtil.nullToString(request.getSession().getAttribute("roleID"), "1");
		
		ResultMessageBean resultMessageBean = (ResultMessageBean) session.getAttribute("resultMessageBean");
		String userType = StringUtil.nullToEmpty(session.getAttribute("userType"));
		
		
		String requestString = "formLastAccess=" + session.getAttribute("loginTime");
		String encryptedString = QueryCrypt.encrypt(request, requestString);
		int numOfRecordToShow = Integer.parseInt(application.getAttribute("numOfRecordToShow").toString());
		int currentPageNum = Integer.parseInt(StringUtil.nullToEmpty(request.getParameter("actionType")).equals("search") ? "0" : StringUtil.nullOrEmptyToString(rpo.getParameter("pageNumber"), "0"));
		//		int currentPageNum = Integer.parseInt(((request.getParameter("pageNumber")) == null) ? "0" : request.getParameter("pageNumber"));
		logger.info("\nruleList: " + customerList.size());

		NavigationBean limits = NavigationSortingUtil.getNavigationLimits(currentPageNum, numOfRecordToShow, customerList.size());
		int startNumber = limits.getStartNavigationRowNo();
		int endNumber = limits.getEndNavigationRowNo();
		int maxNumPage = limits.getLastRowNo();



		StringBuffer staticSortingParameterList = new StringBuffer();
		staticSortingParameterList.append( requestString);
		staticSortingParameterList.append("&pageNumber=");
		staticSortingParameterList.append(currentPageNum);
		staticSortingParameterList.append("&formAction=Sort");
		staticSortingParameterList.append("&sort=");


		pageConstructor.append("<table border=0 class=\"mainTableWrapper\">");
		pageConstructor.append("<tr>");
		pageConstructor.append("	<td>");

		if(resultMessageBean != null ){

			if(resultMessageBean.isSucceeded()){
				pageConstructor.append("		<div id=\"msg_box_id\" name=\"msg_box_id\" class=\"msg_box_good\">");
				pageConstructor.append("			<h4><img src=\""+ clientDirName +"/assets/common/successIcon.jpg\" width=\"20\" height=\"20\">");				
				pageConstructor.append(					"&nbsp;&nbsp;"	+ resultMessageBean.getResultDescription());
				pageConstructor.append(					"&nbsp;|"+ "<a  onclick=\"document.getElementById('msg_box_id').style.display='none';return false;\" href=\"#\">Close</a>");
				pageConstructor.append("   			</h4>");
				pageConstructor.append("		</div>");
			} else{
				pageConstructor.append("		<div id=\"msg_box_id\" name=\"msg_box_id\" class=\"msg_box_bad\">");
				pageConstructor.append("			<h4><img src=\""+ clientDirName +"/assets/common/errorIcon.png\" width=\"20\" height=\"20\">");
				pageConstructor.append(					"&nbsp;&nbsp;"	+ resultMessageBean.getResultDescription());
				pageConstructor.append(					"&nbsp;|"+ "<a  onclick=\"document.getElementById('msg_box_id').style.display='none';return false;\" href=\"#\">Close</a>");
				pageConstructor.append("   			</h4>");
				pageConstructor.append("		</div>");
			}
			
			session.removeAttribute("resultMessageBean");
		}

		pageConstructor.append("	</td>");
		pageConstructor.append("</tr>");


		/*pageConstructor.append("<tr>");
		pageConstructor.append("	<td align=\"center\">");

		pageConstructor.append("		<table class=\"searchBarTable\" width=\"70%\">");
		pageConstructor.append("			<tr>");
		pageConstructor.append("				<td width=\"30%\">");
		pageConstructor.append("					<span class=\"FrmCompTitle\">");
		pageConstructor.append("						Criteria &nbsp;&nbsp;");
		pageConstructor.append("						<input autofocus=\"autofocus\" name=\"filterBy\" id=\"filterBy\" type=\"text\" size=\"25\" maxlength=\"25\" label=\"Filter By\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" tabindex=\"1\" "+(actionType.equals("search") ? "disabled" : "" )+" value=\""+criteriaValue+"\" /> ");
		pageConstructor.append("					</span>");
		pageConstructor.append("				</td>");
		pageConstructor.append("				<td width=\"30%\">");
		pageConstructor.append("					<span class=\"FrmCompTitle\">");
		pageConstructor.append("						Filter Type &nbsp;&nbsp;");
		pageConstructor.append("						<select name=\"filterCriteriaID\" id=\"filterCriteriaID\" label=\"Criteria\" tabindex=\"4\" "+(actionType.equals("search") ? "disabled" : "" )+" >");
//		pageConstructor.append("							<option title=\"\" value=\"0\"></option>");
		pageConstructor.append("							<option title=\"Search By Rule Code\" value=\"1\" "+(criteriaTypeID.equals("1") ? " selected" : "")+" >Code</option>");
		pageConstructor.append("							<option title=\"Search By Special Rule Code\" value=\"2\" "+(criteriaTypeID.equals("2") ? " selected" : "")+" >Name</option>");
		pageConstructor.append("							<option title=\"Search by Rule Description\" value=\"3\" "+(criteriaTypeID.equals("3") ? " selected" : "")+">Title</option>");
		pageConstructor.append("							<option title=\"Search By Status\" value=\"4\" "+(criteriaTypeID.equals("4") ? " selected" : "")+">Status</option>");
		pageConstructor.append("						</select>");
		pageConstructor.append("					</span>");
		pageConstructor.append("				</td>");
		pageConstructor.append("				<td width=\"25%\">");
		pageConstructor.append("					<span class=\"FrmCompTitle\">");
		pageConstructor.append("						<input type=\"checkbox\" name=\"includeInactive\" id=\"includeInactive\" value=\"n\" onclick=\"changeInactiveRuleCheckbox();\" "+(actionType.equals("search") ? "disabled" : "")+(isIncludingInactive.equals("y") ? "checked" : "")+">Include Inactive Rules");
		pageConstructor.append("					</span>");
		pageConstructor.append("				</td>");
		pageConstructor.append("				<td width=\"15%\" align=\"center\">");
		pageConstructor.append("					<input type=\"hidden\" name=\"actionType\" id=\"actionType\" value=\"N\">");
		pageConstructor.append("					<a href=\"#\" class=\"btn_medium btn_orange btn_awesome\" rel=\"nofollow\" onclick=\"if(ValidateForm()){submitForm('document.reqForm','"+this.getServletName()+"?" + encryptedString + "', "+(actionType.equals("search")?"'reset'":"'search'")+");}\">"+(actionType.equals("search") ? "RESET" : "SEARCH")+"</a>");
		pageConstructor.append("				</td>");
		pageConstructor.append("			</tr>");
		pageConstructor.append("		</table>");

		pageConstructor.append("	</td>");
		pageConstructor.append("</tr>");*/

		/*pageConstructor.append("<tr>");
		pageConstructor.append("	<td>");
		pageConstructor.append("  		<br />");
		pageConstructor.append("  	</td>");
		pageConstructor.append("</tr>");*/

		pageConstructor.append("<tr>");
		pageConstructor.append("	<td align=\"center\">");
		pageConstructor.append("		<fieldset class=\"mainFSet\">");
		pageConstructor.append("			<legend class=\"mainFSet\" >Customer List</legend>");
		pageConstructor.append("			<table class=\"mainFSetTable\">");
		//		pageConstructor.append("		<table class=\"mainRepTable\" border=\"1\">");
		/*pageConstructor.append("		<tr>");
		pageConstructor.append("			<td class=\"reportHeader\">");
		pageConstructor.append("				Plan List");
		pageConstructor.append("		  	</td>");
		pageConstructor.append("		</tr>");*/

		pageConstructor.append("		<tr>");
		pageConstructor.append("			<td>");
		pageConstructor.append("				<table class=\"mainDataTable\">");
		pageConstructor.append("				<tr class=\"RepHeaderCol\">");
		pageConstructor.append("					<td id=\"mainDataTable\" width=\"20%\" class=\"HeaderLink\">");
		pageConstructor.append("						<a class=\"HeaderLink\" "+HTMLDesign.getSortingLink(this.getServletName(), staticSortingParameterList, request, "4") +">");
		pageConstructor.append("							File Number");
		pageConstructor.append("						</a>");
		pageConstructor.append(HTMLDesign.getSortingArrowIfApplicable("4", sort, direction, clientDirName));
		pageConstructor.append("					</td>");
		pageConstructor.append("					<td width=\"30%\" class=\"HeaderNoLink\">");
		pageConstructor.append("						<a class=\"HeaderLink\" "+HTMLDesign.getSortingLink(this.getServletName(), staticSortingParameterList, request, "6") +">");
		pageConstructor.append("							First Name");
		pageConstructor.append("						</a>");
		pageConstructor.append(HTMLDesign.getSortingArrowIfApplicable("6", sort, direction, clientDirName));
		pageConstructor.append("					</td>");
		pageConstructor.append("					<td width=\"20%\" class=\"HeaderNoLink\">");
		pageConstructor.append("						<a class=\"HeaderLink\" "+HTMLDesign.getSortingLink(this.getServletName(), staticSortingParameterList, request, "5") +">");
		pageConstructor.append("							Middle Name");
		pageConstructor.append("						</a>");
		pageConstructor.append(HTMLDesign.getSortingArrowIfApplicable("5", sort, direction, clientDirName));
		pageConstructor.append("					</td>");
		pageConstructor.append("					<td width=\"20%\" class=\"HeaderNoLink\">");
		pageConstructor.append("						<a class=\"HeaderLink\" "+HTMLDesign.getSortingLink(this.getServletName(), staticSortingParameterList, request, "9") +">");
		pageConstructor.append("							Last Name");
		pageConstructor.append("						</a>");
		pageConstructor.append(HTMLDesign.getSortingArrowIfApplicable("9", sort, direction, clientDirName));
		pageConstructor.append("					</td>");
		pageConstructor.append("					<td width=\"10%\" class=\"HeaderNoLink\">");
		pageConstructor.append("						<a class=\"HeaderLink\" "+HTMLDesign.getSortingLink(this.getServletName(), staticSortingParameterList, request, "8") +">");
		pageConstructor.append("							First Sales Date");
		pageConstructor.append("						</a>");
		pageConstructor.append(HTMLDesign.getSortingArrowIfApplicable("8", sort, direction, clientDirName));
		pageConstructor.append("					</td>");	
		pageConstructor.append("					<td width=\"10%\" class=\"HeaderNoLink\">");
		pageConstructor.append("						<a class=\"HeaderLink\" "+HTMLDesign.getSortingLink(this.getServletName(), staticSortingParameterList, request, "8") +">");
		pageConstructor.append("							Age");
		pageConstructor.append("						</a>");
		pageConstructor.append(HTMLDesign.getSortingArrowIfApplicable("8", sort, direction, clientDirName));
		pageConstructor.append("					</td>");
		pageConstructor.append("				</tr>");





		if (customerList!=null && customerList.size() > 0) {
			String rowColorClass = "";
			customerBean = new CustomerBean();
			StringBuffer viewDetailsLinkParameters = new StringBuffer();
			String encryptedViewDetailsLinkParameters = null;
			CustomerIdentityBean customerIdentityBean = null;
			ArrayList<CustomerIdentityBean> cidList=null;
			
			for(int i = startNumber; (i < endNumber) && (i < customerList.size()); i++) {
				customerBean = (CustomerBean) customerList.get(i);

				cidList = adminBO.getCustIdentityRecordList(null, "CUSTOMER_ID", customerBean.getCustomerID(), "", "");
				
				customerIdentityBean = (cidList==null || cidList.size()==0)?new CustomerIdentityBean() : cidList.get(0);
						
				rowColorClass = HTMLUtil.getRowColor(i, "RepDetailsCol1", "RepDetailsCol2");
				
				
				viewDetailsLinkParameters = new StringBuffer();
				viewDetailsLinkParameters.append(requestString);
				viewDetailsLinkParameters.append("&repAction=V");
				viewDetailsLinkParameters.append("&customerID="+customerBean.getCustomerID());
				encryptedViewDetailsLinkParameters = QueryCrypt.encrypt(request, viewDetailsLinkParameters.toString());

				pageConstructor.append("				<tr class=\"" + rowColorClass + "\">");
				pageConstructor.append("					<td class=\"stringStyleEng\">");
				pageConstructor.append("						<a class=\"linkStyle\" href=\"Customer?"+encryptedViewDetailsLinkParameters+"\" title=\"Click Here to view Customer details\" >");
				pageConstructor.append(StringUtil.nullToEmpty(customerBean.getCustomerID()));
				pageConstructor.append("						</a>");
				pageConstructor.append("					</td>");
				pageConstructor.append("					<td class=\"stringStyleEng\">");
				pageConstructor.append(StringUtil.nullToEmpty(customerIdentityBean.getFirstName()));
				pageConstructor.append("					</td>");
				pageConstructor.append("					<td class=\"stringStyleEng\">");
				pageConstructor.append(StringUtil.nullToEmpty(customerIdentityBean.getMiddleName()));
				pageConstructor.append("					</td>");
				pageConstructor.append("					<td class=\"stringStyleEng\">");
				pageConstructor.append(StringUtil.nullToEmpty(customerIdentityBean.getLastName()));
				pageConstructor.append("					</td>");
				pageConstructor.append("					<td class=\"numberStyleEng\">");
				pageConstructor.append(StringUtil.nullToEmpty(customerBean.getFirstSalesDate()));
				pageConstructor.append("					</td>");
				pageConstructor.append("					<td class=\"quantityStyleEng\">");
				pageConstructor.append(simpleDateFormat.format(customerIdentityBean.getBirthDate()));				
				pageConstructor.append("					</td>");
				pageConstructor.append("				</tr>");
			}
		}else {
			pageConstructor.append("				<tr>");
			pageConstructor.append("					<td class=\"linkStyleEng\" align=\"center\" colspan=\"6\">");
			pageConstructor.append("						No Data Found");
			pageConstructor.append("					</td>");
			pageConstructor.append("				</tr>");
		}


		pageConstructor.append("				</table>");
		pageConstructor.append("			</td>");
		pageConstructor.append("		</tr>");
		
		
		pageConstructor.append("		<tr>");
		pageConstructor.append("			<td>");
		pageConstructor.append("				<table id=\"navigationTable\">");
		pageConstructor.append("				<tr>");
		pageConstructor.append("					<td id=\"navigationTable\">");

		if (customerList.size() > numOfRecordToShow ) {
			StringBuffer staticNavigationParameterList = new StringBuffer();
			
			staticNavigationParameterList.append(requestString);
			staticNavigationParameterList.append("&direction=" + direction);
			staticNavigationParameterList.append("&sort=" + sort);

			pageConstructor.append(HTMLDesign.getNavigationBar(request, staticNavigationParameterList.toString(), 1, customerList.size(), 5, 5, this.getServletName(), "pageNumber",clientDirName, "Eng"));
		}

		pageConstructor.append("					</td>");
		pageConstructor.append("				</tr>");
		pageConstructor.append("				</table>");
		pageConstructor.append("			</td>");
		pageConstructor.append("		</tr>");
		
		pageConstructor.append("		</table>");
		pageConstructor.append("		</fieldset>");
		pageConstructor.append("	</td>");
		pageConstructor.append("</tr>");
		
		if(roleID.equals("1") || roleID.equals("2")){
			pageConstructor.append("<tr>");
			pageConstructor.append("	<td>");
			pageConstructor.append("		<div align=\"center\">");
			pageConstructor.append("			<table border=\"0\">");
			pageConstructor.append("			<tr>");
			pageConstructor.append("				<td width=\"37%\">");
			pageConstructor.append("					<br />");
			pageConstructor.append("					<div align=\"center\">");
//			pageConstructor.append("						<input type=\"button\" class=\"standardBoldButton\" size=\"30\" value=\"Add New Client\" onClick=\"window.location.href='Client?"+requestString+"'\">");
			pageConstructor.append("							<a href=\"#\" class=\"btn_large btn_blue btn_awesome\" rel=\"nofollow\" onClick=\"window.location.href='Customer?"+requestString+"'\">Create New Customer</a>");
			pageConstructor.append("					</div>");
			pageConstructor.append("				</td>");
			pageConstructor.append("			</tr>");
			pageConstructor.append("			</table>");
			pageConstructor.append("		</div>");
			pageConstructor.append("	</td>");
			pageConstructor.append("</tr>");
		}

		pageConstructor.append("</table>");



		this.setPageConstructor(pageConstructor);
	}

}
