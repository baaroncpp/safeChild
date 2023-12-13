ALTER TABLE t_account ADD COLUMN current_balance NUMERIC NOT NULL;
ALTER TABLE t_account ADD COLUMN is_school_account BOOLEAN DEFAULT FALSE;

INSERT INTO t_account (account_name, account_number, status, current_balance)
values
    ('SMS MAIN ACCOUNT', 'SMS256', 'ACTIVE', 0);