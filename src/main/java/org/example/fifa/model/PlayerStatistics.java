package org.example.fifa.model;

import org.example.fifa.model.enums.DurationUnit;

public class PlayerStatistics {
    private String id;
    private String playerId;
    private String seasonId;
    private int scoredGoals;
    private double playingTimeValue;
    private DurationUnit playingTimeUnit;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public String getSeasonId() { return seasonId; }
    public void setSeasonId(String seasonId) { this.seasonId = seasonId; }

    public int getScoredGoals() { return scoredGoals; }
    public void setScoredGoals(int scoredGoals) { this.scoredGoals = scoredGoals; }

    public double getPlayingTimeValue() { return playingTimeValue; }
    public void setPlayingTimeValue(double playingTimeValue) { this.playingTimeValue = playingTimeValue; }

    public DurationUnit getPlayingTimeUnit() { return playingTimeUnit; }
    public void setPlayingTimeUnit(DurationUnit playingTimeUnit) { this.playingTimeUnit = playingTimeUnit; }
}
