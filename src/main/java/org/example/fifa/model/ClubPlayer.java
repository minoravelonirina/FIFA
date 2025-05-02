package org.example.fifa.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.fifa.model.enums.Position;

import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClubPlayer extends Player {
    private Club club;

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public ClubPlayer(String id, String name, int number, int age, Position position, String nationality, Club club) {
        super(id, name, number, age, position, nationality);
        this.club = club;
    }

    public ClubPlayer() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        ClubPlayer that = (ClubPlayer) o;
        return Objects.equals(club, that.club);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), club);
    }
}
