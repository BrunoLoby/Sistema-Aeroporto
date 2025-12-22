-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: sistema_aeroporto
-- ------------------------------------------------------
-- Server version	9.5.0

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'b7d52274-c41c-11f0-8d02-00e26968b4be:1-139';

--
-- Table structure for table `aeroporto`
--

DROP TABLE IF EXISTS `aeroporto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aeroporto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cidade` varchar(45) NOT NULL,
  `abreviacao` varchar(45) NOT NULL,
  `dt_criacao` date NOT NULL,
  `dt_modificacao` date NOT NULL,
  `nome` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aeroporto`
--

LOCK TABLES `aeroporto` WRITE;
/*!40000 ALTER TABLE `aeroporto` DISABLE KEYS */;
INSERT INTO `aeroporto` VALUES (1,'Ribeirão Preto - SP','RAO','2025-12-01','2025-12-01','Aeroporto Estadual Dr. Leite Lopes');
/*!40000 ALTER TABLE `aeroporto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assentos`
--

DROP TABLE IF EXISTS `assentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assentos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cod_assento` varchar(10) NOT NULL,
  `dt_modificacao` date NOT NULL,
  `dt_criacao` date NOT NULL,
  `id_voo` int NOT NULL,
  `ocupado` tinyint(1) NOT NULL DEFAULT '0',
  `nome_passageiro` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_assentos_voo` (`id_voo`),
  CONSTRAINT `fk_assentos_voo` FOREIGN KEY (`id_voo`) REFERENCES `voo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assentos`
--

LOCK TABLES `assentos` WRITE;
/*!40000 ALTER TABLE `assentos` DISABLE KEYS */;
INSERT INTO `assentos` VALUES (11,'A1','2025-12-02','2025-12-02',6,1,'RONALDO'),(12,'A2','2025-12-02','2025-12-02',6,1,'RENATA'),(13,'A3','2025-12-02','2025-12-02',6,0,NULL),(14,'A4','2025-12-02','2025-12-02',6,0,NULL),(15,'A5','2025-12-02','2025-12-02',6,0,NULL),(16,'A6','2025-12-02','2025-12-02',6,0,NULL),(17,'A7','2025-12-02','2025-12-02',6,0,NULL),(18,'A8','2025-12-02','2025-12-02',6,0,NULL),(19,'A9','2025-12-02','2025-12-02',6,0,NULL),(20,'A10','2025-12-02','2025-12-02',6,0,NULL);
/*!40000 ALTER TABLE `assentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `companhia`
--

DROP TABLE IF EXISTS `companhia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companhia` (
  `id` int NOT NULL AUTO_INCREMENT,
  `abreviacao` varchar(45) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `dt_criacao` date NOT NULL,
  `dt_modificacao` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companhia`
--

LOCK TABLES `companhia` WRITE;
/*!40000 ALTER TABLE `companhia` DISABLE KEYS */;
INSERT INTO `companhia` VALUES (1,'AD','Azul','2025-11-25','2025-12-01'),(5,'IATA','Oceanic Airlines','2025-11-26','2025-12-01'),(6,'G3','GOL','2025-11-30','2025-12-01');
/*!40000 ALTER TABLE `companhia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `passageiro`
--

DROP TABLE IF EXISTS `passageiro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `passageiro` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nascimento` varchar(45) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `login` varchar(45) NOT NULL,
  `senha` varchar(45) NOT NULL,
  `documento` varchar(45) NOT NULL,
  `dt_criacao` date NOT NULL,
  `dt_modificacao` date NOT NULL,
  `foto` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `passageiro`
--

LOCK TABLES `passageiro` WRITE;
/*!40000 ALTER TABLE `passageiro` DISABLE KEYS */;
INSERT INTO `passageiro` VALUES (17,'18/02/2003','RENATA','PASSAGEIRO','1234','123-09','2025-11-30','2025-11-30','Renata.jpg'),(20,'13/08/1928','RONALDO','PASSAGEIRO','1010','345-00','2025-12-02','2025-12-02','Ronaldo.jpg');
/*!40000 ALTER TABLE `passageiro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cod` bigint NOT NULL,
  `valor` double NOT NULL,
  `id_voo` int NOT NULL,
  `id_passageiro` int NOT NULL,
  `id_assento` int NOT NULL,
  `dt_criacao` date NOT NULL,
  `dt_modificacao` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_voo` (`id_voo`),
  KEY `id_passageiro` (`id_passageiro`),
  KEY `id_assento` (`id_assento`),
  CONSTRAINT `ticket_ibfk_1` FOREIGN KEY (`id_voo`) REFERENCES `voo` (`id`),
  CONSTRAINT `ticket_ibfk_2` FOREIGN KEY (`id_passageiro`) REFERENCES `passageiro` (`id`),
  CONSTRAINT `ticket_ibfk_3` FOREIGN KEY (`id_assento`) REFERENCES `assentos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket`
--

LOCK TABLES `ticket` WRITE;
/*!40000 ALTER TABLE `ticket` DISABLE KEYS */;
INSERT INTO `ticket` VALUES (2,202500001,1200,6,20,11,'2025-12-02','2025-12-02');
/*!40000 ALTER TABLE `ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tipoUsuario` int NOT NULL,
  `login` varchar(45) NOT NULL,
  `senha` varchar(45) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `documento` varchar(45) NOT NULL,
  `nascimento` varchar(45) NOT NULL,
  `idPassageiro` int DEFAULT NULL,
  `dt_criacao` date NOT NULL,
  `dt_modificacao` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,1,'ADM','1001','Bruno','525-88','19/08/2004',NULL,'2025-11-30','2025-11-30'),(2,1,'ADM2','2002','Joao Pedro','125-02','20/05/2005',NULL,'2025-11-30','2025-11-30'),(3,2,'FUNCIONARIO','3003','Amanda','125-01','22/02/2002',NULL,'2025-11-30','2025-11-30'),(11,3,'PASSAGEIRO','1234','RENATA','123-09','18/02/2003',17,'2025-11-30','2025-11-30'),(13,3,'PASSAGEIRO','1010','RONALDO','345-00','13/08/1928',20,'2025-12-02','2025-12-02');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voo`
--

DROP TABLE IF EXISTS `voo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `origem` varchar(45) NOT NULL,
  `destino` varchar(45) NOT NULL,
  `duracao` varchar(45) NOT NULL,
  `estado` varchar(45) NOT NULL,
  `capacidade` int NOT NULL,
  `dt_criacao` date NOT NULL,
  `dt_modificacao` date NOT NULL,
  `id_companhia` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_voo_companhia` (`id_companhia`),
  CONSTRAINT `fk_voo_companhia` FOREIGN KEY (`id_companhia`) REFERENCES `companhia` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voo`
--

LOCK TABLES `voo` WRITE;
/*!40000 ALTER TABLE `voo` DISABLE KEYS */;
INSERT INTO `voo` VALUES (6,'Ituverava','Uberaba','1h00','Atrasado',10,'2025-11-30','2025-11-30',6);
/*!40000 ALTER TABLE `voo` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-02 23:14:30
