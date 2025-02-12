-- Insertion des données dans la table Model
INSERT INTO Model (modelName, brandName) VALUES
                                             ('Tesla Model S', 'Tesla'),
                                             ('Ford Mustang', 'Ford'),
                                             ('Yamaha R1', 'Yamaha'),
                                             ('Mercedes Sprinter', 'Mercedes'),
                                             ('Audi RS6', 'Audi'),
                                             ('Mercedes-AMG C63', 'Mercedes-Benz'),
                                             ('Porsche 911 Turbo', 'Porsche'),
                                             ('Lamborghini Huracán', 'Lamborghini'),
                                             ('Ferrari F8 Tributo', 'Ferrari'),
                                             ('Nissan GT-R', 'Nissan'),
                                             ('Lamborghini Urus', 'Lamborghini');

-- Insertion des données dans la table VehicleType
INSERT INTO VehicleType (typeName) VALUES
                                       ('CAR'),
                                       ('MOTORCYCLE'),
                                       ('VAN');



INSERT INTO LeTresBonCoin.Vehicle (idVehicle, status, price, countryOfOrigin, vehicule_model_id, idType, horse_power, vehicle_power_source, numberOfDoors, transmissionType, image_url, category, engine_capacity, vehicleType, engineCapacity) VALUES (1, 'Disponible', 60000.00, 'États-Unis', 1, 1, 450, 'ELECTRIC', 4, 'AUTOMATIC', '/images/tesla.jpg', 'BERLINE', 0, 'CAR', null);
INSERT INTO LeTresBonCoin.Vehicle (idVehicle, status, price, countryOfOrigin, vehicule_model_id, idType, horse_power, vehicle_power_source, numberOfDoors, transmissionType, image_url, category, engine_capacity, vehicleType, engineCapacity) VALUES (2, 'Occasion', 35000.00, 'Allemagne', 2, 1, 350, 'PETROL', 2, 'MANUAL', '/images/Ford-Mustang.png', 'COUPE', 5000, 'CAR', null);
INSERT INTO LeTresBonCoin.Vehicle (idVehicle, status, price, countryOfOrigin, vehicule_model_id, idType, horse_power, vehicle_power_source, numberOfDoors, transmissionType, image_url, category, engine_capacity, vehicleType, engineCapacity) VALUES (3, 'Neuf', 18000.00, 'Japon', 3, 2, 200, 'PETROL', null, null, '/images/Yamaha-R1.jpg', 'SPORTIVE', 1000, 'MOTORCYCLE', null);
INSERT INTO LeTresBonCoin.Vehicle (idVehicle, status, price, countryOfOrigin, vehicule_model_id, idType, horse_power, vehicle_power_source, numberOfDoors, transmissionType, image_url, category, engine_capacity, vehicleType, engineCapacity) VALUES (4, 'Disponible', 75000.00, 'Allemagne', 4, 1, 480, 'PETROL', 4, 'AUTOMATIC', '/images/BMW_M3.jpg', 'BERLINE', 3000, 'CAR', null);
INSERT INTO LeTresBonCoin.Vehicle (idVehicle, status, price, countryOfOrigin, vehicule_model_id, idType, horse_power, vehicle_power_source, numberOfDoors, transmissionType, image_url, category, engine_capacity, vehicleType, engineCapacity) VALUES (5, 'Disponible', 120000.00, 'Allemagne', 5, 1, 600, 'PETROL', 4, 'AUTOMATIC', '/images/audi_rs6.jpg', 'BERLINE', 4000, 'CAR', null);
INSERT INTO LeTresBonCoin.Vehicle (idVehicle, status, price, countryOfOrigin, vehicule_model_id, idType, horse_power, vehicle_power_source, numberOfDoors, transmissionType, image_url, category, engine_capacity, vehicleType, engineCapacity) VALUES (6, 'Disponible', 95000.00, 'Allemagne', 6, 1, 510, 'PETROL', 4, 'AUTOMATIC', '/images/amg-c63.jpg', 'BERLINE', 4000, 'CAR', null);
INSERT INTO LeTresBonCoin.Vehicle (idVehicle, status, price, countryOfOrigin, vehicule_model_id, idType, horse_power, vehicle_power_source, numberOfDoors, transmissionType, image_url, category, engine_capacity, vehicleType, engineCapacity) VALUES (7, 'Neuf', 180000.00, 'Allemagne', 7, 1, 640, 'PETROL', 2, 'AUTOMATIC', '/images/porsche-911.jpg', 'COUPE', 3800, 'CAR', null);
INSERT INTO LeTresBonCoin.Vehicle (idVehicle, status, price, countryOfOrigin, vehicule_model_id, idType, horse_power, vehicle_power_source, numberOfDoors, transmissionType, image_url, category, engine_capacity, vehicleType, engineCapacity) VALUES (8, 'Neuf', 250000.00, 'Italie', 8, 1, 610, 'PETROL', 2, 'AUTOMATIC', '/images/huracan.jpg', 'COUPE', 5200, 'CAR', null);
INSERT INTO LeTresBonCoin.Vehicle (idVehicle, status, price, countryOfOrigin, vehicule_model_id, idType, horse_power, vehicle_power_source, numberOfDoors, transmissionType, image_url, category, engine_capacity, vehicleType, engineCapacity) VALUES (9, 'Neuf', 280000.00, 'Italie', 9, 1, 720, 'PETROL', 2, 'AUTOMATIC', '/images/ferrari_f8.jpeg', 'COUPE', 3900, 'CAR', null);
INSERT INTO LeTresBonCoin.Vehicle (idVehicle, status, price, countryOfOrigin, vehicule_model_id, idType, horse_power, vehicle_power_source, numberOfDoors, transmissionType, image_url, category, engine_capacity, vehicleType, engineCapacity) VALUES (10, 'Occasion', 90000.00, 'Japon', 10, 1, 570, 'PETROL', 2, 'AUTOMATIC', '/images/nissan-gtr.jpg', 'COUPE', 3800, 'CAR', null);
INSERT INTO LeTresBonCoin.Vehicle (idVehicle, status, price, countryOfOrigin, vehicule_model_id, idType, horse_power, vehicle_power_source, numberOfDoors, transmissionType, image_url, category, engine_capacity, vehicleType, engineCapacity) VALUES (139, 'dispo', 240000.00, 'Italie', 11, null, 600, 'DIESEL', 4, 'AUTOMATIC', '/images/car.png', null, null, 'CAR', null);



INSERT INTO LeTresBonCoin.Client (idClient, name, phoneNumber, email, postalAddress, creditCardNumber, cveNumber, password, role, active_command_id) VALUES (1, 'Jean Dupont', '0123456789', 'jean.dupont@email.com', '10 rue de Paris', '1234567812345678', '123', 'Password123$', 'CLIENT', null);
INSERT INTO LeTresBonCoin.Client (idClient, name, phoneNumber, email, postalAddress, creditCardNumber, cveNumber, password, role, active_command_id) VALUES (2, 'Alice Martin', '0987654321', 'alice.martin@email.com', '15 avenue des Champs', '789147113369', '457', 'password456', 'ADMIN', null);
INSERT INTO LeTresBonCoin.Client (idClient, name, phoneNumber, email, postalAddress, creditCardNumber, cveNumber, password, role, active_command_id) VALUES (4, 'dzazad', null, 'dzadzadz.dazdaz@dazdza.com', null, null, null, 'A789$dazdza', 'CLIENT', null);

INSERT INTO LeTresBonCoin.ReviewEntity (idReview, comment, rating, reviewDate, client_idClient, vehicle_idVehicle) VALUES (1, 'très bonne voiture', 5, '2025-02-10', 2, 1);
INSERT INTO LeTresBonCoin.ReviewEntity (idReview, comment, rating, reviewDate, client_idClient, vehicle_idVehicle) VALUES (2, 'Bien', 4, '2025-02-10', 2, 1);
INSERT INTO LeTresBonCoin.ReviewEntity (idReview, comment, rating, reviewDate, client_idClient, vehicle_idVehicle) VALUES (3, 'Sympa', 3, '2025-02-10', 2, 1);
INSERT INTO LeTresBonCoin.ReviewEntity (idReview, comment, rating, reviewDate, client_idClient, vehicle_idVehicle) VALUES (4, 'Cool', 3, '2025-02-10', 2, 1);
INSERT INTO LeTresBonCoin.ReviewEntity (idReview, comment, rating, reviewDate, client_idClient, vehicle_idVehicle) VALUES (17, 'Super !!!', 5, '2025-02-11', 1, 7);
