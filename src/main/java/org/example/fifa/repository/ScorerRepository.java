package org.example.fifa.repository;

import org.example.fifa.model.Player;
import org.example.fifa.rest.dto.PlayerScorerDto;
import org.example.fifa.rest.dto.ScorerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Component
public class ScorerRepository {

    @Autowired private DataSource dataSource;
    @Autowired private PlayerRepository playerRepository;

    public List<ScorerDto> getScorer (String clubId) throws SQLException {
        List<ScorerDto> list = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from goal where match_id = ?"
             )){
            statement.setString(1, clubId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(scorerDtoMapper(resultSet));
                }
                return list;
            }
        }
    }

    private ScorerDto scorerDtoMapper(ResultSet resultSet) throws SQLException {
        ScorerDto scorerDto = new ScorerDto();
        Player player = playerRepository.findById(resultSet.getString("player_id"));
        PlayerScorerDto playerScorerDto = new PlayerScorerDto(player.getId(), player.getName(), player.getNumber());
        scorerDto.setPlayer(playerScorerDto);
        scorerDto.setMinuteOfGoal(resultSet.getInt("minute_of_goal"));
        scorerDto.setOwnGoal(resultSet.getBoolean("own_goal"));
        return scorerDto;
    }

}
