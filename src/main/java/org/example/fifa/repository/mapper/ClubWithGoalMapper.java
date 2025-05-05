package org.example.fifa.repository.mapper;

import org.example.fifa.repository.ClubRepository;
import org.example.fifa.repository.PlayerRepository;
import org.example.fifa.repository.ScorerRepository;
import org.example.fifa.rest.dto.ClubWithGoalsDto;
import org.example.fifa.rest.dto.ScorerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

@Component
public class ClubWithGoalMapper implements Function<ResultSet, ClubWithGoalsDto> {

    @Autowired private ScorerRepository scorerRepository;

    @Override
    public ClubWithGoalsDto apply(ResultSet resultSet) {
        try {
            ClubWithGoalsDto clubWithGoalsDto = new ClubWithGoalsDto();
            String idClub = resultSet.getString("id");
            clubWithGoalsDto.setId(idClub);
            clubWithGoalsDto.setName(resultSet.getString("name"));
            clubWithGoalsDto.setAcronym(resultSet.getString("acronym"));
            List<ScorerDto> scorerList = scorerRepository.getScorer(idClub);
            clubWithGoalsDto.setScorers(scorerList);

            return clubWithGoalsDto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

