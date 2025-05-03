package org.example.fifa.repository;

import org.example.fifa.model.Season;
import org.example.fifa.model.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SeasonRepository {
    private final DataSource dataSource;

    @Autowired
    public SeasonRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Season> findAll() {
        List<Season> seasons = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM season ORDER BY year")) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    seasons.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all seasons", e);
        }
        return seasons;
    }

    public Season save(Season season) {
        Season existingSeason = findByYear(season.getYear());
        try (Connection connection = dataSource.getConnection()) {
            if (existingSeason == null) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO season (id, year, alias, status) VALUES (?, ?, ?, ?::status)")) {
                    statement.setString(1, season.getId());
                    statement.setInt(2, season.getYear());
                    statement.setString(3, season.getAlias());
                    statement.setString(4, season.getStatus().toString());
                    statement.executeUpdate();
                }
            } else {
                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE season SET alias = ?, status = ?::status WHERE year = ?")) {
                    statement.setString(1, season.getAlias());
                    statement.setString(2, season.getStatus().toString());
                    statement.setInt(3, season.getYear());
                    statement.executeUpdate();
                }
            }
            return findByYear(season.getYear());
        } catch (SQLException e) {
            throw new RuntimeException("Error saving season: " + e.getMessage(), e);
        }
    }

    public Season findByYear(Integer year) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM season WHERE year = ? OR id = ?")) {

            statement.setInt(1, year);
            statement.setString(2, "S" + year + "-" + (year + 1));

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding season by year", e);
        }
        return null;
    }

    private Season mapRow(ResultSet rs) throws SQLException {
        return new Season(
                rs.getString("id"),
                Status.valueOf(rs.getString("status")),
                rs.getInt("year"),
                rs.getString("alias")
        );
    }
}
