 CREATE TABLE estate_agent(
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
   name varchar(255),
   address varchar(255),
   login varchar(40) NOT NULL UNIQUE,
   password varchar(40));

 CREATE TABLE estate (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
  city varchar(255),
  postal_code varchar(255),
  street varchar(255),
  street_number varchar (20),
  square_area double);

 CREATE TABLE manages (
 	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
 	estate_id integer,
 	estate_agent_id integer,
 	foreign key (estate_id) references estate(id),
	foreign key (estate_agent_id) references estate_agent(id)
 );
 
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


Create table person (
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY, 
	first_name VARCHAR(255),
	name VARCHAR(255),
	address VARCHAR(255)
);


Create table sells (
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY, 
	fk_house_id INTEGER NOT NULL UNIQUE,
	fk_person_id INTEGER NOT NULL,
	FOREIGN KEY (fk_house_id) REFERENCES house(id)
	ON DELETE CASCADE,
	FOREIGN KEY (fk_person_id) REFERENCES person(id)
	ON DELETE CASCADE
);


Create table rents (
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
	fk_apartment_id INTEGER NOT NULL UNIQUE,
	fk_person_id INTEGER NOT NULL,
	FOREIGN KEY(fk_apartment_id) REFERENCES apartment(id)
	ON DELETE CASCADE,
	FOREIGN KEY (fk_person_id) REFERENCES person(id)
	ON DELETE CASCADE
);

Create table purchase_contract (
	fk_id INTEGER NOT NULL UNIQUE,
	contract_no INTEGER NOT NULL UNIQUE,
	date DATE,
	place VARCHAR(255),
	no_of_installments INTEGER,
	interest_rate DOUBLE,
	FOREIGN KEY(fk_id) REFERENCES sells(id)
	ON DELETE CASCADE
);

Create table tenancy_contract (
	fk_id INTEGER NOT NULL UNIQUE,
	contract_no INTEGER NOT NULL UNIQUE,
	date DATE,
	place VARCHAR(255),
	start_date DATE,
	duration BIGINT,
	additional_costs DOUBLE,
	FOREIGN KEY(fk_id) REFERENCES rents(id)
	ON DELETE CASCADE
);

