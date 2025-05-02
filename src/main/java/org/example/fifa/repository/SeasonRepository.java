package org.example.fifa.repository;

import org.example.fifa.model.Season;
import org.example.fifa.model.enums.Status;
import org.example.fifa.repository.mapper.SeasonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Component
public class SeasonRepository implements CrudDAO<Season> {

    @Autowired private SeasonMapper seasonMapper;

    @Override
    public List<Season> findAll() {
        return List.of();
    }

    @Override
    public Season findById(String id) {
        return null;
    }

    @Override
    public Season findByName(String name) {
        return null;
    }

    @Override
    public List<Season> save(List<Season> entity) {
        return null;
    }

    @Override
    public List<Season> update(List<Season> entity) {
        return null;
    }

    @Override
    public void delete(Season entity) {

    }

    public Season updateSeasonOfStatus(LocalDate seasonYear, Status status){
        return null;
    }
}
