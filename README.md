# Spring Boot JWT Authentication - Comparison Project

This project contains two Spring Boot applications using JWT authentication with PostgreSQL, for my curiosity and eager to build and test.

1. Barebone JWT
- Manual JWT handling (no Spring security)
- Custom token validation
- Manual password hashing with BCrypt
- Runs on port 8081

2. Spring Security JWT
- Spring security integration
- Custom JwtAuthenticationFilter
- BCrypt with Passwordencode
- Runs on port 8082

Both applications:
- PostgreSQL
- Seeds SuperAdmin user
- Provide /register, /login and /me endpoints
- Swagger/OpenAPI documentation


## Tech Stack

- Java 25
- Spring Boot
- Spring Data JPA
- PostgreSQL
- JJWT
- (second app) Spring Security
- Lombok
- OpenAPI / Swagger


## Setup

### Database

<i>File table.sql, in root folder database, contain queries for creating database and table.<i>
1. Create two Database
- CREATE DATABASE jwt_basic;
- CREATE DATABASE jwt_security;
2. Create same table in both db's
- CREATE TABLE users (<br>
    id BIGSERIAL PRIMARY KEY,<br>
    username VARCHAR(100) NOT NULL UNIQUE,<br>
    password VARCHAR(255) NOT NULL,<br>
    role VARCHAR(50) NOT NULL DEFAULT 'USER',<br>
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP<br>
);<br>

### Environment varialbles

<i>Both project use local environment variables for <b>JWT_SECRET, POSTGRESQL_PASSWORD, POSTGRESQL_USERNAME</b>.<i><br>
<i>These can be set by: Run -> Edit Configurations -> Select project

<img width="868" height="583" alt="Env variables" src="https://github.com/user-attachments/assets/4bb840e2-3b76-4eb0-a4b8-8ba13c5bd72f" />

