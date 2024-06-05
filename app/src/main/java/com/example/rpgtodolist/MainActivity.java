package com.example.rpgtodolist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rpgtodolist.fragments.PointsFragment;
import com.example.rpgtodolist.fragments.TodoListFragment;
import com.example.rpgtodolist.fragments.UserCompareFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    API api;
    public String currentFragmentTag;
    TodosDBHelper todosDBHelper;
    StatsViewModel statsViewModel;
    CountDownTimer hitTimer;
    User user;
    ImageView imageView;
    ProgressBar progressBar;
    TextView enemyTextView, enemyHPTextView, hitTimeTextView, strInfoTextView, agilityInfoTextView, intlInfoTextView;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;
    @IdRes
    int selectedMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        api = new API();
        todosDBHelper = new TodosDBHelper(MainActivity.this);

        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        enemyTextView = findViewById(R.id.enemyTextView);
        enemyHPTextView = findViewById(R.id.enemyHPTextView);
        hitTimeTextView = findViewById(R.id.hitTimeTextView);
        strInfoTextView = findViewById(R.id.strInfoTextView);
        agilityInfoTextView = findViewById(R.id.agilityInfoTextView);
        intlInfoTextView = findViewById(R.id.intlInfoTextView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        AsyncTask.execute(() -> {
//            user = api.getUser();

            runOnUiThread(() -> {
            });
        });

        if(!getApplicationContext().getDatabasePath("todos.db").exists()) {
            int hp = todosDBHelper.insertEnemies();
            while(hp == 0) { try {Thread.sleep(100); } catch(InterruptedException e) {} }

            User newUser = new User(99999, 100, 200, 150, new Random().nextInt(30) + 1, hp, 0, (System.currentTimeMillis()/1000)+150);
            boolean res2 = todosDBHelper.insertUser(newUser);
            while(!res2) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        setDisplayInfo();

        currentFragmentTag = "ACTIVE_TODOS";
        replace(new TodoListFragment(todosDBHelper.getOverTodos(false)), currentFragmentTag);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            TodoListFragment fr;
            PointsFragment pfr;

            switch (item.getItemId()) {
                case R.id.miHome:
                    fr = (TodoListFragment) getSupportFragmentManager().findFragmentByTag("ACTIVE_TODOS");
                    if (fr == null || currentFragmentTag != "ACTIVE_TODOS") {
                        user = todosDBHelper.getUser();

                        currentFragmentTag = "ACTIVE_TODOS";
                        replace(new TodoListFragment(todosDBHelper.getOverTodos(false)), currentFragmentTag);
                        selectedMI = item.getItemId();
                    }
                    break;

                case R.id.miHidden:
                    fr = (TodoListFragment) getSupportFragmentManager().findFragmentByTag("OVER_TODOS");
                    if (fr == null || currentFragmentTag != "OVER_TODOS") {
                        user = todosDBHelper.getUser();

                        currentFragmentTag = "OVER_TODOS";
                        replace(new TodoListFragment(todosDBHelper.getOverTodos(true)), currentFragmentTag);
                        selectedMI = item.getItemId();
                    }
                    break;

                case R.id.miStats:
                    pfr = (PointsFragment) getSupportFragmentManager().findFragmentByTag("STATS");
                    if (pfr == null || currentFragmentTag != "STATS") {
                        user = todosDBHelper.getUser();

                        PointsFragment pf = new PointsFragment();

                        Bundle b = new Bundle();
                        user = todosDBHelper.getUser();
                        b.putInt("pts", user.getAvailablePts());
                        b.putInt("str", user.getStr());
                        b.putInt("agility", user.getAgility());
                        b.putInt("intl", user.getIntl());
                        pf.setArguments(b);
                        currentFragmentTag = "STATS";
                        replace(pf, currentFragmentTag);
                        selectedMI = item.getItemId();
                    } else if(currentFragmentTag == "STATS") {
                        UserCompareFragment ucf = new UserCompareFragment();

                        currentFragmentTag = "USER_COMPARE";
                        replace(ucf, currentFragmentTag);
                    }
                    break;

                case R.id.miHideDone:
                    user = todosDBHelper.getUser();

                    showConfirmDialog();
                    break;

                case R.id.placeholder:
                    bottomNavigationView.setSelectedItemId(selectedMI);
                    break;
            }


            return true;
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTodoItemActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        if(hitTimer != null) {
            hitTimer.cancel();
        }

        hitTimer = runHitTimer();
        hitTimer.start();

        statsViewModel = new ViewModelProvider(this).get(StatsViewModel.class);
        statsViewModel.getStatsArray().observe(this, item -> {
            updateStatsDisplay(item);
        });

        addBotNavMargins();
    }

    @Override
    protected void onDestroy() {
        System.out.println("MainActivity onDestroy");

        hitTimer.cancel();
        hitTimer = null;

        super.onDestroy();
    }

    private void hitEnemy() {
        boolean crit = Calculate.crit(Calculate.intl(user.getIntl()));
        int hit = crit ?
                Calculate.strength(user.getStr()) * 2 :
                Calculate.strength(user.getStr());
        if(crit) {
            enemyHPTextView.setTextColor(Color.parseColor("#FF5228"));
            CountDownTimer orangeHealth = new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long l) {}

                @Override
                public void onFinish() {
                    enemyHPTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), com.google.android.material.R.color.abc_secondary_text_material_dark));
                }
            }.start();
        }

        user.setEnemyHP(user.getEnemyHP() - hit);
        // int enemyMaxHP = Calculate.newEnemyHP(todosDBHelper.getEnemyInfo(user.getEnemyId()).getStartHP(), user.getKillCount());
        System.out.println("after hit: " + user.getEnemyHP());
    }

    private void drawNewEnemy() {
        Random r = new Random();
        user.setEnemyId((int)Math.ceil(r.nextDouble()*10));
        user.setEnemyHP(Calculate.newEnemyHP(todosDBHelper.getEnemyInfo(user.getEnemyId()).getStartHP(), user.getKillCount()));
    }

    private CountDownTimer runHitTimer() {
        CountDownTimer res = new CountDownTimer(getHitTimeLeft(user.getHitTime()), 1000) {
            @Override
            public void onTick(long l) {
                hitTimeTextView.setText("Hit in " + Calculate.hitTimeCountdown(l/1000));
            }

            @Override
            public void onFinish() {
                user = todosDBHelper.getUser();
                hitEnemy();
                if(user.getEnemyHP() <= 0) {
                    user.setKillCount(user.getKillCount() + 1);
                    drawNewEnemy();
                }
                updateHitTime();
                System.out.println("New hit time: " + user.getHitTime() + " / +" + getHitTimeLeft(user.getHitTime()));
                todosDBHelper.updateUser(user);
                setDisplayInfo();
                restartTimer();
            }
        };

        return res;
    }

    private void setDisplayInfo() {
        Pair<Enemy, User> displayInfo = todosDBHelper.getDisplayInfo();
        user = displayInfo.second;

        int imgId = ((int)Math.ceil(displayInfo.first.getEnemyId() / 3f)) * 10 + ((displayInfo.first.getEnemyId()-1) % 3) + 1;
        int resouceId = this.getResources().
                getIdentifier("_" + imgId, "drawable", this.getPackageName());
        imageView.setImageResource(resouceId);

        int enemyMaxHP = Calculate.newEnemyHP(todosDBHelper.getEnemyInfo(user.getEnemyId()).getStartHP(), user.getKillCount());
        enemyHPTextView.setText(String.format("%d/%d", user.getEnemyHP(), enemyMaxHP));
        progressBar.setProgress(
                (int) ((displayInfo.second.getEnemyHP() / (float) enemyMaxHP) * 100)
        );

        System.out.println("Enemy Info: " + displayInfo.first.getName() + " (" + imgId + ") ");
        System.out.println("Enemy ID: " + displayInfo.first.getEnemyId());
        System.out.println("Current enemy HP: " + displayInfo.second.getEnemyHP());
        enemyTextView.setText(displayInfo.first.getName());

        int[] stats = {
                displayInfo.second.getAvailablePts(),
                displayInfo.second.getStr(),
                displayInfo.second.getAgility(),
                displayInfo.second.getIntl()
        };
        updateStatsDisplay(stats);
    }

    private String[] calculateStats(int str, int agility, int intl) {
        String[] res = new String[3];

        res[0] = String.format("%dATK", (int)(1f+((float)str-1f)/4f));
        float ag = 24f/(1f+((float)agility-1f)/4f);
        res[1] = String.format("1 hit/%.2fh", ag);
        res[2] = String.format("%.2f%% Crit Chance", 100f-(100f/(1f+(((float)intl-1f)/75f))));

        return res;
    }

    private void updateStatsDisplay(int[] stats) {
        // int[] stats ma jeszcze ptsAvailable dlatego od 1 : - <
        String[] calcStats = calculateStats(
                stats[1],
                stats[2],
                stats[3]
        );

        strInfoTextView.setText("Strength: " + stats[1] + " (" + calcStats[0] + ")");
        agilityInfoTextView.setText("Agility: " + stats[2] + " (" + calcStats[1] + ")");
        intlInfoTextView.setText("Intelligence: " + stats[3] + " (" + calcStats[2] + ")");
    }

    private long getHitTimeLeft(long unixTimestamp) {
        return unixTimestamp * 1000 - System.currentTimeMillis();
    }

    private void updateHitTime() {
        user.setHitTime(Calculate.newHitTime(user.getHitTime(), user.getAgility()));
        System.out.println("MainActivity updateHitTime");
    }

    private void restartTimer() {
        if(hitTimer != null) {
            hitTimer.cancel();
            hitTimer = null;
        }

        hitTimer = runHitTimer();
        hitTimer.start();
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Hide done todos")
                .setMessage("Do you really want to end ticked todos?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        hideDoneTodos();
                        Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                        reapplyFragment();
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bottomNavigationView.setSelectedItemId(selectedMI);
                    }
                }).show();
    }

    private void reapplyFragment() {
        List<Todo> newTodoList;

        switch(selectedMI) {
            case R.id.miHome:
                newTodoList = todosDBHelper.getOverTodos(false);
                replace(new TodoListFragment(newTodoList), currentFragmentTag);
                bottomNavigationView.setSelectedItemId(selectedMI);
                break;
            case R.id.miHidden:
                newTodoList = todosDBHelper.getOverTodos(true);
                replace(new TodoListFragment(newTodoList), currentFragmentTag);
                bottomNavigationView.setSelectedItemId(selectedMI);
                break;
        }
    }

    private void hideDoneTodos() {
        List<Todo> todos = todosDBHelper.getAllTodos();
        for(Todo todo:todos) {
            if(todo.isDone() && !todo.isOver()) {
                todosDBHelper.setOver(todo.getId(), true);
                user.availablePts += todo.getPoints();
            }
        }
        todosDBHelper.insertUser(user);
    }

//    private void reloadFragment() {
//        Fragment frg = getSupportFragmentManager().findFragmentByTag(currentFragmentTag);
//        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.detach(frg);
//        ft.attach(frg);
//        ft.commit();
//    }

    private void addBotNavMargins() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == -1) {
                        Toast.makeText(MainActivity.this, "New Todo Added!", Toast.LENGTH_SHORT).show();
                        reapplyFragment();
                    }
                    System.out.println(result.getResultCode());
                    bottomNavigationView.setSelectedItemId(selectedMI);
                }
            }
    );

    private void replace(Fragment fragment, String fragmentTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        for (Fragment f : getSupportFragmentManager().getFragments()) {
            if (f != null) {
                getSupportFragmentManager().beginTransaction().remove(f).commit();
            }
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, fragment, fragmentTag);
        fragmentTransaction.commit();
        currentFragmentTag = fragmentTag;
    }
}