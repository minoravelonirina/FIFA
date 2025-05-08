package org.example.fifa.model;


import org.example.fifa.model.enums.TransferType;

public class Transfer {
    private String id;
    private String playerId;
    private String clubId;
    private TransferType type;
    private String transferDate;

    public Transfer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public TransferType getType() {
        return type;
    }

    public void setType(TransferType type) {
        this.type = type;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public Transfer(String id, String playerId, String clubId, TransferType type, String transferDate) {
        this.id = id;
        this.playerId = playerId;
        this.clubId = clubId;
        this.type = type;
        this.transferDate = transferDate;
    }
}
