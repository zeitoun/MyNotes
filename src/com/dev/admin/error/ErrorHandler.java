package com.dev.admin.error;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.optica.admin.common.base.PageServlet;


/**
 * The Class ErrorHandler.
 *
 * @author HaythamDouaihy
 */
public class ErrorHandler extends PageServlet {


	private static final long serialVersionUID = 1L;
	private static final Logger logger=Logger.getLogger(ErrorHandler.class);

	public synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		try{
			setContentType("text/html;charset=windows-1256");
			setLanguage(session,request);
			setTitle("error");
			setIncludeMenu(false);
			setIncludeLeftMenu(false);
//			this.setIncludeHorizontalUserInfo(false);
			constructPage(request, session, response);
			super.doPost(request, response);
		}catch (Exception e) {
			logger.error("ERROR", e); 
			response.sendRedirect("ErrorHandler");
			
		}finally{
			
		}
	}

	private void constructPage(HttpServletRequest request,HttpSession session, HttpServletResponse response) throws Exception{

		logger.info("ENTERING ERROR HANDLER");
		setIncludeLanguage(false);
//		String language = getLanguage();

		StringBuffer pageConstructor =  new StringBuffer();
		pageConstructor.append("	 <br/>");
		pageConstructor.append("	<form method=\"post\" name=\"form1\"  id=\"form1\">");

		pageConstructor.append("   			<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"60%\" align=\"center\">");
		pageConstructor.append("   				<tr>");
		pageConstructor.append("   					<td>");
		pageConstructor.append("   						<table cellspacing=\"0\" cellpadding=\"0\" border=\"1\" width=\"100%\" align=\"center\" class=\"requestTable\">");
		pageConstructor.append("   								<tr>");
		pageConstructor.append("   									<td class=\"reportHeader\">&nbsp;");
//		pageConstructor.append("   										"+Translator.translate("theServiceIsNotAvailable",language));
		pageConstructor.append("   									</td>");
		pageConstructor.append("   								</tr>");
		pageConstructor.append("   								<tr class=\"FrmCol1\">");
		pageConstructor.append("   									<td>");
		pageConstructor.append("   										<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">");
		pageConstructor.append("   											<tr>");
		pageConstructor.append("   												<td class=\"notbordered\">");
		pageConstructor.append("   													<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"width=\"100%\">");
		pageConstructor.append("   														<tr>");
		pageConstructor.append("   															<td align=\"center\" class=\"notbordered\">");
		pageConstructor.append("   																&nbsp;");
		pageConstructor.append("   																<h2 class=\"warning\">");
		pageConstructor.append("   																	Service is Currently Not Available");
		pageConstructor.append("   																</h2>");
		pageConstructor.append("   																&nbsp;");
		pageConstructor.append("   															</td>");
		pageConstructor.append("   														</tr>");
		pageConstructor.append("   													</table>");
		pageConstructor.append("   												</td>");
		pageConstructor.append("   											</tr>");
		pageConstructor.append("   										</table>");
		pageConstructor.append("   									</td>");
		pageConstructor.append("   								</tr>");
		pageConstructor.append("   							</table>");
		pageConstructor.append("   						<br>");
		pageConstructor.append("   					</td>");
		pageConstructor.append("   				</tr>");
		pageConstructor.append("   			</table>");

		pageConstructor.append("		</form>");
		setPageConstructor(pageConstructor);
	}
}
