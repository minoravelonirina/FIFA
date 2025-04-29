package org.example.fifa.model;

import java.util.List;
import java.util.Objects;

public class Championship {
    private String id;
    private String period;
    List<Match> matchList;

    public Championship(String id, String period, List<Match> matchList) {
        this.id = id;
        this.period = period;
        this.matchList = matchList;
    }

    public Championship(){};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<Match> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<Match> matchList) {
        this.matchList = matchList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Championship that = (Championship) o;
        return Objects.equals(id, that.id) && Objects.equals(period, that.period) && Objects.equals(matchList, that.matchList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, period, matchList);
    }

    @Override
    public String toString() {
        return "Championship{" +
                "id=" + id +
                ", period='" + period + '\'' +
                ", matchList=" + matchList +
                '}';
    }
}
