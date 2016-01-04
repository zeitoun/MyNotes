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
import com.optica.bean.common.ProfessionalFieldBean;

public class ProfessionalFieldList extends PageSecureServlet {
	/**
	 * 
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProfessionalFieldList.class);
	private final Logger logger = Logger.getLogger(ProfessionalFieldList.class);
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
	 * Constructor of the object.
	 */
	public ProfessionalFieldList() {
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

		logger.info("\n\n Professional Field List...");

		HttpSession session = request.getSession(true);
		session.removeAttribute("professionalFieldBean");
		session.removeAttribute("professionalFieldID");

		try{
			if(session.getAttribute("userID")!= null ){

				this.setLanguage(session, request);
				this.setIncludeMenu(true);
				this.setBrowserEncoding();
				this.setDatabaseEncoding();

				StringBuffer scriptIncludeBuffer = new StringBuffer();
				scriptIncludeBuffer.append("	<script language=\"Javascript\" src=\"js/ProfessionalFieldList.js\"></script>");
				scriptIncludeBuffer.append("	<link rel=\"stylesheet\" type=\"text/css\"  href=\"clientassets/files_optica/css/font.css\">");
				setScriptIncludes(new String(scriptIncludeBuffer));
				scriptIncludeBuffer = null;

				StringBuffer jsInstanceBuffer = new StringBuffer();
				jsInstanceBuffer.append(" formIns = getFormUtil(\"reqForm\");");
				setJsInstance(new String(jsInstanceBuffer));
				jsInstanceBuffer = null;


				setOnLoadFunctions("document.getElementById('configSection').className += 'CURRENT';");


				RequestParameterObject rpo = QueryCrypt.decrypt(request);
				ArrayList<ProfessionalFieldBean> professionalFieldList = null;
				String actionType = StringUtil.nullToEmpty(request.getParameter("actionType"));



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
					} 
				}


				String criteriaTypeID = "";
				String criteriaType = "";
				String criteriaValue = "";
				if (actionType.equals("search") ) {

					criteriaTypeID = StringUtil.nullToEmpty(request.getParameter("filterCriteriaID"));
					criteriaValue = StringUtil.nullToEmpty(request.getParameter("filterBy"));

					logger.info("criteriaType: " + criteriaType + "\ncriteriaValue: " + criteriaValue);

					if(!criteriaTypeID.equals("")){

						if(criteriaTypeID.equals("1")) // code
							criteriaType = "PRO_FIELD_CODE";
						else if(criteriaTypeID.equals("2")) // title
							criteriaType = "PRO_FIELD_DESC";
					}

					professionalFieldList = adminBO.getProfessionalFieldRecordListFiltered(null, criteriaType, criteriaValue, sort, direction);


				}else {

					professionalFieldList = adminBO.getProfessionalFieldRecordListFiltered(null, null, null, sort, direction);

				}



				setTitle("ProfessionalFieldList");
				setValueBean("professionalFieldListBean");

				constructPage(request, session, response, professionalFieldList, rpo, sort, direction, actionType, criteriaTypeID, criteriaValue);
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
	 * Construct page.
	 * 
	 * @param request
	 * @param session
	 * @param response
	 * @param transactionTypeList
	 * @param rpo
	 * @param sort
	 * @param direction
	 * @param actionType
	 * @param criteriaTypeID
	 * @param criteriaValue
	 */
	public void constructPage(HttpServletRequest request, HttpSession session, HttpServletResponse response, ArrayList<ProfessionalFieldBean> professionalFieldList, 
			RequestParameterObject rpo, String sort, String direction, String actionType, String criteriaTypeID, String criteriaValue){

		StringBuffer pageConstructor = new StringBuffer();
		ProfessionalFieldBean professionalFieldBean = null;

		ResultMessageBean resultMessageBean = (ResultMessageBean) session.getAttribute("resultMessageBean");



		String requestString = "formLastAccess=" + session.getAttribute("loginTime");
		String encryptedString = QueryCrypt.encrypt(request, requestString);
		int numOfRecordToShow = Integer.parseInt(application.getAttribute("numOfRecordToShow").toString());
		int currentPageNum = Integer.parseInt(StringUtil.nullToEmpty(request.getParameter("actionType")).equals("search") ? "0" : StringUtil.nullOrEmptyToString(rpo.getParameter("pageNumber"), "0"));
		logger.info("\nprofessionalField: " + professionalFieldList.size());

		NavigationBean limits = NavigationSortingUtil.getNavigationLimits(currentPageNum, numOfRecordToShow, professionalFieldList.size());
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
		pageConstructor.append("	<td colspan=2>");

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


		pageConstructor.append("<tr>");
		pageConstructor.append("	<td colspan=2>");
		pageConstructor.append("  		<br />");
		pageConstructor.append("  	</td>");
		pageConstructor.append("</tr>");
		
		
		pageConstructor.append("<tr>");
		pageConstructor.append("	<td align=\"center\" width=\"20%\">");

		pageConstructor.append("		<table class=\"searchBarTable\">");
		pageConstructor.append("			<tr>");
		pageConstructor.append("				<td width=\"40%\">");
		pageConstructor.append("					<span class=\"FrmCompTitle\">");
		pageConstructor.append("						Criteria &nbsp;&nbsp;");
		pageConstructor.append("						<input autofocus=\"autofocus\" name=\"filterBy\" id=\"filterBy\" type=\"text\" label=\"Filter By\" maxlength=\"15\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"1\" "+(actionType.equals("search") ? "disabled" : "" )+" value=\""+criteriaValue+"\" /> ");
		pageConstructor.append("					</span>");
		pageConstructor.append("				</td>");
		pageConstructor.append("			</tr>");
		pageConstructor.append("			<tr>");
		pageConstructor.append("				<td width=\"40%\">");
		pageConstructor.append("					<span class=\"FrmCompTitle\">");
		pageConstructor.append("						Filter Type &nbsp;&nbsp;");
		pageConstructor.append("						<select name=\"filterCriteriaID\" id=\"filterCriteriaID\" label=\"Criteria\" tabindex=\"4\" "+(actionType.equals("search") ? "disabled" : "" )+" >");
//		pageConstructor.append("							<option title=\"\" value=\"0\"></option>");
		pageConstructor.append("							<option title=\"Search By ProfessionalField Code\" value=\"1\" "+(criteriaTypeID.equals("1") ? " selected" : "")+" >Code</option>");
		pageConstructor.append("							<option title=\"Search by ProfessionalField Description\" value=\"2\" "+(criteriaTypeID.equals("2") ? " selected" : "")+" >Title</option>");
		pageConstructor.append("						</select>");
		pageConstructor.append("					</span>");
		pageConstructor.append("				</td>");
		pageConstructor.append("			</tr>");
		pageConstructor.append("			<tr>");
		pageConstructor.append("				<td width=\"20%\" align=\"center\">");
		pageConstructor.append("					<input type=\"hidden\" name=\"actionType\" id=\"actionType\" value=\"N\">");
		//		pageConstructor.append("					<input type=\"submit\" id=\"search\" name=\"search\" class=\"standardButton\" size=\"30\" value=\"Search\" onclick=\"if(ValidateForm()){submitForm('document.reqForm','"+this.getServletName()+/*"?" + requestString + */"', 'search');}\" />");
		pageConstructor.append("					<a href=\"#\" class=\"btn_medium btn_orange btn_awesome\" rel=\"nofollow\" onclick=\"if(ValidateForm()){submitForm('document.reqForm','"+this.getServletName()+"?" + encryptedString + "', "+(actionType.equals("search")?"'reset'":"'search'")+");}\">"+(actionType.equals("search") ? "RESET" : "SEARCH")+"</a>");
		pageConstructor.append("				</td>");
		pageConstructor.append("			</tr>");
		pageConstructor.append("		</table>");

		pageConstructor.append("	</td>");
//		pageConstructor.append("</tr>");
//
//
//		pageConstructor.append("<tr>");
		pageConstructor.append("	<td align=\"center\" width=\"80%\">");
		pageConstructor.append("		<fieldset class=\"mainFSet\">");
		pageConstructor.append("			<legend class=\"mainFSet\" >Professional Field List</legend>");
		pageConstructor.append("			<table class=\"mainFSetTable\">");


		pageConstructor.append("			<tr>");
		pageConstructor.append("				<td>");
		pageConstructor.append("					<table class=\"mainDataTable\">");
		pageConstructor.append("					<tr class=\"RepHeaderCol\">");
		pageConstructor.append("						<td id=\"mainDataTable\" width=\"20%\" class=\"HeaderLink\">");
		pageConstructor.append("							<a class=\"HeaderLink\" "+HTMLDesign.getSortingLink(this.getServletName(), staticSortingParameterList, request, "2") +">");
		pageConstructor.append("								Code");
		pageConstructor.append("							</a>");
		pageConstructor.append(HTMLDesign.getSortingArrowIfApplicable("2", sort, direction, clientDirName));
		pageConstructor.append("						</td>");
		pageConstructor.append("						<td width=\"30%\" class=\"HeaderNoLink\">");
		pageConstructor.append("							<a class=\"HeaderLink\" "+HTMLDesign.getSortingLink(this.getServletName(), staticSortingParameterList, request, "3") +">");
		pageConstructor.append("								Title");
		pageConstructor.append("							</a>");
		pageConstructor.append(HTMLDesign.getSortingArrowIfApplicable("3", sort, direction, clientDirName));
		pageConstructor.append("						</td>");
		pageConstructor.append("					</tr>");


		if (professionalFieldList!=null && professionalFieldList.size() > 0) {
			String rowColorClass = "";
			professionalFieldBean = new ProfessionalFieldBean();
			StringBuffer viewDetailsLinkParameters = new StringBuffer();
			String encryptedViewDetailsLinkParameters = null;

			for(int i = startNumber; (i < endNumber) && (i < professionalFieldList.size()); i++) {
				professionalFieldBean = (ProfessionalFieldBean) professionalFieldList.get(i);

				rowColorClass = HTMLUtil.getRowColor(i, "RepDetailsCol1", "RepDetailsCol2");
				
				viewDetailsLinkParameters = new StringBuffer();
				viewDetailsLinkParameters.append(requestString);
				viewDetailsLinkParameters.append("&repAction=V");
				viewDetailsLinkParameters.append("&professionalFieldID="+professionalFieldBean.getProFieldID());
				encryptedViewDetailsLinkParameters = QueryCrypt.encrypt(request, viewDetailsLinkParameters.toString());

				pageConstructor.append("				<tr class=\"" + rowColorClass + "\">");
				pageConstructor.append("					<td class=\"stringStyleEng\">");
				pageConstructor.append("						<a class=\"linkStyle\" href=\"ProfessionalField?"+encryptedViewDetailsLinkParameters+"\" title=\"Click Here to view Professional Field details\" >");
				pageConstructor.append(StringUtil.nullToEmpty(professionalFieldBean.getProFieldCode()));
				pageConstructor.append("						</a>");
				pageConstructor.append("					</td>");
				pageConstructor.append("					<td class=\"stringStyleEng\">");
				pageConstructor.append(StringUtil.nullToEmpty(professionalFieldBean.getProFieldDesc()));
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
		if (professionalFieldList.size() > numOfRecordToShow ) {
			pageConstructor.append("		<tr>");
			pageConstructor.append("			<td >");
			pageConstructor.append("				<table id=\"navigationTable\">");
			pageConstructor.append("				<tr>");
			pageConstructor.append("					<td id=\"navigationTable\">");

			StringBuffer staticNavigationParameterList = new StringBuffer();
			staticNavigationParameterList.append(requestString);
			staticNavigationParameterList.append("&direction=" + direction);
			staticNavigationParameterList.append("&sort=" + sort);
			pageConstructor.append(HTMLDesign.getNavigationBar(request, staticNavigationParameterList.toString(), currentPageNum, professionalFieldList.size(), endNumber, maxNumPage, this.getServletName(), "pageNumber",clientDirName, "Eng"));

			pageConstructor.append("					</td>");
			pageConstructor.append("				</tr>");
			pageConstructor.append("				</table>");
			pageConstructor.append("			</td>");
			pageConstructor.append("		</tr>");
		}
		pageConstructor.append("		</table>");
		pageConstructor.append("	</td>");
		pageConstructor.append("</tr>");

		String userRole = StringUtil.nullToEmpty(session.getAttribute("userRole"));
		if(userRole.equals("1")){

			StringBuffer addNewLinkParameters = new StringBuffer();
			addNewLinkParameters.append(requestString);
			addNewLinkParameters.append("&repAction=N");
			String encryptedViewDetailsLinkParameters = QueryCrypt.encrypt(request, addNewLinkParameters.toString());

			pageConstructor.append("<tr>");
			pageConstructor.append("	<td colspan=2>");
			pageConstructor.append("		<div align=\"center\">");
			pageConstructor.append("			<table border=\"0\">");
			pageConstructor.append("			<tr>");
			pageConstructor.append("				<td width=\"37%\">");
			pageConstructor.append("					<br />");
			pageConstructor.append("					<div align=\"center\">");
			pageConstructor.append("					<a href=\"#\" class=\"btn_large btn_blue btn_awesome\" rel=\"nofollow\" onClick=\"this.disabled='disabled';createLoadingImg();window.location.href='ProfessionalField?"+encryptedViewDetailsLinkParameters+"'\">ADD NEW</a>");
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
