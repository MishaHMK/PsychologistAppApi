## :brain: **Therapist Booking App "Project MindBloom"** 💮

Java Spring Boot backend API of online system for searching therapists and booking their services

This is the Backend part of a team project at Mate Academy

## 🎨  Here you can see the key parts of the system, crafted by my talented teammates
  - [Client part of the project (React)](https://github.com/taniavozniuk/Psychologist-Search-Service) 
  - [FigmaJam development board](https://www.figma.com/board/dLSKiKU8Il18s0TCn3MXLg/mental-health?node-id=0-1&t=dd4uuKOUetJx9qz8-1) (link to UI assets in the end of document)

## :mag_right: **Project Overview**

"Project MindBloom" provides different operations:
- Role-based access/authorization (Admin, Customer) 
- User management: Allows to see/edit data about user profile
- Pshychologists management: Allows to find/create/remove/edit data about psychologists
- Review management: Allows to see/create/remove reviews for pshychologists
- Booking management: Allows to find/create/remove/edit data about bookings of avaliable psychologists meetings
- Payment management: Allows to see/create/confirm/cancel/renew payments with support of Stripe payment system
- Email notifications: Provides meassages with interactive link to support user workflow in system

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
| Spring Boot 3.4.2     | Advanced architecture framework for building applications     |
| Spring Boot Web      | Enables embedded web server and REST API development           |
| Spring Data JPA     | Simplifies database access operations using JPA and ORM         |
| Spring Boot Security   | Provides authentication and authorization capabilities       |
| Spring Boot Validation | Ready-to-use collection of data constraints/checks           |
| Spting Boot Mail       | Library to integrate mail sending                      |

**Data storage and access:**
| Tool        | Description                                                   |
|-------------|---------------------------------------------------------------|
| MySQL 8.0.33 | Database management system                                   |
| Hibernate   | Bidirectional mapping tool between Java code and SQL database |
| Liquibase   | Tool for database creation and version control                |

**External functional API:**
| Tool        | Description                                                |
|-------------|------------------------------------------------------------|
| Stripe API | Library to integrate Stripe payment functions to API project |
| Gmail API  | Toolking to manage mail sending via SMTP protocol  |    

**Additional libs and tools:**
 | Tool      | Description                                              |
|-----------|-----------------------------------------------------------|
| Lombok    | Library for Java code simplification                      |
| MapStruct | Tool for simple data mapping                              |
| JWT       | Authorization standard                                    |
| Swagger   | Tools to create API documentation                         |
| Redis (Lettuce) | Caching of paginated data for quick access          |

**Deployment:**
| Tool      | Description                                               |
|-----------|-----------------------------------------------------------|
| Docker    | Platform for project packaging and deployment             |
| Namecheap | Source of domain with SSL certificate (Nginx on EC2 VM)   |
| AWS       | Platform to deploy Java project, Database and Redis       |
| AWS S3    | Contains images of user profiles and psychologist pages   |
| AWS EC2   | Virtual machine to run composed docker image of project   | 
| AWS RDS   | Database service for deployed project                     | 
| AWS ECR | Docker contatiner registry to receive images for EC2        | 
| AWS Route 53 | Connects EC2 IP to domain                              | 

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
8. Check if all data in .env file is put and resources files in src/main and src/java are filled
9. [Register](https://docs.stripe.com/)  Stripe account and visit Dashboard to receive API Keys (Secret Key)
10. Create image storage like Imgur or S3 which can access your images by URL
11. Prepare SMTP Google account and keys (if you need Mail Sending functions)
12. Download and install [Docker](https://www.docker.com/) if you won't use Docker
   put `spring.docker.compose.enabled=false` and comment (via # symbol) fields `spring.data.redis.host` 
   and `spring.data.redis.host` in src/main/resources/application.properties
13. Open terminal (cmd) in root folder and do `docker-compose build` and `docker-compose up`
14. Run project `mvn clean install` and then `java -jar target/PsychologistApp-0.0.1-SNAPSHOT.jar`
15. Proceed to [Interactive Swagger Documentation](http://localhost:8080/api/swagger-ui/index.html)

🔸 Backend endpoints are testable in deployed [Documentation](https://www.mindbloom-api.store/api/swagger-ui/index.html#/)
🔸 Full project with [UI Client](https://github.com/taniavozniuk/Psychologist-Search-Service) is testable [here](https://taniavozniuk.github.io/Psychologist-Search-Service/#/)

## :page_facing_up: **Endpoints explanation**

Some endpoints require a [role] for access, use JWT token (Bearer) or Basic authentication.

Enpoints with pagination store data in Redis cache storage for faster access of previously received data. 
Cache automatically updates after performing operations, which can change state of this data.

Register, Booking and Payment operations are supported by email with interactive links.

**AuthController:** Handles registration and login requests.
- POST: `/api/auth/registration` - register new user.
- POST: `/api/auth/login` - login user and receive JWT token.

**UserController:** Handles registration and login requests.
- PUT: `/api/users/update` - update currently logged in user profile data [Admin].
- PATCH: `/api/users/update-image` - update role of the selected user by his id.
- GET: `/api/users/me` - receive currently logged in user info.
- DELETE: `/api/users/remove-user` - remove currently logged user profile from db.

**PsychologistController:** Handles requests for psychologist operations (Authorization can be required).  
- GET: `/api/psychologists` - Receive all psychologists (pagination).
- GET: `/api/psychologists?/{id}` - Receive a specific psychologist data by ID.
- GET: `/api/psychologists/liked` - Receive all psychologists, liked by current user. 
- GET: `/api/psychologists/filtered` - Receive all psychologist data filtered by specific criterias. 
- PATCH: `/api/psychologists/like/{id}` - Like a specific psychologist by ID for current user.
- POST: `/api/psychologists` - Create a new psychologist. [Admin]
- DELETE: `/api/psychologists/{id}` - Soft delete psychologist. [Admin]

**ReviewController:** Handles requests for psychologists reviews from users.  
- GET: `/api/reviews/all/{psychologistsId}` - Receive all reviews for psychologist by id.
- GET: `/api/reviews/review-page/{psychologistsId}` - Receive all latest (sorted/limit 6) reviews for psychologist by id.
- GET: `/api/reviews/{reviewId}` - Get review data bt ID.
- POST: `/api/reviews` - Create new review.
- DELETE: `/api/reviews/{reviewId}` - Delete review by ID. [Admin]

**BookingController:** Handles requests for bookings operations (Authorization is required).  
🔸 Allowed booking type values are: PENDING, CONFIRMED, CANCELED, EXPIRED.
🔸 Allowed booking times are: 9AM - 17PM (:00 minutes)
- GET: `/api/bookings` - Receive all bookings in system. [Admin]
- GET: `/api/bookings/my` - Receive all bookings of current user.
- GET: `/api/bookings/{id}` - Search a specific booking by ID. [Admin/Customer owner]
- GET: `/api/bookings/lockedDates/{psychologistId}` - Receive all locked dates for psychologist by ID.
- GET: `/api/bookings/free_spots/{psychologistId}` - Receive all free spots for psychologist by ID and date.
- POST: `/api/bookings` - Create new booking.
- POST: `/api/bookings/unauthorized` - Create new booking without authorization.
- PATCH: `/api/bookings/update-status/{id}` - Change booking status. [Admin]
- DELETE: `/api/bookings/{id}` - Soft delete booking. [Admin]

**PaymentController:** Handles requests for payment operations.  
🔸 Allowed payment type values are: PENDING, PAID, CANCELED, EXPIRED
- GET: `/api/payments` - Receive all user payments.
- GET: `/api/payments/success` - Receive payment confirmation (redirection).
- GET: `/api/payments/cancel` - Receive payment cancellation (redirection).
- GET: `/api/payments/user/{userId}` - Receive user payments [Admin or CurrentUser].
- GET: `/api/payments/create` - Proceed to payment creation from URL (used in mails).
- POST: `/api/payments` - Create new payment.
- PATCH: `/api/payments/{id}` - Renew expired payment. [Admin]
