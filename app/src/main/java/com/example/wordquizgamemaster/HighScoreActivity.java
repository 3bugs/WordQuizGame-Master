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

import java.util.HashMap;

public class HighScoreActivity extends AppCompatActivity {
    private static final String EASY = "ง่าย";
    private static final String MEDIUM = "ปานกลาง";
    private static final String HARD = "ยาก";

    private ListView mList;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDatabase;
    private SimpleCursorAdapter mAdapter;
    private HashMap<String, Integer> mDifficultyMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        mDbHelper = new DatabaseHelper(this);
        mDatabase = mDbHelper.getWritableDatabase();

        // ข้อมูลแต่ละชุดใน HashMap จะมีคีย์เป็นข้อความที่แสดงกำกับปุ่มเรดิโอ (ง่าย, ปานกลาง, ยาก)
        // และมีค่าเป็น constant ที่กำหนดในคลาส DatabaseHelper
        // หน้าที่ของ HashMap นี้ ก็เพื่อให้เราเอาข้อความของปุ่มเรดิโอ ไปดึงค่า constant ที่สัมพันธ์กันมาใช้งาน
        mDifficultyMap = new HashMap<String, Integer>();
        mDifficultyMap.put(EASY, DatabaseHelper.DIFFICULTY_EASY);
        mDifficultyMap.put(MEDIUM, DatabaseHelper.DIFFICULTY_MEDIUM);
        mDifficultyMap.put(HARD, DatabaseHelper.DIFFICULTY_HARD);

        mList = (ListView) findViewById(R.id.high_score_list_view);
        setListAdapter();

        // กำหนดข้อความปุ่มเรดิโอ
        RadioButton easyRadioButton = (RadioButton) findViewById(R.id.easy_radio_button);
        easyRadioButton.setText(EASY);

        RadioButton mediumRadioButton = (RadioButton) findViewById(R.id.medium_radio_button);
        mediumRadioButton.setText(MEDIUM);

        RadioButton hardRadioButton = (RadioButton) findViewById(R.id.hard_radio_button);
        hardRadioButton.setText(HARD);

        // กำหนด Checked Change Listener ให้กับ RadioGroup
        RadioGroup difficultyRadioGroup = (RadioGroup) findViewById(R.id.difficulty_radio_group);
        difficultyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radio = (RadioButton) findViewById(checkedId);
                showHighScoreByDifficulty(radio.getText().toString());
            }
        });

        // เลือกปุ่มเรดิโอ "ง่าย" เป็นค่าเริ่มต้น
        easyRadioButton.setChecked(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.close();
        mDbHelper.close();
    }

    void setListAdapter() {
        String[] columns = {
                DatabaseHelper.COL_SCORE
        };
        int[] views = {
                R.id.score_text_view
        };

        mAdapter = new SimpleCursorAdapter(this, R.layout.high_score_row, null, columns, views, 0);
        mList.setAdapter(mAdapter);
    }

    void showHighScoreByDifficulty(String difficulty) {
        String sqlSelect = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE " + DatabaseHelper.COL_DIFFICULTY + "=" +
                mDifficultyMap.get(difficulty) +
                " ORDER BY " + DatabaseHelper.COL_SCORE + " DESC LIMIT 5";

        Cursor cursor = mDatabase.rawQuery(sqlSelect, null);
        mAdapter.changeCursor(cursor);
    }
}
