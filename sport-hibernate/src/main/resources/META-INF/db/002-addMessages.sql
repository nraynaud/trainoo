
USE sport_java;




CREATE TABLE  `MESSAGES` (
    `ID` bigint(20) NOT NULL auto_increment,
    `USER_ID` bigint(20) NOT NULL,
    `WORKOUT_ID` bigint(20),
    `CONTENT` varchar(255) character set utf8 NOT NULL,
    PRIMARY KEY  (`ID`),
    KEY `messages_user_ID` USING BTREE (`USER_ID`),
    CONSTRAINT `messages_user_fk` FOREIGN KEY (`USER_ID`) REFERENCES `USERS` (`ID`),
    KEY `messages_workout_ID` USING BTREE (`WORKOUT_ID`),
    CONSTRAINT `messages_workout_fk` FOREIGN KEY (`WORKOUT_ID`) REFERENCES `WORKOUTS` (`ID`)    
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
