package org.example.fifa.service;

import org.example.fifa.model.Club;
import org.example.fifa.model.Coach;
import org.example.fifa.model.Player;
import org.example.fifa.repository.ClubRepository;
import org.example.fifa.repository.CoachRepository;
import org.example.fifa.repository.PlayerRepository;
import org.example.fifa.rest.dto.ClubDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class ClubService {
    @Autowired private ClubRepository clubRepository;
    @Autowired private PlayerRepository playerRepository;
    @Autowired private CoachRepository coachRepository;

    public ResponseEntity<Object> getClubList() throws SQLException {
        List<ClubDto> clubList = new ArrayList<>();
        clubRepository.findAll().forEach(club -> {
            clubList.add(new ClubDto(club));
        });
        return ResponseEntity.status(HttpStatus.OK).body(clubList);
    }

    public ResponseEntity<Object> createOrUpdateClubs(List<Club> clubs){
        List<ClubDto> clubList = new ArrayList<>();
        clubRepository.update(clubs).forEach(club -> {
            clubList.add(new ClubDto(club));
        });
        return ResponseEntity.status(HttpStatus.OK).body(clubList);
    }

    public ResponseEntity<Object> getActualPlayers(String idClub){
        List<Player> players = playerRepository.findByClubId(idClub);
        if (players.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The specific club of id "+idClub+ " is not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(players);
    }

    public ResponseEntity<Object> changePlayerOfClub(String id, List<Player> players) throws SQLException {
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
