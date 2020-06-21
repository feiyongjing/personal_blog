create table `blog`
(
    `id`          bigint primary key AUTO_INCREMENT,
    `user_id`     bigint,
    `title`       varchar(100) NOT NULL UNIQUE,
    `description` varchar(100),
    `content`     varchar(10000) NOT NULL,
    `created_at`  DateTime,
    `updated_at`  DateTime
) DEFAULT CHARSET = utf8mb4;
INSERT `blog` (`user_id`, title, description,content) VALUES
( '1', 'Lennon', '456','789'),
( '2', 'Casey', '123' ,'123'),
( '2', 'River', '123','456');﻿