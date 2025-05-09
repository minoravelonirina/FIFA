package org.example.fifa.repository;

import org.example.fifa.model.Transfer;
import org.example.fifa.model.enums.TransferType;
import org.example.fifa.repository.mapper.TransferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
@Repository
public class TransferRepository {
    private final DataSource dataSource;
    private final TransferMapper transferMapper;

    @Autowired
    public TransferRepository(DataSource dataSource, TransferMapper transferMapper) {
        this.dataSource = dataSource;
        this.transferMapper = transferMapper;
    }

    public List<Transfer> findAll() {
        List<Transfer> transfers = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM transfer ORDER BY transfer_date DESC");
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                transfers.add(transferMapper.apply(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all transfers", e);
        }

        return transfers;
    }

    public Transfer save(Transfer transfer) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO transfer (id, player_id, club_id, type, transfer_date) VALUES (?, ?, ?, ?, ?) returning id, player_id, club_id, type, transfer_date")) {

            statement.setObject(1, transfer.getId());
            statement.setString(2, transfer.getPlayerId());
            statement.setString(3, transfer.getClubId());
            statement.setString(4, transfer.getType().name());
            statement.setTimestamp(5, Timestamp.valueOf(transfer.getTransferDate()));
            statement.addBatch();

            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return transferMapper.apply(resultSet);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving transfer", e);
        }
    }


    public void registerTransfer(String playerId, String clubId, TransferType type) {
        Transfer transfer = new Transfer();
        transfer.setId(UUID.randomUUID());
        transfer.setPlayerId(playerId);
        transfer.setClubId(clubId);
        transfer.setType(type);
        transfer.setTransferDate(LocalDateTime.now());

        save(transfer);
    }
}
