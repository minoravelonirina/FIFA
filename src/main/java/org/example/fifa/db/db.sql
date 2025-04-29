\ c postgres drop database if exists championnat_europeenne;
CREATE DATABASE championnat_europeenne;
\ c championnat_europeenne -- Enum types
create type position as enum (
    'GOAL_KEEPER',
    'MIDFIELDER',
    'STRIKER',
    'DEFENSE'
);
create type status as enum ('NOT_STARTED', 'STARTED', 'FINISHED');
create type duration_unit as enum ('SECOND', 'MINUTE', 'HOUR');
create type championship as enum (
    'PREMIER_LEAGUE',
    'LA_LIGA',
    'BUNDESLIGA',
    'SERIA',
    'LIGUE_1'
);
-- Season table
create table season (
    id varchar primary key,
    year integer not null,
    alias varchar(10) not null,
    status status default 'NOT_STARTED'
);
-- Player table
create table player (
    id varchar primary key,
    name varchar(100) not null,
    number integer unique,
    age integer,
    position position,
    nationality varchar(100),
    club_id varchar references club(id)
);
-- Coach table
create table coach (
    id varchar primary key,
    name varchar(100) unique,
    nationality varchar(100)
);

create table club (
    id varchar primary key,
    name varchar(100) not null,
    acronym varchar(3),
    year_creation integer,
    stadium varchar(100),
    coach_id varchar references coach(id),
    championship championship not null
);

-- Match table
create table "match" (
    id varchar primary key,
    club_playing_home varchar references club(id),
    club_playing_away varchar references club(id),
    stadium varchar(100),
    match_datetime timestamp,
    status status default 'NOT_STARTED',
    season_id varchar references season(id)
);
-- Goal table
create table goal (
    id varchar primary key,
    match_id varchar references "match"(id),
    club_id varchar references club(id),
    player_id varchar references player(id),
    minute_of_goal integer check (
        minute_of_goal between 1 and 90
    ),
    own_goal boolean default false
);

-- Club Statistics table
create table club_statistics (
    id varchar primary key,
    club_id varchar references club(id),
    season_id varchar references season(id),
    ranking_points integer default 0,
    scored_goals integer default 0,
    conceded_goals integer default 0,
    difference_goals integer default 0,
    clean_sheet_number integer default 0,
    unique(club_id, season_id)
);

-- Player Statistics table
create table player_statistics (
    id varchar primary key,
    player_id varchar references player(id),
    season_id varchar references season(id),
    scored_goals integer default 0,
    playing_time_value numeric,
    playing_time_unit duration_unit,
    unique(player_id, season_id)
);


/*create table period (
    id serial primary key,
    date_format_string varchar(20);
);*/
