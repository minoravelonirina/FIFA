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
create type transfer_type as enum ('IN', 'OUT');
create type championship as enum ('PREMIER_LEAGUE', 'LA_LIGA', 'BUNDESLIGA', 'SERIA', 'LIGUE_1');


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
                      championship championship
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

CREATE TABLE transfer (
                          id UUID PRIMARY KEY,
                          player_id VARCHAR NOT NULL REFERENCES player(id),
                          club_id VARCHAR NOT NULL REFERENCES club(id),
                          type VARCHAR(3) NOT NULL CHECK (type IN ('IN', 'OUT')),
                          transfer_date TIMESTAMP NOT NULL
);

--coach
INSERT INTO coach (id, name, nationality) VALUES
                                              ('c1', 'Entraineur 1', 'Français'),
                                              ('c2', 'Entraineur 2', 'Italien'),
                                              ('c3', 'Entraineur 3', 'Allemand');

-- Clubs
INSERT INTO club (id, name, acronym, year_creation, stadium, coach_id, championship) VALUES
                                                                           ('cl1', 'Club 1', 'C1', 1902, 'Stade 1', 'c1', 'PREMIER_LEAGUE'),
                                                                           ('cl2', 'Club 2', 'C2', 1905, 'Stade 2', 'c2', 'PREMIER_LEAGUE'),
                                                                           ('cl3', 'Club 3', 'C3', 1910, 'Stade 3', 'c3', 'PREMIER_LEAGUE');

-- Players
INSERT INTO player (id, name, number, age, position, nationality, club_id) VALUES

                                                                               -- Club 1
                                                                               ('g1', 'Gardien 1', 1, 30, 'GOAL_KEEPER', 'Espagnol', 'cl1'),
                                                                               ('d1', 'Defense 1', 2, 25, 'DEFENSE', 'Espagnol', 'cl1'),
                                                                               ('m1', 'Milieu 1', 5, 24, 'MIDFIELDER', 'Espagnol', 'cl1'),
                                                                               ('a1', 'Attaquant 1', 7, 17, 'STRIKER', 'Espagnol', 'cl1'),

                                                                               -- Club 2
                                                                               ('g2', 'Gardien 2', 1, 21, 'GOAL_KEEPER', 'Espagnol', 'cl2'),
                                                                               ('d2', 'Defense 2', 2, 34, 'DEFENSE', 'Belge', 'cl2'),
                                                                               ('m2', 'Milieu 2', 5, 29, 'MIDFIELDER', 'Français', 'cl2'),
                                                                               ('a2', 'Attaquant 2', 7, 18, 'STRIKER', 'Allemand', 'cl2'),

                                                                               -- Club 3
                                                                               ('g3', 'Gardien 3', 1, 28, 'GOAL_KEEPER', 'Bresilien', 'cl3'),
                                                                               ('d3', 'Defense 3', 2, 21, 'DEFENSE', 'Bresilien', 'cl3'),
                                                                               ('m3', 'Milieu 3', 5, 29, 'MIDFIELDER', 'Français', 'cl3'),
                                                                               ('a3', 'Attaquant 3', 7, 23, 'STRIKER', 'Allemand', 'cl3');

--saison
INSERT INTO season (id, year, alias, status) VALUES
    ('s2024', 2024, '2023-2024', 'NOT_STARTED');

-- matchs
INSERT INTO "match" (id, club_playing_home, club_playing_away, stadium, status, season_id) VALUES
                                                                                               ('m1', 'cl1', 'cl2', 'Stade 1', 'NOT_STARTED', 's2024'),
                                                                                               ('m2', 'cl2', 'cl3', 'Stade 2', 'NOT_STARTED', 's2024'),
                                                                                               ('m3', 'cl1', 'cl3', 'Stade 1', 'NOT_STARTED', 's2024'),
                                                                                               ('m4', 'cl3', 'cl2', 'Stade 3', 'NOT_STARTED', 's2024'),
                                                                                               ('m5', 'cl2', 'cl1', 'Stade 2', 'NOT_STARTED', 's2024'),
                                                                                               ('m6', 'cl3', 'cl1', 'Stade 3', 'NOT_STARTED', 's2024');


insert into goal (id, match_id, club_id, player_id, minute_of_goal, own_goal)
values
    ('g1', 'm1', 'cl1', 'a1', 2, false),
    ('g2', 'm1', 'cl1', 'a1', 8, false),
    ('g3', 'm1', 'cl1', 'm1', 50, false),
    ('g4', 'm1', 'cl1', 'd1', 60, false),
    ('g7', 'm3', 'cl1', 'a1', 69, false),
    ('g12', 'm6', 'cl1', 'a1', 56, false),
    ('g13', 'm6', 'cl1', 'm1', 90, false),


    ('g5', 'm1', 'cl2', 'g1', 1, true),
    ('g8', 'm5', 'cl2', 'a2', 88, false),

    ('g6', 'm2', 'cl3', 'a3', 21, false),
    ('g9', 'm6', 'cl3', 'g1', 88, true),
    ('g10', 'm6', 'cl3', 'g1', 89, true),
    ('g11', 'm6', 'cl3', 'g1', 90, true),

    ('g14', 'm2', 'cl2', null, 0, false),
    ('g15', 'm3', 'cl3', null, 0, false),
    ('g16', 'm4', 'cl3', null, 0, false),
    ('g17', 'm4', 'cl2', null, 0, false),
    ('g18', 'm5', 'cl1', null, 0, false);




INSERT INTO goal (id, match_id, club_id, player_id, minute_of_goal, own_goal)
VALUES
    ('g1', 'm1', 'cl1', 'a1', 120, false),
    ('g2', 'm1', 'cl1', 'a1', 480, false),
    ('g3', 'm1', 'cl1', 'm1', 3000, false),
    ('g4', 'm1', 'cl1', 'd1', 3600, false),
    ('g5', 'm1', 'cl2', 'g1', 60, true),
    ('g6', 'm2', 'cl3', 'a3', 1260, false),
    ('g7', 'm3', 'cl1', 'a1', 4140, false),
    ('g8', 'm5', 'cl2', 'a2', 5280, false),
    ('g9', 'm6', 'cl3', 'g1', 5280, true),
    ('g10', 'm6', 'cl3', 'g1', 5340, true),
    ('g11', 'm6', 'cl3', 'g1', 5400, true),
    ('g12', 'm6', 'cl1', 'a1', 3360, false),
    ('g13', 'm6', 'cl1', 'm1', 5400, false);




WITH match_scores AS (
    SELECT
        m.id AS match_id,
        m.club_playing_home,
        m.club_playing_away,
        COUNT(CASE WHEN g.own_goal = false AND g.club_id = m.club_playing_home THEN 1 END) AS home_goals,
        COUNT(CASE WHEN g.own_goal = false AND g.club_id = m.club_playing_away THEN 1 END) AS away_goals
    FROM match m
             LEFT JOIN goal g ON g.match_id = m.id
    GROUP BY m.id, m.club_playing_home, m.club_playing_away
),
     club_stats AS (
         SELECT
             c.id AS club_id,
             COUNT(CASE
                       WHEN (m.club_playing_home = c.id AND ms.home_goals > ms.away_goals)
                           OR (m.club_playing_away = c.id AND ms.away_goals > ms.home_goals) THEN 3
                       WHEN (m.club_playing_home = c.id OR m.club_playing_away = c.id) AND ms.home_goals = ms.away_goals THEN 1
                       ELSE 0
                 END) AS ranking_points,
             COUNT(CASE WHEN g.own_goal = false AND g.club_id = c.id THEN 1 END) AS scored_goals,
             COUNT(CASE
                       WHEN g.own_goal = false AND g.club_id != c.id AND (m.club_playing_home = c.id OR m.club_playing_away = c.id)
                           THEN 1
                       WHEN g.own_goal = true AND g.club_id = c.id THEN 1
                 END) AS conceded_goals,
             COUNT(CASE
                       WHEN m.club_playing_home = c.id AND ms.away_goals = 0 THEN 1
                       WHEN m.club_playing_away = c.id AND ms.home_goals = 0 THEN 1
                 END) AS clean_sheet_number
         FROM club c
                  LEFT JOIN match m ON m.club_playing_home = c.id OR m.club_playing_away = c.id
                  LEFT JOIN match_scores ms ON ms.match_id = m.id
                  LEFT JOIN goal g ON g.match_id = m.id
                  JOIN season s ON s.id = m.season_id
         WHERE s.year = 2024
         GROUP BY c.id
     )
SELECT
    c.id,
    c.name,
    c.acronym,
    c.year_creation,
    c.stadium,
    co.id AS coach_id,
    co.name AS coach_name,
    co.nationality AS coach_nationality,
    s.year,
    cs.ranking_points,
    cs.scored_goals,
    cs.conceded_goals,
    cs.scored_goals - cs.conceded_goals AS difference_goals,
    cs.clean_sheet_number
FROM club c
         JOIN club_stats cs ON cs.club_id = c.id
         LEFT JOIN coach co ON co.id = c.coach_id
         JOIN match m ON m.club_playing_home = c.id OR m.club_playing_away = c.id
         JOIN season s ON s.id = m.season_id
WHERE s.year = 2024
GROUP BY c.id, c.name, c.acronym, c.year_creation, c.stadium, co.id, co.name, co.nationality, s.year, cs.ranking_points, cs.scored_goals, cs.conceded_goals, cs.clean_sheet_number;









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


 sql = """

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

SELECT COUNT(*) AS scored_goals, g.minute_of_goal
                             FROM goal g
                             JOIN "match" m ON g.match_id = m.id
                                WHERE g.player_id = 'a1'
                             AND g.own_goal = false
                             AND m.season_id = 's2024'
                             group by g.minute_of_goal;




INSERT INTO season (id, year, alias, status) VALUES ('s2024', 2024, '2023-2024', 'NOT_STARTED');

-- coach
INSERT INTO coach (id, name, nationality) VALUES
                                              ('c4', 'Entraineur 4', 'Malgache'),
                                              ('c5', 'Entraineur 5', 'Ivorien'),
                                              ('c6', 'Entraineur 6', 'Espagnol');

--clubs
INSERT INTO club (id, name, acronym, year_creation, stadium, coach_id) VALUES
                                                                           ('cl4', 'Club 4', 'C4', 1902, 'Stade 4', 'c4'),
                                                                           ('cl5', 'Club 5', 'C4', 1905, 'Stade 5', 'c5'),
                                                                           ('cl6', 'Club 6', 'C4', 1910, 'Stade 6', 'c6');

--players
INSERT INTO player (id, name, number, age, position, nationality, club_id) VALUES
                                                                               -- Club 4
                                                                               ('g4', 'Gardien 4', 1, 30, 'GOAL_KEEPER', 'Bresilien', 'cl4'),
                                                                               ('d4', 'Defense 4', 2, 25, 'DEFENSE', 'Bresilien', 'cl4'),
                                                                               ('m4', 'Milieu 4', 5, 24, 'MIDFIELDER', 'Français', 'cl4'),
                                                                               ('a4', 'Attaquant 4', 7, 17, 'STRIKER', 'Allemand', 'cl4'),

                                                                               -- Club 5
                                                                               ('g5', 'Gardien 5', 1, 21, 'GOAL_KEEPER', 'Français', 'cl5'),
                                                                               ('d5', 'Defense 5', 2, 34, 'DEFENSE', 'Belge', 'cl5'),
                                                                               ('m5', 'Milieu 5', 5, 29, 'MIDFIELDER', 'Français', 'cl5'),
                                                                               ('a5', 'Attaquant 5', 7, 18, 'STRIKER', 'Allemand', 'cl5'),

                                                                               -- Club 6
                                                                               ('g6', 'Gardien 6', 1, 28, 'GOAL_KEEPER', 'Espagnol', 'cl6'),
                                                                               ('d6', 'Defense 6', 2, 21, 'DEFENSE', 'Bresilien', 'cl6'),
                                                                               ('m6', 'Milieu 6', 5, 29, 'MIDFIELDER', 'Italien', 'cl6'),
                                                                               ('a6', 'Attaquant 6', 7, 23, 'STRIKER', 'Allemand', 'cl6');

-- Insertion de la saison

-- Insertion des matchs pour le deuxième championnat
INSERT INTO "match" (id, club_playing_home, club_playing_away, stadium, status, season_id) VALUES
                                                                                               ('m7', 'cl4', 'cl5', 'Stade 4', 'NOT_STARTED', 's2024'),
                                                                                               ('m8', 'cl5', 'cl6', 'Stade 5', 'NOT_STARTED', 's2024'),
                                                                                               ('m9', 'cl4', 'cl6', 'Stade 4', 'NOT_STARTED', 's2024'),
                                                                                               ('m10', 'cl6', 'cl5', 'Stade 6', 'NOT_STARTED', 's2024'),
                                                                                               ('m11', 'cl5', 'cl4', 'Stade 5', 'NOT_STARTED', 's2024'),
                                                                                               ('m12', 'cl6', 'cl4', 'Stade 6', 'NOT_STARTED', 's2024');

-- Insertion des buts prévus (basés sur les informations du tableau des matchs)
INSERT INTO goal (id, match_id, club_id, player_id, minute_of_goal, own_goal) VALUES
-- Match 7 (Club 4 vs Club 5)
('g14', 'm7', 'cl4', 'a4', 2, false),  -- Attaquant 4 à 2'
('g15', 'm7', 'cl4', 'a4', 8, false),  -- Attaquant 4 à 8'
('g16', 'm7', 'cl4', 'm4', 50, false), -- Milieu 4 à 50'
('g17', 'm7', 'cl4', 'd4', 60, false), -- Défense 4 à 60'
('g18', 'm7', 'cl5', 'g4', 1, true),   -- Gardien 5 à 1' (own goal)

-- Match 8 (Club 5 vs Club 6)
('g19', 'm8', 'cl5', 'a5', 88, false), -- Attaquant 5 à 88'
('g20', 'm8', 'cl6', 'a6', 21, false), -- Attaquant 6 à 21'

-- Match 9 (Club 4 vs Club 6)
('g21', 'm9', 'cl4', 'a4', 69, false), -- Attaquant 4 à 69'

-- Match 11 (Club 5 vs Club 4)
('g22', 'm11', 'cl5', 'a5', 88, false), -- Attaquant 5 à 88'

-- Match 12 (Club 6 vs Club 4)
('g23', 'm12', 'cl6', 'g4', 88, true),  -- Gardien 6 à 88' (own goal)
('g24', 'm12', 'cl6', 'g4', 89, true),  -- Gardien 6 à 89' (own goal)
('g25', 'm12', 'cl6', 'g4', 90, true),  -- Gardien 6 à 90' (own goal)
('g26', 'm12', 'cl4', 'a4', 56, false), -- Attaquant 4 à 56'
('g27', 'm12', 'cl4', 'm4', 90, false); -- Milieu 4 à 90'