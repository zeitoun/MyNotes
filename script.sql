create table FUEL 
(
	REFUEL_ID			INTEGER					 not null,
   	REFUEL_DATE 	     timestamp                      not null,
   	AMOUNT    		numeric(20,2)                  null,
	LITERS    		numeric(20,2)                  null,
   	RUNED_MILES    	numeric(20,2)                  null,
   	RUNED_KM    		numeric(20,2)                  null,
   	ACTUAL_PRICE		numeric(20,2)				null,
   	FUEL_TYPE			varchar(20)				null,
   	FUEL_STATION_NAME	VARCHAR(20)				null,
   	IS_PAID			char(1)					null,
   	CAR_MODEL			VARCHAR(20)				null,
   	
   	constraint PK_FUEL primary key (REFUEL_ID)
);


create table BOOK 
(
	BOOK_ID			INTEGER				not null,
   	BOOK_NAME 	     VARCHAR(100)			null,
   	AUTHOR    		VARCHAR(100)			null,
	ISBN10    		VARCHAR(20)			null,
   	ISBN13    		VARCHAR(20)			null,
   	GENDER    		VARCHAR(50)			null,
   	SUBJECT			VARCHAR(100)			null,
   	IS_READ			char(1)				null,
   	RATING10			numeric(2)			null,			
   	RATING_NOTE		varchar(100)			null,
   	NO_OF_PAGES		numeric(5)			null,
   	DATE_ADDED		timestamp				null,
   	
   	constraint PK_BOOK primary key (BOOK_ID)
);


create table HOME_RENTAL 
(
	RENTAL_ID				INTEGER				not null,
   	HOME_NAME 	     	VARCHAR(100)			null,
   	LOCATION    			VARCHAR(100)			null,
	MONTHLY_TOTAL_BILL_USD   NUMERIC(20,2)			null,
   	IS_DORM    			char(1)				null,
   	IS_HOME    			char(1)				null,
   	START_DATE			timestamp				null,
   	END_DATE				timestamp				null,
   	NO_OF_STAYING_MONTHS	numeric(5)			null,
   	TOTAL_PAID_AMOUNT_USD	numeric(20,2)			null,
   	TOTAL_PAID_AMOUNT_LBP	numeric(20,2)			null,
   	
   	constraint PK_HOME_RENTAL primary key (RENTAL_ID)
);

create table NOTE
(
	NOTE_ID			INTEGER					 not null,
   	ADDED_DATE 	     timestamp                      not null,
   	UPDATED_DATE 	     timestamp                      not null,
   	CATEGORY    		VARCHAR(20)                  null,
	SHORT_DESC    		VARCHAR(40)                  null,
   	LONG_DESC    		VARCHAR(200)                  null,
   	
   	constraint PK_NOTE primary key (NOTE_ID)
);

create table CREDENTIAL
(
	CRED_ID				INTEGER					 not null,
	CRED_DESC				VARCHAR(20)				null,
	DATE_ADDED 	     	timestamp                      null,
	DATE_UPDATED 	     	timestamp                      null,
   	CRED_ACCOUNT 	     	VARCHAR(30)                      null,
   	CRED_ACCOUNT_PASS	     VARCHAR(30)                      null,
   	CRED_ACCOUNT_PASS2	     VARCHAR(30)                      null,
   	CRED_LINK				VARCHAR(50) 	                 null,
   	NOTE    				VARCHAR(50) 	                 null,
   	
   	constraint PK_CREDENTIAL primary key (CRED_ID)
);

create table NEWS
(
	NEWS_ID				INTEGER					 not null,
	NEWS_DATE 	     	timestamp                      null,
	NEWS_DESC					VARCHAR(20)				null,
	NEWS_DETAILS 	     		VARCHAR(200)                      null,
   	NEWS_DATE_ADDED 	     	TIMESTAMP                      null,
   	
   	constraint PK_NEWS primary key (NEWS_ID)
);

create table ACHIEVEMENT
(
	ACHIEVEMENT_ID				INTEGER					 not null,
	ACHIEVMENT_DATE 	     	timestamp                      null,
	ACHV_DESC					VARCHAR(20)				null,
	ACHV_DETAILS 	     		VARCHAR(200)                      null,
   	ACHV_DATE_ADDED 	     	TIMESTAMP                      null,
   	
   	constraint PK_ACHIEVMENT primary key (ACHIEVEMENT_ID)
);

create table LIFE_EVENT
(
	LIFE_EVENT_ID				INTEGER					 not null,
	LIFE_EVENT_DATE 	     	timestamp                      null,
	LE_BRIEF_DESC				VARCHAR(20)				null,
	LE_DETAILS 	     	VARCHAR(200)                      null,
   	LE_DATE_ADDED 	     	TIMESTAMP                      null,
   	
   	constraint PK_LIFE_EVENT primary key (LIFE_EVENT_ID)
);

create table ASSET
(
	ASSET_ID				INTEGER					 not null,
	ASSET_TYPE 	     	VARCHAR(20)                      null,
	BRIEF_DESC				VARCHAR(20)				null,
	LONG_DESC					VARCHAR(50)				null,
	APPROX_PRICE 	     	NUMERIC(20,4)                      null,
   	CURRENCY 	     	VARCHAR(3)                      null,
   	DATE_ADDED			TIMESTAMP 				null,
   	DATE_MODIFIED			TIMESTAMP					null,
   	NOTE				VARCHAR(200) 				null,
   	
   	constraint PK_ASSET primary key (ASSET_ID)
);

create table CAR_LOG
(
	CAR_LOG_ID				INTEGER					 not null,
	EVENT_TYPE 	     	VARCHAR(20)                      null,
	MILEAGE				NUMERIC(10)				null,
	TOT_MILEAGE			NUMERIC(12,2)				null,
	EVENT_DATE			VARCHAR(50)				null,
	DESC1 	     	VARCHAR(200)                      null,
	DESC2 	     	VARCHAR(200)                      null,
	INVOICE_AMT			NUMERIC(12,2)  null,
   	
   	constraint PK_CAR_LOG primary key (CAR_LOG_ID)
);

create sequence CAR_LOG_SEQ
create sequence ACHIEVEMENT_SEQ
create sequence ASSET_SEQ
create sequence BOOK_SEQ
create sequence CREDENTIAL_SEQ
create sequence FUEL_SEQ
create sequence HOME_RENTAL_SEQ
create sequence LIFE_EVENT_SEQ
create sequence NEWS_SEQ
create sequence NOTE_SEQ 




SAMPLES
----------
INSERT INTO FUEL (REFUEL_ID,REFUEL_DATE,AMOUNT,LITERS,RUNED_MILES,RUNED_KM,ACTUAL_PRICE,FUEL_TYPE,FUEL_STATION_NAME,IS_PAID,CAR_MODEL) 
VALUES (FUEL_SEQ.nextval ,to_date('2014-01-29', 'yyyy-MM-dd'),86000,49,235,0,0,'95 oct','Mercedes Dawra','y','RAV4');

INSERT INTO "HOME"."ASSET" (ASSET_ID,ASSET_TYPE,BRIEF_DESC,LONG_DESC,APPROX_PRICE,CURRENCY,DATE_ADDED,DATE_MODIFIED,NOTE) 
VALUES (ASSET_SEQ.nextval,'home','livingroom','Living Room',3000000,'LBP',to_date('2014-03-16', 'yyyy-MM-dd'),to_date('2014-03-16', 'yyyy-MM-dd'),'s');