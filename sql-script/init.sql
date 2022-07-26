create table CarModel (
    id int identity(1,1) primary key not null,
    name varchar(255),
    description varchar(1024)
);

create table Car (
    id int identity(1,1) primary key not null,
    carModelId int,
    description varchar(1024),
    isReserved varchar(8),
    enabled varchar(8),
    foreign key(carModelId) references CarModel(id)
);

create index carModelId on Car(carModelId);

create table Price (
    id int identity(1,1) primary key not null,
    carModelId int,
    price int,
    foreign key(carModelId) references CarModel(id)
);

create index carModelId on Price(carModelId);

insert into CarModel
values
('Toyota Camry','Toyota Camry'),
('BMW 650','BMW 650');

insert into Car
values
(1,'#1 Toyota Camry','false','true'),
(1,'#2 Toyota Camry','false','true'),
(2,'#1 BMW 650','false','true'),
(2,'#2 BMW 650','false','true');

insert into Price
values
(1,300),
(2,500);

create table [User] (
    id int identity(1,1) primary key not null,
    name varchar(255),
    phoneNumber varchar(255),
    idNumber varchar(20)
);

insert into [User]
values
('Li Lei','13888888888','440305202207240001'),
('Han Mei Mei','13999999999','440305202207240002');

create index idNumber on [User](idNumber);

create table [Order] (
    id int identity(1,1) primary key not null,
    userId int,
    carId int,
    carModelId int,
    startDate date,
    endDate date,
    deposit int,
    status varchar(20),
    createTime date,
    enabled varchar(8),
    foreign key(userId) references [User](id),
    foreign key(carId) references Car(id)
);

create index carModelId on [Order] (carModelId );

create table Bill (
    id int identity(1,1) primary key not null,
    orderId int,
    userId int,
    carModelId int,
    actualReturnDate date,
    expireDays int,
    rentCost int,
    carDamageCost int,
    fines int,
    totalCost int,
    isPaid varchar(8),
    addAmount int,
    returnAmount int,
    foreign key(orderId) references [Order](id)
);

create unique index orderId on Bill (orderId );
create unique index carModelId on Bill (carModelId );

create table Lock (
    [key] varchar(255) primary key not null,
    value varchar(255)
);