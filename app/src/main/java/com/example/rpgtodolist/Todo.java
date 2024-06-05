package com.example.rpgtodolist;

public class Todo {

    int id;
    String title;
    int type;
    long creation_time;
    boolean done;
    boolean over;
    int points;


    public Todo(int id, String title, int type, long creation_time, boolean done, boolean over, int points) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.creation_time = creation_time;
        this.done = done;
        this.over = over;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(long creation_time) {
        this.creation_time = creation_time;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", creation_time=" + creation_time +
                ", done=" + done +
                ", over=" + over +
                '}';
    }
}
