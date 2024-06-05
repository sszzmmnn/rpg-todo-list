package com.example.rpgtodolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TodosDBHelper extends SQLiteOpenHelper {

    public TodosDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public TodosDBHelper(@Nullable Context context) {
        super(context, "todos.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE todos(id integer primary key autoincrement, title text not null, type integer not null, creation_time integer, done integer, over integer, points integer)");
        db.execSQL("CREATE TABLE user(available_pts numeric not null, str numeric not null, agility numeric not null, intl numeric not null, enemy_id integer not null, enemy_hp numeric not null, kill_count numeric not null, timer numeric not null)");
        db.execSQL("CREATE TABLE enemies(enemy_id integer primary key autoincrement, name text not null, start_hp numeric not null)");
        db.execSQL("CREATE TABLE api(name text)");
    }

    public boolean insertUser(User user) {
        ContentValues cv = new ContentValues();

        cv.put("available_pts", user.getAvailablePts());
        cv.put("str", user.getStr());
        cv.put("agility", user.getAgility());
        cv.put("intl", user.getIntl());
        cv.put("enemy_id", user.getEnemyId());
        cv.put("enemy_hp", getEnemyInfo(user.getEnemyId()).getStartHP());
        cv.put("kill_count", user.getKillCount());
        cv.put("timer", user.getHitTime());

        try {
            SQLiteDatabase db = getWritableDatabase();
            db.insertOrThrow("user", null, cv);

            System.out.println("user_insert");
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(User user) {
        ContentValues cv = new ContentValues();

        cv.put("available_pts", user.getAvailablePts());
        cv.put("str", user.getStr());
        cv.put("agility", user.getAgility());
        cv.put("intl", user.getIntl());
        cv.put("enemy_id", user.getEnemyId());
        cv.put("enemy_hp", user.getEnemyHP());
        cv.put("kill_count", user.getKillCount());
        cv.put("timer", user.getHitTime());

        try {
            SQLiteDatabase db = getWritableDatabase();
            db.update("user", cv, null, null);

            System.out.println("user_update");
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int insertEnemies() {
        List<Enemy> enemyList = new ArrayList<>();
        enemyList.add(new Enemy(1, "Darkwing", 150));
        enemyList.add(new Enemy(1, "Crimson Bat", 200));
        enemyList.add(new Enemy(1, "Goldenfang", 250));

        enemyList.add(new Enemy(1, "Desert Strike", 300));
        enemyList.add(new Enemy(1, "Moonstrike", 350));
        enemyList.add(new Enemy(1, "Scarletfang", 400));

        enemyList.add(new Enemy(1, "Grayshade", 400));
        enemyList.add(new Enemy(1, "Frostwhisper", 450));
        enemyList.add(new Enemy(1, "Shadowflare", 500));

        enemyList.add(new Enemy(1, "Ironclad", 250));
        enemyList.add(new Enemy(1, "Crimsonblade", 300));
        enemyList.add(new Enemy(1, "Shadowguard", 350));

        enemyList.add(new Enemy(1, "Verdant Scale", 200));
        enemyList.add(new Enemy(1, "Ambercrest", 250));
        enemyList.add(new Enemy(1, "Shadowtail", 300));

        enemyList.add(new Enemy(1, "Crimson Stinger", 500));
        enemyList.add(new Enemy(1, "Azure Claw", 550));
        enemyList.add(new Enemy(1, "Shadowpincer", 600));

        enemyList.add(new Enemy(1, "Violet Reaper", 350));
        enemyList.add(new Enemy(1, "Shadow Specter", 400));
        enemyList.add(new Enemy(1, "Golden Wraith", 450));

        enemyList.add(new Enemy(1, "Abyssal Trigore", 450));
        enemyList.add(new Enemy(1, "Crimson Hellion", 500));
        enemyList.add(new Enemy(1, "Frostfire Fiend", 550));

        enemyList.add(new Enemy(1, "Timberstraw", 150));
        enemyList.add(new Enemy(1, "Mossbloom", 100));
        enemyList.add(new Enemy(1, "Gilded Sentinel", 200));

        enemyList.add(new Enemy(1, "Gilded Emberchest", 550));
        enemyList.add(new Enemy(1, "Obsidian Ironmaw", 600));
        enemyList.add(new Enemy(1, "Frostbite Azurecoffer", 650));

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        try {
            for(Enemy enemy:enemyList) {
                cv.put("name", enemy.getName());
                cv.put("start_hp", enemy.getStartHP());
                db.insertOrThrow("enemies", null, cv);
                System.out.println("todos_insert: " + enemy.getName());
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enemyList.get(new Random().nextInt(30)+1).getStartHP();
    }

    public Enemy getEnemyInfo(int id) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("enemies", null, "enemy_id = ?", new String[] {String.valueOf(id)}, null, null, null);

        if(cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return null;
        }

        cursor.moveToNext();
        Enemy enemy = new Enemy(id, cursor.getString(1), cursor.getInt(2));

        cursor.close();
        db.close();
        return enemy;
    }

    public Pair<Enemy, User> getDisplayInfo() {

        Pair<Enemy, User> res;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("user", null, null, null, null, null, null);
        cursor.moveToNext();
        User user = new User(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getLong(7));

        cursor = db.query("enemies", null, "enemy_id = ?", new String[] {String.valueOf(user.getEnemyId())}, null, null, null);
        cursor.moveToNext();
        Enemy enemy = new Enemy(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));

        res = new Pair<>(enemy, user);

        cursor.close();
        db.close();
        return res;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS todos");
        onCreate(db);
    }

    public boolean addTodo(Todo todo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title", todo.getTitle());
        cv.put("type", todo.getType());
        cv.put("creation_time", todo.getCreation_time());
        cv.put("done", todo.isDone());
        cv.put("over", todo.isOver());
        cv.put("points", todo.getPoints());

        try {
            db.insertOrThrow("todos", null, cv);
            System.out.println("todos_insert");
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeTodo(int id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete("todos", "id=?", new String[]{Integer.toString(id)}) > 0;
    }

    public List<Todo> getAllTodos(){
        List<Todo> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("todos", null, null,null, null, null, "creation_time");

        while (cursor.moveToNext()){
            Todo todo = new Todo(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4) > 0, cursor.getInt(5) > 0, cursor.getInt(6));
            lista.add(todo);
        }

        cursor.close();
        db.close();

        return lista;
    }

    public List<Todo> getOverTodos(boolean areOver) {
        List<Todo> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor =  db.query("todos", null, "over = ?", new String[] {areOver ? "1" : "0"}, null, null, "creation_time" + (areOver ? " DESC" : ""));

        while (cursor.moveToNext()){
            Todo todo = new Todo(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4) > 0, cursor.getInt(5) > 0, cursor.getInt(6));
            lista.add(todo);
        }

        cursor.close();
        db.close();

        return lista;
    }

    public boolean setOver(int id, boolean over) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("over", over ? 1 : 0);

        int i = db.update("todos",cv,"id = ?", new String[]{String.valueOf(id)});

        db.close();

        if(i>0){
            return true;
        }

        return false;
    }

    public boolean setDone(int id, boolean done) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("done", done ? 1 : 0);

        int i = db.update("todos",cv,"id = ?", new String[]{String.valueOf(id)});

        db.close();

        if(i>0){
            return true;
        }

        return false;
    }

    public boolean setCreationTime(int id, long creation_time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("creation_time", creation_time);

        int i = db.update("todos",cv,"id = ?", new String[]{String.valueOf(id)});

        db.close();

        if(i>0){
            return true;
        }

        return false;
    }

    public User getUser() {
        User res = null;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("user", null, null,null, null, null, null);

        cursor.moveToNext();
        res = new User(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getLong(7));

        db.close();
        return res;
    }

    public boolean updatePoints(int points) {
        User user = getUser();
        user.setAvailablePts(user.getAvailablePts()+points);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("available_pts", user.getAvailablePts());
        System.out.println("updateeeeeeeeeeeeeeeeeeeeee");

        int i = db.update("user",cv,null, null);

        db.close();

        if(i>0){
            return true;
        }

        return false;
    }

    public int[] getStats() {
        int[] res;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("user", new String[]{"str", "agility", "intl"}, null,null, null, null, null);

        cursor.moveToNext();
        res = new int[]{cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)};

        db.close();
        return res;
    }

    public boolean updateStats(int[] pts) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("available_pts", pts[0]);
        cv.put("str", pts[1]);
        cv.put("agility", pts[2]);
        cv.put("intl", pts[3]);

        int i = db.update("user", cv,null, null);
        db.close();

        if(i>0){
            return true;
        } else {
            db.insertOrThrow("todos", null, cv);
            System.out.println("todos_insert");
            db.close();
        }

        return false;
    }

    public String getAPIName() {
        String res;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("api", null, null,null, null, null, null);

        cursor.moveToNext();
        if(cursor.getCount() == 0) { return null; }
        res = cursor.getString(0);

        db.close();

        return res;
    }

    public boolean setAPIName(String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);

        int i = db.update("api", cv,null, null);

        if(i>0){
            return true;
        } else {
            try {
                db.insertOrThrow("api", null, cv);
                System.out.println("api_insert");
                db.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(db.isOpen()) { db.close(); }

        return false;
    }
}
