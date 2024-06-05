package com.example.rpgtodolist;

public class User {

    int availablePts, str, agility, intl, enemyId, enemyHP, killCount;
    long hitTime;
    private String name;
    private int value;

    public User(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public User(int availablePts, int str, int agility, int intl, int enemyId, int enemyHP, int killCount, long hitTime) {
        this.availablePts = availablePts;
        this.str = str;
        this.agility = agility;
        this.intl = intl;
        this.enemyId = enemyId;
        this.enemyHP = enemyHP;
        this.killCount = killCount;
        this.hitTime = hitTime;
    }

    public int getAvailablePts() {
        return availablePts;
    }

    public void setAvailablePts(int availablePts) {
        this.availablePts = availablePts;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getIntl() {
        return intl;
    }

    public void setIntl(int intl) {
        this.intl = intl;
    }

    public int getEnemyId() {
        return enemyId;
    }

    public void setEnemyId(int enemyId) {
        this.enemyId = enemyId;
    }

    public int getEnemyHP() {
        return enemyHP;
    }

    public void setEnemyHP(int enemyHP) {
        this.enemyHP = enemyHP;
    }

    public int getKillCount() {
        return killCount;
    }

    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    public long getHitTime() {
        return hitTime;
    }

    public void setHitTime(long hitTime) {
        this.hitTime = hitTime;
    }

    @Override
    public String toString() {
        return "toString dziala: User{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
