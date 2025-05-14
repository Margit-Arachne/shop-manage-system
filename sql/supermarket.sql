CREATE TABLE fruits (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    price DOUBLE,
    unit VARCHAR(10),
    image_path VARCHAR(255)
);

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(255)
);

INSERT INTO fruits(id, name)
value (1,'苹果');
