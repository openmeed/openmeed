DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS stats;

CREATE TABLE users (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  username VARCHAR(250) NOT NULL,
  email_address VARCHAR(250) NOT NULL,
  career VARCHAR(250) DEFAULT NULL,
  company VARCHAR(250) DEFAULT NULL,
  role_id VARCHAR(250) DEFAULT NULL,
  password VARCHAR(250) DEFAULT NULL
);

CREATE TABLE roles (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  role_name VARCHAR(250) NOT NULL,
  access VARCHAR(250) NOT NULL,
);

CREATE TABLE stats (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  online_users VARCHAR(250) NOT NULL,
  vulnerabilities_detected VARCHAR(250) NOT NULL,
);

INSERT INTO users (username, email_address, career, company,role_id, password) VALUES
    ('Ebenezer', 'egraham15@alustudent.com', 'Lead Developer','African Leadership University','1','seshat'),
    ('Bill', 'bill@gmail.com', 'CEO','Microsoft','2','billionaire');

INSERT INTO roles(role_name,access) VALUES
    ('ADMIN','dashboard'),
    ('USER','analyzer');
