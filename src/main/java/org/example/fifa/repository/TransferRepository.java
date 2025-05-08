package org.example.fifa.repository;

import org.example.fifa.model.Transfer;
import org.example.fifa.model.enums.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransferRepository {
    private final DataSource dataSource;

    @Autowired
    public TransferRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Transfer> findAll() {
        List<Transfer> transfers = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM transfer ORDER BY transfer_date DESC");
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                transfers.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all transfers", e);
        }

        return transfers;
    }

    public Transfer save(Transfer transfer) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO transfer (id, player_id, club_id, type, transfer_date) VALUES (?, ?, ?, ?, ?)")) {

            statement.setString(1, transfer.getId());
            statement.setString(2, transfer.getPlayerId());
            statement.setString(3, transfer.getClubId());
            statement.setString(4, transfer.getType().name());
            statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.parse(transfer.getTransferDate())));
            statement.executeUpdate();

            return transfer;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving transfer", e);
        }
    }

    private Transfer mapRow(ResultSet rs) throws SQLException {
        String typeStr = rs.getString("type");
        TransferType type;

        try {
            type = TransferType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SQLException("Invalid transfer type value in DB: " + typeStr, e);
        }

        return new Transfer(
                rs.getString("id"),
                rs.getString("player_id"),
                rs.getString("club_id"),
                type,
                rs.getTimestamp("transfer_date").toLocalDateTime().toString()
        );
    }


}
