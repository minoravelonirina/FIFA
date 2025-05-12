package org.example.fifa.repository;

import org.example.fifa.model.Coach;
import org.example.fifa.repository.mapper.CoachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Component
public class CoachRepository {

    @Autowired private DataSource dataSource;
    @Autowired private CoachMapper coachMapper;

    public Coach getCoach(String idClub){
        try (Connection connection = dataSource.getConnection();

             PreparedStatement statement = connection.prepareStatement(
                     "select coach.id, coach.name, coach.nationality " +
                             "from coach " +
                             "left join club " +
                             "on club.coach_id = coach.id " +
                             "where club.id = ?"
             )) {
            statement.setString(1, idClub);

            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return coachMapper.apply(resultSet);
                }
                return null;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Il y a un probleme", e);
        }
    }


    public void save(Coach coach) throws SQLException {
        try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "insert into coach (id, name, nationality) values (?, ?, ?) " +
                        "on conflict (id) do update set " +
                        "name=excluded.name, " +
                        "nationality=excluded.nationality " +
                        "returning id, name, nationality "
        )){
            try {
                statement.setString(1, coach.getId());
                statement.setString(2, coach.getName());
                statement.setString(3, coach.getNationality());
                statement.addBatch();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    coachMapper.apply(resultSet);
                }
            }
        }
    }
}
