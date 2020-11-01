DROP TABLE IF EXISTS `jr`.`restaurant`;

CREATE TABLE `jr`.`restaurant`
(
    `id`    INT          NOT NULL AUTO_INCREMENT,
    `name`  VARCHAR(255) NOT NULL,
    `votes` INT          NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `restaurant_name_idx` (`name` ASC)
);

DROP TABLE IF EXISTS `jr`.`meal`;

CREATE TABLE `jr`.`meal`
(
    `id`         INT                                               NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(255)                                      NOT NULL,
    `category`   ENUM ('salad', 'soup', 'main', 'desert', 'drink') NOT NULL,
    `price`      INT                                               NOT NULL,
    `restaurant` INT                                               NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `restaurant_idx` (`restaurant` ASC),
    CONSTRAINT `restaurant`
        FOREIGN KEY (`restaurant`)
            REFERENCES `jr`.`restaurant` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);
