package org.example.fifa.service;

import org.example.fifa.model.Season;
import org.example.fifa.model.enums.Status;
import org.example.fifa.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Component
public class SeasonService {
    @Autowired private SeasonRepository seasonRepository;

    public ResponseEntity<Object> getAllSeasons(){
        seasonRepository.findAll();
        return null;
    }

    public ResponseEntity<Object> saveSeasonList(List<Season> seasonList){
        seasonRepository.save(seasonList);
        return null;
    }

    public ResponseEntity<Object> updateSeasonStatus(LocalDate seasonYear, Status status){
        seasonRepository.updateSeasonOfStatus(seasonYear, status);
        return null;
    }


}
