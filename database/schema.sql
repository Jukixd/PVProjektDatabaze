CREATE DATABASE IF NOT EXISTS board_game_cafe;
USE board_game_cafe;

CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20)
);

CREATE TABLE game (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    genre ENUM('STRATEGIE', 'RODINNA', 'PARTY', 'RPG') NOT NULL,
    rental_price FLOAT NOT NULL,
    is_available BOOLEAN DEFAULT TRUE
);

CREATE TABLE cafe_table (
    id INT AUTO_INCREMENT PRIMARY KEY,
    seats INT NOT NULL,
    location_description VARCHAR(255)
);

CREATE TABLE rental (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    rental_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_price FLOAT,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE rental_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rental_id INT NOT NULL,
    game_id INT NOT NULL,
    FOREIGN KEY (rental_id) REFERENCES rental(id),
    FOREIGN KEY (game_id) REFERENCES game(id)
);