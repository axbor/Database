-- Delete the tables if they exist. Set foreign_key_checks = 0 to
-- disable foreign key checks, so the tables may be dropped in
-- arbitrary order.
set foreign_key_checks = 0;

drop table if exists PalletsInBatch;
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

-- Creating the tables

create table Customer (
	customerName varchar(50),
	address varchar(50),
	primary key(customerName)
);

create table Ordering (
	orderNumber int auto_increment,
	customerName varchar(50),
	orderDate datetime,
	expectedDeliverDate date,
	deliveryDate datetime,
	primary key(orderNumber),
	foreign key(customerName) references Customer(customerName)
);

create table Recipe(
	cookieName varchar(50),
	primary key(cookieName)
);

create table Pallet(
	palletNumber int auto_increment,
	orderNumber int,
	status varchar(50),
	prodTime dateTime,
	primary key(palletNumber),
	foreign key(orderNumber) references Ordering(orderNumber)
);

create table RawMaterial (
	ingredientName varchar(50),
	amount double, 
	lastDeliveryDate date, 
	lastDeliveryAmount int,
	primary key(ingredientName)
);

create table ProductionBatch(
	batchNumber int auto_increment, 
	cookieName varchar(50), 
	prodDate datetime, 
	QA varchar(50),
	primary key(batchNumber),
	foreign key(cookieName) references Recipe(cookieName)
);

create table PalletsInBatch(
	palletNumber int,
	batchNumber int,
	primary key(palletNumber, batchNumber),
	foreign key(palletNumber) references Pallet(palletNumber),
	foreign key(batchNumber) references ProductionBatch(batchNumber)
);

create table PalletOrder (
	orderNumber int,
	cookieName varchar(50),
	nbrOfPallets int,
	
	primary key(orderNumber, cookieName),
	foreign key(orderNumber) references Ordering(orderNumber),
	foreign key(cookieName) references Recipe(cookieName)
);

create table CookieContains (
	cookieName varchar(50),
	ingredientName varchar(50),
	amount double,
	primary key(cookieName, ingredientName),
	foreign key(cookieName) references Recipe(cookieName),
	foreign key(ingredientName) references RawMaterial(ingredientName)
);


-- Insert into RawMaterial


insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount)
 values('Flour', 20000,  now(), 10);

insert into RawMaterial(ingredientName,amount,  lastDeliveryDate, lastDeliveryAmount) 
values('Butter', 20000, now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Icing sugar', 20000, now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Roasted, chopped nuts', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Ground, roasted nuts', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Fine-ground nuts', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Bread crumbs', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Sugar', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Egg whites', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Chocolate', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Marzipan', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Egg', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Potato starch', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Wheat flour', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Sodium bicarbonate', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Vanilla', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Chopped almonds', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Cinnamon', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Vanilla sugar', 20000,  now(), 10);

insert into RawMaterial(ingredientName, amount, lastDeliveryDate, lastDeliveryAmount) 
values('Eggs', 20000,  now(), 10);


-- Insert into Recipe


insert into Recipe(cookieName) values('Nut ring');
insert into Recipe(cookieName) values('Nut cookie');
insert into Recipe(cookieName) values('Amneris');
insert into Recipe(cookieName) values('Tango');
insert into Recipe(cookieName) values('Almond Delight');
insert into Recipe(cookieName) values('Berliner');

-- Insert into CookieContains

insert into CookieContains(cookieName, ingredientName, amount)
values('Nut ring', 'Butter', 0.450);
insert into CookieContains(cookieName, ingredientName, amount)
values('Nut ring', 'Flour', 0.450);
insert into CookieContains(cookieName, ingredientName, amount)
values('Nut ring', 'Icing sugar', 0.190);
insert into CookieContains(cookieName, ingredientName, amount)
values('Nut ring', 'Roasted, chopped nuts', 0.225);

insert into CookieContains(cookieName, ingredientName, amount)
values('Nut cookie', 'Fine-ground nuts', 0.750);
insert into CookieContains(cookieName, ingredientName, amount)
values('Nut cookie', 'Ground, roasted nuts', 0.625);
insert into CookieContains(cookieName, ingredientName, amount)
values('Nut cookie', 'Bread crumbs', 0.125);
insert into CookieContains(cookieName, ingredientName, amount)
values('Nut cookie', 'Sugar', 0.375);
insert into CookieContains(cookieName, ingredientName, amount)
values('Nut cookie', 'Egg whites', 0.35);
insert into CookieContains(cookieName, ingredientName, amount)
values('Nut cookie', 'Chocolate', 0.050);

insert into CookieContains(cookieName, ingredientName, amount)
values('Amneris', 'Marzipan', 0.750);
insert into CookieContains(cookieName, ingredientName, amount)
values('Amneris', 'Butter', 0.250);
insert into CookieContains(cookieName, ingredientName, amount)
values('Amneris', 'Eggs', 0.250);
insert into CookieContains(cookieName, ingredientName, amount)
values('Amneris', 'Potato starch', 0.025);
insert into CookieContains(cookieName, ingredientName, amount)
values('Amneris', 'Wheat flour', 0.025);

insert into CookieContains(cookieName, ingredientName, amount)
values('Tango', 'Butter', 0.200);
insert into CookieContains(cookieName, ingredientName, amount)
values('Tango', 'Sugar', 0.250);
insert into CookieContains(cookieName, ingredientName, amount)
values('Tango', 'Flour', 0.300);
insert into CookieContains(cookieName, ingredientName, amount)
values('Tango', 'Sodium bicarbonate', 0.004);
insert into CookieContains(cookieName, ingredientName, amount)
values('Tango', 'Vanilla', 0.002);

insert into CookieContains(cookieName, ingredientName, amount)
values('Almond delight', 'Butter', 0.400);
insert into CookieContains(cookieName, ingredientName, amount)
values('Almond delight', 'Sugar', 0.270);
insert into CookieContains(cookieName, ingredientName, amount)
values('Almond delight', 'Chopped almonds', 0.279);
insert into CookieContains(cookieName, ingredientName, amount)
values('Almond delight', 'Flour', 0.400);
insert into CookieContains(cookieName, ingredientName, amount)
values('Almond delight', 'Cinnamon', 0.010);

insert into CookieContains(cookieName, ingredientName, amount)
values('Berliner', 'Flour', 0.350);
insert into CookieContains(cookieName, ingredientName, amount)
values('Berliner', 'Butter', 0.250);
insert into CookieContains(cookieName, ingredientName, amount)
values('Berliner', 'Icing sugar', 0.100);
insert into CookieContains(cookieName, ingredientName, amount)
values('Berliner', 'Eggs', 0.050);
insert into CookieContains(cookieName, ingredientName, amount)
values('Berliner', 'Vanilla sugar', 0.005);
insert into CookieContains(cookieName, ingredientName, amount)
values('Berliner', 'Chocolate', 0.050);


-- Insert into Customer


insert into Customer(customerName, address) values('Finkakor AB','Helsingborg');
insert into Customer(customerName, address) values('Småbröd AB','Malmö');
insert into Customer(customerName, address) values('Kaffebröd AB','Lanskrona');
insert into Customer(customerName, address) values('Bjudkakor AB','Ystad');
insert into Customer(customerName, address) values('Kalaskakor AB','Trelleborg');
insert into Customer(customerName, address) values('Partykakor AB','Kristianstad');
insert into Customer(customerName, address) values('Gästkakor AB','Hässlehåla');
insert into Customer(customerName, address) values('Skånekakor AB','Perstorp');


insert into Ordering values(default, 'Finkakor AB', now(), '2014-04-03', null );
insert into PalletOrder values(1, 'Tango', 5);
insert into PalletOrder values(1, 'Berliner', 5);
insert into PalletOrder values(1, 'Nut ring', 5);
insert into Ordering values(default, 'Finkakor AB', now(), '2014-04-04', null);
insert into PalletOrder values(2, 'Tango', 5);
insert into PalletOrder values(2, 'Berliner', 5);
insert into PalletOrder values(2, 'Nut ring', 5);
insert into Ordering values(default, 'Finkakor AB', now(), '2014-04-05', null);
insert into PalletOrder values(3, 'Tango', 5);
insert into PalletOrder values(3, 'Berliner', 5);
insert into PalletOrder values(3, 'Nut ring', 5);
insert into Ordering values(default, 'Finkakor AB', now(), '2014-04-06', null);
insert into PalletOrder values(4, 'Tango', 5);
insert into PalletOrder values(4, 'Berliner', 5);
insert into PalletOrder values(4, 'Nut ring', 5);
insert into Ordering values(default, 'Småbröd AB', now(), '2014-04-07', null);
insert into PalletOrder values(5, 'Tango', 3);
insert into PalletOrder values(5, 'Berliner', 5);
insert into PalletOrder values(5, 'Amneris', 5);
insert into Ordering values(default, 'Skånekakor AB', now(), '2014-04-08', null);
insert into PalletOrder values(6, 'Tango', 2);
insert into PalletOrder values(6, 'Amneris', 5);
insert into PalletOrder values(6, 'Nut ring', 5);
insert into Ordering values(default, 'Skånekakor AB', now(), '2014-04-09', null);
insert into PalletOrder values(7, 'Amneris', 1);
insert into PalletOrder values(7, 'Berliner', 5);
insert into PalletOrder values(7, 'Nut ring', 5);

