Java Spring Boot backend API of online system for searching psychologists and byuing their service

## :computer: **How to run the project on Windows**
1. Download [Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) and [Maven](https://maven.apache.org/install.html).
2. Open your terminal (cmd) and check Java installation by `java -version` and Maven `mvn -version`
3. Clone repository: Open your terminal (cmd) and use `git clone https://github.com/MishaHMK/Spring-Boot-Book.git`.
4. Download and install [MySQL](https://dev.mysql.com/downloads/installer/).
5. Open your terminal (cmd) and create MySQL user `mysql -u USER -p`.
6. Create a database `CREATE DATABASE DB_NAME;`
7. In src/main/recources/application.properties put proper MySQL db data:
  ![image](https://github.com/user-attachments/assets/c8973069-6f6c-429b-ba80-44a534d198a9)
)
8. Run project `mvn clean install` and then `java -jar target/PsychologistApp-0.0.1-SNAPSHOT.jar`
9. Proceed to [Interactive Swagger Documentation](http://localhost:8080/api/swagger-ui/index.html)
