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

-- Club table
create table club (
                      id serial primary key,
                      name varchar(100),
                      acronym varchar(10),
                      creation_date timestamp,
                      stadium_id integer references stadium(id),
                      coach_id integer references coach(id),
                      id_league integer references league(id)
);

create table championship (
                              id serial primary key,
                              id_period integer references period(id)
)

-- Match table
create table match (
                       id serial primary key,
                       date timestamp,
                       stadium_id integer references stadium(id),
                       id_clubs_in_match integer references clubs_in_match(id),
                       id_championship references championship(id)
);

-- ClubsInMatch table
create table clubs_in_match (
                                id serial primary key,
                                match_id integer references match(id),
                                club_1_id integer references club(id),
                                club_2_id integer references club(id),
                                home_club_id integer references club(id),
                                away_club_id integer references club(id)
);

create table score (
                       id serial primary key,
                       rang integer,
                       id_match integer references match(id),
                       id_club integer references club(id),
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
