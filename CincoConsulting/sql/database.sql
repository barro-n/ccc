use `bvaughn5`;

drop table if exists invoice_item;
drop table if exists invoice;
drop table if exists item;
drop table if exists company;
drop table if exists email;
drop table if exists person;
drop table if exists address;
drop table if exists zip_code;
drop table if exists state;

create table state (
    state_id int auto_increment primary key,
    state_code varchar(2) unique not null
);

create table zip_code (
    zip_id int auto_increment primary key,
    zip_code varchar(10) unique not null
);

create table address (
    address_id int auto_increment primary key,
    street varchar(255) not null,
    city varchar(100) not null,
    state_id int not null, 
    zip_id int not null,
    foreign key (state_id) references state(state_id),
    foreign key (zip_id) references zip_code(zip_id)
);

create table person (
    person_id int auto_increment primary key,
    person_uuid varchar(36) unique not null,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    phone varchar(20)
);

create table email (
    email_id int auto_increment primary key,
    person_id int not null,
    email_address varchar(255) not null,
    foreign key (person_id) references person(person_id) on delete cascade
);

create table company (
    company_id int auto_increment primary key,
    company_uuid varchar(36) unique not null,
    name varchar(255) not null,
    primary_contact_id int not null,
    address_id int not null,
    foreign key (primary_contact_id) references person(person_id),
    foreign key (address_id) references address(address_id)
);

create table item (
    item_id int auto_increment primary key,
    item_uuid varchar(36) unique not null,
    name varchar(255) not null,
    type char(1) not null,
    cost_per_unit decimal(10, 2) null,
    cost_per_hour decimal(10, 2) null,
    service_fee decimal(10, 2) null,
    annual_fee decimal(10, 2) null
);

create table invoice (
    invoice_id int auto_increment primary key,
    invoice_uuid varchar(36) unique not null,
    customer_id int not null,
    salesperson_id int not null,
    invoice_date date not null,
    foreign key (customer_id) references company(company_id),
    foreign key (salesperson_id) references person(person_id)
);

create table invoice_item (
    invoice_item_id int auto_increment primary key,
    invoice_id int not null,
    item_id int not null,
    type varchar(20) not null,
    quantity int null,
    consultant_id int null,
    billed_hours decimal(10, 2) null,
    start_date date null,
    end_date date null,
    foreign key (invoice_id) references invoice(invoice_id) on delete cascade,
    foreign key (item_id) references item(item_id),
    foreign key (consultant_id) references person(person_id)
);

insert into state (state_code) values ('DC'), ('NE'), ('VA'), ('MD'), ('NY'), ('CA'), ('TX');

insert into zip_code (zip_code) values ('20566'), ('68508'), ('22102'), ('20740'), ('10001'), ('90210'), ('73301');

insert into address (street, city, state_id, zip_id) values 
('1 Cyber Command Center', 'Washington', 1, 1),
('1400 Security Dr', 'Lincoln', 2, 2),
('123 Threat Intel Blvd', 'McLean', 3, 3),
('555 Park Ave', 'New York', 5, 5),
('888 Hollywood Blvd', 'Los Angeles', 6, 6),
('404 Tech Lane', 'Austin', 7, 7);

insert into person (person_uuid, first_name, last_name, phone) values 
('11111111-1111-1111-1111-111111111111', 'Barron', 'Vaughn', '555-010-1010'),
('22222222-2222-2222-2222-222222222222', 'Lewis', 'Rokke', '555-020-2020'),
('33333333-3333-3333-3333-333333333333', 'Enver', 'Hoxha', '555-030-3030'),
('44444444-4444-4444-4444-444444444444', 'Christopher', 'Bourke', '555-040-4040'),
('55555555-5555-5555-5555-555555555555', 'Ada', 'Lovelace', '555-050-5050'),
('66666666-6666-6666-6666-666666666666', 'Alan', 'Turing', '555-060-6060'),
('77777777-7777-7777-7777-777777777777', 'Grace', 'Hopper', '555-070-7070');

insert into email (person_id, email_address) values 
(1, 'barron@barron47.com'),
(1, 'kiriakou@cia.gov'),
(2, 'netanyahu@telaviv.goy'),
(3, 'tungtungsahur@brainrot.com'),
(5, 'ada@analytical.engine'),
(6, 'alan@bletchleypark.uk'),
(7, 'grace@navy.mil');

insert into company (company_uuid, name, primary_contact_id, address_id) values 
('55555555-5555-5555-5555-555555555555', 'NSA', 1, 1),
('66666666-6666-6666-6666-666666666666', 'Sentinel Data Solutions', 2, 3),
('77777777-8888-9999-0000-111111111111', 'Wayne Enterprises', 5, 4),
('88888888-9999-0000-1111-222222222222', 'Stark Industries', 6, 5),
('99999999-0000-1111-2222-333333333333', 'Cyberdyne Systems', 7, 6);

insert into item (item_uuid, name, type, cost_per_unit, cost_per_hour, service_fee, annual_fee) values 
('77777777-7777-7777-7777-777777777777', 'Next-Gen Hardware Firewall v9', 'E', 2500.00, null, null, null),
('88888888-8888-8888-8888-888888888888', 'IDPS Security Appliance', 'E', 850.00, null, null, null),
('99999999-9999-9999-9999-999999999999', 'Penetration Testing (Red Team)', 'S', null, 250.00, null, null),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Incident Response Retainer', 'S', null, 350.00, null, null),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Zero Trust Network Access', 'L', null, null, 500.00, 15000.00),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'SIEM Enterprise Software', 'L', null, null, 1000.00, 25000.00),
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Cloud Server Rack', 'E', 4500.00, null, null, null),
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Database Optimization', 'S', null, 175.00, null, null),
('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Enterprise Antivirus', 'L', null, null, 250.00, 5000.00);

insert into invoice (invoice_uuid, customer_id, salesperson_id, invoice_date) values 
('dddddddd-dddd-dddd-dddd-dddddddddddd', 2, 1, '2026-03-01'), -- Barron sells to Sentinel Data
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 1, 2, '2026-03-15'), -- Lewis sells to NSA
('10101010-1010-1010-1010-101010101010', 3, 4, '2026-04-10'),
('20202020-2020-2020-2020-202020202020', 4, 3, '2026-05-22'),
('30303030-3030-3030-3030-303030303030', 5, 1, '2026-06-05');

insert into invoice_item (invoice_id, item_id, type, quantity, consultant_id, billed_hours, start_date, end_date) values 
-- Sentinel Data buys firewalls and pentesting
(1, 1, 'Purchase', 5, null, null, null, null),
(1, 3, 'BilledService', null, 3, 40.00, null, null),
-- NSA leases appliances and buys a ZTNA license
(2, 2, 'Lease', 12, null, null, '2026-04-01', '2029-03-31'), 
(2, 5, 'BilledLicense', null, null, null, '2026-04-01', '2027-03-31'),
-- Wayne Enterprises buys cloud racks and optimization
(3, 7, 'Purchase', 10, null, null, null, null),
(3, 8, 'BilledService', null, 6, 25.50, null, null),
-- Stark Industries leases firewalls and buys SIEM
(4, 1, 'Lease', 8, null, null, '2026-06-01', '2029-05-31'),
(4, 6, 'BilledLicense', null, null, null, '2026-06-01', '2027-05-31'),
-- Cyberdyne Systems buys everything
(5, 7, 'Lease', 20, null, null, '2026-07-01', '2029-06-30'),
(5, 9, 'BilledLicense', null, null, null, '2026-07-01', '2028-06-30'),
(5, 3, 'BilledService', null, 5, 100.00, null, null);