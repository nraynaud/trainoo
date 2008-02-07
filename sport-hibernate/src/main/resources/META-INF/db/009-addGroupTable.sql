CREATE TABLE `GROUPS` (
    `ID` bigint(20) NOT NULL auto_increment,
    `CREATION_DATE` datetime NOT NULL,
    `OWNER_ID` bigint(20) NOT NULL,
    `NAME` VARCHAR(255) character set utf8 NOT NULL,
    `DESCRIPTION` TEXT character set utf8,
    PRIMARY KEY  (`ID`),
    UNIQUE KEY `NAME` (`NAME`),
    KEY `group_owner_ID` USING BTREE (`OWNER_ID`),
    CONSTRAINT `group_owner_fk` FOREIGN KEY (`OWNER_ID`) REFERENCES `USERS` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `GROUP_USER` (
    `USER_ID` bigint(20) NOT NULL,
    `GROUP_ID` bigint(20) NOT NULL,
    `JOINING_DATE` datetime NOT NULL,
    PRIMARY KEY  (`USER_ID`, `GROUP_ID`),
    KEY `group_user_user_ID` USING BTREE (`USER_ID`),
    CONSTRAINT `group_user_user_fk` FOREIGN KEY (`USER_ID`) REFERENCES `USERS` (`ID`),
    KEY `group_user_group_ID` USING BTREE (`GROUP_ID`),
    CONSTRAINT `group_user_group_fk` FOREIGN KEY (`GROUP_ID`) REFERENCES `GROUPS` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
