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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dev.admin.common.listener.ContextListener;

/**
 * This class is the Base of all servlets. It contains common getters and setters
 * for all the pages in the system.
 *
 * @author Haythamdouaihy
 */
public class BaseServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	private String language;
	private String contentType;
	private String characterEncoding;
	private String browserEncoding;
	private String databaseEncoding;
	private String valueBean;
	
	private boolean accessible = true; 
	
	protected static ServletContext application = ContextListener.getApplication();
	protected static String clientDirName = "clientassets/"+(String)application.getAttribute("clientDirName");
	
	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException{
		getContextVariables();
		response.setContentType(getContentType());
		request.setCharacterEncoding(getCharacterEncoding());
		setBrowserEncoding();
		setDatabaseEncoding();
	}
	
	/**
	 * Read the variables set in the Servlet Context in WebExchangeServlet
	 * All servlets extending BaseServlet will have those variables set, and
	 * will be available to use using their corresponding getters...
	 * @param application
	 */
	public void getContextVariables(){
		if(getContentType() == null)
			this.setContentType(application.getAttribute("contentType").toString());
		this.setCharacterEncoding(application.getAttribute("characterEncoding").toString());
	}

	/**
	 * Sets the language chosen by the user or the default one if any language is selected
	 * 
	 * @param session
	 * @param request
	 * @param application
	 * @return the language
	 */
	public void setLanguage(HttpSession session,HttpServletRequest request){

		String defaultLanguage = (String)application.getAttribute("defaultLanguage");

		if(language != null && !language.equals("")){
			session.setAttribute("language",language);
		}else if(session.getAttribute("language") == null){
			session.setAttribute("language",defaultLanguage);
			language = defaultLanguage;
		}else{
			language = (String)session.getAttribute("language");
		}
	}

	/**
	 * @return the language
	 */
	public String getLanguage(){
		return this.language;
	}
	/**
	 * 
	 */
	public void setLanguage(String language){
		this.language = language;
	}
	
	/**
	 * @return the characterEncoding
	 */
	public String getCharacterEncoding(){
		return this.characterEncoding;
	}

	/**
	 * @param characterEncoding the characterEncoding to set 
	 */
	public void setCharacterEncoding(String characterEncoding){
		this.characterEncoding = characterEncoding;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType(){
		return this.contentType;
	}

	/**
	 * @param contentType the contentType to set 
	 */
	public void setContentType(String contentType){
		this.contentType = contentType;
	}
	
	/**
	 * @return the browserEncoding
	 */
	public String getBrowserEncoding() {
		return this.browserEncoding;
	}
	
	/**
	 * @param browserEncoding the browserEncoding to set
	 */
	public void setBrowserEncoding() {	
		this.browserEncoding = (String)application.getAttribute("browserEncoding");
	}
	
	/**
	 * @return the databaseEncoding
	 */
	public String getDatabaseEncoding() {
		return this.databaseEncoding;
	}
	
	/**
	 * @param databaseEncoding the databaseEncoding to set
	 */
	public void setDatabaseEncoding() {
		this.databaseEncoding = (String)application.getAttribute("databaseEncoding");
	}
	/**
	 * @return the valueBean
	 */
	public String getValueBean() {
		return valueBean;
	}
	/**
	 * @param valueBean the valueBean to set
	 */
	public void setValueBean(String valueBean) {
		this.valueBean = valueBean;
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException{
		this.doPost(request, response);
	}
	
	/**
	 * @return the accessible
	 */
	public boolean isAccessible() {
		return accessible;
	}
	/**
	 * @param accessible the accessible to set
	 */
	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}
 }
