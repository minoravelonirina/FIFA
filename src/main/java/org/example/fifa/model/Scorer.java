package org.example.fifa.model;

import org.example.fifa.rest.dto.PlayerScorerDto;

import java.util.Objects;

public class Scorer {
    private String id;
    private PlayerScorerDto player;
    private int minuteOfGoal;
    private boolean ownGoal;

    public Scorer(PlayerScorerDto player, int minuteOfGoal, boolean ownGoal) {
        this.player = player;
        this.minuteOfGoal = minuteOfGoal;
        this.ownGoal = ownGoal;
    }

    public Scorer(String id, PlayerScorerDto player, int minuteOfGoal, boolean ownGoal) {
        this.id = id;
        this.player = player;
        this.minuteOfGoal = minuteOfGoal;
        this.ownGoal = ownGoal;
    }

    public Scorer(){};
    public String getId(){return this.id;};
    public void setId(String id){this.id = id;};
    public PlayerScorerDto getPlayer() {return player;}
    public void setPlayer(PlayerScorerDto player) {this.player = player;}
    public int getMinuteOfGoal() {return minuteOfGoal;}
    public void setMinuteOfGoal(int minuteOfGoal) {this.minuteOfGoal = minuteOfGoal;}
    public boolean isOwnGoal() {return ownGoal;}
    public void setOwnGoal(boolean ownGoal) {this.ownGoal = ownGoal;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scorer scorer = (Scorer) o;
        return minuteOfGoal == scorer.minuteOfGoal && ownGoal == scorer.ownGoal && Objects.equals(id, scorer.id) && Objects.equals(player, scorer.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, player, minuteOfGoal, ownGoal);
    }

    @Override
    public String toString() {
        return "Scorer{" +
                "id='" + id + '\'' +
                ", player=" + player +
                ", minuteOfGoal=" + minuteOfGoal +
                ", ownGoal=" + ownGoal +
                '}';
    }
}
