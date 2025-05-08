package org.example.fifa.repository.mapper;

import org.example.fifa.model.Coach;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

@Component
public class CoachMapper implements Function<ResultSet, Coach> {

    @Override
    public Coach apply(ResultSet resultSet) {
        try {
            return new Coach(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("nationality")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
