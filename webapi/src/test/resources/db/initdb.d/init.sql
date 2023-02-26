create table `product`
(
    id         bigint auto_increment primary key,
    product_id varchar(255) not null unique key,
    title      varchar(100) not null,
    price      bigint       not null,
    disabled   boolean      not null,
    created_at timestamp    not null,
    updated_at timestamp
);

create index `product_price_index`
    on `product` (price);

create index `product_title_index`
    on `product` (title);

create index `product_created_at_index`
    on `product` (created_at desc);

create table `wallet`
(
    id         bigint auto_increment primary key,
    balance    bigint    not null,
    created_at timestamp not null,
    updated_at timestamp
);

create table `wallet_transaction`
(
    id         bigint auto_increment primary key,
    receipt    binary(16)  not null,
    wallet_id  bigint      not null,
    amount     bigint      not null,
    type       varchar(50) not null,
    created_at timestamp   not null,
    updated_at timestamp
);

create index `wallet_transaction_wallet_id_index`
    on `wallet_transaction` (wallet_id);

create table `purchase`
(
    id                    bigint auto_increment primary key,
    wallet_id             bigint       not null,
    wallet_transaction_id bigint       not null,
    product_id            varchar(255) not null,
    created_at            timestamp    not null,
    updated_at            timestamp
);

create index `purchase_wallet_id_wallet_transaction_id_index`
    on `purchase` (wallet_id asc, wallet_transaction_id desc);

insert into `wallet`(balance, created_at)
values (100000, now());

insert into `product` (product_id, title, price, disabled, created_at)
values ('coffee.1', 'test_coffee_1', 1000, false, now()),
       ('coffee.2', 'test_coffee_2', 2000, false, now()),
       ('coffee.3', 'test_coffee_3', 3000, false, now()),
       ('coffee.4', 'test_coffee_4', 4000, false, now()),
       ('coffee.5', 'test_coffee_5', 5000, false, now()),
       ('coffee.6', 'test_coffee_6', 6000, false, now()),
       ('coffee.7', 'test_coffee_7', 7000, false, now()),
       ('coffee.8', 'test_coffee_8', 8000, false, now()),
       ('coffee.9', 'test_coffee_9', 9000, false, now()),
       ('coffee.10', 'test_coffee_10', 10000, false, now()),
       ('coffee.11', 'test_coffee_11', 11000, false, now()),
       ('coffee.12', 'test_coffee_12', 12000, false, now())
;

