package org.example.fifa.repository;


import org.example.fifa.model.Player;
import org.example.fifa.model.PlayerStatistics;
import org.example.fifa.model.enums.DurationUnit;
import org.example.fifa.model.exception.NotFoundException;
import org.example.fifa.repository.mapper.PlayerMapper;
import org.example.fifa.rest.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PlayerRepository {
    private final DataSource dataSource;
    private final PlayerMapper playerMapper;
    private final PlayerWithClubRepository playerWithClubRepository;

    @Autowired
    public PlayerRepository(DataSource dataSource, PlayerMapper playerMapper, PlayerWithClubRepository playerWithClubRepository) {
        this.dataSource = dataSource;
        this.playerMapper = playerMapper;
        this.playerWithClubRepository = playerWithClubRepository;
    }

//    public String findPlayers(String name, Integer ageMinimum, Integer ageMaximum, String clubName) {
//        StringBuilder json = new StringBuilder("[");
//        StringBuilder sql = new StringBuilder(
//                "SELECT p.id, p.name, p.number, p.position, p.nationality, p.age, " +
//                        "c.id AS club_id, c.name AS club_name, c.acronym AS club_acronym, " +
//                        "c.year_creation AS club_year_creation, c.stadium AS club_stadium, " +
//                        "ch.name AS coach_name, ch.nationality AS coach_nationality " +
//                        "FROM player p " +
//                        "LEFT JOIN club c ON p.club_id = c.id " +
//                        "LEFT JOIN coach ch ON c.coach_id = ch.id " +
//                        "WHERE 1=1"
//        );
//
//        List<Object> params = new ArrayList<>();
//        if (name != null && !name.isEmpty()) {
//            sql.append(" AND LOWER(p.name) LIKE LOWER(?)");
//            params.add("%" + name + "%");
//        }
//        if (ageMinimum != null) {
//            sql.append(" AND p.age >= ?");
//            params.add(ageMinimum);
//        }
//        if (ageMaximum != null) {
//            sql.append(" AND p.age <= ?");
//            params.add(ageMaximum);
//        }
//        if (clubName != null && !clubName.isEmpty()) {
//            sql.append(" AND LOWER(c.name) LIKE LOWER(?)");
//            params.add("%" + clubName + "%");
//        }
//
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
//
//            for (int i = 0; i < params.size(); i++) {
//                stmt.setObject(i + 1, params.get(i));
//            }
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                boolean first = true;
//                while (rs.next()) {
//                    if (!first) json.append(",");
//                    first = false;
//
//                    json.append("{")
//                            .append("\"id\":\"").append(rs.getString("id")).append("\",")
//                            .append("\"name\":\"").append(rs.getString("name")).append("\",")
//                            .append("\"number\":\"").append(rs.getInt("number")).append(",")
//                            .append("\"position\":\"").append(rs.getString("position")).append("\",")
//                            .append("\"nationality\":\"").append(rs.getString("nationality")).append("\",")
//                            .append("\"age\":").append(rs.getInt("age")).append(",")
//                            .append("\"club\":{")
//                            .append("\"id\":\"").append(rs.getString("club_id")).append("\",")
//                            .append("\"name\":\"").append(rs.getString("club_name")).append("\",")
//                            .append("\"acronym\":\"").append(rs.getString("club_acronym")).append("\",")
//                            .append("\"yearCreation\":").append(rs.getInt("club_year_creation")).append(",")
//                            .append("\"stadium\":\"").append(rs.getString("club_stadium")).append("\",")
//                            .append("\"coach\":{")
//                            .append("\"name\":\"").append(rs.getString("coach_name")).append("\",")
//                            .append("\"nationality\":\"").append(rs.getString("coach_nationality")).append("\"")
//                            .append("}")
//                            .append("}")
//                            .append("}");
//                }
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException("Erreur lors de la récupération des joueurs", e);
//        }
//
//        json.append("]");
//        return json.toString();
//    }


    public List<PlayerDto> findPlayers(String name, Integer ageMinimum, Integer ageMaximum, String clubName) {
//        StringBuilder json = new StringBuilder("[");
        List<PlayerDto> playerDtoList = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.name, p.number, p.position, p.nationality, p.age, " +
                        "c.id AS club_id, c.name AS club_name, c.acronym AS club_acronym, " +
                        "c.year_creation AS club_year_creation, c.stadium AS club_stadium, " +
                        "ch.name AS coach_name, ch.nationality AS coach_nationality " +
                        "FROM player p " +
                        "LEFT JOIN club c ON p.club_id = c.id " +
                        "LEFT JOIN coach ch ON c.coach_id = ch.id " +
                        "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            sql.append(" AND LOWER(p.name) LIKE LOWER(?)");
            params.add("%" + name + "%");
        }
        if (ageMinimum != null) {
            sql.append(" AND p.age >= ?");
            params.add(ageMinimum);
        }
        if (ageMaximum != null) {
            sql.append(" AND p.age <= ?");
            params.add(ageMaximum);
        }
        if (clubName != null && !clubName.isEmpty()) {
            sql.append(" AND LOWER(c.name) LIKE LOWER(?)");
            params.add("%" + clubName + "%");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                   playerDtoList.add(playerWithClubRepository.mapDto(rs));
                }
                return playerDtoList;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des joueurs", e);
        }
    }


    public Player findById(String id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM player WHERE id = ?")) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return playerMapper.mapRow(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du joueur", e);
        }
    }

    public List<Player> findByClubId(String clubId) {
        List<Player> players = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM player WHERE club_id = ?")) {

            statement.setString(1, clubId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    players.add(playerMapper.mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des joueurs du club", e);
        }

        return players;
    }


    public Player save(Player player) {
        Player existingPlayer = findById(player.getId());

        try (Connection connection = dataSource.getConnection()) {
            if (existingPlayer == null) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO player (id, name, number, age, position, nationality, club_id) VALUES (?, ?, ?, ?, ?::player_position, ?, ?)")) {
                    statement.setString(1, player.getId());
                    statement.setString(2, player.getName());
                    statement.setInt(3, player.getNumber());
                    statement.setInt(4, player.getAge());
                    statement.setString(5, player.getPosition().toString());
                    statement.setString(6, player.getNationality());
                    statement.setString(7, null);

                    statement.executeUpdate();
                }
            } else {
                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE player SET name = ?, number = ?, age = ?, position = ?::player_position, nationality = ? WHERE id = ?")) {
                    statement.setString(1, player.getName());
                    statement.setInt(2, player.getNumber());
                    statement.setInt(3, player.getAge());
                    statement.setString(4, player.getPosition().toString());
                    statement.setString(5, player.getNationality());
                    statement.setString(6, player.getId());

                    statement.executeUpdate();
                }
            }
            return findById(player.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Error saving player: " + e.getMessage(), e);
        }
    }

    public List<Player> setClubID(String id, List<Player> players) {
        List<Player> playerList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            for (Player player : players) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE player SET club_id = ? WHERE id = ? " +
                                "RETURNING id, name, number, age, position, nationality, club_id")) {

                    statement.setString(1, id);
                    statement.setString(2, player.getId());

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            playerList.add(playerMapper.mapPlayer(resultSet));
                        } else {
                            throw new RuntimeException("Erreur lors de la mise à jour du joueur " + player.getId());
                        }
                    }
                }
            }
            return playerList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Player> changePlayersByClubId(String clubId, List<Player> newPlayers) throws SQLException {
        List<Player> finalList = new ArrayList<>();

        // 1. Valider tous les joueurs à ajouter
        for (Player player : newPlayers) {
            PlayerDto playerDto = playerWithClubRepository.findByIdWithClub(player.getId());

            if (playerDto == null) {
                finalList.add(player); // nouveau joueur
            } else if (playerDto.getClub() == null) {
                // joueur existant, pas encore attaché
                finalList.add(new Player(
                        playerDto.getId(),
                        playerDto.getName(),
                        playerDto.getNumber(),
                        playerDto.getAge(),
                        playerDto.getPosition(),
                        playerDto.getNationality()
                ));
            } else {
                // le joueur est déjà dans un autre club
                throw new NotFoundException(HttpStatus.BAD_REQUEST,
                        "Player " + playerDto.getName() + " is already attached to another club");
            }
        }

        // 2. Supprimer les anciens joueurs du club uniquement si tout est validé
        List<Player> actualPlayers = findByClubId(clubId);
        setClubID(null, actualPlayers);

        // 3. Ajouter les nouveaux joueurs
        return addNewOrExistingPlayersInCLub(clubId, finalList);
    }

    public List<Player> addNewOrExistingPlayersInCLub(String id, List<Player> players) {
        List<Player> playerList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            for (Player player : players) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "insert into player (id, name, number, age, position, nationality, club_id) values (?, ?, ?, ?, ?::player_position, ?, ?) " +
                                "on conflict (id) do update set " +
                                "name = excluded.name, " +
                                "number = excluded.number, " +
                                "age = excluded.age, " +
                                "position = excluded.position, " +
                                "nationality = excluded.nationality, " +
                                "club_id = excluded.club_id " +
                                "returning id, name, number, age, position, nationality, club_id")) {

                    statement.setString(1, player.getId());
                    statement.setString(2, player.getName());
                    statement.setInt(3, player.getNumber());
                    statement.setInt(4, player.getAge());
                    statement.setString(5, player.getPosition().toString());
                    statement.setString(6, player.getNationality());
                    statement.setString(7, id);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            playerList.add(playerMapper.mapPlayer(resultSet));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return playerList;
    }

    public PlayerStatistics findStatistics(String playerId, String seasonId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM player_statistics WHERE player_id = ? AND season_id = ?")) {

            statement.setString(1, playerId);
            statement.setString(2, seasonId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    PlayerStatistics stats = new PlayerStatistics();
                    stats.setId(rs.getString("id"));
                    stats.setPlayerId(rs.getString("player_id"));
                    stats.setSeasonId(rs.getString("season_id"));
                    stats.setScoredGoals(rs.getInt("scored_goals"));
                    stats.setPlayingTimeValue(rs.getDouble("playing_time_value"));
                    stats.setPlayingTimeUnit(DurationUnit.valueOf(rs.getString("playing_time_unit")));
                    return stats;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding player statistics", e);
        }
        return null;
    }


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
        Player player = findById(resultSet.getString("player_id"));
        PlayerScorerDto playerScorerDto = new PlayerScorerDto(player.getId(), player.getName(), player.getNumber());
        scorerDto.setPlayer(playerScorerDto);
        scorerDto.setMinuteOfGoal(resultSet.getInt("minute_of_goal"));
        scorerDto.setOwnGoal(resultSet.getBoolean("own_goal"));
        return scorerDto;
    }
}
