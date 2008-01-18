
--
-- Create schema sport_java
--

CREATE DATABASE IF NOT EXISTS sport_java;
USE sport_java;


--
-- Definition of table `USERS`
--
CREATE TABLE  `USERS` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) character set utf8 NOT NULL,
  `HASH` varchar(255) character set utf8 default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


--
-- Definition of table `WORKOUTS`
--
CREATE TABLE  `WORKOUTS` (
  `ID` bigint(20) NOT NULL auto_increment,
  `WORKOUT_DATE` datetime NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  `DISTANCE` double default NULL,
  `DURATION` bigint(20) default NULL,
  `DISCIPLINE` varchar(255) character set utf8 NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `user_ID` USING BTREE (`USER_ID`),
  CONSTRAINT `user_fk` FOREIGN KEY (`USER_ID`) REFERENCES `USERS` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;