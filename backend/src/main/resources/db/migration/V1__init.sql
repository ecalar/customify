CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    owner_id BIGINT NOT NULL,
    model_path VARCHAR(500),
    base_price DECIMAL(10,2),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE product_option (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    default_value VARCHAR(255),
    price_supplement DECIMAL(10,2) DEFAULT 0.00,
    CONSTRAINT fk_option_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE TABLE option_choices (
    option_id BIGINT NOT NULL,
    choice VARCHAR(255) NOT NULL,
    CONSTRAINT fk_choice_option FOREIGN KEY (option_id) REFERENCES product_option(id) ON DELETE CASCADE
);

CREATE TABLE configuration (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    data JSON NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_configuration_product FOREIGN KEY (product_id) REFERENCES product(id)
);
