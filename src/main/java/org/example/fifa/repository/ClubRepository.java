package org.example.fifa.repository;

import org.example.fifa.model.Club;
import org.example.fifa.rest.dto.ClubStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
@Component
public class ClubRepository implements CrudDAO<Club>{

    @Autowired private DataSource dataSource;

    @Override
    public List<Club> findAll() {
        return List.of();
    }

    @Override
    public Club findById(Long id) {
        return null;
    }

    @Override
    public Club findByName(String name) {
        return null;
    }

    @Override
    public List<Club> save(List<Club> entity) {
        return null;
    }

    @Override
    public List<Club> update(List<Club> entity) {
        return null;
    }

    @Override
    public void delete(Club entity) {

    }

    public List<ClubStatistics> getAllStatistic(LocalDate seasonYear, boolean hasToBeClassified){
        return null;
    }

}
