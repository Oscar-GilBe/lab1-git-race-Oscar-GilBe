# Modern Web Application

A modern Spring Boot application built with Kotlin, featuring a responsive web interface, session-based authentication, role-based access control, and REST API endpoints.

## 🚀 Features

- **Modern Tech Stack**: Spring Boot 3.5.3, Kotlin 2.2.10, Java 21 LTS
- **User Management**: Registration, login, logout, and deletion
- **Role-Based Access Control**: USER and ADMIN roles with restricted access
- **Greeting System**: Personalized greetings depending on the time of day
- **Greeting History**: Track and view greetings (own history or all histories for ADMIN)
- **Statistics Dashboard**: Top 3 most popular greeted names, total number of registered users and total number of greetings made. Accessible only by ADMIN users. Available as a web dashboard and a REST API
- **Session Handling**: Secure session management for web pages
- **Responsive UI**: Bootstrap 5.3.3 with modern design
- **REST API**: JSON endpoints for greetings, histories, and users
- **Health Monitoring**: Spring Boot Actuator for application health
- **Live Development**: Spring Boot DevTools for automatic reload
- **Interactive HTTP Debugging**: Client-side HTTP request/response visualization
- **Containerization**: Docker support with multi-stage builds
- **Comprehensive Testing**: Unit, integration, and MVC tests
- **Modern Kotlin**: Constructor injection, data classes, and modern syntax

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.5.3
- **Language**: Kotlin 2.2.10
- **Java Version**: 21 LTS
- **Frontend**: Bootstrap 5.3.3, Thymeleaf
- **Build Tool**: Gradle 9.0.0
- **Testing**: JUnit 5, AssertJ, MockMvc
- **Containerization**: Docker

## 📋 Prerequisites

- Java 21 or higher
- Gradle 9.0.0 or higher
- Docker (optional)

## 🏃‍♂️ Quick Start

### Running the Application

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd template-lab1-git-race
   ```

2. **Build the application**
   ```bash
   ./gradlew build
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

4. **Access the application**
   - Web Interface: http://localhost:8080
   - API Endpoint: http://localhost:8080/api/hello
   - Health Check: http://localhost:8080/actuator/health

### Using Docker for Development

1. **Using Docker Compose** (Recommended):
   ```bash
   docker-compose -f docker-compose.dev.yml up --build
   ```

2. **Build and run development container**:
   ```bash
   docker build -f Dockerfile.dev -t modern-web-app-dev .
   docker run -p 8080:8080 -p 35729:35729 -v $(pwd):/app modern-web-app-dev
   # docker run -p 8080:8080 -p 35729:35729 -v $(PWS):/app modern-web-app-dev ## On windows
   ```

The development Docker setup includes:
- **LiveReload Support**: Automatic browser refresh on code changes
- **Volume Mounting**: Source code changes are immediately reflected
- **Spring Boot DevTools**: Automatic application restart on file changes
- **Health Monitoring**: Built-in health checks via Spring Boot Actuator

## 🧪 Testing

Run all tests:
```bash
./gradlew test
```

Run specific test classes:
```bash
./gradlew test --tests "HelloControllerUnitTests"
./gradlew test --tests "IntegrationTest"
```

## 📡 API Endpoints

### Web Endpoints
- `GET /` - Main web page with interactive HTTP debugging tools
- `GET /?name={name}` - Personalized greeting page
- `GET /login` - Login page
- `GET /register` - Registration page
- `GET /home` - Home page (requires login)
- `GET /history/{username}` - View own greeting history (or others if ADMIN)
- `GET /history/all` - View all histories (ADMIN only)
- `GET /statistics` - View statistics dashboard (ADMIN only)
- `GET /logout` - Logout

### REST API Endpoints
#### Greeting
- `GET /api/hello` - Returns a default JSON greeting with timestamp
```
curl.exe -X GET "http://localhost:8080/api/hello"

Response:

{"message":"Good Afternoon, World!","timestamp":"2025-09-26T14:39:45.734176962Z"}
```

- `GET /api/hello?name={name}` - Returns personalized JSON greeting
```
curl.exe -X GET "http://localhost:8080/api/hello?name=pepe"

Response:

{"message":"Good Afternoon, pepe!","timestamp":"2025-09-26T14:56:39.522561944Z"}
```

#### Greeting History
- `GET /api/history` - Returns all greeting histories
```
curl.exe -X GET "http://localhost:8080/api/history"

(summary) Response:

[{"id":1,"message":"Good Afternoon, World!","timestamp":"26-09-2025 16:39:45","username":null},{"id":2,"message":"Good Afternoon, oscar!","timestamp":"26-09-2025 16:54:31","username":"oscar"},
...
,{"id":38,"message":"Good Afternoon, pepe!","timestamp":"26-09-2025 16:56:39","username":"pepe"}]
```

- `GET /api/history/{username}` - Returns greeting history for a user
```
curl.exe -X GET "http://localhost:8080/api/history/pepe"

Response:

[{"id":24,"message":"Good Afternoon, pepe!","timestamp":"26-09-2025 16:55:28","username":"pepe"},{"id":25,"message":"Good Afternoon, pepe!","timestamp":"26-09-2025 16:55:30","username":"pepe"},{"id":26,"message":"Good Afternoon, pepe!","timestamp":"26-09-2025 16:55:30","username":"pepe"},{"id":27,"message":"Good Afternoon, pepe!","timestamp":"26-09-2025 16:55:30","username":"pepe"},{"id":28,"message":"Good Afternoon, pepe!","timestamp":"26-09-2025 16:55:34","username":"pepe"},{"id":38,"message":"Good Afternoon, pepe!","timestamp":"26-09-2025 16:56:39","username":"pepe"}]
```

#### Users
- `POST /api/users` - Create a new user
```
curl.exe -X POST "http://localhost:8080/api/users" -H "Content-Type: application/x-www-form-urlencoded" -d "username=mario&password=secret&role=USER"

Response:

{"id":6,"username":"mario","role":"USER"}
```

- `POST /api/users/login` - Authenticate a user
```
curl.exe -X POST "http://localhost:8080/api/users/login" -H "Content-Type: application/x-www-form-urlencoded" -d "username=mario&password=secret"

Response:

Login successful
```

- `GET /api/users/{username}` - Get a user by username
```
curl.exe -X GET "http://localhost:8080/api/users/mario"

Response:

{"id":6,"username":"mario","role":"USER"}
```

- `GET /api/users` - List all users
```
curl.exe -X GET "http://localhost:8080/api/users"

Response:

[{"id":1,"username":"oscar","role":"USER"},{"id":2,"username":"admin","role":"ADMIN"},{"id":3,"username":"pepe","role":"ADMIN"},{"id":4,"username":"paco","role":"USER"},{"id":5,"username":"miguel","role":"USER"},{"id":6,"username":"mario","role":"USER"}]
```

- `DELETE /api/users/{username}` - Delete a user
```
curl.exe -X DELETE "http://localhost:8080/api/users/mario"

This request does not return a response
```

#### Statistics
- `GET /api/statistics` - Returns JSON statistics
```
curl.exe -X GET "http://localhost:8080/api/statistics"

Response:
{"totalUsers":5,"totalGreetings":39,"top3Names":[{"first":"oscar","second":14},{"first":"admin","second":10},{"first":"paco","second":8}]}

```

### Monitoring Endpoints
- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application information
- `GET /actuator/metrics` - Application metrics

### Interactive HTTP Debugging
- **Web Page Testing**: Test the main page with personalized greetings
- **API Testing**: Test REST endpoints with real-time request/response display
- **Health Check Testing**: Monitor application health status
- **Live Reload**: Spring Boot DevTools automatically reloads on file changes

## Architecture
In the application, we follow the MVC (Model-View-Controller) pattern to separate concerns and organize the code efficiently. Controllers handle HTTP requests, process input, and delegate business logic to services. Services contain the core application logic and interact with repositories, which manage data persistence via entities mapped to the database using JPA. To safely expose data to the client or views, DTOs (Data Transfer Objects) are used to transfer only the necessary information, ensuring encapsulation and avoiding leaking internal entity details. This structure allows controllers to focus on request handling, services on business rules, and repositories on data access, creating a clean and maintainable architecture.

## 🏗️ Project Structure

```
src
├── main
│   ├── kotlin
│   │   ├── HelloWorld.kt                                                     # Main application class
│   │   ├── config
│   │   │   ├── RateLimitFilter.kt
│   │   │   └── SecurityConfig.kt
│   │   ├── core
│   │   │   └── enum
│   │   │       └── Role.kt
│   │   ├── delivery
│   │   │   ├── DTO
│   │   │   │   ├── GreetingHistoryDTO.kt
│   │   │   │   ├── StatisticsDTO.kt
│   │   │   │   └── UserDTO.kt
│   │   │   └── controller
│   │   │       ├── GreetingHistoryController.kt
│   │   │       ├── HelloController.kt                                        # Web and API controllers
│   │   │       ├── StatisticsController.kt
│   │   │       └── UserController.kt
│   │   ├── repository
│   │   │   ├── GreetingHistoryRepository.kt
│   │   │   ├── UserRepository.kt
│   │   │   └── entity
│   │   │       ├── GreetingHistory.kt
│   │   │       └── User.kt
│   │   └── service
│   │       ├── GreetingService.kt
│   │       ├── StatisticsService.kt
│   │       └── UserService.kt
│   └── resources
│       ├── META-INF
│       │   └── additional-spring-configuration-metadata.json
│       ├── application.properties                                          # Application configuration
│       ├── public
│       │   └── assets
│       │       └── logo.svg                                                 # Application logo
│       ├── static
│       │   ├── css
│       │   │   └── styles.css
│       │   └── js
│       │       └── http-debug.js
│       └── templates
│           ├── history.html                                                  # Thymeleaf template
│           ├── home.html
│           ├── login.html
│           ├── register.html
│           ├── statistics.html
│           └── welcome.html
└── test
    ├── kotlin
    │   ├── GreetingServiceTest.kt
    │   ├── IntegrationTest.kt                                                # Integration tests
    │   ├── RateLimitFilterMVCTests.kt
    │   ├── StatisticsServiceTest.kt
    │   ├── UserServiceTest.kt
    │   └── controller
    │       ├── AuthPageControllerMVCTests.kt
    │       ├── AuthPageControllerUnitTests.kt
    │       ├── GreetingHistoryControllerMVCTests.kt
    │       ├── GreetingHistoryControllerUnitTests.kt
    │       ├── GreetingHistoryPageControllerMVCTests.kt
    │       ├── GreetingHistoryPageControllerUnitTests.kt
    │       ├── HelloControllerMVCTests.kt                                     # MVC tests
    │       ├── HelloControllerUnitTests.kt                                    # Unit tests
    │       ├── StatisticsControllerMVCTests.kt
    │       ├── StatisticsControllerUnitTests.kt
    │       ├── UserControllerMVCTests.kt
    │       └── UserControllerUnitTests.kt
    └── resources
        └── application-test.properties                      # Application configuration for Github Action Tests

24 directories, 47 files
```

## ⚙️ Configuration

Key configuration options in `application.properties`:

```properties
# Application settings
spring.application.name=modern-web-app
server.port=8080

# Custom message
app.message=Welcome to the Modern Web App!

# Actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics

# H2 Database Configuration
spring.datasource.url=jdbc:h2:file:./data/greetingdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.web-allow-others=true

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
```
About H2 database configuratiom:
The database is stored on the local filesystem (./data/greetingdb).
The H2 console is accessible at http://localhost:8080/h2-console, allowing SQL queries and inspection of persisted data.

About JPA/Hibernate configuratiom:
ddl-auto=update: Automatically creates/updates database schema based on entities.
show-sql and format_sql: Print executed SQL queries in the logs.
hibernate.jdbc.time_zone=UTC: Ensures timestamps are stored consistently in UTC.

Moreover, application logs include detailed Hibernate SQL queries and parameter bindings for debugging.

## 🐳 Docker Details

The application includes a development-focused Docker setup:

- **Development Dockerfile**: Uses JDK 21 Alpine for development with live reload
- **Docker Compose**: Orchestrates the development environment with volume mounting
- **LiveReload**: Spring Boot DevTools automatically reloads on file changes
- **Volume Mounting**: Source code changes are immediately reflected in the container
- **Health Checks**: Built-in health monitoring via Spring Boot Actuator
- **Development Tools**: Includes wget for health checks and debugging utilities

## 🔧 Development

### Adding New Features

1. **Controllers**: Add new endpoints in the controller package (controller/)
2. Add **entities and repositories** for new persistence needs
3. Add **service methods** for business logic
2. **Templates**: Add new Thymeleaf templates in `src/main/resources/templates/`
3. **Tests**: Add corresponding tests in the test package
4. **Configuration**: Update `application.properties` for new settings

### Code Style

- Use modern Kotlin features (constructor injection, data classes)
- Follow Spring Boot best practices
- Write comprehensive tests for all functionality
- Use descriptive test method names with backticks

## 📊 Monitoring

The application includes Spring Boot Actuator for monitoring:

- **Health**: Application and dependency health status
- **Info**: Application metadata and build information
- **Metrics**: JVM and application metrics

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆕 What's New in This Modern Version

- ✅ Upgraded to Java 21 LTS for better performance
- ✅ Modern Kotlin syntax with constructor injection
- ✅ Separated web and API controllers for better organization
- ✅ Added comprehensive test coverage
- ✅ Implemented Spring Boot Actuator for monitoring
- ✅ Created responsive Bootstrap 5.3.3 UI
- ✅ Added Docker support with multi-stage builds
- ✅ Fixed Bootstrap version inconsistencies
- ✅ Enhanced error handling and validation
- ✅ Added interactive features and API endpoints
