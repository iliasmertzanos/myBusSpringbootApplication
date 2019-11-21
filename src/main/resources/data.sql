/**
 * CREATE Script for init of DB
 */

-- Create 3 OFFLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username) values (1, now(), false, 'OFFLINE',
'driver01pw', 'driver01');

insert into driver (id, date_created, deleted, online_status, password, username) values (2, now(), false, 'OFFLINE',
'driver02pw', 'driver02');

insert into driver (id, date_created, deleted, online_status, password, username) values (3, now(), false, 'OFFLINE',
'driver03pw', 'driver03');


-- Create 3 ONLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username) values (4, now(), true, 'ONLINE',
'driver04pw', 'driver04');

insert into driver (id, date_created, deleted, online_status, password, username) values (5, now(), false, 'ONLINE',
'driver05pw', 'driver05');

insert into driver (id, date_created, deleted, online_status, password, username) values (6, now(), false, 'ONLINE',
'driver06pw', 'driver06');

-- Create 1 OFFLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username)
values
 (7,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'OFFLINE',
'driver07pw', 'driver07');

-- Create 1 ONLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username)
values
 (8,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'ONLINE',
'driver08pw', 'driver08');

--Create bus entities not selected by drivers

insert into bus (convertible, date_created, engine_type, licenseplate, seatcount, id) values ('TRUE', now(),  'PETROL' , 'PO-OZ24654','5', '12' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('TRUE', now(), 'ELECTRIC'   , 'PO-RT8523', '4','5', '13' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('TRUE', now(),  'HYBRID'   , 'HJ-WE7894', '4','5', '14' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('FALSE', now(), 'ELECTRIC'   , 'YX-NM6987', '3','5', '15' ) ;
insert into bus (convertible, date_created,  engine_type, licenseplate, rating, seatcount, id) values ('FALSE', now(),  'GAS'   , 'AS-OZ24654', '1','5', '16' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('TRUE', now(), 'PETROL'   , 'GT-OZ24654', '2','5', '17' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, seatcount, id) values ('TRUE', now(),  'HYBRID' , 'AC-AJ5077','5', '20' ) ;


--Allocate buses to new drivers
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('FALSE', now(), 'PETROL'   , 'YΡ-NM9987', '3','5', '18' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('FALSE', now(), 'GAS'   , 'GH-NM1000', '3','5', '19' ) ;

insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (9, now(), false, 'ONLINE','driver09pw', 'driver09','18');

insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (10, now(), false, 'ONLINE','driver10pw', 'driver10','19');


--Create Drivers and buses for filtering for testing

--create buses

insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('TRUE', now(),'GAS'   , 'VB-NM7894', '3','5', '21' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('FALSE', now(),'GAS'   , 'QW-RT1000', '3','5', '25' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('FALSE', now(),'GAS'   , 'AS-OZ29854', '1','5', '30' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('TRUE', now(), 'ELECTRIC'   , 'PM-RT8523', '4','5', '27' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('FALSE', now(), 'ELECTRIC'   , 'GH-ER9650', '3','5', '23' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('FALSE', now(), 'ELECTRIC'   , 'ro-NM6987', '3','5', '29' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('TRUE', now(),  'PETROL' , 'PO-OZ6654','2', '5', '26' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('FALSE', now(), 'PETROL'   , 'OP-NM1236', '3','5', '22' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('TRUE', now(), 'PETROL'   , 'PL-OZ24654', '2','5', '31' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('TRUE', now(), 'HYBRID'   , 'HJ-ZZ1000', '3','5', '24' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('TRUE', now(),  'HYBRID'   , 'WE-WE7894', '4','5', '28' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('FALSE', now(), 'HYBRID', 'ZT-NM9007', '3','5', '32' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('FALSE', now(), 'HYBRID'   , 'VB-NM3000', '3','5', '33' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, seatcount, id) values ('TRUE', now(),  'HYBRID' , 'HH-HH5077','5', '34' ) ;
insert into bus (convertible, date_created, engine_type, licenseplate, rating, seatcount, id) values ('FALSE', now(), 'HYBRID'   , 'KL-KL9999', '3','5', '35' ) ;

--Allocate bus to drivers

insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (11, now(), false, 'ONLINE','driver10', 'Jan', '21');
insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (12, now(), false, 'ONLINE', 'driver10', 'pedro', '22');
insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (13,  now(), false, 'ONLINE','driver09', 'george', '23');
insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (14, now(), false, 'ONLINE', 'driver10', 'iris', '24');
insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (15, now(), false, 'ONLINE', 'driver10', 'Joachim', '25');
insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (16, now(), false, 'ONLINE',  'driver10', 'Ben','26');
insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (17, now(), false, 'ONLINE', 'driver10', 'Arne', '27');
insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (18, now(), false, 'ONLINE', 'driver10', 'Richard', '28');
insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (19, now(), false, 'ONLINE', 'driver10', 'Thom', '29');
insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (20, now(), false, 'ONLINE', 'driver10', 'Ali', '30');
insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (21, now(), false, 'ONLINE','driver10', 'Sofia', '31');
insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (22, now(), false, 'ONLINE','driver10', 'Maria', '32');
insert into driver (id, date_created, deleted, online_status, password, username, bus_details_id) values (23, now(), false, 'ONLINE','driver10', 'kostas', '33');


