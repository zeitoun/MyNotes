package com.dev.admin.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.hdc.sysdev.utils.StringUtil;
import com.optica.bean.common.AddressBean;
import com.optica.bean.common.AddressTypeBean;
import com.optica.bean.common.BranchBean;
import com.optica.bean.common.CountryBean;
import com.optica.bean.common.PersonTitleBean;
import com.optica.bean.common.PhoneBean;
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
import com.optica.bean.user.ActivityTypeBean;
import com.optica.bean.user.DepartmentBean;
import com.optica.bean.user.RoleBean;
import com.optica.bean.user.UserBean;
import com.optica.constants.common.TableCounter;
import com.optica.vo.common.AddressTypeVO;
import com.optica.vo.common.AddressVO;
import com.optica.vo.common.BranchVO;
import com.optica.vo.common.CountryVO;
import com.optica.vo.common.CurrencyVO;
import com.optica.vo.common.PersonTitleVO;
import com.optica.vo.common.PhoneTypeVO;
import com.optica.vo.common.PhoneVO;
import com.optica.vo.common.ProfessionVO;
import com.optica.vo.common.ProfessionalFieldVO;
import com.optica.vo.common.RatingVO;
import com.optica.vo.customer.BillVO;
import com.optica.vo.customer.CustomerAddressVO;
import com.optica.vo.customer.CustomerIdentityVO;
import com.optica.vo.customer.CustomerOldDBVO;
import com.optica.vo.customer.CustomerPhoneVO;
import com.optica.vo.customer.CustomerProfileVO;
import com.optica.vo.customer.CustomerTypeVO;
import com.optica.vo.customer.CustomerVO;
import com.optica.vo.customer.CustomerVisitVO;
import com.optica.vo.customer.CustomerWorkVO;
import com.optica.vo.customer.JobVO;
import com.optica.vo.customer.OptometryFileVO;
import com.optica.vo.customer.OrderVO;
import com.optica.vo.customer.PrescriptionVO;
import com.optica.vo.customer.VisitTypeVO;
import com.optica.vo.stock.BarCodeVO;
import com.optica.vo.stock.BrandVO;
import com.optica.vo.stock.InvoiceVO;
import com.optica.vo.stock.ItemPackDetVO;
import com.optica.vo.stock.ItemPackVO;
import com.optica.vo.stock.ItemVO;
import com.optica.vo.stock.JournalStockVO;
import com.optica.vo.stock.ProductDetVO;
import com.optica.vo.stock.ProductTypeVO;
import com.optica.vo.stock.ProductVO;
import com.optica.vo.stock.QRCodeVO;
import com.optica.vo.stock.StockVO;
import com.optica.vo.stock.SupplierBrandVO;
import com.optica.vo.stock.SupplierVO;
import com.optica.vo.user.ActivityLogVO;
import com.optica.vo.user.ActivityTypeVO;
import com.optica.vo.user.DepartmentVO;
import com.optica.vo.user.OperationVO;
import com.optica.vo.user.RoleOperationVO;
import com.optica.vo.user.RoleVO;
import com.optica.vo.user.UserVO;

public class AdminDAO {
	/**
	 * 
	 * 
	 * 
	 */
	private static final Logger logger = Logger.getLogger(AdminDAO.class);
	String serverAddress;
	String serviceName;
	String userName;
	String userPassword;
	String oracleDriver;
	/**
	 * 
	 * 
	 * 
	 * @param svrAddress
	 * @param svcName
	 * @param user
	 * @param password
	 * @param oracleDriver
	 */
	public AdminDAO(String svrAddress, String svcName, String user, String password, String oracleDriver){
		this.serverAddress = svrAddress;
		this.serviceName = svcName;
		this.userName = user;
		this.userPassword = password;
		this.oracleDriver = oracleDriver;
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN GET_COUNTER
	 * 
	 * @param connection
	 * @param counterID
	 * @return
	 */
	protected synchronized static int getCounter(Connection connection, Integer counterID) {

		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;

		StringBuffer query = new StringBuffer();

		query.append("SELECT CTR_NO ");
		query.append("  FROM RECORD_CTR ");
		query.append(" WHERE 1=1");
		query.append(" AND	CTR_ID = ?");

		int counter = -1;
		StringBuffer query1 = new StringBuffer();
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(1, counterID);

			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				counter = resultSet.getInt("CTR_NO");
				if (resultSet.wasNull()) {
					counter = -2;
				} else {
					// increment the counter and update the table M_CTSCTR
					counter = counter + 1;
					query1.append("UPDATE RECORD_CTR ");
					query1.append("   SET CTR_NO = ? ");
					query1.append(" WHERE ");
					query1.append("  CTR_ID = ?");
					preparedStatement1 = connection.prepareStatement(new String(query1));
					preparedStatement1.setInt(1, counter);
					preparedStatement1.setInt(2, counterID);
					preparedStatement1.executeUpdate();
				}
			}
		} catch (SQLException sqlException) {
			logger.error("error ", sqlException);
		} finally {
			query = null;
			query1 = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (preparedStatement1 != null)
					preparedStatement1.close();
			} catch (Exception exception) {
				logger.error("error", exception);
			}
		}

		return counter;
	}
	/**
	 * END GET_COUNTER
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_ADDRESS_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewAddressRecord(Connection connection, AddressVO addressVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();



		query.append("INSERT INTO ADDRESS ( ");
		query.append("	ADDRESS_ID, ");
		query.append("	COUNTRY_ID, ");
		query.append("	CITY, ");
		query.append("	ZONE, ");
		query.append("	SECTOR, ");//5
		query.append("	POSITION_X, ");
		query.append("	POSITION_Y) ");
		query.append("VALUES(?,?,?,?,?,");
		query.append(" 		?,?)");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.ADDRESS_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, addressVO.getCountryID());
			preparedStatement.setString(counter++, addressVO.getCity());
			preparedStatement.setString(counter++, addressVO.getZone());
			preparedStatement.setString(counter++, addressVO.getSector());
			preparedStatement.setLong(counter++, addressVO.getGridPositionX());
			preparedStatement.setLong(counter++, addressVO.getGridPositionY());

			inserted = preparedStatement.executeUpdate();
			
			if(inserted==0)
				transactionID=0;			

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
	}
	/**
	 * END ADD_NEW_ADDRESS_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_ADDRESS_TYPE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewAddressTypeRecord(Connection connection, AddressTypeVO addressTypeVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO ADDRESS_TYPE ( ");
		query.append("	ADDRESS_TYPE_ID, ");
		query.append("	ADDRESS_TYPE_CODE, ");
		query.append("	ADDRESS_TYPE_DESC) "); 
		query.append("VALUES (?,?,?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.ADDRESS_TYPE_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, addressTypeVO.getAddrTypeCode());
			preparedStatement.setString(counter++, addressTypeVO.getAddrTypeDesc());
			
			inserted=preparedStatement.executeUpdate();
			
			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
	}
	/**
	 * END ADD_NEW_ADDRESS_TYPE_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_BILL_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewBillRecord(Connection connection, BillVO billVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;
		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO BILL ( ");
		query.append("	BILL_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	ORDER_ID, ");
		query.append("	CREATION_DATE, ");
		query.append("	CREATED_BY, ");
		query.append("	STATUS, ");
		query.append("	NOTE) ");
		query.append("VALUES (?,?,?,?,?, ");
		query.append("		?,?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.BILL_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, billVO.getCustomerID());
			preparedStatement.setInt(counter++, billVO.getOrderID());
			preparedStatement.setTimestamp(counter++, billVO.getCreationDate());
			preparedStatement.setString(counter++, billVO.getCreatedBy());
			preparedStatement.setString(counter++, billVO.getStatus());
			preparedStatement.setString(counter++, billVO.getNote());

			inserted = preparedStatement.executeUpdate();
			
			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
	}
	/**
	 * END ADD_NEW_BILL_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_BRANCH_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewBranchRecord(Connection connection, BranchVO branchVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO BRANCH ( ");
		query.append("	BRANCH_ID, ");
		query.append("	PHONE_ID, ");
		query.append("	BRANCH_CODE, ");
		query.append("	BRANCH_DESC, ");
		query.append("	ADDRESS_ID, ");
		query.append("	NOTE) ");
		query.append("VALUES (?,?,?,?,?, ");
		query.append("			?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.BRANCH_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, branchVO.getPhoneID());
			preparedStatement.setString(counter++, branchVO.getBranchCode());
			preparedStatement.setString(counter++, branchVO.getBranchDesc());
			preparedStatement.setInt(counter++, branchVO.getAddressID());
			preparedStatement.setString(counter++, branchVO.getNote());

			inserted = preparedStatement.executeUpdate();
			
			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
	}
	/**
	 * END ADD_NEW_BRANCH_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_COUNTRY_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewCountryRecord(Connection connection, CountryVO countryVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO COUNTRY ( ");
		query.append("	COUNTRY_ID, ");
		query.append("	COUNTRY_CODE, ");
		query.append("	COUNTRY_DESC) "); 
		query.append("VALUES (?,?,?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.COUNTRY_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, countryVO.getCountryCode());
			preparedStatement.setString(counter++, countryVO.getCountryDesc());

			inserted = preparedStatement.executeUpdate();
			
			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
	}
	/**
	 * END ADD_NEW_COUNTRY_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_CUSTOMER_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewCustomerRecord(Connection connection, CustomerVO customerVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO CUSTOMER ( ");
		query.append("	CUSTOMER_ID, ");
		query.append("	CUST_TYPE_ID, ");
		query.append("	BRANCH_ID, ");
		query.append("	OLD_KEY_NO, ");
		query.append("	NEW_KEY_NO, ");
		query.append("	PHOTO_PATH, ");
		query.append("	CREATION_DATE, ");
		query.append("	CREATED_BY, ");
		query.append("	STATUS, ");
		query.append("	FIRST_SALES_DATE, ");
		query.append("	FIRST_PRESC_DATE, ");
		query.append("	IS_INFOTEST_OR_RXONLY, ");
		query.append("	IS_DIRECT_SALES, ");
		query.append("	IS_SALES_CONTACTLENSES, ");
		query.append("	IS_SALES_HEARING, ");
		query.append("	TOBE_UPDATED_ID, ");
		query.append("	TOBE_UPDATED_PROFILE, ");
		query.append("	TOBE_DELETED, ");
		query.append("	TOBE_DOUBLEFILE_CHECK, ");
		query.append("	TOBE_ARCHVIED, ");
		query.append("	NOTE) ");
		query.append("VALUES (?,?,?,?,?, ");
		query.append("		?,?,?,?,?, ");
		query.append("		?,?,?,?,?, ");
		query.append("		?,?,?,?,?, ");
		query.append("		?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.CUSTOMER_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, customerVO.getCustTypeID());
			preparedStatement.setInt(counter++, customerVO.getBranchID());
			preparedStatement.setInt(counter++, customerVO.getOldKeyNo());
			preparedStatement.setInt(counter++, customerVO.getNewKeyNo());
			preparedStatement.setString(counter++, customerVO.getPhotoPath());
			preparedStatement.setTimestamp(counter++, new Timestamp(customerVO.getCreationDate().getTime()));
			preparedStatement.setInt(counter++, customerVO.getCreatedBy());
			preparedStatement.setString(counter++, customerVO.getStatus());
			preparedStatement.setTimestamp(counter++, new Timestamp(customerVO.getFirstSalesDate().getTime()));
			preparedStatement.setTimestamp(counter++, new Timestamp(customerVO.getFirstPrescDate().getTime()));
			preparedStatement.setBoolean(counter++, customerVO.isInfoTestOrRXOnly());
			preparedStatement.setBoolean(counter++, customerVO.isDirectSales());
			preparedStatement.setBoolean(counter++, customerVO.isSalesContactLenses());
			preparedStatement.setBoolean(counter++, customerVO.isSalesHearing());
			preparedStatement.setString(counter++, customerVO.getToBeUpdatedID());
			preparedStatement.setString(counter++, customerVO.getToBeUpdatedProfile());
			preparedStatement.setString(counter++, customerVO.getToBeDeleted());
			preparedStatement.setString(counter++, customerVO.getToBeDoubleFileCheck());
			preparedStatement.setString(counter++, customerVO.getToBeArchived());
			preparedStatement.setString(counter++, customerVO.getNote());
			

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;		
	}
	/**
	 * END ADD_NEW_CUSTOMER_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN UPDATE_CUSTOMER_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updateCustomerRecord(Connection connection, CustomerVO customerVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();


		query.append("UPDATE CUSTOMER set  ");
		query.append("	CUST_TYPE_ID=?, ");
		query.append("	BRANCH_ID=?, ");
		query.append("	OLD_KEY_NO=?, ");
		query.append("	NEW_KEY_NO=?, ");
		query.append("	PHOTO_PATH=?, ");
		query.append("	CREATION_DATE=?, ");
		query.append("	CREATED_BY=?, ");
		query.append("	STATUS=?, ");
		query.append("	FIRST_SALES_DATE=?, ");
		query.append("	FIRST_PRESC_DATE=?, ");
		query.append("	IS_INFOTEST_OR_RXONLY=?, ");
		query.append("	IS_DIRECT_SALES=?, ");
		query.append("	IS_SALES_CONTACTLENSES=?, ");
		query.append("	IS_SALES_HEARING=?, ");
		query.append("	TOBE_UPDATED_ID=?, ");
		query.append("	TOBE_UPDATED_PROFILE=?, ");
		query.append("	TOBE_DELETED=?, ");
		query.append("	TOBE_DOUBLEFILE_CHECK=?, ");
		query.append("	TOBE_ARCHVIED=?, ");
		query.append("	NOTE=? ");
		query.append("WHERE 1=1 ");
		query.append("	AND CUSTOMER_ID=?, ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			
			preparedStatement.setInt(counter++, customerVO.getCustTypeID());
			preparedStatement.setInt(counter++, customerVO.getBranchID());
			preparedStatement.setInt(counter++, customerVO.getOldKeyNo());
			preparedStatement.setInt(counter++, customerVO.getNewKeyNo());
			preparedStatement.setString(counter++, customerVO.getPhotoPath());
			preparedStatement.setTimestamp(counter++, new Timestamp(customerVO.getCreationDate().getTime()));
			preparedStatement.setInt(counter++, customerVO.getCreatedBy());
			preparedStatement.setString(counter++, customerVO.getStatus());
			preparedStatement.setTimestamp(counter++, new Timestamp(customerVO.getFirstSalesDate().getTime()));
			preparedStatement.setTimestamp(counter++, new Timestamp(customerVO.getFirstPrescDate().getTime()));
			preparedStatement.setBoolean(counter++, customerVO.isInfoTestOrRXOnly());
			preparedStatement.setBoolean(counter++, customerVO.isDirectSales());
			preparedStatement.setBoolean(counter++, customerVO.isSalesContactLenses());
			preparedStatement.setBoolean(counter++, customerVO.isSalesHearing());
			preparedStatement.setString(counter++, customerVO.getToBeUpdatedID());
			preparedStatement.setString(counter++, customerVO.getToBeUpdatedProfile());
			preparedStatement.setString(counter++, customerVO.getToBeDeleted());
			preparedStatement.setString(counter++, customerVO.getToBeDoubleFileCheck());
			preparedStatement.setString(counter++, customerVO.getToBeArchived());
			preparedStatement.setString(counter++, customerVO.getNote());
			preparedStatement.setInt(counter++, customerVO.getCustomerID());
			

			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;	
	}
	/**
	 * END UPDATE_CUSTOMER_RECORD
	 * 
	 * 
	 *
	 *
	 *
	 *
	 * BEGIN REMOVE_CUSTOMER_RECORD
	 * 
	 * @param connection
	 * @param customerID
	 * @return
	 */
	protected static int removeCustomerRecord(Connection connection, Integer customerID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();


		query.append("DELETE FROM CUSTOMER ");
		query.append("WHERE CUSTOMER_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, customerID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;	
	}
	/** 
	 * END REMOVE_CUSTOMER_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_CUSTOMER_TYPE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewCustomerTypeRecord(Connection connection, CustomerTypeVO customerTypeVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO CUSTOMER_TYPE ( ");
		query.append("	CUST_TYPE_ID, ");
		query.append("	CUST_TYPE_CODE, ");
		query.append("	CUST_TYPE_DESC) ");
		query.append("VALUES (?,?,?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.CUSTOMER_TYPE_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, customerTypeVO.getCustTypeCode());
			preparedStatement.setString(counter++, customerTypeVO.getCustTypeDesc());

			inserted = preparedStatement.executeUpdate();

		
			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_CUSTOMER_TYPE_RECORD
	 * 
	 * 
	 *
	 *
	 * 
	 * 
	 * BEGIN UPDATE_CUSTOMER_TYPE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updateCustomerTypeRecord(Connection connection, CustomerTypeVO customerTypeVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();


		query.append("UPDATE CUSTOMER_TYPE SET ");		
		query.append("	CUST_TYPE_CODE=?, ");
		query.append("	CUST_TYPE_DESC=? ");
		query.append("WHERE 1=1 ");
		query.append("	AND CUST_TYPE_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			
			preparedStatement.setString(counter++, customerTypeVO.getCustTypeCode());
			preparedStatement.setString(counter++, customerTypeVO.getCustTypeDesc());
			
			preparedStatement.setInt(counter++, customerTypeVO.getCustTypeID());

			
			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;	
	}
	/**
	 * END UPDATE_CUSTOMER_TYPE_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_CUSTOMER_TYPE_RECORD
	 * 
	 * @param connection
	 * @param customerTypeID
	 * @return
	 */
	protected static int removeCustomerTypeRecord(Connection connection, Integer customerTypeID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();


		query.append("DELETE FROM CUSTOMER_TYPE ");
		query.append("WHERE CUST_TYPE_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, customerTypeID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;	
	}
	/** 
	 * END REMOVE_CUSTOMER_TYPE_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_CUSTOMER_ADDRESS_INFO_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewCustomerAddressInfoRecord(Connection connection, CustomerAddressVO customerAddressVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();



		query.append("INSERT INTO CUST_ADDRESS_INFO ( ");
		query.append("	CUST_ADDRESS_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	ADDRESS_ID, ");
		query.append("	ADDRESS_TYPE_ID, ");
		query.append("	PROXIMITY, ");
		query.append("	NOTE) "); 
		query.append("VALUES (?,?,?,?,?, ");
		query.append("		?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.CUST_ADDRESS_INFO_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, customerAddressVO.getCustomerID());
			preparedStatement.setInt(counter++, customerAddressVO.getAddressID());
			preparedStatement.setInt(counter++, customerAddressVO.getAddressTypeID());
			preparedStatement.setString(counter++, customerAddressVO.getProximity());
			preparedStatement.setString(counter++, customerAddressVO.getNote());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
			
	}
	/**
	 * END ADD_NEW_CUSTOMER_ADDRESS_INFO_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN UPDATE_CUSTOMER_ADDRESS_INFO_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updateCustomerAddressInfoRecord(Connection connection, CustomerAddressVO customerAddressVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();


		query.append("UPDATE CUST_ADDRESS_INFO SET ");
		query.append("	CUSTOMER_ID=?, ");
		query.append("	ADDRESS_ID=?, ");
		query.append("	ADDRESS_TYPE_ID=?, ");
		query.append("	PROXIMITY=?, ");
		query.append("	NOTE=? "); 
		query.append("WHERE 1=1 ");
		query.append("	AND CUST_ADDRESS_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));

			
			preparedStatement.setInt(counter++, customerAddressVO.getCustomerID());
			preparedStatement.setInt(counter++, customerAddressVO.getAddressID());
			preparedStatement.setInt(counter++, customerAddressVO.getAddressTypeID());
			preparedStatement.setString(counter++, customerAddressVO.getProximity());
			preparedStatement.setString(counter++, customerAddressVO.getNote());
			
			preparedStatement.setInt(counter++, customerAddressVO.getCustAddressID());
			

			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;	
	}
	/**
	 * END UPDATE_CUSTOMER_ADDRESS_INFO_RECORD
	 * 
	 * 
	 *
	 *	 
	 *
	 * 
	 * BEGIN REMOVE_CUSTOMER_ADDRESS_INFO_RECORD
	 * 
	 * @param connection
	 * @param customerAddressID
	 * @return
	 */
	protected static int removeCustomerAddressInfoRecord(Connection connection, Integer customerAddressID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();


		query.append("DELETE FROM CUST_ADDRESS_INFO ");
		query.append("WHERE CUST_ADDRESS_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, customerAddressID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;	
	}
	/** 
	 * END REMOVE_CUSTOMER_ADDRESS_INFO_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_CUSTOMER_OLDDB_INFO_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewCustomerOldDBInfoRecord(Connection connection, CustomerOldDBVO customerOldDBVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO CUST_OLDDB_INFO ( ");
		query.append("	CUST_OLDDB_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	FIRST_MID_LAST, ");
		query.append("	CENTER, ");
		query.append("	FILE_CREATION_DATE, ");
		query.append("	FIRST_DATE, ");
		query.append("	FILE_NO, ");
		query.append("	IS_IMPORTED_OPTICAL, ");
		query.append("	IS_IMPORTED_CONTACTLENSE, ");
		query.append("	MOBILE_NO, ");
		query.append("	OLD_CONTACTLENSE) "); 
		query.append("VALUES (?,?,?,?,?, ");
		query.append("		?,?,?,?,?, ");
		query.append("		?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.CUST_OLDDB_INFO_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, customerOldDBVO.getCustomerID());
			preparedStatement.setString(counter++, customerOldDBVO.getFirstMidLast());
			preparedStatement.setString(counter++, customerOldDBVO.getCenter());
			preparedStatement.setTimestamp(counter++, customerOldDBVO.getFileCreationDate());
			preparedStatement.setTimestamp(counter++, customerOldDBVO.getFirstDate());
			preparedStatement.setInt(counter++, customerOldDBVO.getFileNo());
			preparedStatement.setBoolean(counter++, customerOldDBVO.isImportedOptical());
			preparedStatement.setBoolean(counter++, customerOldDBVO.isImportedContactLense());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
			
	}
	/**
	 * END ADD_NEW_CUSTOMER_OLDDB_INFO_RECORD
	 * 
	 * 
	 *
	 *
	 *
	 * 
	 * BEGIN UPDATE_CUSTOMER_OLDDB_INFO_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updateCustomerOldDBInfoRecord(Connection connection, CustomerOldDBVO customerOldDBVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();


		query.append("UPDATE CUST_OLDDB_INFO SET ");
		query.append("	CUSTOMER_ID=?, ");
		query.append("	FIRST_MID_LAST=?, ");
		query.append("	CENTER=?, ");
		query.append("	FILE_CREATION_DATE=?, ");
		query.append("	FIRST_DATE=?, ");
		query.append("	FILE_NO=?, ");
		query.append("	IS_IMPORTED_OPTICAL=?, ");
		query.append("	IS_IMPORTED_CONTACTLENSE=?, ");
		query.append("	MOBILE_NO=?, ");
		query.append("	OLD_CONTACTLENSE=? "); 
		query.append("WHERE 1=1 ");
		query.append("	AND CUST_OLDDB_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, customerOldDBVO.getCustomerID());
			preparedStatement.setString(counter++, customerOldDBVO.getFirstMidLast());
			preparedStatement.setString(counter++, customerOldDBVO.getCenter());
			preparedStatement.setTimestamp(counter++, customerOldDBVO.getFileCreationDate());
			preparedStatement.setTimestamp(counter++, customerOldDBVO.getFirstDate());
			preparedStatement.setInt(counter++, customerOldDBVO.getFileNo());
			preparedStatement.setBoolean(counter++, customerOldDBVO.isImportedOptical());
			preparedStatement.setBoolean(counter++, customerOldDBVO.isImportedContactLense());

			preparedStatement.setInt(counter++, customerOldDBVO.getCustOldDBID());
			
			
			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;	
	}
	/**
	 * END UPDATE_CUSTOMER_OLDDB_INFO_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_CUSTOMER_OLDDB_INFO_RECORD
	 * 
	 * @param connection
	 * @param customerOldDBID
	 * @return
	 */
	protected static int removeCustomerOldDBInfoRecord(Connection connection, Integer customerOldDBID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();


		query.append("DELETE FROM CUST_OLDDB_INFO ");
		query.append("WHERE CUST_OLDDB_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, customerOldDBID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;	
	}
	/** 
	 * END REMOVE_CUSTOMER_OLDDB_INFO_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_CUSTOMER_IDENTITY_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewCustomerIdentityRecord(Connection connection, CustomerIdentityVO customerIdentityVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO CUST_PERSONAL_INFO ( ");
		query.append("	CUST_PERSONAL_INFO_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	TITLE_ID, ");
		query.append("	FIRST_NAME, ");
		query.append("	MIDDLE_NAME, ");
		query.append("	LAST_NAME, ");
		query.append("	BIRTH_DATE) "); 
		query.append("VALUES (?,?,?,?,?, ");
		query.append("		?,?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.CUST_PERSONAL_INFO_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, customerIdentityVO.getCustomerID());
			preparedStatement.setInt(counter++, customerIdentityVO.getPersonTitleID());
			preparedStatement.setString(counter++, customerIdentityVO.getFirstName());
			preparedStatement.setString(counter++, customerIdentityVO.getMiddleName());
			preparedStatement.setString(counter++, customerIdentityVO.getLastName());
			preparedStatement.setTimestamp(counter++, customerIdentityVO.getBirthDate());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
			
	}
	/**
	 * END ADD_NEW_CUSTOMER_IDENTITY_RECORD
	 * 
	 * 
	 *
	 *
	 * 
	 * 
	 * BEGIN UPDATE_CUSTOMER_PERSONAL_INFO_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updateCustomerPersonalInfoRecord(Connection connection, CustomerIdentityVO customerIdentityVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();


		query.append("UPDATE CUST_PERSONAL_INFO SET ");
		query.append("	CUSTOMER_ID=?, ");
		query.append("	TITLE_ID=?, ");
		query.append("	FIRST_NAME=?, ");
		query.append("	MIDDLE_NAME=?, ");
		query.append("	LAST_NAME=?, ");
		query.append("	BIRTH_DATE=? "); 
		query.append("WHERE 1=1 ");
		query.append("	AND	CUST_PERSONAL_INFO_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, customerIdentityVO.getCustomerID());
			preparedStatement.setInt(counter++, customerIdentityVO.getPersonTitleID());
			preparedStatement.setString(counter++, customerIdentityVO.getFirstName());
			preparedStatement.setString(counter++, customerIdentityVO.getMiddleName());
			preparedStatement.setString(counter++, customerIdentityVO.getLastName());
			preparedStatement.setTimestamp(counter++, customerIdentityVO.getBirthDate());

			preparedStatement.setInt(counter++, customerIdentityVO.getCustPersonalInfoID());

			
			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;		
	}
	/**
	 * END UPDATE_CUSTOMER_PERSONAL_INFO_RECORD
	 * 
	 * 
	 *
	 *  
	 *  
	 * 
	 * BEGIN REMOVE_CUSTOMER_PERSONAL_INFO_RECORD
	 * 
	 * @param connection
	 * @param customerPersonalInfoID
	 * @return
	 */
	protected static int removeCustomerPersonalInfoRecord(Connection connection, Integer customerPersonalInfoID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();


		query.append("DELETE FROM CUST_PERSONAL_INFO ");
		query.append("WHERE CUST_PERSONAL_INFO_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, customerPersonalInfoID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;	
	}
	/** 
	 * END REMOVE_CUSTOMER_PERSONAL_INFO_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_CUSTOMER_PHONE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewCustomerPhoneRecord(Connection connection, CustomerPhoneVO customerPhoneVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO CUST_PHONE ( ");
		query.append("	CUST_PHONE_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	PHONE_ID, ");
		query.append("	COPY_TOMOBILE_FLAG, ");
		query.append("	IS_PRIMARY) "); 
		query.append("VALUES (?,?,?,?,?,) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.CUST_PHONE_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, customerPhoneVO.getCustomerID());
			preparedStatement.setInt(counter++, customerPhoneVO.getPhoneID());
			preparedStatement.setString(counter++, customerPhoneVO.getCopyToMobileFlag());
			preparedStatement.setBoolean(counter++, customerPhoneVO.isPrimary());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_CUSTOMER_PHONE_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN UPDATE_CUSTOMER_PHONE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updateCustomerPhoneRecord(Connection connection, CustomerPhoneVO customerPhoneVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();


		query.append("UPDATE CUST_PHONE SET ");
		query.append("	CUSTOMER_ID=?, ");
		query.append("	PHONE_ID=?, ");
		query.append("	COPY_TOMOBILE_FLAG=?, ");
		query.append("	IS_PRIMARY=? "); 
		query.append("WHERE 1=1 ");
		query.append("	AND CUST_PHONE_ID=? ");
		


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, customerPhoneVO.getCustomerID());
			preparedStatement.setInt(counter++, customerPhoneVO.getPhoneID());
			preparedStatement.setString(counter++, customerPhoneVO.getCopyToMobileFlag());
			preparedStatement.setBoolean(counter++, customerPhoneVO.isPrimary());

			preparedStatement.setInt(counter++, customerPhoneVO.getCustPhoneID());
			
			
			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;	
	}
	/**
	 * END UPDATE_CUSTOMER_PHONE_RECORD
	 * 
	 * 
	 * 
	 * 
	 *  
	 * 
	 * BEGIN REMOVE_CUSTOMER_PHONE_RECORD
	 * 
	 * @param connection
	 * @param customerPhoneID
	 * @return
	 */
	protected static int removeCustomerPhoneRecord(Connection connection, Integer customerPhoneID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();


		query.append("DELETE FROM CUST_PHONE ");
		query.append("WHERE CUST_PHONE_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, customerPhoneID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;		
	}
	/** 
	 * END REMOVE_CUSTOMER_PHONE_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_CUSTOMER_PROFILE_INFO_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewCustomerProfileInfoRecord(Connection connection, CustomerProfileVO customerProfileVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO CUST_PROFILE_INFO ( ");
		query.append("	CUST_PROFILE_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	PROFESSION_ID, ");
		query.append("	JOB_POSITION, ");
		query.append("	FINANCIAL_RATING_ID, ");//5
		query.append("	SERVICE_RATING_ID, ");
		query.append("	SOCIAL_RATING_ID) "); 
		query.append("VALUES (?,?,?,?,? ");
		query.append("		?,?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.CUST_PROFILE_INFO_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, customerProfileVO.getCustomerID());
			preparedStatement.setInt(counter++, customerProfileVO.getProfessionID());
			preparedStatement.setString(counter++, customerProfileVO.getJobPosition());			
			preparedStatement.setInt(counter++, customerProfileVO.getFinancialRatingID());
			preparedStatement.setInt(counter++, customerProfileVO.getServiceRatingID());
			preparedStatement.setInt(counter++, customerProfileVO.getSocialRatingID());
			
			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_CUSTOMER_PROFILE_INFO_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN UPDATE_CUSTOMER_PROFILE_INFO_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updateCustomerProfileInfoRecord(Connection connection, CustomerProfileVO customerProfileVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();


		query.append("UPDATE CUST_PROFILE_INFO SET ");
		query.append("	CUSTOMER_ID=?, ");
		query.append("	PROFESSION_ID=?, ");
		query.append("	JOB_POSITION=?, ");
		query.append("	FINANCIAL_RATING_ID=?, ");
		query.append("	SERVICE_RATING_ID=?, ");//5
		query.append("	SOCIAL_RATING_ID=? "); 		
		query.append("WHERE 1=1 ");
		query.append("	AND CUST_PROFILE_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, customerProfileVO.getCustomerID());
			preparedStatement.setInt(counter++, customerProfileVO.getProfessionID());
			preparedStatement.setString(counter++, customerProfileVO.getJobPosition());
			preparedStatement.setInt(counter++, customerProfileVO.getFinancialRatingID());
			preparedStatement.setInt(counter++, customerProfileVO.getServiceRatingID());
			preparedStatement.setInt(counter++, customerProfileVO.getSocialRatingID());
			
			preparedStatement.setInt(counter++, customerProfileVO.getCustProfileID());
			

			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;	
	}
	/**
	 * END UPDATE_CUSTOMER_PROFILE_INFO_RECORD
	 * 
	 * 
	 *
	 *
	 *
	 * 
	 * BEGIN REMOVE_CUSTOMER_PROFILE_INFO_RECORD
	 * 
	 * @param connection
	 * @param customerProfileID
	 * @return
	 */
	protected static int removeCustomerProfileInfoRecord(Connection connection, Integer customerProfileID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();


		query.append("DELETE FROM CUST_PROFILE_INFO ");
		query.append("WHERE CUST_PROFILE_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, customerProfileID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;		
	}
	/** 
	 * END REMOVE_CUSTOMER_PROFILE_INFO_RECORD
	 * 
	 * 
	 *
	 * 
	 *
	 * 
	 * BEGIN ADD_NEW_CUSTOMER_VISIT_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewCustomerVisitRecord(Connection connection, CustomerVisitVO customerVisitVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO CUST_VISIT ( ");
		query.append("	CUST_VISIT_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	VISIT_DATE, ");
		query.append("	BRANCH_ID, ");
		query.append("	VISIT_TYPE_ID) "); 
		query.append("VALUES (?,?,?,?,?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.CUST_VISIT_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, customerVisitVO.getCustomerID());
			preparedStatement.setTimestamp(counter++, customerVisitVO.getVisitDate());
			preparedStatement.setInt(counter++, customerVisitVO.getBranchID());
			preparedStatement.setInt(counter++, customerVisitVO.getVisitTypeID());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
			
	}
	/**
	 * END ADD_NEW_CUSTOMER_VISIT_RECORD
	 * 
	 * 
	 *
	 *
	 * 
	 * 
	 * BEGIN UPDATE_CUSTOMER_VISIT_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updateCustomerVisitRecord(Connection connection, CustomerVisitVO customerVisitVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();


		query.append("UPDATE CUST_VISIT SET ");
		query.append("	CUST_VISIT_ID, ");
		query.append("	CUSTOMER_ID=?, ");
		query.append("	VISIT_DATE=?, ");
		query.append("	BRANCH_ID=?, ");
		query.append("	VISIT_TYPE_ID=? "); 
		query.append("WHERE 1=1 ");
		query.append("	AND CUST_VISIT_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, customerVisitVO.getCustomerID());
			preparedStatement.setTimestamp(counter++, customerVisitVO.getVisitDate());
			preparedStatement.setInt(counter++, customerVisitVO.getBranchID());
			preparedStatement.setInt(counter++, customerVisitVO.getVisitTypeID());
			
			preparedStatement.setInt(counter++, customerVisitVO.getVisitTypeID());


			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;		
	}
	/**
	 * END UPDATE_CUSTOMER_VISIT_RECORD
	 * 
	 * 
	 *
	 * 
	 *
	 * 
	 * BEGIN REMOVE_CUSTOMER_VISIT_RECORD
	 * 
	 * @param connection
	 * @param customerVisitID
	 * @return
	 */
	protected static int removeCustomerVisitRecord(Connection connection, Integer customerVisitID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();


		query.append("DELETE FROM CUST_VISIT ");
		query.append("WHERE CUST_VISIT_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, customerVisitID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;	
	}
	/** 
	 * END REMOVE_CUSTOMER_VISIT_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_CUSTOMER_WORK_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewCustomerWorkRecord(Connection connection, CustomerWorkVO customerWorkVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO CUST_WORK_INFO ( ");
		query.append("	CUST_WORK_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	COMPANY_NAME, ");
		query.append("	EMAIL_ADDR, ");
		query.append("	WEBSITE_ADDR, ");
		query.append("	TWITTER_ADDR, ");
		query.append("	FACEBOOK_ADDR, ");
		query.append("	WHATSUP_ADDR, ");
		query.append("	LINKEDIN_ADDR, ");
		query.append("	ADDTO_MOURSEL, ");
		query.append("	ADDTO_FAMILY, ");
		query.append("	ADDTO_GROUP, ");
		query.append("	IS_CURRENT_WORK) "); 	
		query.append("VALUES (?,?,?,?,?, ");
		query.append("		?,?,?,?,?, ");
		query.append("		?,?,?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.CUST_WORK_INFO_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, customerWorkVO.getCustomerID());
			preparedStatement.setString(counter++, customerWorkVO.getCompanyName());
			preparedStatement.setString(counter++, customerWorkVO.getEmailAddr());
			preparedStatement.setString(counter++, customerWorkVO.getWebsiteAddr());
			preparedStatement.setString(counter++, customerWorkVO.getTwitterAddr());
			preparedStatement.setString(counter++, customerWorkVO.getFacebookAddr());
			preparedStatement.setString(counter++, customerWorkVO.getWhatsupAddr());
			preparedStatement.setString(counter++, customerWorkVO.getLinkedInAddr());
			preparedStatement.setString(counter++, customerWorkVO.getAddToMoursel());
			preparedStatement.setString(counter++, customerWorkVO.getAddToFamily());
			preparedStatement.setString(counter++, customerWorkVO.getAddToGroup());
			preparedStatement.setBoolean(counter++, customerWorkVO.isCurrentWork());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
			
	}
	/**
	 * END ADD_NEW_CUSTOMER_WORK_RECORD
	 * 
	 * 
	 *
	 *
	 * 
	 * 
	 * BEGIN UPDATE_CUSTOMER_WORK_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updateCustomerWorkRecord(Connection connection, CustomerWorkVO customerWorkVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();


		query.append("UPDATE CUST_WORK_INFO SET ");
		query.append("	CUSTOMER_ID=?, ");
		query.append("	COMPANY_NAME=?, ");
		query.append("	EMAIL_ADDR=?, ");
		query.append("	WEBSITE_ADDR=?, ");
		query.append("	TWITTER_ADDR=?, ");
		query.append("	FACEBOOK_ADDR=?, ");
		query.append("	WHATSUP_ADDR=?, ");
		query.append("	LINKEDIN_ADDR=?, ");
		query.append("	ADDTO_MOURSEL=?, ");
		query.append("	ADDTO_FAMILY=?, ");
		query.append("	ADDTO_GROUP=?, ");
		query.append("	IS_CURRENT_WORK=? "); 	
		query.append("WHERE 1=1 ");
		query.append("	AND CUST_WORK_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, customerWorkVO.getCustomerID());
			preparedStatement.setString(counter++, customerWorkVO.getCompanyName());
			preparedStatement.setString(counter++, customerWorkVO.getEmailAddr());
			preparedStatement.setString(counter++, customerWorkVO.getWebsiteAddr());
			preparedStatement.setString(counter++, customerWorkVO.getTwitterAddr());
			preparedStatement.setString(counter++, customerWorkVO.getFacebookAddr());
			preparedStatement.setString(counter++, customerWorkVO.getWhatsupAddr());
			preparedStatement.setString(counter++, customerWorkVO.getLinkedInAddr());
			preparedStatement.setString(counter++, customerWorkVO.getAddToMoursel());
			preparedStatement.setString(counter++, customerWorkVO.getAddToFamily());
			preparedStatement.setString(counter++, customerWorkVO.getAddToGroup());
			preparedStatement.setBoolean(counter++, customerWorkVO.isCurrentWork());

			preparedStatement.setInt(counter++, customerWorkVO.getCustWorkID());

			
			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;	
	}
	/**
	 * END UPDATE_CUSTOMER_WORK_RECORD
	 * 
	 * 
	 *
	 * 
	 *
	 * 
	 * BEGIN REMOVE_CUSTOMER_WORK_RECORD
	 * 
	 * @param connection
	 * @param customerWorkID
	 * @return
	 */
	protected static int removeCustomerWorkRecord(Connection connection, Integer customerWorkID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();


		query.append("DELETE FROM CUST_WORK_INFO ");
		query.append("WHERE CUST_WORK_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, customerWorkID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;	
	}
	/** 
	 * END REMOVE_CUSTOMER_WORK_RECORD
	 *
	 * 
	 * 
	 *
	 * 
	 * 
	 * BEGIN ADD_NEW_DEPARTMENT_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewDepartmentRecord(Connection connection, DepartmentVO departmentVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();



		query.append("INSERT INTO DEPARTMENT ( ");
		query.append("	DEP_ID, ");
		query.append("	BRANCH_ID, ");
		query.append("	DEP_CODE, ");
		query.append("	DEP_DESC, ");
		query.append("	NOTE) ");
		query.append("VALUES (?,?,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.DEPARTMENT_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, departmentVO.getBranchID());
			preparedStatement.setString(counter++, departmentVO.getDepartmentCode());
			preparedStatement.setString(counter++, departmentVO.getDepartmentDesc());
			preparedStatement.setString(counter++, departmentVO.getNote());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
			
	}
	/**
	 * END ADD_NEW_DEPARTMENT_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_INVOICE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewInvoiceRecord(Connection connection, InvoiceVO invoiceVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();



		query.append("INSERT INTO INVOICE ( ");
		query.append("	INVOICE_ID, ");
		query.append("	INVOICE_CODE, ");
		query.append("	INVOICE_DESC, ");
		query.append("	SUPP_ID, ");
		query.append("	JOB_ID, ");
		query.append("	STATUS, ");
		query.append("	NOTE) "); 
		query.append("VALUES (?,?,?,?,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.INVOICE_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, invoiceVO.getInvoiceID());
			preparedStatement.setString(counter++, invoiceVO.getInvoiceCode());
			preparedStatement.setString(counter++, invoiceVO.getInvoiceDesc());
			preparedStatement.setInt(counter++, invoiceVO.getSupplierID());
			preparedStatement.setInt(counter++, invoiceVO.getJobID());
			preparedStatement.setString(counter++, invoiceVO.getStatus());
			preparedStatement.setString(counter++, invoiceVO.getNote());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_INVOICE_RECORD
	 * 
	 * 
	 *
	 *
	 * 
	 * 
	 * BEGIN ADD_NEW_JOB_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewJobRecord(Connection connection, JobVO jobVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();



		query.append("INSERT INTO JOB ( ");
		query.append("	JOB_ID, ");
		query.append("	ORDER_ID, ");
		query.append("	JOB_CODE, ");
		query.append("	JOB_DESC, ");
		query.append("	STATUS, ");
		query.append("	NOTE) "); 
		query.append("VALUES (?,?,?,?,?, ");
		query.append("	?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.JOB_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, jobVO.getOrderID());
			preparedStatement.setString(counter++, jobVO.getJobCode());
			preparedStatement.setString(counter++, jobVO.getJobDesc());
			preparedStatement.setString(counter++, jobVO.getStatus());
			preparedStatement.setString(counter++, jobVO.getNote());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_JOB_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_OPERATION_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewOperationRecord(Connection connection, OperationVO operationVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();



		query.append("INSERT INTO OPERATION ( ");
		query.append("	OPERATION_ID, ");
		query.append("	OPERATION_KEY, ");
		query.append("	OPERATION_PID, ");
		query.append("	OPERATION_DESC, ");
		query.append("	STATUS ");
		query.append("	NOTE) ");
		query.append("VALUES (?,?,?,?,? ");
		query.append("		,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.OPERATION_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, operationVO.getOperationKey());
			preparedStatement.setInt(counter++, operationVO.getOperationParentID());
			preparedStatement.setString(counter++, operationVO.getOperationDesc());
			preparedStatement.setString(counter++, operationVO.getStatus());
			preparedStatement.setString(counter++, operationVO.getNote());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_OPERATION_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_OPTI_ORDER_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewOptiOrderRecord(Connection connection, OrderVO orderVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO OPTI_ORDER ( ");
		query.append("	ORDER_ID, ");
		query.append("	PRESC_ID, ");
		query.append("	ORDER_CODE, ");
		query.append("	ORDER_DESC, ");
		query.append("	CREATION_DATE, ");
		query.append("	CREATED_BY, ");
		query.append("	CLOSED_DATE, ");
		query.append("	CLOSED_BY, ");
		query.append("	STATUS, ");
		query.append("	NOTE) ");
		query.append("VALUES (?,?,?,?,?, ");
		query.append("		?,?,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.ORDER_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, orderVO.getPrescriptionID());
			preparedStatement.setString(counter++, orderVO.getOrderCode());
			preparedStatement.setString(counter++, orderVO.getOrderDesc());
			preparedStatement.setTimestamp(counter++, orderVO.getCreatedDate());
			preparedStatement.setInt(counter++, orderVO.getCreatedBy());
			preparedStatement.setTimestamp(counter++, orderVO.getClosedDate());
			preparedStatement.setInt(counter++, orderVO.getClosedBy());
			preparedStatement.setString(counter++, orderVO.getStatus());
			preparedStatement.setString(counter++, orderVO.getNote());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_OPTI_ORDER_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_OPTI_USER_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewUserRecord(Connection connection, UserVO userVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();



		query.append("INSERT INTO OPTI_USER ( ");
		query.append("	USER_ID, ");
		query.append("	USER_NAME, ");
		query.append("	PASSWORD, ");
		query.append("	BRANCH_ID, ");
		query.append("	DEP_ID, ");
		query.append("	ROLE_ID, ");
		query.append("	PHONE_ID, ");
		query.append("	EMAIL, ");
		query.append("	CREATION_DATE, ");
		query.append("	CREATED_BY, ");
		query.append("	STATUS, ");
		query.append("	PWD_LASTMODIFIED_DATE, ");
		query.append("	LAST_ACCESS_TIME, ");
		query.append("	SESSION_ID, ");
		query.append("	REMOTE_HOST) "); 
		query.append("VALUES (?,?,?,?,?, ");
		query.append("		?,?,?,?,?, ");
		query.append("		?,?,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.USER_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, userVO.getUserName());
			preparedStatement.setString(counter++, userVO.getPassword());
			preparedStatement.setInt(counter++, userVO.getBranchID());
			preparedStatement.setInt(counter++, userVO.getDepartmentID());
			preparedStatement.setInt(counter++, userVO.getRoleID());
			preparedStatement.setInt(counter++, userVO.getPhoneID());
			preparedStatement.setString(counter++, userVO.getEmail());
			preparedStatement.setTimestamp(counter++, userVO.getCreationDate());
			preparedStatement.setInt(counter++, userVO.getCreatedBy());
			preparedStatement.setString(counter++, userVO.getStatus());
			if( userVO.getPasswordLastModifiedDate() != null){
				preparedStatement.setTimestamp(counter++, userVO.getPasswordLastModifiedDate());
			}else{
				preparedStatement.setNull(counter++, Types.TIMESTAMP);
			}
			if( userVO.getLastAccessTime() != null){
				preparedStatement.setTimestamp(counter++, userVO.getLastAccessTime());
			}else{
				preparedStatement.setNull(counter++, Types.TIMESTAMP);
			}
			preparedStatement.setString(counter++, userVO.getSessionID());
			preparedStatement.setString(counter++, userVO.getRemoteHost());

			
			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_OPTI_USER_RECORD
	 *  
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_OPTOMETRY_FILE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewOptometryFileRecord(Connection connection, OptometryFileVO optoVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO OPTOMETRY_FILE ( ");
		query.append("	OPTO_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	OPTO_CODE, ");
		query.append("	OPTO_DESC, ");
		query.append("	CREATION_DATE, ");
		query.append("	CREATED_BY, ");
		query.append("	STATUS, ");
		query.append("	NOTE) ");
		query.append("VALUES (?,?,?,?,?, ");
		query.append("		?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.OPTOMETRY_FILE_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, optoVO.getCustomerID());
			preparedStatement.setString(counter++, optoVO.getOptoCode());
			preparedStatement.setString(counter++, optoVO.getOptoDesc());
			preparedStatement.setTimestamp(counter++, optoVO.getCreatedDate());
			preparedStatement.setInt(counter++, optoVO.getCreatedBy());
			preparedStatement.setString(counter++, optoVO.getStatus());
			preparedStatement.setString(counter++, optoVO.getNote());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
			
	}
	/**
	 * END ADD_NEW_OPTOMETRY_FILE_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_PERSON_TITLE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewPersonTitleRecord(Connection connection, PersonTitleVO personTitleVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO PERSON_TITLE ( ");
		query.append("	TITLE_ID, ");
		query.append("	TITLE_CODE, ");
		query.append("	TITLE_DESC) "); 
		query.append("VALUES (?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.PERSON_TITLE_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, personTitleVO.getPersonTitleCode());
			preparedStatement.setString(counter++, personTitleVO.getPersonTitleDesc());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_PERSON_TITLE_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_PHONE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewPhoneRecord(Connection connection, PhoneVO phoneVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();



		query.append("INSERT INTO PHONE ( ");
		query.append("	PHONE_ID, ");
		query.append("	PHONE_TYPE_ID, ");
		query.append("	PHONE_NO, ");
		query.append("	IS_POSTPAID) ");
		query.append("VALUES (?,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.PHONE_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, phoneVO.getPhoneTypeID());
			preparedStatement.setLong(counter++, phoneVO.getPhoneNo());
			preparedStatement.setBoolean(counter++, phoneVO.isPostpaid());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_PHONE_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_PHONE_TYPE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewPhoneTypeRecord(Connection connection, PhoneTypeVO phoneTypeVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO PHONE_TYPE ( ");
		query.append("	PHONE_TYPE_ID, ");
		query.append("	PHONE_TYPE_CODE, ");
		query.append("	PHONE_TYPE_DESC, ");
		query.append("	NOTE) "); 
		query.append("VALUES (?,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.PHONE_TYPE_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, phoneTypeVO.getPhoneTypeCode());
			preparedStatement.setString(counter++, phoneTypeVO.getPhoneTypeDesc());
			preparedStatement.setString(counter++, phoneTypeVO.getNote());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_PHONE_TYPE_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_PRESCRIPTION_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewPrescriptionRecord(Connection connection, PrescriptionVO prescriptionVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();

		

		query.append("INSERT INTO PRESCRIPTION ( ");
		query.append("	PRESC_ID, ");
		query.append("	OPTO_ID, ");
		query.append("	PRESC_CODE, ");
		query.append("	PRESC_DESC, ");
		query.append("	CREATION_DATE, ");
		query.append("	CREATED_BY, ");
		query.append("	STATUS,) "); 
		query.append("	NOTE) "); 
		query.append("VALUES (?,?,?,?,?, ");
		query.append("		?,?,?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.PRESCRIPTION_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, prescriptionVO.getOptometryID());
			preparedStatement.setString(counter++, prescriptionVO.getPrescCode());
			preparedStatement.setString(counter++, prescriptionVO.getPrescDesc());
			preparedStatement.setTimestamp(counter++, prescriptionVO.getCreatedDate());
			preparedStatement.setInt(counter++, prescriptionVO.getCreatedBy());
			preparedStatement.setString(counter++, prescriptionVO.getStatus());
			preparedStatement.setString(counter++, prescriptionVO.getNote());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_PRESCRIPTION_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_PRODUCT_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewProductRecord(Connection connection, ProductVO productVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO PRODUCT ( ");
		query.append("	PROD_ID, ");
		query.append("	PROD_TYPE_ID, ");
		query.append("	PROD_CODE, ");
		query.append("	PROD_DESC, ");
		query.append("	CREATION_DATE, ");
		query.append("	CREATED_BY, ");
		query.append("	STATUS, ");
		query.append("	NOTE) "); 
		query.append("VALUES (?,?,?,?,?, ");
		query.append("		?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.PRODUCT_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, productVO.getProductTypeID());
			preparedStatement.setString(counter++, productVO.getProductCode());
			preparedStatement.setString(counter++, productVO.getProductDesc());
			preparedStatement.setTimestamp(counter++, productVO.getCreationDate());
			preparedStatement.setInt(counter++, productVO.getCreatedBy());
			preparedStatement.setString(counter++, productVO.getStatus());
			preparedStatement.setString(counter++, productVO.getNote());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_PRODUCT_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_PRODUCT_SUB_TYPE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewProductDetRecord(Connection connection, ProductDetVO productDetVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();



		query.append("INSERT INTO PRODUCT_DET ( ");
		query.append("	PROD_DET_ID, ");
		query.append("	PROD_ID, ");
		query.append("	COLUMN_NAME, ");
		query.append("	COLUMN_VALUE, ");
		query.append("	STATUS) ");
		query.append("VALUES (?,?,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.PRODUCT_DET_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, productDetVO.getProductID());
			preparedStatement.setString(counter++, productDetVO.getColumnName());
			preparedStatement.setString(counter++, productDetVO.getColumnValue());
			preparedStatement.setString(counter++, productDetVO.getStatus());
			
			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_PRODUCT_SUB_TYPE_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_PRODUCT_TYPE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewProductTypeRecord(Connection connection, ProductTypeVO productTypeVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();



		query.append("INSERT INTO PRODUCT_TYPE ( ");
		query.append("	PROD_TYPE_ID, ");
		query.append("	PROD_TYPE_CODE, ");
		query.append("	PROD_TYPE_DESC) "); 
		query.append("VALUES (?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.PRODUCT_TYPE_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, productTypeVO.getProdTypeCode());
			preparedStatement.setString(counter++, productTypeVO.getProdTypeDesc());

			inserted = preparedStatement.executeUpdate();

		
			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_PRODUCT_TYPE_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_PROFESSION_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewProfessionRecord(Connection connection, ProfessionVO professionVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();
		

		query.append("INSERT INTO PROFESSION ( ");
		query.append("	PROFESSION_ID, ");
		query.append("	PRO_FIELD_ID, ");
		query.append("	PROFESSION_CODE, ");
		query.append("	PROFESSION_DESC) "); 
		query.append("VALUES (?,?,?,?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.PROFESSION_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, professionVO.getProFieldID());
			preparedStatement.setString(counter++, professionVO.getProfessionCode());
			preparedStatement.setString(counter++, professionVO.getProfessionDesc());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_PROFESSION_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_PROFESSIONAL_FIELD_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewProfessionalFieldRecord(Connection connection, ProfessionalFieldVO professionalFieldVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();

				
		query.append("INSERT INTO PROFESSIONAL_FIELD ( ");
		query.append("	PRO_FIELD_ID, ");
		query.append("	PRO_FIELD_CODE, ");
		query.append("	PRO_FIELD_DESC) "); 	
		query.append("VALUES (?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.PROFESSIONAL_FIELD_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, professionalFieldVO.getProFieldCode());
			preparedStatement.setString(counter++, professionalFieldVO.getProFieldDesc());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_PROFESSIONAL_FIELD_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_RATING_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewRatingRecord(Connection connection, RatingVO ratingVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();



		query.append("INSERT INTO RATING ( ");
		query.append("	RATING_ID, ");
		query.append("	RATING_CODE, ");
		query.append("	RATING_DESC) "); 
		query.append("VALUES (?,?,?) ");


		int transactionID = AdminDAO.getCounter(connection, TableCounter.RATING_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, ratingVO.getRatingCode());
			preparedStatement.setString(counter++, ratingVO.getRatingDesc());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_RATING_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_ROLE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewRoleRecord(Connection connection, RoleVO roleVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO ROLE ( ");
		query.append("	ROLE_ID, ");
		query.append("	ROLE_CODE, ");
		query.append("	ROLE_DESC, ");
		query.append("	STATUS, ");
		query.append("	NOTE) ");
		query.append("VALUES (?,?,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.ROLE_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, roleVO.getRoleCode());
			preparedStatement.setString(counter++, roleVO.getRoleDesc());
			preparedStatement.setString(counter++, roleVO.getStatus());
			preparedStatement.setString(counter++, roleVO.getNote());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_ROLE_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_ROLE_OPERATION_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewRoleOperationRecord(Connection connection, RoleOperationVO roleOperationVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();



		query.append("INSERT INTO ROLE_OPERATION ( ");
		query.append("	ROLE_OPERATION_ID, ");
		query.append("	ROLE_ID, ");
		query.append("	OPERATION_ID) "); 
		query.append("VALUES (?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.ROLE_OPERATION_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, roleOperationVO.getRoleID());
			preparedStatement.setInt(counter++, roleOperationVO.getOperationID());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_ROLE_OPERATION_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_STOCK_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewStockRecord(Connection connection, StockVO stockVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO STOCK ( ");
		query.append("	STOCK_ID, ");
		query.append("	BRANCH_ID, ");
		query.append("	ITEM_ID, ");
		query.append("	ITEM_PACK_ID, ");
		query.append("	PROD_DET_ID, ");//5
		query.append("	QUANTITY, ");
		query.append("	STOCK_CODE, ");
		query.append("	STOCK_DESC, ");
		query.append("	LAST_UPDATED, ");
		query.append("	LAST_UPDATED_BY, ");//10
		query.append("	STATUS, ");
		query.append("	NOTE) "); 
		query.append("VALUES (?,?,?,?,?, ");
		query.append("		?,?,?,?,?, ");
		query.append("		?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.STOCK_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, stockVO.getBranchID());
			preparedStatement.setInt(counter++, stockVO.getItemID());
			preparedStatement.setInt(counter++, stockVO.getItemPackID());
			preparedStatement.setInt(counter++, stockVO.getProductDetID());
			preparedStatement.setInt(counter++, stockVO.getQuantity());
			preparedStatement.setString(counter++, stockVO.getStockCode());
			preparedStatement.setString(counter++, stockVO.getStockDesc());
			preparedStatement.setTimestamp(counter++, stockVO.getLastUpdated());
			preparedStatement.setString(counter++, stockVO.getLastUpdatedBy());
			preparedStatement.setString(counter++, stockVO.getStatus());
			preparedStatement.setString(counter++, stockVO.getNote());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_STOCK_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_SUPPLIER_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewSupplierRecord(Connection connection, SupplierVO supplierVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO SUPPLIER ( ");
		query.append("	SUPP_ID, ");
		query.append("	SUPP_CODE, ");
		query.append("	SUPP_DESC, ");
		query.append("	EMAIL_ADDR, ");
		query.append("	PHONE_ID, ");
		query.append("	OFFICE_ADDR_ID, ");
		query.append("	STATUS, "); 
		query.append("	NOTE) "); 
		query.append("VALUES (?,?,?,?,?, ");
		query.append("		?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.SUPPLIER_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, supplierVO.getSupplierCode());
			preparedStatement.setString(counter++, supplierVO.getSupplierDesc());
			preparedStatement.setString(counter++, supplierVO.getEmailAddress());
			preparedStatement.setInt(counter++, supplierVO.getPhoneID());
			preparedStatement.setInt(counter++, supplierVO.getOfficeAddressID());
			preparedStatement.setString(counter++, supplierVO.getStatus());
			preparedStatement.setString(counter++, supplierVO.getNote());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_SUPPLIER_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_VISIT_TYPE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewVisitTypeRecord(Connection connection, VisitTypeVO visitTypeVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO VISIT_TYPE ( ");
		query.append("	VISIT_TYPE_ID, ");
		query.append("	VISIT_TYPE_CODE, ");
		query.append("	VISIT_TYPE_DESC) "); 
		query.append("VALUES (?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.VISIT_TYPE_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, visitTypeVO.getVisitTypeCode());
			preparedStatement.setString(counter++, visitTypeVO.getVisitTypeDesc());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_VISIT_TYPE_RECORD
	 * 
	 * 
	 *
	 *  
	 * 
	 * 
	 * BEGIN ADD_NEW_SUPPLIER_BRAND_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewSupplierBrandRecord(Connection connection, SupplierBrandVO supplierBrandVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO SUPPLIER_BRAND ( ");
		query.append("	SUPP_BRAND_ID, ");
		query.append("	SUPP_ID, ");
		query.append("	BRAND_ID) "); 
		query.append("VALUES (?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.SUPPLIER_BRAND_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, supplierBrandVO.getSuppID());
			preparedStatement.setInt(counter++, supplierBrandVO.getBrandID());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
		
	}
	/**
	 * END ADD_NEW_SUPPLIER_BRAND_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_BAR_CODE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewBarCodeRecord(Connection connection, BarCodeVO barCodeVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO BARCODE ( ");
		query.append("	BARCODE_ID, ");
		query.append("	BARCODE_NO, ");
		query.append("	DATE_OF_CREATION, ");
		query.append("	CREATED_BY) "); 
		query.append("VALUES (?,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.BARCODE_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, barCodeVO.getBarCodeNo());
			preparedStatement.setTimestamp(counter++, barCodeVO.getDateOfCreation());
			preparedStatement.setInt(counter++, barCodeVO.getCreatedBy());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
	}
	/**
	 * END ADD_NEW_BAR_CODE_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_QR_CODE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewQrCodeRecord(Connection connection, QRCodeVO qrCodeVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO QRCODE ( ");
		query.append("	QRCODE_ID, ");
		query.append("	QRCODE_NO, ");
		query.append("	RELATED_LINK, ");
		query.append("	DATE_OF_CREATION, ");
		query.append("	CREATED_BY) "); 
		query.append("VALUES (?,?,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.QRCODE_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setLong(counter++, qrCodeVO.getQrCodeNo());
			preparedStatement.setString(counter++, qrCodeVO.getRelatedLink());
			preparedStatement.setTimestamp(counter++, qrCodeVO.getCreationDate());
			preparedStatement.setInt(counter++, qrCodeVO.getCreatedBy());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
	}
	/**
	 * END ADD_NEW_QR_CODE_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_BRAND_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewBrandRecord(Connection connection, BrandVO brandVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO BRAND ( ");
		query.append("	BRAND_ID, ");
		query.append("	BRAND_CODE, ");
		query.append("	BRAND_NAME, ");
		query.append("	MADE_IN, ");
//		query.append("	MADE_BY, ");
		query.append("	STATUS, ");
		query.append("	NOTE) "); 
		query.append("VALUES (?,?,?,?,? ");
		query.append("		,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.BRAND_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, brandVO.getBrandCode());
			preparedStatement.setString(counter++, brandVO.getBrandName());
			preparedStatement.setString(counter++, brandVO.getMadeIn());
			preparedStatement.setString(counter++, brandVO.getStatus());
			preparedStatement.setString(counter++, brandVO.getNote());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
	}
	/**
	 * END ADD_NEW_BRAND_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_CURRENCY_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewCurrencyRecord(Connection connection, CurrencyVO currencyVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO CURRENCY ( ");
		query.append("	CY_ID, ");
		query.append("	CY_CODE, ");
		query.append("	CY_DESC, ");
		query.append("	DECIMAL_POINTS, ");
		query.append("	ROUNDING_METH) "); 
		query.append("VALUES (?,?,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.CURRENCY_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, currencyVO.getCurrencyCode());
			preparedStatement.setString(counter++, currencyVO.getCurrencyDesc());
			preparedStatement.setInt(counter++, currencyVO.getDecimalPoints());
			preparedStatement.setInt(counter++, currencyVO.getRoundingMethod());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
	}
	/**
	 * END ADD_NEW_CURRENCY_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_ITEM_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewItemRecord(Connection connection, ItemVO itemVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO ITEM ( ");
		query.append("	ITEM_ID, ");
		query.append("	PROD_ID, ");
		query.append("	SUPP_BRAND_ID, ");
		query.append("	BARCODE_ID, ");
		query.append("	QRCODE_ID, ");//5
		query.append("	ITEM_CODE, ");
		query.append("	ITEM_DESCRIPTION, ");
		query.append("	CY_ID, ");
		query.append("	COST_PRICE) "); 
		query.append("VALUES (?,?,?,?,? ");
		query.append("		,?,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.ITEM_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, itemVO.getProductID());
			preparedStatement.setInt(counter++, itemVO.getSupplierBrandID());
			preparedStatement.setInt(counter++, itemVO.getBarCodeID());
			preparedStatement.setInt(counter++, itemVO.getQrCodeID());
			preparedStatement.setString(counter++, itemVO.getItemCode());
			preparedStatement.setString(counter++, itemVO.getItemDesc());
			preparedStatement.setInt(counter++, itemVO.getCurrencyID());
			preparedStatement.setDouble(counter++, itemVO.getCostPrice());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;	
	}
	/**
	 * END ADD_NEW_ITEM_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_ITEM_PACK_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewItemPackRecord(Connection connection, ItemPackVO itemPackVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO ITEM_PACK ( ");
		query.append("	ITEM_PACK_ID, ");
		query.append("	SUPP_BRAND_ID, ");
		query.append("	ITEM_PACK_CODE, ");
		query.append("	ITEM_PACK_DESC, ");
		query.append("	QRCODE_ID, ");
		query.append("	CY_ID, ");
		query.append("	BARCODE_ID, ");
		query.append("	COST_PRICE, ");
		query.append("	STATUS) "); 
		query.append("VALUES (?,?,?,?,? ");
		query.append("		?,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.ITEMPACK_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, itemPackVO.getSupplierBrandID());
			preparedStatement.setString(counter++, itemPackVO.getItemPackCode());
			preparedStatement.setString(counter++, itemPackVO.getItemPackDesc());
			preparedStatement.setInt(counter++, itemPackVO.getQrCodeID());
			preparedStatement.setInt(counter++, itemPackVO.getCurrencyID());
			preparedStatement.setInt(counter++, itemPackVO.getBarCodeID());
			preparedStatement.setDouble(counter++, itemPackVO.getCostPrice());
			preparedStatement.setString(counter++, itemPackVO.getStatus());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;		
	}
	/**
	 * END ADD_NEW_ITEM_PACK_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_ITEM_PACK_DET_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewItemPackDetRecord(Connection connection, ItemPackDetVO itemPackDetVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO ITEM_PACK_DET ( ");
		query.append("	ITEM_PACK_DET_ID, ");
		query.append("	ITEM_PACK_ID, ");
		query.append("	ITEM_ID, ");
		query.append("	QUANTITY) "); 
		query.append("VALUES (?,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.ITEMPACKDET_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, itemPackDetVO.getItemPackID());
			preparedStatement.setInt(counter++, itemPackDetVO.getItemID());
			preparedStatement.setInt(counter++, itemPackDetVO.getQuantity());

			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;		
	}
	/**
	 * END ADD_NEW_ITEM_PACK_DET_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_ACTIVITY_TYPE_RECORD
	 * 
	 *  
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewActivityTypeRecord(Connection connection, ActivityTypeVO activityTypeVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO ACTIVITY_TYPE ( ");
		query.append("	ACTIVITY_TYPE_ID, ");
		query.append("	ACTIVITY_TYPE_DESC, ");
		query.append("	STATUS) ");
		query.append("VALUES (?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.ACTIVITY_TYPE_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setString(counter++, activityTypeVO.getActivityTypeDesc());
			preparedStatement.setString(counter++, activityTypeVO.getStatus());
			
			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;		
	}
	/**
	 * END ADD_NEW_ACTIVITY_TYPE_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN ADD_NEW_JOURNAL_STOCK_RECORD
	 * 
	 *  
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int addNewJournalStockRecord(Connection connection, JournalStockVO journalStockVO){

		PreparedStatement preparedStatement = null;
		int inserted = 0;

		StringBuffer query = new StringBuffer();


		query.append("INSERT INTO JOURNAL_STOCK ( ");
		query.append("	JST_ID, ");
		query.append("	STOCK_ID, ");
		query.append("	QUANTITY_ADDED, ");
//		query.append("	QUANTITY_REMOVED, ");
		query.append("	UPDATED_DATE, ");
		query.append("	UPDATED_BY, ");
		query.append("	INVOICE_NO, ");
		query.append("	BILL_NO, ");
		query.append("	JST_DESC) "); 
		query.append("VALUES (?,?,?,?,? ");
		query.append("		,?,?,?) ");

		
		int transactionID = AdminDAO.getCounter(connection, TableCounter.JOURNALSTOCK_TBL.value());
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));


			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, journalStockVO.getStockID());
			preparedStatement.setInt(counter++, journalStockVO.getQuantityChanged());
			preparedStatement.setInt(counter++, journalStockVO.getStockID());
			preparedStatement.setTimestamp(counter++, journalStockVO.getUpdatedDate());
			preparedStatement.setInt(counter++, journalStockVO.getUpdatedBy());
			preparedStatement.setInt(counter++, journalStockVO.getInvoiceID());
			preparedStatement.setInt(counter++, journalStockVO.getBillID());
			preparedStatement.setString(counter++, journalStockVO.getJstCode());
			preparedStatement.setString(counter++, journalStockVO.getJstDesc());
			
			inserted = preparedStatement.executeUpdate();

			if(inserted==0)
				transactionID=0;

		} catch (SQLException sqlException) {
			transactionID=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				transactionID=0;
				logger.error("error",exception);
			}
		}
		return transactionID;		
	}
	/**
	 * END ADD_NEW_JOURNAL_STOCK_RECORD
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN GET_USER_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @return
	 */
	protected static ArrayList<UserBean> getUserRecordListFiltered(Connection connection, Integer userID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<UserBean> userList = new ArrayList<UserBean>();
		UserBean userBean = null;

		StringBuffer query = new StringBuffer();

		query.append("SELECT "); 
		query.append("	USER_ID, ");
		query.append("	USER_NAME, ");
		query.append("	PASSWORD, ");
		query.append("	BRANCH_ID, ");
		query.append("	DEP_ID, ");//5
		query.append("	ROLE_ID, ");
		query.append("	PHONE_ID, ");
		query.append("	EMAIL, ");
		query.append("	CREATION_DATE, ");
		query.append("	CREATED_BY, ");//10
		query.append("	STATUS, ");
		query.append("	PWD_LASTMODIFIED_DATE, ");
		query.append("	LAST_ACCESS_TIME, ");
		query.append("	SESSION_ID, ");
		query.append("	REMOTE_HOST ");//15
		query.append("FROM OPTI_USER ");
		query.append("WHERE 1=1 ");
		
		if(userID!=null && userID>0)
			query.append("	AND USER_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("2"))
				query.append("UPPER(USER_NAME) ");
			else if(sort.equals("8"))
				query.append("UPPER(EMAIL) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		int counter=0;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, userID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			
			resultSet = preparedStatement.executeQuery();


			BranchBean branchBean=null;
			DepartmentBean departmentBean=null;
			RoleBean roleBean=null;
			PhoneBean phoneBean=null;
			if (resultSet.next()) {
				userBean = new UserBean();

				userBean.setUserID(resultSet.getString("USER_ID"));
				userBean.setUserName(resultSet.getString("USER_NAME"));

				branchBean = new BranchBean();
				branchBean.setBranchID(resultSet.getString("BRANCH_ID"));
				userBean.setBranchBean(branchBean);
				
				departmentBean = new DepartmentBean();
				departmentBean.setDepartmentID(resultSet.getString("DEP_ID"));
				userBean.setDepartmentBean(departmentBean);
				
				roleBean = new RoleBean();
				roleBean.setRoleID(resultSet.getString("ROLE_ID"));
				userBean.setRoleBean(roleBean);
				
				phoneBean = new PhoneBean();
				phoneBean.setPhoneID(resultSet.getString("PHONE_ID"));
				userBean.setPhoneBean(phoneBean);
				
				userBean.setEmail(resultSet.getString("EMAIL"));
				userBean.setCreatedBy(resultSet.getString("CREATED_BY"));
				userBean.setCreationDate(resultSet.getTimestamp("CREATED_DATE"));
				userBean.setStatus(resultSet.getString("STATUS"));
				userBean.setPasswordLastModifiedDate(resultSet.getTimestamp("PWD_LASTMODIFIED"));
				userBean.setLastAccessTime(resultSet.getTimestamp("LAST_ACCESS_TIME"));
				userBean.setSessionID(resultSet.getString("SESSION_ID"));
				
				userList.add(userBean);

			}

		} catch (SQLException sqlException) {
			userList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				logger.error("error",exception);
			}
		}
		return userList;	
	}
	/**
	 * END GET_USER_RECORD_LIST_FILTERED
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN LOGIN
	 * 
	 * @param connection
	 * @param userName
	 * @param encPassword
	 * @return
	 */
	protected static boolean login(Connection connection, String userName, String encPassword){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		UserVO userVO = null;
		boolean authenticated = false;

		StringBuffer query = new StringBuffer();
		

		query.append("SELECT ");
		query.append("		USER_ID,");
		query.append("		USER_NAME,");
		query.append("		PASSWORD,");
		query.append("		LAST_ACCESS_TIME,");
		query.append("		SESSION_ID ");

		query.append("FROM 	OPTI_USER ");
		if(!StringUtil.nullToEmpty(userName).equals(""))
			query.append("WHERE OPTI_USER.USER_NAME=?");


		int counter = 1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));

			if(!StringUtil.nullToEmpty(userName).equals(""))
				preparedStatement.setString(counter++, userName);

			resultSet = preparedStatement.executeQuery();


			if (resultSet.next()) {
				userVO = new UserVO();

				userVO.setUserID(resultSet.getInt("USER_ID"));
				userVO.setUserName(resultSet.getString("USER_NAME"));
				userVO.setPassword(resultSet.getString("PASSWORD"));
				userVO.setLastAccessTime(resultSet.getTimestamp("LAST_ACCESS_TIME"));
				userVO.setSessionID(resultSet.getString("SESSION_ID"));

				if (StringUtil.nullToEmpty(userVO.getPassword()).equals(encPassword))
					authenticated = true;
				else
					authenticated = false;

			} else{
				authenticated = false;
			}


		} catch (SQLException sqlException) {
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
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
	 * @param connection
	 * @param userID
	 * @param sessionID
	 * @param loginTime
	 * @return
	 */
	protected static boolean updateUserSession(Connection connection, int userID, String sessionID, Timestamp loginTime){

		PreparedStatement preparedStatement = null;
		boolean updated = true;

		StringBuffer query = new StringBuffer();

		query.append("UPDATE OPTI_USER ");
		query.append(" 	SET LAST_ACCESS_TIME = ?,");
		query.append("			SESSION_ID = ?");
		query.append(" 	WHERE USER_ID = ?");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));

			preparedStatement.setTimestamp(counter++, loginTime);
			preparedStatement.setString(counter++, sessionID);
			preparedStatement.setInt(counter++, userID);


			preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=false;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				logger.error("error",exception);
			}
		}
		return updated;	
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
	 * @param connection
	 * @param userID
	 * @param sessionId
	 * @return
	 */
	protected static boolean logout(Connection connection, int userID, String sessionId) {

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		StringBuffer query = new StringBuffer();
		query.append("UPDATE OPTI_USER ");
		query.append("	SET SESSION_ID =? ");
		query.append("	WHERE USER_ID = ?");
		if (sessionId != null) {
			query.append("	  AND SESSION_ID =? ");
		}
		boolean isLoggedOut = true;

		int counter = 1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setNull(counter++, Types.VARCHAR);
			preparedStatement.setInt(counter++, userID);
			if (sessionId != null) {
				preparedStatement.setString(counter++, sessionId);
			}
			preparedStatement.executeUpdate();
		} catch (SQLException sqlException) {
			isLoggedOut = false;
			logger.error("SQLException", sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException sqlException) {
				logger.error("SQLException", sqlException);
			}
		}
		return isLoggedOut;
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
	 * 
	 * @param connection
	 * @param userID
	 * @param userName
	 * @return
	 */
	protected static UserBean getUserDetails(Connection connection, Integer userID, String userName){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		UserBean userBean = null;

		StringBuffer query = new StringBuffer();

		
		query.append("SELECT "); 
		query.append("	USER_ID, ");
		query.append("	USER_NAME, ");
		query.append("	PASSWORD, ");
		query.append("	BRANCH_ID, ");
		query.append("	DEP_ID, ");
		query.append("	ROLE_ID, ");
		query.append("	PHONE_ID, ");
		query.append("	EMAIL, ");
		query.append("	CREATION_DATE, ");
		query.append("	CREATED_BY, ");
		query.append("	STATUS, ");
		query.append("	PWD_LASTMODIFIED_DATE, ");
		query.append("	LAST_ACCESS_TIME, ");
		query.append("	SESSION_ID, ");
		query.append("	REMOTE_HOST ");
		query.append("FROM OPTI_USER ");
		query.append("WHERE 1=1 ");
		
		if(userID != null && userID>0)
			query.append(" AND OPTI_USER.USER_ID=?");
		if(!StringUtil.nullToEmpty(userName).equals(""))
			query.append("AND OPTI_USER.USER_NAME=?");


		int counter = 1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));

			if(userID != 0)
				preparedStatement.setInt(counter++, userID);

			if(!StringUtil.nullToEmpty(userName).equals(""))
				preparedStatement.setString(counter++, userName);


			resultSet = preparedStatement.executeQuery();


			BranchBean branchBean=null;
			DepartmentBean departmentBean=null;
			RoleBean roleBean=null;
			PhoneBean phoneBean=null;
			if (resultSet.next()) {
				userBean = new UserBean();

				userBean.setUserID(resultSet.getString("USER_ID"));
				userBean.setUserName(resultSet.getString("USER_NAME"));

				branchBean = new BranchBean();
				branchBean.setBranchID(resultSet.getString("BRANCH_ID"));
				userBean.setBranchBean(branchBean);
				
				departmentBean = new DepartmentBean();
				departmentBean.setDepartmentID(resultSet.getString("DEP_ID"));
				userBean.setDepartmentBean(departmentBean);
				
				roleBean = new RoleBean();
				roleBean.setRoleID(resultSet.getString("ROLE_ID"));
				userBean.setRoleBean(roleBean);
				
				phoneBean = new PhoneBean();
				phoneBean.setPhoneID(resultSet.getString("PHONE_ID"));
				userBean.setPhoneBean(phoneBean);
				
				userBean.setEmail(resultSet.getString("EMAIL"));
				userBean.setCreatedBy(resultSet.getString("CREATED_BY"));
				userBean.setCreationDate(resultSet.getTimestamp("CREATION_DATE"));
				userBean.setStatus(resultSet.getString("STATUS"));
				userBean.setPasswordLastModifiedDate(resultSet.getTimestamp("PWD_LASTMODIFIED_DATE"));
				userBean.setLastAccessTime(resultSet.getTimestamp("LAST_ACCESS_TIME"));
				userBean.setSessionID(resultSet.getString("SESSION_ID"));
				userBean.setRemoteHost(resultSet.getString("REMOTE_HOST"));
				

			}

		} catch (SQLException sqlException) {
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
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
	 * BEGIN GET_ACTIVITY_LOGGING
	 * 
	 * @param connection
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
	protected static ArrayList<ActivityLogBean> getActivityLogging(Connection connection, Integer userID, Integer activityID, Timestamp fromDate, Timestamp toDate, String criteriaType, String criteriaValue, String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		StringBuffer query = new StringBuffer();

		
		query.append("SELECT ");
		query.append("	ACTIVITY_LOG.USER_ID, ");
		query.append("	ACTIVITY_LOG.ACTIVITY_DATE, ");
		query.append("	ACTIVITY_LOG.ACTIVITY_DESC, ");
		query.append("	ACTIVITY_LOG.ACTIVITY_TYPE_ID, ");
		query.append("	ACTIVITY_TYPE.ACTIVITY_TYPE_DESC, ");//5 
		query.append("	'submitted' AS STATUS, ");
		query.append("	ACTIVITY_LOG.REFERENCE, ");
		query.append("	ACTIVITY_LOG.ACTIVITY_ID ");//8
		query.append("FROM ACTIVITY_LOG,ACTIVITY_TYPE ");		
		query.append("WHERE ACTIVITY_LOG.ACTIVITY_TYPE_ID = ACTIVITY_TYPE.ACTIVITY_TYPE_ID ");
		query.append("	AND ACTIVITY_TYPE.STATUS = '1' ");
		//query.append("	AND ACTIVITY_LOG.ACTIVITY_TYPE_ID IN (1,2) ");

		if(userID != null)
			query.append("	AND ACTIVITY_LOG.USER_ID = ? ");					
		if(activityID != null)
			query.append("	AND ACTIVITY_LOG.ACTIVITY_ID ");	
		if(fromDate != null && toDate !=null)
			query.append("	AND ACTIVITY_LOG.ACTIVITY_DATE BETWEEN ? AND ? ");

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");



		query.append("ORDER BY ");

		query.append(sort);
		query.append(" ");
		query.append(direction);
		query.append(" ");
		if(direction.equals("asc")){
			query.append(" NULLS FIRST");
		} else{
			query.append(" NULLS LAST");
		}



		ArrayList<ActivityLogBean> activityList = new ArrayList<ActivityLogBean>();
		ActivityLogBean activityLogBean = null;

		try {
			preparedStatement = connection.prepareStatement(new String(query));
			int counter = 1;

			if(userID != null)
				preparedStatement.setInt(counter++, userID);
			if(activityID != null)
				preparedStatement.setInt(counter++, activityID);
			if (fromDate != null && toDate != null) {
				preparedStatement.setDate(counter++, new java.sql.Date(fromDate.getTime()));
				preparedStatement.setDate(counter++, new java.sql.Date(toDate.getTime()));
			}
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");

			resultSet = preparedStatement.executeQuery();

			ActivityTypeBean activityTypeBean=null;
			UserBean userBean = null;
			while (resultSet.next()) {
				activityLogBean = new ActivityLogBean();

				userBean = new UserBean();
				userBean.setUserID(resultSet.getString("USER_ID"));
				activityLogBean.setUserBean(userBean);
				
				activityLogBean.setActivityDate(resultSet.getDate("ACTIVITY_DATE"));
				activityLogBean.setActivityDesc(resultSet.getString("ACTIVITY_DESC"));
				
				activityTypeBean = new ActivityTypeBean();
				activityTypeBean.setActivityTypeID(resultSet.getString("ACTIVITY_TYPE_ID"));
				activityTypeBean.setActivityTypeDesc(resultSet.getString("ACTIVITY_TYPE_DESC"));
				activityLogBean.setActivityTypeBean(activityTypeBean);
				
				activityLogBean.setReference(resultSet.getString("REFERENCE"));
				activityLogBean.setActivityID(resultSet.getString("ACTIVITY_ID"));


				activityList.add(activityLogBean);
			}
		} catch (SQLException sqlException) {
			logger.error("error ", sqlException);

		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException ignore) {
				logger.error("error", ignore);
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
	 * 
	 * @param connection
	 * @param activityTypeId
	 * @param userNumber
	 * @param activityDate
	 * @param activityDescription
	 * @param sessionId
	 * @param reference
	 * @return
	 */
	protected static int logActivity(Connection connection, ActivityLogVO activityLogVO) {
		PreparedStatement preparedStatement= null;
		ResultSet resultSet = null;

		StringBuffer query = new StringBuffer();
		int inserted = 0;
		int counter=1;


		query.append("INSERT INTO ACTIVITY_LOG ");
		query.append("( ");
		query.append("	ACTIVITY_ID, ");
		query.append("	ACTIVITY_TYPE_ID, ");
		query.append("	USER_ID, ");
		query.append("	ACTIVITY_DATE, ");
		query.append("	ACTIVITY_DESC, ");//5
		query.append("	SESSION_ID, ");
		query.append("	REFERENCE ) ");
		query.append("VALUES (?,?,?,?,?,");
		query.append("		?,?)");



		int transactionID = AdminDAO.getCounter(connection, TableCounter.ACTIVITY_LOG_TBL.value());

		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, transactionID);
			preparedStatement.setInt(counter++, activityLogVO.getActivityTypeID());
			preparedStatement.setInt(counter++, activityLogVO.getUserID());
			preparedStatement.setTimestamp(counter++, activityLogVO.getActivityDate());
			preparedStatement.setString(counter++, activityLogVO.getActivityDesc());
			preparedStatement.setString(counter++, activityLogVO.getSessionID());			
			preparedStatement.setString(counter++, activityLogVO.getReference());

			inserted = preparedStatement.executeUpdate();


		} catch (SQLException sqlException) {
			inserted=0;
			logger.error("SQLException",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet!= null)
					resultSet.close();
				if (preparedStatement!= null)
					preparedStatement.close();
			} catch (SQLException sqlException) {
				logger.error("SQLException",sqlException);
			}
		}		
		return inserted;
	}
	/** 
	 * END LOG_ACTIVITY
	 * 
	 * 
	 * 
	 *
	 *
	 * 
	 * BEGIN GET_CUSTOMER_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @return
	 */
	protected static ArrayList<CustomerBean> getCustomerRecordListFiltered(Connection connection, Integer customerID, 
								String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<CustomerBean> customerList = new ArrayList<CustomerBean>();
		CustomerBean customerBean = null;

		StringBuffer query = new StringBuffer();

		query.append("SELECT ");
		query.append("	CUSTOMER_ID, ");
		query.append("	CUST_TYPE_ID, ");
		query.append("	BRANCH_ID, ");
		query.append("	OLD_KEY_NO, ");
		query.append("	NEW_KEY_NO, ");//5
		query.append("	PHOTO_PATH, ");
		query.append("	CREATION_DATE, ");
		query.append("	CREATED_BY, ");
		query.append("	STATUS, ");
		query.append("	FIRST_SALES_DATE, ");//10
		query.append("	FIRST_PRESC_DATE, ");
		query.append("	IS_INFOTEST_OR_RXONLY, ");
		query.append("	IS_DIRECT_SALES, ");
		query.append("	IS_SALES_CONTACTLENSES, ");
		query.append("	IS_SALES_HEARING, ");//15
		query.append("	TOBE_UPDATED_ID, ");
		query.append("	TOBE_UPDATED_PROFILE, ");
		query.append("	TOBE_DELETED, ");
		query.append("	TOBE_DOUBLEFILE_CHECK, ");
		query.append("	TOBE_ARCHVIED, ");//20
		query.append("	NOTE ");		
		query.append("FROM CUSTOMER ");
		query.append("WHERE 1=1 ");
		
		if(customerID!=null && customerID>0)
			query.append("	AND CUSTOMER_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append("	AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");
			query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(customerID!=null && customerID>0)
				preparedStatement.setInt(counter++, customerID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			resultSet = preparedStatement.executeQuery();


			BranchBean branchBean=null;
			CustomerTypeBean customerTypeBean=null;
			while (resultSet.next()) {
				customerBean = new CustomerBean();

				customerBean.setCustomerID(resultSet.getString("CUSTOMER_ID"));
				
				customerTypeBean = new CustomerTypeBean();
				customerTypeBean.setCustTypeID("CUST_TYPE_ID");
				customerBean.setCustTypeBean(customerTypeBean);

				branchBean = new BranchBean();
				branchBean.setBranchID(resultSet.getString("BRANCH_ID"));
				customerBean.setBranchBean(branchBean);
				
				customerBean.setOldKeyNo(resultSet.getString("OLD_KEY_NO"));
				customerBean.setNewKeyNo(resultSet.getString("NEW_KEY_NO"));
				customerBean.setPhotoPath(resultSet.getString("PHOTO_PATH"));
				customerBean.setCreationDate(resultSet.getDate("CREATION_DATE"));
				customerBean.setCreatedBy(resultSet.getString("CREATED_BY"));
				customerBean.setStatus(resultSet.getString("STATUS"));
				customerBean.setFirstSalesDate(resultSet.getDate("FIRST_SALES_DATE"));
				customerBean.setFirstPrescDate(resultSet.getDate("FIRST_PRESC_DATE"));
				customerBean.setInfoTestOrRXOnly((resultSet.getString("IS_INFOTEST_OR_RXONLY").equals("y"))?true:false);
				customerBean.setDirectSales((resultSet.getString("IS_DIRECT_SALES").equals("y"))?true:false);
				customerBean.setSalesContactLenses((resultSet.getString("IS_SALES_CONTACTLENSES").equals("y"))?true:false);
				customerBean.setSalesHearing((resultSet.getString("IS_SALES_HEARING").equals("y"))?true:false);
				customerBean.setToBeUpdatedID(resultSet.getString("TOBE_UPDATED_ID"));
				customerBean.setToBeUpdatedProfile(resultSet.getString("TOBE_UPDATED_PROFILE"));
				customerBean.setToBeDeleted(resultSet.getString("TOBE_DELETED"));
				customerBean.setToBeDoubleFileCheck(resultSet.getString("TOBE_DOUBLEFILE_CHECK"));
				customerBean.setToBeArchived(resultSet.getString("TOBE_ARCHVIED"));
				customerBean.setNote(resultSet.getString("NOTE"));
				
				customerList.add(customerBean);
			}

		} catch (SQLException sqlException) {
			customerList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				logger.error("error",exception);
			}
		}
		return customerList;	
	}
	/**
	 * END GET_CUSTOMER_RECORD_LIST_FILTERED
	 *  
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN GET_CUSTOMER_TYPE_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @return
	 */
	protected static ArrayList<CustomerTypeBean> getCustTypeRecordListFiltered(Connection connection, Integer customerTypeID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<CustomerTypeBean> customerTypeList = new ArrayList<CustomerTypeBean>();
		CustomerTypeBean customerTypeBean = null;

		StringBuffer query = new StringBuffer();

		query.append("SELECT ");
		query.append("	CUST_TYPE_ID, ");
		query.append("	CUST_TYPE_CODE, ");
		query.append("	CUST_TYPE_DESC ");
		query.append("FROM CUSTOMER_TYPE ");
		query.append("WHERE 1=1 ");
		
		if(customerTypeID!=null && customerTypeID>0)
			query.append("	AND CUST_TYPE_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("2"))
				query.append("UPPER(CUST_TYPE_CODE) ");
			else if(sort.equals("3"))
				query.append("UPPER(CUST_TYPE_DESC) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(customerTypeID!=null && customerTypeID>0)
				preparedStatement.setInt(counter++, customerTypeID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			
			resultSet = preparedStatement.executeQuery();


			while (resultSet.next()) {
				customerTypeBean = new CustomerTypeBean();

				customerTypeBean.setCustTypeID(resultSet.getString("CUST_TYPE_ID"));
				customerTypeBean.setCustTypeCode(resultSet.getString("CUST_TYPE_CODE"));
				customerTypeBean.setCustTypeDesc(resultSet.getString("CUST_TYPE_DESC"));

				customerTypeList.add(customerTypeBean);
			}

		} catch (SQLException sqlException) {
			customerTypeList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				logger.error("error",exception);
			}
		}
		return customerTypeList;	
	}
	/**
	 * END GET_CUSTOMER_TYPE_RECORD_LIST_FILTERED
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN GET_CUSTOMER_ADDRESS_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @return
	 */
	protected static ArrayList<CustomerAddressBean> getCustAddressRecordListFiltered(Connection connection, Integer customerAddressID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<CustomerAddressBean> customerAddressList = new ArrayList<CustomerAddressBean>();
		CustomerAddressBean customerAddressBean = null;

		StringBuffer query = new StringBuffer();

		query.append("SELECT ");
		query.append("	CUST_ADDRESS_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	ADDRESS_ID, ");
		query.append("	ADDRESS_TYPE_ID, ");
		query.append("	PROXIMITY, ");//5
		query.append("	NOTE "); 
		query.append("FROM CUST_ADDRESS_INFO ");
		query.append("WHERE 1=1 ");
		
		
		if(customerAddressID!=null && customerAddressID>0)
			query.append("	AND CUST_ADDRESS_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("5"))
				query.append("UPPER(PROXIMITY) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(customerAddressID!=null && customerAddressID>0)
				preparedStatement.setInt(counter++, customerAddressID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			
			resultSet = preparedStatement.executeQuery();


			CustomerBean customerBean=null;
			AddressBean addressBean=null;
			AddressTypeBean addressTypeBean=null;
			while (resultSet.next()) {
				customerAddressBean = new CustomerAddressBean();

				customerAddressBean.setCustAddressID(resultSet.getString("CUST_ADDRESS_ID"));
				
				customerBean = new CustomerBean();
				customerBean.setCustomerID(resultSet.getString("CUSTOMER_ID"));
				customerAddressBean.setCustomerBean(customerBean);
				
				addressBean = new AddressBean();
				addressBean.setAddressID(resultSet.getString("ADDRESS_ID"));
				customerAddressBean.setAddressBean(addressBean);
				
				addressTypeBean = new AddressTypeBean();
				addressTypeBean.setAddrTypeID(resultSet.getString("ADDRESS_TYPE_ID"));
				customerAddressBean.setAddressTypeBean(addressTypeBean);
				
				customerAddressBean.setProximity(resultSet.getString("PROXIMITY"));
				customerAddressBean.setNote(resultSet.getString("NOTE"));
				
				customerAddressList.add(customerAddressBean);

			}

		} catch (SQLException sqlException) {
			customerAddressList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				logger.error("error",exception);
			}
		}
		return customerAddressList;	
	}
	/**
	 * END GET_CUSTOMER_ADDRESS_RECORD_LIST_FILTERED
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN GET_CUSTOMER_OLDDB_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @return
	 */
	protected static ArrayList<CustomerOldDBBean> getCustOldDBRecordListFiltered(Connection connection, Integer customerOldDBID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<CustomerOldDBBean> custOldDBList = new ArrayList<CustomerOldDBBean>();
		CustomerOldDBBean customerOldDBBean = null;

		StringBuffer query = new StringBuffer();

		query.append("SELECT ");
		query.append("	CUST_OLDDB_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	FIRST_MID_LAST, ");
		query.append("	CENTER, ");
		query.append("	FILE_CREATION_DATE, ");//5
		query.append("	FIRST_DATE, ");
		query.append("	FILE_NO, ");
		query.append("	IS_IMPORTED_OPTICAL, ");
		query.append("	IS_IMPORTED_CONTACTLENSE, ");
		query.append("	MOBILE_NO, ");//10
		query.append("	OLD_CONTACTLENSE "); 
		query.append("FROM CUST_OLDDB_INFO ");
		query.append("WHERE 1=1 ");
		
		
		if(customerOldDBID!=null && customerOldDBID>0)
			query.append("	AND CUST_OLDDB_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("3"))
				query.append("UPPER(FIRST_MID_LAST) ");
			else if(sort.equals("4"))
				query.append("UPPER(CENTER) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(customerOldDBID!=null && customerOldDBID>0)
				preparedStatement.setInt(counter++, customerOldDBID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			
			resultSet = preparedStatement.executeQuery();


			CustomerBean customerBean=null;
			while (resultSet.next()) {
				customerOldDBBean = new CustomerOldDBBean();

				customerOldDBBean.setCustOldDBID(resultSet.getString("CUST_OLDDB_ID"));

				customerBean = new CustomerBean();
				customerBean.setCustomerID(resultSet.getString("CUSTOMER_ID"));
				customerOldDBBean.setCustomerBean(customerBean);

				customerOldDBBean.setFirstMidLast(resultSet.getString("FIRST_MID_LAST"));
				customerOldDBBean.setCenter(resultSet.getString("CENTER"));
				customerOldDBBean.setFileCreationDate(resultSet.getDate("FILE_CREATION_DATE"));
				customerOldDBBean.setFirstDate(resultSet.getDate("FIRST_DATE"));
				customerOldDBBean.setFileNo(resultSet.getString("FILE_NO"));
				customerOldDBBean.setImportedOptical(resultSet.getBoolean("IS_IMPORTED_OPTICAL"));
				customerOldDBBean.setImportedContactLense(resultSet.getBoolean("IS_IMPORTED_CONTACTLENSE"));
				customerOldDBBean.setMobileNo(resultSet.getString("MOBILE_NO"));
				customerOldDBBean.setOldContactLense(resultSet.getString("OLD_CONTACTLENSE"));
				
				custOldDBList.add(customerOldDBBean);

			}

		} catch (SQLException sqlException) {
			custOldDBList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				logger.error("error",exception);
			}
		}
		return custOldDBList;	
	}
	/**
	 * END GET_CUSTOMER_OLDDB_RECORD_LIST_FILTERED
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN GET_CUSTOMER_IDENTITY_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @return
	 */
	protected static ArrayList<CustomerIdentityBean> getCustIdentityRecordListFiltered(Connection connection, Integer customerIdentityID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<CustomerIdentityBean> customerIdentityList = new ArrayList<CustomerIdentityBean>();
		CustomerIdentityBean customerIdentityBean = null;

		StringBuffer query = new StringBuffer();

		query.append("SELECT ");
		query.append("	CUST_PERSONAL_INFO_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	TITLE_ID, ");
		query.append("	FIRST_NAME, ");
		query.append("	MIDDLE_NAME, ");//5
		query.append("	LAST_NAME, ");
		query.append("	BIRTH_DATE ");  
		query.append("FROM CUST_PERSONAL_INFO ");
		query.append("WHERE 1=1 ");
		
		
		if(customerIdentityID!=null && customerIdentityID>0)
			query.append("	AND CUST_PERSONAL_INFO_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("4"))
				query.append("UPPER(FIRST_NAME) ");
			else if(sort.equals("5"))
				query.append("UPPER(MIDDLE_NAME) ");
			else if(sort.equals("6"))
				query.append("UPPER(LAST_NAME) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(customerIdentityID!=null && customerIdentityID>0)
				preparedStatement.setInt(counter++,customerIdentityID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			
			resultSet = preparedStatement.executeQuery();


			CustomerBean customerBean=null;
			PersonTitleBean personTitleBean=null;
			while (resultSet.next()) {
				customerIdentityBean = new CustomerIdentityBean();

				customerIdentityBean.setCustPersonalInfoID(resultSet.getString("CUST_PERSONAL_INFO_ID"));
				
				customerBean = new CustomerBean();
				customerBean.setCustomerID(resultSet.getString("CUSTOMER_ID"));
				customerIdentityBean.setCustomerBean(customerBean);
				
				personTitleBean = new PersonTitleBean();
				personTitleBean.setPersonTitleID(resultSet.getString("TITLE_ID"));
				customerIdentityBean.setPersonTitleBean(personTitleBean);
				
				customerIdentityBean.setFirstName(resultSet.getString("FIRST_NAME"));
				customerIdentityBean.setMiddleName(resultSet.getString("MIDDLE_NAME"));
				customerIdentityBean.setLastName(resultSet.getString("LAST_NAME"));
				customerIdentityBean.setBirthDate(resultSet.getDate("BIRTH_DATE"));
				
				customerIdentityList.add(customerIdentityBean);

			}

		} catch (SQLException sqlException) {
			customerIdentityList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				logger.error("error",exception);
			}
		}
		return customerIdentityList;	
	}
	/**
	 * END GET_CUSTOMER_IDENTITY_RECORD_LIST_FILTERED
	 * 
	 * 
	 *  
	 * 
	 * 
	 * 
	 * BEGIN GET_CUSTOMER_PHONE_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @return
	 */
	protected static ArrayList<CustomerPhoneBean> getCustPhoneRecordListFiltered(Connection connection, Integer customerPhoneID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<CustomerPhoneBean> customerPhoneList = new ArrayList<CustomerPhoneBean>();
		CustomerPhoneBean customerPhoneBean = null;

		StringBuffer query = new StringBuffer();

		query.append("SELECT "); 
		query.append("	CUST_PHONE_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	PHONE_ID, ");
		query.append("	COPY_TOMOBILE_FLAG, ");
		query.append("	IS_PRIMARY ");//5
		query.append("FROM CUST_PHONE ");
		query.append("WHERE 1=1 ");
		
		if(customerPhoneID!=null && customerPhoneID>0)
			query.append("	AND CUST_PHONE_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");
			query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(customerPhoneID!=null && customerPhoneID>0)
				preparedStatement.setInt(counter++, customerPhoneID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			
			resultSet = preparedStatement.executeQuery();


			CustomerBean customerBean=null;
			PhoneBean phoneBean=null;
			while (resultSet.next()) {
				customerPhoneBean = new CustomerPhoneBean();

				customerPhoneBean.setCustPhoneID(resultSet.getString("CUST_PHONE_ID"));

				customerBean = new CustomerBean();
				customerBean.setCustomerID(resultSet.getString("CUSTOMER_ID"));
				customerPhoneBean.setCustomerBean(customerBean);
				
				phoneBean = new PhoneBean();
				phoneBean.setPhoneID(resultSet.getString("PHONE_ID"));
				customerPhoneBean.setPhoneBean(phoneBean);
				
				customerPhoneBean.setCopyToMobileFlag(resultSet.getString("COPY_TOMOBILE_FLAG"));
				customerPhoneBean.setPrimary(resultSet.getBoolean("IS_PRIMARY"));
				
				customerPhoneList.add(customerPhoneBean);

			}

		} catch (SQLException sqlException) {
			customerPhoneList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				logger.error("error",exception);
			}
		}
		return customerPhoneList;	
	}
	/**
	 * END GET_CUSTOMER_PHONE_RECORD_LIST_FILTERED
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN GET_CUSTOMER_PROFILE_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @return
	 */
	protected static ArrayList<CustomerProfileBean> getCustProfileRecordListFiltered(Connection connection, Integer customerProfileID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<CustomerProfileBean> customerProfileList = new ArrayList<CustomerProfileBean>();
		CustomerProfileBean customerProfileBean = null;

		StringBuffer query = new StringBuffer();


		query.append("SELECT "); 
		query.append("	CUST_PROFILE_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	PROFESSION_ID, ");
		query.append("	JOB_POSITION, ");
		query.append("	FINANCIAL_RATING_ID, ");//5
		query.append("	SERVICE_RATING_ID, ");
		query.append("	SOCIAL_RATING_ID ");
		query.append("FROM CUST_PROFILE_INFO ");
		query.append("WHERE 1=1 ");
		
		if(customerProfileID!=null && customerProfileID>0)
			query.append("	AND CUST_PROFILE_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("2"))
				query.append("UPPER(JOB_POSITION) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			
			if(customerProfileID!=null && customerProfileID>0)
				preparedStatement.setInt(counter++, customerProfileID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			
			resultSet = preparedStatement.executeQuery();


			CustomerBean customerBean=null;
			RatingBean financialRatingBean=null;
			RatingBean socialRatingBean=null;
			RatingBean serviceRatingBean=null;
			ProfessionBean professionBean=null;
			
			while (resultSet.next()) {
				customerProfileBean = new CustomerProfileBean();

				customerProfileBean.setCustProfileID(resultSet.getString("CUST_PROFILE_ID"));				

				professionBean = new ProfessionBean();
				professionBean.setProfessionID(resultSet.getString("PROFESSION_ID"));
				customerProfileBean.setProfessionBean(professionBean);
				
				customerBean = new CustomerBean();
				customerBean.setCustomerID(resultSet.getString("CUSTOMER_ID"));
				customerProfileBean.setCustomerBean(customerBean);
				
				customerProfileBean.setJobPosition(resultSet.getString("JOB_POSITION"));
				
				financialRatingBean = new RatingBean();
				financialRatingBean.setRatingID(resultSet.getString("FINANCIAL_RATING_ID"));
				customerProfileBean.setFinancialRatingBean(financialRatingBean);
				
				serviceRatingBean = new RatingBean();
				serviceRatingBean.setRatingID(resultSet.getString("SERVICE_RATING_ID"));
				customerProfileBean.setServiceRatingBean(serviceRatingBean);
				
				socialRatingBean = new RatingBean();
				socialRatingBean.setRatingID(resultSet.getString("SERVICE_RATING_ID"));
				customerProfileBean.setSocialRatingBean(socialRatingBean);
				
				
				customerProfileList.add(customerProfileBean);
			}

		} catch (SQLException sqlException) {
			customerProfileList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				logger.error("error",exception);
			}
		}
		return customerProfileList;	
	}
	/**
	 * END GET_CUSTOMER_PROFILE_RECORD_LIST_FILTERED
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN GET_CUSTOMER_VISIT_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @return
	 */
	protected static ArrayList<CustomerVisitBean> getCustVisitRecordListFiltered(Connection connection, Integer customerVisitID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<CustomerVisitBean> customerVisitList = new ArrayList<CustomerVisitBean>();
		CustomerVisitBean customerVisitBean = null;

		StringBuffer query = new StringBuffer();

		query.append("SELECT "); 
		query.append("	CUST_VISIT_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	VISIT_DATE, ");
		query.append("	BRANCH_ID, ");
		query.append("	VISIT_TYPE_ID ");//5 
		query.append("FROM CUST_VISIT ");
		query.append("WHERE 1=1 ");
		
		if(customerVisitID!=null && customerVisitID>0)
			query.append("	AND CUST_VISIT_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");
			query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			
			if(customerVisitID!=null && customerVisitID>0)
				preparedStatement.setInt(counter++, customerVisitID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			
			resultSet = preparedStatement.executeQuery();


			CustomerBean customerBean=null;
			BranchBean branchBean=null;
			VisitTypeBean visitTypeBean=null;
			while (resultSet.next()) {
				customerVisitBean = new CustomerVisitBean();

				customerVisitBean.setCustVisitID(resultSet.getString("CUST_VISIT_ID"));

				customerBean = new CustomerBean();
				customerBean.setCustomerID(resultSet.getString("CUSTOMER_ID"));
				customerVisitBean.setCustomerBean(customerBean);

				customerVisitBean.setVisitDate(resultSet.getDate("VISIT_DATE"));

				branchBean = new BranchBean();
				branchBean.setBranchID(resultSet.getString("BRANCH_ID"));
				customerVisitBean.setBranchBean(branchBean);
				
				visitTypeBean = new VisitTypeBean();
				visitTypeBean.setVisitTypeID(resultSet.getString("VISIT_TYPE_ID"));
				customerVisitBean.setVisitTypeBean(visitTypeBean);
				
				customerVisitList.add(customerVisitBean);

			}

		} catch (SQLException sqlException) {
			customerVisitList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				logger.error("error",exception);
			}
		}
		return customerVisitList;	
	}
	/**
	 * END GET_CUSTOMER_VISIT_RECORD_LIST_FILTERED
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN GET_CUSTOMER_WORK_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @return
	 */
	protected static ArrayList<CustomerWorkBean> getCustWorkRecordListFiltered(Connection connection, Integer customerWorkID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<CustomerWorkBean> customerWorkList = new ArrayList<CustomerWorkBean>();
		CustomerWorkBean customerWorkBean = null;

		StringBuffer query = new StringBuffer();

		query.append("SELECT "); 
		query.append("	CUST_WORK_ID, ");
		query.append("	CUSTOMER_ID, ");
		query.append("	COMPANY_NAME, ");
		query.append("	EMAIL_ADDR, ");
		query.append("	WEBSITE_ADDR, ");//5
		query.append("	TWITTER_ADDR, ");
		query.append("	FACEBOOK_ADDR, ");
		query.append("	WHATSUP_ADDR, ");
		query.append("	LINKEDIN_ADDR, ");
		query.append("	ADDTO_MOURSEL, ");//10
		query.append("	ADDTO_FAMILY, ");
		query.append("	ADDTO_GROUP, ");
		query.append("	IS_CURRENT_WORK "); 	
		query.append("FROM CUST_WORK_INFO ");
		query.append("WHERE 1=1 ");
		
		if(customerWorkID!=null && customerWorkID>0)
			query.append("	AND CUST_WORK_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("3"))
				query.append("UPPER(COMPANY_NAME) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(customerWorkID!=null && customerWorkID>0)
				preparedStatement.setInt(counter++, customerWorkID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			resultSet = preparedStatement.executeQuery();


			CustomerBean customerBean=null;
			while (resultSet.next()) {
				customerWorkBean = new CustomerWorkBean();

				customerWorkBean.setCustWorkID(resultSet.getString("USER_NAME"));
				
				customerBean = new CustomerBean();
				customerBean.setCustomerID(resultSet.getString("BRANCH_ID"));
				customerWorkBean.setCustomerBean(customerBean);
				
				customerWorkBean.setCompanyName(resultSet.getString("USER_NAME"));
				customerWorkBean.setEmailAddr(resultSet.getString("USER_NAME"));
				customerWorkBean.setWebsiteAddr(resultSet.getString("USER_NAME"));
				customerWorkBean.setTwitterAddr(resultSet.getString("USER_NAME"));
				customerWorkBean.setFacebookAddr(resultSet.getString("USER_NAME"));
				customerWorkBean.setWhatsupAddr(resultSet.getString("USER_NAME"));
				customerWorkBean.setLinkedInAddr(resultSet.getString("USER_NAME"));				
				customerWorkBean.setAddToMoursel(resultSet.getString("USER_NAME"));
				customerWorkBean.setAddToFamily(resultSet.getString("USER_ID"));
				customerWorkBean.setAddToGroup(resultSet.getString("USER_NAME"));				
				customerWorkBean.setCurrentWork(resultSet.getBoolean("USER_NAME"));
								
				customerWorkList.add(customerWorkBean);

			}

		} catch (SQLException sqlException) {
			customerWorkList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				logger.error("error",exception);
			}
		}
		return customerWorkList;	
	}
	/**
	 * END GET_CUSTOMER_WORK_RECORD_LIST_FILTERED
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN GET_PERSON_TITLE_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @param personTitleID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	protected static ArrayList<PersonTitleBean> getPersonTitleRecordListFiltered(Connection connection, Integer personTitleID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<PersonTitleBean> personTitleList = new ArrayList<PersonTitleBean>();
		PersonTitleBean personTitleBean = null;

		StringBuffer query = new StringBuffer();


		query.append("SELECT "); 
		query.append("	TITLE_ID, ");
		query.append("	TITLE_CODE, ");
		query.append("	TITLE_DESC ");
		query.append("FROM PERSON_TITLE ");
		query.append("WHERE 1=1 ");
		
		if(personTitleID!=null && personTitleID>0)
			query.append("	AND TITLE_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("2"))
				query.append("UPPER(TITLE_CODE) ");
			else if(sort.equals("3"))
				query.append("UPPER(TITLE_DESC) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(personTitleID!=null && personTitleID>0)
				preparedStatement.setInt(counter++, personTitleID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			resultSet = preparedStatement.executeQuery();


			while (resultSet.next()) {
				personTitleBean = new PersonTitleBean();

				personTitleBean.setPersonTitleID(resultSet.getString("TITLE_ID"));
				personTitleBean.setPersonTitleCode(resultSet.getString("TITLE_CODE"));
				personTitleBean.setPersonTitleDesc(resultSet.getString("TITLE_DESC"));
								
				personTitleList.add(personTitleBean);

			}

		} catch (SQLException sqlException) {
			personTitleList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
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
	 * BEGIN GET_PROFILE_CATEGORY_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @param personTitleID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	protected static ArrayList<RatingBean> getRatingRecordListFiltered(Connection connection, Integer ratingID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<RatingBean> ratingList = new ArrayList<RatingBean>();
		RatingBean ratingBean = null;

		StringBuffer query = new StringBuffer();

		query.append("SELECT "); 
		query.append("	RATING_ID, ");
		query.append("	RATING_CODE, ");
		query.append("	RATING_DESC ");
		query.append("FROM RATING ");
		query.append("WHERE 1=1 ");
		
		if(ratingID!=null && ratingID>0)
			query.append("	AND RATING_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("2"))
				query.append("UPPER(RATING_CODE) ");
			else if(sort.equals("3"))
				query.append("UPPER(RATING_DESC) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(ratingID!=null && ratingID>0)
				preparedStatement.setInt(counter++, ratingID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			resultSet = preparedStatement.executeQuery();


			while (resultSet.next()) {
				ratingBean = new RatingBean();

				ratingBean.setRatingID(resultSet.getString("RATING_ID"));
				ratingBean.setRatingCode(resultSet.getString("RATING_CODE"));
				ratingBean.setRatingDesc(resultSet.getString("RATING_DESC"));
								
				ratingList.add(ratingBean);

			}

		} catch (SQLException sqlException) {
			ratingList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				logger.error("error",exception);
			}
		}
		return ratingList;	
	}
	/**
	 * END GET_PROFILE_CATEGORY_RECORD_LIST_FILTERED
	 * 
	 * 
	 *
	 *
	 * 
	 * 
	 * BEGIN GET_PROFESSIONAL_FIELD_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @param personTitleID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	protected static ArrayList<ProfessionalFieldBean> getProfessionalFieldRecordListFiltered(Connection connection, Integer professionalFieldID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<ProfessionalFieldBean> professionalFieldList = new ArrayList<ProfessionalFieldBean>();
		ProfessionalFieldBean professionalFieldBean = null;

		StringBuffer query = new StringBuffer();

		
		query.append("SELECT "); 
		query.append("	PRO_FIELD_ID, ");
		query.append("	PRO_FIELD_CODE, ");
		query.append("	PRO_FIELD_DESC ");
		query.append("FROM PROFESSIONAL_FIELD ");
		query.append("WHERE 1=1 ");
		
		if(professionalFieldID!=null && professionalFieldID>0)
			query.append("	AND PRO_FIELD_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("3"))
				query.append("UPPER(PRO_FIELD_CODE) ");
			else if(sort.equals("4"))
				query.append("UPPER(PRO_FIELD_DESC) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(professionalFieldID!=null && professionalFieldID>0)
				preparedStatement.setInt(counter++, professionalFieldID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			resultSet = preparedStatement.executeQuery();


			while (resultSet.next()) {
				professionalFieldBean = new ProfessionalFieldBean();

				professionalFieldBean.setProFieldID(resultSet.getString("PRO_FIELD_ID"));
			
				professionalFieldBean.setProFieldCode(resultSet.getString("PRO_FIELD_CODE"));
				professionalFieldBean.setProFieldDesc(resultSet.getString("PRO_FIELD_DESC"));
								
				professionalFieldList.add(professionalFieldBean);

			}

		} catch (SQLException sqlException) {
			professionalFieldList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
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
	 * @param connection
	 * @param personTitleID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	protected static ArrayList<ProfessionBean> getProfessionRecordListFiltered(Connection connection, Integer professionID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<ProfessionBean> professionList = new ArrayList<ProfessionBean>();
		ProfessionBean professionBean = null;

		StringBuffer query = new StringBuffer();


		query.append("SELECT "); 
		query.append("	PROFESSION_ID, ");
		query.append("	PF.PRO_FIELD_ID, ");
		query.append("	PF.PRO_FIELD_CODE, ");
		query.append("	PROFESSION_CODE, ");
		query.append("	PROFESSION_DESC ");//5
		query.append("FROM PROFESSION, PROFESSIONAL_FIELD PF ");
		query.append("WHERE 1=1 ");
		query.append("	AND PROFESSION.PRO_FIELD_ID=PF.PRO_FIELD_ID ");
		
		if(professionID!=null && professionID>0)
			query.append("	AND PROFESSION_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("4"))
				query.append("UPPER(PROFESSION_CODE) ");
			else if(sort.equals("5"))
				query.append("UPPER(PROFESSION_DESC) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(professionID!=null && professionID>0)
				preparedStatement.setInt(counter++, professionID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			resultSet = preparedStatement.executeQuery();

			ProfessionalFieldBean professionalFieldBean = null;
			while (resultSet.next()) {
				professionBean = new ProfessionBean();

				professionBean.setProfessionID(resultSet.getString("PROFESSION_ID"));

				professionalFieldBean = new ProfessionalFieldBean();
				professionalFieldBean.setProFieldID(resultSet.getString("PRO_FIELD_ID"));
				professionalFieldBean.setProFieldCode(resultSet.getString("PRO_FIELD_CODE"));
				professionBean.setProFieldBean(professionalFieldBean);
				
				professionBean.setProfessionCode(resultSet.getString("PROFESSION_CODE"));
				professionBean.setProfessionDesc(resultSet.getString("PROFESSION_DESC"));
								
				professionList.add(professionBean);

			}

		} catch (SQLException sqlException) {
			professionList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
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
	 * 
	 *  
	 * 
	 * GET_BRANCH_RECORD_LIST_FILTERED      
	 * 
	 * @param connection
	 * @param personTitleID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	protected static ArrayList<BranchBean> getBranchRecordListFiltered(Connection connection, Integer branchID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<BranchBean> branchList = new ArrayList<BranchBean>();
		BranchBean branchBean = null;

		StringBuffer query = new StringBuffer();


		query.append("SELECT "); 
		query.append("	BRANCH_ID, ");
		query.append("	PHONE_ID, ");
		query.append("	BRANCH_CODE, ");
		query.append("	BRANCH_DESC, ");
		query.append("	ADDRESS_ID, ");
		query.append("	NOTE ");
		query.append("FROM BRANCH ");
		query.append("WHERE 1=1 ");
		
		if(branchID!=null && branchID>0)
			query.append("	AND BRANCH_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("3"))
				query.append("UPPER(BRANCH_CODE) ");
			else if(sort.equals("4"))
				query.append("UPPER(BRANCH_DESC) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(branchID!=null && branchID>0)
				preparedStatement.setInt(counter++, branchID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			resultSet = preparedStatement.executeQuery();

			PhoneBean phoneBean=null;
			AddressBean addressBean=null;		
			while (resultSet.next()) {
				branchBean = new BranchBean();

				branchBean.setBranchID(resultSet.getString("BRANCH_ID"));
				
				phoneBean = new PhoneBean();
				phoneBean.setPhoneID(resultSet.getString("PHONE_ID"));
				branchBean.setPhoneBean(phoneBean);
				
				branchBean.setBranchCode(resultSet.getString("BRANCH_CODE"));
				branchBean.setBranchDesc(resultSet.getString("BRANCH_DESC"));
				
				addressBean = new AddressBean();
				addressBean.setAddressID(resultSet.getString("ADDRESS_ID"));
				branchBean.setAddressBean(addressBean);
				
				branchBean.setNote(resultSet.getString("NOTE"));
				
								
				branchList.add(branchBean);

			}

		} catch (SQLException sqlException) {
			branchList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
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
	 * GET_COUNTRY_RECORD_LIST_FILTERED      
	 * 
	 * @param connection
	 * @param personTitleID
	 * @param criteriaType
	 * @param criteriaValue
	 * @param sort
	 * @param direction
	 * @return
	 */
	protected static ArrayList<CountryBean> getCountryRecordListFiltered(Connection connection, Integer countryID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<CountryBean> countryList = new ArrayList<CountryBean>();
		CountryBean countryBean = null;

		StringBuffer query = new StringBuffer();

		query.append("SELECT "); 
		query.append("	COUNTRY_ID, ");
		query.append("	COUNTRY_CODE, ");
		query.append("	COUNTRY_DESC ");
		query.append("FROM COUNTRY ");
		query.append("WHERE 1=1 ");
		
		if(countryID!=null && countryID>0)
			query.append("	AND COUNTRY_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("2"))
				query.append("UPPER(COUNTRY_CODE) ");
			else if(sort.equals("3"))
				query.append("UPPER(COUNTRY_DESC) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(countryID!=null && countryID>0)
				preparedStatement.setInt(counter++, countryID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				countryBean = new CountryBean();

				countryBean.setCountryID(resultSet.getString("COUNTRY_ID"));
				countryBean.setCountryCode(resultSet.getString("COUNTRY_CODE"));
				countryBean.setCountryDesc(resultSet.getString("COUNTRY_DESC"));
								
				countryList.add(countryBean);

			}

		} catch (SQLException sqlException) {
			countryList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
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
	 * @param connection
	 * @return
	 */
	protected static ArrayList<AddressTypeBean> getAddressTypeRecordListFiltered(Connection connection, Integer addressTypeID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<AddressTypeBean> addressTypeList = new ArrayList<AddressTypeBean>();
		AddressTypeBean addressTypeBean = null;

		StringBuffer query = new StringBuffer();



		query.append("SELECT ");
		query.append("	ADDRESS_TYPE_ID, ");
		query.append("	ADDRESS_TYPE_CODE, ");
		query.append("	ADDRESS_TYPE_DESC ");
		query.append("FROM ADDRESS_TYPE ");
		query.append("WHERE 1=1 ");
		
		if(addressTypeID!=null && addressTypeID>0)
			query.append("	AND ADDRESS_TYPE_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("2"))
				query.append("UPPER(ADDRESS_TYPE_CODE) ");
			else if(sort.equals("3"))
				query.append("UPPER(ADDRESS_TYPE_DESC) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(addressTypeID!=null && addressTypeID>0)
				preparedStatement.setInt(counter++, addressTypeID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			
			resultSet = preparedStatement.executeQuery();


			while (resultSet.next()) {
				addressTypeBean = new AddressTypeBean();

				addressTypeBean.setAddrTypeID(resultSet.getString("ADDRESS_TYPE_ID"));
				addressTypeBean.setAddrTypeCode(resultSet.getString("ADDRESS_TYPE_CODE"));
				addressTypeBean.setAddrTypeDesc(resultSet.getString("ADDRESS_TYPE_DESC"));

				addressTypeList.add(addressTypeBean);
			}

		} catch (SQLException sqlException) {
			addressTypeList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
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
	 * BEGIN GET_VISIT_TYPE_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @return
	 */
	protected static ArrayList<VisitTypeBean> getVisitTypeRecordListFiltered(Connection connection, Integer visitTypeID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<VisitTypeBean> visitTypeList = new ArrayList<VisitTypeBean>();
		VisitTypeBean visitTypeBean = null;

		StringBuffer query = new StringBuffer();

		
		query.append("SELECT ");
		query.append("	VISIT_TYPE_ID, ");
		query.append("	VISIT_TYPE_CODE, ");
		query.append("	VISIT_TYPE_DESC ");
		query.append("FROM VISIT_TYPE ");
		query.append("WHERE 1=1 ");
		
		if(visitTypeID!=null && visitTypeID>0)
			query.append("	AND VISIT_TYPE_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("2"))
				query.append("UPPER(VISIT_TYPE_CODE) ");
			else if(sort.equals("3"))
				query.append("UPPER(VISIT_TYPE_DESC) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(visitTypeID!=null && visitTypeID>0)
				preparedStatement.setInt(counter++, visitTypeID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			
			resultSet = preparedStatement.executeQuery();


			while (resultSet.next()) {
				visitTypeBean = new VisitTypeBean();

				visitTypeBean.setVisitTypeID(resultSet.getString("VISIT_TYPE_ID"));
				visitTypeBean.setVisitTypeCode(resultSet.getString("VISIT_TYPE_CODE"));
				visitTypeBean.setVisitTypeDesc(resultSet.getString("VISIT_TYPE_DESC"));

				visitTypeList.add(visitTypeBean);
			}

		} catch (SQLException sqlException) {
			visitTypeList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				logger.error("error",exception);
			}
		}
		return visitTypeList;	
	}
	/**
	 * END GET_CUSTOMER_TYPE_RECORD_LIST_FILTERED
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BEGIN GET_PHONE_TYPE_RECORD_LIST_FILTERED
	 * 
	 * @param connection
	 * @return
	 */
	protected static ArrayList<PhoneTypeBean> getPhoneTypeRecordListFiltered(Connection connection, Integer phoneTypeID, String criteriaType, String criteriaValue,
								String sort, String direction){

		PreparedStatement preparedStatement = null;
		ResultSet resultSet= null;
		ArrayList<PhoneTypeBean> phoneTypeList = new ArrayList<PhoneTypeBean>();
		PhoneTypeBean phoneTypeBean = null;

		StringBuffer query = new StringBuffer();

		

		query.append("SELECT ");
		query.append("	PHONE_TYPE_ID, ");
		query.append("	PHONE_TYPE_CODE, ");
		query.append("	PHONE_TYPE_DESC ");
		query.append("FROM PHONE_TYPE ");
		query.append("WHERE 1=1 ");
		
		if(phoneTypeID!=null && phoneTypeID>0)
			query.append("	AND PHONE_TYPE_ID=? ");
			

		if(!StringUtil.nullToEmpty(criteriaType).equals(""))
			query.append(" AND UPPER (" + criteriaType +") LIKE UPPER(?) ");


		if( !StringUtil.nullToEmpty(sort).equals("") ){
			query.append("ORDER BY ");

			if(sort.equals("2"))
				query.append("UPPER(PHONE_TYPE_CODE) ");
			else if(sort.equals("3"))
				query.append("UPPER(PHONE_TYPE_DESC) ");
			else
				query.append(sort);
		}


		if( !StringUtil.nullToEmpty(sort).equals("") && !StringUtil.nullToEmpty(direction).equals("") ){
			query.append(" ");
			query.append(direction);
			query.append(" ");

			if(direction.equals("asc")){
				query.append(" NULLS FIRST");
			} else{
				query.append(" NULLS LAST");
			}
		}
		
		
		
		
		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			if(phoneTypeID!=null && phoneTypeID>0)
				preparedStatement.setInt(counter++, phoneTypeID);
			
			if(!StringUtil.nullToEmpty(criteriaType).equals(""))
				preparedStatement.setString(counter++, "%"+criteriaValue+"%");
			
			
			resultSet = preparedStatement.executeQuery();


			while (resultSet.next()) {
				phoneTypeBean = new PhoneTypeBean();

				phoneTypeBean.setPhoneTypeID(resultSet.getString("PHONE_TYPE_ID"));
				phoneTypeBean.setPhoneTypeCode(resultSet.getString("PHONE_TYPE_CODE"));
				phoneTypeBean.setPhoneTypeDesc(resultSet.getString("PHONE_TYPE_DESC"));

				phoneTypeList.add(phoneTypeBean);
			}

		} catch (SQLException sqlException) {
			phoneTypeList=null;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
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
	 * BEGIN UPDATE_RATING_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updateRatingRecord(Connection connection, RatingVO ratingVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();

		
		query.append("UPDATE RATING SET ");		
		query.append("	RATING_CODE=?, ");
		query.append("	RATING_DESC=? ");
		query.append("WHERE 1=1 ");
		query.append("	AND RATING_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			
			preparedStatement.setString(counter++, ratingVO.getRatingCode());
			preparedStatement.setString(counter++, ratingVO.getRatingDesc());
			
			preparedStatement.setInt(counter++, ratingVO.getRatingID());

			
			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;		
	}
	/**
	 * END UPDATE_RATING_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_RATING_RECORD
	 * 
	 * @param connection
	 * @param customerTypeID
	 * @return
	 */
	protected static int removeRatingRecord(Connection connection, Integer ratingID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();

		query.append("DELETE FROM RATING ");
		query.append("WHERE RATING_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, ratingID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;	
	}
	/** 
	 * END REMOVE_RATING_RECORD
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN UPDATE_PROFESSIONAL_FIELD_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updateProfessionalFieldRecord(Connection connection, ProfessionalFieldVO professionalFieldVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();


		query.append("UPDATE PROFESSIONAL_FIELD SET ");		
		query.append("	PRO_FIELD_CODE=?, ");
		query.append("	PRO_FIELD_DESC=? ");
		query.append("WHERE 1=1 ");
		query.append("	AND PRO_FIELD_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			
			preparedStatement.setString(counter++, professionalFieldVO.getProFieldCode());
			preparedStatement.setString(counter++, professionalFieldVO.getProFieldDesc());
			
			preparedStatement.setInt(counter++, professionalFieldVO.getProFieldID());

			
			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;		
	}
	/**
	 * END UPDATE_PROFESSIONAL_FIELD_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_PROFESSIONAL_FIELD_RECORD
	 * 
	 * @param connection
	 * @param customerTypeID
	 * @return
	 */
	protected static int removeProfessionalFieldRecord(Connection connection, Integer professionalFieldID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();

		
		query.append("DELETE FROM PROFESSIONAL_FIELD ");
		query.append("WHERE PRO_FIELD_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, professionalFieldID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;	
	}
	/** 
	 * END REMOVE_PROFESSIONAL_FIELD_RECORD
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN UPDATE_PROFESSION_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updateProfessionRecord(Connection connection, ProfessionVO professionVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();


		query.append("UPDATE PROFESSION SET ");		
		query.append("	PRO_FIELD_ID=?, ");
		query.append("	PROFESSION_CODE=?, ");
		query.append("	PROFESSION_DESC=? ");
		query.append("WHERE 1=1 ");
		query.append("	AND PROFESSION_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			
			preparedStatement.setInt(counter++, professionVO.getProFieldID());
			preparedStatement.setString(counter++, professionVO.getProfessionCode());
			preparedStatement.setString(counter++, professionVO.getProfessionDesc());
			
			preparedStatement.setInt(counter++, professionVO.getProfessionID());

			
			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;	
	}
	/**
	 * END UPDATE_PROFESSION_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_PROFESSION_RECORD
	 * 
	 * @param connection
	 * @param customerTypeID
	 * @return
	 */
	protected static int removeProfessionRecord(Connection connection, Integer professionID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();


		query.append("DELETE FROM PROFESSION ");
		query.append("WHERE PROFESSION_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, professionID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;	
	}
	/** 
	 * END REMOVE_PROFESSION_RECORD
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN UPDATE_ADDRESS_TYPE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updateAddressTypeRecord(Connection connection, AddressTypeVO addressTypeVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();


		query.append("UPDATE ADDRESS_TYPE SET ");		
		query.append("	ADDRESS_TYPE_CODE=?, ");
		query.append("	ADDRESS_TYPE_DESC=? ");
		query.append("WHERE 1=1 ");
		query.append("	AND ADDRESS_TYPE_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			
			preparedStatement.setString(counter++, addressTypeVO.getAddrTypeCode());
			preparedStatement.setString(counter++, addressTypeVO.getAddrTypeDesc());
			
			preparedStatement.setInt(counter++, addressTypeVO.getAddrTypeID());

			
			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;	
	}
	/**
	 * END UPDATE_ADDRESS_TYPE_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_ADDRESS_TYPE_RECORD
	 * 
	 * @param connection
	 * @param customerTypeID
	 * @return
	 */
	protected static int removeAddressTypeRecord(Connection connection, Integer addressTypeID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();


		query.append("DELETE FROM ADDRESS_TYPE ");
		query.append("WHERE ADDRESS_TYPE_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, addressTypeID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;		
	}
	/** 
	 * END REMOVE_ADDRESS_TYPE_RECORD
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN UPDATE_PHONE_TYPE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updatePhoneTypeRecord(Connection connection, PhoneTypeVO phoneTypeVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();


		query.append("UPDATE PHONE_TYPE SET ");		
		query.append("	PHONE_TYPE_CODE=?, ");
		query.append("	PHONE_TYPE_DESC=? ");
		query.append("WHERE 1=1 ");
		query.append("	AND PHONE_TYPE_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			
			preparedStatement.setString(counter++, phoneTypeVO.getPhoneTypeCode());
			preparedStatement.setString(counter++, phoneTypeVO.getPhoneTypeDesc());
			
			preparedStatement.setInt(counter++, phoneTypeVO.getPhoneTypeID());

			
			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;		
	}
	/**
	 * END UPDATE_PHONE_TYPE_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_PHONE_TYPE_RECORD
	 * 
	 * @param connection
	 * @param customerTypeID
	 * @return
	 */
	protected static int removePhoneTypeRecord(Connection connection, Integer phoneTypeID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();


		query.append("DELETE FROM PHONE_TYPE ");
		query.append("WHERE PHONE_TYPE_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, phoneTypeID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;		
	}
	/** 
	 * END REMOVE_PHONE_TYPE_RECORD
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN UPDATE_VISIT_TYPE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updateVisitTypeRecord(Connection connection, VisitTypeVO visitTypeVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();

		
		query.append("UPDATE VISIT_TYPE SET ");		
		query.append("	VISIT_TYPE_CODE=?, ");
		query.append("	VISIT_TYPE_DESC=? ");
		query.append("WHERE 1=1 ");
		query.append("	AND VISIT_TYPE_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			
			preparedStatement.setString(counter++, visitTypeVO.getVisitTypeCode());
			preparedStatement.setString(counter++, visitTypeVO.getVisitTypeDesc());
			
			preparedStatement.setInt(counter++, visitTypeVO.getVisitTypeID());

			
			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;	
	}
	/**
	 * END UPDATE_VISIT_TYPE_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_VISIT_TYPE_RECORD
	 * 
	 * @param connection
	 * @param customerTypeID
	 * @return
	 */
	protected static int removeVisitTypeRecord(Connection connection, Integer visitTypeID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();


		query.append("DELETE FROM VISIT_TYPE ");
		query.append("WHERE VISIT_TYPE_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, visitTypeID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;		
	}
	/** 
	 * END REMOVE_CUSTOMER_TYPE_RECORD
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * BEGIN UPDATE_PERSON_TITLE_RECORD
	 * 
	 * @param connection
	 * @param userVO
	 * @return
	 */
	protected static int updatePersonTitleRecord(Connection connection, PersonTitleVO personTitleVO){

		PreparedStatement preparedStatement = null;
		int updated = 0;

		StringBuffer query = new StringBuffer();


		query.append("UPDATE PERSON_TITLE SET ");		
		query.append("	TITLE_CODE=?, ");
		query.append("	TITLE_DESC=? ");
		query.append("WHERE 1=1 ");
		query.append("	AND TITLE_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			
			preparedStatement.setString(counter++, personTitleVO.getPersonTitleCode());
			preparedStatement.setString(counter++, personTitleVO.getPersonTitleDesc());
			
			preparedStatement.setInt(counter++, personTitleVO.getPersonTitleID());

			
			updated = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			updated=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				updated=0;
				logger.error("error",exception);
			}
		}
		return updated;	
	}
	/**
	 * END UPDATE_PERSON_TITLE_RECORD
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * BEGIN REMOVE_PERSON_TITLE_RECORD
	 * 
	 * @param connection
	 * @param customerTypeID
	 * @return
	 */
	protected static int removePersonTitleRecord(Connection connection, Integer personTitleID){

		PreparedStatement preparedStatement = null;
		int removed = 0;

		StringBuffer query = new StringBuffer();


		query.append("DELETE FROM CUSTOMER_TYPE ");
		query.append("WHERE CUST_TYPE_ID=? ");


		int counter=1;
		try {
			preparedStatement = connection.prepareStatement(new String(query));
			preparedStatement.setInt(counter++, personTitleID);

			removed = preparedStatement.executeUpdate();

		} catch (SQLException sqlException) {
			removed=0;
			logger.error("error",sqlException);
		} finally {
			query = null;
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception exception) {
				removed=0;
				logger.error("error",exception);
			}
		}
		return removed;		
	}
	/** 
	 * END REMOVE_PERSON_TITLE_RECORD
	 * 
	 * 
	 * 
	 */
}
