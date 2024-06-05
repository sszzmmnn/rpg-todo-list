package com.example.rpgtodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class AddTodoItemActivity extends AppCompatActivity {

    TextView selectedDateTV, pointsTextView;
    int points;
    long dateUnix, timeUnix;
    boolean dateSet = false, timeSet = false;
    EditText titleEditText;
    Spinner todoTypeSpinner;
    Button datePickerBtn, timePickerBtn, addTodoBtn;
    ImageButton pointRmImgBtn, pointAddImgBtn;
    TodoType todoType;
    List<TodoType> todoTypes;
    ArrayAdapter<TodoType> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo_item);

        selectedDateTV = findViewById(R.id.selectedDateTV);
        selectedDateTV.setText("");
        titleEditText = findViewById(R.id.titleEditText);
        todoTypeSpinner = findViewById(R.id.todoTypeSpinner);

        datePickerBtn = findViewById(R.id.datePickerBtn);
        timePickerBtn = findViewById(R.id.timePickerBtn);
        addTodoBtn = findViewById(R.id.addTodoBtn);
        pointRmImgBtn = findViewById(R.id.pointRmImgBtn);
        pointAddImgBtn = findViewById(R.id.pointAddImgBtn);
        pointsTextView = findViewById(R.id.pointsTextView);

        points = 1;
        pointsTextView.setText("1pt");

        todoTypes = new ArrayList<>();
        todoTypes.add(new TodoType("One-time", 0));
        todoTypes.add(new TodoType("Daily", 1));
        todoTypes.add(new TodoType("Weekly", 2));

        todoTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                todoType = (TodoType) todoTypeSpinner.getSelectedItem();
                datePickerBtn.setEnabled(todoType.getValue() == 0);
                timePickerBtn.setEnabled(todoType.getValue() == 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pointRmImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(points == 1) return;
                points--;
                pointsTextView.setText(points + "pt" + (points > 1 ? "s" : ""));
            }
        });

        pointAddImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(points == 3) return;
                points++;
                pointsTextView.setText(points + "pt" + (points > 1 ? "s" : ""));
            }
        });

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTodoItemActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                                dateUnix = calendar.getTimeInMillis() / 1000L;
                                System.out.println("dateUnix: " + dateUnix);
                                dateSet = true;
                                updateDateDisplay();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        timePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTodoItemActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                System.out.println(hourOfDay + " " + minute);
                                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                                calendar.set(1970, 0, 1, hourOfDay, minute, 0);
                                timeUnix = calendar.getTimeInMillis() / 1000L;
                                System.out.println("dateUnix: " + timeUnix);
                                timeSet = true;
                                updateDateDisplay();
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        addTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time = dateUnix + timeUnix;

                if(time < System.currentTimeMillis() / 1000) {
                    Toast.makeText(AddTodoItemActivity.this, "It's a bit late to do this, is it not?", Toast.LENGTH_SHORT).show();
                    return;
                }

                TodoType tdt = (TodoType) todoTypeSpinner.getSelectedItem();

                String todoError = newTodo(tdt.getValue());
                if(!todoError.equals("")) {
                    Toast.makeText(AddTodoItemActivity.this, todoError, Toast.LENGTH_SHORT).show();
                    return;
                }

                System.out.println(titleEditText.getText() + " - " + tdt.getValue()  + " " + time);

                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, todoTypes);
        todoTypeSpinner.setAdapter(adapter);
    }

    private String newTodo(int todoType) {

        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        switch(todoType) {
            case 1:
                c.add(Calendar.HOUR_OF_DAY, 24);
                break;
            case 2:
                c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                //c.add(Calendar.DAY_OF_MONTH, 7);
                break;
        }

        Date d = c.getTime();

        long time = d.getTime() / 1000L;

        if(titleEditText.getText().toString().matches("")) {
            return "Todo name is missing!";
        }

        if(todoType == 0) {
            if (!dateSet || !timeSet) {
                return "You forgot to set the time!";
                //TODO: moze zrobic zeby by default ustawialo sie na za 24h?
            }

            time = dateUnix + timeUnix;
            System.out.println("todoType0: " + time);
        }

        Todo todo = new Todo(0, titleEditText.getText().toString(), todoType, time, false, false, points);
        System.out.println("getCreationTime: " + todo.getCreation_time());

        TodosDBHelper db = new TodosDBHelper(this);
        if(!db.addTodo(todo)) {
            return "Something went wrong! No idea what though...";
        }

        return "";
    }

    private void updateDateDisplay() {
        SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yyyy H:mm");
        String dateFormatted = DateFormat.format(new Date(dateUnix*1000L + timeUnix*1000L));
        selectedDateTV.setText("The todo will disappear on " + dateFormatted);
    }
}