package org.example.fifa.rest.controller;

import org.example.fifa.model.enums.Status;
import org.example.fifa.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Component
public class MatchController {
    @Autowired private MatchService matchService;

    @PostMapping("/matchMaker/{seasonYear}")
    public ResponseEntity<Object> createAllMatches(@PathVariable LocalDate seasonYear){
        return matchService.saveMatches(seasonYear, List.of());
    }

    @GetMapping("/matches/{seasonYear}")
    public ResponseEntity<Object> getAllMatches(@PathVariable LocalDate seasonYear,
                                                @RequestParam Status matchStatus,
                                                @RequestParam String clubPlayingName,
                                                @RequestParam LocalDate matchAfter,
                                                @RequestParam LocalDate matchBeforeOrEquals){
        return matchService.getAll(seasonYear, matchStatus, clubPlayingName, matchAfter, matchBeforeOrEquals);
    }

    @PutMapping("/matches/{id}/status")
    public ResponseEntity<Object> changeMatchStatus(@PathVariable String id, @RequestParam Status status){
        return matchService.changeStatus(id, status);
    }

    @PostMapping("/matches/{id}/goals")
    public ResponseEntity<Object> addGoals(@PathVariable String id){
        return matchService.addGoals(id);
    }
}
