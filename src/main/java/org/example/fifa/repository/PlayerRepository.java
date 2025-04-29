package org.example.fifa.repository;

import org.example.fifa.model.Player;
import org.example.fifa.repository.mapper.PlayerMapper;
import org.example.fifa.rest.dto.PlayerStatisticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
@Component
public class PlayerRepository implements CrudDAO<Player> {

    @Autowired private DataSource dataSource;
    @Autowired private PlayerMapper playerMapper;

    @Override
    public List<Player> findAll() {
        return List.of();
    }

    @Override
    public Player findById(Long id) {
        return null;
    }

    @Override
    public Player findByName(String name) {
        return null;
    }

    @Override
    public List<Player> save(List<Player> entity) {
        return null;
    }

    @Override
    public List<Player> update(List<Player> entity) {
        return null;
    }

    @Override
    public void delete(Player entity) {

    }

    public PlayerStatisticDto getStatisticOfSpecificPlayer(String id, LocalDate seasonYear){
        return null;
    }

    public List<Player> getPlayersByClubId(String id){
        return null;
    }

    public List<Player> changePlayersByClubId(String club, List<Player> players){
        return null;
    }

    public List<Player> addNewOrExistingPlayersInCLub(String id, List<Player> players){
        return null;
    }
}
