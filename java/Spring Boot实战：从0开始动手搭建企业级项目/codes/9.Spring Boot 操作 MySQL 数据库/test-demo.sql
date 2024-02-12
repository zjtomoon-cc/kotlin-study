DROP TABLE IF EXISTS `jdbc_test`;

CREATE TABLE `jdbc_test` (
  `ds_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `ds_type` varchar(100) DEFAULT NULL COMMENT '数据源类型',
  `ds_name` varchar(100) DEFAULT NULL COMMENT '数据源名称',
  PRIMARY KEY (`ds_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8;

/*Data for the table `jdbc_test` */

insert  into `jdbc_test`(`ds_id`,`ds_type`,`ds_name`) values (1,'com.zaxxer.hikari.HikariDataSource','hikari数据源'),(2,'org.apache.commons.dbcp2.BasicDataSource','dbcp2数据源');


DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '登录名',
  `password` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Data for the table `jdbc_test` */

insert  into `tb_user`(`id`,`name`,`password`) values (1,'Spring Boot','123456'),(2,'MyBatis','123456'),(3,'Thymeleaf','123456'),(4,'Java','123456'),(5,'MySQL','123456'),(6,'IDEA','123456');