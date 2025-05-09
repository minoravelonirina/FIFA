package org.example.fifa.repository;


import org.example.fifa.model.Player;
import org.example.fifa.model.enums.TransferType;
import org.example.fifa.model.exception.NotFoundException;
import org.example.fifa.repository.mapper.PlayerMapper;
import org.example.fifa.repository.mapper.PlayerStatisticMapper;
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
    private final PlayerStatisticMapper playerStatisticMapper;
    private final TransferRepository transferRepository;

    @Autowired
    public PlayerRepository(DataSource dataSource, PlayerMapper playerMapper, PlayerWithClubRepository playerWithClubRepository, PlayerStatisticMapper playerStatisticMapper, TransferRepository transferRepository) {
        this.dataSource = dataSource;
        this.playerMapper = playerMapper;
        this.playerWithClubRepository = playerWithClubRepository;
        this.playerStatisticMapper = playerStatisticMapper;
        this.transferRepository = transferRepository;
    }

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

    public void setClubID(String newClubId, List<Player> players, String actualClubId) {
        List<Player> playerList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            for (Player player : players) {

                if (newClubId == null){
                    transferRepository.registerTransfer(player.getId(), actualClubId, TransferType.OUT);
                }
                else {
                    transferRepository.registerTransfer(player.getId(), newClubId, TransferType.IN);
                }


                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE player SET club_id = ? WHERE id = ? " +
                                "RETURNING id, name, number, age, position, nationality, club_id")) {

                    statement.setString(1, newClubId);
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
            } else if (playerDto.getClub() == null || playerDto.getClub().getId().equals(clubId)) {
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
        setClubID(null, actualPlayers, clubId);

        // 3. Ajouter les nouveaux joueurs
        return addNewOrExistingPlayersInCLub(clubId, finalList);
    }


    public List<Player> addNewOrExistingPlayersInCLub(String id, List<Player> players) {
        List<Player> playerList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            for (Player player : players) {
                transferRepository.registerTransfer(player.getId(), id, TransferType.IN);
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


    public PlayerStatisticDto findStatistics(String playerId, String seasonId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) AS scored_goals, g.minute_of_goal " +
                             "FROM goal g " +
                             "         JOIN \"match\" m ON g.match_id = m.id " +
                             "WHERE g.player_id = ? " +
                             "  AND g.own_goal = false " +
                             "  AND m.season_id = ? " +
                             "group by g.minute_of_goal ")) {

            statement.setString(1, playerId);
            statement.setString(2, seasonId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return playerStatisticMapper.apply(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding player statistics", e);
        }
    }


    public List<ScorerDto> getScorerOfClub (String clubId) throws SQLException {
        List<ScorerDto> list = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from goal where club_id = ?"
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


    public List<ScorerDto> getScorerOfMatch (String clubId, String matchId) throws SQLException {
        List<ScorerDto> list = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from goal where club_id = ? and match_id = ?"
             )){
            statement.setString(1, clubId);
            statement.setString(2, matchId);
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
