ALTER TABLE `MESSAGES` ADD COLUMN `DELETED_BY` bigint(20) DEFAULT NULL AFTER `IS_READ`;
ALTER TABLE `MESSAGES` ADD KEY `messages_deleted_ID` USING BTREE (`DELETED_BY`);
ALTER TABLE `MESSAGES` ADD CONSTRAINT `messages_deleted_fk` FOREIGN KEY (`DELETED_BY`) REFERENCES `USERS` (`ID`);
