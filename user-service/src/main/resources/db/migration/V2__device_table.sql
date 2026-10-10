CREATE TABLE `devices` (
    `id` BIGINT NOT NULL AUTO_INCREMENT ,
    `name` VARCHAR(100) NOT NULL,
    `type` VARCHAR(100),
    `location` VARCHAR(100) NOT NULL,
    `user_id` BIGINT,
    PRIMARY KEY (`id`),
    KEY `idx_device_user_id` (`user_id`),
    CONSTRAINT `fk_device_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;