package org.example.fifa.model;

import org.example.fifa.model.enums.Status;

import java.time.LocalDateTime;
import java.util.Objects;

public class Match {
    private String id;
    private LocalDateTime matchDatetime;
    private String stadium;
    private Club clubPlayingHome;
    private Club clubPlayingAway;
    private Status actualStatus;

    public Match(){};

    public Match(String id, LocalDateTime matchDatetime, String stadium, Club clubPlayingHome, Club clubPlayingAway, Status actualStatus) {
        this.id = id;
        this.matchDatetime = matchDatetime;
        this.stadium = stadium;
        this.clubPlayingHome = clubPlayingHome;
        this.clubPlayingAway = clubPlayingAway;
        this.actualStatus = actualStatus;
    }

    public Status getActualStatus() {return actualStatus;}
    public void setActualStatus(Status actualStatus) {this.actualStatus = actualStatus;}
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public LocalDateTime getMatchDatetime() {return matchDatetime;}
    public void setMatchDatetime(LocalDateTime matchDatetime) {this.matchDatetime = matchDatetime;}
    public String getStadium() {return stadium;}
    public void setStadium(String stadium) {this.stadium = stadium;}
    public Club getClubPlayingHome() {return clubPlayingHome;}
    public void setClubPlayingHome(Club clubPlayingHome) {this.clubPlayingHome = clubPlayingHome;}
    public Club getClubPlayingAway() {return clubPlayingAway;}
    public void setClubPlayingAway(Club clubPlayingAway) {this.clubPlayingAway = clubPlayingAway;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(id, match.id) && Objects.equals(matchDatetime, match.matchDatetime) && Objects.equals(stadium, match.stadium) && Objects.equals(clubPlayingHome, match.clubPlayingHome) && Objects.equals(clubPlayingAway, match.clubPlayingAway) && Objects.equals(actualStatus, match.actualStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matchDatetime, stadium, clubPlayingHome, clubPlayingAway, actualStatus);
    }

    @Override
    public String toString() {
        return "Match{" +
                "id='" + id + '\'' +
                ", matchDatetime=" + matchDatetime +
                ", stadium='" + stadium + '\'' +
                ", clubPlayingHome=" + clubPlayingHome +
                ", clubPlayingAway=" + clubPlayingAway +
                ", actualStatus=" + actualStatus +
                '}';
    }
}
