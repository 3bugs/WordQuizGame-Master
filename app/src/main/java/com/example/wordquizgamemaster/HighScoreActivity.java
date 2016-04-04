package com.example.wordquizgamemaster;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.wordquizgamemaster.db.DatabaseHelper;

import java.util.Arrays;

public class HighScoreActivity extends AppCompatActivity {

    private static final String TAG = HighScoreActivity.class.getSimpleName();

    private ListView mList;
    private DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        mHelper = new DatabaseHelper(this);
        mDatabase = mHelper.getWritableDatabase();

        mList = (ListView) findViewById(R.id.high_score_list_view);
        setListAdapter();

        // กำหนดข้อความปุ่มเรดิโอ
        RadioButton easyRadioButton = (RadioButton) findViewById(R.id.easy_radio_button);
        easyRadioButton.setText(R.string.easy_label);
        RadioButton mediumRadioButton = (RadioButton) findViewById(R.id.medium_radio_button);
        mediumRadioButton.setText(R.string.medium_label);
        RadioButton hardRadioButton = (RadioButton) findViewById(R.id.hard_radio_button);
        hardRadioButton.setText(R.string.hard_label);

        // กำหนด Checked Change Listener ให้กับ RadioGroup
        RadioGroup difficultyRadioGroup = (RadioGroup) findViewById(R.id.difficulty_radio_group);
        difficultyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String[] diffLabels = getResources().getStringArray(R.array.difficulty_labels);
                String selectedRadioLabel = ((RadioButton) findViewById(checkedId)).getText().toString();
                int diffIndex = Arrays.asList(diffLabels).indexOf(selectedRadioLabel);
                showHighScoreByDifficulty(diffIndex);
            }
        });

        // เลือกปุ่มเรดิโอ "ง่าย" เป็นค่าเริ่มต้น
        easyRadioButton.setChecked(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.close();
        mHelper.close();
    }

    void setListAdapter() {
        final String[] columns = {
                DatabaseHelper.COL_SCORE
        };
        final int[] views = {
                R.id.score_text_view
        };

        mAdapter = new SimpleCursorAdapter(this, R.layout.high_score_row, null, columns, views, 0);
        mList.setAdapter(mAdapter);
    }

    void showHighScoreByDifficulty(int difficulty) {
/*
        final String sqlSelect = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE " + DatabaseHelper.COL_DIFFICULTY + "=" + difficulty +
                " ORDER BY " + DatabaseHelper.COL_SCORE + " DESC LIMIT 5";
        Cursor cursor = mDatabase.rawQuery(sqlSelect, null);
*/

        Cursor cursor = mDatabase.query(
                DatabaseHelper.TABLE_NAME,
                null,
                DatabaseHelper.COL_DIFFICULTY + "=?",
                new String[]{String.valueOf(difficulty)},
                null,
                null,
                DatabaseHelper.COL_SCORE + " DESC",
                "5"
        );
        mAdapter.changeCursor(cursor);
    }
}
