package com.dev.admin.common.design;

import javax.servlet.http.HttpServletRequest;

public class HTMLDesign {



	/**
	 * Gets the Navigation Bar
	 * 
	 * @param request
	 * @param sort                    is the value of the parameter "sort"    
	 * @param currentPageNumber       is the value of the parameter "pageNumber"
	 * @param listSize
	 * @param endNumber
	 * @param maxPageNumber
	 * @param servletName
	 * @param sortParameterName       is the name of the parameter "sort"
	 * @param pageNumberParameterName is the name of the parameter "pageNumber"
	 * @return
	 */
	public static String getNavigationBar(HttpServletRequest request, String staticNavigationParameterList, int currentPageNumber, int listSize, int endNumber, int maxPageNumber, String servletName, String pageNumberParameterName,String clientDirName,String language){

		StringBuffer navigationBarConstructor = new StringBuffer();
		String folder = ( language.equals("Arb")? "rtl" : "ltr");
		// Next Page Number
		int firstPageNumber = 0;	
		int previousPageNumber = 0;
		int nextPageNumber = 0;
		int lastPageNumber = 0;

		//										< ----- BEGIN Navigation Bar ----- >");
		navigationBarConstructor.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");

		navigationBarConstructor.append("	<tr height=\"22\">");

		navigationBarConstructor.append("		<td width=\"8\">");
		navigationBarConstructor.append("			<img src=\""+ clientDirName +"/assets/common/"+folder+"/corner-b-L1.gif\" border=\"0\">");
		navigationBarConstructor.append("		</td>");

		navigationBarConstructor.append("		<td background=\""+ clientDirName +"/assets/common/bg-b1.gif\">");

		navigationBarConstructor.append("			<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" >");

		navigationBarConstructor.append("				<tr>");
		navigationBarConstructor.append("					<td width=\"60\">");

		// first & previous buttons
		if( currentPageNumber > 0 ){
			StringBuffer firstParameterList = new StringBuffer();
			firstParameterList.append(staticNavigationParameterList);
			firstParameterList.append("&action=First");
			firstPageNumber = 0;
			firstParameterList.append("&"+pageNumberParameterName+"="+firstPageNumber);

			navigationBarConstructor.append("					<a href=\""+servletName+"?"+firstParameterList+"\">");
			navigationBarConstructor.append("						<img src=\""+ clientDirName +"/assets/common/"+folder+"/first-arrow.gif\" border=\"0\" />");
			navigationBarConstructor.append("					</a>&nbsp;&nbsp;&nbsp;");		


			StringBuffer previousParameterList = new StringBuffer();
			previousParameterList.append(staticNavigationParameterList);
			previousParameterList.append("&action=Previous");
			previousPageNumber = currentPageNumber - 1;
			previousParameterList.append("&"+pageNumberParameterName+"="+previousPageNumber);

			navigationBarConstructor.append("					<a href=\""+servletName+"?"+previousParameterList+"\">");
			navigationBarConstructor.append("						<img src=\""+ clientDirName +"/assets/common/"+folder+"/previous-arrow.gif\" border=\"0\" />");
			navigationBarConstructor.append("					</a>");
		}

		navigationBarConstructor.append("					</td>");

		navigationBarConstructor.append("					<td width=\"200\" align=\"center\">");
		//		navigationBarConstructor.append("						<span class=\"StandardTextNavigationBar\">"+  Translator.translate("page", language)+" " + new Integer(currentPageNumber + 1) + " "+ Translator.translate("of", language) +" " + maxPageNumber + "</span>");
		navigationBarConstructor.append("						<span class=\"StandardTextNavigationBar\">Page " + new Integer(currentPageNumber + 1) + " of " + maxPageNumber + "</span>");
		navigationBarConstructor.append("					</td>");

		navigationBarConstructor.append("					<td width=\"60\">");

		// next & last buttons
		if( ( (currentPageNumber) < maxPageNumber - 1) && (endNumber < listSize) ){
			StringBuffer nextParameterList = new StringBuffer();
			nextParameterList.append(staticNavigationParameterList);
			nextParameterList.append("&action=Next");
			nextPageNumber = currentPageNumber + 1;
			nextParameterList.append("&"+pageNumberParameterName+"="+nextPageNumber);


			navigationBarConstructor.append("					<a href=\""+servletName+"?"+nextParameterList+"\">");
			navigationBarConstructor.append("						<img src=\""+ clientDirName +"/assets/common/"+folder+"/next-arrow.gif\" border=\"0\" />");
			navigationBarConstructor.append("					</a>&nbsp;&nbsp;&nbsp;");


			StringBuffer lastParameterList = new StringBuffer();
			lastParameterList.append(staticNavigationParameterList);
			lastParameterList.append("&action=Last");
			lastPageNumber = maxPageNumber - 1;
			lastParameterList.append("&"+pageNumberParameterName+"="+lastPageNumber);

			navigationBarConstructor.append("					<a href=\""+servletName+"?"+lastParameterList+"\">");
			navigationBarConstructor.append("						<img src=\""+ clientDirName +"/assets/common/"+folder+"/last-arrow.gif\" border=\"0\" />");
			navigationBarConstructor.append("					</a>");
		}

		navigationBarConstructor.append("					</td>");
		navigationBarConstructor.append("				</tr>");
		navigationBarConstructor.append("			</table>");

		navigationBarConstructor.append("		</td>");

		navigationBarConstructor.append("		<td width=\"8\">");

		navigationBarConstructor.append("			<img src=\""+ clientDirName +"/assets/common/"+folder+"/corner-b-R1.gif\" border=\"0\">");
		navigationBarConstructor.append("		</td>");
		navigationBarConstructor.append("	</tr>");

		navigationBarConstructor.append("</table>");
		//										< ----- END Navigation Bar ----- >

		return navigationBarConstructor.toString();
	}



	/**
	 * Gets sorting link
	 * 
	 * @return  
	 */
	public static String getSortingLink(String servletName, StringBuffer staticSortingParameterList, HttpServletRequest request, String sortingOrder){

		StringBuffer sortingParameterList = new StringBuffer();
		sortingParameterList.append(staticSortingParameterList);
		sortingParameterList.append(sortingOrder);
		//		String sortingEncryptedString = QueryCrypt.encrypt(request, sortingParameterList.toString());

		return	"href=\""+ servletName +"?"+ sortingParameterList +"\" onMouseOver=\"style.cursor='pointer'\"";
	}

	public static String getSortingArrowIfApplicable(String columnNumber, String currentColumnNumber, String currentDirection, String clientDirName){
		if(columnNumber.equals(currentColumnNumber)){
			if(currentDirection.equals("asc")){
				return "<img src=\""+ clientDirName +"/assets/common/arrow_up_red.jpg\" height=\"10px\" width=\"10px\" border=\"0\">";
			} else if(currentDirection.equals("desc")){
				return "<img src=\""+ clientDirName +"/assets/common/arrow_down_red.jpg\" height=\"10px\" width=\"10px\" border=\"0\">";
			}
		}

		return "";

	}



	public static String getDateRangeCalendarIFrame(){

		StringBuffer dateRangeCalendarIFrame = new StringBuffer("");		
		dateRangeCalendarIFrame.append("<iframe width=132 height=142 name=\"gToday:contrast:agenda.js\" id=\"gToday:contrast:agenda.js\" src=\"common/calendar/DateRange/ipopeng.htm\" scrolling=\"no\" frameborder=\"0\" style=\"visibility:visible; z-index:999; position:absolute; top:-500px;\" >");
		dateRangeCalendarIFrame.append("</iframe>");
		return  dateRangeCalendarIFrame.toString();
	}


}



