package org.example.fifa.rest.dto;

import org.example.fifa.model.PlayingTime;

import java.util.Objects;

public class PlayerStatisticDto {
    private int scoredGoals;
    private PlayingTime playingTime;

    public PlayerStatisticDto(int scoredGoals, PlayingTime playingTime) {
        this.scoredGoals = scoredGoals;
        this.playingTime = playingTime;
    }

    public PlayerStatisticDto(){};

    public int getScoredGoals() {return scoredGoals;}
    public void setScoredGoals(int scoredGoals) {this.scoredGoals = scoredGoals;}
    public PlayingTime getPlayingTime() {return playingTime;}
    public void setPlayingTime(PlayingTime playingTime) {this.playingTime = playingTime;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerStatisticDto that = (PlayerStatisticDto) o;
        return scoredGoals == that.scoredGoals && Objects.equals(playingTime, that.playingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scoredGoals, playingTime);
    }

    @Override
    public String toString() {
        return "StatisticPlayerDto{" +
                "scoredGoals=" + scoredGoals +
                ", playingTime=" + playingTime +
                '}';
    }
}
