# Proyecto-PCS--2

Para compilar el proyecto ejecutamos el siguiente comando:

    mvn clean compile

Para lanzar el servidor ejecutamos el siguiente comando:

    mvn spring-boot:run

Para lanzar el cliente ejecutamos el siguiente comando:

    mvn exec:java -Dexec.mainClass="com.seguros.cer"

Para acceder a la pagina desde el navegador entramos en:

    http://localhost:8080/

Para abrir base de datos (contraseña root):

    mysql -u root -p
    USE segurosdb;   --> para usar la base de datos seguros
