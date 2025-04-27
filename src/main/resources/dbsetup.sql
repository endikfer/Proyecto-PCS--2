CREATE USER IF NOT EXISTS 'g2'@'%' IDENTIFIED BY 'g2';

GRANT ALL ON segurosdb.* TO 'g2'@'%';
FLUSH PRIVILEGES;

CREATE SCHEMA IF NOT EXISTS segurosdb;

USE segurosdb;

CREATE TABLE IF NOT EXISTS administrador (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

insert into administrador (email, nombre, password) values ('admin@gmail.com', 'admin1234', '1234');

CREATE TABLE IF NOT EXISTS clientes(
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

insert into clientes (email, nombre, password) values ('user@gmail.com', 'user', 'user01');

select * from clientes;