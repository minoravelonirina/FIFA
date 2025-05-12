package org.example.fifa.service;

import org.example.fifa.model.Match;
import org.example.fifa.model.Season;
import org.example.fifa.model.enums.Status;
import org.example.fifa.repository.MatchRepository;
import org.example.fifa.repository.SeasonRepository;
import org.example.fifa.rest.dto.MatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
@Component
public class MatchService {

    @Autowired private MatchRepository matchRepository;
    @Autowired private SeasonRepository seasonRepository;

    public ResponseEntity<Object> saveMatches(LocalDate seasonYear, List<Match> matches) throws SQLException {
        Season season = seasonRepository.findByYear(seasonYear.getYear());

        List<MatchDto> matchList = matchRepository.findAll(seasonYear, null, null, null, null);

        if (!matchList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The list of matches for the specific season is already generated");
        }
        if (season == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The season is not found");
        }
        if (season.getStatus().equals(Status.STARTED)){
            return ResponseEntity.status(HttpStatus.OK).body(matchRepository.createAllMatches(seasonYear, matches));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The season is not started or already finished");

    }



    public ResponseEntity<Object> getAll(LocalDate seasonYear, Status matchStatus, String clubPlayingName, LocalDate matchAfter, LocalDate matchBeforeOrEquals) throws SQLException {
        List<MatchDto> matchList = matchRepository.findAll(seasonYear, matchStatus, clubPlayingName, matchAfter, matchBeforeOrEquals);

        if (matchList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The list of matches for the specific season is not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(matchList);
    }



    public ResponseEntity<Object> changeStatus(String id, Status status){
        MatchDto match = matchRepository.getById(id);
        Status statusOfMatch = match.getActualStatus();

        if (statusOfMatch.equals(Status.NOT_STARTED) && status.equals(Status.STARTED)
        || statusOfMatch.equals(Status.STARTED) && status.equals(Status.FINISHED)
        ){
            return ResponseEntity.status(HttpStatus.OK).body(matchRepository.changeStatusOfMatch(id, status));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Change the status to : "+ status+ " is not allowed");
    }



    public ResponseEntity<Object> addGoals(String id, List<Object> requestList){
        MatchDto match = matchRepository.getById(id);
        Status statusOfMatch = match.getActualStatus();

        if (statusOfMatch.equals(Status.STARTED)){
            return ResponseEntity.ok(matchRepository.saveGoalsInMatch(id, requestList));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The status of match is not STARTED");

    }
}
