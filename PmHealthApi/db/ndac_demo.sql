CREATE DATABASE  IF NOT EXISTS `pm_health` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `pm_health`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: pm_health
-- ------------------------------------------------------
-- Server version	5.7.17-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `diagnoses`
--

DROP TABLE IF EXISTS `diagnoses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagnoses` (
  `patient_id` int(11) NOT NULL,
  `visit_id` int(11) NOT NULL,
  `diagnosis` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`patient_id`,`visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagnoses`
--

LOCK TABLES `diagnoses` WRITE;
/*!40000 ALTER TABLE `diagnoses` DISABLE KEYS */;
INSERT INTO `diagnoses` VALUES (1,6,'HIV'),(1,7,'Hepatitis B'),(1,8,'Hepatotoxicity'),(2,9,'brain cancer');
/*!40000 ALTER TABLE `diagnoses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `links`
--

DROP TABLE IF EXISTS `links`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `links` (
  `link_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`link_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `links`
--

LOCK TABLES `links` WRITE;
/*!40000 ALTER TABLE `links` DISABLE KEYS */;
INSERT INTO `links` VALUES (1,'Patients'),(2,'Request Access'),(3,'Medicines'),(4,'My Record'),(5,'Messages'),(10,'Actions');
/*!40000 ALTER TABLE `links` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicines`
--

DROP TABLE IF EXISTS `medicines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `medicines` (
  `med_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(300) DEFAULT NULL,
  `dosage` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`med_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21863 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicines`
--

LOCK TABLES `medicines` WRITE;
/*!40000 ALTER TABLE `medicines` DISABLE KEYS */;
INSERT INTO `medicines` VALUES (21818,'Acetaminophen','325 mg to 1 g every 4 to 6 hours'),(21819,'Adderall','5 mg 1 or 2 times a day'),(21823,'Amoxicillin','200 mg once a day'),(21831,'Codeine','125 mg every 6 to 8 hours'),(21833,'Cymbalta','500 mg daily'),(21837,'Ibuprofen','500 mg every 6 hours'),(21838,'Simvastatin ','100 mg every 4 hours'),(21839,'Lisinopril','5 mg every 12 hours'),(21840,'Loratadine','230 mg every 4 hours'),(21843,'Lyrica','1 tablet daily'),(21844,'Meloxicam','100 mg daily'),(21845,'Metformin','250 mg every 8 hours'),(21846,'Metoprolol','1 tablet a day'),(21849,'Oxycodone','4 tbalets a day'),(21850,'Pantoprazole','400 mg every 4 to 6 hours'),(21851,'Prednisone','300 mg every 4 hours'),(21852,'Tramadol','150 mg every 6 hours'),(21853,'Trazodone','1 g every day'),(21856,'Xanax','1 tablet when needed, no more than 1 every two days'),(21857,'Zoloft','350 mg a day'),(21858,'Atripla','1 tablet a day'),(21859,'Avastin','1 tablet a day'),(21860,'Chlorhexidine','1 tablet every 4 to 6 hours'),(21861,'Neomycin','200 mg every 8 hours'),(21862,'Penicillin','100 mg every 4 to 6 hours');
/*!40000 ALTER TABLE `medicines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_info`
--

DROP TABLE IF EXISTS `patient_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_info` (
  `patient_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `dob` varchar(45) DEFAULT NULL,
  `gender` varchar(45) DEFAULT NULL,
  `ssn` varchar(45) DEFAULT NULL,
  `race` varchar(45) DEFAULT NULL,
  `marital_status` varchar(45) DEFAULT NULL,
  `cell_phone` varchar(45) DEFAULT NULL,
  `work_phone` varchar(45) DEFAULT NULL,
  `home_phone` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`patient_id`),
  KEY `fk_user_id_idx` (`user_id`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_info`
--

LOCK TABLES `patient_info` WRITE;
/*!40000 ALTER TABLE `patient_info` DISABLE KEYS */;
INSERT INTO `patient_info` VALUES (1,1,'Chris Smith','05/17/1959','male','123-45-6789','white','married','571-725-9834','301-744-3851','703-222-9045','csmith@mail.com','123 Main Street'),(2,2,'Betty Johnson','10/22/1985','female','987-65-4321','white','single','571-422-1783','301-049-4451','703-177-9233','betty@mail.com','456 Main Street');
/*!40000 ALTER TABLE `patient_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescriptions`
--

DROP TABLE IF EXISTS `prescriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prescriptions` (
  `prescription_id` int(11) NOT NULL AUTO_INCREMENT,
  `visit_id` int(11) DEFAULT NULL,
  `medicine` varchar(45) DEFAULT NULL,
  `dosage` varchar(45) DEFAULT NULL,
  `duration` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`prescription_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
INSERT INTO `prescriptions` VALUES (5,6,'Lisinopril','5 mg every 12 hours','5 days'),(6,7,'Atripla','1 tablet a day','2 weeks'),(7,8,'Penicillin','100 mg every 4 to 6 hours','3 weeks'),(8,9,'Ibuprofen','500 mg every 6 hours','until symptoms subside'),(9,10,'Top Secret Cancer Drug','500 mg daily','1 week'),(10,11,'Top Secret Cancer Drug','500 mg daily','1 week'),(11,12,'Top Secret Cancer Drug','500 mg daily','1 week');
/*!40000 ALTER TABLE `prescriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sessions` (
  `session_id` varchar(50) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
INSERT INTO `sessions` VALUES ('002125bb35d945e694cc1e588f34d109',2),('02b702d1a8714bceac445f90c7bae6f8',5),('050853b17844495cb01ff9ab0862aa36',5),('0670ea09a3ef4ef39218fe9f35e0caf9',4),('0a77581e36674bb3a8989781827ff343',1),('0e3dc92c8c9942cbaa761520ecefa128',4),('0ff6150476eb4151aca2c80033539cf0',1),('123e226e5e7e43d3abb53ef94fd51559',4),('1596e16e11394988a8c5ce99ba7cbed1',5),('1cb9d90a52b646bb93d3da539076d5e8',2),('1d56fbb08a34432b8ad746ed81988765',4),('1dda1d9859c544169e0921fd5a156727',4),('1fe815e3a47c465a8a6ba5a17b416abd',5),('25cedd9efa724073b191d18e290af207',4),('269c83202df346ef92108667a7e22c7e',6),('272c91751be84981a7917ce0b0deb011',6),('2d5921504d524a4881f448fbf38f5f72',5),('3342f6b892d14a1abae21c90f5dc6046',4),('37061df04ba34ae682cb363462e4e3f8',5),('3788f55a133f4fef915f663c76ff34ec',4),('3c6a90c2dfd24ee9ad4a86393874d9f3',1),('40694379c4a34bcc8ebe191a1145e2c9',1),('40ef95d963974380ac487f0c0f1922ec',3),('41105578e7d84fbf995b312bed078d2d',1),('42319261560f46f68990aec7eeaae7e8',5),('43a7f75bfef84fd9b4600cbec0e60677',4),('44bf1ab2c84145569af480fe86bb0ca9',4),('45aaa8313ae54eb192c23782cfcc4d42',2),('49325a79fc314e6f81b2d4af1c64ea76',2),('49d7a42b45bb463a9cf15e69dfb9b1d6',5),('4ad3834ab84b4fa5af1e169f7350d161',4),('4c7fd67674574075ae25389493525e1f',4),('4ffe4b94a97c45aab01e66fba95a4b33',6),('50dc10848ce54ef581b49fb45ecfdf28',4),('50fec68fa8b44cb0aa978fd97b26561c',4),('514e3273a5374d56ac5e4e992c4406d1',4),('526e8f2660fa4e6289bbeb1b7c27d337',4),('52d731b2d91c4d719c28648256ce2bec',4),('54d8f6ae21884398877ea5e6888e8e63',6),('54f888cb03fc4443a16d253cf8673fa6',6),('5838fba2be554a5b9ecc70447c4e06be',4),('587427d70c064bd0b55450749136004b',1),('5bb27681808140e9a85b610965c3ce40',6),('5dafa8584c24437f9287427468f23b91',4),('5e765a7690b945f9835722c49dec0381',4),('60a75f000899452989fcce35da7e7258',6),('6193fef152554e6cbb27da6659f45a5b',6),('64e98924c1ab454cabac0df700c6a702',4),('656616a3edac42d3a94a8d50a527d069',5),('6797dbb8618442a1a37513e53a132639',1),('6a1038cc6146441cad15c87fdd8c7a6c',2),('6c84aba618bd4b82a8e50b91de1b8c6e',4),('6e24f669432a48eaa74e265cdc25fd3e',4),('6f549051967e442a912ca9e7cf903618',4),('6f614044673243c6a4dd53d0201caf3b',4),('7121533453e84ab8a5afc58adcf151cf',4),('71652396194b4bb3b59c8190674c0120',6),('738f836638344b8892f70e1851b6a9d2',2),('74c1e7da0d88434ab952e8f759c0548a',1),('771399f2701c4b1d8b0e28fdcb346238',4),('7a5482f85a394670a7f5f93337b91abe',6),('7c2b89ab2c764884815d328f96d99b8a',5),('7c6e0d9565b24ff8a0fc190cffe92c11',5),('7cb0030d98c44375ade1da1d84448a89',3),('7ce69c88a0a64c0d879f1cd822bb4e83',4),('7e677756fc8344a3bc3961c0dcda5ee5',5),('7ee6aca26d25482f8df482bf82716ae0',1),('7f45a95ca9944a73a1cbb1f30dd55e00',6),('805c4c2be1cf4b689479e7f111380d88',4),('86935d6310f1491bb1d49d5d3a836063',6),('8860b7d3f7b242ef881d76ce0b077067',1),('8a175562ef744dbf82db0d9498bbcb1d',5),('8a4dfd16c3c24a2f900484f2d08913f5',6),('8a5996b85a1340fe8393e112ebc5253d',6),('8c38d2773a6e4232805bd9557f5714d2',4),('906f64fdaacd47e9b408dd0de1e3451e',1),('913994a9a8384ce1b73cef6f7db09bda',6),('9205de6c8e37413e9a10744b33568407',4),('96e9c533ca0b457881a96e78bf6ab7ab',4),('9b268054e88a472e9340783e230dfad9',4),('9bcaebc9aa474ad7a7c134fcd7edc152',1),('9e80331694034f9f94502423227c07cd',4),('a0e86ae35b9a4b4bb7e1b1a2f44de7b6',2),('a104d4709b8643abb1368bc50fb6f65e',4),('a13249b918e244fd9369a5878ac119e2',6),('a32f06ce2b404735b0689278149bb348',4),('a796e0d4c4264f40996f50459147ab9e',1),('a9af272b5cd2433190740709f91326aa',5),('aae5de1c7b504679b4193ebd486c5ad6',3),('ab0325b3d7764f12bf79b8e6f06fdeaf',5),('ab3b3e44101f43f789506304dab02f68',4),('abc4e2aa58fa4e1395e0f1f0f973fc93',4),('abd4bf4fda9748179b1e570583fa0248',4),('acdb9d18aa6a4c849a181c952ecb545f',6),('b1202c5a7d5b47ccbfa08c9984cad955',5),('b42177b4b08f48c78d4157074099a701',4),('b79133bde79f4d98bd70bdd14fd0449e',4),('b8ab520d16cc4384a8f39a79b075efe9',4),('bf3742cd34f14407a0b1398baf9def03',5),('bf39d1617aad416a8bdf37c5cc1c276f',4),('bf526c2e1e6e4ec993fe9d728eda0f98',4),('bf75772b9b264ad89069a4d1e75d3485',4),('c1662752302b4ae1a910eb1ac54b2896',4),('c2fbfff3168048f4aa3e3af5b15000e8',4),('c3c0eb7a1c4a43a086f965f1ffc0ca80',6),('c54996f985fe4314a84255aeb6dc13a3',5),('c7a14e835ca54c2b9dd95c858236ca2d',4),('c87d0460b38b4861a429c83c605ba24e',4),('cb1049ce105a42c5964347c43dc5ad11',6),('cec0c2bed9de4d1db483114aa8c3f5a4',5),('cf56cb478d8346ed8844c60ab56dca7c',4),('cf89ddf62500471e8810300f8ee31d61',4),('d0f10d53442d4eb6a75f15372d9a7ad4',5),('d841a77a974541c4a09edd8b17c7dd22',1),('dac7ab19082947019e1c6a5d05517e12',5),('dc62fe77f997413196dcec55cc94bea8',5),('dc874c2ad53847f4bbea3b76d244fdb2',5),('dccbf8d4ddc04cbd90e0bbabab35576c',4),('dd92204c8fb54d55a3f829cdc3edcc3a',5),('e344bc65087c481b8d3a5c049395b46f',6),('e53f4c671e2b4bc295fd59c254c2ac93',2),('e57a59ac9ad6496987a72a91d8a928cb',4),('e9d6632182d2492bb722322dd113f121',1),('ea9b6f622c2c4b18a3563c60c2d9675d',6),('ef8c8c65824641f98f12f0fb4cd4201a',4),('f00957c1310c4faa91557beb150a742c',4),('f01616f0d38a4cb9ad654f39903bffe0',3),('f0d7d47ac3b34de2acfc46a8bb6e6942',3),('f10f3bd50f0048b393811b7744758d91',4),('f12b33f569644b0b8d9e09c0db85c757',5),('f2e2c1b735d04703b69abf00244d1805',5),('f56df245e6614556ac39e51860816812',4),('f922034f47cd4e3e866e06ae1f1e6154',3),('f9d5cd7212c94e0c9069efcfac628ff8',4),('faa254d3dc504a8a9f37ef23e0ad8d3b',4),('fc654da72a424adca7ea2a31bab0d2af',4),('fcb1852582634844b11d58094af0f7d8',6);
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `treatments`
--

DROP TABLE IF EXISTS `treatments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `treatments` (
  `patient_id` int(11) NOT NULL,
  `visit_id` int(11) NOT NULL,
  `treatment` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`patient_id`,`visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `treatments`
--

LOCK TABLES `treatments` WRITE;
/*!40000 ALTER TABLE `treatments` DISABLE KEYS */;
INSERT INTO `treatments` VALUES (1,6,'HIV medication and treatment plan'),(1,7,'Antiviral medication'),(1,8,'Liver transplant. Drug Rehab.'),(2,10,'chemotherapy'),(2,11,'chemotherapy'),(2,12,'chemotherapy');
/*!40000 ALTER TABLE `treatments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'chris','10073f51ec9fcde986b2baaab2edd7d7dd846b899014c4f7d5765705be7935d9f4c62cfa446202b2fc67c1334ac1f801d0242c0d5d18ac360e6c11d1f326e2c20971ac3a34c32747ff8b5d157f032e597f6'),(2,'betty','1008f600d7d196436241b07eddfacecd1d0876774ee046886bf3a9ae8007ba7fe06bd7bd76244785bc5df7317311458cdc896dd08a50e5b5edec6f753146c0a2172f4df3b9410dfd652d3d6d08b7fe64cb3'),(3,'alice','100f207069020cd44291b25ba0d4b12e9761555ded12948b54bf88dd7332bbd7d49260427ce0286a3686e2a186e4fa4ced9f8433102b175a2d59e12c45a3e84fa96dbd86b8ea26feb17f6dbc0b931445136'),(4,'bob','1002a0616cec26e70ccb19d6a1b6cc38ebfee9f6f95af9d3763409858373bfb703881ed9f8e3d0c12fc743613b50da09cb9af00dd9f189af822175b010b689b6da89ac14012c5f0cf962572fc20401eb615'),(5,'emily','100e760eb3681761e41f025513297212f634fdae58cbb085dbca8e9460365b5adb124f7f4c55e1db39c36c0431ef80bc005d8f50ab3e63bc06a3d6a87b00224dd46388aaed7f6745c150e83e54dbbe38c2d'),(6,'lucy','100cd0dbb3a009f345e3a1906ace4f7e0513e3cdcbdd70daebdff5824141eff2f97d73d2e8c4a6cb32b3565ee12c68887f63a67718cb7bb493e917f9dad7df45f5e433c7d08780ee49fc11c44df3f34384c');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visit_notes`
--

DROP TABLE IF EXISTS `visit_notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_notes` (
  `visit_note_id` int(11) NOT NULL AUTO_INCREMENT,
  `visit_id` int(11) NOT NULL,
  `note` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`visit_note_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visit_notes`
--

LOCK TABLES `visit_notes` WRITE;
/*!40000 ALTER TABLE `visit_notes` DISABLE KEYS */;
INSERT INTO `visit_notes` VALUES (3,6,'First case of HIV this year.  Expected with dramatic weight loss and fever.'),(5,8,'clear case of drug use, recommending rehab'),(6,9,'Cancerous tumor found in patient\'s brain.  Optimistic chem therapy will be enough.'),(7,7,'Another disease potentially caused by non sterile injections.  I\'m assuming this is related to drug use and the patient\'s appearance leads me to further believe so.'),(9,10,'First round of chemo went ok.  Didn\'t see the progress I wanted to which conerns me but let\'s see what happens next month'),(12,11,'Still would have liked to seen more progress.  Second round of chemo therapy went better.  Her support system could be better.  The family hardly visits her.'),(13,12,'Patient is responding well to treatment.  We are making progress.');
/*!40000 ALTER TABLE `visit_notes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visits`
--

DROP TABLE IF EXISTS `visits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visits` (
  `visit_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(11) NOT NULL,
  `admission_date` datetime DEFAULT NULL,
  `discharge_date` datetime DEFAULT NULL,
  `reason` varchar(500) DEFAULT NULL,
  `result` varchar(500) DEFAULT NULL,
  `notes` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`visit_id`),
  KEY `v_p_id_idx` (`patient_id`),
  CONSTRAINT `v_p_id` FOREIGN KEY (`patient_id`) REFERENCES `patient_info` (`patient_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visits`
--

LOCK TABLES `visits` WRITE;
/*!40000 ALTER TABLE `visits` DISABLE KEYS */;
INSERT INTO `visits` VALUES (6,1,'2016-01-14 08:45:03','2016-01-15 12:44:21','Fatigue and loss of appetite.  Nausea and sore throat.','Confirmed HIV','First case of HIV this year.  Expected with dramatic weight loss and fever.'),(7,1,'2016-01-17 12:49:41','2016-01-18 13:01:16','Fever, fatigue, nausea','Hepatitis B','Another disease potentially caused by non sterile injections.  I\'m assuming this is related to drug use and the patient\'s appearance leads me to further believe so.'),(8,1,'2016-07-09 12:49:41','2016-07-12 12:45:09','Loss of appetite, nausea, fatigue, stomach pain','Hepatotoxicity.  Liver transplant.','clear case of drug use, recommending rehab'),(9,2,'2016-01-22 15:45:44','2016-01-27 16:00:21','severe headaches','Medium size brain tumor found in biopsy','Cancerous tumor found in patient\'s brain.  Optimistic chem therapy will be enough.'),(10,2,'2016-02-22 15:45:52','2016-01-27 16:02:50','chemotherapy','treatment went well',NULL),(11,2,'2016-03-22 15:44:24','2016-01-27 15:59:29','chemotherapy','treatment went well',NULL),(12,2,'2016-04-22 15:44:12','2016-01-27 16:03:45','chemotherapy','treatment went well','Patient seems to be responding well to treatment.');
/*!40000 ALTER TABLE `visits` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vitals`
--

DROP TABLE IF EXISTS `vitals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vitals` (
  `visit_id` int(11) NOT NULL,
  `height` int(11) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `temperature` decimal(4,1) DEFAULT NULL,
  `pulse` int(11) DEFAULT NULL,
  `blood_pressure` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`visit_id`),
  KEY `fk_vitals_visit_id_idx` (`visit_id`),
  CONSTRAINT `fk_vitals_visit_id` FOREIGN KEY (`visit_id`) REFERENCES `visits` (`visit_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vitals`
--

LOCK TABLES `vitals` WRITE;
/*!40000 ALTER TABLE `vitals` DISABLE KEYS */;
INSERT INTO `vitals` VALUES (6,180,70,102.2,60,'140/90'),(7,180,71,99.5,68,'130/80'),(8,180,72,99.2,70,'125/80'),(9,170,55,98.4,70,'115/75'),(10,170,50,98.9,70,'120/75'),(11,170,49,98.7,70,'125/80'),(12,170,48,98.3,70,'130/80');
/*!40000 ALTER TABLE `vitals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'pm_health'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-09 19:07:20
