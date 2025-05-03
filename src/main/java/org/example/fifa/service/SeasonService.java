package org.example.fifa.service;

import org.example.fifa.repository.mapper.SeasonMapper;
import org.example.fifa.model.Season;
import org.example.fifa.model.enums.Status;
import org.example.fifa.repository.SeasonRepository;
import org.example.fifa.rest.dto.CreateSeasonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeasonService {
    private final SeasonRepository seasonRepository;
    private final SeasonMapper seasonMapper;

    @Autowired
    public SeasonService(SeasonRepository seasonRepository, SeasonMapper seasonMapper) {
        this.seasonRepository = seasonRepository;
        this.seasonMapper = seasonMapper;
    }

    public List<Season> findAll() {
        return seasonRepository.findAll();
    }

    public List<Season> createSeasons(List<CreateSeasonDto> createSeasonDtos) {
        if (createSeasonDtos == null || createSeasonDtos.isEmpty()) {
            throw new IllegalArgumentException("Season list cannot be empty");
        }

        createSeasonDtos.forEach(dto -> {
            if (dto.getYear() < 2000) {
                throw new IllegalArgumentException("Year must be greater than 2000");
            }
            if (seasonRepository.findByYear(dto.getYear()) != null) {
                throw new IllegalStateException("Season already exists for year: " + dto.getYear());
            }
        });

        List<Season> seasons = createSeasonDtos.stream()
                .map(seasonMapper::toEntity)
                .collect(Collectors.toList());

        return seasons.stream()
                .map(seasonRepository::save)
                .collect(Collectors.toList());
    }

    public ResponseEntity<Season> updateStatus(Integer year, Status newStatus) {
        try {
            Season season = seasonRepository.findByYear(year);
            if (season == null) {
                return ResponseEntity.notFound().build();
            }

            validateStatusTransition(season.getStatus(), newStatus);
            season.setStatus(newStatus);
            Season updatedSeason = seasonRepository.save(season);
            return ResponseEntity.ok(updatedSeason);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private void validateStatusTransition(Status currentStatus, Status newStatus) {
        if (currentStatus == Status.FINISHED ||
                (currentStatus == Status.STARTED && newStatus == Status.NOT_STARTED) ||
                (currentStatus == Status.NOT_STARTED && newStatus == Status.FINISHED)) {
            throw new IllegalStateException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
    }
}
