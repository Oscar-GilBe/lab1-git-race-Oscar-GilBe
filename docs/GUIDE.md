---
title: "Web Engineering 2025-2026"
subtitle: "Lab 1: Git Race"   
date: "2025-09-11"
format:
  html:
    toc: true
    toc-depth: 3
    number-sections: true
    code-fold: true
    code-tools: true
    theme: cosmo
    css: |
      body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
      .quarto-title-block { border-bottom: 2px solid #2c3e50; padding-bottom: 1rem; }
      h1, h2, h3 { color: #2c3e50; }
      pre { background-color: #f8f9fa; border-left: 4px solid #007acc; }
      .callout { border-left: 4px solid #28a745; }
  pdf:
    documentclass: article
    classoption: [11pt, a4paper]
    toc: true
    toc-depth: 3
    number-sections: true
    geometry: [margin=2.5cm, headheight=15pt]
    fontsize: 11pt
    linestretch: 1.15
    colorlinks: true
    breakurl: true
    urlcolor: blue
    linkcolor: blue
    citecolor: blue
    hyperrefoptions:
      - linktoc=all
      - bookmarksnumbered=true
      - bookmarksopen=true
    header-includes:
      - |
        \usepackage{helvet}
        \renewcommand{\familydefault}{\sfdefault}
        \usepackage{hyperref}
        \usepackage{fancyhdr}
        \pagestyle{fancy}
        \fancyhf{}
        \fancyhead[L]{Web Engineering 2025-2026}
        \fancyhead[R]{Lab 1: Git Race}
        \fancyfoot[C]{\thepage}
        \renewcommand{\headrulewidth}{0.4pt}
        \usepackage{microtype}
        \usepackage{booktabs}
        \usepackage{array}
        \usepackage{longtable}
        \usepackage{xcolor}
        \definecolor{sectioncolor}{RGB}{44,62,80}
        \usepackage{sectsty}
        \allsectionsfont{\color{sectioncolor}}
  docx:
    toc: true
    number-sections: true
    reference-doc: custom-reference.docx
lang: en
---

Welcome to the first lab assignment of the 2025--2026 course! This guide will help you complete the assignment efficiently. Although this guide is command-line oriented, you are welcome to use IDEs like **VS Code**, **IntelliJ IDEA**, or **Eclipse**—all of which fully support the tools we'll be using. Ensure you have at least **Java 17** installed on your system before getting started.

## System Requirements

For this assignment, we'll be using the following technologies:

1. **Programming Language**: We're using [Kotlin 2.2.10](https://kotlinlang.org/), a versatile, open-source language that's popular for Android development and server-side applications.
2. **Build System**: The project is built using [Gradle 9.0.0](https://gradle.org/), a powerful tool that automates the building process, including compiling, linking, and packaging your code.
3. **Framework**: The application is based on [Spring Boot 3.5.3](https://docs.spring.io/spring-boot/), a framework that simplifies the development of production-grade applications. It requires Java 17 and is compatible with Java up to version 21.
4. **Java Version**: The project uses **Java 21 LTS** for optimal performance and modern language features (can be downgraded to Java 17).
5. **Frontend**: The application features a modern responsive UI built with **Bootstrap 5.3.3** and **Thymeleaf** templating.
6. **Containerization**: Full **Docker** support with multi-stage builds for production deployment.

## Getting Started

### Setting Up Git

1. **Install Git**: Download and install [git](https://git-scm.com/) on your system.
2. **GitHub Account**: If you haven’t already, sign up for an education account on [GitHub](https://github.com) using your real name and university email address.
3. **Configure Git**: Set up Git on your local machine with the following commands:

   ```bash
   git config --global user.name "Your Real Name"
   git config --global user.email "your_nip@unizar.es"
   ```

4. **Authenticate with GitHub**: Authenticate via HTTPS (recommended) or SSH. You can follow the [GitHub guide](https://docs.github.com/en/get-started/quickstart/set-up-git) for detailed instructions.

### Clone and Run the Application

1. **Clone the Repository**: Use the following commands to clone the repository to your local machine:

   ```bash
   git clone https://github.com/UNIZAR-30246-WebEngineering/lab1-git-race.git
   cd lab1-git-race-your-github-username
   ```

2. **Test the Repository**: Navigate to the `lab1-git-race` folder in your terminal and run:

   ```bash
   ./gradlew check
   ```

3. **Run the Application**: Start the application with:

   ```bash
   ./gradlew bootRun
   ```

   and open your browser at [http://localhost:8080](http://localhost:8080) to see the application running.

4. **Access the Application**: Once running, you can access:
   - **Web Interface**: [http://localhost:8080](http://localhost:8080) -- Main application with interactive features
   - **REST API**: [http://localhost:8080/api/hello](http://localhost:8080/api/hello) -- JSON API endpoint
   - **Health Check**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health) -- Application health status

### Using Docker for Development

The application includes Docker support optimized for development with live reload capabilities:

#### Development Docker Setup

1. **Using Docker Compose** (Recommended for Development):
   ```bash
   docker-compose -f docker-compose.dev.yml up --build
   ```

2. **Build and Run Development Container**:
   ```bash
   docker build -f Dockerfile.dev -t modern-web-app-dev .
   docker run -p 8080:8080 -p 35729:35729 -v $(pwd):/app modern-web-app-dev
   # docker run -p 8080:8080 -p 35729:35729 -v $(PWS):/app modern-web-app-dev ## On windows
   ```

#### Development Docker Features

The development Docker setup includes:

- **Spring Boot 3.5.3 + Kotlin 2.2.10 + Java 21 LTS** runtime environment
- **LiveReload Support**: Automatic browser refresh on code changes (port 35729)
- **Volume Mounting**: Source code changes are immediately reflected in the container
- **Spring Boot DevTools**: Automatic application restart on file changes
- **Health Checks**: Built-in health monitoring via Spring Boot Actuator
- **Development Profile**: Optimized for development with debugging enabled

#### Accessing the Application

Once the Docker container is running, you can access:
- **Web Interface**: http://localhost:8080
- **REST API**: http://localhost:8080/api/hello
- **Health Check**: http://localhost:8080/actuator/health
- **LiveReload**: Automatically enabled for CSS/JS changes

#### Development Workflow

1. **Start the container**: `docker-compose -f docker-compose.dev.yml up --build`
2. **Edit your code**: Changes are automatically detected and the application restarts
3. **View changes**: Browser automatically refreshes for static assets
4. **Stop the container**: `docker-compose -f docker-compose.dev.yml down`

## Current Application Features

The application has been significantly enhanced and now includes:

### 🚀 Core Features
- **Modern Web Interface**: Responsive Bootstrap 5.3.3 UI with professional design
- **REST API Endpoints**: JSON-based API with timestamp support
- **Health Monitoring**: Spring Boot Actuator for application health and metrics
- **Interactive HTTP Debugging**: Client-side tools for testing API endpoints
- **Docker Support**: Multi-stage Docker builds for containerized deployment
- **Live Development**: Spring Boot DevTools for automatic reload during development

### 🛠️ Technical Improvements
- **Modern Kotlin**: Constructor injection, data classes, and latest language features
- **Comprehensive Testing**: Unit tests, integration tests, and MVC tests
- **Production Ready**: Health checks, metrics, and monitoring capabilities
- **Development Tools**: Hot reload, live reload, and debugging utilities

## Objective

Your goal is to enhance and document the existing modern web application by introducing new functionalities. Specifically, you should:

- **Add at least 100 lines** of documentation and code improvements.
- Document your work in the source code and provide explanations in the **REPORT.md** file.
- Follow [Kotlin best practices](https://kotlinlang.org/docs/kotlin-doc.html) for Kotlin files and [GitHub Markdown](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax) for `.md` files.

### Potential New Functionalities

The application already includes REST API endpoints and a modern UI. Consider adding these additional features:

#### Enhanced User Experience
- **Multi-language Support**: Allow users to select their preferred language (e.g., English, Spanish, French) with internationalization (i18n)
- **Time-based Greeting**: Modify the greeting message based on the time of day (e.g., "Good Morning", "Good Afternoon", "Good Evening")
- **Theme Customization**: Allow users to customize the look and feel (dark/light mode, color schemes)
- **User Preferences**: Store user preferences in cookies or local storage

#### Data Management
- **Greeting History**: Log and display the history of greetings with timestamps and user information
- **Statistics Dashboard**: Show usage statistics, popular names, and greeting patterns
- **Data Persistence**: Add a database to store greeting history and user data

#### Advanced Features
- **User Authentication**: Implement basic user authentication with login/logout functionality
- **Rate Limiting**: Add API rate limiting to prevent abuse
- **Caching**: Implement caching for frequently accessed data
- **WebSocket Support**: Real-time updates for greeting history or notifications

#### API Enhancements
- **Additional API Endpoints**: Create endpoints for managing greeting history, user preferences
- **API Versioning**: Implement API versioning for backward compatibility
- **OpenAPI Documentation**: Add Swagger/OpenAPI documentation for the REST API
- **Error Handling**: Enhanced error responses with proper HTTP status codes

#### Development & DevOps
- **GitHub Actions**: Set up a CI/CD pipeline with automated testing and deployment
- **Code Quality**: Add code quality tools (SonarQube, Checkstyle, etc.)
- **Performance Monitoring**: Add application performance monitoring (APM)
- **Logging**: Enhanced logging with structured logging and log aggregation

### What to Document

Your documentation should cover topics such as:

#### Code Documentation
- **Kotlin Code**: Add KDoc comments to classes, functions, and complex logic
- **Configuration**: Document application properties and their purposes
- **API Endpoints**: Document request/response formats and parameters
- **Architecture**: Explain the MVC pattern implementation and component interactions

#### Technical Documentation
- **Build Process**: How to build, test, and deploy the application
- **Technology Stack**: Detailed explanations of Spring Boot, Kotlin, Bootstrap, and other technologies
- **Development Setup**: Step-by-step setup instructions for new developers
- **Testing Strategy**: How tests are organized and what they cover

#### User Documentation
- **API Usage**: Examples of how to use the REST API endpoints
- **Feature Explanations**: How new features work and how to use them
- **Troubleshooting**: Common issues and their solutions

#### Requirements
- **Language**: Documentation must be in English with at least B1 level proficiency
- **Format**: Use proper Markdown formatting for `.md` files
- **Completeness**: Cover all new functionality you add
- **Accuracy**: Ensure all code examples and instructions work correctly

### Bonus Opportunity

Outstanding contributions will earn you a 5% bonus on your individual project score for the _URLShortener_ project. **Students seeking the 5% bonus must request an appointment and defend their work in person.**

#### Bonus Defense Process

1. **Request Appointment**: Contact your instructor to schedule a defense session
2. **Prepare Defense**: Be ready to explain and demonstrate your work
3. **Detailed AI Disclosure**: Provide comprehensive disclosure of all AI assistance used
4. **Technical Demonstration**: Show your code, tests, and documentation
5. **Answer Questions**: Respond to technical questions about your implementation

#### Outstanding Contribution Criteria

**MANDATORY REQUIREMENT**: Proper Git practices are **mandatory** for the 5% bonus. This includes:

- **Meaningful Commit Messages**: Clear, descriptive commit messages that explain what and why
- **Logical Commit History**: Well-organized commits that tell a story of your development process
- **Proper Branching**: Use of feature branches for new functionality
- **No Large Commits**: Avoid committing large amounts of code at once
- **Clean History**: No unnecessary merge commits or messy commit history

Contributions are considered outstanding if they meet the Git requirements AND demonstrate excellence in:

#### Documentation Excellence
- **Reference Quality**: Documentation that can serve as a reference for other students
- **Comprehensive Coverage**: Complete documentation of all features and technical aspects
- **Clear Examples**: Well-documented code examples with explanations

#### Code Quality
- **Test-Driven Development**: New features implemented following TDD practices with comprehensive test coverage
- **Modern Kotlin**: Use of advanced Kotlin features and Spring Boot best practices
- **Clean Architecture**: Well-structured, maintainable code following SOLID principles

#### Advanced Tooling
- **Gradle Expertise**: Advanced Gradle configuration, custom tasks, or build optimizations
- **CI/CD Pipeline**: GitHub Actions workflows for automated testing, building, and deployment
- **Docker Optimization**: Advanced Docker configurations, multi-stage builds, or container orchestration

#### Innovation
- **Creative Features**: Innovative functionality that goes beyond basic requirements
- **Performance Optimization**: Code optimizations that improve application performance
- **Security Enhancements**: Security improvements and best practices implementation

## Development Workflow

### Adding New Features

1. **Implement Changes**: Add your new functionality following the existing patterns

2. **Add Tests**: Write comprehensive tests for your new features

### Code Quality

- Follow Kotlin coding conventions
- Write meaningful commit messages
- Add comprehensive tests for new functionality
- Update documentation as needed
- Use modern Kotlin features (constructor injection, data classes)

### Git Best Practices (Mandatory for 5% Bonus)

#### Commit Message Guidelines
- **Format**: Use imperative mood (e.g., "Add user authentication", not "Added user authentication")
- **Length**: Keep the first line under 50 characters, add details in the body if needed
- **Content**: Explain what the commit does and why (if not obvious)

**Good Examples**:
```
Add REST API endpoint for greeting history
Fix Bootstrap CSS loading issue in production
Update documentation for new Docker features
```

**Bad Examples**:
```
fix
updates
changes
```

#### Commit History Best Practices
- **Atomic Commits**: Each commit should represent one logical change
- **Frequent Commits**: Commit often with small, focused changes
- **Clean History**: Use `git rebase` to clean up your commit history before pushing
- **Feature Branches**: Create branches for new features: `git checkout -b feature/user-authentication`

#### Branching Strategy
```bash
# Create feature branch
git checkout -b feature/your-feature-name

# Make commits
git add .
git commit -m "Add initial feature implementation"

# Clean up history if needed
git rebase -i HEAD~3

# Push to GitHub
git push origin feature/your-feature-name
```

#### Avoiding Common Mistakes
- **No Large Commits**: Don't commit everything at once
- **No Merge Commits**: Use rebase instead of merge when possible
- **No Commit Messages Like "fix"**: Always be descriptive
- **No Committing Build Files**: Use `.gitignore` properly

### Complete the Assignment

1. **Review and Document**: Go through the code and add relevant documentation. Make sure your additions are correct and useful.
2. **Create REPORT.md**: Write a comprehensive report following the structure provided above.
3. **Test Your Changes**: After updating the documentation, ensure everything works by running `./gradlew check` again.
4. **Create Zip File**: Create a zip file of your complete project for Moodle submission:
   ```bash
   # Exclude build directories and IDE files
   zip -r lab1-git-race-submission.zip . -x "build/*" ".gradle/*" ".idea/*" "*.iml" ".DS_Store" "node_modules/*"
   ```
5. **Submit to Moodle**: Upload the zip file to Moodle before the deadline. Your instructor will review your submission and inform you whether you've passed or if further work is needed.
6. **Bonus Defense** (Optional): If you believe your work qualifies for the 5% bonus, request an appointment to defend your work in person.

## Tips for Success

### Getting Started
1. **Explore the Codebase**: Take time to understand the existing code structure before making changes
2. **Run Tests First**: Always run `./gradlew test` to ensure the application works before making modifications
3. **Start Small**: Begin with simple enhancements and gradually add more complex features

### Best Practices
1. **Commit Frequently**: Make small, focused commits with descriptive messages
2. **Test Your Changes**: Write tests for new functionality and ensure all tests pass
3. **Document as You Go**: Add documentation while implementing features, not as an afterthought
4. **Follow Kotlin Conventions**: Use modern Kotlin features and Spring Boot best practices

### Common Pitfalls to Avoid
1. **Breaking Existing Functionality**: Always test that existing features still work after your changes
2. **Incomplete Documentation**: Ensure your documentation is complete and accurate
3. **Poor Git Practices**: This will disqualify you from the 5% bonus
   - Vague commit messages like "fix" or "update"
   - Large commits with multiple unrelated changes
   - Messy commit history with unnecessary merge commits
4. **Ignoring Tests**: Don't skip writing tests for your new features
5. **Incomplete AI Disclosure**: Failing to properly document AI assistance used

### Getting Help
- Use the Moodle Q&A forum for questions and discussions with your instructor
- Check the Spring Boot and Kotlin documentation for technical questions
- Review the existing test files to understand testing patterns
- Use the interactive HTTP debugging tools in the web interface to test your API endpoints

## Submission and Evaluation

### Submission Deadline

The deadline for this assignment is **September 26th**. If you fail to submit by then, you will incur a 20% penalty on your individual project score for the _URLShortener_ project. This penalty only applies if you have not attempted to submit anything of value.

### Submission Requirements

You must submit your work in **two ways**:

1. **Moodle Submission**: Upload a zip file of your complete project to Moodle
2. **GitHub Repository**: Push your changes to the GitHub repository for evaluation

#### Moodle Zip File Submission

Create a zip file containing your complete project and upload it to Moodle. The zip file should include:

- All source code files
- Documentation files (README.md, REPORT.md)
- Test files
- Configuration files
- Any additional files you've created

#### REPORT.md File Requirements

You must create a `REPORT.md` file in your project root that includes:

1. **Description of Changes**: Detailed explanation of all modifications and enhancements you made
2. **AI Disclosure**: Complete disclosure of any AI tools or assistance used during development, including:
    - Specific AI tools used (ChatGPT, GitHub Copilot, etc.)
    - What code or documentation was generated with AI assistance
    - How much of your work was AI-assisted vs. original
    - Any AI-generated code that was modified or adapted
3. **Learning Outcomes**: What you learned from completing this assignment
4. **Technical Decisions**: Explanation of technical choices and their rationale

**Sample REPORT.md Structure**:
```markdown
# Lab 1 Git Race -- Project Report

## Description of Changes
[Detailed description of all changes made]

## Technical Decisions
[Explanation of technical choices made]

## Learning Outcomes
[What you learned from this assignment]

## AI Disclosure
### AI Tools Used
- [List specific AI tools used]

### AI-Assisted Work
- [Describe what was generated with AI assistance]
- [Percentage of AI-assisted vs. original work]
- [Any modifications made to AI-generated code]

### Original Work

- [Describe work done without AI assistance]
- [Your understanding and learning process]
```

Good luck with your assignment!
