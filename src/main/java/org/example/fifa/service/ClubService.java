package org.example.fifa.service;

import org.example.fifa.model.Club;
import org.example.fifa.model.Player;
import org.example.fifa.repository.ClubRepository;
import org.example.fifa.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Component
public class ClubService {
    @Autowired private ClubRepository clubRepository;
    @Autowired private PlayerRepository playerRepository;

    public ResponseEntity<Object> getClubList(){
        clubRepository.findAll();
        return null;
    }

    public ResponseEntity<Object> createOrUpdateClub(List<Club> clubs){
        clubRepository.update(clubs);
        return null;
    }

    public ResponseEntity<Object> getActualPlayers(String idClub){
        playerRepository.getPlayersByClubId(idClub);
        return null;
    }

    public ResponseEntity<Object> changePlayerOfClub(String id, List<Player> players){
        playerRepository.changePlayersByClubId(id, players);
        return null;
    }

    public ResponseEntity<Object> addPlayersInClub(String id, List<Player> playerList){
        playerRepository.addNewOrExistingPlayersInCLub(id, playerList);
        return null;
    }

    public ResponseEntity<Object> getClubsStatistics(LocalDate seasonYear, boolean hasToBeClassified){
        clubRepository.getAllStatistic(seasonYear, hasToBeClassified);
        return null;
    }


}
