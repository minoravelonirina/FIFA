package org.example.fifa.repository;

import org.example.fifa.model.Match;
import org.example.fifa.model.enums.Status;
import org.example.fifa.rest.dto.MatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
@Component
public class MatchRepository implements CrudDAO<Match>{

    @Autowired private DataSource dataSource;

    @Override
    public List<Match> findAll() {
        return List.of();
    }

    @Override
    public Match findById(Long id) {
        return null;
    }

    @Override
    public Match findByName(String name) {
        return null;
    }

    @Override
    public List<Match> save(List<Match> entity) {
        return null;
    }

    @Override
    public List<Match> update(List<Match> entity) {
        return null;
    }

    @Override
    public void delete(Match entity) {
    }

    public List<Match> createAllMatches(String seasonYear, List<Match> matches){
        return null;
    }

    public List<Match> findAll(String id, Status matchStatus, String clubPlayingName, LocalDate matchAfter, LocalDate matchBeforeOrEquals){
        return null;
    }

    public Match changeStatusOfMatch(String id, Status status){
        return null;
    }

    public List<MatchDto> addGoalsInMatch(String id, List<>){
        return null;
    }
}
