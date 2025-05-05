package org.example.fifa.repository;

import org.example.fifa.model.Match;
import org.example.fifa.model.RequestGoal;
import org.example.fifa.model.enums.Status;
import org.example.fifa.rest.dto.ClubWithGoalsDto;
import org.example.fifa.rest.dto.MatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Component
public class MatchRepository implements CrudDAO<Match>{

    @Autowired private DataSource dataSource;
    @Autowired private ClubRepository clubRepository;

    @Override
    public List<Match> findAll() {
        return List.of();
    }

    @Override
    public Match findById(String id) {
        return null;
    }

    @Override
    public Match findByName(String name) {
        return null;
    }

    @Override
    public List<Match> save(List<Match> entity) {
        return null;
    }

    @Override
    public List<Match> update(List<Match> entity) {
        return null;
    }

    @Override
    public void delete(Match entity) {
    }

    public List<MatchDto> createAllMatches(LocalDate seasonYear, List<Match> matches) {
        List<MatchDto> matchDtos = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
//            "insert into \"match\" (id, match_datetime, status, stadium, club_playing_home, club_playing_away, season_id) " +
//                    "values (?, ?, ?, ?, ?, ?, ?) returning id, match_datetime, status, stadium, club_playing_home, club_playing_away"
                     "INSERT INTO \"match\" (" +
                             "id, match_datetime, status, stadium, club_playing_home, club_playing_away, season_id) " +
                             "SELECT ?, ?, ?::status, ?, ?, ?, ? " +
                             "WHERE EXISTS (SELECT 1 FROM season WHERE year = ?) " +
                             "RETURNING id, match_datetime, status, stadium, club_playing_home, club_playing_away, season_id;"

             )) {

            for (Match match : matches) {
                statement.setString(1, match.getId());
                statement.setTimestamp(2, Timestamp.valueOf(match.getMatchDatetime()));
                statement.setString(3, match.getActualStatus().name());
                statement.setString(4, match.getStadium());
                statement.setString(5, match.getClubPlayingHome().getId());
                statement.setString(6, match.getClubPlayingAway().getId());
                statement.setInt(7, seasonYear.getYear());
                statement.setInt(8, seasonYear.getYear());
                statement.addBatch();
            }
            return  null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<MatchDto> findAll(LocalDate seasonYear, Status matchStatus, String clubPlayingName, LocalDate matchAfter, LocalDate matchBeforeOrEquals) throws SQLException, SQLException {
        List<MatchDto> matches = new ArrayList<>();

        String sql = """
            SELECT m.*
            FROM match m
            JOIN season s ON m.season_id = s.id
            JOIN club ch ON m.club_playing_home = ch.id
            JOIN club ca ON m.club_playing_away = ca.id
            WHERE s.year = ?
              AND (? IS NULL OR m.status = ?)
              AND (? IS NULL OR ch.name ILIKE ? OR ca.name ILIKE ?)
              AND (? IS NULL OR m.match_datetime > ?)
              AND (? IS NULL OR m.match_datetime <= ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seasonYear.getYear());

            stmt.setObject(2, matchStatus != null ? matchStatus.name() : null);
            stmt.setObject(3, matchStatus != null ? matchStatus.name() : null);

            stmt.setString(4, clubPlayingName != null ? "%" + clubPlayingName + "%" : null);
            stmt.setString(5, clubPlayingName != null ? "%" + clubPlayingName + "%" : null);
            stmt.setString(6, clubPlayingName != null ? "%" + clubPlayingName + "%" : null);

            stmt.setObject(7, matchAfter);
            stmt.setObject(8, matchAfter);

            stmt.setObject(9, matchBeforeOrEquals);
            stmt.setObject(10, matchBeforeOrEquals);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                matches.add(mapMatch(rs));
            }
            return matches;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des matchs", e);
        }
    }


    public List<MatchDto> changeStatusOfMatch(String id, Status status){
        List<MatchDto> listOfMatch = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "update \"match\" set status = ?::status where id = ? returning id, match_datetime, status, stadium, club_playing_home, club_playing_away"
             )) {
            statement.setString(1, status.name());
            statement.setString(2, id);
            statement.addBatch();

            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()){
                    listOfMatch.add(mapMatch(resultSet));
                }
                return listOfMatch;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public MatchDto getById(String idMatch) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select id, match_datetime, status, stadium, club_playing_home, club_playing_away from \"match\" where id = ?"
             )) {
            statement.setString(1, idMatch);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapMatch(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Il y a eu un probleme lors de la recuperation du match", e);
        }
    }


    public List<MatchDto> saveGoalsInMatch(String matchId, List<RequestGoal> requestList) {
        List<MatchDto> matchHaveGoals = new ArrayList<>();

        String insertSql = "INSERT INTO goal (id, match_id, club_id, player_id, minute_of_goal) " +
                "VALUES (?, ?, ?, ?, ?) returning id, match_id, club_id, player_id, minute_of_goal";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSql)) {

            for (RequestGoal goal : requestList) {
                String goalId = UUID.randomUUID().toString();
                statement.setString(1, goalId);
                statement.setString(2, matchId);
                statement.setString(3, goal.getClubId());
                statement.setString(4, goal.getScorerIdentifier());
                statement.setInt(5, goal.getMinuteOfGoal());

                statement.addBatch(); // Accumule les insertions pour exécution en lot
            }
            statement.executeBatch(); // Exécute tous les inserts d'un coup pour optimiser

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                while (resultSet.next()) {
                    matchHaveGoals.add(mapMatch(resultSet));
                }
                return matchHaveGoals;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement des buts", e);
        }
    }

    private MatchDto mapMatch(ResultSet rs) throws SQLException {
        MatchDto match = new MatchDto();
        match.setId(rs.getString("id"));
        match.setMatchDatetime(rs.getTimestamp("match_datetime").toLocalDateTime());
        match.setActualStatus(Status.valueOf(rs.getString("status")));
        match.setStadium(rs.getString("stadium"));
        String idClubHome = rs.getString("club_playing_home");
        String idClubAway = rs.getString("club_playing_away");
        ClubWithGoalsDto clubHome = idClubHome != null ? clubRepository.findByIdClub(idClubHome) : null;
        ClubWithGoalsDto clubAway = idClubAway != null ? clubRepository.findByIdClub(idClubAway) : null;
        match.setClubPlayingHome(clubHome);
        match.setClubPlayingAway(clubAway);

        return match;
    }
}
