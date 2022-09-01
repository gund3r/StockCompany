
CREATE TABLE IF NOT EXISTS stock
    (id BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS good
    (id BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    vendor_code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL UNIQUE,
    last_purchase_price INT NOT NULL,
    last_sale_price INT NOT NULL,
    );

CREATE TABLE IF NOT EXISTS stock_good
    (stock_id BIGINT NOT NULL,
    good_id BIGINT NOT NULL,
    amount INT NOT NULL,
    PRIMARY KEY (stock_id, good_id),
    FOREIGN KEY (stock_id) REFERENCES stock(id),
    FOREIGN KEY (good_id) REFERENCES good(id)
    );

CREATE TABLE IF NOT EXISTS sale_goods_document
    (id BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
     document_number INT NOT NULL,
     stock_id_from BIGINT NOT NULL,
     sale_goods_id BIGINT NOT NULL,
     sale_goods_amount INT NOT NULL,
     price INT NOT NULL
    );

CREATE TABLE IF NOT EXISTS new_goods_document
    (id BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
     document_number INT NOT NULL,
     stock_id BIGINT NOT NULL,
     new_goods_id BIGINT NOT NULL,
     new_goods_amount INT NOT NULL,
     price INT NOT NULL
    );

CREATE TABLE IF NOT EXISTS moving_goods_document
    (id BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
     document_number INT NOT NULL,
     stock_id_from BIGINT NOT NULL,
     stock_id_to BIGINT NOT NULL,
     moving_goods_id BIGINT NOT NULL
    );