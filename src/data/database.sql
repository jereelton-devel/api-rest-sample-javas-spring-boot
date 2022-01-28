
/*TABLE CUSTOMERS*/

DROP TABLE IF EXISTS `api-rest-sample-javas-spring-boot`.customers;

CREATE TABLE `api-rest-sample-javas-spring-boot`.customers (
	id varchar(100) NOT NULL UNIQUE,
	name varchar(200) NOT NULL,
	active varchar(10) NOT NULL,
	PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `api-rest-sample-javas-spring-boot`.address;

/*TABLE ADDRESS*/

CREATE TABLE `api-rest-sample-javas-spring-boot`.address (
	id varchar(100) NOT NULL UNIQUE,
	customer_id varchar(100) NOT NULL,
	street varchar(100) NOT NULL,
	num int NOT NULL,
	district varchar(100) NOT NULL,
	city varchar(100) NOT NULL,
	country varchar(100) NOT NULL,
	zipcode varchar(100) NOT NULL,
	PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

