package org.example.fifa.repository.mapper;

import org.example.fifa.model.Club;
import org.example.fifa.model.Coach;
import org.example.fifa.model.enums.Championship;
import org.example.fifa.repository.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Function;

@Component
public class ClubMapper implements Function<ResultSet, Club> {

    @Autowired private CoachRepository coachRepository;

    @Override
    public Club apply(ResultSet resultSet) {
        Club club = new Club();

        try {
            String id = resultSet.getString("id");
            club.setId(id);
            club.setName(resultSet.getString("name"));
            club.setAcronym(resultSet.getString("acronym"));
            club.setYearCreation(resultSet.getInt("year_creation"));
            club.setStadium(resultSet.getString("stadium"));
            Coach coach = coachRepository.getCoach(id);
            club.setPlayers(new ArrayList<>());
            club.setCoach(coach);
            return club;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Club applyTest(ResultSet resultSet) {
        Club club = new Club();

        try {
            String id = resultSet.getString("id");
            club.setId(id);
            club.setName(resultSet.getString("name"));
            club.setAcronym(resultSet.getString("acronym"));
            club.setYearCreation(resultSet.getInt("year_creation"));
            club.setStadium(resultSet.getString("stadium"));
            Coach coach = coachRepository.getCoach(id);
            club.setChampionship(Championship.valueOf(resultSet.getString("championship")));
            club.setPlayers(new ArrayList<>());
            club.setCoach(coach);
            return club;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
