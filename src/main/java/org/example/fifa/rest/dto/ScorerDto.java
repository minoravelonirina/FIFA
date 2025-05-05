package org.example.fifa.rest.dto;

import org.example.fifa.model.Player;

import java.util.Objects;

public class ScorerDto {
    private PlayerScorerDto player;
    private int minuteOfGoal;
    private boolean ownGoal;

    public ScorerDto(PlayerScorerDto player, int minuteOfGoal, boolean ownGoal) {
        this.player = player;
        this.minuteOfGoal = minuteOfGoal;
        this.ownGoal = ownGoal;
    }

    public ScorerDto(){};

    public PlayerScorerDto getPlayer() {
        return player;
    }

    public void setPlayer(PlayerScorerDto player) {
        this.player = player;
    }

    public int getMinuteOfGoal() {
        return minuteOfGoal;
    }

    public void setMinuteOfGoal(int minuteOfGoal) {
        this.minuteOfGoal = minuteOfGoal;
    }

    public boolean isOwnGoal() {
        return ownGoal;
    }

    public void setOwnGoal(boolean ownGoal) {
        this.ownGoal = ownGoal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScorerDto scorerDto = (ScorerDto) o;
        return minuteOfGoal == scorerDto.minuteOfGoal && ownGoal == scorerDto.ownGoal && Objects.equals(player, scorerDto.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, minuteOfGoal, ownGoal);
    }

    @Override
    public String toString() {
        return "ScorerDto{" +
                "player=" + player +
                ", minuteOfGoal=" + minuteOfGoal +
                ", ownGoal=" + ownGoal +
                '}';
    }
}
