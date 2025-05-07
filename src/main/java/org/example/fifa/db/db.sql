\ c postgres drop database if exists championnat_europeenne;
CREATE DATABASE championnat_europeenne;
\ c championnat_europeenne
create type player_position as enum (
    'GOAL_KEEPER',
    'MIDFIELDER',
    'STRIKER',
    'DEFENSE'
);
create type status as enum ('NOT_STARTED', 'STARTED', 'FINISHED');
create type duration_unit as enum ('SECOND', 'MINUTE', 'HOUR');


create table season (
                        id varchar primary key,
                        year integer not null,
                        alias varchar(10) not null,
                        status status default 'NOT_STARTED'
);

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
                      coach_id varchar references coach(id) not null,
                      league varchar(100)
);

create table player (
                        id varchar primary key,
                        name varchar(100) not null,
                        number integer,
                        age integer,
                        position player_position,
                        nationality varchar(100),
                        club_id varchar references club(id),
                        unique(number, club_id)
);

create table "match" (
                         id varchar primary key,
                         club_playing_home varchar references club(id),
                         club_playing_away varchar references club(id),
                         stadium varchar(100),
                         match_datetime timestamp,
                         status status default 'NOT_STARTED',
                         season_id varchar references season(id)
);

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



-- 1. Insertion de la saison 2024-2025
INSERT INTO season (id, year, alias, status)
VALUES ('S2024-2025', 2024, 'S2024-2025', 'NOT_STARTED');

-- 2. Insertion des entraîneurs
INSERT INTO coach (id, name, nationality) VALUES
                                              ('C1', 'Carlo Ancelotti', 'Italien'),
                                              ('C2', 'Hansi Flick', 'Allemand');

-- 3. Insertion des clubs
INSERT INTO club (id, name, acronym, year_creation, stadium, coach_id, league) VALUES
                                                                                         ('CL-RMA', 'Real Madrid FC', 'RMA', 1902, 'Santiago Bernabeu', 'C1', 'LA_LIGA'),
                                                                                         ('CL-FCB', 'FC Barcelone', 'FCB', 1899, 'Lluís Companys', 'C2', 'LA_LIGA'),
                                                                                         ('CL-ATM', 'Atletico Madrid', 'ATM', 1880, 'Metropolitano', 'C2', 'LA_LIGA'),
                                                                                         ('CL-MCI', 'Manchester City', 'MCI', 1880, 'Etihad Stadium', 'C1', 'PREMIER_LEAGUE'),
                                                                                         ('CL-PSG', 'Paris Saint Germain', 'PSG', 1970, 'Parc des princes', 'C1', 'LIGUE_1'),
                                                                                         ('CL-FCBM', 'FC Bayern Munich', 'FCB', 1900, 'Allianz Arena', 'C2', 'BUNDESLIGA'),
                                                                                         ('CL-JUV', 'Juventus FC', 'JUV', 1897, 'Juventus Stadium', 'C1', 'SERIA');

-- 4. Insertion des joueurs
INSERT INTO player (id, name, number, age, position, nationality, club_id) VALUES
                                                                               ('P1', 'Vinicius Jr', 7, 24, 'STRIKER', 'Brésil', 'CL-RMA'),
                                                                               ('P2', 'Kylian Mbappé', 9, 26, 'STRIKER', 'France', 'CL-RMA'),
                                                                               ('P3', 'Lamine Yamal', 19, 17, 'STRIKER', 'Espagne', 'CL-FCB'),
                                                                               ('P4', 'Ferran Torres', 7, 25, 'STRIKER', 'Espagne', 'CL-FCB');

-- 5. Insertion des matchs
INSERT INTO "match" (id, club_playing_home, club_playing_away, stadium, match_datetime, status, season_id) VALUES
                                                                                                               ('M1', 'CL-RMA', 'CL-FCB', 'Santiago Bernabeu', '2025-05-01 21:00:00', 'NOT_STARTED', 'S2024-2025'),
                                                                                                               ('M2', 'CL-FCB', 'CL-RMA', 'Lluís Companys', '2025-05-08 18:00:00', 'NOT_STARTED', 'S2024-2025'),
                                                                                                               ('M3', 'CL-ATM', 'CL-FCB', 'Metropolitano', NULL, 'NOT_STARTED', 'S2024-2025'),
                                                                                                               ('M4', 'CL-ATM', 'CL-RMA', 'Metropolitano', NULL, 'NOT_STARTED', 'S2024-2025'),
                                                                                                               ('M5', 'CL-FCB', 'CL-ATM', 'Lluís Companys', NULL, 'NOT_STARTED', 'S2024-2025'),
                                                                                                               ('M6', 'CL-RMA', 'CL-ATM', 'Santiago Bernabeu', NULL, 'NOT_STARTED', 'S2024-2025');


String sql = """

                SELECT\s
               c.id,
               c.name,
               c.acronym,
               c.year_creation,
               c.stadium,
               co.name AS coach_name,
               co.nationality AS coach_nationality,
              \s
               -- Buts marqués par le club pendant la saison
               COALESCE(SUM(CASE\s
                   WHEN g.own_goal = false AND g.club_id = c.id THEN 1\s
                   ELSE 0\s
               END), 0) AS scored_goals,

               -- Buts encaissés (buts contre ce club)
               COALESCE(SUM(CASE\s
                   WHEN g.own_goal = false AND (g.club_id != c.id AND (m.club_playing_home = c.id OR m.club_playing_away = c.id)) THEN 1\s
                   ELSE 0\s
               END), 0) AS conceded_goals,

               -- Points : victoire = 3, nul = 1, défaite = 0
               COALESCE(SUM(CASE
                   WHEN m.club_playing_home = c.id AND home_goals > away_goals THEN 3
                   WHEN m.club_playing_away = c.id AND away_goals > home_goals THEN 3
                   WHEN home_goals = away_goals AND (m.club_playing_home = c.id OR m.club_playing_away = c.id) THEN 1
                   ELSE 0
               END), 0) AS ranking_points,

               -- Clean sheets
               COALESCE(SUM(CASE
                   WHEN m.club_playing_home = c.id AND away_goals = 0 THEN 1
                   WHEN m.club_playing_away = c.id AND home_goals = 0 THEN 1
                   ELSE 0
               END), 0) AS clean_sheet_number

           FROM club c
           LEFT JOIN coach co ON co.id = c.coach_id
           LEFT JOIN match m ON m.club_playing_home = c.id OR m.club_playing_away = c.id
           LEFT JOIN (
               SELECT\s
                   g.match_id,
                   g.club_id,
                   g.own_goal,
                   COUNT(*) FILTER (WHERE g.own_goal = false) AS goals
               FROM goal g
               GROUP BY g.match_id, g.club_id, g.own_goal
           ) g ON g.match_id = m.id

           -- Buts par match et club (pour calculs de score final du match)
           LEFT JOIN (
               SELECT\s
                   m.id AS match_id,
                   -- Buts marqués par club à domicile
                   COUNT(CASE WHEN g.own_goal = false AND g.club_id = m.club_playing_home THEN 1 END) AS home_goals,
                   -- Buts marqués par club à l’extérieur
                   COUNT(CASE WHEN g.own_goal = false AND g.club_id = m.club_playing_away THEN 1 END) AS away_goals
               FROM match m
               LEFT JOIN goal g ON g.match_id = m.id
               GROUP BY m.id
           ) match_goals ON match_goals.match_id = m.id

           JOIN season s ON m.season_id = s.id
           WHERE s.year = ?
           GROUP BY\s
               c.id, c.name, c.acronym, c.year_creation, c.stadium,
               co.name, co.nationality,
               home_goals, away_goals;

           """;