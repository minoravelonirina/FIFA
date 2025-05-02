package org.example.fifa.repository;

import org.example.fifa.model.Club;
import org.example.fifa.repository.mapper.ClubMapper;
import org.example.fifa.rest.dto.ClubStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Component
public class ClubRepository implements CrudDAO<Club>{

    @Autowired private DataSource dataSource;
    @Autowired private ClubMapper clubMapper;
    @Autowired private CoachRepository coachRepository;

    @Override
    public List<Club> findAll() throws SQLException {
        List<Club> clubList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from club"
             )){
                 try (ResultSet resultSet = statement.executeQuery()){
                     while (resultSet.next()){
                         clubList.add(clubMapper.apply(resultSet));
                     }
                     return clubList;
                 }
                 catch (SQLException e) {
                     throw new RuntimeException("Erreur lors de la recuperation des clubs", e);
                 }
        }
    }


    @Override
    public Club findById(String id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from club where id = ?"
             )){
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return clubMapper.apply(resultSet);
                }
                return null;
            }
            catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la recuperation des clubs", e);
            }
        }
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
        List<Club> clubs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "insert into club (id, name, acronym, year_creation, stadium, coach_id) values (?, ?, ?, ?, ?, ?) " +
                        "on conflict (id) do update set " +
                        "name=excluded.name, " +
                        "acronym=excluded.acronym, " +
                        "year_creation=excluded.year_creation, " +
                        "stadium=excluded.stadium, " +
                        "coach_id=excluded.coach_id " +
                        "returning id, name, acronym, year_creation, stadium, coach_id "
        )) {
            entity.forEach(club -> {
                try {
                    statement.setString(1, club.getId());
                    statement.setString(2, club.getName());
                    statement.setString(3, club.getAcronym());
                    statement.setInt(4, club.getYearCreation());
                    statement.setString(5, club.getStadium());
                    statement.setString(6, club.getCoach().getId());
                    coachRepository.save(club.getCoach());
                    statement.addBatch();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    clubs.add(clubMapper.apply(resultSet));
                }
                return clubs;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Club entity) {

    }

    public List<ClubStatistics> getAllStatistic(LocalDate seasonYear, boolean hasToBeClassified){
        return null;
    }

}
