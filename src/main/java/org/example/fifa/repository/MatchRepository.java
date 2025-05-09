package org.example.fifa.repository;

import org.example.fifa.model.Match;
import org.example.fifa.model.RequestGoal;
import org.example.fifa.model.Season;
import org.example.fifa.model.enums.Status;
import org.example.fifa.rest.dto.ClubWithGoalsDto;
import org.example.fifa.rest.dto.MatchDto;
import org.example.fifa.rest.dto.ScorerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Component
public class MatchRepository implements CrudDAO<Match>{

    @Autowired private DataSource dataSource;
    @Autowired private ClubRepository clubRepository;
    @Autowired private SeasonRepository seasonRepository;

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
                Season season = seasonRepository.findByYear(seasonYear.getYear());
                statement.setString(7, season.getId());
                statement.setInt(8, seasonYear.getYear());
            }
            try (ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    matchDtos.add(mapMatch(resultSet));
                }
                return matchDtos;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


//    public List<MatchDto> findAll(LocalDate seasonYear, Status matchStatus, String clubPlayingName, LocalDate matchAfter, LocalDate matchBeforeOrEquals) throws SQLException, SQLException {
//        List<MatchDto> matches = new ArrayList<>();
//
//        String sql = """
//            SELECT m.*
//            FROM match m
//            JOIN season s ON m.season_id = s.id
//            JOIN club ch ON m.club_playing_home = ch.id
//            JOIN club ca ON m.club_playing_away = ca.id
//            WHERE s.year = ?
//              AND (? IS NULL OR m.status = ?)
//              AND (? IS NULL OR ch.name ILIKE ? OR ca.name ILIKE ?)
//              AND (? IS NULL OR m.match_datetime > ?)
//              AND (? IS NULL OR m.match_datetime <= ?)
//        """;
//
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setInt(1, seasonYear.getYear());
//
//            stmt.setObject(2, matchStatus != null ? matchStatus.name() : null);
//            stmt.setObject(3, matchStatus != null ? matchStatus.name() : null);
//
//            stmt.setString(4, clubPlayingName != null ? "%" + clubPlayingName + "%" : null);
//            stmt.setString(5, clubPlayingName != null ? "%" + clubPlayingName + "%" : null);
//            stmt.setString(6, clubPlayingName != null ? "%" + clubPlayingName + "%" : null);
//
//            stmt.setObject(7, matchAfter);
//            stmt.setObject(8, matchAfter);
//
//            stmt.setObject(9, matchBeforeOrEquals);
//            stmt.setObject(10, matchBeforeOrEquals);
//
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                matches.add(mapMatch(rs));
//            }
//            return matches;
//
//        } catch (SQLException e) {
//            throw new RuntimeException("Erreur lors de la récupération des matchs", e);
//        }
//    }



    public List<MatchDto> findAll(LocalDate seasonYear, Status matchStatus, String clubPlayingName, LocalDate matchAfter, LocalDate matchBeforeOrEquals) throws SQLException {
        List<MatchDto> matches = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT m.*
        FROM match m
        JOIN season s ON m.season_id = s.id
        JOIN club ch ON m.club_playing_home = ch.id
        JOIN club ca ON m.club_playing_away = ca.id
        WHERE s.year = ?
    """);

        if (matchStatus != null) {
            sql.append(" AND m.status = ?");
            params.add(matchStatus);
        }

        // Ajouter condition club name
        if (clubPlayingName != null && !clubPlayingName.isEmpty()) {
            sql.append(" AND (ch.name ILIKE ? OR ca.name ILIKE ?)");
            String likePattern = "%" + clubPlayingName + "%";
            params.add(likePattern);
            params.add(likePattern);
        }

        // Date après
        if (matchAfter != null) {
            sql.append(" AND m.match_datetime > ?");
            params.add(Date.valueOf(matchAfter));
        }

        // Date avant ou égale
        if (matchBeforeOrEquals != null) {
            sql.append(" AND m.match_datetime <= ?");
            params.add(Date.valueOf(matchBeforeOrEquals));
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            stmt.setInt(1, seasonYear.getYear());

            int index = 2;
            for (Object param : params) {

                if (param == null) {
                    stmt.setNull(index, Types.OTHER);
                }

                else if (param instanceof String) {
                    stmt.setString(index, (String) param);
                }

                else if (param instanceof LocalDate) {
                    stmt.setDate(index, Date.valueOf((LocalDate) param));
                }

                else if (param instanceof Status) {
                    stmt.setObject(index, ((Status) param).name(), Types.OTHER);
                }

                else {
                    stmt.setObject(index, param);
                }
                index++;
            }

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

        String insertSql = "INSERT INTO goal (match_id, club_id, player_id, minute_of_goal) " +
                "VALUES (?, ?, ?, ?) returning match_id, club_id, player_id, minute_of_goal";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSql)) {

            for (RequestGoal goal : requestList) {
                statement.setString(1, matchId);
                statement.setString(2, goal.getClubId());
                statement.setString(3, goal.getScorerIdentifier());
                statement.setInt(4, goal.getMinuteOfGoal());

                statement.addBatch(); // Accumule les insertions pour exécution en lot
            }
            statement.executeBatch(); // Exécute tous les inserts d'un coup pour optimiser

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    matchHaveGoals.add(mapMatchGoal(resultSet, matchId));
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
        LocalDateTime dateTime = rs.getTimestamp("match_datetime") != null
                ? rs.getTimestamp("match_datetime").toLocalDateTime()
                : null;
        match.setMatchDatetime(dateTime);
        match.setActualStatus(Status.valueOf(rs.getString("status")));
        match.setStadium(rs.getString("stadium"));
        String idClubHome = rs.getString("club_playing_home");
        String idClubAway = rs.getString("club_playing_away");
        ClubWithGoalsDto clubHome = idClubHome != null ? clubRepository.findByIdClub(idClubHome, match.getId()) : null;
        ClubWithGoalsDto clubAway = idClubAway != null ? clubRepository.findByIdClub(idClubAway, match.getId()) : null;
        match.setClubPlayingHome(clubHome);
        match.setClubPlayingAway(clubAway);

        return match;
    }

    private MatchDto mapMatchGoal(ResultSet rs, String matchId) throws SQLException {
        MatchDto match = new MatchDto();
        MatchDto match1 = getById(matchId);
        match.setId(matchId);
        match.setMatchDatetime(match1.getMatchDatetime());
        match.setActualStatus(match1.getActualStatus());
        match.setStadium(match1.getStadium());
        ClubWithGoalsDto club = clubRepository.findByIdClub(rs.getString("club_id"), match1.getId());
        if (match1.getClubPlayingAway().getId().equals(club.getId())){
            match.setClubPlayingAway(club);
            match.setClubPlayingHome(match1.getClubPlayingHome());
        }
        match.setClubPlayingHome(club);
        match.setClubPlayingAway(match1.getClubPlayingAway());
        return match;
    };


}
