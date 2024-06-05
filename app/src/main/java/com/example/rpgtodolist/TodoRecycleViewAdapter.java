package com.example.rpgtodolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class TodoRecycleViewAdapter extends RecyclerView.Adapter<TodoRecycleViewAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<Todo> mojaLista;
    Context ctx;

    public TodoRecycleViewAdapter(List<Todo> lista, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.mojaLista = lista;
    }

    @NonNull
    @Override
    public TodoRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.todo_item, parent, false);
        ctx = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoRecycleViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String nazwa = mojaLista.get(position).getTitle();
        long creation_time = mojaLista.get(position).getCreation_time();
        boolean done = mojaLista.get(position).isDone();
        boolean over = mojaLista.get(position).isOver();

        holder.nameTV.setText(nazwa);
        holder.checkBox.setChecked(done);

        if (holder.cdt != null) {
            holder.cdt.cancel();
        }

        if(over) { holder.checkBox.setEnabled(false); }

        setCreationTime(holder);

        holder.cdt = new CountDownTimer(getTimeLeft(creation_time),1000) {
            @Override
            public void onTick(long l) {
                holder.textView2.setText(getDisplayTime(
                        l/1000,
                        mojaLista.get(position).getType(),
                        mojaLista.get(position).isOver()
                ));
            }

            @Override
            public void onFinish() {
                TodosDBHelper db = new TodosDBHelper(ctx);

                // chowanie todo
                if(!over) {
                    holder.itemView.setVisibility(View.GONE);
                    ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    holder.itemView.setLayoutParams(params);
                    //TODO✓: dodawanie punktów jeśli todo jest zaznaczony ale nie skasowany

                    if(done) {
                        db.updatePoints(mojaLista.get(position).getPoints());
                    }
                }

                if(mojaLista.get(position).getType() > 0) {
                    setCreationTime(holder);
                    if(mojaLista.get(position).isOver()) {
                        db.setOver(
                                mojaLista.get(position).getId(),
                                false
                        );
                        db.setDone(
                                mojaLista.get(position).getId(),
                                false
                        );
                    }
                } else {
                    if (!over) {
                        mojaLista.get(position).setOver(true);
                        db.setOver(
                                mojaLista.get(position).getId(),
                                mojaLista.get(position).isOver()
                        );
                    }
                    holder.checkBox.setEnabled(false);
                    holder.textView2.setText("koniec!");
                }
            }
        }.start();


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodosDBHelper db = new TodosDBHelper(ctx);
                mojaLista.get(position).setDone(holder.checkBox.isChecked());
                db.setDone(
                        mojaLista.get(position).getId(),
                        mojaLista.get(position).isDone()
                );
            }
        });

        holder.removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodosDBHelper db = new TodosDBHelper(ctx);
                showRemoveTodoConfirmDialog(holder, mojaLista.get(position).getId());
            }
        });
    }

    private void setCreationTime(TodoRecycleViewAdapter.ViewHolder holder) {
        if(mojaLista.get(holder.getAdapterPosition()).getType() > 0) {
            if(mojaLista.get(holder.getAdapterPosition()).getCreation_time() < System.currentTimeMillis() / 1000L) {

                Calendar c = new GregorianCalendar();
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);

                switch(mojaLista.get(holder.getAdapterPosition()).getType()) {
                    case 1:
                        c.add(Calendar.HOUR_OF_DAY, 24);
                        break;
                    case 2:
                        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        c.add(Calendar.DAY_OF_MONTH, 7);
                        break;
                }

                Date d = c.getTime();

                long time = d.getTime() / 1000L;

                mojaLista.get(holder.getAdapterPosition()).setCreation_time(time);
                TodosDBHelper db = new TodosDBHelper(ctx);
                db.setCreationTime(
                        mojaLista.get(holder.getAdapterPosition()).getId(),
                        mojaLista.get(holder.getAdapterPosition()).getCreation_time()
                );
            }
        }
    }

    private void showRemoveTodoConfirmDialog(TodoRecycleViewAdapter.ViewHolder holder, int id) {
        new AlertDialog.Builder(ctx)
                .setTitle("Delete the todo")
                .setMessage("Are you sure you want to delete this todo?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        removeTodo(holder, id);
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                }).show();
    }

    private void removeTodo(TodoRecycleViewAdapter.ViewHolder holder, int id) {
        TodosDBHelper db = new TodosDBHelper(ctx);

        holder.itemView.setVisibility(View.GONE);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = 0;
        params.width = 0;
        holder.itemView.setLayoutParams(params);

        System.out.println(db.removeTodo(id));
    }


    @Override
    public int getItemCount() {
        return mojaLista.size();
    }

    public long getTimeLeft(long unixTimestamp) {
        Date timestamp = new Date(unixTimestamp * 1000L);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(timestamp);

        Date desiredMoment = calendar.getTime();

        long currentTimeUtc = System.currentTimeMillis();

        long timeDifferenceSeconds = (desiredMoment.getTime() - currentTimeUtc);
        System.out.println(String.format("%d - %d = %d\t %d", desiredMoment.getTime(), currentTimeUtc, timeDifferenceSeconds, unixTimestamp));

        return timeDifferenceSeconds;
    }

    public String getDisplayTime(long seconds, int type, boolean over) {
        long days = seconds / (24 * 60 * 60);
        seconds %= (24 * 60 * 60);
        long hours = seconds / (60 * 60);
        seconds %= (60 * 60);
        long minutes = seconds / 60;
        seconds %= 60;

        StringBuilder timeStringBuilder = new StringBuilder();

        if(over) {
            timeStringBuilder.append("Done!");
            if(type > 0) {
                timeStringBuilder.append(" Refreshing in ");
            } else {
                return timeStringBuilder.toString();
            }
        }

        int displayCount = 0;
        if (days > 0 && displayCount < 2) {
            timeStringBuilder.append(days).append("d ");
            displayCount++;
        }
        if (hours > 0 && displayCount < 2) {
            timeStringBuilder.append(hours).append("h ");
            displayCount++;
        }
        if (minutes > 0 && displayCount < 2) {
            timeStringBuilder.append(minutes).append("min ");
            displayCount++;
        }
        if (seconds > 0 && displayCount < 2) {
            timeStringBuilder.append(seconds).append("s");
            displayCount++;
        }

        return timeStringBuilder.toString();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout todoItemLayout;
        TextView nameTV, textView2;
        CheckBox checkBox;
        ImageButton removeImageButton;
        public CountDownTimer cdt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            todoItemLayout = itemView.findViewById(R.id.todoItemLayout);
            nameTV = itemView.findViewById(R.id.nameTV);
            textView2 = itemView.findViewById(R.id.textView2);
            checkBox = itemView.findViewById(R.id.checkBox);
            removeImageButton = itemView.findViewById(R.id.removeImageButton);
        }
    }
}
