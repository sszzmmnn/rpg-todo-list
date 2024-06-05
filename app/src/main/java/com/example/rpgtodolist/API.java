package com.example.rpgtodolist;

import androidx.annotation.NonNull;

import com.example.rpgtodolist.dto.UserStatsDto;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class API {

    public API() { }

    public User getUser(String name) {
        Gson gson = new Gson();

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
                    System.out.println(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        });

        return null;
    }

    public boolean submitUser(User user) {
        UserStatsDto uawdto = new UserStatsDto(
                user.getAvailablePts(),
                user.getStr(),
                user.getAgility(),
                user.getIntl(),
                user.getEnemyId(),
                user.getKillCount()
        );

        //BiggerUserAPIWrapperDto bauwdto = new BiggerUserAPIWrapperDto();

        return true;
    }
}
