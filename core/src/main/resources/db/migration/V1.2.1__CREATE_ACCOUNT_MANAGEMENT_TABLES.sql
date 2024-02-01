CREATE TABLE t_account(
    id SERIAL PRIMARY KEY,
    created_on TIMESTAMP NOT NULL DEFAULT now(),
    created_by INTEGER REFERENCES t_user(id),
    modified_on TIMESTAMP,
    modified_by INTEGER REFERENCES t_user(id),
    is_deleted BOOLEAN DEFAULT FALSE,
    account_name VARCHAR(200),
    account_number VARCHAR(10) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL,
    school_id INTEGER REFERENCES t_student(id)
);

CREATE TABLE t_momo_deposit(
    id SERIAL PRIMARY KEY,
    created_on TIMESTAMP NOT NULL DEFAULT now(),
    created_by INTEGER REFERENCES t_user(id),
    modified_on TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    modified_by INTEGER REFERENCES t_user(id),
    amount_deposit NUMERIC NOT NULL,
    transaction_status VARCHAR(30) NOT NULL,
    msisdn VARCHAR(15) NOT NULL,
    external_reference_id  VARCHAR(100) NOT NULL,
    depositor_name VARCHAR(200)
);

CREATE TABLE t_account_transaction(
    id SERIAL PRIMARY KEY,
    created_on TIMESTAMP NOT NULL DEFAULT now(),
    created_by INTEGER REFERENCES t_user(id),
    modified_on TIMESTAMP,
    modified_by INTEGER REFERENCES t_user(id),
    is_deleted BOOLEAN DEFAULT FALSE,
    account_id INTEGER REFERENCES t_account(id),
    transaction_type VARCHAR(30) NOT NULL,
    non_reversal BOOLEAN DEFAULT FALSE,
    transaction_status VARCHAR(30) NOT NULL,
    note TEXT,
    balance_before NUMERIC NOT NULL,
    balance_after NUMERIC NOT NULL,
    external_transaction_id VARCHAR(100) NOT NULL
);

CREATE TABLE t_cash_flow(
    id SERIAL PRIMARY KEY,
    created_on TIMESTAMP NOT NULL DEFAULT now(),
    created_by INTEGER REFERENCES t_user(id),
    modified_on TIMESTAMP,
    modified_by INTEGER REFERENCES t_user(id),
    is_deleted BOOLEAN DEFAULT FALSE,
    amount NUMERIC NOT NULL,
    from_account_transaction_id INTEGER REFERENCES t_account_transaction(id),
    to_account_transaction_id INTEGER REFERENCES t_account_transaction(id),
    from_account INTEGER REFERENCES t_account(id),
    to_account INTEGER REFERENCES t_account(id),
    cash_flow_type VARCHAR(30) NOT NULL
);