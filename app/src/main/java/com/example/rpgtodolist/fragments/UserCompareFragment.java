package com.example.rpgtodolist.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rpgtodolist.R;
import com.example.rpgtodolist.TodosDBHelper;
import com.example.rpgtodolist.dto.GetUserStatsDto;
import com.example.rpgtodolist.dto.UserStatsDto;

public class UserCompareFragment extends Fragment {

    GetUserStatsDto getUserStatsDto;
    TextView userNameTextView, userAvailablePtsTextView, userStrTextView, userAgilityTextView, userIntlTextView, userEnemyTextView, userKillCountTextView;

    public UserCompareFragment() {
        // Required empty public constructor
        getUserStatsDto = null;
    }

    public UserCompareFragment(GetUserStatsDto getUserStatsDto) {
        this.getUserStatsDto = getUserStatsDto;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userNameTextView = view.findViewById(R.id.userNameTextView);
        userAvailablePtsTextView = view.findViewById(R.id.userAvailablePtsTextView);
        userStrTextView = view.findViewById(R.id.userStrTextView);
        userAgilityTextView = view.findViewById(R.id.userAgilityTextView);
        userIntlTextView = view.findViewById(R.id.userIntlTextView);
        userEnemyTextView = view.findViewById(R.id.userEnemyTextView);
        userKillCountTextView = view.findViewById(R.id.userKillCountTextView);

        if(getUserStatsDto == null) {
            userNameTextView.setText("Click \"Compare\" to retrieve someone's stats!");
        } else {
            userNameTextView.setText("Name: " + getUserStatsDto.getName());
            userAvailablePtsTextView.setText("Available points: " + getUserStatsDto.getValue().getAvailable_pts());
            userStrTextView.setText("Strength: " + getUserStatsDto.getValue().getStrength());
            userAgilityTextView.setText("Agility: " + getUserStatsDto.getValue().getAgility());
            userIntlTextView.setText("Intelligence: " + getUserStatsDto.getValue().getIntelligence());
            userEnemyTextView.setText("Current enemy: " + new TodosDBHelper(getContext()).getEnemyInfo(getUserStatsDto.getValue().getEnemy_id()).getName());
            userKillCountTextView.setText("Kill count: " + getUserStatsDto.getValue().getKill_count());
        }
    }

    // TODO: Rename and change types and number of parameters
    public static UserCompareFragment newInstance() {
        UserCompareFragment fragment = new UserCompareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_compare, container, false);
    }
}