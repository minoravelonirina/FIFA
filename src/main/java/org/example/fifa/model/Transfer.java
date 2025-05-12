package org.example.fifa.model;


import org.example.fifa.model.enums.TransferType;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transfer {
    private UUID id;
    private String playerId;
    private String clubId;
    private TransferType type;
    private LocalDateTime transferDate;

    public Transfer(UUID id, String playerId, String clubId, TransferType type, LocalDateTime transferDate) {
        this.id = id;
        this.playerId = playerId;
        this.clubId = clubId;
        this.type = type;
        this.transferDate = transferDate;
    }

    public Transfer() {}

    public UUID getId() {return id;}
    public void setId(UUID id) {this.id = id;}
    public String getPlayerId() {return playerId;}
    public void setPlayerId(String playerId) {this.playerId = playerId;}
    public String getClubId() {return clubId;}
    public void setClubId(String clubId) {this.clubId = clubId;}
    public TransferType getType() {return type;}
    public void setType(TransferType type) {this.type = type;}
    public LocalDateTime getTransferDate() {return transferDate;}
    public void setTransferDate(LocalDateTime transferDate) {this.transferDate = transferDate;}
}
