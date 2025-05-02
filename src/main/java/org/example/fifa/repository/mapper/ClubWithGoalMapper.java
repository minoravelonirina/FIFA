package org.example.fifa.repository.mapper;

import org.example.fifa.rest.dto.ClubWithGoalsDto;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

@Component
public class ClubWithGoalMapper implements Function<ResultSet, ClubWithGoalsDto> {

    @Override
    public ClubWithGoalsDto apply(ResultSet resultSet) {
//        try {
//            return new ClubWithGoalsDto(
//                    resultSet.getString("id"),
//                    resultSet.getString("name"),
//                    resultSet.getString("acronym"),
//                    resultSet.getInt("score"),
//
////                    resultSet.getInt("scorers")
//            );
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return null;
    }
}
