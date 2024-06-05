package com.example.rpgtodolist.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rpgtodolist.R;
import com.example.rpgtodolist.Todo;
import com.example.rpgtodolist.TodoRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoListFragment extends Fragment {

    List<Todo> lista = new ArrayList<>();
    RecyclerView recyclerView;
    TodoRecycleViewAdapter todoRecycleViewAdapter;
    List<TodoRecycleViewAdapter.ViewHolder> activeViewHolders = new ArrayList<>();

    public TodoListFragment(List<Todo> todoList) {
        lista = todoList;
    }

    public TodoListFragment() { }

    @Override
    public void onPause() {
        super.onPause();

        for (int i = 0; i < lista.size(); i++) {
            TodoRecycleViewAdapter.ViewHolder viewHolder = (TodoRecycleViewAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null && viewHolder.cdt != null) {
                viewHolder.cdt.cancel();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // bundle here
        recyclerView = view.findViewById(R.id.todoRV);
        todoRecycleViewAdapter = new TodoRecycleViewAdapter(lista, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(todoRecycleViewAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("TodoListFragment onDestroyView");
        todoRecycleViewAdapter.getItemCount();
//        todoRecycleViewAdapter.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("TodoListFragment onDestroy");
    }

    public static TodoListFragment newInstance() {
        TodoListFragment fragment = new TodoListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public List<Todo> getLista() {
        return lista;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_todo_list_fragment, container, false);
    }
}