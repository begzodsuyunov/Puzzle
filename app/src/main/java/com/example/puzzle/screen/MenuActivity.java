package com.example.puzzle.screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

import com.example.puzzle.R;
import com.example.puzzle.utils.MySharedPreference;

import java.util.Objects;

public class MenuActivity extends AppCompatActivity {


    MySharedPreference mySharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        mySharedPreference = MySharedPreference.getInstance(getApplicationContext());

        AppCompatButton continueGame = findViewById(R.id.continueGame);
        AppCompatButton newGame = findViewById(R.id.newGame);
        AppCompatButton level = findViewById(R.id.level);

        String time = mySharedPreference.getPauseTime().toString();
        String levelText = mySharedPreference.getLevel();

        continueGame.setOnClickListener(view -> {

            if (Objects.equals(levelText, "easy")) {
                Intent intent = new Intent(new Intent(MenuActivity.this, MainActivity.class));
                intent.putExtra("time", time);
                startActivity(intent);
            } else if (Objects.equals(levelText, "medium")) {
                Intent intent = new Intent(new Intent(MenuActivity.this, SecondLevelActivity.class));
                intent.putExtra("time", time);
                startActivity(intent);
            } else if (Objects.equals(levelText, "hard")) {
                Intent intent = new Intent(new Intent(MenuActivity.this, ThirdLevelActivity.class));
                intent.putExtra("time", time);
                startActivity(intent);
            }
        });

        newGame.setOnClickListener(view -> {
            Intent intent = new Intent(new Intent(MenuActivity.this, MainActivity.class));
            intent.putExtra("time", "");
            startActivity(intent);
        });

        level.setOnClickListener(view ->

                startActivity(new Intent(MenuActivity.this, LevelActivity.class)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}