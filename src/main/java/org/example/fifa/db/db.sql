\c postgres
   drop database if exists championnat_europeenne;

CREATE DATABASE championnat_europeenne;
\c championnat_europeenne

-- Enum types
create type position as enum ('GOALKEEPER', 'MIDFIELDER', 'FORWARD', 'DEFENDER');

-- Player table
create table player (
                        id serial primary key,
                        name varchar(100),
                        number integer unique,
                        age integer,
                        position position,
                        nationality varchar(100)
);

-- Coach table
create table coach (
                       id serial primary key,
                       name varchar(100) unique,
                       nationality varchar(100)
);

-- Stadium table
create table stadium (
                         id serial primary key,
                         name varchar(100)
);

create table league (
                        id serial primary key,
                        name varchar(100)
);

create table championship (
                              id serial primary key,
                              period_id integer references period(id)
)

-- Club table
create table club (
                      id serial primary key,
                      name varchar(100),
                      acronym varchar(10),
                      creation_date timestamp,
                      stadium_id integer references stadium(id),
                      coach_id integer references coach(id),
                      league_id integer references league(id)  -- club maromaro anatina league iray
);

-- Match table
create table matches (
                       id serial primary key,
                       date timestamp,
                       stadium_id integer references stadium(id), -- manana stade iray
                       championship_id references championship(id)  ,-- match maromaro anaty championnat iray
                        home_club_id integer references club(id),
                        away_club_id integer references club(id),
                        check (home_club_id <> away_club_id) -- les 2 clubs sont different
);


create table score (
                       id serial primary key,
                       rang integer,
                       matches_id integer references matches(id),
                       club_id integer references club(id),
                       points integer
);

create table period (
                        id serial primary key,
                        date_format_string varchar(20);
);

create table ranking (
                         id serial primary key,
                         number integer unique,
                         club_id integer references club(id),
                         points integer
);