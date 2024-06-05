package com.example.rpgtodolist.dto;

public class UserStatsDto {
    int available_pts, strength, agility, intelligence, enemy_id, kill_count;

    public UserStatsDto(int available_pts, int strength, int agility, int intelligence, int enemy_id, int kill_count) {
        this.available_pts = available_pts;
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
        this.enemy_id = enemy_id;
        this.kill_count = kill_count;
    }

    public int getAvailable_pts() {
        return available_pts;
    }

    public void setAvailable_pts(int available_pts) {
        this.available_pts = available_pts;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getEnemy_id() {
        return enemy_id;
    }

    public void setEnemy_id(int enemy_id) {
        this.enemy_id = enemy_id;
    }

    public int getKill_count() {
        return kill_count;
    }

    public void setKill_count(int kill_count) {
        this.kill_count = kill_count;
    }

    @Override
    public String toString() {
        return "{" +
                "\"available_pts\":" + available_pts +
                ",\"strength\":" + strength +
                ",\"agility\":" + agility +
                ",\"intelligence\":" + intelligence +
                ",\"enemy_id\":" + enemy_id +
                ",\"kill_count\":" + kill_count +
                '}';
    }
}

