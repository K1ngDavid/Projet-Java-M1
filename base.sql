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
    password         varchar(255) null,
    constraint email
        unique (email)
);

create table Command
(
    idCommand     int auto_increment
        primary key,
    commandStatus varchar(255) null,
    commandDate   date         not null,
    idClient      int          null,
    constraint FK_Command_Client
        foreign key (idClient) references Client (idClient)
            on delete set null
);

create table Invoice
(
    idInvoice   int auto_increment
        primary key,
    invoiceDate date           not null,
    totalAmount decimal(38, 2) null,
    idCommand   int            null,
    constraint FK_Invoice_Command
        foreign key (idCommand) references Command (idCommand)
            on delete cascade
);

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
    idVehicle             int auto_increment
        primary key,
    status                varchar(255)                                                                                                                              null,
    price                 decimal(38, 2)                                                                                                                            null,
    countryOfOrigin       varchar(255)                                                                                                                              null,
    vehicule_model_id     int                                                                                                                                       null,
    idType                int                                                                                                                                       null,
    horse_power           int                                                                                                                                       null,
    vehicle_power_source  enum ('DIESEL', 'ELECTRIC', 'HYBRID', 'PETROL')                                                                                           null,
    numberOfDoors         int                                                                                                                                       null,
    transmissionType      enum ('AUTOMATIC', 'AWD', 'CVT', 'FOUR_WHEEL_DRIVE', 'MANUAL', 'SEMI_AUTOMATIC')                                                          null,
    image_url             varchar(255)                                                                                                                              null,
    category              enum ('SPORTIVE', 'SUPERSUPERSPORTIVE', 'BERLINE', 'SUV', 'HATCHBACK', 'COUPE', 'CABRIOLET', 'CROSSOVER', 'MINIVAN', 'PICKUP', 'CLASSIC') null,
    engine_capacity       int                                                                                                                                       null,
    vehicleType           varchar(31)                                                                                                                               not null,
    engineCapacity        int                                                                                                                                       null,
    ClientEntity_idClient int                                                                                                                                       null,
    constraint FK_Vehicle_Model
        foreign key (vehicule_model_id) references Model (idModel)
            on delete set null,
    constraint FK_Vehicle_Type
        foreign key (idType) references VehicleType (idType)
            on delete set null,
    constraint FKo9mnvbku9969x99is6lj1wxxj
        foreign key (ClientEntity_idClient) references Client (idClient)
);

create table CommandLine
(
    idCommand int not null,
    idVehicle int not null,
    primary key (idCommand, idVehicle),
    constraint FK_CommandLine_Command
        foreign key (idCommand) references Command (idCommand)
            on delete cascade,
    constraint FK_CommandLine_Vehicle
        foreign key (idVehicle) references Vehicle (idVehicle)
            on delete cascade
);

