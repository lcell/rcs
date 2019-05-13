--
-- Dumping data for table `rcs_user`
--

LOCK TABLES `rcs_user` WRITE;
/*!40000 ALTER TABLE `rcs_user` DISABLE KEYS */;
INSERT INTO `rcs_user` VALUES (1,'admin123','$2a$10$.rEfyBb/GURD9P2p0fRg/OAJGloGNDkJS4lY0cQHeqDgsbdTylBpu','https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif','2018-02-01 00:00:00','2018-02-01 00:00:00',0,'[1]');
/*!40000 ALTER TABLE `rcs_user` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `rcs_role`
--

LOCK TABLES `rcs_role` WRITE;
/*!40000 ALTER TABLE `rcs_role` DISABLE KEYS */;
INSERT INTO `rcs_role` VALUES (1,'超级管理员','所有模块的权限',1,'2019-01-01 00:00:00','2019-01-01 00:00:00',0);
/*!40000 ALTER TABLE `rcs_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `rcs_permission`
--

LOCK TABLES `rcs_permission` WRITE;
/*!40000 ALTER TABLE `rcs_permission` DISABLE KEYS */;
INSERT INTO `rcs_permission` VALUES (1,1,'*','2019-01-01 00:00:00','2019-01-01 00:00:00',0);
/*!40000 ALTER TABLE `rcs_permission` ENABLE KEYS */;
UNLOCK TABLES;
