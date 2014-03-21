-- Delete the tables if they exist. Set foreign_key_checks = 0 to
-- disable foreign key checks, so the tables may be dropped in
-- arbitrary order.
set foreign_key_checks = 0;

drop table if exists BatchesInPallet;
drop table if exists OrdersInShipment;
drop table if exists PalletOrder;
drop table if exists CookieContains;
drop table if exists RawMaterial;
drop table if exists Pallet;
drop table if exists ProductionBatch;
drop table if exists Ordering;
drop table if exists Recipe;
drop table if exists Shipment;
drop table if exists Customer;

set foreign_key_checks = 1;

create table Customer (
	customerName varchar(50),
	address varchar(50),
	primary key(customerName)
);

create table Ordering (
	orderNumber int,
	customerName varchar(50),
	orderDate date,
	deliveryDate date,
	primary key(orderNumber),
	foreign key(customerName) references Customer(customerName)
);

create table Recipe(
	cookieName varchar(20),
	primary key(cookieName)
);

create table Shipment (
	shipmentNumber int,
	primary key(shipmentNumber)
);

create table Pallet(
	palletNumber int,
	shipmentNumber int,
	status varchar(20),
	primary key(palletNumber),
	foreign key(shipmentNumber) references Shipment(shipmentNumber)
);


create table RawMaterial (
	ingredientName varchar(20),
	amount int, 
	lastDeliveryDate date, 
	lastDeliveryAmount int, 
	primary key(ingredientName)
);

create table ProductionBatch(
	batchNumber int, 
	cookieName varchar(20), 
	prodDate date, 
	QA varchar(20),
	primary key(batchNumber),
	foreign key(cookieName) references Recipe(cookieName)
);

create table batchesInPallet(
	palletNumber int,
	batchNumber int,
	primary key(palletNumber, batchNumber),
	foreign key(palletNumber) references Pallet(palletNumber),
	foreign key(batchNumber) references ProductionBatch(batchNumber)
);

create table OrdersInShipment (
	orderNumber int, 
	shipmentNumber int, 
	primary key(orderNumber, shipmentNumber),
	foreign key(orderNumber) references Ordering(orderNumber),
	foreign key(shipmentNumber) references Shipment(shipmentNumber) 
);

create table PalletOrder (
	orderNumber int,
	cookieName varchar(20),
	amount int,
	primary key(orderNumber, cookieName),
	foreign key(orderNumber) references Ordering(orderNumber),
	foreign key(cookieName) references Recipe(cookieName)
);

create table CookieContains (
	cookieName varchar(20),
	ingredientName varchar(20),
	amount int,
	primary key(cookieName, ingredientName),
	foreign key(cookieName) references Recipe(cookieName),
	foreign key(ingredientName) references RawMaterial(ingredientName)
);
