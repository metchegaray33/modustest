
CREATE TABLE IF NOT EXISTS `feriados` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) DEFAULT NULL,
  `fecha` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `suppliers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255)  NOT NULL,
  `address` varchar(255) NOT NULL,
  `contact` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `supplier_id` bigint NOT NULL,
  `transaction_id` varchar(255)  NOT NULL,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `content` LONGTEXT  NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;



