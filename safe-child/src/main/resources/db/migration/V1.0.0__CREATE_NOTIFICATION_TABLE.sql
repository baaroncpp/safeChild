CREATE TABLE IF NOT EXISTS t_notification(
    id BIGSERIAL primary key,
    created_on timestamp not null default now(),
    receiver VARCHAR(15),
    sender VARCHAR(50),
    message TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    status_note TEXT,
    transaction_id TEXT,
    external_transaction_id VARCHAR(20)
);