package com.dev.admin.request;

import java.io.IOException;
import java.sql.Timestamp;
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
import com.optica.admin.common.base.PageSecureServlet;
import com.optica.admin.formbeans.ResultMessageBean;
import com.optica.bean.customer.VisitTypeBean;
import com.optica.constants.common.ActivityType;
import com.optica.vo.customer.VisitTypeVO;
import com.optica.vo.user.ActivityLogVO;

public class VisitType extends PageSecureServlet {
	/**
	 * 
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = Logger.getLogger(VisitType.class);
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
	public VisitType() {
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

				ArrayList<VisitTypeBean> visitTypeList = new ArrayList<VisitTypeBean>();
				VisitTypeBean visitTypeBean = new VisitTypeBean();
				String requestString = "formLastAccess=" + session.getAttribute("loginTime");
				String encryptedString = QueryCrypt.encrypt(request, requestString);

				setLanguage(session,request);
				setBrowserEncoding();
				setDatabaseEncoding();

				StringBuffer scriptIncludeBuffer = new StringBuffer();
				scriptIncludeBuffer.append("			<script language=\"Javascript\" src=\"js/VisitType.js\"></script> ");
				scriptIncludeBuffer.append("	<link rel=\"stylesheet\" type=\"text/css\"  href=\"clientassets/files_optica/css/font.css\">");
				setScriptIncludes(new String(scriptIncludeBuffer));

				StringBuffer jsInstanceBuffer = new StringBuffer();
				jsInstanceBuffer.append(" formIns = getFormUtil(\"reqForm\");");
				jsInstanceBuffer.append(" numberInstance = getNumberUtil();");
				setJsInstance(new String(jsInstanceBuffer));
				jsInstanceBuffer = null;


				if(session.getAttribute("visitTypeBean")!=null)
					visitTypeBean = (VisitTypeBean)session.getAttribute("visitTypeBean");
				else
					session.setAttribute("visitTypeBean", visitTypeBean);


				setOnLoadFunctions("document.getElementById('configSection').className += 'CURRENT';");


				String repAction = "";
				RequestParameterObject rpo = QueryCrypt.decrypt(request);

				String actionType = StringUtil.nullToEmpty(request.getParameter("actionType"));
				logger.info("actionType: " + actionType);

				if(actionType.equals(""))
					repAction = StringUtil.nullToEmpty(rpo.getParameter("repAction"));



				if(repAction.equals("V")){// external call for VIEW action
					Integer visitTypeID = Integer.parseInt(StringUtil.nullOrEmptyToString(rpo.getParameter("visitTypeID"), "0"));

					visitTypeList = adminBO.getVisitTypeRecordListFiltered(visitTypeID, "", "", "", "");
					visitTypeBean= visitTypeList.get(0);
					
					session.setAttribute("visitTypeID", visitTypeID);

				} else if(repAction.equals("N")){// external call for NEW action

				} else{// internal calls

					if (actionType.equals("new") ) {
						ResultMessageBean resultMessageBean = submitAction(request, session);

						session.setAttribute("resultMessageBean", resultMessageBean);						
						session.removeAttribute("visitTypeBean");
						session.removeAttribute("visitTypeID");

						response.sendRedirect("VisitTypeList?"+encryptedString);

					} else if(actionType.equals("archive")){

					} else if(actionType.equals("delete")){	

						ResultMessageBean resultMessageBean = deleteAction(session);

						session.setAttribute("resultMessageBean", resultMessageBean);						
						session.removeAttribute("visitTypeBean");
						session.removeAttribute("visitTypeID");

						response.sendRedirect("VisitTypeList?"+encryptedString);

					} else if(actionType.equals("clear")){
						visitTypeBean = submitActionReset(session);

					} else if(actionType.equals("edit") || actionType.equals("duplicate")){

						Integer visitTypeID = Integer.parseInt(StringUtil.nullOrEmptyToString(rpo.getParameter("visitTypeID"), "0"));

						visitTypeList = adminBO.getVisitTypeRecordListFiltered(visitTypeID, "", "", "", "");
						visitTypeBean= visitTypeList.get(0);
						
						session.setAttribute("visitTypeID", visitTypeID);

					}

				}
				setTitle("Visit Type");

				constructPage(request, visitTypeBean, requestString, repAction, actionType);

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
	 * Construct page.
	 *
	 * @param request the request
	 * @param session the session
	 * @param response the response
	 */
	private void constructPage(HttpServletRequest request, VisitTypeBean visitTypeBean, String requestString, String repAction, String actionType) throws Exception{

		StringBuffer pageConstructor = new StringBuffer();
		RequestParameterObject rpo = QueryCrypt.decrypt(request);
		String viewType = StringUtil.nullToEmpty(rpo.getParameter("viewType"));
		String userType = StringUtil.nullToString(request.getSession().getAttribute("userType"), "user");
//		String trxTypeStatus = StringUtil.nullToEmpty(visitTypeBean.getStatus());


		pageConstructor.append("<input type=\"hidden\" name=\"actionType\" id=\"actionType\" value=\"\">");
		pageConstructor.append("<input type=\"hidden\" name=\"rootActionType\" id=\"rootActionType\" value=\""+actionType+"\">");
//		pageConstructor.append("<input name=\"systemDate\" id=\"systemDate\" type=\"hidden\" onpaste=\"return false\" value=\""+ simpleDateFormat.format(new java.util.Date()) +"\" /> ");
		pageConstructor.append("<table class=\"mainTableWrapper\">");
		pageConstructor.append("<tr>");
		pageConstructor.append("	<td width=\"70%\" align=\"right\" valign=\"top\">");
		pageConstructor.append("		<fieldset class=\"mainFSet\">");
		pageConstructor.append("			<legend class=\"mainFSet\" >Visit Type</legend>");
		pageConstructor.append("			<table class=\"mainFSetTable\">");
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


		pageConstructor.append("	  		<tr>");
		pageConstructor.append("				<td valign=\"top\">");
		pageConstructor.append("		 			<table class=\"mainReqTable\">");
		pageConstructor.append("					<tr>");
		pageConstructor.append("    	       			<td colspan=\"5\" >");
		pageConstructor.append("							<fieldset class=\"subFSet\">");
		pageConstructor.append("								<legend>General</legend>");
		pageConstructor.append("			 					<table class=\"subFSetTable\">");
		pageConstructor.append("								<tr>");
		pageConstructor.append("                   					<td width=\"10%\">");
		pageConstructor.append("										<span class=\"FrmCompTitle\">");
		pageConstructor.append("											Code*");
		pageConstructor.append("                           				</span>");
		pageConstructor.append("                           			</td>");
		pageConstructor.append("                   					<td width=\"20%\">");
		if(repAction.equals("V") || actionType.equals("edit")){
			pageConstructor.append("										<span class=\"note\">");
			pageConstructor.append(StringUtil.nullToEmpty(visitTypeBean.getVisitTypeCode()).toUpperCase());
			pageConstructor.append("                          				</span>");	
			pageConstructor.append("										<input type=\"hidden\" name=\"visitTypeCode\" id=\"visitTypeCode\" value=\""+StringUtil.nullToEmpty(visitTypeBean.getVisitTypeCode())+"\">");
		} else{
			pageConstructor.append("										<input name=\"visitTypeCode\" id=\"visitTypeCode\" type=\"text\" required=\"required\" size=\"12\" maxlength=\"10\" size=\"4\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" onpaste=\"return false\" tabindex=\"1\" value=\""+StringUtil.nullToEmpty(visitTypeBean.getVisitTypeCode())+"\" autofocus=\"autofocus\" /> ");
		}
		pageConstructor.append("                           			</td>");
		pageConstructor.append("                   					<td width=\"10%\">");
		pageConstructor.append("										<span class=\"FrmCompTitle\">");
		pageConstructor.append("											Title*");
		pageConstructor.append("                           				</span>");
		pageConstructor.append("                           			</td>");
		pageConstructor.append("                   					<td width=\"40%\">");
		if(repAction.equals("V")){
			pageConstructor.append("										<span class=\"note\">");
			pageConstructor.append(StringUtil.nullToEmpty(visitTypeBean.getVisitTypeDesc()).toUpperCase());
			pageConstructor.append("                           				</span>");
		} else{
			pageConstructor.append("										<input name=\"visitTypeDesc\" id=\"visitTypeDesc\" type=\"text\" required maxlength=\"30\" size=\"30\" autocomplete=\"off\" onkeypress=\"return formIns.doLowSecurityFilter(event)\" tabindex=\"2\" value=\""+StringUtil.nullToEmpty(visitTypeBean.getVisitTypeDesc())+"\" /> ");
		}
		pageConstructor.append("                           			</td>");
		pageConstructor.append("								</tr>");
		pageConstructor.append("								<tr>");
		pageConstructor.append("                   					<td>");
		pageConstructor.append("										<span class=\"FrmCompTitle\">");
		pageConstructor.append("											Description");
		pageConstructor.append("   	    	               				</span>");
		pageConstructor.append("            	           			</td>");
		pageConstructor.append("                           			<td colspan=\"5\" align=\"left\">");
		if(repAction.equals("V")){
			pageConstructor.append("										<span class=\"note\">");
			pageConstructor.append(StringUtil.nullToEmpty(visitTypeBean.getNote()).toUpperCase());
			pageConstructor.append("                           				</span>");
		} else{
			pageConstructor.append("										<textarea wrap=\"hard\" name=\"note\" id=\"note\" rows=\"2\" cols=\"50\" required=\"true\" onkeypress=\"return ( formIns.doLowSecurityFilter(event));\" tabindex=\"3\">"+StringUtil.nullToEmpty(visitTypeBean.getNote())+"</textarea>");
		}
		pageConstructor.append("									</td>");
		pageConstructor.append("								</tr>");
		pageConstructor.append("								</table>");
		pageConstructor.append("							</fieldset >");
		pageConstructor.append("						</td>");
		pageConstructor.append("					</tr>");



		pageConstructor.append("					</table>");
		pageConstructor.append("					<hr />");
		pageConstructor.append("				</td>");
		pageConstructor.append("			</tr>");
		//Bottom Buttons Table
		pageConstructor.append("			<tr>");
		pageConstructor.append("				<td align=\"center\">");
		pageConstructor.append("					<table class=\"buttonsTableDown\" border=\"0\">");
		pageConstructor.append("					<tr>");
		pageConstructor.append("						<td width=\"50%\" align=\"left\">");
		pageConstructor.append( constructBackButton(request, requestString, userType, repAction, actionType, viewType) );
		pageConstructor.append("&nbsp;");
		pageConstructor.append( constructDeleteButton(request, requestString, userType, repAction, actionType, viewType) );
		pageConstructor.append("						</td>");
		pageConstructor.append("						<td width=\"50%\" align=\"right\">");
		pageConstructor.append( constructSubmitButton(request, requestString, userType, repAction, actionType, viewType) );
		pageConstructor.append("&nbsp;");
		pageConstructor.append( constructCancelButton(request, requestString, userType, repAction, actionType, viewType) );
		pageConstructor.append("						</td>");		
		pageConstructor.append("					</tr>");
		pageConstructor.append("					</table>");
		pageConstructor.append("				</td>");
		pageConstructor.append("			</tr>");
		pageConstructor.append("			</table>");
		pageConstructor.append("		</fieldset>");
		pageConstructor.append("	</td>");



		pageConstructor.append("	<td width=\"30%\"  valign=\"top\" align=\"center\">");
		pageConstructor.append("		<div style=\"height: 550px; overflow: auto;\">");
		pageConstructor.append("			<table class=\"rightLinksTable\">");
		pageConstructor.append("			<tr>");
		pageConstructor.append("				<td align=\"center\" width=\"75%\">");
		pageConstructor.append("					<fieldset class=\"subFSet\">");
		pageConstructor.append("						<legend class=\"subFSet\" >Quick Links</legend>");
		pageConstructor.append("						<table class=\"subFSetTable\">");
		pageConstructor.append("							<div style =\"float:left;\" valign=\"bottom\" style=\"font-weight:bold\">");

		pageConstructor.append("								<ul>");
/*		if(!repAction.equals("N")){
			StringBuffer parameterList = new StringBuffer();
			parameterList.append(requestString);
			parameterList.append("&visitTypeID="+transactionTypeBean.getTrxTypeID());
			String encryptedParameterList = QueryCrypt.encrypt(request, parameterList.toString());
			pageConstructor.append("									<li>");
			pageConstructor.append("										<a class=\"linkStyle\" onclick=\"popitup('PlanListView?"+encryptedParameterList+"')\" href=\"#\">Related Plans</a>");
			pageConstructor.append("									</li>");
		}
		if(!repAction.equals("N")){
			StringBuffer parameterList = new StringBuffer();
			parameterList.append(requestString);
			parameterList.append("&transactionTypeID="+transactionTypeBean.getTrxTypeID());
			String encryptedParameterList = QueryCrypt.encrypt(request, parameterList.toString());
			pageConstructor.append("									<li>");
			pageConstructor.append("										<a class=\"linkStyle\" onclick=\"popitup('RuleListView?"+encryptedParameterList+"')\" href=\"#\">Related Rules</a>");
			pageConstructor.append("									</li>");
		}*/
		pageConstructor.append("									<hr style=\"width: 100%; height:3px; background: red repeat scroll center; \" />");
		pageConstructor.append("									<li>");
		pageConstructor.append("										<a class=\"linkStyle\" href=\"#\" onclick=\"return confirm('Are you sure you want to leave this page without saving changes?')\">Print This Page</a>");
		pageConstructor.append("									</li>");
		pageConstructor.append("									<li>");
		pageConstructor.append("										<a class=\"linkStyle\" href=\"LogoutProcess\" onclick=\"return confirm('Are you sure you want to leave this page without saving changes?')\">Logout</a>");
		pageConstructor.append("									</li>");
		pageConstructor.append("								</ul>");
		pageConstructor.append("							</div>");
		pageConstructor.append("						</table>");
		pageConstructor.append("					</fieldset>");
		pageConstructor.append("				</td>");
		pageConstructor.append("				<td align=\"center\" width=\"25%\"></td>");
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


		setPageConstructor(pageConstructor);
	}
	/**
	 * 
	 * 
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	private ResultMessageBean submitAction(HttpServletRequest request, HttpSession session)
			throws Exception{

		ResultMessageBean resultMessageBean = new ResultMessageBean();
		boolean isUpdated = false;

		VisitTypeVO visitTypeVO = new VisitTypeVO();
		Integer userID = Integer.parseInt(StringUtil.nullToString(session.getAttribute("userID"), "0"));		


		visitTypeVO.setVisitTypeCode(StringUtil.nullToEmpty(request.getParameter("visitTypeCode")));
		visitTypeVO.setVisitTypeDesc(StringUtil.nullToEmpty(request.getParameter("visitTypeDesc")));
		visitTypeVO.setNote(StringUtil.nullToEmpty(request.getParameter("note")));
		/**
		 * 
		 * 
		 * 
		 * BEGIN SUBMITTING REQUEST
		 * CASE WHEN EDITING AN OLD RECORD THAT IS NOT YET ACTIVE
		 * STPES ARE TO EDIT THE SAME RECORD IN THE DATABASE WITOUT CREATING ANY NEW RECORD
		 */
		String rootActionType = StringUtil.nullToEmpty(request.getParameter("rootActionType"));
		if(rootActionType.equals("edit")){//edit without creating history record

			int visitTypeID = Integer.parseInt(StringUtil.nullOrEmptyToString(request.getSession().getAttribute("visitTypeID"), "0"));

			if(visitTypeID > 0){
				visitTypeVO.setVisitTypeID(visitTypeID);
				isUpdated = adminBO.updateVisitType(visitTypeVO);

				if(isUpdated){
					logger.info("user: " + userID + " has successfully updated the visitTypeID: " + visitTypeID);
					ActivityLogVO activityLogVO = new ActivityLogVO();
					activityLogVO.setActivityDate(new Timestamp(new java.util.Date().getTime()));
					activityLogVO.setActivityDesc("user Updated visitType successfully");
					activityLogVO.setActivityTypeID(ActivityType.EDITVISITTYPE_ACT.value());
					activityLogVO.setReference(new Integer(visitTypeID).toString());
					activityLogVO.setSessionID(request.getSession().getId());
					activityLogVO.setUserID(userID);					
					adminBO.logActivity(activityLogVO);

					resultMessageBean.setSucceeded(true);
					resultMessageBean.setResultDescription("Success. visitTypeID: " + visitTypeID + " has been successfully updated");
				}else{
					logger.info("user: " + userID + " failed to update the visitTypeID: " + visitTypeID + ". no changes affected the system");
					resultMessageBean.setSucceeded(false);
					resultMessageBean.setResultDescription("Error At server side! Failed to updated visitTypeID: " + visitTypeID + "");
				}

			}
			/**
			 * 
			 * 
			 * 
			 * CASE OF ADDING NEW RECORD
			 * CHECK IF THE VISITTYPE CODE TO BE ADDED IS UNIQUE IN THE SYSTEM	
			 */
		} else if(!rootActionType.equals("edit")){
			

			int visitTypeID = adminBO.addNewVisitType(visitTypeVO);	

			if(visitTypeID > 0){
				logger.info("user: " + userID + " has successfully added a new visitType to the system. the new visitTypeID is: " + visitTypeID);

				ActivityLogVO activityLogVO = new ActivityLogVO();
				activityLogVO.setActivityDate(new Timestamp(new java.util.Date().getTime()));
				activityLogVO.setActivityDesc("user Updated visitType successfully");
				activityLogVO.setActivityTypeID(ActivityType.ADDVISITTYPE_ACT.value());
				activityLogVO.setReference(new Integer(visitTypeID).toString());
				activityLogVO.setSessionID(request.getSession().getId());
				activityLogVO.setUserID(userID);					
				adminBO.logActivity(activityLogVO);
				

				resultMessageBean.setSucceeded(true);
				resultMessageBean.setResultDescription("Success. visitTypeID: " + visitTypeID + " has been successfully stored in the system");					

			}else{
				logger.info("user: " + userID + " failed to add a new visitType to the system. no changes affected the system");

				resultMessageBean.setSucceeded(false);
				resultMessageBean.setResultDescription("Error At server side! Failed to add visitType. please try later");
			}
			
		} 
		return resultMessageBean;
	}
	/**
	 * 
	 * 
	 * 
	 * @param session
	 * @return
	 * @throws Exception
	 */
	private ResultMessageBean deleteAction(HttpSession session)
			throws Exception{

		ResultMessageBean resultMessageBean = new ResultMessageBean();

		Integer visitTypeID = Integer.parseInt(StringUtil.nullOrEmptyToString(session.getAttribute("visitTypeID"), "0"));
		Integer userID = Integer.parseInt(StringUtil.nullOrEmptyToString(session.getAttribute("userID"), "1"));


		boolean deleted = adminBO.removeVisitType(visitTypeID);

		if(deleted){
			logger.info("user: " + userID + " has successfully Deleted the visitType: " + visitTypeID);

			ActivityLogVO activityLogVO = new ActivityLogVO();
			activityLogVO.setActivityDate(new Timestamp(new java.util.Date().getTime()));
			activityLogVO.setActivityDesc("user Deleted visitType successfully");
			activityLogVO.setActivityTypeID(ActivityType.DELETEVISITTYPE_ACT.value());
			activityLogVO.setReference(new Integer(visitTypeID).toString());
			activityLogVO.setSessionID(session.getId());
			activityLogVO.setUserID(userID);					
			adminBO.logActivity(activityLogVO);
			
			resultMessageBean.setSucceeded(true);
			resultMessageBean.setResultDescription("Success. visitTypeID: " + visitTypeID + " has been successfully Deleted");

		} else{
			logger.info("user: " + userID + ". Problem when Deleting visitType: " + visitTypeID);

			resultMessageBean.setSucceeded(false);
			resultMessageBean.setResultDescription("Problem Deleting visitTypeID: " + visitTypeID + ". Please Try Later");

		}
		return resultMessageBean;

	}
	/**
	 * 
	 * 
	 * 
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public VisitTypeBean submitActionReset(HttpSession session)
			throws Exception{

		session.removeAttribute("visitTypeBean");
		session.removeAttribute("visitTypeID");
		return new VisitTypeBean();
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
			buttonConstructor.append("			onClick=\"this.disabled='disabled';createLoadingImg();window.location.href='VisitTypeList?"+encryptedParameterList+"';\" >");
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
			buttonConstructor.append("			onClick=\"this.disabled='disabled';createLoadingImg();window.location.href='VisitTypeList?"+encryptedParameterList+"';\">");
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
