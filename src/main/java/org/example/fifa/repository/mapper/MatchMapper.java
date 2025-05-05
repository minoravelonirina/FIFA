package org.example.fifa.repository.mapper;

import org.example.fifa.model.Club;
import org.example.fifa.model.Match;
import org.example.fifa.model.Season;
import org.example.fifa.model.enums.Status;
import org.example.fifa.repository.ClubRepository;
import org.example.fifa.rest.dto.ClubStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

@Component
public class MatchMapper implements Function<ResultSet, Match> {

    @Autowired private ClubRepository clubRepository;

    @Override
    public Match apply(ResultSet resultSet) {
        Match match = new Match();
        try {
            match.setId(resultSet.getString("id"));
            match.setMatchDatetime(resultSet.getTimestamp("match_datetime").toLocalDateTime());
            match.setActualStatus(Status.valueOf(resultSet.getString("status")));
            match.setStadium(resultSet.getString("stadium"));
//            String idClubHome = resultSet.getString("club_playing_home");
//            String idClubAway = resultSet.getString("club_playing_away");
//            Club clubHome = idClubHome != null ? clubRepository.findById(idClubHome) : null;
//            Club clubAway = idClubAway != null ? clubRepository.findById(idClubAway) : null;
//            match.setClubPlayingHome(new ClubStatistics(clubHome));
//            match.setClubPlayingAway(new ClubStatistics(clubAway));
            return match;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
