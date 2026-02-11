--Run these in pgAdmin

--For bare bone application
CREATE DATABASE jwt_basic;

--For Spring security version
CREATE DATABASE jwt_security;

--Same table in both db's
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);