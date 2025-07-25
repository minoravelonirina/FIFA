package org.example.fifa.service;

import org.example.fifa.model.Transfer;
import org.example.fifa.model.enums.TransferType;
import org.example.fifa.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransferService {
    private final TransferRepository transferRepository;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }
}
