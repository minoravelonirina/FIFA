package org.example.fifa.model;

import java.util.Objects;

public class RequestGoal {
    private String clubId;
    private String scorerIdentifier;
    private int minuteOfGoal;

    public RequestGoal(String clubId, String scorerIdentifier, int minuteOfGoal) {
        this.clubId = clubId;
        this.scorerIdentifier = scorerIdentifier;
        this.minuteOfGoal = minuteOfGoal;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getScorerIdentifier() {
        return scorerIdentifier;
    }

    public void setScorerIdentifier(String scorerIdentifier) {
        this.scorerIdentifier = scorerIdentifier;
    }

    public int getMinuteOfGoal() {
        return minuteOfGoal;
    }

    public void setMinuteOfGoal(int minuteOfGoal) {
        this.minuteOfGoal = minuteOfGoal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestGoal that = (RequestGoal) o;
        return minuteOfGoal == that.minuteOfGoal && Objects.equals(clubId, that.clubId) && Objects.equals(scorerIdentifier, that.scorerIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clubId, scorerIdentifier, minuteOfGoal);
    }

    @Override
    public String toString() {
        return "RequestGoal{" +
                "clubId='" + clubId + '\'' +
                ", scorerIdentifier='" + scorerIdentifier + '\'' +
                ", minuteOfGoal=" + minuteOfGoal +
                '}';
    }
}
