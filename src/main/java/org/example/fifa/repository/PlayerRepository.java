package org.example.fifa.repository;

import org.example.fifa.model.Club;
import org.example.fifa.model.Player;
import org.example.fifa.model.enums.Position;
import org.example.fifa.repository.mapper.PlayerMapper;
import org.example.fifa.rest.dto.PlayerDto;
import org.example.fifa.rest.dto.PlayerStatisticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PlayerRepository {
    private final DataSource dataSource;
    private final PlayerMapper playerMapper;
    private final ClubRepository clubRepository;

    @Autowired
    public PlayerRepository(DataSource dataSource, PlayerMapper playerMapper, ClubRepository clubRepository) {
        this.dataSource = dataSource;
        this.playerMapper = playerMapper;
        this.clubRepository = clubRepository;
    }

    public List<Player> findAll(String name, Integer ageMinimum, Integer ageMaximum, String clubName) {
        List<Player> players = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT p.* FROM player p");

        if (clubName != null && !clubName.isEmpty()) {
            sql.append(" JOIN club c ON p.club_id = c.id");
        }

        sql.append(" WHERE 1=1");
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

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    players.add(playerMapper.mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des joueurs", e);
        }

        return players;
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
                        "INSERT INTO player (id, name, number, age, position, nationality, club_id) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                    statement.setString(1, player.getId());
                    statement.setString(2, player.getName());
                    statement.setInt(3, player.getNumber());
                    statement.setInt(4, player.getAge());
                    statement.setString(5, player.getPosition().toString());
                    statement.setString(6, player.getNationality());
//                    statement.setString(7, player.getClubId());

                    statement.executeUpdate();
                }
            } else {
                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE player SET name = ?, number = ?, age = ?, position = ?, nationality = ?, club_id = ? WHERE id = ?")) {
                    statement.setString(1, player.getName());
                    statement.setInt(2, player.getNumber());
                    statement.setInt(3, player.getAge());
                    statement.setString(4, player.getPosition().toString());
                    statement.setString(5, player.getNationality());
                    //statement.setString(6, player.getClubId());
                    statement.setString(7, player.getId());

                    statement.executeUpdate();
                }
            }
            return player;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du joueur: " + e.getMessage(), e);
        }
    }

    public List<Player> setClubID(String id, List<Player> players) {
        List<Player> playerList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE player SET club_id = ? WHERE id = ? returning id, name, number, age, position, nationality, club_id")) {
                    players.forEach(player -> {
                        try {
                            statement.setString(1, id);
                            statement.setString(2, player.getId());
                            statement.executeUpdate();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    try (ResultSet resultSet = statement.executeQuery()){
                        while (resultSet.next()){
                            playerList.add(playerMapper.mapPlayer(resultSet));
                        }
                    }
                }
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return playerList;
    }


    public void deleteById(String id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM player WHERE id = ?")) {

            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du joueur", e);
        }
    }


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



    public PlayerStatisticDto getStatisticOfSpecificPlayer(String id, LocalDate seasonYear){
        return null;
    }


    // CLUB //
//    public List<Player> getPlayersByClubId(String id){
//        List<Player> players = new ArrayList<>();
//        try (Connection connection = dataSource.getConnection();
//             PreparedStatement statement = connection.prepareStatement(
//                     "select * from player where club_id = ?"
//             )) {
//            statement.setString(1, id);
//            try (ResultSet resultSet = statement.executeQuery()){
//                while (resultSet.next()){
//                    players.add(playerMapper.mapDto(resultSet));
//                }
//                return players;
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }


    public List<Player> changePlayersByClubId(String clubId, List<Player> newPlayers) throws SQLException {
        List<PlayerDto> sameOfNewPlayer = new ArrayList<>();

        // maka an le actuel anatin le club//
        List<Player> actualPlayers = findByClubId(clubId);

        // manala an le player tao am le club //
        List<Player> setPlayers = setClubID(null, actualPlayers);

        // maka ilay player izay mitovy id am le ho ampidirina, manao fampitahana //
        newPlayers.forEach(player -> {
            sameOfNewPlayer.add(findByIdWithClub(player.getId()));
        });

        sameOfNewPlayer.forEach(playerDto -> {
            if (playerDto.getClub().getId().equals(clubId) || playerDto.getClub().getId().equals(null)){

            }
        });

        // condition //
//         if

        // ovaina ilay club_id //
//        try (Connection connection = dataSource.getConnection();
//        PreparedStatement statement = connection.prepareStatement(
//                ""
//        ))
        return null;
    }

    public PlayerDto mapDto(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String clubId = resultSet.getString("club_id");
        Club club = clubRepository.findById(clubId);
        Position position = Position.valueOf(resultSet.getString("position"));
        String nationality = resultSet.getString("nationality");
        int age = resultSet.getInt("age");
        String name = resultSet.getString("name");
        int number = resultSet.getInt("number");
        return new PlayerDto(id, club, position, nationality, age, name, number);
    }

    public List<Player> addNewOrExistingPlayersInCLub(String id, List<Player> newPlayers){
        List<Player> playerList = new ArrayList<>();
        newPlayers.forEach(player -> {

        });
        try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                ""
        )) {
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

