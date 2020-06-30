-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: librarymanagement
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `borrow`
--

DROP TABLE IF EXISTS `borrow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrow` (
  `borrowId` int(11) NOT NULL AUTO_INCREMENT,
  `nic` varchar(15) NOT NULL,
  `isbn` varchar(50) NOT NULL,
  `date` date NOT NULL,
  `returnDare` date NOT NULL,
  `status` varchar(15) DEFAULT 'not returned',
  PRIMARY KEY (`borrowId`),
  KEY `fk_isbn` (`isbn`),
  KEY `fk_nic` (`nic`),
  CONSTRAINT `fk_isbn` FOREIGN KEY (`isbn`) REFERENCES `book` (`isbn`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_nic` FOREIGN KEY (`nic`) REFERENCES `member` (`nic`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow`
--

LOCK TABLES `borrow` WRITE;
/*!40000 ALTER TABLE `borrow` DISABLE KEYS */;
INSERT INTO `borrow` VALUES (1,'951344354v','ISBN001','2020-04-03','2020-04-12','not returned'),(2,'200116704742','ISBN002','2020-04-03','2020-04-12','not returned'),(3,'951344354v','ISBN002','2020-04-11','2020-04-18','returned'),(4,'951344354v','ISBN001','2020-04-11','2020-04-18','returned'),(5,'951344354v','ISBN002','2020-04-11','2020-04-18','returned'),(6,'951344354v','ISBN009','2020-04-11','2020-04-18','returned'),(7,'951344354v','ISBN001','2020-04-11','2020-04-18','returned'),(8,'951344354v','ISBN002','2020-04-11','2020-04-18','returned'),(9,'951344354v','ISBN006','2020-04-11','2020-04-18','returned'),(10,'951344354v','ISBN008','2020-04-21','2020-04-28','not returned');
/*!40000 ALTER TABLE `borrow` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-20  4:55:03
