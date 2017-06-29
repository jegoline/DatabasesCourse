CREATE TABLE product_dim (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
  article_name varchar(255),
  group_name varchar(255),
  family_name varchar(255),
  category_name varchar(255));
  
CREATE TABLE shop_dim (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
  shop_name varchar(255),
  stadt_name varchar(255),
  region_name varchar(255),
  land_name varchar(255));
  
  
 CREATE TABLE sales (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
   article_id INTEGER,
   shop_id INTEGER,
   amount INTEGER);
  