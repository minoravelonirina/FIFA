package org.example.fifa.repository;

import org.example.fifa.model.Match;
import org.example.fifa.model.enums.Status;
import org.example.fifa.repository.mapper.MatchMapper;
import org.example.fifa.rest.dto.MatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Component
public class MatchRepository implements CrudDAO<Match>{

    @Autowired private DataSource dataSource;
    @Autowired private MatchMapper matchMapper;

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

    public List<Match> createAllMatches(LocalDate seasonYear, List<Match> matches){
        return null;
    }

//    public List<Match> findAll(LocalDate seasonYear, Status matchStatus, String clubPlayingName, LocalDate matchAfter, LocalDate matchBeforeOrEquals){
//        return null;
//    }

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

            stmt.setObject(1, seasonYear);

            stmt.setObject(2, matchStatus != null ? matchStatus.name() : null);
            stmt.setObject(3, matchStatus != null ? matchStatus.name() : null);

            stmt.setObject(4, clubPlayingName != null ? "%" + clubPlayingName + "%" : null);
            stmt.setObject(5, clubPlayingName != null ? "%" + clubPlayingName + "%" : null);
            stmt.setObject(6, clubPlayingName != null ? "%" + clubPlayingName + "%" : null);

            stmt.setObject(7, matchAfter);
            stmt.setObject(8, matchAfter);

            stmt.setObject(9, matchBeforeOrEquals);
            stmt.setObject(10, matchBeforeOrEquals);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                matches.add(mapMatch(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des matchs", e);
        }

        return matches;
    }

    private MatchDto mapMatch(ResultSet rs) throws SQLException {
        MatchDto match = new MatchDto();
        match.setId(rs.getString("id"));
        match.setMatchDatetime(rs.getTimestamp("match_datetime").toLocalDateTime());
        match.setActualStatus(Status.valueOf(rs.getString("status")));

        return match;
    }


    public Match changeStatusOfMatch(String id, Status status){
        return null;
    }

    public List<MatchDto> saveGoalsInMatch(String id ){
        return null;
    }
}
