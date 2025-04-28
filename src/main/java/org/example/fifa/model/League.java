package org.example.fifa.model;

import java.util.List;
import java.util.Objects;

public class League {
    private Long id;
    private String leagueNam;
    private List<Club> clubs;
    private String country;

    public League(Long id, String leagueNam, List<Club> clubs, String country) {
        this.id = id;
        this.leagueNam = leagueNam;
        this.clubs = clubs;
        this.country = country;
    }

    public League(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeagueNam() {
        return leagueNam;
    }

    public void setLeagueNam(String leagueNam) {
        this.leagueNam = leagueNam;
    }

    public List<Club> getClubs() {
        return clubs;
    }

    public void setClubs(List<Club> clubs) {
        this.clubs = clubs;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        League league = (League) o;
        return Objects.equals(id, league.id) && Objects.equals(leagueNam, league.leagueNam) && Objects.equals(clubs, league.clubs) && Objects.equals(country, league.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, leagueNam, clubs, country);
    }

    @Override
    public String toString() {
        return "League{" +
                "id=" + id +
                ", leagueNam='" + leagueNam + '\'' +
                ", clubs=" + clubs +
                ", country='" + country + '\'' +
                '}';
    }
}
