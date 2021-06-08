INSERT INTO users(email, name, password, is_admin)
VALUES ('olegvynnyk@gmail.com', 'Oleg Vynnyk', '1111', true),
       ('justinbieber@gmail.com', 'Justin Bieber', '1111', false),
       ('britneyspears@gmail.com', 'Britney Spears', '1111', false);

INSERT INTO clients(user_id, status)
VALUES (2, 'ACTIVE'),
       (3, 'ACTIVE');

UPDATE users SET client_id = 1 WHERE id = 2;
UPDATE users SET client_id = 2 WHERE id = 3;

INSERT INTO accounts(id, client_id, name, balance, status, iban)
VALUES (1, 1, 'salary', 2500, 'ACTIVE', 'UA2532820912340000056789'),
       (2, 2, 'Sugar Daddy', 40000, 'ACTIVE', 'UA2532820112340010056781'),
       (3, 2, 'Salary', 15223, 'ACTIVE', 'UA2592820912940010999781');


INSERT INTO accounts(id, client_id, name, balance, status, iban)
VALUES (4, 1, 'account_4', 1000, 'BLOCKED', 'UA00000_4'),
       (5, 1, 'account_5', 1000, 'ACTIVE', 'UA00000_5'),
       (6, 1, 'account_6', 1000, 'ACTIVE', 'UA00000_6'),
       (7, 1, 'account_7', 1000, 'ACTIVE', 'UA00000_7'),
       (8, 1, 'account_8', 1000, 'ACTIVE', 'UA00000_8'),
       (9, 1, 'account_9', 1000, 'ACTIVE', 'UA00000_9'),
       (10, 1, 'account_10', 1000, 'ACTIVE', 'UA00000_10'),
       (11, 1, 'account_11', 1000, 'ACTIVE', 'UA00000_11'),
       (12, 1, 'account_12', 1000, 'ACTIVE', 'UA00000_12'),
       (13, 1, 'account_13', 1000, 'ACTIVE', 'UA00000_13'),
       (14, 1, 'account_14', 1000, 'ACTIVE', 'UA00000_14'),
       (15, 1, 'account_15', 1000, 'ACTIVE', 'UA00000_15'),
       (16, 1, 'account_16', 1000, 'ACTIVE', 'UA00000_16'),
       (17, 1, 'account_17', 1000, 'ACTIVE', 'UA00000_17'),
       (18, 1, 'account_18', 1000, 'ACTIVE', 'UA00000_18'),
       (19, 1, 'account_19', 1000, 'ACTIVE', 'UA00000_19'),
       (20, 1, 'account_20', 1000, 'ACTIVE', 'UA00000_20'),
       (21, 1, 'account_21', 1000, 'ACTIVE', 'UA00000_21'),
       (22, 1, 'account_22', 1000, 'ACTIVE', 'UA00000_22');

INSERT INTO credit_cards(id, card_number, account_id, is_expired)
VALUES (1, '0000111122223333', 1, false),
       (2, '1111222233334444', 2, false),
       (3, '2222333344445555', 2, false);

UPDATE accounts SET credit_card_id = 1 WHERE id = 1;
UPDATE accounts SET credit_card_id = 2 WHERE id = 2;
UPDATE accounts SET credit_card_id = 3 WHERE id = 3;

INSERT INTO payments (id, account_id, amount, status, receiver_iban, payment_date, details)
VALUES (1, 2, 1000, 'SENT', 'UA2532820112340010056781', '2021-03-12', 'Cat food'),
       (2, 2, 2499, 'SENT', 'UA2532820112340010056781', '2021-04-19', ''),
       (3, 2, 23, 'SENT', 'UA2532820112340010056781', '2021-05-01', ''),
       (4, 2, 150, 'SENT', 'UA2532820112340010056781', '2021-05-15', ''),
       (5, 2, 349, 'CREATED', 'UA2532820112340010056781', '2021-05-15', 'aaaaaaaaaaa'),
       (6, 3, 500, 'SENT', 'UA2512320112341230056123', '2021-04-19', 'Burger King'),
       (7, 3, 349, 'SENT', 'UA2532820112340010051111', '2021-05-15', 'McDonalds'),
       (8, 2, 99999, 'CREATED', 'UA2532820112340010056781', '2021-05-15', 'too big amount');

INSERT INTO unblock_requests (id, action_date, creation_date, account_id, client_id)
 VALUES (1, '2021-05-23 15:30:58.821000', '2021-05-23 15:30:55.991000', 2, 2);
INSERT INTO unblock_requests (id, action_date, creation_date, account_id, client_id)
VALUES (2, '2021-05-23 15:31:08.629000', '2021-05-23 15:31:06.538000', 2, 2);
INSERT INTO unblock_requests (id, action_date, creation_date, account_id, client_id)
VALUES (3, '2021-05-23 15:31:08.629000', '2021-05-23 15:31:06.538000', 2, 2);
INSERT INTO unblock_requests (id, action_date, creation_date, account_id, client_id)
VALUES (4, '2021-05-23 15:31:08.629000', '2021-05-23 15:31:06.538000', 2, 2);
INSERT INTO unblock_requests (id, action_date, creation_date, account_id, client_id)
VALUES (5, '2021-05-23 15:31:08.629000', '2021-05-23 15:31:06.538000', 2, 2);
INSERT INTO unblock_requests (id, action_date, creation_date, account_id, client_id)
VALUES (6, '2021-05-23 15:31:08.629000', '2021-05-23 15:31:06.538000', 2, 2);
INSERT INTO unblock_requests (id, action_date, creation_date, account_id, client_id)
VALUES (7, '2021-05-23 15:31:08.629000', '2021-05-23 15:31:06.538000', 2, 2);
