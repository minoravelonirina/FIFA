package org.example.fifa.repository.mapper;

import org.example.fifa.model.Club;
import org.example.fifa.model.enums.Position;
import org.example.fifa.repository.ClubRepository;
import org.example.fifa.rest.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

@Component
public class PlayerWithClubMapper implements Function<ResultSet, PlayerDto> {

    @Autowired private ClubRepository clubRepository;

    @Override
    public PlayerDto apply(ResultSet resultSet) {
        try {
            String id = resultSet.getString("id");
            String clubId = resultSet.getString("club_id");
            Club club = clubId != null ? clubRepository.findById(clubId) : null;
            Position position = Position.valueOf(resultSet.getString("position"));
            String nationality = resultSet.getString("nationality");
            int age = resultSet.getInt("age");
            String name = resultSet.getString("name");
            int number = 0;
            number = resultSet.getInt("number");

            return new PlayerDto(id, club, position, nationality, age, name, number);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
