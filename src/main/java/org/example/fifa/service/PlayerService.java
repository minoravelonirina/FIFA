package org.example.fifa.service;

import org.example.fifa.model.ClubPlayer;
import org.example.fifa.model.Player;
import org.example.fifa.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Component
public class PlayerService  {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<ClubPlayer> findPlayers(String name, Integer ageMinimum, Integer ageMaximum, String clubName) {
        List<Player> players = playerRepository.findAll(name, ageMinimum, ageMaximum, clubName);
        return players.stream()
                .map(player -> {
                    ClubPlayer clubPlayer = new ClubPlayer();
                    clubPlayer.setId(player.getId());
                    clubPlayer.setName(player.getName());
                    clubPlayer.setNumber(player.getNumber());
                    clubPlayer.setAge(player.getAge());
                    clubPlayer.setPosition(player.getPosition());
                    clubPlayer.setNationality(player.getNationality());
                    //clubPlayer.setClubId(player.getClubId());
                    return clubPlayer;
                })
                .collect(Collectors.toList());
    }

    public Player findById(String id) {
        return playerRepository.findById(id);
    }

    public List<Player> findByClubId(String clubId) {
        return playerRepository.findByClubId(clubId);
    }

    public Player save(Player player) {
        return playerRepository.save(player);
    }

    public void deleteById(String id) {
        playerRepository.deleteById(id);
    }
}
