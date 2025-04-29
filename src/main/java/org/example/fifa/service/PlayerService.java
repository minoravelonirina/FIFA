package org.example.fifa.service;

import org.example.fifa.model.Player;
import org.example.fifa.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Component
public class PlayerService  {
    @Autowired private PlayerRepository playerRepository;

    public ResponseEntity<Object> getAllPlayers(){
        playerRepository.findAll();
        return null;
    }

    public ResponseEntity<Object> updatePlayerList(List<Player> players){
        playerRepository.update(players);
        return null;
    }

    public ResponseEntity<Object> getStatisticOfSpecificPlayer(String idPlayer, LocalDate seasonYear){
        playerRepository.getStatisticOfSpecificPlayer(idPlayer, seasonYear);
        return null;
    }
}
