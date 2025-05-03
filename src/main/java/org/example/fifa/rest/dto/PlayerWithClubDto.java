/*package org.example.fifa.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.fifa.model.Club;
import org.example.fifa.model.enums.Position;
import org.example.fifa.rest.dto.ClubDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerWithClubDto {
    private String id;
    private ClubDto club;
    private Position position;
    private String nationality;
    private int age;
    private String name;
    private int number;

    public PlayerWithClubDto(String id, Club club, Position position, String nationality, int age, String name, int number) {
        this.id = id;
        this.club = club != null ? new ClubDto(club) : null;
        this.position = position;
        this.nationality = nationality;
        this.age = age;
        this.name = name;
        this.number = number;
    }
}*/

package org.example.fifa.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.fifa.model.Club;
import org.example.fifa.model.enums.Position;
import org.example.fifa.rest.dto.ClubDto;

@Data
@NoArgsConstructor
public class PlayerWithClubDto {
    private String id;
    private Club club;
    private Position position;
    private String nationality;
    private int age;
    private String name;
    private int number;

    public PlayerWithClubDto(String id, Club club, Position position, String nationality, int age, String name, int number) {
        this.id = id;
        this.club = club;
        this.position = position;
        this.nationality = nationality;
        this.age = age;
        this.name = name;
        this.number = number;
    }

    public String getId() { return id; }
    public Club getClub() { return club; }
    public Position getPosition() { return position; }
    public String getNationality() { return nationality; }
    public int getAge() { return age; }
    public String getName() { return name; }
    public int getNumber() { return number; }
}
