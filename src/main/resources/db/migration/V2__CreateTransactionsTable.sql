CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    merchant_request_id VARCHAR(255),
    checkout_request_id VARCHAR(255),
    mpesa_receipt_number VARCHAR(100),
    phone_number VARCHAR(50),
    amount DOUBLE PRECISION,
    transaction_date BIGINT,
    result_desc TEXT,
    result_code INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
