package org.example.fifa.repository.mapper;

import org.example.fifa.model.Transfer;
import org.example.fifa.model.enums.TransferType;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Function;

@Component
public class TransferMapper implements Function<ResultSet, Transfer> {
    @Override
    public Transfer apply(ResultSet rs) {

        try {
            return new Transfer(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("player_id"),
                    rs.getString("club_id"),
                    TransferType.valueOf(rs.getString("type")),
                    rs.getTimestamp("transfer_date").toLocalDateTime()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
