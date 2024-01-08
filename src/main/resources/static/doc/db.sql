
CREATE DATABASE IF NOT EXISTS `domain_hunter` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

USE `domain_hunter`;

CREATE TABLE `domain_hunter`.`domain`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `sld`         VARCHAR(16)     NOT NULL COMMENT '二级域名',
    `tld`         VARCHAR(8)      NOT NULL COMMENT '顶级域名',
    `avail`       BOOLEAN         NULL     DEFAULT NULL COMMENT '是否可用',
    `price`       INT UNSIGNED    NULL     DEFAULT NULL COMMENT '价格',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `valid`       BOOLEAN         NOT NULL DEFAULT TRUE COMMENT '是否有效',
    PRIMARY KEY `id` (`id`),
    UNIQUE KEY `sld_tld` (`sld`, `tld`),
    KEY `price` (`price`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_bin COMMENT ='域名';

INSERT INTO `domain_hunter`.`domain` (`id`, `sld`, `tld`)
VALUES (1, 'ice', 'run');

CREATE USER 'domain_hunter'@'%' IDENTIFIED BY 'domain_hunter';

GRANT ALL PRIVILEGES ON domain_hunter.* TO 'domain_hunter'@'%';

FLUSH PRIVILEGES;
