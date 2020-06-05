create table `user`
(
    `id`                 bigint primary key AUTO_INCREMENT,
    `username`           varchar(10)  NOT NULL UNIQUE,
    `encrypted_password` varchar(100) NOT NULL,
    `avatar`             varchar(100),
    `created_at`         DateTime,
    `updated_at`         DateTime
)DEFAULT CHARSET=utf8mb4;