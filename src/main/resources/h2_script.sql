DROP TABLE IF EXISTS `meal`;
DROP TABLE IF EXISTS `restaurant`;

CREATE TABLE `restaurant`
(
    `id`    INT          NOT NULL AUTO_INCREMENT,
    `name`  VARCHAR(255) NOT NULL UNIQUE,
    `votes` INT          NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `meal`
(
    `id`         INT                                               NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(255)                                      NOT NULL,
    `category`   ENUM ('SALAD', 'SOUP', 'MAIN', 'DESERT', 'DRINK') NOT NULL,
    `price`      INT                                               NOT NULL,
    `restaurant` INT                                               NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `restaurant`
        FOREIGN KEY (`restaurant`)
            REFERENCES `restaurant` (`id`)
);

INSERT INTO `restaurant` (`name`, `votes`)
VALUES ('Marcellis', '3');
INSERT INTO `restaurant` (`name`, `votes`)
VALUES ('Phalli Khinkali', '9');

INSERT INTO `meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Ceaser Salad', 'salad', '350', '1');
INSERT INTO `meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Tomato Soup', 'soup', '290', '1');
INSERT INTO `meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Four Seasons pizza', 'main', '560', '1');
INSERT INTO `meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Apple pie', 'desert', '250', '1');
INSERT INTO `meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Latte', 'drink', '350', '1');

INSERT INTO `meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Georgian Salad', 'salad', '290', '2');
INSERT INTO `meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Kharcho Soup', 'soup', '310', '2');
INSERT INTO `meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Chicken Kebab', 'main', '360', '2');
INSERT INTO `meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Chocolate', 'desert', '200', '2');
INSERT INTO `meal` (`name`, `category`, `price`, `restaurant`)
VALUES ('Tea', 'drink', '250', '2');