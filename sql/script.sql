-- ==========================================================
-- PROJEKT: Board Game Cafe Manager
-- AUTOR:   Tomáš Majer
-- EMAIL:   tomasxgolds@gmail.com
-- DATUM:   8.01.2026
-- ==========================================================

-- 1. Vytvoření databáze a uživatele
CREATE DATABASE IF NOT EXISTS board_game_cafe;
USE board_game_cafe;

DROP VIEW IF EXISTS view_customer_stats;
DROP VIEW IF EXISTS view_genre_stats;
DROP TABLE IF EXISTS rental_item;
DROP TABLE IF EXISTS rental;
DROP TABLE IF EXISTS cafe_table;
DROP TABLE IF EXISTS game;
DROP TABLE IF EXISTS customer;


-- 2. DDL - Vytvoření tabulek
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
    table_id INT NOT NULL,
    rental_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_price FLOAT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (table_id) REFERENCES cafe_table(id) -- <--- VAZBA
);

CREATE TABLE rental_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rental_id INT NOT NULL,
    game_id INT NOT NULL,
    FOREIGN KEY (rental_id) REFERENCES rental(id),
    FOREIGN KEY (game_id) REFERENCES game(id)
);


-- 3. Views (Pohledy)
CREATE VIEW view_customer_stats AS
SELECT c.name, COUNT(r.id) as rental_count, SUM(r.total_price) as total_spent
FROM customer c
JOIN rental r ON c.id = r.customer_id
GROUP BY c.id, c.name
ORDER BY total_spent DESC;

CREATE VIEW view_genre_stats AS
SELECT g.genre, COUNT(ri.id) as rental_count, AVG(g.rental_price) as avg_price
FROM game g
JOIN rental_item ri ON g.id = ri.game_id
GROUP BY g.genre
ORDER BY rental_count DESC;