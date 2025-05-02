package org.example.fifa.repository;

import org.example.fifa.model.Championship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Component
public class ChampionshipRepository implements CrudDAO<Championship>{

    @Autowired private DataSource dataSource;

    @Override
    public List<Championship> findAll() {
        return List.of();
    }

    @Override
    public Championship findById(String id) {
        return null;
    }

    @Override
    public Championship findByName(String name) {
        return null;
    }

    @Override
    public List<Championship> save(List<Championship> entity) {
        return null;
    }

    @Override
    public List<Championship> update(List<Championship> entity) {
        return null;
    }

    @Override
    public void delete(Championship entity) {

    }
}
