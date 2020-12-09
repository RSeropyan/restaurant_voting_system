# DROP SCHEMA IF EXISTS `project`;
#
# CREATE SCHEMA `project` DEFAULT CHARACTER SET utf8 ;

DROP TABLE IF EXISTS `project`.`meal`;
DROP TABLE IF EXISTS `project`.`restaurant`;

CREATE TABLE `project`.`restaurant`
(
    `id`    INT          NOT NULL AUTO_INCREMENT,
    `name`  VARCHAR(255) NOT NULL,
    `votes` INT          NOT NULL,
    CONSTRAINT `unique_name` UNIQUE (`name`),
    PRIMARY KEY (`id`)
);

CREATE TABLE `project`.`meal`
(
    `id`         INT                                               NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(255)                                      NOT NULL,
    `category`   ENUM ('SALAD', 'SOUP', 'MAIN', 'DESERT', 'DRINK') NOT NULL,
    `price`      INT                                               NOT NULL,
    `restaurant` INT                                               NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `unique_name_category_restaurant` UNIQUE (`name`, `category`, `restaurant`),
    FOREIGN KEY (`restaurant`) REFERENCES `project`.`restaurant` (`id`)
);

INSERT INTO `project`.`restaurant` (`name`, `votes`)
VALUES ('Marcellis', '3');
INSERT INTO `project`.`restaurant` (`name`, `votes`)
VALUES ('Phalli Khinkali', '9');

INSERT INTO `project`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Ceaser Salad', 'salad', '350', '1');
INSERT INTO `project`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Tomato Soup', 'soup', '290', '1');
INSERT INTO `project`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Four Seasons pizza', 'main', '560', '1');
INSERT INTO `project`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Apple pie', 'desert', '250', '1');
INSERT INTO `project`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Latte', 'drink', '350', '1');

INSERT INTO `project`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Georgian Salad', 'salad', '290', '2');
INSERT INTO `project`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Kharcho Soup', 'soup', '310', '2');
INSERT INTO `project`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Chicken Kebab', 'main', '360', '2');
INSERT INTO `project`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Chocolate', 'desert', '200', '2');
INSERT INTO `project`.`meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Tea', 'drink', '250', '2');
