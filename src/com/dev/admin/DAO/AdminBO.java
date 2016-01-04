package com.dev.admin.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.hdc.sysdev.utils.StringUtil;
import com.optica.bean.common.AddressTypeBean;
import com.optica.bean.common.BranchBean;
import com.optica.bean.common.CountryBean;
import com.optica.bean.common.PersonTitleBean;
import com.optica.bean.common.PhoneTypeBean;
import com.optica.bean.common.ProfessionBean;
import com.optica.bean.common.ProfessionalFieldBean;
import com.optica.bean.common.RatingBean;
import com.optica.bean.customer.CustomerAddressBean;
import com.optica.bean.customer.CustomerBean;
import com.optica.bean.customer.CustomerIdentityBean;
import com.optica.bean.customer.CustomerOldDBBean;
import com.optica.bean.customer.CustomerPhoneBean;
import com.optica.bean.customer.CustomerProfileBean;
import com.optica.bean.customer.CustomerTypeBean;
import com.optica.bean.customer.CustomerVisitBean;
import com.optica.bean.customer.CustomerWorkBean;
import com.optica.bean.customer.VisitTypeBean;
import com.optica.bean.user.ActivityLogBean;
import com.optica.bean.user.UserBean;
import com.optica.vo.common.AddressTypeVO;
import com.optica.vo.common.PersonTitleVO;
import com.optica.vo.common.PhoneTypeVO;
import com.optica.vo.common.ProfessionVO;
import com.optica.vo.common.ProfessionalFieldVO;
import com.optica.vo.common.RatingVO;
import com.optica.vo.customer.CustomerPhoneVO;
import com.optica.vo.customer.CustomerTypeVO;
import com.optica.vo.customer.CustomerVO;
import com.optica.vo.customer.CustomerVisitVO;
import com.optica.vo.customer.VisitTypeVO;
import com.optica.vo.user.ActivityLogVO;
import com.optica.vo.user.UserVO;


public class AdminBO {

	private static final Logger logger = Logger.getLogger(AdminBO.class);
	
	static String serverAddress;
	static String serviceName;
	static String userName;
	static String userPassword;
	static String oracleDriver;
	/**
	 * 
	 * 
	 * 
	 * BEGIN CONSTRUCTOR
	 * 
	 * @param svrAddress
	 * @param svcName
	 * @param user
	 * @param password
	 * @param oracleDriver
	 */
	public AdminBO(String svrAddress, String svcName, String user, String password, String oracleDriver){
		AdminBO.serverAddress = svrAddress;
		AdminBO.serviceName = svcName;
		AdminBO.userName = user;
		AdminBO.userPassword = password;
		AdminBO.oracleDriver = oracleDriver;
	}
	/**
	 * END CONSTRUCTOR
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN GET_CONNECTION
	 * CREATES A NEW JDBC CONNECTION TO THE DATABASE, WHETHER FROM DATASOURCE OR FROM DRIVER MANAGER
	 * 
	 * @param usingDataSource
	 * @return
	 */
	private static Connection getConnection(boolean usingDataSource) {

		Connection connection = null;

		try{
			if (usingDataSource) {

				Context initContext = new InitialContext();
				Context envContext = (Context)initContext.lookup("java:comp/env") ;
				DataSource ds = (DataSource)envContext.lookup("opticaDB");
				connection = ds.getConnection();

			} else {// using driver manager

				String DB_URL_THIN = (new StringBuilder()).append("jdbc:oracle:thin:").append(userName)
						.append("/").append(userPassword).append("@").append(serverAddress)
						.append(":1521:").append(serviceName).toString();

				Class.forName(oracleDriver);
				connection = DriverManager.getConnection(DB_URL_THIN, userName, userPassword);

			} 

		}catch (SQLException sqlExcetpion) {
			logger.error("SQLExcetpion", sqlExcetpion);
			//		} catch (NamingException e) {
			//			log.error("error",e);
		} catch (Exception exception) {
			logger.error("Exception", exception);
		}

		return connection;
	}
	/**
	 * END GET_CONNECTION
	 * 
	 *  
	 *  
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_CUSTOMER
	 * 
	 * @param customerVO
	 * @return
	 */
	public int addNewCustomer(CustomerVO customerVO){

		Connection custTBConnection = null;
		int customerID = 0;
		int inserted=0;


		try{
			custTBConnection = AdminBO.getConnection(true);
			custTBConnection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			customerID = AdminDAO.addNewCustomerRecord(custTBConnection, customerVO);
			/**
			 * 
			 * 
			 * 	
			 */
			if(customerID>0){
				if(customerVO.getCustomerAddressVO()!=null && customerVO.getCustomerAddressVO().getAddressID()>0){
					customerVO.getCustomerAddressVO().setCustomerID(customerID);
					inserted = AdminDAO.addNewCustomerAddressInfoRecord(custTBConnection, customerVO.getCustomerAddressVO());
					if(inserted<=0)
						customerID=0;
				}
			}
			if(customerID>0){
				if(customerVO.getCustomerIdentityVO()!=null && customerVO.getCustomerIdentityVO().getPersonTitleID()>0){
					customerVO.getCustomerIdentityVO().setCustomerID(customerID);
					inserted = AdminDAO.addNewCustomerIdentityRecord(custTBConnection, customerVO.getCustomerIdentityVO());
				}
				if(inserted<=0)
					customerID=0;
			}
			if(customerID>0){
				if(customerVO.getCustomerOldDBVO()!=null && customerVO.getCustomerOldDBVO().getFileNo()>0 ){
					customerVO.getCustomerOldDBVO().setCustomerID(customerID);
					inserted = AdminDAO.addNewCustomerOldDBInfoRecord(custTBConnection, customerVO.getCustomerOldDBVO());
				}
				if(inserted<=0)
					customerID=0;
			}
			if(customerID>0){
				if(customerVO.getCustomerProfileVO()!=null && customerVO.getCustomerProfileVO().getJobPosition().equals("")){
					customerVO.getCustomerProfileVO().setCustomerID(customerID);
					inserted = AdminDAO.addNewCustomerProfileInfoRecord(custTBConnection, customerVO.getCustomerProfileVO());
				}
				if(inserted<=0)
					customerID=0;
			}
			if(customerID>0){
				if(customerVO.getCustomerWorkVO()!=null && !StringUtil.nullToEmpty(customerVO.getCustomerWorkVO().getCompanyName()).equals("")){
					customerVO.getCustomerWorkVO().setCustomerID(customerID);
					inserted = AdminDAO.addNewCustomerWorkRecord(custTBConnection, customerVO.getCustomerWorkVO());
				}
			}
			if(customerID>0){
				ArrayList<CustomerVisitVO> customerVisitList = customerVO.getCustomerVisitList();
				CustomerVisitVO customerVisitVO=null;
				for(int i=0; customerVisitList!=null && i<customerVisitList.size(); i++){
					customerVisitVO = customerVisitList.get(i);
					customerVisitVO.setCustomerID(customerID);
					
					inserted = AdminDAO.addNewCustomerVisitRecord(custTBConnection, customerVisitVO);
					if(inserted>0){
						continue;
					} else{
						customerID=0;
						break;
					}
				}
			}
			if(customerID>0){
				ArrayList<CustomerPhoneVO> customerPhoneList = customerVO.getCustomerPhoneList();
				CustomerPhoneVO customerPhoneVO=null;
				for (int i=0; customerPhoneList!=null && i<customerPhoneList.size(); i++){
					customerPhoneVO = customerPhoneList.get(i);
					customerPhoneVO.setCustomerID(customerID);
					
					inserted = AdminDAO.addNewCustomerPhoneRecord(custTBConnection, customerPhoneVO);
					if(inserted>0){
						continue;
					} else{
						customerID=0;
						break;
					}
				}
			}
			
		} catch (SQLException sqlException) {
			customerID = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			customerID = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(customerID>0){	
					custTBConnection.commit();
				}else{
					custTBConnection.rollback();
				}

				if (custTBConnection != null)
					custTBConnection.close();

			} catch (Exception exception) {
				customerID = 0;
				logger.error("error",exception);
			}
		}
		
		return customerID;	
	}
	/**
	 * END ADD_NEW_CUSTOMER
	 * 
	 * 
	 * 
	 *   
	 * 
	 * 
	 * BEGIN UPDATE_CUSTOMER
	 * 
	 * @param customerVO
	 * @return
	 */
	public boolean updateCustomer(CustomerVO customerVO){

		Connection custTBConnection = null;
		int updated=0;


		try{
			custTBConnection = AdminBO.getConnection(true);
			custTBConnection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			updated = AdminDAO.updateCustomerRecord(custTBConnection, customerVO);
			/**
			 * 
			 * 
			 * 	
			 */
			if(updated>0){
				if(customerVO.getCustomerAddressVO()!=null && customerVO.getCustomerAddressVO().getAddressID()>0){
					updated = AdminDAO.updateCustomerAddressInfoRecord(custTBConnection, customerVO.getCustomerAddressVO());
				}
			}
			if(updated>0){
				if(customerVO.getCustomerIdentityVO()!=null && customerVO.getCustomerIdentityVO().getPersonTitleID()>0){
					updated = AdminDAO.updateCustomerPersonalInfoRecord(custTBConnection, customerVO.getCustomerIdentityVO());
				}
			}
			if(updated>0){
				if(customerVO.getCustomerOldDBVO()!=null && customerVO.getCustomerOldDBVO().getFileNo()>0 ){
					updated = AdminDAO.updateCustomerOldDBInfoRecord(custTBConnection, customerVO.getCustomerOldDBVO());
				}
			}
			if(updated>0){
				if(customerVO.getCustomerProfileVO()!=null && customerVO.getCustomerProfileVO().getJobPosition().equals("")){
					updated = AdminDAO.updateCustomerProfileInfoRecord(custTBConnection, customerVO.getCustomerProfileVO());
				}
			}
			if(updated>0){
				if(customerVO.getCustomerWorkVO()!=null && !StringUtil.nullToEmpty(customerVO.getCustomerWorkVO().getCompanyName()).equals("")){
					updated = AdminDAO.updateCustomerWorkRecord(custTBConnection, customerVO.getCustomerWorkVO());
				}
			}
			if(updated>0){
				ArrayList<CustomerVisitVO> customerVisitList = customerVO.getCustomerVisitList();
				CustomerVisitVO customerVisitVO=null;
				for(int i=0; customerVisitList!=null && i<customerVisitList.size(); i++){
					customerVisitVO = customerVisitList.get(i);
					
					updated = AdminDAO.updateCustomerVisitRecord(custTBConnection, customerVisitVO);
					if(updated>0){
						continue;
					} else{
						break;
					}
				}
			}
			if(updated>0){
				ArrayList<CustomerPhoneVO> customerPhoneList = customerVO.getCustomerPhoneList();
				CustomerPhoneVO customerPhoneVO=null;
				for (int i=0; customerPhoneList!=null && i<customerPhoneList.size(); i++){
					customerPhoneVO = customerPhoneList.get(i);
					
					updated = AdminDAO.addNewCustomerPhoneRecord(custTBConnection, customerPhoneVO);
					if(updated>0){
						continue;
					} else{
						break;
					}
				}
			}
			
		} catch (SQLException sqlException) {
			updated = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			updated = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(updated>0){	
					custTBConnection.commit();
				}else{
					custTBConnection.rollback();
				}

				if (custTBConnection != null)
					custTBConnection.close();

			} catch (Exception exception) {
				updated = 0;
				logger.error("error",exception);
			}
		}
		
		return updated>0?true:false;	
	}
	/**
	 * END UPDATE_CUSTOMER
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_CUSTOMER
	 * 
	 * @param customerVO
	 * @return
	 */
	public boolean removeCustomer(Integer customerID){

		Connection connection = null;
		int removed=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			removed = AdminDAO.removeCustomerRecord(connection, customerID);
			/**
			 * 
			 * 
			 * 	
			 */
			if(removed>0){
				/**
				 * remove customer address
				 */
				ArrayList<CustomerAddressBean> customerAddressList = AdminDAO.getCustAddressRecordListFiltered(connection, null, 
																	"CUSTOMER_ID", customerID.toString(), "", "");
				
				if(customerAddressList!=null && customerAddressList.size()>0){
					CustomerAddressBean customerAddressBean = customerAddressList.get(0);
					Integer customerAddressID = Integer.parseInt(customerAddressBean.getCustAddressID());
					
					removed = AdminDAO.removeCustomerAddressInfoRecord(connection, customerAddressID);
					
				}
			}
			if(removed>0){
				/**
				 * remove customer identity
				 */
				ArrayList<CustomerIdentityBean> customerIdentityList = AdminDAO.getCustIdentityRecordListFiltered(connection, null, 
																	"CUSTOMER_ID", customerID.toString(), "", "");
			
				if(customerIdentityList!=null && customerIdentityList.size()>0){
					CustomerIdentityBean customerIdentityBean = customerIdentityList.get(0);
					Integer customerIdentityID = Integer.parseInt(customerIdentityBean.getCustPersonalInfoID());
					
					removed = AdminDAO.removeCustomerPersonalInfoRecord(connection, customerIdentityID);
				}
			}
			if(removed>0){
				/**
				 * remove customerOLDDB
				 */
				ArrayList<CustomerOldDBBean> customerOldDBList = AdminDAO.getCustOldDBRecordListFiltered(connection, null, 
																	"CUSTOMER_ID", customerID.toString(), "", "");

				if(customerOldDBList!=null && customerOldDBList.size()>0){
					CustomerOldDBBean customerOldDBBean = customerOldDBList.get(0);
					Integer customerOldDBID = Integer.parseInt(customerOldDBBean.getCustOldDBID());
				
					removed = AdminDAO.removeCustomerOldDBInfoRecord(connection, customerOldDBID);
				}
			}
			if(removed>0){
				/**
				 * remove customer profile
				 */
				ArrayList<CustomerProfileBean> customerProfileList = AdminDAO.getCustProfileRecordListFiltered(connection, null, 
																	"CUSTOMER_ID", customerID.toString(), "", "");

				if(customerProfileList!=null && customerProfileList.size()>0){
					CustomerProfileBean customerProfileBean = customerProfileList.get(0);
					Integer customerProfileID = Integer.parseInt(customerProfileBean.getCustProfileID());
				
					removed = AdminDAO.removeCustomerProfileInfoRecord(connection, customerProfileID);
				}
			}
			if(removed>0){
				/**
				 * remove customerwork
				 */
				ArrayList<CustomerWorkBean> customerWorkList = AdminDAO.getCustWorkRecordListFiltered(connection, null, 
						"CUSTOMER_ID", customerID.toString(), "", "");

				if(customerWorkList!=null && customerWorkList.size()>0){
					CustomerWorkBean customerWorkBean = customerWorkList.get(0);
					Integer customerWorkID = Integer.parseInt(customerWorkBean.getCustWorkID());
				
					removed = AdminDAO.removeCustomerWorkRecord(connection, customerWorkID);
				}
			}
			if(removed>0){
				/**
				 * remove customer visits
				 */
				ArrayList<CustomerVisitBean> customerVisitList = AdminDAO.getCustVisitRecordListFiltered(connection, null, 
															"CUSTOMER_ID", customerID.toString(), "", "");
				
				CustomerVisitBean customerVisitBean = null; 
				Integer customerVisitID = 0;
				for(int i=0; customerVisitList!=null && customerVisitList.size()>0; i++){
					customerVisitBean = customerVisitList.get(i);
					customerVisitID = Integer.parseInt(customerVisitBean.getCustVisitID());
				
					removed = AdminDAO.removeCustomerVisitRecord(connection, customerVisitID);
					if(removed>0){
						continue;
					} else{
						break;
					}
				}
			}
			if(removed>0){
				/**
				 * remove customer phones
				 */
				ArrayList<CustomerPhoneBean> customerPhoneList = AdminDAO.getCustPhoneRecordListFiltered(connection, null, 
																"CUSTOMER_ID", customerID.toString(), "", "");

				CustomerPhoneBean customerPhoneBean = null; 
				Integer customerPhoneID = 0;
				for(int i=0; customerPhoneList!=null && customerPhoneList.size()>0; i++){
					customerPhoneBean = customerPhoneList.get(i);
					customerPhoneID = Integer.parseInt(customerPhoneBean.getCustPhoneID());
					
					removed = AdminDAO.removeCustomerPhoneRecord(connection, customerPhoneID);
					if(removed>0){
						continue;
					} else{
						break;
					}
				}
			}
			
		} catch (SQLException sqlException) {
			removed = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			removed = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(removed>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				removed = 0;
				logger.error("error",exception);
			}
		}
		
		return removed<=0?false:true;	
	}
	/**
	 * END REMOVE_CUSTOMER
	 * 
	 * 
	 * 
	 * 
	 *  
	 * 
	 * BEGIN
	 * RETRIEVES ACTIVITY LIST OF A CURRENT USER
	 * IT CAN BE FILTERED BY DATE
	 * AND CAN BE FILTERED BY BY ACTIVITY ID TO GET THE ACTIVITY DETAILS
	 * 
	 * @param userID
	 * @param activityID
	 * @param fromDate
	 * @param toDate
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<ActivityLogBean> getActivityLogging(Integer userID, Integer activityID, Timestamp fromDate, Timestamp toDate, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<ActivityLogBean> activityList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 
			 */
			activityList = AdminDAO.getActivityLogging(connection, userID, activityID, fromDate, toDate, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 
			 */

		} catch (Exception exception) {
			activityList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				activityList = null;
				logger.error("error",exception);
			}

		}
		return activityList;
	}
	/**
	 * END GET_ACTIVITY_LOGGING
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN LOG_ACTIVITY
	 * LOGS USER ACTIVITY BY ACTIVITY_TYPE
	 * CREATES A NEW RECORD IN ACTIVITY_LOG TABLE
	 * 
	 * @param activityTypeId
	 * @param userNumber
	 * @param activityDate
	 * @param activityDescription
	 * @param sessionId
	 * @param reference
	 * @return
	 */
	public int logActivity(ActivityLogVO activityLogVO) {
		Connection logTBConnection = null;
		int logID = 0;


		try{
			logTBConnection = AdminBO.getConnection(true);
			logTBConnection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 
			 */
			logID = AdminDAO.logActivity(logTBConnection, activityLogVO);
			/**
			 * 
			 * 
			 * 
			 */
		} catch (SQLException sqlException) {
			logID = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			logID = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(logID>0){	
					logTBConnection.commit();
				}else{
					logTBConnection.rollback();
				}

				if (logTBConnection != null)
					logTBConnection.close();

			} catch (Exception exception) {
				logID = 0;
				logger.error("error",exception);
			}
		}
		
		return logID;
	}
	/**
	 * END LOG_ACTIVITY
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_USER
	 * ADDS NEW USER TO THE SYSTEM
	 * 
	 * @param userVO
	 * @return
	 */
	public int addNewUser(UserVO userVO){

		Connection userTBConnection = null;
		int userID = 0;


		try{
			userTBConnection = AdminBO.getConnection(true);
			userTBConnection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			userID = AdminDAO.addNewUserRecord(userTBConnection, userVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			userID = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			userID = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(userID>0){	
					userTBConnection.commit();
				}else{
					userTBConnection.rollback();
				}

				if (userTBConnection != null)
					userTBConnection.close();

			} catch (Exception exception) {
				userID = 0;
				logger.error("error",exception);
			}
		}
		
		return userID;	
	}
	/**
	 * END ADD_NEW_USER
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN GET_ALL_USERS
	 * RETRIEVES ALL USERS FROM THE SYSTEM
	 * 
	 * @return
	 */
	public ArrayList<UserBean> getUserRecordList(Integer userID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<UserBean> userList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			userList = AdminDAO.getUserRecordListFiltered(connection, userID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			userList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				userList = null;
				logger.error("error",exception);
			}

		}
		return userList;	
	}
	/**
	 * END GET_ALL_USERS
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN LOGIN
	 * AUTHENTICATES CREDENTIALS OF A USER TRYING TO LOGIN TO THE SYSTEM
	 * 
	 * @param userName
	 * @param encPassword
	 * @return
	 */
	public boolean login(String userName, String encPassword){

		
		Connection connection = null;
		boolean authenticated = false;

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			authenticated = AdminDAO.login(connection, userName, encPassword);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			authenticated = false;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				authenticated = false;
				logger.error("error",exception);
			}

		}
		return authenticated;	
		
	}
	/**
	 * END LOGIN
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN UPDATE_USER_SESSION
	 * SETS USER NEW SESSION AND LAST ACCESS TIME OF A NEW LOGGED IN USER
	 * 
	 * @param userID
	 * @param sessionID
	 * @param loginTime
	 * @return
	 */
	public boolean updateUserSession(int userID, String sessionID, Timestamp loginTime){

		Connection userTBConnection = null;
		boolean sessionUpdated = false;


		try{
			userTBConnection = AdminBO.getConnection(true);
			userTBConnection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			sessionUpdated = AdminDAO.updateUserSession(userTBConnection, userID, sessionID, loginTime);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			sessionUpdated = false;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			sessionUpdated = false;
			logger.error("error",exception);
		} finally {
			try {			
				if(sessionUpdated){	
					userTBConnection.commit();
				}else{
					userTBConnection.rollback();
				}

				if (userTBConnection != null)
					userTBConnection.close();

			} catch (Exception exception) {
				sessionUpdated = false;
				logger.error("error",exception);
			}
		}
		
		return sessionUpdated;	
	}
	/**
	 * END UPDATE_USER_SESSION
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN LOGOUT
	 * LOGS OUT A USER BY REMOVING HIS CURRENT SESSION FROM DB 
	 * 
	 * @param userID
	 * @param sessionId
	 * @return
	 */
	public boolean logout(int userID, String sessionId) {

		Connection userTBConnection = null;
		boolean loggedOut = false;


		try{
			userTBConnection = AdminBO.getConnection(true);
			userTBConnection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */			
			loggedOut = AdminDAO.logout(userTBConnection, userID, sessionId);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			loggedOut = false;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			loggedOut = false;
			logger.error("error",exception);
		} finally {
			try {			
				if(loggedOut){	
					userTBConnection.commit();
				}else{
					userTBConnection.rollback();
				}

				if (userTBConnection != null)
					userTBConnection.close();

			} catch (Exception exception) {
				loggedOut = false;
				logger.error("error",exception);
			}
		}
		
		return loggedOut;	
		
	}
	/**
	 * END LOGOUT
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN GET_USER_DETAILS
	 * RETRIEVES USER DETAILS OF A GIVEN USER_ID OR USER_NAME
	 * 
	 * @param userID
	 * @param userName
	 * @return
	 */
	public UserBean getUserDetails(Integer userID, String userName){

		Connection connection = null;
		UserBean userBean = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			userBean = AdminDAO.getUserDetails(connection, userID, userName);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			userBean = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				userBean = null;
				logger.error("error",exception);
			}

		}
		return userBean;	
	}
	/**
	 * END GET_USER_DETAILS
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN GET_CUSTOMER_RECORD_LIST
	 * 
	 * @param customerID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<CustomerBean> getCustomerRecordListFiltered(Integer customerID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<CustomerBean> customerList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			customerList = AdminDAO.getCustomerRecordListFiltered(connection, customerID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			customerList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				customerList = null;
				logger.error("error",exception);
			}

		}
		return customerList;	
	}
	/**
	 * END GET_CUSTOMER_RECORD_LIST
	 * 
	 * 
	 * 
	 * 
	 *  
	 * 
	 * BEGIN GET_CUSTOMER_TYPE_RECORD_LIST
	 * 
	 * @param customerTypeID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<CustomerTypeBean> getCustTypeRecordListFiltered(Integer customerTypeID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<CustomerTypeBean> customerTypeList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			customerTypeList = AdminDAO.getCustTypeRecordListFiltered(connection, customerTypeID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			customerTypeList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				customerTypeList = null;
				logger.error("error",exception);
			}

		}
		return customerTypeList;	
	}
	/**
	 * END GET_CUSTOMER_TYPE_RECORD_LIST
	 * 
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN GET_CUSTOMER_ADDRESS_RECORD_LIST
	 * RETRIEVES ALL USERS FROM THE SYSTEM
	 * 
	 * @param customerAddressID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<CustomerAddressBean> getCustAddressRecordList(Integer customerAddressID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<CustomerAddressBean> customerAddressList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			customerAddressList = AdminDAO.getCustAddressRecordListFiltered(connection, customerAddressID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			customerAddressList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				customerAddressList = null;
				logger.error("error",exception);
			}

		}
		return customerAddressList;	
	}
	/**
	 * END GET_CUSTOMER_ADDRESS_RECORD_LIST
	 * 
	 * 
	 * 
	 * 
	 *   
	 * 
	 * BEGIN GET_CUST_IDENTITY_RECORD_LIST
	 * 
	 * @param customerIdentityID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<CustomerIdentityBean> getCustIdentityRecordList(Integer customerIdentityID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<CustomerIdentityBean> customerIdentityList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			customerIdentityList = AdminDAO.getCustIdentityRecordListFiltered(connection, customerIdentityID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			customerIdentityList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				customerIdentityList = null;
				logger.error("error",exception);
			}

		}
		return customerIdentityList;	
	}
	/**
	 * END GET_CUST_IDENTITY_RECORD_LIST
	 * 
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN GET_CUSTOMER_PHONE_RECORD_LIST
	 * 
	 * @param customerPhoneID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<CustomerPhoneBean> getCustPhoneRecordList(Integer customerPhoneID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<CustomerPhoneBean> customerPhoneList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			customerPhoneList = AdminDAO.getCustPhoneRecordListFiltered(connection, customerPhoneID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			customerPhoneList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				customerPhoneList = null;
				logger.error("error",exception);
			}

		}
		return customerPhoneList;	
	}
	/**
	 * END GET_CUSTOMER_PHONE_RECORD_LIST
	 * 
	 * 
	 * 
	 * 
	 *  
	 * 
	 * BEGIN GET_CUSTOMER_PROFILE_RECORD_LIST
	 * 
	 * @param customerProfileID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<CustomerProfileBean> getCustProfileRecordList(Integer customerProfileID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<CustomerProfileBean> customerProfileList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			customerProfileList = AdminDAO.getCustProfileRecordListFiltered(connection, customerProfileID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			customerProfileList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				customerProfileList = null;
				logger.error("error",exception);
			}

		}
		return customerProfileList;	
	}
	/**
	 * END GET_CUSTOMER_PROFILE_RECORD_LIST
	 * 
	 * 
	 * 
	 * 
	 *  
	 * 
	 * BEGIN GET_CUSTOMER_VISIT_RECORD_LIST
	 * 
	 * @param customerVisitID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<CustomerVisitBean> getCustVisitRecordList(Integer customerVisitID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<CustomerVisitBean> customerVisitList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			customerVisitList = AdminDAO.getCustVisitRecordListFiltered(connection, customerVisitID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			customerVisitList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				customerVisitList = null;
				logger.error("error",exception);
			}

		}
		return customerVisitList;	
	}
	/**
	 * END GET_CUSTOMER_VISIT_RECORD_LIST
	 * 
	 * 
	 * 
	 * 
	 *  
	 * 
	 * BEGIN GET_CUSTOMER_WORK_RECORD_LIST
	 * 
	 * @param customerWorkID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<CustomerWorkBean> getCustWorkRecordList(Integer customerWorkID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<CustomerWorkBean> customerWorkList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			customerWorkList = AdminDAO.getCustWorkRecordListFiltered(connection, customerWorkID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			customerWorkList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				customerWorkList = null;
				logger.error("error",exception);
			}

		}
		return customerWorkList;	
	}
	/**
	 * END GET_CUSTOMER_WORK_RECORD_LIST
	 * 
	 * 
	 * 
	 *
	 *  
	 * 
	 * BEGIN GET_PERSON_TITLE_RECORD_LIST
	 * 
	 * @param customerWorkID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<PersonTitleBean> getPersonTitleRecordListFiltered(Integer personTitleID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<PersonTitleBean> personTitleList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			personTitleList = AdminDAO.getPersonTitleRecordListFiltered(connection, personTitleID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			personTitleList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				personTitleList = null;
				logger.error("error",exception);
			}

		}
		return personTitleList;	
	}
	/**
	 * END GET_PERSON_TITLE_RECORD_LIST
	 * 
	 * 
	 * 
	 *
	 *  
	 * 
	 * BEGIN GET_RATING_RECORD_LIST_FILTERED
	 * 
	 * @param customerWorkID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<RatingBean> getRatingRecordListFiltered(Integer ratingID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<RatingBean> ratingList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			ratingList = AdminDAO.getRatingRecordListFiltered(connection, ratingID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			ratingList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				ratingList = null;
				logger.error("error",exception);
			}

		}
		return ratingList;	
	}
	/**
	 * END GET_RATING_RECORD_LIST_FILTERED
	 * 
	 * 
	 * 
	 *
	 *  
	 * 
	 * BEGIN GET_PROFESSIONAL_FIELD_RECORD_LIST_FILTERED
	 * 
	 * @param customerWorkID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<ProfessionalFieldBean> getProfessionalFieldRecordListFiltered(Integer professionalFieldID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<ProfessionalFieldBean> professionalFieldList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			professionalFieldList = AdminDAO.getProfessionalFieldRecordListFiltered(connection, professionalFieldID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			professionalFieldList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				professionalFieldList = null;
				logger.error("error",exception);
			}

		}
		return professionalFieldList;	
	}
	/**
	 * END GET_PROFESSIONAL_FIELD_RECORD_LIST_FILTERED
	 * 
	 * 
	 * 
	 *
	 *
	 * 
	 * BEGIN GET_PROFESSION_RECORD_LIST_FILTERED
	 * 
	 * @param customerWorkID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<ProfessionBean> getProfessionRecordListFiltered(Integer professionID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<ProfessionBean> professionList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			professionList = AdminDAO.getProfessionRecordListFiltered(connection, professionID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			professionList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				professionList = null;
				logger.error("error",exception);
			}

		}
		return professionList;	
	}
	/**
	 * END GET_PROFESSION_RECORD_LIST_FILTERED
	 * 
	 *
	 * 
	 *  *  
	 * 
	 * BEGIN GET_BRANCH_RECORD_LIST_FILTERED
	 * 
	 * @param branchID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<BranchBean> getBranchRecordListFiltered(Integer branchID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<BranchBean> branchList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			branchList = AdminDAO.getBranchRecordListFiltered(connection, branchID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			branchList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				branchList = null;
				logger.error("error",exception);
			}

		}
		return branchList;	
	}
	/**
	 * END GET_BRANCH_RECORD_LIST_FILTERED
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN GET_COUNTRY_RECORD_LIST_FILTERED
	 * 
	 * @param customerWorkID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<CountryBean> getCountryRecordListFiltered(Integer countryID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<CountryBean> countryList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			countryList = AdminDAO.getCountryRecordListFiltered(connection, countryID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			countryList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				countryList = null;
				logger.error("error",exception);
			}

		}
		return countryList;	
	}
	/**
	 * END GET_COUNTRY_RECORD_LIST_FILTERED
	 * 
	 * 
	 * 
	 *
	 *
	 * 
	 * BEGIN GET_ADDRESS_TYPE_RECORD_LIST_FILTERED
	 * 
	 * @param addressTypeID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<AddressTypeBean> getAddressTypeRecordListFiltered(Integer addressTypeID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<AddressTypeBean> addressTypeList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			addressTypeList = AdminDAO.getAddressTypeRecordListFiltered(connection, addressTypeID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			addressTypeList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				addressTypeList = null;
				logger.error("error",exception);
			}

		}
		return addressTypeList;	
	}
	/**
	 * END GET_ADDRESS_TYPE_RECORD_LIST_FILTERED
	 * 
	 * 
	 * 	 
	 * 
	 *  
	 * 
	 * BEGIN GET_PHONE_TYPE_RECORD_LIST_FILTERED
	 * 
	 * @param phoneTypeID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<PhoneTypeBean> getPhoneTypeRecordListFiltered(Integer phoneTypeID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<PhoneTypeBean> phoneTypeList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			phoneTypeList = AdminDAO.getPhoneTypeRecordListFiltered(connection, phoneTypeID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			phoneTypeList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				phoneTypeList = null;
				logger.error("error",exception);
			}

		}
		return phoneTypeList;	
	}
	/**
	 * END GET_PHONE_TYPE_RECORD_LIST_FILTERED
	 * 
	 * 
	 * 
	 * 	 
	 *  
	 * 
	 * BEGIN GET_VISIT_TYPE_RECORD_LIST_FILTERED
	 * 
	 * @param visitTypeID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	public ArrayList<VisitTypeBean> getVisitTypeRecordListFiltered(Integer visitTypeID, String criteriaType, String criteriaValue, String sort, String direction){

		Connection connection = null;
		ArrayList<VisitTypeBean> visitTypeList = null; 

		try{
			connection = AdminBO.getConnection(true);
			/**
			 * 
			 * 
			 * 	
			 */
			visitTypeList = AdminDAO.getVisitTypeRecordListFiltered(connection, visitTypeID, criteriaType, criteriaValue, sort, direction);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (Exception exception) {
			visitTypeList = null;
			logger.error("error",exception);
		} finally {
			try {			
				if (connection != null)
					connection.close();
			} catch (Exception exception) {
				visitTypeList = null;
				logger.error("error",exception);
			}

		}
		return visitTypeList;	
	}
	/**
	 * END GET_VISIT_TYPE_RECORD_LIST_FILTERED
	 * 
	 * 
	 *
	 * 
	 *
	 * 
	 * BEGIN UPDATE_CUSTOMER_TYPE
	 * 
	 * @param customerTypeVO
	 * @return
	 */
	public boolean updateCustomerType(CustomerTypeVO customerTypeVO){

		Connection custTBConnection = null;
		int updated=0;


		try{
			custTBConnection = AdminBO.getConnection(true);
			custTBConnection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			updated = AdminDAO.updateCustomerTypeRecord(custTBConnection, customerTypeVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			updated = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			updated = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(updated>0){	
					custTBConnection.commit();
				}else{
					custTBConnection.rollback();
				}

				if (custTBConnection != null)
					custTBConnection.close();

			} catch (Exception exception) {
				updated = 0;
				logger.error("error",exception);
			}
		}
		
		return updated>0?true:false;	
	}
	/**
	 * END UPDATE_CUSTOMER_TYPE
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_CUSTOMER_TYPE
	 * 
	 * @param customerTypeID
	 * @return
	 */
	public boolean removeCustomerType(Integer customerTypeID){

		Connection connection = null;
		int removed=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			removed = AdminDAO.removeCustomerTypeRecord(connection, customerTypeID);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			removed = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			removed = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(removed>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				removed = 0;
				logger.error("error",exception);
			}
		}
		
		return removed<=0?false:true;	
	}
	/**
	 * END REMOVE_CUSTOMER_TYPE
	 * 
	 * 
	 *
	 *
	 *
	 * 
	 * BEGIN UPDATE_RATING
	 * 
	 * @param ratingVO
	 * @return
	 */
	public boolean updateRating(RatingVO ratingVO){

		Connection connection = null;
		int updated=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			updated = AdminDAO.updateRatingRecord(connection, ratingVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			updated = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			updated = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(updated>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				updated = 0;
				logger.error("error",exception);
			}
		}
		
		return updated>0?true:false;	
	}
	/**
	 * END UPDATE_RATING
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_RATING
	 * 
	 * @param ratingID
	 * @return
	 */
	public boolean removeRating(Integer ratingID){

		Connection connection = null;
		int removed=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			removed = AdminDAO.removeRatingRecord(connection, ratingID);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			removed = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			removed = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(removed>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				removed = 0;
				logger.error("error",exception);
			}
		}
		
		return removed<=0?false:true;	
	}
	/**
	 * END REMOVE_RATING
	 * 
	 * 
	 *
	 *
	 *
	 * 
	 * BEGIN UPDATE_PROFESSIONAL_FIELD
	 * 
	 * @param professionalFieldVO
	 * @return
	 */
	public boolean updateProfessionalField(ProfessionalFieldVO professionalFieldVO){

		Connection connection = null;
		int updated=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			updated = AdminDAO.updateProfessionalFieldRecord(connection, professionalFieldVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			updated = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			updated = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(updated>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				updated = 0;
				logger.error("error",exception);
			}
		}
		
		return updated>0?true:false;	
	}
	/**
	 * END UPDATE_PROFESSIONAL_FIELD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_PROFESSIONAL_FIELD
	 * 
	 * @param professionalFieldID
	 * @return
	 */
	public boolean removeProfessionalField(Integer professionalFieldID){

		Connection connection = null;
		int removed=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			removed = AdminDAO.removeProfessionalFieldRecord(connection, professionalFieldID);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			removed = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			removed = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(removed>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				removed = 0;
				logger.error("error",exception);
			}
		}
		
		return removed<=0?false:true;	
	}
	/**
	 * END REMOVE_PROFESSIONAL_FIELD
	 * 
	 * 
	 *
	 *
	 *
	 * 
	 * BEGIN UPDATE_PROFESSION
	 * 
	 * @param professionVO
	 * @return
	 */
	public boolean updateProfession(ProfessionVO professionVO){

		Connection connection = null;
		int updated=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			updated = AdminDAO.updateProfessionRecord(connection, professionVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			updated = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			updated = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(updated>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				updated = 0;
				logger.error("error",exception);
			}
		}
		
		return updated>0?true:false;	
	}
	/**
	 * END UPDATE_PROFESSION
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_PROFESSION
	 * 
	 * @param professionID
	 * @return
	 */
	public boolean removeProfession(Integer professionID){

		Connection connection = null;
		int removed=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			removed = AdminDAO.removeProfessionRecord(connection, professionID);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			removed = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			removed = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(removed>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				removed = 0;
				logger.error("error",exception);
			}
		}
		
		return removed<=0?false:true;	
	}
	/**
	 * END REMOVE_PROFESSION
	 * 
	 * 
	 *
	 *
	 *
	 * 
	 * BEGIN UPDATE_ADDRESS_TYPE
	 * 
	 * @param addressTypeVO
	 * @return
	 */
	public boolean updateAddressType(AddressTypeVO addressTypeVO){

		Connection connection = null;
		int updated=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			updated = AdminDAO.updateAddressTypeRecord(connection, addressTypeVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			updated = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			updated = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(updated>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				updated = 0;
				logger.error("error",exception);
			}
		}
		
		return updated>0?true:false;	
	}
	/**
	 * END UPDATE_ADDRESS_TYPE
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_ADDRESS_TYPE
	 * 
	 * @param addressTypeID
	 * @return
	 */
	public boolean removeAddressType(Integer addressTypeID){

		Connection connection = null;
		int removed=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			removed = AdminDAO.removeAddressTypeRecord(connection, addressTypeID);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			removed = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			removed = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(removed>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				removed = 0;
				logger.error("error",exception);
			}
		}
		
		return removed<=0?false:true;	
	}
	/**
	 * END REMOVE_ADDRESS_TYPE
	 * 
	 * 
	 *
	 *
	 *
	 * 
	 * BEGIN UPDATE_PHONE_TYPE
	 * 
	 * @param phoneTypeVO
	 * @return
	 */
	public boolean updatePhoneType(PhoneTypeVO phoneTypeVO){

		Connection connection = null;
		int updated=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			updated = AdminDAO.updatePhoneTypeRecord(connection, phoneTypeVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			updated = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			updated = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(updated>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				updated = 0;
				logger.error("error",exception);
			}
		}
		
		return updated>0?true:false;	
	}
	/**
	 * END UPDATE_PHONE_TYPE
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_PHOE_TYPE
	 * 
	 * @param phoneTypeID
	 * @return
	 */
	public boolean removePhoneType(Integer phoneTypeID){

		Connection connection = null;
		int removed=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			removed = AdminDAO.removePhoneTypeRecord(connection, phoneTypeID);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			removed = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			removed = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(removed>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				removed = 0;
				logger.error("error",exception);
			}
		}
		
		return removed<=0?false:true;	
	}
	/**
	 * END REMOVE_PHONE_TYPE
	 * 
	 * 
	 *
	 *
	 *
	 * 
	 * BEGIN UPDATE_VISIT_TYPE
	 * 
	 * @param visitTypeVO
	 * @return
	 */
	public boolean updateVisitType(VisitTypeVO visitTypeVO){

		Connection connection = null;
		int updated=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			updated = AdminDAO.updateVisitTypeRecord(connection, visitTypeVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			updated = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			updated = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(updated>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				updated = 0;
				logger.error("error",exception);
			}
		}
		
		return updated>0?true:false;	
	}
	/**
	 * END UPDATE_VISIT_TYPE
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_VISIT_TYPE
	 * 
	 * @param visitTypeID
	 * @return
	 */
	public boolean removeVisitType(Integer visitTypeID){

		Connection connection = null;
		int removed=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			removed = AdminDAO.removeVisitTypeRecord(connection, visitTypeID);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			removed = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			removed = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(removed>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				removed = 0;
				logger.error("error",exception);
			}
		}
		
		return removed<=0?false:true;	
	}
	/**
	 * END REMOVE_VISIT_TYPE
	 * 
	 * 
	 *
	 *
	 *
	 * 
	 * BEGIN UPDATE_PERSON_TITLE
	 * 
	 * @param personTitleVO
	 * @return
	 */
	public boolean updatePersonTitle(PersonTitleVO personTitleVO){

		Connection connection = null;
		int updated=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			updated = AdminDAO.updatePersonTitleRecord(connection, personTitleVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			updated = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			updated = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(updated>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				updated = 0;
				logger.error("error",exception);
			}
		}
		
		return updated>0?true:false;	
	}
	/**
	 * END UPDATE_PERSON_TITLE
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_PERSON_TITLE
	 * 
	 * @param personTitleID
	 * @return
	 */
	public boolean removePersonTitle(Integer personTitleID){

		Connection connection = null;
		int removed=0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			removed = AdminDAO.removeCustomerTypeRecord(connection, personTitleID);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			removed = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			removed = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(removed>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				removed = 0;
				logger.error("error",exception);
			}
		}
		
		return removed<=0?false:true;	
	}
	/**
	 * END REMOVE_PERSON_TITLE
	 * 
	 * 
	 *
	 *
	 * 
	 * 
	 * BEGIN ADD_NEW_CUSTOMER_TYPE
	 * 
	 * @param customerTypeVO
	 * @return
	 */
	public int addNewCustomerType(CustomerTypeVO customerTypeVO){

		Connection connection = null;
		int customerTypeID = 0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			customerTypeID = AdminDAO.addNewCustomerTypeRecord(connection, customerTypeVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			customerTypeID = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			customerTypeID = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(customerTypeID>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				customerTypeID = 0;
				logger.error("error",exception);
			}
		}
		
		return customerTypeID;	
	}
	/**
	 * END ADD_NEW_CUSTOMER_TYPE
	 * 
	 * 
	 *
	 *
	 *
	 * 
	 * BEGIN ADD_NEW_PROFESSIONAL_FIELD
	 * 
	 * @param professionalFieldVO
	 * @return
	 */
	public int addNewProfessionalField(ProfessionalFieldVO professionalFieldVO){

		Connection connection = null;
		int proFieldID = 0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			proFieldID = AdminDAO.addNewProfessionalFieldRecord(connection, professionalFieldVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			proFieldID = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			proFieldID = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(proFieldID>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				proFieldID = 0;
				logger.error("error",exception);
			}
		}
		
		return proFieldID;	
	}
	/**
	 * END ADD_NEW_PROFESSIONAL_FIELD
	 * 
	 * 
	 *
	 *
	 * 
	 * 
	 * BEGIN ADD_NEW_PROFESSION
	 * 
	 * @param professionVO
	 * @return
	 */
	public int addNewProfession(ProfessionVO professionVO){

		Connection connection = null;
		int professionID = 0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			professionID = AdminDAO.addNewProfessionRecord(connection, professionVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			professionID = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			professionID = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(professionID>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				professionID = 0;
				logger.error("error",exception);
			}
		}
		
		return professionID;	
	}
	/**
	 * END ADD_NEW_PROFESSION
	 * 
	 * 
	 *
	 *
	 * 
	 * 
	 * BEGIN ADD_NEW_ADDRESS_TYPE
	 * 
	 * @param addressTypeVO
	 * @return
	 */
	public int addNewAddressType(AddressTypeVO addressTypeVO){

		Connection connection = null;
		int addressTypeID = 0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			addressTypeID = AdminDAO.addNewAddressTypeRecord(connection, addressTypeVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			addressTypeID = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			addressTypeID = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(addressTypeID>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				addressTypeID = 0;
				logger.error("error",exception);
			}
		}
		
		return addressTypeID;	
	}
	/**
	 * END ADD_NEW_ADDRESS_TYPE
	 * 
	 * 
	 *
	 *
	 * 
	 * 
	 * BEGIN ADD_NEW_PHONE_TYPE
	 * 
	 * @param phoneTypeVO
	 * @return
	 */
	public int addNewPhoneType(PhoneTypeVO phoneTypeVO){

		Connection connection = null;
		int phoneTypeID = 0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			phoneTypeID = AdminDAO.addNewPhoneTypeRecord(connection, phoneTypeVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			phoneTypeID = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			phoneTypeID = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(phoneTypeID>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				phoneTypeID = 0;
				logger.error("error",exception);
			}
		}
		
		return phoneTypeID;	
	}
	/**
	 * END ADD_NEW_PHONE_TYPE
	 * 
	 * 
	 *
	 *
	 * 
	 * 
	 * BEGIN ADD_NEW_VISIT_TYPE
	 * 
	 * @param customerTypeVO
	 * @return
	 */
	public int addNewVisitType(VisitTypeVO visitTypeVO){

		Connection connection = null;
		int visitTypeID = 0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			visitTypeID = AdminDAO.addNewVisitTypeRecord(connection, visitTypeVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			visitTypeID = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			visitTypeID = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(visitTypeID>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				visitTypeID = 0;
				logger.error("error",exception);
			}
		}
		
		return visitTypeID;	
	}
	/**
	 * END ADD_NEW_VISIT_TYPE
	 * 
	 * 
	 *
	 *
	 * 
	 * 
	 * BEGIN ADD_NEW_PERSON_TITLE
	 * 
	 * @param personTitleVO
	 * @return
	 */
	public int addNewPersonTitle(PersonTitleVO personTitleVO){

		Connection connection = null;
		int personTitleID = 0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			personTitleID = AdminDAO.addNewPersonTitleRecord(connection, personTitleVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			personTitleID = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			personTitleID = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(personTitleID>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				personTitleID = 0;
				logger.error("error",exception);
			}
		}
		
		return personTitleID;	
	}
	/**
	 * END ADD_NEW_PERSON_TITLE
	 * 
	 * 
	 *
	 *
	 * 
	 * 
	 * BEGIN ADD_NEW_RATING
	 * 
	 * @param ratingVO
	 * @return
	 */
	public int addNewRating(RatingVO ratingVO){

		Connection connection = null;
		int ratingID = 0;


		try{
			connection = AdminBO.getConnection(true);
			connection.setAutoCommit(false);
			/**
			 * 
			 * 
			 * 	
			 */
			ratingID = AdminDAO.addNewRatingRecord(connection, ratingVO);
			/**
			 * 
			 * 
			 * 	
			 */
		} catch (SQLException sqlException) {
			ratingID = 0;
			logger.error("error",sqlException);
		} catch (Exception exception) {
			ratingID = 0;
			logger.error("error",exception);
		} finally {
			try {			
				if(ratingID>0){	
					connection.commit();
				}else{
					connection.rollback();
				}

				if (connection != null)
					connection.close();

			} catch (Exception exception) {
				ratingID = 0;
				logger.error("error",exception);
			}
		}
		
		return ratingID;	
	}
	/**
	 * END ADD_NEW_RATING
	 * 
	 * 
	 */ 
	 
	
}
