
USE sport_java;


CREATE TABLE  `MESSAGES` (
    `ID` bigint(20) NOT NULL auto_increment,
    `USER_ID` bigint(20) NOT NULL,
    `CONTENT` varchar(255) character set utf8 NOT NULL,
    PRIMARY KEY  (`ID`),
    KEY `user_ID` USING BTREE (`USER_ID`),
    CONSTRAINT `user_fk` FOREIGN KEY (`USER_ID`) REFERENCES `USERS` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
