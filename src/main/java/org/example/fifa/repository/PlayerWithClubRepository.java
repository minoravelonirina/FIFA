package org.example.fifa.repository;

import org.example.fifa.model.Club;
import org.example.fifa.model.enums.Position;
import org.example.fifa.rest.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Component
public class PlayerWithClubRepository {

    @Autowired private ClubRepository clubRepository;
    @Autowired private DataSource dataSource;

    public PlayerDto findByIdWithClub(String id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM player WHERE id = ?")) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapDto(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du joueur", e);
        }
    }

    public PlayerDto mapDto(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String clubId = resultSet.getString("club_id");
        Club club = clubId != null ? clubRepository.findById(clubId) : null;
        Position position = Position.valueOf(resultSet.getString("position"));
        String nationality = resultSet.getString("nationality");
        int age = resultSet.getInt("age");
        String name = resultSet.getString("name");
        int number = resultSet.getInt("number");
        return new PlayerDto(id, club, position, nationality, age, name, number);
    }
}
