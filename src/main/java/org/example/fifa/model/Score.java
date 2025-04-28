package org.example.fifa.model;

import java.util.Objects;

public class Score {
    private int homeScore;
    private int awayScore;

    public Score(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public Score(){};

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return homeScore == score.homeScore && awayScore == score.awayScore;
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeScore, awayScore);
    }

    @Override
    public String toString() {
        return "Score{" +
                "homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                '}';
    }
}
