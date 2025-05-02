package org.example.fifa.rest.dto;

import org.example.fifa.model.Club;
import org.example.fifa.model.enums.Status;

import java.time.LocalDateTime;
import java.util.Objects;

public class MatchDto {
    private String id;
    private LocalDateTime matchDatetime;
    private String stadium;
    private ClubWithGoalsDto clubPlayingHome;
    private ClubWithGoalsDto clubPlayingAway;
    private Status actualStatus;

    public MatchDto(String id, LocalDateTime matchDatetime, String stadium, ClubWithGoalsDto clubPlayingHome, ClubWithGoalsDto clubPlayingAway, Status actualStatus) {
        this.id = id;
        this.matchDatetime = matchDatetime;
        this.stadium = stadium;
        this.clubPlayingHome = clubPlayingHome;
        this.clubPlayingAway = clubPlayingAway;
        this.actualStatus = actualStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getMatchDatetime() {
        return matchDatetime;
    }

    public void setMatchDatetime(LocalDateTime matchDatetime) {
        this.matchDatetime = matchDatetime;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public ClubWithGoalsDto getClubPlayingHome() {
        return clubPlayingHome;
    }

    public void setClubPlayingHome(ClubWithGoalsDto clubPlayingHome) {
        this.clubPlayingHome = clubPlayingHome;
    }

    public ClubWithGoalsDto getClubPlayingAway() {
        return clubPlayingAway;
    }

    public void setClubPlayingAway(ClubWithGoalsDto clubPlayingAway) {
        this.clubPlayingAway = clubPlayingAway;
    }

    public Status getActualStatus() {
        return actualStatus;
    }

    public void setActualStatus(Status actualStatus) {
        this.actualStatus = actualStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchDto matchDto = (MatchDto) o;
        return Objects.equals(id, matchDto.id) && Objects.equals(matchDatetime, matchDto.matchDatetime) && Objects.equals(stadium, matchDto.stadium) && Objects.equals(clubPlayingHome, matchDto.clubPlayingHome) && Objects.equals(clubPlayingAway, matchDto.clubPlayingAway) && actualStatus == matchDto.actualStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matchDatetime, stadium, clubPlayingHome, clubPlayingAway, actualStatus);
    }

    @Override
    public String toString() {
        return "MatchDto{" +
                "id='" + id + '\'' +
                ", matchDatetime=" + matchDatetime +
                ", stadium='" + stadium + '\'' +
                ", clubPlayingHome=" + clubPlayingHome +
                ", clubPlayingAway=" + clubPlayingAway +
                ", actualStatus=" + actualStatus +
                '}';
    }
}
