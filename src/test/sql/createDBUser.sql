drop table if exists activity;
drop table if exists users;
drop table if exists sport;
drop table if exists route;


create table users (
  uid serial primary key,
  name varchar(30),
  email varchar(35),
  unique(email)
);

create table sport (
  sid serial primary key,
  description varchar(20),
  name varchar(30)
);

create table route (
  rid serial primary key,
  startLoc varchar(20),
  endLoc varchar(20),
  distance decimal
);

create table activity (
  aid serial primary key,
  duration varchar(25),
  date varchar(11),
  uid int references users(uid),
  rid int references route(rid),
  sid int references sport(sid)
  timestamp timestamp
);
