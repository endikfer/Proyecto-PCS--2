# Proyecto-PCS--2

Para compilar el proyecto ejecutamos el siguiente comando:

    mvn clean compile

Para lanzar el servidor ejecutamos el siguiente comando:

    mvn spring-boot:run

Para lanzar el cliente ejecutamos el siguiente comando:

    mvn exec:java

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

Comando para ejecutar teses de rendimiento
    mvn verify -Pperformance

Comando para ejecutar teses de integracion
    mvn verify -Pintegration