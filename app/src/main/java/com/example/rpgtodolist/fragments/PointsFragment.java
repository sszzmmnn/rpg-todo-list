package com.example.rpgtodolist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rpgtodolist.MainActivity;
import com.example.rpgtodolist.R;
import com.example.rpgtodolist.StatsViewModel;
import com.example.rpgtodolist.TodosDBHelper;
import com.example.rpgtodolist.User;
import com.example.rpgtodolist.dto.GetUserStatsDto;
import com.example.rpgtodolist.dto.UpdateUserDto;
import com.example.rpgtodolist.dto.UpdateUserNoHashDto;
import com.example.rpgtodolist.dto.UserStatsDto;
import com.example.rpgtodolist.utils.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PointsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PointsFragment extends Fragment {

    StatsViewModel statsViewModel;
    Button saveStatsBtn, compareUserBtn;
    ImageButton strMinusImgBtn, strPlusImgBtn, agilityMinusImgBtn, agilityPlusImgBtn, intlMinusImgBtn, intlPlusImgBtn;
    TextView availablePtsTextView, strCountTextView, agilityCountTextView, intlCountTextView;
    int ptsAvailable = 300, strPts = 0, agilityPts = 0, intlPts = 0, defaultAvailable, defaultStr, defaultAgility, defaultIntl;
    int[] ptsArray;

    public PointsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle b = this.getArguments();
        if(b != null) {
            ptsAvailable = b.getInt("pts");
            strPts = b.getInt("str");
            agilityPts = b.getInt("agility");
            intlPts = b.getInt("intl");

            defaultAvailable = ptsAvailable;
            defaultStr = strPts;
            defaultAgility = agilityPts;
            defaultIntl = intlPts;
        }

        availablePtsTextView = view.findViewById(R.id.availablePtsTextView);

        strCountTextView = view.findViewById(R.id.strCountTextView);
        strMinusImgBtn = view.findViewById(R.id.strMinusImgBtn);
        strPlusImgBtn = view.findViewById(R.id.strPlusImgBtn);

        agilityCountTextView = view.findViewById(R.id.agilityCountTextView);
        agilityMinusImgBtn = view.findViewById(R.id.agilityMinusImgBtn);
        agilityPlusImgBtn = view.findViewById(R.id.agilityPlusImgBtn);

        intlCountTextView = view.findViewById(R.id.intlCountTextView);
        intlMinusImgBtn = view.findViewById(R.id.intlMinusImgBtn);
        intlPlusImgBtn = view.findViewById(R.id.intlPlusImgBtn);

        compareUserBtn = view.findViewById(R.id.compareUserBtn);
        saveStatsBtn = view.findViewById(R.id.saveStatsBtn);

        updateAvailablePts(ptsAvailable);
        strCountTextView.setText(String.format("%d", strPts));
        agilityCountTextView.setText(String.format("%d", agilityPts));
        intlCountTextView.setText(String.format("%d", intlPts));

        ptsArray = new int[] { ptsAvailable, strPts, agilityPts, intlPts };
        statsViewModel = new ViewModelProvider(requireActivity()).get(StatsViewModel.class);

        strMinusImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(strPts > defaultStr) {
                    updateAvailablePts(++ptsAvailable);
                    strCountTextView.setText(String.format("%d", --strPts));
                    ptsArray[0] = ptsAvailable;
                    ptsArray[1] = strPts;
                    statsViewModel.setStatsArray(ptsArray);
                }

                availablePtsCheck();
            }
        });

        strPlusImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ptsAvailable > 0) {
                    updateAvailablePts(--ptsAvailable);
                    strCountTextView.setText(String.format("%d", ++strPts));
                    ptsArray[0] = ptsAvailable;
                    ptsArray[1] = strPts;
                    statsViewModel.setStatsArray(ptsArray);
                }

                availablePtsCheck();
            }
        });

        agilityMinusImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(agilityPts > defaultAgility) {
                    updateAvailablePts(++ptsAvailable);
                    agilityCountTextView.setText(String.format("%d", --agilityPts));
                    ptsArray[0] = ptsAvailable;
                    ptsArray[2] = agilityPts;
                    statsViewModel.setStatsArray(ptsArray);
                }

                availablePtsCheck();
            }
        });

        agilityPlusImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ptsAvailable > 0) {
                    updateAvailablePts(--ptsAvailable);
                    agilityCountTextView.setText(String.format("%d", ++agilityPts));
                    ptsArray[0] = ptsAvailable;
                    ptsArray[2] = agilityPts;
                    statsViewModel.setStatsArray(ptsArray);
                }

                availablePtsCheck();
            }
        });

        intlMinusImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(intlPts > defaultIntl) {
                    updateAvailablePts(++ptsAvailable);
                    intlCountTextView.setText(String.format("%d", --intlPts));
                    ptsArray[0] = ptsAvailable;
                    ptsArray[3] = intlPts;
                    statsViewModel.setStatsArray(ptsArray);
                }

                availablePtsCheck();
            }
        });

        intlPlusImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ptsAvailable > 0) {
                    updateAvailablePts(--ptsAvailable);
                    intlCountTextView.setText(String.format("%d", ++intlPts));
                    ptsArray[0] = ptsAvailable;
                    ptsArray[3] = intlPts;
                    statsViewModel.setStatsArray(ptsArray);
                }

                availablePtsCheck();
            }
        });

        compareUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlertDialog();
            }
        });

        saveStatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodosDBHelper db = new TodosDBHelper(getContext());
                if(db.updateStats(new int[]{ptsAvailable, strPts, agilityPts, intlPts})) {
                    Toast.makeText(getContext(), "Stats saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openAlertDialog() {
        String apiName = new TodosDBHelper(getContext()).getAPIName();
        if(apiName == null || apiName.isEmpty()) {
            insertAPINameDialog();
        } else {
            getUserStatsDialog();
        }
    }

    private void insertAPINameDialog() {
        final String[] m_Text = {""};

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("What is your name?");

        final EditText input = new EditText(this.getContext());

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().toString().length() < 2) {
                    Toast.makeText(getContext(), "Name is too short!", Toast.LENGTH_SHORT).show();
                } else {
                    m_Text[0] = input.getText().toString();
                    checkIfUserExists(m_Text[0]);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void getUserStatsDialog() {
        final String[] m_Text = {""};

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Enter name");

        final EditText input = new EditText(this.getContext());

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text[0] = input.getText().toString();
                getUser(m_Text[0]);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateAvailablePts(int ptsAvailable) {
        availablePtsTextView.setText(String.format("Available points: %d", ptsAvailable));
    }

    private void availablePtsCheck() {
        boolean enabled = true;
        if(ptsAvailable == 0) {
            enabled = false;
        }

        boolean strEnabled = true;
        if(strPts == 0) {
            strEnabled = false;
        }

        boolean agilityEnabled = true;
        if(agilityPts == 0) {
            agilityEnabled = false;
        }

        boolean intlEnabled = true;
        if(intlPts == 0) {
            intlEnabled = false;
        }

        strMinusImgBtn.setEnabled(strEnabled);
        strPlusImgBtn.setEnabled(enabled);
        agilityMinusImgBtn.setEnabled(agilityEnabled);
        agilityPlusImgBtn.setEnabled(enabled);
        intlMinusImgBtn.setEnabled(intlEnabled);
        intlPlusImgBtn.setEnabled(enabled);
    }

    public static PointsFragment newInstance() {
        PointsFragment fragment = new PointsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_points, container, false);
    }

    public void checkIfUserExists(String name) {
        Gson gson = new Gson();
        final GetUserStatsDto[] getUserStatsDto = new GetUserStatsDto[1];

        OkHttpClient client = new OkHttpClient();
        String url = /*API URL*/ + name;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Connection", "close")
                .header("Accept-language", "pl")
                .header("User-Agent", "mobile")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.code() == 200) {
                    String responseBody = response.body().string();
                    System.out.println(responseBody);

                    getUserStatsDto[0] = gson.fromJson(responseBody, GetUserStatsDto.class);
                    if(!getUserStatsDto[0].isExists()){
                        TodosDBHelper db = new TodosDBHelper(getContext());
                        db.setAPIName(name);
                        User user = db.getUser();

                        UserStatsDto userStatsDto = new UserStatsDto(
                                user.getAvailablePts(),
                                user.getStr(),
                                user.getAgility(),
                                user.getIntl(),
                                user.getEnemyId(),
                                user.getKillCount()
                        );

                        UpdateUserDto updateUserDto = new UpdateUserDto(name, userStatsDto, getSHA256(new UpdateUserNoHashDto(name, userStatsDto)));
                        submitUser(updateUserDto, false);
                        getActivity().runOnUiThread(() -> getUserStatsDialog());
                    } else {
                        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "User with this name already exists!", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void getUser(String name) {
        Gson gson = new Gson();
        final GetUserStatsDto[] getUserStatsDto = new GetUserStatsDto[1];

        // aktualizacja użytkownika
        User user = new TodosDBHelper(getContext()).getUser();

        UserStatsDto userStatsDto = new UserStatsDto(
                user.getAvailablePts(),
                user.getStr(),
                user.getAgility(),
                user.getIntl(),
                user.getEnemyId(),
                user.getKillCount()
        );

        String apiName = new TodosDBHelper(getContext()).getAPIName();

        UpdateUserDto updateUserDto = new UpdateUserDto(
                apiName, userStatsDto, getSHA256(
                        new UpdateUserNoHashDto(apiName, userStatsDto)
                )
        );
        submitUser(updateUserDto, true);

        // pobranie innego użytkownika
        OkHttpClient client = new OkHttpClient();
        String url = /*API URL*/ + name;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Connection", "close")
                .header("Accept-language", "pl")
                .header("User-Agent", "mobile")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.code() == 200) {
                    String responseBody = response.body().string();
                    System.out.println(responseBody);

                    getUserStatsDto[0] = gson.fromJson(responseBody, GetUserStatsDto.class);
                    if(getUserStatsDto[0].isExists()){
                        UserStatsDto user = getUserStatsDto[0].getValue();
                        getActivity().runOnUiThread(() -> {
                            MainActivity mainActivity = (MainActivity) getActivity();
                            mainActivity.currentFragmentTag = "USER_COMPARE";
                            Utils.replace(new UserCompareFragment(getUserStatsDto[0]), "USER_COMPARE", getActivity());
                        });
                    } else {
                        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "User with this name does not exist!", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void submitUser(UpdateUserDto updateUserDto, boolean update) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        String url = /*API URL*/"";
        if(update) { updateUserDto.setHash(updateUserDto.getHash() + "update"); }
        RequestBody body = RequestBody.create(updateUserDto.toString(), JSON);
        System.out.println(updateUserDto.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Connection", "close")
                .header("Accept-language", "pl")
                .header("User-Agent", "mobile")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }

    private String getSHA256(UpdateUserNoHashDto updateUserNoHashDto) {
        String input = String.valueOf(updateUserNoHashDto);
        System.out.println("updateUserNoHashDto: " + input);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}