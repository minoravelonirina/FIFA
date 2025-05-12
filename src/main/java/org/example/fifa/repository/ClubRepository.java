package org.example.fifa.repository;

import org.example.fifa.model.Club;
import org.example.fifa.repository.mapper.ClubMapper;
import org.example.fifa.rest.dto.ClubStatistics;
import org.example.fifa.rest.dto.ClubWithGoalsDto;
import org.example.fifa.model.Scorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Component
public class ClubRepository {

    @Autowired private DataSource dataSource;
    @Autowired private ClubMapper clubMapper;
    @Autowired private CoachRepository coachRepository;
    @Autowired
    @Lazy
    private PlayerRepository playerRepository;


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

    public List<Club> findAllTest() throws SQLException {
        List<Club> clubList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from club"
             )){
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    clubList.add(clubMapper.applyTest(resultSet));
                }
                return clubList;
            }
            catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la recuperation des clubs", e);
            }
        }
    }


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


    public ClubWithGoalsDto findByIdClub(String idClub, String matchId) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from club where id = ?"
             )){
            statement.setString(1, idClub);
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return clubWithGoalMapper(resultSet, matchId);
                }
                System.out.println("clubMapper = " + clubWithGoalMapper(resultSet, matchId));
                return null;
            }
            catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la recuperation des clubs", e);
            }
        }
    }


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



    public List<Club> update(List<Club> entity) throws SQLException {
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
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            clubs.add(clubMapper.apply(resultSet));
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return clubs;
    }



    public List<ClubStatistics> getAllStatistic(int seasonYear) throws SQLException {
        String sql = """
    WITH match_results AS (
        SELECT
            m.id AS match_id,
            m.club_playing_home,
            m.club_playing_away,
            m.season_id,
            COUNT(\s
                CASE\s
                    WHEN g.own_goal = false AND g.club_id = m.club_playing_home THEN 1
                    WHEN g.own_goal = true AND g.club_id = m.club_playing_away THEN 1
                    ELSE 0
                END
            ) AS home_goals,
            COUNT(
                CASE\s
                    WHEN g.own_goal = false AND g.club_id = m.club_playing_away THEN 1
                    WHEN g.own_goal = true AND g.club_id = m.club_playing_home THEN 1
                    ELSE 0
                END
            ) AS away_goals
        FROM match m
        LEFT JOIN goal g ON g.match_id = m.id
        JOIN season s ON s.id = m.season_id
        WHERE s.year = ?
        GROUP BY m.id, m.club_playing_home, m.club_playing_away, m.season_id
    ),

    club_match_points AS (
        SELECT
            mr.match_id,
            c.id AS club_id,
            CASE
                WHEN (mr.club_playing_home = c.id AND mr.home_goals > mr.away_goals)
                  OR (mr.club_playing_away = c.id AND mr.away_goals > mr.home_goals) THEN 3
                WHEN mr.home_goals = mr.away_goals THEN 1
                ELSE 0
            END AS points
        FROM match_results mr
        JOIN club c ON c.id IN (mr.club_playing_home, mr.club_playing_away)
    ),

    ranking_points AS (
        SELECT
            cmp.club_id,
            SUM(cmp.points) AS points
        FROM club_match_points cmp
        GROUP BY cmp.club_id
    ),

   goal_stats AS (
       SELECT
           c.id AS club_id,
           COUNT(CASE WHEN g.own_goal = false AND g.club_id = c.id THEN 1 END) AS scored,
           COUNT(CASE
               WHEN g.own_goal = false AND g.club_id != c.id
                    AND (m.club_playing_home = c.id OR m.club_playing_away = c.id) THEN 1
               WHEN g.own_goal = true AND g.club_id = c.id THEN 1
           END) AS conceded
       FROM club c
       LEFT JOIN match m ON m.club_playing_home = c.id OR m.club_playing_away = c.id
       LEFT JOIN goal g ON g.match_id = m.id
       JOIN season s ON s.id = m.season_id
       WHERE s.year = ?
       GROUP BY c.id
   )
   ,

    clean_sheets AS (
        SELECT
            c.id AS club_id,
            COUNT(*) AS clean_sheets
        FROM club c
        JOIN match_results mr ON c.id = mr.club_playing_home OR c.id = mr.club_playing_away
        WHERE (
            (mr.club_playing_home = c.id AND mr.away_goals = 0) OR
            (mr.club_playing_away = c.id AND mr.home_goals = 0)
        )
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
        COALESCE(g.scored, 0) AS scored_goals,
        COALESCE(g.conceded, 0) AS conceded_goals,
        COALESCE(g.scored, 0) - COALESCE(g.conceded, 0) AS difference_goals,
        COALESCE(rp.points, 0) AS ranking_points,
        COALESCE(cs.clean_sheets, 0) AS clean_sheet_number
    FROM club c
    LEFT JOIN coach co ON c.coach_id = co.id
    JOIN season s ON s.year = ?
    LEFT JOIN ranking_points rp ON rp.club_id = c.id
    LEFT JOIN goal_stats g ON g.club_id = c.id
    LEFT JOIN clean_sheets cs ON cs.club_id = c.id;
""";


        List<ClubStatistics> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seasonYear);
            stmt.setInt(2, seasonYear);
            stmt.setInt(3, seasonYear);

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
            stats.setScoredGoals(resultSet.getInt("scored_goals"));
            stats.setConcededGoals(resultSet.getInt("conceded_goals"));
            stats.setRankingPoints(resultSet.getInt("ranking_points"));
            stats.setDifferenceGoals(resultSet.getInt("difference_goals"));
            stats.setCleanSheetNumber(resultSet.getInt("clean_sheet_number"));
            return stats;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private ClubWithGoalsDto clubWithGoalMapper(ResultSet resultSet, String matchId) {
        try {

            ClubWithGoalsDto clubWithGoalsDto = new ClubWithGoalsDto();
            String idClub = resultSet.getString("id");
            clubWithGoalsDto.setId(idClub);
            clubWithGoalsDto.setName(resultSet.getString("name"));
            clubWithGoalsDto.setAcronym(resultSet.getString("acronym"));
            List<Scorer> scorerList = playerRepository.getScorerOfMatch(idClub, matchId);
            clubWithGoalsDto.setScorers(scorerList);

            return clubWithGoalsDto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
