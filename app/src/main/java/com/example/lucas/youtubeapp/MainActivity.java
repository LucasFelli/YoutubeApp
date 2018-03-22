package com.example.lucas.youtubeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Button button;
    YoutubeAPI ytApi;
    EditText editText;

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
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editText.getText().toString();
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
        recyclerView = findViewById(R.id.main_reclycer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
