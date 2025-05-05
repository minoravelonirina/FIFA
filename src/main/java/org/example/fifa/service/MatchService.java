package org.example.fifa.service;

import org.example.fifa.model.Match;
import org.example.fifa.model.RequestGoal;
import org.example.fifa.model.enums.Status;
import org.example.fifa.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    public ResponseEntity<Object> saveMatches(LocalDate seasonYear, List<Match> matches){
        matchRepository.createAllMatches(seasonYear, matches);
        return null;
    }

    public ResponseEntity<Object> getAll(LocalDate seasonYear, Status matchStatus, String clubPlayingName, LocalDate matchAfter, LocalDate matchBeforeOrEquals) throws SQLException {
        return ResponseEntity.status(HttpStatus.OK).body(matchRepository.findAll(seasonYear, matchStatus, clubPlayingName, matchAfter, matchBeforeOrEquals));
    }

    public ResponseEntity<Object> changeStatus(String id, Status status){
        matchRepository.changeStatusOfMatch(id, status);
        return null;
    }

    public ResponseEntity<Object> addGoals(String id, List<RequestGoal> requestList){
        return ResponseEntity.ok(matchRepository.saveGoalsInMatch(id, requestList));
    }
}
