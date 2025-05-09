package org.example.fifa.service;

import org.example.fifa.model.Club;
import org.example.fifa.model.Coach;
import org.example.fifa.model.Player;
import org.example.fifa.model.Season;
import org.example.fifa.model.enums.Status;
import org.example.fifa.repository.*;
import org.example.fifa.rest.dto.ClubStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Component
public class ClubService {
    @Autowired private ClubRepository clubRepository;
    @Autowired private PlayerRepository playerRepository;
    @Autowired private SeasonRepository seasonRepository;


    public ResponseEntity<Object> getClubList() throws SQLException {
        List<Club> clubList = new ArrayList<>(clubRepository.findAll());
        return ResponseEntity.status(HttpStatus.OK).body(clubList);
    }


    public ResponseEntity<Object> createOrUpdateClubs(List<Club> clubs) throws SQLException {
        List<Club> clubList = new ArrayList<>(clubRepository.update(clubs));
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
        List<Player> actualPlayers = playerRepository.findByClubId(id);
        if (actualPlayers.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The specific club of id "+id+ " is not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(playerRepository.changePlayersByClubId(id, players));
    }


    public ResponseEntity<Object> addPlayersInClub(String id, List<Player> playerList){
        if (playerList.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The list of players is empty");
        }
        return ResponseEntity.status(HttpStatus.OK).body(playerRepository.addNewOrExistingPlayersInCLub(id, playerList));
    }


    public ResponseEntity<Object> getClubsStatistics(int seasonYear, boolean hasToBeClassified) throws SQLException {
        List<ClubStatistics> stats = clubRepository.getAllStatistic(seasonYear);
        Season season = seasonRepository.findByYear(seasonYear);

        if (season.getStatus().equals(Status.FINISHED)){
            return ResponseEntity.badRequest().body("The season is not finished");
        }
        if (stats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No statistics for the season " + seasonYear);
        }
        if (hasToBeClassified) {
            stats.sort(Comparator
                    .comparingInt(ClubStatistics::getRankingPoints).reversed()
                    .thenComparingInt(ClubStatistics::getDifferenceGoals).reversed()
                    .thenComparingInt(ClubStatistics::getCleanSheetNumber).reversed());
        } else {
            stats.sort(Comparator.comparing(ClubStatistics::getName));
        }
        return ResponseEntity.status(HttpStatus.OK).body(stats);
    }
}
