package com.example.puzzle.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {

    private static MySharedPreference mysSharedPreference;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static MySharedPreference getInstance(Context context) {
        if (mysSharedPreference == null) {
            mysSharedPreference = new MySharedPreference(context);
        }
        return mysSharedPreference;
    }

    private MySharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("app_name", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setIsPlaying(boolean isPlaying) {
        editor.putBoolean("is_playing", isPlaying);
        editor.apply();
    }

    public Boolean getIsPlaying() {
        return sharedPreferences.getBoolean("is_playing", false);
    }

    public void setIsPlayingHard(boolean isPlaying) {
        editor.putBoolean("is_playing_hard", isPlaying);
        editor.apply();
    }

    public Boolean getIsPlayingHard() {
        return sharedPreferences.getBoolean("is_playing_hard", false);
    }

    public void setIsPlayingMedium(boolean isPlaying) {
        editor.putBoolean("is_playing_medium", isPlaying);
        editor.apply();
    }

    public Boolean getIsPlayingMedium() {
        return sharedPreferences.getBoolean("is_playing_medium", false);
    }

    public void setLevel(String level) {
        editor.putString("level", level);
        editor.apply();
    }

    public String getLevel() {
        return sharedPreferences.getString("level", "easy");
    }

    public void setScore(int score) {
        editor.putInt("score", score);
        editor.apply();
    }

    public int getScore() {
        return sharedPreferences.getInt("score", 0);
    }

    public void setScoreHard(int score) {
        editor.putInt("score_hard", score);
        editor.apply();
    }

    public int getScoreHard() {
        return sharedPreferences.getInt("score_hard", 0);
    }

    public void setScoreMedium(int score) {
        editor.putInt("score_medium", score);
        editor.apply();
    }

    public int getScoreMedium() {
        return sharedPreferences.getInt("score_medium", 0);
    }


    public void setPauseTime(long pause_time) {
        editor.putLong("pause_time", pause_time);
        editor.apply();
    }


    public Long getPauseTime() {
        return sharedPreferences.getLong("pause_time", 0);
    }

    public void setPauseTimeHard(long pause_time) {
        editor.putLong("pause_time_hard", pause_time);
        editor.apply();
    }


    public Long getPauseTimeHard() {
        return sharedPreferences.getLong("pause_time_hard", 0);
    }

    public void setPauseTimeMedium(long pause_time) {
        editor.putLong("pause_time_medium", pause_time);
        editor.apply();
    }

    public Long getPauseTimeMedium() {
        return sharedPreferences.getLong("pause_time_medium", 0);
    }


    public void setNumbers(String numbers) {
        editor.putString("numbers", numbers);
        editor.apply();
    }

    public String getNumbers() {
        return sharedPreferences.getString("numbers", "1#2#3#4#5#6#7#8#9#10#11#12#13#14#15##");
    }


    public void setNumbersHard(String numbers) {
        editor.putString("numbers_hard", numbers);
        editor.apply();
    }

    public String getNumbersHard() {
        return sharedPreferences.getString("numbers_hard", "1#2#3#4#5#6#7#8#9#10#11#12#13#14#15#16#17#18#19#20#21#22#23#24#25#26#27#28#29#30#31#32#33#34#35##");
    }

    public void setNumbersMedium(String numbers) {
        editor.putString("numbers_medium", numbers);
        editor.apply();
    }

    public String getNumbersMedium() {
        return sharedPreferences.getString("numbers_medium", "1#2#3#4#5#6#7#8#9#10#11#12#13#14#15#16#17#18#19#20#21#22#23#24##");
    }

    public void setBestScore(String score) {
        editor.putString("best_score", score);
        editor.apply();
    }

    public String getBestScoreMedium() {
        return sharedPreferences.getString("best_score_medium", "");
    }

    public void setBestScoreMedium(String score) {
        editor.putString("best_score_medium", score);
        editor.apply();
    }

    public String getBestScoreHard() {
        return sharedPreferences.getString("best_score_hard", "");
    }

    public void setBestScoreHard(String score) {
        editor.putString("best_score_hard", score);
        editor.apply();
    }

    public String getBestScore() {
        return sharedPreferences.getString("best_score", "");
    }

    public void setBestTime(String time) {
        editor.putString("best_time", time);
        editor.apply();
    }

    public String getBestTime() {
        return sharedPreferences.getString("best_time", "");
    }


    public void setBestTimeMedium(String time) {
        editor.putString("best_time_medium", time);
        editor.apply();
    }

    public String getBestTimeMedium() {
        return sharedPreferences.getString("best_time_medium", "");
    }

    public void setBestTimeHard(String time) {
        editor.putString("best_time_medium", time);
        editor.apply();
    }

    public String getBestTimeHard() {
        return sharedPreferences.getString("best_time_medium", "");
    }


    public void setSound(boolean sound) {
        editor.putBoolean("sound", sound);
        editor.apply();
    }

    public Boolean getSound() {
        return sharedPreferences.getBoolean("sound", true);
    }


}
