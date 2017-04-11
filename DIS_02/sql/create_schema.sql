 CREATE TABLE estate_agent(
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
   name varchar(255),
   address varchar(255),
   login varchar(40) NOT NULL UNIQUE,
   password varchar(40));

   drop table estate;

 CREATE TABLE estate (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
  city varchar(255),
  postal_code varchar(255),
  street varchar(255),
  street_number varchar (20),
  square_area double);

  
drop table manages;
 CREATE TABLE manages (
 	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
 	estate_id integer,
 	estate_agent_id integer,
 	foreign key (estate_id) references estate(id),
	foreign key (estate_agent_id) references estate_agent(id)
 );

 drop table house;
 
 CREATE TABLE house (
  id integer not null primary key,
  floors integer,
  price DECIMAL(10, 2),
  garden char(1),
  foreign key (id) references estate(id));

  CREATE TABLE apartment(
  id integer not null primary key,
  floor integer,
  rent DECIMAL(10, 2),
  rooms double,
  balcony integer,
  builtin_kitchen char(1),
  foreign key (id) references estate(id));

 Create table purchase_contract (
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
	contract_no INTEGER NOT NULL UNIQUE,
	date DATE,
	place VARCHAR(255),
	no_of_installments INTEGER,
	interest_rate DOUBLE
);

Create table tenancy_contract (
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
	contract_no Integer NOT NULL UNIQUE,
	date DATE,
	place VARCHAR(255),
	start_date DATE,
	duration BIGINT
);


Create table person (
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY, 
	first_name VARCHAR(255),
	name VARCHAR(255),
	address VARCHAR(255)
);


Create table sells (
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY, 
	house_id integer,
	purchase_contract_id integer,
	person_id integer,
	foreign key (house_id) references house(id),
	foreign key (purchase_contract_id) references purchase_contract(id),
	foreign key (person_id) references person(id)
);


select * from tenancy_contract;

Create table rent (
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY, 
	appartment_id integer,
	tenancy_contract_id integer,
	person_id integer,
	foreign key (appartment_id) references apartment(id),
	foreign key (tenancy_contract_id) references tenancy_contract(id),
	foreign key (person_id) references person(id)
);