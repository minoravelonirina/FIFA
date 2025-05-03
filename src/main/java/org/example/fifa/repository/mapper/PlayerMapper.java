package org.example.fifa.repository.mapper;

import org.example.fifa.model.Club;
import org.example.fifa.model.Player;
import org.example.fifa.model.enums.Position;
import org.example.fifa.rest.dto.PlayerDto;
import org.example.fifa.rest.dto.PlayerWithClubDto;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlayerMapper {
    public Player mapRow(ResultSet rs) throws SQLException {
        Player player = new Player();
        player.setId(rs.getString("id"));
        player.setName(rs.getString("name"));
        player.setNumber(rs.getInt("number"));
        player.setAge(rs.getInt("age"));
        player.setNationality(rs.getString("nationality"));

//        player.setClubId(rs.getString("club_id"));

        // Map Position enum
        String positionStr = rs.getString("position");
        if (positionStr != null) {
            try {
                player.setPosition(Position.valueOf(positionStr));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        return player;
    }

    public Player mapPlayer(ResultSet rs) throws SQLException {
        Player player = new Player();
        player.setId(rs.getString("id"));
        player.setName(rs.getString("name"));
        player.setNumber(rs.getInt("number"));
        player.setAge(rs.getInt("age"));
        player.setNationality(rs.getString("nationality"));
        player.setPosition(Position.valueOf(rs.getString("position")));
        return player;
    }

    public PlayerWithClubDto mapToPlayerWithClubDto(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String name = rs.getString("name");
        int number = rs.getInt("number");
        int age = rs.getInt("age");
        String nationality = rs.getString("nationality");
        Position position = Position.valueOf(rs.getString("position"));

        Club club = new Club();
        club.setId(rs.getString("club_id"));
        club.setName(rs.getString("club_name"));
        club.setAcronym(rs.getString("club_acronym"));
        club.setYearCreation(rs.getInt("club_year_creation"));
        club.setStadium(rs.getString("club_stadium"));

        return new PlayerWithClubDto(id, club, position, nationality, age, name, number);
    }
}
