package org.example.fifa.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Match {
    private Long id;
    private LocalDateTime date;
    private String stadium;
    private Club home_club;
    private Club away_club;
    private Score score;

    public Match(Long id, LocalDateTime date, String stadium, Club home_club, Club away_club, Score score) {
        this.id = id;
        this.date = date;
        this.stadium = stadium;
        this.home_club = home_club;
        this.away_club = away_club;
        this.score = score;
    }

    public Match(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public Club getHome_club() {
        return home_club;
    }

    public void setHome_club(Club home_club) {
        this.home_club = home_club;
    }

    public Club getAway_club() {
        return away_club;
    }

    public void setAway_club(Club away_club) {
        this.away_club = away_club;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(id, match.id) && Objects.equals(date, match.date) && Objects.equals(stadium, match.stadium) && Objects.equals(home_club, match.home_club) && Objects.equals(away_club, match.away_club) && Objects.equals(score, match.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, stadium, home_club, away_club, score);
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", date=" + date +
                ", stadium='" + stadium + '\'' +
                ", home_club=" + home_club +
                ", away_club=" + away_club +
                ", scores=" + score +
                '}';
    }
}
