CREATE TABLE `user_saved_stock` (
  `userId` int DEFAULT NULL,
  `symbol` varchar(10) DEFAULT NULL,
  `stockprice` decimal(10,2) DEFAULT NULL,
  `stockquantity` int DEFAULT NULL,
  `date` varchar(15) NOT NULL,
  `time` varchar(15) NOT NULL,
  FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
);
