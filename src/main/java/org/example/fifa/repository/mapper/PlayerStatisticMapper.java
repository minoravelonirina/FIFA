package org.example.fifa.repository.mapper;


import org.example.fifa.model.PlayingTime;
import org.example.fifa.model.enums.DurationUnit;
import org.example.fifa.rest.dto.PlayerStatisticDto;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

@Component
public class PlayerStatisticMapper implements Function<ResultSet, PlayerStatisticDto> {

    @Override
    public PlayerStatisticDto apply(ResultSet resultSet) {
        try {
            PlayerStatisticDto playerStatisticDto = new PlayerStatisticDto();
            playerStatisticDto.setScoredGoals(resultSet.getInt("scored_goals"));
            PlayingTime time = new PlayingTime();
            time.setValue(32400);
            time.setDurationUnit(DurationUnit.SECOND);
            playerStatisticDto.setPlayingTime(time);
            return playerStatisticDto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
