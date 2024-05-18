CREATE DATABASE bank;

\c bank

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS account (
    account_id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    customer_id UUID NOT NULL,
    country VARCHAR(64) NOT NULL
);

CREATE TYPE currency_type AS ENUM ('EUR', 'SEK', 'GBP', 'USD');

CREATE TABLE IF NOT EXISTS balance (
    account_id UUID NOT NULL,
    amount NUMERIC DEFAULT 0.0 NOT NULL,
    currency currency_type NOT NULL,
    PRIMARY KEY (account_id, currency),
    FOREIGN KEY (account_id) REFERENCES account(account_id)
);

CREATE TYPE direction_type AS ENUM ('IN', 'OUT');

CREATE TABLE IF NOT EXISTS transaction (
    transaction_id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    account_id UUID NOT NULL,
    amount NUMERIC NOT NULL,
    currency currency_type NOT NULL,
    direction direction_type NOT NULL,
    description TEXT,
    FOREIGN KEY (account_id) REFERENCES account(account_id)
);