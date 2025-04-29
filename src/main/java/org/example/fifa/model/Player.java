package org.example.fifa.model;

import org.example.fifa.model.enums.Position;

import java.util.Objects;

public class Player {
    private String id;
    private String name;
    private int number;
    private int age;
    private Position position;
    private String nationality;

    public Player(String id, String name, int number, int age, Position position, String nationality) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.age = age;
        this.position = position;
        this.nationality = nationality;
    }

    public Player(){};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return number == player.number && age == player.age && Objects.equals(id, player.id) && Objects.equals(name, player.name) && position == player.position && Objects.equals(nationality, player.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, number, age, position, nationality);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", playerName='" + name + '\'' +
                ", number=" + number +
                ", age=" + age +
                ", position=" + position +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
