-- =====================================================
-- gjun POS System Database Initialization Script
-- Compatible with MySQL 5.7 / 8.x
-- =====================================================
DROP DATABASE IF EXISTS gjun;
CREATE DATABASE gjun CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gjun;

-- =====================================================
-- Member Table
-- =====================================================
CREATE TABLE member (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(50),
    role ENUM('admin', 'user') DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =====================================================
-- Product Table
-- =====================================================
CREATE TABLE product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT DEFAULT 0,
    category VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =====================================================
-- Orders Table
-- =====================================================
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- Order Items Table
-- =====================================================
CREATE TABLE order_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- Purchases Table (進貨資料)
-- =====================================================
CREATE TABLE purchases (
    purchase_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    cost DECIMAL(10,2),
    purchase_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- View: sales_summary
-- =====================================================
CREATE OR REPLACE VIEW sales_summary AS
SELECT 
    DATE(o.order_date) AS sale_date,
    SUM(oi.quantity * oi.price) AS total_sales,
    COUNT(DISTINCT o.order_id) AS order_count
FROM orders o
JOIN order_items oi ON o.order_id = oi.order_id
GROUP BY DATE(o.order_date)
ORDER BY sale_date DESC;

-- =====================================================
-- Seed Data
-- =====================================================
INSERT INTO member (username, password, name, email, phone, role) VALUES
('admin', '1234', 'Administrator', 'admin@gjun.com', '0912345678', 'admin'),
('user1', '1234', 'Test User', 'user1@gjun.com', '0922334455', 'user');

INSERT INTO product (name, price, stock, category) VALUES
('Laptop Pro 15', 35999.00, 10, 'Electronics'),
('Wireless Mouse', 499.00, 100, 'Accessories');

INSERT INTO purchases (product_id, quantity, cost) VALUES
(1, 10, 30000.00),
(2, 100, 300.00);

INSERT INTO orders (member_id, total_amount) VALUES (2, 36997.00);
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(1, 1, 1, 35999.00),
(1, 2, 2, 499.00);
