CREATE DATABASE  IF NOT EXISTS `pm_health` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `pm_health`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: healthapp
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
  `diagnoses` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`patient_id`,`visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagnoses`
--

LOCK TABLES `diagnoses` WRITE;
/*!40000 ALTER TABLE `diagnoses` DISABLE KEYS */;
INSERT INTO `diagnoses` VALUES (1,1,'Gingivitis and periodonatal disease'),(1,2,'Otis externa'),(1,3,'Food poisoning'),(1,4,'Allergic reaction'),(1,5,'Lower back contusion'),(1,6,'HIV'),(1,7,'Hepatitis B'),(1,8,'Hepatotoxicity'),(2,9,'brain cancer');
/*!40000 ALTER TABLE `diagnoses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `labs`
--

DROP TABLE IF EXISTS `labs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `labs` (
  `patient_id` int(11) NOT NULL,
  PRIMARY KEY (`patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `labs`
--

LOCK TABLES `labs` WRITE;
/*!40000 ALTER TABLE `labs` DISABLE KEYS */;
/*!40000 ALTER TABLE `labs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_info`
--

DROP TABLE IF EXISTS `patient_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_info` (
  `patient_id` int(11) NOT NULL,
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
  PRIMARY KEY (`patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_info`
--

LOCK TABLES `patient_info` WRITE;
/*!40000 ALTER TABLE `patient_info` DISABLE KEYS */;
INSERT INTO `patient_info` VALUES (1,'Christopher Smith','05/17/1958','male','123-45-6789','white','married','571-725-9834','301-744-3851','703-222-9045','csmith@mail.com'),(2,'Betty Johnson','10/22/1985','female','987-65-4321','white','single','571-422-1783','301-049-4451','703-177-9233','betty@mail.com');
/*!40000 ALTER TABLE `patient_info` ENABLE KEYS */;
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
INSERT INTO `treatments` VALUES (1,1,'Fluids and Antibiotics. Inpatient care.'),(1,2,'Antibiotics'),(1,3,'Stomach pump'),(1,4,'Applied antibiotic ointment'),(1,5,'Ibuprofen and ice'),(1,6,'HIV medication and treatment plan'),(1,7,'Antiviral medication'),(1,8,'Liver transplant. Drug Rehab.'),(2,10,'chemotherapy'),(2,11,'chemotherapy'),(2,12,'chemotherapy');
/*!40000 ALTER TABLE `treatments` ENABLE KEYS */;
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
INSERT INTO `visit_notes` VALUES (1,1,'A few days in the hospital should cure the gingivits. Might run other tests to determine cause and alternative treatment plans.'),(2,2,'Rare case of Otitis externa, no labs needed.'),(3,6,'First case of HIV this year.  Expected with dramatic weight loss and fever.'),(4,7,'Another disease potentially caused by non sterile injections.  '),(5,8,'clear case of drug use, recommending rehab'),(6,9,'Cancerous tumor found in patient\'s brain.  '),(7,7,'I\'m assuming this is related to drug use and the patient\'s appearance leads me to further believe so.'),(8,9,'Optimistic chem therapy will be enough.'),(9,10,'First round of chemo went ok'),(10,10,'Didn\'t see the progress I wanted to which conerns me but let\'s see what happens next month'),(11,11,'Second round of chemo therapy went better'),(12,11,'Still would have liked to seen more progress.  Her support system could be better.  The family hardly visits her.'),(13,12,'Patient is responding well to treatment.  We are making progress.');
/*!40000 ALTER TABLE `visit_notes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visits`
--

DROP TABLE IF EXISTS `visits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visits` (
  `visit_id` int(11) NOT NULL DEFAULT '0',
  `patient_id` int(11) NOT NULL,
  `admission_date` datetime DEFAULT NULL,
  `discharge_date` datetime DEFAULT NULL,
  `reason` varchar(500) DEFAULT NULL,
  `result` varchar(500) DEFAULT NULL,
  `notes` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`visit_id`),
  KEY `v_p_id_idx` (`patient_id`),
  CONSTRAINT `v_p_id` FOREIGN KEY (`patient_id`) REFERENCES `patient_info` (`patient_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visits`
--

LOCK TABLES `visits` WRITE;
/*!40000 ALTER TABLE `visits` DISABLE KEYS */;
INSERT INTO `visits` VALUES (1,1,'2015-05-02 12:49:41','2015-05-05 12:49:41','Inflammation in the mouth',' Gingivitis and periodontal disease.  Admitted to inpatient for treatment.','A few days in the hospital should cure the gingivits. Might run other tests to determine cause and alternative treatment plans.'),(2,1,'2015-05-12 12:57:04','2015-05-12 12:57:04','Inflammation of the ear.  Trouble hearing. Headaches.','Otitis externa','Rare case of Otitis externa, no labs needed.'),(3,1,'2015-07-05 12:46:02','2015-07-05 12:46:02','Severe stomach pain and nausea','Food poisoning',NULL),(4,1,'2015-11-12 13:21:10','2015-11-12 13:21:10','Rash','Applied antibiotic skin creme, and rash went away',NULL),(5,1,'2015-12-21 13:31:36','2015-12-22 13:31:36','Injury to lower back','No serious damage, just rest.',NULL),(6,1,'2016-01-15 12:44:21','2016-01-15 12:44:21','Fever','Confirmed HIV','First case of HIV this year.  Expected with dramatic weight loss and fever.'),(7,1,'2016-01-18 13:01:16','2016-01-18 13:01:16','fatigue, nausea, itching','Hepatitis B','Another disease potentially caused by non sterile injections.  I\'m assuming this is related to drug use and the patient\'s appearance leads me to further believe so.'),(8,1,'2016-07-12 12:45:09','2016-07-12 12:45:09','loss of appetite, nausea, fatigue','Hepatotoxicity.  Liver transplant.','clear case of drug use, recommending rehab'),(9,2,'2016-01-22 15:45:44','2016-01-27 16:00:21','severe headaches','Medium size brain tumor found in biopsy','Cancerous tumor found in patient\'s brain.  Optimistic chem therapy will be enough.'),(10,2,'2016-02-22 15:45:52','2016-01-27 16:02:50','chemotherapy','treatment went well',NULL),(11,2,'2016-03-22 15:44:24','2016-01-27 15:59:29','chemotherapy','treatment went well',NULL),(12,2,'2016-04-22 15:44:12','2016-01-27 16:03:45','chemotherapy','treatment went well','Patient seems to be responding well to treatment.');
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
INSERT INTO `vitals` VALUES (1,180,81,98.4,73,'120/80'),(2,180,80,98.6,72,'120/80'),(3,180,81,98.7,72,'120/80'),(4,180,82,98.7,71,'120/80'),(5,180,81,98.9,72,'120/80'),(6,180,70,102.2,60,'140/90'),(7,180,71,99.5,68,'130/80'),(8,180,72,99.2,70,'125/80'),(9,170,55,98.4,70,'115/75'),(10,170,50,98.9,70,'120/75'),(11,170,49,98.7,70,'125/80'),(12,170,48,98.3,70,'130/80');
/*!40000 ALTER TABLE `vitals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'healthapp'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-16  9:35:02
