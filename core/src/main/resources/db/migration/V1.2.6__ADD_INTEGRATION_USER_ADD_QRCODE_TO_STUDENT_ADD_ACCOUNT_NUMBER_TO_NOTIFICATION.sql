ALTER TABLE t_student ADD COLUMN qr_code VARCHAR(200);
ALTER TABLE t_notification ADD COLUMN account_number VARCHAR(20);
INSERT INTO t_account (account_name, account_number, status, current_balance)
values
    ('SMS COLLECTION', 'SMS256C', 'ACTIVE', 0);

UPDATE t_account SET account_number = 'SMS TOP UP' WHERE account_number = 'SMS256';