drop table if exists User;
drop table if exists Product;
drop table if exists Bid;
drop table if exists RelatedProduct;

create table User
(
  id VARCHAR(10) PRIMARY KEY,
  salutation varchar(10),
  firstname varchar(20),
  lastname varchar(20),
  email varchar(150),
  password varchar(8),
  dateOfBirth date,
  wonAuctionsCount int,
  lostAuctionsCount int,
  runningAuctionsCount int,
  balance int
);

create table Product
(
  id VARCHAR(10) primary key,
  name varchar(100),
  year int,
  producer varchar(100),
  expired boolean,
  auctionEnd date,
  image varchar(200),
  imageAlt varchar(200)
);

create table Bid
(
  id VARCHAR(10) primary key,
  product VARCHAR(10) references Product(id),
  user VARCHAR(10) references User(id),
  amount int
);

create table RelatedProduct
(
  id VARCHAR(10) primary key,
  product VARCHAR(10) references Product(id),
  nameDe varchar(255)
);