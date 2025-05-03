package org.example.fifa.rest.dto;

import org.example.fifa.model.enums.Status;

public class UpdateSeasonStatusDto {
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}