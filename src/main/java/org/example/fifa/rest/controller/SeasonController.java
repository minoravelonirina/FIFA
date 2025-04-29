package org.example.fifa.rest.controller;

import org.example.fifa.model.Season;
import org.example.fifa.model.enums.Status;
import org.example.fifa.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Component
public class SeasonController {

    @Autowired private SeasonService seasonService;

    @GetMapping("/seasons")
    public ResponseEntity<Object> getAll(){
        return seasonService.getAllSeasons();
    }

    @PostMapping("/seasons")
    public ResponseEntity<Object> saveSeasons(@RequestBody List<Season> seasonList){
        return seasonService.saveSeasonList(seasonList);
    }

    @PutMapping("/seasons/{seasonYear}/status")
    public ResponseEntity<Object> updateSeasonStatus(@PathVariable LocalDate seasonYear,
                                                     @RequestBody Status status){
        return seasonService.updateSeasonStatus(seasonYear, status);
    }



}
