package com.example.puzzle.screen;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puzzle.R;
import com.example.puzzle.model.Winner;
import com.example.puzzle.utils.Coordinate;
import com.example.puzzle.utils.MySharedPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ThirdLevelActivity extends AppCompatActivity {

    ViewGroup group;
    private TextView textScore;
    private Chronometer textTime;
    private TextView bestTime;
    private TextView bestScore;
    ImageView imageView;
    private Button[][] buttons;
    private MySharedPreference mySharedPreference;
    ArrayList<Winner> winners = new ArrayList<>();
    private ArrayList<Integer> numbers;
    private Coordinate emptyCoordinate;
    private int score;
    private long pauseTime;
    private boolean isStarted;
    private MediaPlayer mediaPlayer = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_level);

        mySharedPreference = MySharedPreference.getInstance(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.burron);

        loadView();

        if (mySharedPreference.getSound()) {
            imageView.setImageResource(R.drawable.ic_baseline_volume_up_24);
        } else {
            imageView.setImageResource(R.drawable.ic_baseline_volume_off_24);
        }
        loadButtons();
        initNumbers();
        loadNumberToButton();

        bestScore.setText(mySharedPreference.getBestScoreHard());
        bestTime.setText(mySharedPreference.getBestTimeHard());
        String time = getIntent().getStringExtra("time");
        if (savedInstanceState == null && !time.equals("")) {
            isStarted = mySharedPreference.getIsPlayingHard();
            if (isStarted) {
                score = mySharedPreference.getScoreHard();
                pauseTime = mySharedPreference.getPauseTimeHard();
                String numbersText = mySharedPreference.getNumbersHard();
                String[] numbersArray = numbersText.split("#");
                List<String> numberList = new ArrayList<>(Arrays.asList(numbersArray));
                textScore.setText(String.valueOf(score));
                loadSavedNumbers(numberList);

            } else {
                onRestartGame();
            }

        }

    }


    private void loadView() {

        findViewById(R.id.finish).setOnClickListener(v -> exitDialog());
        findViewById(R.id.restart).setOnClickListener(v -> onRestartGame());
        findViewById(R.id.info).setOnClickListener(v -> infoDialog());
        findViewById(R.id.menu).setOnClickListener(v -> settingDialog());
        imageView = findViewById(R.id.image);
        group = findViewById(R.id.group_items);
        textScore = findViewById(R.id.score_text);
        textTime = findViewById(R.id.time_text);
        bestTime = findViewById(R.id.time_text_best);
        bestScore = findViewById(R.id.score_text_best);
    }

    private void settingDialog() {

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        ConstraintLayout mainLayout = findViewById(R.id.container);
        View dialogView = inflater.inflate(R.layout.settings_dialog, mainLayout, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        SwitchCompat sound = dialogView.findViewById(R.id.switch_sound);
        LinearLayout ok = dialogView.findViewById(R.id.ok);

        ok.setOnClickListener(view -> dialog.cancel());

        sound.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            if (isChecked) {
                mySharedPreference.setSound(true);
                imageView.setImageResource(R.drawable.ic_baseline_volume_up_24);
                onResume();
            } else {
                mySharedPreference.setSound(false);
                imageView.setImageResource(R.drawable.ic_baseline_volume_off_24);
                onStart();
            }
        });
        sound.setChecked(mySharedPreference.getSound());
        dialog.setView(dialogView);
        dialog.show();

    }


    private void loadNumberToButton() {
        shuffle();
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                int index = i * 6 + j;
                if (index < 35) {
                    buttons[i][j].setText(String.valueOf(numbers.get(index)));
                }
            }
        }
        buttons[5][5].setText("");
        emptyCoordinate = new Coordinate(5, 5);
        score = 0;
        textScore.setText(String.valueOf(score));
        isStarted = true;
        textTime.setBase(SystemClock.elapsedRealtime());
        textTime.start();
    }

    private void shuffle() {
        numbers.remove(Integer.valueOf(0));
        Collections.shuffle(numbers);
        if (isSolvable(numbers)) {
        } else {
            shuffle();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LLL", textTime.getText().toString());
        pauseTime = SystemClock.elapsedRealtime() - textTime.getBase();
        textTime.stop();
        mySharedPreference.setIsPlayingHard(true);
        mySharedPreference.setScoreHard(score);
        mySharedPreference.setPauseTimeHard(pauseTime);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < group.getChildCount() - 1; i++) {
            builder.append(((Button) group.getChildAt(i)).getText().toString() + "#");
        }
        builder.append(((Button) group.getChildAt(35)).getText().toString());
        mySharedPreference.setNumbersHard(builder.toString());
    }

    public boolean isSolvable(List<Integer> puzzle) {
        numbers.add(0);
        int parity = 0;
        int gridWidth = (int) Math.sqrt(puzzle.size());
        int row = 0;
        int blankRow = 0;

        for (int i = 0; i < puzzle.size(); i++) {
            if (i % gridWidth == 0) {
                row++;
            }
            if (puzzle.get(i) == 0) {
                blankRow = row;
                continue;
            }
            for (int j = i + 1; j < puzzle.size(); j++) {
                if (puzzle.get(i) > puzzle.get(j) && puzzle.get(j) != 0) {
                    parity++;
                }
            }
        }

        if (gridWidth % 2 == 0) {
            if (blankRow % 2 == 0) {
                return parity % 2 == 0;
            } else {
                return parity % 2 != 0;
            }
        } else {
            return parity % 2 == 0;
        }
    }

    private void initNumbers() {
        numbers = new ArrayList<>();
        for (int i = 1; i <= 35; i++) {
            numbers.add(i);
        }
    }

    private void loadButtons() {
        ViewGroup group = findViewById(R.id.group_items);
        int count = group.getChildCount();
        int size = (int) Math.sqrt(count);
        buttons = new Button[size][size];

        for (int i = 0; i < count; i++) {
            View view = group.getChildAt(i);
            Button button = (Button) view;
            button.setOnClickListener(this::onButtonCLick);
            int y = i / size;
            int x = i % size;
            button.setTag(new Coordinate(x, y));
            buttons[y][x] = button;
        }
    }

    private void onButtonCLick(View view) {


        Button button = (Button) view;
        Coordinate c = (Coordinate) button.getTag();
        if (mySharedPreference.getSound() && !buttons[c.getY()][c.getX()].getText().equals("")) {
            mediaPlayer.start();
        } else {
            mediaPlayer.pause();
        }
        int eX = emptyCoordinate.getX();
        int eY = emptyCoordinate.getY();
        int dX = abs(c.getX() - eX);
        int dY = abs(c.getY() - eY);
        if (dX + dY == 1) {
            score++;
            textScore.setText(String.valueOf(score));
            buttons[eY][eX].setText(button.getText());
            button.setText("");
            emptyCoordinate = c;
            if (isWin()) {
                Toast.makeText(this, "You win", Toast.LENGTH_SHORT).show();
                onRestartGame();
                textTime.stop();
            }
        }
    }

    private boolean isWin() {
        if (!(emptyCoordinate.getX() == 5 && emptyCoordinate.getY() == 5)) return false;
        for (int i = 0; i < 35; i++) {
            String s = buttons[i / 6][i % 6].getText().toString();
            if (!s.equals(String.valueOf(i + 1))) return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            winDialog();
        }
        return true;
    }


    private void onRestartGame() {
        loadNumberToButton();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void winDialog() {


        textTime.stop();
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setCancelable(false);

        LayoutInflater inflater = getLayoutInflater();
        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.container);

        View dialogView = inflater.inflate(R.layout.win_dialog, mainLayout, false);
        TextView scoreT = dialogView.findViewById(R.id.score);
        TextView time = dialogView.findViewById(R.id.time);
        LinearLayout restart = dialogView.findViewById(R.id.restart);
        LinearLayout menu = dialogView.findViewById(R.id.menu);
        LinearLayout level = dialogView.findViewById(R.id.level);
        restart.setOnClickListener(view -> {
            loadNumberToButton();
            dialog.cancel();
        });
        level.setOnClickListener(view -> {

            startActivity(new Intent(this, LevelActivity.class));
        });

        menu.setOnClickListener(view -> {
            startActivity(new Intent(ThirdLevelActivity.this, MenuActivity.class));
            dialog.cancel();
        });

        scoreT.setText("Score : " + textScore.getText());
        time.setText("Time : " + textTime.getText());
        onPause();
        winners.add(new Winner(textTime.getBase(), score, textTime.getText().toString()));
        ArrayList<Long> winnerBest = new ArrayList<>();
        for (int i = 0; i < winners.size(); i++) {
            winnerBest.add(winners.get(i).getTime());
        }
        Collections.sort(winnerBest);

        Winner win = new Winner();
        for (int i = 0; i < winners.size(); i++) {
            if (winners.get(i).getTime() == winnerBest.get(winnerBest.size() - 1)) {
                win = winners.get(i);
            }
        }

        TextView text = (TextView) findViewById(R.id.time_text_best);
        text.setText(String.valueOf(win.getText()));

        TextView textBestScore = (TextView) findViewById(R.id.score_text_best);
        textBestScore.setText(String.valueOf(win.getScore()));

        mySharedPreference.setBestScoreHard(String.valueOf(win.getScore()));
        mySharedPreference.setBestTimeHard(win.getText());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setView(dialogView);
        dialog.show();

    }

    private void infoDialog() {

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.container);
        View dialogView = inflater.inflate(R.layout.info_dialog, mainLayout, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        pauseTime = SystemClock.elapsedRealtime() - textTime.getBase();
        textTime.stop();
        LinearLayout ok = dialogView.findViewById(R.id.ok);

        ok.setOnClickListener(view -> {
            if (isStarted) {
                onResume();
                dialog.cancel();
            }
        });
        dialog.setView(dialogView);
        dialog.show();

    }

    private void menuDialog() {

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.container);
        View dialogView = inflater.inflate(R.layout.menu_dialog, mainLayout, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        pauseTime = SystemClock.elapsedRealtime() - textTime.getBase();
        textTime.stop();
        AppCompatButton continueGame = dialogView.findViewById(R.id.continueGame);
        AppCompatButton level = dialogView.findViewById(R.id.level);
        AppCompatButton newGame = dialogView.findViewById(R.id.start);

        continueGame.setOnClickListener(view -> {
            if (isStarted) {
                onResume();
                dialog.cancel();
            }
        });

        level.setOnClickListener(view -> {

            startActivity(new Intent(this, LevelActivity.class));
            dialog.cancel();
        });
        newGame.setOnClickListener(view -> {
            loadNumberToButton();
            dialog.cancel();
        });
        dialog.setView(dialogView);
        dialog.show();

    }


    @Override
    public void onBackPressed() {
        exitDialog();
    }

    private void exitDialog() {

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.container);
        View dialogView = inflater.inflate(R.layout.exit_dialog, mainLayout, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        LinearLayout no = dialogView.findViewById(R.id.no);
        LinearLayout yes = dialogView.findViewById(R.id.yes);

        no.setOnClickListener(view -> dialog.cancel());
        yes.setOnClickListener(view -> {
            finish();
        });
        dialog.setView(dialogView);
        dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isStarted) {
            textTime.setBase(SystemClock.elapsedRealtime() - pauseTime);
            textTime.start();
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("RRR", "RESTORED");
        score = savedInstanceState.getInt("score", 0);
        pauseTime = savedInstanceState.getLong("pause_time", 0);
        isStarted = savedInstanceState.getBoolean("is_started");
        List<String> numbersList = savedInstanceState.getStringArrayList("numbers");

        textScore.setText(String.valueOf(score));

        loadSavedNumbers(numbersList);

    }

    private void loadSavedNumbers(List<String> numbers) {
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i).equals("")) {
                emptyCoordinate = new Coordinate(i % 6, i / 6);
            }
            buttons[i / 6][i % 6].setText(numbers.get(i));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d("RRR", "SAVED");
        outState.putInt("score", score);
        outState.putLong("pause_time", pauseTime);
        outState.putBoolean("is_started", isStarted);
        ArrayList<String> numbers = new ArrayList<>();
        for (int i = 0; i < group.getChildCount(); i++) {
            numbers.add(((Button) group.getChildAt(i)).getText().toString());
        }
        outState.putStringArrayList("numbers", numbers);
        super.onSaveInstanceState(outState);
    }
}