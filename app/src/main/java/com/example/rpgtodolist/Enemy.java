package com.example.rpgtodolist;

public class Enemy {
    int enemyId, startHP;
    String name;

    public Enemy(int enemyId, String name, int startHP) {
        this.enemyId = enemyId;
        this.startHP = startHP;
        this.name = name;
    }

    public int getEnemyId() {
        return enemyId;
    }

    public void setEnemyId(int enemyId) {
        this.enemyId = enemyId;
    }

    public int getStartHP() {
        return startHP;
    }

    public void setStartHP(int startHP) {
        this.startHP = startHP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Enemy{" +
                "enemyId=" + enemyId +
                ", startHP=" + startHP +
                ", name='" + name + '\'' +
                '}';
    }
}
