package com.example.puzzle.screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

import com.example.puzzle.R;
import com.example.puzzle.utils.MySharedPreference;

public class LevelActivity extends AppCompatActivity {

    MySharedPreference mySharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);


        mySharedPreference = MySharedPreference.getInstance(this);
        AppCompatButton easy = findViewById(R.id.easy);
        AppCompatButton medium = findViewById(R.id.medium);
        AppCompatButton hard = findViewById(R.id.hard);

        String time = mySharedPreference.getPauseTime().toString();

        easy.setOnClickListener(view -> {
            mySharedPreference.setLevel("easy");
            Intent intent = new Intent(new Intent(LevelActivity.this, MainActivity.class));
            intent.putExtra("time", time);
            startActivity(intent);
        });

        medium.setOnClickListener(view -> {
            mySharedPreference.setLevel("medium");
            Intent intent = new Intent(new Intent(LevelActivity.this, SecondLevelActivity.class));
            intent.putExtra("time", time);
            startActivity(intent);
        });
        hard.setOnClickListener(view -> {
            mySharedPreference.setLevel("hard");
            Intent intent = new Intent(new Intent(LevelActivity.this, ThirdLevelActivity.class));
            intent.putExtra("time", time);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MenuActivity.class));
    }

}