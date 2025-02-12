-- MariaDB dump 10.19  Distrib 10.6.11-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: LeTresBonCoin
-- ------------------------------------------------------
-- Server version	10.6.11-MariaDB-2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Client`
--

DROP TABLE IF EXISTS `Client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Client` (
  `active_command_id` int(11) DEFAULT NULL,
  `idClient` int(11) NOT NULL AUTO_INCREMENT,
  `creditCardNumber` varchar(255) DEFAULT NULL,
  `cveNumber` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `postalAddress` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','CLIENT') NOT NULL,
  PRIMARY KEY (`idClient`),
  UNIQUE KEY `UKdaofs0xgu27nl8pa5satoyuv5` (`active_command_id`),
  UNIQUE KEY `UKf07ymtqaif0tbcawyb71l3one` (`email`),
  CONSTRAINT `FK1g38r915t5b83aagbsw6pgddr` FOREIGN KEY (`active_command_id`) REFERENCES `Command` (`idCommand`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Client`
--

LOCK TABLES `Client` WRITE;
/*!40000 ALTER TABLE `Client` DISABLE KEYS */;
INSERT INTO `Client` (`active_command_id`, `idClient`, `creditCardNumber`, `cveNumber`, `email`, `name`, `password`, `phoneNumber`, `postalAddress`, `role`) VALUES (NULL,1,'1234567812345678','123','jean.dupont@email.com','Jean Dupont','Password123$','0123456789','10 rue de Paris','CLIENT'),(NULL,2,'789147113369','457','alice.martin@email.com','Alice Martin','password456','0987654321','15 avenue des Champs','ADMIN'),(NULL,4,NULL,NULL,'dzadzadz.dazdaz@dazdza.com','dzazad','A789$dazdza',NULL,NULL,'CLIENT');
/*!40000 ALTER TABLE `Client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Command`
--

DROP TABLE IF EXISTS `Command`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Command` (
  `idCommand` int(11) NOT NULL AUTO_INCREMENT,
  `commandDate` date DEFAULT NULL,
  `commandStatus` varchar(255) DEFAULT NULL,
  `idClient` int(11) DEFAULT NULL,
  PRIMARY KEY (`idCommand`),
  KEY `FKhrjauq3xp9qknm91uk6do6r6c` (`idClient`),
  CONSTRAINT `FKhrjauq3xp9qknm91uk6do6r6c` FOREIGN KEY (`idClient`) REFERENCES `Client` (`idClient`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Command`
--

LOCK TABLES `Command` WRITE;
/*!40000 ALTER TABLE `Command` DISABLE KEYS */;
INSERT INTO `Command` (`idCommand`, `commandDate`, `commandStatus`, `idClient`) VALUES (1,'2025-02-12','Payée',1),(2,'2025-02-12','Payée',1),(4,'2025-02-12','Payée',1),(5,'2025-02-12','En attente',1),(6,'2025-02-12','En attente',1);
/*!40000 ALTER TABLE `Command` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CommandLine`
--

DROP TABLE IF EXISTS `CommandLine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CommandLine` (
  `idCommand` int(11) NOT NULL,
  `idVehicle` int(11) NOT NULL,
  PRIMARY KEY (`idCommand`,`idVehicle`),
  KEY `FKbwx1cue06wbex5qp5u5fthejw` (`idVehicle`),
  CONSTRAINT `FKbwx1cue06wbex5qp5u5fthejw` FOREIGN KEY (`idVehicle`) REFERENCES `Vehicle` (`idVehicle`),
  CONSTRAINT `FKcq5r7f8v6e75bmc9sk5ocesy4` FOREIGN KEY (`idCommand`) REFERENCES `Command` (`idCommand`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CommandLine`
--

LOCK TABLES `CommandLine` WRITE;
/*!40000 ALTER TABLE `CommandLine` DISABLE KEYS */;
INSERT INTO `CommandLine` (`idCommand`, `idVehicle`) VALUES (1,1),(2,3),(4,2),(5,6),(6,7);
/*!40000 ALTER TABLE `CommandLine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Invoice`
--

DROP TABLE IF EXISTS `Invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Invoice` (
  `idInvoice` int(11) NOT NULL AUTO_INCREMENT,
  `idCommand` int(11) DEFAULT NULL,
  `invoiceDate` date DEFAULT NULL,
  `totalAmount` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`idInvoice`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Invoice`
--

LOCK TABLES `Invoice` WRITE;
/*!40000 ALTER TABLE `Invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `Invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Model`
--

DROP TABLE IF EXISTS `Model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Model` (
  `idModel` int(11) NOT NULL AUTO_INCREMENT,
  `brandName` varchar(255) DEFAULT NULL,
  `modelName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idModel`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Model`
--

LOCK TABLES `Model` WRITE;
/*!40000 ALTER TABLE `Model` DISABLE KEYS */;
INSERT INTO `Model` (`idModel`, `brandName`, `modelName`) VALUES (1,'Tesla','Tesla Model S'),(2,'Ford','Ford Mustang'),(3,'Yamaha','Yamaha R1'),(4,'Mercedes','Mercedes Sprinter'),(5,'Audi','Audi RS6'),(6,'Mercedes-Benz','Mercedes-AMG C63'),(7,'Porsche','Porsche 911 Turbo'),(8,'Lamborghini','Lamborghini Huracán'),(9,'Ferrari','Ferrari F8 Tributo'),(10,'Nissan','Nissan GT-R'),(11,'Lamborghini','Lamborghini Urus');
/*!40000 ALTER TABLE `Model` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ReviewEntity`
--

DROP TABLE IF EXISTS `ReviewEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ReviewEntity` (
  `idReview` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` varchar(2000) DEFAULT NULL,
  `rating` int(11) NOT NULL,
  `reviewDate` date DEFAULT NULL,
  `client_idClient` int(11) DEFAULT NULL,
  `vehicle_idVehicle` int(11) DEFAULT NULL,
  PRIMARY KEY (`idReview`),
  KEY `FKf6pekyb357ikpy9hmyfalbn0q` (`client_idClient`),
  KEY `FK13efw7njskhelar5s39pl41md` (`vehicle_idVehicle`),
  CONSTRAINT `FK13efw7njskhelar5s39pl41md` FOREIGN KEY (`vehicle_idVehicle`) REFERENCES `Vehicle` (`idVehicle`),
  CONSTRAINT `FKf6pekyb357ikpy9hmyfalbn0q` FOREIGN KEY (`client_idClient`) REFERENCES `Client` (`idClient`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ReviewEntity`
--

LOCK TABLES `ReviewEntity` WRITE;
/*!40000 ALTER TABLE `ReviewEntity` DISABLE KEYS */;
INSERT INTO `ReviewEntity` (`idReview`, `comment`, `rating`, `reviewDate`, `client_idClient`, `vehicle_idVehicle`) VALUES (1,'très bonne voiture',5,'2025-02-10',2,1),(2,'Bien',4,'2025-02-10',2,1),(3,'Sympa',3,'2025-02-10',2,1),(4,'Cool',3,'2025-02-10',2,1),(17,'Super !!!',5,'2025-02-11',1,7),(18,'C est top !',5,'2025-02-12',1,2);


/*!40000 ALTER TABLE `ReviewEntity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Vehicle`
--

DROP TABLE IF EXISTS `Vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Vehicle` (
  `engineCapacity` int(11) DEFAULT NULL,
  `horse_power` int(11) DEFAULT NULL,
  `idVehicle` int(11) NOT NULL AUTO_INCREMENT,
  `numberOfDoors` int(11) DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `vehicule_model_id` int(11) DEFAULT NULL,
  `vehicleType` varchar(31) NOT NULL,
  `countryOfOrigin` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `transmissionType` enum('AUTOMATIC','AWD','CVT','FOUR_WHEEL_DRIVE','MANUAL','SEMI_AUTOMATIC') DEFAULT NULL,
  `vehicle_power_source` enum('DIESEL','ELECTRIC','HYBRID','PETROL') DEFAULT NULL,
  `idType` int(11) DEFAULT NULL,
  `vehicle_category` enum('BERLINE','CABRIOLET','CLASSIC','COUPE','CROSSOVER','HATCHBACK','MINIVAN','MOTORCYCLE','OTHER','PICKUP','SPORTIVE','SUPERSUPERSPORTIVE','SUV') DEFAULT NULL,
  PRIMARY KEY (`idVehicle`),
  UNIQUE KEY `UKq8kc9cmqy8vvbd3amc75qdbvo` (`vehicule_model_id`),
  KEY `FK_Vehicle_Type` (`idType`),
  CONSTRAINT `FK_Vehicle_Model` FOREIGN KEY (`vehicule_model_id`) REFERENCES `Model` (`idModel`),
  CONSTRAINT `FK_Vehicle_Type` FOREIGN KEY (`idType`) REFERENCES `VehicleType` (`idType`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Vehicle`
--

LOCK TABLES `Vehicle` WRITE;
/*!40000 ALTER TABLE `Vehicle` DISABLE KEYS */;
INSERT INTO `Vehicle` (`engineCapacity`, `horse_power`, `idVehicle`, `numberOfDoors`, `price`, `vehicule_model_id`, `vehicleType`, `countryOfOrigin`, `image_url`, `status`, `transmissionType`, `vehicle_power_source`, `idType`, `vehicle_category`) VALUES (0,450,1,4,60000.00,1,'CAR','États-Unis','/images/tesla.jpg','Disponible','AUTOMATIC','ELECTRIC',1,'BERLINE'),(5000,350,2,2,35000.00,2,'CAR','Allemagne','/images/Ford-Mustang.png','Occasion','MANUAL','PETROL',1,'COUPE'),(1000,200,3,NULL,18000.00,3,'MOTORCYCLE','Japon','/images/Yamaha-R1.jpg','Neuf',NULL,'PETROL',2,'SPORTIVE'),(3000,480,4,4,75000.00,4,'CAR','Allemagne','/images/BMW_M3.jpg','Disponible','AUTOMATIC','PETROL',1,'BERLINE'),(4000,600,5,4,120000.00,5,'CAR','Allemagne','/images/audi_rs6.jpg','Disponible','AUTOMATIC','PETROL',1,'BERLINE'),(4000,510,6,4,95000.00,6,'CAR','Allemagne','/images/amg-c63.jpg','Disponible','AUTOMATIC','PETROL',1,'BERLINE'),(3800,640,7,2,180000.00,7,'CAR','Allemagne','/images/porsche-911.jpg','Neuf','AUTOMATIC','PETROL',1,'COUPE'),(5200,610,8,2,250000.00,8,'CAR','Italie','/images/huracan.jpg','Neuf','AUTOMATIC','PETROL',1,'COUPE'),(3900,720,9,2,280000.00,9,'CAR','Italie','/images/ferrari_f8.jpeg','Neuf','AUTOMATIC','PETROL',1,'COUPE'),(3800,570,10,2,90000.00,10,'CAR','Japon','/images/nissan-gtr.jpg','Occasion','AUTOMATIC','PETROL',1,'COUPE'),(NULL,600,139,4,240000.00,11,'CAR','Italie','/images/car.png','dispo','AUTOMATIC','DIESEL',NULL,NULL);
/*!40000 ALTER TABLE `Vehicle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `VehicleType`
--

DROP TABLE IF EXISTS `VehicleType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VehicleType` (
  `idType` int(11) NOT NULL AUTO_INCREMENT,
  `typeName` enum('CAR','MOTORCYCLE','OTHER','VAN') NOT NULL,
  PRIMARY KEY (`idType`),
  UNIQUE KEY `UK5qq9dofn7npcjm67nbn8ircts` (`typeName`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VehicleType`
--

LOCK TABLES `VehicleType` WRITE;
/*!40000 ALTER TABLE `VehicleType` DISABLE KEYS */;
INSERT INTO `VehicleType` (`idType`, `typeName`) VALUES (1,'CAR'),(2,'MOTORCYCLE'),(3,'VAN');
/*!40000 ALTER TABLE `VehicleType` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-12 19:36:55
