package org.example.fifa.service;


import org.example.fifa.model.*;
import org.example.fifa.model.enums.Status;
import org.example.fifa.repository.MatchRepository;
import org.example.fifa.repository.PlayerRepository;
import org.example.fifa.repository.SeasonRepository;
import org.example.fifa.rest.dto.MatchDto;
import org.example.fifa.rest.dto.PlayerDto;
import org.example.fifa.rest.dto.PlayerStatisticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Component
public class PlayerService  {
    private final PlayerRepository playerRepository;
    private final SeasonRepository seasonRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, SeasonRepository seasonRepository) {
        this.playerRepository = playerRepository;
        this.seasonRepository = seasonRepository;
    }

    public ResponseEntity<Object> findPlayers(String name, Integer ageMinimum, Integer ageMaximum, String clubName) {
        try {
            List<PlayerDto> players = playerRepository.findPlayers(name, ageMinimum, ageMaximum, clubName);

            if (players.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(players);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la recherche des joueurs : " + e.getMessage());
        }
    }


    public ResponseEntity<Player> findById(String id) {
        Player player = playerRepository.findById(id);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(player);
    }


    public ResponseEntity<List<Player>> saveAll(List<Player> players) {
        List<Player> savedPlayers = players.stream()
                .map(this::save)
                .collect(Collectors.toList());
        return ResponseEntity.ok(savedPlayers);
    }


    public Player save(Player player) {
        validatePlayer(player);
        return playerRepository.save(player);
    }


    private void validatePlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (player.getName() == null || player.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Player name is required");
        }
        if (player.getPosition() == null) {
            throw new IllegalArgumentException("Player position is required");
        }
        if (player.getNumber() <= 0) {
            throw new IllegalArgumentException("Player number must be positive");
        }
        if (player.getAge() <= 0) {
            throw new IllegalArgumentException("Player age must be positive");
        }
    }


    public ResponseEntity<Object> findPlayerStatistics(String playerId, LocalDate seasonYear) throws SQLException {
        Season season = seasonRepository.findByYear(seasonYear.getYear());
        if (!season.getStatus().equals(Status.FINISHED)) {
            return ResponseEntity.badRequest().body("The season is not finished");
        }
        if (season != null) {
            return ResponseEntity.ok(playerRepository.findStatistics(playerId, season.getId()));
        }
        return ResponseEntity.notFound().build();

    }

}