package org.example.fifa.model;

import org.example.fifa.model.enums.DurationUnit;

import java.util.Objects;

public class PlayingTime {
    private int value;
    private DurationUnit durationUnit;

    public PlayingTime(int value, DurationUnit durationUnit) {
        this.value = value;
        this.durationUnit = durationUnit;
    }

    public PlayingTime(){};

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public DurationUnit getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(DurationUnit durationUnit) {
        this.durationUnit = durationUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayingTime that = (PlayingTime) o;
        return value == that.value && durationUnit == that.durationUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, durationUnit);
    }

    @Override
    public String toString() {
        return "PlayingTime{" +
                "value=" + value +
                ", durationUnit=" + durationUnit +
                '}';
    }
}
