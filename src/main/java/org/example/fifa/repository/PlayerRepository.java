package org.example.fifa.repository;

import org.example.fifa.model.Club;
import org.example.fifa.model.Player;
import org.example.fifa.model.enums.Position;
import org.example.fifa.model.exception.NotFoundException;
import org.example.fifa.repository.mapper.PlayerMapper;
import org.example.fifa.rest.dto.ClubDto;
import org.example.fifa.rest.dto.PlayerDto;
import org.example.fifa.rest.dto.PlayerStatisticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
//                    statement.setString(6, player.getClubId());
                    statement.setString(6, player.getId());

                    statement.executeUpdate();
                }
            }
            return player;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du joueur: " + e.getMessage(), e);
        }
    }

    public PlayerStatisticDto getStatisticOfSpecificPlayer(String id, LocalDate seasonYear){
        return null;
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

//
//    public List<Player> setClubID(String id, List<Player> players) {
//        List<Player> playerList = new ArrayList<>();
//
//        try (Connection connection = dataSource.getConnection()) {
//                try (PreparedStatement statement = connection.prepareStatement(
//                        "UPDATE player SET club_id = ? WHERE id = ? returning id, name, number, age, position, nationality, club_id")) {
//                    players.forEach(player -> {
//                        try {
//                            statement.setString(1, id);
//                            statement.setString(2, player.getId());
//                            statement.addBatch();
//                        } catch (SQLException e) {
//                            throw new RuntimeException(e);
//                        }
//                    });
//                    int[] updateCounts = statement.executeBatch();
//                    if (updateCounts.length != players.size()) {
//                        throw new RuntimeException("Erreur lors de la mise à jour des joueurs");
//                    }
//                    for (int i = 0; i < updateCounts.length; i++) {
//                        if (updateCounts[i] != 1) {
//                            throw new RuntimeException("Erreur lors de la mise à jour du joueur " + players.get(i).getId());
//                        }
//                    }
//                    ResultSet resultSet = statement.getGeneratedKeys();
//                        while (resultSet.next()){
//                            playerList.add(playerMapper.mapPlayer(resultSet));
//                        }
//                    System.out.println("TEST2 setClubID");
//                        return playerList;
//                }
//            } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public PlayerDto findByIdWithClub(String id) {
//        try (Connection connection = dataSource.getConnection();
//             PreparedStatement statement = connection.prepareStatement(
//                     "SELECT * FROM player WHERE id = ?")) {
//
//            statement.setString(1, id);
//
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    System.out.println("TEST3 findByIdWithClub");
//                    return mapDto(resultSet);
//                }
//                return null;
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("Erreur lors de la recherche du joueur", e);
//        }
//    }
//
//
//    public List<Player> addNewOrExistingPlayersInCLub(String id, List<Player> players) {
//        List<Player> playerList = new ArrayList<>();
//
//        try (Connection connection = dataSource.getConnection()) {
//            try (PreparedStatement statement = connection.prepareStatement(
//                    "insert into player (id, name, number, age, position, nationality, club_id) values (?, ?, ?, ?, ?, ?, ?) " +
//                            "on conflict (id) do update set " +
//                            "name = excluded.name, " +
//                            "number = excluded.number, " +
//                            "age = excluded.age, " +
//                            "position = excluded.position, " +
//                            "nationality = excluded.nationality, " +
//                            "club_id = excluded.club_id " +
//                            "returning id, name, number, age, position, nationality, club_id")) {
//                players.forEach(player -> {
//                    try {
//                        statement.setString(1, player.getId());
//                        statement.setString(2, player.getName());
//                        statement.setInt(3, player.getNumber());
//                        statement.setInt(4, player.getAge());
//                        statement.setString(5, player.getPosition().toString());
//                        statement.setString(6, player.getNationality());
//                        statement.setString(7, id);
//
//                        statement.executeUpdate();
//                    } catch (SQLException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//                try (ResultSet resultSet = statement.executeQuery()){
//                    while (resultSet.next()){
//                        playerList.add(playerMapper.mapPlayer(resultSet));
//                    }
//                    System.out.println("TEST4 addNewOrExistingPlayersInCLub");
//                }
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return playerList;
//    }
//
//
//    public List<Player> changePlayersByClubId(String clubId, List<Player> newPlayers) throws SQLException {
//        List<PlayerDto> sameOfNewPlayer = new ArrayList<>();
//        List<Player> finalList = new ArrayList<>();
//
//        // GET ACTUAL PLAYERS OF CLUB
//        List<Player> actualPlayers = findByClubId(clubId);
//
//        // DELETE PLAYERS OF CLUB
//        List<Player> setPlayers = setClubID(null, actualPlayers);
//
//        // COMPARE LIST OF THE NEW PLAYERS WITH PLAYERS IN THE DATABASE
//        newPlayers.forEach(player -> {
//            sameOfNewPlayer.add(findByIdWithClub(player.getId()));
//        });
//
//        // ADD NEW PLAYERS IN CLUB OR SET ID CLUB IF EXISTS
//        sameOfNewPlayer.forEach(playerDto -> {
//
//            if (playerDto.getClub() == null){
//                finalList.add(
//                        new Player(
//                        playerDto.getId(),
//                        playerDto.getName(),
//                        playerDto.getNumber(),
//                        playerDto.getAge(),
//                        playerDto.getPosition(),
//                        playerDto.getNationality()
//                ));
//                System.out.println("TEST5 changePlayersByClubId");
//            } else {
//                throw new NotFoundException(HttpStatus.BAD_REQUEST, "Player "+playerDto.getName()+ " is already attached to another club");
//            }
//        });
//        return addNewOrExistingPlayersInCLub(clubId, finalList);
//    }
//
//    public PlayerDto mapDto(ResultSet resultSet) throws SQLException {
//        String id = resultSet.getString("id");
//        String clubId = resultSet.getString("club_id");
//        ClubDto club = new ClubDto(clubRepository.findById(clubId));
//        Position position = Position.valueOf(resultSet.getString("position"));
//        String nationality = resultSet.getString("nationality");
//        int age = resultSet.getInt("age");
//        String name = resultSet.getString("name");
//        int number = resultSet.getInt("number");
//        return new PlayerDto(id, club, position, nationality, age, name, number);
//    }


//    public List<Player> setClubID(String id, List<Player> players) {
//        List<Player> playerList = new ArrayList<>();
//
//        try (Connection connection = dataSource.getConnection()) {
//            try (PreparedStatement statement = connection.prepareStatement(
//                    "UPDATE player SET club_id = ? WHERE id = ? returning id, name, number, age, position, nationality, club_id")) {
//
//                for (Player player : players) {
//                    statement.setString(1, id);
//                    statement.setString(2, player.getId());
//                    statement.addBatch();
//                }
//
//                int[] updateCounts = statement.executeBatch();
//
//                if (updateCounts.length != players.size()) {
//                    throw new RuntimeException("Erreur lors de la mise à jour des joueurs");
//                }
//
//                for (int i = 0; i < updateCounts.length; i++) {
//                    if (updateCounts[i] != 1) {
//                        throw new RuntimeException("Erreur lors de la mise à jour du joueur " + players.get(i).getId());
//                    }
//                }
//
//                try (ResultSet resultSet = statement.getResultSet()) {
//                    while (resultSet != null && resultSet.next()) {
//                        playerList.add(playerMapper.mapPlayer(resultSet));
//                    }
//                }
//
//                System.out.println("TEST2 setClubID");
//                return playerList;
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }


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
            System.out.println("TEST2 setClubID");
            return playerList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public PlayerDto findByIdWithClub(String id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM player WHERE id = ?")) {

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("TEST3 findByIdWithClub");
                    return mapDto(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du joueur", e);
        }
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
            System.out.println("TEST4 addNewOrExistingPlayersInCLub");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return playerList;
    }

//    public List<Player> changePlayersByClubId(String clubId, List<Player> newPlayers) throws SQLException {
//        List<PlayerDto> sameOfNewPlayer = new ArrayList<>();
//        List<Player> finalList = new ArrayList<>();
//
//        // GET ACTUAL PLAYERS OF CLUB
//        List<Player> actualPlayers = findByClubId(clubId);
//
//        // DELETE PLAYERS OF CLUB
//        List<Player> setPlayers = setClubID(null, actualPlayers);
//
//        // COMPARE LIST OF THE NEW PLAYERS WITH PLAYERS IN THE DATABASE
//        for (Player player : newPlayers) {
//            PlayerDto playerDto = findByIdWithClub(player.getId());
//            if (playerDto == null) {
//                finalList.add(player);
//            }
//            sameOfNewPlayer.add(playerDto);
//        }
//
//        // ADD NEW PLAYERS IN CLUB OR SET ID CLUB IF EXISTS
//        for (PlayerDto playerDto : sameOfNewPlayer) {
//
//            if (playerDto.getClub() == null) {
//                finalList.add(new Player(
//                        playerDto.getId(),
//                        playerDto.getName(),
//                        playerDto.getNumber(),
//                        playerDto.getAge(),
//                        playerDto.getPosition(),
//                        playerDto.getNationality()
//                ));
//                System.out.println("TEST5 changePlayersByClubId");
//            } else {
//                throw new NotFoundException(HttpStatus.BAD_REQUEST,
//                        "Player " + playerDto.getName() + " is already attached to another club");
//            }
//        }
//
//        return addNewOrExistingPlayersInCLub(clubId, finalList);
//    }


    public List<Player> changePlayersByClubId(String clubId, List<Player> newPlayers) throws SQLException {
        List<Player> finalList = new ArrayList<>();

        // 1. Valider tous les joueurs à ajouter
        for (Player player : newPlayers) {
            PlayerDto playerDto = findByIdWithClub(player.getId());

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


    public PlayerDto mapDto(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String clubId = resultSet.getString("club_id");
        ClubDto club = clubId != null ? new ClubDto(clubRepository.findById(clubId)) : null;
        Position position = Position.valueOf(resultSet.getString("position"));
        String nationality = resultSet.getString("nationality");
        int age = resultSet.getInt("age");
        String name = resultSet.getString("name");
        int number = resultSet.getInt("number");
        return new PlayerDto(id, club, position, nationality, age, name, number);
    }

}

