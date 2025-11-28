DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    course VARCHAR(255) NOT NULL,
    age INT NOT NULL CHECK (age > 0),
    password VARCHAR(255) NOT NULL DEFAULT '1234',
    dataCreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dataUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    image_path VARCHAR(500) NULL
);

INSERT INTO customers (name, description, course, age, password, dataCreated, dataUpdated)VALUES ('John Doe', ' ', 'DAM2', 25, '1234', '2023-03-15 10:30:00', '2023-03-15 10:30:00');
INSERT INTO customers (name, description, course, age, password, dataCreated, dataUpdated) VALUES ('Jane Smith', ' ', 'DAW1', 19, '1234', '2024-06-22 14:45:00', '2024-06-22 14:45:00');
INSERT INTO customers (name, description, course, age, password, dataCreated, dataUpdated) VALUES ('Bob Johnson', ' ', 'ASIX2', 28, '1234', '2025-01-08 09:15:00', '2025-01-08 09:15:00');
