
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


/*INSERTS*/

INSERT INTO `api-rest-sample-javas-spring-boot`.customers (id, name, active)
VALUES('226952c5582d9245ebd2d4d17da711e8', 'Lucas Leite', 'yes');

INSERT INTO `api-rest-sample-javas-spring-boot`.customers (id, name, active)
VALUES('27403613e6b8fdc8d58768d7bfd9ae44', 'Jose Antonio', 'yes');

INSERT INTO `api-rest-sample-javas-spring-boot`.customers (id, name, active)
VALUES('744523e0aad5d46ef0cee63e74e951f4', 'Manoel Nobrega', 'yes');

INSERT INTO `api-rest-sample-javas-spring-boot`.customers (id, name, active)
VALUES('7b2ec13340b3a637e359b966405bcc97', 'Marcos Silva', 'no');

INSERT INTO `api-rest-sample-javas-spring-boot`.customers (id, name, active)
VALUES('94226203b5d5dc5d6503bec5dd125497', 'Joseane Sobral', 'no');

INSERT INTO `api-rest-sample-javas-spring-boot`.customers (id, name, active)
VALUES('97befb51c3240e10119f4e80b3a05b71', 'Joel Barbosa', 'yes');

INSERT INTO `api-rest-sample-javas-spring-boot`.customers (id, name, active)
VALUES('b652cc5167c60228d4f0c329df5a4e6f', 'Amanda Oliveira', 'no');

INSERT INTO `api-rest-sample-javas-spring-boot`.customers (id, name, active)
VALUES('e00507c48439d8dae2fb2e31326cc356', 'Samanta Nunes', 'no');

INSERT INTO `api-rest-sample-javas-spring-boot`.customers (id, name, active)
VALUES('fc26c1192d4d2c054dfbb87be05c4b5d', 'Mariana Santos', 'no');





