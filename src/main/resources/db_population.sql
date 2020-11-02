DROP TABLE IF EXISTS `jr`.`meal`;
DROP TABLE IF EXISTS `jr`.`restaurant`;

CREATE TABLE `jr`.`restaurant`
(
    `id`    INT          NOT NULL AUTO_INCREMENT,
    `name`  VARCHAR(255) NOT NULL,
    `votes` INT          NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `restaurant_name_idx` (`name` ASC)
);

CREATE TABLE `jr`.`meal`
(
    `id`         INT                                               NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(255)                                      NOT NULL,
    `category`   ENUM ('SALAD', 'SOUP', 'MAIN', 'DESERT', 'DRINK') NOT NULL,
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

INSERT INTO `jr`.`restaurant` (`name`, `votes`)
VALUES ('Marcellis', '3');
INSERT INTO `jr`.`restaurant` (`name`, `votes`)
VALUES ('Phalli Khinkali', '9');

INSERT INTO `jr`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Ceaser Salad', 'salad', '350', '1');
INSERT INTO `jr`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Tomato Soup', 'soup', '290', '1');
INSERT INTO `jr`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Four Seasons pizza', 'main', '560', '1');
INSERT INTO `jr`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Apple pie', 'desert', '250', '1');
INSERT INTO `jr`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Latte', 'drink', '350', '1');

INSERT INTO `jr`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Georgian Salad', 'salad', '290', '2');
INSERT INTO `jr`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Kharcho Soup', 'soup', '310', '2');
INSERT INTO `jr`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Chicken Kebab', 'main', '360', '2');
INSERT INTO `jr`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Chocolate', 'desert', '200', '2');
INSERT INTO `jr`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Tea', 'drink', '250', '2');
