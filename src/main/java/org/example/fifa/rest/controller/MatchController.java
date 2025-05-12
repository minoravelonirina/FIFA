package org.example.fifa.rest.controller;

import org.example.fifa.model.Match;
import org.example.fifa.model.enums.Status;
import org.example.fifa.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RestController
@Component
public class MatchController {
    @Autowired private MatchService matchService;

    @PostMapping("/matchMaker/{seasonYear}")
    public ResponseEntity<Object> createAllMatches(@PathVariable LocalDate seasonYear, @RequestBody List<Match> matches) throws SQLException {
        return matchService.saveMatches(seasonYear, matches);
    }

    @GetMapping("/matches/{seasonYear}")
    public ResponseEntity<Object> getAllMatches(@PathVariable LocalDate seasonYear,
                                                @RequestParam(required = false) String matchStatus,
                                                @RequestParam(required = false) String clubPlayingName,
                                                @RequestParam(required = false) LocalDate matchAfter,
                                                @RequestParam(required = false) LocalDate matchBeforeOrEquals) throws SQLException {
        return matchService.getAll(seasonYear, Status.valueOf(matchStatus), clubPlayingName, matchAfter, matchBeforeOrEquals);
    }

    @PutMapping("/matches/{id}/status")
    public ResponseEntity<Object> changeMatchStatus(@PathVariable String id, @RequestBody Status status){
        return matchService.changeStatus(id, status);
    }

    @PostMapping("/matches/{id}/goals")
    public ResponseEntity<Object> addGoals(@PathVariable String id, @RequestBody List<Object> list){
        return matchService.addGoals(id, list);
    }
}
