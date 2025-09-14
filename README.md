# TESTIMX QA Quiz – Full‑stack starter

This repository contains a minimal yet complete starting point for the
**TESTIMX QA Quiz** application.  The goal of this project is to
provide a modern back‑end that exposes the data and functions
required by the mobile clients (iOS/Android) and web UIs supplied
with this task.  All server functionality is encapsulated in a
Spring Boot application that talks to a MySQL database.  The
front‑end artefacts (HTML and assets) can be found under the
`frontend/` folder and can be served by a static file server or
integrated into a mobile WebView.

## Features

The back‑end implements the following high level requirements:

* **Content management** – administrators can create, read,
  update and delete categories, questions and answer options.  Each
  question stores four options along with a flag indicating which
  option is correct as well as a rich explanation to display when
  the user gets a question wrong.  Bulk import/export in CSV or
  JSON can be added by extending the service layer.

* **Learning experience** – the `/api/quizzes/start` endpoint
  begins a quiz for the authenticated user and returns a series
  of questions with their options.  The
  `/api/quizzes/{sessionId}/answer` endpoint accepts user
  answers, immediately evaluates correctness and returns both the
  coloured result and an explanation if the answer was wrong.

* **Measurement and reporting** – each quiz session is
  persisted together with per‑question results.  Endpoints under
  `/api/history` expose aggregated progress such as the number of
  quizzes taken, average score and streak.  A separate endpoint
  provides the history of past quiz attempts with timestamps and
  scores by category.

* **Identity and authorisation** – users can register or log in
  through the `/api/auth/signup` and `/api/auth/signin` endpoints.
  Passwords are stored securely using a BCrypt encoder.  Upon
  successful login the server returns a JWT that must be provided
  in the `Authorization` header for all subsequent requests.  Roles
  (`ROLE_STUDENT` and `ROLE_ADMIN`) control access to protected
  endpoints (for example, only administrators may modify
  categories and questions).

* **Scalability and quality** – the Spring configuration makes it
  trivial to enable caching, paging and rate limiting via
  additional annotations should the application need to handle
  larger data sets.  All user input is validated and errors are
  returned as structured JSON with appropriate status codes.  A
  global exception handler simplifies error reporting and ensures
  uncaught exceptions are logged.

## Project layout

```
testimx-qa-quiz/
├── backend/               # Spring Boot application
│   ├── pom.xml            # Maven build descriptor
│   └── src/
│       ├── main/java/com/testimx/qaquiz/
│       │   ├── Application.java              # Boot entry point
│       │   ├── config/                      # Security & JWT config
│       │   ├── controller/                  # REST controllers
│       │   ├── dto/                         # Request/response payloads
│       │   ├── model/                       # JPA entities
│       │   ├── repository/                  # Spring Data repositories
│       │   └── service/                     # Business logic
│       └── main/resources/
│           └── application.properties       # Data source, JWT secret…
├── frontend/               # Static HTML UI (provided in the task)
│   ├── index.html          # Landing page
│   ├── categories.html     # Category selection screen
│   ├── quiz.html           # Quiz questions screen
│   ├── results.html        # Score summary screen
│   ├── progress.html       # Progress & history
│   └── settings.html       # Application settings
└── README.md               # This file
```

## Getting started

1. **Database** – install MySQL and create a database called
   `testimx_db` (or adjust the `spring.datasource.url` in
   `backend/src/main/resources/application.properties`).  Configure
   the username and password accordingly.  When the application
   starts for the first time it will automatically create the
   necessary tables.

2. **Build the back‑end** – from the `backend/` directory run
   `mvn clean package`.  This will download dependencies, compile
   the code and produce a runnable jar in the `target/` folder.

3. **Run the server** – execute the JAR with `java -jar
   target/backend-0.0.1-SNAPSHOT.jar`.  The API will be
   available on `http://localhost:8080`.  To enable hot reload and
   easier development you can also use `mvn spring-boot:run`.

4. **Use the API** – send a `POST` request to `/api/auth/signup`
   with JSON containing `username`, `email` and `password` to
   register a new user.  Then call `/api/auth/signin` to obtain a
   JWT.  Include this token in the `Authorization: Bearer <token>`
   header for all authenticated requests such as `/api/categories`,
   `/api/questions` or `/api/quizzes/start`.

5. **Serve the front‑end** – the static HTML files under
   `frontend/` can be opened directly in a browser or served from
   any web server.  They are intended as reference templates for
   building the mobile UI in a WebView or with a hybrid framework.

## Extending the system

This starter provides a solid foundation but leaves plenty of room
for enhancement.  You could add:

* CSV/JSON import/export endpoints for questions and categories.
* Support for multiple languages by adding a `language` field to
  questions and categories and returning the appropriate
  translation based on an `Accept‑Language` header.
* A GraphQL API alongside or instead of the REST controllers.
* Caching with Spring’s `@Cacheable` and a concurrent rate limiting
  filter.

Contributions and adaptations are welcome—this project is intended
to be extended and tailored to your needs.