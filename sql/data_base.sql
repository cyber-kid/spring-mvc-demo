CREATE DATABASE app;

USE app;

CREATE USER 'app'@'localhost' IDENTIFIED BY 'test';

GRANT ALL PRIVILEGES ON app.* TO 'app'@'localhost';