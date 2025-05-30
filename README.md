## :brain: **Therapist Booking App "Project MindBloom"** 💮:

Java Spring Boot backend API of online system for searching therapists and booking their services

This project is the Backend part of a team project at Mate Academy
 
Here, you can see the [UI (React)](https://github.com/taniavozniuk/Psychologist-Search-Service) designed by my talented teammates

## :mag_right: **Project Overview**

"Project MindBloom" provides different operations:
- Role-based access/authorization (Admin, Customer) 
- User management: Allows to find/edit data about users in system
- Accommodation management: Allows to find/create/remove/edit data about psychologists in system
- Booking management: Allows to find/create/remove/edit data about bookings of avaliable accommodations
- Payment management: Allows to create/confirm/cancel/renew payments with support of Stripe payment system
- Email notifications: Provides notification and interactive link to support user workflow in system

## ⏬ You can find overview of all api endpoints in the end of this document

## :hammer_and_wrench: **Technology Stack**
**Core:**
| Tool    | Description                                         |
|---------|-----------------------------------------------------|
| Java 17 | Core programming language of the backend            |
| Maven   | Project management and build tool                   |

**Spring:**
| Tool                     | Description                                                |
|--------------------------|------------------------------------------------------------|
| Spring Boot 3.4.2     | Advanced architecture framework for building applications |
| Spring Boot Web      | Enables embedded web server and REST API development       |
| Spring Data JPA     | Simplifies database access operations using JPA and ORM    |
| Spring Boot Security   | Provides authentication and authorization capabilities    |
| Spring Boot Validation | Ready-to-use collection of data constraints/checks         |

**Data storage and access:**
| Tool        | Description                                                |
|-------------|------------------------------------------------------------|
| MySQL 8.0.33 | Database management system                                 |
| Hibernate   | Bidirectional mapping tool between Java code and SQL database |
| Liquibase   | Tool for database creation and version control             |

**External functional API:**
| Tool        | Description                                                |
|-------------|------------------------------------------------------------|
| Spting Boot Mail | Library to integrate mail sending                      |
| Stripe API | Library to integrate Stripe payment functions to API project |


**Additional libs and tools:**
 | Tool      | Description                                              |
|-----------|-----------------------------------------------------------|
| Lombok    | Library for Java code simplification                      |
| MapStruct | Tool for simple data mapping                              |
| JWT       | Authorization standard                                    |
| Swagger   | Tools to create API documentation                         |
| Redis (Lettuce) | Caching of paginated data for quick access          |

**Deployment:**
| Docker    | Platform for project packaging and deployment             |
| AWS       |                                                           |


## :computer: **How to run the project on Windows**
1. Download [Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) and [Maven](https://maven.apache.org/install.html).
2. Open your terminal (cmd) and check Java installation by `java -version` and Maven `mvn -version`
3. Clone repository: Open your terminal (cmd) and use `https://github.com/MishaHMK/PsychologistAppApi.git`.
4. Download and install [MySQL](https://dev.mysql.com/downloads/installer/).
5. Open your terminal (cmd) and create MySQL user `mysql -u USER -p`.
6. Create a database `CREATE DATABASE DB_NAME;`
7. In src/main/recources/application.properties put proper MySQL db data:
  ![image](https://github.com/user-attachments/assets/c8973069-6f6c-429b-ba80-44a534d198a9)
)
8. Run project `mvn clean install` and then `java -jar target/PsychologistApp-0.0.1-SNAPSHOT.jar`
9. Proceed to [Interactive Swagger Documentation](http://localhost:8080/api/swagger-ui/index.html)
