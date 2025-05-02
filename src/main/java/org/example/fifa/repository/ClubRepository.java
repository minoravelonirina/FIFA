package org.example.fifa.repository;

import org.example.fifa.model.Club;
import org.example.fifa.repository.mapper.ClubMapper;
import org.example.fifa.rest.dto.ClubStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Component
public class ClubRepository implements CrudDAO<Club>{

    @Autowired private DataSource dataSource;
    @Autowired private ClubMapper clubMapper;
    @Autowired private CoachRepository coachRepository;

    @Override
    public List<Club> findAll() throws SQLException {
        List<Club> clubList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from club"
             )){
                 try (ResultSet resultSet = statement.executeQuery()){
                     while (resultSet.next()){
                         clubList.add(clubMapper.apply(resultSet));
                     }
                     return clubList;
                 }
                 catch (SQLException e) {
                     throw new RuntimeException("Erreur lors de la recuperation des clubs", e);
                 }
        }
    }


    @Override
    public Club findById(String id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from club where id = ?"
             )){
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return clubMapper.apply(resultSet);
                }
                return null;
            }
            catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la recuperation des clubs", e);
            }
        }
    }

    @Override
    public Club findByName(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from club where name = ?"
             )){
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return clubMapper.apply(resultSet);
                }
                return null;
            }
            catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la recuperation des clubs", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Club> save(List<Club> entity) {
        return null;
    }

    @Override
    public List<Club> update(List<Club> entity) {
        List<Club> clubs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "insert into club (id, name, acronym, year_creation, stadium, coach_id) values (?, ?, ?, ?, ?, ?) " +
                        "on conflict (id) do update set " +
                        "name=excluded.name, " +
                        "acronym=excluded.acronym, " +
                        "year_creation=excluded.year_creation, " +
                        "stadium=excluded.stadium, " +
                        "coach_id=excluded.coach_id " +
                        "returning id, name, acronym, year_creation, stadium, coach_id "
        )) {
            entity.forEach(club -> {
                try {
                    statement.setString(1, club.getId());
                    statement.setString(2, club.getName());
                    statement.setString(3, club.getAcronym());
                    statement.setInt(4, club.getYearCreation());
                    statement.setString(5, club.getStadium());
                    statement.setString(6, club.getCoach().getId());
                    coachRepository.save(club.getCoach());
                    statement.addBatch();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    clubs.add(clubMapper.apply(resultSet));
                }
                return clubs;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Club entity) {

    }

    public List<ClubStatistics> getAllStatistic(int seasonYear) throws SQLException {
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

        List<ClubStatistics> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seasonYear);

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    result.add(clubStatisticsMapper(resultSet));
                }
                return result;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching club statistics", e);
        }
    }

    private ClubStatistics clubStatisticsMapper(ResultSet resultSet) throws SQLException {
        ClubStatistics stats = new ClubStatistics();
        try {
            stats.setId(resultSet.getString("id"));
            String clubName = resultSet.getString("name");
            Club club = findByName(clubName);
            stats.setName(club.getName());
            stats.setCoach(club.getCoach());
            stats.setAcronym(club.getAcronym());
            stats.setYearCreation(club.getYearCreation());
            stats.setStadium(club.getStadium());
            int scoredGoals = resultSet.getInt("scored_goals");
            int concededGoals = resultSet.getInt("conceded_goals");
            int differenceGoals = scoredGoals - concededGoals;
            stats.setRankingPoints(resultSet.getInt("ranking_points"));
            stats.setScoredGoals(scoredGoals);
            stats.setConcededGoals(concededGoals);
            stats.setDifferenceGoals(differenceGoals);
            stats.setCleanSheetNumber(resultSet.getInt("clean_sheet_number"));
            return stats;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
