package org.example.fifa.repository.mapper;

import org.example.fifa.model.Season;
import org.example.fifa.model.enums.Status;
import org.example.fifa.rest.dto.CreateSeasonDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SeasonMapper {
    public Season toEntity(CreateSeasonDto dto) {
        Season season = new Season();
        season.setId(UUID.randomUUID().toString());
        season.setYear(dto.getYear());
        season.setAlias(generateAlias(dto.getYear()));
        season.setStatus(Status.NOT_STARTED);
        return season;
    }

    private String generateAlias(int year) {
        return String.format("S%d-%d", year, year + 1);
    }
}
