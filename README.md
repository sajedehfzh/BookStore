
# Book API

This is a demo project for a Spring Boot Book API.

## Prerequisites

- Java 17
- Maven

## How to Run the Application

1. **Package the application:**
   ```bash
   ./mvnw clean install
   ```
2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```   
The application will be running on `http://localhost:8080`.

## H2 Database

This project uses an H2 in-memory database, which is portable, fast, and lightweight.

### Accessing the H2 Console

To access the H2 database console and check the data, config is in the `src/main/resources/application.properties` file.


1. **Access the console:**

   Once the application is running, you can access the H2 console in your browser at:

   `http://localhost:8080/h2-console`

   Use the following credentials to log in:

   - **Driver Class:** `org.h2.Driver`
   - **JDBC URL:** `jdbc:h2:file:./data/bookdb`
   - **User Name:** `sa`
   - **Password:** (leave blank)

## Project Structure

The project follows a layered architecture:

- **`controller`**: Handles incoming HTTP requests and responses.
- **`service`**: Contains the business logic.
- **`repository`**: Manages data access and interaction with the database.
- **`model`**: Defines the data entities.
- **`dto`**: Data Transfer Objects for API requests and responses.
- **`exception`**: Custom exception handlers.
- **`converter`**: Converts between different data types (e.g., for database columns).
- **`validator`**: Custom validation logic.

## API Collection

You can find an API collection in the `BookStore.postman_collection.json` file. You can import this file into an application like Postman to see examples of requests and responses for the API. 