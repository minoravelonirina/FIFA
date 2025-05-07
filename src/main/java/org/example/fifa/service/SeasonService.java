package org.example.fifa.service;

import org.example.fifa.repository.MatchRepository;
import org.example.fifa.repository.mapper.SeasonMapper;
import org.example.fifa.model.Season;
import org.example.fifa.model.enums.Status;
import org.example.fifa.repository.SeasonRepository;
import org.example.fifa.rest.dto.CreateSeasonDto;
import org.example.fifa.rest.dto.MatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class SeasonService {
    private final SeasonRepository seasonRepository;
    private final SeasonMapper seasonMapper;
    private final MatchRepository matchRepository;

    @Autowired
    public SeasonService(SeasonRepository seasonRepository, SeasonMapper seasonMapper, MatchRepository matchRepository) {
        this.seasonRepository = seasonRepository;
        this.seasonMapper = seasonMapper;
        this.matchRepository = matchRepository;
    }

    public List<Season> findAll() {
        return seasonRepository.findAll();
    }

    public ResponseEntity<Object> createSeasons(List<CreateSeasonDto> createSeasonDtos) {
        if (createSeasonDtos == null || createSeasonDtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Season list cannot be empty");
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
                .toList();

        return ResponseEntity.ok( seasons.stream()
                .map(seasonRepository::save)
                .collect(Collectors.toList()));
    }


    public ResponseEntity<Object> updateStatus(LocalDate year, Status newStatus) throws SQLException {
        try {
            Season season = seasonRepository.findByYear(year.getYear());

            if (season == null) {
                return ResponseEntity.notFound().build();
            }

            if (newStatus == Status.FINISHED) {
                List<MatchDto> matches = matchRepository.findAll(year, null, null, null, null);
                boolean hasUnfinishedMatch = matches.stream()
                        .anyMatch(matchDto -> matchDto.getActualStatus() != Status.FINISHED);

                if (hasUnfinishedMatch) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("All match is not finished for the season");
                }
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
