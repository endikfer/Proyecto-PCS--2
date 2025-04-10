CREATE USER IF NOT EXISTS 'g2'@'%' IDENTIFIED BY 'g2';

GRANT ALL ON segurosdb.* TO 'g2'@'%';
FLUSH PRIVILEGES;

USE segurosdb;

insert into administrador (email, nombre, password) values ('admin@gmail.com', 'admin1234', '1234');

select * from administrador;