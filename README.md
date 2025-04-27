# Proyecto-PCS--2

Antes de ejecutar el proyecto debes crear la base de datos en tu dispositivo.
Para ello, usa el documento llamado dbsetup.sql que se encuentra en la ruta:

    src/main/resources/dbsetup.sql

Debes ejecutarlo en tu dispositivo de base de datos MySQL para crear las tablas necesarias.

Para compilar el proyecto ejecutamos el siguiente comando:

    mvn clean compile

Para lanzar el servidor ejecutamos el siguiente comando:

    mvn spring-boot:run

Para lanzar el cliente ejecutamos el siguiente comando:

    mvn exec:java

Comando para ejecutar teses de rendimiento

    mvn verify -Pperformance

Comando para ejecutar teses de integracion

    mvn verify -Pintegration

Para acceder a la pagina desde el navegador entramos en:

    http://localhost:8080/

Para abrir base de datos (contraseña root):

    mysql -u root -p
    USE segurosdb;   --> para usar la base de datos seguros

El usuario por defecto es:

    user@gmail.com

La contraseña por defecto para el usuario base es:

    user01

El usuario por defecto admin  es:

    admin@gmail.com

El contraseña por defecto admin  es:

    1234