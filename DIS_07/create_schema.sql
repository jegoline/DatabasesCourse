drop table sales;
drop table time_dim;
drop table shop_dim;
drop table product_dim;
  
  
  CREATE TABLE product_dim (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
   article_name VARCHAR(255),
   group_name VARCHAR(255),
   family_name VARCHAR(255),
   category_name VARCHAR(255)
   );
  
  CREATE TABLE shop_dim (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
   shop_name VARCHAR(255),
   stadt_name VARCHAR(255),
   region_name VARCHAR(255),
   land_name VARCHAR(255)
   );
  
  CREATE TABLE time_dim (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
   year VARCHAR(255),
   quarter VARCHAR(255),
   month VARCHAR(255),
   date DATE
   );
  
  CREATE TABLE sales (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
   items_sold INTEGER,
   amount DOUBLE,
   fk_time_id INTEGER NOT NULL,
   fk_shop_id INTEGER NOT NULL,
   fk_article_id INTEGER NOT NULL,
   FOREIGN KEY (fk_time_id) REFERENCES time_dim(id)
   ON DELETE CASCADE,
   FOREIGN KEY (fk_shop_id) REFERENCES shop_dim(id)
   ON DELETE CASCADE,
   FOREIGN KEY (fk_article_id) REFERENCES product_dim(id)
   ON DELETE CASCADE
   );
  
  %% Deprecated
  CREATE TABLE sales_scratch (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
   date DATE,
   shop_name VARCHAR(255),
   article_name VARCHAR(255),
   items_sold INTEGER,
   amount DOUBLE
   );