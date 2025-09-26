# Lab 1 Git Race -- Project Report

## Initial Issues Setting Up the Project on Windows and Their Resolutions

### Problem with gradlew

```
$> docker-compose -f docker-compose.dev.yml up --build
 => [modern-web-app-dev 5/6] RUN chmod +x ./gradlew                                                                    0.3s
 => ERROR [modern-web-app-dev 6/6] RUN ./gradlew dependencies --no-daemon                                              0.5s
------
 > [modern-web-app-dev 6/6] RUN ./gradlew dependencies --no-daemon:
0.476 env: ‘sh\r’: No such file or directory
0.476 env: use -[v]S to pass options in shebang lines
------
failed to solve: process "/bin/sh -c ./gradlew dependencies --no-daemon" did not complete successfully: exit code: 127
```

Solution: change the gradlew file format from CRLF to LF.

### Problem with docker run

```
$> docker run -p 8080:8080 -p 35729:35729 -v $(pwd):/app modern-web-app-dev
docker: invalid reference format.
See 'docker run --help'.
```

Solution: use PWD in uppercase.

```
$> docker run -p 8080:8080 -p 35729:35729 -v ${PWD}:/app modern-web-app-dev
Starting a Gradle Daemon (subsequent builds will be faster)
...
```

---

## Description of Changes

### 1. Time-based Greeting

The greeting message changes based on the time of day

| Hour          | Greeting       |
| ------------- | -------------- |
| 07:00 – 15:59 | Good Morning   |
| 16:00 – 20:59 | Good Afternoon |
| 21:00 – 06:59 | Good Night     |

### 2. Data Persistence

Greeting history and user data are stored in a file-stored H2 database

### 3. User Authentication

Basic user authentication with signup, login and logout functionality.
Authentication in the system is handled using sessions. When a user logs in successfully, their username and role are stored in the session. This allows the application to track the logged-in user and enforce role-based access control. Upon logout, the session is invalidated, clearing all user information and preventing further access to protected pages or API endpoints.

### 4. Greeting History

Greeting history is tracked and displayed with timestamps and user information. Regular users can only view their own greeting history, while administrators have full access: they can see their own greetings, view the greeting history of other users, and access a complete list of all greetings in the system.

### 5. Rate Limiting

API rate limiting is implemented to prevent abuse and ensure fair usage. Each client IP is restricted to a maximum of 5 requests per minute across all API endpoints, helping to protect the system from excessive or malicious traffic.

### 6. Statistics Dashboard

The Statistics Dashboard provides an overview of application usage, including the top 3 most popular greeted names, the total number of registered users, and the total number of greetings made. This feature is accessible only to ADMIN users and is available both as a web dashboard and via a REST API endpoint.

### 7. Code Documentation

All code is documented using KDoc, providing clear explanations for classes, methods, and properties. This ensures maintainability and readability for future developers.

### 8. Comprehensive Test Coverage

The project includes extensive tests for all implemented controllers and services. Tests consist of Unit Tests, MVC Tests, and Integration Tests, ensuring the correctness and stability of the application.

### 9. CI workflow:

Each commit triggers a GitHub Actions workflow that runs all tests automatically. Test results are saved as downloadable artifacts, and KDoc-generated documentation is published to GitHub Pages at: https://oscar-gilbe.github.io/lab1-git-race-Oscar-GilBe/

### 10. Controllers

All controllers are implemented in both REST and MVC versions, allowing the application to serve JSON API endpoints as well as Thymeleaf-based web pages for user interaction.

#### API Endpoints

##### Web Endpoints

- `GET /` - Main web page with interactive HTTP debugging tools
- `GET /?name={name}` - Personalized greeting page
- `GET /login` - Login page
- `GET /register` - Registration page
- `GET /home` - Home page (requires login)
- `GET /history/{username}` - View own greeting history (or others if ADMIN)
- `GET /history/all` - View all histories (ADMIN only)
- `GET /statistics` - View statistics dashboard (ADMIN only)
- `GET /logout` - Logout

##### REST API Endpoints

###### Greeting

- `GET /api/hello` - Returns JSON greeting with timestamp
- `GET /api/hello?name={name}` - Returns personalized JSON greeting

###### Greeting History

- `GET /api/history` - Returns all greeting histories
- `GET /api/history/{username}` - Returns greeting history for a user

###### Users

- `POST /api/users` - Create a new user
- `POST /api/users/login` - Authenticate a user
- `GET /api/users/{username}` - Get a user by username
- `GET /api/users` - List all users
- `DELETE /api/users/{username}` - Delete a user

###### Statistics

- `GET /api/statistics` - Returns JSON statistics (ADMIN only)

###### Monitoring Endpoints

- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application information
- `GET /actuator/metrics` - Application metrics

---

## Technical Decisions

- **BCrypt for Password Encryption**
  We use BCrypt to securely hash user passwords before storing them in the database. This hashing method has been used in previous subjects and provides strong protection against password attacks.

- **Bucket4j + Caffeine for API Rate Limiting**
  We implement API rate limiting using Bucket4j to restrict each IP to a maximum of 5 requests per minute. Caffeine cache is used to efficiently store the IP-to-bucket mapping, preventing memory growth and ensuring scalability.

- **Mockito with JUnit for Testing**
  For unit and integration testing, we use Mockito to mock dependencies and JUnit to structure and run tests. This allows us to test controllers and services in isolation and verify their behavior reliably.

- **Session-based authentication**
  Another technical decision we made was to implement session-based authentication instead of using JWT or token-based systems. By storing the username and role directly in the server-side session, we simplified access control for both web pages and REST endpoints, enabling role-based restrictions (e.g., ADMIN-only access to statistics) while keeping the authentication flow straightforward and secure.

- **H2 databases**
  We use H2 in-memory and file-based databases for different environments. During testing, H2 runs entirely in memory to ensure fast, isolated, and reproducible test execution, while in development and local runs it persists to disk so that user and greeting history data is retained between sessions.

---

## Learning Outcomes

During this lab assignment, I gained hands-on experience with **Kotlin and Spring Boot**, applying modern software development practices and frameworks. Working with Spring Boot DevTools and Live Reload allowed me to see immediate changes in templates and fix compilation issues efficiently, although I also learned its limitations in certain situations. I became familiar with the structure of a Spring Boot application, including how entities, repositories, services, controllers, and DTOs interact across layers.

I learned how to implement **secure and structured data handling** using DTOs. For instance, sensitive information such as encrypted passwords is never exposed in UserDTOs, and timestamps in GreetingHistoryDTOs are formatted in the Europe/Madrid timezone while storing them in UTC in the database. This taught me best practices in data formatting and separation of concerns between persistence and presentation layers.

A significant part of the lab assignment involved **server-side rendering** with Thymeleaf and Bootstrap. I learned how to integrate backend business logic directly into web templates, manage dynamic content, and implement session-based personalization, such as displaying usernames or admin-specific features, without relying on heavy client-side frameworks.

Working on **API rate limiting** provided insight into protecting web services from abuse. I implemented rate limiting with **Bucket4j** and used **Caffeine cache** to optimize memory usage and facilitate scalability, controlling the maximum number of requests per minute per client IP and preventing excessive resource consumption. This taught me both the theory and practical techniques for enforcing API usage policies in real-world applications.

Finally, I gained valuable experience with **project management and DevOps practices**. I configured **Spring profiles** to separate environments, ensuring tests could run without API rate limiting (except for the specific rate limit test) and using **H2 in-memory databases** for testing while relying on **H2 on disk** for development. I also learned to work with the **H2 Console** to inspect populated tables and run SQL queries directly. For testing, I made extensive use of **mocks** to isolate components, and I stored **test execution results** in report files accessible via the browser (build/reports/tests/test/index.html). These reports were also uploaded as **artifacts** through GitHub Actions, making them downloadable after each run. On the documentation side, I created a **custom Gradle task** to generate HTML documentation using **KDoc and Dokka**, ensuring maintainable and easily accessible technical references. From a version control perspective, I practiced Git techniques such as **rebase** to clean up commit history. Finally, I set up a **CI workflow with GitHub Actions** to automatically execute tests, generate reports, and publish the documentation to GitHub Pages. This process highlighted the importance of automation, maintainable documentation, and reproducible environments in professional software projects.

---

## AI Disclosure

### AI Tools Used

ChatGPT

### AI-Assisted Work

**What was generated with AI assistance**
AI was used for:

* Clarifying parts of the original codebase provided by the professor.
* Exploring different approaches to implementing the API Rate Limit Filter in order to determine which was the simplest and most efficient.
* Organizing project files in a logical and clean way, following hexagonal/clean architecture principles.
* Configuring access to the H2 Console.
* Creating HTML and Thymeleaf templates. Since the frontend was not a primary focus of this subject, this was considered an effective use of AI.
* Generating KDoc documentation. All documentation was reviewed and adapted to ensure it accurately reflected the codebase and remained correct and consistent.
* Creating skeleton tests and providing mock/when examples, since no mocks had been implemented before and I did not yet have strong experience in testing.
* Troubleshooting complex errors. For example, there were significant issues with GitHub Actions not running tests correctly due to H2 database write-permission problems during the build. With AI’s help, the solution was to run tests using an in-memory H2 instance and to create a dedicated test profile along with a new properties file (`application-test.properties`).

**Percentage of AI-assisted vs. original work**: Approximately **45%** of the work was AI-assisted. This mainly included documentation, initial test skeletons, and early drafts of features such as the API Rate Limit Filter.

**Any modifications made to AI-generated code**: All AI-generated content (documentation, test skeletons, and API Rate Limit Filter drafts) was thoroughly reviewed, adapted, and modified to ensure accuracy, alignment with project requirements, and compliance with clean coding practices. Nothing was used without verification and adjustment to fit the project’s goals.

### Original Work

The main business logic of the project was developed without AI assistance. This includes the implementation of **controllers, services, DTOs, repositories, and JPA entities**. Since JPA had already been used in the _Databases 2_ course, it was straightforward to declare the entities to be persisted and to define some queries (for example, the one that retrieves the **Top 3 most popular greeted names**).

My learning process was based on studying the **codebase provided by the professor**, reading the **Clean Architecture blog** (https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html), and occasionally consulting AI to provide useful examples for learning how to implement specific features such as the **API rate limiter** or **test skeletons**.
