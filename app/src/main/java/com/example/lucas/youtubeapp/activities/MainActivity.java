package com.example.lucas.youtubeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.lucas.youtubeapp.models.Item;
import com.example.lucas.youtubeapp.viewholders.Link;
import com.example.lucas.youtubeapp.R;
import com.example.lucas.youtubeapp.YoutubeAPI;
import com.example.lucas.youtubeapp.adapter.YoutubeAdapter;
import com.example.lucas.youtubeapp.models.YoutubeCallAnswer;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Button button;
    YoutubeAPI ytApi;
    AutoCompleteTextView editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeRecyclerView();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YoutubeAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ytApi = retrofit.create(YoutubeAPI.class);

        editText = findViewById(R.id.query);
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        String myStrings = settings.getString("previousSearch", "");
        setEditTextAdapter(myStrings.split(","));

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editText.getText().toString();

                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                String myStrings = settings.getString("previousSearch","");
                ArrayList<String> uniqueStrings = new ArrayList<>(Arrays.asList(myStrings.split(",")));
                if (!uniqueStrings.contains(query)){
                    myStrings = myStrings + "," + query;
                    setEditTextAdapter(myStrings.split(","));
                }
                editor.putString("previousSearch", myStrings);
                editor.apply();

                Call<YoutubeCallAnswer> call = ytApi.search(query);

                call.enqueue(new Callback<YoutubeCallAnswer>() {
                    @Override
                    public void onResponse(Call<YoutubeCallAnswer> call, Response<YoutubeCallAnswer> response){
                        YoutubeCallAnswer resp = response.body();
                        recyclerView.setAdapter(new YoutubeAdapter(resp.getItems(), new Link(){
                            @Override
                            public void onVideoClickListener(Item video){
                                String videoId = video.getId().getVideoId();
                                Intent intent = new Intent(MainActivity.this, YoutubeActivity.class);
                                intent.putExtra("VideoId", videoId);
                                startActivity(intent);
                            }
                        }));
                    }

                    @Override
                    public void onFailure(Call<YoutubeCallAnswer> call, Throwable t){
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setEditTextAdapter(String[] strings){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_dropdown_item_1line, strings);
        editText.setAdapter(adapter);
    }
}
