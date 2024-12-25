create table Client
(
    idClient         int auto_increment
        primary key,
    name             varchar(255) null,
    phoneNumber      varchar(255) null,
    email            varchar(255) null,
    postalAddress    varchar(255) null,
    creditCardNumber varchar(255) null,
    cveNumber        varchar(255) null,
    password         varchar(255) null
);

create table Command
(
    idCommand     int auto_increment
        primary key,
    commandStatus varchar(255) null,
    commandDate   date         not null,
    idClient      int          null,
    constraint Command_ibfk_1
        foreign key (idClient) references Client (idClient)
            on delete set null
);

create index idClient
    on Command (idClient);

create table Invoice
(
    idInvoice   int auto_increment
        primary key,
    invoiceDate date           not null,
    totalAmount decimal(38, 2) null,
    idCommand   int            null,
    constraint Invoice_ibfk_1
        foreign key (idCommand) references Command (idCommand)
            on delete cascade
);

create index idCommand
    on Invoice (idCommand);

create table Model
(
    idModel   int auto_increment
        primary key,
    modelName varchar(255) null,
    brandName varchar(255) null
);

create table VehicleType
(
    idType   int auto_increment
        primary key,
    typeName varchar(100) not null
);

create table Vehicle
(
    idVehicle                  int auto_increment
        primary key,
    status                     varchar(255)                                                                                                                   null,
    price                      decimal(38, 2)                                                                                                                 null,
    countryOfOrigin            varchar(255)                                                                                                                   null,
    vehicule_model_id          int                                                                                                                            null,
    idType                     int                                                                                                                            null,
    horse_power                int                                                                                                                            null,
    vehicle_power_source       enum ('DIESEL', 'ELECTRIC', 'HYBRID', 'PETROL')                                                                                null,
    ClientEntity_idClient      int                                                                                                                            not null,
    vehicleEntitySet_idVehicle int                                                                                                                            not null,
    DTYPE                      varchar(31)                                                                                                                    not null,
    numberOfDoors              int                                                                                                                            null,
    transmissionType           enum ('AUTOMATIC', 'AWD', 'CVT', 'FOUR_WHEEL_DRIVE', 'MANUAL', 'SEMI_AUTOMATIC')                                               null,
    image_url                  varchar(500)                                                                                                                   null,
    category                   enum ('SPORTIVE', 'SUPERSUPERSPORTIVE', 'BERLINE', 'SUV', 'HATCHBACK', 'COUPE', 'CABRIOLET', 'CROSSOVER', 'MINIVAN', 'PICKUP') null,
    constraint UKmmrjew7wu915tdtcnhd2op16i
        unique (vehicleEntitySet_idVehicle),
    constraint FKby9xh8956jw17dq5gpqerted0
        foreign key (vehicleEntitySet_idVehicle) references Vehicle (idVehicle),
    constraint FKo9mnvbku9969x99is6lj1wxxj
        foreign key (ClientEntity_idClient) references Client (idClient),
    constraint Vehicle_ibfk_1
        foreign key (vehicule_model_id) references Model (idModel)
            on delete set null,
    constraint Vehicle_ibfk_2
        foreign key (idType) references VehicleType (idType)
            on delete set null
);

create table Car
(
    idVehicle        int                                                                              not null
        primary key,
    numberOfDoors    int                                                                              null,
    transmissionType enum ('MANUAL', 'AUTOMATIC', 'SEMI_AUTOMATIC', 'CVT', 'AWD', 'FOUR_WHEEL_DRIVE') null,
    constraint Car_ibfk_1
        foreign key (idVehicle) references Vehicle (idVehicle)
            on delete cascade
);

create table CommandLine
(
    idCommand int not null,
    idVehicle int not null,
    primary key (idCommand, idVehicle),
    constraint CommandLine_ibfk_1
        foreign key (idCommand) references Command (idCommand)
            on delete cascade,
    constraint CommandLine_ibfk_2
        foreign key (idVehicle) references Vehicle (idVehicle)
            on delete cascade
);

create index idVehicle
    on CommandLine (idVehicle);

create table Motorcycle
(
    idVehicle      int not null
        primary key,
    engineCapacity int null,
    constraint Motorcycle_ibfk_1
        foreign key (idVehicle) references Vehicle (idVehicle)
            on delete cascade
);

create index idModel
    on Vehicle (vehicule_model_id);

create index idType
    on Vehicle (idType);

