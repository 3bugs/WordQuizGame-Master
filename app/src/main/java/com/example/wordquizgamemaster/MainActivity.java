package com.example.wordquizgamemaster;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playGameButton = (Button) findViewById(R.id.play_game_button);
        playGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MainActivity", "Hello log");

                Toast t = Toast.makeText(
                        MainActivity.this,
                        "Hello Toast",
                        Toast.LENGTH_SHORT
                );
                t.show();

                //showPlainChooseDifficultyDialog();
                showCustomChooseDifficultyDialog();
            }
        });

        Button highScoreButton = (Button) findViewById(R.id.high_score_button);
        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HighScoreActivity.class);
                startActivity(i);
            }
        });
    }

    private void showPlainChooseDifficultyDialog() {
        final String[] diffLabels = getResources().getStringArray(R.array.difficulty_labels);
        //final String[] diffLabels = new String[]{"ง่าย", "ปานกลาง", "ยาก"};

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("เลือกระดับความยาก");
        dialog.setIcon(R.drawable.abc);
        dialog.setItems(diffLabels, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "คุณเลือก: " + diffLabels[which]);

                Intent i = new Intent(MainActivity.this, GameActivity.class);
                i.putExtra(GameActivity.KEY_DIFFICULTY, which);
                startActivity(i);
            }
        });
        dialog.show();
    }

    private void showCustomChooseDifficultyDialog() {
        final String[] diffLabels = getResources().getStringArray(R.array.difficulty_labels);
        //final String[] diffLabels = new String[]{"ง่าย", "ปานกลาง", "ยาก"};
        DifficultyOptionsAdapter adapter = new DifficultyOptionsAdapter(
                this,
                R.layout.difficulty_row,
                diffLabels
        );

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("เลือกระดับความยาก");
        dialog.setIcon(R.drawable.abc);
        dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra(GameActivity.KEY_DIFFICULTY, which);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Music.play(this, R.raw.main);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Music.stop();
    }

    private static class DifficultyOptionsAdapter extends ArrayAdapter<String> {

        private Context mContext;
        private int mItemLayoutId;
        private String[] mDifficulties;

        public DifficultyOptionsAdapter(Context context, int itemLayoutId,
                                        String[] difficulties) {
            super(context, itemLayoutId, difficulties);

            this.mContext = context;
            this.mItemLayoutId = itemLayoutId;
            this.mDifficulties = difficulties;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inflater.inflate(mItemLayoutId, parent, false);

            TextView difficultyTextView = (TextView) row.findViewById(R.id.difficulty_text_view);
            ImageView difficultyImageView = (ImageView) row.findViewById(R.id.difficulty_image_view);

            String diff = mDifficulties[position];
            difficultyTextView.setText(diff);

            if (diff.equals("ง่าย")) {
                difficultyImageView.setImageResource(R.drawable.dog_easy);
            } else if (diff.equals("ปานกลาง")) {
                difficultyImageView.setImageResource(R.drawable.dog_medium);
            } else if (diff.equals("ยาก")) {
                difficultyImageView.setImageResource(R.drawable.dog_hard);
            }

            return row;
        }
    }
}
