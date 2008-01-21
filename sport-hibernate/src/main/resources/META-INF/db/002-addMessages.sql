
USE sport_java;




CREATE TABLE  `MESSAGES` (
    `ID` bigint(20) NOT NULL auto_increment,
    `DATE` datetime NOT NULL,
    `SENDER_ID` bigint(20) NOT NULL,
    `RECEIVER_ID` bigint(20) NOT NULL,
    `WORKOUT_ID` bigint(20),
    `CONTENT` TEXT character set utf8 NOT NULL,
    PRIMARY KEY  (`ID`),
    KEY `messages_receiver_ID` USING BTREE (`RECEIVER_ID`),
    CONSTRAINT `messages_receiver_fk` FOREIGN KEY (`RECEIVER_ID`) REFERENCES `USERS` (`ID`),
    KEY `messages_sender_ID` USING BTREE (`SENDER_ID`),
    CONSTRAINT `messages_sender_fk` FOREIGN KEY (`SENDER_ID`) REFERENCES `USERS` (`ID`),
    KEY `messages_workout_ID` USING BTREE (`WORKOUT_ID`),
    CONSTRAINT `messages_workout_fk` FOREIGN KEY (`WORKOUT_ID`) REFERENCES `WORKOUTS` (`ID`)    
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
